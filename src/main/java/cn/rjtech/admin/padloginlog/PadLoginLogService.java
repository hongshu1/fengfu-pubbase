package cn.rjtech.admin.padloginlog;

import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.PadLoginLog;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * 日志监控-平板登录日志
 *
 * @ClassName: PadLoginLogService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-10 10:35
 */
public class PadLoginLogService extends BaseService<PadLoginLog> {
    
    private final PadLoginLog dao = new PadLoginLog().dao();

    @Override
    protected PadLoginLog dao() {
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
     * @param kv         关键词
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
        return dbTemplate("padloginlog.list", kv).paginate(pageNumber, pageSize);
    }

}