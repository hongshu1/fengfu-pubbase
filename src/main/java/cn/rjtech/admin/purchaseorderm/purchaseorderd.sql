#sql("findByInventoryId")
SELECT
	a.iAutoId,
	a.iInventoryId,
	a.iSeq,
	a.iLevel,
	a.cInvLev,
	a.isOutSourced,
	a.iQty,
	a.iWeight,
	a.cMemo,
	inv.cInvCode,
	inv.cInvName,
	inv.cInvStd,
	inv.cInvCode1,
	inv.cInvName1,
	inv.iPurchaseUomId,
	inv.iPkgQty,
	inv.cInvAddCode,
	inv.cInvAddCode1,
	pur.iYear,
	pur.iMonth,
	pur.iDay
FROM
    PS_PurchaseOrderD a
	INNER JOIN Bd_Inventory inv ON a.iInventoryId = inv.iAutoId AND inv.isDeleted = 0
	LEFT JOIN PS_PurchaseOrderD_Qty pur ON a.iAutoId = pur.iPurchaseOrderDid
WHERE a.isDeleted = '0'
#end