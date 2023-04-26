#sql("findByDemandPlanMList")
SELECT
	d.iAutoId,
	d.iYear,
	d.iMonth,
	d.iDate,
	d.iQty,
	d.iDemandPlanMid,
	a.iInventoryId
FROM
	Mrp_DemandPlanD d
	INNER JOIN Mrp_DemandPlanM a ON a.iAutoId = d.iDemandPlanMid
	INNER JOIN Bd_Inventory inv ON inv.iautoId = a.iInventoryId AND inv.isDeleted = '0'
WHERE
	d.iStatus = 1
	AND a.IsDeleted = 0
	AND a.iStatus = 1
    AND a.iOrgId = #para(orgId)
    AND a.iVendorId = #para(iVendorId)
    AND convert(char(10),a.dBeginDate,126) <= #para(beginDate)
    AND convert(char(10),a.dEndDate,126) >=#para(endDate)
    AND inv.iProcessType = #para(processType)
#end

#sql("queryNotGenOrderNum")
SELECT
	COUNT(1)
FROM
	Mrp_DemandPlanD
WHERE
	iAutoId NOT IN (
        #for(v:ids)
            '#(v)' #(for.last?'':',')
        #end
	)
	AND iStatus = 1
	AND iGenType = 0
	AND iDemandPlanMid = #para(iDemandPlanMid)

#end

#sql("findAll")
SELECT
	d.*
FROM
	Mrp_DemandPlanD d
WHERE
    1 = 1
    #if(iDemandPlanMid)
       AND d.iDemandPlanMid = #para(iDemandPlanMid)
    #end
    #if(ids)
      AND  d.iAutoId NOT IN (
            #for(v:ids)
                '#(v)' #(for.last?'':',')
            #end
        )
    #end
#end
