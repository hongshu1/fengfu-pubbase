#sql("findAll")
SELECT
	b.iAutoId,
	b.iSubcontractOrderMid,
	b.iInventoryId,
	inv.cInvCode,
	inv.cInvName,
	inv.cInvStd,
	inv.cInvCode1,
	inv.cInvName1,
	u.cuomname,
	b.iPkgQty,
	b.isPresent,
	b.iVendorAddrId,
	b.cAddress,
	b.cMemo
FROM
	PS_SubcontractOrderD b
	LEFT JOIN Bd_Inventory inv ON inv.iAutoId = b.iInventoryId
	left join bd_uom u on inv.iInventoryUomId1 = u.iautoid
WHERE
	b.isDeleted = 0
	#if(iSubcontractOrderMid)
	    AND b.iSubcontractOrderMid = #para(iSubcontractOrderMid)
	#end
#end
