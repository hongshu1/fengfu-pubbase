package cn.rjtech.admin.momoinvbatch;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.modoc.MoDocService;
import cn.rjtech.admin.momoinvbatch.vo.SubPrintReqVo;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.admin.uom.UomService;
import cn.rjtech.admin.workregionm.WorkregionmService;
import cn.rjtech.admin.workshiftm.WorkshiftmService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;

/**
 * 工单现品票 Controller
 *
 * @ClassName: MoMoinvbatchAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-31 15:35
 */
@CheckPermission(PermissionKey.MOMOINVBATCH)
@UnCheckIfSystemAdmin
@Path(value = "/admin/momoinvbatch", viewPath = "/_view/admin/momoinvbatch")
public class MoMoinvbatchAdminController extends BaseAdminController {

    @Inject
    private UomService uomService;
    @Inject
    private MoDocService moDocService;
    @Inject
    private MoMoinvbatchService service;
    @Inject
    private PersonService personService;
    @Inject
    private InventoryService inventoryService;
    @Inject
    private WorkshiftmService workshiftmService;
    @Inject
    private DepartmentService departmentService;
    @Inject
    private WorkregionmService workregionmService;

    /**
     * 首页 工单现品票做成页面
     */
    public void index(@Para(value = "imodocid") Long imodocid) {
        ValidationUtils.validateId(imodocid, "工单ID");

        Ret r = service.createBarcode(imodocid);
        ValidationUtils.isTrue(r.isOk(), r.getStr("msg"));

        render("index.html");
    }

