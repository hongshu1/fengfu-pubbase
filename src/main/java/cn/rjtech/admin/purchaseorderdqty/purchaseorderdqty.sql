#sql("findByPurchaseOrderMid")
SELECT
	d.iInventoryId,
	qty.*
FROM
	PS_PurchaseOrderD_Qty qty
	INNER JOIN PS_PurchaseOrderD d ON d.iAutoId = qty.iPurchaseOrderDid
	INNER JOIN PS_PurchaseOrderM m ON m.iAutoId = d.iPurchaseOrderMid
WHERE
	m.IsDeleted = 0
    #if(iPurchaseOrderMid)
        AND m.iAutoId = #para(iPurchaseOrderMid)
    #end
#end
