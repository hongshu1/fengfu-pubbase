#sql("paginateAdminDatas")
	select vc.*,VRR.cRdName vrrname,VRS.cRdName vrsname from Bd_VouchRdContrapose vc
		left join bd_rd_Style VRR on VRR.cRdCode = vc.cVRRCode and VRR.bRdFlag = 1 and VRR.isDeleted = '0'
	#if(iorgid)
		and VRR.iorgid = #para(iorgid)
	#end	
		left join bd_rd_Style VRS on VRS.cRdCode = vc.cVRSCode and VRS.bRdFlag = 0 and VRS.isDeleted = '0'
	#if(iorgid)
		and VRS.iorgid = #para(iorgid)
	#end	
	where vc.isDeleted = '0'
	#if(keywords)
		and (vc.cVRRCode like concat('%',#para(keywords),'%') or vc.cVRSCode like concat('%',#para(keywords),'%')
			or VRR.cRdName like concat('%',#para(keywords),'%') or VRS.cRdName like concat('%',#para(keywords),'%')
		)
	#end
	#if(iorgid)
		and vc.iorgid = #para(iorgid)
	#end		
#end

#sql("getCvrrName")
 select cRdName from Bd_Rd_Style where cRdCode=#para(cvrrcode)
 #end