#sql("list")
SELECT
    ware.cWhCode,
    ware.cWhName,
    m.*
FROM Bd_WorkRegionM m
    LEFT JOIN Bd_Department jd ON jd.iautoid = m.iDepId
    LEFT JOIN Bd_Person per ON per.iAutoId = m.iPersonId
    LEFT JOIN Bd_Warehouse ware ON ware.iAutoId = m.iWarehouseId
WHERE m.isDeleted = '0'
    #if(ids)
        AND m.iautoid IN #(ids)
    #end
    #if(cworkcode)
        AND m.cworkcode = #para(cworkcode)
    #end
    #if(cwhname)
        AND ware.cwhname=#para(cwhname)
    #end
    #if(cworkname)
        AND m.cworkname = #para(cworkname)
    #end
    #if(idepid)
        AND m.idepid = #para(idepid)
    #end
    #if(isenabled)
        AND m.isenabled = #para(isenabled == 'true' ? 1 : 0)
    #end
     #if(ipslevel)
        AND m.iPsLevel = #para(ipslevel)
    #end
ORDER BY m.dCreateTime DESC
#end

#sql("findWorkRegionMByLikeId")
SELECT
	*
FROM
	Bd_WorkRegionM
WHERE '#(ids)' LIKE CONCAT ( '%', iAutoId, '%' )
#end



#sql("getWorkRegionMByDeptIdList")
SELECT
iAutoId
FROM Bd_WorkRegionM
WHERE iDepId IN (#(teamIdList))
#end

