package cn.rjtech.admin.containerstockind;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.ContainerStockOutD;
import com.jfinal.kit.Ret;

/**
 * 容器出库详情表service
 */
public class ContainerStockOutDService extends BaseService<ContainerStockOutD> {
    private final ContainerStockOutD dao=new ContainerStockOutD().dao();

    @Override
    protected int systemLogTargetType() { return ProjectSystemLogTargetType.NONE.getValue(); }

    @Override
    protected ContainerStockOutD dao() {
        return null;
    }

    /**
     * 保存
     * @param
     * @return
     */
    public Ret save(ContainerStockOutD stockOutD) {
        if(stockOutD==null || isOk(stockOutD.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(container.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success=stockOutD.save();
        if(success) {
            //添加日志
            //addSaveSystemLog(container.getIAutoId(), JBoltUserKit.getUserId(), container.getName());
        }
        return ret(success);
    }
}
