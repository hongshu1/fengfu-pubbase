### 据排产层级去产线档案找到未删除状态的默认产线，拿到存货id，去存货档案表找到存货编码和存货名称 的sql
#sql("planSummaryList")
SELECT iInventoryId, cInvCode, cInvName
FROM Bd_WorkRegionM wrm
         INNER JOIN Bd_InventoryWorkRegion iwr ON wrm.iAutoId = iwr.iWorkRegionMid
         INNER JOIN Bd_Inventory inv ON iwr.iInventoryId = inv.iAutoId
WHERE wrm.iPsLevel = #para(hierarchy)
  AND iwr.isDefault = 1
  AND iwr.isDeleted = 0
    #end