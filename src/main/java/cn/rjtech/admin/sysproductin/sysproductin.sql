
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
        END AS statename,so.state,so.BillNo as billno,so.CreateDate,so.repositoryName as repositoryname,so.deptName as deptname,
				so.warehousingType as warehousingtype,so.AuditDate,so.BillType,so.remark,p.name
FROM T_Sys_ProductIn so
LEFT JOIN #(getBaseDbName()).dbo.jb_user p on so.CreatePerson = p.username
where 1=1 and so.IsDeleted = '0'
	#if(billno)
		and so.BillNo like concat('%',#para(billno),'%')
	#end
	#if(deptname)
		and so.deptName like concat('%',#para(deptname),'%')
	#end
	#if(repositoryname)
		and so.repositoryName like concat('%',#para(repositoryname),'%')
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
FROM T_Sys_ProductInDetail a
left join T_Sys_ProductIn i on a.MasID = i.AutoID
where 1=1 and a.isdeleted = 0
	#if(masid)
		and a.MasID = #para(masid)
	#end
ORDER BY a.ModifyDate DESC
#end
