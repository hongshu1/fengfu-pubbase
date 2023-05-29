#sql("getNflat")
SELECT top 1 nflat
from exch
WHERE cexch_name = #para(cexchname)
ORDER BY i_id DESC, iYear DESC, cdate DESC
#end