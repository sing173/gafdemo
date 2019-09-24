package gafdemo;

import gafdemo.aviator.RuleFunction;
import com.googlecode.aviator.AviatorEvaluator;
import gafdemo.pojo.rule.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by luomingxing on 2019/8/22.
 */
public class TestRuleFunction {

    @Deprecated
    @Test
    public void testRuleTable(){
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("event.age", 17);
        env.put("event.male", '女');
        env.put("result", "nil");
        env = buildTestRuleTable(env);

        AviatorEvaluator.addFunction(new RuleFunction());
        AviatorEvaluator.execute("rule(rule.ruleTable1)", env);
        System.out.println(env.get("result"));

    }

    public class TestResult {
        private String result_personalInfo;

        public TestResult(String result_personalInfo){
            this.result_personalInfo = result_personalInfo;
        }

        public String getPersonalInfo() {
            return result_personalInfo;
        }

        public void setPersonalInfo(String result_personalInfo) {
            this.result_personalInfo = result_personalInfo;
        }
    }

    @Test
    public void testRuleFlow(){
        AviatorEvaluator.addFunction(new RuleFunction());

        Map<String, Object> env = new HashMap<String, Object>();
        Map<String, Object> env_event = new HashMap<String, Object>();
        env_event.put("cType", "准新客户");
        env_event.put("incomeOfYear", 1000000);
        env_event.put("age", 19);
        env_event.put("male", "女");
        env_event.put("car", "有车");
        env_event.put("house", "有房");
        env_event.put("channel", "网申");
        env_event.put("overdue", 3);
        env_event.put("education", "大专");
        env_event.put("company", "公司");
        env_event.put("marriage", "婚姻");
        env.put("event", env_event);

        env.put("result_approval", "最终审批");
        env.put("result_access", "准入");
        env.put("result_scoreCard1", 0);
        env.put("result_personalInfo", "个人信息");
//        Map<String, Object> env_result = new HashMap<String, Object>();
//        env_result.put("result_personalInfo", "个人信息");
//        env.put("result", env_result);
//        TestResult result = new TestResult("abc");
//        env.put("result", result);

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

//        if((Boolean) AviatorEvaluator.execute("result_personalInfo == 'abc'", env)){
//
//            AviatorEvaluator.execute("abc = 'lmx'", env);
//            if((Boolean) AviatorEvaluator.execute("abc == 'lmx'", env)){
//                System.out.println("lmx in the house");
//            }
//        }
        AviatorEvaluator.execute("rule(rule.ruleFlow1)", env);
        AviatorEvaluator.execute("rule(rule.ruleSet1)", env);
        AviatorEvaluator.execute("rule(rule.ruleTable1)", env);

//        for (Map.Entry<String, Object> entry : env.entrySet()){
//            System.out.println("key="+entry.getKey()+";value="+entry.getValue());
//        }
        System.out.println("key=result_approval"+";value="+env.get("result_approval"));
        System.out.println("key=result_access"+";value="+env.get("result_access"));
        System.out.println("key=result_personalInfo"+";value="+env.get("result_personalInfo"));
        System.out.println("key=result_scoreCard1"+";value="+env.get("result_scoreCard1"));
    }

