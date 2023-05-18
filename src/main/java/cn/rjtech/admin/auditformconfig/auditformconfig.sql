#sql("pageList")
select * from Bd_AuditFormConfig where IsDeleted = '0'
#if(keywords)
and (cFormSn like '#(keywords)%' or cFormName like '%#(keywords)%')
#end
#end

#sql("resourceList")
select t1.iAutoId as iFormId, t1.cFormCode as cFormSn, t1.cFormName
from Bd_Form t1 where IsDeleted = '0' and not exists(select 1 from Bd_AuditFormConfig t2 where t1.iAutoId = t2.iFormId)
#if(itemHidden)
and t1.iAutoId not in (#(itemHidden))
#end
#if(keywords)
and (t1.cFormCode like '#(keywords)%' or t1.cFormName like '%#(keywords)%')
#end
#end
