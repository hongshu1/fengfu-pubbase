
#sql("recpor")
SELECT
    mp.AutoID,
    mp.BillNo,
    mp.SourceBillNo,
    mp.BillType,
    dm.cDepName,
    wsm.cWorkShiftName,
    md.dPlanDate,
    it.iAutoId AS IAUTOID,
    it.cInvCode,
    it.cInvCode1,
    it.cInvName1,
    uom.cUomName,
    md.iQty,
    mp.ccreatename,
    mp.dcreatetime,
    md.iAutoId AS mdID
FROM
    T_Sys_MaterialsPrepare mp
        LEFT JOIN Mo_MoDoc md ON md.iAutoId = mp.SourceBillID
        LEFT JOIN Bd_Inventory it ON it.iAutoId= md.iInventoryId
        LEFT JOIN Bd_Department dm ON dm.iAutoId= md.iDepartmentId
        LEFT JOIN Bd_WorkShiftM wsm ON wsm.iAutoId= md.iWorkShiftMid
        LEFT JOIN Bd_Uom uom ON uom.iAutoId= it.iManufactureUomId
WHERE 1=1
    #if(billno)
  AND BillNo = #para(billno)
  #end
  #if(sourcebillno)
  AND mp.SourceBillNo = #para(sourcebillno)
  #end
  #if(cinvcode)
  AND it.cInvCode = #para(cinvcode)
  #end
  #if(cinvcode1)
  AND it.cInvCode1 = #para(cinvcode1)
  #end
  #if(cinvname1)
  AND it.cInvName1 = #para(cinvname1)
  #end
  #if(startTime)
  AND md.dPlanDate >= #para(startTime)
  #end
  #if(endTime)
  AND md.dPlanDate <= #para(endTime)
  #end
  #if(dplandate)
  AND md.dPlanDate = #para(dplandate)
  #end
ORDER BY mp.dcreatetime DESC
    #end


    #sql("recpordetail")
SELECT mp.AutoID,
       mp.BillNo,
       mp.SourceBillID,
       mpd.InvCode,
       mpd.Qty,
       mpd.Num,
       it.cInvCode1,
       it.cInvName1,
       it.cInvStd,
       uom.cUomName,
       wh.cWhName,
       wp.cPositionName,
       sbp.Barcode
FROM T_Sys_MaterialsPrepareDetail mpd
         LEFT JOIN T_Sys_MaterialsPrepare mp ON mpd.MasID = mp.AutoID
         LEFT JOIN Bd_Inventory it ON mpd.InvCode = it.cInvCode
         LEFT JOIN Bd_Uom uom ON it.iInventoryUomId1 = uom.iAutoId
         LEFT JOIN T_Sys_StockBarcodePosition sbp ON mpd.Barcode = sbp.Barcode
         LEFT JOIN Bd_Warehouse wh ON sbp.WhCode = wh.cWhCode
         LEFT JOIN Bd_Warehouse_Position wp ON sbp.PosCode = wp.cPositionCode
WHERE 1 = 1
  AND mp.BillNo = '#(billno)'
   #end




    #sql("recpor1")
SELECT mp.AutoID,
       mp.BillNo,
       mp.SourceBillNo,
       dpm.cDepName,
       wsm.cWorkShiftName,
       md.dPlanDate,
       md.iQty,
       it.cInvCode,
       it.cInvCode1,
       it.cInvName1,
       uom.cUomName,
       wrm.cWorkCode
FROM T_Sys_MaterialsPrepare mp
         LEFT JOIN Mo_MoDoc md ON mp.SourceBillID = md.iAutoId
         LEFT JOIN Bd_Department dpm ON md.iDepartmentId = dpm.iAutoId
         LEFT JOIN Bd_WorkShiftM wsm ON md.iWorkShiftMid = wsm.iAutoId
         LEFT JOIN Bd_WorkRegionM wrm ON md.iWorkRegionMid = wrm.iAutoId
         LEFT JOIN Bd_Inventory it ON md.iInventoryId = it.iAutoId
         LEFT JOIN Bd_Uom uom ON it.iManufactureUomId = uom.iAutoId
WHERE 1 = 1
  #if(startTime)
  AND dPlanDate >= #para(startTime)
  #end
  #if(endTime)
  AND dPlanDate <= #para(endTime)
  #end
  #if(dplandate)
  AND dPlanDate = #para(dplandate)
  #end
  #if(cworkshiftcode)
  AND cWorkShiftCode = #para(cworkshiftcode)
  #end
  #if(cworkcode)
  AND cWorkCode = #para(cworkcode)
  #end
