package gafdemo.groovy.delegate.rule

import com.googlecode.aviator.AviatorEvaluator
import com.googlecode.aviator.Expression
import gafdemo.groovy.pogo.rule.RuleTable

/**
 * Created by luomingxing on 2019/9/9.
 */
class RuleTableDelegate extends BaseDelegate{

    RuleTableDelegate(RuleTable ruleTable){
        this.rule = ruleTable
    }

    def methodMissing(String name, Object args) {
        if('row' == name || 'column' == name || 'assign' == name){
            def closure = args[0]
            closure.delegate = new RuleTableDelegate(rule)
            closure.resolveStrategy = Closure.DELEGATE_FIRST
            closure()
        } else if('rowItem' == name){
            Expression rowExpression = AviatorEvaluator.compile(args[1] as String)
            rule.rowItemMap[args[0]] = rowExpression
        } else if('columnItem' == name){
            Expression columnExpression = AviatorEvaluator.compile(args[1] as String)
            rule.columnItemMap[args[0]] = columnExpression
        } else if('assignItem' == name){
            def assignItemList = []
            args.each {arg ->
                Expression assignExpression = AviatorEvaluator.compile(arg)
                assignItemList << assignExpression
            }

            rule.assignItemList << assignItemList
        }

    }
}

