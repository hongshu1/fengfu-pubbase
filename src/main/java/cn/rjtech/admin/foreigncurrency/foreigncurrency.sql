#sql("paginateAdminDatas")
	select * from Bd_ForeignCurrency where isDeleted = 0
		#if(keywords)
			and (cexch_name like concat('%',#para(keywords),'%') or  cexch_code like concat('%',#para(keywords),'%'))
		#end
		#if(iorgid)
			and iorgid = #para(iorgid)
		#end
		#if(id)
           and iautoid = #para(id)
		#end
#end
