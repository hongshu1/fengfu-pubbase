package cn.rjtech.entity.vo.prodformm;


import java.io.Serializable;
import java.util.List;

public class ProdFormMResVo implements Serializable {

    /**
     * 动态表格标题
     */
    private List<ProdFormMFormItemVo> formItemLists;

    /**
     * 动态表格内容
     */
    private  List<ProdformDetailVo> findByIdGetDetail;

    public List<ProdFormMFormItemVo> getFormItemLists(){
        return  formItemLists;
    }
    public void  setFormItemLists(List<ProdFormMFormItemVo> formItemLists){
        this.formItemLists=formItemLists;
    }

    public List<ProdformDetailVo> getFindByIdGetDetail(){
        return  findByIdGetDetail;
    }
    public void setFindByIdGetDetail(List<ProdformDetailVo> findByIdGetDetail){
        this.findByIdGetDetail=findByIdGetDetail;
    }

}