ORDER BY md.dPlanDate DESC
    #end


    #sql("barcode")
SELECT mp.AutoID,
       mp.OrganizeCode,
       mp.BillNo,
       mp.BillType,
       mp.BillDate,
       mp.AuditPerson,
       mp.AuditDate,
       mp.CreatePerson,
       mp.CreateDate,
       mp.ModifyPerson,
       mp.ModifyDate,
       mp.SourceBillNo,
       mp.SourceBillID,
       md.cMoDocNo,
       md.dPlanDate,
       md.iQty,
       md.iStatus,
       it.cInvCode,
       it.cInvCode1,
       it.cInvName1,
       it.cInvStd,
       dpm.cDepName,
       wsm.cWorkShiftName,
       wsm.cWorkShiftCode,
       uom.cUomName,
       wrm.cWorkName,
       wrm.cWorkCode,
       md.iQty * iri.iUsageUOM AS totalQty,
       emm.cEquipmentModelName
FROM T_Sys_MaterialsPrepare mp
         LEFT JOIN Mo_MoDoc md ON mp.SourceBillID = md.iAutoId
         LEFT JOIN Bd_Inventory it ON md.iInventoryId = it.iAutoId
         LEFT JOIN Bd_Department dpm ON md.iDepartmentId = dpm.iAutoId
         LEFT JOIN Bd_WorkShiftM wsm ON md.iWorkShiftMid = wsm.iAutoId
         LEFT JOIN Bd_Uom uom ON it.iManufactureUomId = uom.iAutoId
         LEFT JOIN Bd_WorkRegionM wrm ON md.iWorkRegionMid = wrm.iAutoId
         LEFT JOIN Bd_EquipmentModel emm ON it.iEquipmentModelId = emm.iAutoId
         LEFT JOIN Bd_InventoryRouting ir ON md.iInventoryRouting = ir.iAutoId
         LEFT JOIN Bd_InventoryRoutingConfig irc ON ir.iAutoId = irc.iInventoryRoutingId
         LEFT JOIN Bd_InventoryRoutingInvc iri ON irc.iAutoId = iri.iInventoryRoutingConfigId
WHERE 1 = 1
  AND BillNo = '#(billno)' #if(cmodocno)
  AND cMoDocNo = #para(cmodocno)
  #end
  #if(cinvcode)
  AND InvCode = #para(cinvcode)
  #end
  #if(cinvcode1)
  AND cInvCode1 = #para(cinvcode1)
  #end
  #if(cinvname1)
  AND cInvName1 = #para(cinvname1)
  #end
  #if(startTime)
  AND dPlanDate >= #para(startTime)
  #end
  #if(endTime)
  AND dPlanDate <= #para(endTime)
  #end
  #if(dplandate)
  AND dPlanDate = #para(dplandate)
  #end
  #if(cworkshiftcode)
  AND cWorkShiftCode = #para(cworkshiftcode)
  #end
  #if(cworkcode)
  AND cWorkCode = #para(cworkcode)
  #end
ORDER BY mp.CreateDate DESC
    #end


    #sql("Auto")
SELECT
    md.iAutoId,
    md.dPlanDate,
    md.cMoDocNo,
    dm.cDepName,
    wrm.cWorkName,
    wsm.cWorkShiftName,
    it.iAutoId AS AUTOID,
    it.cInvCode,
    it.cInvCode1,
    it.cInvName1,
    uom.cUomName,
    md.iQty
FROM
    Mo_MoDoc md
        LEFT JOIN Bd_Inventory it ON it.iAutoId= md.iInventoryId
        LEFT JOIN Bd_Department dm ON dm.iAutoId= md.iDepartmentId
        LEFT JOIN Bd_WorkRegionM wrm ON wrm.iAutoId= md.iWorkRegionMid
        LEFT JOIN Bd_WorkShiftM wsm ON wsm.iAutoId= md.iWorkShiftMid
        LEFT JOIN Bd_Uom uom ON uom.iAutoId= it.iManufactureUomId
WHERE
        1 = 1
  AND md.iStatus = 2
    #if(dplandate)
  AND dPlanDate = #para(dplandate)
  #end
  #if(startTime)
  AND dPlanDate >= #para(startTime)
  #end
  #if(endTime)
  AND dPlanDate <= #para(endTime)
  #end
