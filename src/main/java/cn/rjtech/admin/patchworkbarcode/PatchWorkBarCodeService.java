package cn.rjtech.admin.patchworkbarcode;

import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.Inventory;
import com.alibaba.fastjson.JSON;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.List;

public class PatchWorkBarCodeService extends BaseService<Inventory> {

    private final Inventory dao = new Inventory().dao();

    @Override
    protected Inventory dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /*
     * 加载打印的现品票数据
     * */
    public List<Record> datas(String cinvcode) {
        List<Record> recordList = dbTemplate("patchworkbarcode.datas", Kv.by("cinvcode", cinvcode)).find();
        return recordList;
    }

    /**
     * 选择补打条码
     */
    public Page<Record> selectDatas(Integer pageNumber, Integer pageSize, Kv kv) {
        kv.set("organizecode", getOrgCode());
        Page<Record> paginate = dbTemplate("patchworkbarcode.barcodeSelectDatas", kv).paginate(pageNumber, pageSize);
        return paginate;
    }

    /*
     * 打印条码
     * */
    public List<Record> SubmitStripForm(Kv kv) {
        //TODO 打印条码
        String cinvcode = kv.getStr("cinvcode");//存货编码
        String cinvcode1 = kv.getStr("cinvcode1"); //客户部番
        String cinvname1 = kv.getStr("cinvname1"); //部品名称
        String datas = kv.getStr("datas"); //现品票
        List<Kv> jsonArray = JSON.parseArray(datas, Kv.class);
        ArrayList<Record> records = new ArrayList<>();
        for (Kv value : jsonArray) {
            Record record = new Record();
            record.set("cbarcode",value.getStr("cbarcode"));
            record.set("iqty",value.getStr("iqty"));
            //添加
            records.add(record);
        }
        return records;
    }

}