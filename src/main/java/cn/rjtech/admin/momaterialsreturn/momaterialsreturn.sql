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