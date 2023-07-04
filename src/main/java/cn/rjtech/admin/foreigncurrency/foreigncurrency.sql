#sql("paginateAdminDatas")
select
    exch.nflat,
    cu.*
from
    Bd_ForeignCurrency cu
    LEFT JOIN (
	SELECT
		a.cexch_name,
		a.nflat
	FROM
		(
		SELECT
			cexch_name,
			nflat,
			ROW_NUMBER() OVER( partition BY cexch_name ORDER BY iYear, iperiod DESC ) AS rn
		FROM
			bd_exch
		WHERE
			IsDeleted = '0'
			#if(iorgid)
		        and iorgid = #para(iorgid)
		    #end
		) a
	WHERE
		a.rn = 1
	) exch ON exch.cexch_name = cu.cexch_name
where cu.isDeleted = 0
		#if(keywords)
			and (cu.cexch_name like concat('%',#para(keywords),'%') or  cu.cexch_code like concat('%',#para(keywords),'%'))
		#end
		#if(iorgid)
			and cu.iorgid = #para(iorgid)
		#end
		#if(id)
           and cu.iautoid = #para(id)
		#end
#end

#sql("getAllCexchName")
SELECT cexch_name
FROM Bd_ForeignCurrency where 1=1 and isDeleted = 0
#if(iorgid)
	and iorgid = #para(iorgid)
#end
GROUP BY cexch_name
#end
