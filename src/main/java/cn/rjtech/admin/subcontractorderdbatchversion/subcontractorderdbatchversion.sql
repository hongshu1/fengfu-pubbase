#sql("findBySubcontractOrderMid")
SELECT
	inv.cInvCode,
	inv.cInvCode1,
	inv.cInvName1,
	a.*
FROM
	PS_SubcontractOrderDBatchVersion a
	INNER JOIN Bd_Inventory inv ON inv.iAutoId = a.iInventoryId
WHERE
	a.iSubcontractOrderMid = #para(iSubcontractOrderMid)
	order by a.dPlanDate,inv.cInvCode,a.cVersion asc
#end
