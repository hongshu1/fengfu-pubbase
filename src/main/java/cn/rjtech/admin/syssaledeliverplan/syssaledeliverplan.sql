#sql("RdStyle")
SELECT  a.*
FROM Bd_Rd_Style a
where a.bRdFlag = '0'
	#if(q)
		and (a.cRdCode like concat('%',#para(q),'%') OR a.cRdName like concat('%',#para(q),'%'))
	#end
#end



#sql("order")
SELECT  a.*,b.cCusName as cuname
FROM Co_SubcontractSaleOrderM a
left join Bd_Customer b on  a.iCustomerId = b.iAutoId
where a.IsDeleted = '0'
	#if(icustomerid)
		and a.iCustomerId = #para(icustomerid)
	#end
	#if(cuname)
		and b.cCusName = #para(cuname)
	#end
	#if(corderno)
		and a.cOrderNo like concat('%',#para(corderno),'%')
	#end
	#if(startTime)
		and a.dOrderDate >= #para(startTime)
	#end
	#if(endTime)
		and a.dOrderDate <= #para(endTime)
	#end
#end



#sql("saletype")
SELECT  a.*
FROM Bd_SaleType a
where a.IsDeleted = '0'
	#if(q)
		and (a.cSTCode like concat('%',#para(q),'%') OR a.cSTName like concat('%',#para(q),'%'))
	#end
#end

#sql("department")
SELECT  a.*
FROM Bd_Department a
where a.IsDeleted = '0'
	#if(q)
		and (a.cDepCode like concat('%',#para(q),'%') OR a.cDepName like concat('%',#para(q),'%'))
	#end
#end


#sql("customeraddr")
SELECT  a.*
FROM Bd_CustomerAddr a
where a.IsDeleted = '0'
	#if(q)
		and (a.cDistrictCode like concat('%',#para(q),'%') OR a.cDistrictName like concat('%',#para(q),'%'))
	#end
#end


#sql("settlestyle")
SELECT  a.*
FROM Bd_SettleStyle a
where a.IsDeleted = '0'
	#if(q)
		and (a.cSSCode like concat('%',#para(q),'%') OR a.cSSName like concat('%',#para(q),'%'))
	#end
#end


#sql("foreigncurrency")
SELECT  a.*,a.cexch_code as cexchcode,a.cexch_name as cexchname
FROM Bd_ForeignCurrency a
where a.IsDeleted = '0'
	#if(q)
		and (a.cexch_code like concat('%',#para(q),'%') OR a.cexch_name like concat('%',#para(q),'%'))
	#end
#end




#sql("syssaledeliverplanList")
SELECT  a.*,sa.cSTCode,sa.cSTName,de.cDepCode,de.cDepName,cr.cCusName
FROM T_Sys_SaleDeliverPlan a
LEFT JOIN  Bd_SaleType sa on sa.cSTCode = a.RdCode
LEFT JOIN Bd_Department de on de.cDepCode = a.DeptCode
LEFT JOIN Co_SubcontractSaleOrderM sm on sm.cOrderNo = a.BillNo
LEFT JOIN Bd_Customer cr on  sm.iCustomerId = cr.iAutoId
where 1=1
	#if(billno)
		and a.BillNo like concat('%',#para(billno),'%')
	#end
	#if(cdepname)
		and de.cDepName like concat('%',#para(cdepname),'%')
	#end
	#if(ccusname)
		and cr.cCusName like concat('%',#para(ccusname),'%')
	#end
ORDER BY a.ModifyDate DESC
#end



#sql("dList")
SELECT  a.*, t1.Barcode,
       t1.SourceID as SourceBIllNoRow,
       t1.SourceBillType as SourceBillType,
       t1.SourceBillNo,t1.Qty,t1.BarcodeDate,
       t1.BarcodeID as SourceBillID,
       t1.MasID as SourceBillDid,
       t1.pat,
       i.*,

       (SELECT cUomName FROM Bd_Uom WHERE i.iInventoryUomId1 = iautoid) as InventorycUomName,
       (SELECT cUomName FROM Bd_Uom WHERE i.iPurchaseUomId = iautoid) as PurchasecUomName,
       (SELECT cContainerCode FROM Bd_Container WHERE i.iContainerClassId = iautoid) as cContainerCode,
       u.cUomClassName,
       t3.cInvCCode,
       t3.cInvCName,
       t4.cEquipmentModelName,
        wh.cWhName as whname
FROM T_Sys_SaleDeliverPlanDetail a
LEFT JOIN  V_Sys_BarcodeDetail t1 on t1.Barcode = a.Barcode
LEFT JOIN bd_inventory i ON i.cinvcode = t1.Invcode
LEFT JOIN Bd_UomClass u ON i.iUomClassId = u.iautoid
LEFT JOIN Bd_InventoryClass t3 ON i.iInventoryClassId = t3.iautoid
LEFT JOIN Bd_EquipmentModel t4 ON i.iEquipmentModelId = t4.iautoid
LEFT JOIN Bd_Warehouse wh on wh.cWhCode = a.WhCode

where 1=1
	#if(masid)
		and a.MasID = #para(masid)
	#end
ORDER BY a.ModifyDate DESC
#end

