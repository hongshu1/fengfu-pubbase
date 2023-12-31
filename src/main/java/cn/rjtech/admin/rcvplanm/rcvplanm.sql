#sql("getAdminDatas")
SELECT  rm.iAutoId as iautoid,
       statename=
       CASE WHEN rm.iStatus=1 THEN '已保存'
		    WHEN rm.iStatus=2 THEN '待审批'
			WHEN rm.iStatus=3 THEN '已审批'
	        WHEN rm.iStatus=4 THEN '审批不通过'
        END,
       rm.iStatus,
       rm.cRcvPlanNo,
       u.cCusCode,
       u.cCusName,
       rm.cCreateName,
       rm.dUpdateTime,
       rm.dCreateTime
FROM SM_RcvPlanM rm
LEFT JOIN Bd_Customer u on rm.iCustomerId = u.iautoid
where rm.IsDeleted = '0'
	#if(crcvplanno)
		and rm.cRcvPlanNo like concat('%',#para(crcvplanno),'%')
	#end
	#if(ccusname)
		and u.cCusName like concat('%',#para(ccusname),'%')
	#end
	#if(startTime)
		and rm.dCreateTime >= #para(startTime)
	#end
	#if(endTime)
		and rm.dCreateTime <= #para(endTime)
	#end
	    and rm.IsDeleted = '0'
ORDER BY rm.dUpdateTime DESC
#end

#sql("dList")
SELECT  a.*,i.cInvCode,i.cinvcode1,i.cinvname1,i.cinvstd,u.cUomName
FROM SM_RcvPlanD a
	left join Bd_Inventory i on a.iInventoryId = i.iautoid
	left join Bd_Uom u on i.iInventoryUomId1 = u.iautoid
where a.isDeleted = '0'
	#if(rcvplanmid)
		and a.iRcvPlanMid = #para(rcvplanmid)
	#end
ORDER BY a.dUpdateTime DESC
#end

