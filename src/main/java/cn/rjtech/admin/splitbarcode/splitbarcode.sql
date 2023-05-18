#sql("BarCodeSelectDatas")
SELECT t1.cInvCode,t1.cInvStd,t1.cInvName,
       t2.cbarcode,t2.iQty
FROM Bd_Inventory t1
    INNER JOIN (
    SELECT iinventoryId,cBarcode,iQty from PS_PurchaseOrderDBatch
    UNION ALL
    SELECT iinventoryId,cBarcode,iQty from PS_SubcontractOrderDBatch
) t2 on t2.iinventoryId = t1.iAutoId
where t1.isDeleted = '0'
#if(cinvname)
  and  t1.cinvcode = #para(cinvname)
#end
#if(cinvname)
  and t1.cinvname = #para(cinvname)
#end
#if(cbarcode)
  and t2.cbarcode = #para(cbarcode)
#end
order by t1.dUpdateTime desc
#end

#sql("findByListCsourceid")
SELECT t2.*,t3.Batch,t3.PAT
from  T_Sys_ScanLog log
INNER JOIN (
    SELECT iPurchaseOrderDid,iinventoryId,cBarcode,iQty,cSourceld,cSourceBarcode,cSeq from PS_PurchaseOrderDBatch
    UNION ALL
    SELECT iSubcontractOrderDid,iinventoryId,cBarcode,iQty,cSourceld,cSourceBarcode,cSeq from PS_SubcontractOrderDBatch
) t2 on log.barcode = t2.cSourceBarcode
left join T_Sys_BarcodeDetail t3 on log.InvCode = t3.Invcode and log.Barcode = t3.Barcode
where 1=1
#if(csourceid)
and t2.cSourceld = #para(csourceid)
#end
order by t2.cSeq
#end

#sql("barcodeDatas")
SELECT log.*,inv.invname,inv.invstd,inv.unitname,
       t3.cInvCode1,t3.cInvName1,t3.iInventoryUomId1,
       t4.iautoid csourceid,t4.ipurchaseorderdid
from  T_Sys_ScanLog log
LEFT JOIN UFDATA_001_2023.dbo.v_sys_inventory inv on inv.invcode = log.invcode
LEFT JOIN Bd_Inventory t3 on log.invcode = t3.cInvCode
LEFT JOIN (
    SELECT iAutoId,iPurchaseOrderDid,iinventoryId,cBarcode,iQty,cSourceld,cSourceBarcode from PS_PurchaseOrderDBatch
    UNION ALL
    SELECT iAutoId,iSubcontractOrderDid,iinventoryId,cBarcode,iQty,cSourceld,cSourceBarcode from PS_SubcontractOrderDBatch
) t4 on log.Barcode = t4.cbarcode
WHERE log.ProcessType = 'BarcodeSplit' and log.TagBillType='begin'
#if(organizeCode)
  and log.organizecode = #para(organizecode)
#end
#if(organizeCode)
  and inv.organizecode = #para(organizecode)
#end
#if(invcode)
  and log.invcode like concat('%',#para(invcode),'%')
#end
#if(barcode)
  and log.invcode like concat('%',#para(barcode),'%')
#end
#if(barcode)
  and log.invcode like concat('%',#para(barcode),'%')
#end
#if(invname)
  and inv.invname like concat('%',#para(invname),'%')
#end
#if(cinvcode1)
  and t3.cinvcode1 like concat('%',#para(cinvcode1),'%')
#end
  order by log.CreateDate desc
#end

#sql("findByShiWu")
SELECT log.*,inv.invname,inv.invstd,inv.unitname,
t3.cInvCode1,t3.cInvName1,t3.iInventoryUomId1,
t4.iautoid csourceid,t4.iqty as qty
from  T_Sys_ScanLog log
      LEFT JOIN UFDATA_001_2023.dbo.v_sys_inventory inv on inv.invcode = log.invcode
      LEFT JOIN Bd_Inventory t3 on log.invcode = t3.cInvCode
      LEFT JOIN (
SELECT iAutoId,iPurchaseOrderDid,iinventoryId,cBarcode,iQty,cSourceld,cSourceBarcode from PS_PurchaseOrderDBatch
UNION ALL
SELECT iAutoId,iSubcontractOrderDid,iinventoryId,cBarcode,iQty,cSourceld,cSourceBarcode from PS_SubcontractOrderDBatch
) t4 on log.Barcode = t4.cbarcode
WHERE log.ProcessType = 'BarcodeSplit' and log.TagBillType='begin'
#if(logno)
and log.logno = #para(logno)
#end
#end