package cn.rjtech.entity.vo.stockoutqcformm;

import java.io.Serializable;

import com.jfinal.plugin.activerecord.Record;

import cn.rjtech.model.momdata.StockoutQcFormM;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/4/27 11:38
 * @Description 跳转到检验页面
 */
public class StockoutQcFormMCheckout implements Serializable {

    private StockoutQcFormM stockoutQcFormM;

    private Record record;

    public StockoutQcFormM getStockoutQcFormM() {
        return stockoutQcFormM;
    }

    public void setStockoutQcFormM(StockoutQcFormM stockoutQcFormM) {
        this.stockoutQcFormM = stockoutQcFormM;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }
}
