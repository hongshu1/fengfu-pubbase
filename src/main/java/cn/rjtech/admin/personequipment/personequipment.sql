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

#sql("getPersonequipmentByCpsnnumId")
SELECT
	bpe.iautoid,
	bpe.iequipmentid,
	be.cequipmentcode,
	be.cequipmentname,
	bwrm.cWorkCode,
	bwrm.cworkname
FROM
	bd_personequipment bpe
	LEFT JOIN Bd_Person person ON bpe.iPersonId = person.iautoid
	LEFT JOIN bd_equipment be ON bpe.iequipmentid = be.iautoid
	LEFT JOIN bd_workregionm bwrm ON bwrm.iautoid = be.iworkregionmid
WHERE
	bpe.isdeleted = 0
	AND person.cPsn_Num= #para(cpsnnum)
	AND bwrm.cWorkCode= ( SELECT cWorkCode FROM Bd_WorkRegionM WHERE iAutoId = #(iautoid))
#end