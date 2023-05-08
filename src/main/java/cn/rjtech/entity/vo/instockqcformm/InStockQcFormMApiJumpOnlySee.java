package cn.rjtech.entity.vo.instockqcformm;

import java.io.Serializable;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;

import cn.rjtech.model.momdata.InStockQcFormM;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/5/5 10:38
 * @Description 跳转到onlysee页面
 */
public class InStockQcFormMApiJumpOnlySee implements Serializable {

    private InStockQcFormM inStockQcFormM;

    private Record record;

    private List<Record> stockoutqcformlist;

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

    public List<Record> getStockoutqcformlist() {
        return stockoutqcformlist;
    }

    public void setStockoutqcformlist(List<Record> stockoutqcformlist) {
        this.stockoutqcformlist = stockoutqcformlist;
    }
}
