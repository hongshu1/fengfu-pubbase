#sql("paginateAdminDatas")
SELECT
	c.*,
	cs.cContainerClassName,
	wh.cWhCode,
	wh.cWhName
FROM
	Bd_Container c
    LEFT JOIN Bd_ContainerClass cs ON c.iContainerClassId = cs.iAutoId
	LEFT JOIN Bd_Warehouse wh ON c.iWarehouseId = wh.iAutoId
	WHERE c.isDeleted = 0
    #if(cContainerCode)
        AND c.cContainerCode LIKE CONCAT('%', #para(cContainerCode), '%')
    #end
    #if(cContainerName)
        AND c.cContainerName LIKE CONCAT('%', #para(cContainerName), '%')
    #end
    #if(iContainerClassId)
        AND c.iContainerClassId = #para(iContainerClassId)
    #end
    #if(isInner)
        AND c.isInner = #para(isInner)
    #end
    #if(ids)
        AND CHARINDEX(','+cast((select c.iAutoId) as nvarchar(20))+',' , ','+#para(ids)+',') > 0
    #end
	ORDER BY c.dCreateTime DESC
#end