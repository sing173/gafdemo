package gafdemo.groovy.pogo.event


/**
 * Created by luomingxing on 2019/9/25.
 */
class CepEventGroovy extends DataSourceEvent{
    String id
    String name
    String type
    String keyBy

    Map<String, CepPatternGroovy> patternMap = [:]

}
