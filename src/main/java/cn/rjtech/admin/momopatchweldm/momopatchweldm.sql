#sql("getMoPatchWeldMList")
###根据条件查询补焊记录
SELECT a.iAutoId,a.iAuditStatus,b.cMoDocNo,b.dPlanDate,b.iQty,
       c.cWorkCode,c.cWorkName,d.cWorkShiftCode,d.cWorkShiftName,
       e.cDepName,f.cInvCode,f.cInvCode1,f.cInvName1,
       a.cCreateName,a.dCreateTime,a.cAuditName,a.dAuditTime,
       CASE a.iAuditStatus
           WHEN 0 THEN
               '未审核'
           WHEN 1 THEN
               '待审核'
           WHEN 2 THEN
               '审核通过'
           WHEN 3 THEN
               '审核不通过'
           END AS statename
FROM Mo_MoPatchWeldM AS a
         LEFT JOIN Mo_MoDoc AS b ON a.iMoDocId = b.iAutoId
         LEFT JOIN Bd_WorkRegionM AS c ON b.iWorkRegionMid = c.iAutoId
         LEFT JOIN Bd_WorkShiftM AS d ON b.iWorkShiftMid = d.iAutoId
         LEFT JOIN Bd_Department AS e ON b.iDepartmentId = e.iAutoId
         LEFT JOIN Bd_Inventory AS f ON b.iInventoryId = f.iAutoId
WHERE 1 = 1
    #if(mid)
        AND a.iAutoId = #para(mid)
    #end
    #if(cmodocno)
        AND b.cMoDocNo LIKE CONCAT('%', #para(cmodocno), '%')
    #end
    #if(cworkname)
        AND c.cWorkName LIKE CONCAT('%', #para(cworkname), '%')
    #end
    #if(cworkshiftname)
        AND d.cWorkShiftName LIKE CONCAT('%', #para(cworkshiftname), '%')
    #end
    #if(iauditstatus)
        AND a.iAuditStatus = #para(iauditstatus)
    #end
    #if(startdate)
        AND b.dPlanDate >= #para(startdate)
    #end
    #if(enddate)
        AND b.dPlanDate <= #para(enddate)
    #end
    ORDER BY a.dCreateTime DESC
#end

#sql("getMoMopatchwelddList")
###根据条件查询补焊记录明细
SELECT a.*,b.iSeq,b.cOperationName,d.cEquipmentName
FROM Mo_MoPatchWeldD AS a
         LEFT JOIN Mo_MoRoutingConfig AS b ON a.iMoRoutingConfigId = b.iAutoId
         LEFT JOIN Mo_MoRoutingEquipment AS c ON b.iAutoId = c.iMoRoutingConfigId
         LEFT JOIN Bd_Equipment AS d ON c.iEquipmentId = d.iAutoId
WHERE a.iMoPatchWeldMid = #para(mid)
ORDER BY b.iSeq
#end

#sql("getMoMopatchwelddApiList")
###根据条件查询补焊记录明细
SELECT a.iMoDocId,b.iAutoId AS iMoRoutingConfigId,b.cOperationName,d.cEquipmentName,0 AS iQty
FROM Mo_MoRouting AS a
         LEFT JOIN Mo_MoRoutingConfig AS b ON a.iAutoId = b.iMoRoutingId
         LEFT JOIN Mo_MoRoutingEquipment AS c ON b.iAutoId = c.iMoRoutingConfigId
         LEFT JOIN Bd_Equipment AS d ON c.iEquipmentId = d.iAutoId
WHERE a.iMoDocId = #para(imodocid)
ORDER BY b.iSeq
#end