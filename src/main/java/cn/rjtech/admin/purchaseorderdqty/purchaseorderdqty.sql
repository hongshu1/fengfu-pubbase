#sql("findByPurchaseOrderMid")
SELECT
	d.iInventoryId,
	a.*
FROM
	PS_PurchaseOrderD_Qty a
	INNER JOIN PS_PurchaseOrderD d ON d.iAutoId = a.iPurchaseOrderDid
where d.iPurchaseOrderMid = #para(iPurchaseOrderMid)
#end
