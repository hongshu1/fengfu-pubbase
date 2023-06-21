#sql("paginateAdminDatas")
	select pm.* from pl_investment_plan pm
		where 1=1
	#if(cdepcode)
		and pm.cdepcode = #para(cdepcode)
	#end
	#if(ibudgetyear)
		and pm.ibudgetyear = #(ibudgetyear)
	#end
	#if(ibudgettype)
		and pm.ibudgettype = #para(ibudgettype)
	#end
	#if(iauditstatus)
		and pm.iauditstatus in (#(iauditstatus))
	#end
	#if(ieffectivestatus)
		and pm.ieffectivestatus in (#(ieffectivestatus))
	#end	
	### 超级管理员不过滤权限部门
	#if(!isSystemAdmin)
	    ### 存在角色部门配置过滤处理
	    #if(accessCdepCodes && accessCdepCodes.size() > 0)
	        AND pm.cDepCode IN (
	            #for(code:accessCdepCodes)
	                '#(code)' #(for.last?'':',')
	            #end
	        )
	    #end
	#end	
	order by pm.iautoid desc
#end

#sql("isExistsProposalDatas")
select count(1) from pl_proposald pd where exists (
	select 1 from pl_investment_plan ip
		left join pl_investment_plan_item ipi on ip.iautoid = ipi.iplanid 
		where ip.iautoid = #para(iplanid) and pd.iSourceId = ipi.iautoid
)
#end