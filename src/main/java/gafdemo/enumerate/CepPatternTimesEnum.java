package gafdemo.enumerate;


import org.apache.flink.cep.pattern.Pattern;

/**
 *
 * @author luomingxing
 * @date 2019/9/23
 */
public enum CepPatternTimesEnum {
    /**
     *
     */
    oneOrMore,
    times,
    timesRange,
    timesOrMore;

    CepPatternTimesEnum(){

    }
}
