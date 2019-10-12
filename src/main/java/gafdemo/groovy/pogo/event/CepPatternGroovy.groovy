package gafdemo.groovy.pogo.event

import org.apache.flink.cep.pattern.Pattern

/**
 * Created by luomingxing on 2019/9/25.
 */
class CepPatternGroovy implements Serializable{
    String id
    String name
    String type
    double groupWeight
    double weight

    transient Pattern<DataSourceEvent, DataSourceEvent> pattern

    List<String> getAllPatternName(List<String> patternNames, Pattern pattern){
        patternNames.add(pattern.getName())
        Pattern prePattern = pattern.getPrevious()
        if(prePattern != null) {
            getAllPatternName(patternNames, prePattern)
        }
        return patternNames
    }
}
