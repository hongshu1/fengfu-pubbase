#sql("findByImdocId")
SELECT
    a.iSeq,
    a.cBarcode,
    a.iQty,
    a.iPrintStatus,
    a.iStatus,
    a.iMoDocId,
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