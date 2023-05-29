#sql("list")
SELECT t1.*,t2.iautoid spotcheckformiautoid,t2.cSpotCheckFormName,
       t3.iautoid inventoryiautoid,t3.cInvCode,t3.cInvName,t3.cInvCode1,t3.cInvAddCode,t3.cInvName1,
       t3.iEquipmentModelId,t3.cInvStd,t3.iUomClassId,t3.iInventoryUomId1,t3.iInventoryUomClassId2,
       t4.iautoid equipiautoid,t4.cEquipmentModelCode,t4.cEquipmentModelName,t5.cUomCode,t5.cUomName
FROM Bd_InventorySpotCheckForm t1
    LEFT JOIN Bd_SpotCheckForm t2 on t1.iSpotCheckFormId = t2.iAutoId
    LEFT JOIN Bd_Inventory t3 on  t1.iInventoryId = t3.iAutoId
    LEFT JOIN Bd_EquipmentModel t4 on t3.iEquipmentModelId = t4.iAutoId
    LEFT JOIN Bd_Uom t5 on t3.iInventoryUomId1 = t5.iAutoId
WHERE t1.IsDeleted='0'
#if(iautoid)
  and t1.iautoid = #para(iautoid)
#end
#if(itype)
  and t1.itype = #para(itype)
#end
#if(cspotcheckformname)
  and t2.cspotcheckformname = #para(cspotcheckformname)
#end
#if(cinvcode1)
  and t3.cinvcode1 = #para(cinvcode1)
#end
#if(cinvaddcode)
  and t3.cinvaddcode = #para(cinvaddcode)
#end
#if(cinvname1)
  and t3.cinvname1 = #para(cinvname1)
#end
#if(iequipmentmodelid)
  and t3.iequipmentmodelid = #para(iequipmentmodelid)
#end
    ORDER BY t1.dUpdateTime DESC
#end


#sql("exportList")
SELECT t1.*, t2.iautoid as spotcheckformiautoid, t2.cSpotCheckFormName,
            t3.iautoid as inventoryiautoid, t3.cInvCode, t3.cInvName, t3.cInvCode1, t3.cInvAddCode,
            t3.cInvName1, t3.iEquipmentModelId, t3.cInvStd, t3.iUomClassId, t3.iInventoryUomId1, 
            t3.iInventoryUomClassId2, t4.cEquipmentModelCode, t4.cEquipmentModelName,t5.cUomCode,t5.cUomName
            FROM Bd_InventorySpotCheckForm t1
            LEFT JOIN Bd_SpotCheckForm t2 on t1.iSpotCheckFormId = t2.iAutoId
            LEFT JOIN Bd_Inventory t3 on  t1.iInventoryId = t3.iAutoId
            LEFT JOIN Bd_EquipmentModel t4 on t3.iEquipmentModelId = t4.iAutoId 
            LEFT JOIN Bd_Uom t5 on t3.iInventoryUomId1 = t5.iAutoId
            WHERE t1.IsDeleted='0'
              #if(iautoid)
              and t1.iautoid= #para(iautoid)
              #end
            ORDER BY t1.dUpdateTime DESC
#end


#sql("exportList")
SELECT t1.*, t2.iautoid as spotcheckformiautoid, t2.cSpotCheckFormName,
            t3.iautoid as inventoryiautoid, t3.cInvCode, t3.cInvName, t3.cInvCode1, t3.cInvAddCode,
            t3.cInvName1, t3.iEquipmentModelId, t3.cInvStd, t3.iUomClassId, t3.iInventoryUomId1, 
            t3.iInventoryUomClassId2, t4.cEquipmentModelCode, t4.cEquipmentModelName,t5.cUomCode,t5.cUomName
            FROM Bd_InventorySpotCheckForm t1
            LEFT JOIN Bd_SpotCheckForm t2 on t1.iSpotCheckFormId = t2.iAutoId
            LEFT JOIN Bd_Inventory t3 on  t1.iInventoryId = t3.iAutoId
            LEFT JOIN Bd_EquipmentModel t4 on t3.iEquipmentModelId = t4.iAutoId 
            LEFT JOIN Bd_Uom t5 on t3.iInventoryUomId1 = t5.iAutoId
            WHERE t1.IsDeleted='0'
              #if(iautoid)
              and t1.iautoid= #para(iautoid)
              #end
            ORDER BY t1.dUpdateTime DESC
#end