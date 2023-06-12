#sql("getBomComparePageData")
SELECT
    d.iAutoId,
	d.iBomMid,
	d.cInvCode,
	d.cInvName,
	d.cInvStd,
	d.cCode,
	d.iCodeLevel,
	d.cVersion,
	d.iInventoryUomId1,
	d.dEnableDate,
	d.dDisableDate,
	d.iPartType,
	d.iBaseQty,
	d.iWeight,
	d.cVenName,
	d.isVirtual,
	d.bProxyForeign,
	d.cMemo,
	uom.cUomName AS cuomname
FROM
	Bd_BomD d
	LEFT JOIN Bd_Uom uom ON uom.iAutoId = d.iInventoryUomId1
WHERE
	d.isDeleted = '0'
	AND d.iBomMid = #para(iBomMid)
	ORDER BY d.iAutoId ASC
#end
