#sql("barcodeSelectDatas")
SELECT t1.cInvCode,t1.cInvName,t1.cInvStd,
       t3.unitname,
       t2.cbarcode,t2.iQty,
       t4.batch
FROM Bd_Inventory t1
         INNER JOIN (
    SELECT iPurchaseOrderDid,iinventoryId,cBarcode,iQty,cSourceld,cSourceBarcode,cSeq from PS_PurchaseOrderDBatch
    UNION ALL
    SELECT iSubcontractOrderDid,iinventoryId,cBarcode,iQty,cSourceld,cSourceBarcode,cSeq from PS_SubcontractOrderDBatch
) t2 on t2.iinventoryId = t1.iAutoId
         LEFT JOIN UFDATA_001_2023.dbo.v_sys_inventory t3 on t1.cInvCode = t3.invcode and t1.cInvName = t3.invname
         LEFT JOIN T_Sys_BarcodeDetail t4 on t1.cInvCode = t4.invcode
where t1.isDeleted = '0'
#(cinvcode)
  and t1.cInvCode = #para(cinvcode)
#end
#(cinvname)
  and t1.cInvName = #para(cinvname)
#end
#(cbarcode)
  and t2.cbarcode = #para(cbarcode)
#end
  order by t1.cupdatetime desc
#end

#sql("barcodeDatas")

#end

#sql("findByShiWu")

#end

#sql("datas")

#end

#sql("findByLogId")

#end

#sql("formdatas")

#end