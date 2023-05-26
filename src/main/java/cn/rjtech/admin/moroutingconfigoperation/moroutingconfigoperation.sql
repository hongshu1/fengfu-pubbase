#sql ("findOperationList")
SELECT
    a.*,p.iAutoId,p.cOperationCode,p.cOperationName
FROM
    Mo_MoRoutingConfig_Operation a
        LEFT JOIN Bd_Operation p ON a.iOperationId= p.iAutoId
        LEFT JOIN Mo_MoRoutingConfig d ON a.iMoInventoryRoutingConfigId= d.iAutoId
        LEFT JOIN Mo_MoRouting b ON d.iMoRoutingId= b.iAutoId
        LEFT JOIN Mo_MoDoc c ON b.iMoDocId= c.iAutoId
        where  1=1
        #if(iMoDocId)
        AND c.iAutoId=#para(iMoDocId)
#end
#end