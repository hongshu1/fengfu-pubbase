#sql("findRoutingAll")
SELECT
    a.iAutoId as iSourceId,
    a.*
FROM Bd_InvPart a
LEFT JOIN Bd_Inventory inv on inv.iAutoId = a.iInventoryId
WHERE a.iOrgId = #para(orgId) AND a.isEffective = '1'
	 #if(keyWords)
        AND (
           (inv.cInvCode LIKE CONCAT('%',#para(keyWords),'%')) OR ( inv.cInvName LIKE CONCAT('%',#para(keyWords),'%') )
          OR (inv.cInvCode1 LIKE CONCAT('%',#para(keyWords),'%')) OR ( inv.cInvAddCode1 LIKE CONCAT('%',#para(keyWords),'%') )
          OR (inv.cInvName1 LIKE CONCAT('%',#para(keyWords),'%')) OR ( inv.cInvName2 LIKE CONCAT('%',#para(keyWords),'%') )
          OR (a.cPartName LIKE CONCAT('%',#para(keyWords),'%'))
        )
	  #end
	ORDER BY a.iSeq ASC
#end

#sql("findParentAll")
SELECT * FROM Bd_InvPart a WHERE a.iOrgId = #para(orgId) AND ISNULL(a.iPid, '') = '' AND a.isEffective = '1'
#end

#sql("getRoutingDetails")
SELECT
    *
FROM (
SELECT
	a.iAutoId,
	a.cPartName,
	a.iInventoryId,
	a.iPid,
	a.iParentInvId,
CASE
	WHEN ISNULL( inv.cInvName, '' ) = '' THEN
		'【虚拟件-' + routingc.cOperationName+ '】' ELSE inv.cInvName
	END AS rsinventoryname,
	inv.cInvCode,
	inv.cInvCode1,
	inv.cInvName1,
	STUFF(
		(
		SELECT
			', ' + be.cEquipmentName
		FROM
			Bd_InventoryRoutingConfig birc2
			LEFT JOIN Bd_InventoryRoutingEquipment bire ON birc2.iAutoId = bire.iInventoryRoutingConfigId
			LEFT JOIN Bd_Equipment be ON be.iAutoId = bire.iEquipmentId
		WHERE
			birc2.iAutoId = routingc.iAutoId FOR XML PATH ( '' )
		),
		1,
		2,
		''
	) AS cequipmentnames,
	routingc.iSeq,
    routingc.cMergedSeq,
    routingc.cOperationName,
    routingc.iType,
    routingc.iRsInventoryId,
    routingc.cProductSn,
    routingc.cProductTechSn,
    routingc.iMergedNum,
    routingc.iMergeRate,
    routingc.iMergeSecs,
    routingc.iSecs,
routingc.cMemo
FROM
	Bd_InvPart a
	INNER JOIN Bd_InventoryRoutingConfig routingc ON routingc.iAutoId = a.iInventoryRoutingConfigId
	LEFT JOIN Bd_Inventory inv ON inv.iAutoId = routingc.iRsInventoryId
WHERE
	a.isEffective = '1'
	#if(orgId)
	    AND a.iOrgId = #para(orgId)
	#end
	#if(iParentInvId)
	   AND a.iParentInvId = #para(iParentInvId)
	#end
) a
WHERE
    1 = 1
	#if(cInvCode)
	   AND (a.cInvCode LIKE CONCAT('%',#para(cInvCode),'%'))
	#end
	#if(cInvCode1)
	   AND (a.cInvCode1 LIKE CONCAT('%',#para(cInvCode1),'%'))
	#end
	#if(cInvName1)
	   AND (a.cInvName1 LIKE CONCAT('%',#para(cInvName1),'%'))
	#end
ORDER BY
	a.iSeq ASC
#end

#sql("findRoutingVersion")
SELECT
	inv.cInvCode,
	inv.cInvCode1,
	inv.cInvName,
	inv.cInvName1,
	routing.*
FROM
	Bd_InventoryRouting routing
	LEFT JOIN Bd_Inventory inv ON inv.iAutoId = routing.iInventoryId
WHERE
    1 = 1
    #if(orgId)
        AND inv.iOrgId = #para(orgId)
    #end
    #if(id)
         AND routing.iAutoId = #para(id)
    #end
    #if(cRoutingName)
        AND (routing.cRoutingName LIKE CONCAT('%',#para(cRoutingName),'%'))
    #end
    #if(cInvCode)
	   AND (inv.cInvCode LIKE CONCAT('%',#para(cInvCode),'%'))
	#end
	#if(cInvCode1)
	   AND (inv.cInvCode1 LIKE CONCAT('%',#para(cInvCode1),'%'))
	#end
	#if(cInvName1)
	   AND (inv.cInvName1 LIKE CONCAT('%',#para(cInvName1),'%'))
	#end
#end
