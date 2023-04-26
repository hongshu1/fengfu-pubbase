#sql("findByPurchaseOrderMId")
SELECT
	inv.cInvCode,
	inv.cInvName,
	inv.cInvCode1,
	inv.cInvName1,
	a.*
FROM
	PS_PurchaseOrderDBatch a
	INNER JOIN PS_PurchaseOrderD d ON d.iAutoId = a.iPurchaseOrderDid
	INNER JOIN Bd_Inventory inv ON inv.iAutoId = a.iinventoryId
WHERE
	d.isDeleted = 0
	#if(iPurchaseOrderMid)
	    AND d.iPurchaseOrderMid = #para(iPurchaseOrderMid)
	#end
    #if(isEffective)
        AND a.isEffective = #para(isEffective)
    #end
	order by a.dPlanDate,inv.cInvCode,a.cVersion asc
#end
