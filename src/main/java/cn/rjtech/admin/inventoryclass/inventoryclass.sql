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

#sql("getRouingConfigs")
SELECT
    a.*,
    b.name as itypename,
    c.name as cproductsnname,
    d.name as cproducttechsnname,
    i.cInvName1 rsinventoryname
FROM
    Bd_InventoryRoutingConfig a
        LEFT JOIN #(getBaseDbName()).dbo.jb_dictionary b ON a.iType = b.sn AND b.type_key = 'process_type'
        LEFT JOIN #(getBaseDbName()).dbo.jb_dictionary c ON a.cProductSn = c.sn AND c.type_key = 'cproductsn_type'
        LEFT JOIN #(getBaseDbName()).dbo.jb_dictionary d ON a.cProductTechSn = d.sn AND d.type_key = 'product_tech'
        left join Bd_Inventory i on a.irsinventoryid = i.iAutoId
WHERE a.isEnabled = 1 AND
        a.iInventoryRoutingId = #para(iinventoryroutingid)
ORDER BY a.iSeq ASC
#end

#sql("getRouings")
SELECT
    ir.*
FROM Bd_InventoryRouting ir
WHERE ir.iinventoryid = #para(iinventoryid)
#end