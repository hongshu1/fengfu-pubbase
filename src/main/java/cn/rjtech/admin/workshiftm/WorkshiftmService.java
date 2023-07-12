package cn.rjtech.admin.workshiftm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.workshiftd.WorkshiftdService;
import cn.rjtech.cache.CusFieldsMappingdCache;
import cn.rjtech.model.momdata.Workshiftd;
import cn.rjtech.model.momdata.Workshiftm;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.util.*;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 生产班次 Service
 *
 * @ClassName: WorkshiftmService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-10-27 09:25
 */
public class WorkshiftmService extends BaseService<Workshiftm> {

    private final Workshiftm dao = new Workshiftm().dao();

    @Override
    protected Workshiftm dao() {
        return dao;
    }

    @Inject
    private WorkshiftdService workshiftdService;

    /**
     * 后台管理分页查询
     */
    public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {
        kv.set(Workshiftm.IORGID, getOrgId());
        return dbTemplate("workshiftm.paginateAdminDatas", kv).paginate(pageNumber, pageSize);
        //return paginateByKeywords("iautoid","DESC", pageNumber, pageSize, keywords, "iautoid");
    }

    /**
     * 后台管理分页查询
     */
    public List<Record> getSelect() {
        return dbTemplate("workshiftm.getSelect").find();
    }

    public List<Record> getDataExport(Kv kv) {
        List<Record> list = dbTemplate("workshiftm.getDataExport2", kv).find();
        for (Record record : list) {
            record.set("dstarttime", record.getStr("dstarttime"));
            record.set("dendtime", record.getStr("dendtime"));
        }
        return list;
    }