ORDER BY md.dPlanDate DESC
    #end


    #sql("getMaterialsOutLines")
SELECT mp.AutoID,
       mp.OrganizeCode,
       mp.BillNo,
       mp.BillType,
       mp.BillDate,
       mp.AuditPerson,
       mp.AuditDate,
       mp.CreatePerson,
       mp.CreateDate,
       mp.ModifyPerson,
       mp.ModifyDate,
       mp.SourceBillNo,
       mp.SourceBillID,
       mpd.InvCode,
       mpd.Barcode,
       mpd.Qty,
       md.cMoDocNo,
       md.dPlanDate,
       md.iQty,
       md.iStatus,
       it.cInvCode1,
       it.cInvName1,
       it.cInvStd,
       dpm.cDepName,
       wsm.cWorkShiftName,
       uom.cUomName,
       wrm.cWorkName,
       emm.cEquipmentModelName
FROM T_Sys_MaterialsPrepare mp
         LEFT JOIN T_Sys_MaterialsPrepareDetail mpd ON mpd.MasID = mp.AutoID
         LEFT JOIN Mo_MoDoc md ON md.iAutoId = mp.SourceBillID
         LEFT JOIN Bd_Inventory it ON it.iAutoId = md.iInventoryId
         LEFT JOIN Bd_Department dpm ON dpm.iAutoId = md.iDepartmentId
         LEFT JOIN Bd_WorkShiftM wsm ON wsm.iAutoId = md.iWorkShiftMid
         LEFT JOIN Bd_Uom uom ON uom.iAutoId = it.iManufactureUomId
         LEFT JOIN Bd_WorkRegionM wrm ON wrm.iAutoId = md.iWorkRegionMid
         LEFT JOIN Bd_EquipmentModel emm ON emm.iAutoId = it.iEquipmentModelId
WHERE 1 = 1
  AND md.iStatus = 2
  AND md.cMoDocNo = '#(cmodocno)' #end


#sql("getManualdatas")
SELECT
    it.cInvCode,
    it.cInvCode1,
    it.cInvName1,
    it.cInvStd,
    uom.cUomName
FROM
    Mo_MoRoutingInvc mri
        LEFT JOIN Bd_Inventory it ON mri.iInventoryId= it.iAutoId
        LEFT JOIN Mo_MoRoutingConfig mrc ON mri.iMoRoutingConfigId= mrc.iAutoId
        LEFT JOIN Mo_MoRouting mr ON mrc.iMoRoutingId= mr.iAutoId
        LEFT JOIN Mo_MoDoc md ON mr.iMoDocId= md.iAutoId
        LEFT JOIN Bd_Uom uom ON uom.iAutoId= it.iInventoryUomId1
WHERE 1 = 1
  AND  md.cMoDocNo='#(cmodocno)' #end



#sql("getDetaildatas")
SELECT it.cInvCode,
       it.cInvCode1,
       it.cInvName1,
       it.cInvStd,
       uom.cUomName,
       sbp.Batch,
       sbp.Qty,
       mp.Billno,
       mp.SourceBillID
FROM
    Mo_MoRoutingInvc mri
        LEFT JOIN Bd_Inventory it ON mri.iInventoryId= it.iAutoId
        LEFT JOIN Mo_MoRoutingConfig mrc ON mri.iMoRoutingConfigId= mrc.iAutoId
        LEFT JOIN Mo_MoRouting mr ON mrc.iMoRoutingId= mr.iAutoId
        LEFT JOIN Mo_MoDoc md ON mr.iMoDocId= md.iAutoId
        LEFT JOIN T_Sys_MaterialsPrepare mp ON mp.SourceBillID= md.iAutoId
        LEFT JOIN Bd_Uom uom ON uom.iAutoId= it.iInventoryUomId1
        LEFT JOIN T_Sys_StockBarcodePosition sbp ON sbp.InvCode= it.cInvCode
WHERE 1 = 1
  AND  mp.BillNo='#(billno)'
  AND  sbp.State='1'
  AND  it.cInvCode is not null
GROUP BY
    it.cInvCode,
    it.cInvCode1,
    it.cInvName1,
    it.cInvStd,
    uom.cUomName,
    sbp.Batch,
    sbp.Qty,
    mp.Billno,
    mp.SourceBillID
ORDER BY sbp.Batch ASC #end



  #sql("getBarcodedatas")
