package cn.rjtech.entity.vo.workregionm;

import java.io.Serializable;

/**
 * 产线
 *
 *
 */
public class WorkRegionm implements Serializable {

    /**
     * 主键ID
     */
    private Long iautoid;
    /**
     * 产线名称
     */
    private String cworkname;

    public WorkRegionm() {
    }

    public Long getIautoid() {
        return iautoid;
    }

    public void setIautoid(Long iautoid) {
        this.iautoid = iautoid;
    }

    public String getCworkname() {
        return cworkname;
    }

    public void setCworkname(String cworkname) {
        this.cworkname = cworkname;
    }

}
