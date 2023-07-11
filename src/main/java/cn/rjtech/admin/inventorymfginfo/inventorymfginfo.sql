#sql("inventoryMfgInfo")
SELECT top 1 o.isIQC1
FROM Bd_InventoryMfgInfo o
INNER JOIN Bd_Inventory y ON y.iAutoId = o.iInventoryId
WHERE iorgid = #para(iorgid) AND y.cInvCode = #para(cinvcode)
ORDER BY o.iAutoId DESC
#end