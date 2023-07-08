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

#sql("findRegularFlatDatas")
	#(cdatesql)
	left join 
	(
		select cdate,nflat from bd_exch ec where itype in (#(itype)) and cexch_name = #para(cexchnamekv) and iyear = datename(year,getdate())
	) exch_rs on cdate_rs.cdate = exch_rs.cdate
	order by cdate_rs.cdate
#end

#sql("findFloatFlatDatas")
select cdate_rs.cdate,exch_rs.nflat1,exch_rs.nflat2,2 nflattype,
	(select top 1 iautoid from bd_foreigncurrency where cexch_name = #para(cexchnamekv) and isdeleted = 0) foreigncurrencyid
from (
	select concat(datename(year,getdate()),'.01') cdate
	union all
	select concat(datename(year,getdate()),'.02') cdate
	union all
	select concat(datename(year,getdate()),'.03') cdate
	union all
	select concat(datename(year,getdate()),'.04') cdate
	union all
	select concat(datename(year,getdate()),'.05') cdate
	union all
	select concat(datename(year,getdate()),'.06') cdate
	union all
	select concat(datename(year,getdate()),'.07') cdate
	union all
	select concat(datename(year,getdate()),'.08') cdate
	union all
	select concat(datename(year,getdate()),'.09') cdate
	union all
	select concat(datename(year,getdate()),'.10') cdate
	union all
	select concat(datename(year,getdate()),'.11') cdate
	union all
	select concat(datename(year,getdate()),'.12') cdate
) cdate_rs
left join 
(
select concat(iyear,'.',right(concat('000',cdate),2)) cdate,
	min(case itype when 2 then nflat end) nflat1,
	min(case itype when 3 then nflat end) nflat2 
	from bd_exch ec where itype in(#(itype)) and cexch_name = #para(cexchnamekv) 
	and iyear = datename(year,getdate()) GROUP BY iyear,cdate
) exch_rs on cdate_rs.cdate = exch_rs.cdate
order by cdate_rs.cdate
#end