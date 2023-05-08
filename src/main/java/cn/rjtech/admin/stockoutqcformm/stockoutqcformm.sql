#sql("list")
SELECT t1.*,
       t2.cQcFormName,
       t3.cInvCode1,
       t3.cInvName1,
       t3.cInvStd,
       t3.iInventoryUomId1,
       t3.cInvAddCode,
       t6.cEquipmentName
FROM PL_StockoutQcFormM t1
         LEFT JOIN Bd_QcForm t2 ON t1.iQcFormId = t2.iAutoId
         LEFT JOIN Bd_Inventory t3 ON t1.iInventoryId = t3.iAutoId
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
       t2.cQcItemName,
       t3.cQcParamName,
       t4.iQcFormTableParamId,
       t5.iAutoId as iFormParamId,
       t5.iStdVal,
       t5.iMaxVal,
       t5.iMinVal,
       t5.cOptions,
       t5.iSeq,
       t5.iType
FROM Bd_QcFormItem t1
         LEFT JOIN Bd_QcItem t2 ON t1.iQcItemId = t2.iAutoId
         LEFT JOIN Bd_QcParam t3 ON t2.iAutoId = t3.iQcItemId
         LEFT JOIN Bd_QcFormTableItem t4 ON t1.iAutoId = t4.iQcFormItemId
         LEFT JOIN Bd_QcFormTableParam t5 ON t4.iQcFormTableParamId = t5.iAutoId
WHERE t1.isDeleted = '0'
  #if(iqcformid)
  AND t1.iqcformId = #para(iqcformid)
  #end
ORDER BY t1.iSeq ASC
#end

#sql("findChecoutListByIformParamid")
SELECT
    t1.*,t4.cQcParamName,t5.cQcItemName
FROM PL_InStockQcFormD t1
         LEFT JOIN Bd_QcFormTableItem t2 on t1.iFormParamId = t2.iAutoId
         LEFT JOIN Bd_QcFormParam t3 ON t2.iQcFormParamId = t3.iAutoId
         LEFT JOIN Bd_QcParam t4 ON t3.iQcParamId = t4.iAutoId
         LEFT JOIN Bd_QcItem t5 ON t4.iQcItemId = t5.iAutoId
WHERE
    #if(istockoutqcformmid)
    t1.istockoutqcformmid = #para(istockoutqcformmid)
    #end
ORDER BY t1.iSeq asc
#end

#sql("getonlyseelistByiautoid")
SELECT t1.* ,
       t2.iautoid lineiautoid,
       t2.iSeq,
       t2.cValue,
       t5.cQcParamName,
       t6.cQcItemName
FROM  PL_StockoutQcFormD t1
          LEFT JOIN PL_StockoutQcFormD_Line t2 ON t1.iAutoId = t2.iStockoutQcFormDid
          LEFT JOIN Bd_QcFormTableItem t3 ON t1.iFormParamId = t3.iAutoId
          LEFT JOIN Bd_QcFormParam t4 ON t3.iQcFormParamId = t4.iAutoId
          LEFT JOIN Bd_QcParam t5 ON t4.iQcParamId = t5.iAutoId
          LEFT JOIN Bd_QcItem t6 ON t5.iQcItemId = t6.iAutoId
WHERE
  #if(istockoutqcformmid)
  t1.istockoutqcformmid = #para(istockoutqcformmid)
#end
ORDER BY t1.iSeq asc
#end