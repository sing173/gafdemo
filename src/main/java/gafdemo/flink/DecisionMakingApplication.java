package gafdemo.flink;

import gafdemo.flink.source.MonitoringEventSource;
import gafdemo.groovy.DslEvaluator;
import gafdemo.groovy.pogo.event.CepEventGroovy;
import gafdemo.groovy.pogo.event.CepPatternGroovy;
import gafdemo.groovy.pogo.event.DataSourceEvent;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.IngestionTimeExtractor;

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
            "    patternGroup {\n" +
            "        weight = 1\n" +
            "        pattern {\n" +
            "            name = \"first\"\n" +
            "            type = \"begin\"\n" +
            "            condition {\n" +
            "                and(\"rule(rule1)\")\n" +
            "                or(\"rule(rule2)\")\n" +
            "            }\n" +
            "            next {\n" +
            "                name = \"second\"\n" +
            "                type = \"next\"\n" +
            "                condition {\n" +
            "                    and(\"rule(rule3)\")\n" +
            "                }\n" +
            "                times('oneOrMore', 4)\n" +
            "                next {\n" +
            "                    name = \"third\"\n" +
            "                    type = \"followedBy\"\n" +
            "                    condition {\n" +
            "                        and(\"rule(rule4)\")\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "            within('DAYS', 1)\n" +
            "        }\n" +
            "        pattern {\n" +
            "            weight = 2\n" +
            "            name = \"pattern2\"\n" +
            "            subtype = \"childEvent1\"\n" +
            "            type = \"begin\"\n" +
            "            condition {\n" +
            "                and(\"rule(ruleSet)\")\n" +
            "            }\n" +
            "            \n" +
            "        }\n" +
            "        pattern {\n" +
            "            weight = 3\n" +
            "            name = \"pattern3\"\n" +
            "            subtype = \"childEvent1\"\n" +
            "            type = \"begin\"\n" +
            "            condition {\n" +
            "                and(\"rule(rule5)\")\n" +
            "            }\n" +
            "            \n" +
            "        }\n" +
            "    }\n" +
            "    patternGroup {\n" +
            "        weight = 2\n" +
            "        pattern {\n" +
            "            weight = 5\n" +
            "            name = \"patternGroup2\"\n" +
            "            subtype = \"childEvent1\"\n" +
            "            type = \"begin\"\n" +
            "            condition {\n" +
            "                and(\"rule(rule8)\")\n" +
            "            }\n" +
            "            \n" +
            "        }\n" +
            "    }\n" +
            "}";

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);

        DataStream<DataSourceEvent> inputEventStream = env
                .addSource(new MonitoringEventSource())
                .assignTimestampsAndWatermarks(new IngestionTimeExtractor<>());

        DslEvaluator dslEvaluator = new DslEvaluator();
        CepEventGroovy cepEventGroovy = (CepEventGroovy) dslEvaluator.executeDslForEvent(eventDsl);

        for (Map.Entry<String, CepPatternGroovy> patternMap : cepEventGroovy.getPatternMap().entrySet()) {
            PatternStream<DataSourceEvent> patternStream = CEP.pattern(
                    inputEventStream.keyBy(cepEventGroovy.getKeyBy()),
                    patternMap.getValue().getPattern());

            DataStream<Object> timeDataStream = patternStream.select(new PatternSelectFunction<DataSourceEvent, Object>() {
                @Override
                public Object select(Map<String, List<DataSourceEvent>> map) throws Exception {
                    return map.get("f").get(0);


                }
            });
        }



        env.execute("DecisionMaking job");
    }
}
