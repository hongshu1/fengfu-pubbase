#sql("findAll")
SELECT
	b.iAutoId,
	b.iPurchaseOrderMid,
	b.iInventoryId,
	inv.cInvCode,
	inv.cInvName,
	inv.cInvCode1,
	inv.cInvStd,
	inv.cInvName1,
	b.iPkgQty,
	u.cuomname,
	b.isPresent,
	b.iVendorAddrId,
	b.cAddress,
	b.cMemo
FROM
	PS_PurchaseOrderD b
	LEFT JOIN Bd_Inventory inv ON inv.iAutoId = b.iInventoryId
	left join bd_uom u on inv.iInventoryUomId1 = u.iautoid
WHERE
	b.isDeleted = 0
	#if(iPurchaseOrderMid)
	    AND b.iPurchaseOrderMid = #para(iPurchaseOrderMid)
	#end
#end
