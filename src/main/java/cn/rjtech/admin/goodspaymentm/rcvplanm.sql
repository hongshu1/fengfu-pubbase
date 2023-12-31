
#sql("recpor")
SELECT  rm.iAutoId as iautoid, CASE rm.iStatus
        WHEN 1 THEN
        '已保存'
				WHEN 2 THEN
        '待审批'
				WHEN 3 THEN
        '已审批'
				WHEN 4 THEN
        '审批不通过'
        END AS iStatus,rm.iStatus as iStatusId,rm.cRcvPlanNo,u.cCusCode,u.cCusName,rm.cCreateName,rm.dUpdateTime,rm.dCreateTime
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
ORDER BY rm.dUpdateTime DESC
#end

#sql("dList")
SELECT  a.*
FROM SM_RcvPlanD a
where a.isDeleted = '0'
	#if(rcvplanmid)
		and a.iRcvPlanMid = #para(rcvplanmid)
	#end
ORDER BY a.cRcvTime DESC
#end

