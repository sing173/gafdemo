package groovy.delegate

import com.googlecode.aviator.AviatorEvaluator
import com.googlecode.aviator.Expression
import groovy.pogo.Rule
import groovy.pogo.RuleCard

/**
 * Created by luomingxing on 2019/9/9.
 */
class RuleCardDelegate {
    def ruleCard

    RuleCardDelegate(ruleCard){
        this.ruleCard = ruleCard
    }

    def methodMissing(String name, Object args) {
        if('info' == name){
            def infoClosure = args[0]
            infoClosure.delegate = new BaseInfoDelegate(ruleCard)
            infoClosure.resolveStrategy = Closure.DELEGATE_FIRST
            infoClosure()
        } else if('propertyScore' == name){
            def propertyClosure = args[0]
            def propertyScore = new RuleCard.RuleCardPropertyScore()
            propertyClosure.delegate = new RuleCardPropertyScoreDelegate(propertyScore)
            propertyClosure.resolveStrategy = Closure.DELEGATE_FIRST
            propertyClosure()
            ruleCard.propertyScoreList << propertyScore
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
//        if('propertyCode' == name){
//            this.propertyScore.propertyCode = args[0] as String
//        } else if('propertyWeight' == name){
//            this.propertyScore.propertyWeight = args[0] as Integer
//        }
//        else
        if('propertyItem' == name){
            def propertyItem = [:]
            //参数0为评分项的条件，参数1为评分项的得分比如(1:"age>18")
            Expression expression = AviatorEvaluator.compile(args[0])
            propertyItem[args[1]] = expression
            this.propertyScore.propertyItemList << propertyItem
        }

    }
}


