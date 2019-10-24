package gafdemo.flink.source;

import gafdemo.groovy.pogo.event.DataSourceEvent;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;

import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 *
 * @author luomingxing
 */
public class DemoEventSource extends RichParallelSourceFunction<DataSourceEvent> {

    private boolean running = true;
    private Random random = new Random();

    @Override
    public void run(SourceContext<DataSourceEvent> sourceContext) throws Exception {
        while (running) {
            TimeUnit.MILLISECONDS.sleep(10);
            DataSourceEvent dataSourceEvent = new DataSourceEvent();
            dataSourceEvent.setSeqNo(UUID.randomUUID().toString());
            dataSourceEvent.setEventType("Risk");
            int cardNo = random.nextInt(100);
            dataSourceEvent.setEventTime(System.currentTimeMillis()+"");
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("cardNo", cardNo);
            dataMap.put("trade", random.nextInt(10000));
            dataSourceEvent.setData(dataMap);
            dataSourceEvent.setAviatorEnv("event", dataMap);
            System.out.println("build event -- seqNo:"+dataSourceEvent.getSeqNo()+",cardNo:"+dataMap.get("cardNo")+",trade:"+dataMap.get("trade")+",time:"+dataSourceEvent.getEventTime());
            sourceContext.collect(dataSourceEvent);



        }
    }

    @Override
    public void cancel() {
        running = false;
    }
}
