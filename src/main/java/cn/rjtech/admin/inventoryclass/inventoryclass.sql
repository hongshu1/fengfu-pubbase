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
#if(cInvName1)
  AND i.cInvName1 like CONCAT('%', #para(cInvName1), '%')
#end
#if(cInvCode)
  AND i.cInvCode = #para(cInvCode)
#end
#if(cInvAddCode)
  AND i.cInvAddCode = #para(cInvAddCode)
#end
#if(sqlids)
  AND i.iAutoId in (#(sqlids))
#end
#end


#sql("workRegions")
select
 iw.*,wr.cWorkName cworkname,wr.cWorkCode cworkcode,p.cPsn_Name cpersonname,
 d.cDepName cdepname,jd.name defaultname
from Bd_InventoryWorkRegion iw
inner join Bd_WorkRegionM wr on iw.iWorkRegionMid = wr.iAutoId
left join Bd_Person p on wr.iPersonId = p.iAutoId
left join Bd_Department d on iw.iDepId = d.iAutoId
left join #(getBaseDbName()).dbo.jb_dictionary jd on iw.isDefault + 1 = jd.sort_rank and jd.type_key = 'options_boolean'
where iw.iInventoryId = #para(iInventoryId)
#end

#sql("inventorySpotCheckList")
SELECT
    i.*,i.iautoid as inventoryiautoid,ic.cInvCName cinvcname,eq.*,
    eq.iautoid equipiautoid,
    uom.iautoid uomiautoid,
    uom.iUomClassId,
    uom.cUomCode,
    uom.cUomName
FROM Bd_Inventory i
    inner join Bd_InventoryClass ic on i.iInventoryClassId = ic.iautoid
    left join Bd_EquipmentModel eq on i.iEquipmentModelId = eq.iautoid
    left join Bd_Uom uom on i.iInventoryUomId1 = uom.iautoid
WHERE 1=1
#if(iInventoryClassId)
    AND i.iInventoryClassId = #para(iInventoryClassId)
#end
#if(isEnabled)
    AND i.isenabled = '1'
#end
#if(cInvCode1)
    AND i.cInvCode1 = #para(cInvCode1)
#end
#if(cInvName)
    AND i.cInvName like CONCAT('%', #para(cInvName), '%')
#end
#if(cInvName1)
    AND i.cInvName1 like CONCAT('%', #para(cInvName1), '%')
#end
#if(cInvCode)
    AND i.cInvCode = #para(cInvCode)
#end
#if(cInvAddCode)
    AND i.cInvAddCode = #para(cInvAddCode)
#end
#if(sqlids)
    AND i.iAutoId in (#(sqlids))
#end
#end