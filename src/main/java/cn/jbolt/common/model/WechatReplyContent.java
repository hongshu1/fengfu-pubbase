package cn.jbolt.common.model;

import cn.jbolt.common.model.base.BaseWechatReplyContent;
import cn.jbolt.core.annotation.TableBind;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "main" , table = "jb_wechat_reply_content" , primaryKey = "id" , idGenMode = "snowflake")
public class WechatReplyContent extends BaseWechatReplyContent<WechatReplyContent> {

}
