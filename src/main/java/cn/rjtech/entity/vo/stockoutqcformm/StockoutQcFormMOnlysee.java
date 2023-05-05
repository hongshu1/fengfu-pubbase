package cn.rjtech.entity.vo.stockoutqcformm;

import java.io.Serializable;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;

import cn.rjtech.model.momdata.StockoutQcFormM;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/4/27 14:41
 * @Description 点击查看按钮，跳转到“查看”页面（该页面只能查看，不能编辑）
 */
public class StockoutQcFormMOnlysee implements Serializable {

    private StockoutQcFormM stockoutQcFormM;

    private Record       record;

    private List<Record> stockoutqcformlist;

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

    public List<Record> getStockoutqcformlist() {
        return stockoutqcformlist;
    }

    public void setStockoutqcformlist(List<Record> stockoutqcformlist) {
        this.stockoutqcformlist = stockoutqcformlist;
    }
}
