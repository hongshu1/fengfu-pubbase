#sql("paginateAdminDatas")
SELECT   auditState =
         CASE WHEN m.iAuditStatus=0 THEN '未审核'
              WHEN m.iAuditStatus=1 THEN '待审核'
              WHEN m.iAuditStatus=2 THEN '审核通过'
              WHEN m.iAuditStatus=3 THEN '审核不通过'
              ELSE '' END, m.*,d.cMoDocNo,t.cDepName,s.BillNo
FROM Mo_MaterialsReturnM m
 LEFT  JOIN   Mo_MoDoc d  on m.iMoDocId=d.iAutoId
 LEFT JOIN  Bd_Department t on d.iDepartmentId=t.iAutoId
 LEFT  JOIN  T_Sys_TransVouch s on s.SourceBillDid=d.iAutoId
 where  1=1
 #if(imodocid)
 and m.iMoDocId=#para(imodocid)
#end
 #if(billno)
 and s.billNo LIKE concat('%',#para(billno),'%')
#end
#end

#sql("findByImodocId")
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
    #if(imaterialsreturnmid)
 and m.iAutoId=#para(imaterialsreturnmid)
#end
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

#sql("getMaterialScanLogBycBarcode")
SELECT * FROM (
SELECT MAX(type) type,A.cBarcode FROM (
select
d.isScanned AS type,
d.cBarcode
from Mo_MaterialScanLog d
left join Bd_Inventory i on d.iInventoryId=i.iAutoId
UNION ALL
select
3 AS type,
d.cBarcode
from Mo_MaterialScanUsedLogD d
left join Bd_Inventory i on d.iInventoryId=i.iAutoId
left join Mo_MaterialScanUsedLogM m on d.iMaterialScanUsedLogMid=m.iAutoId
)A
GROUP BY A.cBarcode
) B
LEFT JOIN (
select d.iAutoId,
d.isScanned AS type,
d.cBarcode,
d.iMoDocId,
d.iplayQty as iscannedqty,
d.iQty,
d.iInventoryId,
i.cInvCode,
i.cInvAddCode,
i.cInvName,
i.cInvStd,
i.iUomClassId
from Mo_MaterialScanLog d
left join Bd_Inventory i on d.iInventoryId=i.iAutoId
UNION ALL
select d.iAutoId,
3 AS type,
d.cBarcode,
m.iMoDocId,
d.iscannedqty,
d.iQty,
d.iInventoryId,
i.cInvCode,
i.cInvAddCode,
i.cInvName,
i.cInvStd,
i.iUomClassId
from Mo_MaterialScanUsedLogD d
left join Bd_Inventory i on d.iInventoryId=i.iAutoId
left join Mo_MaterialScanUsedLogM m on d.iMaterialScanUsedLogMid=m.iAutoId
) C ON B.cBarcode = C.cBarcode AND C.type = B.type
WHERE C.TYPE<>0 and B.cBarcode=#para(barcode)
#end


#sql ("getmomaterialscanusedlogList")
SELECT * FROM (
SELECT MAX(type) type,A.cBarcode FROM (
select
d.isScanned AS type,
d.cBarcode
from Mo_MaterialScanLog d
left join Bd_Inventory i on d.iInventoryId=i.iAutoId
UNION ALL
select
3 AS type,
d.cBarcode
from Mo_MaterialScanUsedLogD d
left join Bd_Inventory i on d.iInventoryId=i.iAutoId
left join Mo_MaterialScanUsedLogM m on d.iMaterialScanUsedLogMid=m.iAutoId
)A
GROUP BY A.cBarcode
) B
LEFT JOIN (
select d.iAutoId,
d.isScanned AS type,
d.cBarcode,
d.iMoDocId,
d.iplayQty as iscannedqty,
d.iQty,
d.iInventoryId,
i.cInvCode,
i.cInvAddCode,
i.cInvName,
i.cInvStd,
i.iUomClassId
from Mo_MaterialScanLog d
left join Bd_Inventory i on d.iInventoryId=i.iAutoId
UNION ALL
select d.iAutoId,
3 AS type,
d.cBarcode,
m.iMoDocId,
d.iscannedqty,
d.iQty,
d.iInventoryId,
i.cInvCode,
i.cInvAddCode,
i.cInvName,
i.cInvStd,
i.iUomClassId
from Mo_MaterialScanUsedLogD d
left join Bd_Inventory i on d.iInventoryId=i.iAutoId
left join Mo_MaterialScanUsedLogM m on d.iMaterialScanUsedLogMid=m.iAutoId
) C ON B.cBarcode = C.cBarcode AND C.type = B.type
WHERE C.TYPE<>0
#end