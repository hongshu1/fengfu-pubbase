
### 根据类型进行分组，查询某年每月的使用数量，使用PIVOT将month进行行列转换。
### #sql("getAccessDeptIdList")
### SELECT type, [1], [2], [3], [4], [5], [6], [7], [8], [9], [10], [11], [12]
### FROM (
###     SELECT type, month, number
###      FROM table_name
###          WHERE year = #()
###      ) AS SourceTable
###      PIVOT (
###          SUM(number)
###          FOR month IN ([1], [2], [3], [4], [5], [6], [7], [8], [9], [10], [11], [12])
###      ) AS PivotTable
###  #end
