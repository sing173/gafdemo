package gafdemo.groovy.delegate.event

import gafdemo.aviator.AviatorCondition
import gafdemo.enumerate.CepPatternNextEnum
import gafdemo.enumerate.CepPatternTimesEnum
import gafdemo.groovy.pogo.event.CepEventGroovy
import gafdemo.groovy.pogo.event.CepPatternGroovy
import gafdemo.pojo.event.CepPattern
import org.apache.flink.cep.pattern.Pattern
import org.apache.flink.streaming.api.windowing.time.Time

import java.util.concurrent.TimeUnit

/**
 * Created by luomingxing on 2019/9/23.
 */
class CepPatternDelegate {
    CepPatternGroovy cepPattern

    def getId(){
        return this.cepPattern.id
    }
    def setId(String id){
        this.cepPattern.id = id
    }

    def getName(){
        return this.cepPattern.name
    }
    def setName(String name){
        this.cepPattern.name = name
    }

    def getType(){
        return this.cepPattern.type
    }
    def setType(String type){
        this.cepPattern.type = type
        begin()
    }

    def getWeight(){
        return this.cepPattern.weight
    }
    def setWeight(weight){
        this.cepPattern.weight = weight
    }

    CepPatternDelegate(CepPatternGroovy cepPattern){
        this.cepPattern = cepPattern
    }

    def begin(){
        CepPatternNextEnum cepPatternNextEnum = CepPatternNextEnum.valueOf(this.type)
        switch (cepPatternNextEnum){
            case CepPatternNextEnum.begin:
                this.cepPattern.pattern = Pattern.begin(this.name)
                break
            case CepPatternNextEnum.next:
                this.cepPattern.pattern = cepPattern.pattern.next(this.name)
                break
            case CepPatternNextEnum.followedBy:
                this.cepPattern.pattern = cepPattern.pattern.followedBy(this.name)
                break
            case CepPatternNextEnum.followedByAny:
                this.cepPattern.pattern = cepPattern.pattern.followedByAny(this.name)
                break
        }

    }

    def times(String timesName, int timesFrom, int timesTo){
        CepPatternTimesEnum cepPatternTimesEnum = CepPatternTimesEnum.valueOf(timesName)
        switch (cepPatternTimesEnum){
            case CepPatternTimesEnum.oneOrMore:
                cepPattern.pattern = cepPattern.pattern.oneOrMore()
                break
            case CepPatternTimesEnum.times:
                cepPattern.pattern = cepPattern.pattern.times(timesFrom)
                break
            case CepPatternTimesEnum.timesRange:
                cepPattern.pattern = cepPattern.pattern.times(timesFrom, timesTo)
                break
            case CepPatternTimesEnum.timesOrMore:
                cepPattern.pattern = cepPattern.pattern.timesOrMore(timesFrom)
                break

        }
    }

    def within(String withinTimeUnit, int withinTimeSize){
        TimeUnit timeUnit = TimeUnit.valueOf(withinTimeUnit)
        this.cepPattern.pattern = cepPattern.pattern.within(Time.of(withinTimeSize, timeUnit))
    }

    def methodMissing(String name, Object args) {
        if('condition' == name) {
            def conditionClosure = args[0]
            conditionClosure.delegate = new CepPatternDelegate(cepPattern)
            conditionClosure.resolveStrategy = Closure.DELEGATE_FIRST
            conditionClosure()
        } else if('and' == name) {
            String expression = args[0] as String
            cepPattern.pattern = cepPattern.pattern.where(new AviatorCondition(expression))
        } else if('or' == name) {
            String expression = args[0] as String
            cepPattern.pattern = cepPattern.pattern.or(new AviatorCondition(expression))
        } else if('until' == name) {
            String expression = args[0] as String
            cepPattern.pattern = cepPattern.pattern.until(new AviatorCondition(expression))
        } else if('times' == name) {
            String timesName = args[0] as String
            int timesFrom = 0
            int timesTo = 0
            int argSize = args.collect().size()
            if(argSize >= 2)
                timesFrom = args[1] as Integer
            if(argSize == 3)
                timesTo = args[2] as Integer
            times(timesName, timesFrom, timesTo)
        } else if('within' == name) {
            String withinTimeUnit = args[0] as String
            int withinTimeSize = args[1] as Integer
            within(withinTimeUnit, withinTimeSize)
        } else if('next' == name) {
            def nextClosure = args[0]
            CepPatternDelegate patternDelegate = new CepPatternDelegate(this.cepPattern)
            nextClosure.delegate = patternDelegate
            nextClosure.resolveStrategy = Closure.DELEGATE_FIRST
            nextClosure()
        }


    }
}

