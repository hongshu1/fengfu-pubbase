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
SELECT t1.*,
       t2.*,
       v.cVenName as venname,
       b.cInvCode1,
       b.cInvCode as cinvcode,
       b.cInvName as cinvname,
       b.cInvName1,
       b.cInvStd  as cinvstd,
       uom.cUomName
FROM T_Sys_PUReceiveDetail t1
         LEFT JOIN (select a.cBarcode         as oldcbarcode,
                           a.cCompleteBarcode as cbarcode,
                           a.dPlanDate,

                           a.iQty             as quantity

                    from PS_PurchaseOrderDBatch a
                    where 1 = 1
                    union all
                    select a.cBarcode         as oldcbarcode,
                           a.cCompleteBarcode as cbarcode,
                           a.dPlanDate,
                           a.iQty             as quantity
                    from PS_SubcontractOrderDBatch a
                    where 1 = 1) t2 on t1.Barcode = t2.cbarcode
         left join Bd_Vendor v on t1.VenCode = v.cVenCode
         left join Bd_Inventory b on t1.invcode = b.cInvCode
         left join Bd_Uom uom on b.iPurchaseUomId = uom.iAutoId
where 1 = 1
  and t1.MasID = '#(id)'
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
       t4.cEquipmentModelName
from
    V_Sys_BarcodeDetail t1
         LEFT JOIN bd_inventory i ON i.cinvcode = t1.Invcode
         LEFT JOIN Bd_UomClass u ON i.iUomClassId = u.iautoid
         LEFT JOIN Bd_InventoryClass t3 ON i.iInventoryClassId = t3.iautoid
         LEFT JOIN Bd_EquipmentModel t4 ON i.iEquipmentModelId = t4.iautoid
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
select t.*,
    s.cRdName as scrdname,
    s.cRdCode as scrdcode,
    dep.cdepcode,dep.cdepname,
 'false' as isEdit from
