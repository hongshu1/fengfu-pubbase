#sql("list")
SELECT
    jd.name cDepName,
    jd.sn cDepCode,
    m.*
FROM Bd_WorkRegionM m
    LEFT JOIN #(getBaseDbName()).dbo.jb_dept jd ON jd.id = m.iDepId
WHERE m.isDeleted = '0'
    #if(ids)
        AND m.iautoid IN #(ids)
    #end
    #if(cworkcode)
        AND m.cworkcode = #para(cworkcode)
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

#sql("findByWarehouse")
SELECT
	iAutoId,
	cWhCode,
	cWhName
FROM
	Bd_Warehouse
	WHERE
	    iOrgId = #para(orgId)
	AND isDeleted = '0'
#end

#sql("findByWareHouseId")
SELECT iAutoId,cAreaCode,cAreaName FROM Bd_Warehouse_Area WHERE iWarehouseId = #para(wareHouseId) AND isEnabled = 1 AND isDeleted = '0'
#end
