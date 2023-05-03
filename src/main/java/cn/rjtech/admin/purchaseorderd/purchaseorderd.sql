#sql("findAll")
SELECT
	b.iAutoId,
	b.iPurchaseOrderMid,
	b.iInventoryId,
	inv.cInvCode,
	inv.cInvName,
	inv.cInvCode1,
	inv.cInvName1,
	inv.iPkgQty,
	b.isPresent,
	b.iVendorAddrId,
	b.cAddress,
	b.iSum,
	b.cMemo
FROM
	PS_PurchaseOrderD b
	LEFT JOIN Bd_Inventory inv ON inv.iAutoId = b.iInventoryId
WHERE
	b.isDeleted = 0
	#if(iPurchaseOrderMid)
	    AND b.iPurchaseOrderMid = #para(iPurchaseOrderMid)
	#end
#end
