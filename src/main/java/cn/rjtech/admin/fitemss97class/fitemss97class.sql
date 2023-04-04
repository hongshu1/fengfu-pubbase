#sql("paginateAdminDatas")
	select * from Bd_fitemss97class where 1=1 and isdeleted = 0
	#if(keywords)
	 and (cItemCcode like concat('%',#para(keywords),'%') or cItemCname like concat('%',#para(keywords),'%')) 
	#end
	#if(iorgid)
		and iorgid = #para(iorgid)
	#end	
#end