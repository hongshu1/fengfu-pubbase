package cn.rjtech.admin.rcvdocqcformm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.Page;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.para.JBoltPara;
import cn.jbolt.core.util.JBoltRealUrlUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;

import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.admin.rcvdocdefect.RcvDocDefectService;
import cn.rjtech.admin.rcvdocqcformd.RcvDocQcFormDService;
import cn.rjtech.admin.rcvdocqcformdline.RcvdocqcformdLineService;
import cn.rjtech.model.momdata.RcvDocDefect;
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
    @Inject
    private RcvDocDefectService      rcvDocDefectService;      ////质量管理-来料异常品记录

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
     * 根据表格ID生成table
     * */
    public Ret createTable(Long iautoid,String cqcformname) {
        RcvDocQcFormM rcvDocQcFormM = findById(iautoid);
        if (null == rcvDocQcFormM) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //1、根据表格ID查询数据
        Long iQcFormId = rcvDocQcFormM.getIQcFormId();//表格ID
        List<Record> recordList = dbTemplate("rcvdocqcformm.getCheckoutList", Kv.by("iqcformid", iQcFormId)).find();
        if (recordList.isEmpty()) {
            return fail(cqcformname + "：没有检验项目，无法生成来料检查表");
        }
        ArrayList<RcvDocQcFormD> rcvDocQcFormDS = new ArrayList<>();
        for (Record record : recordList) {
            RcvDocQcFormD rcvDocQcFormD = new RcvDocQcFormD();//质量管理-来料检单行配置表
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
     * 根据iautoid查询数据,并跳到检验页面
     * */
    public Record getCheckoutListByIautoId(Long iautoId) {
        return dbTemplate("rcvdocqcformm.list", Kv.by("iautoId", iautoId)).findFirst();
    }

    /*
     * 点击检验时，进入弹窗自动加载table的数据
     * */
    public List<Record> getCheckOutTableDatas(Kv kv) {
        return clearZero(dbTemplate("rcvdocqcformm.findChecoutListByIformParamid", kv).find());
    }

    /*
     * 在检验页面点击确定
     * */
    public Ret saveCheckOutTable(JBoltPara JboltPara) {
        if (JboltPara == null || JboltPara.isEmpty()) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        String docqcformmiautoid = JboltPara.getString("docqcformmiautoid"); //主表id
        JSONArray serializeSubmitList = JboltPara.getJSONArray("serializeSubmitList");
        List<RcvdocqcformdLine> rcvdocqcformdLines = new ArrayList<>();
        for (int i = 0; i < serializeSubmitList.size(); i++) {
            JSONObject jsonObject = serializeSubmitList.getJSONObject(i);
            String ircvdocqcformdid = jsonObject.getString("iautoid");
            String iseq = jsonObject.getString("iseq");
            JSONArray serializeElement = jsonObject.getJSONArray("serializeElement");
            JSONArray elementList = serializeElement.getJSONArray(0);
            for (int j = 0; j < elementList.size(); j++) {
                JSONObject object = elementList.getJSONObject(j);
                String name = object.getString("name");
                String cvalue = object.getString("value");

                RcvdocqcformdLine rcvdocqcformdLine = new RcvdocqcformdLine();//质量管理-来料检明细列值表
                saveRcvdocqcformdLineModel(rcvdocqcformdLine, ircvdocqcformdid, iseq, cvalue);
                rcvdocqcformdLines.add(rcvdocqcformdLine);
            }
        }
        //保存line
        rcvdocqcformdLineService.batchSave(rcvdocqcformdLines);

        /*
         * 来料检表（PL_RcvDocQcFormM）
         * 1.如果isok=0，代表不合格，将iStatus更新为2，isCompleted更新为1；
         * 2.如果isok=1，代表合格，将iStatus更新为3，isCompleted更新为1
         * */
        RcvDocQcFormM docQcFormM = findById(docqcformmiautoid);
        saveDocQcFormMModel(docQcFormM, JboltPara);
        update(docQcFormM);

        //如果结果为不合格，记录不良品
        String isok = JboltPara.getString("isok");
        if (isok.equals("0")){
            RcvDocDefect defect = rcvDocDefectService
                .findStockoutDefectByiRcvDocQcFormMid(docqcformmiautoid);
            if (null == defect){
                RcvDocDefect rcvDocDefect = new RcvDocDefect();
                rcvDocDefectService.saveRcvDocDefectModel(rcvDocDefect,docQcFormM);
                rcvDocDefectService.save(rcvDocDefect);
            }
        }
        return ret(true);
    }

    /*
     * 点击查看时，进入弹窗自动加载table的数据
     * */
    public List<Record> getonlyseelistByiautoid(Kv kv) {
        List<Record> recordList = dbTemplate("rcvdocqcformm.getonlyseelistByiautoid", kv).find();
        List<Record> clearRecordList = clearZero(recordList);

        Map<Object, List<Record>> map = clearRecordList.stream()
            .collect(Collectors.groupingBy(p -> p.get("iautoid"), Collectors.toList()));

        List<Record> records = new ArrayList<>();
        for (Entry<Object, List<Record>> entry : map.entrySet()) {
            List<Record> value = entry.getValue();
            for (int i = 0; i < value.size(); i++) {
                value.get(i).set("name", "cA" + (i+1));
            }
            Record record = new Record();
            Record record1 = value.get(0);
            record.set("cvalueList", value);
            record.set("coptions", record1.get("coptions"));
            record.set("cqcformparamids", record1.get("cqcformparamids"));
            record.set("cqcitemname", record1.get("cqcitemname"));
            record.set("cqcparamname", record1.get("cqcparamname"));
            record.set("iautoid", record1.get("iautoid"));
            record.set("iformparamid", record1.get("iformparamid  "));
            record.set("imaxval", record1.get("imaxval"));
            record.set("iminval", record1.get("iminval"));
            record.set("iqcformid", record1.get("iqcformid"));
            record.set("ircvdocqcformmid", record1.get("ircvdocqcformmid"));
            record.set("iseq", record1.get("iseq"));
            record.set("istdval", record1.get("istdval"));
            record.set("isubseq", record1.get("isubseq"));
            record.set("itype", record1.get("itype"));
            records.add(record);
        }
        List<Record> resultRecord =
            records.stream().sorted(Comparator.comparing(e -> e.getInt("iseq"))).collect(Collectors.toList());
        return resultRecord;
    }

    public List<Record> getonlyseelistByiautoid(Long iautoid) {
        Kv kv = new Kv();
        kv.set("iautoid", iautoid);
        List<Record> recordList = dbTemplate("rcvdocqcformm.getonlyseelistByiautoid", kv).find();
        List<Record> clearRecordList = clearZero(recordList);

        Map<Object, List<Record>> map = clearRecordList.stream()
            .collect(Collectors.groupingBy(p -> p.get("iautoid"), Collectors.toList()));
        List<Record> docparamlist = new ArrayList<>();
        for (Entry<Object, List<Record>> entry : map.entrySet()) {
            docparamlist = entry.getValue();
            break;
        }
        return docparamlist;
    }

    /*
     * 清除多余的零
     * */
    public List<Record> clearZero(List<Record> recordList) {
        recordList.stream().forEach(e -> {
            e.set("istdval", e.getBigDecimal("istdval").stripTrailingZeros().toPlainString());
            e.set("imaxval", e.getBigDecimal("imaxval").stripTrailingZeros().toPlainString());
            e.set("iminval", e.getBigDecimal("iminval").stripTrailingZeros().toPlainString());
        });
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
     * 在编辑页面点击确定
     * */
    public Ret saveEditTable(JBoltPara JboltPara) {
        if (JboltPara == null || JboltPara.isEmpty()) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        String docqcformmiautoid = JboltPara.getString("docqcformmiautoid"); //主表id
        String isok = JboltPara.getString("isok");
        //是否合格不能为空
        if (StringUtils.isBlank(isok)) {
            return fail("请判定是否合格");
        }

        JSONArray serializeSubmitList = JboltPara.getJSONArray("serializeSubmitList");
        List<RcvdocqcformdLine> rcvdocqcformdLines = new ArrayList<>();
        for (int i = 0; i < serializeSubmitList.size(); i++) {
            JSONObject jsonObject = serializeSubmitList.getJSONObject(i);
            String iseq = jsonObject.getString("iseq");
            JSONArray cvaluelist = jsonObject.getJSONArray("cvaluelist");
            JSONArray serializeElement = jsonObject.getJSONArray("serializeElement");
            JSONArray elementList = serializeElement.getJSONArray(0);
            for (int j = 0; j < elementList.size(); j++) {
                JSONObject object = elementList.getJSONObject(j);
                String name = object.getString("name");
                String cvalue = object.getString("value");
                JSONObject cvaluelistJSONObject = cvaluelist.getJSONObject(j);
                Long lineiautoid = cvaluelistJSONObject.getLong("lineiautoid");
                RcvdocqcformdLine rcvdocqcformdLine = rcvdocqcformdLineService.findById(lineiautoid);//质量管理-来料检明细列值表
                rcvdocqcformdLine.setCValue(cvalue);
                rcvdocqcformdLines.add(rcvdocqcformdLine);
            }
        }
        //更新line
        rcvdocqcformdLineService.batchUpdate(rcvdocqcformdLines);

        RcvDocQcFormM docQcFormM = findById(docqcformmiautoid);
        saveDocQcFormMModel(docQcFormM, JboltPara);
        Ret ret = update(docQcFormM);
        if (ret.isFail()) {
            return ret;
        }
        return ret(true);
    }

    /*
     * 给主表传参
     * */
    public void saveDocQcFormMModel(RcvDocQcFormM docQcFormM, JBoltPara JboltPara) {
        docQcFormM.setCMeasurePurpose(JboltPara.getString("cmeasurepurpose"));//测定目的
        docQcFormM.setCMeasureReason(JboltPara.getString("cmeasurereason"));//测定理由
        docQcFormM.setCMeasureUnit(JboltPara.getString("cmeasureunit")); //测定单位
        docQcFormM.setCMemo(JboltPara.getString("cmemo"));//备注
        docQcFormM.setCDcNo(JboltPara.getString("cdcno")); //设变号
        String isok = JboltPara.getString("isok");
        docQcFormM.setIsOk(isok.equalsIgnoreCase("0") ? false : true);//是否合格
        docQcFormM.setIStatus(isok.equalsIgnoreCase("0") ? 2 : 3);
        docQcFormM.setIsCompleted(true);
    }

    /*
     * 给质量管理-来料检明细列值传参
     * */
    public void saveRcvdocqcformdLineModel(RcvdocqcformdLine rcvdocqcformdLine,
                                           String iautoid, String iseq, String cvalue) {
        rcvdocqcformdLine.setIAutoId(JBoltSnowflakeKit.me.nextId());
        rcvdocqcformdLine.setIRcvDocQcFormDid(Long.valueOf(iautoid));
        rcvdocqcformdLine.setISeq(Integer.valueOf(iseq));
        rcvdocqcformdLine.setCValue(cvalue);
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

}