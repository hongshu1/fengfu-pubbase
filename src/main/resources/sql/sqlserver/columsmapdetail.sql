#sql("getMasIdList")
SELECT AutoID,MasID,FlagType,PFlag,Flag,TagColumn,SourceColumn,SourceFlag,DefaultValue
FROM T_Sys_ColumsMapDetail
WHERE ISNULL(TagColumn, '')!=''
#if(masid)
 AND masid = #para(masid)
#end
#end

#sql("getMasIdFlagList")
SELECT AutoID,MasID,FlagType,PFlag,Flag,TagColumn,SourceColumn,SourceFlag,DefaultValue
FROM T_Sys_ColumsMapDetail
WHERE isnull(CLosePerson, '')=''
#if(masid)
 AND MasId=#para(masid)
#end
#if(flag)
 AND flag=#para(flag)
 AND ISNULL(TagColumn, '')!=''
#end
#end

#sql("theFirstLayerOfData")
SELECT AutoID,MasID,FlagType,PFlag,Flag,TagColumn,SourceColumn,SourceFlag,DefaultValue
FROM T_Sys_ColumsMapDetail
WHERE ISNULL(TagColumn, '')='' AND isnull(PFlag, '')='' AND isnull(CLosePerson, '')=''
#if(masid)
 AND masid = #para(masid)
#end
#end

#sql("theFirstLayerOfDataExtend")
SELECT AutoID,MasID,FlagType,PFlag,Flag,TagColumn,SourceColumn,SourceFlag,DefaultValue
FROM T_Sys_ColumsMapDetail
WHERE ISNULL(TagColumn, '')!='' AND isnull(PFlag, '')=''  AND  isnull(Flag, '')='' AND isnull(CLosePerson, '')=''
#if(masid)
 AND masid = #para(masid)
#end
#end

#sql("queryLowerLevelData")
SELECT AutoID,MasID,FlagType,PFlag,Flag,TagColumn,SourceColumn,SourceFlag,DefaultValue
FROM T_Sys_ColumsMapDetail
WHERE ((ISNULL(TagColumn, '')!=''
#if(flag)
 AND flag=#para(flag)
#end
) OR (isnull(TagColumn, '')=''
#if(pflag)
 AND PFlag=#para(pflag)
#end
)) AND isnull(CLosePerson, '')=''
#if(masid)
 AND masid = #para(masid)
#end
#end

#sql("getLogVouchTypeColumsMapDetailList")
select c.* from T_Sys_ColumsMapDetail c left join T_Sys_VouchBusiness  b on c.MasID=b.MapID
left join T_Sys_VouchProcess p on p.ProcessID=b.AutoID
inner join T_Sys_VouchType v on v.Autoid=p.MasID
where v.VouchCode=#para(vouchType)
group by c.autoid,c.masid,c.level,c.flagtype,c.pflag,c.flag,c.tagcolumn,c.sourcecolumn,
c.sourceflag,c.defaultvalue,c.memo,c.closeperson,c.createperson,c.createdate,c.modifyperson,c.modifydate
#end

#sql("getVouchProcessRuslte")
select resultAnalysis FROM dbo.T_Sys_VouchProcessRuslte where 1=1
#if(sourceType)
 and sourcetype=#para(sourceType)
#end
#end