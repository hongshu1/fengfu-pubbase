#sql("list")
select a.*,e.cSpotCheckFormName ,f.cInvCode,f.cInvCode1,f.cInvName1,w.cWorkName,ws.cWorkShiftName,f.iAutoId as iinventoryid,
       CASE a.iAuditStatus
           WHEN 0 THEN
               '未审核'
           WHEN 1 THEN
               '待审核'
           WHEN 2 THEN
               '审核通过'
           WHEN 3 THEN
               '审核不通过'
           END AS statename,m.iCompQty
from PL_SpotCheckFormM a
         left join Bd_InventoryRoutingConfig b on a.iMoRoutingConfigId = b.iAutoId
         left join Mo_MoRouting c on a.iMoDocId= c.iMoDocId
         left join Bd_InventorySpotCheckForm d on d.iInventoryId = c.iInventoryId and a.iSpotCheckFormId=d.iSpotCheckFormId
         left join Bd_SpotCheckForm e on e.iAutoId = d.iSpotCheckFormId
         left join Bd_Inventory f on f.iAutoId = d.iInventoryId
         left join Mo_MoDoc m on m.iAutoId=a.iMoDocId
         left join Bd_WorkRegionM w on w.iAutoId=m.iWorkRegionMid
         left join Bd_WorkShiftM ws on ws.iAutoId=m.iWorkShiftMid
         left join Bd_Department de on de.iAutoId=m.iDepartmentId
where
      1=1
      #if(id)
      and a.iAutoId=#para(id)
      #end
      #if(iauditstatus)
      and a.iauditstatus=#para(iauditstatus)
      #end
     #if(cspotcheckformname)
      and e.cSpotCheckFormName=#para(cspotcheckformname)
      #end
      #if(cmodocno)
      and a.cmodocno=#para(cmodocno)
      #end
        #if(iworkregionmid)
      and m.iWorkRegionMid=#para(iworkregionmid)
      #end
     #if(iworkshiftmid)
      and m.iWorkShiftMid=#para(iworkshiftmid)
      #end
      #if(dcreatetime)
        AND convert(date,a.dCreateTime) >= convert(date,#para(dcreatetime))
    #end
    #if(ecreatetime)
        AND convert(date,a.dCreateTime) <= convert(date,#para(ecreatetime))
    #end
        #if(itype)
      and  a.iType=#para(itype)
    #end
#end