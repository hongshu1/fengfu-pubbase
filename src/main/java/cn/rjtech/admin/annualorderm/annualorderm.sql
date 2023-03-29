#sql("paginateAdminDatas")
	select iautoid,iOrderStatus,'' cAuditProgress,corderno,
		c.cCusName,aom.iyear,aom.cCreateName,aom.dCreateTime,iAuditStatus
	from Co_AnnualOrderM aom
		left join Bd_Customer c on aom.iCustomerId = c.iautoid
	where 1=1 and aom.isdeleted = 0
	#if(iorgid)
		and aom.iorgid = #para(iorgid)
	#end
#end