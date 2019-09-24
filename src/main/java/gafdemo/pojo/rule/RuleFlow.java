package gafdemo.pojo.rule;

import java.util.Map;

/**
 * Created by luomingxing on 2019/8/27.
 */
public class RuleFlow extends BaseRule implements AviEvaluation {
    private RuleFlowNode treeRoot;
    private Map<String, Object> env;

    public RuleFlow(RuleFlowNode treeRoot){
        this.treeRoot = treeRoot;
    }

    @Override
    public Boolean execute(Map<String, Object> env) {
        this.env = env;
        if(treeRoot != null){
            visitTree(treeRoot);
        }
        return true;
    }

    /**
     * 遍历树节点
     * @param treeNode
     */
    private void visitTree(RuleFlowNode treeNode){
        //判断是否是叶子节点（输出节点）
        if(!treeNode.isLeaf()){
            //则判断是否满足条件/命中，满足访问左边树，不满足访问右边树
            if (treeNode.execute(env)) {
                //TODO 左边树必须有，否则报错
                visitTree(treeNode.getLeftChild());
            } else {
                if(treeNode.hasRightTree()){
                    visitTree(treeNode.getRightChild());
                }
            }
        } else {
            //TODO 叶子节点（输出节点）特殊处理
            treeNode.execute(env);
        }

    }




}
