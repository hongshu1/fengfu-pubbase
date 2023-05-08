#sql("findBySubContractOrderMId")
SELECT
	*
FROM
	PS_SubcontractOrderRef
WHERE
	iSubContractOrderDid IN ( SELECT iAutoId FROM PS_SubcontractOrderD oderd WHERE oderd.iSubcontractOrderMid = #para(iSubcontractOrderMid) )
#end

#sql("removeBySubContractOrderMId")
DELETE
	PS_SubcontractOrderRef
WHERE
	iSubContractOrderDid IN ( SELECT iAutoId FROM PS_SubcontractOrderD oderd WHERE oderd.iSubcontractOrderMid = #para(iSubcontractOrderMid) )
#end

#sql("findBySubContractOrderDIds")
SELECT
	*
FROM
	PS_SubcontractOrderRef
WHERE
	iSubContractOrderDid IN (
        #for (id : ids)
             '#(id)' #(for.last?'':',')
        #end
	)
#end

#sql("removeBySubContractOrderDIds")
DELETE
	PS_SubcontractOrderRef
WHERE
	iSubContractOrderDid IN (
        #for (id : ids)
             '#(id)' #(for.last?'':',')
        #end
	)
#end
