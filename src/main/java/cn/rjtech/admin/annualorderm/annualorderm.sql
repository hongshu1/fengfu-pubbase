#sql("paginateAdminDatas")
	select aom.iautoid,aom.iOrderStatus,'' cAuditProgress,aom.corderno,
		c.cCusName,aom.iyear,aom.cCreateName,aom.dCreateTime,aom.iAuditStatus
	from Co_AnnualOrderM aom
		left join Bd_Customer c on aom.iCustomerId = c.iautoid
	where 1=1 and aom.isdeleted = 0
	#if(iorgid)
		and aom.iorgid = #para(iorgid)
	#end
	#if(cOrderNo)
		and aom.cOrderNo like concat('%',#para(cOrderNo),'%') 
	#end
	#if(cCusName)
		and c.cCusName like concat('%',#para(cCusName),'%') 
	#end
	#if(iAuditStatus)
		and aom.iAuditStatus = #para(iAuditStatus) 
	#end
	#if(iYear)
		and aom.iYear = #para(iYear) 
	#end
	#if(cCreateName)
		and aom.cCreateName like concat('%',#para(cCreateName),'%') 
	#end
#end