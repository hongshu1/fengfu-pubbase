###首页数据
#sql("selectData")
SELECT
    s1.*,
    s2.iAutoId as iAutoId2,
    s2.cRdName,
    s2.cRdCode
FROM
    Bd_SaleType s1
        LEFT JOIN Bd_Rd_Style s2 ON s2.cRdCode = s1.cRdCode
WHERE s1.IsDeleted = '0'
#if(cstcode)
  and s1.cSTCode like CONCAT('%', #para(cstcode), '%')
#end
#if(cstname)
  and s1.cSTName like CONCAT('%', #para(cstname), '%')
#end
#end
