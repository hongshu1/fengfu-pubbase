###---------------------------------------------------------物料需求计划计算---------------------

#sql("getInvInfoList")
###查询物料集信息 销售类型不为null
SELECT
    a.iAutoId AS invId, ###物料id
    a.cInvCode AS invCode,  ###物料编码
    a.cInvCode1,  ###客户部番
    a.cInvName1,  ###部品名称
    a.iSaleType,  ###销售类型
    i.iPsLevel,  ###排产层级
    b.iVendorId,  ###供应商id
    c.cVenCode,  ###供应商编码
    c.cVenName,  ###供应商名称
    a.iPkgQty,  ###包装数量
    d.iInnerInStockDays  ###标准在库天数
FROM Bd_Inventory AS a
         LEFT JOIN Bd_InventoryStockConfig AS b ON a.iAutoId = b.iInventoryId
         LEFT JOIN Bd_Vendor AS c ON b.iVendorId = c.iAutoId
         LEFT JOIN Bd_InventoryPlan AS d ON d.iInventoryId = a.iAutoId
         LEFT JOIN Bd_InventoryWorkRegion AS h ON a.iAutoId = h.iInventoryId AND h.isDefault = 1 AND h.isDeleted = 0
		 LEFT JOIN Bd_WorkRegionM AS i ON h.iWorkRegionMid = i.iAutoId AND i.isDeleted = 0
WHERE a.isDeleted = 0 AND a.iSaleType IS NOT NULL
#end

#sql("getInvInfoByidsList")
###查询物料集信息
SELECT
    a.iAutoId AS invId, ###物料id
    a.cInvCode AS invCode,  ###物料编码
    a.cInvCode1,  ###客户部番
    a.cInvName1,  ###部品名称
    a.iSaleType,  ###销售类型
    i.iPsLevel,  ###排产层级
    b.iVendorId,  ###供应商id
    c.cVenCode,  ###供应商编码
    c.cVenName,  ###供应商名称
    a.iPkgQty,  ###包装数量
    d.iInnerInStockDays  ###标准在库天数
FROM Bd_Inventory AS a
         LEFT JOIN Bd_InventoryStockConfig AS b ON a.iAutoId = b.iInventoryId
         LEFT JOIN Bd_Vendor AS c ON b.iVendorId = c.iAutoId
         LEFT JOIN Bd_InventoryPlan AS d ON d.iInventoryId = a.iAutoId
         LEFT JOIN Bd_InventoryWorkRegion AS h ON a.iAutoId = h.iInventoryId AND h.isDefault = 1 AND h.isDeleted = 0
         LEFT JOIN Bd_WorkRegionM AS i ON h.iWorkRegionMid = i.iAutoId AND i.isDeleted = 0
WHERE a.isDeleted = 0 AND a.iAutoId IN #(ids)
#end

#sql("selectBOMCompare")
SELECT
    bomc.iBOMMasterId,
    bomc.iInventoryId ,
    iUsageUOM = ISNULL(bomc.iQty, 1),
    iBOMMasterIdListStr= (
        STUFF(
                (SELECT ',' + cast(iAutoId as varchar)
                 FROM Bd_BOMMaster
                 WHERE iInventoryId= bomc.iInventoryId
                    FOR xml path('')
            ),1,1,''
                    )
        )
FROM
    Bd_BOMCompare AS bomc
WHERE isDeleted = '0' AND isEffective = 1
#end

#sql("getApsMonthPlanSumList")
###根据日期及条件获取月周生产计划表数据三班汇总
SELECT
    b.iYear,
    b.iMonth,
    b.iDate,
    (b.iQty2 + b.iQty3 + b.iQty4) AS iQty3,
    a.iInventoryId AS invId,
    c.cInvCode,
    c.cInvCode1,
    c.cInvName1,
    c.iSaleType
FROM Aps_WeekScheduleDetails AS a
         LEFT JOIN Aps_WeekScheduleD_Qty AS b ON a.iAutoId = b.iWeekScheduleDid
         LEFT JOIN Bd_Inventory AS c ON a.iInventoryId = c.iAutoId
