#sql("datas")
SELECT * FROM (SELECT
	master.iAutoId AS id,
	master.cBomVersion,
	minv.cInvCode,
	minv.cInvName,
	minv.cInvStd,
	minv.cInvCode1,
	minv.cInvAddCode1,
	minv.cInvName1,
	minv.cInvName2,
	uom.cUomName,
    NULL iQty,
    minv.iweight,
    master.iOrgId,
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
UNION ALL
SELECT
	bbc.iAutoId AS id,
	a.cBomVersion,
	minv.cInvCode,
	minv.cInvName,
	minv.cInvStd,
	minv.cInvCode1,
	minv.cInvAddCode1,
	minv.cInvName1,
	minv.cInvName2,
	uom2.cUomName,
	bbc.iQty,
	bbc.iWeight,
	a.iOrgId,
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
	  ) a
	   WHERE 1 = 1
	  #if(orgId)
	    AND a.iOrgId = #para(orgId)
	  #end
	  #if(pid)
        AND (a.id = #para(pid) OR a.pid = #para(pid))
	  #end
	  #if(keyWords)
        AND (
           (a.cInvCode LIKE CONCAT('%',#para(keyWords),'%')) OR ( a.cInvName LIKE CONCAT('%',#para(keyWords),'%') )
          OR (a.cInvCode1 LIKE CONCAT('%',#para(keyWords),'%')) OR ( a.cInvAddCode1 LIKE CONCAT('%',#para(keyWords),'%') )
          OR (a.cInvName1 LIKE CONCAT('%',#para(keyWords),'%')) OR ( a.cInvName2 LIKE CONCAT('%',#para(keyWords),'%') )
        )
	  #end
#end

#sql("getVersionRecord")
SELECT
	master.iAutoId AS id,
	master.cBomVersion,
	master.dEnableDate,
	master.dDisableDate,
	master.iAuditStatus,
	master.cCreateName,
	master.dCreateTime,
	minv.cInvCode,
	minv.cInvName,
	minv.cInvStd,
	minv.cInvCode1,
	minv.cInvAddCode1,
	minv.cInvName1,
	minv.cInvName2,
	uom.cUomName
FROM
	Bd_BomMaster master
	INNER JOIN Bd_Inventory minv ON minv.iAutoId = master.iInventoryId
	LEFT JOIN Bd_Uom uom ON uom.iAutoId = minv.iUomClassId
WHERE
	master.IsDeleted = '0'
	AND master.isEnabled = '1'
	#if(orgId)
	AND  master.iOrgId = #para(orgId)
	#end
	#if(cInvCode)
	    AND minv.cInvCode LIKE CONCAT('%',#para(cInvCode),'%')
	#end
	#if(cInvCode1)
	    AND minv.minv.cInvCode1 LIKE CONCAT('%',#para(cInvCode1),'%')
	#end
	#if(cInvName1)
	    AND minv.minv.cInvName1 LIKE CONCAT('%',#para(cInvName1),'%')
	#end
	ORDER BY master.dCreateTime ASC
#end

#sql("queryBomMasterId")
SELECT a.id FROM (SELECT
	master.iAutoId AS id,
	minv.cInvCode,
	minv.cInvName,
	minv.cInvStd,
	minv.cInvCode1,
	minv.cInvAddCode1,
	minv.cInvName1,
	minv.cInvName2,
    master.iOrgId
FROM
	Bd_BomMaster master
	INNER JOIN Bd_Inventory minv ON minv.iAutoId = master.iInventoryId
	LEFT JOIN Bd_Uom uom ON uom.iAutoId = minv.iUomClassId
WHERE
	master.IsDeleted = '0'
	AND master.isEnabled = '1'
	AND master.dEnableDate <= GETDATE()
	AND master.dDisableDate >= GETDATE()
UNION ALL
SELECT
	bbc.iBOMMasterId AS id,
	minv.cInvCode,
	minv.cInvName,
	minv.cInvStd,
	minv.cInvCode1,
	minv.cInvAddCode1,
	minv.cInvName1,
	minv.cInvName2,
	a.iOrgId
FROM
	Bd_BomCompare bbc
	INNER JOIN Bd_Inventory minv ON minv.iAutoId = bbc.iInventoryId
	INNER JOIN Bd_BomMaster a ON a.iAutoId = bbc.iBOMMasterId AND a.dEnableDate <= GETDATE() AND a.dDisableDate >= GETDATE()
WHERE
	bbc.IsDeleted = '0'
	AND a.IsDeleted = '0'
	AND a.isEnabled = '1'
	  ) a
	   WHERE 1 = 1
	  #if(orgId)
	    AND a.iOrgId = #para(orgId)
	  #end
	  #if(keyWords)
        AND (
           (a.cInvCode LIKE CONCAT('%',#para(keyWords),'%')) OR ( a.cInvName LIKE CONCAT('%',#para(keyWords),'%') )
          OR (a.cInvCode1 LIKE CONCAT('%',#para(keyWords),'%')) OR ( a.cInvAddCode1 LIKE CONCAT('%',#para(keyWords),'%') )
          OR (a.cInvName1 LIKE CONCAT('%',#para(keyWords),'%')) OR ( a.cInvName2 LIKE CONCAT('%',#para(keyWords),'%') )
        )
	  #end
	  group by a.id
#end
