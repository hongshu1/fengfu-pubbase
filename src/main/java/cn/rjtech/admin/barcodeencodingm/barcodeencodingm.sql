#sql("findbarcodeencodingmId")
select * from bd_barcodeencodingm  where 1=1
    #if(keywords??)
#setLocal(kw=SqlUtil.likeValue(keywords))
 and ((ruleCode like '%#(kw)%') or (ruleName like '%#(kw)%') or (isBox like '%#(kw)%')
 or (orgcode like '%#(kw)%') or (createby like '%#(kw)%') or (updateby like '%#(kw)%')
)
#end
order by ruleCode desc
#end



#sql("findByCrulecode")
select *
from bd_barcodeencodingm
where corgcode=#para(corgcode)
and crulecode=#para(crulecode)
#end


#sql("findByCitem")
select *
from bd_barcodeencodingm
where corgcode=#para(corgcode)
and citem=#para(citem)
and istate='1'
#end

