package cn.rjtech.admin.schedudemandplan;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BOMCompareDTO implements Serializable {
    //当前料号的母件id
    private Long iBOMMasterId;
    //料号id
    private Long iItemId;
    //用量比
    private BigDecimal iUsageUOM;
    //提前期
    private Integer cProdAdvance;
    //当前物料作为母件时的id集
    private String iBOMMasterIdListStr;
    //当前物料的子件物料集合
    private List<BOMCompareDTO> childrenList = new ArrayList<>();

    public List<BOMCompareDTO> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<BOMCompareDTO> childrenList) {
        this.childrenList = childrenList;
    }

    public Long getiBOMMasterId() {
        return iBOMMasterId;
    }

    public void setiBOMMasterId(Long iBOMMasterId) {
        this.iBOMMasterId = iBOMMasterId;
    }

    public Long getiItemId() {
        return iItemId;
    }

    public void setiItemId(Long iItemId) {
        this.iItemId = iItemId;
    }

    public BigDecimal getiUsageUOM() {
        return iUsageUOM;
    }

    public void setiUsageUOM(BigDecimal iUsageUOM) {
        this.iUsageUOM = iUsageUOM;
    }

    public Integer getcProdAdvance() {
        return cProdAdvance;
    }

    public void setcProdAdvance(Integer cProdAdvance) {
        this.cProdAdvance = cProdAdvance;
    }

    public String getiBOMMasterIdListStr() {
        return iBOMMasterIdListStr;
    }

    public void setiBOMMasterIdListStr(String iBOMMasterIdListStr) {
        this.iBOMMasterIdListStr = iBOMMasterIdListStr;
    }
}
