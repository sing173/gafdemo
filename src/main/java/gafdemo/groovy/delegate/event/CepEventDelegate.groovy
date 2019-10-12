package gafdemo.groovy.delegate.event

import gafdemo.groovy.pogo.event.CepEventGroovy
import gafdemo.groovy.pogo.event.CepPatternGroovy

/**
 * Created by luomingxing on 2019/9/24.
 */
class CepEventDelegate {
    CepEventGroovy cepEventGroovy

    def getId(){
        return this.cepEventGroovy.id
    }
    def setId(String id){
        this.cepEventGroovy.id = id
    }

    def getName(){
        return this.cepEventGroovy.name
    }
    def setName(String name){
        this.cepEventGroovy.name = name
    }

    def getType(){
        return this.cepEventGroovy.type
    }
    def setType(String type){
        this.cepEventGroovy.type = type
    }

    def getKeyBy(){
        return this.cepEventGroovy.keyBy
    }
    def setKeyBy(String keyBy){
        this.cepEventGroovy.keyBy = keyBy
    }

    CepEventDelegate(CepEventGroovy cepEventGroovy){
        this.cepEventGroovy = cepEventGroovy
    }

    def methodMissing(String name, Object args) {
        if ('patternGroup' == name) {
            def groupClosure = args[0]
            CepGroupPatternDelegate cepGroupPatternDelegate = new CepGroupPatternDelegate()
            groupClosure.delegate = cepGroupPatternDelegate
            groupClosure.resolveStrategy = Closure.DELEGATE_FIRST
            groupClosure()
            cepEventGroovy.patternMap.putAll(cepGroupPatternDelegate.patternMap)
        }

    }
}

class CepGroupPatternDelegate {
    double weight
    Map<String, CepPatternGroovy> patternMap = [:]

    def getWeight(){
        return this.weight
    }
    def setWeight(weight){
        this.weight = weight
    }

    def methodMissing(String name, Object args) {
        if ('pattern' == name) {
            def patternClosure = args[0]
            CepPatternGroovy cepPattern = new CepPatternGroovy()
            CepPatternDelegate cepPatternDelegate = new CepPatternDelegate(cepPattern)
            patternClosure.delegate = cepPatternDelegate
            patternClosure.resolveStrategy = Closure.DELEGATE_FIRST
            patternClosure()

            //收集flink pattern待生成执行计划时使用
            cepPattern.groupWeight = this.weight
            patternMap[cepPattern.name] = cepPattern
        }
    }
}
