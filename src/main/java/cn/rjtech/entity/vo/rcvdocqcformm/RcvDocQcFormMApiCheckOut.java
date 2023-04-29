package cn.rjtech.entity.vo.rcvdocqcformm;

import java.io.Serializable;

import com.jfinal.plugin.activerecord.Record;

import cn.rjtech.model.momdata.RcvDocQcFormM;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/4/28 9:20
 * @Description 点击检验按钮，跳转到检验页面
 */
public class RcvDocQcFormMApiCheckOut implements Serializable {

    public RcvDocQcFormM rcvDocQcFormM;

    public Record        record;

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
}
