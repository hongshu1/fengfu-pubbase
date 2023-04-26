package cn.rjtech.admin.stockoutqcformm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import cn.rjtech.admin.rcvdocqcformm.RcvDocQcFormMService;
import cn.rjtech.admin.stockoutdefect.StockoutDefectService;
import cn.rjtech.admin.stockoutqcformd.StockoutQcFormDService;
import cn.rjtech.admin.stockoutqcformdline.StockoutqcformdLineService;
import cn.rjtech.model.momdata.StockoutDefect;
import cn.rjtech.model.momdata.StockoutQcFormD;
import cn.rjtech.model.momdata.StockoutQcFormM;
import cn.rjtech.model.momdata.StockoutqcformdLine;

/**
 * 质量管理-出库检
 *
 * @ClassName: StockoutQcFormMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-12 19:18
 */
public class StockoutQcFormMService extends BaseService<StockoutQcFormM> {

    private final StockoutQcFormM dao = new StockoutQcFormM().dao();

    @Inject
    private RcvDocQcFormMService       rcvDocQcFormMService;
    @Inject
    private StockoutqcformdLineService stockoutqcformdLineService;
    @Inject
    private StockoutQcFormDService     stockoutQcFormDService;
    @Inject
    private StockoutDefectService      stockoutDefectService; //出库检异常品单

