package gafdemo.groovy.pogo.rule


/**
 * Created by luomingxing on 2019/9/9.
 */
class Rule extends BaseRule {
    def filter
    def assign
    def otherAssign
    def filterExpress
    def assignExpress
    def otherAssignExpress

    @Override
    Boolean execute(Map<String, Object> env) {
        println("execute rule:$this")
        //执行if判断
        hit = (Boolean) filterExpress.execute(env)
        if(hit){
            //当满足条件并有赋值，则执行赋值
            if(assignExpress != null){
                assignExpress.execute(env)
            }
        } else if(otherAssignExpress != null){
            //if未满足并有否则动作则执行
            otherAssignExpress.execute(env)
        }
        hit
    }
}
