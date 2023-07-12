package cn.rjtech.admin.mergebarcode;

import cn.hutool.http.HttpUtil;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.util.JBoltDateUtil;
import cn.rjtech.admin.userthirdparty.UserThirdpartyService;
import cn.rjtech.config.AppConfig;
import cn.rjtech.erp.mood.CollectorsUtil;
import cn.rjtech.model.momdata.Inventory;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 合并条码 Service
 *
 * @author: 佛山市瑞杰科技有限公司
 */
public class MergeBarCodeService extends BaseService<Inventory> {

    private final Inventory dao = new Inventory().dao();

    @Override
    protected Inventory dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return 0;
    }

    @Inject
    UserThirdpartyService userThirdpartyService;

    /**
     * 合并条码实物编码选择选择
     */
    public Page<Record> StripSelectDatas(Integer pageNumber, Integer pageSize, Kv kv) {
        kv.set("organizecode", getOrgCode());
        Page<Record> paginate = dbTemplate("mergebarcode.barcodeSelectDatas", kv).paginate(pageNumber, pageSize);
        return paginate;
    }


    public String SubmitStripForm(Kv kv) {
        //获取表头数据
        String datas = kv.getStr("datas");
        List<Kv> jsonArray = JSON.parseArray(datas, Kv.class);
        String organizecode = getOrgCode();
        String u8Name = userThirdpartyService.getU8Name(JBoltUserKit.getUserId());
        Date now = new Date();
        String loginDate = JBoltDateUtil.format(now, "yyyy-MM-dd");
        Kv target = new Kv();
        target.set("organizeCode", organizecode);
        target.set("userCode", u8Name);

        Kv PreAllocate = new Kv();
        PreAllocate.set("usercode", u8Name);
        PreAllocate.set("organizeCode", organizecode);
        PreAllocate.set("type", "BarcodeMerge");
        PreAllocate.set("loginDate", loginDate);

        target.set("PreAllocate", PreAllocate);

        List<Map> MainData = new ArrayList<>();
        // 获取传过来的料号去重后的集合，判断是否存在不一致的情况
        List<String> invCodes = jsonArray.stream().map(m -> m.getStr("cinvcode")).distinct().collect(Collectors.toList());
        // 判断料号是否不一致,重复提醒重新选择
        if (invCodes.size() > 1) {
            throw new RuntimeException("料号不一致，请重新选择！");
        }
        // 按实物编码前缀一致为一组，先特殊处理实物编码，将实物编码后面的“H202302280001-01”的数据切割成H202302280001
        jsonArray.stream().forEach(f -> {
            String barcode = f.getStr("cbarcode").split("-")[0];
            f.set("cbarcode", barcode);
        });
        // 处理完成实物编码后，进行分组排序
        Map<String, List<Kv>> groupMap = jsonArray.stream()
            .collect(Collectors.groupingBy(g -> g.getStr("cbarcode"), LinkedHashMap::new, Collectors.toList()));
        BigDecimal totalNum = jsonArray.stream().collect(CollectorsUtil.summingBigDecimal(s -> new BigDecimal(s.getStr("qty"))));
        boolean isFisrt = true;
        Kv items = jsonArray.get(0);// 获取到第一条数
        for (String key : groupMap.keySet()) {
            // int num=0;
            List<Kv> list = groupMap.get(key);
            for (int i = 0; i < list.size(); i++) {
                Kv item = list.get(i);
                // num+=item.getInt("number");//总数
                if (i == list.size() - 1) {// 最后一条时候，要创建对象赋值给接口组装数据
                    // String invCode=item.getStr("invCode");
                    Kv mian = new Kv();
                    if (isFisrt) {
                        mian.set("qty", totalNum);
                        isFisrt = false;
                    } else {
                        mian.set("qty", 0);
                    }

                    mian.set("barcode", item.getStr("cbarcode"));// 实物编码
                    mian.set("invcode", item.getStr("cinvcode"));
                    mian.set("invname", item.getStr("cinvname"));
                    mian.set("pqty", item.getStr("qty"));
                    mian.set("Tag", "BarcodeMerge");
                    mian.set("PBarCode", items.getStr("cbarcode"));// 第一条的实物编码
                    MainData.add(mian);
                }
            }
        }
        target.set("MainData", MainData);
        System.out.println(JSON.toJSONString(target) + 666);
        return base_inBarCode(JSON.toJSONString(target));
    }


    public String base_inBarCode(String json) {
        String post = HttpUtil.post(AppConfig.getVouchProcessDynamicSubmitUrl(), json);
        JSONObject res = JSON.parseObject(post);
        ValidationUtils.notNull(res, "解析JSON为空");

        String state = res.getString("state");
        String message = res.getString("message");
        if (message == null) {
            message = res.getString("msg");
        }
        ValidationUtils.notNull(state, "json:" + json + ";" + message);
        ValidationUtils.equals(state, "ok", state + ";" + "json:" + json + ";" + message);
        return post;
    }

    public Page<Record> barcodeDatas(Integer pageNumber, Integer pageSize, Kv kv) {
        Page<Record> paginate = dbTemplate("mergebarcode.barcodeDatas", kv).paginate(pageNumber, pageSize);
        return paginate;
    }

    public List<Record> findShiWuByCSourceId(String csourceid) {
        List<Record> list = dbTemplate("mergebarcode.findShiWuByCSourceId", Kv.by("csourceid", csourceid)).find();
        return list;
    }

    /**
     * 合并条码记录
     */
    public Page<Record> datas(Integer pageNumber, Integer pageSize, Kv kv) {
        Page<Record> paginate = dbTemplate("mergebarcode.datas", kv).paginate(pageNumber, pageSize);
        return paginate;
    }

    public Record findByLogId(String logno) {
        return dbTemplate("mergebarcode.findByLogno", Kv.by("logno", logno)).findFirst();
    }

}