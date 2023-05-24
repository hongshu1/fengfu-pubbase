#sql("findAll")
SELECT
    v.iAutoId,
    v.cInvCode,
    v.cInvName,
    v.cInvCode1,
    a.iPlanQty,
    a.iScannedQty,
    v.cInvStd
FROM
    Mo_MaterialsScanSum a
        LEFT JOIN Mo_MoDoc b ON a.iMoDocId= b.iAutoId
        LEFT JOIN Mo_MoRouting c ON c.iMoDocId= b.iAutoId
        LEFT JOIN Mo_MoRoutingConfig d ON d.iMoRoutingId= c.iAutoId
        LEFT JOIN Mo_MoRoutingInvc f ON f.iMoRoutingConfigId= d.iAutoId
        LEFT JOIN Bd_Inventory v ON f.iInventoryId= v.iAutoId
###单位
where  1=1
#if(iMoDocId)
AND a.iMoDocId=#para(iMoDocId)
#end
#end

#sql("findByBarcode")
SELECT
    mpd.InvCode,
    mpd.Barcode,
    mpd.Qty,
    md.cMoDocNo,
    it.cInvCode1,
    it.cInvName1
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

