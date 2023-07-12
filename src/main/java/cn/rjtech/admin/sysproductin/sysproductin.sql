
#sql("recpor")
select so.AutoID, CASE so.iAuditStatus
        WHEN 0 THEN
        '未审核'
				WHEN 1 THEN
        '待审核'
				WHEN 2 THEN
        '审核通过'
				WHEN 3 THEN
        '审核不通过'
        END AS statename,so.iAuditStatus,so.BillNo as billno,so.dcreatetime,so.Whcode,ck.cWhName as repositoryname,so.DeptCode,dept.cDepName as deptname,
				so.RdCode,rd.cRdName as rdcodename,so.BillType,so.Memo,so.cupdatename as name,so.cAuditname as sname
FROM T_Sys_ProductIn so
LEFT JOIN Bd_Warehouse ck on so.Whcode = ck.cWhCode
LEFT JOIN Bd_Department dept on so.DeptCode =dept.cDepCode
LEFT JOIN Bd_Rd_Style rd on so.RdCode = rd.cRdCode
where so.isDeleted = '0'
#if(billno)
	and so.BillNo like concat('%',#para(billno),'%')
#end
#if(deptname)
	and dept.cDepName = #para(deptname)
#end
#if(whname)
	and ck.cWhName = #para(whname)
#end
#if(state)
    and so.iAuditStatus = #para(state)
#end
#if(startTime)
	and so.dcreatetime >= #para(startTime)
#end
#if(endTime)
	and so.dcreatetime <= #para(endTime)
#end
ORDER BY so.dupdatetime DESC
#end


#sql("dList")
select
    a.iMoDocId,a.cCompleteBarcode as barcode,
    b.cInvCode as invcode,b.cInvAddCode as invaddcode,
    b.cInvName as invname,b.cInvStd as invstd,
    b.cInvCode,b.cInvAddCode,b.cInvName,b.cInvStd ,
    uom.cUomCode,uom.cUomName,
    a.iQty as qty,
    mo.cEquipmentModelCode,mo.cEquipmentModelName,
    a.iMoDocId as SourceBillNo,a.iMoDocId+'-'+CAST(a.cVersion AS NVARCHAR(10)) as SourceBillNoRow,
	a.iMoDocId as SourceBillID,	a.iMoDocId as SourceBillDid
FROM T_Sys_ProductInDetail detail
left join Mo_MoInvBatch a on detail.barcode = a.cCompleteBarcode
left join Bd_Inventory b on a.iinventoryId = b.iAutoId
LEFT JOIN Bd_Uom uom on b.iInventoryUomId1 = uom.iAutoId
LEFT JOIN Bd_EquipmentModel	mo	on b.iEquipmentModelId = mo.iAutoId
left join T_Sys_ProductInDetail pd on pd.Barcode = a.cCompleteBarcode
where 1=1
#if(masid)
	and detail.MasID = #para(masid)
#end
	order by a.dupdatetime desc
#end

#sql("wareHouse")
select
    a.*
from Bd_Warehouse a
where 1=1
#if(q)
	and (a.cWhCode like concat('%',#para(q),'%') OR a.cWhName like concat('%',#para(q),'%'))
#end
#end



#sql("RdStyle")
select
    a.*
from Bd_Rd_Style a
where a.bRdFlag = '1'
#if(q)
	and (a.cRdCode like concat('%',#para(q),'%') OR a.cRdName like concat('%',#para(q),'%'))
#end
#end


#sql("Department")
select
	a.*
from Bd_Department a
where 1=1
#if(q)
	and (a.cdepCode like concat('%',#para(q),'%') OR a.cdepName like concat('%',#para(q),'%'))
#end
#end

#sql("selectname")
select
    u.*
from #(getBaseDbName()).dbo.jb_user u
where 1=1
#if(username)
	and u.username= #para(username)
#end
#end



#sql("getBarcodeDatas")
select  top #(limit)
    a.iMoDocId,a.cCompleteBarcode as barcode,
    b.cInvCode as invcode,b.cInvAddCode as invaddcode,
    b.cInvName as invname,b.cInvStd as invstd,
    b.cInvCode,b.cInvAddCode,b.cInvName,b.cInvStd ,
    uom.cUomCode,uom.cUomName,
    a.iQty as qty,
    mo.cEquipmentModelCode,mo.cEquipmentModelName,
    a.iMoDocId as SourceBillNo,a.iMoDocId+'-'+CAST(a.cVersion AS NVARCHAR(10)) as SourceBillNoRow,
	a.iMoDocId as SourceBillID,	a.iMoDocId as SourceBillDid
FROM Mo_MoInvBatch a
left join Bd_Inventory b on a.iinventoryId = b.iAutoId
LEFT JOIN Bd_Uom uom on b.iInventoryUomId1 = uom.iAutoId
LEFT JOIN Bd_EquipmentModel	mo	on b.iEquipmentModelId = mo.iAutoId
left join T_Sys_ProductInDetail pd on pd.Barcode = a.cCompleteBarcode
where a.isEffective = '1'
    #if(q)
		and (b.cinvcode like concat('%',#para(q),'%') or b.cinvcode1 like concat('%',#para(q),'%')
			or b.cinvname1 like concat('%',#para(q),'%') or a.cCompleteBarcode like concat('%',#para(q),'%')

		)
	#end
		AND b.cOrgCode = #(orgCode)
        AND pd.AutoID IS NULL
#end

#sql("barcodeDatas")
SELECT *
FROM T_Sys_ProductInDetail
WHERE isDeleted = 0
   	#if(barcode)
		and Barcode = #para(barcode)
	#end
#end


#sql("barcode")
select
    a.iMoDocId,a.cCompleteBarcode as barcode,
    b.cInvCode as invcode,b.cInvAddCode as invaddcode,
    b.cInvName as invname,b.cInvStd as invstd,
    b.cInvCode,b.cInvAddCode,b.cInvName,b.cInvStd ,
    uom.cUomCode,uom.cUomName,
    a.iQty as qty,
    mo.cEquipmentModelCode,mo.cEquipmentModelName,
    a.iMoDocId as SourceBillNo,a.iMoDocId+'-'+CAST(a.cVersion AS NVARCHAR(10)) as SourceBillNoRow,
	a.iMoDocId as SourceBillID,	a.iMoDocId as SourceBillDid
FROM Mo_MoInvBatch a
left join Bd_Inventory b on a.iinventoryId = b.iAutoId
LEFT JOIN Bd_Uom uom on b.iInventoryUomId1 = uom.iAutoId
LEFT JOIN Bd_EquipmentModel	mo	on b.iEquipmentModelId = mo.iAutoId
left join T_Sys_ProductInDetail pd on pd.Barcode = a.cCompleteBarcode
where a.isEffective = '1'
   	#if(barcode)
		and a.cCompleteBarcode = #para(barcode)
	#end
#end