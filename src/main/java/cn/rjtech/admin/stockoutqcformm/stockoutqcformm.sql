#sql("list")
SELECT t1.*,
       t2.cCusCode,t2.cCusName,
       t3.cQcFormName,
       t4.cInvCode1,t4.cInvName1,t4.cInvStd,t4.iInventoryUomId1,t4.cInvAddCode
from PL_StockoutQcFormM t1
         LEFT JOIN Bd_Customer t2 ON t1.iCustomerId = t2.iAutoId
         LEFT JOIN Bd_QcForm t3 ON t1.iQcFormId = t3.iAutoId
         LEFT JOIN Bd_Inventory t4 ON t1.iInventoryId = t4.iAutoId
where t1.IsDeleted = '0'
  #if(iautoid)
  AND t1.iautoid = #para(iautoid)
  #end
  #if(cstockoutqcformno)
  AND t1.cstockoutqcformno =#para(cstockoutqcformno)
  #end
  #if(crcvdocno)
  AND t1.crcvdocno =#para(crcvdocno)
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
  #if(istatus)
  AND t1.istatus =#para(istatus)
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
ORDER BY t1.dUpdateTime DESC
#end