#sql("formItemList")
SELECT t1.*
FROM Bd_ProdFormItem t1, Bd_ProdForm t2
WHERE t1.iProdFormId = t2.iAutoId
#end

#sql("qcitemlist")
SELECT *
FROM Bd_ProdItem
WHERE isDeleted = '0'
    #if(iQcItemId != null)
	AND CHARINDEX( ','+cast((select iAutoId) as nvarchar(20))+',', #para(iQcItemId) ) =0
    #end
#end

#sql("formItemLists")
SELECT
	t1.*,
	t3.cProdItemName
FROM
    Bd_ProdFormItem t1
	LEFT JOIN Bd_ProdItem t3 ON t3.iAutoId = t1.iProdItemId
WHERE
	t1.iProdFormId = #para(iqcformid)
ORDER BY
	t1.iSeq
#end

