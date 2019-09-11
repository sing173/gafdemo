package groovy.delegate

import groovy.pogo.Rule

/**
 * Created by luomingxing on 2019/9/9.
 */
class RuleTableDelegate {
    def ruleTable

    RuleTableDelegate(ruleTable){
        this.ruleTable = ruleTable
    }

    def methodMissing(String name, Object args) {
        if('info' == name){
            def infoClosure = args[0]
            infoClosure.delegate = new BaseInfoDelegate(ruleTable)
            infoClosure.resolveStrategy = Closure.DELEGATE_FIRST
            infoClosure()
        } else if('rule' == name){
            def ruleClosure = args[0]
            def rule = new Rule()
            ruleClosure.delegate = new RuleDelegate(rule)
            ruleClosure.resolveStrategy = Closure.DELEGATE_FIRST
            ruleClosure()
            ruleTable.ruleList << rule
        }

    }
}

