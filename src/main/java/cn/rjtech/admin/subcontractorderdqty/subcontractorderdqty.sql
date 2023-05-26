#sql("findBySubcontractOrderMId")
SELECT
	d.iInventoryId,
	inv.iPkgQty,
	qty.*
FROM
	PS_SubcontractOrderD_Qty qty
	INNER JOIN PS_SubcontractOrderD d ON d.iAutoId = qty.iSubcontractOrderDid
	INNER JOIN PS_SubcontractOrderM m ON m.iAutoId = d.iSubcontractOrderMid
	INNER JOIN Bd_Inventory inv ON inv.iAutoId = d.iInventoryId
WHERE
	m.IsDeleted = 0
    #if(iSubcontractOrderMid)
        AND m.iAutoId = #para(iSubcontractOrderMid)
    #end
#end
