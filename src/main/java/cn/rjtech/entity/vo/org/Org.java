package cn.rjtech.entity.vo.org;

import java.io.Serializable;

/**
 * 组织
 *
 * @author Kephon
 */
public class Org implements Serializable {

    /**
     * 组织ID
     */
    private String id;
    /**
     * 组织编码
     */
    private String code;
    /**
     * 组织名称
     */
    private String name;

    public Org() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Org{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
