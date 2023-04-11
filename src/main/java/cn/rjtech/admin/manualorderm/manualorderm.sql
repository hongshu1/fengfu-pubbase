#sql("list")
select mom.*,d.name statename,c.cCusName from Co_ManualOrderM mom
 inner join Bd_Customer c on c.iAutoId = mom.iCustomerId
 inner join #(getBaseDbName()).dbo.jb_dictionary d on d.sn = mom.iOrderStatus and d.type_key = 'manual_order_state'
 where 1=1

#if(corderno)
and mom.cOrderNo = #para(corderno)
#end
#if(iyear)
and mom.iYear = #para(iyear)
#end
#if(imonth)
and mom.iMonth = #para(imonth)
#end
#if(iorderstatus)
and mom.iOrderStatus = #para(iorderstatus)
#end
#if(starttime)
and pl.dCreateTime >= #para(starttime)
#end
#if(endtime)
and pl.dCreateTime <= #para(endtime)
#end
order by pl.dCreateTime desc
#end

#sql("manualorderds")
select mod.*,i.cInvCode,i.cInvCode1,i.cInvName1,i.cInvStd,u1.cUomName from Co_ManualOrderD mod
left join Bd_Inventory i on mod.iInventoryId = i.iAutoId
left join bd_uom u1 on u1.iAutoId = i.iInventoryUomId1
where mod.iManualOrderMid = #(imanualordermid)
#end