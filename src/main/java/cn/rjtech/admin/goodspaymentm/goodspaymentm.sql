
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
        END AS iStatus,rm.iStatus as iStatusId,rm.cGoodsPaymentNo,rm.iCustomerId,u.cCusCode,u.cCusName,rm.cCreateName,rm.dUpdateTime,rm.dCreateTime
FROM SM_GoodsPaymentM rm
LEFT JOIN Bd_Customer u on rm.iCustomerId = u.iautoid
where 1=1 and rm.IsDeleted = '0'
	#if(crcvplanno)
		and rm.cGoodsPaymentNo like concat('%',#para(crcvplanno),'%')
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
FROM SM_GoodsPaymentD a
where 1=1 and a.isdeleted = 0
	#if(goodspaymentmid)
		and a.iGoodsPaymentMid = #para(goodspaymentmid)
	#end
ORDER BY a.dUpdateTime DESC
#end