    /**
     * 保存
     */
    public Ret save(Workshiftm workshiftm) {
        if (workshiftm == null || isOk(workshiftm.getIautoid())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(workshiftm.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = workshiftm.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(workshiftm.getIautoid(), JBoltUserKit.getUserId(), workshiftm.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(Workshiftm workshiftm) {
        if (workshiftm == null || notOk(workshiftm.getIautoid())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        Workshiftm dbWorkshiftm = findById(workshiftm.getIautoid());
        if (dbWorkshiftm == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(workshiftm.getName(), workshiftm.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = workshiftm.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(workshiftm.getIautoid(), JBoltUserKit.getUserId(), workshiftm.getName());
        }
        return ret(success);
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        tx(() -> {
            for (String id : StrSplitter.split(ids, COMMA, true, true)) {
                Workshiftm workshiftm = findById(id);
                ValidationUtils.notNull(workshiftm, JBoltMsg.DATA_NOT_EXIST);
                workshiftm.setIsdeleted(true);
                workshiftm.update();
            }
            return true;
        });
        return SUCCESS;
        //return deleteByIds(ids,true);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param workshiftm 要删除的model
     * @param kv         携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(Workshiftm workshiftm, Kv kv) {
        //addDeleteSystemLog(workshiftm.getIautoid(), JBoltUserKit.getUserId(),workshiftm.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param workshiftm 要删除的model
     * @param kv         携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(Workshiftm workshiftm, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(workshiftm, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 切换isdeleted属性
     */
    public Ret toggleIsdeleted(Long id) {
        return toggleBoolean(id, "isDeleted");
    }

    /**
     * 切换isenabled属性
     */
    public Ret toggleIsenabled(Long id) {
        return toggleBoolean(id, "isEnabled");
    }

    /**
     * 检测是否可以toggle操作指定列
     *
     * @param workshiftm 要toggle的model
     * @param column     操作的哪一列
     * @param kv         携带额外参数一般用不上
     */
    @Override
    public String checkCanToggle(Workshiftm workshiftm, String column, Kv kv) {
        //没有问题就返回null  有问题就返回提示string 字符串
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(Workshiftm workshiftm, String column, Kv kv) {
        //addUpdateSystemLog(workshiftm.getIautoid(), JBoltUserKit.getUserId(), workshiftm.getName(),"的字段["+column+"]值:"+workshiftm.get(column));
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param workshiftm model
     * @param kv         携带额外参数一般用不上
     */
    @Override
    public String checkInUse(Workshiftm workshiftm, Kv kv) {
        //这里用来覆盖 检测Workshiftm是否被其它表引用
        return null;
    }

    public Workshiftm findWorkShiftCodeInfo(String workShiftCode) {
        return dao().findFirst("SELECT * FROM Bd_WorkShiftM WHERE cWorkShiftCode = ?", workShiftCode);
    }

    public Ret updateEditTable(JBoltTable jBoltTable, Long userId, Date now) {
        Workshiftm workshiftm = jBoltTable.getFormModel(Workshiftm.class, "workshiftm");
        ValidationUtils.notNull(workshiftm, JBoltMsg.PARAM_ERROR);

        tx(() -> {

            //修改
            if (isOk(workshiftm.getIautoid())) {
                workshiftm.setIupdateby(JBoltUserKit.getUserId());
                workshiftm.setDupdatetime(now);
                workshiftm.setCupdatename(JBoltUserKit.getUserName());
                ValidationUtils.isTrue(workshiftm.update(), JBoltMsg.FAIL);
            } else {
                //新增
                //编码是否存在
                ValidationUtils.isTrue(findWorkShiftCodeInfo(workshiftm.getCworkshiftcode()) == null, "编码重复");
                workshiftm.setIautoid(JBoltSnowflakeKit.me.nextId());
                workshiftm.setIcreateby(JBoltUserKit.getUserId());
                workshiftm.setCcreatename(JBoltUserKit.getUserName());
                workshiftm.setDcreatetime(now);
                workshiftm.setIupdateby(JBoltUserKit.getUserId());
                workshiftm.setCupdatename(JBoltUserKit.getUserName());
                workshiftm.setDupdatetime(now);

                workshiftm.setCorgcode(getOrgCode());
                workshiftm.setCorgname(getOrgName());
                workshiftm.setIorgid(getOrgId());

                ValidationUtils.isTrue(workshiftm.save(), JBoltMsg.FAIL);
            }
            //新增
            List<Workshiftd> saveRecords = jBoltTable.getSaveModelList(Workshiftd.class);
            if (CollUtil.isNotEmpty(saveRecords)) {
                for (Workshiftd row : saveRecords) {
                    row.setIworkshiftmid(workshiftm.getIautoid());
                }
                workshiftdService.batchSave(saveRecords, 500);
            }

            //修改
            List<Workshiftd> updateRecords = jBoltTable.getUpdateModelList(Workshiftd.class);
            if (CollUtil.isNotEmpty(updateRecords)) {
                workshiftdService.batchUpdate(updateRecords, 500);
            }

            // 删除
            Object[] deletes = jBoltTable.getDelete();
            if (ArrayUtil.isNotEmpty(deletes)) {
                workshiftdService.deleteMultiByIds(deletes);
            }

            return true;
        });

        return SUCCESS;
    }

    public boolean notExists(String cworkshiftcode) {
        return null == queryInt("SELECT TOP 1 1 FROM Bd_WorkshiftM WHERE cworkshiftcode = ? AND IsDeleted = ? AND iOrgId = ? ", cworkshiftcode, ZERO_STR, getOrgId());
    }

    public Ret importExcelData(File file) {
        // 使用字段配置维护
        List<Record> datas = CusFieldsMappingdCache.ME.getImportRecordsByTableName(file, table());
        ValidationUtils.notEmpty(datas, "导入数据不能为空");
        
        List<Workshiftm> workshiftmList = new ArrayList<>();
        List<Workshiftd> workshiftdList = new ArrayList<>();
        
        Date now = new Date();
        
        Set<Kv> workshiftmsSet = new HashSet<>();
        
        // 处理主表数据
        for (Record p : datas) {
            String cworkshiftcode = p.getStr("cworkshiftcode");
            
            ValidationUtils.notBlank(cworkshiftcode, "编码为空！");
            ValidationUtils.notBlank(p.getStr("cworkshiftname"), "名称为空！");
            ValidationUtils.notBlank(p.getStr("dstarttime"), "出勤开始时间为空！");
            ValidationUtils.notBlank(p.getStr("dendtime"), "出勤结束时间为空！");
            
            ValidationUtils.isTrue(notExists(cworkshiftcode), cworkshiftcode + "编码重复");
            
            workshiftmsSet.add(Kv.by("cworkshiftcode", p.getStr("cworkshiftcode"))
                    .set("cworkshiftname", p.getStr("cworkshiftname"))
                    .set("dstarttime", p.getStr("dstarttime"))
                    .set("dendtime", p.getStr("dendtime"))
                    .set("cmemo", p.getStr("cmemo")));
        }

        for (Kv kv : workshiftmsSet) {
            Workshiftm workshiftm = new Workshiftm();
            
            workshiftm.setIautoid(JBoltSnowflakeKit.me.nextId())
                    .setCworkshiftcode(kv.getStr("cworkshiftcode"))
                    .setCworkshiftname(kv.getStr("cworkshiftname"))
                    .setDstarttime(kv.getStr("dstarttime"))
                    .setDendtime(kv.getStr("dendtime"))
                    .setCmemo(kv.getStr("cmemo"))
                    .setIcreateby(JBoltUserKit.getUserId())
                    .setCcreatename(JBoltUserKit.getUserName())
                    .setDcreatetime(now)
                    .setIsenabled(true)
                    .setIupdateby(JBoltUserKit.getUserId())
                    .setCupdatename(JBoltUserKit.getUserName())
                    .setDupdatetime(now)
                    .setCorgcode(getOrgCode())
                    .setCorgname(getOrgName())
                    .setIorgid(getOrgId());
            workshiftmList.add(workshiftm);

            // 处理细表
            for (Record p : datas) {
                if (isOk(p.getInt("itype")) && ObjUtil.equals(p.getStr("cworkshiftcode"), workshiftm.getCworkshiftcode())) {
                    Workshiftd workshiftd = new Workshiftd();
                    workshiftd.setIworkshiftmid(workshiftm.getIautoid())
                            .setDstarttime(p.getStr("dstarttimed"))
                            .setDendtime(p.getStr("dendtimed"))
                            .setCmemo(p.getStr("cmemo"))
                            .setItype(p.getInt("itype"));

                    // 将时间字符串转换为 Date 对象
                    Date date1 = DateUtil.parse(workshiftd.getDstarttime(), "HH:mm");
                    Date date2 = DateUtil.parse(workshiftd.getDendtime(), "HH:mm");
                    
                    // 两个时间相差分钟
                    workshiftd.setIminute((int) DateUtil.between(date1, date2, DateUnit.MINUTE));
                    workshiftdList.add(workshiftd);
                }
            }
        }

        tx(() -> {
            batchSave(workshiftmList);
            
            workshiftdService.batchSave(workshiftdList);
            
            return true;
        });

        return SUCCESS;
    }

    public Workshiftm findByName(String cworkshiftname) {
        return findFirst(selectSql().eq("isDeleted", "0").eq("isEnabled", "1").eq("cWorkShiftName", cworkshiftname));
    }
}
