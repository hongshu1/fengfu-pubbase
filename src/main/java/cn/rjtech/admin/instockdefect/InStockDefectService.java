package cn.rjtech.admin.instockdefect;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.instockqcformm.InStockQcFormMService;
import cn.rjtech.model.momdata.InStockDefect;
import cn.rjtech.model.momdata.InStockQcFormM;
import cn.rjtech.util.BillNoUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 在库异常记录 Service
 *
 * @ClassName: InStockDefectService
 * @author: RJ
 * @date: 2023-04-25 14:58
 */
public class InStockDefectService extends BaseService<InStockDefect> {

    private final InStockDefect dao = new InStockDefect().dao();

    @Inject
    private InStockQcFormMService inStockQcFormMService;      ////质量管理-在库检

    @Override
    protected InStockDefect dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public Page<Record> paginateAdminDatas(int pageSize, int pageNumber, Kv kv) {
        return dbTemplate("instockdefect.paginateAdminDatas", kv).paginate(pageNumber, pageSize);
    }

    /**
     * 保存
     */
    public Ret save(InStockDefect inStockDefect) {
        if (inStockDefect == null || isOk(inStockDefect.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(inStockDefect.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = inStockDefect.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(inStockDefect.getIautoid(), JBoltUserKit.getUserId(), inStockDefect.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(InStockDefect inStockDefect) {
        if (inStockDefect == null || notOk(inStockDefect.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        InStockDefect dbInStockDefect = findById(inStockDefect.getIAutoId());
        if (dbInStockDefect == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(inStockDefect.getName(), inStockDefect.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = inStockDefect.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(inStockDefect.getIautoid(), JBoltUserKit.getUserId(), inStockDefect.getName());
        }
        return ret(success);
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        return deleteByIds(ids, true);
    }

    /**
     * 删除
     */
    public Ret delete(Long id) {
        return deleteById(id, true);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param inStockDefect 要删除的model
     * @param kv            携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(InStockDefect inStockDefect, Kv kv) {
        //addDeleteSystemLog(inStockDefect.getIautoid(), JBoltUserKit.getUserId(),inStockDefect.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param inStockDefect 要删除的model
     * @param kv            携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(InStockDefect inStockDefect, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(inStockDefect, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 切换isfirsttime属性
     */
    public Ret toggleIsFirstTime(Long id) {
        return toggleBoolean(id, "isFirstTime");
    }

    /**
     * 切换isdeleted属性
     */
    public Ret toggleIsDeleted(Long id) {
        return toggleBoolean(id, "IsDeleted");
    }

    /**
     * 检测是否可以toggle操作指定列
     *
     * @param inStockDefect 要toggle的model
     * @param column        操作的哪一列
     * @param kv            携带额外参数一般用不上
     */
    @Override
    public String checkCanToggle(InStockDefect inStockDefect, String column, Kv kv) {
        //没有问题就返回null  有问题就返回提示string 字符串
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(InStockDefect inStockDefect, String column, Kv kv) {
        //addUpdateSystemLog(inStockDefect.getIautoid(), JBoltUserKit.getUserId(), inStockDefect.getName(),"的字段["+column+"]值:"+inStockDefect.get(column));
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param inStockDefect model
     * @param kv            携带额外参数一般用不上
     */
    @Override
    public String checkInUse(InStockDefect inStockDefect, Kv kv) {
        //这里用来覆盖 检测InStockDefect是否被其它表引用
        return null;
    }

    /**
     * 更新状态并保存数据方法
     */
    public Ret updateEditTable(Kv formRecord) {
        Date now = new Date();

        tx(() -> {
            // 判断是否有主键id
            if (isOk(formRecord.getStr("iautoid"))) {
                InStockDefect inStockDefect = findById(formRecord.getLong("iautoid"));
                if (inStockDefect.getIStatus() == 1) {
                    //录入数据
                    inStockDefect.setCApproach(formRecord.getStr("capproach"));
                    inStockDefect.setIStatus(2);
                    //更新人和时间
                    inStockDefect.setIUpdateBy(JBoltUserKit.getUserId());
                    inStockDefect.setCUpdateName(JBoltUserKit.getUserName());
                    inStockDefect.setDUpdateTime(now);

                }
                inStockDefect.update();
            } else {
                //保存未有主键的数据
                inStockDefectLinesave(formRecord, now);
            }

            return true;
        });
        return SUCCESS;
    }

    /**
     * 编辑保存
     */
    public void inStockDefectLinesave(Kv formRecord, Date now) {
        InStockDefect inStockDefect = new InStockDefect();
        inStockDefect.setIAutoId(formRecord.getLong("iautoid"));

        //质量管理-来料检明细
        InStockQcFormM inStockQcFormM = inStockQcFormMService.findById(formRecord.getLong("iinstockqcformmid"));
        inStockDefect.setIInStockQcFormMid(inStockQcFormM.getIAutoId());
//        inStockDefect.setIVendorId(formRecord.getLong("inStockQcFormM.iCustomerId"));
        inStockDefect.setIInventoryId(inStockQcFormM.getIInventoryId());
        inStockDefect.setIQcUserId(inStockQcFormM.getIUpdateBy());
        inStockDefect.setDQcTime(inStockQcFormM.getDUpdateTime());

        //录入填写的数据
        inStockDefect.setIStatus(1);
        inStockDefect.setIDqQty(formRecord.getBigDecimal("idqqty"));
        inStockDefect.setIRespType(formRecord.getInt("iresptype"));
        inStockDefect.setIsFirstTime(formRecord.getBoolean("isfirsttime"));
        inStockDefect.setCBadnessSns(formRecord.getStr("cbadnesssns"));
        inStockDefect.setCDesc(formRecord.getStr("cdesc"));

        //必录入基本数据
        inStockDefect.setIAutoId(JBoltSnowflakeKit.me.nextId());
        String billNo = BillNoUtils.getcDocNo(getOrgId(), "YCP", 5);
        inStockDefect.setCDocNo(billNo);
        inStockDefect.setIOrgId(getOrgId());
        inStockDefect.setCOrgCode(getOrgCode());
        inStockDefect.setCOrgName(getOrgName());
        inStockDefect.setICreateBy(JBoltUserKit.getUserId());
        inStockDefect.setCCreateName(JBoltUserKit.getUserName());
        inStockDefect.setDCreateTime(now);
        inStockDefect.setIUpdateBy(JBoltUserKit.getUserId());
        inStockDefect.setCUpdateName(JBoltUserKit.getUserName());
        inStockDefect.setDUpdateTime(now);
        inStockDefect.save();
    }

    /**
     * 在库检明细
     */
    public Record getinStockQcFormMList(Long iautoid) {
        return dbTemplate("instockdefect.getinStockQcFormMList", Kv.by("iautoid", iautoid)).findFirst();
    }

    //API在库主页查询
    public Page<Record> getPageListApi(Integer pageNumber, Integer pageSize, Kv kv) {
        return dbTemplate("instockdefect.paginateAdminDatasapi", kv).paginate(pageNumber, pageSize);
    }

    //API在库异常品查看与编辑
    public Map<String, Object> getInStockDefectListApi(Long iautoid, Long iinstockqcformmid, String type) {

        Map<String, Object> map = new HashMap<>();

        InStockDefect inStockDefect = findById(iautoid);
        Record inStockQcFormM = getinStockQcFormMList(iinstockqcformmid);
        map.put("inStockDefect", inStockDefect);
        map.put("inStockQcFormM", inStockQcFormM);
        map.put("type", type);
        if (inStockDefect == null) {
            return map;
        }
        if (inStockDefect.getIStatus() == 1) {
            map.put("isfirsttime", (inStockDefect.getIsFirstTime() == true) ? "首发" : "再发");
            map.put("iresptype",
                (inStockDefect.getIRespType() == 1) ? "供应商" : (inStockDefect.getIRespType() == 2 ? "工程内" : "其他"));
        } else if (inStockDefect.getIStatus() == 2) {
            int getCApproach = Integer.parseInt(inStockDefect.getCApproach());
            map.put("capproach", (getCApproach == 1) ? "报废" : (getCApproach == 2 ? "返修" : "退货"));
            map.put("isfirsttime", (inStockDefect.getIsFirstTime() == true) ? "首发" : "再发");
            map.put("iresptype",
                (inStockDefect.getIRespType() == 1) ? "供应商" : (inStockDefect.getIRespType() == 2 ? "工程内" : "其他"));
        }
        return map;
    }

    public void saveInStockOutDefectModel(InStockDefect inStockDefect, InStockQcFormM inStockQcFormM) {
        Date date = new Date();
        Long userId = JBoltUserKit.getUserId();
        String userName = JBoltUserKit.getUserName();
        inStockDefect.setIAutoId(JBoltSnowflakeKit.me.nextId());
        inStockDefect.setCDocNo(JBoltSnowflakeKit.me.nextIdStr());   //异常品单号
        inStockDefect.setIInStockQcFormMid(inStockQcFormM.getIAutoId()); //在库检id
        inStockDefect.setIInventoryId(inStockQcFormM.getIInventoryId());  //存货ID
        inStockDefect.setIStatus(1);                                       //状态：1. 待记录 2. 待判定 3. 已完成
        inStockDefect.setCDesc(inStockQcFormM.getCMemo());                //不良内容描述
        inStockDefect.setIQcUserId(inStockQcFormM.getIQcUserId());        //检验用户ID
        inStockDefect.setDQcTime(inStockQcFormM.getDUpdateTime());        //检验时间

        inStockDefect.setICreateBy(userId);
        inStockDefect.setDCreateTime(date);
        inStockDefect.setCCreateName(userName);
        inStockDefect.setIOrgId(getOrgId());
        inStockDefect.setCOrgCode(getOrgCode());
        inStockDefect.setCOrgName(getOrgName());
        inStockDefect.setIUpdateBy(userId);
        inStockDefect.setCUpdateName(userName);
        inStockDefect.setDUpdateTime(date);
    }

    /*
     * 根据出货检id查询异常品质单
     */
    public InStockDefect findDefectByiInStockQcFormMid(Object iInStockQcFormMid) {
        return findFirst("SELECT * FROM PL_InStockDefect WHERE iInStockQcFormMid = ?", iInStockQcFormMid);
    }

    /**
     * 打印数据
     *
     * @param kv 参数
     */
    public Object getQRCodeCheck(Kv kv) {
        return dbTemplate("instockdefect.containerPrintData", kv).find();
    }
}