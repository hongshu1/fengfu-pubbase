/**
 * 登录页面使用
 */
function changeCaptcha(){
	$("#captcha").val("");
	$("#captchaimg").attr("src","admin/captcha?t="+new Date().getTime());
}
$(function(){
	changeParentLayerDialogBtnTitle(0,"登 录");
});
	var _submit_ing=false;
	function submitThisForm(successCallback){
		if(_submit_ing){
			return false;
		}
		_submit_ing=true;
		var form=$("#loginForm");
		if(FormChecker.check(form)){
			var url=form.action;
			LayerMsgBox.loading("正在登录...",10000);
			form.ajaxSubmit({
				type:"post",
				url:url,
				success:function(ret){
					if(ret.state=="ok"){
						LayerMsgBox.success(ret.msg,500,function(){
							if(successCallback){
								successCallback();
							}
							_submit_ing=false;
						});
					}else{
						changeCaptcha();
						LayerMsgBox.error(ret.msg,1500,function(){
							_submit_ing=false;
							});
					}
				},
				error:function(){
					changeCaptcha();
					LayerMsgBox.error("发生异常",1500,function(){
						_submit_ing=false;
						});
					}
			});
		}else{
			changeCaptcha();
			_submit_ing=false;
			}
	}
