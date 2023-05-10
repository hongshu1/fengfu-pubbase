#sql("pageList")
SELECT
	qc.cQcFormName,
	inv.cInvCode,
	inv.cInvCode1,
	inv.cInvName,
	inv.cInvName1,
	inv.cInvStd,
	uom.cUomName,
	t2.cEquipmentModelName as machineName,
	t1.*
FROM
	Bd_InventoryQcForm t1
	INNER JOIN Bd_QcForm qc ON qc.iAutoId = t1.iQcFormId
	INNER JOIN Bd_Inventory inv on inv.iAutoId = t1.iInventoryId
	LEFT JOIN Bd_Uom uom ON uom.iAutoId = inv.iUomClassId
	left join Bd_EquipmentModel t2 on inv.iEquipmentModelId = t2.iAutoId
WHERE
	 t1.IsDeleted = '0'
    #if(keywords)
        and qc.cQcFormName like '%#(keywords)%'
    #end
    #if(item)
         and (inv.cInvCode like CONCAT('%', #para(item), '%') or inv.cInvName like CONCAT('%', #para(item), '%'))
    #end

    #if(cInvCode1)
        and inv.cInvCode1 like '%#(cInvCode1)%'
    #end
    #if(cInvName1)
         and inv.cInvName1 like '%#(cInvName1)%'
    #end
    #if(iQcFormId)
         and t1.iQcFormId = '#(iQcFormId)'
    #end
    #if(isAdd)
        and t1.iQcFormId = ' '
    #end
        order by t1.dUpdateTime desc
    #end

#sql("listData")
select * from Bd_InventoryQcForm t1 where t1.IsDeleted = '0'
#if(iQcFormId)
 and t1.iQcFormId = '#(iQcFormId)'
#else
 and t1.iQcFormId = ' '
#end
#if(keywords)
 and (t1.iInventoryCode like '#(keywords)%' or t1.iInventoryName like '%#(keywords)%')
#end
#if(CustomerManager)
 and t1.CustomerManager like '%#(CustomerManager)%'
#end
#if(componentName)
 and t1.componentName like '%#(componentName)%'
#end
#if(machineType)
 and t1.machineType like '%#(machineType)%'
#end
#end

#sql("resourceList")
SELECT
    t1.iAutoId as iInventoryId,
    t1.cInvCode as cInvCode,
    t1.cInvName as cInvName,
    t1.cInvName1 as cInvName1,
    t1.cInvCode1 as cInvCode1,
    t1.cInvStd as cInvStd,
    t3.cUomName as cUomName,
    t2.cEquipmentModelName as machineName
    from
    Bd_Inventory t1
    left join Bd_EquipmentModel t2 on t1.iEquipmentModelId = t2.iAutoId
    left join Bd_Uom t3 on t1.iInventoryUomId1 = t3.iAutoId
    where t1.isDeleted = '0' and not exists (select 1 from Bd_InventoryQcForm t2 where t2.iInventoryId = t1.iAutoId
    and t2.IsDeleted = '0')
#if(orgCode)
   and t1.cOrgCode = #(orgCode)
#end
#if(itemHidden)
    and t1.iAutoId not in (#(itemHidden))
#end
#if(keywords)
    and (t1.cInvCode like CONCAT('%', #para(keywords), '%') or t1.cInvName like CONCAT('%', #para(keywords), '%'))
#end
#if(cInvCode1)
    and t1.cInvCode1 like CONCAT('%',#para(cInvCode1),'%' )
#end
#if(cInvName1)
    and t1.cInvName1 like CONCAT('%', #para(cInvName1), '%')
#end
#end

#sql("getFormList")
select
 iAutoId as iQcFormId,
 cQcFormName as iQcFormName
 from Bd_QcForm t1 where 1=1 and isDeleted = '0'
 and not exists (select 1 from Bd_InventoryQcForm t2 where t1.iAutoId = t2.iQcFormId)
#if(keywords)
and t1.cQcFormName like '%#(keywords)%'
#end
#end

#sql("findDicBySn")
select * from #(getBaseDbName()).dbo.jb_dictionary where type_key = 'inspection_type' and sn = '#(sn)'
#end
