#sql("findBySubcontractOrderMId")
SELECT
	inv.cInvCode,
	inv.cInvName,
	inv.cInvCode1,
	inv.cInvName1,
	a.*
FROM
	PS_SubcontractOrderDBatch a
	INNER JOIN PS_SubcontractOrderD d ON d.iAutoId = a.iSubcontractOrderDid
	INNER JOIN Bd_Inventory inv ON inv.iAutoId = a.iinventoryId
WHERE
	d.isDeleted = 0
	#if(iSubcontractOrderMid)
	    AND d.iSubcontractOrderMid = #para(iSubcontractOrderMid)
	#end
    #if(isEffective)
        AND a.isEffective = #para(isEffective)
    #end
	order by a.dPlanDate,inv.cInvCode,a.cVersion asc
#end