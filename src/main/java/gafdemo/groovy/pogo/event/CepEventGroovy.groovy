package gafdemo.groovy.pogo.event


import org.apache.flink.streaming.api.datastream.DataStream


/**
 * Created by luomingxing on 2019/9/25.
 */
class CepEventGroovy {
    String id
    String name
    String type
    String keyBy

    /**
     * 该事件下的所有匹配模式，key为模式名称
     */
    Map<String, CepPatternGroovy> patternMap = [:]

    /**
     * 模式匹配后的结果，key为模式名称
     */
    Map<String, DataStream<CepEventResult>> dataStreamMap = [:]

}
