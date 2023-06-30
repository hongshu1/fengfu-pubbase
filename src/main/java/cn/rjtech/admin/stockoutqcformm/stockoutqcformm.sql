#sql("list")
SELECT t1.*,
       t2.cQcFormName,
       t3.cinvcode,
       t3.cInvCode1,
       t3.cInvName1,
       t3.cInvStd,
       t3.iInventoryUomId1,
       t3.cInvAddCode,
       t6.cEquipmentName,
       u.cUomCode,u.cUomName,
       t7.cpics,t7.ctypeids,t7.ctypenames,
       t8.ccusname,
       co.cOrderNo,
       statusname =
        CASE WHEN t1.istatus=0 THEN '未有检查表'
        WHEN t1.istatus=1 THEN '待检'
        WHEN t1.istatus=2 THEN '不合格'
        WHEN t1.istatus=3 THEN '合格' END
FROM PL_StockoutQcFormM t1
         LEFT JOIN Bd_QcForm t2 ON t1.iQcFormId = t2.iAutoId
         LEFT JOIN Bd_Inventory t3 ON t1.iInventoryId = t3.iAutoId
         LEFT JOIN Bd_Equipment t6 ON t3.iEquipmentModelId = t6.iAutoId
         LEFT JOIN Bd_Uom u on t3.iInventoryUomId1 = u.iautoid
         LEFT JOIN Bd_InventoryQcForm t7 on t1.iqcformid = t7.iqcformid and t1.iInventoryId = t7.iInventoryId
         LEFT JOIN Bd_Customer t8 on t1.iCustomerId = t8.iAutoId
         LEFT JOIN Co_ManualOrderM co on t1.iManualOrderMid = co.iAutoId
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
  #if(cinvcode)
  AND t3.cinvcode LIKE CONCAT('%', #para(cinvcode), '%')
  #end
  #if(cinvaddcode)
  AND t3.cinvaddcode LIKE CONCAT('%', #para(cinvaddcode), '%')
  #end
  #if(cinvcode1)
  AND t3.cinvcode1 LIKE CONCAT('%', #para(cinvcode1), '%')
  #end
  #if(cinvname1)
  AND t3.cinvname1 LIKE CONCAT('%', #para(cinvname1), '%')
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

#sql("getStockoutQcFormDByMasid")
select t1.* from PL_StockoutQcFormD t1
where 1=1
#if(istockoutqcformmid)
  and t1.iStockoutQcFormMid = #para(istockoutqcformmid)
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

#sql("getStockQcFormDLineList")
select * from PL_StockoutQcFormD_Line where iStockoutQcFormDid=#para(istockoutqcformdid)
#end