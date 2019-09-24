package gafdemo.groovy.delegate.event

import gafdemo.pojo.event.CepEvent
import gafdemo.pojo.event.CepPattern
import org.apache.flink.cep.pattern.Pattern

/**
 * Created by luomingxing on 2019/9/24.
 */
class CepEventDelegate {
    CepEvent cepEvent
    List<Pattern> patternList = []

    def getId(){
        return this.cepEvent.id
    }
    def setId(String id){
        this.cepEvent.id = id
    }

    def getName(){
        return this.cepEvent.name
    }
    def setName(String name){
        this.cepEvent.name = name
    }

    def getType(){
        return this.cepEvent.type
    }
    def setType(String type){
        this.cepEvent.type = type
    }

    CepEventDelegate(CepEvent cepEvent){
        this.cepEvent = cepEvent
    }

    def methodMissing(String name, Object args) {
        if ('patternGroup' == name) {
            def groupClosure = args[0]
            CepGroupPatternDelegate cepGroupPatternDelegate = new CepGroupPatternDelegate()
            groupClosure.delegate = cepGroupPatternDelegate
            groupClosure.resolveStrategy = Closure.DELEGATE_FIRST
            groupClosure()
            //根据权重归纳模式组对象，待事件触发后按权重选择匹配的模式
            this.cepEvent.patternGroupMap[cepGroupPatternDelegate.weight] =
                    cepGroupPatternDelegate.cepPatternList as List<CepPattern>
            //收集flink pattern待生成执行计划时使用
            patternList.addAll(cepGroupPatternDelegate.patternList)
        }

    }
}

class CepGroupPatternDelegate {
    double weight
    List<Pattern> patternList = []
    List<CepPattern> cepPatternList = []

    def getWeight(){
        return this.weight
    }
    def setWeight(weight){
        this.weight = weight
    }

    def methodMissing(String name, Object args) {
        if ('pattern' == name) {
            def patternClosure = args[0]
            CepPatternDelegate cepPatternDelegate = new CepPatternDelegate(new CepPattern(), null)
            patternClosure.delegate = cepPatternDelegate
            patternClosure.resolveStrategy = Closure.DELEGATE_FIRST
            patternClosure()
            this.patternList << cepPatternDelegate.pattern
            this.cepPatternList << cepPatternDelegate.cepPattern
        }


    }
}
