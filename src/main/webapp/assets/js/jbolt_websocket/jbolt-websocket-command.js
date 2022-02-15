/**
 * JBolt websocket command配置和handler处理
 * JBolt 内置处理 请勿修改
 * 想扩展其他自定义指令 请移步到jbolt-websocket-common-extend.js
 */
export default {
	/**
	 * 接收服务器端处理完msgcenter_check_unread指令结果 并执行响应
	 * @param res  {type:3,from:xxx,to:xxx,command:xxx,data:xxx}
	 */
	msgcenter_check_unread:function(res){
		if(res.data){
			showMsgCenterRedDot();
		}else{
			hideMsgCenterRedDot();
		}
	},
	new_notice:function(res){
		showMsgCenterRedDot();
		JBoltNotifyBox.success({msg:res.data});
	},
	new_todo:function(res){
		showMsgCenterRedDot();
		JBoltNotifyBox.success({msg:res.data});
	},
	user_forced_offline:function(res){
		JBoltNotifyBox.warning({msg:res.data});
	},
	user_terminal_offline:function(res){
		JBoltNotifyBox.warning({msg:res.data});
	},
	/**
	 * ping指令处理
	 * @param res  {type:3,from:xxx,to:xxx,command:xxx,data:xxx}
	 */
	ping:function(res){
		jboltlog(res);
	},
	/**
	 * 接收pong指令处理
	 * @param res  {type:3,from:xxx,to:xxx,command:xxx,data:xxx}
	 */
	pong:function(res){
		jboltlog(res);
	},
	/**
	 * 接收server_time指令处理
	 * @param res  {type:3,from:xxx,to:xxx,command:xxx,data:xxx}
	 */
	server_time:function(res){
		jboltlog(res);
	}
	
};