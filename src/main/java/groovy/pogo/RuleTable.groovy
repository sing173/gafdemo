package groovy.pogo

/**
 * Created by luomingxing on 2019/9/10.
 */
class RuleTable extends BaseRule{
    def rowItemMap = [:]
    def columnItemMap = [:]
    def assignItemList = []

    /**
     * 0 简单决策表；1 复杂决策表
     */
    def type

    @Override
    def execute(Map<String, Object> env) {
        //TODO 根据表类型执行决策

        this.hit
    }
}
