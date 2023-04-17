#sql("formItemList")
SELECT t1.*
FROM Bd_QcFormItem t1, Bd_QcForm t2
WHERE t1.iQcFormId = t2.iAutoId
#end

#sql("qcitemlist")
SELECT *
FROM Bd_QcItem
WHERE isDeleted = '0'
    #if(iQcItemId != null)
	AND CHARINDEX( ','+cast((select iAutoId) as nvarchar(20))+',', #para(iQcItemId) ) =0
    #end
#end

#sql("formItemLists")
SELECT
   t1.*,t3.cQcItemName,t2.cQcFormName
FROM Bd_QcFormItem t1
         LEFT JOIN Bd_QcForm t2 ON t2.iAutoId = t1.iQcFormId
         left join Bd_QcItem t3 on t3.iAutoId = t1.iQcItemId
WHERE
      t1.iQcFormId = #para(iQcFormId)
ORDER BY
    t1.iAutoId
    #end

