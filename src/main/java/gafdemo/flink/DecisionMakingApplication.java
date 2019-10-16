package gafdemo.flink;

import com.googlecode.aviator.AviatorEvaluator;
import gafdemo.aviator.RuleGFunction;
import gafdemo.flink.source.DemoEventSource;
import gafdemo.groovy.DslEvaluator;
import gafdemo.groovy.pogo.event.CepEventGroovy;
import gafdemo.groovy.pogo.event.CepEventResult;
import gafdemo.groovy.pogo.event.CepPatternGroovy;
import gafdemo.groovy.pogo.event.DataSourceEvent;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.IngestionTimeExtractor;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.HashMap;
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
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "            within('SECONDS', 10)\n" +
            "        }\n" +
            "        pattern {\n" +
            "            weight = 2\n" +
            "            mainName = \"pattern2\"\n" +
            "            name = \"pattern2_1\"\n" +
            "            subtype = \"childEvent1\"\n" +
            "            type = \"begin\"\n" +
            "            condition {\n" +
            "                and(\"event.trade > 5000\")\n" +
            "            }\n" +
            "                next {\n" +
            "                    name = \"pattern2_2\"\n" +
            "                    type = \"followedBy\"\n" +
            "                    condition {\n" +
            "                        and(\"event.trade > 6000\")\n" +
            "                    }\n" +
            "                }\n" +
            "        }\n" +
            "        pattern {\n" +
            "            weight = 3\n" +
            "            mainName = \"pattern3\"\n" +
            "            name = \"pattern3_1\"\n" +
            "            subtype = \"childEvent1\"\n" +
            "            type = \"begin\"\n" +
            "            condition {\n" +
            "                and(\"event.trade > 3000 && event.trade < 5000\")\n" +
            "            }\n" +
            "            \n" +
            "        }\n" +
            "    }\n" +
            "    patternGroup {\n" +
            "        weight = 2\n" +
            "        pattern {\n" +
            "            weight = 5\n" +
            "            mainName = \"patternGroup2\"\n" +
            "            name = \"patternGroup2_1\"\n" +
            "            subtype = \"childEvent1\"\n" +
            "            type = \"begin\"\n" +
            "            condition {\n" +
            "                and(\"event.cardNo < 10\")\n" +
            "            }\n" +
            "            \n" +
            "        }\n" +
            "    }\n" +
            "}";

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
        env.setParallelism(10);

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
        DataStream<CepEventResult> dataStream = null;
        for (Map.Entry<String, CepPatternGroovy> patternMap : cepEventGroovy.getPatternMap().entrySet()) {
            CepPatternGroovy cepPattern = patternMap.getValue();
            //创建模式流
            PatternStream<DataSourceEvent> patternStream = CEP.pattern(
                    inputEventStream.keyBy(dataSourceEvent -> dataSourceEvent.getData().get(cepEventGroovy.getKeyBy())),
                    cepPattern.getPattern());
            //创建模式匹配后的处理函数
            MyPatternSelectFunction selectFunction = new MyPatternSelectFunction(cepPattern);
            //把模式流合并起来统一处理（权重计算、组装唯一输出）
            if(dataStream == null) {
                dataStream = patternStream.flatSelect(selectFunction);
            } else {
                dataStream = dataStream.union(patternStream.flatSelect(selectFunction));
            }
        }
        assert dataStream != null;
        //统一处理一个事件下所有命中的模式流
        dataStream
                .keyBy("seqNo")
                .timeWindow(Time.milliseconds(3))
                .process(new ProcessWindowFunction<CepEventResult, CepEventResult, Tuple, TimeWindow>() {
                    @Override
                    public void process(Tuple tuple, Context context, Iterable<CepEventResult> iterable, Collector<CepEventResult> collector) {
                        //TODO 权重计算、合并结果、对外输出等
                        iterable.forEach(collector::collect);
                    }
                })
                .print();

        env.execute("DecisionMaking job");
    }
}
