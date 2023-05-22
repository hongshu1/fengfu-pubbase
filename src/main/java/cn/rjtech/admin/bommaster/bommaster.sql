#sql("datas")
SELECT * FROM (SELECT
	master.iAutoId AS id,
	master.cBomVersion,
	master.iInventoryId,
	minv.cInvCode,
	minv.cInvName,
	minv.cInvStd,
	minv.cInvCode1,
	minv.cInvAddCode1,
	minv.cInvName1,
	minv.cInvName2,
	uom.cUomName,
    NULL iQty,
    minv.iweight,
    master.iOrgId,
	NULL pid
FROM
	Bd_BomMaster master
	INNER JOIN Bd_Inventory minv ON minv.iAutoId = master.iInventoryId
	LEFT JOIN Bd_Uom uom ON uom.iAutoId = minv.iUomClassId
WHERE
	master.IsDeleted = '0'
	AND master.isEnabled = '1'
	AND CONVERT(DATE, master.dEnableDate) <= CONVERT(DATE,  GETDATE())
	AND CONVERT(DATE, master.dDisableDate) >= CONVERT(DATE,  GETDATE())
	AND master.iAuditStatus = 2
	AND master.isEffective = 1
UNION ALL
SELECT
	bbc.iAutoId AS id,
	a.cBomVersion,
	bbc.iInventoryId,
	minv.cInvCode,
	minv.cInvName,
	minv.cInvStd,
	minv.cInvCode1,
	minv.cInvAddCode1,
	minv.cInvName1,
	minv.cInvName2,
	uom2.cUomName,
	bbc.iQty,
	bbc.iWeight,
	a.iOrgId,
	bbc.iPid pid
FROM
	Bd_BomCompare bbc
	INNER JOIN Bd_Inventory minv ON minv.iAutoId = bbc.iInventoryId
	LEFT JOIN Bd_Uom uom2 ON uom2.iAutoId = minv.iUomClassId
	INNER JOIN Bd_BomMaster a ON a.iAutoId = bbc.iBOMMasterId AND CONVERT(DATE, a.dEnableDate) <= CONVERT(DATE,  GETDATE()) AND CONVERT(DATE, a.dDisableDate) >= CONVERT(DATE,  GETDATE())
WHERE
	bbc.IsDeleted = '0'
	AND a.IsDeleted = '0'
	AND a.isEnabled = '1'
	AND a.iAuditStatus = 2
	AND a.isEffective = 1
	  ) a
	   WHERE 1 = 1
	  #if(orgId)
	    AND a.iOrgId = #para(orgId)
	  #end

	  #if(invId)
        AND a.iInventoryId = #para(invId)
      #end

	  #if(pid)
        AND (a.id = #para(pid) OR a.pid = #para(pid))
	  #end
	  #if(keyWords)
        AND (
           (a.cInvCode LIKE CONCAT('%',#para(keyWords),'%')) OR ( a.cInvName LIKE CONCAT('%',#para(keyWords),'%') )
          OR (a.cInvCode1 LIKE CONCAT('%',#para(keyWords),'%')) OR ( a.cInvAddCode1 LIKE CONCAT('%',#para(keyWords),'%') )
          OR (a.cInvName1 LIKE CONCAT('%',#para(keyWords),'%')) OR ( a.cInvName2 LIKE CONCAT('%',#para(keyWords),'%') )
        )
	  #end
#end

#sql("getVersionRecord")
SELECT
	master.iAutoId AS id,
	master.cBomVersion,
	master.dEnableDate,
	master.dDisableDate,
	master.iAuditStatus,
	master.cCreateName,
	master.dCreateTime,
	minv.cInvCode,
	minv.cInvName,
	minv.cInvStd,
	minv.cInvCode1,
	minv.cInvAddCode1,
	minv.cInvName1,
	minv.cInvName2,
	uom.cUomName
FROM
	Bd_BomMaster master
	INNER JOIN Bd_Inventory minv ON minv.iAutoId = master.iInventoryId
	LEFT JOIN Bd_Uom uom ON uom.iAutoId = minv.iUomClassId
WHERE
	master.IsDeleted = '0'
	AND master.isEnabled = '1'
	#if(orgId)
	AND  master.iOrgId = #para(orgId)
	#end
	#if(cInvCode)
	    AND minv.cInvCode LIKE CONCAT('%',#para(cInvCode),'%')
	#end
	#if(cInvCode1)
	    AND minv.minv.cInvCode1 LIKE CONCAT('%',#para(cInvCode1),'%')
	#end
	#if(cInvName1)
	    AND minv.minv.cInvName1 LIKE CONCAT('%',#para(cInvName1),'%')
	#end
	ORDER BY master.dCreateTime ASC
#end

#sql("queryBomMasterId")
SELECT a.id FROM (SELECT
	master.iAutoId AS id,
	minv.cInvCode,
	minv.cInvName,
	minv.cInvStd,
	minv.cInvCode1,
	minv.cInvAddCode1,
	minv.cInvName1,
	minv.cInvName2,
    master.iOrgId
FROM
	Bd_BomMaster master
	INNER JOIN Bd_Inventory minv ON minv.iAutoId = master.iInventoryId
	LEFT JOIN Bd_Uom uom ON uom.iAutoId = minv.iUomClassId
WHERE
	master.IsDeleted = '0'
	AND master.isEnabled = '1'
	AND CONVERT(DATE, master.dEnableDate) <= CONVERT(DATE,  GETDATE())
	AND CONVERT(DATE, master.dDisableDate) >= CONVERT(DATE,  GETDATE())
UNION ALL
SELECT
	bbc.iBOMMasterId AS id,
	minv.cInvCode,
	minv.cInvName,
	minv.cInvStd,
	minv.cInvCode1,
	minv.cInvAddCode1,
	minv.cInvName1,
	minv.cInvName2,
	a.iOrgId
FROM
	Bd_BomCompare bbc
	INNER JOIN Bd_Inventory minv ON minv.iAutoId = bbc.iInventoryId
	INNER JOIN Bd_BomMaster a ON a.iAutoId = bbc.iBOMMasterId AND CONVERT(DATE, a.dEnableDate) <= CONVERT(DATE,  GETDATE()) AND CONVERT(DATE, a.dDisableDate) >= CONVERT(DATE,  GETDATE())
WHERE
	bbc.IsDeleted = '0'
	AND a.IsDeleted = '0'
	AND a.isEnabled = '1'
	  ) a
	   WHERE 1 = 1
	  #if(orgId)
	    AND a.iOrgId = #para(orgId)
	  #end
	  #if(keyWords)
        AND (
           (a.cInvCode LIKE CONCAT('%',#para(keyWords),'%')) OR ( a.cInvName LIKE CONCAT('%',#para(keyWords),'%') )
          OR (a.cInvCode1 LIKE CONCAT('%',#para(keyWords),'%')) OR ( a.cInvAddCode1 LIKE CONCAT('%',#para(keyWords),'%') )
          OR (a.cInvName1 LIKE CONCAT('%',#para(keyWords),'%')) OR ( a.cInvName2 LIKE CONCAT('%',#para(keyWords),'%') )
        )
	  #end
	  group by a.id
#end

#sql("getBomMasterByInvId")
SELECT
	master.iAutoId AS id,
	master.cBomVersion,
	minv.cInvCode,
	minv.cInvName,
	minv.cInvStd,
	minv.cInvCode1,
	minv.cInvAddCode1,
	minv.cInvName1,
	minv.cInvName2,
	uom.cUomName,
    NULL iQty,
    minv.iweight,
    master.iOrgId,
	NULL pid
FROM
	Bd_BomMaster master
	INNER JOIN Bd_Inventory minv ON minv.iAutoId = master.iInventoryId
	LEFT JOIN Bd_Uom uom ON uom.iAutoId = minv.iUomClassId
WHERE
	master.IsDeleted = '0'
	AND master.isEnabled = '1'
	AND CONVERT(DATE, master.dEnableDate) <= CONVERT(DATE,  GETDATE())
	AND CONVERT(DATE, master.dDisableDate) >= CONVERT(DATE,  GETDATE())
	AND master.iAuditStatus = 2
	AND master.isEffective = 1
	#if(orgId)
	    AND master.iOrgId = #para(orgId)
	#end
    #if(invId)
        AND master.iInventoryId = #para(invId)
    #end
#end

#sql("findBomCompareByBomMasterInvId")
SELECT
	bom.masterInvId,
	ic.cInvCName cinvcname,
	manuuom.cUomName manufactureuom,
	purUom.cUomName purchaseuom,
	inv.*
FROM
	Bd_Inventory inv
	INNER JOIN Bd_InventoryClass ic ON ic.iautoid = inv.iInventoryClassId
	LEFT JOIN Bd_Uom manuuom ON manuuom.iAutoId = inv.iManufactureUomId
	LEFT JOIN Bd_Uom puruom ON puruom.iAutoId = inv.iPurchaseUomId
	INNER JOIN V_Bd_BomMasterInvId bom ON bom.iInventoryId = inv.iAutoId
	WHERE
	    1 = 1
	#if(orgId)
	    AND bom.iOrgId = #para(orgId)
	#end

    #if(isAdd)
         #if(invId)
            AND bom.masterInvId = #para(invId)
        #else
            AND bom.masterInvId = ''
	    #end
    #end
	#if(masterInvId)
       AND bom.masterInvId = #para(masterInvId) OR inv.iAutoId = #para(masterInvId)
	#end
	#if(cInvCode1)
         AND inv.cInvCode1 like CONCAT('%', #para(cInvCode1), '%')
    #end
    #if(cInvName)
        AND inv.cInvName like CONCAT('%', #para(cInvName), '%')
    #end
    #if(cInvCode)
        AND inv.cInvCode like CONCAT('%', #para(cInvCode), '%')
    #end

#end

#sql("")
SELECT
	bom.masterInvId,
	ic.cInvCName cinvcname,
	manuuom.cUomName manufactureuom,
	purUom.cUomName purchaseuom,
	inv.*
FROM
	Bd_Inventory inv
	INNER JOIN Bd_InventoryClass ic ON ic.iautoid = inv.iInventoryClassId
	LEFT JOIN Bd_Uom manuuom ON manuuom.iAutoId = inv.iManufactureUomId
	LEFT JOIN Bd_Uom puruom ON puruom.iAutoId = inv.iPurchaseUomId
	INNER JOIN V_Bd_BomMasterInvId bom ON bom.iInventoryId = inv.iAutoId
WHERE
	bom.ipid = 451617187998654

	#if(invId)
        AND a.iInventoryId = #para(invId)
    #else
        AND a.iInventoryId = ''
	#end

	#if(orgId)
	    AND a.iOrgId = #para(orgId)
	#end
	#if(cInvCode1)
         AND minv.cInvCode1 like CONCAT('%', #para(cInvCode1), '%')
    #end
    #if(cInvName)
        AND minv.cInvName like CONCAT('%', #para(cInvName), '%')
    #end
    #if(cInvCode)
        AND minv.cInvCode like CONCAT('%', #para(cInvCode), '%')
    #end
#end

#sql("getMaster")
SELECT DISTINCT
    birc.iSeq,--工序号
    birc.cOperationName,--工序名称
    birc.iType,--工序类型
    birc.cMergedSeq,--合并工序
    birc.cProductSn,--生产方式
    birc.iMergedNum,--合并要员Bd_Customer
    birc.iMergeRate,--要员
    birc.iMergeSecs,--合并工时
    birc.iSecs,--单工时
    bi.cInvName,--成品，半成品名称(包括成品/半成品)
    birc.iInventoryId,--原料集子id
    STUFF(
            (
                SELECT
                        ', ' + be.cEquipmentName
                FROM
                    Bd_InventoryRoutingConfig birc2
                        left JOIN Bd_InventoryRoutingEquipment bire ON birc2.iAutoId = bire.iInventoryRoutingConfigId
                        left JOIN Bd_Equipment be ON be.iAutoId = bire.iEquipmentId
                WHERE
                        birc2.iAutoId = birc.iAutoId FOR XML PATH ( '' )
        ),
		1,
		2,
		''
	) AS cEquipmentName --设备名

FROM
    Bd_Inventory bi --	LEFT JOIN Bd_InventoryRoutingConfig birc ON bi.iAutoId = birc.iRsInventoryId --筛选半成品/成品(每个存货有一个配置表)
        LEFT JOIN Bd_InventoryRouting bir ON bi.iAutoId = bir.iInventoryId --每个存货有一条工艺路线
        LEFT JOIN (
        SELECT
            birc.iSeq,--工序号
            birc.cOperationName,--工序名称
            birc.iType,--工序类型
            birc.cMergedSeq,--合并工序
            birc.cProductSn,--生产方式
            birc.iMergedNum,--合并要员Bd_Customer
            birc.iMergeRate,--要员
            birc.iMergeSecs,--合并工时
            birc.iSecs,--单工时
            be.cEquipmentName,
            birc.iRsInventoryId,
            biri.iInventoryId,
            birc.iAutoId
        FROM
            Bd_InventoryRoutingConfig birc
                LEFT JOIN Bd_InventoryRoutingEquipment bire ON birc.iAutoId = bire.iInventoryRoutingConfigId
                LEFT JOIN Bd_Equipment be ON be.iAutoId = bire.iEquipmentId
                LEFT JOIN Bd_InventoryRoutingInvc biri ON birc.iAutoId = biri.iInventoryRoutingConfigId
    ) birc ON bi.iAutoId = birc.iRsInventoryId
WHERE
        bir.isEnabled = 1 and
        CONVERT ( VARCHAR, GETDATE( ), 23 ) > bir.dToDate
#end

#sql("getCompare")
SELECT
    bi.iAutoId,
    bi.cInvName
FROM
    Bd_Inventory bi
        LEFT JOIN Bd_BomCompare bbc ON bi.iAutoId = bbc.iInventoryId
#end
