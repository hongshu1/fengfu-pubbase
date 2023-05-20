package cn.rjtech.wms.utils;

import cn.hutool.setting.Setting;
import cn.jbolt.core.base.config.JBoltConfig;
import cn.jbolt.core.base.config.JBoltExtendDatabaseConfig;
import cn.jbolt.core.util.JBoltStringUtil;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.log.Log;

import java.sql.*;
import java.util.*;

public class DBUtils {

    private static final Log LOG = Log.getLog(DBUtils.class);

    /**
     * 获取数据库连接
     *
     * @param driver   驱动名
     * @param url      连接url
     * @param username 用户名
     * @param password 密码
     * @return 数据库连接
     */
    private static Connection getConnection(String driver, String url, String username, String password) {
        try {
            // 1. 加载驱动类
            Class.forName(driver);
            // 2. 获取数据库连接对象
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取数据库连接失败");
        }
    }

    /**
     * 获取连接
     *
     * @param dbAlias 数据库名称
     * @return 数据库连接
     */
    public static Connection getConnection(String dbAlias) {
        Setting setting = JBoltExtendDatabaseConfig.me().getSetting().getSetting(dbAlias);
        ValidationUtils.notNull(setting, String.format("指定的数据源‘%s’不存在", dbAlias));
        
        String url = setting.get("jdbc_url");
        String driver = JBoltConfig.getDriverClass(setting.get("db_type"));
        String username = setting.get("user");
        String password = setting.get("password");

//        if ("ds1".equals(dbAlias) || dbAlias == null) {
//            url = AppConfig.globalGetProperty("jdbc.url");
//            driver = AppConfig.globalGetProperty("jdbc.driver");
//            username = AppConfig.globalGetProperty("jdbc.username");
//            password = AppConfig.globalGetProperty("jdbc.databasepassword");
//        } else {
//            StringBuilder prefix = new StringBuilder("jdbc.");
//            prefix.append(dbAlias);
//            url = AppConfig.globalGetProperty(prefix + ".url");
//            driver = AppConfig.globalGetProperty(prefix + ".driver");
//            username = AppConfig.globalGetProperty(prefix + ".username");
//            password = AppConfig.globalGetProperty(prefix + ".databasepassword");
//        }
        return getConnection(driver, url, username, password);
    }

    /**
     * 获取连接
     *
     * @param dbAlias 数据库别名
     * @param jdbcUrl 前台指定的jdbcUrl参数
     * @return 数据库连接
     */
    public static Connection getConnection(String dbAlias, String jdbcUrl) {
        Setting setting = JBoltExtendDatabaseConfig.me().getSetting().getSetting(dbAlias);
        ValidationUtils.notNull(setting, String.format("指定的数据源‘%s’不存在", dbAlias));

        String url = setting.get("jdbc_url");
        String driver = JBoltConfig.getDriverClass(setting.get("db_type"));
        String username = setting.get("user");
        String password = setting.get("password");
        
        if(JBoltStringUtil.isBlank(jdbcUrl)){
            jdbcUrl = url;
        }

//        if ("ds1".equals(dbAlias) || dbAlias == null) {
//            url = params.get("jdbc_url") == null ? AppConfig.globalGetProperty("jdbc.url") : params.get("jdbc_url").toString();
//            driver = AppConfig.globalGetProperty("jdbc.driver");
//            username = AppConfig.globalGetProperty("jdbc.username");
//            password = AppConfig.globalGetProperty("jdbc.databasepassword");
//        } else {
//            StringBuilder prefix = new StringBuilder("jdbc.");
//            prefix.append(dbAlias);
//            url = params.get("jdbc_url") == null ? AppConfig.globalGetProperty(prefix + ".url") : params.get("jdbc_url").toString();
//            driver = AppConfig.globalGetProperty(prefix + ".driver");
//            username = AppConfig.globalGetProperty(prefix + ".username");
//            password = AppConfig.globalGetProperty(prefix + ".databasepassword");
//        }

        return getConnection(driver, jdbcUrl, username, password);
    }

