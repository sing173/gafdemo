package gafdemo.groovy.delegate.event

import gafdemo.aviator.AviatorCondition
import gafdemo.enumerate.CepPatternNextEnum
import gafdemo.enumerate.CepPatternTimesEnum
import gafdemo.pojo.event.CepPattern
import org.apache.flink.cep.pattern.Pattern
import org.apache.flink.streaming.api.windowing.time.Time

import java.util.concurrent.TimeUnit

/**
 * Created by luomingxing on 2019/9/23.
 */
class CepPatternDelegate {
    CepPattern cepPattern
    Pattern pattern

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

    CepPatternDelegate(CepPattern cepPattern, Pattern pattern){
        this.cepPattern = cepPattern
        this.pattern = pattern
    }

    def begin(){
        CepPatternNextEnum cepPatternNextEnum = CepPatternNextEnum.valueOf(this.type)
        switch (cepPatternNextEnum){
            case CepPatternNextEnum.begin:
                this.pattern = Pattern.begin(this.name)
                break
            case CepPatternNextEnum.next:
                this.pattern = pattern.next(this.name)
                break
            case CepPatternNextEnum.followedBy:
                this.pattern = pattern.followedBy(this.name)
                break
            case CepPatternNextEnum.followedByAny:
                this.pattern = pattern.followedByAny(this.name)
                break
        }

    }

    def times(){
        CepPatternTimesEnum cepPatternTimesEnum = CepPatternTimesEnum.valueOf(cepPattern.timesName)
        switch (cepPatternTimesEnum){
            case CepPatternTimesEnum.oneOrMore:
                pattern = pattern.oneOrMore()
                break
            case CepPatternTimesEnum.times:
                pattern = pattern.times(cepPattern.timesFrom)
                break
            case CepPatternTimesEnum.timesRange:
                pattern = pattern.times(cepPattern.timesFrom, cepPattern.timesTo)
                break
            case CepPatternTimesEnum.timesOrMore:
                pattern = pattern.timesOrMore(cepPattern.timesFrom)
                break

        }
    }

    def within(){
        TimeUnit timeUnit = TimeUnit.valueOf(cepPattern.withinTimeUnit)
        this.pattern = pattern.within(Time.of(cepPattern.withinTimeSize, timeUnit))
    }

    def methodMissing(String name, Object args) {
        if('condition' == name) {
            def conditionClosure = args[0]
            conditionClosure.delegate = new CepPatternDelegate(cepPattern, pattern)
            conditionClosure.resolveStrategy = Closure.DELEGATE_FIRST
            conditionClosure()
        } else if('and' == name) {
            String expression = args[0] as String
            pattern = pattern.where(new AviatorCondition(expression))
        } else if('or' == name) {
            String expression = args[0] as String
            pattern = pattern.or(new AviatorCondition(expression))
        } else if('until' == name) {
            String expression = args[0] as String
            pattern = pattern.until(new AviatorCondition(expression))
        } else if('times' == name) {
            this.cepPattern.timesName = args[0] as String
            int argSize = args.collect().size()
            if(argSize >= 2)
                this.cepPattern.timesFrom = args[1] as Integer
            if(argSize == 3)
                this.cepPattern.timesTo = args[2] as Integer
            times()
        } else if('within' == name) {
            this.cepPattern.withinTimeUnit = args[0] as String
            this.cepPattern.withinTimeSize = args[1] as Integer
            within()
        } else if('next' == name) {
            def nextClosure = args[0]
            CepPatternDelegate patternDelegate = new CepPatternDelegate(new CepPattern(), pattern)
            nextClosure.delegate = patternDelegate
            nextClosure.resolveStrategy = Closure.DELEGATE_FIRST
            nextClosure()
            this.cepPattern.nextPattern = patternDelegate.cepPattern
            this.pattern = patternDelegate.pattern
        }


    }
}

