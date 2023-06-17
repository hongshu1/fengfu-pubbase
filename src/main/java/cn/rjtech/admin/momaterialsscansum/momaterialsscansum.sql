#sql("findAll")
SELECT
    v.iAutoId,
    v.cInvCode,
    v.cInvName,
    v.cInvCode1,
    a.iPlanQty,
    a.iScannedQty,
    v.cInvStd,
    uom.cUomName
FROM
    Mo_MaterialsScanSum a
        LEFT JOIN Mo_MoDoc b ON a.iMoDocId= b.iAutoId
        LEFT JOIN Mo_MoRouting c ON c.iMoDocId= b.iAutoId
        LEFT JOIN Mo_MoRoutingConfig d ON d.iMoRoutingId= c.iAutoId
        LEFT JOIN Mo_MoRoutingInvc f ON f.iMoRoutingConfigId= d.iAutoId
        LEFT JOIN Bd_Inventory v ON f.iInventoryId= v.iAutoId
        LEFT JOIN Bd_Uom uom ON uom.iAutoId = v.iInventoryUomId1
    ###单位
where  1=1
    #if(iMoDocId)
  AND a.iMoDocId=#para(iMoDocId)
    #end
    #if(barcode)
  AND mpd.Barcode=#para(barcode)
    #end
    #end

    #sql("findByBarcode")
SELECT
    mpd.InvCode,
    mpd.Barcode,
    mpd.Qty,
    md.iAutoId as iMoDocId,
    it.cInvCode1,
    it.cInvName1,
    it.iAutoID as iInventoryId,
    mpd.AutoID as iMaterialsPrepairDid

FROM
    T_Sys_MaterialsPrepareDetail mpd
        LEFT JOIN T_Sys_MaterialsPrepare mp ON mpd.MasID = mp.AutoID
        LEFT JOIN Mo_MoDoc md ON md.iAutoId = mp.SourceBillID
        LEFT JOIN Bd_Inventory it ON it.cInvCode = mpd.InvCode

WHERE
        1 = 1
    #if(imodocid)
AND md.iAutoId=#para(imodocid)
#end
    #if(barcode)
AND mpd.Barcode=#para(barcode)
#end
#end

#sql("getMoMaterialScanLogList")   ### 扫描现品票
SELECT
    mpd.InvCode,
    m.cBarcode,
    mpd.Qty,
    md.cMoDocNo,
    it.cInvCode1,
    it.cInvName1,
    it.cInvCode,
    it.cInvName,
    uom.cUomName,
    it.cInvStd

FROM
    Mo_MaterialScanLog m
        LEFT JOIN Mo_MoDoc md ON md.iAutoId =m.iMoDocId
        LEFT JOIN T_Sys_MaterialsPrepareDetail mpd  ON m.iMaterialsPrepairDid=mpd.AutoID
        LEFT JOIN Bd_Inventory it ON it.cInvCode = mpd.InvCode
        LEFT JOIN Bd_Uom uom ON uom.iAutoId = it.iInventoryUomId1
where 1=1
    #if(imodocid)
and  md.iAutoId=#para(imodocid)
#end
    #if(barcode)
AND mpd.Barcode=#para(barcode)
#end

#end

#sql("getMoMaterialNotScanLogList")  ###未扫描

SELECT
    mpd.InvCode,
    s.cBarcode as barcode,
    s.iScanQty as Qty,
    md.cMoDocNo,
    it.cInvCode1,
    it.cInvName1,
    it.cInvCode,
    it.cInvName,
    uom.cUomName,
    it.cInvStd
FROM
    T_Sys_MaterialsPrepareScan s
        LEFT  JOIN    T_Sys_MaterialsPrepareDetail mpd on s.iMaterialsPrepareDetailId=mpd.AutoId
        LEFT JOIN Mo_MoDoc md ON md.iAutoId = mpd.SourceBillID
        LEFT JOIN Bd_Inventory it ON it.cInvCode = mpd.InvCode
        LEFT JOIN Bd_Uom uom ON uom.iAutoId = it.iInventoryUomId1
WHERE 1=1
    #if(imodocid)
and  md.iAutoId=#para(imodocid)
#end
    #if(barcode)
AND s.cBarcode=#para(barcode)
#end
 #end


    #sql("findInvCodeUseNum")  ###查找子件物料计划数量
SELECT
    a.iInventoryId,
    f.cInvCode,
    a.iUsageUOM,
    a.iUsageUOM*d.iQty AS planIqty
FROM
    Mo_MoRoutingInvc a   ### 工艺工序物料集
        LEFT JOIN Bd_Inventory f ON a.iInventoryId= f.iAutoId
    LEFT JOIN Mo_MoRoutingConfig b ON a.iMoRoutingConfigId= b.iAutoId ###工单工艺配置
    LEFT JOIN Mo_MoRouting c ON b.iMoRoutingId= c.iAutoId   ###工艺路线
    LEFT JOIN Mo_MoDoc d ON c.iMoDocId= d.iAutoId
where 1=1
    #if(imodocid)
  and  d.iAutoId=#para(imodocid)
    #end
    #if(iinventoryid)
  and  a.iInventoryId=#para(iinventoryid)
    #end
    #end




