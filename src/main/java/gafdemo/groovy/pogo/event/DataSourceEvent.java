package gafdemo.groovy.pogo.event;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luomingxing on 2019/9/24.
 */
public class DataSourceEvent implements Serializable {
    private String seqNo;
    private String eventType;
    private String eventTime;
    private long statusCode;

    private Map<String, Object> data;
    private Map<String, Object> result;

    private Map<String, Object> aviatorEnv = new HashMap<>();

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public long getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(long statusCode) {
        this.statusCode = statusCode;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;

    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public void setAviatorEnv(Map<String, Object> data){
        Map<String, Object> aviatorEnvEvent = new HashMap<>(data);
        aviatorEnv.put("event", aviatorEnvEvent);
    }

    public Map<String, Object> getAviatorEnv(){
        return this.aviatorEnv;
    }

    @Override
    public String toString() {
        return "DataSourceEvent {"
                +"seqNo='" + seqNo
                +"',time='" + getEventTime()
                +"',data=" + data;
    }
}
