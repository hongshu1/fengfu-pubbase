#sql("findAll")
SELECT
	b.iAutoId,
	b.iPurchaseOrderMid,
	b.iInventoryId,
	inv.cInvCode AS afterCInvCode,
	inv.cInvName AS afterCInvName,
	inv.cInvCode1 AS afterCInvCode1,
	inv.cInvName1 AS afterCInvName1,
	inv.iPkgQty AS afterIPkgQty,
	b.iSourceInventoryId,
	sinv.cInvCode,
	sinv.cInvName,
	sinv.cInvCode1,
	sinv.cInvName1,
	sinv.iPkgQty,
	b.isPresent,
	b.iVendorAddrId,
	b.cAddress,
	b.iSum,
	b.iSourceSum,
	b.cMemo
FROM
	PS_PurchaseOrderD b
	LEFT JOIN Bd_Inventory sinv ON sinv.iAutoId = b.iSourceInventoryId
	LEFT JOIN Bd_Inventory inv ON inv.iAutoId = b.iInventoryId
	AND b.iInventoryId <> b.iSourceInventoryId
WHERE
	b.isDeleted = 0
	#if(iPurchaseOrderMid)
	    AND b.iPurchaseOrderMid = #para(iPurchaseOrderMid)
	#end
#end
