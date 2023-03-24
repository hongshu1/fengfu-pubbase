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
        AND wa.iWarehouseId = #para(iwarehouseid)
    #end
    #if(isenabled)
        AND wa.isEnabled = #para(isenabled)
    #end
    #if(ids)
        AND CHARINDEX(','+cast((select wa.iAutoId) as nvarchar(20))+',' , ','+#para(ids)+',') > 0
    #end
	ORDER BY wa.dCreateTime DESC
#end

#sql("findByWareHouseId")
SELECT iAutoId,cAreaCode,cAreaName FROM Bd_Warehouse_Area WHERE iWarehouseId = #para(wareHouseId) AND isEnabled = 1 AND isDeleted = '0'
#end
