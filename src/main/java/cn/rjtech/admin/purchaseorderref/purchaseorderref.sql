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

#sql("findByPurchaseOrderDIds")
SELECT
	*
FROM
	PS_PurchaseOrderRef
WHERE
	iPurchaseOrderDid IN (
        #for (id : ids)
             '#(id)' #(for.last?'':',')
        #end
	)
#end

#sql("removeByPurchaseOrderDIds")
DELETE
	PS_PurchaseOrderRef
WHERE
	iPurchaseOrderDid IN (
        #for (id : ids)
             '#(id)' #(for.last?'':',')
        #end
	)
#end
