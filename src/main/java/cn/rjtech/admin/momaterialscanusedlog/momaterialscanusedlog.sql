#sql("getMaterialscanList")
SELECT
    t2.barcode,
    t2.qty,
    u.cUomClassName,
    i.cInvCode,
    i.cInvName,
    md.cMoDocNo,
    i.cInvCode1,
    i.cInvName1
FROM
    T_Sys_MaterialsOutDetail t2
        LEFT JOIN T_Sys_MaterialsOut t1 ON t2.MasID= t1.AutoID
        LEFT JOIN Mo_MoDoc md ON t1.SourceBillDid= md.iAutoId
        LEFT JOIN bd_inventory i ON i.cinvcode = t2.Invcode
        LEFT JOIN Bd_UomClass u ON i.iUomClassId = u.iautoid
        LEFT JOIN Bd_InventoryClass t3 ON i.iInventoryClassId = t3.iautoid
WHERE
        1 = 1
        and t2.barcode not in(SELECT d.cBarcode from Mo_MaterialScanUsedLogD d
                                                         LEFT JOIN Mo_MaterialScanUsedLogM on d.iMaterialScanUsedLogMid=d.iAutoId
                              WHERE 1=1
                                #if(imodocid)
                                  and   m.iMoDocId=#(imodocid)
                                   #end )
)
#if(imodocid)
and  t1.SourceBillDid=#para(imodocid)
#end
#end


#sql("getMoMaterialscanusedlogList")
SELECT
    mpd.InvCode,
    mpd.Barcode,
    mpd.Qty,
    md.cMoDocNo,
    it.cInvCode1,###客户部番
    it.cInvName1, ### 部品名称
    md.dPlanDate,
    uom.cUomName,
    md.iQty,
    it.cInvStd,###规格
    md.iStatus,
    f.iScannedQty
FROM
    T_Sys_MaterialsPrepareDetail mpd
        LEFT JOIN T_Sys_MaterialsPrepare mp ON mpd.MasID = mp.AutoID
        LEFT JOIN Mo_MoDoc md ON md.iAutoId = mp.SourceBillID
        LEFT JOIN Bd_Inventory it ON it.cInvCode = mpd.InvCode
        LEFT JOIN Bd_Uom uom ON uom.iAutoId = it.iManufactureUomId
        LEFT JOIN (
        SELECT
            d.cBarcode,
            d.iScannedQty
        FROM
            Mo_MaterialScanUsedLogD d
                LEFT JOIN Mo_MaterialScanUsedLogM m
                          ON d.iMaterialScanUsedLogMid= m.iAutoId
        WHERE
            1 = 1
            #if(imodocid)
            and m.iMoDocId=#para(imodocid)
            #end
    ) f ON f.cBarcode= mpd.Barcode

    #if(imodocid)
and  mp.SourceBillid=#para(imodocid)
#end

#end


#sql("getMaterialsPrepareList")
SELECT
    mpd.InvCode,
    mpd.Barcode,
    mpd.Qty,
    md.cMoDocNo,
    it.cInvCode1,###客户部番
    it.cInvName1, ### 部品名称
    md.dPlanDate,
        uom.cUomName,
    md.iQty,
    it.cInvStd,###规格
    md.iStatus
FROM
    T_Sys_MaterialsPrepareDetail mpd
        LEFT JOIN T_Sys_MaterialsPrepare mp ON mpd.MasID = mp.AutoID
        LEFT JOIN Mo_MoDoc md ON md.iAutoId = mp.SourceBillID
        LEFT JOIN Bd_Inventory it ON it.cInvCode = mpd.InvCode
        LEFT JOIN Bd_Uom uom ON uom.iAutoId = it.iManufactureUomId
where 1=1
    #if(imodocid)
and  md.iAutoId=#para(imodocid)
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
    #if(barcode)
AND mpd.Barcode=#para(barcode)
#end
#end

#sql("getBarcodeAllBycBarcode")
    SELECT LOG.*, cInvCode, cInvCode1,cInvName1,cInvStd, uom.cUomName
    FROM Mo_MaterialScanLog LOG
    LEFT JOIN Bd_Inventory INV ON LOG.iInventoryId=INV.iAutoId
    LEFT JOIN Bd_Uom uom ON uom.iAutoId = INV.iInventoryUomId1
    WHERE  LOG.iMoDocId = #para(imodocid)  and isScanned=1
    #if(iautoid)
    and LOG.iAutoId not in (#(iautoid))
    #end
#end


#sql("getMaterialScanUsedLog")
    SELECT LOG.*,LOG.iQty iScannedQty,iQty-iQty iRemainQty,cInvCode, cInvCode1,cInvName1,cInvStd, uom.cUomName
    FROM Mo_MaterialScanLog LOG
    LEFT JOIN Bd_Inventory INV ON LOG.iInventoryId=INV.iAutoId
    LEFT JOIN Bd_Uom uom ON uom.iAutoId = INV.iInventoryUomId1
    WHERE  LOG.iMoDocId = #para(imodocid)  and isScanned=1
    and LOG.cBarcode=#para(barcode)
#end