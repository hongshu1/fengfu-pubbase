package cn.rjtech.admin.purchaseattachment;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.PurchaseAttachment;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;
/**
 * 申购单附件 Service
 * @ClassName: PurchaseAttachmentService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-30 15:15
 */
public class PurchaseAttachmentService extends BaseService<PurchaseAttachment> {

	private final PurchaseAttachment dao = new PurchaseAttachment().dao();

	@Override
	protected PurchaseAttachment dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<PurchaseAttachment> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param purchaseAttachment
	 * @return
	 */
	public Ret save(PurchaseAttachment purchaseAttachment) {
		if(purchaseAttachment==null || isOk(purchaseAttachment.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(purchaseAttachment.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=purchaseAttachment.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(purchaseAttachment.getIautoid(), JBoltUserKit.getUserId(), purchaseAttachment.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param purchaseAttachment
	 * @return
	 */
	public Ret update(PurchaseAttachment purchaseAttachment) {
		if(purchaseAttachment==null || notOk(purchaseAttachment.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		PurchaseAttachment dbPurchaseAttachment=findById(purchaseAttachment.getIAutoId());
		if(dbPurchaseAttachment==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(purchaseAttachment.getName(), purchaseAttachment.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=purchaseAttachment.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(purchaseAttachment.getIautoid(), JBoltUserKit.getUserId(), purchaseAttachment.getName());
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
	 * @param purchaseAttachment 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(PurchaseAttachment purchaseAttachment, Kv kv) {
		//addDeleteSystemLog(purchaseAttachment.getIautoid(), JBoltUserKit.getUserId(),purchaseAttachment.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param purchaseAttachment 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(PurchaseAttachment purchaseAttachment, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(purchaseAttachment, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType 
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

    public List<PurchaseAttachment> getList(long ipurchasemid) {
        return find("SELECT * FROM PL_Purchase_Attachment WHERE ipurchasemid = ? ", ipurchasemid);
    }

	public void deleteByPurchaseId(Long ipurchasemid) {
		delete(deleteSql().eq("ipurchasemid", ipurchasemid));
	}

}