#sql("list")
SELECT * from Bd_SpotCheckForm where isDeleted= 0 and isEnabled=1
    ORDER BY dUpdateTime DESC
#end