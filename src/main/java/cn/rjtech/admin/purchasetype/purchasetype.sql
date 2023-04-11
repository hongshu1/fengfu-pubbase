###首页数据
#sql("selectAll")
SELECT
    p.*,
    s.iAutoId as iAutoId2,
    s.cRdName,
    s.cRdCode
FROM
    Bd_PurchaseType p
        LEFT JOIN Bd_Rd_Style s ON s.cRdCode = p.cRdCode
WHERE p.IsDeleted = '0'
#if(cPTCode)
  and p.cPTCode like CONCAT('%', #para(cPTCode), '%')
#end
#if(cPTName)
and p.cPTName like CONCAT('%', #para(cPTName), '%')
#end
#end