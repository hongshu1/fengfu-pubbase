#sql("barcodeSelectDatas")
select t1.iAutoId,t1.cInvCode,t1.cInvName,
       t1.cInvCode1,t1.cInvAddCode,t1.cInvName1,t1.cInvStd
from bd_inventory t1
where t1.isDeleted = '0' and t1.isEnabled = '1'
#if(cinvcode)
and t1.cinvcode =#para(cinvcode)
#end
#if(cinvcode1)
  and t1.cinvcode1 = #para(cinvcode1)
#end
#if(cinvname1)
  and t1.cinvname1 = #para(cinvname1)
#end
order by t1.dUpdateTime desc
#end

#sql("datas")
SELECT t1.iAutoId,t1.cInvCode,t1.cInvName,
       t1.cInvCode1,t1.cInvAddCode,t1.cInvName1,t1.cInvStd,
       t2.cBarcode,t2.iQty
FROM Bd_Inventory t1
         inner join (
    SELECT iAutoId,iPurchaseOrderDid,iinventoryId,cBarcode,iQty,cSourceld,cSourceBarcode from PS_PurchaseOrderDBatch
    UNION ALL
    SELECT iAutoId,iSubcontractOrderDid,iinventoryId,cBarcode,iQty,cSourceld,cSourceBarcode from PS_SubcontractOrderDBatch
) t2 on t1.iautoid = t2.iinventoryId
where 1=1
#if(cinvcode)
    and t1.cInvCode = #para(cinvcode)
#end
#end