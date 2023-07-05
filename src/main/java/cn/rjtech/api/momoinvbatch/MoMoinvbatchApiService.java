package cn.rjtech.api.momoinvbatch;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.modoc.MoDocService;
import cn.rjtech.admin.momoinvbatch.MoMoinvbatchService;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.admin.uom.UomService;
import cn.rjtech.admin.workregionm.WorkregionmService;
import cn.rjtech.admin.workshiftm.WorkshiftmService;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;

public class MoMoinvbatchApiService extends JBoltApiBaseService {
    
    @Inject
    private MoDocService moDocService;
    @Inject
    private InventoryService inventoryService;
    @Inject
    private UomService uomService;
    @Inject
    private WorkregionmService workregionmService;
    @Inject
    private PersonService personService;
    @Inject
    private WorkshiftmService workshiftmService;
    @Inject
    private DepartmentService departmentService;
    @Inject
    private MoMoinvbatchService moMoinvbatchService;


    public Ret getModocData(Long imodocid) {
        MoDoc moDoc = moDocService.findById(imodocid);
        ValidationUtils.notNull(moDoc, JBoltMsg.DATA_NOT_EXIST);
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

        return JBoltApiRet.API_SUCCESS_WITH_DATA(Okv.by("modoc", moRecod));
    }

    /**
     * 返回现品票数据
     */
    public Ret getMoMoinvbatchDatas(Long imodocid, Integer iprintstatus) {
        Page<Record> page = moMoinvbatchService.paginateAdminDatas(1, 10000, Kv.by("imodocid", imodocid).set("iprintstatus", iprintstatus));
        if (notOk(page)) {
            return SUCCESS;
        }
        
        return JBoltApiRet.API_SUCCESS_WITH_DATA(Okv.by("momoinvbatchs", page.getList()));
    }
    
}