package cn.rjtech.admin.splitbarcode;

import cn.hutool.http.HttpUtil;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.util.JBoltDateUtil;
import cn.rjtech.admin.userthirdparty.UserThirdpartyService;
import cn.rjtech.base.service.BaseU9RecordService;
import cn.rjtech.common.organize.OrganizeService;
import cn.rjtech.config.AppConfig;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.ParameterException;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 拆分条码 Service
 *
 * @author: 佛山市瑞杰科技有限公司
 */
public class SplitBarCodeService extends BaseU9RecordService {
    @Inject
    OrganizeService organizeService;
    @Inject
    UserThirdpartyService userThirdpartyService;

    @Override
    protected String getTableName() {
        return "";
    }

    @Override
    protected String getPrimaryKey() {
        return null;
    }

    /**
     * 拆框载具选择
     */
    public Page<Record> BarCodeSelectDatas(Integer pageNumber, Integer pageSize, Kv kv) {
        kv.set("organizecode", getOrgId());
        Page<Record> paginate = dbTemplate("barcode.BarCodeSelectDatas", kv).paginate(pageNumber, pageSize);
        return paginate;
    }


    public String SubmitForm(Kv kv) {
        String datas = kv.getStr("datas");
        String invcodes = kv.getStr("invcodes");
        String barcodes = kv.getStr("barcodes");
        // int qtys = kv.getInt("qtys");
        BigDecimal qtys = kv.getBigDecimal("qtys");
        String invnames = kv.getStr("invnames");
        List<Kv> jsonArray = JSON.parseArray(datas, Kv.class);
        String organizecode = organizeService.getOrganizecode();
        String u9Name = userThirdpartyService.getU9Name(JBoltUserKit.getUserId());
        Date now = new Date();
        String loginDate = JBoltDateUtil.format(now, "yyyy-MM-dd");
        Kv target = new Kv();
        target.set("organizeCode", organizecode);
        target.set("userCode", u9Name);


        Kv PreAllocate = new Kv();
        PreAllocate.set("usercode", u9Name);
        PreAllocate.set("organizeCode", organizecode);
        PreAllocate.set("type", "BarcodeSplit");
        PreAllocate.set("loginDate", loginDate);

        target.set("PreAllocate", PreAllocate);
        List<Map> MainData = new ArrayList<>();
        int num = 0;

        for (int i = 0; i < jsonArray.size(); i++) {
            Kv item = jsonArray.get(i);
            num += item.getInt("number");
            Kv mian = new Kv();
            mian.set("qty", item.getStr("number"));
            String str = "";
            if (i > 9) {
                str += (i + 1);
            } else {
                str = str + "0" + (i + 1);
            }
            mian.set("shiwu", barcodes);
            mian.set("liaohao", invcodes);
            mian.set("pinmin", invnames);
            mian.set("invcode", invcodes);
            mian.set("invname", invnames);
            mian.set("billno", barcodes);
            mian.set("index", str);
            mian.set("billrowno", str);
            mian.set("shiwuRowNo", barcodes + "-" + str);
            mian.set("Tag", "BarcodeSplit");

            MainData.add(mian);

        }
        ValidationUtils.notNull(qtys, "条码数量为空！");
        if (qtys.intValue() < num) {
            // if(qtys<num){
            throw new ParameterException(num + ";该拆分数量已经大于条码数量！");
        }
        target.set("MainData", MainData);

        System.out.println(JSON.toJSONString(target) + 666);
        return base_inBarCode(JSON.toJSONString(target));
    }


    public String base_inBarCode(String json) {
        String post = HttpUtil.post(AppConfig.getVouchSumbmitUrl(), json);
        JSONObject res = JSON.parseObject(post);
        ValidationUtils.notNull(res, "解析JSON为空");

        String code = res.getString("code");
        String message = res.getString("message");
        if (message == null) {
            message = res.getString("msg");
        }

        ValidationUtils.notNull(code, "json:" + json + ";" + message);
        ValidationUtils.equals(code, "200", code + ";" + "json:" + json + ";" + message);
        return post;
    }

    public Page<Record> barcodeDatas(Integer pageNumber, Integer pageSize, Kv kv) {
        String organizecode = organizeService.getOrganizecode();
        kv.set("organizecode", organizecode);
        Page<Record> paginate = dbTemplate("barcode.barcodeDatas", kv).paginate(pageNumber, pageSize);
        return paginate;
    }

    public Record findByShiWu(String autoid) {
        return dbTemplate("barcode.findByShiWu", Kv.by("autoid", autoid)).findFirst();
    }

    public List<Record> findListBylogno(String logno) {
        List<Record> list = dbTemplate("barcode.findByListlogno", Kv.by("logno", logno)).find();
        return list;
    }
}