    @Override
    protected StockoutQcFormM dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     */
    public Page<StockoutQcFormM> getAdminDatas(int pageNumber, int pageSize) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //排序
        sql.desc("id");
        return paginate(sql);
    }

    public Page<Record> pageList(Kv kv) {
        return dbTemplate("stockoutqcformm.list", kv).paginate(kv.getInt("page"), kv.getInt("pageSize"));
    }

    /**
     * 保存
     */
    public Ret save(StockoutQcFormM stockoutQcFormM) {
        if (stockoutQcFormM == null || isOk(stockoutQcFormM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        boolean success = stockoutQcFormM.save();
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(StockoutQcFormM stockoutQcFormM) {
        if (stockoutQcFormM == null || notOk(stockoutQcFormM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        StockoutQcFormM dbStockoutQcFormM = findById(stockoutQcFormM.getIAutoId());
        if (dbStockoutQcFormM == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        boolean success = stockoutQcFormM.update();
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param stockoutQcFormM 要删除的model
     * @param kv              携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(StockoutQcFormM stockoutQcFormM, Kv kv) {
        //addDeleteSystemLog(stockoutQcFormM.getId(), JBoltUserKit.getUserId(),stockoutQcFormM.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param stockoutQcFormM model
     * @param kv              携带额外参数一般用不上
     */
    @Override
    public String checkInUse(StockoutQcFormM stockoutQcFormM, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
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
    public Ret editTable(JBoltPara JboltPara) {
        if (JboltPara == null || JboltPara.isEmpty()) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        String stockqcformmiautoid = JboltPara.getString("stockqcformmiautoid"); //主表id
        String isok = JboltPara.getString("isok");
        //是否合格不能为空
        if (StringUtils.isBlank(isok)) {
            return fail("请判定是否合格");
        }

        JSONArray serializeSubmitList = JboltPara.getJSONArray("serializeSubmitList");
        List<StockoutqcformdLine> stockoutqcformdLines = new ArrayList<>();
        for (int i = 0; i < serializeSubmitList.size(); i++) {
            JSONObject jsonObject = serializeSubmitList.getJSONObject(i);
            JSONArray cvaluelist = jsonObject.getJSONArray("cvaluelist");
            JSONArray serializeElement = jsonObject.getJSONArray("serializeElement");
            JSONArray elementList = serializeElement.getJSONArray(0);
            for (int j = 0; j < elementList.size(); j++) {
                JSONObject object = elementList.getJSONObject(j);
                String cvalue = object.getString("value");
                JSONObject cvaluelistJSONObject = cvaluelist.getJSONObject(j);
                Long lineiautoid = cvaluelistJSONObject.getLong("lineiautoid");
                StockoutqcformdLine stockoutqcformdLine = stockoutqcformdLineService.findById(lineiautoid);//质量管理-来料检明细列值表
                stockoutqcformdLine.setCValue(cvalue);
                stockoutqcformdLines.add(stockoutqcformdLine);
            }
        }
        //更新line
        stockoutqcformdLineService.batchUpdate(stockoutqcformdLines);

        StockoutQcFormM stockoutQcFormM = findById(stockqcformmiautoid);
        saveStockoutQcFormmModel(stockoutQcFormM, JboltPara);
		Ret ret = update(stockoutQcFormM);
		if (ret.isFail()) {
			return ret;
		}
        return ret(true);
    }

    /*
     * 在检验页面点击确定
     * */
    public Ret editCheckOutTable(JBoltPara JboltPara) {
        if (JboltPara == null || JboltPara.isEmpty()) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        String stockqcformmiautoid = JboltPara.getString("stockqcformmiautoid"); //主表id

        JSONArray serializeSubmitList = JboltPara.getJSONArray("serializeSubmitList");
        List<StockoutqcformdLine> stockoutqcformdLines = new ArrayList<>();
        for (int i = 0; i < serializeSubmitList.size(); i++) {
            JSONObject jsonObject = serializeSubmitList.getJSONObject(i);
            String istockoutqcformdid = jsonObject.getString("iautoid");
            String iseq = jsonObject.getString("iseq");
            JSONArray serializeElement = jsonObject.getJSONArray("serializeElement");
            JSONArray elementList = serializeElement.getJSONArray(0);
            for (int j = 0; j < elementList.size(); j++) {
                JSONObject object = elementList.getJSONObject(j);
                String name = object.getString("name");
                String cvalue = object.getString("value");

                StockoutqcformdLine stockoutqcformdLine = new StockoutqcformdLine();//质量管理-出库检明细列值表
                saveStockQcFormdLineModel(stockoutqcformdLine, istockoutqcformdid, iseq, cvalue);
                stockoutqcformdLines.add(stockoutqcformdLine);
            }
        }
        //保存line
        stockoutqcformdLineService.batchSave(stockoutqcformdLines);
        /*
         * 出库检表
         * 1.如果isok=0，代表不合格，将iStatus更新为2，isCompleted更新为1；
         * 2.如果isok=1，代表合格，将iStatus更新为3，isCompleted更新为1
         * */
        StockoutQcFormM stockoutQcFormM = findById(stockqcformmiautoid);
        saveStockoutQcFormmModel(stockoutQcFormM, JboltPara);
        update(stockoutQcFormM);

        //如果检验结果不合格，生成在库异常品记录
        StockoutDefect defect = stockoutDefectService
            .findStockoutDefectByiStockoutQcFormMid(stockqcformmiautoid);
        if (null == defect) {
            StockoutDefect stockoutDefect = new StockoutDefect();
            stockoutDefectService.savestockoutDefectmodel(stockoutDefect, stockoutQcFormM);
            stockoutDefectService.save(stockoutDefect);
        }
        return ret(true);
    }

    /*
     * 给出库检明细列值表传参
     * */
    public void saveStockQcFormdLineModel(StockoutqcformdLine stockoutqcformdLine, String iautoid, String iseq, String cvalue) {
        stockoutqcformdLine.setIAutoId(JBoltSnowflakeKit.me.nextId());
        stockoutqcformdLine.setIStockoutQcFormDid(Long.valueOf(iautoid));
        stockoutqcformdLine.setISeq(Integer.valueOf(iseq));
        stockoutqcformdLine.setCValue(cvalue);
    }

    /*
     * 给出库检主表传参
     * */
    public void saveStockoutQcFormmModel(StockoutQcFormM stockoutQcFormM, JBoltPara JboltPara) {
        stockoutQcFormM.setCMeasurePurpose(JboltPara.getString("cmeasurepurpose"));//测定目的
        stockoutQcFormM.setCMeasureReason(JboltPara.getString("cmeasurereason"));//测定理由
        stockoutQcFormM.setCMeasureUnit(JboltPara.getString("cmeasureunit")); //测定单位
        stockoutQcFormM.setCMemo(JboltPara.getString("cmemo"));//备注
        stockoutQcFormM.setCDcNo(JboltPara.getString("cdcno")); //设变号
        String isok = JboltPara.getString("isok");
        stockoutQcFormM.setIsOk(isok.equalsIgnoreCase("0") ? false : true);//是否合格
        stockoutQcFormM.setIStatus(isok.equalsIgnoreCase("0") ? 2 : 3);
        stockoutQcFormM.setIsCompleted(true);
    }

    /*
     * 点击检验时，进入弹窗自动加载table的数据
     * */
    public List<Record> getCheckOutTableDatas(Kv kv) {
        return rcvDocQcFormMService.clearZero(dbTemplate("stockoutqcformm.findChecoutListByIformParamid", kv).find());
    }

    /*
     * 点击检验时，进入弹窗自动加载table的数据
     * */
    public List<Record> getonlyseelistByiautoid(Kv kv) {
        List<Record> recordList = dbTemplate("stockoutqcformm.getonlyseelistByiautoid", kv).find();
        List<Record> clearRecordList = clearZero(recordList);

        Map<Object, List<Record>> map = clearRecordList.stream()
            .collect(Collectors.groupingBy(p -> p.get("iautoid"), Collectors.toList()));

        List<Record> records = new ArrayList<>();
        for (Entry<Object, List<Record>> entry : map.entrySet()) {
            List<Record> value = entry.getValue();
            for (int i = 0; i < value.size(); i++) {
                value.get(i).set("name", "cA" + (i + 1));
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
        List<Record> recordList = dbTemplate("stockoutqcformm.getonlyseelistByiautoid", kv).find();
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

    /*
     * 根据表格ID生成table
     * */
    public Ret createTable(JBoltPara jBoltPara) {
        Long iautoid = jBoltPara.getLong("iautoid");
        StockoutQcFormM stockoutQcFormM = findById(iautoid);
        if (null == stockoutQcFormM) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //1、根据表格ID查询数据
        Long iQcFormId = stockoutQcFormM.getIQcFormId();//表格ID
        List<Record> recordList = dbTemplate("rcvdocqcformm.getCheckoutList", Kv.by("iqcformid", iQcFormId)).find();
        if (recordList.isEmpty()) {
            return fail(jBoltPara.get("cqcformname") + "：没有检验项目，无法生成出库检验表");
        }
        ArrayList<StockoutQcFormD> stockoutQcFormDS = new ArrayList<>();
        for (Record record : recordList) {
            StockoutQcFormD stockoutQcFormD = new StockoutQcFormD();//质量管理-来料检单行配置表
            stockoutQcFormD.setIAutoId(JBoltSnowflakeKit.me.nextId());
            stockoutQcFormD.setIStockoutQcFormMid(iautoid);//来料检id
            stockoutQcFormD.setIQcFormId(iQcFormId);//检验表格ID
            stockoutQcFormD.setIFormParamId(record.getLong("iFormParamId"));//检验项目ID
            stockoutQcFormD.setISeq(record.get("iSeq"));
            stockoutQcFormD.setISubSeq(record.get("iSubSeq"));
            stockoutQcFormD.setCQcFormParamIds(record.getStr("cQcFormParamIds"));
            stockoutQcFormD.setIType(record.get("iType"));
            stockoutQcFormD.setIStdVal(record.get("iStdVal"));
            stockoutQcFormD.setIMaxVal(record.get("iMaxVal"));
            stockoutQcFormD.setIMinVal(record.get("iMinVal"));
            stockoutQcFormD.setCOptions(record.get("cOptions"));
            stockoutQcFormDS.add(stockoutQcFormD);
        }
        stockoutQcFormDService.batchSave(stockoutQcFormDS);

        //2、更新PL_RcvDocQcFormM检验结果(istatus)为“待检-1”
        stockoutQcFormM.setIStatus(1);
        Ret ret = update(stockoutQcFormM);
        if (ret.isFail()) {
            return ret;
        }
        return SUCCESS;
    }

    /*
     * 根据iautoid查询数据,并跳到检验页面
     * */
    public Record getCheckoutListByIautoId(Long iautoId) {
        return dbTemplate("stockoutqcformm.list", Kv.by("iautoId", iautoId)).findFirst();
    }

}