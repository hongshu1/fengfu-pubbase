#sql("list")
select pl.*,p.cPadCode,p.cPadName,p.cMac,u.name,u.username from Bd_PadLoginLog pl
 inner  join Bd_Pad p on p.iAutoId = pl.iPadId
 inner  join #(getBaseDbName()).dbo.jb_user u on u.id = pl.iUserId
 where 1=1

#if(cpadcode)
and p.cPadCode = #para(cpadcode)
#end
#if(cpadname)
and p.cPadName = #para(cpadname)
#end
#if(name)
and u.name = #para(name)
#end
#if(username)
and u.username = #para(username)
#end
#if(starttime)
and pl.dCreateTime >= #para(starttime)
#end
#if(endtime)
and pl.dCreateTime <= #para(endtime)
#end
order by pl.dCreateTime desc
#end