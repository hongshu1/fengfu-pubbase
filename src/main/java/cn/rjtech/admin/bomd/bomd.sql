#sql("getBomComparePageData")
SELECT
    d.iAutoId,
    d.iInventoryId,
	d.iBomMid,
	d.iPid,
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
	d.iInvPartBomMid,
	uom.cUomName AS cuomname
FROM
	Bd_BomD d
	LEFT JOIN Bd_Uom uom ON uom.iAutoId = d.iInventoryUomId1
WHERE
	d.isDeleted = '0'
	#if(iBomMid)
	AND d.iPid = #para(iBomMid)
	#end
	#if(invCode)
      AND d.cInvCode LIKE CONCAT('%',#para(invCode),'%')
	#end
	#if(invName)
        AND d.cInvName LIKE CONCAT('%',#para(invName),'%')
	#end
	ORDER BY d.iAutoId ASC
#end

#sql("getEffectiveBomCompare")
SELECT
	*
FROM
	Bd_BomD
WHERE
	isDeleted = '0'
	AND EXISTS (
	SELECT
		1
	FROM
		Bd_BomM a
	WHERE
		a.isDeleted = '0'
		AND a.iOrgId = #para(orgId)
		AND a.dEnableDate <= CONVERT ( DATE, GETDATE( ) )
		AND a.dDisableDate >= CONVERT ( DATE, GETDATE( ) )

		AND a.iAutoId = iPid
		#if(invIds)
            AND iInventoryId NOT IN (
                #for(id:invIds)
                    '#(id)' #(for.last?'':',')
                #end
            )
        #end
	)
#end

#sql("getParentBomList")
SELECT
	master.iAutoId,
	NULL AS iPid,
	master.iInventoryId,
	master.cInvCode,
	master.cInvName
FROM
	Bd_BomM master
WHERE
	master.IsDeleted = '0'

	#if(orgId)
	AND master.iOrgId = #para(orgId)
	#end
#end
