#sql("dashBoardTodoDatas")
	select id,title,url from jb_todo td where 1=1 
	#if(userid)
		and user_id = #para(userid)
	#end
	and exists (
		select 1 from #(getMomdataDbName()).dbo.bd_formapproval fa 
			inner join #(getMomdataDbName()).dbo.bd_formapprovald fad on fa.iautoid = fad.iformapprovalid
			inner join #(getMomdataDbName()).dbo.bd_formapprovalflowm fafm on fafm.iApprovalDid = fad.iautoid
			inner join #(getMomdataDbName()).dbo.bd_formapprovalflowd fafd on fafd.iformapprovalflowmid = fafm.iautoid
		where fa.iformobjectid = td.form_id and fafd.iUserId = td.user_id and fa.isdeleted = 0 and fafd.iAuditStatus = #para(iauditstatus)   
	)
#end