    /**
     * 关闭所有资源
     *
     * @param rs   获得的结果集
     * @param stmt 参数
     * @param conn 连接
     */
    public static void closeAll(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建临时表
     *
     * @param dbAlias 数据库别名
     * @param SqlStr  sql语句
     * @return 是否成功
     */
    public static Boolean CreateTempTable(String dbAlias, String SqlStr) {
        Connection conn = null;
        PreparedStatement pre = null;

        try {
            conn = DBUtils.getConnection(dbAlias);
            pre = conn.prepareStatement(SqlStr);
            pre.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.closeAll(null, pre, conn);
        }

        return false;
    }

    /**
     * 调用存储过程获取输出值的数据以及结果集的数据
     *
     * @param dbAlias       jeesite中yml配的数据库名
     * @param procedureName 存储过程名
     * @param paramsMap     存储过程的参数集（参数名必须跟储存过程的参数名一致）
     * @return {resultData=[{}],resultOutput=[{}, {},.....]}  没有结果记得存储过程不返回resultData
     * 如： {resultOutput=[{}, {},.....]}
     */
    public static Map<String, Object> ExecSql(String dbAlias, String procedureName, Map<String, Object> paramsMap) throws Exception {
        // 其中并没有使用到paramsList，到这个参数，要根据后续需求进行对应修改添加
        Connection conn = null;
        ResultSet rs = null;
        CallableStatement call = null;

        StringBuilder buffer = new StringBuilder();

        int index = 1;

        // 最终返回的数据集合
        Map<String, Integer> outputIndex = new HashMap<>(10);
        try {
            // 条件为真则说明该存储过程需要传入参数
            if (paramsMap != null && paramsMap.size() > 0) {
                int mapsize = paramsMap.size();
                //根据传入的参数集拼接占位符
                for (int i = 0; i < mapsize; i++) {
                    buffer.append("?");
                    if (i < mapsize - 1) {
                        buffer.append(",");
                    }
                }
            }

            conn = getConnection(dbAlias);
            String sql = "{call" + " " + procedureName + "(" + buffer + ")}";
            // 执行sql语句
            call = conn.prepareCall(sql);

            //如果有参数则将参数放入到相应的占位符
            if (!("".equals(buffer.toString()))) {
                //使用迭代器，获取key
                for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    //将值放到对应的占位符
                    call.setObject(index, value);
                    //获取存储过程的参数情况
                    List<Map<String, Object>> paramResult = GetParamBySql(dbAlias, "dbo", procedureName);
                    for (int i = 0; i < paramResult.size(); i++) {
                        boolean isEq = paramResult.get(i).get("name").equals("@" + key);
                        Integer isEq1 = (Integer) paramResult.get(i).get("isoutparam");
                        if (isEq && isEq1 == 1) {
                            call.registerOutParameter(i + 1, Types.OTHER);
                            //保存输出参数的参数名
                            outputIndex.put(key, i + 1);
                        }
                    }
                    LOG.info(key + " " + value);
                    if (index < paramsMap.size()) {
                        index++;
                    }
                }
            }

            boolean execute = true;
            // 获取存储过程结果集(该步骤不可删除，删除后下方call.getResultSet获得对象为null)
            try {
                //执行有返回值的存储过程（executeQuery方法执行没有返回值的存储过程会报错）
                ResultSet executeQuery = call.executeQuery();
            } catch (SQLException e) {
                call.execute();
                execute = false;
            }
            int numi = 0;
            Map<String, Object> mapRet = new HashMap<>();
            // 遍历结果集
            while (execute) {
                rs = call.getResultSet();
                // 获取结果集列名
                ResultSetMetaData rsm = rs.getMetaData();
                List<String> allColumn = new ArrayList<>();
                // 获取结果集列名集合
                int cH = 1;
                while (cH <= rsm.getColumnCount()) {
                    String columnName = rsm.getColumnName(cH);
                    allColumn.add(columnName);
                    cH++;
                }
                List<Map<String, Object>> mapList = new ArrayList<>();
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>();
                    int i = 1;
                    while (i <= allColumn.size()) {
                        String string = rs.getString(i);
                        map.put(allColumn.get(i - 1), string);
                        i++;
                    }
                    mapList.add(map);
                }
                execute = call.getMoreResults();
                numi++;
                mapRet.put(String.valueOf(numi), mapList);
            }

            //获取有输出参数的存储过程的输出参数值
            if (!"".equals(buffer.toString())) {
                for (Map.Entry<String, Integer> entry : outputIndex.entrySet()) {
                    Object outputValue = call.getObject(entry.getValue());
                    paramsMap.put(entry.getKey(), outputValue);
                }
            }
            LOG.info("mapRet: {}", mapRet);
            return mapRet;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            DBUtils.closeAll(rs, call, conn);
        }
    }

    /**
     * 调用存储过程获取输出值的数据以及结果集的数据 20221103 潘家宝添加
     *
     * @param dbAlias       jeesite中yml配的数据库名
     * @param procedureName 存储过程名
     * @param paramsMap     存储过程的参数集（参数名必须跟储存过程的参数名一致）
     * @return {resultData=[{}],resultOutput=[{}, {},.....]}  没有结果记得存储过程不返回resultData
     * 如： {resultOutput=[{}, {},.....]}
     */
    public static Map<String, Object> ExecSql(String dbAlias, String erpdbschemas, String procedureName, Map<String, Object> paramsMap, String jdbcUrl) {
        // 其中并没有使用到paramsList，到这个参数，要根据后续需求进行对应修改添加
        Connection conn = null;
        ResultSet rs = null;
        CallableStatement call = null;

        StringBuilder buffer = new StringBuilder();

        int index = 1;

        // 最终返回的数据集合
        Map<String, Integer> outputIndex = new HashMap<>();

        try {
            //条件为真则说明该存储过程需要传入参数
            if (paramsMap != null && paramsMap.size() > 0) {
                int mapsize = paramsMap.size();
                //根据传入的参数集拼接占位符
                for (int i = 0; i < mapsize; i++) {
                    buffer.append("?");
                    if (i < mapsize - 1) {
                        buffer.append(",");
                    }
                }
            }

            conn = getConnection(dbAlias, jdbcUrl);
            String sql = "{call" + " " + erpdbschemas + "." + procedureName + "(" + buffer + ")}";
            //String sql = "{call mes.P_Sys_ReportForProduceTaskNoticeGP'''and20221108''','',''()}";
            // 执行sql语句
            call = conn.prepareCall(sql);

            //如果有参数则将参数放入到相应的占位符
            if (!("".equals(buffer.toString()))) {
                // 使用迭代器，获取key
                // Iterator<Map.Entry<String, Object>> iter = paramsMap.entrySet().iterator()
                //获取存储过程的参数情况
                List<Map<String, Object>> paramResult = GetParamBySql(dbAlias, erpdbschemas, procedureName);
                for (int i = 0; i < paramResult.size(); i++) {
                    call.setObject(index, paramsMap.get(paramResult.get(i).get("name").toString().replace("@", "").toLowerCase()));
                    Integer isEq1 = (Integer) paramResult.get(i).get("isoutparam");
                    if (isEq1 == 1) {
                        call.registerOutParameter(i + 1, Types.OTHER);
                        //保存输出参数的参数名
                        outputIndex.put(paramResult.get(i).get("name").toString(), i + 1);
                    }
                    if (index < paramsMap.size()) {
                        index++;
                    }
                }
				/*while (iter.hasNext()) {
					Map.Entry<String, Object> entry = iter.next();
					String key = entry.getKey();
					Object value = entry.getValue();
					//将值放到对应的占位符
					call.setObject(index, value);
					for(int i=0;i<paramResult.size();i++){
						boolean isEq = paramResult.get(i).get("name").equals("@"+key);
						Integer isEq1 = (Integer)paramResult.get(i).get("isoutparam");
						if(isEq&&isEq1==1){
							call.registerOutParameter(i+1, Types.OTHER);
							//保存输出参数的参数名
							outputIndex.put(key,i+1);
						}
					}
					LOG.info(key + " " + value);
					if (index < paramsMap.size()) {
						index++;
					}
				}*/
            }
            boolean execute = true;
            // 获取存储过程结果集(该步骤不可删除，删除后下方call.getResultSet获得对象为null)
            try {
                //执行有返回值的存储过程（executeQuery方法执行没有返回值的存储过程会报错）
                ResultSet executeQuery = call.executeQuery();
            } catch (SQLException e) {
                call.execute();
                execute = false;
            }
            int numi = 0;
            Map<String, Object> mapRet = new HashMap<>();
            // 遍历结果集
            while (execute) {
                rs = call.getResultSet();
                // 获取结果集列名
                ResultSetMetaData rsm = rs.getMetaData();
                List<String> allColumn = new ArrayList<>();
                // 获取结果集列名集合
                int cH = 1;
                while (cH <= rsm.getColumnCount()) {
                    String columnName = rsm.getColumnName(cH);
                    allColumn.add(columnName);
                    cH++;
                }
                List<Map<String, Object>> mapList = new ArrayList<>();
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>();
                    int i = 1;
                    while (i <= allColumn.size()) {
                        String string = rs.getString(i);
                        map.put(allColumn.get(i - 1), string);
                        i++;
                    }
                    mapList.add(map);
                }
                execute = call.getMoreResults();
                numi++;
                mapRet.put(String.valueOf(numi), mapList);
            }

            //获取有输出参数的存储过程的输出参数值
            if (!"".equals(buffer.toString())) {
                for (Map.Entry<String, Integer> entry : outputIndex.entrySet()) {
                    Object outputValue = call.getObject(entry.getValue());
                    paramsMap.put(entry.getKey(), outputValue);
                }
            }
            LOG.info("mapRet: {}", mapRet);
            return mapRet;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("调用存储过程异常: " + e.getLocalizedMessage());
        } finally {
            DBUtils.closeAll(rs, call, conn);
        }
    }

    /**
     * 调用存储过程集的数据
     *
     * @param dbAlias       jeesite中yml配的数据库名
     * @param procedureName 存储过程名
     * @param paramsMap     存储过程的参数集（参数名必须跟储存过程的参数名一致）
     *                      {resultData=[{}],resultOutput=[{}, {},.....]}  没有结果记得存储过程不返回resultData
     *                      如： {resultOutput=[{}, {},.....]}
     */
    public static void ExecSqlWithNoResult(String dbAlias, String procedureName, Map<String, Object> paramsMap) {
        // 其中并没有使用到paramsList，到这个参数，要根据后续需求进行对应修改添加
        Connection conn = null;
        ResultSet rs = null;
        CallableStatement call = null;
        StringBuilder buffer = new StringBuilder();
        int index = 1;
        //最终返回的数据集合
        Map<String, Integer> outputIndex = new HashMap<>();
        try {
            //条件为真则说明该存储过程需要传入参数
            if (paramsMap != null && paramsMap.size() > 0) {
                int mapsize = paramsMap.size();
                //根据传入的参数集拼接占位符
                for (int i = 0; i < mapsize; i++) {
                    buffer.append("?");
                    if (i < mapsize - 1) {
                        buffer.append(",");
                    }
                }
            }

            conn = getConnection(dbAlias);
            String sql = "{call" + " " + procedureName + "(" + buffer + ")}";
            // 执行sql语句
            call = conn.prepareCall(sql);

            //如果有参数则将参数放入到相应的占位符
            if (!("".equals(buffer.toString()))) {
                //使用迭代器，获取key
                for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    //将值放到对应的占位符
                    call.setObject(index, value);
                    //获取存储过程的参数情况
                    List<Map<String, Object>> paramResult = GetParamBySql(dbAlias, "dbo", procedureName);
                    for (int i = 0; i < paramResult.size(); i++) {
                        boolean isEq = paramResult.get(i).get("name").equals("@" + key);
                        Integer isEq1 = (Integer) paramResult.get(i).get("isoutparam");
                        if (isEq && isEq1 == 1) {
                            call.registerOutParameter(i + 1, Types.OTHER);
                            //保存输出参数的参数名
                            outputIndex.put(key, i + 1);
                        }
                    }
                    LOG.info(key + " " + value);
                    if (index < paramsMap.size()) {
                        index++;
                    }
                }
            }

            // boolean execute = true
            // 获取存储过程结果集(该步骤不可删除，删除后下方call.getResultSet获得对象为null)
            try {
                //执行有返回值的存储过程（executeQuery方法执行没有返回值的存储过程会报错）
                ResultSet executeQuery = call.executeQuery();
            } catch (SQLException e) {
                call.execute();
                // execute = false
            }

            //获取有输出参数的存储过程的输出参数值
            if (!"".equals(buffer.toString())) {
                for (Map.Entry<String, Integer> entry : outputIndex.entrySet()) {
                    Object outputValue = call.getObject(entry.getValue());
                    paramsMap.put(entry.getKey(), outputValue);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("调用存储过程异常：" + e.getLocalizedMessage());
        } finally {
            DBUtils.closeAll(rs, call, conn);
        }
    }

    /**
     * 在对应数据库中查询对应信息，返回单表信息
     *
     * @param dbAlias 数据库别名
     * @param sqlStr  查询的Sql语句
     */
    public static List<Map<String, Object>> ExecSqlToList(String dbAlias, String sqlStr) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pre = null;

        try {
            // List<Map<String, Object>> dataList = new ArrayList<>()
            List<Map<String, Object>> listMap = new ArrayList<>();

            conn = DBUtils.getConnection(dbAlias);
            // 执行sql语句
            pre = conn.prepareStatement(sqlStr);
            // 获取存储过程结果集(该步骤不可删除，删除后下方call.getResultSet获得对象为null)
            ResultSet executeQuery = pre.executeQuery();

            pre.execute();

            boolean execute = true;

            pre.execute();

            // 遍历结果集
            while (execute) {
                // Map<String, Object> dataMap = new HashMap<>();
                rs = pre.getResultSet();
                // 获取结果集列名
                ResultSetMetaData rsmd = rs.getMetaData();

                List<Map<String, Object>> columns = new ArrayList<>();

                // 获取结果集列名集合
                // Map<String, Object> cloumName = new LinkedHashMap<>()
                // List<String> nameList = new ArrayList<>()
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    Map<String, Object> columnPerferences = new HashMap<>();
                    columnPerferences.put("columnName", rsmd.getColumnName(i));
                    columnPerferences.put("columnType", rsmd.getColumnType(i));
                    // cloumName.put(rsmd.getColumnName(i), rsmd.getColumnName(i))
                    columns.add(columnPerferences);
                    // nameList.add(rsmd.getColumnName(i))
                }

                // dataMap.put("dataName", nameList)
                // 循环获取所有数据
                while (rs.next()) {
                    Map<String, Object> resultMap = new LinkedHashMap<>();
                    for (Map<String, Object> column : columns) {
                        int columnType = Integer.parseInt(column.get("columnType").toString());
                        String columnName = column.get("columnName").toString();
                        if (Types.VARCHAR == columnType) {
                            resultMap.put(columnName, rs.getObject(columnName) == null ? null : rs.getString(columnName));
                        } else if (Types.INTEGER == columnType) {
                            resultMap.put(columnName, rs.getObject(columnName) == null ? null : rs.getInt(columnName));
                        } else if (Types.SMALLINT == columnType) {
                            resultMap.put(columnName, rs.getObject(columnName) == null ? null : rs.getShort(columnName));
                        } else {
                            resultMap.put(columnName, rs.getObject(columnName) == null ? null : rs.getString(columnName));
                        }

                    }
                    LOG.info("map集合: {}", resultMap);
                    listMap.add(resultMap);
                }

                // dataMap.put("dataList", listMap)
                execute = pre.getMoreResults();
                // dataList.add(dataMap)
            }
            return listMap;
        } catch (Exception e) {
            LOG.error(e.getLocalizedMessage());
            e.printStackTrace();
        } finally {
            DBUtils.closeAll(rs, pre, conn);
        }
        return null;
    }

    /**
     * 传入数据库名与查询的表名，在对应数据库中查询对应信息
     */
    public static List<Map<String, Object>> ExecSql(String dbAlias, String sqlStr) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pre = null;

        try {
            List<Map<String, Object>> dataList = new ArrayList<>();
            conn = DBUtils.getConnection(dbAlias);
            // 执行sql语句
            pre = conn.prepareStatement(sqlStr);
            // 获取存储过程结果集(该步骤不可删除，删除后下方call.getResultSet获得对象为null)
            ResultSet executeQuery = pre.executeQuery();
            pre.execute();
            boolean execute = true;
            pre.execute();
            // 遍历结果集
            while (execute) {
                Map<String, Object> dataMap = new HashMap<>();
                rs = pre.getResultSet();
                // 获取结果集列名
                ResultSetMetaData rsmd = rs.getMetaData();
                List<Map<String, Object>> listMap = new ArrayList<>();
                List<Map<String, Object>> columns = new ArrayList<>();
                // 获取结果集列名集合
                // Map<String, Object> cloumName = new LinkedHashMap<>()
                List<String> nameList = new ArrayList<>();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    Map<String, Object> columnPerferences = new HashMap<>();
                    columnPerferences.put("columnName", rsmd.getColumnName(i));
                    columnPerferences.put("columnType", rsmd.getColumnType(i));
                    // cloumName.put(rsmd.getColumnName(i), rsmd.getColumnName(i))
                    columns.add(columnPerferences);
                    nameList.add(rsmd.getColumnName(i));
                }
                dataMap.put("dataName", nameList);
                // 循环获取所有数据
                while (rs.next()) {
                    Map<String, Object> resultMap = new LinkedHashMap<>();
                    for (Map<String, Object> column : columns) {
                        int columnType = Integer.parseInt(column.get("columnType").toString());
                        String columnName = column.get("columnName").toString();
                        if (Types.VARCHAR == columnType) {
                            resultMap.put(columnName, rs.getObject(columnName) == null ? null : rs.getString(columnName));
                        } else if (Types.INTEGER == columnType) {
                            resultMap.put(columnName, rs.getObject(columnName) == null ? null : rs.getInt(columnName));
                        } else if (Types.SMALLINT == columnType) {
                            resultMap.put(columnName, rs.getObject(columnName) == null ? null : rs.getShort(columnName));
                        } else {
                            resultMap.put(columnName, rs.getObject(columnName) == null ? null : rs.getString(columnName));
                        }
                    }
                    LOG.info("map集合: {}", resultMap);
                    listMap.add(resultMap);
                }
                dataMap.put("dataList", listMap);
                execute = pre.getMoreResults();
                dataList.add(dataMap);

            }
            return dataList;
        } catch (Exception e) {
            LOG.error(e.getLocalizedMessage());
            e.printStackTrace();
        } finally {
            DBUtils.closeAll(rs, pre, conn);
        }
        return null;
    }

    /**
     * 传入数据库名与查询的语句，在对应数据库中查询对应信息 20222203  潘家宝添加
     *
     * @param jdbcUrl 动态参数
     */
    public static List<Map<String, Object>> ExecSql(String dbAlias, String sqlStr, String jdbcUrl) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pre = null;

        try {
            List<Map<String, Object>> dataList = new ArrayList<>();

            conn = DBUtils.getConnection(dbAlias, jdbcUrl);
            // 执行sql语句
            pre = conn.prepareStatement(sqlStr);
            // 获取存储过程结果集(该步骤不可删除，删除后下方call.getResultSet获得对象为null)
            ResultSet executeQuery = pre.executeQuery();

            pre.execute();

            boolean execute = true;

            pre.execute();
            // 遍历结果集
            while (execute) {
                Map<String, Object> dataMap = new HashMap<>();
                rs = pre.getResultSet();
                // 获取结果集列名
                ResultSetMetaData rsmd = rs.getMetaData();
                List<Map<String, Object>> listMap = new ArrayList<>();
                List<Map<String, Object>> columns = new ArrayList<>();
                // 获取结果集列名集合
                // Map<String, Object> cloumName = new LinkedHashMap<>()
                List<String> nameList = new ArrayList<>();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    Map<String, Object> columnPerferences = new HashMap<>();
                    columnPerferences.put("columnName", rsmd.getColumnName(i));
                    columnPerferences.put("columnType", rsmd.getColumnType(i));
                    // cloumName.put(rsmd.getColumnName(i), rsmd.getColumnName(i))
                    columns.add(columnPerferences);
                    nameList.add(rsmd.getColumnName(i));
                }
                dataMap.put("dataName", nameList);
                // 循环获取所有数据
                while (rs.next()) {
                    Map<String, Object> resultMap = new LinkedHashMap<>();
                    for (Map<String, Object> column : columns) {
                        int columnType = Integer.parseInt(column.get("columnType").toString());
                        String columnName = column.get("columnName").toString();
                        if (Types.VARCHAR == columnType) {
                            resultMap.put(columnName, rs.getObject(columnName) == null ? null : rs.getString(columnName));
                        } else if (Types.INTEGER == columnType) {
                            resultMap.put(columnName, rs.getObject(columnName) == null ? null : rs.getInt(columnName));
                        } else if (Types.SMALLINT == columnType) {
                            resultMap.put(columnName, rs.getObject(columnName) == null ? null : rs.getShort(columnName));
                        } else {
                            resultMap.put(columnName, rs.getObject(columnName) == null ? null : rs.getString(columnName));
                        }

                    }
                    LOG.info("map集合: {}", resultMap);
                    listMap.add(resultMap);
                }
                dataMap.put("dataList", listMap);
                execute = pre.getMoreResults();
                dataList.add(dataMap);
            }
            return dataList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.closeAll(rs, pre, conn);
        }
        return null;
    }

    /**
     * JDBC 分页查询
     *
     * @param sql       SQL 查询语句
     * @param firstPage 起始页
     * @param maxSize   返回数据条数
     * @return ResultSet 返回结果集
     */
    public static List<Map<String, Object>> ExecSqlPage(String dbAlias, String sql, int firstPage, int maxSize) throws SQLException {
        Connection conn = DBUtils.getConnection(dbAlias);
        PreparedStatement pre = conn.prepareStatement(sql);
        pre.setMaxRows(maxSize);
        ResultSet rs = pre.executeQuery();
        rs.relative(firstPage);
        return ResultSetToList(rs);
    }

    public static List<Map<String, Object>> ResultSetToList(ResultSet rsData) {
        List<Map<String, Object>> list = new ArrayList<>();

        try {
            // 获得结果集结构信息,元数据
            ResultSetMetaData md = rsData.getMetaData();
            // 获得列数
            int columnCount = md.getColumnCount();
            while (rsData.next()) {
                Map<String, Object> rowData = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(md.getColumnName(i), rsData.getObject(i));
                }
                list.add(rowData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 通过SQL语句获取存储过程的参数情况
     *
     * @param dbAlias       数据库名
     * @param procedureName 存储过程名
     */
    public static List<Map<String, Object>> GetParamBySql(String dbAlias, String erpdbschemas, String procedureName) {
        StringBuffer sqlstr = new StringBuffer();
        List<Map<String, Object>> listmap = new ArrayList<>();
        try {
            sqlstr.append("select name,isoutparam from syscolumns where ID in    \n" +
                            "  (SELECT id FROM sysobjects as a  \n" + "  " +
                            " WHERE OBJECTPROPERTY(id, N'IsProcedure') = 1    \n" + "   and id = object_id(N'[")
                    .append(erpdbschemas).append("].[");
            // 存储过程名
            sqlstr.append(procedureName);
            sqlstr.append("]'))");
            //判断该参数类型（输入或者输出）
            LOG.info("sql: {}", sqlstr);
            listmap = ExecSqlToList(dbAlias, sqlstr.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listmap;
    }

}
