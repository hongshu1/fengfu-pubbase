#sql("findByInventoryId")
SELECT
	a.iAutoId,
	a.iInventoryId,
	a.iSeq,
	a.iLevel,
	a.cInvLev,
	a.isOutSourced,
	a.iQty,
	a.iWeight,
	a.cMemo,
	inv.cInvCode,
	inv.cInvName,
	inv.cInvStd,
	inv.cInvCode1,
	inv.cInvName1,
	inv.iPurchaseUomId,
	inv.iPkgQty,
	inv.cInvAddCode,
	inv.cInvAddCode1,
	pur.iYear,
	pur.iMonth,
	pur.iDay
FROM
    PS_PurchaseOrderD a
	INNER JOIN Bd_Inventory inv ON a.iInventoryId = inv.iAutoId AND inv.isDeleted = 0
	LEFT JOIN PS_PurchaseOrderD_Qty pur ON a.iAutoId = pur.iPurchaseOrderDid
WHERE a.isDeleted = '0'
#end

#sql("findAll")
SELECT
	a.*,
	putype.cPTName,
	ven.cVenName
FROM
	PS_PurchaseOrderM a
	LEFT JOIN Bd_PurchaseType putype ON putype.iAutoId = a.iPurchaseTypeId
	LEFT JOIN Bd_Vendor ven ON ven.iAutoId = a.iVendorId
WHERE
	a.IsDeleted = 0
	#if(cOrderNo)
        AND a.cOrderNo LIKE CONCAT('%',#para(cOrderNo),'%')
	#end
    #if(cVenName)
        AND ven.cVenName LIKE CONCAT('%',#para(cVenName),'%')
	#end
	#if(iAuditStatus)
        AND a.iAuditStatus = #para(iAuditStatus)
	#end
	#if(startDate)
        AND convert(char(10),a.dBeginDate,126) <= #para(startDate)
	#end
	#if(endDate)
        AND convert(char(10),a.dEndDate,126) >= #para(endDate)
	#end
 order by a.dCreateTime desc
#end

#sql("findBycOrder")
   SELECT
 orderm.cOrderNo DocNo,
 ven.cVenCode cvencode,
 orderm.cCreateName cmaker,
 orderm.dOrderDate dDate,
 '' cPersonCode,
 '普通采购' cBusType,
 '普通销售' cPTCode,
 	'' inum,
  orderm.cCurrency cexch_name,
	orderm.iExchangeRate iExchRate,
	orderm.iTaxRate iTaxRate,
	orderm.cMemo cmemo,
	inv.cInvCode cInvCode,
	inv.cInvName cInvName,
	Qty.iQty iQuantity,
	 CONCAT(Qty.iYear,'-',Qty.iMonth,'-',Qty.iMonth)dPlanDate,
		0 iQuotedPrice,
	 iSeq irowno,
	100 KL,
	0 iNatDisCount
FROM
 PS_PurchaseOrderD_Qty qty
 INNER JOIN PS_PurchaseOrderD orderd ON orderd.iAutoId = qty.iPurchaseOrderDid
 INNER JOIN PS_PurchaseOrderM orderm ON orderm.iAutoId = orderd.iPurchaseOrderMid
 INNER JOIN Bd_Inventory inv ON inv.iAutoId = orderd.iInventoryId
 LEFT JOIN Bd_Vendor ven ON ven.iAutoId = orderm.iVendorId
 LEFT JOIN bd_person per ON per.iAutoId= orderm.iDutyUserId
WHERE
 orderm.IAUTOID = #para(iautoid)
 ORDER BY Qty.iYear, Qty.iMonth, Qty.iDate
#end


#sql("findBycOrderNo")
SELECT
	ROW_NUMBER() OVER (ORDER BY inv.cInvCode) AS SequenceNumber,
	CONCAT(a.cBarcode,'-',cVersion)cBarcode,
	inv.cInvCode1,
	dPlanDate,
	iQty
FROM
	PS_PurchaseOrderDBatch a
	INNER JOIN PS_PurchaseOrderD d ON d.iAutoId = a.iPurchaseOrderDid
	INNER JOIN Bd_Inventory inv ON inv.iAutoId = a.iinventoryId
	INNER JOIN PS_PurchaseOrderM M ON M.iAutoId=d.iPurchaseOrderMid
WHERE
	cVersion = 00
	and M.iautoid=#para(iautoid)
#end

#sql("findByBarcodeOnOrder")
 SELECT
	CONCAT(a.cBarcode,'-',cVersion)cBarcode,
	cInvCode1,
	cInvName1,
	cInvStd,
	d.cAddress,
	dPlanDate,
	M.dOrderDate,
	cVenName,
	iQty,
	cOrderNo
FROM
	PS_PurchaseOrderDBatch a
	INNER JOIN PS_PurchaseOrderD d ON d.iAutoId = a.iPurchaseOrderDid
	INNER JOIN Bd_Inventory inv ON inv.iAutoId = a.iinventoryId
	INNER JOIN PS_PurchaseOrderM M ON M.iAutoId=d.iPurchaseOrderMid
	INNER JOIN Bd_Vendor V on V.iAutoId = M.iVendorId
WHERE
	cVersion=00
	and M.iautoid=#para(iautoid)
#end
