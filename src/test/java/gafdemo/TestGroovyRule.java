package gafdemo;

import com.googlecode.aviator.AviatorEvaluator;
import gafdemo.aviator.RuleGFunction;
import gafdemo.pojo.Order;
import gafdemo.pojo.OrderSource;
import gafdemo.groovy.DslEvaluator;
import gafdemo.groovy.pogo.event.DataSourceEvent;
import gafdemo.groovy.pogo.rule.RuleCard;
import gafdemo.groovy.pogo.rule.RuleFlow;
import gafdemo.groovy.pogo.rule.RuleSet;
import gafdemo.groovy.pogo.rule.RuleTable;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by luomingxing on 2019/9/11.
 */
public class TestGroovyRule {
    private DslEvaluator dslEvaluator = new DslEvaluator();

    @Test
    public void testGroovyRule(){
        AviatorEvaluator.addFunction(new RuleGFunction());

        Map<String, Object> env = new HashMap<String, Object>();
        Map<String, Object> env_event = new HashMap<String, Object>();
        env_event.put("cType", "准新客户");
        env_event.put("incomeOfYear", 1000000);
        env_event.put("age", 19);
        env_event.put("male", "男");
        env_event.put("car", "有车");
        env_event.put("house", "有房");
        env_event.put("channel", "网申");
        env_event.put("overdue", 3);
        env_event.put("education", "大专");
        env_event.put("company", "双照");
        env_event.put("marriage", "未婚");
        env.put("event", env_event);

        env.put("result_approval", "最终审批");
        env.put("result_access", "准入");
        env.put("result_scoreCard1", 0);
        env.put("result_personalInfo", "个人信息");

        //构建规则流作为参数传入
        Map<String, Object> env_rule = new HashMap<String, Object>();
        env.put("rule", env_rule);
        //构建规则集
        buildTestRuleSet(env_rule);
        //构建决策表
        buildTestRuleTable(env_rule);
        //构建评分卡
        buildTestRuleCard(env_rule);
        //构建决策流
        buildTestRuleFlow(env_rule);

        AviatorEvaluator.execute("ruleG(rule.ruleFlow1)", env);
        AviatorEvaluator.execute("ruleG(rule.ruleSet1)", env);
        AviatorEvaluator.execute("ruleG(rule.ruleTable1)", env);
        AviatorEvaluator.execute("ruleG(rule.ruleCard1)", env);

        System.out.println("--------print result:");
        System.out.println("key=result_approval"+";value="+env.get("result_approval"));
        System.out.println("key=result_access"+";value="+env.get("result_access"));
        System.out.println("key=result_personalInfo"+";value="+env.get("result_personalInfo"));
        System.out.println("key=result_scoreCard1"+";value="+env.get("result_scoreCard1"));
    }

