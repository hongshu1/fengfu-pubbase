
#sql("recpor")
select so.AutoID, CASE so.state
        WHEN 1 THEN
        '已保存'
				WHEN 2 THEN
        '待审批'
				WHEN 3 THEN
        '已审批'
				WHEN 4 THEN
        '审批不通过'
        END AS statename,so.state,so.BillNo as billno,so.CreateDate as createdate,so.DeptCode as deptcode,
				so.IRdCode as irdcode,so.ORdCode as ordcode,so.AuditPerson as auditperson,so.AuditDate as auditdate,so.Memo as memo,so.CreatePerson as createperson
FROM T_Sys_Assem so

where 1=1
	#if(billno)
		and so.BillNo like concat('%',#para(billno),'%')
	#end
	#if(deptcode)
		and so.DeptCode like concat('%',#para(deptcode),'%')
	#end
	#if(state)
		and so.state = #para(state)
	#end
	#if(startTime)
		and so.CreateDate >= #para(startTime)
	#end
	#if(endTime)
		and so.CreateDate <= #para(endTime)
	#end
ORDER BY so.CreateDate DESC
#end

#sql("dList")
SELECT  a.*
FROM T_Sys_AssemDetail a
where a.isDeleted = '0'
	#if(masid)
		and a.MasID = #para(masid)
	#end
ORDER BY a.ModifyDate DESC
#end



#sql("dictionary")
SELECT  a.*
FROM #(getBaseDbName()).dbo.jb_dictionary a
where a.type_id='1659456737559646208' and a.enable='1'
	#if(q)
		and (a.name like concat('%',#para(q),'%') OR a.type_key like concat('%',#para(q),'%'))
	#end
ORDER BY a.sort_rank asc
#end


#sql("style")
SELECT  a.*
FROM Bd_Rd_Style a
where a.isDeleted = '0'
	#if(brdflag)
		and a.bRdFlag = #para(brdflag)
	#end
	#if(q)
		and (a.cRdCode like concat('%',#para(q),'%') OR a.cRdName like concat('%',#para(q),'%'))
	#end

ORDER BY a.dUpdateTime asc
#end




#sql("getBarcodeDatas")
select top #(limit)
       t1.Barcode,
       t1.SourceID as SourceBIllNoRow,
       t1.SourceBillType as SourceBillType,
       t1.SourceBillNo,t1.Qty,t1.BarcodeDate,
       t1.BarcodeID as SourceBillID,
       t1.MasID as SourceBillDid,
       t1.pat,
       i.*,

       (SELECT cUomName FROM Bd_Uom WHERE i.iInventoryUomId1 = iautoid) as InventorycUomName,
       (SELECT cUomName FROM Bd_Uom WHERE i.iPurchaseUomId = iautoid) as PurchasecUomName,
       (SELECT cContainerCode FROM Bd_Container WHERE i.iContainerClassId = iautoid) as cContainerCode,
       u.cUomClassName,
       t3.cInvCCode,
       t3.cInvCName,
       t4.cEquipmentModelName,
			 ic.iBeforeInventoryId as iciBeforeInventoryId,
			 ii.cInvCode as iicInvCode,
			 ii.cInvName as iicInvName,
			 ii.cInvCode1 as iicInvCode1,
			 ii.cInvName1 as iicInvName1,
			 ii.cInvStd as iicInvStd,
			 ii.iInventoryUomId1 as iiiInventoryUomId1,
			 ui.cUomClassName as iicUomClassName,
			 ii.iEquipmentModelId as iiiEquipmentModelId,
			 t5.cEquipmentModelName as iicEquipmentModelName

from V_Sys_BarcodeDetail t1
LEFT JOIN bd_inventory i ON i.cinvcode = t1.Invcode
LEFT JOIN Bd_UomClass u ON i.iUomClassId = u.iautoid
LEFT JOIN Bd_InventoryClass t3 ON i.iInventoryClassId = t3.iautoid
LEFT JOIN Bd_EquipmentModel t4 ON i.iEquipmentModelId = t4.iautoid
LEFT JOIN Bd_InventoryChange ic on i.iAutoId = ic.iBeforeInventoryId
LEFT JOIN bd_inventory ii on ii.iAutoId = ic.iAfterInventoryId
LEFT JOIN Bd_UomClass ui ON ii.iUomClassId = ui.iautoid
LEFT JOIN Bd_EquipmentModel t5 ON ii.iEquipmentModelId = t5.iautoid
where 1=1
    #if(q)
		and (i.cinvcode like concat('%',#para(q),'%') or i.cinvcode1 like concat('%',#para(q),'%')
			or i.cinvname1 like concat('%',#para(q),'%') or i.cinvstd like concat('%',#para(q),'%')
			or u.cUomClassName like concat('%',#para(q),'%') or t1.Barcode like concat('%',#para(q),'%')
		)
	#end
	 AND t1.OrganizeCode = #(orgCode)
#end
