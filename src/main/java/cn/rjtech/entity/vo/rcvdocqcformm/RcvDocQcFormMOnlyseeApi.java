package cn.rjtech.entity.vo.rcvdocqcformm;

import java.io.Serializable;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;

import cn.rjtech.model.momdata.RcvDocQcFormM;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/4/28 10:40
 * @Description 点击查看按钮，跳转到“查看”页面（该页面只能查看，不能编辑）
 */
public class RcvDocQcFormMOnlyseeApi implements Serializable {

    private RcvDocQcFormM rcvDocQcFormM;

    private Record record;

    private List<Record> docparamlist;

    public RcvDocQcFormM getRcvDocQcFormM() {
        return rcvDocQcFormM;
    }

    public void setRcvDocQcFormM(RcvDocQcFormM rcvDocQcFormM) {
        this.rcvDocQcFormM = rcvDocQcFormM;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public List<Record> getDocparamlist() {
        return docparamlist;
    }

    public void setDocparamlist(List<Record> docparamlist) {
        this.docparamlist = docparamlist;
    }
}
