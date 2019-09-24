package gafdemo;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.Options;
import com.googlecode.aviator.runtime.FunctionArgument;
import gafdemo.enumerate.CepPatternTimesEnum;
import org.apache.flink.cep.pattern.Pattern;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * Created by luomingxing on 2019/8/22.
 */
public class TestAviator {

    @Test
    public void test(){
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("age", 17);
        env.put("male", '女');

        AviatorEvaluator.defineFunction("rule2","lambda(male) -> " +
                "println('in rule2');" +
                "male == '男' ? " +
                "'成年男性' : '成年女性' " +
                "end");
        AviatorEvaluator.defineFunction("rule3","lambda(male) -> " +
                "println('in rule3');" +
                "male == '女' ? " +
                "'未成年女性' : '未成年男性' " +
                "end");
        AviatorEvaluator.defineFunction("rule1","lambda(age) -> " +
                "age >= 18 ? " +
                "rule2(male) : rule3(male) " +
                "end");

        String ruleStream = "rule1(age)";
        Expression ruleExp1 = AviatorEvaluator.compile(ruleStream);
        System.out.println(ruleExp1.execute(env));

    }

    @Test
    public void testArg(){
        AviatorEvaluator.setOption(Options.CAPTURE_FUNCTION_ARGS, true);

        List<FunctionArgument> args = (List<FunctionArgument>) AviatorEvaluator
                .execute("f = lambda(a,bc, d) -> __args__ end; f(1,2,100+2)");

        assertEquals(3, args.size());
        System.out.println(args);
        assertEquals(0, args.get(0).getIndex());
        assertEquals("1", args.get(0).getExpression());
        assertEquals(1, args.get(1).getIndex());
        assertEquals("2", args.get(1).getExpression());
        assertEquals(2, args.get(2).getIndex());
        assertEquals("100+2", args.get(2).getExpression());
    }



    @Test
    public void testArg3(){
        if((Boolean) AviatorEvaluator.execute("a = 'a'"))
            System.out.println("in testArg3");

    }

    @Test
    public void testTime(){
        TimeUnit timeUnit = TimeUnit.valueOf("SECONDS");
        System.out.println(timeUnit.toString());

    }
}