    private Map<String, Object> buildTestRuleTable(Map<String, Object> env_rule){
        try {
            //规则配置
            Rule rule1 = new Rule("event.male == '男' && event.age >= 18 && event.car == '有车' && event.house == '有房'",
                    "result_personalInfo = '成年男性有车有房'", null);
            Rule rule2 = new Rule("event.male == '男' && event.age >= 18 && event.car != '有车' && event.house == '有房'",
                    "result_personalInfo = '成年男性有房'", null);
            Rule rule3 = new Rule("event.male == '男' && event.age >= 18 && event.car == '有车' && event.house != '有房'",
                    "result_personalInfo = '成年男性有车'", null);
            Rule rule4 = new Rule("event.male == '男' && event.age >= 18 && event.car != '有车' && event.house != '有房'",
                    "result_personalInfo = '成年男性'", null);
            Rule rule5 = new Rule("event.male == '男' && event.age < 18",
                    "result_personalInfo = '未成年男性'", null);
            Rule rule6 = new Rule("event.male == '女' && event.age >= 18 && event.car == '有车' && event.house == '有房'",
                    "result_personalInfo = '成年女性有车有房'", null);
            Rule rule7 = new Rule("event.male == '女' && event.age >= 18 && event.car != '有车' && event.house == '有房'",
                    "result_personalInfo = '成年女性有房'", null);
            Rule rule8 = new Rule("event.male == '女' && event.age >= 18 && event.car == '有车' && event.house != '有房'",
                    "result_personalInfo = '成年女性有车'", null);
            Rule rule9 = new Rule("event.male == '女' && event.age >= 18 && event.car != '有车' && event.house != '有房'",
                    "result_personalInfo = '成年女性'", null);
            Rule rule10 = new Rule("event.male == '女' && event.age < 18",
                    "result_personalInfo = '未成年女性'", null);

            RuleTable ruleTable = new RuleTable(0);
            ruleTable.addRuleRow(rule1);
            ruleTable.addRuleRow(rule2);
            ruleTable.addRuleRow(rule3);
            ruleTable.addRuleRow(rule4);
            ruleTable.addRuleRow(rule5);
            ruleTable.addRuleRow(rule6);
            ruleTable.addRuleRow(rule7);
            ruleTable.addRuleRow(rule8);
            ruleTable.addRuleRow(rule9);
            ruleTable.addRuleRow(rule10);
            //规则都放在规则map中，作为参数传入
            env_rule.put("ruleTable1", ruleTable);

            return env_rule;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Map<String, Object> buildTestRuleSet(Map<String, Object> env_rule){
        try {
            Rule rule1 = new Rule("event.incomeOfYear <= 20000", "result_access = '年收入低'", null);
            Rule rule2 = new Rule("event.channel == '网申'", "result_access = '非法渠道'", null);
            Rule rule3 = new Rule("event.age < 20 || event.age > 50", "result_access = '年龄不符'", null);
            RuleSet ruleSet = new RuleSet(0);
            ruleSet.addRule(rule1);
            ruleSet.addRule(rule2);
            ruleSet.addRule(rule3);
            env_rule.put("ruleSet1", ruleSet);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return env_rule;
    }

    private Map<String, Object> buildTestRuleCard(Map<String, Object> env_rule){
        try {
            Rule rule1 = new Rule("event.incomeOfYear < 100000 ", "result_scoreCard1 = 1", null);
            Rule rule2 = new Rule("event.overdue < 5 ", "result_scoreCard1 = result_scoreCard1 + 2", null);
            Rule rule3 = new Rule("event.education == '高中' ", "result_scoreCard1 = result_scoreCard1 + 1", null);
            Rule rule4 = new Rule("event.education == '大专' ", "result_scoreCard1 = result_scoreCard1 + 2", null);
            Rule rule5 = new Rule("event.education == '本科' ", "result_scoreCard1 = result_scoreCard1 + 3", null);

            RuleCard ruleCard = new RuleCard("", 0);
            ruleCard.addRule(rule1);
            ruleCard.addRule(rule2);
            ruleCard.addRule(rule3);
            ruleCard.addRule(rule4);
            ruleCard.addRule(rule5);
            env_rule.put("ruleCard1", ruleCard);
            return env_rule;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Map<String, Object> buildTestRuleFlow(Map<String, Object> env_rule){
        RuleFlowNode node1 = new RuleFlowNode("event.cType == '新客户'");
        RuleFlowNode node1_l = new RuleFlowNode("rule(rule.ruleSet1)");
        RuleFlowNode node1_r = new RuleFlowNode("event.cType == '准新客户'");
        node1.setLeftChild(node1_l);
        node1.setRightChild(node1_r);
        RuleFlowNode node2 = new RuleFlowNode("result_access == '准入'",
                "result_approval = '新客通过'", "result_approval = '新客不通过'");
        node1_l.setLeftChild(node2);
        RuleFlowNode node3 = new RuleFlowNode("rule(rule.ruleTable1)");
        node3.setLeftChild(new RuleFlowNode("result_approval = '准新通过'"));
        node1_r.setLeftChild(node3);
        RuleFlowNode node4 = new RuleFlowNode("event.cType == '存量客户'");
        node1_r.setRightChild(node4);
        RuleFlowNode node4_l = new RuleFlowNode("rule(rule.ruleCard1)");
        node4.setLeftChild(node4_l);
        node4_l.setLeftChild(new RuleFlowNode("result_approval = '存量客通过'"));

        RuleFlow ruleFlow = new RuleFlow(node1);
        env_rule.put("ruleFlow1", ruleFlow);

        return env_rule;
    }
}
