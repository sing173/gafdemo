package groovy.pogo

/**
 * Created by luomingxing on 2019/9/10.
 */
class RuleFlow extends BaseRule{
    def rootNode
    def env

    RuleFlow(){
        RuleFlowNode.metaClass.execute = { args_env ->
            if(this.isLeaf()){
                this.expression.execute(args_env)
                return false
            } else {
                return (Boolean) this.expression.execute(args_env)
            }
        }
    }

    @Override
    def execute(Map<String, Object> env) {
        this.env = env
        visitTree(rootNode)
        return true
    }

    def visitTree(treeNode) {
        //判断是否是叶子节点（输出节点）
        if (!treeNode.isLeaf()) {
            //则判断是否满足条件/命中，满足访问左边树，不满足访问右边树
            if (treeNode.execute(env)) {
                //TODO 左边树必须有，否则报错
                visitTree(treeNode.getLeftChild())
            } else {
                if (treeNode.hasRightTree()) {
                    visitTree(treeNode.getRightChild())
                }
            }
        } else {
            //TODO 叶子节点（输出节点）特殊处理
            treeNode.execute(env)
        }
    }

    class RuleFlowNode {
        def expressionStr
        def expression
        def leftChild
        def rightChild

        def isLeaf(){
            return leftChild == null && rightChild == null
        }

        def hasLeftTree(){
            return leftChild != null
        }

        def hasRightTree(){
            return rightChild != null
        }
    }
}