SELECT
    sbp.AutoID,
    sbp.Barcode,
    sbp.WhCode,
    sbp.Qty,
    sbp.Batch,
    uom.cUomName
FROM
    T_Sys_StockBarcodePosition sbp
        LEFT JOIN Bd_Inventory it ON sbp.InvCode= it.cInvCode
        LEFT JOIN Bd_Uom uom ON it.iInventoryUomId1=uom.iAutoId
WHERE
        sbp.InvCode is not null
    AND sbp.InvCode= '#(invcode)'
    AND sbp.State= '1'
    AND sbp.Batch= '#(batch)' #end


#sql("getBarcodedatas1")
SELECT
    mps.iWarehouseAreaId,
    mps.cBarcode,
    mps.iQty,
    uom.cUomName,
    mps.cBatch,
    mps.dProdDate
FROM
    T_Sys_MaterialsPrepareScan mps
        LEFT JOIN Bd_Inventory it ON it.iAutoId= mps.iInventoryId
        LEFT JOIN Bd_Uom uom ON it.iInventoryUomId1=uom.iAutoId
WHERE
    mps.iAutoId is not null
  AND it.cInvCode= '#(invcode)'
  AND mps.cBatch= '#(batch)' #end


  #sql("getMaterialsOutLines3")
SELECT *
FROM T_Sys_MaterialsPrepareDetail mpd
WHERE 1 = 1
  AND mpd.MasID = '#(autoid)' #end



#sql("cworkname")
SELECT *
FROM Bd_InventoryClass ic
WHERE ic.isDeleted = '0'
#end



#sql("cworkshiftcode")
SELECT *
FROM Bd_WorkShiftM wsm
WHERE wsm.isDeleted = '0' #if(isenabled)
  AND wsm.isenabled = #para(isenabled)
  #end
#end


#sql("barcodeDatas")
SELECT
    sbp.AutoID,
    sbp.InvCode,
    sbp.Barcode,
    sbp.Batch,
    sbp.Qty,
    it.iAutoId,
    it.cInvCode,
    it.cInvCode1,
    it.cInvName1,
    it.cInvStd,
    uom.cUomName
FROM
    T_Sys_StockBarcodePosition sbp
        LEFT JOIN Bd_Inventory it ON sbp.InvCode=it.cInvCode
        LEFT JOIN Bd_Uom uom ON it.iInventoryUomId1=uom.iAutoId
WHERE
        1 = 1
  AND sbp.Barcode='#(barcode)'
  AND sbp.InvCode IS NOT NULL
#end



#sql("getManualAdddatas")
SELECT
    mri.iInventoryId,
    it.cInvCode,
    it.cInvCode1,
    it.cInvName1,
    it.cInvStd,
    uom.cUomName,
    em.cEquipmentModelName,
    ic.cInvCName,
    mri.iUsageUOM * md.iQty AS planIqty
FROM
    Mo_MoRoutingInvc mri
        LEFT JOIN Bd_Inventory it ON mri.iInventoryId= it.iAutoId
        LEFT JOIN Mo_MoRoutingConfig mrc ON mri.iMoRoutingConfigId= mrc.iAutoId
        LEFT JOIN Mo_MoRouting mr ON mrc.iMoRoutingId= mr.iAutoId
        LEFT JOIN Mo_MoDoc md ON mr.iMoDocId= md.iAutoId
        LEFT JOIN Bd_Uom uom ON uom.iAutoId= it.iInventoryUomId1
        LEFT JOIN Bd_EquipmentModel em ON em.iAutoId= it.iEquipmentModelId
        LEFT JOIN Bd_InventoryClass ic ON ic.iAutoId= it.iInventoryClassId
WHERE 1 = 1
    #if(itID)
  AND mri.iInventoryId=#para(itID)
    #end
  #if(cinvcode)
  AND it.cInvCode=#para(cinvcode)
    #end
    #if(cinvcode1)
  AND it.cInvCode1=#para(cinvcode1)
    #end
    #if(cinvname1)
  AND it.cInvName1=#para(cinvname1)
    #end
    #if(cworkcode)
  AND ic.iAutoId=#para(cworkcode)
    #end
  AND  md.cMoDocNo='#(cmodocno)' #end


#sql("getChooseM")
SELECT
    it.iAutoId,
    it.cInvCode,
    it.cInvCode1,
    it.cInvName1,
    it.cInvStd,
    uom.cUomName,
    wh.cWhName,
    wp.cPositionName,
    sbp.Barcode
