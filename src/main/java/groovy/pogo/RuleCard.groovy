package groovy.pogo

import com.googlecode.aviator.Expression

/**
 * Created by luomingxing on 2019/9/10.
 */
class RuleCard extends BaseRule {
    def propertyScoreList = []
    def type

    class RuleCardPropertyScore {
        def propertyCode
        def propertyWeight
        def propertyScore
        def propertyItemList = []

    }

    @Override
    def execute(Map<String, Object> env) {
        propertyScoreList.each {propertyScore ->
            //TODO 根据算法做评分加权


        }

        this.hit
    }
}
