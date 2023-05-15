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
		,p.name,s.name as sname,v.cVenName as venname
FROM T_Sys_PUReceive so
LEFT JOIN #(getBaseDbName()).dbo.jb_user p on so.CreatePerson = p.username
LEFT JOIN #(getBaseDbName()).dbo.jb_user s on so.AuditPerson = s.username
LEFT JOIN Bd_Vendor v on so.VenCode = v.cVenCode
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
SELECT  a.*,
        i.*,
       u.cUomClassName,
       t3.cInvCCode,
       t3.cInvCName,
       t1.Qty as qtys
FROM T_Sys_PUReceiveDetail a
         LEFT JOIN V_Sys_BarcodeDetail t1 ON a.Barcode = t1.Barcode
         LEFT JOIN bd_inventory i ON i.cinvcode = t1.Invcode
         LEFT JOIN Bd_UomClass u ON i.iUomClassId = u.iautoid
         LEFT JOIN Bd_InventoryClass t3 ON i.iInventoryClassId = t3.iautoid
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
SELECT
    a.cVenCode as vencode,a.cVenName as venname,
    a.*
FROM Bd_Vendor a
where 1=1
	#if(q)
		and (a.cVenCode like concat('%',#para(q),'%') OR a.cVenName like concat('%',#para(q),'%'))
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

