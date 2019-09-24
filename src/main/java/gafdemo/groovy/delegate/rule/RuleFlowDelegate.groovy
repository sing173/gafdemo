package gafdemo.groovy.delegate.rule

import com.googlecode.aviator.AviatorEvaluator
import gafdemo.groovy.pogo.rule.RuleFlow

/**
 * Created by luomingxing on 2019/9/11.
 */
class RuleFlowDelegate extends BaseDelegate{

    RuleFlowDelegate(RuleFlow ruleFlow){
        this.rule = ruleFlow
    }

    def methodMissing(String name, Object args) {
        if('rootNode' == name){
            def flowNodeClosure = args[0]
            def rootNode = new RuleFlow.RuleFlowNode()
            flowNodeClosure.delegate = new RuleFlowNodeDelegate(rootNode)
            flowNodeClosure.resolveStrategy = Closure.DELEGATE_FIRST
            flowNodeClosure()
            this.rule.rootNode = rootNode

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

