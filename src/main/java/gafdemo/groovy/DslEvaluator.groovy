package gafdemo.groovy

import gafdemo.groovy.delegate.event.CepEventDelegate
import gafdemo.groovy.delegate.event.CepPatternDelegate
import gafdemo.groovy.delegate.rule.RuleCardDelegate
import gafdemo.groovy.delegate.rule.RuleDelegate
import gafdemo.groovy.delegate.rule.RuleFlowDelegate
import gafdemo.groovy.delegate.rule.RuleSetDelegate
import gafdemo.groovy.delegate.rule.RuleTableDelegate
import gafdemo.groovy.pogo.rule.Rule
import gafdemo.groovy.pogo.rule.RuleCard
import gafdemo.groovy.pogo.rule.RuleFlow
import gafdemo.groovy.pogo.rule.RuleSet
import gafdemo.groovy.pogo.rule.RuleTable
import gafdemo.pojo.event.CepEvent
import gafdemo.pojo.event.CepPattern

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

    @Deprecated
    def executeDslForPattern(dslScript) {
        executeScript(dslScript, 'pattern'){ closure ->
            CepPattern cepPattern = new CepPattern()
            CepPatternDelegate patternDelegate = new CepPatternDelegate(cepPattern, null)
            closure.delegate = patternDelegate
            closure.resolveStrategy = Closure.DELEGATE_FIRST
            closure()

            def cepPatternMap = [:]
            cepPatternMap[cepPattern] = patternDelegate.pattern
            return cepPatternMap
        }
    }

    def executeDslForEvent(dslScript) {
        executeScript(dslScript, 'event'){ closure ->
            CepEvent cepEvent = new CepEvent()
            CepEventDelegate cepEventDelegate = new CepEventDelegate(cepEvent)
            closure.delegate = cepEventDelegate
            closure.resolveStrategy = Closure.DELEGATE_FIRST
            closure()

            def cepEventMap = [:]
            cepEventMap[cepEvent] = cepEventDelegate.patternList
            return cepEventMap
        }
    }
}
