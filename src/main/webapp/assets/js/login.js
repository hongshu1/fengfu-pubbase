var jbolt_login_js_version = "1.0.2";
/**
 * 登录页面使用
 */
//layer msg模块封装
var LayerMsgBox={
		alert:function(msg,icon,handler){
			if(icon){
				layer.alert(msg,{icon:icon}, function(index){
						if(handler){
							handler();
						}
					  layer.close(index);
					});  
			}else{
				layer.alert(msg, function(index){
					if(handler){
						handler();
					}
				  layer.close(index);
				});  
			}
			
		},
		/**
		 * 弹出成功信息,并执行回调方法
		 * @param msg
		 * @param time
		 * @param handler
		 */
		success:function(msg,time,handler){
			if(!msg){msg="操作成功";}
			if(!time){time=1000;}
			var index=layer.msg(msg,{time:time,icon:1},function(){
				if(handler){
					handler();
				}
			});
			return index;
		},


		/**
		 * 弹出Error,并执行回调方法
		 * @param msg
		 * @param time
		 */
		error:function(msg,time,handler){
			if(!msg){msg="错误";}
			if(!time){time=1500;}
			var index=layer.msg(msg,{time:time,icon:2},function(){
				if(handler){
					handler();
				}
			});
			return index;
		},
		/**
		 * 弹出进度
		 * @param msg
		 * @param time
		 */
		loading:function(msg,time,handler){
			if(!msg){msg="执行中...";}
			var index=null;
			time=(time?time:10000);
			if(time){
				index=layer.msg(msg,{time:time,icon:16,shade:0.3},function(){
					if(handler){
						handler();
					}
				});
			}else{
				index=layer.msg(msg,{icon:16});
			}
			return index;
		},
		closeLoading:function(){
			setTimeout(function(){
				layer.closeAll('dialog'); //关闭加载层
			}, 500);
		},
		closeLoadingNow:function(){
				layer.closeAll('dialog'); //关闭加载层
		}

}

/**
 * ajax封装
 */
var Ajax={
	 post:function(url,data,success,error){
				$.ajax({
					url:url,
					type:"post",
					dataType:"json",
					data:data,
					success:function(data){
						if(data.state=="ok"){
							if(success){
								success(data);
							}
						}else{
								LayerMsgBox.alert(data.msg,2);
								if(error){
									error();
								}
						}
					},
					error:function(){
						LayerMsgBox.alert("网络通讯异常",2);
						if(error){
							error();
						}
						
					}
					
				});
			}
}

function changeCaptcha(){
	$("#captcha").val("");
	$("#captchaimg").attr("src","admin/captcha?t="+new Date().getTime());
}
function checkForm(form){
	var username=$.trim(form.find("input[name='username']").val());
	if(!username){
		LayerMsgBox.error("请输入用户名");
		return false;
	}
	var password=$.trim(form.find("input[name='password']").val());
	if(!password){
		LayerMsgBox.error("请输入密码");
		return false;
	}
	var captchaInput=form.find("#captcha");
	if(captchaInput&&captchaInput.length>0){
		var captcha=$.trim(captchaInput.val());
		if(!captcha){
			LayerMsgBox.error("请输入验证码");
			return false;
		}
	}
	
	
	return true;
}
//提交登录
function submitForm(form){
		var sf=$(form);
		if(checkForm(sf)){
			LayerMsgBox.loading("登录中...",10000);
			Ajax.post("admin/login",sf.serialize(),function(res){
				LayerMsgBox.success("登录成功",1000,function(){
					var base=$("base").attr("href");
					if(base.charAt(base.length-1)=='/'){
						window.location.href=base+"admin";
					}else{
						window.location.href=base+"/admin";
					}
				});
			},function(){
				changeCaptcha();
			})
		}
		return false;
	}

	/**
	 * 初始化登录页面样式
	 * @returns
	 */
	function initLoginStyle(){
		var glassStyle=$.cookie('jbolt_login_glassStyle');
		var bgimgBlur=$.cookie('jbolt_login_bgimgBlur');
		var needNest=$.cookie('jbolt_login_nest');
		
		if(glassStyle&&glassStyle=="true"){
			$(".jbolt_loginform").addClass("jbolt_bg_glass");
		}else{
			$(".jbolt_loginform").removeClass("jbolt_bg_glass");
		}
		if(bgimgBlur&&bgimgBlur=="true"){
			$(".bodybgmask").addClass("blur");
		}else{
			$(".bodybgmask").removeClass("blur");
		}
		var startnest=true;
		if(needNest&&needNest=="false"||(!needNest&&systemLoginNest=="false")){
			startnest=false;
			//用户自己设置了false或者系统全局设置了false 但是用户的设置还未初始化的时候 就不启动nest
		}
		if(startnest){
			nest();
		}

	}
	/**
	 * 调用nest特效
	 * @returns
	 */
	function nest(){
		$.getScript("assets/plugins/canvas-nest/canvas-nest.umd.js").
		done(function(){
			new CanvasNest(document.body,{
			    color: '255,255,255',
			    opacity:0.5,
			    zIndex:1,
			    count: 200,
			  });
		});
		
	}

	$(function(){
		initLoginStyle();
		/*if(self!=top){
			if(parent.showReloginDialog){
				parent.showReloginDialog();
			}
		}else{
			initLoginStyle();
		}*/
		});