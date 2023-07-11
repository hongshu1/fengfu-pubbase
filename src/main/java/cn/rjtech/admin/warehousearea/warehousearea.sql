#sql("paginateAdminDatas")
SELECT
	wa.*,
	wh.cWhCode,
	wh.cWhName
FROM
	Bd_Warehouse_Area wa
	LEFT JOIN Bd_Warehouse wh ON wa.iWarehouseId = wh.iAutoId
	WHERE wa.isDeleted = 0
    #if(careacode)
        AND wa.cAreaCode LIKE CONCAT('%', #para(careacode), '%')
    #end
    #if(careaname)
        AND wa.cAreaName LIKE CONCAT('%', #para(careaname), '%')
    #end
    #if(iwarehouseid)
        AND wa.iWarehouseId = #(iwarehouseid)
    #end
    #if(isenabled)
        AND wa.isEnabled = #para(isenabled)
    #end
    #if(imaxcapacityMin)
        AND wa.iMaxCapacity >= #para(imaxcapacityMin)
    #end
    #if(imaxcapacityMax)
        AND wa.iMaxCapacity <= #para(imaxcapacityMax)
    #end
    #if(ids)
        AND wa.iAutoId IN (#(ids))
    #end
    #if(cwhcode)
        AND wh.cwhcode LIKE CONCAT('%', #para(cwhcode), '%')
    #end
	ORDER BY wa.dCreateTime DESC
#end

#sql("findByWareHouseId")
SELECT iAutoId,cAreaCode,cAreaName FROM Bd_Warehouse_Area WHERE iWarehouseId = #para(wareHouseId) AND isEnabled = 1 AND isDeleted = '0'
#end

#sql("containerPrintData")
SELECT
wa.*,
(ISNULL(wa.cAreaCode, '')+'+'+ISNULL(wa.cAreaName, '')) careacodename,
wh.cWhCode
FROM Bd_Warehouse_Area wa
LEFT JOIN Bd_Warehouse wh ON wa.iWarehouseId = wh.iAutoId
WHERE  wa.isDeleted = 0
 #if(ids)
   AND CHARINDEX(','+cast((select wa.iAutoId) as nvarchar(20))+',' , ','+#para(ids)+',') > 0
 #end
ORDER BY
wa.dCreateTime DESC
#end

#sql("options")
SELECT
	wa.cAreaCode,wa.cAreaName,wa.iWarehouseId
FROM
	Bd_Warehouse_Area wa
where wa.isEnabled = '1' and wa.isDeleted = '0'
#if(iwarehouseid)
and wa.iWarehouseId = #para(iwarehouseid)
#end
order by wa.dUpdateTime desc
#end

#sql("verifyDuplication")
SELECT
	ISNULL( COUNT ( iAutoId ), 0 )
FROM
	Bd_Warehouse_Area
WHERE
	isDeleted = 0
#if(careacode)
    AND careacode = #para(careacode)
#end
#if(careaname)
    AND careaname = #para(careaname)
#end
#if(iwarehouseid)
    AND iWarehouseId = #para(iwarehouseid)
#end
#if(iautoid)
    AND iautoid != #para(iautoid)
#end
#end

#sql("getiAutoIdByCwhname")
SELECT
	iAutoId
FROM
	Bd_Warehouse
WHERE
	isDeleted = 0
	AND cWhName = #para(cwhname)
#end

#sql("getShelvesById")
SELECT ISNULL(COUNT(iAutoId), 0) qty FROM Bd_Warehouse_Shelves WHERE iWarehouseAreaId IN (#(id)) AND isDeleted = 0
#end