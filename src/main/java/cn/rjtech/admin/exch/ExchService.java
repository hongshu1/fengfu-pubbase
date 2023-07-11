package cn.rjtech.admin.exch;

import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.util.JBoltStringUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.enums.IsEnableEnum;
import cn.rjtech.model.momdata.Exch;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
/**
 * 币种汇率档案 Service
 * @ClassName: ExchService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-22 19:40
 */
public class ExchService extends BaseService<Exch> {

	private final Exch dao = new Exch().dao();

	@Override
	protected Exch dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<Exch> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param exch
	 * @return
	 */
	public Ret save(Exch exch) {
		if(exch==null || isOk(exch.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(exch.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=exch.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(exch.getIautoid(), JBoltUserKit.getUserId(), exch.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param exch
	 * @return
	 */
	public Ret update(Exch exch) {
		if(exch==null || notOk(exch.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		Exch dbExch=findById(exch.getIAutoId());
		if(dbExch==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(exch.getName(), exch.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=exch.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(exch.getIautoid(), JBoltUserKit.getUserId(), exch.getName());
		}
		return ret(success);
	}

	/**
	 * 删除 指定多个ID
	 * @param ids
	 * @return
	 */
	public Ret deleteByBatchIds(String ids) {
		return deleteByIds(ids,true);
	}

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public Ret delete(Long id) {
		return deleteById(id,true);
	}

	/**
	 * 删除数据后执行的回调
	 * @param exch 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(Exch exch, Kv kv) {
		//addDeleteSystemLog(exch.getIautoid(), JBoltUserKit.getUserId(),exch.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param exch 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(Exch exch, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(exch, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType 
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

	/**
	 * 切换isdeleted属性
	 */
	public Ret toggleIsDeleted(Long id) {
		return toggleBoolean(id, "isDeleted");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param exch 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(Exch exch,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(Exch exch, String column, Kv kv) {
		//addUpdateSystemLog(exch.getIautoid(), JBoltUserKit.getUserId(), exch.getName(),"的字段["+column+"]值:"+exch.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param exch model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(Exch exch, Kv kv) {
		//这里用来覆盖 检测Exch是否被其它表引用
		return null;
	}

    /**
     * 获取汇率
     * @return
     */
    public Record getNflat(String cexchname) {
        if (JBoltStringUtil.isBlank(cexchname)) {
            return new Record().set("nflat", null);
        }
        return dbTemplate("exch.getNflat", Kv.by("cexchname", cexchname).set("iorgid",getOrgId()).set("isdeleted",ZERO_STR)).findFirst();
    }

        public Exch getNameByLatestExch(Long orgId, String name){
            if (StrUtil.isBlank(name)){
                return null;
            }
            String sqlStr ="SELECT TOP 1 " +
                    "ex.iYear, " +
                    "ex.iperiod, " +
                    "ex.nflat " +
                    "FROM " +
                    "Bd_Exch ex " +
                    "WHERE " +
                    "ex.iOrgId = ? " +
                    "AND ex.cexch_name = ? " +
                    "AND ex.isDeleted = '0' " +
                    "ORDER BY " +
                    "iYear DESC, " +
                    "iperiod DESC";
            return findFirst(sqlStr, orgId, name);
        }
        
    public Exch findUniqueExch(Long iorgid,String cexchName,Integer itype,String cdate,Integer iyear){
    	return findFirst(selectSql().eq("iorgid", iorgid).eq("cexch_name", cexchName).eq("itype", itype).eq("cdate", cdate).eq("iyear", iyear).eq("isdeleted", IsEnableEnum.NO.getValue()));
    }

	public void deleteByCexchName(String cexchName) {
		delete(deleteSql().eq("cexch_name", cexchName));
	}
}