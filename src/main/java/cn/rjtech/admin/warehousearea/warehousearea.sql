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
     #if(iwarehouseid)
        AND wa.iWarehouseId LIKE CONCAT('%', #para(iwarehouseid), '%')
    #end
    #if(careaname)
        AND wa.cAreaName LIKE CONCAT('%', #para(careaname), '%')
    #end
    #if(iwarehouseid)
        AND wa.iWarehouseId = #para(iwarehouseid)
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
        AND CHARINDEX(','+cast((select wa.iAutoId) as nvarchar(20))+',' , ','+#para(ids)+',') > 0
    #end
	ORDER BY wa.cAreaCode DESC
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
order by wa.dUpdateTime desc
#end
