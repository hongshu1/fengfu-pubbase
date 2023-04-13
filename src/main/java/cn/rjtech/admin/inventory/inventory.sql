#sql("getAutocompleteData")
	select i.*,u.cuomname from bd_inventory i
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
 left join Bd_Equipment e on re.iEquipmentId = e.iAutoId
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