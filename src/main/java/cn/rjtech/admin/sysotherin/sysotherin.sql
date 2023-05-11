
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
        END AS statename,so.state,so.warehousingNo as warehousingno,so.CreateDate,so.repositoryName as repositoryname,
				so.warehousingType as warehousingtype,so.BillType,so.BillNo,so.VenCode,so.venName as venname,so.ModifyDate,so.remark
,p.name
FROM T_Sys_OtherIn so
LEFT JOIN #(getBaseDbName()).dbo.jb_user p on so.CreatePerson = p.id
where so.IsDeleted = '0'
	#if(warehousingno)
		and so.warehousingNo like concat('%',#para(warehousingno),'%')
	#end
	#if(repositoryname)
		and so.repositoryName like concat('%',#para(repositoryname),'%')
	#end
	#if(deptname)
		and so.deptName like concat('%',#para(deptname),'%')
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
FROM T_Sys_OtherInDetail a
left join T_Sys_OtherIn i on a.MasID = i.AutoID
where a.isDeleted = '0'
	#if(masid)
		and a.MasID = #para(masid)
	#end
ORDER BY a.ModifyDate DESC
#end

