package gafdemo.flink.source;

import gafdemo.groovy.pogo.event.DataSourceEvent;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;


/**
 * @author luomingxing
 */
public class MonitoringEventSource extends RichParallelSourceFunction<DataSourceEvent> {

    private boolean running = true;

    public MonitoringEventSource(){

    }

    @Override
    public void open(Configuration configuration) {

    }

    @Override
    public void run(SourceContext<DataSourceEvent> sourceContext) throws Exception {
        while (running) {
            DataSourceEvent monitoringEvent = null;
            sourceContext.collect(monitoringEvent);

            Thread.sleep(1000);
        }
    }

    @Override
    public void cancel() {
        running = false;
    }
}
