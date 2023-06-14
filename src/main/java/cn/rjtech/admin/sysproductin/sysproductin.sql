
#sql("recpor")
select so.AutoID, CASE so.iAuditStatus
        WHEN 0 THEN
        '未审核'
				WHEN 1 THEN
        '待审核'
				WHEN 2 THEN
        '审核通过'
				WHEN 3 THEN
        '审核不通过'
        END AS statename,so.iAuditStatus,so.BillNo as billno,so.dcreatetime,so.Whcode,ck.cWhName as repositoryname,so.DeptCode,dept.cDepName as deptname,
				so.RdCode,rd.cRdName as rdcodename,so.BillType,so.Memo,so.cupdatename as name,so.cAuditname as sname
FROM T_Sys_ProductIn so
LEFT JOIN Bd_Warehouse ck on so.Whcode = ck.cWhCode
LEFT JOIN Bd_Department dept on so.DeptCode =dept.cDepCode
LEFT JOIN Bd_Rd_Style rd on so.RdCode = rd.cRdCode
where so.isDeleted = '0'
#if(billno)
	and so.BillNo like concat('%',#para(billno),'%')
#end
#if(deptcode)
	and so.DeptCode = #para(deptcode)
#end
#if(whcode)
	and so.Whcode = #para(whcode)
#end
#if(startTime)
	and so.dcreatetime >= #para(startTime)
#end
#if(endTime)
	and so.dcreatetime <= #para(endTime)
#end
ORDER BY so.dupdatetime DESC
#end


#sql("dList")
SELECT
	a.*, t1.Barcode,
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
	t4.cEquipmentModelName
FROM T_Sys_ProductInDetail a
	left join  V_Sys_BarcodeDetail t1 on t1.Barcode = a.Barcode
	left join bd_inventory i on i.cinvcode = t1.Invcode
	left join Bd_UomClass u on i.iUomClassId = u.iautoid
	left join Bd_InventoryClass t3 on i.iInventoryClassId = t3.iautoid
	left join Bd_EquipmentModel t4 on i.iEquipmentModelId = t4.iautoid
where 1=1
#if(masid)
	and a.MasID = #para(masid)
#end
	order by a.dupdatetime desc
#end

#sql("wareHouse")
select
    a.*
from Bd_Warehouse a
where 1=1
#if(q)
	and (a.cWhCode like concat('%',#para(q),'%') OR a.cWhName like concat('%',#para(q),'%'))
#end
#end



#sql("RdStyle")
select
    a.*
from Bd_Rd_Style a
where a.bRdFlag = '1'
#if(q)
	and (a.cRdCode like concat('%',#para(q),'%') OR a.cRdName like concat('%',#para(q),'%'))
#end
#end


#sql("Department")
select
	a.*
from Bd_Department a
where 1=1
#if(q)
	and (a.cdepCode like concat('%',#para(q),'%') OR a.cdepName like concat('%',#para(q),'%'))
#end
#end

#sql("selectname")
select
    u.*
from #(getBaseDbName()).dbo.jb_user u
where 1=1
#if(username)
	and u.username= #para(username)
#end
#end