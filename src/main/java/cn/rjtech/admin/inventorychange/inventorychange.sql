#sql("list")
SELECT
	beforinv.iweight,
	beforinv.cInvCode AS beforeInventorycode,
	beforinv.cInvCode1 AS beforeInvCode1,
	beforinv.cInvName1 AS beforeinvname1,
	afterinv.cInvCode AS afterInventorycode,
	afterinv.cInvCode1 AS afterinvcode1,
	afterinv.cInvName1 AS afterinvname1,
	beforuom.cUomName AS beforuomname,
	afteruom.cUomName AS afteruomname,
	invchanage.*
FROM
	Bd_InventoryChange invchanage
	INNER JOIN Bd_Inventory beforinv ON beforinv.iAutoId = invchanage.iBeforeInventoryId
	INNER JOIN Bd_Inventory afterinv ON afterinv.iAutoId= invchanage.iAfterInventoryId
	LEFT JOIN Bd_Uom beforuom ON beforuom.iAutoId = beforinv.iUomClassId
	LEFT JOIN Bd_Uom afteruom ON afteruom.iAutoId = afterinv.iUomClassId
WHERE
	invchanage.IsDeleted = '0'
	#if(beforeInventory)
        AND beforinv.cInvCode1 LIKE CONCAT('', #para(beforeInventory),'')
	#end
	#if(beforeInventoryName)
        AND beforinv.cInvName1 LIKE CONCAT('', #para(beforeInventoryName),'')
	#end
	#if(afterInventory)
        AND afterinv.cInvCode1 LIKE CONCAT('', #para(afterInventory),'')
	#end
	#if(afterInventoryName)
        AND afterinv.cInvName1 LIKE CONCAT('', #para(afterInventoryName),'')
	#end
    #if(ids)
    AND invchanage.iautoid in #(ids)
    #end

    #if(null != sortColumn)
    order by #(sortColumn) #(sortType)
    #end
#end
#sql("inventoryAutocomplete")
SELECT
    inv.iautoid as itemId,
	inv.iweight,
	inv.cInvCode AS cInvCode,
	inv.cInvCode1 AS cInvCode1,
	inv.cInvName1 AS cInvName1,
	uom.cUomName
FROM
	Bd_Inventory inv
	LEFT JOIN Bd_Uom uom ON uom.iAutoId = inv.iUomClassId
	WHERE
	    1 = 1
	    #if(keywords)
            AND (inv.cInvCode LIKE CONCAT('%', #para(keywords), '%') OR inv.cInvCode1 LIKE CONCAT('%', #para(keywords), '%') OR inv.cInvName1 LIKE CONCAT('%', #para(keywords), '%'))
	    #end
	    #if(itemId)
	        AND inv.iautoId = #para(itemId)
	    #end
#end
