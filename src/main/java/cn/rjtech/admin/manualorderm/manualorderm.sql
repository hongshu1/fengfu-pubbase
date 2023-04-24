#sql("list")
select mom.*,d.name statename,c.cCusName from Co_ManualOrderM mom
 inner join Bd_Customer c on c.iAutoId = mom.iCustomerId
 inner join #(getBaseDbName()).dbo.jb_dictionary d on d.sn = mom.iOrderStatus and d.type_key = 'manual_order_state'
 where mom.IsDeleted = '0'

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
and mom.dCreateTime >= #para(starttime)
#end
#if(endtime)
and mom.dCreateTime <= #para(endtime)
#end
#if(sqlids)
and mom.iAutoId in (#(sqlids))
#end
order by mom.dCreateTime desc
#end

#sql("manualorderds")
select md.*,i.cInvCode,i.cInvCode1,i.cInvName1,i.cInvStd,u1.cUomName
from Co_ManualOrderD md
left join Bd_Inventory i on md.iInventoryId = i.iAutoId
left join bd_uom u1 on u1.iAutoId = i.iInventoryUomId1
where md.iManualOrderMid = #para(imanualordermid)
#end

#sql("getDatasByIds")
select mom.* from Co_ManualOrderM mom
 where mom.IsDeleted = '0'
 #if(sqlids)
and mom.iAutoId in (#(sqlids))
#end
#end