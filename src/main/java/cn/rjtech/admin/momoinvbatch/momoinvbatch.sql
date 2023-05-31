#sql("findByImdocId")
SELECT
    a.iSeq,
    a.cBarcode,
    a.iQty,
    a.iPrintStatus,
    a.iStatus,
    a.iMoDocId
FROM
    Mo_MoInvBatch a
        LEFT JOIN Mo_MoDoc b ON b.iAutoId= a.iMoDocId
        where  1=1
       #if(imodocid)
       and a.iMoDocId=#para(imodocid)
#end
#end