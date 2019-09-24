package gafdemo.aviator;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;
import gafdemo.groovy.pogo.rule.BaseRule;

import java.util.Map;

/**
 *
 * @author luomingxing
 * @date 2019/9/11
 */
public class RuleGFunction extends AbstractFunction {

    @Override
    public String getName() {
        return "ruleG";
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        BaseRule rule = (BaseRule) FunctionUtils.getJavaObject(arg1, env);
        return AviatorBoolean.valueOf(rule.execute(env));
    }


}
