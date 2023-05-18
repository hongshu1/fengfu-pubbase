package cn.rjtech.util;

import net.fenghaitao.AutoExcel;
import net.fenghaitao.imports.DataSet;
import net.fenghaitao.parameters.ImportPara;

import java.util.List;

/**
 * AutoExcel工具类
 *
 * @author Kephon
 */
public class AutoExcelUtil {

    /**
     * 从Excel文件中读取Sheet的内容, 适用表头字段名固定的场景
     *
     * @param excelPath Excel文件完整路径
     * @param paras     Sheet参数
     * @return 数据集合
     */
    public static DataSet readExcel(String excelPath, List<ImportPara> paras) {
        return AutoExcel.read(excelPath, paras);
    }

}
