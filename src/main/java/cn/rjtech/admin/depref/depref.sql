#sql("findAllProposalData")
select  
	bd.iautoid,bd.cdepcode,bd.cdepname,bd.ipid
from 
	bd_department bd where 1=1 and bd.isDeleted = 0 and bd.isProposal = 1 and bd.isEnabled = 1
	#if(iorgid)
		and bd.iOrgId = #para(iorgid)
	#end
	#if(keywords)
		and (bd.cdepcode like concat('%',#para(keywords),'%') or bd.cdepname like concat('%',#para(keywords),'%'))
	#end
#end