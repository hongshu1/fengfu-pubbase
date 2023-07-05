package cn.rjtech.entity.vo.spotcheckformm;

import java.util.List;

public class SubmitFormVo {

    /**
     * form提交数据
     */
    private FormJsonData formJsonData;

    /**
     * 表格内容
     */
    private List<TableJsonData> tableJsonData;

    public FormJsonData getFormJsonDataVo(){
        return formJsonData;
    }
    public void setFormJsonDataVo(FormJsonData formJsonDataVo){
        this.formJsonData=formJsonDataVo;
    }
    public  void setTableJsonData(List<TableJsonData> tableJsonData){
        this.tableJsonData=tableJsonData;
    }
    public List<TableJsonData> getTableJsonData(){
        return tableJsonData;
    }

}
