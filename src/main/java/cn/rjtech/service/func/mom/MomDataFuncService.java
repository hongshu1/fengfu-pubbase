package cn.rjtech.service.func.mom;

import cn.rjtech.base.service.func.BaseMomDataFuncService;

import java.sql.CallableStatement;
import java.sql.Types;

/**
 * MES 存储过程
 *
 * @author Kephon
 */
public class MomDataFuncService extends BaseMomDataFuncService {

    /**
     * 获取一位流水的工序卡号（唯一） 生成RouteNO
     */
    public String getNextRouteNo(long orgId, String prefix, int billNoLength) {
        return (String) execute((conn) -> {
                    CallableStatement proc = conn.prepareCall("{ call P_Sys_GetNextRouteNo(?, ?, ?, ?) }");
                    proc.setObject(1, orgId);
                    proc.setObject(2, prefix);
                    proc.setObject(3, billNoLength);
                    // 注册输出参数
                    proc.registerOutParameter(4, Types.VARCHAR);

                    proc.execute();
                    return proc.getString(4);
                }
        );
    }
    
    public String getNextNo(String prefix, int billNoLength) {
        return (String) execute((conn) -> {
                    CallableStatement proc = conn.prepareCall("{ call P_Sys_GetNextNo(?, ?, ?) }");
                    proc.setObject(1, prefix);
                    proc.setObject(2, billNoLength);
                    // 注册输出参数
                    proc.registerOutParameter(3, Types.VARCHAR);
                    proc.execute();
                    return proc.getString(3);
                }
        );
    }
}
