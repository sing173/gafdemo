package groovy.delegate

import com.googlecode.aviator.AviatorEvaluator

/**
 * Created by luomingxing on 2019/9/9.
 */
class RuleDelegate {
    def rule

    RuleDelegate(rule){
        this.rule = rule
    }

    def methodMissing(String name, Object args) {
        if('info' == name){
            def infoClosure = args[0]
            infoClosure.delegate = new BaseInfoDelegate(rule)
            infoClosure.resolveStrategy = Closure.DELEGATE_FIRST
            infoClosure()
        }
        else if('filter' == name){
            rule.filter = args[0] as String
            rule.filterExpress = AviatorEvaluator.compile(args[0] as String)
        } else if('assign' == name){
            rule.assign = args[0] as String
            rule.assignExpress = AviatorEvaluator.compile(args[0] as String)
        } else {
            rule.otherAssign = args[0] as String
            rule.otherAssignExpress = AviatorEvaluator.compile(args[0] as String)
        }

    }
}

