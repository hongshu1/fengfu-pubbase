#sql("paginateAdminDatas")
	select aom.iautoid,aom.iOrderStatus,'' cAuditProgress,aom.corderno,
           aom.cCusName,aom.iyear,aom.cCreateName,aom.dCreateTime,aom.iAuditStatus
	from Co_AnnualOrderM aom
	where aom.isDeleted = '0'
	#if(iorgid)
		and aom.iorgid = #para(iorgid)
	#end
	#if(cOrderNo)
		and aom.cOrderNo like concat('%',#para(cOrderNo),'%') 
	#end
	#if(cCusName)
		and aom.cCusName like concat('%',#para(cCusName),'%')
	#end
	#if(iOrderStatus)
	    AND aom.iOrderStatus = #para(iOrderStatus)
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
    ORDER BY aom.dCreateTime desc
#end