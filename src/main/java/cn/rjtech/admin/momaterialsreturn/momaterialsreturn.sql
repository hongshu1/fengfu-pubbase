#sql("paginateAdminDatas")
SELECT   auditState =
         CASE WHEN a.iAuditStatus=0 THEN '未审核'
              WHEN a.iAuditStatus=1 THEN '待审核'
              WHEN a.iAuditStatus=2 THEN '审核通过'
              WHEN a.iAuditStatus=3 THEN '审核不通过'
              ELSE '' END, m.*
FROM Mo_MaterialsReturnM m
 LEFT  JOIN   Mo_MoDoc d  on m.iMoDocId=d.iAutoId
 where  1=1
 #if(imodocid)
 and m.iMoDocId=#para(imodocid)
#end
#end

#sql("#findByImodocId")
SELECT
    d.iAutoId
    a.cMoDocNo,
        d.cbarcode,
    i.cInvCode,
    i.cInvName,
    i.cInvCode1,
    i.cInvName1,
    i.cInvStd,
    u.cUomName

FROM
    Mo_MaterialsReturnD d
        LEFT JOIN Bd_Inventory i ON d.iInventoryId= i.iAutoId
        LEFT JOIN Bd_Uom u on i.iInventoryUomId1=u.iAutoId
        LEFT JOIN Mo_MaterialsReturnM m ON d.iMaterialsReturnMid= m.iAutoId
        LEFT JOIN Mo_MoDoc a ON m.iMoDocId= a.iAutoId
where  1=1
    #if(imodocid)
 and a.iAutoId=#para(imodocid)
#end
    #if(cbarcode)
 and d.cBarcode=#para(cbarcode)
#end
  #if(corgcode)
 and m.cOrgCode=#para(corgcode)
#end
    #end