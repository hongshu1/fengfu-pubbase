#sql("paginateAdminDatas")
SELECT
        AuditState =
        CASE WHEN t1.iAuditStatus=0 THEN '未审核'
             WHEN t1.iAuditStatus=1 THEN '待审核'
             WHEN t1.iAuditStatus=2 THEN '审核通过'
             WHEN t1.iAuditStatus=3 THEN '审核不通过' END,
        TypeName =
        CASE
             WHEN t1.Type='OtherOutMES' THEN '特殊领料单'
             WHEN t1.Type='StockCheckVouch' THEN '库存盘点单'
             WHEN t1.Type='OtherOut' THEN '手动新增'END,
    t1.*,
    t4.cDepName,
    t2.cWhName,
    t5.cRdName
FROM
    T_Sys_OtherOut t1
    LEFT JOIN Bd_Warehouse t2 ON t2.cWhCode = t1.Whcode
    LEFT JOIN Bd_Department t4 ON t4.iAutoId = t1.DeptCode
    LEFT JOIN Bd_Rd_Style t5 ON t5.cRdCode = t1.RdCode
WHERE 1 = 1

    #if(billno)
        AND t1.BillNo like '%#(billno)%'
    #end
    #if(whname)
        AND t2.cWhName like '%#(whname)%'
    #end
    #if(deptname)
        AND t4.cDepName like '%#(deptname)%'
    #end
   #if(iorderstatus)
        AND t1.iAuditStatus = #para(iorderstatus)
    #end
    #if(OrgCode)
        AND t1.OrganizeCode = #para(OrgCode)
    #end
#if(startdate)
    and CONVERT(VARCHAR(10),t1.dCreateTime,23) >='#(startdate)'
#end
#if(enddate)
    and CONVERT(VARCHAR(10),t1.dCreateTime,23) <='#(enddate)'
#end
order by t1.dCreateTime desc
    #end

#sql("getOtherOutLines")
SELECT t2.*,
       i.*,
       u.cUomClassName,
       t3.cInvCCode,
       t3.cInvCName
FROM T_Sys_OtherOut t1,
     T_Sys_OtherOutDetail t2
         LEFT JOIN bd_inventory i ON i.cinvcode = t2.Invcode
         LEFT JOIN Bd_UomClass u ON i.iUomClassId = u.iautoid
         LEFT JOIN Bd_InventoryClass t3 ON i.iInventoryClassId = t3.iautoid
WHERE
        t1.AutoID = t2.MasID AND  t1.AutoID = '#(autoid)'
    #end




#sql("getMaterialsOutLines")
SELECT t2.*,
       i.*,
       u.cUomClassName,
       t3.cInvCCode,
       t3.cInvCName
FROM T_Sys_MaterialsOut t1,
     T_Sys_MaterialsOutDetail t2
         LEFT JOIN bd_inventory i ON i.cinvcode = t2.Invcode
         LEFT JOIN Bd_UomClass u ON i.iUomClassId = u.iautoid
         LEFT JOIN Bd_InventoryClass t3 ON i.iInventoryClassId = t3.iautoid
WHERE
    t1.AutoID = t2.MasID
    AND  t1.AutoID = '#(autoid)'
    #if(OrgCode)
        AND t1.OrganizeCode = #para(OrgCode)
    #end
    #end



#sql("moDetailData")
SELECT *
FROM V_Sys_MODetail
WHERE 1 = 1
    #if(monorow)
        AND MONoRow like '%#(monorow)%'
    #end
#end


#sql("getrcvMODetailList")
SELECT *
FROM V_Sys_MODetail
WHERE MOId = #(iautoid)
#end

#sql("getRDStyleDatas")
SELECT cRdCode,
       cRdName
FROM Bd_Rd_Style
WHERE cOrgCode = #(OrgCode)
AND bRdFlag = #(bRdFlag)
#end






#sql("otherOutBarcodeDatas")
select m.cOrderNo AS sourcebillno,
       a.cCompleteBarcode AS barcode,
       b.cInvCode AS invcode,
       b.cInvCode1,
       b.cInvName1,
       b.cInvName,
       b.cInvAddCode,
       a.dPlanDate AS plandate,
       b.cInvStd AS cinvstd,
       a.iQty AS qty,
       a.iAutoId AS autoid,
       m.cOrderNo AS SourceBillNo,
       m.iBusType AS SourceBillType,
       m.cOrderNo+ '-' + CAST ( tc.iseq AS NVARCHAR ( 10 ) ) AS SourceBillNoRow,
       m.cOrderNo AS SourceBillID,
       d.iAutoId AS SourceBillDid,
       m.iVendorId,
       v.cVenCode AS vencode,
       v.cVenName AS venname,
       uom.cUomCode AS purchasecuomcode,
       uom.cUomName AS purchasecuomname,
       config.iWarehouseId,
       wh.cWhCode AS WhCode,
       wh.cWhName AS whname,
       area.cAreaCode AS poscode,
       area.cAreaName AS posname,
       area.iMaxCapacity AS qtys
