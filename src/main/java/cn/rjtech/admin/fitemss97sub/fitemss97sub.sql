#sql("paginateAdminDatas")
	select * from Bd_fitemss97sub where 1=1 and isDeleted = '0'
		#if(keywords)
		 and (citemcode like concat('%',#para(keywords),'%') or citemname like concat('%',#para(keywords),'%'))
		#end
		#if(iorgid)
			and iorgid = #para(iorgid)
		#end		
#end