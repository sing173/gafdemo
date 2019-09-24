package gafdemo.pojo.event;

import gafdemo.enumerate.CepPatternTimesEnum;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by luomingxing on 2019/9/23.
 */
public class CepPattern {
    private String id;
    private String name;
    private String type;
    private double weight;
    private List<String> conditionList;
    private String withinTimeUnit;
    private Integer withinTimeSize;
    private String timesName;
    private Integer timesFrom;
    private Integer timesTo;
    private String until;
    private CepPattern nextPattern;

    public CepPattern(){
    }

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

    public List<String> getConditionList() {
        return conditionList;
    }

    public void setConditionList(List<String> conditionList) {
        this.conditionList = conditionList;
    }

    public String getWithinTimeUnit() {
        return withinTimeUnit;
    }

    public void setWithinTimeUnit(String withinTimeUnit) {
        this.withinTimeUnit = withinTimeUnit;
    }

    public Integer getWithinTimeSize() {
        return withinTimeSize;
    }

    public void setWithinTimeSize(Integer withinTimeSize) {
        this.withinTimeSize = withinTimeSize;
    }

    public String getTimesName() {
        return timesName;
    }

    public void setTimesName(String timesName) {
        this.timesName = timesName;
    }

    public Integer getTimesFrom() {
        return timesFrom;
    }

    public void setTimesFrom(Integer timesFrom) {
        this.timesFrom = timesFrom;
    }

    public Integer getTimesTo() {
        return timesTo;
    }

    public void setTimesTo(Integer timesTo) {
        this.timesTo = timesTo;
    }

    public String getUntil() {
        return until;
    }

    public void setUntil(String until) {
        this.until = until;
    }

    public CepPattern getNextPattern() {
        return nextPattern;
    }

    public void setNextPattern(CepPattern nextPattern) {
        this.nextPattern = nextPattern;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
