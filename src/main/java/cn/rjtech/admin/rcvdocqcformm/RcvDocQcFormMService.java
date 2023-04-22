package cn.rjtech.admin.rcvdocqcformm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.omg.PortableInterceptor.SUCCESSFUL;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.jfinal.aop.Inject;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.Page;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.para.JBoltPara;
import cn.jbolt.core.util.JBoltRealUrlUtil;
import cn.jbolt.extend.config.ExtendUploadFolder;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;

import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.sun.corba.se.spi.ior.ObjectKey;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.admin.rcvdocqcformd.RcvDocQcFormDService;
import cn.rjtech.admin.rcvdocqcformdline.RcvdocqcformdLineService;
import cn.rjtech.model.momdata.RcvDocQcFormD;
import cn.rjtech.model.momdata.RcvDocQcFormM;
import cn.rjtech.model.momdata.RcvdocqcformdLine;

/**
 * 质量管理-来料检
 *
 * @ClassName: RcvDocQcFormMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-11 11:44
 */
public class RcvDocQcFormMService extends BaseService<RcvDocQcFormM> {

    private final RcvDocQcFormM dao = new RcvDocQcFormM().dao();

    @Inject
    private RcvDocQcFormDService     rcvDocQcFormDService; //质量管理-来料检单行配置表
    @Inject
    private RcvdocqcformdLineService rcvdocqcformdLineService; //质量管理-来料检明细列值表

