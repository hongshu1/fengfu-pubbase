### --列表数据源
#sql("pageList")
SELECT
    t1.*,
    (SELECT TOP 1 Wa.cWhName FROM T_Sys_SaleDeliverDetail  LEFT JOIN Bd_Warehouse Wa ON WhCode = Wa.cWhCode) AS WhName,
    Rd.cRdName AS RdName,
    de.cDepName AS DepName,
    cus.cCusName AS CusName
FROM
    T_Sys_SaleDeliver t1
        LEFT JOIN Bd_Rd_Style Rd ON t1.RdCode = Rd.cRdCode
        LEFT JOIN Bd_Department de ON t1.DeptCode =de.cDepCode
        LEFT JOIN Bd_Customer cus ON  t1.VenCode=cus.cCusCode
WHERE 1=1
    #if(sqlids)
    AND t1.AutoID in (#(sqlids))
    #end
#if(billNo)
    AND t1.BillNo LIKE CONCAT('%', #para(BillNo), '%')
#end
#if(cCusCode)
    AND t2.WhCode LIKE CONCAT('%', #para(cCusCode), '%')
#end
#if(whName)
    AND t2.WhCode LIKE CONCAT('%', #para(WhCode), '%')
#end
#if(startTime)
    AND t1.CreateDate >= #para(startTime)
#end
#if(endTime)
    AND t1.CreateDate <= #para(endTime)
#end
#if(null != sortColumn)
    ORDER BY #(sortColumn) #(sortType)
#end
#end


### --导出列表
#sql("exportList")
SELECT
    t1.*,
    t2.WhCode,
    t2.PosCode,
    t2.CusBarcode,
    t2.Barcode,
    t2.InvCode,
    t2.Num,
    t2.Qty
FROM
    T_Sys_SaleDeliver t1
    JOIN T_Sys_SaleDeliverDetail t2 ON t1.AutoID = t2.MasID
WHERE 1=1
#if(billNo)
    AND t1.BillNo LIKE CONCAT('%', #para(BillNo), '%')
#end
#if(cCusCode)
    AND t2.WhCode LIKE CONCAT('%', #para(cCusCode), '%')
#end
#if(whName)
    AND t2.WhCode LIKE CONCAT('%', #para(WhCode), '%')
#end
#if(startTime)
    AND t1.CreateDate >= #para(startTime)
#end
#if(endTime)
    AND t1.CreateDate <= #para(endTime)
#end
#end

#sql("getLineData")
SELECT
    t1.*
FROM
    T_Sys_SaleDeliverDetail t1
where 1=1
#if(masId)
    and t1.MasID = #para(masId)
#end
#end


#sql("getFormDatas")
SELECT
    t1.*,
    t2.WhCode,
    (SELECT cWhName FROM Bd_Warehouse WHERE t2.WhCode = cWhCode) AS WhName,
    Rd.cRdName AS RdName,
    de.cDepName AS DepName,
    cus.cCusName AS CusName,
    t2.PosCode,
    t2.CusBarcode,
    t2.Barcode,
    t2.InvCode,
    t2.Num,
    ( SELECT SUM(Qty)  FROM T_Sys_SaleDeliverDetail WHERE t1.AutoID = t2.MasID ) AS MaxQty
FROM
    T_Sys_SaleDeliver t1
        LEFT JOIN T_Sys_SaleDeliverDetail t2 ON t1.AutoID = t2.MasID
        LEFT JOIN Bd_Rd_Style Rd ON t1.RdCode = Rd.cRdCode
        LEFT JOIN Bd_Department de ON t1.DeptCode =de.cDepCode
        LEFT JOIN Bd_Customer cus ON  t1.VenCode=cus.cCusCode
WHERE
    1 = 1
    #if(AutoID)
    and t1.AutoID = #para(AutoID)
    #end
#if(OrganizeCode)
    and t1.OrganizeCode = #para(OrganizeCode)
#end
ORDER BY
    t1.createDate DESC
#end







#sql("pushu")
SELECT
    t1.*
FROM
    T_Sys_SaleDeliver t1
WHERE 1=1
#if(ids)
    AND t1.AutoID in (#para(ids))
#end
#end

#sql("pushudetail")
SELECT
    t1.*
FROM
    T_Sys_SaleDeliverDetail t1
WHERE 1=1
#if(id)
    AND t1.MasID = #para(id)
#end
#end