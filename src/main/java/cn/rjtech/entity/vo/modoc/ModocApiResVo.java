package cn.rjtech.entity.vo.modoc;

import com.jfinal.plugin.activerecord.Record;

import java.util.HashMap;
import java.util.List;

/**
 * @ClassName :
 * @Description :
 * @Author : dongjunjun
 * @Date: 2023-06-15
 */
public class ModocApiResVo {
    /**
     * 工单信息
     */
    Record doc;
    /**
     * 生产任务
     */
    List<Record> job;

    public Record getDoc() {
        return doc;
    }

    public void setDoc(Record doc) {
        this.doc = doc;
    }

    public List<Record> getJob() {
        return job;
    }

    public void setJob(List<Record> job) {
        this.job = job;
    }
}
