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
    inv.iAutoId as itemId,
	inv.iweight,
	inv.cInvAddCode1,
	inv.cInvName,
	inv.cInvStd,
	inv.cInvCode AS cInvCode,
	inv.cInvCode1 AS cInvCode1,
	inv.cInvName1 AS cInvName1,
	inv.cInvName2 AS cInvName2,
	uom.cUomName,
	inv.iCustomerMId,
	ven.iAutoId AS venid,
	ven.cVenCode,
	ven.cVenName
FROM
	Bd_Inventory inv
	LEFT JOIN Bd_Uom uom ON uom.iAutoId = inv.iInventoryUomId1
	LEFT JOIN Bd_InventoryStockConfig invstock ON invstock.iInventoryId = inv.iAutoId
	LEFT JOIN Bd_Vendor ven ON ven.iAutoId = invstock.iVendorId
	WHERE
	    1 = 1
	    #if(q)
            AND (inv.cInvCode LIKE CONCAT('%', #para(q), '%') OR inv.cInvCode1 LIKE CONCAT('%', #para(q), '%') OR inv.cInvName1 LIKE CONCAT('%', #para(q), '%'))
	    #end
	    #if(itemId)
	        AND inv.iautoId = #para(itemId)
	    #end
	    #if(iEquipmentModelId)
	        AND INV.iEquipmentModelId = #para(iEquipmentModelId)
	    #end
	    #if(cInvCode)
             AND inv.cInvCode LIKE CONCAT('%', #para(cInvCode), '%')
	    #end
	    #if(cInvCode1)
             AND inv.cInvCode LIKE CONCAT('%', #para(cInvCode1), '%')
	    #end
	    #if(cInvName)
             AND inv.cInvName LIKE CONCAT('%', #para(cInvName), '%')
	    #end
	    #if(cVenName)
	        AND ven.cVenName LIKE CONCAT('%', #para(cVenName), '%')
	    #end
#end

#sql("findList")
SELECT
	a.iAfterInventoryId,
	a.iBeforeInventoryId,
	a.iChangeRate,
	inv.cInvStd,
	inv.cInvCode as afterCInvCode,
	inv.cInvName as afterCInvName,
	inv.cInvName1 as afterCInvName1,
	inv.cInvCode1 as afterCInvCode1,
	inv.isGavePresent as isGavePresent,
	inv.iPkgQty as iPkgQty,
	inv.iPurchaseUomId,
	uom.cUomName
FROM
	Bd_InventoryChange a
	INNER JOIN Bd_Inventory inv ON inv.iautoId = a.iBeforeInventoryId
	AND inv.isDeleted = '0'
	LEFT JOIN Bd_Uom uom ON uom.iAutoId = inv.iPurchaseUomId
	AND uom.isDeleted = '0'
WHERE
	a.iOrgId = #para(orgId)
	AND a.IsDeleted = 0
#end
