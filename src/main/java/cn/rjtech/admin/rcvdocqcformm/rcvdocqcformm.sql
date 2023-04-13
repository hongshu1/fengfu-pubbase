#sql("list")
SELECT
    t1.*,
    t2.iAutoId qcformiautoid,t2.cQcFormName,
    t3.cInvCode1,t3.cInvName1,t3.cInvStd,t3.iInventoryUomId1,t3.cInvAddCode,
    t4.cVenName,t5.*,t6.cEquipmentName
FROM PL_RcvDocQcFormM t1
         LEFT JOIN Bd_QcForm t2 ON t1.iQcFormId = t2.iAutoId
         LEFT JOIN Bd_Inventory t3 ON t1.iInventoryId = t3.iAutoId
         LEFT JOIN Bd_Vendor t4 ON t1.iVendorId = t4.iAutoId
         LEFT JOIN PL_RcvDocQcFormD t5 ON t1.iRcvDocId = t5.iAutoId
         LEFT JOIN Bd_Equipment t6 ON t3.iEquipmentModelId = t6.iAutoId
where t1.IsDeleted = '0'
  #if(iautoid)
  AND t1.iautoid =#para(iautoid)
  #end
  #if(crcvdocqcformno)
  AND t1.crcvdocqcformno =#para(crcvdocqcformno)
  #end
  #if(crcvdocno)
  AND t1.crcvdocno =#para(crcvdocno)
  #end
  #if(cvenname)
  AND t4.cvenname =#para(cvenname)
  #end
  #if(cinvaddcode)
  AND t3.cinvaddcode =#para(cinvaddcode)
  #end
  #if(cinvcode1)
  AND t3.cinvcode1 =#para(cinvcode1)
  #end
  #if(cinvname1)
  AND t3.cinvname1 =#para(cinvname1)
  #end
  #if(iqcuserid)
  AND t1.iqcuserid =#para(iqcuserid)
  #end
  #if(iscompleted)
  AND t1.iscompleted =#para(iscompleted)
  #end
  #if(istatus)
  AND t1.istatus =#para(istatus)
  #end
  #if(cqcformname)
  AND t2.cqcformname =#para(cqcformname)
  #end
  #if(dcreatetime)
  AND t1.dcreatetime =#para(dcreatetime)
  #end
ORDER BY t1.dUpdateTime
    DESC
#end