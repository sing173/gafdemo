package gafdemo.pojo;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by luomingxing on 2019/10/8.
 */
public class OrderSource implements SourceFunction<Order> {
    Random random = new Random();
    private int index = 0;

    public OrderSource(int index) {
        this.index = index;
    }

    @Override
    public void run(SourceFunction.SourceContext<Order> ctx) throws Exception {
        while (true) {
            TimeUnit.MILLISECONDS.sleep(10);
            // 为了区分，我们简单生0~2的id, 和版本0~99
            int id = random.nextInt(1);
            Order o = new Order(id, random.nextInt(100));
            System.out.println(index+ " build order:"+o.toString());
            ctx.collect(o);
        }
    }
    @Override
    public void cancel() {

    }
}
