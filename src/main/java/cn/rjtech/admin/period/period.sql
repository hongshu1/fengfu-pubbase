#sql("paginateAdminDatas")
select * from Bas_Period where 1=1
#if(iservicetype)
	and iservicetype in (#(iservicetype))
#end
#if(isenabled)
	and isenabled = #para(isenabled)
#end
#if(ibudgettype)
	and ibudgettype = #para(ibudgettype)
#end
#if(ibudgetyear)
	and ibudgetyear = #para(ibudgetyear)
#end
order by iautoid desc
#end

#sql("findPeriodByKv")
	select * from bas_period where 1=1
		#if(periodId)
			and iautoid = #(periodId)
		#end
		#if(ibudgetyear)
			and ibudgetsyear = #(ibudgetyear)
		#end
		#if(ibudgettype)
			and ibudgettype = #(ibudgettype)
		#end
		#if(iservicetype)
			and iservicetype = #(iservicetype)
		#end
		#if(isenabled)
			and isenabled = #para(isenabled)
		#end
		#if(iorgid)
			and iorgid = #(iorgid)
		#end
		order by ibudgetyear desc
#end

#sql("findListModelByIds")
	select * from bas_period where iautoid in (#(iperiodids))
#end