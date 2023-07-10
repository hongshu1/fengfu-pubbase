#sql("workRegions")
select
 iw.*,wr.cWorkName cworkname,wr.cWorkCode cworkcode,p.cPsn_Name cpersonname,
 d.cDepName cdepname,jd.name defaultname
from Bd_PadWorkRegion iw
inner join Bd_WorkRegionM wr on iw.iWorkRegionMid = wr.iAutoId
left join Bd_Person p on wr.iPersonId = p.iAutoId
left join Bd_Department d on wr.iDepId = d.iAutoId
left join #(getBaseDbName()).dbo.jb_dictionary jd on iw.isDefault = jd.sn and jd.type_key = 'options_boolean'
where iw.iPadId = #para(ipadid)
#end

#sql("list")
SELECT
	iAutoId,
	cPadName,
	cPadCode,
	cMac,
	isEnabled,
	cCreateName,
	dCreateTime
FROM
	Bd_Pad
WHERE
	IsDeleted =0

#if(cpadcode)
and a.cPadCode LIKE CONCAT('%',#para(cpadcode),'%')
#end
#if(cpadname)
and a.cPadName LIKE CONCAT('%',#para(cpadname),'%')
#end
#if(iworkregionmid)
and a.iWorkRegionMid = #para(iworkregionmid)
#end
#if(mac)
and cMac = #para(mac)
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

#sql("uniqueCheck")
SELECT ISNULL(COUNT(iAutoId), 0) FROM Bd_Pad WHERE IsDeleted=0
#if(code)
AND cPadCode = #para(code)
#end
#if(mac)
AND cMac = #para(mac)
#end
#if(iautoid)
AND iautoid <> #(iautoid)
#end
#end