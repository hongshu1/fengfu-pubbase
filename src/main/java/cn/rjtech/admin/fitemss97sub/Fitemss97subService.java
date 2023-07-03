package cn.rjtech.admin.fitemss97sub;

import cn.hutool.core.util.ObjUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.bean.JsTreeBean;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.model.momdata.Fitemss97sub;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 项目管理大类项目子目录 Service
 * @ClassName: Fitemss97subService
 * @author: heming
 * @date: 2023-03-27 10:22
 */
public class Fitemss97subService extends BaseService<Fitemss97sub> {

	private final Fitemss97sub dao = new Fitemss97sub().dao();

	@Override
	protected Fitemss97sub dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv para) {

           List<Record> list =new ArrayList<>();
		if(ObjUtil.isNull(para.getStr("fitemss97subid"))){
			List<Record> fitemss98list = dbTemplate("fitemss97sub.fitemss98list", para).find();
			List<Record> fitemssZFlist = dbTemplate("fitemss97sub.fitemssZFlist", para).find();
			List<Record> fitemss03list = dbTemplate("fitemss97sub.fitemss03list", para).find();
			List<Record> fitemss00list = dbTemplate("fitemss97sub.fitemss00list", para).find();
			list=Stream.of(fitemss00list,fitemss98list, fitemss03list,fitemssZFlist ).flatMap(Collection::stream).collect(Collectors.toList());
		}else{
			Record first = dbTemplate("fitemss97.findfitemss97List", Kv.by("fitemss97subid", para.getStr("fitemss97subid"))).findFirst();
			/**
			 * 查找子项
			 */
			String fitemss97subid = selectSub(para.getStr("fitemss97subid"));
			para.set("fitemss97subid",fitemss97subid);
				switch (first.getStr("citemccode")){
					case "00":
						List<Record> fitemss00list = dbTemplate("fitemss97sub.fitemss00list", para).find();
						list=Stream.of(fitemss00list).flatMap(Collection::stream).collect(Collectors.toList());
						break;
					case "98":
						List<Record> fitemss98list = dbTemplate("fitemss97sub.fitemss98list", para).find();
						list=Stream.of(fitemss98list).flatMap(Collection::stream).collect(Collectors.toList());
						break;
					case "03":
						List<Record> fitemss03list = dbTemplate("fitemss97sub.fitemss03list", para).find();
						list=Stream.of(fitemss03list).flatMap(Collection::stream).collect(Collectors.toList());
						break;
					case "ZF":
						List<Record> fitemssZFlist = dbTemplate("fitemss97sub.fitemssZFlist", para).find();
						list=Stream.of(fitemssZFlist).flatMap(Collection::stream).collect(Collectors.toList());
						break;
					}

		}
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


	//判断是否有有子项，有的话把子项全找出来
    public String selectSub(String fitemss97subid) {
		List<Record> list = dbTemplate("fitemss97.findfitemss97sub", Kv.by("isourceid", fitemss97subid)).find();
		if(list.size()!=0){
			StringJoiner stringJoiner=new StringJoiner(",");
			for (Record record: list){
				List<Record> subList = dbTemplate("fitemss97.findfitemss97sub", Kv.by("isourceid",  record.get("iautoid"))).find();
				if(subList.size()!=0){
					for (Record sub :subList) {
						String iautoid = sub.getStr("iautoid");
						stringJoiner.add(iautoid);
					}
				}else{
					    stringJoiner.add(record.getStr("iautoid"));
				}

			}
			return stringJoiner.toString();
		}
			return fitemss97subid;
	}


	/**
	 * 得到后台分类数据树 包含所有数据
	 *
	 *
	 * @param openLevel 打开级别
	 */
	public List<JsTreeBean> getMgrTree(int openLevel, String sn) {

		List<JsTreeBean> jsTreeBeanList = new ArrayList<>();

			List<Record> subRecords = dbTemplate("fitemss97.findfitemss97List",Kv.by("citemccode",sn)).find();
			for (Record subRecord : subRecords) {
				Long id = subRecord.getLong("iAutoId");
				Object pid = subRecord.getStr("iSourceId");
				String text = "[" + subRecord.getStr("citemcode") + "]" + subRecord.getStr("citemname");
				String type = subRecord.getStr("citemcode");
				JsTreeBean jsTreeBean = new JsTreeBean(id, pid, text, type, "", false);
				jsTreeBeanList.add(jsTreeBean);
		}


		return jsTreeBeanList;
	}


	/**
	 * 保存
	 * @param fitemss97sub
	 * @return
	 */
	public Ret save(Fitemss97sub fitemss97sub) {
		if(fitemss97sub==null || isOk(fitemss97sub.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		fitemss97sub.setIOrgId(getOrgId());
		fitemss97sub.setCOrgCode(getOrgCode());
		fitemss97sub.setCOrgName(getOrgName());
		User user = JBoltUserKit.getUser();
		Date now = new Date();
		fitemss97sub.setICreateBy(user.getId());
		fitemss97sub.setCCreateName(user.getName());
		fitemss97sub.setDCreateTime(now);
		fitemss97sub.setIUpdateBy(user.getId());
		fitemss97sub.setCUpdateName(user.getName());
		fitemss97sub.setDUpdateTime(now);
		fitemss97sub.setISource(SourceEnum.MES.getValue());
		fitemss97sub.setIsDeleted(false);
		boolean success=fitemss97sub.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(fitemss97sub.getIautoid(), JBoltUserKit.getUserId(), fitemss97sub.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param fitemss97sub
	 * @return
	 */
	public Ret update(Fitemss97sub fitemss97sub) {
		if(fitemss97sub==null || notOk(fitemss97sub.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		Fitemss97sub dbFitemss97sub=findById(fitemss97sub.getIAutoId());
		if(dbFitemss97sub==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		User user = JBoltUserKit.getUser();
		Date now = new Date();
		fitemss97sub.setIUpdateBy(user.getId());
		fitemss97sub.setCUpdateName(user.getName());
		fitemss97sub.setDUpdateTime(now);
		boolean success=fitemss97sub.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(fitemss97sub.getIautoid(), JBoltUserKit.getUserId(), fitemss97sub.getName());
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
		return updateColumn(id, "isdeleted", true);
	}

	/**
	 * 删除数据后执行的回调
	 * @param fitemss97sub 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(Fitemss97sub fitemss97sub, Kv kv) {
		//addDeleteSystemLog(fitemss97sub.getIautoid(), JBoltUserKit.getUserId(),fitemss97sub.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param fitemss97sub 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(Fitemss97sub fitemss97sub, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(fitemss97sub, kv);
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
		return toggleBoolean(id, "IsDeleted");
	}
	/**
	 * 切换iclose属性
	 */
	public Ret toggleIclose(Long id) {
		return toggleBoolean(id, "iclose");
	}
	/**
	 * 检测是否可以toggle操作指定列
	 * @param fitemss97sub 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(Fitemss97sub fitemss97sub,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(Fitemss97sub fitemss97sub, String column, Kv kv) {
		//addUpdateSystemLog(fitemss97sub.getIautoid(), JBoltUserKit.getUserId(), fitemss97sub.getName(),"的字段["+column+"]值:"+fitemss97sub.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param fitemss97sub model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(Fitemss97sub fitemss97sub, Kv kv) {
		//这里用来覆盖 检测Fitemss97sub是否被其它表引用
		return null;
	}

}