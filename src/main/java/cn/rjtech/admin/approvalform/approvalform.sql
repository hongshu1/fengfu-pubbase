#sql("pageList")
select t1.iAutoId, t1.iFormId, t2.cFormCode as cFormSn, t2.cFormName from Bd_ApprovalForm t1
left join Bd_Form t2 on t1.iFormId = t2.iAutoId
where t1.iApprovalMid = '#(id)'
#end


#sql("resourceList")
select t1.iAutoId as iFormId, t1.cFormCode as cFormSn, t1.cFormName
from Bd_Form t1 where IsDeleted = '0' and isApproval = '1' and not exists(select 1 from Bd_ApprovalForm t2 where t1
.iAutoId = t2
.iFormId)
#if(itemHidden)
and t1.iAutoId not in (#(itemHidden))
#end
#if(keywords)
and (t1.cFormCode like '#(keywords)%' or t1.cFormName like '%#(keywords)%')
#end
#end
