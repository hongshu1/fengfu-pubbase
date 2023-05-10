
#sql("recpor")
select so.AutoID, CASE so.state
        WHEN 1 THEN
        '已保存'
				WHEN 2 THEN
        '待审批'
				WHEN 3 THEN
        '已审批'
				WHEN 4 THEN
        '审批不通过'
        END AS statename,so.state,so.BillNo as billno,so.transformation,so.CreateDate as createdate,so.DeptCode as deptcode,so.deptName as deptname,
				so.IRdCode as irdcode,so.ORdCode as ordcode,so.AuditPerson as auditperson,so.AuditDate as auditdate,so.Memo as memo,so.CreatePerson as createperson,p.name,s.name as sname
FROM T_Sys_Assem so
LEFT JOIN #(getBaseDbName()).dbo.jb_user p on so.CreatePerson = p.username
LEFT JOIN #(getBaseDbName()).dbo.jb_user s on so.AuditPerson = s.username
where 1=1 and so.IsDeleted = '0'
	#if(billno)
		and so.BillNo like concat('%',#para(billno),'%')
	#end
	#if(deptcode)
		and so.DeptCode like concat('%',#para(deptcode),'%')
	#end
	#if(state)
		and so.state = #para(state)
	#end
	#if(startTime)
		and so.CreateDate >= #para(startTime)
	#end
	#if(endTime)
		and so.CreateDate <= #para(endTime)
	#end
ORDER BY so.ModifyDate DESC
#end

#sql("dList")
SELECT  a.*
FROM T_Sys_AssemDetail a
where 1=1 and a.isdeleted = 0
	#if(masid)
		and a.MasID = #para(masid)
	#end
ORDER BY a.ModifyDate DESC
#end