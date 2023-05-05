#sql("workRegions")
select
 iw.*,wr.cWorkName cworkname,wr.cWorkCode cworkcode,p.cPsn_Name cpersonname,
 d.cDepName cdepname,jd.name defaultname
from Bd_PadWorkRegion iw
inner join Bd_WorkRegionM wr on iw.iWorkRegionMid = wr.iAutoId
left join Bd_Person p on wr.iPersonId = p.iAutoId
left join Bd_Department d on wr.iDepId = d.iAutoId
left join #(getBaseDbName()).dbo.jb_dictionary jd on iw.isDefault + 1 = jd.sort_rank and jd.type_key = 'options_boolean'
where iw.iPadId = #para(ipadid)
#end

#sql("list")
select * from (
select DISTINCT p.*,pwr2.iWorkRegionMid iworkregionmid,
        stuff((
        SELECT ',' + wr.cWorkName
        FROM Bd_PadWorkRegion pwr
				left join Bd_WorkRegionM wr on pwr.iWorkRegionMid = wr.iAutoId
        WHERE pwr.iPadId = p.iAutoId
        FOR XML path('')
    ), 1, 1, '') workregions
from Bd_Pad p
LEFT JOIN Bd_PadWorkRegion pwr2 on p.iAutoId = pwr2.iPadId
where p.IsDeleted = 0
) a where 1=1

#if(cpadcode)
and a.cPadCode = #para(cpadcode)
#end
#if(cpadname)
and a.cPadName = #para(cpadname)
#end
#if(iworkregionmid)
and a.iWorkRegionMid = #para(iworkregionmid)
#end
#end

#sql("getPadWorkRegionByCmac")
SELECT
	gion.*,
	bpad.cPadCode,
	bpad.cPadName,
	workm.cWorkName
FROM
	Bd_PadWorkRegion gion
	LEFT JOIN Bd_Pad bpad ON gion.iPadId = bpad.iAutoId
	LEFT JOIN Bd_WorkRegionM workm ON gion.iWorkRegionMid = workm.iAutoId
WHERE
	bpad.cMac = #para(cmac)
#end