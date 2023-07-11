
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
        END AS iStatus,rm.iStatus as iStatusId,rm.iAuditStatus,rm.cGoodsPaymentNo,rm.iCustomerId,u.cCusCode,u.cCusName,rm.cCreateName,rm.dUpdateTime,rm.dCreateTime
FROM SM_GoodsPaymentM rm
LEFT JOIN Bd_Customer u on rm.iCustomerId = u.iautoid
where rm.IsDeleted = '0'
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
	#(getDataPermissionSql("rm", "corgcode"))
ORDER BY rm.dUpdateTime DESC
#end


#sql("dList")
SELECT  a.*,i.cInvCode,i.cinvcode1,i.cinvname1,i.cinvstd,u.cUomName,i.cinvname
FROM SM_GoodsPaymentD a
	left join Bd_Inventory i on a.iInventoryId = i.iautoid
	left join Bd_Uom u on i.iInventoryUomId1 = u.iautoid
where a.isDeleted = '0'
	#if(goodspaymentmid)
		and a.iGoodsPaymentMid = #para(goodspaymentmid)
	#end
ORDER BY a.dUpdateTime DESC
#end


#sql("getBarcodeDatas")
SELECT top #(limit)
	cwm.iCustomerId,
	bi.cInvAddCode,
	bi.cinvcode,
	bi.cinvname,
	bi.cinvname1,
	bi.cInvCode1,
	bi.cinvStd,
	bi.iSalesUomId,
	cwd.*
FROM
	Co_WeekOrderD cwd
	LEFT JOIN bd_inventory bi ON bi.iAutoId = cwd.iInventoryId 	AND bi.isDeleted = 0
	LEFT JOIN Co_WeekOrderM cwm ON cwd.iWeekOrderMid = cwm.iAutoId AND cwm.IsDeleted=0
where cwm.iCustomerId = #para(icustomerid)
  #if(q)
    and (bi.cinvcode like concat('%',#para(q),'%') or bi.cInvName1 like concat('%',#para(q),'%'))
  #end
#end