#sql("paginateAdminDatas")
	select * from Bd_ForeignCurrency where 1=1 and isDeleted = 0
		#if(keywords)
			and (cexch_name like concat('%',#para(keywords),'%') or  cexch_code like concat('%',#para(keywords),'%'))
		#end
		#if(iorgid)
			and iorgid = #para(iorgid)
		#end			
#end