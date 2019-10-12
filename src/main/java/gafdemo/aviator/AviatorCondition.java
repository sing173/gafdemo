package gafdemo.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import gafdemo.groovy.pogo.event.DataSourceEvent;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;

import java.io.Serializable;

/**
 * Created by luomingxing on 2019/9/23.
 */
public class AviatorCondition extends IterativeCondition<DataSourceEvent> implements Serializable {
    private String expression;

    public AviatorCondition(String expression) {
        this.expression = expression;
    }

    @Override
    public boolean filter(DataSourceEvent cepEvent, Context<DataSourceEvent> context) {
        return (Boolean) AviatorEvaluator.execute(expression, cepEvent.getAviatorEnv());
    }
}
