package cn.rjtech.admin.commonmenu;

import cn.hutool.core.collection.CollUtil;
import cn.jbolt._admin.permission.PermissionService;
import cn.jbolt.common.model.CommonMenu;
import cn.jbolt.core.base.JBoltGlobalConfigKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltGlobalConfigCache;
import cn.jbolt.core.cache.JBoltPermissionCache;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.Permission;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.util.JBoltStringUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.CommonMenuTypeEnum;
import cn.rjtech.enums.IsEnableEnum;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.hutool.core.text.StrPool.COMMA;
/**
 * 常用菜单 Service
 * @ClassName: CommonMenuService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-19 21:02
 */
public class CommonMenuService extends BaseService<CommonMenu> {

	@Inject
	private PermissionService permissionService;
	
	private final CommonMenu dao = new CommonMenu().dao();
	
	@Override
	protected CommonMenu dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<CommonMenu> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("id","DESC", pageNumber, pageSize, keywords, "id");
	}

	/**
	 * 保存
	 * @param commonMenu
	 * @return
	 */
	public Ret save(CommonMenu commonMenu) {
		if(commonMenu==null || isOk(commonMenu.getId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(commonMenu.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=commonMenu.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(commonMenu.getId(), JBoltUserKit.getUserId(), commonMenu.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param commonMenu
	 * @return
	 */
	public Ret update(CommonMenu commonMenu) {
		if(commonMenu==null || notOk(commonMenu.getId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		CommonMenu dbCommonMenu=findById(commonMenu.getId());
		if(dbCommonMenu==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(commonMenu.getName(), commonMenu.getId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=commonMenu.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(commonMenu.getId(), JBoltUserKit.getUserId(), commonMenu.getName());
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
	 * @param commonMenu 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(CommonMenu commonMenu, Kv kv) {
		//addDeleteSystemLog(commonMenu.getId(), JBoltUserKit.getUserId(),commonMenu.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param commonMenu 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(CommonMenu commonMenu, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(commonMenu, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType 
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

	public List<ArrayList<Record>> getDashBoardCommonMenu(Kv para) {
		List<Record> commonMenuList = dbTemplate("commonmenu.getDashBoardCommonMenu",para).find();
		int commonMenuListSize = commonMenuList.size();
		double perRowNum = 6;
		double commonMenuRow = Math.ceil(commonMenuListSize / perRowNum);
		List<ArrayList<Record>> list = new ArrayList<ArrayList<Record>>();
		for (int i=0; i<(int) commonMenuRow; i++) {
			ArrayList<Record> aList = new ArrayList<Record>();
			if((i+1) * (int)perRowNum >= commonMenuListSize){
				for (int j = i*(int)perRowNum; j < commonMenuListSize; j++) {
					aList.add(commonMenuList.get(j));
				}
			}else{
				for (int j = i*(int)perRowNum; j < (i+1)*(int)perRowNum; j++) {
					aList.add(commonMenuList.get(j));
				}
			}
			list.add(aList);
		}
		return list;
	}
	/**
	 * 禀议系统的顶部导航配置的一级菜单下面的所有可跳转页面的末级菜单与当前登陆用户的菜单权限的交集
	 * */
	public List<Permission> findAllCanCheckedMenu() {
		//禀议系统的顶部导航配置的一级菜单下面的所有可跳转页面的末级菜单
		List<Permission> proposalTopNavMenuList = permissionService.findProposalTopNabMenu();
		List<Permission> topNavCanCheckedMenuList = new ArrayList<Permission>();
		getEndMenu(proposalTopNavMenuList,topNavCanCheckedMenuList);
		//当前登陆用户的菜单权限
		List<Permission> loginUserPermissionList = JBoltPermissionCache.me.getRoleMenus(JBoltUserKit.getUserId(), JBoltGlobalConfigCache.me.getLongConfigValue(JBoltGlobalConfigKey.MOM_APP_ID), JBoltGlobalConfigCache.me.getLongConfigValue(JBoltGlobalConfigKey.MOM_APPLICATION_ID), JBoltUserKit.getUserRoleIds());
		List<Permission> loginUserCanCheckedMenuList = new ArrayList<Permission>();
		getEndMenu(loginUserPermissionList,loginUserCanCheckedMenuList);
		//求交集
		List<Permission> allCanCheckedMenu = topNavCanCheckedMenuList.stream().filter(p1 -> loginUserCanCheckedMenuList.stream().anyMatch(p2 -> p1.getId().equals(p2.getId()))).collect(Collectors.toList());
		return allCanCheckedMenu;
	}
	private void getEndMenu(List<Permission> pList,List<Permission> endMenuList){
		if(CollUtil.isEmpty(pList)) return;
		for (Permission record : pList) {
			String url = record.getUrl();
			if(JBoltStringUtil.isNotBlank(url)) {
				endMenuList.add(record);
				continue;
			}
			Long permissionId = record.getId();
			List<Permission> nextLevelMenuList =permissionService.find(permissionService.selectSql().eq("pid", permissionId).eq("is_menu", IsEnableEnum.YES.getValue()).eq("is_deleted", IsEnableEnum.NO.getValue()));
			getEndMenu(nextLevelMenuList,endMenuList);
		}
	}
	/**
	 * 获取常用菜单id集合
	 * */
	public Ret findCheckedIds() {
		List<CommonMenu> list = find(selectSql().eq("type", CommonMenuTypeEnum.PROPOSAL.getValue()));
		List<Long> idList = list.stream().map(commonMenu->commonMenu.getMenuId()).collect(Collectors.toList());
        return successWithData(Okv.by("ids", CollUtil.join(idList, COMMA)));
    }
	
	public Ret saveTableDatas(Kv para) {
		String permissionId = para.getStr("permissionId");
		ValidationUtils.notBlank(permissionId, "菜单未勾选!");
		tx(()->{
			//删除禀议系统的常用菜单
			delete(deleteSql().eq("type", CommonMenuTypeEnum.PROPOSAL.getValue()));
			//重新生成禀议系统的常用菜单
			String[] permissionIdArr = permissionId.split(",");
			for (String menuId : permissionIdArr) {
				CommonMenu commonMenu = new CommonMenu(); 
				commonMenu.setType(CommonMenuTypeEnum.PROPOSAL.getValue());
				commonMenu.setMenuId(Long.parseLong(menuId));
				commonMenu.setUserId(JBoltUserKit.getUserId());
				ValidationUtils.isTrue(commonMenu.save(), ErrorMsg.SAVE_FAILED);
			}
			return true;
		});
		return SUCCESS;
	}
	
}