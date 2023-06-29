#sql("formParamList")
SELECT t1.*,
       t3.cQcItemName AS cqcitemnames,
       t2.cQcParamName AS cqcparamnames
FROM Bd_SpotCheckFormParam t1
         INNER JOIN Bd_QcParam t2 ON t1.iSpotCheckParamId = t2.iAutoId
         INNER JOIN Bd_SpotCheckParam t3 ON t2.iQcItemId = t3.iAutoId
WHERE
        t1.isDeleted = '0'
    #if(qcFormId)
        AND t1.iSpotCheckFormItemId = #para(qcFormId)
    #end
ORDER BY t1.iItemParamSeq
    #end

    #sql("qcformparamlist")
SELECT
    t2.cProdItemCode,
    t2.cProdItemName,
    t1.*
FROM
    Bd_ProdParam t1
        INNER JOIN Bd_ProdItem t2 ON t1.iProdItemId = t2.iAutoId
WHERE
        t2.isDeleted = '0'
  AND t1.isDeleted = '0'
    #if(iQcItemIds)
	    AND t2.iAutoId IN (
	     #for (itemId : iQcItemIds.split(','))
           '#(itemId)' #(for.last?'':',')
	     #end
	    )
	#end
	#if(ids)
	    AND CHARINDEX( ','+cast((select t1.iAutoId) as nvarchar(20))+',', #para(ids) ) =0
	#end
ORDER BY t2.cProdItemCode ASC
    #end

#sql("findByFormId")
SELECT
	t2.cProdItemCode,
	t2.cProdItemName AS cqcitemnames,
	t1.cProdParamName AS cqcparamnames,
	t1.iAutoId AS iqcparamid,
	t3.iItemSeq,
	t3.iItemParamSeq,
	t1.*
FROM
    Bd_ProdParam t1
	INNER JOIN Bd_ProdItem t2 ON t1.iProdItemId = t2.iAutoId
	INNER JOIN Bd_ProdFormParam t3 on t3.iProdParamId = t1.iAutoId
WHERE
	t2.isDeleted = '0'
	AND t1.isDeleted = '0'
	#if(qcFormId)
	    AND t3.iProdFormId = #para(qcFormId)
	#end
	ORDER BY t3.iItemSeq,t3.iItemParamSeq ASC
#end





