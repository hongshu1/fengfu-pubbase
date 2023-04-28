package cn.rjtech.admin.containerstockind;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.ContainerStockInD;
import com.jfinal.kit.Ret;

import java.util.List;

/**
 * 容器档案-容器入库明细
 */
public class ContainerStockInDService extends BaseService<ContainerStockInD> {
    private final ContainerStockInD dao=new ContainerStockInD().dao();
    @Override
    protected ContainerStockInD dao() { return dao; }

    @Override
    protected int systemLogTargetType() { return ProjectSystemLogTargetType.NONE.getValue(); }

    /**
     * 保存
     * @param
     * @return
     */
    public Ret save(ContainerStockInD stockInD) {
        if(stockInD==null || isOk(stockInD.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(container.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success=stockInD.save();
        if(success) {
            //添加日志
            //addSaveSystemLog(container.getIAutoId(), JBoltUserKit.getUserId(), container.getName());
        }
        return ret(success);
    }

    public List<String> selectByContainerId(List<Long> ids) {
        return query("SELECT iContainerStockInMid FROM Bd_ContainerStockInD WHERE iContainerId in ("+ids+")");
    }

    public void deleteByContainerId(List<Long> idArr) {
        update("UPDATE Bd_ContainerStockInD SET isDeleted = 1 WHERE iAutoId IN (" +idArr + ") ");
    }
}