    private void buildTestRuleFlow(Map<String, Object> env_rule) {
        RuleFlow ruleFlow = (RuleFlow) dslEvaluator.executeDslForRuleFlow("ruleFlow {\n" +
                "    name = \"决策流1\"\n" +
                "    code = \"ruleFlow1\"\n" +
                "    id = \"dddd11\"\n" +
                "    rootNode {\n" +
                "        expression(\"event.company == '双照'\")\n" +
                "        leftChild {\n" +
                "            expression(\"event.marriage == '已婚'\")\n" +
                "            leftChild {\n" +
                "                expression(\"ruleG(rule.ruleSet1)\")\n" +
                "                leftChild {\n" +
                "                       expression(\"result_approval = '执行规则集1后通过'\")\n" +
                "                 }\n" +
                "            }\n" +
                "            rightChild {\n" +
                "                expression(\"ruleG(rule.ruleTable1)\")\n" +
                "                leftChild {\n" +
                "                    expression(\"result_approval = '执行决策表1后通过' \")\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "        rightChild {\n" +
                "            expression(\"ruleG(rule.ruleCard1) \")\n" +
                "            leftChild {\n" +
                "                    expression(\"result_approval = '执行评分卡1后通过' \")\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");
        System.out.println("build ruleFlow:" + ruleFlow.toString());
        env_rule.put("ruleFlow1", ruleFlow);

    }

    private void buildTestRuleCard(Map<String, Object> env_rule) {
        RuleCard ruleCard = (RuleCard) dslEvaluator.executeDslForRuleCard("ruleCard {\n" +
                "    name = \"评分卡1\"\n" +
                "    code = \"ruleCard1\"\n" +
                "    id = \"ccc11\"\n" +
                "    type = 2\n" +
                "    propertyScore {\n" +
                "        propertyCode = \"event.incomeOfYear\"\n" +
                "        propertyWeight = 1\n" +
                "        propertyItem(1,\"event.incomeOfYear < 100000\")\n" +
                "        propertyItem(2,\"event.incomeOfYear > 100000\")\n" +
                "    }\n" +
                "    propertyScore {\n" +
                "        propertyCode = \"event.education\"\n" +
                "        propertyWeight = 5\n" +
                "        propertyItem(3,\"event.education == '高中'\")\n" +
                "        propertyItem(4,\"event.education == '大专'\")\n" +
                "        propertyItem(5,\"event.education == '本科'\")\n" +
                "    }\n" +
                "}\n");
        System.out.println("build ruleCard:" + ruleCard.toString());
        env_rule.put("ruleCard1", ruleCard);
    }

    private void buildTestRuleTable(Map<String, Object> env_rule) {
        RuleTable ruleTable = (RuleTable) dslEvaluator.executeDslForRuleTable("ruleTable {\n" +
                "    name = \"决策表1\"\n" +
                "    code = \"ruleTable1\"\n" +
                "    id = \"ffff11\"\n" +
                "    type = 1\n" +
                "    row {\n" +
                "        rowItem(0,\"event.male == '男' && event.age >= 18\")\n" +
                "        rowItem(1,\"event.male == '男' && event.age < 18\")\n" +
                "    }\n" +
                "    column {\n" +
                "        columnItem(0,\"event.car == '有车'\")\n" +
                "        columnItem(1,\"event.house == '有房'\")\n" +
                "    }\n" +
                "    assign {\n" +
                "        assignItem(\"result_personalInfo = '成年男性有车'\",\"result_personalInfo = '成年男性有房'\")\n" +
                "        assignItem(\"result_personalInfo = '未成年男性有车'\",\"result_personalInfo = '未成年男性有房'\")\n" +
                "    }\n" +
                "}");
        System.out.println("build ruleTable:" + ruleTable.toString());
        env_rule.put("ruleTable1", ruleTable);
    }

    private void buildTestRuleSet(Map<String, Object> env_rule) {
        RuleSet ruleSet = (RuleSet) dslEvaluator.executeDslForRuleSet("ruleSet {\n" +
                "    name = \"规则集1\"\n" +
                "    code = \"ruleSet1\"\n" +
                "    id = \"abc11\"\n" +
                "    type = 1\n" +
                "    rule {\n" +
                "        name = \"规则1\"\n" +
                "        code = \"rule1\"\n" +
                "        id = \"abc1\"\n" +
                "        filter(\"event.incomeOfYear <= 20000\")\n" +
                "        assign(\"result_access = '年收入低'\")\n" +
                "    }\n" +
                "    rule {\n" +
                "        name = \"规则2\"\n" +
                "        code = \"rule2\"\n" +
                "        id = \"abc2\"\n" +
                "        filter(\"event.channel == '网申'\")\n" +
                "        assign(\"result_access = '非法渠道'\")\n" +
                "    }\n" +
                "    rule {\n" +
                "        name = \"规则3\"\n" +
                "        code = \"rule3\"\n" +
                "        id = \"abc3\"\n" +
                "        filter(\"event.age < 20 || event.age > 50\")\n" +
                "        assign(\"result_access = '年龄不符'\")\n" +
                "    }\n" +
                "}");
        System.out.println("build ruleSet:" + ruleSet.toString());
        env_rule.put("ruleSet1", ruleSet);
    }

    public static void main(String[] args) throws Exception {

//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//
//        // this can be used in a streaming program like this (assuming we have a StreamExecutionEnvironment env)
//        env.fromElements(Tuple2.of(2L, 3L), Tuple2.of(2L, 5L), Tuple2.of(1L, 7L), Tuple2.of(2L, 4L), Tuple2.of(1L, 2L))
//                .keyBy(0) // 以数组的第一个元素作为key
//                .reduce((ReduceFunction<Tuple2<Long, Long>>) (t2, t1) -> new Tuple2<>(t1.f0 + t2.f0, t2.f1 + t1.f1)) // value做累加
//                .print();
//
//        env.execute("execute");

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        DataStream<Order> userInfoDataStream = env.addSource(new OrderSource(1));
        BroadcastStream<Order> userInfoDataStream2 = env.addSource(new OrderSource(2)).broadcast(null);
        userInfoDataStream.connect(userInfoDataStream2).process(new BroadcastProcessFunction<Order, Order, Object>() {
            @Override
            public void processElement(Order order, ReadOnlyContext readOnlyContext, Collector<Object> collector) throws Exception {



            }

            @Override
            public void processBroadcastElement(Order order, Context context, Collector<Object> collector) throws Exception {

            }
        });
        userInfoDataStream.keyBy("").window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .process(new ProcessWindowFunction<Order, DataSourceEvent, Tuple, TimeWindow>() {
            @Override
            public void process(Tuple tuple, Context context, Iterable<Order> iterable, Collector<DataSourceEvent> collector) throws Exception {

            }

            @Override
            public void clear(Context context) throws Exception {

                super.clear(context);
            }
        }).addSink(new SinkFunction<DataSourceEvent>() {
            @Override
            public void invoke(DataSourceEvent value, Context context) throws Exception {

            }
        });

        DataStream<Order> timedData = userInfoDataStream.assignTimestampsAndWatermarks(new AscendingTimestampExtractor<Order>() {
            @Override
            public long extractAscendingTimestamp(Order element) {
                return element.getMdTime().getTime();
            }
        });

        SingleOutputStreamOperator<Order> reduce = timedData
                .keyBy("id")
                .timeWindow(Time.seconds(3))
                .reduce(new ReduceFunction<Order>() {
                    @Override
                    public Order reduce(Order t1, Order t2) throws Exception {
                        if(t1.getVersion() < t2.getVersion()) {
                            return t2;
                        } else {
                            return t1;
                        }
                    }
                });

        reduce.print();
        env.execute("test");
    }

}
