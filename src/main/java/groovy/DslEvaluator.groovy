package groovy

import groovy.delegate.RuleCardDelegate
import groovy.delegate.RuleDelegate
import groovy.delegate.RuleFlowDelegate
import groovy.delegate.RuleSetDelegate
import groovy.delegate.RuleTableDelegate
import groovy.pogo.Rule
import groovy.pogo.RuleCard
import groovy.pogo.RuleFlow
import groovy.pogo.RuleSet
import groovy.pogo.RuleTable

/**
 * Created by luomingxing on 2019/9/10.
 */
class DslEvaluator {

    def createMetaClass(Class clazz, Closure closure) {
        def emc = new ExpandoMetaClass(clazz, false)
        closure(emc)
        emc.initialize()
        return emc
    }

    def executeScript(dslScriptCode, rootName, closure) {
        Script dslScript = new GroovyShell().parse(dslScriptCode)

        dslScript.metaClass = createMetaClass(dslScript.class) { emc ->
            emc."$rootName" = closure
        }
        return dslScript.run()
    }

    def executeDslForRule(dslScript) {
        executeScript(dslScript, 'rule'){ closure ->
            def rule = new Rule()
            closure.delegate = new RuleDelegate(rule)
            closure.resolveStrategy = Closure.DELEGATE_FIRST
            closure()

            return rule
        }
    }

    def executeDslForRuleSet(dslScript) {
        executeScript(dslScript, 'ruleSet'){ closure ->
            def ruleSet = new RuleSet()
            closure.delegate = new RuleSetDelegate(ruleSet)
            closure.resolveStrategy = Closure.DELEGATE_FIRST
            closure()

            return ruleSet
        }
    }

    def executeDslForRuleTable(dslScript) {
        executeScript(dslScript, 'ruleTable'){ closure ->
            def ruleTable = new RuleTable()
            closure.delegate = new RuleTableDelegate(ruleTable)
            closure.resolveStrategy = Closure.DELEGATE_FIRST
            closure()

            return ruleTable
        }
    }

    def executeDslForRuleCard(dslScript) {
        executeScript(dslScript, 'ruleCard'){ closure ->
            def ruleCard = new RuleCard()
            closure.delegate = new RuleCardDelegate(ruleCard)
            closure.resolveStrategy = Closure.DELEGATE_FIRST
            closure()

            return ruleCard
        }
    }

    def executeDslForRuleFlow(dslScript) {
        executeScript(dslScript, 'ruleFlow'){ closure ->
            def ruleFlow = new RuleFlow()
            closure.delegate = new RuleFlowDelegate(ruleFlow)
            closure.resolveStrategy = Closure.DELEGATE_FIRST
            closure()

            return ruleFlow
        }
    }
}
