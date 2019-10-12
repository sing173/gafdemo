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
    private List<String> allPatternName;

    public MyPatternSelectFunction(CepPatternGroovy cepPattern){
        this.cepPattern = cepPattern;
        this.allPatternName = cepPattern.getAllPatternName(new ArrayList<>(), cepPattern.getPattern());
    }

    @Override
    public void flatSelect(Map<String, List<DataSourceEvent>> patternMap, Collector<CepEventResult> collector) {
        CepEventResult cepEventResult = new CepEventResult();
        assert allPatternName != null;
        //拿第一个命中事件即当前事件
        cepEventResult.setHitPattern(cepPattern.getName());
        List<DataSourceEvent> currEventList = patternMap.get(allPatternName.get(0));
        DataSourceEvent currDataSourceEvent = currEventList.get(currEventList.size() -1);
        cepEventResult.setSeqNo(currDataSourceEvent.getSeqNo());
        //TODO 最终结果处理
        cepEventResult.setResult(currDataSourceEvent.getResult());
        //遍历每个模式，获取命中模式的事件源
        allPatternName.forEach(patternName -> {
            List<DataSourceEvent> dataSourceEvents = patternMap.get(patternName);
            //TODO 根据业务逻辑加工事件输出
            cepEventResult.getHitPatternEventMap().put(patternName, dataSourceEvents);


        });
        collector.collect(cepEventResult);
    }


}
