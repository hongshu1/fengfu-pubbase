#sql("formParamList")
SELECT t1.*,
       t3.cQcItemName AS cqcitemnames,
       t2.cQcParamName AS cqcparamnames
FROM Bd_QcFormParam t1,
     Bd_QcParam t2,
     Bd_QcItem t3
WHERE  t1.iQcFormItemId = '#(qcformitemid)'
  AND t1.iQcParamId = t2.iAutoId
  AND t2.iQcItemId = t3.iAutoId
ORDER BY t1.iItemParamSeq
#end





#sql("qcformparamlist")
SELECT t2.cQcItemName,
       t1.*,
       t3.iSeq AS iItemSeq,
       t3.iAutoId AS iqcformitemid
FROM   Bd_QcParam t1
           LEFT JOIN Bd_QcItem t2 ON t1.iQcItemId = t2.iAutoId
           LEFT JOIN Bd_QcFormItem t3 ON  '#(iqcformitemid)' = t3.iAutoId
WHERE t2.isDeleted = '0'
    AND t3.iQcItemId = t2.iAutoId
   #if(iQcParamId != null)
	AND CHARINDEX( ','+cast((select t1.iAutoId) as nvarchar(20))+',', #para(iQcParamId) ) =0
    #end
#end



