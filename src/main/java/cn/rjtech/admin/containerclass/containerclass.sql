#sql("paginateAdminDatas")
SELECT * FROM Bd_ContainerClass WHERE IsDeleted=0 ORDER BY dCreateTime DESC
#end

#sql("getContainerByDid")
SELECT ISNULL(COUNT(iAutoId), 0) FROM Bd_Container WHERE iContainerClassId IN (#(ids)) AND IsDeleted=0
#end
