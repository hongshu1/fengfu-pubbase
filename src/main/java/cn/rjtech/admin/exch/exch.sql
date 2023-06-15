#sql("getNflat")
SELECT top 1 nflat
from bd_exch
WHERE cexch_name = #para(cexchname) and isDeleted = 0
#if(iorgid)
	and iorgid = #para(iorgid)
#end
ORDER BY iYear DESC, iperiod DESC
#end