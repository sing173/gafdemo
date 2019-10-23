package gafdemo.groovy.pogo.event

class CepEventResult implements Serializable{
    String seqNo
    String hitPattern

    /**
     * 命中的模式链
     */
    CepPatternGroovy patternGroovy

    /**
     * 触发事件的最后一个请求，即当前请求
     */
    DataSourceEvent currentDataSourceEvent

    /**
     * 命中后的输出变量
     */
    Map<String, Object> result = [:]

    /**
     * 模式链下每个模式命中的事件集合，key为模式名称
     */
    Map<String, List<DataSourceEvent>> hitPatternEventMap = [:]

    @Override
    String toString() {
        if(hitPatternEventMap != null && hitPatternEventMap.size() > 0) {
            def allOutPut = ""
            hitPatternEventMap.each {map ->
                def outPut
                def patternName = map.key
                if(outPut == null) {
                    outPut = "hitPatternName:$patternName------\n"
                } else {
                    outPut += "\nhitPatternName:$patternName------\n"
                }
                allOutPut += outPut
                List<DataSourceEvent> dataSourceEventList = map.value
                dataSourceEventList.each {dataSourceEvent ->
                    allOutPut += "$dataSourceEvent\n"
                }
            }
            return allOutPut
        } else {
            return currentDataSourceEvent
        }

    }

}
