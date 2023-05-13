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
        END AS statename,so.state,so.BillNo as billno,so.CreateDate as createdate,
        so.VenCode as vencode
		,p.name,s.name as sname
FROM T_Sys_PUReceive so
LEFT JOIN #(getBaseDbName()).dbo.jb_user p on so.CreatePerson = p.username
LEFT JOIN #(getBaseDbName()).dbo.jb_user s on so.AuditPerson = s.username
where 1=1
	#if(billno)
		and so.BillNo like concat('%',#para(billno),'%')
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
FROM T_Sys_PUReceiveDetail a
where 1=1
	#if(masid)
		and a.MasID = #para(masid)
	#end
     #if(spotticket)
    and a.spotticket = #para(spotticket)
     #end
ORDER BY a.ModifyDate DESC
#end


#sql("venCode")
SELECT  a.*
FROM V_Sys_Vendor a
where 1=1
	#if(q)
		and (a.VenCode like concat('%',#para(q),'%') OR a.VenName like concat('%',#para(q),'%'))
	#end
#end


#sql("Whcode")
SELECT  a.*
FROM V_Sys_WareHouse a
where 1=1
	#if(q)
		and (a.whcode like concat('%',#para(q),'%') OR a.whName like concat('%',#para(q),'%'))
	#end
#end