WHERE a.isDeleted = '0'
  AND a.iInventoryId IN #(ids)
  AND
      (CAST(b.iYear  AS NVARCHAR(30))+'-'+CAST(CASE WHEN b.iMonth<10 THEN '0'+CAST(b.iMonth AS NVARCHAR(30) )
      ELSE CAST(b.iMonth AS NVARCHAR(30) ) END AS NVARCHAR(30)) +'-'+CAST( CASE WHEN b.iDate<10 THEN '0'+CAST(b.iDate AS NVARCHAR(30) )
      ELSE CAST(b.iDate AS NVARCHAR(30) )
      END AS NVARCHAR(30)) ) >= #para(startdate)
  AND
      (CAST(b.iYear  AS NVARCHAR(30))+'-'+CAST(CASE WHEN b.iMonth<10 THEN '0'+CAST(b.iMonth AS NVARCHAR(30) )
      ELSE CAST(b.iMonth AS NVARCHAR(30) ) END AS NVARCHAR(30)) +'-'+CAST( CASE WHEN b.iDate<10 THEN '0'+CAST(b.iDate AS NVARCHAR(30) )
      ELSE CAST(b.iDate AS NVARCHAR(30) )
      END AS NVARCHAR(30)) ) <= #para(enddate)
#end


#sql("getPinvInfoList")
###查询所需物料所有父级物料及其用量信息
select
    b.iInventoryId as invId,d.cInvCode as invCode,
    c.iAutoId as pInvId,c.cInvCode as pInvCode,
    c.iSaleType AS piSaleType,
    b.iQty as Realqty
from Bd_BomMaster as a
         left join Bd_BomCompare as b on a.iAutoId = b.iBOMMasterId
         left join Bd_Inventory as c on a.iInventoryId = c.iAutoId
         left join Bd_Inventory as d on b.iInventoryId = d.iAutoId
where a.isDeleted = 0 AND a.isEffective = 1
  AND b.iInventoryId in #(ids)
#end

#sql("getScheduEndDateList")
###查询各个排产层级的最大排产日期
SELECT iLevel,MAX(dScheduleEndTime) AS dScheduleEndTime
FROM Aps_WeekSchedule
WHERE IsDeleted = 0
GROUP BY iLevel
#end


#sql("getDemandComputeDQtyList")
###根据日期查询物料需求计划
SELECT c.cInvCode, a.iQty1,a.iQty2,a.iQty3,a.iQty4,a.iQty5
FROM Mrp_DemandComputeD AS a
         LEFT JOIN Mrp_DemandComputeM AS b ON a.iDemandComputeMid = b.iAutoId
         LEFT JOIN Bd_Inventory AS c ON a.iInventoryId = c.iAutoId
WHERE b.IsDeleted = 0
  AND a.iInventoryId IN #(ids)
  AND
        (CAST(a.iYear  AS NVARCHAR(30))+'-'+CAST(CASE WHEN a.iMonth<10 THEN '0'+CAST(a.iMonth AS NVARCHAR(30) )
        ELSE CAST(a.iMonth AS NVARCHAR(30) ) END AS NVARCHAR(30)) +'-'+CAST( CASE WHEN a.iDate<10 THEN '0'+CAST(a.iDate AS NVARCHAR(30) )
        ELSE CAST(a.iDate AS NVARCHAR(30) )
        END AS NVARCHAR(30)) ) = #para(startdate)
#end

#sql("deleteDemandComputeD")
###根据日期删除物料需求计划
DELETE FROM Mrp_DemandComputeD
WHERE (CAST(iYear  AS NVARCHAR(30))+'-'+CAST(CASE WHEN iMonth<10 THEN '0'+CAST(iMonth AS NVARCHAR(30) )
        ELSE CAST(iMonth AS NVARCHAR(30) ) END AS NVARCHAR(30)) +'-'+CAST( CASE WHEN iDate<10 THEN '0'+CAST(iDate AS NVARCHAR(30) )
        ELSE CAST(iDate AS NVARCHAR(30) )
        END AS NVARCHAR(30)) ) >= #para(startdate)
#end

#sql("getDemandComputeDList")
###根据条件查询物料需求计划
SELECT
    (CAST(b.iYear  AS NVARCHAR(30))+'-'+CAST(CASE WHEN b.iMonth<10 THEN '0'+CAST(b.iMonth AS NVARCHAR(30) )
    ELSE CAST(b.iMonth AS NVARCHAR(30) ) END AS NVARCHAR(30)) +'-'+CAST( CASE WHEN b.iDate<10 THEN '0'+CAST(b.iDate AS NVARCHAR(30) )
    ELSE CAST(b.iDate AS NVARCHAR(30) )
    END AS NVARCHAR(30)) ) AS planDate,
    b.iYear,
    b.iMonth,
    b.iDate,
    b.iQty1,
    b.iQty2,
    b.iQty3,
    b.iQty4,
    b.iQty5,
    b.iInventoryId AS invId,
    c.cInvCode,
    c.cInvCode1,
    c.cInvName1,
    b.iVendorId,
    e.cVenCode,
    e.cVenName,
    c.iPkgQty,
    f.iInnerInStockDays
