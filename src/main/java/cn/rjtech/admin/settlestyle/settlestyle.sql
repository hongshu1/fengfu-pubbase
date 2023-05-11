#sql("paginateAdminDatas")
select * from Bd_SettleStyle where isDeleted = '0'
#if(keywords)
	and (csscode like concat('%',#para(keywords),'%') or cssname like concat('%',#para(keywords),'%')) 
#end
#if(iorgid)
	and iorgid = #para(iorgid)
#end
#end