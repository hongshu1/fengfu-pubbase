package cn.rjtech.admin.syspuinstore;

import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.model.momdata.SysPuinstore;
import cn.rjtech.model.momdata.SysPuinstoredetail;
import cn.rjtech.u9.entity.syspuinstore.SysPuinstoreDTO;
import cn.rjtech.u9.entity.syspuinstore.SysPuinstoreDTO.Main;
import cn.rjtech.u9.entity.syspuinstore.SysPuinstoreDTO.PreAllocate;
import cn.rjtech.util.BaseInU8Util;
import cn.rjtech.util.ValidationUtils;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;

import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;

import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;

import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 采购入库单
 *
 * @ClassName: SysPuinstoreService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 15:38
 */
public class SysPuinstoreService extends BaseService<SysPuinstore> {

    private final SysPuinstore dao = new SysPuinstore().dao();

    @Override
    protected SysPuinstore dao() {
        return dao;
    }

    @Inject
    private SysPuinstoredetailService syspuinstoredetailservice;

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber      第几页
     * @param pageSize        每页几条数据
     * @param keywords        关键词
     * @param BillType        到货单类型;采购PO  委外OM
     * @param procureType     采购类型
     * @param warehousingType 入库类别
     */
    public Page<SysPuinstore> getAdminDatas(int pageNumber, int pageSize, String keywords, String BillType, String procureType,
                                            String warehousingType) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eq("BillType", BillType);
        sql.eq("procureType", procureType);
        sql.eq("warehousingType", warehousingType);
        //关键词模糊查询
        sql.likeMulti(keywords, "repositoryName", "deptName", "remark");
        //排序
        sql.desc("AutoID");
        return paginate(sql);
    }

    /**
     * 保存
     */
    public Ret save(SysPuinstore sysPuinstore) {
        if (sysPuinstore == null || isOk(sysPuinstore.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(sysPuinstore.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = sysPuinstore.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(sysPuinstore.getAutoID(), JBoltUserKit.getUserId(), sysPuinstore.getName());
        }
        return ret(success);
    }

    /*
     * 审核
     * */
    public Ret autit(Long autoid) {
        SysPuinstore sysPuinstore = findById(autoid);
        //1、更新审核人、审核时间、状态
        sysPuinstore.setAuditPerson(JBoltUserKit.getUserName());
        sysPuinstore.setAuditDate(new Date());
        sysPuinstore.setState("2");
        //2、推送u8入库
        String json = getSysPuinstoreDto(sysPuinstore);
        String post = new BaseInU8Util().base_in(json);
        System.out.println(post);
        Ret ret = update(sysPuinstore);
        return ret;
    }

    /**
     * 更新
     */
    public Ret update(SysPuinstore sysPuinstore) {
        if (sysPuinstore == null || notOk(sysPuinstore.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        SysPuinstore dbSysPuinstore = findById(sysPuinstore.getAutoID());
        if (dbSysPuinstore == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(sysPuinstore.getName(), sysPuinstore.getAutoID())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = sysPuinstore.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(sysPuinstore.getAutoID(), JBoltUserKit.getUserId(), sysPuinstore.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param sysPuinstore 要删除的model
     * @param kv           携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(SysPuinstore sysPuinstore, Kv kv) {
        //addDeleteSystemLog(sysPuinstore.getAutoID(), JBoltUserKit.getUserId(),sysPuinstore.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param sysPuinstore model
     * @param kv           携带额外参数一般用不上
     */
    @Override
    public String checkInUse(SysPuinstore sysPuinstore, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(SysPuinstore sysPuinstore, String column, Kv kv) {
        //addUpdateSystemLog(sysPuinstore.getAutoID(), JBoltUserKit.getUserId(), sysPuinstore.getName(),"的字段["+column+"]值:"+sysPuinstore.get(column));
        /**
         switch(column){
         case "IsDeleted":
         break;
         }
         */
        return null;
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
        return dbTemplate("syspuinstore.recpor", kv).paginate(pageNumber, pageSize);
    }

    /**
     * 删除
     */
    public Ret delete(Long id) {
        tx(() -> {
            //从表的数据
            deleteSysPuinstoredetailByMasId(String.valueOf(id));
            //删除主表数据
            delete(id);
            return true;
        });
        return ret(true);
    }

    public Ret deleteSysPuinstoredetailByMasId(String id) {
        List<SysPuinstoredetail> puinstoredetails = syspuinstoredetailservice.findDetailByMasID(id);
        if (!puinstoredetails.isEmpty()) {
            List<String> collect = puinstoredetails.stream().map(SysPuinstoredetail::getAutoID).collect(Collectors.toList());
            syspuinstoredetailservice.deleteByIds(String.join(",", collect));
        }
        return ret(true);
    }

    /**
     * 批量删除主从表
     */
    public Ret deleteRmRdByIds(String ids) {
        tx(() -> {
            String[] split = ids.split(",");
            for (String id : split) {
                //删除从表
                deleteSysPuinstoredetailByMasId(id);
            }
            //删除主表
            deleteByIds(split);
            return true;
        });
        return ret(true);
    }

    /**
     * 执行JBoltTable表格整体提交
     */
    public Ret submitByJBoltTable(JBoltTable jBoltTable) {
        SysPuinstore sysPuinstore = jBoltTable.getFormModel(SysPuinstore.class);
        //获取当前用户信息？
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        tx(() -> {
            //1、如果存在autoid，更新
            if (isOk(sysPuinstore.getAutoID())) {
                //更新主表

                //更明细表
            } else {
                //2、否则新增
                //新增主表
                Record record = jBoltTable.getFormRecord();
                SysPuinstore puinstore = new SysPuinstore();
                saveSysPuinstoreModel(puinstore, record);
                ValidationUtils.isTrue(puinstore.save(), JBoltMsg.FAIL);
                //新增明细表
                List<Record> saveRecordList = jBoltTable.getSaveRecordList();
                List<SysPuinstoredetail> detailList = new ArrayList<>();
                syspuinstoredetailservice
                    .saveSysPuinstoredetailModel(detailList, saveRecordList, puinstore, record.get("whcode"));
                int[] ints = syspuinstoredetailservice.batchSave(detailList);
                System.out.println(ints);
//                ValidationUtils.notNull(syspuinstoredetailservice.batchSave(detailList), JBoltMsg.FAIL);
            }
            return true;
        });
        return SUCCESS;
    }

    public void saveSysPuinstoreModel(SysPuinstore puinstore, Record record) {
        Date date = new Date();
        puinstore.setAutoID(String.valueOf(JBoltSnowflakeKit.me.nextId()));
        puinstore.setBillType(record.get("billtype"));//采购类型
        puinstore.setOrganizeCode(getOrgCode());
        puinstore.setBillNo(record.get("billno"));
//        puinstore.setBillDate(record.get("")); //单据日期
        puinstore.setRdCode(record.get("rdcode"));
        puinstore.setDeptCode(record.get("deptcode"));
        puinstore.setSourceBillNo(record.get("sourcebillno"));//来源单号（订单号）
        puinstore.setSourceBillID(record.get("sourcebillno"));//来源单据id
        puinstore.setVenCode(record.get("vencode"));
        puinstore.setMemo(record.get("memo"));
        puinstore.setCreatePerson(JBoltUserKit.getUserName());
        puinstore.setCreateDate(date);
        puinstore.setAuditPerson(getOrgName()); //审核人
        puinstore.setAuditDate(date); //审核时间
        puinstore.setModifyPerson(JBoltUserKit.getUserName());
        puinstore.setModifyDate(date);
        puinstore.setState("1");

    }

    /*
     * 获取仓库名
     * */
    public List<Record> getWareHouseName(Kv kv) {
        return dbTemplate(u8SourceConfigName(), "syspuinstore.getWareHouseName", kv).find();
    }

    /*
     * 组推送u8的json
     * */
    public String getSysPuinstoreDto(SysPuinstore puinstore) {
        String json = "";
        SysPuinstoreDTO dto = new SysPuinstoreDTO();
        //主数据
        ArrayList<Main> MainData = new ArrayList<>();
        Main main = new Main();

        MainData.add(main);
        //其它数据
        PreAllocate preAllocate = new PreAllocate();
        preAllocate.setCreatePerson(puinstore.getCreatePerson());
        preAllocate.setCreatePersonName(puinstore.getCreatePerson());
        preAllocate.setLoginDate(String.valueOf(puinstore.getCreateDate()));
        preAllocate.setOrganizeCode(puinstore.getOrganizeCode());
        preAllocate.setTag("PUInStore");
        preAllocate.setType("PUInStore");
        preAllocate.setUserCode(puinstore.getCreatePerson());
        //放入dto
        dto.setMainData(MainData);
        dto.setPreAllocate(preAllocate);
        dto.setUserCode(puinstore.getCreatePerson());
        dto.setOrganizeCode(puinstore.getOrganizeCode());
        dto.setToken("");
        //返回
        return JSON.toJSONString(dto);
    }
}