#sql("selectEquipmentModels")
SELECT e.* from Bd_EquipmentModel e
 WHERE e.isDeleted = 0
#if(cequipmentmodelcode)
AND e.cequipmentmodelcode = #para(cequipmentmodelcode)
#end
#if(cequipmentmodelname)
AND e.cequipmentmodelname like CONCAT('%', #para(cequipmentmodelname), '%')
#end
#if(sqlids)
AND e.iAutoId in (#(sqlids))
#end
order by e.iAutoId desc
#end