#sql("paginateAdminDatas")
	select * from Bd_VouchRdContrapose where 1=1 and isdeleted = 0
	#if(keywords)
		and (cVRRCode like concat('%',#para(keywords),'%') or cVRSCode like concat('%',#para(keywords),'%'))
	#end
#end