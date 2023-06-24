#sql("list")
SELECT t1.*,
       t5.cQcFormName,
       t3.cInvCode1,
       t3.cInvName1,
       t3.cInvStd,
       t3.iInventoryUomId1,
       t3.cInvAddCode,
       t4.cVenName,
       t6.cEquipmentName,
       u.cUomCode,u.cUomName,
       t7.cpics,t7.ctypeids,t7.ctypenames
FROM PL_RcvDocQcFormM t1
         LEFT JOIN Bd_Inventory t3 ON t1.iInventoryId = t3.iAutoId
         LEFT JOIN Bd_Vendor t4 ON t1.iVendorId = t4.iAutoId
         LEFT JOIN Bd_Equipment t6 ON t3.iEquipmentModelId = t6.iAutoId
         LEFT JOIN Bd_Uom u on t3.iInventoryUomId1 = u.iautoid
         LEFT JOIN Bd_QcForm t5 on t1.iqcformid = t5.iautoid
         LEFT JOIN Bd_InventoryQcForm t7 on t1.iqcformid = t7.iqcformid and t1.iInventoryId = t7.iInventoryId
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
  AND t5.cqcformname =#para(cqcformname)
  #end
  #if(starttime)
  AND t1.dcreatetime >= #para(starttime)
  #end
  #if(endtime)
  AND t1.dcreatetime <= #para(endtime)
  #end
ORDER BY t1.dUpdateTime DESC
#end

#sql("findChecoutListByIformParamid")
SELECT
    t1.*,t4.cQcParamName,t5.cQcItemName
FROM PL_RcvDocQcFormD t1
         LEFT JOIN Bd_QcFormTableItem t2 on t1.iFormParamId = t2.iAutoId
         LEFT JOIN Bd_QcFormParam t3 ON t2.iQcFormParamId = t3.iAutoId
         LEFT JOIN Bd_QcParam t4 ON t3.iQcParamId = t4.iAutoId
         LEFT JOIN Bd_QcItem t5 ON t4.iQcItemId = t5.iAutoId
WHERE
    #if(ircvdocqcformmid)
    t1.ircvdocqcformmid = #para(ircvdocqcformmid)
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
FROM  PL_RcvDocQcFormD t1
          LEFT JOIN PL_RcvDocQcFormD_Line t2 ON t1.iAutoId = t2.iRcvDocQcFormDid
          LEFT JOIN Bd_QcFormTableItem t3 ON t1.iFormParamId = t3.iAutoId
          LEFT JOIN Bd_QcFormParam t4 ON t3.iQcFormParamId = t4.iAutoId
          LEFT JOIN Bd_QcParam t5 ON t4.iQcParamId = t5.iAutoId
          LEFT JOIN Bd_QcItem t6 ON t5.iQcItemId = t6.iAutoId
WHERE
  #if(ircvdocqcformmid)
  t1.ircvdocqcformmid = #para(ircvdocqcformmid)
  #end
ORDER BY t1.iSeq asc
#end


#sql("getCheckOutTableDatas")
select t1.* from PL_RcvDocQcFormD t1
where 1=1
#if(ircvdocqcformmid)
    and t1.ircvdocqcformmid = #para(ircvdocqcformmid)
#end
order by t1.iSeq asc
#end

#sql("getQcFormTableItemList")
select t1.*,t2.cQcItemName,t3.cQcParamName from Bd_QcFormTableItem t1
left join Bd_QcItem t2 on t1.iQcFormItemId = t2.iAutoId
left join Bd_QcParam t3 on t1.iQcFormParamId = t3.iAutoId
where t1.iqcformtableparamid=#para(iqcformtableparamid)
and t1.iqcformid=#para(iqcformid)
#end

#sql("getRcvdocqcformdLineList")
select * from PL_RcvDocQcFormD_Line where iRcvDocQcFormDid=#para(iRcvDocQcFormDid)
#end