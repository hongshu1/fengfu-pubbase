#sql("getAutocompleteData")
	select top #(limit) i.*,u.cuomname from bd_inventory i
		left join bd_uom u on i.iInventoryUomId1 = u.iautoid
	where 1=1
	#if(q)
		and (i.cinvcode like concat('%',#para(q),'%') or i.cinvcode1 like concat('%',#para(q),'%')
			or i.cinvname1 like concat('%',#para(q),'%') or i.cinvstd like concat('%',#para(q),'%')
			or u.cuomname like concat('%',#para(q),'%')
		)
	#end
#end

#sql("getRoutingInvcs")
select ri.*,i.cInvCode,i.cInvName,i.cInvStd,i.cInvName1,u1.cUomName purchaseuom,u2.cUomName manufactureuom
from Bd_InventoryRoutingInvc ri
left join Bd_Inventory i on i.iAutoId = ri.iInventoryId
left join bd_uom u1 on u1.iAutoId = i.iPurchaseUomId
left join bd_uom u2 on u1.iAutoId = i.iManufactureUomId
 where 1=1
   #if(configid)
   and ri.iInventoryRoutingConfigId = #para(configid)
   #end
   #if(idsstr)
   and ri.iInventoryRoutingConfigId in (#(idsstr))
   #end
#end

#sql("getInventoryDataList")
select i.*,u1.cUomName purchaseuom,u2.cUomName manufactureuom
from  Bd_Inventory i
left join bd_uom u1 on u1.iAutoId = i.iPurchaseUomId
left join bd_uom u2 on u1.iAutoId = i.iManufactureUomId
#end

#sql("getRoutingEqps")
SELECT re.*, e.cEquipmentCode,e.cEquipmentName,w.cWorkName workName,dp.cDepName
 from Bd_InventoryRoutingEquipment re
  join Bd_Equipment e on re.iEquipmentId = e.iAutoId
 left join Bd_WorkRegionM w on e.iworkregionmid = w.iAutoId
 left join Bd_Department dp on dp.iAutoId = w.iDepId
 WHERE re.iInventoryRoutingConfigId = #para(configid)
#end

#sql("options")
select i.*,u1.cUomName
from  Bd_Inventory i
left join bd_uom u1 on u1.iAutoId = i.iInventoryUomId1
where i.isEnabled = '1' and i.isDeleted = '0'
#end

#sql("getInventoryList")
SELECT inv.cinvcode, inv.cinvname, inv.cinvstd, inv.cinvaddcode, inv.cAddress, inv.itaxrate
FROM bd_Inventory inv
WHERE 1 = 1
#if(q)
    AND (cInvCode like CONCAT ('%', #para(q), '%')
    OR  cInvName like CONCAT ('%', #para(q), '%')
    OR  cInvStd like CONCAT ('%', #para(q), '%'))
#end
#if(cinvccode)
 AND cInvCCode LIKE CONCAT (#para(cinvccode), '%')
#end
ORDER BY inv.cinvcode ASC
#end

#sql("findCapacityByInvId")
	SELECT
	bd_wms.cWorkShiftCode,
	bd_wms.cWorkShiftName,
	ic.*
FROM
	Bd_InventoryCapacity ic
	INNER JOIN Bd_WorkShiftM bd_wms ON bd_wms.iAutoId = ic.iWorkShiftMid
WHERE
	bd_wms.isEnabled = '1'
	AND bd_wms.isDeleted = '0'
	AND bd_wms.iOrgId = 1
	#if(invId)
	    AND ic.iInventoryId = #para(invId)
	#end
ORDER BY
	bd_wms.cWorkShiftCode
#end
