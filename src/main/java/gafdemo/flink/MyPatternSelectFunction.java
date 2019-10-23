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
        //CepPatternGroovy中的pattern对象不能序列号，所以先获取模式链下所有模式名称
        this.allPatternName = cepPattern.getAllPatternName(new ArrayList<>(), cepPattern.getPattern());
    }

    @Override
    public void flatSelect(Map<String, List<DataSourceEvent>> patternMap, Collector<CepEventResult> collector) {
        System.out.println("pattern select "+System.currentTimeMillis()+"");
        CepEventResult cepEventResult = new CepEventResult();
        assert allPatternName != null;
        //模式链名称
        cepEventResult.setHitPattern(cepPattern.getMainName());
        cepEventResult.setPatternGroovy(cepPattern);
        //拿第一个命中事件源即当前事件源
        List<DataSourceEvent> currEventList = patternMap.get(allPatternName.get(0));
        DataSourceEvent currDataSourceEvent = currEventList.get(currEventList.size() -1);
        cepEventResult.setCurrentDataSourceEvent(currDataSourceEvent);
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
