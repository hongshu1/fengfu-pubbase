package cn.rjtech.admin.datasource;

import cn.hutool.core.text.StrSplitter;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.datasource.JBoltDataSourceUtil;
import cn.jbolt.core.db.datasource.JBoltDatasource;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;

import java.util.ArrayList;
import java.util.List;

import static cn.hutool.core.text.CharPool.COMMA;

/**
 * 数据源
 *
 * @author Kephon
 */
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/datasource")
public class DataSourceAdminController extends BaseAdminController {

    /**
     * 获取指定数据源的库名
     *
     * @param dataSources 数据源，多个","分隔
     */
    public void list(@Para(value = "dataSources") String dataSources) {
        ValidationUtils.notBlank(dataSources, JBoltMsg.PARAM_ERROR);

        List<Kv> dataSourceList = new ArrayList<>();

        List<String> dsNames = StrSplitter.split(dataSources, COMMA, true, true);

        for (String dsname : dsNames) {
            JBoltDatasource ds = JBoltDataSourceUtil.me.getJBoltDatasource(dsname);
            ValidationUtils.notNull(ds, String.format("数据源别名 %s 不存在", dsname));

            dataSourceList.add(Kv.by("dsname", dsname).set("dbname", ds.getDbName()));
        }

        renderJsonData(dataSourceList);
    }

}
