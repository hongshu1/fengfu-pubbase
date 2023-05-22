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