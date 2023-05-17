
#sql("recpor")
SELECT mp.AutoID,
       mp.OrganizeCode,
       mp.BillNo,
       mp.BillType,
       mp.BillDate,
       mp.AuditPerson,
       mp.AuditDate,
       mp.CreatePerson,
       mp.CreateDate,
       mp.ModifyPerson,
       mp.ModifyDate,
       mp.SourceBillNo,
       mp.SourceBillID,
       mpd.InvCode,
       mpd.Barcode,
       mpd.Qty,
       it.cInvName1,
       it.cInvCode1,
       it.cInvAddCode1,
       md.iWorkShiftMid,
       CASE md.iStatus
           WHEN 1 THEN
               '未安排人员'
           WHEN 2 THEN
               '已安排人员'
           WHEN 3 THEN
               '生成备料单'
           WHEN 4 THEN
               '待生产'
           WHEN 5 THEN
               '生产中'
           WHEN 6 THEN
               '已完工'
           WHEN 7 THEN
               '已关闭'
           WHEN 8 THEN
               '已取消'
           END AS statename
FROM T_Sys_MaterialsPrepare mp
         LEFT JOIN T_Sys_MaterialsPrepareDetail mpd ON mpd.MasID = mp.AutoID
         LEFT JOIN Mo_MoDoc md ON md.iMoTaskId = mp.SourceBillID
         LEFT JOIN Bd_Inventory it ON it.cInvCode = mpd.InvCode
where 1 = 1 #if(startTime)
  AND dPlanDate >= #para(startTime)
  #end
  #if(endTime)
  AND dPlanDate <= #para(endTime)
  #end
ORDER BY mp.CreateDate DESC
    #end

    #sql("findColumns")
SELECT *
FROM Bd_WorkRegionM wrm
WHERE wrm.isDeleted = '0' #if(isenabled)
  AND wrm.isenabled = #para(isenabled)
  #end
#end

#sql("findColumns1")
SELECT *
FROM Bd_WorkShiftM wsm
WHERE wsm.isDeleted = '0' #if(isenabled)
  AND wsm.isenabled = #para(isenabled)
  #end
#end
