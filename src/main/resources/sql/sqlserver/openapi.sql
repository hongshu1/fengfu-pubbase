#sql("api")
SELECT *
FROM T_Sys_OpenAPI
where 1=1
#if(code)
    AND Code = #para(code)
#end
#end



#sql("findByCodeAndTag")
SELECT *
FROM T_Sys_OpenAPI
where 1=1
AND Code = #para(code)
AND tag = #para(tag)
#end



#sql("processBus")
select p.Autoid,p.MasID,p.GroupSeq,p.Seq,p.ExechgeSeq,p.ReturnSeq,p.ProcessID,p.ProcessFlag,p.ClosePerson as pClosePerson,b.CreatePerson as bClosePerson,
       b.MapID as InitializeMapID,b.ProcessName,b.ProcessFlag as BProcessFlag,api.Code,api.Tag,api.Url,api.ResultType,b.Memo,AutoRollback,IgnoreFailure
from t_sys_vouchprocess p
left join t_sys_vouchbusiness b on p.ProcessID=b.AutoID
left join t_sys_openapi api on b.APIID=api.AutoID
where MasID=#para(masID) ORDER BY p.GroupSeq,p.Seq
#end

#sql("processBusDynamic")
select top 1 p.Autoid,p.MasID,p.GroupSeq,p.Seq,p.ExechgeSeq,p.ReturnSeq,p.ProcessID,p.ProcessFlag,p.ClosePerson as pClosePerson,b.CreatePerson as bClosePerson,
       b.MapID as InitializeMapID,b.ProcessName,b.ProcessFlag as BProcessFlag,api.Code,api.Tag,api.Url,api.ResultType,b.Memo,AutoRollback,IgnoreFailure
from T_Sys_VouchProcessDynamic p
         left join t_sys_vouchbusiness b on p.ProcessID=b.AutoID
         left join t_sys_openapi api on b.APIID=api.AutoID
where MasID=#para(masID) and VersionID=#para(seqBusinessID) and p.stat=0
ORDER BY p.GroupSeq,p.Seq
#end

#sql("allProcessBusDynamic")
select p.Autoid,p.MasID,p.GroupSeq,p.Seq,p.ExechgeSeq,p.ReturnSeq,p.ProcessID,p.ProcessFlag,p.ClosePerson as pClosePerson,b.CreatePerson as bClosePerson,
             b.MapID as InitializeMapID,b.ProcessName,b.ProcessFlag as BProcessFlag,api.Code,api.Tag,api.Url,api.ResultType,b.Memo,AutoRollback,IgnoreFailure
from T_Sys_VouchProcessDynamic p
left join t_sys_vouchbusiness b on p.ProcessID=b.AutoID
left join t_sys_openapi api on b.APIID=api.AutoID
where MasID=#para(masID) and VersionID=#para(seqBusinessID)
ORDER BY p.GroupSeq,p.Seq
#end