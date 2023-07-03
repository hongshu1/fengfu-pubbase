package cn.rjtech.entity.vo.prodformm;


import com.jfinal.plugin.activerecord.Record;

import java.util.List;

public class ProdFormMResVo {

    /**
     * 动态表格标题
     */
    private List<Record> formItemLists;

    /**
     * 动态表格内容
     */
    private  List<Record> findByIdGetDetail;

    public List<Record> getFormItemLists(){
        return  formItemLists;
    }
    public void  setFormItemLists(List<Record> formItemLists){
        this.formItemLists=formItemLists;
    }

    public List<Record> getFindByIdGetDetail(){
        return  findByIdGetDetail;
    }
    public void setFindByIdGetDetail(List<Record> findByIdGetDetail){
        this.findByIdGetDetail=findByIdGetDetail;
    }

}

