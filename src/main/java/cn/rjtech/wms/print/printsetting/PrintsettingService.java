package cn.rjtech.wms.print.printsetting;

import cn.hutool.core.text.StrSplitter;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.common.model.Printsetting;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.wms.print.printmachine.PrintmachineService;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;

import static cn.hutool.core.util.StrUtil.COMMA;

/**
 * 打印设置 Service
 *
 * @ClassName: PrintsettingService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-24 14:24
 */
public class PrintsettingService extends BaseService<Printsetting> {

    private final Printsetting dao = new Printsetting().dao();

    @Inject
    private PrintmachineService printmachineService;

    @Override
    protected Printsetting dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv para) {
        return dbTemplate("printsetting.list",para).paginate(pageNumber,pageSize);
    }

    /**
     * 保存
     */
    public Ret save(Printsetting printsetting) {
        if (printsetting == null || isOk(printsetting.getAutoid())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        printsetting.setCreatedate(new Date());
        printsetting.setCreateperson(JBoltUserKit.getUserId().toString());
        printsetting.setOrganizecode(getOrgCode());
        tx(() -> {
            // ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);
            ValidationUtils.isTrue(printsetting.save(), ErrorMsg.SAVE_FAILED);


            // TODO 其他业务代码实现

            return true;
        });

        // 添加日志
        // addSaveSystemLog(printsetting.getAutoid(), JBoltUserKit.getUserId(), printsetting.getName());
        return SUCCESS;
    }

    /**
     * 更新
     */
    public Ret update(Printsetting printsetting) {
        if (printsetting == null || notOk(printsetting.getAutoid())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        printsetting.setOrganizecode(getOrgCode());
        printsetting.setModifydate(new Date());
        printsetting.setModifyperson(JBoltUserKit.getUserId().toString());
        tx(() -> {
            // 更新时需要判断数据存在
            Printsetting dbPrintsetting = findById(printsetting.getAutoid());
            ValidationUtils.notNull(dbPrintsetting, JBoltMsg.DATA_NOT_EXIST);

            // TODO 其他业务代码实现
            // ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);

            ValidationUtils.isTrue(printsetting.update(), ErrorMsg.UPDATE_FAILED);

            return true;
        });

        //添加日志
        //addUpdateSystemLog(printsetting.getAutoid(), JBoltUserKit.getUserId(), printsetting.getName());
        return SUCCESS;
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        tx(() -> {
            for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
                long autoId = Long.parseLong(idStr);
                Printsetting dbPrintsetting = findById(autoId);
                ValidationUtils.notNull(dbPrintsetting, JBoltMsg.DATA_NOT_EXIST);

                // TODO 可能需要补充校验组织账套权限
                // TODO 存在关联使用时，校验是否仍在使用

                ValidationUtils.isTrue(dbPrintsetting.delete(), JBoltMsg.FAIL);
            }

            return true;
        });

        // 添加日志
        // Printsetting printsetting = ret.getAs("data");
        // addDeleteSystemLog(AutoID, JBoltUserKit.getUserId(), SystemLog.TARGETTYPE_xxx, printsetting.getName());
        return SUCCESS;
    }

    /**
     * 删除数据后执行的回调
     *
     * @param printsetting 要删除的model
     * @param kv          携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(Printsetting printsetting, Kv kv) {
        //addDeleteSystemLog(printsetting.getAutoid(), JBoltUserKit.getUserId(),printsetting.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param printsetting 要删除的model
     * @param kv          携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(Printsetting printsetting, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(printsetting, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    public String getPrintMachineCode(String organizeCode, String tag) {
        Kv paras = Kv.by("organizeCode", organizeCode)
                .set("tag", tag);

        return dbTemplate("printsetting.getPrintMachineCode", paras).queryStr();
    }

    public List<Record> valueandsymbolSplit(String value, String symbol) {
        Kv paras = Kv.by("value", value)
                .set("symbol", symbol);

        return dbTemplate("printsetting.valueandsymbolSplit", paras).find();
    }

    public Printsetting getPrintSetting(String tag) {
        Kv paras = Kv.by("tag", tag);

        return daoTemplate("printsetting.getPrintSetting", paras).findFirst();
    }

    public Printsetting getPrintSettingByReportfilename(String reportfilename) {
        Okv para = Okv.by("reportfilename", reportfilename);

        return daoTemplate("printsetting.getPrintSetting", para).findFirst();
    }


    /**
     * 打印类型
     * */
    public List<Printsetting> printDatas(Kv kv) {
        return daoTemplate("printsetting.printDatas",kv).find();
    }

    /**
     * 打印类型_原材料
     * */
    public List<Printsetting> printDatasPureceive() {
        return daoTemplate("printsetting.printDatasPureceive").find();
    }

    /**
     * 产成品打印类型
     * */
    public List<Printsetting> printDatasProduction(Kv kv) {
        return daoTemplate("printsetting.printDatasProduction",kv).find();
    }
}