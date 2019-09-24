package gafdemo.groovy.pogo.rule

/**
 * Created by luomingxing on 2019/9/10.
 */
class RuleSet extends BaseRule{
    def ruleMap = [:]
    /**
     * 0 优先模式；1 贪婪模式
     */
    def type

    @Override
    Boolean execute(Map<String, Object> env) {
        println("--------execute ruleSet:$this")
        if(0 == type){
            //优先级模式命中一个规则即跳出，不再执行其他规则
            ruleMap.each {rule ->
                if(rule.value.execute(env)){
                    this.hit = true
                    return true
                }
            }
        } else {
            //贪婪模式全模式执行
            ruleMap.each { rule ->
                if(rule.value.execute(env) && !this.hasHit()){
                    this.hit = true
                }
            }
        }
        return this.hit
    }
}
