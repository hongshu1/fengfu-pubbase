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
	WHERE 1=1
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
    #if(isdeleted)
        AND ws.isDeleted = #para(isdeleted)
    #end
    #if(ids)
        AND CHARINDEX(','+cast((select ws.iAutoId) as nvarchar(20))+',' , ','+#para(ids)+',') > 0
    #end
	ORDER BY ws.dCreateTime DESC
#end

#sql("selectPrint")
SELECT
ws.*,
(ISNULL(wh.cWhCode, '')+'|'+ISNULL(wa.cAreaCode, '')+'|'+ISNULL(ws.cShelvesCode,'')) cbarcode,
wh.cWhCode,
wh.cWhName,
wa.cAreaCode,
wa.cAreaName
FROM
Bd_Warehouse_Shelves ws
LEFT JOIN Bd_Warehouse wh ON wh.iAutoId = ws.iWarehouseId
LEFT JOIN Bd_Warehouse_Area wa ON wa.iAutoId = ws.iWarehouseAreaId
WHERE 1 = 1
AND ws.isDeleted = '0'
AND ws.iAutoId IN #(str)
ORDER BY
ws.dCreateTime DESC
#end