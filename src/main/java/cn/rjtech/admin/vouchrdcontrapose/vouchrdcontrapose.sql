#sql("paginateAdminDatas")
	select * from Bd_VouchRdContrapose where isDeleted = '0'
	#if(keywords)
		and (cVRRCode like concat('%',#para(keywords),'%') or cVRSCode like concat('%',#para(keywords),'%'))
	#end
	#if(iorgid)
		and iorgid = #para(iorgid)
	#end		
#end