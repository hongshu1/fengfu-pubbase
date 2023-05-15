#sql("getAutocompleteData")
select top #(limit)
    i.*,
    u.cUomClassName,
    (SELECT cUomName FROM Bd_Uom WHERE i.iInventoryUomId1 = iautoid) as InventorycUomName,
    (SELECT cUomName FROM Bd_Uom WHERE i.iPurchaseUomId = iautoid) as PurchasecUomName,
    t3.cInvCCode,
    t3.cInvCName,
    t4.cEquipmentModelName
from bd_inventory i
         LEFT JOIN Bd_UomClass u ON i.iUomClassId = u.iautoid
         LEFT JOIN Bd_InventoryClass t3 ON i.iInventoryClassId = t3.iautoid
         LEFT JOIN Bd_EquipmentModel t4 ON i.iEquipmentModelId = t4.iautoid

where 1=1
    #if(q)
		and (i.cinvcode like concat('%',#para(q),'%') or i.cinvcode1 like concat('%',#para(q),'%')
			or i.cinvname1 like concat('%',#para(q),'%') or i.cinvstd like concat('%',#para(q),'%')
			or u.cUomClassName like concat('%',#para(q),'%')
		)
	#end
#end



#sql("getBarcodeDatas")
select top #(limit)
       t1.Barcode,
       t1.SourceID as SourceBIllNoRow,
       t1.SourceBillType as SourceBillType,
       t1.SourceBillNo,t1.Qty,t1.BarcodeDate,
       t1.BarcodeID as SourceBillID,
       t1.MasID as SourceBillDid,
       t1.pat,
       i.*,

       (SELECT cUomName FROM Bd_Uom WHERE i.iInventoryUomId1 = iautoid) as InventorycUomName,
       (SELECT cUomName FROM Bd_Uom WHERE i.iPurchaseUomId = iautoid) as PurchasecUomName,
       (SELECT cUomName FROM Bd_Container WHERE i.iContainerClassId = iautoid) as cContainerCode,
       u.cUomClassName,
       t3.cInvCCode,
       t3.cInvCName,
       t4.cEquipmentModelName
from
    V_Sys_BarcodeDetail t1
         LEFT JOIN bd_inventory i ON i.cinvcode = t1.Invcode
         LEFT JOIN Bd_UomClass u ON i.iUomClassId = u.iautoid
         LEFT JOIN Bd_InventoryClass t3 ON i.iInventoryClassId = t3.iautoid
         LEFT JOIN Bd_EquipmentModel t4 ON i.iEquipmentModelId = t4.iautoid
where 1=1
    #if(q)
		and (i.cinvcode like concat('%',#para(q),'%') or i.cinvcode1 like concat('%',#para(q),'%')
			or i.cinvname1 like concat('%',#para(q),'%') or i.cinvstd like concat('%',#para(q),'%')
			or u.cUomClassName like concat('%',#para(q),'%') or t1.Barcode like concat('%',#para(q),'%')
		)
	#end
	 AND t1.OrganizeCode = #(orgCode)
#end


#sql("paginateAdminDatas")
SELECT
      t1.*
FROM
    T_Sys_OtherOut t1
WHERE 1 = 1

    #if(selectparam)
    AND (t1.BillNo LIKE CONCAT('%',#para(selectparam), '%')
    OR t1.cSpecRcvDocNo LIKE CONCAT('%', #para(selectparam), '%')
    OR t1.DeptCode LIKE CONCAT('%', #para(selectparam), '%')
    )
    #end
   #if(iorderstatus)
        AND t1.iOrderStatus = #para(iorderstatus)
    #end
#if(startdate)
    and CONVERT(VARCHAR(10),t1.ModifyDate,23) >='#(startdate)'
#end
#if(enddate)
    and CONVERT(VARCHAR(10),t1.ModifyDate,23) <='#(enddate)'
#end
order by t1.ModifyDate desc
    #end



#sql("getOtherOutLines")
SELECT t2.*,
       i.*,
       u.cUomClassName,
       t3.cInvCCode,
       t3.cInvCName
FROM T_Sys_OtherOut t1,
     T_Sys_OtherOutDetail t2
         LEFT JOIN bd_inventory i ON i.cinvcode = t2.Invcode
         LEFT JOIN Bd_UomClass u ON i.iUomClassId = u.iautoid
         LEFT JOIN Bd_InventoryClass t3 ON i.iInventoryClassId = t3.iautoid
WHERE
    t1.AutoID = t2.MasID AND  t1.AutoID = '#(autoid)'
#end