    @Override
    protected RcvDocQcFormM dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber  第几页
     * @param pageSize    每页几条数据
     * @param keywords    关键词
     * @param isCompleted 是否已完成：0. 未完成 1. 已完成
     * @param isCpkSigned 是否CPK数值的输入内容按正负数显示：0. 否 1. 是
     * @param isOk        是否判定合格：0. 否 1. 是
     * @param IsDeleted   删除状态：0. 未删除 1. 已删除
     */
    public Page<RcvDocQcFormM> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isCompleted,
                                             Boolean isCpkSigned, Boolean isOk, Boolean IsDeleted) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eqBooleanToChar("isCompleted", isCompleted);
        sql.eqBooleanToChar("isCpkSigned", isCpkSigned);
        sql.eqBooleanToChar("isOk", isOk);
        sql.eqBooleanToChar("IsDeleted", IsDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords, "cOrgName", "cCreateName", "cUpdateName");
        //排序
        sql.desc("iAutoId");
        return paginate(sql);
    }

    public Page<Record> pageList(Kv kv) {
        return dbTemplate("rcvdocqcformm.list", kv).paginate(kv.getInt("page"), kv.getInt("pageSize"));
    }

    /*
     * 点击检验时，进入弹窗自动加载table的数据
     * */
    public List<Record> getCheckOutTableDatas(Kv kv) {
        List<Record> recordList = dbTemplate("rcvdocqcformm.findChecoutListByIformParamid", kv).find();
        for (Record record : recordList) {
            record.set("istdval",record.getBigDecimal("istdval").stripTrailingZeros().toPlainString());
            record.set("imaxval",record.getBigDecimal("imaxval").stripTrailingZeros().toPlainString());
            record.set("iminval",record.getBigDecimal("iminval").stripTrailingZeros().toPlainString());
        }
        return recordList;
    }

    /**
     * 保存
     */
    public Ret save(RcvDocQcFormM rcvDocQcFormM) {
        if (rcvDocQcFormM == null || isOk(rcvDocQcFormM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(rcvDocQcFormM.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = rcvDocQcFormM.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(rcvDocQcFormM.getIAutoId(), JBoltUserKit.getUserId(), rcvDocQcFormM.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(RcvDocQcFormM rcvDocQcFormM) {
        if (rcvDocQcFormM == null || notOk(rcvDocQcFormM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        RcvDocQcFormM dbRcvDocQcFormM = findById(rcvDocQcFormM.getIAutoId());
        if (dbRcvDocQcFormM == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        boolean success = rcvDocQcFormM.update();
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param rcvDocQcFormM 要删除的model
     * @param kv            携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(RcvDocQcFormM rcvDocQcFormM, Kv kv) {
        //addDeleteSystemLog(rcvDocQcFormM.getIAutoId(), JBoltUserKit.getUserId(),rcvDocQcFormM.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param rcvDocQcFormM model
     * @param kv            携带额外参数一般用不上
     */
    @Override
    public String checkInUse(RcvDocQcFormM rcvDocQcFormM, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(RcvDocQcFormM rcvDocQcFormM, String column, Kv kv) {
        //addUpdateSystemLog(rcvDocQcFormM.getIAutoId(), JBoltUserKit.getUserId(), rcvDocQcFormM.getName(),"的字段["+column+"]值:"+rcvDocQcFormM.get(column));
        /**
         switch(column){
         case "isCompleted":
         break;
         case "isCpkSigned":
         break;
         case "isOk":
         break;
         }
         */
        return null;
    }

    /**
     * 上传图片
     */
    public List<String> uploadImage(List<UploadFile> files) {
        List<String> imgList = new ArrayList<>();
        if (ObjectUtil.isEmpty(files)) {
            return imgList;
        }
        for (UploadFile uploadFile : files) {
            String localUrl = FileUtil.normalize(JBoltRealUrlUtil.get(JFinal.me().getConstants().getBaseUploadPath() + '/'
                + uploadFile.getUploadPath() + '/' + uploadFile.getFileName()));
            imgList.add(localUrl);
        }
        return imgList;
    }

    /*
     * 更新
     * */
    public Ret editTable(JBoltPara JboltPara) {
        if (JboltPara == null || JboltPara.isEmpty()) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        String cmeasurereason = JboltPara.getString("cmeasurereason"); //测定理由
        String cmeasurepurpose = JboltPara.getString("cmeasurepurpose"); //测定目的
        String cmeasureunit = JboltPara.getString("cmeasureunit"); //测定单位
        String cmemo = JboltPara.getString("cmemo"); //备注
        String isok = JboltPara.getString("isok"); //是否合格
        String cdcno = JboltPara.getString("cdcno"); //设变号
        JSONArray serializeSubmitList = JboltPara.getJSONArray("serializeSubmitList");
        String[] serializeSubmitLists = JboltPara.getStringToArray("serializeSubmitList");
        for (int i=0;i<serializeSubmitList.size();i++){
            JSONObject jsonObject = serializeSubmitList.getJSONObject(i);
            String iqcformid = jsonObject.getString("iqcformid");
            String ircvdocqcformmid = jsonObject.getString("ircvdocqcformmid");
            String iautoid = jsonObject.getString("iautoid");
            String coptions = jsonObject.getString("coptions");
            JSONArray serializeElement = jsonObject.getJSONArray("serializeElement");
            Param param = new Gson().fromJson(jsonObject, Param.class);
            System.out.println(param);
        }

        //tableDataList转为map
        /*Map<String, Object> innerMap = tableDataList.getInnerMap();
        //对innerMap排序
        Map<String, Object> sortMap = sortMapByKey(innerMap);
        sortMap.forEach((key, value) -> {
            System.out.println(key);
            String s = value.toString();
            List<String> list = oobjectToList(value, String.class)
                .stream()
                .filter(e -> StringUtils.isNotBlank(e))
                .collect(Collectors.toList());
            System.out.println("list==>" + new Gson().toJson(list));
            //1、将table数据保存在检验表

        });*/
        //2、将输入框的数据保存在【来料检表（PL_RcvDocQcFormM）】
//        RcvDocQcFormM docQcFormM = findById(JboltPara.getString("iautoid"));
//        saveDocQcFormMModel(docQcFormM,JboltPara);

        RcvDocQcFormD rcvDocQcFormD = new RcvDocQcFormD();//质量管理-来料检单行配置表
        RcvdocqcformdLine rcvdocqcformdLine = new RcvdocqcformdLine();//质量管理-来料检明细列值表

        return ret(true);
    }

    public void saveDocQcFormMModel(RcvDocQcFormM docQcFormM, JBoltPara JboltPara) {
        docQcFormM.setCMeasurePurpose(JboltPara.getString("cmeasurepurpose"));//测定目的
        docQcFormM.setCMeasureReason(JboltPara.getString("cmeasurereason"));//测定理由
        docQcFormM.setCMeasureUnit(JboltPara.getString("cmeasureunit")); //测定单位
        docQcFormM.setCMemo(JboltPara.getString("cmemo"));//备注
        docQcFormM.setCDcNo(JboltPara.getString("cdcno")); //设变号
        String isok = JboltPara.getString("isok");
        docQcFormM.setIsOk(isok.equalsIgnoreCase("0") ? false : true);//是否合格
    }

    public static Map<String, Object> sortMapByKey(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, Object> sortMap = new TreeMap<String, Object>(new KeyCompareUtil());
        sortMap.putAll(map);
        return sortMap;
    }

    /**
     * 从小到大排序
     */
    public static class KeyCompareUtil implements Comparator<String> {

        @Override
        public int compare(String s1, String s2) {
            return Integer.valueOf(s1).compareTo(Integer.valueOf(s2));
        }
    }

    /**
     * object 转 list
     *
     * @param obj   需要转换的List对象
     * @param clazz List中元素的class
     */
    public static <T> List<T> oobjectToList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        // 判断 obj 是否包含 List 类型
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                // 使用Class.cast做类型转换
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

    /*
     * 根据表格ID生成table
     * */
    public Ret createTable(JBoltPara jBoltPara) {
        Long iautoid = jBoltPara.getLong("iautoid");
        RcvDocQcFormM rcvDocQcFormM = findById(iautoid);
        if (null == rcvDocQcFormM) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //1、根据表格ID查询数据
        Long iQcFormId = rcvDocQcFormM.getIQcFormId();//表格ID
        List<Record> recordList = dbTemplate("rcvdocqcformm.getCheckoutList", Kv.by("iqcformid", iQcFormId)).find();
        ArrayList<RcvDocQcFormD> rcvDocQcFormDS = new ArrayList<>();
        for (Record record : recordList) {
            RcvDocQcFormD rcvDocQcFormD = new RcvDocQcFormD();//质量管理-来料检单行配置表
            saveRcvDocQcFormDModel(rcvDocQcFormD, record, iautoid, iQcFormId);
            rcvDocQcFormDS.add(rcvDocQcFormD);
        }
        rcvDocQcFormDService.batchSave(rcvDocQcFormDS);

        //2、更新PL_RcvDocQcFormM检验结果(istatus)为“待检-1”
        rcvDocQcFormM.setIStatus(1);
        Ret ret = update(rcvDocQcFormM);
        if (ret.isFail()) {
            return ret;
        }
        return SUCCESS;
    }

    /*
     * 给质量管理-来料检单行配置传参
     * */
    public void saveRcvDocQcFormDModel(RcvDocQcFormD rcvDocQcFormD, Record record, Long iautoid, Long iQcFormId) {
        rcvDocQcFormD.setIAutoId(JBoltSnowflakeKit.me.nextId());
        rcvDocQcFormD.setIRcvDocQcFormMid(iautoid);//来料检id
        rcvDocQcFormD.setIQcFormId(iQcFormId);//检验表格ID
        rcvDocQcFormD.setIFormParamId(record.getLong("iFormParamId"));//检验项目ID
        rcvDocQcFormD.setISeq(record.get("iSeq"));
        rcvDocQcFormD.setISubSeq(record.get("iSubSeq"));
        rcvDocQcFormD.setCQcFormParamIds(record.getStr("cQcFormParamIds"));
        rcvDocQcFormD.setIType(record.get("iType"));
        rcvDocQcFormD.setIStdVal(record.get("iStdVal"));
        rcvDocQcFormD.setIMaxVal(record.get("iMaxVal"));
        rcvDocQcFormD.setIMinVal(record.get("iMinVal"));
        rcvDocQcFormD.setCOptions(record.get("cOptions"));
    }

    /*
     * 给质量管理-来料检明细列值传参
     * */
    public void saveRcvdocqcformdLineModel() {
        RcvdocqcformdLine rcvdocqcformdLine = new RcvdocqcformdLine();//质量管理-来料检明细列值表
        rcvdocqcformdLine.setIAutoId(JBoltSnowflakeKit.me.nextId());
        rcvdocqcformdLine.setIRcvDocQcFormDid(new Long(0));
        rcvdocqcformdLine.setISeq(0);
        rcvdocqcformdLine.setCValue("");
    }

    public Record getCheckoutListByIautoId(Long iautoId) {
        return dbTemplate("rcvdocqcformm.list", Kv.by("iautoId", iautoId)).findFirst();
    }

    public static class Param{
        public String name;
        public List<value> values;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<value> getValues() {
            return values;
        }

        public void setValues(List<value> values) {
            this.values = values;
        }

        public static class value{
            public String value;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }
}