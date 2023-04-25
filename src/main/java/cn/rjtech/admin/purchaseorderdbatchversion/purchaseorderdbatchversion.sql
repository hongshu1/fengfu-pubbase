#sql("findByPurchaseOrderMid")
SELECT
	inv.cInvCode,
	inv.cInvCode1,
	inv.cInvName1,
	a.*
FROM
	PS_PurchaseOrderDBatchVersion a
	INNER JOIN Bd_Inventory inv ON inv.iAutoId = a.iInventoryId
WHERE
	a.iPurchaseOrderMid = #para(iPurchaseOrderMid)
	order by a.dPlanDate,inv.cInvCode,a.cVersion asc
#end
