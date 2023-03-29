###首页数据
#sql("selectData")
SELECT
    s1.*,
    s2.cRdName,
    s2.cRdCode
FROM
    Bd_SaleType s1
LEFT JOIN Bd_Rd_Style s2 ON s2.cRdCode = s1.iRdStyleId
WHERE
    s1.IsDeleted = '0'
#end

###收发类型编码，名字
#sql("selectRdStyle")
SELECT cRdCode,cRdName FROM Bd_Rd_Style where IsDeleted = '0'
#end