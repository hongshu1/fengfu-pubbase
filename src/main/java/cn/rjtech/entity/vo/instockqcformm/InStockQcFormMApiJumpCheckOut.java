package cn.rjtech.entity.vo.instockqcformm;

import java.io.Serializable;

import com.jfinal.plugin.activerecord.Record;

import cn.rjtech.model.momdata.InStockQcFormM;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/5/5 9:42
 * @Description 跳转到检验页面
 */
public class InStockQcFormMApiJumpCheckOut implements Serializable {

    private InStockQcFormM inStockQcFormM;

    private Record record;

    public InStockQcFormM getInStockQcFormM() {
        return inStockQcFormM;
    }

    public void setInStockQcFormM(InStockQcFormM inStockQcFormM) {
        this.inStockQcFormM = inStockQcFormM;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }
}
