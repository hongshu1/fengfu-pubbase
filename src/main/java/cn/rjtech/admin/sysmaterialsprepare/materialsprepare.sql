
#sql("recpor")
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
       uom.cUomName
FROM T_Sys_MaterialsPrepare mp
         LEFT JOIN Mo_MoDoc md ON mp.SourceBillID = md.iAutoId
         LEFT JOIN Bd_Department dpm ON md.iDepartmentId = dpm.iAutoId
         LEFT JOIN Bd_WorkShiftM wsm ON md.iWorkShiftMid = wsm.iAutoId
         LEFT JOIN Bd_Inventory it ON md.iInventoryId = it.iAutoId
         LEFT JOIN Bd_Uom uom ON it.iManufactureUomId = uom.iAutoId
WHERE 1 = 1 #if(billno)
  AND BillNo = #para(billno)
  #end
  #if(cmodocno)
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


    #sql("recpordetail")
SELECT mp.BillNo,
       mp.SourceBillID,
       mpd.InvCode,
       mpd.Qty,
       it.cInvCode1,
       it.cInvName1,
       it.cInvStd,
       uom.cUomName
FROM T_Sys_MaterialsPrepareDetail mpd
         LEFT JOIN T_Sys_MaterialsPrepare mp ON mpd.MasID = mp.AutoID
         LEFT JOIN Bd_Inventory it ON mpd.InvCode = it.cInvCode
         LEFT JOIN Bd_Uom uom ON it.iInventoryUomId1 = uom.iAutoId
WHERE 1 = 1
  AND mp.BillNo = '#(billno)' #end




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
       uom.cUomName
FROM T_Sys_MaterialsPrepare mp
         LEFT JOIN Mo_MoDoc md ON mp.SourceBillID = md.iAutoId
         LEFT JOIN Bd_Department dpm ON md.iDepartmentId = dpm.iAutoId
         LEFT JOIN Bd_WorkShiftM wsm ON md.iWorkShiftMid = wsm.iAutoId
         LEFT JOIN Bd_WorkRegionM wrm ON md.iWorkRegionMid = wrm.iAutoId
         LEFT JOIN Bd_Inventory it ON md.iInventoryId = it.iAutoId
         LEFT JOIN Bd_Uom uom ON it.iManufactureUomId = uom.iAutoId
         LEFT JOIN Bd_InventoryRouting ir ON md.iInventoryRouting = ir.iAutoId
         LEFT JOIN Bd_InventoryRoutingConfig irc ON ir.iAutoId = irc.iInventoryRoutingId
         LEFT JOIN Bd_InventoryRoutingInvc iri ON irc.iAutoId = iri.iInventoryRoutingConfigId
WHERE 1 = 1
  AND mp.BillNo is not null #if(startTime)
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
GROUP BY mp.AutoID,
    mp.BillNo,
    mp.SourceBillNo,
    dpm.cDepName,
    wsm.cWorkShiftName,
    md.dPlanDate,
    md.iQty,
    it.cInvCode,
    it.cInvCode1,
    it.cInvName1,
    uom.cUomName
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
SELECT md.iAutoId,
       md.dPlanDate,
       md.cMoDocNo,
       md.iQty,
       dpm.cDepName,
       wsm.cWorkShiftName,
       wrm.cWorkName,
       it.cInvCode,
       it.cInvCode1,
       it.cInvName1,
       uom.cUomName
FROM Mo_MoDoc md
         LEFT JOIN Bd_Department dpm ON md.iDepartmentId = dpm.iAutoId
         LEFT JOIN Bd_WorkShiftM wsm ON md.iWorkShiftMid = wsm.iAutoId
         LEFT JOIN Bd_WorkRegionM wrm ON md.iWorkRegionMid = wrm.iAutoId
         LEFT JOIN Bd_Inventory it ON md.iInventoryId = it.iAutoId
         LEFT JOIN Bd_Uom uom ON it.iManufactureUomId = uom.iAutoId
WHERE 1 = 1
  AND md.iStatus = 2 #if(dplandate)
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
SELECT mpd.InvCode,
       it.cInvCode1,
       it.cInvName1,
       it.cInvStd,
       uom.cUomName
FROM T_Sys_MaterialsPrepareDetail mpd
         LEFT JOIN T_Sys_MaterialsPrepare mp ON mpd.MasID = mp.AutoID
         LEFT JOIN Bd_Inventory it ON mpd.InvCode = it.cInvCode
         LEFT JOIN Bd_Uom uom ON it.iInventoryUomId1 = uom.iAutoId
WHERE 1 = 1
  AND mp.BillNo = '#(billno)'
  AND mpd.InvCode is not null #end



#sql("getDetaildatas")
SELECT mpd.InvCode,
       mpd.Qty,
       CASE mpd.State
           WHEN 1 THEN
               '已扫描'
           WHEN 2 THEN
               '未扫描'
           END AS statename,
       it.cInvCode1,
       it.cInvName1,
       it.cInvStd,
       uom.cUomName
FROM T_Sys_MaterialsPrepareDetail mpd
         LEFT JOIN T_Sys_MaterialsPrepare mp ON mpd.MasID = mp.AutoID
         LEFT JOIN Bd_Inventory it ON mpd.InvCode = it.cInvCode
         LEFT JOIN Bd_Uom uom ON it.iInventoryUomId1 = uom.iAutoId
WHERE 1 = 1
  AND mp.BillNo = '#(billno)'
  AND mpd.InvCode is not null #end



  #sql("getBarcodedatas")
SELECT
    sl.AutoID,
    sl.Barcode,
    sl.InvCode,
    it.iAutoId,
    it.cInvCode1,
    it.cInvName1,
    it.cInvStd,
    it.iInventoryUomId1,
    sl.Qty,
    sl.Batch
FROM
    T_Sys_ScanLog sl
        LEFT JOIN Bd_Inventory it ON sl.InvCode= it.cInvCode
WHERE
        sl.Barcode= '#(barcode)' #end



  #sql("getMaterialsOutLines3")
SELECT *
FROM T_Sys_MaterialsPrepareDetail mpd
WHERE 1 = 1
  AND mpd.MasID = '#(autoid)' #end



#sql("cworkname")
SELECT *
FROM Bd_WorkRegionM wrm
WHERE wrm.isDeleted = '0' #if(isenabled)
  AND wrm.isenabled = #para(isenabled)
  #end
#end



#sql("cworkshiftcode")
SELECT *
FROM Bd_WorkShiftM wsm
WHERE wsm.isDeleted = '0' #if(isenabled)
  AND wsm.isenabled = #para(isenabled)
  #end
#end


#sql("barcodeDatas")
SELECT *
FROM PS_SubcontractOrderDBatch
WHERE isEffective = 1 #if(barcode)
		and Barcode = #para(barcode)
	#end
#end






