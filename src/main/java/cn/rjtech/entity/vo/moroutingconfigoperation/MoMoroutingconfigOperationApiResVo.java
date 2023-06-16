package cn.rjtech.entity.vo.moroutingconfigoperation;

import java.io.Serializable;

/**
 * @ClassName :
 * @Description : 工单工序结果集
 * @Author : dongjunjun
 * @Date: 2023-06-16
 */
public class MoMoroutingconfigOperationApiResVo implements Serializable {
    private Long ioperationid;
    private  String coperationname;

    public Long getIoperationid() {
        return ioperationid;
    }

    public void setIoperationid(Long ioperationid) {
        this.ioperationid = ioperationid;
    }

    public String getCoperationname() {
        return coperationname;
    }

    public void setCoperationname(String coperationname) {
        this.coperationname = coperationname;
    }
}
