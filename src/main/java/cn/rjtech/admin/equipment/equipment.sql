#sql("selectWorkRegs")
SELECT iAutoId,cWorkName from Bd_WorkRegionM WHERE isDeleted = 0
#end

#sql("selectEquipments")
SELECT e.*,w.cWorkName workName,d.name stateName,dp.cDepName from Bd_Equipment e
 left join Bd_WorkRegionM w on e.iworkregionmid = w.iAutoId
 left join Bd_Department dp on dp.iAutoId = w.iDepId
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