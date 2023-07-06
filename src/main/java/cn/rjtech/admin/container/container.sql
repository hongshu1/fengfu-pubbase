#sql("paginateAdminDatas")
SELECT
	c.*,
	cs.cContainerClassName,
	wh.cWhCode,
	wh.cWhName,
    dt.cDepCode,
    dt.cDepName
FROM
	Bd_Container c
    LEFT JOIN Bd_ContainerClass cs ON c.iContainerClassId = cs.iAutoId
	LEFT JOIN Bd_Warehouse wh ON c.iWarehouseId = wh.iAutoId
	LEFT JOIN bd_department dt ON c.iDepId = dt.iAutoId
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

#sql("containerPrintData")
SELECT
c.*,
(ISNULL(c.cContainerName, '')+'+'+ISNULL(c.cContainerCode, '')) ccontainercodename,
cs.cContainerClassName,
dt.cDepCode,
dt.cDepName
FROM
Bd_Container c
LEFT JOIN Bd_ContainerClass cs ON c.iContainerClassId = cs.iAutoId
LEFT JOIN bd_department dt ON c.iDepId = dt.iAutoId
WHERE  c.isDeleted = 0
 #if(ids)
   AND CHARINDEX(','+cast((select c.iAutoId) as nvarchar(20))+',' , ','+#para(ids)+',') > 0
 #end
ORDER BY
c.dCreateTime DESC
#end

#sql("uniqueCheck")
SELECT ISNULL(COUNT(iAutoId), 0) from Bd_Container WHERE IsDeleted=0
#if(ccontainercode)
 AND cContainerCode = #para(ccontainercode)
#end
#if(ccontainername)
 AND cContainerName = #para(ccontainername)
#end
#end

#sql("getContainerClassByName")
SELECT * FROM Bd_ContainerClass WHERE IsDeleted=0 AND cContainerClassName = #para(name)
#end
