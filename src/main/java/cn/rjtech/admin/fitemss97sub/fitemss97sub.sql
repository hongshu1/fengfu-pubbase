#sql("paginateAdminDatas")
	select * from Bd_fitemss97sub where 1=1 and isdeleted = 0
		#if(keywords)
		 and (citemcode like concat('%',#para(keywords),'%') or citemname like concat('%',#para(keywords),'%'))
		#end
#end