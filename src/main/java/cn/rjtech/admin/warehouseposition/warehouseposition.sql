#sql("paginateAdminDatas")
SELECT
	wp.*,
	wh.cWhCode,
	wh.cWhName,
	wa.cAreaCode,
	wa.cAreaName,
	ws.cShelvesCode,
	ws.cShelvesName
FROM Bd_Warehouse_Position wp
	LEFT JOIN Bd_Warehouse wh ON wh.iAutoId = wp.iWarehouseId
	LEFT JOIN Bd_Warehouse_Area wa ON wa.iAutoId = wp.iWarehouseAreaId
	LEFT JOIN Bd_Warehouse_Shelves ws ON ws.iAutoId = wp.iWarehouseShelvesId
WHERE wp.isDeleted = 0
    #if(iautoid)
        AND wp.iAutoid = #para(iautoid)
    #end
    #if(cpositioncode)
        AND wp.cPositionCode LIKE CONCAT('%', #para(cpositioncode), '%')
    #end
    #if(cpositionname)
        AND wp.cPositionName LIKE CONCAT('%', #para(cpositionname), '%')
    #end
    #if(iwarehouseid)
        AND wp.iWarehouseId = #para(iwarehouseid)
    #end
    #if(isenabled)
        AND wp.isEnabled = #para(isenabled)
    #end
    #if(ids)
        AND CHARINDEX(','+cast((select wp.iAutoId) as nvarchar(20))+',' , ','+#para(ids)+',') > 0
    #end

ORDER BY wp.cPositionCode DESC
#end

#sql("findByMouldsId")
SELECT wp.*
FROM (
        SELECT iWarehousePositionId
        FROM Bd_MouldsRecord
        WHERE isDeleted = '0' AND iType = 1 AND iMouldsId = #para(iMouldsId)
        GROUP BY iWarehousePositionId
    ) whr
    LEFT JOIN Bd_Warehouse_Position wp ON whr.iWarehousePositionId = wp.iAutoId
    WHERE wp.isDeleted = '0' AND wp.isEnabled = '0'
ORDER BY wp.dCreateTime
#end

###打印
#sql("printwarehouseposition")
SELECT
    wp.*,
    wh.cWhCode,
  (ISNULL(wh.cWhCode, '')+'|'+ISNULL(wa.cAreaCode, '')+'|'+ISNULL(ws.cShelvesCode,'')+'|'+ISNULL(wp.cPositionCode,'')) cbarcode,
  (ISNULL(wh.cWhName,'')+'|'+ISNULL(wa.cAreaName,'')+'|'+ISNULL(ws.cShelvesName,'')+ISNULL(wp.cPositionName,'')) cbarname,
    wa.cAreaCode,
    wa.cAreaName,
    ws.cShelvesCode,
    ws.cShelvesName
FROM
    Bd_Warehouse_Position wp
LEFT JOIN Bd_Warehouse wh ON wh.iAutoId = wp.iWarehouseId
LEFT JOIN Bd_Warehouse_Area wa ON wa.iAutoId = wp.iWarehouseAreaId
LEFT JOIN Bd_Warehouse_Shelves ws ON ws.iAutoId = wp.iWarehouseShelvesId
WHERE
    1 = 1
AND wp.isDeleted = '0'
AND wp.iautoid IN #(str)
ORDER BY wp.dCreateTime DESC
#end

#sql("verifyDuplication")
SELECT ISNULL(COUNT(iAutoId), 0) FROM Bd_Warehouse_Position
WHERE isDeleted=0
#if(cpositioncode)
    AND cPositionCode = #para(cpositioncode)
#end
#if(cpositionname)
    AND cPositionName = #para(cpositionname)
#end
#if(iwarehouseid)
    AND iWarehouseId = #(iwarehouseid)
#end
#if(iwarehouseareaid)
    AND iWarehouseAreaId = #(iwarehouseareaid)
#end
#if(iwarehouseshelvesid)
    AND iWarehouseShelvesId = #(iwarehouseshelvesid)
#end
#if(iautoid)
    AND iAutoId != #(iautoid)
#end
#end