(select '#(combination)'                              as combination,
    '#(barcodetype)'                                 as barcodetype,
    'PO'                                             as SourceBillType
     ,m.cOrderNo                                          as SourceBillNo
     , m.cOrderNo + '-' + cast(tc.iseq as NVARCHAR(10)) as SourceBillNoRow
     , m.iAutoId                                        as SourceBillID
     , m.idepartmentid
     , m.iPurchaseTypeId
     , m.iBusType as orderibustype
     ,d.iAutoId                                           as SourceBillDid
     ,tc.iSeq                                             as RowNo
     , b.cInvCode as invcode
     , b.cInvName
     , b.cInvCode1
     , b.cInvName1
     , a.cBarcode                                       as oldBarcode
     , a.cCompleteBarcode                               as barcode
     , CONVERT(VARCHAR(10), a.dPlanDate, 120)          as dPlanDate
     , b.cInvStd                                        as cinvstd
     , a.iQty                                           as qty
     , a.iQty                                           as quantity
     , v.cVenCode                                       as vencode
     , v.cVenName                                       as venname
     , uom.cUomCode
     , uom.cUomName
     , area.cAreaCode                                   as poscode
from PS_PurchaseOrderDBatch a
         left join Bd_Inventory b on a.iinventoryId = b.iAutoId
         LEFT JOIN Bd_InventoryStockConfig config ON config.iInventoryId = b.iAutoId
         LEFT JOIN Bd_Warehouse_Area area ON area.iAutoId = config.iWarehouseAreaId
         left join PS_PurchaseOrderD d on a.iPurchaseOrderDid = d.iAutoId
         left join PS_PurchaseOrderM m on m.iAutoId = d.iPurchaseOrderMid
         left join Bd_Vendor v on m.iVendorId = v.iAutoId
         left join PS_PurchaseOrderD_Qty tc
                   on tc.iPurchaseOrderDid = d.iAutoId
                       and tc.iAutoId = a.iPurchaseOrderdQtyId
         left join Bd_Uom uom on b.iPurchaseUomId = uom.iAutoId
where a.isEffective = '1'
  and m.IsDeleted = '0'
###   and m.hideInvalid = '0'
  and d.isDeleted = '0'
  #if(keywords)
    and (b.cInvCode like concat('%', '#(keywords)', '%') or b.cInvName like concat('%', '#(keywords)', '%')
    or a.cCompleteBarcode like concat('%', '#(keywords)', '%'))
  #end
  #if(barcode)
  and a.cCompleteBarcode = '#(barcode)'
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
    '#(barcodetype)'                                 as barcodetype,
    'OM'                                             as SourceBillType
     ,m.cOrderNo                                          as SourceBillNo
     , m.cOrderNo + '-' + cast(tc.iseq as NVARCHAR(10)) as SourceBillNoRow
     , m.iAutoId                                        as SourceBillID
     , m.idepartmentid
     , m.iPurchaseTypeId
     , m.iBusType as orderibustype
     ,d.iAutoId                                           as SourceBillDid
     ,tc.iSeq                                             as RowNo
     , b.cInvCode as invcode
     , b.cInvName
     , b.cInvCode1
     , b.cInvName1
     , a.cBarcode                                       as oldBarcode
     , a.cCompleteBarcode                               as barcode
     , CONVERT(VARCHAR(10), a.dPlanDate, 120)          as dPlanDate
     , b.cInvStd                                        as cinvstd
     , a.iQty                                           as qty
     , a.iQty                                           as quantity
     , v.cVenCode                                       as vencode
     , v.cVenName                                       as venname
     , uom.cUomCode
     , uom.cUomName
     , area.cAreaCode                                   as poscode
from PS_SubcontractOrderDBatch a
         left join Bd_Inventory b on a.iinventoryId = b.iAutoId
         LEFT JOIN Bd_InventoryStockConfig config ON config.iInventoryId = b.iAutoId
         LEFT JOIN Bd_Warehouse_Area area ON area.iAutoId = config.iWarehouseAreaId
         left join PS_SubcontractOrderD d on a.iSubcontractOrderDid = d.iAutoId
         left join PS_SubcontractOrderM m on m.iAutoId = d.iSubcontractOrderMid
         left join Bd_Vendor v on m.iVendorId = v.iAutoId
         left join PS_SubcontractOrderD_Qty tc
                   on tc.iSubcontractOrderDid = d.iAutoId
                       and tc.iAutoId = a.iSubcontractOrderDid
         left join Bd_Uom uom on b.iPurchaseUomId = uom.iAutoId
where a.isEffective = '1'
  and m.IsDeleted = '0'
###   and m.hideInvalid = '0'
  and d.isDeleted = '0'
  #if(keywords)
  and (b.cInvCode like concat('%', '#(keywords)', '%') or b.cInvName like concat('%', '#(keywords)', '%')
    or a.cCompleteBarcode like concat('%', '#(keywords)', '%'))
  #end
  #if(barcode)
  and a.cCompleteBarcode = '#(barcode)'
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
  LEFT JOIN Bd_PurchaseType p on p.iAutoId = t.iPurchaseTypeId
LEFT JOIN Bd_Rd_Style s ON s.cRdCode = p.cRdCode
left join Bd_Department dep on t.idepartmentid = dep.iautoid
  where 1=1
  #if(sourceBillType)
  and t.SourceBillType = '#(sourceBillType)'
  #end
  #if(detailHidden)
  and t.barcode not in (#(detailHidden))
  #end
  and not exists (select 1 from T_Sys_PUReceiveDetail detail where detail.Barcode = t.barcode and detail.isDeleted = '0')
#end

#sql("findWhArea")
SELECT  a.*
FROM Bd_Warehouse_Area a
         LEFT JOIN Bd_Warehouse wh ON a.iWarehouseId = wh.iAutoId
where a.isDeleted = '0'
  and wh.isDeleted = '0'
    #if(keywords)
    and (a.cAreaCode like concat('%','#(keywords)','%') OR a.cAreaName like concat('%','#(keywords)','%'))
    #end
    #if(whCode)
    and wh.cWhCode = '#(whCode)'
    #end
    #if(orgCode)
    and wh.cOrgCode = '#(orgCode)'
    #end
#end

#sql("findListByMasId")
SELECT  t1.*, t2.*, v.cVenName, v.iAutoId as veniAutoId,
       b.cInvCode1,
       b.cInvCode as cinvcode,
       b.cInvName as cinvname,
       b.cInvName1,
       b.cInvStd  as cinvstd,
       uom.cUomName,
       uom.cUomCode,
###info.isIQC1,
qc.iAutoId as qcIAutoId, qc.cDcCode
FROM T_Sys_PUReceiveDetail t1
         LEFT JOIN (select a.cBarcode         as oldcbarcode,
                           a.cCompleteBarcode as cbarcode,
                           a.dPlanDate,

                           a.iQty             as quantity

                    from PS_PurchaseOrderDBatch a
                    where 1 = 1
                    union all
                    select a.cBarcode         as oldcbarcode,
                           a.cCompleteBarcode as cbarcode,
                           a.dPlanDate,
                           a.iQty             as quantity
                    from PS_SubcontractOrderDBatch a
                    where 1 = 1) t2 on t1.Barcode = t2.cbarcode
         left join Bd_Vendor v on t1.VenCode = v.cVenCode
         left join Bd_Inventory b on t1.invcode = b.cInvCode
         left join Bd_Uom uom on b.iPurchaseUomId = uom.iAutoId
        ###left join Bd_InventoryMfgInfo info on t2.iInventoryId = info.iInventoryId
        left join Bd_InventoryQcForm qc on qc.iInventoryId = b.iAutoId
where t1.MasID = '#(masId)' and t1.Barcode is not null
#end


#sql("findAfterInvData")
select t2.cInvCode                          as beforeCode,
       t3.cInvCode                          as afterCode,
       t3.cInvCode                          as invcode,
       '#(sourceBillType)'                  as SourceBillType,
       'false'                              as isEdit,
       'true'                               as isEditQty,
       'false'                              as isEditWeight,
       t3.cInvCode1,
       t3.cInvName1,
       t3.cInvStd,
       CONVERT(VARCHAR(10), GETDATE(), 120) as dPlanDate,
       uom.cUomCode,
       uom.cUomName,
       '#(supplier)'                        as vencode,
       area.cAreaCode                       as poscode
from Bd_InventoryChange t1
         left join Bd_Inventory t2 on t1.iBeforeInventoryId = t2.iAutoId
         left join Bd_Inventory t3 on t1.iAfterInventoryId = t3.iAutoId
         left join Bd_Uom uom on t3.iPurchaseUomId = uom.iAutoId
         LEFT JOIN Bd_InventoryStockConfig config ON config.iInventoryId = t3.iAutoId
         LEFT JOIN Bd_Warehouse_Area area ON area.iAutoId = config.iWarehouseAreaId
where t2.cInvCode = '#(itemCode)'
#end

#sql("getOrder")
select t.*, s.cRdName as scrdname, s.cRdCode as scrdcode, dep.cdepcode, dep.cdepname
from (select 'PO'                                   as SourceBillType,
             m.cOrderNo                             as SourceBillNo,
             m.iAutoId                              as SourceBillID,
             m.idepartmentid,
             m.iPurchaseTypeId,
             m.iBusType                             as orderibustype,
             d.iAutoId                              as SourceBillDid,
             a.cBarcode                             as oldBarcode,
             a.cCompleteBarcode                     as barcode,
             CONVERT(VARCHAR(10), a.dPlanDate, 120) as dPlanDate
      from PS_PurchaseOrderDBatch a
               left join PS_PurchaseOrderD d on a.iPurchaseOrderDid = d.iAutoId
               left join PS_PurchaseOrderM m on m.iAutoId = d.iPurchaseOrderMid
      where a.isEffective = '1'
        and m.IsDeleted = '0'
        and d.isDeleted = '0'
        and a.cCompleteBarcode = '#(barcode)'
      union all
      select 'OM'                                   as SourceBillType,
             m.cOrderNo                             as SourceBillNo,
             m.iAutoId                              as SourceBillID,
             m.idepartmentid,
             m.iPurchaseTypeId,
             m.iBusType                             as orderibustype,
             d.iAutoId                              as SourceBillDid,
             a.cBarcode                             as oldBarcode,
             a.cCompleteBarcode                     as barcode,
             CONVERT(VARCHAR(10), a.dPlanDate, 120) as dPlanDate
      from PS_SubcontractOrderDBatch a
               left join PS_SubcontractOrderD d on a.iSubcontractOrderDid = d.iAutoId
               left join PS_SubcontractOrderM m on m.iAutoId = d.iSubcontractOrderMid
      where a.isEffective = '1'
        and m.IsDeleted = '0'
        and d.isDeleted = '0'
        and a.cCompleteBarcode = '#(barcode)'
     ) t
         LEFT JOIN Bd_PurchaseType p on p.iAutoId = t.iPurchaseTypeId
         LEFT JOIN Bd_Rd_Style s ON s.cRdCode = p.cRdCode
         left join Bd_Department dep on t.idepartmentid = dep.iautoid
where 1 = 1
#end
