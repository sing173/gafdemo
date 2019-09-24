package gafdemo.aviator;

import gafdemo.pojo.event.CepEvent;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;

import java.io.Serializable;

/**
 * Created by luomingxing on 2019/9/23.
 */
public class AviatorCondition extends IterativeCondition<CepEvent> implements Serializable {
    private String expression;

    public AviatorCondition(String expression) {
        this.expression = expression;
    }

    @Override
    public boolean filter(CepEvent cepEvent, Context<CepEvent> context) throws Exception {
        return false;
    }
}