    /**
     * 数据源
     */
    @UnCheck
    public void datas() {
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 新增
     */
    public void add() {
        set("imodocid", getLong("imodocid"));
        getModoc(getLong("imodocid"));
        render("add2.html");
    }

    /**
     * 编辑
     */
    public void edit() {
        MoMoinvbatch moMoinvbatch = service.findById(getLong(0));
        if (moMoinvbatch == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("moMoinvbatch", moMoinvbatch);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(MoMoinvbatch.class, "moMoinvbatch")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(MoMoinvbatch.class, "moMoinvbatch")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.delete(getLong(0)));
    }

    /**
     * 切换toggleIsDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleIsDeleted(getLong(0)));
    }

    /**
     * 创建工单现品票
     */
    public void createBarcode() {
        Long imodocid = getLong("imodocid");
        //renderJsonData(service.createBarcode(imodocid));
    }

    /**
     * 获取工单信息
     */
    @UnCheck
    public void getModoc(Long imodocid) {
        MoDoc moDoc = moDocService.findById(imodocid);
        if (moDoc == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }

        Record moRecod = moDoc.toRecord();

        if (isOk(moDoc.getIInventoryId())) {
            // 存货
            Inventory inventory = inventoryService.findById(moDoc.getIInventoryId());
            if (inventory != null) {
                // 料品编码
                moRecod.set("cinvcode", inventory.getCInvCode());
                // 客户部番
                moRecod.set("cinvcode1", inventory.getCInvCode1());
                // 部品名称
                moRecod.set("cinvname1", inventory.getCInvName1());
                moRecod.set("ipkgqty", inventory.getIPkgQty());

                // 生产单位
                Uom uom = uomService.findFirst(Okv.create().setIfNotNull(Uom.IAUTOID, inventory.getICostUomId()), Uom.IAUTOID, "desc");
                if (uom != null) {
                    moRecod.set("manufactureuom", uom.getCUomName());
                }
                // 规格
                moRecod.set("cinvstd", inventory.getCInvStd());
            }

            // 工艺路线

        }

        // 差异数量
        if (moDoc.getIQty() != null && moDoc.getICompQty() != null) {
            BigDecimal cyqty = moDoc.getIQty().subtract(moDoc.getICompQty());
            moRecod.set("cyqty", cyqty);
        }

        // 产线
        if (isOk(moDoc.getIWorkRegionMid())) {
            Workregionm workregionm = workregionmService.findById(moDoc.getIWorkRegionMid());
            if (workregionm != null) {
                moRecod.set("cworkname", workregionm.getCWorkName());
            }
        }
        // 作业人员
        String psnName = moDocService.getPsnNameById(moDoc.getIAutoId());
        moRecod.set("psnname", psnName);
        // 产线组长
        if (isOk(moDoc.getIDutyPersonId()))
        {
            Person person = personService.findById(moDoc.getIDutyPersonId());
            if (person != null) {
                moRecod.set("cdutypersonname", person.getCpsnName());
            }
        }


        // 班次
        if (isOk(moDoc.getIWorkShiftMid())) {
            Workshiftm workshiftm = workshiftmService.findById(moDoc.getIWorkShiftMid());
            if (workshiftm != null) {
                moRecod.set("cworkshiftname", workshiftm.getCworkshiftname());
            }
        }

        // 部门
        if (isOk(moDoc.getIDepartmentId())) {
            Department department = departmentService.findById(moDoc.getIDepartmentId());
            if (department != null) {
                moRecod.set("cdepname", department.getCDepName());
            }
        }
        set("moDoc", moRecod);
    }

    public void editprint() {
        MoMoinvbatch moMoinvbatch = service.findById(getLong(0));
        if (moMoinvbatch == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }

        Record moMoinvbatchRecord = moMoinvbatch.toRecord();
        MoDoc moDoc = moDocService.findById(moMoinvbatch.getIMoDocId());
        
        if (moDoc != null) {
            //产线
            if (isOk(moDoc.getIWorkRegionMid())) {
                Workregionm workregionm = workregionmService.findById(moDoc.getIWorkRegionMid());
                if (workregionm != null) {
                    if (isOk(workregionm.getIPersonId())) {
                        Person person = personService.findFirstByUserId(workregionm.getIPersonId());
                        moMoinvbatchRecord.set("workleader", person.getCpsnName());
                    }
                }
            }
            //作业员
            String jobname = service.getModocLastProcessPerson(moDoc.getIAutoId()).toString();
            moMoinvbatchRecord.set("jobname", jobname);
        }
        
        set("moMoinvbatch", moMoinvbatchRecord);
        render("edit.html");
    }

    /**
     * 提交打印
     */
    public void subPrint() {
        SubPrintReqVo subPrintReqVo = new SubPrintReqVo();

        subPrintReqVo.setIseq(getInt("moMoinvbatch.iseq"));
        subPrintReqVo.setIautoid(getLong("moMoinvbatch.iautoid"));
        subPrintReqVo.setMemo(get("moMoinvbatch.memo"));
        subPrintReqVo.setCbarcode(get("moMoinvbatch.cbarcode"));
        subPrintReqVo.setJobname(get("moMoinvbatch.jobname"));
        subPrintReqVo.setIqty(getBigDecimal("moMoinvbatch.iqty"));
        subPrintReqVo.setWorkheader(get("moMoinvbatch.workleader"));

        renderJson(service.subPrint(subPrintReqVo));
    }

    /**
     * 提交打印
     */
    public void subListPrint() {
        String ids = get("ids");
        Long imodocid = getLong("imodocid");
        String workleader = get("workleader");
        String jobname = get("jobname");
        String memo = get("memo");
        renderJson(service.subListPrint(ids, imodocid, workleader, memo));
    }

    /**
     * 提交批量打印界面
     */
    public void subListPrintPage() {
        MoDoc moDoc = moDocService.findById(getLong("imodocid"));
        if (moDoc != null) {
            // 产线
            if (isOk(moDoc.getIWorkRegionMid())) {
                Workregionm workregionm = workregionmService.findById(moDoc.getIWorkRegionMid());
                if (workregionm != null) {
                    if (isOk(workregionm.getIPersonId())) {
                        Person person = personService.findFirstByUserId(workregionm.getIPersonId());
                        set("workleader", person.getCpsnName());
                    }
                }
            }
            // 作业员
            String jobname = service.getModocLastProcessPerson(moDoc.getIAutoId()).toString();
            set("jobname", jobname);
        }

        keepPara();
        render("listprint.html");
    }

    /**
     * 打印模板测试数据
     */
    public void printdataList() {
        renderJson(service.printdataList());
    }

    /**
     * 更新数量
     */
    public void updatePage() {
        MoMoinvbatch moMoinvbatch = service.findById(getLong(0));
        if (moMoinvbatch == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("moMoinvbatch", moMoinvbatch);
        render("update.html");
    }

    /**
     * 更新状态
     */
    public void updateStatus() {
        Long iautoid = getLong("moMoinvbatch.iautoid");
        BigDecimal iqty = getBigDecimal("moMoinvbatch.iqty");
        renderJson(service.updateStatus(iautoid, iqty));
    }

    /**
     * 报工
     */
    public void updateWork() {
        MoMoinvbatch moMoinvbatch = service.findById(getLong(0));
        if (moMoinvbatch == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        renderJson(service.updateWork(moMoinvbatch));
    }

    /**
     * 生成现品票
     */
    @CheckPermission(PermissionKey.MOMOINVBATCH_CREATEMOMOINVBATCH)
    public void createMomoinvbatch()
    {
        renderJson(service.createMomoinvbatch(getLong("imodocid")));
    }

    /**
     * 批量报工
     */
    @CheckPermission(PermissionKey.MOMOINVBATCH_WORKBYIDS)
    public void workByIds()
    {
        renderJson(service.workByIds(get("imodocid"), get("ids")));
    }

    /**
     * 撤回
     */
    @CheckPermission(PermissionKey.MOMOINVBATCH_WITHDRAW)
    public void withdraw() {
        renderJson(service.withdraw(getLong("iautoid")));
    }

    /**
     * 修改数量
     */
    @CheckPermission(PermissionKey.MOMOINVBATCH_UPDATENUMBER)
    public void updateNumber()
    {
        Long iautoid = getLong("moMoinvbatch.iautoid");
        BigDecimal newQty = getBigDecimal("moMoinvbatch.iqty");
        renderJson(service.updateNumber(iautoid, newQty));
    }

    /**
     * 批量打印
     */
    @CheckPermission(PermissionKey.MOMOINVBATCH_BATCHPRINT)
    public void batchPrint()
    {
        renderJson(service.batchPrint(get("imodocid"), get("ids")));
    }
}
