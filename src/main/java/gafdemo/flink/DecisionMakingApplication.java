package gafdemo.flink;

import com.googlecode.aviator.AviatorEvaluator;
import gafdemo.aviator.RuleGFunction;
import gafdemo.flink.source.DemoEventSource;
import gafdemo.groovy.DslEvaluator;
import gafdemo.groovy.pogo.event.CepEventGroovy;
import gafdemo.groovy.pogo.event.CepEventResult;
import gafdemo.groovy.pogo.event.CepPatternGroovy;
import gafdemo.groovy.pogo.event.DataSourceEvent;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.functions.RichReduceFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.IngestionTimeExtractor;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.List;
import java.util.Map;

/**
 * Created by luomingxing on 2019/9/24.
 */
public class DecisionMakingApplication {
    private static String eventDsl = "event {\n" +
            "    id = \"1234\"\n" +
            "    name = \"childEvent1\"\n" +
            "    type = \"risk\"\n" +
            "    keyBy = \"cardNo\"\n" +
            "    patternGroup {\n" +
            "        weight = 1\n" +
            "        pattern {\n" +
            "           weight = 2\n" +
            "            mainName = \"pattern1\"\n" +
            "            name = \"pattern1_1\"\n" +
            "            type = \"begin\"\n" +
            "            condition {\n" +
            "                and(\"event.cardNo > 10 && event.cardNo < 30\")\n" +
            "                or(\"event.trade < 1000\")\n" +
            "            }\n" +
            "            next {\n" +
            "                name = \"pattern1_2\"\n" +
            "                type = \"next\"\n" +
            "                condition {\n" +
            "                    and(\"event.cardNo > 30\")\n" +
            "                }\n" +
            "                times('times', 2)\n" +
            "                next {\n" +
            "                    name = \"pattern1_3\"\n" +
            "                    type = \"followedBy\"\n" +
            "                    condition {\n" +
            "                        and(\"event.trade < 3000\")\n" +
            "                        and(\"result_code1 = event.cardNo;event.trade < 3000\")\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "            within('SECONDS', 10)\n" +
            "        }\n" +
//            "        pattern {\n" +
//            "            weight = 2\n" +
//            "            mainName = \"pattern2\"\n" +
//            "            name = \"pattern2_1\"\n" +
//            "            subtype = \"childEvent1\"\n" +
//            "            type = \"begin\"\n" +
//            "            condition {\n" +
//            "                and(\"event.trade > 5000\")\n" +
//            "            }\n" +
//            "                next {\n" +
//            "                    name = \"pattern2_2\"\n" +
//            "                    type = \"followedBy\"\n" +
//            "                    condition {\n" +
//            "                        and(\"event.trade > 6000\")\n" +
//            "                    }\n" +
//            "                }\n" +
//            "        }\n" +
//            "        pattern {\n" +
//            "            weight = 3\n" +
//            "            mainName = \"pattern3\"\n" +
//            "            name = \"pattern3_1\"\n" +
//            "            subtype = \"childEvent1\"\n" +
//            "            type = \"begin\"\n" +
//            "            condition {\n" +
//            "                and(\"event.trade > 3000 && event.trade < 5000\")\n" +
//            "            }\n" +
//            "            \n" +
//            "        }\n" +
            "    }\n" +
//            "    patternGroup {\n" +
//            "        weight = 2\n" +
//            "        pattern {\n" +
//            "            weight = 5\n" +
//            "            mainName = \"patternGroup2\"\n" +
//            "            name = \"patternGroup2_1\"\n" +
//            "            subtype = \"childEvent1\"\n" +
//            "            type = \"begin\"\n" +
//            "            condition {\n" +
//            "                and(\"event.cardNo < 10\")\n" +
//            "            }\n" +
//            "            \n" +
//            "        }\n" +
//            "    }\n" +
            "}";

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
        env.setParallelism(1);
        env.setBufferTimeout(1);

        //定义Aviator自定义规则函数
        AviatorEvaluator.addFunction(new RuleGFunction());

        //初始化数据源
        DataStream<DataSourceEvent> inputEventStream = env
                .addSource(new DemoEventSource())
                .assignTimestampsAndWatermarks(new IngestionTimeExtractor<>());

        //解析事件模式配置
        DslEvaluator dslEvaluator = new DslEvaluator();
        CepEventGroovy cepEventGroovy = (CepEventGroovy) dslEvaluator.executeDslForEvent(eventDsl);

