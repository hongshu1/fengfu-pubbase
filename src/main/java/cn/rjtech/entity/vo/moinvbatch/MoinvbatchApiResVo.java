package cn.rjtech.entity.vo.moinvbatch;

import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 现品票API接口返回数据
 */
public class MoinvbatchApiResVo {
    /**
     * 工单信息
     */
    Record moDoc;
    /**
     * 现品票
     */
    List<Record> moMoinvbatchs;

    public Record getMoDoc() {
        return moDoc;
    }

    public void setMoDoc(Record moDoc) {
        this.moDoc = moDoc;
    }

    public List<Record> getMoMoinvbatchs() {
        return moMoinvbatchs;
    }

    public void setMoMoinvbatchs(List<Record> moMoinvbatchs) {
        this.moMoinvbatchs = moMoinvbatchs;
    }
}
