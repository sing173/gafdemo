package groovy.delegate

import com.googlecode.aviator.AviatorEvaluator
import groovy.pogo.RuleFlow

/**
 * Created by luomingxing on 2019/9/11.
 */
class RuleFlowDelegate {
    def ruleFlow

    RuleFlowDelegate(ruleFlow){
        this.ruleFlow = ruleFlow
    }

    def methodMissing(String name, Object args) {
        if('info' == name){
            def infoClosure = args[0]
            infoClosure.delegate = new BaseInfoDelegate(ruleFlow)
            infoClosure.resolveStrategy = Closure.DELEGATE_FIRST
            infoClosure()
        } else if('rootNode' == name){
            def flowNodeClosure = args[0]
            def rootNode = new RuleFlow.RuleFlowNode()
            flowNodeClosure.delegate = new RuleFlowNodeDelegate(rootNode)
            flowNodeClosure.resolveStrategy = Closure.DELEGATE_FIRST
            flowNodeClosure()
            this.ruleFlow.rootNode = rootNode

        }

    }

}

class RuleFlowNodeDelegate {
    def ruleFlowNode

    RuleFlowNodeDelegate(ruleFlowNode){
        this.ruleFlowNode = ruleFlowNode
    }

    def methodMissing(String name, Object args) {
        if('expression' == name) {
            this.ruleFlowNode.expressionStr = args[0] as String
            this.ruleFlowNode.expression = AviatorEvaluator.compile(this.ruleFlowNode.expressionStr as String)
        } else if('leftChild' == name) {
            def flowNodeClosure = args[0]
            def flowNodeL = new RuleFlow.RuleFlowNode()
            ruleFlowNode.leftChild = flowNodeL
            flowNodeClosure.delegate = new RuleFlowNodeDelegate(flowNodeL)
            flowNodeClosure.resolveStrategy = Closure.DELEGATE_FIRST
            flowNodeClosure()

        } else if('rightChild' == name) {
            def flowNodeClosure = args[0]
            def flowNodeR = new RuleFlow.RuleFlowNode()
            ruleFlowNode.rightChild = flowNodeR
            flowNodeClosure.delegate = new RuleFlowNodeDelegate(flowNodeR)
            flowNodeClosure.resolveStrategy = Closure.DELEGATE_FIRST
            flowNodeClosure()

        }
    }
}

