package gafdemo.groovy.pogo.rule

import com.googlecode.aviator.Expression

/**
 * Created by luomingxing on 2019/9/10.
 */
class RuleCard extends BaseRule {
    def propertyScoreList = []
    /**
     * 评分类型 0 加权求和 1 求和 2 平均值 3 最大值 4 最小值
     */
    def type
    Double totalScore = 0

    @Override
    Boolean execute(Map<String, Object> env) {
        println("--------execute ruleCard: $this, type: $type")
        propertyScoreList.eachWithIndex {propertyScorePogo, index ->
            println("execute ruleCard propertyScore $propertyScorePogo.propertyCode")
            for(Map.Entry<Object, Object> propertyItem : propertyScorePogo.propertyItemMap) {
                //执行评分项的评分条件
                Expression expression = propertyItem.value as Expression
                //命中后给当前的属性评分项赋值分数并跳出循环,执行下一个属性评分
                if(expression.execute(env)) {
                    propertyScorePogo.propertyScore = propertyItem.key as Integer
                    println("execute hit $propertyScorePogo.propertyCode,score:$propertyScorePogo.propertyScore")
                    break
                }
            }
            switch (type){
                case 0://加权求和
                    propertyScorePogo.propertyScore = propertyScorePogo.propertyScore * propertyScorePogo.propertyWeight
                    totalScore += propertyScorePogo.propertyScore
                    break
                case 1://求和
                case 2://平均值
                    totalScore += propertyScorePogo.propertyScore
                    break
                case 3://最大值
                    if(totalScore < (Double)propertyScorePogo.propertyScore) {
                        totalScore = propertyScorePogo.propertyScore
                    }
                    break
                case 4://最小值
                    if(totalScore > (Double)propertyScorePogo.propertyScore) {
                        totalScore = propertyScorePogo.propertyScore
                    }
                    break
            }
        }
        //平均值在最后计算一次
        if(2 == type) {
            totalScore = totalScore / propertyScoreList.size()
        }
        println("execute totalScore= $totalScore")
        this.hit = totalScore > 0
    }

    class RuleCardPropertyScore {
        def propertyCode
        def propertyWeight
        double propertyScore = 0
        def propertyItemMap = [:]

        String toString(){
            "propertyCode:$propertyCode,propertyWeight:$propertyWeight,propertyScore:$propertyScore"
        }
    }
}