FROM
    Mo_MoRoutingInvc mri
        LEFT JOIN Bd_Inventory it ON mri.iInventoryId= it.iAutoId
        LEFT JOIN Mo_MoRoutingConfig mrc ON mri.iMoRoutingConfigId= mrc.iAutoId
        LEFT JOIN Mo_MoRouting mr ON mrc.iMoRoutingId= mr.iAutoId
        LEFT JOIN Mo_MoDoc md ON mr.iMoDocId= md.iAutoId
        LEFT JOIN Bd_Uom uom ON uom.iAutoId= it.iInventoryUomId1
        LEFT JOIN T_Sys_StockBarcodePosition sbp ON sbp.InvCode= it.cInvCode
        LEFT JOIN Bd_Warehouse wh ON wh.cWhCode= sbp.WhCode
        LEFT JOIN Bd_Warehouse_Position wp ON wp.cPositionCode= sbp.PosCode
WHERE 1 = 1
  AND  sbp.Barcode='#(barcodes)'
  AND  md.cMoDocNo='#(cmodocno)'#end





#sql("test")
SELECT
    iri.iInventoryId,
    it.cInvCode,
    iri.iUsageUOM,
    iri.iUsageUOM * md.iQty AS planIqty,
    sbp.Barcode,
    sbp.Qty,
    sbp.PosCode,
    sbp.PackRate
FROM
    Mo_MoRoutingInvc iri
        LEFT JOIN Bd_Inventory it ON iri.iInventoryId= it.iAutoId
        LEFT JOIN Mo_MoRoutingConfig irc ON iri.iMoRoutingConfigId= irc.iAutoId
        LEFT JOIN Mo_MoRouting ir ON irc.iMoRoutingId= ir.iAutoId
        LEFT JOIN Mo_MoDoc md ON ir.iMoDocId= md.iAutoId
        LEFT JOIN T_Sys_StockBarcodePosition sbp ON sbp.InvCode= it.cInvCode
WHERE 1=1
  AND  md.iAutoId='#(imodocid)'
  AND sbp.AutoID is not null
#end


#sql("test1")
SELECT
    iri.iInventoryId,
    it.cInvCode,
    iri.iUsageUOM,
    iri.iUsageUOM * md.iQty AS planIqty,
    sbp.Barcode,
    sbp.Qty,
    sbp.PosCode,
    sbp.PackRate
FROM
    Mo_MoRoutingInvc iri
        LEFT JOIN Bd_Inventory it ON iri.iInventoryId= it.iAutoId
        LEFT JOIN Mo_MoRoutingConfig irc ON iri.iMoRoutingConfigId= irc.iAutoId
        LEFT JOIN Mo_MoRouting ir ON irc.iMoRoutingId= ir.iAutoId
        LEFT JOIN Mo_MoDoc md ON ir.iMoDocId= md.iAutoId
        LEFT JOIN T_Sys_StockBarcodePosition sbp ON sbp.InvCode= it.cInvCode
WHERE 1=1
  AND  md.cMoDocNo='#(cmodocno)'
  AND  sbp.Barcode='#(barcode)'
    #end



#sql("iWorkRegionMid")
SELECT *
FROM Bd_WorkRegionM ic
WHERE ic.isDeleted = '0'
    #end







#sql("xianjinxianchu")
SELECT it.cInvCode,
       it.cInvCode1,
       it.cInvName1,
       mri.iUsageUOM * md.iQty AS planIqty,
       it.cInvStd,
       uom.cUomName,
       sbp.Batch,
       sbp.Qty,
       sbp.Barcode,
       sbp.WhCode,
       sbp.PosCode,
       mp.AutoID,
       mp.Billno,
       mp.SourceBillID,
       md.cMoDocNo
FROM
    Mo_MoRoutingInvc mri
        LEFT JOIN Bd_Inventory it ON mri.iInventoryId= it.iAutoId
        LEFT JOIN Mo_MoRoutingConfig mrc ON mri.iMoRoutingConfigId= mrc.iAutoId
        LEFT JOIN Mo_MoRouting mr ON mrc.iMoRoutingId= mr.iAutoId
        LEFT JOIN Mo_MoDoc md ON mr.iMoDocId= md.iAutoId
        LEFT JOIN T_Sys_MaterialsPrepare mp ON mp.SourceBillID= md.iAutoId
        LEFT JOIN Bd_Uom uom ON uom.iAutoId= it.iInventoryUomId1
        LEFT JOIN T_Sys_StockBarcodePosition sbp ON sbp.InvCode= it.cInvCode
