#sql("findAllProposalData")
select  
	bd.iautoid,bd.cdepcode,bd.cdepname,bd.ipid
from 
	bd_department bd where 1=1 and bd.isDeleted = 0 and bd.isEnabled = 1 and bd.isProposal = 1 
	#if(iorgid)
		and bd.iOrgId = #para(iorgid)
	#end
	#if(keywords)
		and (bd.cdepcode like concat('%',#para(keywords),'%') or bd.cdepname like concat('%',#para(keywords),'%'))
	#end
#end

#sql("findDepDataByPId")
select * from bd_department bd where 1=1 and bd.isDeleted = 0 and bd.isEnabled = 1 and bd.ipid = #para(ipid) 
	#if(iorgid)
		and bd.iOrgId = #para(iorgid)
	#end
	#if(enddepkeywords)
		and (bd.cdepcode like concat('%',#para(enddepkeywords),'%') or bd.cdepname like concat('%',#para(enddepkeywords),'%')) 
	#end
#end

#sql("findCheckedIds")
select ireldepid from bd_depref where 1=1 and isDeleted = '0'
	#if(iorgid)
		and iOrgId = #para(iorgid)
	#end
	#if(idepid)
		and idepid = #para(idepid)
	#end
#end

#sql("findDepRefDataForEndDep")
	select * from bd_depref where isDeleted = '0'
	#if(iorgid)
		and iOrgId = #para(iorgid)
	#end
	#if(idepid)
		and idepid = #para(idepid)
	#end
	#if(ireldepid)
		and ireldepid = #para(ireldepid)
	#end	
#end


#sql("findIsDefaultEndDepRecord")
select endbd.* from bd_depref dr 
left join bd_department bd on dr.idepid = bd.iautoid
left join bd_department endbd on dr.iRelDepId = endbd.iautoid and endbd.isDeleted = '0' and endbd.isEnabled = '1'
where bd.isDeleted = '0' and bd.isEnabled = '1' and dr.isdefault = '1'
#if(cdepcode)
	and bd.cdepcode = #para(cdepcode)
#end
#end