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
		,p.name,s.name as sname,v.cVenName as venname,so.Type as type
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
    m.cOrderNo as sourcebillno,
    aa.cBarcode as barcode,
    b.cInvCode ,
    b.cInvCode1,
    b.cInvName1,
    aa.dPlanDate as plandate,
    aa.iQty as qtys,
    m.cOrderNo as SourceBillNo,
    m.iBusType as SourceBillType,
    d.iAutoId as SourceBillNoRow,
    m.cOrderNo as SourceBillID,
    d.iAutoId as SourceBillDid,
    m.iVendorId
FROM T_Sys_PUReceiveDetail a
LEFT JOIN PS_PurchaseOrderDBatch aa on aa.cBarcode = a.Barcode
LEFT JOIN Bd_Inventory b on aa.iinventoryId = b.iAutoId
LEFT JOIN PS_PurchaseOrderD d on aa.iPurchaseOrderDid = d.iAutoId
LEFT JOIN PS_PurchaseOrderM m on m.iAutoId = d.iPurchaseOrderMid
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
SELECT  a.*,a.cWhCode as whcode,a.cWhName as whname
FROM Bd_Warehouse a
where 1=1
	#if(q)
		and (a.cWhCode like concat('%',#para(q),'%') OR a.cWhName like concat('%',#para(q),'%'))
	#end
#end


#sql("wareHousepos")
SELECT  a.*
FROM Bd_Warehouse_Shelves a
where 1=1

	#if(q)
		and (a.cShelvesCode like concat('%',#para(q),'%') OR a.cShelvesName like concat('%',#para(q),'%'))
	#end
	#if(whcodeid)
		and a.iWarehouseId = #para(whcodeid)
	#end
#end



#sql("getBarcodeDatas")
select top #(limit)
    m.cOrderNo as sourcebillno,
    a.cBarcode as barcode,
    b.cInvCode ,
    b.cInvCode1,
    b.cInvName1,
    a.dPlanDate as plandate,
    b.cInvStd as cinvstd,
    a.iQty as qty,
    m.cOrderNo as SourceBillNo,
    m.iBusType as SourceBillType,
    d.iAutoId as SourceBillNoRow,
    m.cOrderNo as SourceBillID,
    d.iAutoId as SourceBillDid,
    m.iVendorId
FROM PS_PurchaseOrderDBatch a
LEFT JOIN Bd_Inventory b on a.iinventoryId = b.iAutoId
LEFT JOIN PS_PurchaseOrderD d on a.iPurchaseOrderDid = d.iAutoId
LEFT JOIN PS_PurchaseOrderM m on m.iAutoId = d.iPurchaseOrderMid
where a.isEffective = '1'
    #if(q)
		and (b.cinvcode like concat('%',#para(q),'%') or b.cinvcode1 like concat('%',#para(q),'%')
			or b.cinvname1 like concat('%',#para(q),'%') or a.cBarcode like concat('%',#para(q),'%')
		)
	#end
	 AND b.cOrgCode = #(orgCode)

	#if(vencode)
		and m.iVendorId = #para(vencode)
	#end
#end



#sql("getsourcebillno")
SELECT  a.*
FROM V_Sys_PODetail a
where 1=1
	#if(sourcebillno)
		and a.SourceBillNo = #para(sourcebillno)
	#end
#end




#sql("selectRdCode")
SELECT
    p.*,
    s.iAutoId as iAutoId2,
    s.cRdName,
    s.cRdCode
FROM
    Bd_PurchaseType p
        LEFT JOIN Bd_Rd_Style s ON s.cRdCode = p.cRdCode
WHERE p.IsDeleted = '0'
#if(cPTCode)
  and p.cPTCode like CONCAT('%', #para(cPTCode), '%')
#end
#if(cPTName)
and p.cPTName like CONCAT('%', #para(cPTName), '%')
#end
#end


