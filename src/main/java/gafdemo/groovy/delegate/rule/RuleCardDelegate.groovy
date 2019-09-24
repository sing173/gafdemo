package gafdemo.groovy.delegate.rule

import com.googlecode.aviator.AviatorEvaluator
import com.googlecode.aviator.Expression
import gafdemo.groovy.pogo.rule.RuleCard

/**
 * Created by luomingxing on 2019/9/9.
 */
class RuleCardDelegate extends BaseDelegate{

    RuleCardDelegate(RuleCard ruleCard){
        this.rule = ruleCard
    }

    def methodMissing(String name, Object args) {
        if('propertyScore' == name){
            def propertyClosure = args[0]
            def propertyScore = new RuleCard.RuleCardPropertyScore()
            propertyClosure.delegate = new RuleCardPropertyScoreDelegate(propertyScore)
            propertyClosure.resolveStrategy = Closure.DELEGATE_FIRST
            propertyClosure()
            rule.propertyScoreList << propertyScore
        }

    }
}


class RuleCardPropertyScoreDelegate {
    def propertyScore

    def getPropertyCode(){
        return this.propertyScore.propertyCode
    }

    def setPropertyCode(propertyCode){
        this.propertyScore.propertyCode = propertyCode
    }

    def getPropertyWeight(){
        return this.propertyScore.propertyWeight
    }

    def setPropertyWeight(propertyWeight){
        this.propertyScore.propertyWeight = propertyWeight
    }

    RuleCardPropertyScoreDelegate(propertyScore){
        this.propertyScore = propertyScore
    }

    def methodMissing(String name, Object args) {
        if('propertyItem' == name){
            //参数0为评分项的得分,参数1为评分项的条件，比如(1:"age>18")
            Expression expression = AviatorEvaluator.compile(args[1])
            this.propertyScore.propertyItemMap[args[0]] = expression
        }

    }
}


