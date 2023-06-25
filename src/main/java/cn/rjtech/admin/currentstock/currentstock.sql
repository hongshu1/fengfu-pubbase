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
SELECT w.whname, w.whcode
FROM V_Sys_WareHouse w
WHERE w.OrganizeCode = #para(organizecode)
#if(q)
and( w.whname like concat ('%',#para(q),'%') or w.whcode like concat ('%',#para(q),'%'))
#end
group by w.whname, w.whcode
#end

#sql("autocompletePosition")
SELECT p.PosCode, p.PosName
FROM V_Sys_Position p
left join V_Sys_WareHouse w on w.whcode = p.whcode
WHERE p.OrganizeCode = #para(organizecode)
#if(q)
and p.PosName like concat ('%',#para(q),'%')
#end
#if(whcode)
and w.whcode=#para(whcode)
#end
group by p.PosCode, p.PosName
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

