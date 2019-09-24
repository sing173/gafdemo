package gafdemo.groovy.pogo.rule

/**
 * Created by luomingxing on 2019/9/9.
 */
abstract class BaseRule {
    protected String id
    protected String name
    protected String code
    protected Boolean hit

    def hasHit(){
        return null != hit && hit
    }

    String toString() {
        "ID: $id, Name: $name, Code: $code"
    }

    abstract Boolean execute(Map<String, Object> env)
}