FROM Mrp_DemandComputeD AS b
         LEFT JOIN Bd_Inventory AS c ON b.iInventoryId = c.iAutoId
         LEFT JOIN Bd_Vendor AS e ON b.iVendorId = e.iAutoId
         LEFT JOIN Bd_InventoryPlan AS f ON f.iInventoryId = c.iAutoId
WHERE 1 = 1
    #if(cvenname)
        AND e.cVenName LIKE CONCAT('%', #para(cvenname), '%')
    #end
    #if(cinvcode)
        AND c.cInvCode LIKE CONCAT('%', #para(cinvcode), '%')
    #end
    #if(cinvcode1)
        AND c.cInvCode1 LIKE CONCAT('%', #para(cinvcode1), '%')
    #end
    #if(cinvname1)
        AND c.cInvName1 LIKE CONCAT('%', #para(cinvname1), '%')
    #end
  AND
      (CAST(b.iYear  AS NVARCHAR(30))+'-'+CAST(CASE WHEN b.iMonth<10 THEN '0'+CAST(b.iMonth AS NVARCHAR(30) )
      ELSE CAST(b.iMonth AS NVARCHAR(30) ) END AS NVARCHAR(30)) +'-'+CAST( CASE WHEN b.iDate<10 THEN '0'+CAST(b.iDate AS NVARCHAR(30) )
      ELSE CAST(b.iDate AS NVARCHAR(30) )
      END AS NVARCHAR(30)) ) >= #para(startdate)
  AND
      (CAST(b.iYear  AS NVARCHAR(30))+'-'+CAST(CASE WHEN b.iMonth<10 THEN '0'+CAST(b.iMonth AS NVARCHAR(30) )
      ELSE CAST(b.iMonth AS NVARCHAR(30) ) END AS NVARCHAR(30)) +'-'+CAST( CASE WHEN b.iDate<10 THEN '0'+CAST(b.iDate AS NVARCHAR(30) )
      ELSE CAST(b.iDate AS NVARCHAR(30) )
      END AS NVARCHAR(30)) ) <= #para(enddate)
#end

//-----------------------------------------------------------------物料需求计划预示-----------------------------------------------


#sql("getMrpDemandForecastMList")
###查询物料需求计划预示主表
SELECT * FROM Mrp_DemandForecastM
WHERE IsDeleted = 0
    #if(cforecastno)
        AND cForecastNo LIKE CONCAT('%', #para(cforecastno), '%')
    #end
    #if(startdate)
        AND CONVERT(VARCHAR(10),dCreateTime,120) >= #para(startdate)
    #end
    #if(enddate)
        AND CONVERT(VARCHAR(10),dCreateTime,120) <= #para(enddate)
    #end
ORDER BY dPlanDate DESC
#end


#sql("getMrpDemandForecastDList")
###根据日期及条件获取物料需求计划预示子表数据
SELECT
    b.iYear,
    b.iMonth,
    b.iDate,
    b.iQty2,
    b.iInventoryId AS invId,
    c.cInvCode,
    c.cInvCode1,
    c.cInvName1,
    b.iVendorId,
    e.cVenCode,
    e.cVenName,
    c.iPkgQty,
    f.iInnerInStockDays
FROM Mrp_DemandForecastD AS b
         LEFT JOIN Bd_Inventory AS c ON b.iInventoryId = c.iAutoId
         LEFT JOIN Bd_Vendor AS e ON b.iVendorId = e.iAutoId
         LEFT JOIN Bd_InventoryPlan AS f ON f.iInventoryId = c.iAutoId
WHERE b.iDemandForecastMid = #para(idemandforecastmid)
    #if(cvenname)
        AND e.cVenName LIKE CONCAT('%', #para(cvenname), '%')
    #end
    #if(cinvcode)
        AND c.cInvCode LIKE CONCAT('%', #para(cinvcode), '%')
    #end
    #if(cinvcode1)
        AND c.cInvCode1 LIKE CONCAT('%', #para(cinvcode1), '%')
    #end
    #if(cinvname1)
        AND c.cInvName1 LIKE CONCAT('%', #para(cinvname1), '%')
    #end
  AND
      (CAST(b.iYear  AS NVARCHAR(30))+'-'+CAST(CASE WHEN b.iMonth<10 THEN '0'+CAST(b.iMonth AS NVARCHAR(30) )
      ELSE CAST(b.iMonth AS NVARCHAR(30) ) END AS NVARCHAR(30)) +'-'+CAST( CASE WHEN b.iDate<10 THEN '0'+CAST(b.iDate AS NVARCHAR(30) )
      ELSE CAST(b.iDate AS NVARCHAR(30) )
      END AS NVARCHAR(30)) ) >= #para(startdate)
  AND
      (CAST(b.iYear  AS NVARCHAR(30))+'-'+CAST(CASE WHEN b.iMonth<10 THEN '0'+CAST(b.iMonth AS NVARCHAR(30) )
      ELSE CAST(b.iMonth AS NVARCHAR(30) ) END AS NVARCHAR(30)) +'-'+CAST( CASE WHEN b.iDate<10 THEN '0'+CAST(b.iDate AS NVARCHAR(30) )
      ELSE CAST(b.iDate AS NVARCHAR(30) )
      END AS NVARCHAR(30)) ) <= #para(enddate)
#end



//-----------------------------------------------------------------物料到货计划-----------------------------------------------


#sql("getMrpDemandPlanMList")
###查询物料到货计划主表
SELECT * FROM Mrp_DemandPlanM
WHERE IsDeleted = 0
    #if(cplanno)
        AND cPlanNo LIKE CONCAT('%', #para(cplanno), '%')
    #end
     #if(istatus)
        AND iStatus = #para(istatus)
    #end
    #if(startdate)
        AND CONVERT(VARCHAR(10),dCreateTime,120) >= #para(startdate)
    #end
    #if(enddate)
        AND CONVERT(VARCHAR(10),dCreateTime,120) <= #para(enddate)
    #end
ORDER BY dPlanDate DESC
#end


#sql("getMrpDemandPlanDList")
###根据日期及条件获取物料到货计划子表数据
SELECT
    b.iYear,
    b.iMonth,
    b.iDate,
    b.iQty,
    b.iInventoryId AS invId,
    c.cInvCode,
    c.cInvCode1,
    c.cInvName1,
    b.iVendorId,
    e.cVenCode,
    e.cVenName,
    c.iPkgQty,
    f.iInnerInStockDays
FROM Mrp_DemandPlanD AS b
         LEFT JOIN Bd_Inventory AS c ON b.iInventoryId = c.iAutoId
         LEFT JOIN Bd_Vendor AS e ON b.iVendorId = e.iAutoId
         LEFT JOIN Bd_InventoryPlan AS f ON f.iInventoryId = c.iAutoId
WHERE b.iDemandPlanMid = #para(idemandplanmid)
    #if(cvenname)
        AND e.cVenName LIKE CONCAT('%', #para(cvenname), '%')
    #end
    #if(cinvcode)
        AND c.cInvCode LIKE CONCAT('%', #para(cinvcode), '%')
    #end
    #if(cinvcode1)
        AND c.cInvCode1 LIKE CONCAT('%', #para(cinvcode1), '%')
    #end
    #if(cinvname1)
        AND c.cInvName1 LIKE CONCAT('%', #para(cinvname1), '%')
    #end
  AND
      (CAST(b.iYear  AS NVARCHAR(30))+'-'+CAST(CASE WHEN b.iMonth<10 THEN '0'+CAST(b.iMonth AS NVARCHAR(30) )
      ELSE CAST(b.iMonth AS NVARCHAR(30) ) END AS NVARCHAR(30)) +'-'+CAST( CASE WHEN b.iDate<10 THEN '0'+CAST(b.iDate AS NVARCHAR(30) )
      ELSE CAST(b.iDate AS NVARCHAR(30) )
      END AS NVARCHAR(30)) ) >= #para(startdate)
  AND
      (CAST(b.iYear  AS NVARCHAR(30))+'-'+CAST(CASE WHEN b.iMonth<10 THEN '0'+CAST(b.iMonth AS NVARCHAR(30) )
      ELSE CAST(b.iMonth AS NVARCHAR(30) ) END AS NVARCHAR(30)) +'-'+CAST( CASE WHEN b.iDate<10 THEN '0'+CAST(b.iDate AS NVARCHAR(30) )
      ELSE CAST(b.iDate AS NVARCHAR(30) )
      END AS NVARCHAR(30)) ) <= #para(enddate)
#end