        //构建一个事件的所有模式流
        DataStream<CepEventResult> dataStream = getDefaultStream(inputEventStream, cepEventGroovy.getKeyBy());
        for (Map.Entry<String, CepPatternGroovy> patternMap : cepEventGroovy.getPatternMap().entrySet()) {
            CepPatternGroovy cepPattern = patternMap.getValue();
            //创建模式流
            PatternStream<DataSourceEvent> patternStream = CEP.pattern(
                    inputEventStream.keyBy(dataSourceEvent -> dataSourceEvent.getData().get(cepEventGroovy.getKeyBy())),
                    cepPattern.getPattern());
            //创建模式匹配后的处理函数
            MyPatternSelectFunction selectFunction = new MyPatternSelectFunction(cepPattern);
            //把模式流合并起来统一处理（权重计算、组装唯一输出）
            DataStream<CepEventResult> dataStream1 = patternStream.flatSelect(selectFunction);
            dataStream1.print();
            dataStream = dataStream.union(dataStream1);
        }
        assert dataStream != null;
        //统一处理一个事件下所有命中的模式流
        dataStream
                .keyBy("seqNo")
                .timeWindow(Time.milliseconds(10))
//                .reduce(new ReduceFunction<CepEventResult>() {
//                    @Override
//                    public CepEventResult reduce(CepEventResult cepEventResult, CepEventResult t1) throws Exception {
//                        System.out.println("dataStream process "+System.currentTimeMillis());
//                        return cepEventResult.getPatternGroovy().getWeight() > t1.getPatternGroovy().getWeight() ?
//                                cepEventResult :
//                                t1;
//                    }
//                })
                .process(new ProcessWindowFunction<CepEventResult, CepEventResult, Tuple, TimeWindow>() {
                    @Override
                    public void process(Tuple tuple, Context context, Iterable<CepEventResult> iterable, Collector<CepEventResult> collector) {
                        System.out.println("dataStream process "+System.currentTimeMillis() + " " + tuple.toString());
                        //TODO 只命中默认模式时直接输出当前事件数据
                        if(iterable.spliterator().getExactSizeIfKnown() == 1) {
                            CepEventResult cepEventResult = iterable.iterator().next();
                            collector.collect(cepEventResult);
                        } else {
                            //TODO 权重计算、合并结果、对外输出等
                            iterable.forEach(cepEventResult -> {
                                //排除默认模式
                                if(!"default".equals(cepEventResult.getHitPattern())) {
                                    long startTime = Long.valueOf(cepEventResult.getCurrentDataSourceEvent().getEventTime());
                                    System.out.println("decision total time = "+ (System.currentTimeMillis() - startTime) +";key = " + tuple.toString());
                                    collector.collect(cepEventResult);
                                }
                            });
                        }
                    }
                })
//                .map(new MapFunction<CepEventResult, Object>() {
//                    @Override
//                    public Object map(CepEventResult cepEventResult) throws Exception {
//                        System.out.println("dataStream process "+System.currentTimeMillis());
//                        return cepEventResult;
//                    }
//                })
                .print();

        env.execute("DecisionMaking job");
    }

    private static DataStream<CepEventResult> getDefaultStream(DataStream<DataSourceEvent> inputEventStream, String keyBy){
        Pattern<DataSourceEvent, DataSourceEvent> pattern = Pattern.<DataSourceEvent>begin("default").where(new SimpleCondition<DataSourceEvent>() {
            @Override
            public boolean filter(DataSourceEvent dataSourceEvent) throws Exception {
                //作为同步事件未匹配到任何模式时的默认返回（在风控中即为通过）
//                System.out.println("Data in:" +dataSourceEvent);
                return true;
            }
        });

        PatternStream<DataSourceEvent> patternStream = CEP.pattern(
                inputEventStream.keyBy(dataSourceEvent -> dataSourceEvent.getData().get(keyBy)),
                pattern);

        return patternStream.select((Map<String, List<DataSourceEvent>> map) -> {
            DataSourceEvent dataSourceEvent = map.get("default").get(0);
            CepEventResult cepEventResult = new CepEventResult();
            CepPatternGroovy cepPattern = new CepPatternGroovy();
            cepPattern.setWeight(0);
            cepEventResult.setHitPattern("default");
            cepEventResult.setPatternGroovy(cepPattern);
            cepEventResult.setCurrentDataSourceEvent(dataSourceEvent);
            cepEventResult.setSeqNo(dataSourceEvent.getSeqNo());
            return cepEventResult;
        });
    }
}
