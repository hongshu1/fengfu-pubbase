#sql("datas")
select t1.*,t2.cWhName,t3.cPsn_Name
from T_Sys_StockCheckVouch t1
left join Bd_Warehouse t2 on t1.WhCode = t2.cWhCode
left join bd_person t3 on t1.checkperson = t3.iautoid
     where 1=1
#if(billno)
  and t1.BillNo like CONCAT('%', #para(billno), '%')
#end
#if(whcode)
  and t1.whcode = #para(whcode)
#end
#if(checktype)
  and t1.checktype = #para(checktype)
#end
#if(startTime)
 and t1.CreateDate >= #para(startTime)
#end
#if(endTime)
  and t1.CreateDate <= #para(endTime)
#end
order by t1.ModifyDate desc
#end

#sql("findBillboAndWhnameAndcAreaNameByAutoid")
select t1.billno,t2.cWhName,t3.cAreaName
from T_Sys_StockCheckVouch t1
left join Bd_Warehouse t2 on t1.WhCode = t2.cWhCode
left join Bd_Warehouse_area t3 on t2.iAutoId = t3.iWarehouseId
where 1=1
      #if(autoid)
      and t1.autoid = #para(autoid)
      #end
#end

#sql("list")
SELECT
t1.*,
t3.cInvCode,t3.cInvStd,t3.cInvCode1,t3.cInvName1,t3.iInventoryUomId1,
t4.cInvCCode,t4.cInvCName,
t5.cAreaCode,t5.cAreaName
from T_Sys_StockCheckVouch t1
LEFT JOIN Bd_Warehouse t2 on t1.WhCode = t2.cWhCode
LEFT JOIN Bd_Inventory t3 on t2.cBarCode = t3.cBarcode
LEFT JOIN Bd_InventoryClass t4 on t3.iAutoId = t4.iPid
LEFT JOIN Bd_Warehouse_Area t5  on t2.iAutoId = t5.iWarehouseId
where 1=1
#if(autoid)
and t1.autoid = #para(autoid)
#end
#if(careacode)
and t5.cAreaCode = #para(careacode)
#end
#if(state)
and t1.state = #para(state)
#end
#if(cinvccode)
and t4.cInvCCode = #para(cinvccode)
#end
#if(cinvcode)
and t3.cInvCode = #para(cinvcode)
#end
#if(cInvCode1)
and t3.cInvCode1 = #para(cinvcode1)
#end
#if(cInvName1)
and t3.cInvName1 = #para(cinvname1)
#end
order by t1.ModifyDate desc
#end

#sql("details")
SELECT
t1.*,
t3.cInvCode,t3.cInvStd,t3.cInvCode1,t3.cInvName1,t3.iInventoryUomId1,
t4.cInvCCode,t4.cInvCName,
t5.cAreaCode,t5.cAreaName,t6.cBarcode,t7.Batch
from T_Sys_StockCheckVouch t1
LEFT JOIN Bd_Warehouse t2 on t1.WhCode = t2.cWhCode
LEFT JOIN Bd_Inventory t3 on t2.cBarCode = t3.cBarcode
LEFT JOIN Bd_InventoryClass t4 on t3.iAutoId = t4.iPid
LEFT JOIN Bd_Warehouse_Area t5  on t2.iAutoId = t5.iWarehouseId
LEFT JOIN PS_PurchaseOrderDBatch t6 on t3.iAutoId = t6.iinventoryId
LEFT JOIN T_Sys_BarcodeDetail t7 on t3.cInvCode = t7.Invcode and t6.cBarcode = t7.Barcode
where 1=1
#if(autoid)
and t1.autoid = #para(autoid)
#end
#if(careacode)
and t5.cAreaCode = #para(careacode)
#end
#if(state)
and t1.state = #para(state)
#end
#if(cinvccode)
and t4.cInvCCode = #para(cinvccode)
#end
#if(cinvcode)
and t3.cInvCode = #para(cinvcode)
#end
#if(cInvCode1)
and t3.cInvCode1 = #para(cinvcode1)
#end
#if(cInvName1)
and t3.cInvName1 = #para(cinvname1)
#end
order by t1.ModifyDate desc
#end

