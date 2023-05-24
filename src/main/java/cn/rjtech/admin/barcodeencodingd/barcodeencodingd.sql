### 根据主表id查询细表数据
#sql("findBybarcodeencodingdId")
SELECT
    bd.*
FROM
    bd_barcodeencodingd bd
    LEFT JOIN bd_barcodeencodingm  bm  ON bm.iautoid = bd.mid
WHERE bm.CORGCODE=#para(corgcode)
and
bm.iautoid = #para(ibarcodeencodingmid)
order by iseq asc
#end

#sql("indexdata")
select
bm.*
from
bd_barcodeencodingm bm
where bm.corgcode=#para(corgcode)
order by  dcreatetime desc
#end



















