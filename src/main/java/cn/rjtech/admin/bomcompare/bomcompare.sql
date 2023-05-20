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
	a.iVendorId,
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


### bomMasterId不为空，需要排除当前母件id以及复制母件id不等于当前id,等于不需要统计；

#sql("getCommonInv")
SELECT
    DISTINCT a.iBOMMasterId
FROM
	Bd_BomCompare a
	INNER JOIN Bd_BomMaster master ON master.iAutoId = a.iBOMMasterId
	INNER JOIN Bd_Inventory inv ON inv.iAutoId = a.iInventoryId AND inv.isDeleted = 0
	LEFT JOIN Bd_Vendor ven ON ven.iAutoId = a.iVendorId
WHERE
	a.isDeleted = '0'
	AND inv.IsDeleted = '0'
	AND master.IsDeleted = '0'
	AND master.isEnabled = '1'
	AND CONVERT(DATE, master.dEnableDate) <= CONVERT(DATE,  GETDATE())
	AND CONVERT(DATE, master.dDisableDate) >= CONVERT(DATE,  GETDATE())
	AND a.iInventoryId = #para(invId)
	#if(iAuditStatus)
	AND master.iAuditStatus = #para(iAuditStatus)
	#end
	#if(isEffective)
	AND master.isEffective = #para(isEffective)
	#end
	AND (a.iQty <> #para(qty) OR a.iWeight <> #para(weight))
	#if(bomMasterId)
	AND a.iBOMMasterId <> #para(bomMasterId)
	AND ISNULL(master.iCopyFromId, '') <> #para(bomMasterId)
	#end
#end
