package gafdemo.pojo.rule;

import java.util.Map;

/**
 * Created by luomingxing on 2019/8/28.
 */
public class BaseRule implements AviEvaluation{
    protected String id;
    protected String name;
    protected String code;
    protected Boolean hit;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getHit() {
        return hit;
    }

    public void setHit(Boolean hit) {
        this.hit = hit;
    }

    @Override
    public Boolean execute(Map<String, Object> env) {
        return null;
    }
}
