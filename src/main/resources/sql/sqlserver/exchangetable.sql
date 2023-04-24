#sql("exchangeTable")
SELECT *
FROM T_Sys_ExchangeTable
WHERE ExchangeID in (#para(ExchangeID))
#end