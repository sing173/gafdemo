package gafdemo.flink;

import gafdemo.groovy.pogo.event.CepEventResult;
import gafdemo.groovy.pogo.event.CepPatternGroovy;
import gafdemo.groovy.pogo.event.DataSourceEvent;
import org.apache.flink.cep.PatternFlatSelectFunction;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by luomingxing on 2019/9/26.
 */
public class MyPatternSelectFunction implements PatternFlatSelectFunction<DataSourceEvent, CepEventResult> {
    private CepPatternGroovy cepPattern;

    public MyPatternSelectFunction(CepPatternGroovy cepPattern){
        this.cepPattern = cepPattern;

    }

    @Override
    public void flatSelect(Map<String, List<DataSourceEvent>> patternMap, Collector<CepEventResult> collector) {
        Pattern pattern = cepPattern.getPattern();
        List<String> patternNames = getAllPatternName(new ArrayList<>(), pattern);

        //遍历每个模式，获取命中模式的事件源
        patternNames.forEach(patternName -> {
            List<DataSourceEvent> dataSourceEvent = patternMap.get(patternName);
            //TODO 根据业务逻辑加工事件输出附加结果DataSourceEvent.result




            collector.collect(null);
        });



    }

    private List<String> getAllPatternName(List<String> patternNames, Pattern pattern){
        patternNames.add(pattern.getName());
        Pattern prePattern = pattern.getPrevious();
        if(prePattern != null) {
            getAllPatternName(patternNames, prePattern);
        }
        return patternNames;
    }
}
