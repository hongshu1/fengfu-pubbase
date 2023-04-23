#sql("findByPurchaseOderMId")
SELECT
	*
FROM
	PS_PurchaseOrderRef
WHERE
	iPurchaseOrderDid IN ( SELECT iAutoId FROM PS_PurchaseOrderD oderd WHERE oderd.iPurchaseOrderMid = #para(purchaseOrderMId) )
#end

#sql("removeByPurchaseOderMId")
DELETE
	PS_PurchaseOrderRef
WHERE
	iPurchaseOrderDid IN ( SELECT iAutoId FROM PS_PurchaseOrderD oderd WHERE oderd.iPurchaseOrderMid = #para(purchaseOrderMId) )
#end
