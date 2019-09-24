package gafdemo.pojo.rule;

import com.google.common.base.Strings;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 规则
 * @author luomingxing
 * @date 2019/8/26
 */
public class Rule extends BaseRule implements AviEvaluation {

    private String filter;
    private String assign;
    private String otherAssign;
    private List<Rule> childList;
    private Expression filterExpress;
    private Expression assignExpress;
    private Expression otherAssignExpress;

    public Rule(String filter, String assign, String otherAssign) throws Exception {
        if(Strings.isNullOrEmpty(filter)){
            throw new Exception("filter cant not be null or empty!");
        }
        filterExpress = AviatorEvaluator.compile(filter);
        if(!Strings.isNullOrEmpty(assign)){
            assignExpress = AviatorEvaluator.compile(assign);
        }
        if(!Strings.isNullOrEmpty(otherAssign)){
            otherAssignExpress = AviatorEvaluator.compile(otherAssign);
        }
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getAssign() {
        return assign;
    }

    public void setAssign(String assign) {
        this.assign = assign;
    }

    public String getOtherAssign() {
        return otherAssign;
    }

    public void setOtherAssign(String otherAssign) {
        this.otherAssign = otherAssign;
    }

    public Expression getFilterExpress() {
        return filterExpress;
    }

    public List<Rule> getChildList() {
        return childList;
    }

    public void addChild(Rule child) {
        if(this.childList == null){
            this.childList = new ArrayList<>();
        }
        this.childList.add(child);
    }

    @Override
    public Boolean execute(Map<String, Object> env) {
        //执行if判断
        hit = (Boolean) filterExpress.execute(env);
        if(hit){
            //当满足条件并有赋值，则执行赋值
            if(assignExpress != null){
                assignExpress.execute(env);
            }
            //遍历执行嵌套的子规则
            if(childList != null && !childList.isEmpty()){
                childList.forEach(rule -> rule.execute(env));
            }
        } else if(otherAssignExpress != null){
            //if未满足并有否则动作则执行
            otherAssignExpress.execute(env);
        }
        return hit;
    }

}
