#sql("CurrentStockByDatas")
select c.*, inv.InvName
from V_Sys_CurrentStock c
left join V_Sys_Inventory inv on c.Invcode = inv.InvCode
where c.OrganizeCode = #para(organizecode) #if(whcode)
and c.whcode =#para(whcode)
#end
#if(poscode)
and c.poscode =#para(poscode)
#end
#if(posname)
and c.posname =#para(posname)
#end
#if(invcode)
and c.invcode like concat ('%',#para(invcode),'%')
#end
#if(invname)
and inv.invname like concat ('%',#para(invname),'%')
#end
#end


#sql("CurrentStockd")
select c.*, inv.InvName
from V_Sys_CurrentStock c
left join V_Sys_Inventory inv on c.Invcode = inv.InvCode
where c.OrganizeCode = #para(organizecode)
#if(whcode)
and c.whcode =#para(whcode)
#end
#if(poscodeSql)
and c.poscode in (#(poscodeSql))
#end
#end


#sql("getdatas")
select * from T_Sys_StockCheckVouch where 1=1 and isDeleted='0'
#end



#sql("datas")
select s.*, w.whname
from #(getMomdataDbName()).dbo.T_Sys_StockCheckVouch s
left join V_Sys_WareHouse w
on s.whcode=w.whcode
where s.isDeleted='0'
#if(checktype)
and s.checktype = #para(checktype)
#end
#if(whname)
and ( w.whname like concat('%', #para(whname), '%')or w.whcode like concat('%' , #para(whname) , '%') )
#end
#if(billno)
and s.billno like concat('%', #para(billno), '%')
#end
#if(sdate)
and s.dcreatetime >= #para(sdate)
#end
#if(edate)
and s.dcreatetime <= #para(edate)
#end
order by dcreatetime desc
#end


#sql("autocompleteWareHouse")
SELECT
    wh.iAutoId,
    wh.cWhCode AS whcode,
    wh.cWhName AS whname
FROM
    Bd_Warehouse wh
WHERE 1=1
    AND  wh.isDeleted = 0
    AND  wh.isEnabled = 1
    AND  wh.cOrgCode = #para(OrgCode)
    #if(q)
        and( w.cWhName like concat ('%',#para(q),'%') or w.cWhCode like concat ('%',#para(q),'%'))
    #end
ORDER BY
    wh.dCreateTime DESC
#end

#sql("autocompletePosition")
SELECT
    p.cAreaCode AS PosCode,
    p.cAreaName AS PosName
FROM
    Bd_Warehouse wh
        LEFT JOIN Bd_Warehouse_Area p ON p.iWarehouseId = wh.iAutoId
WHERE 1=1
        AND p.cOrgCode = #para(OrgCode)
#if(q)
and p.PosName like concat ('%',#para(q),'%')
#end
and wh.cWhCode=#para(whcode)
#end


#sql("autocompleteUser")
SELECT p.id, p.username
FROM  jb_user p
WHERE 1=1
#if(q)
and p.username like concat ('%'
, #para(q)
, '%')
#end
group by p.id, p.username
#end


#sql("invDatas")
select inv.invname,
inv.invstd,
inv.unitName,
inv.InvClassCode,
p.PosName,
d.*
from
#(getMomdataDbName()).dbo.T_Sys_StockCheckVouchDetail d
left join V_Sys_Inventory inv
on inv.invcode=d.invcode
LEFT JOIN  #(getMomdataDbName()).dbo.T_Sys_Position p ON p.PosCode = d.PosCode
where masid=#(mid)
and (p.OrganizeCode=#para(orgcode)
OR p.OrganizeCode IS NULL)
#if(invcode)
and inv.invcode like concat ('%'
, #para(invcode)
, '%')
#end
#if(invname)
and inv.invname like concat ('%'
, #para(invname)
, '%')
#end
#if(posname)
and p.posname like concat ('%', #para(posname), '%')
#end
#if(status)
and d.status like concat ('%', #para(status), '%')
#end


order by d.poscode, d.invcode
#end


#sql("barcodeDatas")
SELECT inv.invname,
inv.invstd,
inv.unitName
inv.InvClassCode,
d.*
FROM
#(getMomdataDbName()).dbo.T_Sys_StockCheckDetail d
LEFT JOIN V_Sys_Inventory inv
ON inv.invcode = d.invcode
WHERE
d.sourceid = #para(stockcheckvouchdid)
and generatetype is null
#if(barcode)
and d.barcode =#para(barcode)
#end

#end


#sql("barcodeDatas_total")
select w.whname,
p.posname,
inv.invname,
inv.invstd,
inv.unitName,
t.*

from (select d.invcode,
d.qty,
d.barcode,
d.createdate,
m.whcode,
s.poscode,
COALESCE(d.GenerateType, 'app') AS source,
d.realqty,
d.adjustqty,
d.sourceid,
d.autoid
from
#(getMomdataDbName()).dbo.T_Sys_StockCheckVouch m
left join #(getMomdataDbName()).dbo.T_Sys_StockCheckVouchDetail s
on s.masid=m.autoid
left join #(getMomdataDbName()).dbo.T_Sys_StockCheckDetail d on s.autoid=d.sourceid
where
d.sourceid = #para(stockcheckvouchdid)

union all

SELECT top 10
bc.invcode, bcpos.qty, bcpos.barcode, bcpos.chgdate as createdate, bcpos.whcode, bcpos.poscode, 'web' as source, null as realqty, null as adjustqty, null as sourceid, null as autoid
FROM
#(getMomdataDbName()).dbo.T_Sys_StockBarcodePosition bcpos
INNER JOIN V_Sys_BarcodeDetail bc
ON bc.barcode= bcpos.barcode
where 1=1
and bcpos.whcode=#para(whcode)
and bcpos.poscode=#para(poscode)
and bcpos.invcode=#para(invcode)
and bcpos.barcode not in (select barcode from #(getMomdataDbName()).dbo.T_Sys_StockCheckDetail)) t

LEFT JOIN v_sys_inventory inv ON inv.invcode = t.invcode
left join V_Sys_WareHouse w on w.whcode = t.whcode
LEFT JOIN #(getMomdataDbName()).dbo.T_Sys_Position p
ON p.PosCode = t.PosCode and p.poscode!= null
#end


#sql("invDatasByIds")
select inv.invname,
inv.invstd,
inv.unitName,
inv.InvClassCode,
p.PosName,
d.*
from
#(getMomdataDbName()).dbo.T_Sys_StockCheckVouchDetail d
left join V_Sys_Inventory inv
on inv.invcode=d.invcode
LEFT JOIN  #(getMomdataDbName()).dbo.T_Sys_Position p ON p.PosCode = d.PosCode
where masid in (#(ids)) and ( p.OrganizeCode=#para(orgcode) OR p.OrganizeCode IS NULL)
order by d.poscode, d.invcode
#end


#sql("findCheckVouchDetailByMasIdAndInvcode")
select t1.* from T_Sys_StockCheckVouchDetail t1
where 1=1
#if(masid)
    and t1.maid = #para(masid)
#end
#if(invcode)
    and t1.invcode = #para(invcode)
#end
#end
