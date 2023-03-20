package cn.rjtech.admin.qcitem;

import java.util.List;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.QcItem;
import cn.rjtech.util.ValidationUtils;

/**
 * 质量建模-检验项目（分类）
 *
 * @ClassName: QcItemService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-17 15:31
 */
public class QcItemService extends BaseService<QcItem> {

    private final QcItem dao = new QcItem().dao();

    @Override
    protected QcItem dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     * @param keywords   关键词
     * @param isDeleted  删除状态：0. 未删除 1. 已删除
     */
    public Page<QcItem> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isDeleted) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eqBooleanToChar("isDeleted", isDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords, "cOrgName", "cQcItemName", "cCreateName", "cUpdatName");
        //排序
        sql.desc("iAutoId");
        return paginate(sql);
    }

    /**
     * 保存
     */
    public Ret save(QcItem qcItem) {
        if (qcItem == null || isOk(qcItem.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //项目编码不能重复
        ValidationUtils.isTrue(findQcItemCode(qcItem.getCQcItemCode())==null, qcItem.getCQcItemCode() + "：项目编码重复");
        boolean success = qcItem.save();
        //给对象QcItem的其它栏位赋值
//		saveWorkClassHandle();
        return ret(success);
    }

    /*
     * 项目编码是否重复
     * */
    public Long findQcItemCode(String cqcItemCode) {
        return queryColumn(selectSql().select(QcItem.CQCITEMCODE).eq(QcItem.CQCITEMCODE, cqcItemCode));
    }

	/*public void saveWorkClassHandle(QcItem qcItem, Long icreateBy, String createName, String createTime){
//		qcItem.setCOrgCode();//组织编码
//		qcItem.setCCreateName();//组织名称
//		qcItem.setCQcItemCode();//检验项目编码
//		qcItem.setCQcItemName();//检验项目名称
		qcItem.setICreateBy(icreateBy);
		qcItem.setCCreateName();
		qcItem.setDCreateTime();
		qcItem.setIUpdateBy(icreateBy);
		qcItem.setCUpdatName();
		qcItem.setDUpdateTime()
//		qcItem.setIsDeleted(false);
	}*/

    /**
     * 更新
     */
    public Ret update(QcItem qcItem) {
        if (qcItem == null || notOk(qcItem.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        QcItem dbQcItem = findById(qcItem.getIAutoId());
        if (dbQcItem == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(qcItem.getName(), qcItem.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = qcItem.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(qcItem.getIAutoId(), JBoltUserKit.getUserId(), qcItem.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param qcItem 要删除的model
     * @param kv     携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(QcItem qcItem, Kv kv) {
        //addDeleteSystemLog(qcItem.getIAutoId(), JBoltUserKit.getUserId(),qcItem.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param qcItem model
     * @param kv     携带额外参数一般用不上
     */
    @Override
    public String checkInUse(QcItem qcItem, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(QcItem qcItem, String column, Kv kv) {
		/*addUpdateSystemLog(qcItem.getIAutoId(), JBoltUserKit.getUserId(), qcItem.getName(),"的字段["+column+"]值:"+qcItem.get(column));
		switch(column){
		    case "isDeleted":
		        break;
		}*/
        return null;
    }

    public List<Record> list(Kv kv) {
        return dbTemplate("qcItem.list", kv).find();
    }

}