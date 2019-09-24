package gafdemo.pojo.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author luomingxing
 * @date 2019/8/27
 */
public class RuleTable extends BaseRule implements AviEvaluation {
    /**
     * 0 简单决策表；1 交叉决策表
     */
    private int type;
    private List<Rule> ruleList = new ArrayList<>();

    public RuleTable(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Rule> getRuleList() {
        return   ruleList;
    }

    public void addRuleRow(Rule rule) {
        this.ruleList.add(rule);
    }

    @Override
    public Boolean execute(Map<String, Object> env) {
        ruleList.forEach(rule -> rule.execute(env));
        return true;
    }
}
