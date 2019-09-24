package gafdemo.pojo.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by luomingxing on 2019/8/27.
 */
public class RuleSet extends BaseRule implements AviEvaluation {
    /**
     * 0 优先模式；1 贪婪模式
     */
    private int type;
    private List<Rule> ruleList = new ArrayList<>();

    public RuleSet(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Rule> getRuleList() {
        return ruleList;
    }

    public void addRule(Rule rule) {
        this.ruleList.add(rule);
    }

    @Override
    public Boolean execute(Map<String, Object> env) {
        if(type == 0){
            //优先级模式命中一个规则即跳出，不再执行其他规则
            for(Rule rule : ruleList){
                if(rule.execute(env)){
                    this.setHit(true);
                    break;
                }
            }
        } else {
            //贪婪模式全模式执行
            ruleList.forEach(rule -> {
                if(rule.execute(env) && !this.hit){
                    this.setHit(true);
                }
            });
        }
        return this.hit;
    }
}
