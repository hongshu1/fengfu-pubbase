#sql("list")
SELECT t1.*,
       t2.cQcFormName,
       t3.cInvCode1,
       t3.cInvName1,
       t3.cInvStd,
       t3.iInventoryUomId1,
       t3.cInvAddCode,
       t6.cEquipmentName
FROM PL_InStockQcFormM t1
         LEFT JOIN Bd_QcForm t2 ON t1.iQcFormId = t2.iAutoId
         LEFT JOIN Bd_Inventory t3 ON t1.iInventoryId = t3.iAutoId
         LEFT JOIN Bd_Equipment t6 ON t3.iEquipmentModelId = t6.iAutoId
where t1.IsDeleted = '0'
#if(iautoid)
  AND t1.iautoid =#para(iautoid)
  #end
  #if(cinvqcformno)
  AND t1.cinvqcformno =#para(cinvqcformno)
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
  #if(starttime)
  AND t1.dcreatetime >= #para(starttime)
  #end
  #if(endtime)
  AND t1.dcreatetime <= #para(endtime)
  #end
ORDER BY t1.dUpdateTime DESC
#end

#sql("getCheckoutList")
SELECT t1.*,
       t3.cQcParamName,
       t4.cQcItemName
FROM Bd_QcFormTableParam t1
         LEFT JOIN Bd_QcFormParam t2 ON t1.iFormParamId = t2.iAutoId
         LEFT JOIN Bd_QcParam t3 ON t2.iQcParamId = t3.iAutoId
         LEFT JOIN Bd_QcItem t4 ON t3.iQcItemId = t4.iAutoId
WHERE t1.isDeleted = '0'
  #if(iqcformid)
  AND t1.iqcformId = #para(iqcformid)
  #end
ORDER BY t1.iSeq asc
#end

#sql("findChecoutListByIformParamid")
SELECT t1.*,
       t3.cQcItemName,
       t4.cQcParamName
FROM PL_InStockQcFormD t1
         LEFT JOIN Bd_QcFormParam t2 ON t1.iFormParamId = t2.iautoid
         LEFT JOIN Bd_QcItem t3 ON t2.iqcformitemid = t3.iAutoId
         LEFT JOIN Bd_QcParam t4 ON t2.iQcParamId = t4.iAutoId
WHERE
    #if(iinstockqcformmid)
    t1.iinstockqcformmid = #para(iinstockqcformmid)
    #end
ORDER BY t1.iSeq asc
#end

#sql("getonlyseelistByiautoid")
SELECT
    t2.*,
    t3.iautoid lineiautoid,t3.iSeq,t3.cValue,
    t5.cQcParamName,
    t6.cQcItemName
FROM PL_InStockQcFormM t1
         LEFT JOIN PL_InStockQcFormD t2 ON t1.iAutoId = t2.iInStockQcFormMid
         LEFT JOIN PL_InStockQcFormD_Line t3 ON t2.iAutoId = t3.iInStockQcFormDid
         LEFT JOIN Bd_QcFormParam t4 ON t2.iFormParamId = t4.iAutoId
         LEFT JOIN Bd_QcParam t5 ON t4.iQcParamId = t5.iAutoId
         LEFT JOIN Bd_QcItem t6 ON t5.iQcItemId = t6.iAutoId
WHERE t1.IsDeleted = '0'
  #if(iautoid)
  AND t1.iautoid = #para(iautoid)
#end
#end