FROM
    PS_PurchaseOrderDBatch a
        LEFT JOIN Bd_Inventory b ON a.iinventoryId = b.iAutoId
        LEFT JOIN PS_PurchaseOrderD d ON a.iPurchaseOrderDid = d.iAutoId
        LEFT JOIN PS_PurchaseOrderM m ON m.iAutoId = d.iPurchaseOrderMid
        LEFT JOIN Bd_Vendor v ON m.iVendorId = v.iAutoId
        LEFT JOIN T_Sys_PUReceiveDetail pd ON pd.Barcode = a.cBarcode
        AND pd.isDeleted = '0'
        LEFT JOIN PS_PurchaseOrderD_Qty tc ON tc.iPurchaseOrderDid = d.iAutoId
        AND tc.iAutoId = a.iPurchaseOrderdQtyId
        LEFT JOIN Bd_Uom uom ON b.iPurchaseUomId = uom.iAutoId
        LEFT JOIN Bd_InventoryStockConfig config ON config.iInventoryId = b.iAutoId
        LEFT JOIN Bd_Warehouse_Area area ON area.iAutoId = config.iWarehouseAreaId
        LEFT JOIN Bd_Warehouse wh ON wh.iAutoId = config.iWarehouseId
where 1=1
    #if(q)
		and (b.cinvcode like concat('%',#para(q),'%') or b.cinvcode1 like concat('%',#para(q),'%')
			or b.cinvname1 like concat('%',#para(q),'%') or a.cCompleteBarcode like concat('%',#para(q),'%')
			or v.cVenCode like concat('%',#para(q),'%')
		)
	#end
	 #if(barcodes != null)
        AND a.cCompleteBarcode not in ( #(barcodes) )
    #end
    #if(detailHidden != null)
    AND  a.cCompleteBarcode not in ( #(detailHidden) )
    #end
    #if(barcode != null)
        AND a.cCompleteBarcode = #para(barcode)
    #end
		AND b.cOrgCode = #(orgCode)
#end


#sql("barcodeDatas")
SELECT *
FROM PS_PurchaseOrderDBatch
WHERE 1=1
    #if(detailHidden)
		and cCompleteBarcode = #para(detailHidden)
	#end
#end


#sql("pushU8List")
SELECT
    t1.AutoID,
    t1.Whcode,
    t1.OrganizeCode,
    t1.VenCode,
    vd.cVenName AS VenName,
    t1.BillDate,
    t2.Qty,
    t1.Type,
    t2.Barcode,
    t2.InvCode,
    t1.RdCode,
    rs.cRdName AS RdName,
    dt.cDepName,
    dt.cDepCode
FROM
    T_Sys_OtherOut t1
        LEFT JOIN T_Sys_OtherOutDetail t2 ON t2.MasID = t1.AutoID
        LEFT JOIN Bd_Vendor vd ON vd.cVenCode = t1.VenCode
        LEFT JOIN Bd_Rd_Style rs ON rs.cRdCode = t1.RdCode
        LEFT JOIN Bd_Department dt ON dt.iAutoId = t1.DeptCode
WHERE t1.AutoID IN (#para(autoid))
    #end

#sql("getCItemCCodeLines")
SELECT cItemCcode,
       cItemCname
FROM Bd_fitemss97class
WHERE 1 = 1
  AND IsDeleted = 0
    #if(q)
		and (cItemCcode like concat('%',#para(q),'%') or cItemCname like concat('%',#para(q),'%'))
	#end
		AND cOrgCode = #para(orgCode)
#end

#sql("getItemCodeLines")
SELECT
    t1.citemcode,t1.citemname,t1.citemccode
FROM
    Bd_fitemss97 t1
        LEFT JOIN Bd_fitemss97class t2 ON t1.citemccode = t2.cItemCcode
WHERE 1 = 1
  AND t1.IsDeleted = 0
    #if(q)
		and (t1.citemcode like concat('%',#para(q),'%') or t1.citemname like concat('%',#para(q),'%'))
	#end
		AND t1.cOrgCode = #(orgCode)
	#if(citemccode)
	    AND t1.citemccode =#para(citemccode)
	#end
#end
