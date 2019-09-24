package gafdemo.groovy.pogo.rule

import com.googlecode.aviator.Expression

/**
 * Created by luomingxing on 2019/9/10.
 */
class RuleTable extends BaseRule{
    def rowItemMap = [:]
    def columnItemMap = [:]
    def assignItemList = []
    def rowHitIndex = -1
    def columnHitIndex = -1

    /**
     * 0 简单决策表；1 复杂决策表
     */
    def type

    @Override
    Boolean execute(Map<String, Object> env) {
        println("--------execute ruleTable $this")
        //根据表类型执行决策
        if(0 == type){
            //简单决策表只需执行行规则
            for(Map.Entry<Object, Object> rowItem : rowItemMap) {
                Expression rowExpression = rowItem.value as Expression
                println("execute row $rowItem.key")
                if (rowExpression.execute(env)) {
                    rowHitIndex = rowItem.key as Integer
                    this.hit = true
                    println("hit row $rowItem.key")

                    //命中后根据行号拿到赋值表达式，执行对应的动作, 简单决策表
                    Expression assignExpress = assignItemList[rowHitIndex][0] as Expression
                    assignExpress.execute(env)
                    break
                }
            }
        } else {
            //交叉决策表先执行行规则，再执行列规则
            for(Map.Entry<Object, Object> rowItem : rowItemMap) {
                Expression rowExpression = rowItem.value as Expression
                println("execute row $rowItem.key")
                if (rowExpression.execute(env)) {
                    rowHitIndex = rowItem.key as Integer
                    println("hit row $rowItem.key")
                    //命中行规则后再遍历列执行列规则
                    for(Map.Entry<Object, Object> columnItem : columnItemMap) {
                        Expression columnExpression = columnItem.value as Expression
                        println("execute column $columnItem.key")
                        if(columnExpression.execute(env)){
                            println("hit column $columnItem.key")
                            //同时命中行列后，根据行列号拿到赋值表达式，并执行
                            columnHitIndex = columnItem.key
                            this.hit = true
                            Expression assignExpress = assignItemList[rowHitIndex][columnHitIndex] as Expression
                            assignExpress.execute(env)
                            return this.hit
                        }
                    }
                }

            }


        }

        this.hit
    }
}
