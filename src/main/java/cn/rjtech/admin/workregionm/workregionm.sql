#sql("list")
SELECT
    jd.name cDepName,
    jd.sn cDepCode,
    p.cPersonName,
    p.cPersonCode,
    m.*
FROM Bd_WorkRegionM m
    LEFT JOIN #(getBaseDbName()).dbo.jb_dept jd ON jd.id = m.iDepId
    LEFT JOIN Bd_Person p ON m.iPersonId = p.iAutoId
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