WHERE 1 = 1
  AND  mp.BillNo='#(billno)'
  AND  sbp.State='1'
  AND  it.cInvCode is not null
ORDER BY sbp.Batch ASC #end




#sql("setqty")
SELECT
    *
FROM
    T_Sys_StockBarcodePosition
WHERE
     Batch IS NOT NULL
  AND InvCode IS NOT NULL
  AND Barcode = '#(barcode)'
#end





#sql("HasBeenPrepared")
SELECT
   *
FROM
    T_Sys_MaterialsPrepareDetail
        where 1=1
        and MasID='#(iAutoId)'
        and InvCode='#(InvCode)'
#end



#sql("HasBeenPrepared1")
SELECT
    a.iInventoryId,
    f.cInvCode,
    a.iUsageUOM,
    a.iUsageUOM*d.iQty AS planIqty,
    sbp.Batch,
    sbp.Barcode,
    mpd.Qty,
    mpd.AutoID
FROM
    Mo_MoRoutingInvc a
        LEFT JOIN Bd_Inventory f ON a.iInventoryId= f.iAutoId
        LEFT JOIN Mo_MoRoutingConfig b ON a.iMoRoutingConfigId= b.iAutoId
        LEFT JOIN Mo_MoRouting c ON b.iMoRoutingId= c.iAutoId
        LEFT JOIN Mo_MoDoc d ON c.iMoDocId= d.iAutoId
        LEFT JOIN T_Sys_StockBarcodePosition sbp ON sbp.InvCode= f.cInvCode
        LEFT JOIN T_Sys_MaterialsPrepareDetail mpd ON mpd.Barcode= sbp.Barcode
WHERE 1=1
  AND  d.iAutoId='#(iAutoId)'
  AND  sbp.InvCode='#(InvCode)'
  AND  sbp.Batch='#(Batch)'
  AND  mpd.AutoID IS NOT NULL
    #end



#sql("xianjinxianchuALL")
SELECT it.cInvCode,
       it.cInvCode1,
       it.cInvName1,
       mri.iUsageUOM * md.iQty AS planIqty,
       it.cInvStd,
       uom.cUomName,
       sbp.Batch,
       sbp.Qty,
       sbp.Barcode,
       sbp.WhCode,
       sbp.PosCode,
       md.cMoDocNo
FROM
    Mo_MoRoutingInvc mri
        LEFT JOIN Bd_Inventory it ON mri.iInventoryId= it.iAutoId
        LEFT JOIN Mo_MoRoutingConfig mrc ON mri.iMoRoutingConfigId= mrc.iAutoId
        LEFT JOIN Mo_MoRouting mr ON mrc.iMoRoutingId= mr.iAutoId
        LEFT JOIN Mo_MoDoc md ON mr.iMoDocId= md.iAutoId
        LEFT JOIN T_Sys_MaterialsPrepare mp ON mp.SourceBillID= md.iAutoId
        LEFT JOIN Bd_Uom uom ON uom.iAutoId= it.iInventoryUomId1
        LEFT JOIN T_Sys_StockBarcodePosition sbp ON sbp.InvCode= it.cInvCode
WHERE 1 = 1
  AND  mp.BillNo='#(billno)'
  AND  sbp.Barcode IS  NULL
ORDER BY sbp.Batch ASC #end




#sql("checkQty")
SELECT
    Qty
FROM T_Sys_MaterialsPrepareDetail mpd
WHERE 1 = 1
AND mpd.MasID='#(autoID)'
   #end




#sql("zijianwuliaoji")
SELECT
    a.iInventoryId,
    f.cInvCode,
    a.iUsageUOM,
    a.iUsageUOM*d.iQty AS planIqty
FROM
    Mo_MoRoutingInvc a
    LEFT JOIN Bd_Inventory f ON a.iInventoryId= f.iAutoId
    LEFT JOIN Mo_MoRoutingConfig b ON a.iMoRoutingConfigId= b.iAutoId
    LEFT JOIN Mo_MoRouting c ON b.iMoRoutingId= c.iAutoId
    LEFT JOIN Mo_MoDoc d ON c.iMoDocId= d.iAutoId
WHERE 1=1
  AND  d.iAutoId='#(imodocid)'
    #end