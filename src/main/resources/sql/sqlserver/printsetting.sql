#sql("getPrintMachineCode")
select PrintMachineCode
from T_Sys_PrintSetting
where 1=1
#if(organizeCode)
    AND organizeCode = #para(organizeCode)
#end
#if(tag)
    AND tag = #para(tag)
#end
#end

#sql("valueandsymbolSplit")
select * FROM dbo.F_Sys_Split(#para(value),#para(symbol)
#end

#sql("getPrintSetting")
select * from T_Sys_PrintSetting
where 1=1
#if(tag)
    AND tag = #para(tag)
#end
#if(reportfilename)
    and reportfilename= #para(reportfilename) and isnull(ExecMethod,'') != ''
#end
#end

#sql("list")
select * from T_Sys_PrintSetting
    where 1=1
    #if(keywords)
   and dbalias LIKE CONCAT('%', #para(keywords), '%') or SourceID LIKE CONCAT('%', #para(keywords), '%') or MacID LIKE CONCAT('%', #para(keywords), '%') or ReportFileName LIKE CONCAT('%', #para(keywords), '%')
    #end
    #if(exectype)
    and exectype LIKE CONCAT('%', #para(exectype), '%')
    #end
    #if(sourcetype)
     and sourcetype LIKE CONCAT('%', #para(sourcetype), '%')
    #end
 #end