package cn.jbolt._admin.msgcenter;

import cn.hutool.core.collection.CollUtil;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.core.service.JBoltDictionaryService;
import cn.rjtech.enums.DataSourceEnum;
import cn.rjtech.model.main.Application;
import cn.rjtech.util.ModelMap;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.common.model.SysMessageTemplate;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
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
public class SysMessageTemplateService extends BaseService<SysMessageTemplate> {

    private final SysMessageTemplate dao = new SysMessageTemplate().dao();

    @Override
    protected SysMessageTemplate dao() {
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
    public Page<SysMessageTemplate> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("id", "DESC", pageNumber, pageSize, keywords, "message_title");
    }

    /**
     * 保存
     *
     * @param sysMessageTemplate
     * @return
     */
    public Ret save(SysMessageTemplate sysMessageTemplate) {
        if (sysMessageTemplate == null || isOk(sysMessageTemplate.getId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(sysMessageTemplate.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = sysMessageTemplate.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(sysMessageTemplate.getId(), JBoltUserKit.getUserId(), sysMessageTemplate.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     *
     * @param sysMessageTemplate
     * @return
     */
    public Ret update(SysMessageTemplate sysMessageTemplate) {
        if (sysMessageTemplate == null || notOk(sysMessageTemplate.getId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        SysMessageTemplate dbSysMessageTemplate = findById(sysMessageTemplate.getId());
        if (dbSysMessageTemplate == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(sysMessageTemplate.getName(), sysMessageTemplate.getId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = sysMessageTemplate.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(sysMessageTemplate.getId(), JBoltUserKit.getUserId(), sysMessageTemplate.getName());
        }
        return ret(success);
    }

    /**
     * 检测是否可以删除
     *
     * @param sysMessageTemplate 要删除的model
     * @param kv                 携带额外参数一般用不上
     * @return
     */
    @Override
    public String checkCanDelete(SysMessageTemplate sysMessageTemplate, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(sysMessageTemplate, kv);
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
     * @param sysMessageTemplate 要toggle的model
     * @param column             操作的哪一列
     * @param kv                 携带额外参数一般用不上
     * @return
     */
    @Override
    public String checkCanToggle(SysMessageTemplate sysMessageTemplate, String column, Kv kv) {
        //没有问题就返回null  有问题就返回提示string 字符串
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(SysMessageTemplate sysMessageTemplate, String column, Kv kv) {
        //addUpdateSystemLog(sysMessageTemplate.getId(), JBoltUserKit.getUserId(), sysMessageTemplate.getName(),"的字段["+column+"]值:"+sysMessageTemplate.get(column));
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param sysMessageTemplate model
     * @param kv                 携带额外参数一般用不上
     * @return
     */
    @Override
    public String checkInUse(SysMessageTemplate sysMessageTemplate, Kv kv) {
        //这里用来覆盖 检测SysMessageTemplate是否被其它表引用
        return null;
    }

    public Page<SysMessageTemplate> adminDatas(Integer pageNumber, Integer pageSize, String keywords,
                                               Boolean isEnabled, Integer messageName, Integer messageChance) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eqBooleanToChar("is_on", isEnabled);
        sql.eq("message_name", messageName, true);
        sql.eq("message_chance", messageChance, true);
        //关键词模糊查询
        sql.likeMulti(keywords, "message_name");
        //排序
        sql.orderBy("id", true);

        Page<SysMessageTemplate> page = paginate(sql);
        //遍历字典,添加字典值
        if (page.getTotalRow() > 0) {
            List<Dictionary> listByTypeKeayName = new JBoltDictionaryService().getListByTypeKey("message_name");
            List<Dictionary> listByTypeKeyChance = new JBoltDictionaryService().getListByTypeKey("message_chance");
            Map<String, String> nameDict = addDictinary(listByTypeKeayName);
            Map<String, String> chanceDict = addDictinary(listByTypeKeyChance);

            List<SysMessageTemplate> pageList = page.getList();
            List<SysMessageTemplate> newList = new ArrayList<>();

            for (int i = 0; i < pageList.size(); i++) {
                SysMessageTemplate sysMessageTemplate = pageList.get(i);
                String nameKey = pageList.get(i).getMessageName().toString();
                if (nameKey!=null && nameDict.get(nameKey)!= null){
                    sysMessageTemplate.setMessageNameValue(nameDict.get(nameKey));
                }
                String chanceKey = pageList.get(i).getMessageChance().toString();
                if (chanceKey!=null &&chanceDict.get(chanceKey)!= null){
                    sysMessageTemplate.setMessageChanceValue(chanceDict.get(chanceKey));
                }
                newList.add(sysMessageTemplate);
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
}
