package groovy.pogo

/**
 * Created by luomingxing on 2019/9/10.
 */
class RuleTable extends BaseRule{
    def ruleList = []
    /**
     * 0 简单决策表；1 复杂决策表
     */
    def type

    @Override
    def execute(Map<String, Object> env) {
        ruleList.each {rule ->
            if(rule.execute(env)){
                this.setHit(true)
                return true
            }
        }
        this.hit
    }
}
