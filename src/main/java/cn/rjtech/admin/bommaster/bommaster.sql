#sql("datas")
SELECT DISTINCT * FROM (SELECT
	master.iAutoId AS id,
	master.cBomVersion,
	minv.cInvCode,
	minv.cInvName,
	minv.cInvStd,
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
	AND master.dEnableDate <= GETDATE()
	AND master.dDisableDate >= GETDATE()
UNION ALL
SELECT
	bbc.iAutoId AS id,
	a.cBomVersion,
	minv.cInvCode,
	minv.cInvName,
	minv.cInvStd,
	uom2.cUomName,
	bbc.iQty,
	bbc.iWeight,
	a.iOrgId,
	bbc.iPid pid
FROM
	Bd_BomCompare bbc
	INNER JOIN Bd_Inventory minv ON minv.iAutoId = bbc.iInventoryId
	LEFT JOIN Bd_Uom uom2 ON uom2.iAutoId = minv.iUomClassId
	INNER JOIN Bd_BomMaster a ON a.iAutoId = bbc.iBOMMasterId AND a.dEnableDate <= GETDATE() AND a.dDisableDate >= GETDATE()
WHERE
	bbc.IsDeleted = '0'
	AND a.IsDeleted = '0'
	AND a.isEnabled = '1'
	  ) a
	   WHERE 1 = 1
	  #if(orgId)
	    AND a.iOrgId = #para(orgId)
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

#sql("test")
WITH a ( iAutoId, pid ) AS (
	SELECT
		b1.iAutoId ,
		b1.iPid AS pid
	FROM
		Bd_BomCompare AS b1  WHERE b1.iPid = 1642802976143011841 OR b1.iAutoId = 1642802976143011841
	UNION ALL
	SELECT
		b2.iAutoId ,
		b2.iPid AS pid
	FROM
		Bd_BomCompare AS b2
		INNER JOIN a ON b2.iPid  = a.iAutoId
	)
	SELECT DISTINCT
	inv.cInvCode,b.iautoId
FROM
	a
	INNER JOIN Bd_BomCompare b ON a.iAutoId = b.iAutoId
	INNER JOIN Bd_Inventory inv ON inv.iAutoId = b.iInventoryId
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