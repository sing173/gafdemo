package gafdemo.pojo;


import groovy.transform.ToString;

import java.sql.Timestamp;

/**
 * Created by luomingxing on 2019/10/8.
 */
public class Order {
    // 主键id
    private Integer id;
    // 版本
    private Integer version;
    private Timestamp mdTime;

    public Order(int id, Integer version) {
        this.id = id;
        this.version = version;
        this.mdTime = new Timestamp(System.currentTimeMillis());
    }

    public Order() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Timestamp getMdTime() {
        return mdTime;
    }

    public void setMdTime(Timestamp mdTime) {
        this.mdTime = mdTime;
    }

    @Override
    public String toString() {
        return "id:"+this.id +" ,version:"+version+" ,mdTime:"+mdTime;
    }
}
