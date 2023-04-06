#sql("datas")
SELECT DISTINCT * FROM (SELECT
	master.iAutoId AS id,
	master.cBomVersion,
	minv.cInvCode,
	minv.cInvName,
	minv.cInvStd,
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

#sql("test")
WITH a ( iAutoId, pid ) AS (
	SELECT
		b1.iAutoId ,
		b1.iPid AS pid
	FROM
		Bd_BomCompare AS b1  WHERE b1.iPid = 1642802976143011841 OR b1.iAutoId = 1642802976143011841
	UNION ALL
	SELECT
		b2.iAutoId ,
		b2.iPid AS pid
	FROM
		Bd_BomCompare AS b2
		INNER JOIN a ON b2.iPid  = a.iAutoId
	)
	SELECT DISTINCT
	inv.cInvCode,b.iautoId
FROM
	a
	INNER JOIN Bd_BomCompare b ON a.iAutoId = b.iAutoId
	INNER JOIN Bd_Inventory inv ON inv.iAutoId = b.iInventoryId
#end
