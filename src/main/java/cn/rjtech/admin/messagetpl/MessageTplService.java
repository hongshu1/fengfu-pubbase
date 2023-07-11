package cn.rjtech.admin.messagetpl;

import cn.jbolt._admin.dictionary.DictionaryTypeKey;
import cn.jbolt._admin.msgcenter.MessageUserService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.core.service.JBoltDictionaryService;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.MessageTpl;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息模板 Service
 *
 * @ClassName: SysMessageTemplateService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-31 15:33
 */
public class MessageTplService extends BaseService<MessageTpl> {

    private final MessageTpl dao = new MessageTpl().dao();
    @Inject
    private MessageUserService messageUserService;

    @Override
    protected MessageTpl dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public Page<MessageTpl> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("id", "DESC", pageNumber, pageSize, keywords, "message_title");
    }

    /**
     * 保存
     */
    public Ret save(MessageTpl messageTpl) {
        if (messageTpl == null || isOk(messageTpl.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        boolean success = messageTpl.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(sysMessageTemplate.getId(), JBoltUserKit.getUserId(), sysMessageTemplate.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(MessageTpl messageTpl) {
        if (messageTpl == null || notOk(messageTpl.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        MessageTpl dbMessageTpl = findById(messageTpl.getIAutoId());
        if (dbMessageTpl == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(sysMessageTemplate.getName(), sysMessageTemplate.getId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = messageTpl.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(sysMessageTemplate.getId(), JBoltUserKit.getUserId(), sysMessageTemplate.getName());
        }
        return ret(success);
    }

    /**
     * 检测是否可以删除
     *
     * @param messageTpl 要删除的model
     * @param kv         携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(MessageTpl messageTpl, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(messageTpl, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 切换delflag属性
     */
    public Ret toggleDelFlag(Long id) {
        return toggleBoolean(id, "del_flag");
    }

    /**
     * 切换ison属性
     */
    public Ret toggleIsOn(Long id) {
        return toggleBoolean(id, "is_on");
    }

    /**
     * 检测是否可以toggle操作指定列
     *
     * @param messageTpl 要toggle的model
     * @param column     操作的哪一列
     * @param kv         携带额外参数一般用不上
     */
    @Override
    public String checkCanToggle(MessageTpl messageTpl, String column, Kv kv) {
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(MessageTpl messageTpl, String column, Kv kv) {
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param messageTpl model
     * @param kv         携带额外参数一般用不上
     */
    @Override
    public String checkInUse(MessageTpl messageTpl, Kv kv) {
        //这里用来覆盖 检测SysMessageTemplate是否被其它表引用
        return null;
    }

    public Page<MessageTpl> adminDatas(Integer pageNumber, Integer pageSize, String keywords, Boolean isEnabled, Integer messageName, String cTrigger) {
        // 创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        // sql条件处理
        sql.eqBooleanToChar(MessageTpl.ISENABLED, isEnabled);
        sql.eq("message_name", messageName, true);
        sql.eq(MessageTpl.CTRIGGER, cTrigger, true);
        sql.eq(MessageTpl.ISDELETED, "0");
        
        // 关键词模糊查询
        sql.likeMulti(keywords, "message_title");
        // 排序
        sql.orderBy("id", false);

        Page<MessageTpl> page = paginate(sql);
        
        // 遍历字典,添加字典值
        if (page.getTotalRow() > 0) {
            List<Dictionary> listByTypeKeayName = new JBoltDictionaryService().getListByTypeKey(DictionaryTypeKey.message_name.name());
            List<Dictionary> listByTypeKeyChance = new JBoltDictionaryService().getListByTypeKey(DictionaryTypeKey.messagetpl_trigger.name());
            Map<String, String> nameDict = addDictinary(listByTypeKeayName);
            Map<String, String> chanceDict = addDictinary(listByTypeKeyChance);

            List<MessageTpl> pageList = page.getList();

            for (MessageTpl messageTpl : pageList) {
                String nameKey = messageTpl.getMessageNameValue();
                if (nameDict.get(nameKey) != null) {
                    messageTpl.setMessageNameValue(nameDict.get(nameKey));
                }
                String chanceKey = messageTpl.getMessageChanceValue();
                if (chanceDict.get(chanceKey) != null) {
                    messageTpl.setMessageChanceValue(chanceDict.get(chanceKey));
                }
            }
        }
        return page;
    }

    private Map<String, String> addDictinary(List<Dictionary> list) {
        Map<String, String> map = new HashMap<>();
        for (Dictionary dictionary : list) {
            map.put(dictionary.getSn(), dictionary.getName());
        }
        return map;
    }

    /**
     * 删除
     */
    public Ret deleteByAjax() {
        return SUCCESS;
    }

    /**
     * 表格提交
     */
    public Ret submitTable(JBoltTable jBoltTable) {
        MessageTpl messageTpl = jBoltTable.getFormModel(MessageTpl.class, "sysMessageTemplate");
        ValidationUtils.notNull(messageTpl, JBoltMsg.PARAM_ERROR);
        
        tx(() -> {
            Long userid = JBoltUserKit.getUserId();
            
            Date now = new Date();
            
            if (messageTpl.getIAutoId() == null) {
                messageTpl.setDCreateTime(now)
                        .setICreateBy(userid)
                        .setDUpdateTime(now)
                        .setIUpdateBy(userid);
                ValidationUtils.isTrue(messageTpl.save(), JBoltMsg.FAIL);
            } else {
                messageTpl.setIUpdateBy(userid).setDUpdateTime(now);
                ValidationUtils.isTrue(messageTpl.update(), JBoltMsg.FAIL);
            }
            
            // 新增人员
            messageUserService.addSubmitTableDatas(jBoltTable, messageTpl.getIAutoId());
            // 修改人员
            messageUserService.updateSubmitTableDatas(jBoltTable);
            // 删除人员
            messageUserService.deleteSubmitTableDatas(jBoltTable);
            
            return true;
        });
        return SUCCESS;
    }

    public List<Record> findMessage(String messageId) {
        return findRecord("SELECT mu.*,ju.username,ju.name,ju.email FROM jb_message_user mu " +
                        "LEFT JOIN jb_user ju on  mu.user_id = ju.id " +
                        "WHERE mu.message_id = ? and mu.del_fag = '0'", false, messageId);
    }
    
}
