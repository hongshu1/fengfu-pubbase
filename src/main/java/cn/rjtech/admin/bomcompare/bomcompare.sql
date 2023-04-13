#sql("findByBomMasterId")
SELECT
	a.iAutoId,
	a.iPid,
	a.iInventoryId,
	a.iRawType,
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
	inv.cInvName2,
	inv.cInvAddCode,
	inv.cInvAddCode1,
	ven.cVenName
FROM
	Bd_BomCompare a
	INNER JOIN Bd_Inventory inv ON inv.iAutoId = a.iInventoryId AND inv.isDeleted = 0
	LEFT JOIN Bd_Vendor ven ON ven.iAutoId = a.iVendorId
WHERE
	a.iBOMMasterId = #para(bomMasterId)
	AND a.isDeleted = '0'
#end

#sql("queryCompareIndex")
SELECT
    COUNT(1)
FROM
	(
	SELECT
		a.cInvLev
	FROM
		Bd_BomCompare a
		INNER JOIN Bd_Inventory inv ON inv.iAutoId = a.iInventoryId
		AND inv.isDeleted = 0
		LEFT JOIN Bd_Vendor ven ON ven.iAutoId = a.iVendorId
	WHERE
		a.iBOMMasterId = #para(bomMasterId)
		AND a.isDeleted = '0'
	GROUP BY
	a.cInvLev
	) a
#end
