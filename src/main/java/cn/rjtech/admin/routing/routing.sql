#sql("findRoutingAll")
SELECT
    a.iAutoId as iSourceId,
    a.*
FROM Bd_InvPart a
LEFT JOIN Bd_Inventory inv on inv.iAutoId = a.iInventoryId
WHERE a.iOrgId = #para(orgId)
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
SELECT * FROM Bd_InvPart a WHERE a.iOrgId = #para(orgId) AND ISNULL(a.iPid, '') = ''
#end
