package groovy.pogo

/**
 * Created by luomingxing on 2019/9/9.
 */
abstract class BaseRule {
    protected String id
    protected String name
    protected String code
    protected Boolean hit

    String toString() {
        "ID: $id, Name: $name, Code: $code"
    }

    abstract def execute(Map<String, Object> env)
}
