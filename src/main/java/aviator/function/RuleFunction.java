package aviator.function;

import aviator.vo.BaseRule;
import aviator.vo.Rule;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;

import java.util.Map;

/**
 *
 * @author luomingxing
 * @date 2019/8/23
 */
public class  RuleFunction extends AbstractFunction {

    @Override
    public String getName() {
        return "rule";
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        BaseRule rule = (BaseRule) FunctionUtils.getJavaObject(arg1, env);
        return AviatorBoolean.valueOf(rule.execute(env));
    }


}
