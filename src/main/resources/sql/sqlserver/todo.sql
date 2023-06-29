#sql("dashBoardTodoDatas")
	select id,title,url from jb_todo td where 1=1 
	#if(userid)
		and user_id = #para(userid)
	#end
	and exists (
		select 1 from (
			select fa.iformobjectid,fafm.iautoid,
				case fafm.iApprovalWay when 3 then fad.iStatus
					else (select top 1 iAuditStatus from #(getMomdataDbName()).dbo.bd_formapprovalflowd fafd where fafd.iFormApprovalFlowMid = fafm.iautoid and fafd.iUserId = #para(userid)) 
					end iAuditStatus
			from #(getMomdataDbName()).dbo.bd_formapproval fa 
				inner join #(getMomdataDbName()).dbo.bd_formapprovald fad on fa.iautoid = fad.iformapprovalid
				inner join #(getMomdataDbName()).dbo.bd_formapprovalflowm fafm on fafm.iApprovalDid = fad.iautoid
			where 1=1 and exists (
				select 1 from #(getMomdataDbName()).dbo.bd_formapprovalflowd fafd1 where fafd1.iformapprovalflowmid = fafm.iautoid and fafd1.iUserId = #para(userid)
			) and fa.isdeleted = 0
		) T where T.iformobjectid = td.form_id and T.iAuditStatus = #para(iauditstatus)
	)
#end