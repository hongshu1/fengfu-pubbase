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
   i.*,ic.cInvCName cinvcname,uom.cUomName
FROM Bd_Inventory i
inner join Bd_InventoryClass ic on i.iInventoryClassId = ic.iautoid
left join Bd_Uom uom on i.iInventoryUomId1 = uom.iautoid
WHERE
    i.isDeleted = '0'
#if(iInventoryClassId)
 AND i.iInventoryClassId = #para(iInventoryClassId)
#end
#if(iInventoryClassCode)
 AND ic.cInvCCode like CONCAT(#para(iInventoryClassCode), '%')
#end
#if(isEnabled)
 AND i.isenabled = #para(isEnabled)
#end
#if(cInvCode1)
 AND i.cInvCode1 like CONCAT('%', #para(cInvCode1), '%')
#end
#if(cInvName)
 AND i.cInvName like CONCAT('%', #para(cInvName), '%')
#end
#if(cInvCode)
 AND i.cInvCode like CONCAT('%', #para(cInvCode), '%')
#end
#if(sqlids)
 AND i.iAutoId in (#(sqlids))
#end
#if(invId)
AND i.iAutoId = #para(invId)
#end
order by i.dUpdateTime desc
#end

#sql("workRegions")
SELECT
	iw.*,
	wr.cWorkName cworkname,
	wr.cWorkCode cworkcode,
	p.cPsn_Name cpersonname,
	d.cDepName cdepname,
	( CASE WHEN iw.isDefault = 1 THEN '是' ELSE '否' END ) defaultname
FROM
	Bd_InventoryWorkRegion iw
	INNER JOIN Bd_WorkRegionM wr ON iw.iWorkRegionMid = wr.iAutoId
	LEFT JOIN Bd_Person p ON wr.iPersonId = p.iAutoId
	LEFT JOIN Bd_Department d ON iw.iDepId = d.iAutoId
where iw.iInventoryId = #para(iInventoryId)
AND iw.isDeleted = '0'
#end

#sql("getRouingConfigs")
SELECT
    a.*,
    b.name as itypename,
    c.name as cproductsnname,
    d.name as cproducttechsnname,
    ri.invcs,rs.drawings,re.equipments
FROM
    Bd_InventoryRoutingConfig a
        LEFT JOIN #(getBaseDbName()).dbo.jb_dictionary b ON a.iType = b.sn AND b.type_key = 'process_type'
        LEFT JOIN #(getBaseDbName()).dbo.jb_dictionary c ON a.cProductSn = c.sn AND c.type_key = 'cproductsn_type'
        LEFT JOIN #(getBaseDbName()).dbo.jb_dictionary d ON a.cProductTechSn = d.sn AND d.type_key = 'product_tech'
        left join (SELECT COUNT(iInventoryRoutingConfigId) invcs ,iInventoryRoutingConfigId configid FROM Bd_InventoryRoutingInvc GROUP BY iInventoryRoutingConfigId) ri on a.iAutoId = ri.configid
        left join (SELECT COUNT(iInventoryRoutingConfigId) drawings ,iInventoryRoutingConfigId configid FROM Bd_InventoryRoutingSop GROUP BY iInventoryRoutingConfigId) rs on a.iAutoId = rs.configid
        left join (SELECT COUNT(iInventoryRoutingConfigId) equipments ,iInventoryRoutingConfigId configid FROM Bd_InventoryRoutingEquipment GROUP BY iInventoryRoutingConfigId) re on a.iAutoId = re.configid
WHERE a.isEnabled = 1 AND
        a.iInventoryRoutingId = #para(iinventoryroutingid)
        #if(ispotcheckformid)
         and a.iSpotCheckFormId=#para(ispotcheckformid)
        #end
         #if(cspotcheckformname)
         and t2.cSpotCheckFormName=#para(cspotcheckformname)
        #end
ORDER BY a.iSeq ASC
#end

#sql("getRouings")
SELECT
    ir.*
FROM Bd_InventoryRouting ir
WHERE ir.iinventoryid = #para(iinventoryid) AND ir.isDeleted = '0'
#end

#sql("inventorySpotCheckList")
SELECT
    i.iInventoryClassId,i.iautoid as inventoryiautoid,i.cInvCode,
    i.cInvName,i.cInvCode1,i.cInvAddCode,i.cInvAddCode1,i.cInvName1,i.cInvStd,
    ic.cInvCName cinvcname,
    eq.cEquipmentModelCode,eq.cEquipmentModelName,
    eq.iautoid equipiautoid,
    uom.iautoid uomiautoid,
    uom.iUomClassId,
    uom.cUomCode,
    uom.cUomName
FROM Bd_Inventory i
    left join Bd_InventoryClass ic on i.iInventoryClassId = ic.iautoid
    left join Bd_EquipmentModel eq on i.iEquipmentModelId = eq.iautoid
    left join Bd_Uom uom on i.iInventoryUomId1 = uom.iautoid
WHERE 1=1 AND i.isenabled = '1'
#if(iInventoryClassId)
    AND i.iInventoryClassId = #para(iInventoryClassId)
#end
#if(cinvcode1)
    AND i.cInvCode1 like CONCAT('%', #para(cinvcode1), '%')
#end
#if(cinvname)
    AND i.cInvName like CONCAT('%', #para(cinvname), '%')
#end
#if(cInvName1)
    AND i.cInvName1 like CONCAT('%', #para(cInvName1), '%')
#end
#if(cinvcode)
    AND i.cInvCode like CONCAT('%', #para(cinvcode), '%')
#end
#if(cinvaddcode)
    AND i.cInvAddCode like CONCAT('%', #para(cinvaddcode), '%')
#end
#if(sqlids)
    AND i.iAutoId in (#(sqlids))
#end
#end

#sql("getSubList")
SELECT *
FROM bd_InventoryClass
WHERE 1=1
#if(iorgid)
	and iorgid = #para(iorgid)
#end
#if(pid > 0)
	and ipid = #para(pid)
#else
	and (ipid is null or ipid = 0)
#end
ORDER BY
    cInvCCode
#end
