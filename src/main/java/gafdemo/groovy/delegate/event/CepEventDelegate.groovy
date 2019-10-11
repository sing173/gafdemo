package gafdemo.groovy.delegate.event

import gafdemo.groovy.pogo.event.CepEventGroovy
import gafdemo.groovy.pogo.event.CepPatternGroovy

/**
 * Created by luomingxing on 2019/9/24.
 */
class CepEventDelegate {
    CepEventGroovy cepEventGroovy

    def getId(){
        return this.cepEventGroovy.cepEvent.id
    }
    def setId(String id){
        this.cepEventGroovy.cepEvent.id = id
    }

    def getName(){
        return this.cepEventGroovy.cepEvent.name
    }
    def setName(String name){
        this.cepEventGroovy.cepEvent.name = name
    }

    def getType(){
        return this.cepEventGroovy.cepEvent.type
    }
    def setType(String type){
        this.cepEventGroovy.cepEvent.type = type
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
            CepPatternDelegate cepPatternDelegate = new CepPatternDelegate(new CepPatternGroovy())
            patternClosure.delegate = cepPatternDelegate
            patternClosure.resolveStrategy = Closure.DELEGATE_FIRST
            patternClosure()

            //收集flink pattern待生成执行计划时使用
            CepPatternGroovy cepPattern = cepPatternDelegate.cepPattern
            cepPattern.groupWeight = this.weight
            patternMap[cepPattern.name] = cepPattern
        }
    }
}
