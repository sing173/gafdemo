package groovy.pogo

import com.googlecode.aviator.Expression

/**
 * Created by luomingxing on 2019/9/10.
 */
class RuleCard extends BaseRule {
    def propertyScoreList = []
    def type

    @Override
    def execute(Map<String, Object> env) {
        propertyScoreList.each {propertyScore ->
            //TODO 根据算法做评分加权


        }

        this.hit
    }

    class RuleCardPropertyScore {
        def propertyCode
        def propertyWeight
        def propertyScore = 0
        def propertyItemList = []

        String toString(){
            "propertyCode:$propertyCode,propertyWeight:$propertyWeight,propertyScore:$propertyScore"
        }
    }
}
