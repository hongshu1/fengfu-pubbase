
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
        END AS statename,so.iAuditStatus,so.BillNo as billno,so.dcreatetime as createdate,so.DeptCode as deptcode,
				so.IRdCode as irdcode,so.ORdCode as ordcode,so.cAuditname as auditperson,so.dAuditTime as auditdate,so.Memo as memo,so.ccreatename as createperson,
				a.name as billtypename,st.cRdName as irdcodename,stc.cRdName as ordcodename,so.U8BillNo
FROM T_Sys_Assem so
LEFT JOIN #(getBaseDbName()).dbo.jb_dictionary a ON so.BillType = a.id
LEFT JOIN Bd_Rd_Style st on st.cRdCode = so.IRdCode
LEFT JOIN Bd_Rd_Style stc on stc.crdcode = so.ORdCode

where 1=1
	#if(billno)
		and so.BillNo like concat('%',#para(billno),'%')
	#end
	#if(deptcode)
		and so.DeptCode like concat('%',#para(deptcode),'%')
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
SELECT t1.AutoID as autoiddetail, ba.Barcode,ba.Barcode as barbarcode, ba.AutoID as barautoid,ba.Qty,ba.InvCode,ba.InvCode AS cinvcode,t1.WhCode as WhCodeh,t1.PosCode as PosCodeh,t1.*, t2.*
FROM	T_Sys_AssemBarcode ba
	LEFT JOIN T_Sys_AssemDetail t1 ON t1.AutoID = ba.MasID
         LEFT JOIN (
SELECT
	a.WhCode,
	wh.cWhName AS whname,
	a.PosCode,
	area.cAreaName AS posname,
	a.Barcode as tionbarcode,
	b.cInvCode1,
	b.cInvName1,
	uom.cUomName ,
	change.iBeforeInventoryId,
    change.iAfterInventoryId
FROM
	T_Sys_StockBarcodePosition a
	LEFT JOIN Bd_Warehouse wh ON a.WhCode = wh.cWhCode
	LEFT JOIN Bd_Warehouse_Area area ON area.cAreaCode = a.PosCode
	LEFT JOIN UFDATA_001_2023.dbo.V_sys_BarcodeDetail ua ON ua.Barcode = a.Barcode
	LEFT JOIN Bd_Customer mer ON ua.CusCode = mer.cCusCode
	LEFT JOIN bd_inventory b ON ua.InvCode = b.cInvCode
	LEFT JOIN Bd_Uom uom ON b.iInventoryUomId1 = uom.iAutoId
	LEFT JOIN Bd_InventoryChange change ON change.iBeforeInventoryId= b.iAutoId and change.IsDeleted=0
	LEFT JOIN Bd_InventoryStockConfig config ON config.iInventoryId = b.iAutoId

         ) t2 on ba.Barcode = t2.tionbarcode

where 1=1
	#if(masid)
		and t1.MasID = #para(masid)
	#end

#end



#sql("dictionary")
SELECT  a.*
FROM #(getBaseDbName()).dbo.jb_dictionary a
where a.type_id='1659456737559646208' and a.enable='1'
	#if(q)
		and (a.name like concat('%',#para(q),'%') OR a.type_key like concat('%',#para(q),'%'))
	#end
	#if(id)
		and a.id = #para(id)
	#end
ORDER BY a.sort_rank asc
#end


#sql("style")
SELECT  a.*
FROM Bd_Rd_Style a
where a.isDeleted = '0'
	#if(brdflag)
		and a.bRdFlag = #para(brdflag)
	#end
	#if(q)
		and (a.cRdCode like concat('%',#para(q),'%') OR a.cRdName like concat('%',#para(q),'%'))
	#end
	#if(crdcode)
		and a.cRdCode = #para(crdcode)
	#end
ORDER BY a.dUpdateTime asc
#end




#sql("getBarcodeDatas")
select top #(limit)
       t1.Barcode,
       t1.SourceID as SourceBIllNoRow,
       t1.SourceBillType as SourceBillType,
       t1.SourceBillNo,t1.Qty,t1.BarcodeDate,
       t1.BarcodeID as SourceBillID,
       t1.MasID as SourceBillDid,
       t1.pat,
       i.*,

       (SELECT cUomName FROM Bd_Uom WHERE i.iInventoryUomId1 = iautoid) as InventorycUomName,
       (SELECT cUomName FROM Bd_Uom WHERE i.iPurchaseUomId = iautoid) as PurchasecUomName,
       (SELECT cContainerCode FROM Bd_Container WHERE i.iContainerClassId = iautoid) as cContainerCode,
       u.cUomClassName,
       t3.cInvCCode,
       t3.cInvCName,
       t4.cEquipmentModelName,
			 ic.iBeforeInventoryId as iciBeforeInventoryId,
			 ii.cInvCode as iicInvCode,
			 ii.cInvName as iicInvName,
			 ii.cInvCode1 as iicInvCode1,
			 ii.cInvName1 as iicInvName1,
			 ii.cInvStd as iicInvStd,
			 ii.iInventoryUomId1 as iiiInventoryUomId1,
			 ui.cUomClassName as iicUomClassName,
			 ii.iEquipmentModelId as iiiEquipmentModelId,
			 t5.cEquipmentModelName as iicEquipmentModelName

from V_Sys_BarcodeDetail t1
LEFT JOIN bd_inventory i ON i.cinvcode = t1.Invcode
LEFT JOIN Bd_UomClass u ON i.iUomClassId = u.iautoid
LEFT JOIN Bd_InventoryClass t3 ON i.iInventoryClassId = t3.iautoid
LEFT JOIN Bd_EquipmentModel t4 ON i.iEquipmentModelId = t4.iautoid
LEFT JOIN Bd_InventoryChange ic on i.iAutoId = ic.iBeforeInventoryId
LEFT JOIN bd_inventory ii on ii.iAutoId = ic.iAfterInventoryId
LEFT JOIN Bd_UomClass ui ON ii.iUomClassId = ui.iautoid
LEFT JOIN Bd_EquipmentModel t5 ON ii.iEquipmentModelId = t5.iautoid
where 1=1
    #if(q)
		and (i.cinvcode like concat('%',#para(q),'%') or i.cinvcode1 like concat('%',#para(q),'%')
			or i.cinvname1 like concat('%',#para(q),'%') or i.cinvstd like concat('%',#para(q),'%')
			or u.cUomClassName like concat('%',#para(q),'%') or t1.Barcode like concat('%',#para(q),'%')
		)
	#end
	 AND t1.OrganizeCode = #(orgCode)
#end





#sql("getResource")
select t.* from
(

SELECT
	'#(combination)' AS combination,
	'#(assemtype)' AS assemtype,
	a.WhCode,
	wh.cWhName AS whname,
	a.PosCode,
	area.cAreaName AS posname,
	a.Barcode ,
	bd.InvCode AS cinvcode,
	bd.InvCode,
	b.cInvCode1,
	b.cInvName1,
	uom.cUomName ,
	a.Qty,
	change.iBeforeInventoryId,
    change.iAfterInventoryId,
    bd.SourceBillNo,
    ua.VenCode
FROM
	T_Sys_StockBarcodePosition a
	LEFT JOIN Bd_Warehouse wh ON a.WhCode = wh.cWhCode
	LEFT JOIN Bd_Warehouse_Area area ON area.cAreaCode = a.PosCode
	LEFT JOIN UFDATA_001_2023.dbo.V_sys_BarcodeDetail ua ON ua.Barcode = a.Barcode
	LEFT JOIN Bd_Customer mer ON ua.CusCode = mer.cCusCode
	LEFT JOIN bd_inventory b ON ua.InvCode = b.cInvCode
	LEFT JOIN Bd_Uom uom ON b.iInventoryUomId1 = uom.iAutoId
	LEFT JOIN Bd_InventoryChange change ON change.iBeforeInventoryId= b.iAutoId
	LEFT JOIN Bd_InventoryStockConfig config ON config.iInventoryId = b.iAutoId
	LEFT JOIN T_Sys_AssemBarcode sse ON sse.Barcode = a.Barcode   and sse.isDeleted ='0'
	LEFT JOIN V_Sys_BarcodeDetail bd on bd.Barcode = a.Barcode
where  sse.AutoID IS NULL
    and change.iAutoId is not null
  #if(keywords)
    and (b.cInvCode like concat('%', '#(keywords)', '%') or b.cInvName like concat('%', '#(keywords)', '%')
    or a.Barcode like concat('%', '#(keywords)', '%'))
  #end
  ) t

#end




#sql("getBarcode")
select t.* , t.cInvCode as invcode from
(
SELECT
	'#(combination)' AS combination,
	'#(assemtype)' AS assemtype,
	a.WhCode,
	wh.cWhName AS whname,
	a.PosCode,
	area.cAreaName AS posname,
	a.Barcode ,
	bd.InvCode AS cinvcode,
	bd.InvCode,
	b.cInvCode1,
	b.cInvName1,
	uom.cUomName ,
	a.Qty,
	change.iBeforeInventoryId,
    change.iAfterInventoryId,
    bd.SourceBillNo,
    ua.VenCode
FROM
	T_Sys_StockBarcodePosition a
	LEFT JOIN Bd_Warehouse wh ON a.WhCode = wh.cWhCode
	LEFT JOIN Bd_Warehouse_Area area ON area.cAreaCode = a.PosCode
	LEFT JOIN UFDATA_001_2023.dbo.V_sys_BarcodeDetail ua ON ua.Barcode = a.Barcode
	LEFT JOIN Bd_Customer mer ON ua.CusCode = mer.cCusCode
	LEFT JOIN bd_inventory b ON ua.InvCode = b.cInvCode
	LEFT JOIN Bd_Uom uom ON b.iInventoryUomId1 = uom.iAutoId
	LEFT JOIN Bd_InventoryChange change ON change.iBeforeInventoryId= b.iAutoId
	LEFT JOIN Bd_InventoryStockConfig config ON config.iInventoryId = b.iAutoId
	LEFT JOIN T_Sys_AssemBarcode sse ON sse.Barcode = a.Barcode   and sse.isDeleted ='0'
	LEFT JOIN V_Sys_BarcodeDetail bd on bd.Barcode = a.Barcode
where  sse.AutoID IS NULL
    and change.iAutoId is not null
    #if(barcode)
      and a.Barcode = #para(barcode)
    #end
  ) t

#end




#sql("getResourceBeifen")
select t.* from
(select '#(combination)'                              as combination,
    '#(assemtype)'                                 as assemtype,
    'PO'                                             as SourceBillType
     ,m.cOrderNo                                          as SourceBillNo
     , m.cOrderNo + '-' + cast(tc.iseq as NVARCHAR(10)) as SourceBillNoRow
     , m.iAutoId                                        as SourceBillID
     ,d.iAutoId                                           as SourceBillDid
     ,tc.iSeq                                             as RowNo
     , b.cInvCode
     , b.cInvName
     , b.cInvCode1
     , b.cInvName1
     , a.cCompleteBarcode                                       as barcode
     , CONVERT(VARCHAR(10), a.dPlanDate, 120)          as dPlanDate
     , b.cInvStd                                        as cinvstd
     , a.iQty                                           as qty
     , a.iQty                                           as quantity
     , v.cVenCode                                       as vencode
     , v.cVenName                                       as venname
     , uom.cUomCode
     , uom.cUomName
     ,change.iBeforeInventoryId
     ,change.iAfterInventoryId
from PS_PurchaseOrderDBatch a
         left join Bd_Inventory b on a.iinventoryId = b.iAutoId
         left join PS_PurchaseOrderD d on a.iPurchaseOrderDid = d.iAutoId
         left join PS_PurchaseOrderM m on m.iAutoId = d.iPurchaseOrderMid
         left join Bd_Vendor v on m.iVendorId = v.iAutoId
         left join PS_PurchaseOrderD_Qty tc
                   on tc.iPurchaseOrderDid = d.iAutoId
                       and tc.iAutoId = a.iPurchaseOrderdQtyId
         left join Bd_Uom uom on b.iPurchaseUomId = uom.iAutoId
         left join Bd_InventoryChange change on change.iBeforeInventoryId=b.iAutoId
where a.isEffective = '1' and change.iAutoId is not null
  and m.IsDeleted = '0'
###   and m.hideInvalid = '0'
  and d.isDeleted = '0'
  #if(keywords)
    and (b.cInvCode like concat('%', '#(keywords)', '%') or b.cInvName like concat('%', '#(keywords)', '%')
    or a.cCompleteBarcode like concat('%', '#(keywords)', '%'))
  #end
  #if(supplier)
    and (v.cVenCode like concat('%', '#(supplier)', '%') or v.cVenName like concat('%', '#(supplier)', '%'))
  #end
  #if(poNumber)
  and m.cOrderNo = '#(poNumber)'
  #end
  #if(orgCode)
  and b.cOrgCode = '#(orgCode)'
  #end
union all
select '#(combination)'                              as combination,
    '#(assemtype)'                                 as assemtype,
    'OM'                                             as SourceBillType
     ,m.cOrderNo                                          as SourceBillNo
     , m.cOrderNo + '-' + cast(tc.iseq as NVARCHAR(10)) as SourceBillNoRow
     , m.iAutoId                                        as SourceBillID
     ,d.iAutoId                                           as SourceBillDid
     ,tc.iSeq                                             as RowNo
     , b.cInvCode
     , b.cInvName
     , b.cInvCode1
     , b.cInvName1
     , a.cBarcode                                       as barcode
     , CONVERT(VARCHAR(10), a.dPlanDate, 120)          as dPlanDate
     , b.cInvStd                                        as cinvstd
     , a.iQty                                           as qty
     , a.iQty                                           as quantity
     , v.cVenCode                                       as vencode
     , v.cVenName                                       as venname
     , uom.cUomCode
     , uom.cUomName
     ,change.iBeforeInventoryId
     ,change.iAfterInventoryId
from PS_SubcontractOrderDBatch a
         left join Bd_Inventory b on a.iinventoryId = b.iAutoId
         left join PS_SubcontractOrderD d on a.iSubcontractOrderDid = d.iAutoId
         left join PS_SubcontractOrderM m on m.iAutoId = d.iSubcontractOrderMid
         left join Bd_Vendor v on m.iVendorId = v.iAutoId
         left join PS_SubcontractOrderD_Qty tc
                   on tc.iSubcontractOrderDid = d.iAutoId
                       and tc.iAutoId = a.iSubcontractOrderDid
         left join Bd_Uom uom on b.iPurchaseUomId = uom.iAutoId
         left join Bd_InventoryChange change on change.iBeforeInventoryId=b.iAutoId
where a.isEffective = '1' and change.iAutoId is not null
  and m.IsDeleted = '0'
###   and m.hideInvalid = '0'
  and d.isDeleted = '0'
  #if(keywords)
  and (b.cInvCode like concat('%', '#(keywords)', '%') or b.cInvName like concat('%', '#(keywords)', '%')
    or a.cBarcode like concat('%', '#(keywords)', '%'))
  #end
    #if(supplier)
    and (v.cVenCode like concat('%', '#(supplier)', '%') or v.cVenName like concat('%', '#(supplier)', '%'))
  #end
  #if(poNumber)
  and m.cOrderNo = '#(poNumber)'
  #end
  #if(orgCode)
  and b.cOrgCode = '#(orgCode)'
  #end
  ) t
  where 1=1
  #if(sourceBillType)
  and t.SourceBillType = '#(sourceBillType)'
  #end
  #if(detailHidden)
  and t.SourceBillDid not in (#(detailHidden))
  #end
  and not exists (select 1 from T_Sys_PUReceiveDetail detail where  detail.barcode = t.barcode)
  and not exists (select 1 from T_Sys_AssemDetail adetail where  adetail.barcode = t.barcode)
#end



#sql("findU8RdRecord01Id")
select r.* from UFDATA_001_2023.dbo.AssemVouch r where r.cAVCode = #para(cCode)
#end