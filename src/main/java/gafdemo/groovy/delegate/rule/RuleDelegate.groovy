package gafdemo.groovy.delegate.rule

import com.googlecode.aviator.AviatorEvaluator
import gafdemo.groovy.pogo.rule.Rule

/**
 * Created by luomingxing on 2019/9/9.
 */
class RuleDelegate extends BaseDelegate{

    RuleDelegate(Rule rule){
        this.rule = rule
    }

    def methodMissing(String name, Object args) {
        if('filter' == name){
            rule.filter = args[0] as String
            rule.filterExpress = AviatorEvaluator.compile(args[0] as String)
        } else if('assign' == name){
            rule.assign = args[0] as String
            rule.assignExpress = AviatorEvaluator.compile(args[0] as String)
        } else if('otherAssign' == name){
            rule.otherAssign = args[0] as String
            rule.otherAssignExpress = AviatorEvaluator.compile(args[0] as String)
        }

    }
}

