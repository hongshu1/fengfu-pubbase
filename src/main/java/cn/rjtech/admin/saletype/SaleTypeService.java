package cn.rjtech.admin.saletype;


import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.model.momdata.SaleType;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 销售类型 Service
 *
 * @ClassName: SaleTypeService
 * @author: WYX
 * @date: 2023-03-28 11:04
 */
public class SaleTypeService extends BaseService<SaleType> {

    private final SaleType dao = new SaleType().dao();

    @Override
    protected SaleType dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     *
     * @param pageNumber
     * @param pageSize
     * @param keywords
     * @return
     */
    public Page<SaleType> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {

        List<SaleType> list = paginateByKeywords("iAutoId", "DESC", pageNumber, pageSize, keywords, "iAutoId").getList();
        return paginateByKeywords("iAutoId", "DESC", pageNumber, pageSize, keywords, "iAutoId");
    }

    public List<Record> selectData(Kv kv) {
        return  dbTemplate("saletype.selectData", kv).find();
    }


    public Page<Record> selectAll(int pageNumber, int pageSize,Kv kv) {
        List<Record> list = dbTemplate("saletype.selectData", kv).find();

        long totalRow;
        totalRow=list.size();
        int totalPage = (int) (totalRow / pageSize);
        if (totalRow % pageSize != 0) {
            totalPage++;
        }
        List<Record> recordArrayList = new ArrayList<>();
        int pageStart=pageNumber==1?0:(pageNumber-1)*pageSize;//截取的开始位置
        int pageEnd= Math.min((int) totalRow, pageNumber * pageSize);
        if(totalRow>pageStart){
            recordArrayList =list.subList(pageStart, pageEnd);
        }

        return  new Page<>(recordArrayList, pageNumber, pageSize, totalPage, (int) totalRow);
    }



    /**
     * 保存
     *
     * @param saleType
     * @return
     */
    public Ret save(SaleType saleType) {
        if (saleType == null || isOk(saleType.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        ValidationUtils.assertNull(findBycSTCode(saleType.getCSTCode()), "销售类型编码重复");

//		List attrs = find(selectSql().select("cRdCode").eq("iAutoId", saleType.getIRdStyleId()));
        setSaleTypeClass(saleType);

        //if(existsName(saleType.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = saleType.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(saleType.getIautoid(), JBoltUserKit.getUserId(), saleType.getName());
        }
        return ret(success);
    }

    /**
     * 查找销售类型编码
     *
     * @param cSTCode
     * @return
     */
    public SaleType findBycSTCode(String cSTCode) {
        return findFirst(Okv.by("cSTCode", cSTCode).set("isDeleted", false), "iautoid", "asc");
    }

    /**
     * 设置参数
     *
     * @param saleType
     * @return
     */
    private SaleType setSaleTypeClass(SaleType saleType) {
        saleType.setIsDeleted(false);
        Long userId = JBoltUserKit.getUserId();
        saleType.setICreateBy(userId);    //创建人ID
        saleType.setIUpdateBy(userId);    //更新人ID
        String userName = JBoltUserKit.getUserName();
        saleType.setCCreateName(userName);    //创建人名称
        saleType.setCUpdateName(userName);    //更新人名称
        Date date = new Date();
        saleType.setDCreateTime(date);    //创建时间
        saleType.setDUpdateTime(date);    //更新时间
        saleType.setISource(SourceEnum.MES.getValue());
        List<SaleType> crdcode = find("select cRdCode from Bd_Rd_Style where iAutoId = ?", saleType.getIRdStyleId());
        saleType.setCRdCode(crdcode.get(0).getCRdCode());    //0：下标，，第一个
        return saleType;
    }

    /**
     * 更新
     *
     * @param saleType
     * @return
     */
    public Ret update(SaleType saleType) {
        if (saleType == null || notOk(saleType.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        SaleType dbSaleType = findById(saleType.getIAutoId());
        if (dbSaleType == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(saleType.getName(), saleType.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = saleType.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(saleType.getIautoid(), JBoltUserKit.getUserId(), saleType.getName());
        }
        return ret(success);
    }

    /**
     * 删除 指定多个ID
     *
     * @param ids
     * @return
     */
    public Ret deleteByBatchIds(String ids) {
        return deleteByIds(ids, true);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public Ret delete(Long id) {
        return deleteById(id, true);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param saleType 要删除的model
     * @param kv       携带额外参数一般用不上
     * @return
     */
    @Override
    protected String afterDelete(SaleType saleType, Kv kv) {
        //addDeleteSystemLog(saleType.getIautoid(), JBoltUserKit.getUserId(),saleType.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param saleType 要删除的model
     * @param kv       携带额外参数一般用不上
     * @return
     */
    @Override
    public String checkCanDelete(SaleType saleType, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(saleType, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     *
     * @return
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 切换bstmpsmrp属性
     */
    public Ret toggleBstmpsMrp(Long id) {
        return toggleBoolean(id, "bSTMPS_MRP");
    }

    /**
     * 切换bdefault属性
     */
    public Ret toggleBDefault(Long id) {
        return toggleBoolean(id, "bDefault");
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
     * @param saleType 要toggle的model
     * @param column   操作的哪一列
     * @param kv       携带额外参数一般用不上
     * @return
     */
    @Override
    public String checkCanToggle(SaleType saleType, String column, Kv kv) {
        //没有问题就返回null  有问题就返回提示string 字符串
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(SaleType saleType, String column, Kv kv) {
        //addUpdateSystemLog(saleType.getIautoid(), JBoltUserKit.getUserId(), saleType.getName(),"的字段["+column+"]值:"+saleType.get(column));
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param saleType model
     * @param kv       携带额外参数一般用不上
     * @return
     */
    @Override
    public String checkInUse(SaleType saleType, Kv kv) {
        //这里用来覆盖 检测SaleType是否被其它表引用
        return null;
    }


}