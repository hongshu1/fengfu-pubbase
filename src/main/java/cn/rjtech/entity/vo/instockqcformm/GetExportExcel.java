package cn.rjtech.entity.vo.instockqcformm;

import java.io.Serializable;

import com.jfinal.kit.Kv;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/5/8 15:56
 * @Description 导出数据详情
 */
public class GetExportExcel implements Serializable {

    public Kv kv;

    public Kv getKv() {
        return kv;
    }

    public void setKv(Kv kv) {
        this.kv = kv;
    }
}
