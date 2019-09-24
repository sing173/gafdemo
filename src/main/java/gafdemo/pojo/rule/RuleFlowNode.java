package gafdemo.pojo.rule;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;

import java.util.Map;

/**
 * Created by luomingxing on 2019/8/30.
 */
public class RuleFlowNode implements AviEvaluation{
    private RuleFlowNode leftChild;
    private RuleFlowNode rightChild;
    private Expression expression;
    private String expressionStr;

    public RuleFlowNode(String expression){
        this.expressionStr = expression;
        this.expression = AviatorEvaluator.compile(expression);
    }

    public RuleFlowNode(String expression, RuleFlowNode leftChild){
        this.expression = AviatorEvaluator.compile(expression);
        this.leftChild = leftChild;
    }

    public RuleFlowNode(String expression, RuleFlowNode leftChild, RuleFlowNode rightChild){
        this.expression = AviatorEvaluator.compile(expression);
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public RuleFlowNode(String expression, String leftChildExpress, String rightChildExpress){
        this.expression = AviatorEvaluator.compile(expression);
        RuleFlowNode ruleFlowNodeL = new RuleFlowNode(leftChildExpress);
        RuleFlowNode ruleFlowNodeR = new RuleFlowNode(rightChildExpress);
        this.leftChild = ruleFlowNodeL;
        this.rightChild = ruleFlowNodeR;
    }

    public void setLeftChild(RuleFlowNode leftChild) {
        this.leftChild = leftChild;
    }

    public void setRightChild(RuleFlowNode rightChild) {
        this.rightChild = rightChild;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public void setExpression(String expression) {
        this.expression = AviatorEvaluator.compile(expression);
    }

    public RuleFlowNode getLeftChild() {
        return leftChild;
    }

    public RuleFlowNode getRightChild() {
        return rightChild;
    }

    public Expression getExpression() {
        return expression;
    }

    public boolean hasLeftTree(){
        return leftChild != null;
    }

    public boolean hasRightTree(){
        return rightChild != null;
    }

    public boolean isRoot() {
        return leftChild != null && rightChild != null;
    }

    public boolean isLeaf(){
        return leftChild == null && rightChild == null;
    }

    @Override
    public Boolean execute(Map<String, Object> env){
        if(isLeaf()){
            this.expression.execute(env);
            return false;
        } else {
            return (Boolean) this.expression.execute(env);
        }

    }
}
