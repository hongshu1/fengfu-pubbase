package cn.rjtech.admin.containerStockInD;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.base.service.BaseService;
import cn.rjtech.model.momdata.ContainerStockOutM;
import com.jfinal.kit.Ret;

import java.util.Date;

/**
 * 容器出库表service
 */
public class ContainerStockOutMService extends BaseService<ContainerStockOutM> {
    private final ContainerStockOutM dao=new ContainerStockOutM().dao();

    @Override
    public int systemLogTargetType() { return ProjectSystemLogTargetType.NONE.getValue(); }

    @Override
    protected ContainerStockOutM dao() {
        return null;
    }

    /**
     * 保存
     * @param
     * @return
     */
    public Ret save(ContainerStockOutM stockOutM) {
        if(stockOutM==null || isOk(stockOutM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(container.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        //创建信息
        stockOutM.setICreateBy(JBoltUserKit.getUserId());
        stockOutM.setCCreateName(JBoltUserKit.getUserName());
        stockOutM.setDCreateTime(new Date());

        //更新信息
        stockOutM.setIUpdateBy(JBoltUserKit.getUserId());
        stockOutM.setCUpdateName(JBoltUserKit.getUserName());
        stockOutM.setDUpdateTime(new Date());

        //组织信息
        stockOutM.setCOrgCode(getOrgCode());
        stockOutM.setCOrgName(getOrgName());
        stockOutM.setIOrgId(getOrgId());

        boolean success=stockOutM.save();
        if(success) {
            //添加日志
            //addSaveSystemLog(container.getIAutoId(), JBoltUserKit.getUserId(), container.getName());
        }
        return ret(success);
    }
}
