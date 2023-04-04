#sql("datas")
SELECT * FROM (SELECT
	master.iAutoId AS id,
	master.cBomVersion,
	minv.cInvCode,
	minv.cInvName,
	minv.cInvStd,
	uom.cUomName,
    NULL iQty,
    minv.iweight,
	NULL pid
FROM
	Bd_BomMaster master
	INNER JOIN Bd_Inventory minv ON minv.iAutoId = master.iInventoryId
	LEFT JOIN Bd_Uom uom ON uom.iAutoId = minv.iUomClassId
WHERE
	master.IsDeleted = '0'
	AND master.isEnabled = '1'
	AND master.dEnableDate <= GETDATE()
	AND master.dDisableDate >= GETDATE()
	#if(orgId)
	AND master.iOrgId = #para(orgId)
	#end
UNION ALL
SELECT
	bbc.iAutoId AS id,
	a.cBomVersion,
	minv.cInvCode,
	minv.cInvName,
	minv.cInvStd,
	uom2.cUomName,
	bbc.iQty,
	bbc.iWeight,
	bbc.iPid pid
FROM
	Bd_BomCompare bbc
	INNER JOIN Bd_Inventory minv ON minv.iAutoId = bbc.iInventoryId
	LEFT JOIN Bd_Uom uom2 ON uom2.iAutoId = minv.iUomClassId
	INNER JOIN Bd_BomMaster a ON a.iAutoId = bbc.iBOMMasterId AND a.dEnableDate <= GETDATE() AND a.dDisableDate >= GETDATE()
WHERE
	bbc.IsDeleted = '0'
	AND a.IsDeleted = '0'
	AND a.isEnabled = '1'
	  #if(orgId)
	    AND a.iOrgId = #para(orgId)
	  #end) a
#end
