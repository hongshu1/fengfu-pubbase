#sql("findByImdocId")
SELECT
    a.iautoid,
    a.iSeq,
    a.cBarcode,
    a.iQty,
    a.iPrintStatus,
    a.iStatus,
    a.iMoDocId,
    a.cversion,
    CASE
        a.iPrintStatus
        WHEN '1' THEN
            '未打印'
        WHEN '2' THEN
            '已打印'
        END AS iPrintStatusName,
    CASE
        a.iStatus
        WHEN '0' THEN
            '未报工'
        WHEN '1' THEN
            '已报工'
        END AS iStatusName,
        a.cCreateName,
        a.dCreateTime,
        a.cUpdateName,
        a.dUpdateTime
FROM
    Mo_MoInvBatch a
        LEFT JOIN Mo_MoDoc b ON b.iAutoId= a.iMoDocId
        where  1=1
       #if(imodocid)
       and a.iMoDocId=#para(imodocid)
       #end
       #if(iprintstatus)
       and a.iPrintStatus=#para(iprintstatus)
      #end
      #if(istatus)
       and a.istatus=#para(istatus)
      #end
#end

#sql("findLastProcess")   ####查找工单最后一道工序
SELECT TOP
    1 a.iOperationId,
        c.iAutoId
FROM
    Mo_MoRoutingConfig_Operation a
        LEFT JOIN Mo_MoRoutingConfig c ON a.iMoRoutingConfigId= c.iAutoId
        LEFT JOIN Mo_MoRouting b ON c.iMoRoutingId= b.iAutoId
        LEFT JOIN Mo_MoDoc m ON b.iMoDocId= m.iAutoId
where 1=1
#if(modocid)
and m.iAutoId=#para(modocid)
#end
ORDER BY
    a.iAutoId DESC
#end

#sql("findProcessPerson")      ###获取工单工序人员
SELECT
    a.*,p.cPsn_Name as jobanme
FROM
    Mo_MoRoutingConfig_Person a
        LEFT JOIN Bd_Person p ON a.IpersonId= p.iAutoId
        LEFT JOIN Mo_MoRoutingConfig c ON a.iMoRoutingConfigId= c.iAutoId
        LEFT JOIN Mo_MoRouting b ON c.iMoRoutingId= b.iAutoId
        LEFT JOIN Mo_MoDoc m ON b.iMoDocId= m.iAutoId
where 1=1
    #if(modocid)
and m.iAutoId=#para(imodocid)
#end
ORDER BY
    a.iAutoId DESC
#end
