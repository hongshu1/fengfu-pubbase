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
 CONCAT(Qty.iYear,'-',Qty.iMonth,'-',Qty.iDate)dDate,
 per.cPsn_Num cPersonCode,
 '普通采购' cBusType,
 cPTcode cPTCode,
 '' cdepcode,
 	'' inum,
  orderm.cCurrency cexch_name,
	orderm.iExchangeRate iExchRate,
	orderm.iTaxRate iTaxRate,
	orderm.cMemo cmemo,
	inv.cInvCode cInvCode,
	inv.cInvName cInvName,
	Qty.iQty iQuantity,
	qty.iSeq,
	0 iQuotedPrice,
	iSeq irowno,
	100 KL,
	0 iNatDisCount,
	cPTName cPayType

FROM
 PS_PurchaseOrderD_Qty Qty
 INNER JOIN PS_PurchaseOrderD orderd ON orderd.iAutoId = qty.iPurchaseOrderDid
 INNER JOIN PS_PurchaseOrderM orderm ON orderm.iAutoId = orderd.iPurchaseOrderMid
 INNER JOIN Bd_Inventory inv ON inv.iAutoId = orderd.iInventoryId
 LEFT JOIN Bd_PurchaseType TP  ON orderm.iPurchaseTypeId =TP.iAutoId
 LEFT JOIN Bd_Vendor ven ON ven.iAutoId = orderm.iVendorId
 LEFT JOIN bd_person per ON per.iAutoId= orderm.iDutyUserId
 LEFT JOIN  Bd_Department dt on dt.iAutoId = orderm.iDepartmentId

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
    d.isDeleted = 0
	and M.iautoid=#para(iautoid)
	#if(isEnabled_type)
        AND a.isEffective = #para(isEnabled_type)
    #end
#end

#sql("findBycOrderNo2")
		SELECT
	ROW_NUMBER() OVER (ORDER BY a.dPlanDate,inv.cInvCode,a.cVersion) AS SequenceNumber,
	b.iPurchaseOrderdQtyId,
	inv.cInvCode,
	inv.cInvCode1,
	inv.cInvName1,
	a.dPlanDate,
		CONCAT(a.cBarcode,'-',a.cVersion)cBarcode,
		a.iQty,
		CONCAT(a.cSourceBarcode,'-',a.cSourceVersion)cBarcodeVersion,
		iSourceQty,
		iPurchaseOrderdBatchId
FROM
	PS_PurchaseOrderDBatchVersion a
	inner JOIN PS_PurchaseOrderDBatch b on b.iAutoId = a.iPurchaseOrderdBatchId
	INNER JOIN Bd_Inventory inv ON inv.iAutoId = a.iInventoryId
WHERE
	a.cVersion != 00
	and a.iPurchaseOrderMid =#para(iautoid)
	order by a.dPlanDate,inv.cInvCode,a.cVersion asc
#end


#sql("findByBarcodeOnOrder")
 SELECT top 1
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

#sql("findByBarcodeOnOrder2")
 SELECT
   t.*
FROM
    (
						SELECT
						b.iPurchaseOrderdQtyId,
						inv.cInvCode,
						inv.cInvCode1,
						inv.cInvName1,
						cInvStd,
						a.dPlanDate,
							CONCAT(a.cBarcode,'-',a.cVersion)cBarcode,
							a.iQty,
							a.cVersion,
							CONCAT(a.cSourceBarcode,'-',a.cSourceVersion)cBarcodeVersion,
							iSourceQty,
							 MAX(a.cVersion) OVER (PARTITION BY b.iPurchaseOrderdQtyId) max_version,
							 V.cVenName,
							 orderD.cAddress,
							 M.cOrderNo,
							 M.dOrderDate
					FROM
						PS_PurchaseOrderDBatchVersion a
						inner JOIN PS_PurchaseOrderDBatch b on b.iAutoId = a.iPurchaseOrderdBatchId
						INNER JOIN Bd_Inventory inv ON inv.iAutoId = a.iInventoryId
						INNER JOIN PS_PurchaseOrderM M ON M.iAutoId = a.iPurchaseOrderMid
						INNER JOIN PS_PurchaseOrderD orderD on orderD.iPurchaseOrderMid=M.iAutoId
						INNER JOIN Bd_Vendor V on V.iAutoId = M.iVendorId
					WHERE
						a.iPurchaseOrderMid =#para(iautoid)
						and a.cVersion!=00
    ) t
WHERE
    t.cVersion = t.max_version
ORDER BY
    t.dPlanDate,
    t.cInvCode,
    t.cVersion ASC
#end

#sql("getWhcodeAll")
        SELECT  top #(limit) iautoid,cWhName cdistrictname   FROM  Bd_Warehouse WHERE 1=1 and isEnabled =1
        #if(q)
        and (
            cWhCode like concat('%',#para(q),'%') or cWhName like concat('%',#para(q),'%')
        )
        #end
 #end