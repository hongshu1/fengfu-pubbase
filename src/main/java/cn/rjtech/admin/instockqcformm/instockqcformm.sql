#sql("list")
SELECT t1.*,
    t2.cQcFormName,
    t3.cinvcode,
    t3.cInvCode1,
    t3.cInvName1,
    t3.cInvStd,
    t3.iInventoryUomId1,
    t3.cInvAddCode,
    t4.cEquipmentName,
    t5.cUomCode,t5.cUomName,
    t6.cpics,t6.ctypeids,t6.ctypenames,
    t7.cversion,
    statusname =
       CASE WHEN t1.istatus=0 THEN '未有检查表'
            WHEN t1.istatus=1 THEN '待检'
            WHEN t1.istatus=2 THEN '不合格'
            WHEN t1.istatus=3 THEN '合格' END
FROM PL_InStockQcFormM t1
    left JOIN Bd_QcForm t2 ON t1.iQcFormId = t2.iAutoId
    LEFT JOIN Bd_Inventory t3 ON t1.iInventoryId = t3.iAutoId
    LEFT JOIN Bd_Equipment t4 ON t3.iEquipmentModelId = t4.iAutoId
    LEFT JOIN Bd_Uom t5 on t3.iInventoryUomId1 = t5.iautoid
    LEFT JOIN Bd_InventoryQcForm t6 on t1.iqcformid = t6.iqcformid and t1.iInventoryId = t6.iInventoryId
    LEFT JOIN (SELECT iAutoId,iinventoryId,cBarcode,iQty,cVersion from PS_PurchaseOrderDBatch
               UNION ALL
               SELECT iAutoId,iinventoryId,cBarcode,iQty,cVersion from PS_SubcontractOrderDBatch)
    t7 on t1.cBarcode = t7.cBarcode
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
    #if(cbarcode)
    AND t1.cBarcode = #para(cbarcode)
    #end
ORDER BY t1.dUpdateTime DESC
    #end

#sql("getCheckoutList")
    SELECT t1.*,
           t2.cQcItemName,
           t3.cQcParamName,
           t4.iQcFormTableParamId,
           t4.iAutoId as iqcformtableitemid,
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
#if(iinstockqcformmid)
t1.iinstockqcformmid = #para(iinstockqcformmid)
#end
ORDER BY iSeq asc
#end

#sql("getonlyseelistByiautoid")
SELECT t1.* ,
       t2.iautoid lineiautoid,
       t2.iSeq,
       t2.cValue,
       t5.cQcParamName,
       t6.cQcItemName
FROM  PL_InStockQcFormD t1
          LEFT JOIN PL_InStockQcFormD_Line t2 ON t1.iAutoId = t2.iInStockQcFormDid
          LEFT JOIN Bd_QcFormTableItem t3 ON t1.iFormParamId = t3.iAutoId
          LEFT JOIN Bd_QcFormParam t4 ON t3.iQcFormParamId = t4.iAutoId
          LEFT JOIN Bd_QcParam t5 ON t4.iQcParamId = t5.iAutoId
          LEFT JOIN Bd_QcItem t6 ON t5.iQcItemId = t6.iAutoId
WHERE
#if(iinstockqcformmid)
t1.iinstockqcformmid = #para(iinstockqcformmid)
#end
ORDER BY t1.iSeq asc
#end

#sql("getInStockQcFormDByMasId")
select * from PL_InStockQcFormD where 1=1
#if(iinstockqcformdid)
and iInStockQcFormMid = #para(iinstockqcformid)
#end
order by iSeq asc
#end

#sql("getInStockQcFormDLineList")
select * from PL_InStockQcFormD_Line where iInStockQcFormDid=#para(iinstockqcformdid)
#end

#sql("getQcFormTableItemList")
select t1.*,t2.cQcItemName,t3.cQcParamName from Bd_QcFormTableItem t1
left join Bd_QcItem t2 on t1.iQcFormItemId = t2.iAutoId
left join Bd_QcParam t3 on t1.iQcFormParamId = t3.iAutoId
where t1.iqcformtableparamid=#para(iqcformtableparamid)
  and t1.iqcformid=#para(iqcformid)
#end

#sql("findDetailByBarcode")
select t1.iAutoId,t1.cbarcode,t1.iQty,t1.iinventoryId,
       t2.cInvCode as invcode,t2.cInvCode1,t2.cInvName1,
       t3.iqcformid,t3.cdccode,t3.cmeasure
from
     (SELECT iAutoId,iinventoryId,cBarcode,iQty,cVersion from PS_PurchaseOrderDBatch
      UNION ALL
      SELECT iAutoId,iinventoryId,cBarcode,iQty,cVersion from PS_SubcontractOrderDBatch) t1
inner join Bd_Inventory t2 on t1.iinventoryId = t2.iAutoId
left join Bd_InventoryQcForm t3 on t2.iAutoId = t3.iinventoryId
inner join Bd_QcForm t4 on t3.iQcFormId = t4.iautoid
where t1.cBarcode = #para(cbarcode)
order by t3.dupdatetime desc
#end