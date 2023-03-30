package cn.rjtech.admin.ContainerStockInD;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.ContainerStockInM;
import com.jfinal.kit.Ret;

import java.util.Date;
import java.util.List;

/**
 * 容器档案-入库主表
 */
public class ContainerStockInMService extends BaseService<ContainerStockInM> {
    private final ContainerStockInM dao=new ContainerStockInM().dao();
    @Override
    protected ContainerStockInM dao() { return dao; }

    @Override
    protected int systemLogTargetType() { return ProjectSystemLogTargetType.NONE.getValue(); }

    /**
     * 保存
     * @param
     * @return
     */
    public Ret save(ContainerStockInM stockInM) {
        if(stockInM==null || isOk(stockInM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(container.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        //创建信息
        stockInM.setICreateBy(JBoltUserKit.getUserId());
        stockInM.setCCreateName(JBoltUserKit.getUserName());
        stockInM.setDCreateTime(new Date());

        //更新信息
        stockInM.setIUpdateBy(JBoltUserKit.getUserId());
        stockInM.setCUpdateName(JBoltUserKit.getUserName());
        stockInM.setDUpdateTime(new Date());

        //组织信息
        stockInM.setCOrgCode(getOrgCode());
        stockInM.setCOrgName(getOrgName());
        stockInM.setIOrgId(getOrgId());
        boolean success=stockInM.save();
        if(success) {
            //添加日志
            //addSaveSystemLog(container.getIAutoId(), JBoltUserKit.getUserId(), container.getName());
        }
        return ret(success);
    }

    /**
     * 批量删除
     * @param stockInMIds
     */
    public void deleteByStockInMIds(List<String> stockInMIds) {
        update("UPDATE Bd_ContainerStockInM SET isDeleted = 1 WHERE iAutoId IN (" +stockInMIds + ") ");
    }
}
