#sql("list")
SELECT
   *
FROM Bd_InventoryClass
WHERE 1=1
#if(ipid)
 AND iautoid != #para(ipid)
#end
#end

#sql("inventoryList")
SELECT
   i.*,ic.cInvCName cinvcname
FROM Bd_Inventory i
inner join Bd_InventoryClass ic on i.iInventoryClassId = ic.iautoid
WHERE 1=1
#if(iInventoryClassId)
 AND i.iInventoryClassId = #para(iInventoryClassId)
#end
#if(isEnabled)
 AND i.isenabled = #para(isEnabled)
#end
#if(cInvCode1)
 AND i.cInvCode1 = #para(cInvCode1)
#end
#if(cInvName)
 AND i.cInvName like CONCAT('%', #para(cInvName), '%')
#end
#if(cInvCode)
 AND i.cInvCode = #para(cInvCode)
#end
#if(sqlids)
 AND i.iAutoId in (#(sqlids))
#end
#end