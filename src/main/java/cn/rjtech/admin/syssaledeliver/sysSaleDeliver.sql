### --列表数据源
#sql("pageList")
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
    T_Sys_SaleDeliver t1 JOIN T_Sys_SaleDeliverDetail t2 ON t1.AutoID = t2.MasID
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

#sql("getLineDatas")
SELECT
    t1.*
FROM
    T_Sys_SaleDeliverDetail t1
where 1=1
#if(null!=masId)
and t.MasID = #para(masId)
#end
#end