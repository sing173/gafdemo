package gafdemo.pojo.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luomingxing on 2019/9/20.
 */
public class CepEvent {
    private String id;
    private String name;
    private String type;

    private Map<Double, List<CepPattern>> patternGroupMap = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<Double, List<CepPattern>> getPatternGroupMap() {
        return patternGroupMap;
    }

    public void setPatternGroupMap(Map<Double, List<CepPattern>> patternGroupMap) {
        this.patternGroupMap = patternGroupMap;
    }
}
