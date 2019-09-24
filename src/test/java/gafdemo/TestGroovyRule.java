package gafdemo;

import com.googlecode.aviator.AviatorEvaluator;
import gafdemo.aviator.RuleGFunction;
import gafdemo.groovy.DslEvaluator;
import gafdemo.groovy.pogo.rule.RuleCard;
import gafdemo.groovy.pogo.rule.RuleFlow;
import gafdemo.groovy.pogo.rule.RuleSet;
import gafdemo.groovy.pogo.rule.RuleTable;
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
}
