#sql("paginateAdminDatas")
SELECT
	ws.*,
	wh.cWhCode,
	wh.cWhName,
	wa.cAreaCode,
	wa.cAreaName
FROM
	Bd_Warehouse_Shelves ws
	LEFT JOIN Bd_Warehouse wh ON wh.iAutoId = ws.iWarehouseId
	LEFT JOIN Bd_Warehouse_Area wa ON wa.iAutoId = ws.iWarehouseAreaId
	WHERE ws.isDeleted=0
    #if(cshelvescode)
        AND ws.cShelvesCode LIKE CONCAT('%', #para(cshelvescode), '%')
    #end
    #if(cshelvesname)
        AND ws.cShelvesName LIKE CONCAT('%', #para(cshelvesname), '%')
    #end
    #if(iwarehouseid)
        AND ws.iWarehouseId = #para(iwarehouseid)
    #end
    #if(isenabled)
        AND ws.isEnabled = #para(isenabled)
    #end
    #if(iwarehouseareaid)
    AND ws.iWarehouseAreaId=#(iwarehouseareaid)
    #end
    #if(ids)
        AND CHARINDEX(','+cast((select ws.iAutoId) as nvarchar(20))+',' , ','+#para(ids)+',') > 0
    #end
	ORDER BY ws.cShelvesCode DESC
#end

#sql("selectPrint")
SELECT
	ws.*,
	(
		ISNULL( wh.cWhCode, '' ) + '|' + ISNULL( wa.cAreaCode, '' ) + '|' + ISNULL( ws.cShelvesCode, '' )
	) shelvescode,
	(
		ISNULL( wh.cWhCode, '' ) + '|' + ISNULL( wa.cAreaCode, '' ) + '|' + ISNULL( ws.cShelvesCode, '' ) + '-' + ws.cShelvesName
	) barname,
	wh.cWhCode,
	wh.cWhName,
	wa.cAreaCode,
	wa.cAreaName
FROM
	Bd_Warehouse_Shelves ws
	LEFT JOIN Bd_Warehouse wh ON wh.iAutoId = ws.iWarehouseId
	LEFT JOIN Bd_Warehouse_Area wa ON wa.iAutoId = ws.iWarehouseAreaId
WHERE
	1 = 1
	AND ws.isDeleted = '0'
    AND ws.iAutoId IN (#(ids))
    ORDER BY ws.dCreateTime DESC
#end

#sql("verifyDuplication")
select ISNULL(COUNT(iAutoId), 0) FROM Bd_Warehouse_Shelves WHERE isDeleted=0
#if(cshelvescode)
    AND cShelvesCode = #para(cshelvescode)
#end
#if(cshelvesname)
    AND cShelvesName = #para(cshelvesname)
#end
#if(iwarehouseid)
    AND iWarehouseId = #(iwarehouseid)
#end
#if(iwarehouseareaid)
    AND iWarehouseAreaId = #(iwarehouseareaid)
#end
#if(iautoid)
    AND iAutoId != #(iautoid)
#end
#end

#sql("integrityCheck")
SELECT
	area.iWarehouseId,
	area.iAutoId
FROM
	Bd_Warehouse_Area area
	LEFT JOIN Bd_Warehouse war ON area.iWarehouseId = war.iAutoId
WHERE
	area.isDeleted = 0
	AND war.isDeleted = 0
	AND war.cWhName = #para(cwhname)
	AND area.cAreaName =#para(careaname)
#end

#sql("getPositionById")
SELECT ISNULL(COUNT(iAutoId), 0) qty FROM Bd_Warehouse_Position WHERE iWarehouseShelvesId IN (#(id)) AND isDeleted = 0
#end
