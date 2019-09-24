package gafdemo.pojo.rule;

import com.google.common.base.Strings;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by luomingxing on 2019/8/27.
 */
public class RuleCard extends BaseRule implements AviEvaluation{
    private List<Rule> ruleList = new ArrayList<>();
    private String compute;
    private Integer computeMode;
    private Expression computeExpression;

    public RuleCard(String compute, int computeMode){
        this.compute = compute;
        this.computeMode = computeMode;
        if(!Strings.isNullOrEmpty(compute)){
            this.computeExpression = AviatorEvaluator.compile(compute);
        }
    }


    public String getCompute() {
        return compute;
    }

    public void setCompute(String compute) {
        this.compute = compute;
        this.computeExpression = AviatorEvaluator.compile(compute);
    }


    public Integer getComputeMode() {
        return computeMode;
    }

    public void setComputeMode(Integer computeMode) {
        this.computeMode = computeMode;
    }

    public Expression getComputeExpression() {
        return computeExpression;
    }

    public List<Rule> getRuleList() {
        return ruleList;
    }

    public void addRule(Rule rule) {
        this.ruleList.add(rule);
    }

    @Override
    public Boolean execute(Map<String, Object> env) {
        this.ruleList.forEach(rule -> rule.execute(env));
        //TODO 根据算法做评分加权

        return true;
    }

}
