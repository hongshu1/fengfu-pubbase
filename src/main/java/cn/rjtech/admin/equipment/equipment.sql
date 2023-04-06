#sql("selectWorkRegs")
SELECT iAutoId,cWorkName from Bd_WorkRegionM WHERE isDeleted = 0
#end

#sql("selectEquipments")
SELECT e.*,w.cWorkName workName,d.name stateName from Bd_Equipment e
 left join Bd_WorkRegionM w on e.iworkregionmid = w.iAutoId
 left join #(getBaseDbName()).dbo.jb_dictionary d on e.istatus = d.sn and d.type_key = 'healthy_type'
 WHERE e.isDeleted = 0
#if(cequipmentcode)
AND e.cequipmentcode = #para(cequipmentcode)
#end
#if(cequipmentname)
AND e.cequipmentname like CONCAT('%', #para(cequipmentname), '%')
#end
#if(iworkregionmid)
AND e.iworkregionmid = #para(iworkregionmid)
#end
#if(sqlids)
AND e.iAutoId in (#(sqlids))
#end
order by e.iAutoId desc
#end

#sql("getAutocompleteDatas")
	select top #(limit) ep.*,wrm.cworkname from Bd_Equipment ep 
		left join Bd_WorkRegionM wrm on ep.iWorkRegionmId = wrm.iautoid 
	where ep.isenabled = 1
	#if(q)
		and (cequipmentcode like concat('%',#para(q),'%') or cequipmentname like concat('%',#para(q),'%') or wrm.cworkname like concat('%',#para(q),'%'))
	#end
#end