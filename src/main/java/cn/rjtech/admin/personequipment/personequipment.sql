#sql("findEditableDatas")
	select bpe.iautoid,bpe.iequipmentid,be.cequipmentcode,be.cequipmentname,bwrm.cworkname
	from bd_personequipment bpe
		left join bd_equipment be on bpe.iequipmentid = be.iautoid 
		left join bd_workregionm bwrm on bwrm.iautoid = be.iworkregionmid
		where bpe.isdeleted = 0
	#if(iPersonId)
		and bpe.iPersonId = #para(iPersonId) 
	#end
#end