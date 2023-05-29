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
#if(imodocid)
and  t1.SourceBillDid=#para(imodocid)
#end
#end


#sql("getMaterialsPrepareList")
SELECT
    mpd.InvCode,
    mpd.Barcode,
    mpd.Qty,
    md.cMoDocNo,
    it.cInvCode1,
    it.cInvName1,
    md.dPlanDate,
    uom.cUomName,
    md.iQty,
    it.cInvStd,
    md.iStatus
FROM
    T_Sys_MaterialsPrepareDetail mpd
        LEFT JOIN T_Sys_MaterialsPrepare mp ON mpd.MasID = mp.AutoID
        LEFT JOIN Mo_MoDoc md ON md.iAutoId = mp.SourceBillID
        LEFT JOIN Bd_Inventory it ON it.cInvCode = mpd.InvCode
        LEFT JOIN Bd_Uom uom ON uom.iAutoId = it.iManufactureUomId
        where 1=1
    #if(imodocid)
and  t1.SourceBillDid=#para(imodocid)
#end
#end
