#sql("getEmailList")
SELECT email
FROM jb_email_receiver
WHERE receiver_type = #para(receiver_type)
    AND enable = '1'
#end