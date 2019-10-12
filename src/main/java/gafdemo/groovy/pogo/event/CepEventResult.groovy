package gafdemo.groovy.pogo.event

class CepEventResult implements Serializable{
    String seqNo
    String hitPattern

    /**
     * 命中的模式链
     */
    CepPatternGroovy patternGroovy

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
        def outPut = ""
        hitPatternEventMap.each {map ->
            def patternName = map.key
            outPut += "\npatternName:$patternName------\n"
            List<DataSourceEvent> dataSourceEventList = map.value
            dataSourceEventList.each {dataSourceEvent ->
                def seqNo = dataSourceEvent.seqNo
                outPut += "\nevent seqNo:$seqNo"
            }
        }

//        return "current seqNo:$seqNo,hitPattern:$hitPattern\n $outPut"
        return outPut
    }

}
