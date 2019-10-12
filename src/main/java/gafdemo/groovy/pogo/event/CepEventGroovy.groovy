package gafdemo.groovy.pogo.event

/**
 * Created by luomingxing on 2019/9/25.
 */
class CepEventGroovy implements Serializable{
    String id
    String name
    String type
    String keyBy

    /**
     * 该事件下的所有模式链，key为模式链名称
     */
    Map<String, CepPatternGroovy> patternMap = [:]

}
