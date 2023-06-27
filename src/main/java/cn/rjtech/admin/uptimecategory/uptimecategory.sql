#sql("getAdminDatas")
SELECT *
FROM Bd_UptimeCategory
WHERE isDeleted = '0'
ORDER BY dCreateTime DESC
#end