var jbolt_admin_js_version="5.5.1";
//拿到window doc和body
var jboltJsDevMode=false;//当前模式 true是开发调试模式 影响加载插件和jboltlog
var jboltWindow=$(window);
var jboltDocument=$(document);
var jboltBody=$("body");
var jboltAdmin=jboltBody.find(".jbolt_admin");
var jboltWithTabs=jboltAdmin.hasClass("withtabs");
var jboltAdminMain=jboltBody.find(".jbolt_admin_main");
var jboltAdminLeftNavs=jboltBody.find(".jbolt_admin_left_navs");
var jboltAllTopNavs=$(".jbolt_admin_main_top>ul.jbolt_admin_topnavs>li");
var jboltHasTopNav=jboltAllTopNavs&&jboltAllTopNavs.length>0;
var jboltWindowWidth=jboltWindow.width();
var jboltWindowHeight=jboltWindow.height();
var JBOLT_AUTO_LOCK_SCREEN_SECONDS = 0;
var JBoltBaseTagHref=$("base").attr("href");
var msgCenterRedDot = $("#msgCenterRedDot");
var JBoltBaseTagHrefNoSchema=JBoltBaseTagHref.replace("http://","").replace("https://","").replace("www","");
//定义引入界面是否启用pjax
var mainPjaxContainerId='#jbolt-container';
var mainPjaxContainer=$(mainPjaxContainerId);
//summernote组件默认上传地址 JBOlt内封装
var summernote_img_uploadurl="admin/summernote/upload/image/";
//是否引入启用Select2
var hasImportSelect2=false;
//全局维护的一个上传文件组件
var multipleUploadFiles=new Array();
//jbolt 静态资源版本
var JBOLT_ASSETS_VERSION=$.trim($("#JBOLT_ASSETS_VERSION").text());
if(!JBOLT_ASSETS_VERSION){
	JBOLT_ASSETS_VERSION=randomId();
}
//上传路径pre
var JBOLT_BASE_UPLOAD_PATH_PRE=$.trim($("#JBOLT_BASE_UPLOAD_PATH_PRE").text());
//cityPicker组件数据
var JBoltCityPickerDatas=null;
//是否开启刷新从sessionStorag
var JBoltTabKeepAfterReload=("true"==$.trim($("#JBOLT_TAB_KEEP_AFTER_RELOAD").text()));
//是否包含pjax
var hasPjax=isHasPjax();
/**
 * Jbolt封装 tabs选项卡组件
 */
var jbolt_tabbar=$("#jbolt_tabbar");
var jbolt_tabs_container=$("#jbolt_tabs");
var jbolt_tabs_array=[];
//JBoltTable中所有表格 
var JBoltTableInts={};

/**
 * 根据ID获取ele
 */
var g = function (id) {
    return "string" == typeof id ? document.getElementById(id) : id;
};

/**
 * 获得鼠标选中内容
 */
function getSelectText(){
	var text;
	if(window.getSelection){
		var ws =window.getSelection();
		if(ws){
			text = ws.toString();
		}
	}else{
		var ds = document.selection;
		if(ds){
			text = ds.createRange().text;
		}
		
	}
	return text;
}

/**
 * 处理大数据量加载
 * @param data
 * @param pageSize
 * @param callback
 */
function jboltHandleBigData(data, pageSize,callback) {
	pageSize = pageSize || 10;
	var totalCount = data.length; // 共多少条
	var currentPageNumber = 1; // 当前页数
	var totalPageNumer = Math.ceil(totalCount / pageSize); //可分多少页,就是分割为多少个小数组

	var handler = function() {
		var start = (currentPageNumber - 1) * pageSize;
		var end = currentPageNumber * pageSize;
		var currentData = data.slice(start, end); // 当前页的数据
		if (typeof callback === "function") {
			callback(currentData, {
				totalCount:totalCount,
				totalPageNumer:totalPageNumer,
				currentPageNumber:currentPageNumber,
				pageSize:pageSize,
			});
		}
		// console.log(
		//   `共${totalCount}条,共${totalPageNumer}页,当前第${currentPageNumber}条`
		// );
		if (currentPageNumber < totalPageNumer) {
			window.requestAnimationFrame(handler);
		}
		currentPageNumber++;
	};
	handler();
}

/**
 * textarea 高度根据内容自适应
 * @param {Object} ele
 * @param {Object} minHeight
 */
function textareaAutoHeight(ele, minHeight) {
	ele.style.height = 'auto';
	ele.style.minHeight = (minHeight||34)+"px";
	ele.scrollTop = 0;//防抖动
	if(ele.scrollHeight>minHeight) {
		if (ele.hasAttribute("data-show-count")) {
			var showCount = $(ele).data("show-count");
			if (typeof (showCount) == "undefined" || (typeof (showCount) == "boolean" && showCount) || showCount.length == 0) {
				ele.style.height = ele.scrollHeight + 15 + 'px';
			} else {
				ele.style.height = ele.scrollHeight + 5 + 'px';
			}
		} else {
			ele.style.height = ele.scrollHeight + 5 + 'px';
		}
	}

}

/**
 * textarea
 */
var TextareaUtil={
	initEvent:function(parentEle){
		var parent=getRealParentJqueryObject(parentEle);
		if(!isOk(parent)){return false;}
		parent.on("keyup","textarea[data-auto-height]",function(){
			var autoHeight = $(this).data("auto-height")||34;
			if (this.hasAttribute("data-show-count")) {
				var showCount = $(this).data("data-show-count");
				if (typeof(showCount) == "undefined" || (typeof (showCount) == "boolean" && showCount) || showCount.length==0) {
					autoHeight = autoHeight + 15;
				}
			}
			textareaAutoHeight(this,autoHeight);
		});
		var that =this;
		parent.on("keyup","textarea[maxlength][data-show-count]",function(){
			var texta = $(this);
			var maxlength = texta.attr("maxlength");
			if(!maxlength){return;}
			var showCount=texta.data("show-count");
			if (typeof(showCount) == "undefined" || (typeof (showCount) == "boolean" && showCount) || showCount.length==0) {
				var smallCountBox = that.checkSmallCountBox(texta);
				if(isOk(smallCountBox)){
					that.updateCount(texta,smallCountBox);
				}
			}

		});
	},
	checkSmallCountBox:function(texta){
		var maxlength = texta.attr("maxlength");
		if(!maxlength){return;}
		var smallId = texta.data("small-id");
		var smallCountBox = null;
		if(smallId){
			smallCountBox = jboltBody.find("small#"+smallId);
		}else{
			smallId = "txsm_"+randomId();
			texta.data("small-id",smallId).attr("data-small-id",smallId);
			var smallCountBoxHtml= "<small id='"+smallId+"' style='position:relative;font-size:14px;display: inline-block;float:right;color:#777777;margin-top: -25px;padding-right:8px;text-align: right;'><span data-as='inputCount' tooltip data-title='已输入'>"+0+"</span><span>/</span><span data-as='leaveCount' tooltip data-title='剩余'>"+maxlength+"</span><span>/</span><span tooltip data-title='可输入总数'>"+maxlength+"</span></small>";
			texta.after(smallCountBoxHtml);
			smallCountBox = jboltBody.find("small#"+smallId);
			initToolTip(smallCountBox);
		}
		return smallCountBox;
	},
	updateCount:function(texta,smallCountBox){
		var maxlength = texta.attr("maxlength");
		if(!maxlength){return;}
		var inputCount = texta[0].value.length;
		var leaveCount = maxlength - inputCount;
		texta.data("input-count",inputCount);
		texta.data("leave-count",leaveCount);
		var inputCountSpan = smallCountBox.find("span[data-as='inputCount']");
		var leaveCountSpan = smallCountBox.find("span[data-as='leaveCount']");
		inputCountSpan.text(inputCount);
		leaveCountSpan.text(leaveCount);
		if(leaveCount<=0){
			leaveCountSpan.addClass("text-danger");
		}else{
			leaveCountSpan.removeClass("text-danger");
		}
	},
	initUI:function(parentEle) {
		var parent = getRealParentJqueryObject(parentEle);
		if (!isOk(parent)) {
			return false;
		}
		var that =this;
		parent.find("textarea[data-auto-height]").each(function () {
			var texa=$(this);
			var autoHeight = texa.data("auto-height")||34;
			var height = texa.outerHeight();
			var showCount = texa.data("data-show-count");
			if (typeof(showCount) == "undefined" || (typeof (showCount) == "boolean" && showCount) || showCount.length==0) {
				autoHeight = autoHeight + 15;
			}
			if(height < autoHeight){
				this.style.height = autoHeight + "px";
				this.style.minHeight = autoHeight + "px";
			}
		});
		parent.find("textarea[maxlength][data-show-count]").each(function () {
			var texta = $(this);
			var maxlength = texta.attr("maxlength");
			if(!maxlength){return;}
			var showCount = texta.data("show-count");
			if (typeof(showCount) == "undefined" || (typeof (showCount) == "boolean" && showCount) || showCount.length==0) {
				var smallCountBox = that.checkSmallCountBox(texta);
				if (isOk(smallCountBox)) {
					that.updateCount(texta, smallCountBox);
				}
			}

		});
	}
}


/**
 * replaceAll 处理
 */
if(!String.prototype.replaceAll){
	RegExp.quote = function (str) {
		return str.replace(/([.?*+^$[\]\\(){}|-])/g, "\\$1");
	};
	String.prototype.replaceAll = function (s1, s2) {
		return this.replace(new RegExp(RegExp.quote(s1), "gm"), s2);
	}
}
/**
 * trim处理
 */
if(!String.prototype.trim){
	String.prototype.trim=function(){return this.replace(/(^\s*)|(\s*$)/g, "");}
}

if(!String.prototype.startWith||!String.prototype.endWith){
	/**
	 * 构造startWith方法
	 */
	String.prototype.startWith = function(s) {
		if (s == null || s == "" || this.length == 0 || s.length > this.length)
			return false;
		if (this.substr(0, s.length) == s)
			return true;
		else
			return false;
		return true;
	};
	
	/**
	 * 构造endWith方法
	 */
	String.prototype.endWith = function(s) {
		if (s == null || s == "" || this.length == 0 || s.length > this.length)
			return false;
		if (this.substring(this.length - s.length) == s)
			return true;
		else
			return false;
		return true;
	}
}


if(JBoltBaseTagHrefNoSchema.startWith("://")){
	JBoltBaseTagHrefNoSchema=JBoltBaseTagHrefNoSchema.substring(3);
}
//已经加载的插件列表
var loadedPlugins={};
//第三方可动态加载的插件定义
var jboltPlugins={
		"morris":{"js":['assets/plugins/morris/morris.min.js','assets/plugins/morris/raphael.min.js'],"css":['assets/plugins/morris/morris.css']},
		"webcam":{"js":['assets/plugins/webcam/jquery.webcam.min.js']},
		"webcamjs":{"js":['assets/plugins/webcamjs/js/webcam.js','assets/plugins/webcamjs/js/cropbox.js'],"css":['assets/plugins/webcamjs/css/webcam.css']},
		"summernote":{"js":['assets/plugins/summernote/summernote-bs4.min.js','assets/plugins/summernote/lang/summernote-zh-CN.min.js'],"css":['assets/plugins/summernote/summernote-bs4.css']},
		"neditor":{"js":['assets/plugins/neditor/neditor.config.js','assets/plugins/neditor/neditor.all.js','assets/plugins/neditor/neditor.service.js']},
//		"fileinput":{"js":['assets/plugins/bootstrap-fileinput/js/plugins/canvas-to-blob.min.js','assets/plugins/bootstrap-fileinput/js/plugins/sortable.min.js','assets/plugins/bootstrap-fileinput/js/plugins/piexif.min.js','assets/plugins/bootstrap-fileinput/js/plugins/purify.min.js','assets/plugins/bootstrap-fileinput/js/fileinput.min.js','assets/plugins/bootstrap-fileinput/themes/fa/theme.min.js','assets/plugins/bootstrap-fileinput/themes/explorer-fa/theme.min.js','assets/plugins/bootstrap-fileinput/js/locales/zh.js'],"css":['assets/plugins/bootstrap-fileinput/css/fileinput.min.css','assets/plugins/bootstrap-fileinput/themes/explorer-fa/theme.min.css']},
		"fileinput":{"js":['assets/plugins/bootstrap-fileinput/last/js/plugins/sortable.min.js','assets/plugins/bootstrap-fileinput/last/js/plugins/piexif.min.js','assets/plugins/bootstrap-fileinput/last/js/fileinput.min.js','assets/plugins/bootstrap-fileinput/last/themes/fa/theme.min.js','assets/plugins/bootstrap-fileinput/last/themes/explorer-fa/theme.min.js','assets/plugins/bootstrap-fileinput/last/js/locales/zh.js'],"css":['assets/plugins/bootstrap-fileinput/last/css/fileinput.min.css','assets/plugins/bootstrap-fileinput/last/themes/explorer-fa/theme.min.css']},
		"imgviewer":{"js":['assets/plugins/viewer/js/viewer.min.js','assets/plugins/viewer/js/jquery-viewer.min.js'],"css":['assets/plugins/viewer/css/viewer.min.css']},
		"jbolttable":{"js":['assets/plugins/jbolt-table/jbolt-table'+(jboltJsDevMode?'':'.min')+'.js?v='+(jboltJsDevMode?(randomId()):JBOLT_ASSETS_VERSION)],'css':['assets/plugins/jbolt-table/jbolt-table'+(jboltJsDevMode?'':'.min')+'.css?v='+(jboltJsDevMode?(randomId()):JBOLT_ASSETS_VERSION)]},
		"jstree":{"js":['assets/plugins/jstree/jstree.min.js'],"css":['assets/plugins/jstree/themes/default/style.min.css']},
		"splitjs":{"js":['assets/plugins/splitjs/split/split.min.js']},
		"ipicker":{"js":['assets/plugins/ipicker/ipicker.min.js']},
		"autocomplete":{"js":['assets/plugins/jquery/autocomplete/autocomplete'+(jboltJsDevMode?'':'.min')+'.js']},
		"pagination":{"js":['assets/plugins/jquery/pagination/pagination.min.js']},
		"bootstrap-select":{"js":['assets/plugins/bootstrap-select/js/bootstrap-select.min.js','assets/plugins/bootstrap-select/js/i18n/defaults-zh_CN.min.js'],"css":['assets/plugins/bootstrap-select/css/bootstrap-select.min.css']},
		"echarts":{"js":['assets/plugins/echarts/echarts.min.js']},
		"pdfjs":{"js":['assets/plugins/pdfjs/pdf.js']},
		"videojs":{"js":['assets/plugins/videojs/video.min.js'],"css":['assets/plugins/videojs/video-js.min.css']},
		"jcalculator":{"js":['assets/plugins/jcalculator/jcalculator.js'],"css":['assets/plugins/jcalculator/jcalculator.css']},
		"fullcalendar":{"js":['assets/plugins/fullcalendar/main.min.js','assets/plugins/fullcalendar/locales/zh-cn.js'],css:['assets/plugins/fullcalendar/main.min.css']},
		"rangeslider":{"js":['assets/plugins/rangerslider/ion.rangeSlider.min.js'],css:['assets/plugins/rangerslider/ion.rangeSlider.min.css']},
		"hiprint":{"js":['assets/plugins/hiprint/plugins/jspdf/canvas2image.js','assets/plugins/hiprint/plugins/jspdf/canvg.min.js','assets/plugins/hiprint/plugins/jspdf/html2canvas.min.js','assets/plugins/hiprint/plugins/jspdf/jspdf.min.js','assets/plugins/hiprint/polyfill.min.js','assets/plugins/hiprint/hiprint.bundle.js','assets/plugins/hiprint/plugins/JsBarcode.all.min.js','assets/plugins/hiprint/plugins/qrcode.js','assets/plugins/hiprint/plugins/jquery.hiwprint.js'],css:['assets/plugins/hiprint/css/hiprint.css','assets/plugins/hiprint/css/print-lock.css',{url:"assets/plugins/hiprint/css/print-lock.css",media:"print"}]},
		"qiniu":{"js":['assets/plugins/qiniu/3.4.0/qiniu.min.js']},
		"clipboardjs":{"js":["assets/plugins/clipboard/clipboard.min.js"]}
		
}
/**
 * 网页剪贴板封装
 */
var JBoltClipboardUtil={
	init:function(){
		loadJBoltPlugin(['clipboardjs'],function(){
			var jbclipboard = new ClipboardJS('[data-clipboard-text],[data-clipboard-target],[data-clipboard-action]');
			jbclipboard.on('success', function(e) {
				LayerMsgBox.success("复制成功",500);
				e.clearSelection();
			});

			jbclipboard.on('error', function(e) {
				LayerMsgBox.error("复制失败",500);
			});
		});
	}
}
/**
 * 百度地图封装
 */
var JBoltBaiduMapUtil={
	init:function(apiVersion,ak,callback){
		window.JBoltBaiduMapLoadedCallback=function(){
			if(callback){
				callback();
			}
		}
		 var script = document.createElement('script');
		 script.type = 'text/javascript';
		 script.src = '//api.map.baidu.com/api?type=webgl&v='+apiVersion+'&ak='+ak+'&callback=JBoltBaiduMapLoadedCallback';
		 document.body.appendChild(script);
	}
}

/**
 * 加入插件集合
 * @param myPlugin
 * @returns
 */
function addPluginsToJBoltPluginMgr(myPlugin){
	if(isOk(myPlugin)&&jsonObjectValueIsOk(myPlugin)){
		Object.assign(jboltPlugins,myPlugin);
	}
}
/**
 * 指定元素全屏
 * 
 * @param element
 * @returns
 */
function launchFullscreen(element) {
	if(!element){
		element=document.documentElement;
	}
	element.isFullscreen=false;
  if(element.requestFullscreen) {
    element.requestFullscreen();
    element.isFullscreen=true;
  } else if(element.mozRequestFullScreen) {
    element.mozRequestFullScreen();
    element.isFullscreen=true;
  } else if(element.webkitRequestFullscreen) {
    element.webkitRequestFullscreen();
    element.isFullscreen=true;
  } else if(element.msRequestFullscreen) {
    element.msRequestFullscreen();
    element.isFullscreen=true;
  }
}
/**
 * 取消全屏
 * 
 * @param element
 * @returns
 */
function exitFullscreen(element) {
	if(!element){
		element=document.documentElement;
	}
  if(document.exitFullscreen) {
	  document.exitFullscreen();
	  element.isFullscreen=false;
  } else if(document.mozCancelFullScreen) {
	  document.mozCancelFullScreen();
	  element.isFullscreen=false;
  } else if(document.webkitExitFullscreen) {
	  document.webkitExitFullscreen();
	  element.isFullscreen=false;
  }
}

/**
 * 动态切换元素的全屏状态
 * @param element
 * @returns
 */
function toggleFullScreen(element){
	if(!element){
		element=document.documentElement;
	}
	if(element.isFullscreen){
		exitFullscreen(element);
	}else{
		launchFullscreen(element);
	}
}

/**
 * 将指定dom节点插入到另一个节点后
 * @param newnode 要插入的节点对象
 * @param oldnode 要插入的节点前的节点对象
 */
function insertNodeAfter(newnode,oldnode){
	//判断oldnode后面还有没有同类别的标记
	var nextnode = oldnode.nextSibling;
	if(nextnode){ //如果没有则为null,则为false,有的话就为true
		oldnode.parentNode.insertBefore(newnode,nextnode);
	}else{
		oldnode.parentNode.appendChild(newnode);
	}
}
/**
 * 过滤并高亮子节点
 * @param node
 * @param keywords
 * @returns
 */
function filterAndHighlightChildNodes(node,keywords){
	//直接是文字的 直接替换
	if(node.nodeName=="#text"){
		var nodeValue=node.nodeValue;
		var startValue,endValue,centerNode,endNode,index,length;
		if(nodeValue){
			index=nodeValue.indexOf(keywords);
			if(index!=-1){
				length=keywords.length;
				centerNode=document.createElement('span');
				centerNode.className="jb_fspan";
				centerNode.innerHTML='<span class="jb_filter_highlight">'+keywords+'</span>';
				node.nodeValue=nodeValue.substring(0,index);
				insertNodeAfter(centerNode,node);
				endValue=nodeValue.substring(index+length);
				if(endValue){
					endNode=document.createTextNode(endValue);
					insertNodeAfter(endNode,centerNode);
				}
			}
		}
	}else{
		var nodes=node.childNodes;
		if(isOk(nodes)){
			var size=nodes.length;
			for(var i=0;i<size;i++){
				filterAndHighlightChildNodes(nodes[i],keywords);
			}
		}
	}
}
/**
 * 关键词过滤并高亮
 * @param ele
 * @param keywords
 * @returns
 */
function filterAndHighlight(ele,keywords){
	if(keywords){
		var nodes=ele.childNodes;
		if(isOk(nodes)){
			var size=nodes.length;
			for(var i=0;i<size;i++){
				filterAndHighlightChildNodes(nodes[i],keywords);
			}
		}
	}
}
/**
 * 判断json对象是否所有属性里面至少一个属性有值
 */
var jsonObjectValueIsOk=function(obj){
	if(obj==null || typeof(obj)=="undefined"){
		return false;
	}
	var hasAttr=false;
	if(obj.length==undefined){
		hasAttr = Object.prototype.toString.call(obj).toLowerCase() == "[object object]" && JSON.stringify(obj)!='{}';
	}else{
		hasAttr = obj.length&&obj.length>0;
	}
	if(hasAttr){
		var isok=false;
		var columns=Object.keys(obj);
		var tempKey,value;
		for(var i in columns){
			tempKey=columns[i];
			value=obj[tempKey];
			isok=isOk(value) || (typeof(value)!=undefined&&value.toString()!="");
			if(isok){
				break;
			}
		}
		return isok;
	}
	return false;
}
/**
 * 判断数据不是有值的数据
 */
var notOk=function(obj){
	return !isOk(obj);
}
/**
 * 判断数据是否是正确有值的数据
 */
var isOk=function(obj){
	if(!obj){return false;}
	if(isArray(obj)){
		return obj.length>0;
	}
	var type=typeof(obj);
	var result=false;
	switch (type) {
	case "object":
		if(obj.length==undefined){
			result = (Object.prototype.toString.call(obj).toLowerCase() == "[object object]" && JSON.stringify(obj)!='{}');
		}else{
			result = (obj.length&&obj.length>0);
		}
		break;
	case "string":
//		result=(obj!=""&&obj!="0"&&obj.length>0);
		result=(obj!=""&&obj.length>0);
		break;
	case "number":
		result=(obj>0);
		break;
	case "boolean":
		result=obj;
		break;
	case "function":
		result=true;
		break;
	case "undefined":
		result=false;
		break;

	default:
		result=(obj.length&&obj.length>0);
		break;
	}
	return result;
}

/**
 * 输入控制台信息
 */
var jboltlog=function(msg){
	if(jboltJsDevMode){
		console.log("                        ");
		console.log(new Date().Format("yyyy-MM-dd HH:mm:ss:S"));
		console.log(msg);
		console.log("--------  end  ---------");
		console.log("                        ");
	}
}
/**
 * 全屏按钮
 */
var FullScreenBtnUtil={
	init:function(){
		jboltBody.on("click",'[data-fullscreenbtn]',function(){
			var btn=$(this);
			var target=btn.data("target");
			if(target){
				var targetEle;
				if(target.startWith("parent")){
					target=target.replace("parent",'');
					targetEle=btn.closest(target);
				}else{
					targetEle=jboltBody.find(target);
				}
				if(isOk(targetEle)){
					toggleFullScreen(targetEle[0]);
					var icon=btn.find("i.jbicon2.jbi-fullscreen,i.jbicon2.jbi-fullscreen-exit");
					if(isOk(icon)){
						icon.toggleClass("jbi-fullscreen");
						icon.toggleClass("jbi-fullscreen-exit");
					}
					disposeTooltip(btn);
				}
			}else{
				toggleFullScreen();
				var icon=btn.find("i.jbicon2.jbi-fullscreen,i.jbicon2.jbi-fullscreen-exit");
				if(isOk(icon)){
					icon.toggleClass("jbi-fullscreen");
					icon.toggleClass("jbi-fullscreen-exit");
				}
				disposeTooltip(btn);
			}
		});
	}
}

/**
 * 取消tooltip
 * @param ele
 * @returns
 */
function disposeTooltip(ele){
	if(ele[0].hasAttribute("tooltip")||ele[0].hasAttribute("data-tooltip")){
		ele.tooltip("dispose");
		ele.tooltip({ boundary: 'window',container:"body"});
	}
}

/**
 * input带计算器
 */
var JBoltInputWithCalculatorUtil={
		init:function(parentEle){
			var parent=getRealParentJqueryObject(parentEle);
			if(!isOk(parent)){return false;}
			var inputs=parent.find("input[data-with-calculator]");
			if(!isOk(inputs)){return false;}
			this.initInputs(inputs);
		},initInputs:function(inputs){
			if(!isOk(inputs)){return false;}
			var that=this;
			loadJBoltPlugin(['jcalculator'], function(){
				var len=inputs.length;
				for(var i=0;i<len;i++){
					that.initInput(inputs.eq(i),false);
				}
			});
		},initInput:function(input,needLoadPlugin){
			var that=this;
			var inputId=input.attr("id");
			if(!inputId){
				inputId=randomId();
				input.attr("id",inputId);
			}
			
			var isInlineBlock=input.hasClass("d-inline-block");
			if(isInlineBlock){
				input.removeClass("d-inline-block");
			}
			var theme = input.data("theme")||input.data("with-calculator")||"material";
			if(needLoadPlugin){
				loadJBoltPlugin(['jcalculator'], function(){
					input.calculator({theme:theme});
				});
			}else{
				input.calculator({theme:theme});
			}
		}
}
/**
 * input带清空按钮的处理
 */
var JBoltInputWithClearBtnUtil={
		init:function(parentEle){
			var parent=getRealParentJqueryObject(parentEle);
		 	if(!isOk(parent)){return false;}
		 	var inputs=parent.find("input[data-with-clearbtn]");
		 	if(!isOk(inputs)){return false;}
		 	this.initInputs(inputs);
		},initInputs:function(inputs){
			if(!isOk(inputs)){return false;}
			var that=this;
			var len=inputs.length;
			for(var i=0;i<len;i++){
				that.initInput(inputs.eq(i));
			}
		},initInput:function(input){
			var isInlineBlock=input.hasClass("d-inline-block");
			if(isInlineBlock){
				input.removeClass("d-inline-block");
			}
			var wrap=$('<div class="ac_input_box '+(isInlineBlock?'d-inline-block':'')+'"></div>');
			var clearBtnBox=$('<div class="closeBtnBox"><a tabindex="-1" href="javascript:void(0)" class="close" aria-label="Close"><span aria-hidden="true">&times;</span></a></div>');
			input.wrap(wrap);
			input.after(clearBtnBox);
		 
			var inputId=input.attr("id");
			if(!inputId){
				inputId=randomId();
				input.attr("id",inputId);
			}
			input.on("input",function(){
				if(input.val()){
					clearBtnBox.show();
				}else{
					clearBtnBox.hide();
				}
			}).on("change",function(e){
				if(input.val()){
					clearBtnBox.show();
				}else{
					clearBtnBox.hide();
				}
			});
			var clearHandler=input.data("clear-handler");
			clearBtnBox.off("click").on("click",function(e){
				e.preventDefault();
				e.stopPropagation();
				input.removeClass("is-valid");
				input.removeClass("is-invalid");
				var tooltipId=input.attr("aria-describedby");
				if(tooltipId){
					input.tooltip("dispose");
				}
				input.val("").change();
				if(clearHandler){
					if(typeof(clearHandler)=="string"&&clearHandler=="oninput"){
						input.trigger("input");
					}else if(typeof(clearHandler)=="string"&&clearHandler=="onkeyup"){
						input.trigger("keyup");
					}else{
						var exe_handler=eval(clearHandler);
						if(exe_handler&&typeof(exe_handler)=="function"){
							exe_handler(input,input.val());
						}
					}
				}
				if(input[0].hasAttribute("data-with-calculator")){
					input.trigger("clear");
				}
				return false;
			});
				
				 
			if(input.val()){
				clearBtnBox.show();
			}else{
				clearBtnBox.hide();
			}
		}
}
/**
 * jstree组件在JBoltInput组件中设置选中的数据给JBoltInput组件 handler
 * @param tree
 * @param treeNode
 * @returns
 */
function jstreeSetJBoltInputValueHandler(tree,treeNode){
	var hasCheckbox = tree.data("checkbox");
	var textasvalue=tree.data("textasvalue")||false;
	var jboltinputId = tree.data("jboltinput");
	var jbinput=$("#"+jboltinputId);
	if(hasCheckbox){
		var onlyleaf = tree.data("onlyleaf")||false;
		var onlytype = tree.data("onlytype")||"";
		var dataAttrFilterFunc = tree.data("onlyattrfilter")||"";
		var withroot = jbinput.data("jstree-sync-withroot");
		if(typeof(withroot)=="undefined"){
			withroot = false;
		}
		var text = tree.jstree(true).get_all_checked(false,withroot,"text",onlyleaf,onlytype,dataAttrFilterFunc);
		var value = textasvalue?text:(tree.jstree(true).get_all_checked(false,withroot,"id",onlyleaf,onlytype,dataAttrFilterFunc));
		JBoltInputUtil.setValue(tree,text,value);
	}else{
		var valueAttr=jbinput.data("jstree-value-attr");
		if(valueAttr){
			if(!treeNode.data){
				LayerMsgBox.alert("jstree数据接口返回数据必须携带json原始数据，才可以使用data-jstree-value-attr",2);
				return;
			}
			if(valueAttr.indexOf(",")==-1){
				//就指定了一个attr
				var values = (valueAttr ==="text" )?treeNode.text:(treeNode.data[valueAttr]||"");
				JBoltInputUtil.setValue(tree,treeNode.text,(textasvalue?treeNode.text:values));
			}else{
				var attrs = valueAttr.split(",");
				var vss = new Array();
				var tempattr;
				for(var i in attrs){
					tempattr = attrs[i];
					if(tempattr){
						vss.push(treeNode.data[tempattr]||"");
					}else{
						vss.push("");
					}
				}
				JBoltInputUtil.setValue(tree,treeNode.text,vss);
			}
		}else{
			JBoltInputUtil.setValue(tree,treeNode.text,(textasvalue?treeNode.text:treeNode.id));
		}
	}
}
/**
 * 任何组件都能调用的设置JBoltInput值和隐藏域值的处理函数
 * @param ele
 * @param text
 * @param value
 * @returns
 */
function jboltInputSetValueHandler(ele,text,value){
	JBoltInputUtil.setValue(ele,text,value);
}
//input 点击出现下拉portal 组件
var JBoltInputUtil={
		init:function(parentEle){
			 var parent=getRealParentJqueryObject(parentEle);
		 	if(!isOk(parent)){return false;}
		 	var inputs=parent.find("input[data-jboltinput]");
		 	if(!isOk(inputs)){return false;}
		 	this.initInputs(inputs);
		},initInputs:function(inputs){
			if(!isOk(inputs)){return false;}
			var that=this;
			var len=inputs.length;
			for(var i=0;i<len;i++){
				that.initInput(inputs.eq(i));
			}
		},filterTable:function(input){
			if(!isOk(input.input_layer)){
				var inputId = input.attr("id");
				if(inputId){
					var layer = jboltBody.find(".jbolt_input_layer[data-jbolt-input-id='"+inputId+"']");
					if(isOk(layer)){
						input.input_layer = layer;
					}
				}
			}
			if(isOk(input.input_layer)){
				var value=$.trim(input.val());
				if(isOk(value)){
					var tr,text,table=input.input_layer.find("table");
					
					table.find("tbody>tr").each(function(){
						tr=$(this);
						text=tr.text();
						if(text.indexOf(value)!=-1){
							tr.css({"color":"#f00"}).show(100);
						}else{
							tr.css({"color":"#333"}).hide(100);
						}
					});
					
				}else{
					input.input_layer.find("table>tbody>tr").css({"color":"#333"}).show(100);
				}
			}
			
		},filterTree:function(input){
			if(!isOk(input.input_layer)){
				var inputId = input.attr("id");
				if(inputId){
					var layer = jboltBody.find(".jbolt_input_layer[data-jbolt-input-id='"+inputId+"']");
					if(isOk(layer)){
						input.input_layer = layer;
					}
				}
			}
			if(isOk(input.input_layer)){
				var inputtree=input.input_layer.find("div[data-jstree]");
				if(isOk(inputtree)){
					inputtree.jstree(true).search($.trim(input.val()));
				}
			}
		},initInput:function(input){
			var that=this;
			if(!input.attr("id")){
				input.attr("id","jbinput_"+randomId());
			}
			var isInlineBlock=input.hasClass("d-inline-block");
			if(isInlineBlock){
				input.removeClass("d-inline-block");
			}
			var wrap=$('<div class="ac_input_box '+(isInlineBlock?'d-inline-block':'')+'"></div>');
			var clearBtnBox=$('<div class="closeBtnBox"><a tabindex="-1" href="javascript:void(0)" class="close" aria-label="Close"><span aria-hidden="true">&times;</span></a></div>');
			var readonly=input.data("readonly");
			var filterHandler=input.data("filter-handler");
			var canInput=true;
			if(!filterHandler&&(readonly==undefined||(typeof(readonly)=="boolean"&&readonly==true))){
				input.attr("readonly","readonly");
				canInput=false;
			}
			var loadType=input.data("load-type");
			if(!loadType){
				loadType="html";
				input.data("load-type",loadType);
			}
			input.wrap(wrap);
			input.after(clearBtnBox);
			var checkSuccess=false;
			if(loadType=="html"){
				checkSuccess=that.checkInputByHtml(input);
			}else if(loadType=="ajaxportal"){
				checkSuccess=that.checkInputByAjaxPortal(input);
			}else if(loadType=="jstree"){
				checkSuccess=that.checkInputByJstree(input);
			}
			if(checkSuccess){
				var inputId=input.attr("id");
				if(!inputId){
					inputId=randomId();
					input.attr("id",inputId);
				}
				input.off("change").on("change",function(e){
					if(input.val()){
						clearBtnBox.show();
					}else{
						clearBtnBox.hide();
					}
				});
				
				
				input.off("click").on("click",function(e){
					e.preventDefault();
					e.stopPropagation();
					that.showJBoltInputLayer(input);
					return false;
				});
				

				clearBtnBox.off("click").on("click",function(e){
					e.preventDefault();
					e.stopPropagation();
					input.removeClass("is-valid");
					input.removeClass("is-invalid");
					var tooltipId=input.attr("aria-describedby");
					if(tooltipId){
						input.tooltip("dispose");
					}
					input.val("").change();
					that.clearHiddenInputValue(input);
					if(filterHandler=="filterTable"){
						that.filterTable(input);
					}else if(filterHandler=="filterTree"){
						that.filterTree(input);
					}
					that.processJBoltTableHandler(input);
					return false;
				});
				//能输入并且有filterHandler
				that.processInputFilterHandler(input,canInput,filterHandler,clearBtnBox);
			}
			var plancehoder=input.attr("placeholder");
			var inputValue=input.val();
			if(plancehoder&&inputValue&&inputValue===plancehoder){
				clearBtnBox.hide();
				return;
			}
			if(inputValue){
				clearBtnBox.show();
			}else{
				clearBtnBox.hide();
			}
			
		},
		processInputFilterHandler:function(input,canInput,filterHandler){
			input.off("keyup");
			var that = this;
			if(canInput&&filterHandler){
				var clearBtnBox = input.parent().find(".closeBtnBox");
				input.on("keyup",function(e){
					e.preventDefault();
					e.stopPropagation();
					if(!isOk(input.closest("table[data-editable='true']"))){
						that.clearHiddenInputValue(input);
					}
					if(input.val()){
						clearBtnBox.show();
					}else{
						clearBtnBox.hide();
					}
					
					if(filterHandler=="filterTable"){
						that.filterTable(input);
					}else if(filterHandler=="filterTree"){
						that.filterTree(input);
					}else{
						 var exe_handler=eval(filterHandler);
						  if(exe_handler&&typeof(exe_handler)=="function"){
							  exe_handler(input);
						 }
					}
					return false;
				});
			}
		},
		hideJBoltInputLayer:function(input){
			if(isOk(input)){
				if(isOk(input.input_layer)){
					input.input_layer.slideUp(100);
					var menu=input.input_layer.find('.jbolt_ajaxportal_menu');
					if(isOk(menu)){
						menu.hide();
					}
				}
			}else{
				var layers=$(".jbolt_input_layer");
				if(isOk(layers)){
					layers.find('.jbolt_ajaxportal_menu').hide();
					layers.slideUp(100);
				}
			}
		},
		//设置值和绑定隐藏域
		setValue:function(ele,text,value,jsonData){
			var theElement=getRealJqueryObject(ele);
			var jboltInputLayer=theElement.closest(".jbolt_input_layer");
			if(isOk(jboltInputLayer)){
				var inputId=jboltInputLayer.data("jbolt-input-id");
				if(isOk(inputId)){
					var jboltInput=jboltInputLayer.prev("#"+inputId);
					var zeroClear=jboltInput.data("zero-clear");
					var jboltInputValue=text;
					if(zeroClear){
						if(!isOk(value)){
							jboltInputValue="";
						}
					}
					jboltInput.val(jboltInputValue).change();
					var hiddenInputId=jboltInput.data("hidden-input")||jboltInput.data("hiddeninput");
					if(hiddenInputId){
						if(hiddenInputId.indexOf(",")!=-1){
							var hids=hiddenInputId.split(",");
							var valueIsArray=isArray(value);
							var hsize=hids.length;
							if(isOk(hids)&&valueIsArray&&hsize==value.length){
								for(var i=0;i<hsize;i++){
									$("#"+hids[i]).val(value[i]).change();
								}
							}else{
								LayerMsgBox.alert("id为："+inputId+"的JBoltInput组件 隐藏域个数与赋值个数不一致",2);
								return false;
							}
						}else{
							var hiddenInput = $("#"+hiddenInputId);
							if(notOk(hiddenInput)){
								var dname = jboltInput.data("name");
								if(dname){
									jboltInput.before("<input type='hidden' id='"+hiddenInputId+"' name='"+dname+"' />");
									hiddenInput = jboltInput.prev();
								}else{
									LayerMsgBox.alert("指定的hiddenInput不存在时，自动创建，但是必须指定data-name属性",2);
									return false;
								}
							}
							hiddenInput.val(value).change();

						}
					}
					if(jsonData){
						this.processJBoltTableHandler(jboltInput,text,value,jsonData);
					}
				}
			}
			var isCheckboxJstree = theElement[0].hasAttribute("data-jstree") && theElement.data("checkbox");
			if( !isCheckboxJstree && !theElement.dontHideJBoltInputLayer){
				this.hideJBoltInputLayer();
			}
			
		},clearHiddenInputValue:function(input){
			var hiddenInputId=input.data("hidden-input")||input.data("hiddeninput");
			if(hiddenInputId){
				if(hiddenInputId.indexOf(",")!=-1){
					var hids=hiddenInputId.split(",");
					if(isOk(hids)){
						var hsize=hids.length;
						for(var i=0;i<hsize;i++){
							$("#"+hids[i]).val("").change();
						}
					}
				}else{
					$("#"+hiddenInputId).val("").change();
				}
			}
			
		},processJBoltTableHandler:function(input,text,value,data){
			var jbolttableHandler=input.data("jbolttable-handler");
			if(jbolttableHandler){
					var table=input.closest("table");
					if(isOk(table)){
						var jboltTable=table.jboltTable("inst");
						if(jboltTable){
							var td=input.closest("td");
							var jsonData=jboltTable.tableListDatas[td.closest("tr").data("index")];
							var isChangeColumns  =  td.data("jbolttable-handler-ischangecolumns");
							if(!isChangeColumns){
								jboltTable.me.processColConfigChangeColumns(jboltTable,td,data);
							}
							var textasvalue=input.data("textasvalue")||false;
							jbolttableHandler(jboltTable,td,text,(textasvalue?text:value),jsonData,data);
						}
						
					}
			}
		},
		removeLayer:function(input){
			if(input.input_layer){
				input.input_layer = null;
			}
			var inputId = input.attr("id");
			if(inputId){
				var layer = jboltBody.find(".jbolt_input_layer[data-jbolt-input-id='"+inputId+"']");
				if(isOk(layer)){
					layer.remove();
				}
			}
			input.data("input-layer-clear",true);
		},
		showJBoltInputLayer:function(input){
			var that=this;
			if(input.data("input-layer-clear") && input.input_layer){
				input.input_layer = null;
				input.data("input-layer-clear",false).removeAttr("data-input-layer-clear");
			}
			var input_layer=input.input_layer;
			if(isOk(input_layer)&&!input_layer.is(":hidden")){
				return false;
			}
			that.hideJBoltInputLayer();
			var loadType=input.data("load-type");
			if(!input_layer){
				input_layer=$("<div class='jbolt_input_layer'></div>");
				
				var url=input.data("url");
				if(url){
					url=actionUrl(url);
					url=processEleUrlByLinkOtherParamEle(input,url,false);
					if(!url){
						return false;
					}
					url=processJBoltTableEleUrlByLinkColumn(input,url);
					if(!url){
						return false;
					}
				}
				
				if(loadType=="html"){
					var contentId=input.data("content-id");
					var contentBox=getRealJqueryObject(contentId);
					if(isOk(contentBox)){
						contentBox.addClass("show");
						input_layer.append(contentBox);
					}else{
						input_layer.html('<div class="alert alert-warning d-inline-block"><i class="fa fa-warning mr-1"></i>暂无数据</div>')
					}
					input_layer.insertAfter(input);
					input.next().on("click","[data-jboltinput-setvalue-trigger]",function(e){
						var tr=$(this);
						var value=tr.data("value");
						var text=tr.data("text");
						var valueType=typeof(value);
						if(valueType=="string"&&value.indexOf(",")!=-1){
							value=value.split(",");
						}
						var jsonData=tr.data("json");
						that.setValue(tr,text,value,jsonData);
					});
				}else if(loadType=="jstree"){
					//var inputId=input.attr("id");
					var treeBox;
					var openLevel=input.data("open-level");
					openLevel=openLevel?("data-open-level=\""+openLevel+"\""):'';
					var hasCheckbox = input.data("jstree-checkbox");
					var onlyLeaf = input.data("onlyleaf")||false;
					var onlyType = input.data("onlytype")||"";
					var onlyattrfilter = input.data("onlyattrfilter")||"";
					
					var textasvalue = input.data("textasvalue")||false;
					if(input.data("filter-handler")){
						treeBox=$('<div  data-onlyattrfilter="'+onlyattrfilter+'" data-onlyleaf="'+onlyLeaf+'" data-onlytype="'+onlyType+'" data-textasvalue="'+textasvalue+'" data-jboltinput="'+input.attr("id")+'" '+openLevel+' data-search-by-jboltinput="true" data-jstree '+(hasCheckbox?" data-checkbox='true' ":'')+' data-read-url="'+url+'"  data-change-handler="jstreeSetJBoltInputValueHandler"> </div>');
					}else{
						var searchInputBoxId=input.attr("id")+"_jstreesearchbox";
						treeBox=$('<div class="form-group"><input type="text" class="form-control" id="'+searchInputBoxId+'" placeholder="关键字搜索" /></div> '+
					    	'<div   data-onlyattrfilter="'+onlyattrfilter+'"  data-onlyleaf="'+onlyLeaf+'"  data-onlytype="'+onlyType+'" data-textasvalue="'+textasvalue+'" data-jboltinput="'+input.attr("id")+'" '+openLevel+' data-jstree '+(hasCheckbox?" data-checkbox='true' ":'')+'  data-search-input="'+searchInputBoxId+'"  data-read-url="'+url+'"  data-change-handler="jstreeSetJBoltInputValueHandler"> </div>');
					}
					var jbolttableHandler=input.data("jbolttable-handler");
					if(jbolttableHandler){
						treeBox.data("jbolttable-handler",jbolttableHandler);
					}
					input_layer.append(treeBox);
					input_layer.insertAfter(input);
					JSTreeUtil.init(input_layer);
					
				}else if(loadType=="ajaxportal"){
					var refreshMenu=input.data("refresh");
					if(typeof(refreshMenu)=="undefined"){
						refreshMenu=true;
					}
					var portal=$('<div data-ajaxportal '+(refreshMenu?"data-refresh":"")+' data-url="'+url+'"></div>');
					input_layer.append(portal);
					input_layer.insertAfter(input);
					portal.ajaxPortal(true);
					
					portal.on("click","[data-jboltinput-setvalue-trigger]",function(e){
						var tr=$(this);
						var value=tr.data("value");
						var text=tr.data("text");
						var valueType=typeof(value);
						if(valueType=="string"&&value.indexOf(",")!=-1){
							value=value.split(",");
						}
						var jsonData=tr.data("json");
						that.setValue(tr,text,value,jsonData);
					});
				}
				input_layer.on("click",function(e){
					e.preventDefault();
					e.stopPropagation();
					hideAjaxPortalContextMenu();
					return false;
				});
			}else{
				if(loadType=="html"){
					var contentId=input.data("content-id");
					var contentBox=input_layer.find("#"+contentId);
					if(!isOk(contentBox)){
						var contentBox=getRealJqueryObject(contentId);
						if(isOk(contentBox)){
							contentBox.addClass("show");
							input_layer.append(contentBox);
						}else{
							input_layer.html('<div class="alert alert-warning d-inline-block"><i class="fa fa-warning mr-1"></i>暂无数据</div>')
						}
					}
				}else if(loadType=="jstree"){
					
				}else if(loadType=="ajaxportal"){
					var ajaxPortal=input_layer.find("[data-ajaxportal]");
					if(!isOk(ajaxPortal)){
						var portal=$('<div data-ajaxportal data-url="'+url+'"></div>');
						input_layer.append(portal);
						portal.ajaxPortal(true);
					}
				}
			}
			var offset=input.offset(),
			windowScrollTop=jboltWindow.scrollTop(),
			windowScrollLeft=jboltWindow.scrollLeft(),
			height=input.outerHeight(),
			top=offset.top+height+4-windowScrollTop,
			left=offset.left-windowScrollLeft,
			width=input.outerWidth(),
			dataWidth=input.data("width"),
			dataHeight=input.data("height"),
			dataMaxHeight=input.data("max-height");
			if(dataWidth&&dataWidth>width){
					var newWidth=jboltWindowWidth-left-20;
					if(newWidth<dataWidth&&newWidth>width){
						width=newWidth;
					}else{
						width=dataWidth;
					}
			}
			if(!dataHeight){
				dataHeight=350;
			}
			
			var inputId=input.attr("id");
			input_layer.attr("data-jbolt-input-id",inputId).data("jbolt-input-id",inputId);
			if(offset.top+dataHeight>jboltWindowHeight-25){
				top = offset.top-dataHeight-4-windowScrollTop;
			}
			input_layer.css({
				"top":top+"px",
				"left":left+"px",
				"width":width+"px",
				"max-width":width+"px",
				"height":dataHeight+"px",
				"max-height":dataHeight+"px"
			});
			input_layer.slideDown(100);
			input.input_layer=input_layer;
		},checkInputByHtml:function(input){
			var that=this;
			var contentId=input.data("content-id");
			if(!contentId){
				showFormEleErrorStyle(input,"请设置data-content-id属性");
				return false;
			}
			return true;
			
		},checkInputByAjaxPortal:function(input){
			var that=this;
			var url=input.data("url");
			if(!url){
				that.showErrorStyle(input,"请设置data-url属性");
				return false;
			}
			return true;
		},checkInputByJstree:function(input){
			var that=this;
			var url=input.data("url");
			if(!url){
				that.showErrorStyle(input,"请设置data-url属性");
				return false;
			}
			return true;
		}
}
//rangeSlider组件
var RangeSliderUtil={
		reset:function(rangeSliderEle){
			var obj = getRealJqueryObject(rangeSliderEle);
			if(notOk(obj)){
				LayerMsgBox.alert("指定update的组件未找到",2);
				return false;
			}
			loadJBoltPlugin(['rangeslider'], function(){
				obj.data("ionRangeSlider").reset();
			});
		},
		update:function(rangeSliderEle,data){
			var obj = getRealJqueryObject(rangeSliderEle);
			if(notOk(obj)){
				LayerMsgBox.alert("指定update的组件未找到",2);
				return false;
			}
			loadJBoltPlugin(['rangeslider'], function(){
				obj.data("ionRangeSlider").update(data);
			});
		},
		init:function(parentEle){
			var parent=getRealParentJqueryObject(parentEle);
		 	if(!isOk(parent)){return false;}
		 	var inputs=parent.find("input[data-rangeslider],input[data-rangerslider]");
		 	if(!isOk(inputs)){return false;}
		 	this.initInputs(inputs);
		},initInputs:function(inputs){
			if(!isOk(inputs)){return false;}
			var that=this;
			loadJBoltPlugin(['rangeslider'], function(){
				var len=inputs.length;
				for(var i=0;i<len;i++){
					that.initInput(inputs.eq(i),false);
				}
			});
		},initInput:function(input,needLoadPlugin){
			var that=this;
			if(needLoadPlugin){
				loadJBoltPlugin(['rangeslider'], function(){
					that._initInput(input);
				});
			}else{
				that._initInput(input);
			}
			
		},
		processHandler:function(range,handler,data){
			var exe_handler=eval(handler);
			if(exe_handler&&typeof(exe_handler)=="function"){
				exe_handler(range,data);
			}
		},
		_initInput:function(range){
			var options={};
			var that=this;
			var hasOptions=false;
			var startHandler=range.data("start-handler");
			if(startHandler){
				hasOptions = true;
				options.onStart=function(data) {
					that.processHandler(range,startHandler,data);
				}
			}
			var changeHandler=range.data("change-handler");
			if(changeHandler){
				hasOptions = true;
				options.onChange=function(data) {
					that.processHandler(range,changeHandler,data);
				}
			}
			var finishHandler=range.data("finish-handler");
			if(finishHandler){
				hasOptions = true;
				options.onFinish=function(data) {
					that.processHandler(range,finishHandler,data);
				}
			}
			var updateHandler=range.data("update-handler");
			if(updateHandler){
				hasOptions = true;
				options.onUpdate=function(data) {
					that.processHandler(range,updateHandler,data);
				}
			}
			if(hasOptions){
				range.ionRangeSlider(options);
			}else{
				range.ionRangeSlider();
			}
		}
			
}

/**
 * json对象转URL 参数
 * @param json
 * @returns
 */
function jsonToUrlParams(json) {
	var tempArr = [];
	for(var i in json) {
		var key = encodeURIComponent(i);
		var value = encodeURIComponent(json[i]);
		tempArr.push(key + '=' + value);
	}
	var urlParamsStr = tempArr.join('&');
	return urlParamsStr;
}

//城市选择
var CityPickerUtil={
	loadJsonPromise:function(){
        return new Promise(function(resolve){
        	resolve( JBoltCityPickerDatas );
        });
	},
	init:function(parentEle){
		 var parent=getRealParentJqueryObject(parentEle);
		 if(!isOk(parent)){return false;}
		 var pickers=parent.find("[data-citypicker]");
		 if(!isOk(pickers)){return false;}
		 this.initPickers(pickers);
	},initPickers:function(pickers){
		if(!isOk(pickers)){return false;}
		var that=this;
		if(JBoltCityPickerDatas==null){
			$.getJSON("assets/plugins/ipicker/area.json",function(data){
				JBoltCityPickerDatas=data;
				loadJBoltPlugin(["ipicker"],function(){
					var size=pickers.length;
					for(var i=0;i<size;i++){
						that.initPicker(pickers.eq(i));
					}
				});
			 });
		}else{
			loadJBoltPlugin(["ipicker"],function(){
				var size=pickers.length;
				for(var i=0;i<size;i++){
					that.initPicker(pickers.eq(i));
				}
			});
		}
		
		
	},initPicker:function(cp,data){
		if(typeof(data)=="undefined"){
			data=JBoltCityPickerDatas;
		}
		var setvalueto=cp.data("setvalueto");
		if(!setvalueto){
			LayerMsgBox.alert("请设置cityPicker的 data-setvalueto属性 格式 id:name:valueAttr 或者 id:valueAttr",2);
			return false;
		}
		var setValueArr=new Array();
		if(setvalueto.indexOf(";")!=-1){
			var arr=setvalueto.split(";");
			var tempArr;
			for(var i in arr){
				tempArr=arr[i].split(":");
				if(!isOk(tempArr)||(tempArr.length!=3&&tempArr.length!=2)){
					LayerMsgBox.alert("请正确设置cityPicker的 data-setvalueto属性 格式 id:name:valueAttr 或者 id:valueAttr",2);
					return false;
				}
				setValueArr.push(tempArr);
			}
		}else{
			var sarr=setvalueto.split(":");
			if(!isOk(sarr)||(sarr.length!=3 && sarr.length!=2)){
				LayerMsgBox.alert("请正确设置cityPicker的 data-setvalueto属性 格式 id:name:valueAttr 或者 id:valueAttr",2);
				return false;
			}
			setValueArr.push(sarr);
		}
		
		if(!isOk(setValueArr)){
			LayerMsgBox.alert("请正确设置cityPicker的 data-setvalueto属性 格式 id:name:valueAttr 或者 id:valueAttr",2);
			return false;
		}
		
		var that=this;
		var cpId=cp.attr("id");
		if(!cpId){
			cpId="cityPicker_"+randomId();
			cp.attr("id",cpId);
		}
		var disabled=cp.data("disabled")||[];
		var disabledItem=cp.data("disabled-item")||[];
		var theme=cp.data("theme")||"cascader";
		var level=cp.data("level")||3;
		var strict=cp.data("strict")||false;
		var height=cp.data("height")||34;
		var radius=cp.data("radius")||2;
		var maxHeight=cp.data("max-height")||300;
		var placeholder=cp.data("placeholder")||"请选择地区";
		var separator=cp.data("separator")||"/";
		var onlyShowLastLevel=cp.data("only-show-last-level")||false;
		var icon=cp.data("icon")||"triangle";
		var clearHandler=cp.data("clear-handler");
		var selectHandler=cp.data("select-handler");
		var pickerOptions={
	             theme: theme,
	             icon:icon,
	             data: {source:that.loadJsonPromise()},
	             disabled:disabled,
	             level:level,
	             radius:radius,
	             separator:separator,
	             onlyShowLastLevel:onlyShowLastLevel,
	             height:height,
	             disabledItem:disabledItem,
	             maxHeight:maxHeight,
	             clearable:true,
	             placeholder: placeholder,
				 strict:strict,
	             onClear:function(){
	            	 if(clearHandler){
	            		 var exeClearHandler=eval(clearHandler);
	         			 if(exeClearHandler&&typeof(exeClearHandler)=="function"){
	         				exeClearHandler(cp);
	         			 }
	            	 }
	             },
	             onSelect: function ( code, name, all ) {
	            	 that.selectCity(cp,code,name,all,setValueArr);
	            	 if(selectHandler){
	            		 var exeselectHandler=eval(selectHandler);
	         			 if(exeselectHandler&&typeof(exeselectHandler)=="function"){
	         				exeselectHandler(cp,code, name, all);
	         			 }
	            	 }
	             }
	         };
		
		var select=cp.data("selected")||cp.data("select");
		if(select){
			var selectedArray;
			if(isArray(select)){
				selectedArray=select;
			}else if(isNaN(select)){
				selectedArray=select.split(",");
			}else{
				selectedArray=[select];
			}
			
			if(isOk(selectedArray)){
				pickerOptions["selected"]=selectedArray;
			}
		}
		var selectedCallback=cp.data("selected-callback")||cp.data("selected-handler");
		if(selectedCallback){
			var selectedHandler=eval(selectedCallback);
			if(selectedHandler&&typeof(selectedHandler)=="function"){
				pickerOptions["selectedCallback"]=selectedHandler;
			}
		}
		var parent=cp.closest(".form-group");
		var formInline=cp.closest(".form-inline");
		var cpw=cp.data("width")||"100%";
		if(theme=="select"){
			cpw=cp.data("width")||200;
		}else{
			if(!isOk(parent)&&isOk(formInline)){
				cpw=cp.data("width")||200;
			}
		}
		pickerOptions["width"]=cpw;
		that.initHiddenInput(cp,setValueArr);
		iPicker("#"+cpId, pickerOptions);
	},
	initHiddenInput:function(cp,setValueArr){
		var hiddeninputId,hiddeninputName,hiddenInput,len = 0;
		for(var i in setValueArr){
			len = setValueArr[i].length;
			hiddeninputId=setValueArr[i][0];
			if(len==3){
				//如果设置了name就创建隐藏域
				hiddeninputName=(len==2)?null:setValueArr[i][1];
				hiddenInput = jboltBody.find("input[id='"+hiddeninputId+"'][name='"+hiddeninputName+"']");
			}else{
				hiddenInput = jboltBody.find("input[id='"+hiddeninputId+"']");
			}
			if(!isOk(hiddenInput) && len==2){
				LayerMsgBox.alert("指定setvalueto组件不存在，请正确设置cityPicker的 data-setvalueto属性 格式 id:name:valueAttr 或者 id:valueAttr",2);
				return false;
			}
	   		 if(!isOk(hiddenInput) && len==3){
	   			 hiddenInput=$("<input class='form-control my-1' id='"+hiddeninputId+"' type='hidden' name='"+hiddeninputName+"'/>");
	   			 cp.after(hiddenInput);
	   		 }
		}
		
	},
	selectCity:function(cp,code,name,all,setValueArr){
		var hiddeninputId,hiddeninputName,valueAttr,inputValue,hiddenInput,len=0;
		for(var i in setValueArr){
			len = setValueArr[i].length;
			hiddeninputId=setValueArr[i][0];
			if(len == 2){
				valueAttr=setValueArr[i][1];
			}else{
				hiddeninputName=setValueArr[i][1];
				valueAttr=setValueArr[i][2];
			}

			inputValue="";
	   		 if(valueAttr=="code"){
	   			 inputValue=code;
	   		 }else if(valueAttr=="name"){
	   			 inputValue=name;
	   		 }else if(valueAttr=="name_join"){
	   			 inputValue=name.join("");
	   		 }else if(valueAttr=="code_last"){
	   			 inputValue=code[code.length-1];
	   		 }else if(valueAttr=="name_last"){
	   			 inputValue=name[name.length-1];
	   		 }
			if(len == 3){
				//如果设置了name就创建隐藏域
				hiddenInput = jboltBody.find("input[id='"+hiddeninputId+"'][name='"+hiddeninputName+"']");
			}else{
				hiddenInput = jboltBody.find("input[id='"+hiddeninputId+"']");
			}
	   		 if(isOk(hiddenInput)){
	   			 hiddenInput.val(inputValue).change();
	   		 }else{
				if(len == 2){
					LayerMsgBox.alert("指定setvalueto组件不存在，请正确设置cityPicker的 data-setvalueto属性 格式 id:name:valueAttr 或者 id:valueAttr",2);
					return false;
				}
	   			 hiddenInput=$("<input id='"+hiddeninputId+"' type='hidden' name='"+hiddeninputName+"' value='"+inputValue+"'/>");
	   			 cp.after(hiddenInput);
	   		 }
		}
   	 
		
	}
}

/**
 * ajax异步下载文件解析
 * @param xhr
 * @param url
 * @param status
 * @param ele
 * @param handler
 * @param fileName
 * @returns {boolean}
 */
function ajaxParseFile(xhr,url,status,ele,handler,fileName){
	// 请求完成
	if (status === 200) {
		// 返回200
		var blob = xhr.response;
		if(blob.type=="application/json"&&blob.size>0){
			if(ele){
				ele.data("downloading",false);
			}
			LayerMsgBox.closeLoadingNow();
			var reader = new FileReader();
			reader.onload = function(event){
				var json=JSON.parse(reader.result);
				if(json.state=="fail"){
					LayerMsgBox.alert((json.msg||"下载失败"),2);
				}else{
					processAjaxResultNeedConfirmOr(ele,json,function(){
						if(handler){
							handler();
						}
					});
				}
			};
			reader.readAsText(blob);
			return false;
		}

		var downloadFileName="file";
		if(!fileName && ele){
			fileName = ele.data("filename");
		}
		if(fileName){
			downloadFileName =fileName;
		}else{
			var headerName=xhr.getResponseHeader("Content-disposition");
			if(headerName){
				headerName=decodeURI(headerName);
				var dl_filenameArr=headerName.split("=");
				if(dl_filenameArr&&dl_filenameArr.length==2){
					downloadFileName =$.trim(dl_filenameArr[1]);
				}
			}else{
				LayerMsgBox.closeLoadingNow();
				if(ele){
					ele.data("downloading",false);
				}
				LayerMsgBox.alert("下载失败,未设置下载文件的filename",2);
				return false;
			}
		}
		if(ele){
			ele.data("downloading",false);
		}
		LayerMsgBox.closeLoadingNow();
		if(window.navigator.msSaveOrOpenBlob){
			try{
				window.navigator.msSaveOrOpenBlob(blob,downloadFileName);
			}catch(ee){
				LayerMsgBox.closeLoadingNow();
				if(ele){
					ele.data("downloading",false);
				}
				LayerMsgBox.alert("下载失败 无法保存文件",2);
				return false;
			}
		}else{
			var fileBolb=new Blob([blob],{
				type:"application/octet-stream;charset=utf-8"
			});
			var downloadUrl=window.URL.createObjectURL(fileBolb);
			var alink=document.createElement("a");
			alink.download=downloadFileName;
			alink.href=downloadUrl;
			alink.click();
			if(window.URL.revokeObjectUrl){
				window.URL.revokeObjectUrl(downloadUrl);
			}
		}
		if(ele){
			ele.data("downloading",false);
		}
		LayerMsgBox.closeLoadingNow();




	}else{
		LayerMsgBox.closeLoadingNow();
		if(ele){
			ele.data("downloading",false);
		}
		LayerMsgBox.alert("网络异常",2);
	}
}

/**
 * 处理中间态 confirm
 * @param ele
 * @param ret
 * @param newUrl
 * @param type
 * @param orDoThing
 */
function processAjaxResultNeedConfirmOr(ele,ret,orDoThing){
	if(ret.needConfirm && ret.optUrl && ret.reqType){
		LayerMsgBox.confirm(ret.msg||"请确认是否继续",function(){
			LayerMsgBox.loading("正在执行...",jboltAjaxTimeout);
			window.processAjaxResultNeedConfirmOrEle = ele;
			if(ret.optUrl){
				if(ret.reqType == "GET"){
						Ajax.get(ret.optUrl,function(res){
							LayerMsgBox.closeLoadingNow();
							if(orDoThing){
								orDoThing();
							}
						});
				}else if(ret.reqType == "POST"){
					Ajax.post(ret.optUrl,ret.data,function(res){
						LayerMsgBox.closeLoadingNow();
						if(orDoThing){
							orDoThing();
						}
					});
				}else if(ret.reqType == "DOWNLOAD"){
					if(ele){
						var oldUrl = ele.data("url")||ele.attr("href");
						if(ele[0].hasAttribute("data-url")){
							ele.data("url",ret.optUrl).attr("data-url",ret.optUrl);
						}else if(ele[0].hasAttribute("href")){
							ele.attr("href",ret.optUrl);
						}
						var formId= ele.data("form");
						if(formId){
							ele.data("form","").attr("data-form","");
						}
						var usecheckedids= ele.data("usecheckedids");
						if(usecheckedids){
							ele.data("usecheckedids","").attr("data-usecheckedids","");
						}
						DownloadUtil.downloadByEle(ele);

						if(ele[0].hasAttribute("data-url")){
							ele.data("url",oldUrl).attr("data-url",oldUrl);
						}else if(ele[0].hasAttribute("href")){
							ele.attr("href",oldUrl);
						}
						if(formId){
							ele.data("form",formId).attr("data-form",formId);
						}
						if(usecheckedids){
							ele.data("usecheckedids",usecheckedids).attr("data-form",usecheckedids);
						}
					}
				}else{
					LayerMsgBox.closeLoadingNow();
					LayerMsgBox.alert("继续执行的操作类型只能选择GET、POST、DOWNLOAD",2);
				}
			}else{
				LayerMsgBox.closeLoadingNow();
				 LayerMsgBox.alert("未指定 继续执行的URL",2);
			}
		});
	}else{
		orDoThing();
	}
}

/**
 * 自动处理refreshJstree
 *  handler
 * @param jstreeId
 */
function refreshJstreeHandler(jstreeId){
		if(jstreeId){
			loadJBoltPlugin(['jstree'], function(){
				var jstree = $("#"+jstreeId);
				if(notOk(jstree)){
					LayerMsgBox.alert("配置的data-jstree-id指定id无效",2);
				}else{
					var node = jstree.jstree(true).get_selected(false);
					var selectId = isOk(node)?node.id:null;
					JSTreeUtil.refresh(jstree,selectId);
				}
			});
		}else{
			LayerMsgBox.alert("没有配置data-jstree-id",2);
		}

}

//下载封装
var DownloadUtil={
		downloadByEle:function(ele){
			var that=this,btn=getRealJqueryObject(ele),url=btn.data("url")||btn.attr("href"),downloading=btn.data("downloading");
			if(!url){
				 LayerMsgBox.alert("请设置data-url='下载地址'！",2);
				 return false;
			 }
			
			var checkHandler =btn.data("check-handler");
			//检测是否需要检查主表选择数据
			var needCheckMaster = (btn.data("check-master")||false) || (checkHandler && checkHandler=="checkMasterTableId");
			if(needCheckMaster){
				var masterId = btn.data("master-id");
				if(!masterId){
					LayerMsgBox.alert("请先选择主表数据",2);
					return false;
				}
			}
			if(checkHandler && checkHandler!="checkMasterTableId"){
				var execheck_handler=eval(checkHandler);
				if(execheck_handler&&typeof(execheck_handler)=="function"){
					var success = execheck_handler(btn);
					if(!success){
						return false;
					}
				}
			}
			
			
			 var formId=btn.data("form"),usecheckedids=btn.data("usecheckedids"),fileName=btn.data('filename'),loading=btn.data("loading"),params,handler=btn.data("handler"),portalId=btn.data("portalid")||btn.data("portal");
			//如果设置了启用导出勾选数据的话
			 if(usecheckedids){
				 var checkedIds=jboltTableGetCheckedIds(btn);
				 //判断如果勾选了 就只导出勾选数据
				 if(!isOk(checkedIds)){
					 return false;
				 }
				 params={"ids":checkedIds};
			 }else{
				 //如果没设置导出勾选的 就按照form条件
				 if(formId){
					 params=$("#"+formId).serializeJSON();
				 }

				 var withCurrentPage = btn.data("with-current-page");
				 var jboltTable = JBoltTableUtil.getInst(btn);
				 if(jboltTable && jboltTable.page){
					 var pageInfo = {page:(jboltTable.data("pagenumber")||1),pageSize:(jboltTable.data("pagesize")||10)};
					 if(params){
						 Object.assign(params,pageInfo);
					 }else{
						 params = pageInfo;
					 }
				 }
			 }
			 var confirm = btn.data("confirm");
			 var doOptions={
					 ele:btn,
					 url:url,
					 fileName:fileName,
					 params:params,
				     confirm:confirm,
					 loading:loading,
					 handler:handler
				 };
			 if(portalId){
				 doOptions["portalId"]=portalId;
			 }
			 that.download(doOptions);
		},
		init:function(){
			var that=this;
			jboltBody.on("click","[data-downloadbtn]",function(e){
				 e.preventDefault();
				 e.stopPropagation();
				 that.downloadByEle(this);
				return false;
			});
		},doHandler:function(options){
			 if(options.handler){
				  if(options.handler=="refreshPortal"){
					  if(options.portalId){
						  LayerMsgBox.success("操作成功",500,function(){
							  if(options.portalId == "parentPortal"){
								  if(options.ele){
									  var parentPortal= options.ele.closest("[data-ajaxportal]");
									  if(isOk(parentPortal)){
										  parentPortal.ajaxPortal(true);
									  }
								  }
							  }else{
								  $("#"+options.portalId).ajaxPortal(true);
							  }
						  });
					  }else{
						  LayerMsgBox.alert("没有配置data-portalid",2);
					  }
					 
				  }else if(options.handler=="refreshJstree"){
					  var jstreeId = options.jstreeId||(options.ele?options.ele.data("jstree-id"):null);
					  refreshJstreeHandler(jstreeId);
				  }else if(options.handler=="jboltTablePageToFirst"){
					  loadJBoltPlugin(['jbolttable'], function(){
						  jboltTablePageToFirst(options.ele);
					  });
				  }else if(options.handler=="jboltTablePageToLast"){
					  loadJBoltPlugin(['jbolttable'], function(){
						  jboltTablePageToLast(options.ele);
					  });
				  }else if(options.handler=="refreshJBoltTable"||options.handler=="jboltTableRefresh"){
						  loadJBoltPlugin(['jbolttable'], function(){
							  refreshJBoltTable(options.ele);
						  });
				  }else if(options.handler=="refreshJBoltMainTable"||options.handler=="jboltMainTableRefresh"){
					  loadJBoltPlugin(['jbolttable'], function(){
						  refreshJBoltMainTable(options.ele);
					  });
				  }else if(options.handler=="refreshCurrentAjaxPortal"){
					  refreshCurrentAjaxPortal(options.ele);
					  
				  }else if(options.handler=="removeJBoltTableCheckedTr"){
					  loadJBoltPlugin(['jbolttable'], function(){
						  removeJBoltTableCheckedTr(options.ele,false);
					  });
				  }else if(options.handler=="jboltTableRemoveCheckedRow"){
					  loadJBoltPlugin(['jbolttable'], function(){
						  jboltTableRemoveCheckedRow(options.ele,false);
					  });
				  }else{
					  var exe_handler=eval(options.handler);
						if(exe_handler&&typeof(exe_handler)=="function"){
							exe_handler();
						}
				  }
				  
			  }
		},
		/**
		 * 下载 options包含url,fileName,params
		 */
		download:function(options){
			var xhr=null;
		    if (window.XMLHttpRequest){
		    	xhr=new XMLHttpRequest();
		     }else{
		    	xhr=new ActiveXObject("Microsoft.XMLHTTP");
		     }
		    if(xhr==null){
		    	LayerMsgBox.alert("您的浏览器不支持异步上传,请切换使用高级浏览器",2);
		    	return false;
		    }
		    
			 if(!options.url){
				 LayerMsgBox.alert("请设置data-url='下载地址'！",2);
				 return false;
			 }
			 if(options.ele.data("downloading")){
				 LayerMsgBox.alert("正在下载,请稍等",0);
				return false;
			}
			options.loading=options.loading||"下载中...";
			 if(options.confirm){
					var that = this;
				 	if(typeof(options.confirm)=="boolean"){
						options.confirm = "确定下载？";
					}
					 LayerMsgBox.confirm(options.confirm,function(){
						 that.doDownload(xhr,options);
					 });
			 }else{
				 this.doDownload(xhr,options);
			 }

		},
	doDownload:function(xhr,options){
		var that=this;
		options.ele.data("downloading",true);
		LayerMsgBox.loading(options.loading,jboltAjaxTimeout);
		// 定义请求完成的处理函数，请求前也可以增加加载框/禁用下载按钮逻辑
		xhr.onload = function () {
			// 请求完成
			if (this.status === 200) {
				// 返回200
				var blob = this.response;
				if(blob.type=="application/json"&&blob.size>0){
					options.ele.data("downloading",false);
					LayerMsgBox.closeLoadingNow();
					var reader = new FileReader();
					reader.onload = function(event){
						var json=JSON.parse(reader.result);
						if(json.state=="fail"){
							LayerMsgBox.alert((json.msg||"下载失败"),2);
						}else{
							processAjaxResultNeedConfirmOr(options.ele,json,function(){
								that.doHandler(options);
							});
						}
					};
					reader.readAsText(blob);
					return false;
				}
				/* var reader = new FileReader();
                 reader.readAsDataURL(blob);    // 转换为base64，可以直接放入a href
                 reader.onload = function (e) {
                     // 转换完成，创建一个a标签用于下载
                     var a = document.createElement('a');
                     if(options.fileName){
                         a.download =options.fileName;
                     }else{
                         var headerName=xhr.getResponseHeader("Content-disposition");
                         if(headerName){
                             headerName=decodeURI(headerName);
                             var dl_filenameArr=headerName.split("=");
                             if(dl_filenameArr&&dl_filenameArr.length==2){
                                 a.download =$.trim(dl_filenameArr[1]);
                             }
                         }else{
                             options.ele.data("downloading",false);
                              LayerMsgBox.alert("下载失败,未设置下载文件的filename",2);
                             return false;
                         }
                     }
                     a.href = e.target.result;
                     jboltBody.append(a);    // 修复firefox中无法触发click
                     a.click();
                     $(a).remove();
                     options.ele.data("downloading",false);
                     LayerMsgBox.closeLoading();
                     setTimeout(function(){
                         that.doHandler(options);
                     }, 500);
                 }*/
				var downloadFileName="file";
				if(options.fileName){
					downloadFileName =options.fileName;
				}else{
					var headerName=xhr.getResponseHeader("Content-disposition");
					if(headerName){
						headerName=decodeURI(headerName);
						var dl_filenameArr=headerName.split("=");
						if(dl_filenameArr&&dl_filenameArr.length==2){
							downloadFileName =$.trim(dl_filenameArr[1]);
						}
					}else{
						LayerMsgBox.closeLoadingNow();
						options.ele.data("downloading",false);
						LayerMsgBox.alert("下载失败,未设置下载文件的filename",2);
						return false;
					}
				}

				options.ele.data("downloading",false);
				LayerMsgBox.closeLoadingNow();
				if(window.navigator.msSaveOrOpenBlob){
					try{
						window.navigator.msSaveOrOpenBlob(blob,downloadFileName);
					}catch(ee){
						LayerMsgBox.closeLoadingNow();
						options.ele.data("downloading",false);
						LayerMsgBox.alert("下载失败 无法保存文件",2);
						return false;
					}
				}else{
					var fileBolb=new Blob([blob],{
						type:"application/octet-stream;charset=utf-8"
					});
					var downloadUrl=window.URL.createObjectURL(fileBolb);
					var alink=document.createElement("a");
					alink.download=downloadFileName;
					alink.href=downloadUrl;
					alink.click();
					if(window.URL.revokeObjectUrl){
						window.URL.revokeObjectUrl(downloadUrl);
					}
				}
				options.ele.data("downloading",false);
				LayerMsgBox.closeLoadingNow();




			}else{
				LayerMsgBox.closeLoadingNow();
				options.ele.data("downloading",false);
				LayerMsgBox.alert("网络异常",2);
			}
		};
		xhr.open('POST', options.url, true);        // 也可以使用POST方式，根据接口
		xhr.timeout=jboltAjaxTimeout;
		xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		xhr.responseType = "blob";    // 返回类型blob
		xhr.setRequestHeader('AJAX-DOWNLOAD', "true");
		if(options.params){
			// 发送ajax请求
			xhr.send(jsonToUrlParams(options.params));
		}else{
			xhr.send();
		}
	}
}


//jstree封装
var JSTreeUtil={
		delNodeId:0,
		init:function(parentEle){
			 var parent=getRealParentJqueryObject(parentEle);
			 if(!isOk(parent)){return false;}
			 var trees=parent.find("div[data-jstree],section[data-jstree],p[data-jstree]");
			 if(!isOk(trees)){return false;}
			 this.initTrees(trees);
		},initTrees:function(trees){
			if(!isOk(trees)){return false;}
			var that=this;
			loadJBoltPlugin(['jstree'], function(){
				var len=trees.length;
				for(var i=0;i<len;i++){
					that.initTree(trees.eq(i),false);
				}
			});
		},initTree:function(tree,needLoadPlugin){
			var that=this;
			if(needLoadPlugin){
				loadJBoltPlugin(['jstree'], function(){
					that._initTree(tree);
				});
			}else{
				that._initTree(tree);
			}
			
		},
		processCurdDialog:function(tree,url,type,inst,obj){
			if(type == "add" || type == "edit"){
				var btn = $('<button data-openpage="dialog" data-url="'+url+'" data-title="'+obj.text+'" data-area="720,560"></button>');
				var dialogArea=tree.data("dialog-area");
				if(dialogArea){
					btn.data("area",dialogArea).attr("data-area",dialogArea);
				}
				var treeDialogHandler=tree.data("dialog-handler");
				if(treeDialogHandler){
					if(treeDialogHandler == "refreshJstree"){
						var treeId = tree.attr("id");
						if(!treeId){
							treeId = "jstree_"+randomId(5);
							tree.attr("id",treeId);
						}
						btn.data("jstree-id",treeId).attr("data-jstree-id",treeId);
					}
					btn.data("handler",treeDialogHandler).attr("data-handler",treeDialogHandler);
				}
				DialogUtil.openBy(btn);
			}else if(type=="delete"){
				this.processDeletePortal(tree,url,inst,obj);
			}
		},
		processDeletePortal:function(tree,url,inst,obj){
			var that=this;
			var confirm = tree.data("confirm")||"确定删除此项?";
			LayerMsgBox.confirm(confirm,function(){
				LayerMsgBox.loading("执行中...");
				Ajax.get(url,function(res){
					LayerMsgBox.success("删除成功",1000);
					that.delNodeId=obj.id;
					if(inst.is_selected(obj)) {
	                    inst.delete_node(inst.get_selected());
	                }else {
	                    inst.delete_node(obj);
	                } 
				});
			});
			
		},
		processCurdPortal:function(tree,url,type,inst,obj){
			var that=this;
			var portalId=tree.data("portalid")||tree.data("portal");
			var portal=$("#"+portalId);
			if(type=="delete"){
				that.processDeletePortal(tree,url,inst,obj);
			}else{
				portal.ajaxPortal(true,url,true);
			}
			
		},
		clearSelected:function(tree){
			tree.jstree(true).deselect_all();
		},refresh:function(tree,selectId){
			if(typeof(tree)=="string"){
				tree = getRealJqueryObject(tree);
			}
			var readUrl=tree.data("read-url");
			if(selectId){
				if(readUrl.indexOf("?")!=-1){
					readUrl=readUrl+"&selectId="+selectId;
				}else{
					if(readUrl[readUrl.length-1]!='/'){
						readUrl=readUrl+"/"+selectId;
					}else{
						readUrl=readUrl+selectId;
					}
				}
			}
				var that=this;
				Ajax.get(readUrl,function(data){
					 var treeDatas=data.data;
					 tree.jstree(true).deselect_all();
					 tree.jstree(true).settings.core.data=treeDatas;
					 tree.jstree(true).refresh();
					 if(selectId){
						 setTimeout(function(){
							 tree.jstree(true).select_node({id:selectId,selected:true});
							 var node = tree.jstree(true).get_selected(false);
							 if(notOk(node)){
								 that.setChecked(tree,'0');
								 that.clearSelected(tree);
							 }
						 },100);
					 }
				});
		},
		processCurdHandler:function(tree,reference,type){
			var target=tree.data("target");
			if(target==undefined||target=="undefined"){
				target="dialog";
			}
			 var inst = $.jstree.reference(reference),
             obj = inst.get_node(reference);
			 var id=obj.id;
			 if((id==0&&type=="delete")||(id==0&&type=="edit")||(id==0&&target=="dialog"&&type=="edit")){
				LayerMsgBox.alert("不能操作根节点",2);
				return false;
			}
			var that=this;
			 var pid=obj.id;
			 var url="";
			 //根据类型拼接url
			 switch (type) {
			 case 'add':
				 var addUrl = tree.data("add-url");
				 if(addUrl.indexOf("?")==-1){
					 if(addUrl.endWith("/")||addUrl.endWith("-")){
						 url=addUrl+pid;
					 }else{
						 url=addUrl+"/"+pid;
					 }
				 }else{
					 if(addUrl.endWith("/")||addUrl.endWith("-")||addUrl.endWith("=")){
						 url=addUrl+pid;
					 }else{
						 url=addUrl+"&pid="+pid; 
					 }
				 }
				 if(pid=='0' || pid==0){
					 var cformId = tree.data("conditions-form");
					 if(cformId){
						 var cform=$("#"+cformId);
						 if(isOk(cform)){
							 url = urlWithFormData(url,cform);
						 }
					 }
				 }
				 break;
			 case 'edit':
				 url=tree.data("edit-url")+pid;
				 break;
			 case 'portalEdit':
				 url=tree.data("edit-url")+pid;
				 break;
			 case 'delete':
				 url=tree.data("delete-url")+pid;
				 break;
			}
			 //根据target类型 处理具体事件
			 switch (target) {
				 case "dialog":
					 that.processCurdDialog(tree,url,type,inst,obj);
					 break;
				 case "portal":
					 that.processCurdPortal(tree,url,type,inst,obj);
					break;
			}
            
			
			
			
			
		},
		processChangeTextHandler:function(tree,node,handlerName){
			var that=this;
			switch (handlerName) {
				case "portalEdit":
					that.processCurdHandler(tree,node,"portalEdit");
					break;
			}
		},processJBoltTableHandler:function(tree,text,value,data,jbolttableHandler){
			if(jbolttableHandler){
					var table=tree.closest("table");
					if(isOk(table)){
						var jboltTable=table.jboltTable("inst");
						if(jboltTable){
							var td=tree.closest("td");
							var jsonData=jboltTable.tableListDatas[td.closest("tr").data("index")];
							var isChangeColumns  =  td.data("jbolttable-handler-ischangecolumns");
							if(!isChangeColumns){
								jboltTable.me.processColConfigChangeColumns(jboltTable,td,data);
							}
							var textasvalue=tree.data("textasvalue")||false;
							jbolttableHandler(jboltTable,td,text,(textasvalue?text:value),jsonData,data);
						}
						
					}
			}
		},
		_initTree:function(tree){
			var that=this;
			var read_url=tree.data("read-url");
			if(!read_url){
				LayerMsgBox.alert("未设置jstree的data-read-url",2);
				return;
			}
			var openLevel=tree.data("open-level");
			if(typeof(openLevel)=="undefined"){
				openLevel=0;
			}
			if(openLevel!=0){
				if(read_url.charAt(read_url.length-1)=='/'){
					read_url=read_url.substring(0,read_url.length-1);
				}
				if(read_url.indexOf("?")!=-1){
					read_url=read_url+"&openLevel="+openLevel;
				}else{
					read_url=read_url+"?openLevel="+openLevel;
				}
			}
			var form = that.processConditionsForm(tree);
			if(isOk(form)){
				tree.data("orign-read-url",read_url).attr("data-orign-read-url",read_url);
				read_url = urlWithFormData(read_url,form);
			}
			var curd=tree.data("curd");
			if(curd==undefined||curd=="undefined"){
				//如果没有开启curd模式 默认就是false 就是只有查询和change处理
				curd=false;
			}
			
			var treeOptions={
					'core' : {
						'check_callback' : true,
						'animation':100
					},
					'plugins' : ["themes","wholerow"],
			};
			 var searchInputId=tree.data("search-input");
			 var searchInput;
			 var hasCheckbox = false;
			 if(searchInputId){
				 searchInput = jboltBody.find('#'+searchInputId);
				 if(!isOk(searchInput)){
					 LayerMsgBox.alert("jstree组件data-search-input属性设置的id，无法定位",2);
					 return false;
				 }
				treeOptions['plugins'].push('search');
			 }else if(tree.data("search-by-jboltinput")){
				 treeOptions['plugins'].push('search');
			 }
			 
			 var checkboxEnable=tree.data("checkbox");
			 if(checkboxEnable){
				 treeOptions['plugins'].push('checkbox');
				 var onlyleaf=tree.data("onlyleaf")||false;
				 if(onlyleaf){
					 treeOptions['checkbox']={
							 "three_state": false,
							 "cascade": "down",
							 "keep_selected_style": false
					 };
				 }
				 hasCheckbox=true;
			 }
			 
			if(curd){
				treeOptions['plugins'].push('dnd');
				var target=tree.data("target");
				if(target&&target=="portal"){
					var portalId=tree.data("portalid")||tree.data("portal");
					if(!portalId){
						LayerMsgBox.alert("当data-target='portal'时，请设置:data-portalid",2);
						return false;
					}
				}
				var createUrl=tree.data("add-url");
				if(!createUrl){
					LayerMsgBox.alert("请设置创建节点的接口地址:data-add-url",2);
					return false;
				}
				var updateUrl=tree.data("edit-url");
				if(!updateUrl){
					LayerMsgBox.alert("请设置更新节点的接口地址:data-edit-url",2);
					return false;
				}
				var deleteUrl=tree.data("delete-url");
				if(!deleteUrl){
					LayerMsgBox.alert("请设置创建节点的接口地址:data-delete-url",2);
					return false;
				}
				var moveUrl=tree.data("move-url");
				if(!moveUrl){
					LayerMsgBox.alert("请设置创建节点的接口地址:data-move-url",2);
					return false;
				}
				treeOptions['plugins'].push('contextmenu');
				treeOptions['contextmenu']={
				"items":
					{
					"refresh":{
						"label":"刷新数据",
						"action":function(data){
							var reference=data.reference; 
							var inst = $.jstree.reference(reference),
				            obj = inst.get_node(reference);
							that.refresh(tree,obj.id);
							return true;
						}
					},
						"add":{
							"label":"创建子项",
							"action":function(data){
		                         var reference=data.reference; 
								 that.processCurdHandler(tree,reference,"add");
								 return true;
							}
						},
						"edit":{
							"label":"编辑此项",
							"action":function(data){
								 var reference=data.reference;  
								 that.processCurdHandler(tree,reference,"edit");
								 return true;
							}
						},
						"delete":{
							"label":"删除此项",
							"action":function(data){
								 var reference=data.reference;  
								 that.processCurdHandler(tree,reference,"delete");
								 return true;
							}
						}
						
					}
			}
			}else{
				tree.on("contextmenu",function(){
					return false;
				});
			/*	options['contextmenu']={
						"items":
							{
							"refresh":{
								"label":"刷新数据",
								"action":function(data){
									var inst = $.jstree.reference(data.reference);
									var ss=inst.get_selected();
									if(ss&&ss.length>0){
										that.refresh(tree,ss[0]);
									}else{
										that.refresh(tree);
									}
									return false;
								}
							}
							}
				};*/
			}
				
			
			treeOptions['plugins'].push('types');
			var defaultTypes={
			    "#" : {
				  "icon":"fa fa-folder",
			    },
			    "root" : {
			      "icon" : "fa fa-folder",
			    },
			    "default" : {
			      "icon" : "fa fa-folder-o",
			    },
			    "parent" : {
				      "icon" : "fa fa-folder-o",
				 },
			    "node" : {
				      "icon" : "fa fa-file-o",
				 },
				 "parent_opened":{
					 "icon" : "fa fa-folder-open-o",
					},
				 "root_opened":{
					 "icon" : "fa fa-folder-open",
					}
			}
			var types=tree.data("types");
			if(types){
				var cusTypes=eval(types)();
				Object.assign(defaultTypes,cusTypes);
			}
			treeOptions['types']=defaultTypes;
//			console.log(treeOptions['types']);
			var async=tree.data("async");
			var that=this;
			if(async){
				treeOptions['core']['data']=  function (obj, callback) {
                    var jsonarray =[];
                    var url=read_url;
                   if(obj.id!="#"){
                	   if(read_url.indexOf("?")!=-1){
                		   url=read_url+"&pid="+obj.id;
	       				}else{
	       					url=read_url+"?pid="+obj.id;
	       				} 
                   }
                    Ajax.get(url,function(data){
                    	jsonarray=data.data;
    				},null,true);
                    callback.call(this, jsonarray);
                    that.processCheckedByTreeDataValue(tree);
                    that.processCheckedByJBoltInput(tree);
                }
				var theTree=tree.jstree(treeOptions);
				tree.hasCheckbox = hasCheckbox;
				that.initTreeEvent(tree,theTree,searchInput,curd);
				that.processCheckedByTreeDataValue(tree);
				that.processCheckedByJBoltInput(tree);
			}else{
				
				Ajax.get(read_url,function(data){
					var treeDatas=data.data;
					treeOptions['core']['data']=treeDatas;
					var theTree=tree.jstree(treeOptions);
					tree.hasCheckbox = hasCheckbox;
					that.initTreeEvent(tree,theTree,searchInput,curd);
					that.processCheckedByJBoltInput(tree);
					that.processCheckedByTreeDataValue(tree);
				});
			}
			
		},
		processSyncEle:function(tree,node){
			var syncEles = tree.data("sync-ele");
			if(!syncEles){return;}
			var syncmust=tree.data("sync-must");
			if(typeof(syncmust)=="undefined"){
				syncmust=true;
			}
			var onlyleaf=tree.data("onlyleaf")||false;
			var onlytype=tree.data("onlytype")||"";
			var dataAttrFilterFunc = tree.data("onlyattrfilter")||"";
			var ele,attr,withroot,val;
			$(syncEles).each(function(){
				ele=$(this);
				attr=ele.data("sync-attr")||ele.data("value-attr");
				withroot=ele.data("sync-withroot");
				if(typeof(withroot)=="undefined"){
					withroot = false;
				}
				if(tree.hasCheckbox){
					if(attr){
						val = tree.jstree(true).get_all_checked(false,withroot,attr,onlyleaf,onlytype,dataAttrFilterFunc);
					}else{
						val = tree.jstree(true).get_all_checked(false,withroot,null,onlyleaf,onlytype,dataAttrFilterFunc);
					}	
				}else{
					if(node){
						if(attr){
							if(attr.startWith("data.")){
								if(node.data){
									val=node.data[attr.substring(5)];
								}else{
									val='';
								}
							}else{
								val=node[attr];
							}
						}
					}else{
						val=''; 
					}
				}
				 
				 if(typeof(val)=="undefined"){
					 val='';
				 }
				 if(isFormEle(this.tagName)){
					 if(this.value){
							if(syncmust){
								this.value=val;
							}
						}else{
							this.value=val;
						}
				 }else{
					 if(this.innerText){
							if(syncmust){
								this.innerText=val;
							}
						}else{
							this.innerText=val;
						}
				 }
				
			 });
			
			
		},
		setChecked:function(tree,selectId){
			if(!selectId){
				this.clearSelected(tree);
				return;
			}
			if(isArray(selectId)){
				tree.dontHideJBoltInputLayer = true;
				for(var i in selectId){
					if(!tree.jstree(true).is_parent({id:selectId[i]})){
						tree.jstree(true).check_node({id:selectId[i],checked:true})
					}
				}
				tree.dontHideJBoltInputLayer = false;
				return;
			}
			selectId = selectId+"";
			if(selectId.indexOf(",")!=-1){
				var arr = selectId.split(",");
				if(isOk(arr)){
					this.setChecked(tree,arr);
				}else{
					this.clearSelected(tree);
				}
			}else{
				tree.dontHideJBoltInputLayer = true;
				tree.jstree(true).select_node({id:selectId,selected:true});
				tree.dontHideJBoltInputLayer = false;
			}
		},
		processCheckedByTreeDataValue:function(tree){
			var jboltinputId = tree.data("jboltinput");
			if(jboltinputId){
				return;
			}
			var value = tree.data("value")||tree.data("select");
			if(!value){
				value = tree.data("default-value")||tree.data("default-select");
			}
			if(value){
				var that =this;
				setTimeout(function(){
					that.setChecked(tree,value);
				}, 100);
			}
		},
		processCheckedByJBoltInput:function(tree){
			var jboltinputId = tree.data("jboltinput");
			if(!jboltinputId){
				return;
			}
			var jboltInput = $("#"+jboltinputId);
			if(!isOk(jboltInput)){
				return;
			}
			var hiddenInputId = jboltInput.data("hidden-input");
			if(!hiddenInputId){
				return;
			}
			var hiddenInput = $("#"+hiddenInputId);
			if(!isOk(hiddenInput)){
				return;
			}
			var that =this;
			var value = hiddenInput.val();
			setTimeout(function(){
				that.setChecked(tree,value);
			}, 100);
		},
		initTreeEvent:function(tree,theTree,searchInput,curd){
			 var changehandler=tree.data("change-handler");
			 var jbolttablehandler=tree.data("jbolttable-handler");
			 if(tree.hasCheckbox){
				 tree.jstree(true).get_all_checked = function(full,withroot,attr,onlyleaf,onlytype,dataAttrFilterFunc) {
					 var tmp=new Array;
					 var tempId,tempData,datas = this._model.data,types;

					 for(var i in datas){
						 tempData = datas[i];
						 if((onlyleaf && this.is_checked(i) && this.is_leaf(i)) ||(!onlyleaf && (this.is_undetermined(i)||this.is_checked(i)))){
							 tempId = tempData.id;
							 if(attr){
								 if(attr.startWith("data.")){
									 if(tempData){
										 if(tempData.data){
											 tempId=tempData.data[attr.substring(5)];
										 }else{
											 tempId = null;
										 }
									 }else{
										 tempId = null;
									 }
								 }else{
									 if(tempData){
										 tempId=tempData[attr];
									 }else{
										 tempId = null;
									 }
								 }
							 }

							 if(!withroot && (tempId=='#' || tempId == '0' || tempId == null)){
								 continue;
							 }
							 if(isOk(onlytype)){
								 types = onlytype.split(",");
								 if(isOk(types)){
									 if(types.includes(tempData.type)){
										 tmp.push(full?tempData:tempId);
									 }
								 }
							 }else if(isOk(dataAttrFilterFunc)){
								var exe_func=eval(dataAttrFilterFunc);
								if(exe_func&&typeof(exe_func)=="function"){
									if(exe_func(tempData)){
										tmp.push(full?tempData:tempId);
									}
								}
							 }else{
								 tmp.push(full?tempData:tempId);
							 }
							 
						 }
					 }
					 return tmp;
				 }
			}
			var that=this;
			var syncEle = tree.data("sync-ele");
			if(changehandler||jbolttablehandler||isOk(syncEle)){
				 theTree.on('changed.jstree', function (e, tdata) {
					 if(tdata&&tdata.node){
						 if(tree.data("onlyleaf")){
							 var currentNode = tdata.node;
							 if (!tdata.instance.is_leaf(currentNode)) {
								 tdata.instance.deselect_node(currentNode);
								 layer.msg("只能选择子节点",{time:1000});
								 return false;
							 }
						 }
						if(changehandler){
							if(typeof(changehandler)=="string"&&changehandler=="portalEdit"){
								that.processChangeTextHandler(tree,tdata.node,"portalEdit");
							}else{
								var exe_handler=eval(changehandler);
								if(exe_handler&&typeof(exe_handler)=="function"){
									exe_handler(tree,tdata.node);
								}
							}
						}
						
						if(jbolttablehandler){
							var textasvalue=tree.data("textasvalue")||false;
							that.processJBoltTableHandler(tree,tdata.node.text,(textasvalue?tdata.node.text:tdata.node.id),tdata.node.data,jbolttablehandler);
						}
						
						that.processSyncEle(tree,tdata.node);
							
					}
				 });
			 }
			 theTree.on("after_open.jstree",function(e, tdata){
					 if(tdata.node.parent=="#"){
						 tdata.instance.set_type(tdata.node, 'root_opened');
					 }else{
						 tdata.instance.set_type(tdata.node, 'parent_opened');
					 }
				}).on("after_close.jstree",function(e, tdata){
					if(tdata.node.parent=="#"){
						tdata.instance.set_type(tdata.node, 'root');
					}else{
						tdata.instance.set_type(tdata.node, 'parent');
					}
					 
				});
			 
			 if(curd){
				var moveUrl=tree.data("move-url");
				if(!moveUrl){
					LayerMsgBox.alert("请设置创建节点的接口地址:data-move-url",2);
					return false;
				}
				 theTree.on('move_node.jstree', function (e, tdata) {
					 var parent = tdata.parent;
					 if(parent=="#"){
						 parent=0;
					 }
					 LayerMsgBox.loading("处理中...",10000);
						Ajax.post(moveUrl,{
							 'id' : tdata.node.id, 'pid' : parent, 'rank' : tdata.position 
						},function(res){
							LayerMsgBox.success("操作成功",1000);
							that.refresh(tree,tdata.node.id);
						});
					});
			 }
			 
			 
			 if(isOk(searchInput)){
				 var to = false;
				 searchInput.keyup(function() {
				    if(to) { clearTimeout(to); }
				    to = setTimeout(function() {
				    	tree.jstree(true).search(searchInput.val());
				    }, 100);
				  });
			 }
			 
			 
			
		},processConditionsForm:function(jstree){
			var formId = jstree.data("conditions-form");
			if(!formId){return;}
			var form=$("#"+formId);
			if(!isOk(form)){
				LayerMsgBox.alert("树绑定data-conditions-form未找到",2);
				return;
			}
			var that=this;
			form.on("submit",function(e){
				e.preventDefault();
				e.stopPropagation();
				if(FormChecker.check(form)){
					var orignUrl = jstree.data("orign-read-url");
					var read_url = urlWithFormData(orignUrl,form);
					jstree.data("read-url",read_url).attr("data-read-url",read_url);
					var node = jstree.jstree(true).get_selected(false);
					that.refresh(jstree,node);
				}
				return false;
			});
			return form;
		}
}

/**
 * 异步加载css 与 js 文件
 * 
 */
AssetsLazyLoad = (function (doc) {
    /**
     * 如何使用:
     *
     * CSS:
     *
       var cssFile = [           
            '/css/backstage/1.css',
            '/css/backstage/2.css', 
            '/css/backstage/3.css',
            '/css/backstage/4.css',    
            '/css/backstage/5.css',
            '/css/backstage/6.css'          
        ];
        //可以使用数组的形式,加载多个css文件. 也可以传入一个字符串,加载一个css
        AssetsLazyLoad.css(cssFile, function () {
            console.log("css加载完成...");
        });
     *
     *JS:
         var jsFile = [
            '/js/backstage/jquery/1.js',     
            '/js/backstage/jquery/2.js', 
            '/js/backstage/jquery/3.js', 
            '/js/backstage/jquery/4.js', 
            '/js/backstage/jquery/5.js', 
            '/js/backstage/jquery/6.js'             
        ];
        //可以使用数组的形式,加载多个js文件. 也可以传入一个字符串,加载一个js
        AssetsLazyLoad.js(jsFile, function () {
            console.log("js加载完成...");
        });
       */
 
    // Private Property --------------------------------------------------------
 
    var env,
        head,
        pending = {},
        pollCount = 0,
        queue = { css: [], js: [] },
        styleSheets = doc.styleSheets,
        startTime,
        endTime;
 
    // Private Methods --------------------------------------------------------
 
    /**
      创建并返回具有指定名称和属性的HTML元素。
    @method createNode
    @param {String} name 元素名
    @param {Object} attrs 元素属性的 名称/值 映射
    @return {HTMLElement}
    @private
    */
    function createNode(name, attrs) {
        var node = doc.createElement(name), attr;
 
        for (attr in attrs) {
            if (attrs.hasOwnProperty(attr)) {
                node.setAttribute(attr, attrs[attr]);
            }
        }
 
        return node;
    }
 
    /**
       当指定类型的当前挂起资源完成时调用装载。执行关联的回调(如果有)并加载下一个回调队列中的资源。
    @method finish
    @param {String} type 资源类型 ('css' or 'js')
    @private
    */
    function finish(type) {
        var p = pending[type], callback, urls;
 
        if (p) {
            callback = p.callback;
            urls = p.urls;
 
            urls.shift();
            pollCount = 0;
 
            // 如果这是最后一个挂起的url，则执行回调和
            // 启动队列中的下一个请求(如果有)。
            if (!urls.length) {
                callback && callback.call(p.context, p.obj);
                pending[type] = null;
                queue[type].length && load(type);
            }
        }
    }
 
    /**
    填充 <code>env</code> 变量带有用户代理和特性测试信息。
    @method getEnv
    @private
    */
    function getEnv() {
        var ua = navigator.userAgent;
 
        env = {
            //如果此浏览器支持动态禁用异步模式，则为True
            //创建脚本节点
            async: doc.createElement('script').async === true
        };
 
        (env.webkit = /AppleWebKit\//.test(ua))
            || (env.ie = /MSIE|Trident/.test(ua))
            || (env.opera = /Opera/.test(ua))
            || (env.gecko = /Gecko\//.test(ua))
            || (env.unknown = true);
    }
 
    /**
        加载指定的资源或指定类型的下一个资源
        如果没有指定资源，则在队列中。如果指定的资源
        类型已加载，新请求将排队，直到
        第一个请求已经完成。
        当指定资源url数组时，将加载这些url
        如果可能的话，在保持执行顺序的同时并行执行。所有
        浏览器支持CSS的并行加载，但只支持Firefox和Opera
        支持脚本的并行加载。在其他浏览器中，脚本将是
        排队并一次加载一个，以确保正确的执行顺序。
    @method load
    @param {String} type 资源类型 ('css' or 'js')
    @param {String|Array} urls (optional) 要加载的URL或URL数组
    @param {Function} callback (optional) 回调函数
    @param {Object} obj (optional) 对象传递给回调函数
    @param {Object} context (optional) 如果提供，则回调函数将在这个对象的上下文中执行
    @private
    */
    function load(type, urls, callback, obj, context) {
   		//开始计时
        startTime = new Date().getTime();
        
        var _finish = function () { finish(type); },
            isCSS = type === 'css',
            nodes = [],
            i, len, node, p, pendingUrls, url,media;
 
        env || getEnv();
 
        if (urls) {
            //如果url是字符串，则将其包装在数组中。否则假设它是
            //数组并创建它的副本，这样就不会对其进行修改
            urls = typeof urls === 'string' ? [urls] : urls.concat();
 
            // 为每个URL创建一个请求对象。如果指定了多个url，
            // 回调只会在加载所有url之后执行。
            //
            //遗憾的是，Firefox和Opera是唯一能够加载的浏览器
            //脚本并行，同时保持执行顺序。在所有其他
            //浏览器，脚本必须顺序加载。
            //
            //所有浏览器都根据链接的顺序尊重CSS的特性
            // DOM中的元素，而不考虑样式表的顺序
            //实际上是下载的。
            if (isCSS || env.async || env.gecko || env.opera) {
                // 并行加载
                queue[type].push({
                    urls: urls,
                    callback: callback,
                    obj: obj,
                    context: context
                });
            } else {
                // 加载顺序。
                for (i = 0, len = urls.length; i < len; ++i) {
                    queue[type].push({
                        urls: [urls[i]],
                        callback: i === len - 1 ? callback : null, // 回调只添加到最后一个URL
                        obj: obj,
                        context: context
                    });
                }
            }
        }
 
        //如果之前的这种类型的加载请求正在进行中，那么我们将
        //轮到我们了。否则，获取队列中的下一项。
        if (pending[type] || !(p = pending[type] = queue[type].shift())) {
            return;
        }
		//获取head标签
        head || (head = doc.head || doc.getElementsByTagName('head')[0]);
        pendingUrls = p.urls.concat();
 
        for (i = 0, len = pendingUrls.length; i < len; ++i) {
            url = pendingUrls[i];
            //开始拼接 标签
            if (isCSS) {
            	if(typeof(url)=="string"){
            		node = env.gecko ? createNode('style') : createNode('link', {
            			href: url,
            			rel: 'stylesheet'
            		});
            	}else{
            		media=url.media;
            		if(media){
            			node = env.gecko ? createNode('style') : createNode('link', {
                			href: url.url,
                			media:media,
                			rel: url.rel?url.rel:'stylesheet'
                		});
            		}else{
            			node = env.gecko ? createNode('style') : createNode('link', {
                			href: url.url,
                			rel: url.rel?url.rel:'stylesheet'
                		});
            		}
            		
            	}
            } else {
                node = createNode('script', { src: url });
                node.async = false;
            }
 
            node.className = 'lazyload';
            node.setAttribute('charset', 'utf-8');
 
            if (env.ie && !isCSS && 'onreadystatechange' in node && !('draggable' in node)) {
                node.onreadystatechange = function () {
                    if (/loaded|complete/.test(node.readyState)) {
                        node.onreadystatechange = null;
                        _finish();
                    }
                };
            } else if (isCSS && (env.gecko || env.webkit)) {
                // Gecko和WebKit不支持链接节点上的onload事件。
                if (env.webkit) {
                    //在WebKit中，我们可以轮询对文档的更改。样式表
                    //确定样式表何时加载。
                    p.urls[i] = node.href; //解析相对url(或轮询不起作用)
                    pollWebKit();
                } else {
                    //在Gecko中，我们可以将请求的URL导入到<style>节点中
                    //轮询node.sheet.cssRules是否存在。
                    node.innerHTML = '@import "' + url + '";';
                    pollGecko(node);
                }
            } else {
                node.onload = node.onerror = _finish;
            }
 
            nodes.push(node);
        }
 
        for (i = 0, len = nodes.length; i < len; ++i) {
            head.appendChild(nodes[i]);
			//控制台日志部分(不需要可以删除)
			//start
            var url = pendingUrls[i];
            if (/.js/.exec(url)) {
                jboltlog((i + 1) + "--> js成功:  " + url);
            } else if (/.css/.exec(url)) {
            	jboltlog((i + 1) + "--> css成功:  " + url);
            } else {
                console.log("error:  " + url);
            }
            //end
        }
        
		//结束计时
        endTime = new Date().getTime();
		//控制台日志部分(不需要可以删除)
		//(startTime (在此方法开头),endTime(在此行代码上方) 都可以删除 )
        jboltlog("执行时间: " + (endTime - startTime) + " ms  -------  end时间戳:" + endTime);
    }
 
    /**
        开始轮询，以确定指定的样式表何时完成加载
        轮询在加载所有挂起的样式表或加载10个样式表之后停止
        秒(防止停顿)。
    @method pollGecko
    @param {HTMLElement} node 样式节点。
    @private
    */
    function pollGecko(node) {
        var hasRules;
 
        try {
            //我们不需要存储这个值，也不需要再次引用它，但是
            //如果我们不存储它，闭包编译器会认为代码是无用的
            //删除它。
            hasRules = !!node.sheet.cssRules;
        } catch (ex) {
            // 异常意味着样式表仍在加载。
            pollCount += 1;
 
            if (pollCount < 200) {
                setTimeout(function () { pollGecko(node); }, 50);
            } else {
                //我们已经轮询了10秒钟，什么都没有发生。
                //停止轮询并完成挂起的请求，以避免进一步阻塞请求。
                hasRules && finish('css');
            }
 
            return;
        }
        
        // 到这里，样式表已经加载。
        finish('css');
    }
 
    /**
        开始轮询，以确定挂起的样式表何时完成加载
        在WebKit。轮询在加载所有挂起的样式表或加载10个样式表之后停止
        秒(防止停顿)。
    @method pollWebKit
    @private
    */
    function pollWebKit() {
        var css = pending.css, i;
 
        if (css) {
            i = styleSheets.length;
 
            // 查找与挂起的URL匹配的样式表。
            while (--i >= 0) {
                if (styleSheets[i].href === css.urls[0]) {
                    finish('css');
                    break;
                }
            }
 
            pollCount += 1;
 
            if (css) {
                if (pollCount < 200) {
                    setTimeout(pollWebKit, 50);
                } else {
                    //我们已经轮询了10秒钟，什么都没有发生
                    //表示样式表已从文档中删除
                    //在它有机会装载之前。停止轮询并完成挂起
                    //请求以防止阻止进一步的请求。
                    finish('css');
                }
            }
        }
    }
 
    // Public Methods --------------------------------------------------------
    return {
        /**
            请求指定的CSS URL或URL并执行指定的
            当它们完成加载时回调(如果有的话)。如果一个url数组是
            指定后，样式表将与回调并行加载
            将在所有样式表加载完成后执行。
        @method css
        @param {String|Array} urls CSS URL或要加载的CSS URL数组
        @param {Function} callback (optional) 回调函数
        @param {Object} obj (optional) 对象传递给回调函数
        @param {Object} context (optional) 如果提供，回调函数将在这个对象的上下文中执行吗
        @static
        */
        css: function (urls, callback, obj, context) {
            load('css', urls, callback, obj, context);
        },
 
        /**
            请求指定的JavaScript URL并执行指定的
            当它们完成加载时回调(如果有的话)。如果一个url数组是
            指定并得到浏览器的支持后，脚本将被加载进来
            并将在所有脚本完成后执行回调
            完成加载。
            目前，只有Firefox和Opera支持同时并行加载脚本
            保存执行顺序。在其他浏览器中，脚本将是
            排队并一次加载一个，以确保正确的执行顺序
        @method js
        @param {String|Array} urls JS URL或要加载的JS URL数组
        @param {Function} callback (optional) 回调函数
        @param {Object} obj (optional) 对象传递给回调函数
        @param {Object} context (optional) 如果提供，回调函数将在这个对象的上下文中执行吗
        @static
        */
        js: function (urls, callback, obj, context) {
            load('js', urls, callback, obj, context);
        }
    };
})(this.document);
/**
 * 图片查看器
 */
var ImageViewerUtil={
		initViewers:function(viewers){
			if(!isOk(viewers)){return false;}
			var that=this;
			loadJBoltPlugin(["imgviewer"], function(){
				var len=viewers.length;
				for(var i=0;i<len;i++){
					that.initViewer(viewers.eq(i),false);
				}
			});
			
		},
		init:function(parentEle){
			 var parent=getRealParentJqueryObject(parentEle);
			 if(!isOk(parent)){return false;}
			 var viewers=parent.find("[data-imgviewer]");
			 if(!isOk(viewers)){return false;}
			 this.initViewers(viewers);
		},
		_initViewer:function(viewer){
			var isImg=viewer.is("img");
			if(isImg){
				var orignurl=viewer.data("origin-url")||viewer.data("orign-url")||viewer.data("orignurl");
				if(orignurl){
					viewer.viewer({navbar:false,url:"data-origin-url"});
				}else{
					viewer.viewer({navbar:false});
				}
				return;
			}
			var isDivBox=viewer.is("div") || viewer.is("section") || viewer.is("p");
			if(isDivBox){
				var useorign=viewer.data("useorigin")||viewer.data("useorign");
				if(useorign){
					viewer.viewer({url:"data-origin-url"});
				}else{
					viewer.viewer();
				}
				return;
			}
			
		    this.initLinkViewerClick(viewer);
				
		},
		//初始化超链接相册
		albumLinkViewerClick:function(viewer,album,albumLinks){
			LayerMsgBox.loading("加载中...",5000);
			var imgId=viewer.data("imgid");
			var container=viewer.closest(".jbolt_page");
			var existAlbum = container.find(".imgviewer-album[data-id='"+album+"']");
			if(isOk(existAlbum)){
				var imgs = existAlbum.find("img[data-album='"+album+"']");
				if(imgs.length == albumLinks.length){
					var clickImg=existAlbum.find("img[data-album='"+album+"'][data-imgid='"+imgId+"']");
					if(isOk(clickImg)){
						clickImg.trigger("click");
						LayerMsgBox.closeLoadingNow();
						return;
					}
					existAlbum.remove();
				}else{
					existAlbum.remove();
				}
			}
			var html='<div class="imgviewer-album d-none" data-id="'+album+'"><div class="data-imgviewer">';
			var temp,tempImgid,imgurl;
			albumLinks.each(function(){
				temp=$(this);
				tempImgid=temp.data("imgid");
				if(!tempImgid){
					tempImgid = randomId();
					temp.data("imgid",tempImgid).attr("data-imgid",tempImgid);
				}
				imgurl=temp.data("url")||temp.attr("href");
				html+='<img style="width:100px;height:100px" class="d-block"  src="'+imgurl+'"  data-imgid="'+tempImgid+'"  data-album="'+album+'"/>';
			});
			html+='</div></div>';
			existAlbum=$(html);
			container.append(existAlbum);
			ImageViewerUtil._initViewer(existAlbum);
			var clickImg=existAlbum.find("img[data-album='"+album+"'][data-imgid='"+imgId+"']");
			if(isOk(clickImg)){
				clickImg.trigger("click");
			}
			LayerMsgBox.closeLoadingNow();
		},
		//初始化一个链接点击
		initLinkViewerClick:function(viewer){
			var that=this;
			viewer.off("click").on("click",function(e){
				e.preventDefault();
				e.stopPropagation();
				var tar=$(this);
				var url=tar.data("url")||tar.attr("href");
				if(!url){
					LayerMsgBox.alert("请给点击的元素加上data-url属性 配置图片地址",2);
					return;
				}
				var album=tar.data("album");
				if(album){
					var imgId=tar.data("imgid");
					if(!imgId){
						var ranId=randomId();
						tar.data("imgid",ranId).attr("data-imgid",ranId);
					}
					var albums = tar.closest(".jbolt_page").find("[data-imgviewer][data-album='"+album+"'][href],[data-imgviewer][data-album='"+album+"'][data-url]");
					var len = albums.length;
					if(len==1){
						that.oneLinkViewerClick(url);
					}else{
						that.albumLinkViewerClick(tar,album,albums);
					}
				}else{
					that.oneLinkViewerClick(url);
				}
				return false;
			});
		},
		oneLinkViewerClick:function(url){
			var image = new Image();
		     image.src = url;
	         var nviewer = new Viewer(image,{
	          hidden: function () {
	        	  nviewer.destroy();
	          }
	        });
	         nviewer.show();
		},
		initViewer:function(viewer,needLoadPlugin){
			var that=this;
			if(needLoadPlugin){
				loadJBoltPlugin(['imgviewer'], function(){
					that._initViewer(viewer);
				});
			}else{
				this._initViewer(viewer);
			}
			
		}
}
/**
 * 处理autocomplate组件的items
 * @param column_attr
 * @returns
 */
function processAutocompleteItem(data,column_attr,align){
	if(typeof(align)=="undefined"){align="center";}
	var text="";
	if(!data){return "----";}
	if(column_attr.indexOf(",")==-1){
		text=data[column_attr];
		if(!text&&(text=="undefined"||text==undefined)){
			if(column_attr!="text"){
				text=data["text"]||'-';
				if(!text&&(text=="undefined"||text==undefined)){
					text=data["name"]||"-";
					if(!text&&(text=="undefined"||text==undefined)){
						text=data["title"]||"-";
					}
				}
			}else{
				if(!text&&(text=="undefined"||text==undefined)){
					text=data["name"]||"-";
					if(!text&&(text=="undefined"||text==undefined)){
						text=data["title"]||"-";
					}
				}
			}
			
		}
		return "<span style='text-align:"+align+"'>"+text+"</span>";
	}
	var attrs=column_attr.split(",");
	var t,colname,colarr,width,textAlign=align,tempTextAlign;
	for(var i in attrs){
		textAlign=align;
		colname=attrs[i];
		if(colname.indexOf("-")!=-1){
			colarr=colname.split("-");
			t=data[colarr[0]];
			width=colarr[1];
			if(colarr.length==3){
				tempTextAlign=$.trim(colarr[2]);
				if(tempTextAlign&&tempTextAlign.length>0){
					textAlign=tempTextAlign;
				}
			}
			if(width.indexOf("px")==-1&&width.indexOf("%")==-1){
				width=width+"px";
			}
		}else{
			t=data[colname];
			width=null;
		}
		var  ttype=typeof(t);
		if(ttype=="undefined"){
			t="-";
		}else if(ttype=="string"&&t.length==0){
			t="-";
		}
		var isAuto = width;
		width = width?("width:"+width+";"):'';
		text=text+"<span "+(isAuto?"class='auto'":'')+" style='"+width+"text-align:"+textAlign+"'>"+t+"</span>";
	}
	return text;
}
/**
 * 处理一个组件绑定了另外组件作为param的URL
 * dialog autocomplate组件通用
 * @param ele
 * @param url
 * @param bindChange
 * @param bindChangeEvent
 * @returns
 */
function processEleUrlByLinkOtherParamEle(ele,url,bindChange,bindChangeEvent){
	  var params=ele.data("link-para-ele"),paras=null,paramArr,peles,oldParams,eleSelector,isRequired=false;
	  if(params&&params!='#'){
		  paramArr=params.split(",");
		  if(!isOk(paramArr)){
			  return url;
		  }
		  oldParams=params.replaceAll("_required","");
		  var vv,nn,name,eleObj;
		  var msg="";
		$.each(paramArr,function(i,eleSelectorAll){
			isRequired=eleSelectorAll.endWith("_required");
			if(isRequired){
				eleSelector=eleSelectorAll.replace("_required","");
			}else{
				eleSelector=eleSelectorAll;
			}
			if(eleSelector){
				eleObj=$(eleSelector);
				if(isOk(eleObj)){
					if(!eleObj.is("input") && !eleObj.is("textarea")&& !eleObj.is("select")){
						if(eleObj[0].hasAttribute("data-radio")){
							vv=RadioUtil.getCheckedValue(eleObj.data("name"),eleObj);
						}else if(eleObj[0].hasAttribute("data-checkbox")){
							vv=CheckboxUtil.getCheckedValueToString(eleObj.data("name"),",",eleObj);
						}else{
							vv = eleObj.data("value") || eleObj.data("text") || eleObj.val();
						}
					}else{
						vv=eleObj.val();
					}
					if(isRequired && (vv==undefined || vv == '')){
						msg="关联组件["+eleSelector+"]的值不能为空<br/>";
						return false;
					}
					name=eleObj.data("para-name")||eleObj.data("name")||eleObj.attr("name")||eleObj.attr("id");
					if(name){
						if(name.indexOf(".")!=-1){
							name=name.split(".")[1];
						}
						if(isOk(vv)){
							if(paras==null){
								paras={};
							}
							paras[name]=vv;
						}
					}
				}
			}
			
		});
			
		if(msg&&msg.length>0){
			LayerMsgBox.alert(msg,2);
			return false;
		}	
			
	  }else{
		  paras=ele.data("paras");
	  }
	  
	  if(paras){
			url=urlWithJsonData(url,paras);
		 }
	  if(bindChange&&oldParams){
		  $(oldParams).each(function(){
			  var obj=$(this);
			  if(obj.is("select") && obj.data("linkage")){
				  obj.on("linkChange",function(){
					  if(bindChangeEvent){
						  bindChangeEvent();
					  }
				  });
			  }else{
				  obj.on("change",function(){
					  if(bindChangeEvent){
						  bindChangeEvent();
					  }
				  });
			  }
		  })
	  }
	  return url;
}

/**
 * 表格中管理列值
 * @param ele
 * @param url
 * @returns
 */
function processJBoltTableEleUrlByLinkColumn(ele,url){
	 if(!isOk(ele)){return url;}
	  var linkcolumn=ele.data("link-column"),columnName,columnAttr,paras;
	  if(linkcolumn){
		var table=ele.closest("table");
		if(!isOk(table)){return false;}
		var jboltTable=table.jboltTable("inst");
		if(!jboltTable){return false;}
		var tr=ele.closest("tr");
		var columns=linkcolumn.split(",");
		var size=columns.length;
		var colIndex=-1,td,vv,isRequired=false,msg='',columnTh,columnText,trJsonData;
		paras={};
		for(var i=0;i<size;i++){
			columnName=columns[i];
			isRequired=columnName.endWith("_required");
			if(isRequired){
				columnName=columnName.replace("_required","");
			}
			/*colIndex=jboltTable.columnIndexMap[columnName];
			if(colIndex>=0){
				columnAttr=StrUtil.camel(columnName);
				td=tr.find("td[data-col-index='"+colIndex+"']");
				if(isOk(td)){
					vv=td.data("value")||td.data("text")||td.text();
					if(isRequired && (vv==undefined || vv == "")){
						columnTh=jboltTable.thead.find("tr>th[data-col-index='"+colIndex+"']");
						if(isOk(columnTh)){
							columnText=columnTh.text();
						}else{
							columnText=columnName;
						}
						msg="关联本行数据内列["+columnText+"]的值不能为空<br/>";
						break;
					}
					paras[columnAttr]=vv;
				}
			}*/
			
			columnAttr=StrUtil.camel(columnName);
			colIndex=jboltTable.columnIndexMap[columnName];
			trJsonData= jboltTable.tableListDatas[tr.data("index")];
			vv=isOk(trJsonData)?(trJsonData[columnAttr]||trJsonData[columnName]):null;
			if(isRequired && !isOk(vv)){
				if(colIndex>=0){
					columnTh=jboltTable.thead.find("tr>th[data-col-index='"+colIndex+"']");
					if(isOk(columnTh)){
						columnText=columnTh.text();
					}else{
						columnText=columnName;
					}
					msg="关联本行数据内列["+columnText+"]的值不能为空<br/>";
				}else{
					msg="关联本行数据内属性["+columnAttr+"]的值不能为空<br/>";
				}
				break;
			}
			paras[columnAttr]=vv;
			
		}
		if(msg&&msg.length>0){
			LayerMsgBox.alert(msg,2);
			return false;
		}
	  }
	  
	  if(paras){
			url=urlWithJsonData(url,paras);
		 }
	  return url;
}
/**
 * 刷新指定的Autocomplete组件 cache
 * @param inputEle
 * @returns
 */
function flushAutocompleteCache(inputEle){
	AutocompleteUtil.flushCache(inputEle);
}

/**
 * 刷新按钮指定的Autocomplete组件cache数据
 * @param btnEle
 * @returns
 */
function flushTheAutocompleteCache(btnEle){
	var btn=getRealJqueryObject(btnEle);
	var inputEle = btn.data("input-id");
	if(inputEle){
		flushAutocompleteCache(inputEle);
	}else{
		LayerMsgBox.alert("按钮上需要data-input-id属性指定哪个Autocomplete组件",2);
	}
}
/**
 *自动关键词查询检索 完成组件封装
 */
var AutocompleteUtil={
		get:function(ele){
			var ac_input=getRealJqueryObject(ele);
			if(!isOk(ac_input)){
				return null;
			}
			return ac_input.data("acobj");
		},
		resetParamBind:function(ele){
			var ac_input=this.get(ele);
			if(!isOk(ac_input)){
//				LayerMsgBox.alert("AutocompleteUtil.resetParamBind,获取不到autocomplete对象");
				return;
			}
			var url=ac_input.data("url");
			var newUrl=processEleUrlByLinkOtherParamEle(ac_input,url,false);
			if(newUrl){
				newUrl=processJBoltTableEleUrlByLinkColumn(ac_input,newUrl);
				var options={url:newUrl};
				ac_input.setOptions(options);
				ac_input.flushCache();
			}
			
		},
		changeUrl:function(ele,url){
			var options={url:url};
			this.setOptions(ele,options);
		},
		setOptions:function(ele,options){
			var ac_input=this.get(ele);
			if(ac_input){
				ac_input.setOptions(options);
				ac_input.flushCache();
			}else{
				LayerMsgBox.alert("AutocompleteUtil.changeUrl异常,获取不到autocomplete对象")
			}
		},
		flushCache:function(inputEle){
			var input=getRealJqueryObject(inputEle);
			if(isOk(input)){
				input.autocomplete().flushCache();
			}
		},
		initInputs:function(inputs){
			if(!isOk(inputs)){return false;}
			var that=this;
			loadJBoltPlugin(['autocomplete'], function(){
				var len=inputs.length;
				for(var i=0;i<len;i++){
					that.initInput(inputs.eq(i),false);
				}
			});
		},
	init:function(parentEle){
		 var parent=getRealParentJqueryObject(parentEle);
		 if(!isOk(parent)){return false;}
		 var inputs=parent.find("input[data-autocomplete]");
		 if(!isOk(inputs)){return false;}
		 this.initInputs(inputs);
	},defaultFormat:function(data,column_attr,align){
		return processAutocompleteItem(data,column_attr,align);
	},
	processGetSyncEditor:function(input,defaultValueAttr){
		var syncEditorId=input.data("sync-editor-id");
		if(!syncEditorId){return  null;}
		var syncEditorType=input.data("sync-editor-type");
		var syncAttr=input.data("sync-attr");
		var editorInput=document.createElement("input");
		var $ei=$(editorInput);
		$ei.data("editor-id",syncEditorId);
		$ei.data("editor-type",syncEditorType);
		$ei.data("value-attr",syncAttr?syncAttr:defaultValueAttr);
		return  $ei;
	},
	processGetAutocomplateHiddens:function(hiddenInputIds){
		var result=new Array();
		if(hiddenInputIds){
			if(hiddenInputIds.indexOf(",")==-1){
				if(hiddenInputIds.indexOf("#")==-1){
					result.push($("#"+hiddenInputIds));
				}else{
					result.push($(hiddenInputIds));
				}
			}else{
				var temp;
				var arr=hiddenInputIds.split(",");
				for(var i in arr){
					temp=arr[i];
					if(temp.indexOf("#")==-1){
						result.push($("#"+temp));
					}else{
						result.push($(temp));
					}
				}
			}
		}
		return result;
		
	},changeHiddenInputValue:function(input,data,selectValueDefault){
		if(!isOk(input)){return false;}
		var valueType=typeof(selectValueDefault);
		if(valueType=="undefined"){
			selectValueDefault="";
		}
		var valueAttr=input.data("value-attr")||input.data("sync-attr");
		var val="";
		if(valueAttr){
			if(valueAttr=="value"){
				val=val+selectValueDefault+"";
			}else{
				val=val+(data[valueAttr]||"");
			}
		}else{
			val=val+selectValueDefault+"";
		}
		var editorType=input.data("editor-type");
		if(editorType){
			var inputId=input.data("editor-id");
			if(editorType=="neditor"){
				UE.getEditor(inputId).execCommand('insertHtml', val);
			}else if(editorType=="summernote"){
				$('#'+inputId).summernote('code', val);
			}else{
				input.val(val).change();
			}
		}else{
			input.val(val).change();
		}
		
	},changeHiddenInputsValue:function(inputs,data,selectValueDefault){
		if(isOk(inputs)&&isArray(inputs)){
			for(var i in inputs){
				this.changeHiddenInputValue(inputs[i],data,selectValueDefault);
			}
		}else{
			this.changeHiddenInputValue(inputs,data,selectValueDefault);
		}
		
	},clearHiddenInputsValue:function(inputs){
		if(isOk(inputs)){
			if(isArray(inputs)){
				for(var i in inputs){
					inputs[i].val("").change();
				}
			}else{
				inputs.val("").change();
			}
		}
	},processJBoltTableHandler:function(input,data,value){
		var jbolttableHandler=input.data("jbolttable-handler");
		if(jbolttableHandler){
				var table=input.closest("table");
				if(isOk(table)){
					var jboltTable=table.jboltTable("inst");
					if(jboltTable){
						var td=input.closest("td");
						var jsonData=jboltTable.tableListDatas[td.closest("tr").data("index")];
						var isChangeColumns  =  td.data("jbolttable-handler-ischangecolumns");
						if(!isChangeColumns){
							jboltTable.me.processColConfigChangeColumns(jboltTable,td,data);
						}
//						jbolttableHandler(jboltTable,td,input,data,jsonData);
						jbolttableHandler(jboltTable,td,input.data("text"),value,jsonData,data);
						//处理焦点跳转辅助录入
						setTimeout(function(){
							var success=jboltTable.me.processFocusChangeToExtraForm(jboltTable,td);
							if(success){
								var nextTd=jboltTable.tbody.focusTd.nextTd;
								if(nextTd){
									jboltTable.me.changeTdFocus(jboltTable,nextTd);
									jboltTable.isEditableLock=false;
								}
							}
						}, 100);
					}
					
				}
		}
	},
	_initInput:function(input){
		var url=input.data("url");
		var isInlineBlock=input.hasClass("d-inline-block");
		if(isInlineBlock){
			input.removeClass("d-inline-block");
		}
		var wrap=$('<div class="ac_input_box '+(isInlineBlock?'d-inline-block':'')+'"></div>');
		var clearBtnBox=$('<div class="closeBtnBox"><a tabindex="-1" href="javascript:void(0)" class="close" aria-label="Close"><span aria-hidden="true">&times;</span></a></div>');
		
		
		url=actionUrl(url);
		url=processEleUrlByLinkOtherParamEle(input,url,true,function(){
			AutocompleteUtil.resetParamBind(input);
		});
		if(!url){
			return false;
		}
		url=processJBoltTableEleUrlByLinkColumn(input,url);
		if(!url){
			return false;
		}
		var hiddenInputId=input.data("sync-ele")||input.data("hidden-input")||input.data("hiddeninput")||input.data("sync-input")||input.data("syncinput");
//		var synceditor_id=input.data("sync-editor-id");
		/*if(!hiddenInputId&&!synceditor_id){
			LayerMsgBox.alert("autocomplete组件未设置data-hiddeninput或data-sync-editor-id属性,需关联指定的隐藏域或者editor",2);
			return false;
		}*/
		var that=this,
		syncInputArray=new Array(),
		width=input.data("width"),
			height=input.data("height")||300,

		widthAuto=false,
		text_attr=input.data("text-attr"),
		value_attr=input.data("value-attr"),
		column_attr=input.data("column-attr"),
		formatHandler=input.data("format-handler"),
		handler=input.data("handler"),
		limit=input.data("limit");
		if(!limit){
			limit=100;
		}
		if(!width){
			width=parseInt(input.css("width"));
			widthAuto=true;
		}
		if(!text_attr){
			text_attr="text";
		}
		if(!value_attr){
			value_attr="value";
		}
		if(!column_attr){
			column_attr=text_attr;
		}
		if(column_attr.indexOf(",")!=-1&&widthAuto){
			var arr=column_attr.split(",");
			var autoW=(arr.length+1)*100;
			if(autoW>width){
				width=autoW;
			}
		}
		
		var hiddenInput=that.processGetAutocomplateHiddens(hiddenInputId),
		editor=that.processGetSyncEditor(input,value_attr);
		if(editor){
			hiddenInput.push(editor);
		}
		if(isOk(hiddenInput)){
			input.hiddenInput=hiddenInput;
		}
		/*if(hiddenInput.length==0){
			LayerMsgBox.alert("请关联指定的正确的隐藏域或者editor",2);
			return false;
		}*/
		var inputId=input.attr("id");
		if(!inputId){
			inputId="autocomplate_"+randomId();
			input.attr("id",inputId);
		}
		
		input.wrap(wrap);
		input.after(clearBtnBox);
		var refresh=input.data("refresh");
		if(typeof(refresh)=="undefined"){
			refresh=false;
		}
		if(refresh){
			var inJBoltTable=input.closest("table[data-jbolttable]");
			if(!isOk(inJBoltTable)){
			
			var refreshBtnBox=$('<div tooltip title="刷新缓存" class="refreshBtn input-group-append hand text-center"><span class="input-group-text"><i class="fa fa-refresh"></i></span></div>');
			var fg=input.closest(".ac_input_box");
			if(isOk(fg)){
				fg.addClass("input-group");
			}
			input.parent().append(refreshBtnBox);
			refreshBtnBox.off("click").on("click",function(e){
				e.preventDefault();
				e.stopPropagation();
				LayerMsgBox.loading("执行中...",2000);
				disposeTooltip(refreshBtnBox);
				input.flushCache();
				LayerMsgBox.success("缓存已刷新",500);
				return false;
			});
			refreshBtnBox.tooltip({ boundary: 'window',container:"body"})
		}
		}
		var delimiter=input.data("delimiter");
		var textFormatHandler=input.data("text-format-handler");
		var mustMatch = input.data("mustmatch");
		if(typeof(mustMatch)=='undefined'){
			mustMatch=true;
		}
		var align = input.data("text-align")||"center";
		var header = input.data("header");
		//开始绑定
		var ac=input.autocomplete(url,{
		    minChars:0,
		    width:width,
		    scrollHeight:height,
		    matchContains:true,
		    autoFill: false,
		    matchSubset:false,
		    mustMatch:false,
		    dataType: 'json',
		    max:limit,
		    parse: function(res) {
		      var rows = [];
		      if(!res || (!res.data)){
		        return rows;
		      }
		      
		      var datas=res.data,size=datas.length,
		      text,value,optionItem;

		      for(var i=0;i<size;i++){
		    	  optionItem=datas[i];
		    	text=processOptionTextByFormatHandler(textFormatHandler,optionItem,text_attr,delimiter);
				value=processOptionValue(optionItem,value_attr);
		        rows[rows.length] = {
		          data:optionItem,       //下拉框显示数据格式
		          value:value,   //选定后实际数据格式
		          result:text//选定后输入框显示数据格式
		        };
		      }
		      return rows;
		    },
		    formatItem: function(row, i, max) {
		    	return that.defaultFormat(row,column_attr,align);
		    }
		  }).result(function(e,data,value,sec){
			  input.data("exe-result",true);
			  clearBtnBox.show();
			  FormChecker.checkIt(input);
			  that.changeHiddenInputsValue(hiddenInput,data,value);
			  if(handler){
					var exe_handler=eval(handler);
					if(exe_handler&&typeof(exe_handler)=="function"){
						exe_handler(input,hiddenInput,value,data);
					}
				}
			  
			  that.processJBoltTableHandler(input,data,value);
			  
		  }).bind("unmatch", function(){
			  //取消选择的时候 清空hidden
			  input.val("").change();
			  that.clearHiddenInputsValue(hiddenInput);
			  if(handler){
					var exe_handler=eval(handler);
					if(exe_handler&&typeof(exe_handler)=="function"){
						exe_handler(input,hiddenInput);
					}
				}
//			  that.processJBoltTableHandler(input,{});
		  }).blur(function(){
			var textasvalue=input.data("textasvalue");
			if(!textasvalue){
			  clearBtnBox.show();
			if(isOk(hiddenInput)&&isArray(hiddenInput)){
		   		var clear=true,hinput;
		   		for(var i in hiddenInput){
		   			hinput=hiddenInput[i];
		   			if($.trim(hinput.val())){
		   				clear=false;
				   	}
		   		}
		   		if(clear&&mustMatch){
		   			input.val("").change();
		   			clearBtnBox.hide();
		   		}
		   	}else{
		   		if((isOk(hiddenInput)&&hiddenInput.val()=="")) {
					input.val("").change();
					clearBtnBox.hide();
				}
		   	}
			
			}
			}).on("input",function(){
				if(input.val()){
					clearBtnBox.show();
				}else{
					clearBtnBox.hide();
				}
					that.clearHiddenInputsValue(hiddenInput);
				 });
		
		input.data("acobj",ac);
		
		if(input.val()){
			 clearBtnBox.show();
		}
		clearBtnBox.off("click").on("click",function(e){
			e.preventDefault();
			e.stopPropagation();
			input.removeClass("is-valid");
			input.removeClass("is-invalid");
			var tooltipId=input.attr("aria-describedby");
			if(tooltipId){
				input.tooltip("dispose");
			}
			input.val("").change();
			that.clearHiddenInputsValue(hiddenInput);
			clearBtnBox.hide();
			
			 if(handler){
					var exe_handler=eval(handler);
					if(exe_handler&&typeof(exe_handler)=="function"){
						exe_handler(input,hiddenInput);
					}
				}
			 that.processJBoltTableHandler(input);
			return false;
		});
	},
	initInput:function(input,needLoadPlugin){
		var url=input.data("url");
		if(!url){
			LayerMsgBox.alert("autocomplete组件未设置data-url属性",2);
			return false;
		}
		var that=this;
		if(needLoadPlugin){
			loadJBoltPlugin(['autocomplete'], function(){
				that._initInput(input);
			});
		}else{
			that._initInput(input);
		}
	},hideResult:function(){
		if(mainPjaxContainer){
			var inputs=mainPjaxContainer.find("input[data-autocomplete]");
			if(isOk(inputs)){
				var acId;
				inputs.each(function(){
					acId=$(this).data("acresult");
					if(acId){
						$("#"+acId).hide();
					}
				})
			}
		}else{
			$("div.ac_results").hide();
		}
		
	}
}




/**
 * 关闭JBoltLayer组件
 * @param dontShowConfirm
 * @returns
 */
function closeJBoltLayer(dontShowConfirm){
	JBoltLayerUtil.close(dontShowConfirm);
}
/**
 * JBoltLayer组件
 * 从左侧或者右侧滑出
 */
var JBoltLayerUtil={
		init:function(){
			var that=this;
			jboltBody.on("click","[data-jboltlayertrigger]",function(e){
				  e.preventDefault();
				  e.stopPropagation();
				  JBoltInputUtil.hideJBoltInputLayer();
				  JBoltTabUtil.hideJboltContextMenu();
				  FormDate.hide();
				that.open($(this));
				return false;
			}).on("click","[data-closejboltlayer]",function(e){
				  e.preventDefault();
				  e.stopPropagation();
				that.close();
				return false;
			}).on("click",".jbolt_layer",function(e){
				 e.preventDefault();
				e.stopPropagation();
				var layer=$(this);
				if(!layer.data("cancel-click")){
					that.close(false,"jbolt_layer");
				}
				return false;
			}).on("click",".jbolt_layer_portal",function(e){
					if(e.target.tagName.toLowerCase()!="input"){
						FormDate.hide();
					}
					JBoltInputUtil.hideJBoltInputLayer();
				  e.stopPropagation();
			}).on("click",".jbolt_admin_main",function(e){
					that.close(false,"body");
			}).on("submit",".jbolt_layer_portal form[data-jboltlayersubmit]",function(){
				var form=$(this);
				var portal=form.closest("[data-ajaxportal]");
				if(isOk(portal)){
					var needcheck=form.data("needcheck");
					if(needcheck!=undefined&&needcheck!=null&&needcheck==true){
						 if(FormChecker.check(form)){
							 formSubmitToAjaxPortal(form,portal);
						   }
					 }else{
						 formSubmitToAjaxPortal(form,portal);
					 }
				}
				return false;
			});
		},
		openByNav:function(url,openOptions){
			var trigger=$("<a tabindex='-1' style='display:none;' class='openByNavTrigger' href='"+url+"'></a>");
			if((typeof(openOptions.close)=="string"&&openOptions.close=="false")||(typeof(openOptions.close)=="boolean"&&openOptions.close==false)){
				trigger.data("noclose",true);
				trigger.attr("data-noclose","true");
			}
			if((typeof(openOptions.nomask)=="string"&&openOptions.nomask=="true")||(typeof(openOptions.nomask)=="boolean"&&openOptions.nomask==true)){
				trigger.data("nomask",true);
				trigger.attr("data-nomask","true");
			}
			if((typeof(openOptions.resize)=="string"&&openOptions.resize=="true")||(typeof(openOptions.resize)=="boolean"&&openOptions.resize==true)){
				trigger.data("resize",true);
				trigger.attr("data-resize","true");
			}

			if(openOptions.width){
				trigger.data("width",openOptions.width);
			}
			if(openOptions.dir){
				trigger.data("dir",openOptions.dir);
			}
			if(openOptions.confirm){
				trigger.data("confirm",openOptions.confirm);
				trigger.attr("data-confirm",openOptions.confirm);
			}
			if(openOptions.height){
				trigger.data("height",openOptions.height);
			}else if(openOptions.top){
				trigger.data("top",openOptions.top);
			}
			if(openOptions.loadType){
				trigger.data("load-type",openOptions.loadType);
				trigger.attr("data-load-type",openOptions.loadType);
			}
			jboltBody.append(trigger);
			this.open(trigger);
		},
		openBy:function(trigger){
			this.open(trigger);
		},
		processLayerByWindowResize:function(){
			var existLayer=jboltBody.find("#jbolt_layer");
			if(!isOk(existLayer)){
				return false;
			}
			var triggerid=existLayer.data("triggerid");
			if(!isOk(triggerid)){
				return false;
			}
			var trigger=jboltBody.find("#"+triggerid);
			var portal=existLayer.find(".jbolt_layer_portal");
			var top=trigger.data("top");
			if(top&&typeof(top)=="string"&&top.indexOf("%")!=-1){
				var p=Number(top.replace("%",""));
				top=parseInt(jboltWindowHeight*p/100);
			}else if(top&&typeof(top)=="string"&&top.indexOf("%")==-1){
				top=Number(top);
			}
			if(!top&&top!=0){
				top=0;
			}
			var bottom=0;
			var height=trigger.data("height");
			if(height){
				if(typeof(height)=="string"&&height.indexOf("%")!=-1){
					var p=Number(height.replace("%",""));
					height=parseInt(jboltWindowHeight*p/100);
				}else if(typeof(height)=="string"&&height.indexOf("%")==-1){
					height=Number(height);
				}
				if(top==0){
					top=jboltWindowHeight-height;
					if(top<0){
						top=0;
					}
				}
				bottom=jboltWindowHeight-top-height;
				if(bottom<0){
					bottom=0;
				}
			}
			portal.css({
				top:top+"px",
				bottom:bottom+"px"
			});
			
		},
		open:function(trigger){
			disposeTooltip(trigger);
			var url=trigger.attr("href");
			if(!url){
				url=trigger.data("url");
			}
			if(!url){
				LayerMsgBox.alert("jboltlayertrigger未设置URL",2);
				return false;
			}

			var checkHandler=trigger.data("check-handler");
			if(checkHandler){
				var checkResult;
				var exeCheck_handler=eval(checkHandler);
				if(exeCheck_handler&&typeof(exeCheck_handler)=="function"){
					checkResult=exeCheck_handler(trigger);
					if(!checkResult){
						return false;
					}
					if(typeof(checkResult)!="boolean"){
						if(isArray(checkResult)){
							url=url+checkResult.join(",");
						}else{
							url=url+checkResult;
						}
					}
				}
			}
			
			//处理URL
			url=actionUrl(url);
			url=processEleUrlByLinkOtherParamEle(trigger,url);
			if(!url){
				return false;
			}
			url=processJBoltTableEleUrlByLinkColumn(trigger,url);
			if(!url){
				return false;
			}
			var keepOpen=false;
			var existLayer=jboltBody.find("#jbolt_layer");
			if(existLayer&&existLayer.length==1){
				if(trigger[0].hasAttribute("data-keep-open")&&trigger.data("keep-open")){
					keepOpen=true;
				}
				if(keepOpen==false){
					existLayer.remove();
				}
			}
			var triggerid=trigger.attr("id");
			if(!triggerid){
				triggerid=randomId();
				trigger.attr("id",triggerid);
			}
			var top=trigger.data("top");
			if(top&&typeof(top)=="string"&&top.indexOf("%")!=-1){
				var p=Number(top.replace("%",""));
				top=parseInt(jboltWindowHeight*p/100);
			}else if(top&&typeof(top)=="string"&&top.indexOf("%")==-1){
				top=Number(top);
			}
			if(!top&&top!=0){
				top=0;
			}
			var bottom=0;
			var height=trigger.data("height");
			if(height){
				if(typeof(height)=="string"&&height.indexOf("%")!=-1){
					var p=Number(height.replace("%",""));
					height=parseInt(jboltWindowHeight*p/100);
				}else if(typeof(height)=="string"&&height.indexOf("%")==-1){
					height=Number(height);
				}
				if(top==0){
					top=jboltWindowHeight-height;
					if(top<0){
						top=0;
					}
				}
				bottom=jboltWindowHeight-top-height;
				if(bottom<0){
					bottom=0;
				}
			}
			 
			var hasClose=true;
			if(trigger[0].hasAttribute("data-noclose")){
				hasClose=false;
			}
			var noMask=trigger[0].hasAttribute("data-nomask");
			
			
			var width=trigger.data("width");
			if(width&&typeof(width)=="string"&&width.indexOf("%")!=-1){
				var p=Number(width.replace("%",""));
				width=parseInt(jboltWindowWidth*p/100);
			}else if(width&&typeof(width)=="string"&&width.indexOf("%")==-1){
				width=Number(width);
			}
			if(!width){
				width=jboltWindowWidth/2;
				if(width<900){
					width=900;
				}else if(width>1360){
					width=1360;
				}
			}
			trigger.data("portal-width",width);
			var dir=trigger.data("dir");
			if(!dir){
				dir="right";
				trigger.data("dir",dir);
			}
			var canResize=trigger.data("resize");
			var refreshMenu=trigger.data("refresh");
			var loadType=trigger.data("load-type")||"ajaxportal";
			var handler=trigger.data("handler");
			var callback=null;
			if(handler){
				var exe_hanlder=eval(handler);
				if(exe_hanlder&&typeof(exe_hanlder)=="function"){
					callback = exe_hanlder;
				}
			}
			if(keepOpen==false){
				var layer;
				if(loadType=="ajaxportal"){
					layer=$('<div draggable="false" data-triggerid="'+triggerid+'" class="jbolt_layer '+(noMask?" nomask ":"") +dir+(hasClose?" hasclose ":" noclose ")+'" id="jbolt_layer">'+(canResize?"<div style='top:"+top+"px;bottom:"+bottom+"px' class='jbolt_layer_resizebox'></div><div style='top:"+(hasClose?(top+40):top)+"px;bottom:"+bottom+"px' class='jbolt_layer_resizebar'></div>":"")+'<div draggable="false" data-dir="'+dir+'" '+(refreshMenu?"data-refresh":"")+' class="jbolt_layer_portal '+(canResize?"resize":"")+'"  style="top:'+top+'px;width:'+width+'px;bottom:'+bottom+'px"   data-ajaxportal  data-url="'+url+'"></div></div>');
				}else{
					url=processUrlRqType(url,"iframe");
					layer=$('<div draggable="false" data-triggerid="'+triggerid+'" class="jbolt_layer '+(noMask?" nomask ":"") +dir+(hasClose?" hasclose ":" noclose ")+'" id="jbolt_layer">'+(canResize?"<div style='top:"+top+"px;bottom:"+bottom+"px' class='jbolt_layer_resizebox'></div><div style='top:"+(hasClose?(top+40):top)+"px;bottom:"+bottom+"px' class='jbolt_layer_resizebar'></div>":"")+'<div draggable="false" data-dir="'+dir+'" '+(refreshMenu?"data-refresh":"")+' class="jbolt_layer_portal '+(canResize?"resize":"")+'"  style="top:'+top+'px;width:'+width+'px;bottom:'+bottom+'px" data-url="'+url+'"><iframe frameborder="0" class="jbolt_layer_iframe"></iframe></div></div>');
				}
				jboltBody.append(layer);
				if(hasClose){
					layer.append('<a tabindex="-1" data-closejboltlayer  style="top:'+top+'px;"  class="jbolt_layer_close">&times;</a>');
				}
				if(loadType=="ajaxportal"){
					this.load(null,callback);
				}else if(loadType=="iframe"){
					this.iframeLoad(url,callback);
				}
				this.showOpenAnimate(trigger);
				if(canResize){
					this.processResizeEvent(layer,trigger);
				}
			}else{
				if(loadType=="ajaxportal"){
					this.load(url,callback);
				}else if(loadType=="iframe"){
					this.iframeLoad(url,callback);
				}
			}
		},
		iframeLoad:function(url,callback){
			var existLayer=$("#jbolt_layer");
			if(isOk(existLayer)){
				var layerPortalIframe=existLayer.find(".jbolt_layer_portal>iframe");
				if(url){
					layerPortalIframe.attr("src",url);
				}else{
					layerPortalIframe.attr("src",layerPortalIframe.attr("src"));
				}
				if(callback){
					callback();
				}
				
			}
		},
		processResizeEvent:function(layer,trigger){
			var that=this,
			resizePortal=layer.find('.jbolt_layer_portal'),
			resizeBar=layer.find('.jbolt_layer_resizebar'),
			resizeBox=layer.find('.jbolt_layer_resizebox'),
			dir=trigger.data("dir"),
			closeBtn=layer.find('.jbolt_layer_close');
			
			if(isOk(resizePortal)&&isOk(resizeBar)){
				resizeBar.on("mousedown",function(e){
					//如果有弹出的layer就关掉
					FormDate.hide(layer);
					JBoltInputUtil.hideJBoltInputLayer();
					e.preventDefault();
					e.stopPropagation();
					layer.addClass("resizing");
					layer.data("resizing",true);
					layer.data("cancel-click",true);
					layer.css("cursor","e-resize");
					
					var portalOffset=resizePortal.offset(),
					pos=getMousePos(e),
					boxLeft,boxRight,width=portalOffset.left-pos.x,absWidth=Math.abs(width);
					if(dir=="right"){
						boxLeft=pos.x;
						boxRight=0;
						resizeBox.css({
							left:boxLeft,
							right:boxRight
						});
					}else{
						boxLeft=0;
						boxRight=pos.x;
						resizeBox.css({
							left:boxLeft,
							width:boxRight
						});
					}
					
					return false;
				}).on("click",function(e){
					e.preventDefault();
					e.stopPropagation();
					return false;
				});
				layer.on("mouseup",function(e){
					var resizing=layer.data("resizing");
					if(resizing){
						e.preventDefault();
						e.stopPropagation();
						var pos=getMousePos(e);
						that.resize(resizePortal,closeBtn,resizeBar,pos.x);
						layer.data("resizing",false);
						layer.removeAttr("data-resizing");
						layer.css("cursor","auto");
						setTimeout(function(){
							layer.data("cancel-click",false);
							layer.removeClass("resizing");
						}, 60);
						return false;
					}
					
				}).on("mousemove",function(e){
					var resizing=layer.data("resizing");
					if(resizing){
						e.preventDefault();
						e.stopPropagation();
						var portalOffset=resizePortal.offset(),
						pos=getMousePos(e),
						boxLeft,boxRight,width=portalOffset.left-pos.x,absWidth=Math.abs(width);
						if(dir=="right"){
							boxLeft=pos.x;
							boxRight=0;
							resizeBox.css({
								left:boxLeft,
								right:boxRight
							});
						}else{
							boxLeft=0;
							boxRight=pos.x;
							resizeBox.css({
								left:boxLeft,
								width:boxRight
							});
						}
						return false;
					}
				});
			}
			
		},load:function(url,callback){
			var existLayer=$("#jbolt_layer");
			if(existLayer&&existLayer.length==1){
				var layerPortal=existLayer.find(".jbolt_layer_portal");
				if(url){
					layerPortal.ajaxPortal(true,url,true,callback);
				}else{
					layerPortal.ajaxPortal(true,null,true,callback);
				}
			}
		},close:function(dontShowConfirm,from){
			var that=this;
			var existLayer=$("#jbolt_layer");
			if(existLayer&&existLayer.length==1){
				var triggerid=existLayer.data("triggerid");
				var jboltlayertrigger=$("#"+triggerid);
				if(from&&(from=="jbolt_layer"||from=="body")&&jboltlayertrigger.data("only-active-close")){
					//关掉需要关闭的特殊控件
					FormDate.hide(existLayer);
					JBoltInputUtil.hideJBoltInputLayer();
					JBoltTabUtil.hideJboltContextMenu();
					return;
				}
				var confirmAttr=jboltlayertrigger.attr("data-confirm");
				var hasConfirm=(typeof(confirmAttr)!="undefined");
				if(hasConfirm && !dontShowConfirm){
					var confirm=jboltlayertrigger.data("confirm");
					if(!confirm){
						confirm="确认关闭?";
					}
					LayerMsgBox.confirm(confirm,function(){
						that.showCloseAnimateAndRemove(jboltlayertrigger,existLayer);
					})
				}else{
					that.showCloseAnimateAndRemove(jboltlayertrigger,existLayer);
				}
				if(jboltlayertrigger.hasClass("openByNavTrigger")){
					jboltlayertrigger.remove();
				}
				
			}
			//关掉需要关闭的特殊控件
			FormDate.hide(existLayer);
			JBoltInputUtil.hideJBoltInputLayer();
			JBoltTabUtil.hideJboltContextMenu();
		},showCloseAnimateAndRemove:function(trigger,existLayer){
		var layerPortal=existLayer.find(".jbolt_layer_portal");
		var exf=function(callback){
			var dir=trigger.data("dir");
			if(!dir){
				dir="right";
			}

			var layerClose=existLayer.find(".jbolt_layer_close");
			var resizeBar=existLayer.find(".jbolt_layer_resizebar");
			var portalWidth=trigger.data("portal-width");
			if(dir=="right"){
				layerPortal.css({
					right:-portalWidth+"px"
				});
				layerClose.hide().css({
					right:-portalWidth+"px"
				})
				resizeBar.hide().css({
					right:-portalWidth+"px",
					zIndex:0
				})
			}else{
				layerPortal.css({
					left:-jboltWindowWidth+"px"
				});
				layerClose.hide().css({
					left:-jboltWindowWidth+"px"
				})
				resizeBar.hide().css({
					left:-jboltWindowWidth+"px",
					zIndex:0
				});
			}
			if(callback){
				callback();
			}
		}

		var closeFunc=function(){
			var beforeCloseHandler=trigger.data("before-close-handler");
			if(beforeCloseHandler){
				var exeBeforeCloseHandler = eval(beforeCloseHandler);
				if(exeBeforeCloseHandler && typeof(exeBeforeCloseHandler)=="function"){
					exeBeforeCloseHandler(trigger,layerPortal);
				}
			}
			exf(function(){
				existLayer.fadeOut(300,function(){
					var closeHandler=trigger.data("close-handler");
					if(closeHandler){
						var exeHandler = eval(closeHandler);
						if(exeHandler && typeof(exeHandler)=="function"){
							exeHandler(trigger,layerPortal);
						}
					}
					existLayer.remove();
				});
			});
		}

		var closeCheckHandler=trigger.data("close-check-handler");
		if(closeCheckHandler){
			var exeCheckHandler = eval(closeCheckHandler);
			if(exeCheckHandler && typeof(exeCheckHandler)=="function"){
				var result = exeCheckHandler(trigger,layerPortal);
				if(result){
					closeFunc();
				}
			}else{
				console.error("data-close-check-handler无效");
			}
		}else{
			closeFunc();
		}

	},resize:function(resizePortal,layerClose,resizeBar,mouseLeft){
			layerClose.hide();
			resizeBar.hide().css({zIndex:0});
			var dir=resizePortal.data("dir");
			if(!dir){
				dir="right";
			}
			var width=jboltWindowWidth-mouseLeft;
			if(dir=="right"){
				resizePortal.css("width",width);
				if(isOk(resizeBar)){
					setTimeout(function(){
						resizeBar.css({
							right:width-5,
							zIndex:101
						}).fadeIn(200);
					}, 300);
				}
				if(isOk(layerClose)){
					setTimeout(function(){
							layerClose.css({
								right:width
							}).fadeIn(200);
					}, 300);
				}
				
			}else{
				width=mouseLeft;
				resizePortal.css("width",mouseLeft);
				if(isOk(resizeBar)){
					setTimeout(function(){
						resizeBar.css({
							left:width-5,
							zIndex:101
						}).fadeIn(200);
					}, 300);
				}
				if(isOk(layerClose)){
					setTimeout(function(){
						layerClose.css({
							left:width
						}).fadeIn(200);
					}, 300);
				}
			}
			
			
		},showOpenAnimate:function(trigger){
			var existLayer=$("#jbolt_layer");
			if(existLayer&&existLayer.length==1){
				existLayer.fadeIn(200);
				var layerPortal=existLayer.find(".jbolt_layer_portal");
				var layerClose=existLayer.find(".jbolt_layer_close");
				var resizeBar=existLayer.find(".jbolt_layer_resizebar");
				var dir=trigger.data("dir");
				if(!dir){
					dir="right";
				}
				
				//处理遮罩
				if(existLayer.hasClass("nomask")){
					existLayer.addClass(dir);
				}
				
				var width=trigger.data("portal-width");
				if(dir=="right"){
					layerPortal.css({
						right:"0px"
					});
					if(isOk(resizeBar)){
						setTimeout(function(){
							resizeBar.css({
								right:width-5,
								zIndex:101
							}).fadeIn(200);
						}, 300);
					}
					if(isOk(layerClose)){
						setTimeout(function(){
								layerClose.css({
									right:width
								}).fadeIn(200);
						}, 300);
					}
					
				}else{
					layerPortal.css({
						left:"0px",
					});
					if(isOk(resizeBar)){
						setTimeout(function(){
							resizeBar.css({
								left:width-5,
								zIndex:101
							}).fadeIn(200);
						}, 300);
					}
					if(isOk(layerClose)){
						setTimeout(function(){
							layerClose.css({
								left:width
							}).fadeIn(200);
						}, 300);
					}
				}
				
			}
		}
}

function getMousePos(event) {
    var e = event || window.event;
    var scrollX = document.documentElement.scrollLeft || document.body.scrollLeft;
    var scrollY = document.documentElement.scrollTop || document.body.scrollTop;
    var x = e.pageX || e.clientX + scrollX;
    var y = e.pageY || e.clientY + scrollY;
    //alert('x: ' + x + '\ny: ' + y);
    return { 'x': x, 'y': y };
}




/**
 * Jbolt封装 tabs选项卡组件
 */
/**
 * tab组件的右键菜单配置
 */
/*var JBoltTabContextifyOptions = {
		dividerClass:"divider",
		items:[
		  {header: '<i class="fa fa-th mr-1"></i>可选操作'},
		  {text: '<i class="fa fa-close mr-1"></i>关闭这个', onclick: function(e) {
			  JBoltTabUtil.closeJboltTab(e.data.key);
		  }},
		  {text: '<i class="fa fa-refresh mr-1"></i>刷新', onclick: function(e) {
			  JBoltTabUtil.showJboltTab(e.data.key,false,refreshPjaxContainer);
		  }},
		  {divider: true},
		  {text: '<i class="fa fa-window-close-o mr-1"></i>关闭其它', onclick: function(e) {
			  JBoltTabUtil.closeOtherJboltTab(e.data.key);
		  }},
		  {text: '<i class="fa fa-toggle-left mr-1"></i>关闭左侧', onclick: function(e) {
			  JBoltTabUtil.closeAllLeftJboltTab(e.data.key);
		  }},
		  {text: '<i class="fa fa-toggle-right mr-1"></i>关闭右侧', onclick: function(e) {
			  JBoltTabUtil.closeAllRightJboltTab(e.data.key);
		  }},
		  {text: '<i class="fa fa-window-close mr-1"></i>关闭所有', onclick: function(e) {
			  JBoltTabUtil.closeAllJboltTab();
		  }},
		]};*/

/**
 * 左侧导航点击使用IFrame加载
 * @param url
 * @returns
 */
function pjaxAdminLeftNavWithIframe(url,openOptions){
	LayerMsgBox.load(3);
	if(openOptions && openOptions.hideLeftNav){
		hideLeftAndFullMainConainer();
	}else{
		jboltAdmin.removeClass("fullMainContainer");
	}
	url=processUrlRqType(url,"iframe");
	mainPjaxContainer.html('<iframe frameborder="0" src="'+url+'" class="jbolt_main_iframe"></iframe>');
	var iframe=mainPjaxContainer.find("iframe");
	iframe.on("load",function(){
		LayerMsgBox.closeLoadNow();
	}).on("error",function(){
		LayerMsgBox.closeLoadNow();
	});
}
/**
 * 左侧导航点击弹出Dialog
 * @param title
 * @param url
 * @param openOptions
 * @returns
 */
function adminLeftNavshowInDialog(title,url,openOptions){
	//在dialog中加载
	var w="80%",h="80%", btn="no";
	if(openOptions.width){
		w=openOptions.width;
	}
	if(openOptions.height){
		h=openOptions.height;
	}
	if(openOptions.btn&&openOptions.btn=="true"){
		 btn="yes";
	}
	if(openOptions.title){
		var t=$.trim(openOptions.title);
		if(t&&t.length>0){
			title=t;
		}
	}
	url=actionUrl(url);
	   DialogUtil.openNewDialog({
	    	  title:title,
	    	  width:w,
	    	  height:h,
	    	  url:url,
	    	  scroll:"yes",
	    	  offset:"auto",
	    	  btn:btn
	      });
	
}
/**
 * 当前页面跳转到指定URL
 * @param url
 * @param newTabTitle
 * @returns
 */
function currentPageGo(url,newTabTitle){
	if(jboltWithTabs){
		if(newTabTitle){
			changeCurrentTabTitle(newTabTitle);
		}
		JBoltTabUtil.currentTabGo(url);
	}else{
		JBoltPjaxUtil.sendPjax(url,mainPjaxContainerId);
	}
}
/**
 * 页面返回
 * @param url
 * @returns
 */
function pageBack(url){
	if(jboltWithTabs){
		closeCurrentAndReloadTiggerTab();
	 }else{
		 JBoltPjaxUtil.sendPjax(url,mainPjaxContainerId);
	 }
}

/**
 * 选项卡组件
 */
var JBoltTabUtil={
		changeCurrentTabTitle:function(title){
		  var currentTab=this.getCurrentTab();
		  if(currentTab){
			  this.changeTabTitle(currentTab,title);
		  }
		},
		changeTabTitle:function(tab,title){
			if(tab){
				var span=tab.find("span.jb_tabtitle");
				if(isOk(span)){
					span.html(title);
				}
			}
		},
		init:function(){
			var that=this;
			that.createJboltTabContextMenu();
			that.initAdminLeftNavTabsEvent();
			that.initTabsEvent();
			that.initTabsTriggerEvent();
		},
		initTabsTriggerEvent:function(){
			var that=this;
			jboltBody.on("click","[data-tabtrigger]",function(e){
				  e.preventDefault();
				  e.stopPropagation();
				  var nav=$(this);
				  var url=nav.attr("href");
				  if(!url){
					  url=nav.data("url");
				  }
				  if(!url){
					  LayerMsgBox.alert("请指定选项卡内容加载地址",2);
					  return;
				  }
				  var target=nav.attr("target");
				  if(!target){
					  target=nav.data("target");
				  }
				  if(url&&url.indexOf("javascript")==-1){
					  if(target&&target=="_self"){
						  currentTabContentRedirectWithAjaxPortal(url);
					  }else{
						  var key=nav.data("key");
						  var text=$.trim(nav.data("text")||nav.data("title")||nav.attr("title")||nav.text()||"新标签页");
						  var openType=nav.data("open-type");
						  if(!openType){
							  openType=1;
						  }
						  var openOptions=nav.data("open-option");
						  var currentTab=that.getCurrentTab();
						  if(!openOptions || (openOptions && !openOptions.hideLeftNav)){
							  jboltAdmin.removeClass("fullMainContainer");
						  }
						  if(currentTab){
							  var triggerKey=currentTab.data("key");
							  switch (openType) {
							  case 1://系统默认
								  that.addJboltTab(key,url,text,triggerKey,true,openType,openOptions);
								  break;
							  case 2://iframe
								  that.addJboltTabWithIFrame(key,url,text,triggerKey,true,openType,openOptions);
								  break;
							  }
						  }else{
							  switch (openType) {
							  case 1://系统默认
								  that.addJboltTab(key,url,text,null,true,openType,openOptions);
								  break;
							  case 2://iframe
								  that.addJboltTabWithIFrame(key,url,text,null,true,openType,openOptions);
								  break;
							  }
						  }
						  
					  }
					  
				  }
			});
		},initAdminLeftNavTabsEvent:function(){
			var that=this;
			jboltBody.on("click",".jbolt_admin_left_navs a:not([target]):not([href='javascript:void(0)'])",function(e){
				  e.preventDefault();
				  e.stopPropagation();
				  that.openByType($(this));
			});
		},openByType:function(nav){
			if(window.self!=window.top){
				parent.JBoltTabUtil.openByType(nav)
			}else{
				var url=nav.attr("href")||nav.data("url");
				if(!url||url.indexOf("javascript")!=-1){
					return false;
				}
				var key=nav.data("key")||randomId();
				var text=$.trim(nav.data("text")||nav.data("title")||nav.attr("title")||nav.text()||"新标签页");
				var openType=nav.data("open-type");
				  if(!openType){
					  openType=1;
				  }
				  var openOptions=nav.data("open-option");
				url=actionUrl(url);
				url=processEleUrlByLinkOtherParamEle(nav,url);
				url=processJBoltTableEleUrlByLinkColumn(nav,url);
				var triggerKey = null;
				var currentTab=this.getCurrentTab();
				if(isOk(currentTab)){
					triggerKey = currentTab.data("key");
				}
				this.openTabWithOptions(key,url,text,triggerKey,true,openType,openOptions);
			}
			
			 
		},
		/**
		 * 打开一个tab
		 * @param key 自定义tab唯一KEY
		 * @param url 加载地址
		 * @param text tab标题文本
		 * @param triggerKey 上个tab key 默认不用传
		 * @param active 打开后默认是否激活显示
		 * @param openType  1:系统默认 2:iframe 3:dialog 4:jboltlayer 不传或者传空 传0按照默认1
		 * @param openOptions 如果是dialog和jbotlayer类型 json配置需要写出来 不写按照默认
		 */
		openTab:function(key,url,text,triggerKey,active,openType,openOptions){
			this.openTabWithOptions(key,url,text,triggerKey,active,openType,openOptions);
		},
		/**
		 * 打开一个tab
		 * @param key 自定义tab唯一KEY
		 * @param url 加载地址
		 * @param text tab标题文本
		 * @param triggerKey 上个tab key 默认不用传
		 * @param active 打开后默认是否激活显示
		 * @param openType  1:系统默认 2:iframe 3:dialog 4:jboltlayer 不传或者传空 传0按照默认1
		 * @param openOptions 如果是dialog和jbotlayer类型 json配置需要写出来 不写按照默认
		 */
		openTabWithOptions:function(key,url,text,triggerKey,active,openType,openOptions){
			 if(typeof(openType)=="number"){
				  switch (openType) {
					  case 1://系统默认
						  this.addJboltTab(key,url,text,triggerKey,active,openType,openOptions);
						  break;
					  case 2://iframe
						  this.addJboltTabWithIFrame(key,url,text,triggerKey,active,openType,openOptions);
						  break;
					  case 3://Dialog
						  activeLeftNavByKey(key);
						  adminLeftNavshowInDialog(text,url,openOptions);
						  break;
					  case 4://jboltLayer
						  activeLeftNavByKey(key);
						  JBoltLayerUtil.openByNav(url,openOptions);
						  break;
					default://默认
						this.addJboltTab(key,url,text,triggerKey,active,openType,openOptions);
						break;
				  }
			  }else if(typeof(openType)=="string"){
				  switch (openType) {
				  case "iframe"://iframe
					  this.addJboltTabWithIFrame(key,url,text,triggerKey,active,openType,openOptions);
					  break;
				  case "dialog"://Dialog
					  activeLeftNavByKey(key);
					  adminLeftNavshowInDialog(text,url,openOptions);
					  break;
				  case "jboltlayer"://jboltLayer
					  activeLeftNavByKey(key);
					  JBoltLayerUtil.openByNav(url,openOptions);
					  break;
				  default://默认
					  this.addJboltTab(key,url,text,triggerKey,active,openType,openOptions);
					  break;
			  }
		  }			
		},refreshJboltTab:function(key){
			this.showJboltTab(key,false,refreshPjaxContainer);
		},initTabsEvent:function(){
			var that=this;
			//点击标签页 切换显示
			jboltBody.on("click","ul.jbolt_tabs>li",function(e){
				var key=$(this).data("key");
				that.showJboltTab(key,true);
			}).on("click","ul.jbolt_tabs>li>i.close",function(e){
				e.preventDefault();
				e.stopPropagation();
				var key=$(this).parent().data("key");
				that.closeJboltTab(key);
			}).on("click","#jbolt_tabbar .jbolt_tab_left",function(e){
				e.preventDefault();
				that.jboltTabToLeft();
			}).on("click","#jbolt_tabbar .jbolt_tab_right",function(e){
				e.preventDefault();
				e.stopPropagation();
				that.jboltTabToRight();
			}).on("click","#jboltTabContextMenu>a",function(e){
				e.preventDefault();
				e.stopPropagation();
				var func=$(this).data("func");
				var key=$(this).data("key");
				switch (func) {
					case "close":
						JBoltTabUtil.closeJboltTab(key);
						break;
					case "closeAll":
						JBoltTabUtil.closeAllJboltTab();
						break;
					case "closeLeft":
						JBoltTabUtil.closeAllLeftJboltTab(key);
						break;
					case "closeRight":
						JBoltTabUtil.closeAllRightJboltTab(key);
						break;
					case "closeOther":
						JBoltTabUtil.closeOtherJboltTab(key);
						break;
					case "refresh":
						JBoltTabUtil.refreshJboltTab(key);
						break;
					case "back":
						JBoltTabUtil.back(key);
						break;
				}
				that.hideJboltContextMenu();
				return false;
			}).on("dblclick","ul.jbolt_tabs>li",function(e){
				e.preventDefault();
				e.stopPropagation();
				var key=$(this).data("key");
				LayerMsgBox.confirm("确认刷新此选项卡内容？",function(){
					that.showJboltTab(key,false,refreshPjaxContainer);
				});
				/*that.showJboltTab(key,true);
				var tabContent=mainPjaxContainer.find("div#tab_content_"+key);
				if(tabContent&&tabContent.length==1){
					LayerMsgBox.confirm("确认刷新此选项卡内容？",function(){
						tabContent.ajaxPortal(true);
					});
				}*/
			}).on("contextmenu","ul.jbolt_tabs>li",function(e){
				e.preventDefault();
				e.stopPropagation();
				var pos=getMousePos(e);
				that.showTabContextmenu($(this),pos);
				return false;
			}).on("click",'#jboltTabContextMenu',function(e){
				e.preventDefault();
				e.stopPropagation();
				return false;
			}).on("click",function(){
				that.hideJboltContextMenu();
			});
		},
		createJboltTabContextMenu:function(){
			var jboltTabContextMenu=$("#jboltTabContextMenu");
			if(!jboltTabContextMenu||jboltTabContextMenu.length==0){
				var html='<div id="jboltTabContextMenu" class="dropdown-menu">'+
					'<span class="dropdown-item-text"><i class="fa fa-th mr-1"></i>可选操作</span>'+
					'<div class="dropdown-divider"></div>'+
					'<a tabindex="-1" class="dropdown-item d-none" data-func="back" href="javascript:void(0)"><i class="fa fa-reply mr-1"></i>返回</a>'+
					'<a tabindex="-1" class="dropdown-item" data-func="close" href="javascript:void(0)"><i class="fa fa-close mr-1"></i>关闭这个</a>'+
					'<a tabindex="-1" class="dropdown-item" data-func="refresh" href="javascript:void(0)"><i class="fa fa-refresh mr-1"></i>刷新</a>'+
					'<a tabindex="-1" class="dropdown-item" data-func="closeOther" href="javascript:void(0)"><i class="fa fa-window-close-o mr-1"></i>关闭其它</a>'+
					'<a tabindex="-1" class="dropdown-item" data-func="closeLeft" href="javascript:void(0)"><i class="fa fa-toggle-left mr-1"></i>关闭左侧</a>'+
					'<a tabindex="-1" class="dropdown-item" data-func="closeRight" href="javascript:void(0)"><i class="fa fa-toggle-right mr-1"></i>关闭右侧</a>'+
					'<a tabindex="-1" class="dropdown-item" data-func="closeAll" href="javascript:void(0)"><i class="fa fa-window-close mr-1"></i>关闭所有</a>'+
					'</div>';
				jboltBody.append(html);
			}
		},
		hideJboltContextMenu:function(){
			var jboltTabContextMenu=$("#jboltTabContextMenu");
			if(jboltTabContextMenu&&jboltTabContextMenu.length==1){
				jboltTabContextMenu.hide();
			}
		},
		showTabContextmenu:function(tabEle,pos){
			var tab=$(tabEle);
			var key=tab.data("key");
			var jboltTabContextMenu=$("#jboltTabContextMenu");
			if(!jboltTabContextMenu||jboltTabContextMenu.length==0){
				this.createJboltTabContextMenu();
			}
			jboltTabContextMenu.data("key",key);
			jboltTabContextMenu.find("a").data("key",key);
			jboltTabContextMenu.css({
				top:pos.y,
				left:pos.x
			});
			if(tab.data("backurl")){
				jboltTabContextMenu.find("a[data-func='back']").removeClass("d-none");
			}else{
				jboltTabContextMenu.find("a[data-func='back']").addClass("d-none");
			}
			jboltTabContextMenu.show();
		},
		/**
		 * 得到当前显示的tab
		 */
		getCurrentTab:function(){
			var tab=jbolt_tabs_container.find("li.active");
			if(tab&&tab.length==1){
				return tab;
			}
			return null;
		},
		/**
		 * 得到当前显示的tabcontent部分
		 */
		getCurrentTabContent:function(){
			var tabContent=mainPjaxContainer.find("div.jbolt_tabcontent.active");
			if(tabContent&&tabContent.length==1){
				return tabContent;
			}
			return null;
		},
		/**
		 * 判断是否已经存在
		 * @param key
		 * @returns
		 */
		isJboltTabExist:function(key){
			var existLi=jbolt_tabs_container.find("li[data-key='"+key+"']");
			return existLi&&existLi.length==1
		},
		getTab:function(key){
			return jbolt_tabs_container.find("li[data-key='"+key+"']");
		},
		getTabContent:function(key){
			return mainPjaxContainer.find("div.jbolt_tabcontent[data-key='"+key+"']");
		},
		/**
		 * 添加一个不带创建content的tab 用于特殊情况初始化页面tab
		 */
		addJboltTabWithoutContentUrl:function(){
			var that=this;
			var jboltPage=mainPjaxContainer.find(".jbolt_page[data-key]");
			if(jboltPage&&jboltPage.length==1){
				var ok = useSessionStorageToActiveTab();
				if(ok){
					return;
				}
				var url=jboltPage.data("key");
				var nav=jboltAdminLeftNavs.find("a[href='"+url+"']");
				if(nav&&nav.length==1){
					var key=nav.data("key");
					var text=$.trim(nav.text().replace("├",""));
					var openType=nav.data("open-type");
					if(!openType){
						openType=1;
					}
					var openOptions=nav.data("open-option");
					that.addJboltTabWithoutContent(key,url,text,openType,openOptions);
				}else{
					//如果没有按照key找到nav 就找本身设置的text 作为tab的text
					var tabTitle=jboltPage.data("text")||jboltPage.data("title");
					if(!tabTitle){
						var h1=jboltPage.find(".jbolt_page_title h1");
						if(isOk(h1)){
							tabTitle=h1.text();
						}
					}
					var openType=1;
					var openOptions={};
					if(!tabTitle){
						var dontchangeleftnav=jboltPage.data("dontchangeleftnav");
						if(dontchangeleftnav){
							//如果没有规定不能改变 就改一下
							var likeNavs=jboltAdminLeftNavs.find("a[href]");
							if(isOk(likeNavs)){
								var cunav,cuhref,cutext,maxlen,maxtext;
								likeNavs.each(function(){
									cunav=$(this);
									cuhref=cunav.attr("href");
									cutext=$.trim(cunav.text().replace("├",""));
									if(url.indexOf(cuhref)!=-1){
										if((!maxlen||!maxtext)||cuhref.length>maxlen){
											tabTitle=cutext;
											maxlen=cuhref.length;
											openType=cunav.data("open-type");
											if(!openType){
												openType=1;
											}
											openOptions=cunav.data("open-option");
											return false;
										}
									}
								});
							}
						}
					}
					
					if(!tabTitle){
						tabTitle="新标签页";
					}
					
					that.addJboltTabWithoutContent(key,url,tabTitle,openType,openOptions);
				}
			}
			
		},
		/**
		 * 底层方法 
		 */
		addJboltTabWithoutContent:function(key,url,text,openType,openOptions){
			this.openTabWithOptions(key,url,text,null,true,openType,openOptions);
//			var that=this;
//			var exist=that.isJboltTabExist(key);
//			if(!exist){
//				jbolt_tabs_container.append('<li id="tab_'+key+'" data-key="'+key+'" data-url="'+url+'"><span>'+text+'</span><i class="close">&times;</i></li>');
//				pushJBoltTabToArray(key,url,text,null,openType,openOptions);
//				var tabContent=mainPjaxContainer.find("div.jbolt_tabcontent");
//				if(tabContent&&tabContent.length==1){
//					tabContent.data("key",key);
//					tabContent.data("url",url);
//					tabContent.attr("id","tab_content_"+key);
//				}
//			}
//			that.showJboltTab(key);
		},
		/**
		 * 初始化
		 */
		initTabContextMenuEvent:function(){
//			$('#tab_'+key).tabContextMenu(JBoltTabContextifyOptions);
		},
		/**
		 * 添加一个选项卡 使用IFrame加载内容
		 */
		addJboltTabWithIFrame:function(key,url,text,triggerTabKey,active,openType,openOptions){
			var that=this;
			if(openOptions && openOptions.hideLeftNav){
				hideLeftAndFullMainConainer();
			}else{
				jboltAdmin.removeClass("fullMainContainer");
			}
			var exist=that.isJboltTabExist(key);
			if(exist){
				if(active){
					that.showJboltTab(key,true);
				}
			}else{
				LayerMsgBox.load(3);
				url=processUrlRqType(url,"iframe");
				triggerTabKey=triggerTabKey?triggerTabKey:"";
				jbolt_tabs_container.append('<li id="tab_'+key+'" data-key="'+key+'" data-trigger-tab-key="'+triggerTabKey+'"  data-url="'+url+'"><span>'+text+'</span><i class="close">&times;</i></li>');
				pushJBoltTabToArray(key,url,text,triggerTabKey,openType,openOptions);
				mainPjaxContainer.append('<div class="jbolt_tabcontent" id="tab_content_'+key+'"   data-key="'+key+'" data-trigger-tab-key="'+triggerTabKey+'" data-url="'+url+'"><iframe frameborder="0" src="'+url+'" class="jbolt_main_iframe"></iframe></div>')
				if(active){
					that.showJboltTab(key);
				}
				that.jboltTabToRight();
				
				var tabContentIframe='#tab_content_'+key+">iframe";
				var iframe=mainPjaxContainer.find(tabContentIframe);
				iframe.on("load",function(){
					LayerMsgBox.closeLoadNow();
				}).on("error",function(){
					LayerMsgBox.closeLoadNow();
				});
			}
			
		},
		/**
		 * 添加一个tab选项卡
		 * @param key
		 * @param url
		 * @param text
		 * @param triggerTabKey 是从另一个tab过来的就要带着这个
		 * @param active
		 * @param openType
		 * @param openOptions
		 */
		addJboltTab:function(key,url,text,triggerTabKey,active,openType,openOptions){
			if(typeof(active)=="undefined"){
				active = true;
			}
			var that=this;
			if(!isOk(text)){
				text="新标签页";
			}
			var exist=that.isJboltTabExist(key);
			if(exist){
				if(active){
					that.showJboltTab(key,true);
				}
			}else{
				url=actionUrl(url);
				triggerTabKey=triggerTabKey?triggerTabKey:"";
				jbolt_tabs_container.append('<li id="tab_'+key+'" data-key="'+key+'" data-trigger-tab-key="'+triggerTabKey+'" data-url="'+url+'"><span class="jb_tabtitle">'+text+'</span><i class="close">&times;</i></li>');
				pushJBoltTabToArray(key,url,text,triggerTabKey,openType,openOptions);
				mainPjaxContainer.append('<div class="jbolt_tabcontent" id="tab_content_'+key+'" data-trigger-tab-key="'+triggerTabKey+'" data-ajaxportal data-key="'+key+'" data-url="'+url+'"></div>')
				var contentPortal=mainPjaxContainer.find("div#tab_content_"+key);
				contentPortal.ajaxPortal(true,null,true,function(){
					that.changeTabTitleByPortalPageSet(contentPortal,key);
				});
				if(active){
					that.showJboltTab(key);
				}
				that.jboltTabToRight();
			}
			
			
		},changeTabTitleByPortalPageSet:function(contentPortal,key){
			//根据加载的portal里的设置 重新设置tab的title
			var portalPage=contentPortal.find(".jbolt_page[data-title]");
			if(isOk(portalPage)){
				var pageTitle=portalPage.data("title")||portalPage.data("text");
				if(pageTitle){
					this.changeCurrentTabTitle(pageTitle);
				}
			}
			
		},hideAllJboltTab:function(){
			jbolt_tabs_container.find("li.active").removeClass("active");
			mainPjaxContainer.find("div.jbolt_tabcontent.active").removeClass("active");
		},
		/**
		 * 在内页跳转后 将tab的url跟随content的替换掉
		 */
		changeUrlByContentPortal:function(){
			var currentTab=this.getCurrentTab();
			var currentTabContent=this.getCurrentTabContent();
			var url=currentTabContent.data("url");
			currentTab.data("url",url);
		},changeUrl:function(url){
			var currentTab=this.getCurrentTab();
			var currentTabContent=this.getCurrentTabContent();
			if(currentTab&&currentTab.length==1){
				url=actionUrl(url);
				currentTab.data("url",url);
				currentTabContent.data("url",url);
				currentTab.attr("data-url",url);
				currentTabContent.attr("data-url",url);
			}
		},
		currentTabGo:function(url,params){
			var currentTab=this.getCurrentTab();
			if(isOk(currentTab)){
				var currentTabContent=this.getCurrentTabContent();
				url=actionUrl(url);
				if(isOk(params)){
					url=urlWithJsonData(url,params);
				}
				var backUrl=currentTab.data("url");
				currentTab.data("url",url);
				currentTab.data("backurl",backUrl);
				currentTab.attr("data-backurl",backUrl);
				currentTabContent.attr("data-url",url);
				currentTabContent.data("url",url);
				currentTab.attr("data-url",url);
				currentTabContent.ajaxPortal(true,url,true);
			}
//			else{
//				currentTab.data("backurl",'');
//				currentTab.removeAttr("data-backurl");
//			}
		},
		/**
		 * 切换显示一个tab选项卡
		 * @param key
		 * @param processChange
		 * @param callback
		 * @returns
		 */
		showJboltTab:function(key,processChange,callback){
			JBoltInputUtil.hideJBoltInputLayer();
			JBoltTabUtil.hideJboltContextMenu();
			closeJBoltLayer();
			var that=this;
			var li=jbolt_tabs_container.find("li#tab_"+key);
			if(li&&li.length==1){
				var isCurrent=li.hasClass("active");
				//如果当前正好active 就不用管了
				if(!isCurrent){
					//隐藏当前tab
					that.hideAllJboltTab();
					//激活需要显示的
					li.addClass("active");
					var mainTabContent=mainPjaxContainer.find("div#tab_content_"+key);
					mainTabContent.addClass("active");
					activeLeftNavByKey(key);
					processAutoRefreshTabContent(mainTabContent);
				}
				//是否处理更改tab的加载地址
				if(processChange){
					that.changeTabLocation();
				}
				if(callback){
					callback();
				}
				
				activeJBoltTabFromArray(key);
			}
			
		},
		/**
		 * 将左侧导航的第一个弹出选项卡
		 */
		showFirstLeftNavTab:function(){
			var firstNav=jboltAdminLeftNavs.find("a[data-hasurl]").first();
			if(firstNav&&firstNav.length==1){
				var url=firstNav.attr("href");
				if(url&&url.indexOf("javascript")==-1){
				  var key=firstNav.data("key");
				  var text=firstNav.text().replace("├","");
				  var openType=firstNav.data("open-type");
				  if(!openType){
					  openType=1;
				  }
				  var openOptions=firstNav.data("open-option");
//				  that.addJboltTab(key,url,text,null,true,openType,openOptions);
				  this.openTabWithOptions(key,url,text,null,true,openType,openOptions);
				}
			}
			
		},getTabCount:function(){
			var lis=jbolt_tabs_container.find("li");
			if(lis&&lis.length>0){
				return lis.length;
			}
			return 0;
		},
		/**
		 * 关闭指定tab
		 * @param tab
		 */
		close:function(tab){
			this.closeJboltTab(tab.data("key"));
		},
		/**
		 * 关闭当前tab
		 */
		closeCurrentTab:function(){
			var tab = this.getCurrentTab();
			if(isOk(tab)){
				this.close(tab);
			}
		},
		/**
		 * 指定的Tab选项卡 返回操作
		 * @param key
		 * @returns
		 */
		back:function(key){
			var that=this;
			var li=jbolt_tabs_container.find("li#tab_"+key);
			if(li&&li.length==1){
				var backUrl=li.data("backurl");
				if(backUrl){
					li.data("url",backUrl);
					li.attr("data-url",backUrl);
					that.showJboltTab(key);
					var portal=mainPjaxContainer.find("div#tab_content_"+key);
					if(isOk(portal)){
						portal.data("url",backUrl);
						portal.attr("data-url",backUrl);
						li.data("backurl",'');
						li.removeAttr("data-backurl");
						portal.data("backurl","");
						portal.removeAttr("data-backurl");
						portal.ajaxPortal(true,backUrl,true); 
					}
				}
				
			}
		},
		/**
		 * 关闭一个指定的Tab选项卡
		 * @param key
		 * @returns
		 */
		closeJboltTab:function(key){
			var that=this;
			var li=jbolt_tabs_container.find("li#tab_"+key);
			if(li&&li.length==1){
				var active=li.hasClass("active");
				if(active){
					that.changeToBrotherTab(li);
				}
				li.remove();
				var thetabContent=mainPjaxContainer.find("div#tab_content_"+key);
				if(isOk(thetabContent)){
					AjaxPortalUtil.triggerPortalJBoltPageCloseHandler(thetabContent);
					thetabContent.remove();
				}
				removeJBoltTabFromArray(key);
				var count=that.getTabCount();
				if(count==0){
					that.showFirstLeftNavTab();
				}
				
				if(!active){
					that.changeTabLocation();
				}
			}
		},
		/**
		 * 关闭其它
		 */
		closeOtherJboltTab:function(key){
			var that=this;
			that.showJboltTab(key);
			var lis=jbolt_tabs_container.find("li:not("+"#tab_"+key+")");
			if(lis&&lis.length>0){
				var li,li_key,thetabContent;
				lis.each(function(){
					li=$(this);
					li_key=li.data("key");
					li.remove();
					thetabContent = mainPjaxContainer.find("div#tab_content_"+li_key);
					if(isOk(thetabContent)){
						AjaxPortalUtil.triggerPortalJBoltPageCloseHandler(thetabContent);
						thetabContent.remove();
					}
					removeJBoltTabFromArray(li_key);
				});
			}
		},
		/**
		 * 关闭所有
		 */
		closeAllJboltTab:function(){
			jbolt_tabs_container.empty();
			var jboltPages = mainPjaxContainer.find(".jbolt_page[data-close-handler]");
			if(isOk(jboltPages)){
				var jboltPageSize = jboltPages.length;
				var jboltPage;
				for(var i=0;i<jboltPageSize;i++){
					jboltPage = jboltPages.eq(i);
					AjaxPortalUtil.triggerPortalJBoltPageCloseHandler(mainPjaxContainer,jboltPage);
				}
			}
			mainPjaxContainer.empty();
			jbolt_tabs_array=[];
			this.showFirstLeftNavTab();
		},
		/**
		 * 关闭左侧所有
		 */
		closeAllLeftJboltTab:function(key){
			var that=this;
			var self=jbolt_tabs_container.find("li#tab_"+key);
			var lis=self.prevAll("li");
			var selfIsActive=self.hasClass("active");
			//判断删除的里面有没有active的
			var hasActive=false;
			if(lis&&lis.length>0){
				var li,li_key,thetabContent;
				lis.each(function(){
					li=$(this);
					li_key=li.data("key");
					if(li.hasClass("active")){
						hasActive=true;
					}
					li.remove();
					thetabContent = mainPjaxContainer.find("div#tab_content_"+li_key);
					if(isOk(thetabContent)){
						AjaxPortalUtil.triggerPortalJBoltPageCloseHandler(thetabContent);
						thetabContent.remove();
					}
					removeJBoltTabFromArray(li_key);
				});
			}
			
			if(!selfIsActive&&hasActive){
				that.showJboltTab(key);
			}
		
		},
		/**
		 * 关闭右侧所有
		 */
		closeAllRightJboltTab:function(key){
			var that=this;
			var self=jbolt_tabs_container.find("li#tab_"+key);
			var lis=self.nextAll("li");
			var selfIsActive=self.hasClass("active");
			//判断删除的里面有没有active的
			var hasActive=false;
			if(lis&&lis.length>0){
				var li,li_key,thetabContent;
				lis.each(function(){
					li=$(this);
					li_key=li.data("key");
					if(li.hasClass("active")){
						hasActive=true;
					}
					li.remove();
					thetabContent = mainPjaxContainer.find("div#tab_content_"+li_key);
					if(isOk(thetabContent)){
						AjaxPortalUtil.triggerPortalJBoltPageCloseHandler(thetabContent);
						thetabContent.remove();
					}
					removeJBoltTabFromArray(li_key);
				});
			}
			
			if(!selfIsActive&&hasActive){
				that.showJboltTab(key);
			}
		
		},
		/**
		 * 切换到兄弟节点
		 */
		changeToBrotherTab:function(li){
			var that=this;
			var brother=li.prev();
			var exist=brother&&brother.length==1;
			if(!exist){
				brother=li.next();
				exist=brother&&brother.length==1;
			}
			if(exist){
				var key=brother.data("key");
				that.showJboltTab(key);
			}else{
				that.showFirstLeftNavTab();
			}
		},checkCanToLeft:function(){
			var jbolt_tabbar_width=jbolt_tabbar.width();
			var boxWidth=jbolt_tabs_container.outerWidth();
			if(boxWidth>jbolt_tabbar_width){
				//标签总长度大于容器长度 说明满足先决条件了 再去判断 显示长度
				var marginleft=Math.abs(parseInt(jbolt_tabs_container.css("margin-left")));
				var lwidth=Math.abs(parseInt(jbolt_tabbar_width-boxWidth));
				if(marginleft<lwidth){
					//如果左移距离小于差距 就可以左移
					return true;
				}
			}
			return false;
			
		},checkCanToRight:function(){
			var marginleft=Math.abs(parseInt(jbolt_tabs_container.css("margin-left")));
			return marginleft!=0;
		},
		/**
		 * 显示最左侧
		 * @returns
		 */
		jboltTabToLeft:function(){
			jbolt_tabs_container.css("margin-left","0px");
		},
		/**
		 * 显示最右侧
		 * @returns
		 */
		jboltTabToRight:function(){
			var jbolt_tabbar_width=jbolt_tabbar.width();
			var boxWidth=jbolt_tabs_container.outerWidth();
			if(boxWidth>jbolt_tabbar_width){
				var mleft=(jbolt_tabbar_width/2)-boxWidth;
				var leftLi=null;
				var len=0;
				var absmLeft=Math.abs(mleft);
				jbolt_tabs_container.find("li").each(function(){
					mleft=len*-1;
					len=len+$(this).outerWidth();
					if(len>=absmLeft){
						return false;
					}
				});
				jbolt_tabs_container.css("margin-left",mleft+"px");
			}
		},changeTabLocation:function(){
			var that=this;
			var jbolt_tabbar_width=jbolt_tabbar.width();
			var boxWidth=jbolt_tabs_container.outerWidth();
			if(boxWidth>jbolt_tabbar_width){
				//如果超长 才会动弹 拿到激活的tab 让它居中
				var tab=that.getCurrentTab();
				var marginleft=Math.abs(parseInt(jbolt_tabs_container.css("margin-left")));
				var len=0;
				var len1=0;
				jbolt_tabs_container.find("li").each(function(){
					var li=$(this);
					len1=len1+li.outerWidth();
					if(li.data("key")==tab.data("key")){
						return false;
					}else{
						len=len+li.outerWidth();
					}
				});
				if(marginleft==0){
					//说明没动弹 那就判断到激活的距离是不是在显示区域内
					if(len<=jbolt_tabbar_width){
						
					}else{
						var mleft=len*-1;
						jbolt_tabs_container.css("margin-left",mleft+"px");
					}
				}else{
					if(len1<=jbolt_tabbar_width){
						that.jboltTabToLeft();
					}else{
						var mml=(jbolt_tabbar_width/2)-boxWidth;
						var mleft=len*-1;
						if(len>Math.abs(parseInt(mml))){
							that.jboltTabToRight();
						}else{
							jbolt_tabs_container.css("margin-left",mleft+"px");
						}
					}
				}
				
			}else{
				that.jboltTabToLeft();
			}
		}
}
/**
 * 新tab注册
 * @param key
 * @param url
 * @param text
 * @param triggerTabKey
 * @param openType
 * @param openOptions
 * @returns
 */
function pushJBoltTabToArray(key,url,text,triggerTabKey,openType,openOptions){
	if(jboltWithTabs){
		if(!openType){
			openType=1;
		}
		jbolt_tabs_array.push({key:key,url:url,text:text,openType:openType,openOptions:openOptions,triggerTabKey:triggerTabKey});
		sessionStorage.setItem('jbolt_tabs_array', JSON.stringify(jbolt_tabs_array));
	}
}
/**
 * 按照key激活tab 改变json里的active属性值
 * @param key
 * @returns
 */
//function activeJBoltTabFromArray(key){
//	if(jboltWithTabs && key){
//		window.location.hash=key;
//	}
//}
function activeJBoltTabFromArray(key){
	if(jboltWithTabs && isOk(jbolt_tabs_array)){
		JBoltArrayUtil.changeItemsAttrValue(jbolt_tabs_array,"active",false);
		JBoltArrayUtil.changeOneItemOtherAttrValueByAttr(jbolt_tabs_array,"key",key,"active",true);
		sessionStorage.setItem('jbolt_tabs_array',  JSON.stringify(jbolt_tabs_array));
	}
}
/**
 * 关闭的时候删掉
 * @param key
 * @returns
 */
function removeJBoltTabFromArray(key){
	if(jboltWithTabs && isOk(jbolt_tabs_array)){
		JBoltArrayUtil.removeObjectByAttr(jbolt_tabs_array,"key",key);
		sessionStorage.setItem('jbolt_tabs_array',  JSON.stringify(jbolt_tabs_array));
	}
}
/**
 * 清空tabs数组
 * @returns
 */
function clearJBoltTabFromArray(){
	if(jboltWithTabs && isOk(jbolt_tabs_array)){
		jbolt_tabs_array=[];
		sessionStorage.removeItem('jbolt_tabs_array');
	}
}
/**
 * 初始化页面的时候执行从sessionStorage 初始化需要打开的tabs
 * @returns
 */
function initJBoltTabsFromSessionStorage(){
	if(jboltWithTabs && JBoltTabKeepAfterReload){
		var tabsStr = sessionStorage.getItem('jbolt_tabs_array');
		var tabsJson;
		if(isOk(tabsStr)){
			tabsJson = JSON.parse(tabsStr);
		}
		
		if(isOk(tabsJson) && isArray(tabsJson)){
			var tab,activeKey;
			for(var i in tabsJson){
				tab=tabsJson[i];
				if(typeof(tab.active)=="boolean" && tab.active){
					activeKey = tab.key;
				}
				JBoltTabUtil.openTabWithOptions(tab.key,tab.url,tab.text,tab.triggerTabKey,false,tab.openType,tab.openOptions);
			}
			if(activeKey){
				JBoltTabUtil.showJboltTab(activeKey);
			}
		}
	}
}
/**
 * form转JSON
 * @param formEle
 * @returns
 */
function formToJson(formEle){
	 var form=getRealJqueryObject(formEle);
	 if(isOk(form)){
		 return form.serializeJSON();
	 }
	 return null;
}

;(function($) {
	$.fn.extend({
		toggleContent : function(content) {
			var html=this.html();
			if(html&&html>0){
				this.html("");
			}else{
				this.html(content);
			}
			
		}
	});
	
})(jQuery);

;(function($) {
    $.fn.extend({
        formToggleDisabled:function() {
        	var eles=this.find("input,select,textarea,.form-control");
        	if(isOk(eles)){
        		$.each(eles,function(i,ele){
        			if(ele.disabled){
        				ele.disabled=false;
                	}else{
                		ele.disabled=true;
                	}
        		});
        	}
        },
        formDisabled:function() {
        	var eles=this.find("input,select,textarea,.form-control");
        	if(isOk(eles)){
        		$.each(eles,function(i,ele){
        			if(!ele.disabled){
                		ele.disabled=true;
                	}
        		});
        	}
			this.addClass("disabled");
        },formEnable:function() {
        	var eles=this.find("input,select,textarea,.form-control");
        	if(isOk(eles)){
        		$.each(eles,function(i,ele){
        			if(ele.disabled){
                		ele.disabled=false;
                	}
        		});
        	}
			this.removeClass("disabled");
        },formSetJsonVal:function(jsonData,valueHandler) {
        	this[0].reset();
        	var that=this;
        	if(!isOk(jsonData)){
        		var key,eleObj,val;
        		this.find("img[data-switchbtn][data-name]").data("value",'false').each(function(){
        			eleObj=$(this);
        			key=eleObj.data("name");
        			val='false';
        			SwitchBtnUtil.set(key,val,that);
        		});
        	}
        	var eles=this.find("input[name]:not([data-switchbtn-hidden]),select[name],textarea[name],.form-control[name],[data-column][name],img[data-switchbtn][data-name]");
        	if(isOk(eles)){
        		if(!isOk(jsonData)){
        			eles.each(function(){
        				removeFormEleAllStyle($(this));
        			});
        			return;
        		}
        		var key,eleObj,val,tagName;
        		$.each(eles,function(i,ele){
        			val=null;
        			tagName=ele.tagName;
        			eleObj=$(ele);
        			removeFormEleAllStyle(eleObj);
        			key=ele.name||eleObj.data("column")||eleObj.data("name");
        			if(key){
        				//有设置key的话就可以赋值
        				if(jsonData.hasOwnProperty(key)){
        					val=jsonData[key];
        				}
        				if(valueHandler){
    						val=valueHandler(eleObj,jsonData,key,val);
    					}
    					if(typeof(val)=="undefined" || val==null){
    						val='';
    					}
        				if(isFormEle(tagName)){
        					//如果是表单元素就赋值
        					if(tagName=="INPUT"&&(ele.type=="checkbox"||ele.type=="radio")){
        						//如果是checkbox和radio 需要点击一下
        						if(ele.value==val){
        							eleObj.trigger("click");
        						}
        					}else{
        						if(ele.hasAttribute("data-date")){
        							ele.value=val;
        						}else{
        							eleObj.val(val);
        						}
        						eleObj.trigger("change");
        					}
        				}else if(tagName=="IMG"){
    						SwitchBtnUtil.set(key,val,that);
    					}else{
            				eleObj.html(val);
            			}
        			}
        			
        		});
        	}
        }
    });

})(jQuery);

/**
 * 切换表单元素的Disabled属性
 * @param formEle
 * @returns
 */
function formToggleDisabled(formEle){
	 var form=getRealJqueryObject(formEle);
	 if(isOk(form)){
		 form.formToggleDisabled();
	 }
}

/**
 * 表单元素转Disabled
 * @param formEle
 * @returns
 */
function formDisabled(formEle){
	 var form=getRealJqueryObject(formEle);
	 if(isOk(form)){
		 form.formDisabled();
	 }
}

/**
 * 表单元素转Enable
 * @param formEle
 * @returns
 */
function formEnable(formEle){
	var form=getRealJqueryObject(formEle);
	if(isOk(form)){
		form.formEnable();
	}
}

/**
 * 表单通过JSON对象赋值
 * @param formEle
 * @param jsonData
 * @param valueHandler
 * @returns
 */
function formSetJsonVal(formEle,jsonData,valueHandler){
	 var form=getRealJqueryObject(formEle);
	 if(isOk(form)){
		 form.formSetJsonVal(jsonData,valueHandler);
	 }
}

/**
 * 生成随机ID
 */
function randomId(len){
	if(!len){len=8;}
	var text = "";
	var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	for (var i = 0; i < len ; i++){
	    text += possible.charAt(Math.floor(Math.random() * possible.length));
	}
	return text;
}

var formEleArray=["INPUT","SELECT","TEXTAREA"];
function isFormEle(tagName){
	return $.inArray(tagName,formEleArray)>=0;
}

/**
 * switchBtn enableBtn
 */
var SwitchBtnUtil={
		initBtn:function(_btn){
			//var that=this;
				  //如果没设置src就是动态设置的 根据data-value
				  var value=_btn.data("value");
				  if(!value||value=="null"||value=="undefined"){
					  value=false;
				  }else{
					  value=true;
				  }
				  var style=_btn.data("style");
				  if(!style){
					  style="default";
				  }
				  var src="assets/img/switch/"+style+"/"+(value?"on":"off")+".png";
				  _btn.attr("src",src);
				  _btn.data("value",value).attr("data-value",value);
				  
				  var name=_btn.data("name")||_btn.attr("name");
				  if(name){
					  var hiddenId=_btn.data("hiddeninput");
					  var hiddenInput=hiddenId?jboltBody.find("#"+hiddenId):jboltBody.find("input[name='"+name+"']");
					  if(!isOk(hiddenInput)){
						  hiddenInput=$('<input type="hidden" name="'+name+'" value="'+value+'"/>');
						  _btn.after(hiddenInput);
					  }
					  hiddenInput.data("switchbtn-hidden",true).attr("data-switchbtn-hidden",true);
				  }
				  var text=_btn.data("text");
				  if(text&&text.indexOf(":")!=-1){
					  var tta=text.split(":");
					  _btn.trueText='<i class="text-success font-normal">'+tta[0]+'</i>';
					  _btn.falseText='<i class="text-danger font-normal">'+tta[1]+'</i>';
					  var btnLinkText=_btn.nextAll("span.linktext");
					  if(!isOk(btnLinkText)){
						  var linkText=$('<span class="linktext d-inline-block px-1">'+(value?_btn.trueText:_btn.falseText)+'</span>')
						  _btn.after(linkText);
					  }
				  }
				  //that.initBtnEvent(_btn);
		},
		setByCss:function(css,value,parentEle,nochange){
			var parent=getRealParentJqueryObject(parentEle);
			 if(!isOk(parent)){return;}
			var img=parent.find(css);
			 if(!isOk(img)){return;}
			 value=value+'';
			 var oldValue=img.data("value")+'';
			 if(value!=oldValue){
				 	img.data("nochange",nochange); 
					img.trigger("click"); 
			 }else if(value=='false'&&oldValue=='false'){
				  var style=img.data("style");
				  if(!style){
					  style="default";
				  }
				  var src="assets/img/switch/"+style+"/off.png";
				  img.attr("src",src);
				  img.data("value",value).attr("data-value",value);
				  var hiddenId = img.data("hiddeninput");
				  var hiddenInput = hiddenId?jboltBody.find("#"+hiddenId):jboltBody.find("input[name='"+name+"']");
				  if(isOk(hiddenInput)){
					  hiddenInput.val(value);
				  }
				  
				  var text=img.data("text");
				  if(text&&text.indexOf(":")!=-1){
					  var tta=text.split(":");
					  var falseText='<i class="text-danger font-normal">'+tta[1]+'</i>';
					  var linkText=img.nextAll(".linktext");
					  if(isOk(linkText)){
						  linkText.html(falseText);
					   }
				  }
			 }
		},
		set:function(name,value,parentEle,appendCss,nochange){
			 var css = "img[data-switchbtn][data-name~='"+name+"']";
			 if(appendCss){
				 css = css + appendCss;
			 }
			this.setByCss(css,value,parentEle,nochange);
		},
		//初始化点击事件 全局只初始化一次
		initEvent:function(){
			var that=this;
			jboltBody.on("click","img[data-switchbtn]",function(e){
				  var _btn=$(this);
				  e.preventDefault();
				  e.stopPropagation();
				  var doing=_btn.data("doing");
				  if(doing){
					  return false;
				  }
				  var isDisabled=_btn.data("disabled");
				  if(isDisabled){
					  var dmsg=_btn.data("disabled-msg");
					  LayerMsgBox.alert(dmsg?dmsg:"无权操作",2);
					  _btn.data("doing",false);
					  return false;
				  }
				  doing=true;
				  _btn.data("doing",true);
					var url=_btn.data("url");
					var confirm=_btn.data("confirm");
					if(url){
						if(confirm){
							LayerMsgBox.confirm(confirm,function(){
								that.switchIt(_btn);
							},function(){
								_btn.data("doing",false);
							});
						}else{
							that.switchIt(_btn);
						}
						
					}else{
						
						if(confirm){
							LayerMsgBox.confirm(confirm,function(){
								that.switchSuccessHandler(_btn);
							},function(){
								_btn.data("doing",false);
							});
						}else{
							that.switchSuccessHandler(_btn);
						}
						
//						LayerMsgBox.alert("组件未设置URL地址",2);
					}
			  });
			  
			 
		},
		initBtns:function(btns){
			if(!isOk(btns)){return false;}
			var that=this,len=btns.length;
			for(var i=0;i<len;i++){
				that.initBtn(btns.eq(i));
			}
			
		},
		  init:function(parentEle){
			  var that=this;
			  var parent=getRealParentJqueryObject(parentEle);
			  if(!isOk(parent)){return false;}
			  var btns=parent.find("img[data-switchbtn]");
			  if(!isOk(btns)){return false;}
			  that.initBtns(btns);
		  },
		  switchIt:function(_btn){
			  var that=this;
			  var url=_btn.data("url");
				LayerMsgBox.loading("正在执行...",10000);
				url=actionUrl(url);
				url=processEleUrlByLinkOtherParamEle(_btn,url);
      			url=processJBoltTableEleUrlByLinkColumn(_btn,url);
				$.ajax({
					type:"post",
					url:url,
					timeout : 10000, //超时时间设置，单位毫秒
					dataType:"json",
					success:function(data){
						if(data.state=="ok"){
							that.switchSuccessHandler(_btn,data.data);
						}else{
							 _btn.data("doing",false);
							if(data.msg&&data.msg=="jbolt_system_locked"){
								showJboltLockSystem();
							}else if(data.msg&&data.msg=="jbolt_nologin"){
								showReloginDialog();
							}else if(data.msg&&data.msg=="jbolt_terminal_offline"){
								showReloginDialog();
								LayerMsgBox.alert("当前用户已在其它终端登录，本机已下线",2);
							}else{
								LayerMsgBox.alert(data.msg,2);
							}
						}
					},
					error:function(){
						 _btn.data("doing",false);
						LayerMsgBox.alert("操作失败",2);
					}
				});
			  },
			  processJBoltTableFixedColumnSameSwitchBtn:function(_btn){
				  //判断是不是在jboltTable的fixedColumnBox中
				  var jboltTable=_btn.closest("table.jbolt_table");
				  //判断是在JBoltTable中
				  if(!isOk(jboltTable)){return false;}
				  var table_view=jboltTable.closest(".jbolt_table_view");
				  if(!isOk(table_view)){return false;}
				  var fixedBox= _btn.closest(".jbolt_table_fixed");
				  var inFixedColumnTable=isOk(fixedBox);
				  var src=_btn.attr("src");
				  var td=_btn.closest("td");
				  var tdIndex=td.index();
				  var tr=_btn.closest("tr");
				  var trIndex=tr.index();
				  var colIndex=td.data("col-index");
				  var sameTds;
				  if(inFixedColumnTable){
					//已经在fixedColumn中 就需要去更新mainTable中的
					  var mainTable=table_view.find(".jbolt_table_box>.jbolt_table_body:first>table");
					  if(!isOk(mainTable)){return false;}
					  sameTds=mainTable.find("tbody>tr:eq("+trIndex+")>td[data-col-index='"+colIndex+"']");
				  }else{
					  var fixedBoxsTables=table_view.find(".jbolt_table_fixed>.jbolt_table_body>table");
					  if(!isOk(fixedBoxsTables)){return false;}
					  sameTds=fixedBoxsTables.find("tbody>tr:eq("+trIndex+")>td[data-col-index='"+colIndex+"']");
				  }
				  if(isOk(sameTds)){
					  sameTds.find("img[data-switchbtn]").attr("src",src);
				  }
			  },
			  switchMainJBoltTableEditableTd:function(){
				  
			  },
			  //内置可编辑表格里的处理
			  switchEditableTd:function(_btn,result){
				var editingTd=_btn.closest("td");
				if(!isOk(editingTd)){
					LayerMsgBox.alert("switchEditableTd这个handler只能在jbolttable的可编辑表格里用[1]",2);
					return false;
				}
				var tr=editingTd.closest("tr");
				var table=tr.closest("table");
				
				var jboltTable=table.jboltTable("inst");
				if(!jboltTable){
					LayerMsgBox.alert("switchEditableTd这个handler只能在jbolttable的可编辑表格里用[2]",2);
					return false;
				}
				jboltTable.me.processSwitchBtnTd(jboltTable,editingTd,_btn,result);
				
			  },
			  switchSuccessHandler:function(_btn,onOrOff){
				var that=this;
			    var src=_btn.attr("src");
				var handler=_btn.data("handler");
				var on=src.indexOf("off")!=-1;
				if(onOrOff==undefined){
					if(src.indexOf("off")!=-1){
						src=src.replace("off","on");
						_btn.attr("data-value",true).data("value",true);
					}else{
						src=src.replace("on","off");
						_btn.attr("data-value",false).data("value",false);
					}
				}else{
					if(typeof(onOrOff)=="boolean"){
						if(onOrOff){
							//true
							if(src.indexOf("off")!=-1){
								src=src.replace("off","on");
							}
						}else{
							if(src.indexOf("on")!=-1){
								src=src.replace("on","off");
							}
						}
						_btn.attr("data-value",onOrOff).data("value",onOrOff);
					}else{
						if(src.indexOf("off")!=-1){
							src=src.replace("off","on");
							_btn.attr("data-value",true).data("value",true);
						}else{
							src=src.replace("on","off");
							_btn.attr("data-value",false).data("value",false);
						}
					}
						
				}
				
				_btn.attr("src",src);
				if(!_btn.hiddenInput){
					 var name = _btn.data("name");
					 var hiddenId = _btn.data("hidden-input") || _btn.data("hiddeninput");
					  var hiddenInput = hiddenId?jboltBody.find("#"+hiddenId):jboltBody.find("input[name='"+name+"']");
					  if(isOk(hiddenInput)){
						  _btn.hiddenInput=hiddenInput;
					  }
				}
				if(_btn.hiddenInput){
					_btn.hiddenInput.val(on).trigger("change");
				}
				if(!_btn.linkText){
					 var text=_btn.data("text");
					  if(text&&text.indexOf(":")!=-1){
						  var tta=text.split(":");
						  _btn.trueText='<i class="text-success font-normal">'+tta[0]+'</i>';
						  _btn.falseText='<i class="text-danger font-normal">'+tta[1]+'</i>';
						  var btnLinkText=_btn.nextAll(".linktext");
						  if(isOk(btnLinkText)){
							  _btn.linkText=btnLinkText;
						  }
					  }
				}
				if(_btn.linkText){
					_btn.linkText.html(on?_btn.trueText:_btn.falseText);
				}
				var setvalueto=_btn.data("setvalueto");
				if(setvalueto){
					var eleObjs=$(setvalueto);
					if(isOk(eleObjs)){
						var eleObj,ele,tagName;
						eleObjs.each(function(){
							ele=this;
							eleObj=$(ele);
							tagName=ele.tagName;
							if(isFormEle(tagName)){
								if(tagName=="INPUT"&&(ele.type=="checkbox"||ele.type=="radio")){
									if(on){
										if(!ele.checked){
											ele.checked=true;
											eleObj.attr("checked","checked");
											eleObj.trigger("change");
										}
									}else{
										if(ele.checked){
											ele.checked=false;
											eleObj.removeAttr("checked");
											eleObj.trigger("change");
										}
									}
								}else{
									eleObj.val(on).trigger("change");
								}
								
							}else{
								eleObj.text(on);
							}
						});
					}
				}
				//处理一下同步
				this.processJBoltTableFixedColumnSameSwitchBtn(_btn);
				LayerMsgBox.closeLoadingNow();
				if(handler && !_btn.data("nochange")){
					if(handler=="switchEditableTd"){
						that.switchEditableTd(_btn,on);
						if(_btn.data("nomsg")){
							_btn.data("doing",false);
						}else{
							LayerMsgBox.success("操作成功",300,function(){
								 _btn.data("doing",false);
							 });
						}
					}else{
						var exe_handler=eval(handler);
						if(exe_handler&&typeof(exe_handler)=="function"){
							if(_btn.data("nomsg")){
								exe_handler(_btn);
								_btn.data("doing",false);
							}else{
								LayerMsgBox.success("操作成功",300,function(){
									exe_handler(_btn);
									_btn.data("doing",false);
								});
							}
							
						}else{
							if(_btn.data("nomsg")){
								_btn.data("doing",false);
							}else{
								LayerMsgBox.success("操作成功",300,function(){
									_btn.data("doing",false);
								});
							}
						}
					}
				}else{
					if(_btn.data("nomsg")){
						 _btn.data("doing",false);
					}else{
						LayerMsgBox.success("操作成功",300,function(){
							 _btn.data("doing",false);
						 });
					}
					
				}
				
				if(_btn.data("nochange")){
					_btn.data("nochange",false);
				}
				
		  }
}


/*var DragScrollElementUtil={
		init:function(id){
			$('#'+id).niceScroll({
			    cursorcolor: "#ccc",//#CC0071 光标颜色
			    cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
			    touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
			    cursorwidth: "5px", //像素光标的宽度
			    cursorborder: "0", // 游标边框css定义
			    cursorborderradius: "5px",//以像素为光标边界半径
			    autohidemode: true //是否隐藏滚动条
			});
		}
}
var DragScrollUtil={
		init:function(parentId){
			  var that=this;
			  var eles=null;
			  if(parentId){
				  eles=$('#'+parentId).find("[data-dragscroll]")
			  }else{
				  eles=jboltBody.find("[data-dragscroll]");
			  }
			  if(eles&&eles.length>0){
				  for(var i in eles){
					  var id=eles[i].id;
					  if(!id){
						id=randomId();
						eles[i].id=id;
					  }
					  DragScrollElementUtil.init(id);
				  }
			  }
		}
} */
 

/**
 * checkbox工具类封装
 */
var CheckboxUtil={
		processHiddenInput:function(inputName,hiddenInputId,ck){
			var ids=this.getCheckedValueToString(inputName,",",ck);
			$("#"+hiddenInputId).val(ids).change();
		},
		initCheckBoxEvent:function(ck,name,hiddenInputId,handler){
			var that=this;
			if(handler){
			 if(handler=="processHiddenInput"){
				 if(hiddenInputId){
					 that.processHiddenInput(name,hiddenInputId,ck);
				 }else{
					 LayerMsgBox.alert("请checkbox组件设置对应隐藏域data-hidden-input",2);
				 }
			  }else{
				  var exe_handler=eval(handler);
				  if(exe_handler&&typeof(exe_handler)=="function"){
					  ck.find("input[type='checkbox'][name='"+name+"']").unbind("change").on("change",function(){
						  var input=$(this);
						  var values=that.getCheckedValueToString(name,",",ck);
						  if(hiddenInputId){
							  $("#"+hiddenInputId).val(values).change();
						  }
						  exe_handler(ck,values,input,input.is(":checked"));
						  // if(hiddenInputId){
							//   that.processHiddenInput(name,hiddenInputId,ck);
						  // }
					  });
				  }
			  }
			 
		  }else{
			  if(hiddenInputId){
					 ck.find("input[type='checkbox'][name='"+name+"']").unbind("change").on("change",function(){
						 that.processHiddenInput(name,hiddenInputId,ck);
					 });
				}
		  }
		},
		initCheckboxs:function(checkboxs){
			if(!isOk(checkboxs)){return false;}
			var len=checkboxs.length;
			for(var i=0;i<len;i++){
				this.initCheckbox(checkboxs.eq(i));
			}
		},
		initCheckbox:function(ck){
			var that=this,
			handler=ck.data("handler"),
			name=ck.data("name"),
			value=ck.data("value")+"",
			defaultValue=ck.data("default")+"",
			hiddenInputId=ck.data("hidden-input")||ck.data("hiddeninput"),
			url=ck.data("url"),
			label=ck.data("label"),
			optionItems=ck.data("options");
//			 if(!value){value="";}else{value=value+""}
//			  if(!defaultValue){defaultValue="";}else{defaultValue=defaultValue+""}
			  if(url){
				  that.insertDatas(ck,url,name,label,function(){
					  that.initCheckBoxEvent(ck,name,hiddenInputId,handler);
					  that.setChecked(ck,name,value,defaultValue);
					  that.processDisabled(ck);
				  });
			  }else{
				  var ckcb=function(){
					  var hasInline=ck.find(".checkbox-inline");
					  if(hasInline){
						  //处理样式
						  that.processCheckboxAlignAndWidth(ck);
					  }
					  that.initCheckBoxEvent(ck,name,hiddenInputId,handler);
					
					  that.setChecked(ck,name,value,defaultValue);
					  that.processDisabled(ck);
				  }
				  if(isOk(optionItems)){
					  that.insertDatas(ck,optionItems,name,label,ckcb,true);
				  }else{
					  ckcb();
				  }
				  
			  }
			
		 
		},
		processDisabled:function(ck){
			var hasDisabled=ck[0].hasAttribute("data-disabled");
			if(hasDisabled){
				var disabled=ck.data("disabled");
				if(!(typeof(disabled)=="undefined")&&(disabled+"")!="false"){
					var name=ck.data("name");
					var input=ck.find("input[type='checkbox'][name='"+name+"']");
					input.attr("disabled","disabled");
				}
			}
			
		},
		  init:function(parentEle){
			  var parent=getRealParentJqueryObject(parentEle);
			  if(!isOk(parent)){return false;}
			  var checkboxs=parent.find("[data-checkbox]");
			  if(!isOk(checkboxs)){return false;}
			  this.initCheckboxs(checkboxs);
		  },insertDatas:function(ck,url,name,label,callback,withOptions){
			  var that=this;
			  ck.empty();
			  
			  var width=ck.data("width");
			  var labelWidth="";
			  var radioWidth="";
			  if(width){
				  var arr=width.split(",");
				  labelWidth=arr[0];
				  radioWidth=arr[1];
			  }else{
				  labelWidth="col-sm-2";
				  radioWidth="col";
			  }
			  var html='';
			  if(label){
				  var isRequired=(ck.data("rule")&&(!ck.data("notnull")));
				 if(labelWidth.indexOf("px")!=-1){
					 html= '<label class="col-12 col-sm-auto col-form-label '+(isRequired?"is_required":"")+'" style="width:'+labelWidth+'">'+label+'</label>';
				 }else{
					 html= '<label class="'+labelWidth+' col-form-label '+(isRequired?"is_required":"")+'">'+label+'</label>';
				 }
			  }
			  
			  var inline="";
			  var isInline=ck.data("inline");
			  var itemMinWidth=ck.data("item-min-width");
			  if(itemMinWidth){
				  if(typeof(itemMinWidth)=="number"){
					  itemMinWidth=itemMinWidth+"px";
				  }
			  }else{
				  itemMinWidth="60px";
			  }
			  if(isInline){
				  inline="checkbox-inline";
			  }
			  
				var text_attr=ck.data("text-attr");
	      		if(!text_attr){
	      			text_attr="text";
	      		}
	      		
	      		var value_attr=ck.data("value-attr");
	      		if(!value_attr){
	      			value_attr="value";
	      		}
	      		
	      		var delimiter=ck.data("delimiter");
	      		var textFormatHandler=ck.data("text-format-handler");
	      		
	      		var processOptions=function(list){
  					var nodotname=name.replace("\\.","_");
  					if(list&&list.length>0){
  						html+='<div class="'+radioWidth+'"  style="padding-top: 1px;">';
  						var optionItem,text,value,theCheckBoxId,ranid=randomId();
  						for(var i in list){
  							optionItem=list[i];
  							text=processOptionTextByFormatHandler(textFormatHandler,optionItem,text_attr,delimiter);
  							value=processOptionValue(optionItem,value_attr);
  							theCheckBoxId=nodotname+"_"+ranid+"_"+i;
  							var radioHtml = '<div '+(isInline?'style="min-width:'+(itemMinWidth)+';"':'')+' class="checkbox checkbox-primary '+inline+'">'+
  								'<input  id="'+theCheckBoxId+'" type="checkbox" name="'+name+'" value="'+value+'"/>'+
  									'<label for="'+theCheckBoxId+'">'+text+'</label>'+
  								'</div>';
      						html+=radioHtml;
  	  					}
  						html+="</div>";
  						ck.html(html);
  						if(isInline){
							//处理样式
							that.processCheckboxAlignAndWidth(ck);
						}
  					}else{
  						html+='<div class="'+radioWidth+'"  style="padding-top: 1px;"></div>';
  						ck.html(html);
  					}
  					
					if(callback){
						callback();
					}
	      		}
	      		//如果是静态options
	      		if(withOptions){
	      			if(url){
	      				var optionsDatas;
	      				if(!isArray(url)){
	      					var options;
	      					if(url.indexOf(",")!=-1){
	      						options=url.split(",");
	      					}else{
	      						options=[options];
	      					}
	      					
	      					if(isOk(options)){
	      						var option,tempArr,tempOption;
	      						optionsDatas=new Array();
	      						for(var i in options){
	      							option=options[i];
	      							if(option.indexOf(":")!=-1){
	      								tempArr=option.split(":");
		      							if(isOk(tempArr)){
		      								if(tempArr.length>=2){
		      									tempOption={name:tempArr[0],value:tempArr[1]};
		      								}else{
		      									tempOption={name:tempArr[0],value:tempArr[0]};
		      								}
		      							}
		      						}else{
		      							tempOption={name:option,value:option};
		      						}
	      							if(tempOption){
	      								optionsDatas.push(tempOption);
	      							}
	      						}
	      					}
	      					
	      					
	      				}else{
	      					optionsDatas=url;
	      				}
	      				
	      				//调用赋值渲染
	      				processOptions(optionsDatas);
	      				
	      			}
	      		}else{
	      			Ajax.get(url,function(res){
	      				processOptions(res.data);
	      			});
	      		}
			  
		  },
		  processCheckboxAlignAndWidth:function(ck){
			  var alignLeft=ck.data("align-left");
			  if(alignLeft){
					  var chs=ck.find(".checkbox.checkbox-inline");
					  if(isOk(chs)){
						  var max=0,w;
						  chs.each(function(){
							  w=$(this).outerWidth();
							  if(w>max){
								  max=w;
							  }
						  });
						  chs.css({"width":max+"px","max-width":max+"px"});
					  }
			  }
		  },
		  checkByArray:function(parentEle,name,values){
			  var that=this;
			  if(!isOk(values)){
				  //如果没有任何数据 就清空选择
				  that.uncheckAll(name,parentEle);
				  return false;
			  }
			  if(typeof(values)=="string"&&values=="all"){
				  that.checkAll(name,parentEle);
				  return true;
			  }
			  if(!isArray(values)){
				  values=values.toString();
				  if(values.indexOf(",")!=-1){
					  //如果字符串有逗号 就分割为数组
					  values=values.split(",");
					  if(!isOk(values)){
						  //如果没有任何数据 就清空选择
						  that.uncheckAll(name,parentEle);
						  return false;
					  }
				  }else{
					  values=[values];
				  }
			  }
			  var parent=getRealParentJqueryObject(parentEle);
			  var input;
			  for(var i in values){
				  input=parent.find("input[type='checkbox'][name='"+name+"'][value='"+values[i]+"']");
				  if(isOk(input)){
					  input[0].checked=true;
					  input.attr("checked","checked");
				  }
			  }
		  },
		  setChecked:function(parentEle,name,value,defaultValue){
			  if(value!=undefined&&value!="undefined"&&value!=''){
				  if(!isArray(value)){
					  value=value+'';
				  }
			  }else{
				  value='';
			  }
				if(defaultValue!=undefined&&defaultValue!="undefined"&&defaultValue!=''){
					if(!isArray(defaultValue)){
						defaultValue=defaultValue+'';
					}
				}else{
					defaultValue='';
				}
			  var that=this;
			  if(value){
				  that.checkByArray(parentEle,name,value);
			  }else{
				  if(defaultValue||defaultValue==0||defaultValue=="0"){
					 that.checkByArray(parentEle,name,defaultValue);
				  }
			  }
			 
		  },
		  checkIt:function(checkbox){
			  $(checkbox).each(function(){
				  if(!this.checked){
					  $(this).prop("checked",true).change();
				  }
			  });
		  },
		  uncheckIt:function(checkbox){
			  $(checkbox).each(function(){
				  if(this.checked){
					  $(this).prop("checked",false).change();
				  }
			  });
		  },
		  checkAll:function(name,parentEle){
			  var parent=getRealParentJqueryObject(parentEle);
			  parent.find("input[type='checkbox'][name='"+name+"']:not(:checked):not(:disabled)").prop("checked",true).change();
		  },
		  uncheckAll:function(name,parentEle){
			  var parent=getRealParentJqueryObject(parentEle);
			  parent.find("input[type='checkbox'][name='"+name+"']:checked:not(:disabled)").prop("checked",false).change();
		  },convertCheckAll:function(name,parentEle){
			  var parent=getRealParentJqueryObject(parentEle);
			  var checkedList=parent.find("input[type='checkbox'][name='"+name+"']:checked:not(:disabled)");
			  var notCheckedList=parent.find("input[type='checkbox'][name='"+name+"']:not(:checked):not(:disabled)");
			  if(isOk(checkedList)){
				  checkedList.prop("checked",false).change();
			  }
			  if(isOk(notCheckedList)){
				  notCheckedList.prop("checked",true).change();
			  }
		  },getCheckedValue:function(name,parentEle,withoutDisabled){
			  var ids=new Array(),checkedbox;
				 var parent=getRealParentJqueryObject(parentEle);
				 if(withoutDisabled){
					 checkedbox=parent.find("input[type='checkbox'][name='"+name+"']:checked:not(:disabled)");
				 }else{
					 checkedbox=parent.find("input[type='checkbox'][name='"+name+"']:checked");
				 }
				 if(isOk(checkedbox)){
					 checkedbox.each(function(i){
						ids.push($(this).val());
					});
				 }
				return ids;
			},
			//得到选中的值 转为字符分割string字符串
			getCheckedValueToString:function(name,split,parentEle,withoutDisabled){
				var that=this,result;
				var ids=that.getCheckedValue(name,parentEle,withoutDisabled);
			    if(isOk(ids)){
			    	result=ids.join(split?split:",");
			    }
				return result;
			},getCheckedTextToString:function(name,split,parentEle,withoutDisabled){
				var that=this,result;
				var txs=that.getCheckedText(name,parentEle,withoutDisabled);
				if(isOk(txs)){
					result=txs.join(split?split:",");
				}
				return result;
			},
			getCheckedText:function(name,parentEle,withoutDisabled){
				var txs=new Array(),checkedbox;
				var parent=getRealParentJqueryObject(parentEle);
				if(withoutDisabled){
					checkedbox=parent.find("input[type='checkbox'][name='"+name+"']:checked:not(:disabled)");
				}else{
					checkedbox=parent.find("input[type='checkbox'][name='"+name+"']:checked");
				}
				if(isOk(checkedbox)){
					var label;
					checkedbox.each(function(i){
						label=checkedbox.eq(i).parent().find("label");
						if(isOk(label)){
							txs.push(label.text());
						}
					});
				}
				return txs;
			}
}

  

 
/**
 * 富文本编辑器组件初始化
 */
var HtmlEditorUtil={
		ing:false,
		initEditors:function(editors){
			 if(!isOk(editors)){return false;}
			 var that=this,
			 len=editors.length;
			 if(len>0){
				 var neditors=new Array();
				 var summereditors=new Array();
				 var type,editor;
				 for(var i=0;i<len;i++){
					editor=editors.eq(i);
					type=editor.data("editor");
					if(type=="summernote"){
						summereditors.push(editor);
					}else if(type=="neditor"){
						neditors.push(editor);
					}
				 }
				 var neditorSize=neditors.length;
				 var summerSize=summereditors.length;
				 if(neditorSize>0){
					 loadJBoltPlugin(["neditor"],function(){
						for(var i=0;i<neditorSize;i++){
							that.initEditor(neditors[i]);
						}
					 });
				 }
				 if(summerSize>0){
					 loadJBoltPlugin(["summernote"],function(){
						 for(var i=0;i<summerSize;i++){
								that.initEditor(summereditors[i]);
							}
						});
				 }
			 }
			 
		},
		init:function(parentEle){
			 var parent=getRealParentJqueryObject(parentEle);
			 if(!isOk(parent)){return false;}
			 var editors=jboltBody.find("[data-editor]");
			 if(!isOk(editors)){return false;}
			 this.ing=false;
			 this.initEditors(editors);
			
		},initNEditor:function(htmlEditor){
			var that=this;
			var editorId=htmlEditor.attr("id");
			if(!editorId){
				editorId = "neditor_"+randomId(6);
				htmlEditor.attr("id",editorId);
				// alert("请设置编辑器的id属性");
				// return false;
			}
//			var umjs=$("script[src*='neditor']");
//			if(!umjs||umjs.length==0){
//				LayerMsgBox.alert("未引入NEditor的js");
//				return false;
//			}

			var imgmaxsize=htmlEditor.data("imgmaxsize")||200;
			imgmaxsize=imgmaxsize*1024;
			var videomaxsize=htmlEditor.data("videomaxsize")||10;
			videomaxsize=videomaxsize*1024*1024*1024;
			
			var wordCount=htmlEditor.data("wordcount")||false;
			var elementPathEnabled=htmlEditor.data("elementpathenabled")||false;
			var maximumWords=htmlEditor.data("maximumwords")||10000;
			var autoHeightEnabled=htmlEditor.data("autoheightenabled")||false;
			var catchRemoteImageEnable=htmlEditor.data("catchremoteimageenable")||false;
			var toolbarTheme = htmlEditor.data("toolbar")||"normal";
			var toolbarOptions;
			if(toolbarTheme=="normal"){
				toolbarOptions = [
					[   'source',//源码
						'undo', //撤销
						'redo', //重做
						'bold', //加粗
						'italic', //斜体
						'underline', //下划线
						'strikethrough', //删除线
						'fontborder', //字符边框
						'indent', //首行缩进
						'superscript', //上标
						'subscript', //下标
						'justifyleft', //居左对齐
						'justifyright', //居右对齐
						'justifycenter', //居中对齐
						'justifyjustify', //两端对齐
						'forecolor', //字体颜色
						'backcolor', //背景色
						'removeformat', //清除格式
						'formatmatch', //格式刷
						'autotypeset', //自动排版
						'touppercase', //字母大写
						'tolowercase', //字母小写
						'fontfamily', //字体
						'fontsize', //字号
						'paragraph', //段落格式
						'customstyle', //自定义标题
						'searchreplace', //查询替换
						'blockquote', //引用
						'pasteplain', //纯文本粘贴模式
						'selectall', //全选
						'cleardoc', //清空文档

						'link', //超链接
						'unlink', //取消链接
						'emotion', //表情
						'spechars', //特殊字符
						'insertorderedlist', //有序列表
						'insertunorderedlist', //无序列表
						'horizontal', //分隔线
						'date', //日期
						'time', //时间
//				        'simpleupload', //单图上传
						'insertimage', //多图上传
						'wordimage',
						'imagenone', //默认
						'imageleft', //左浮动
						'imageright', //右浮动
						'imagecenter', //居中
						'lineheight', //行间距
						'map', //Baidu地图
						'insertvideo', //视频
						'insertcode', //代码语言
						'inserttable', //插入表格
						'edittable', //表格属性
						'edittd', //单元格属性
						'insertrow', //前插入行
						'insertcol', //前插入列
						'mergeright', //右合并单元格
						'mergedown', //下合并单元格
						'deleterow', //删除行
						'deletecol', //删除列
						'splittorows', //拆分成行
						'splittocols', //拆分成列
						'splittocells', //完全拆分单元格
						'deletecaption', //删除表格标题
						'inserttitle', //插入标题
						'mergecells', //合并多个单元格
						'deletetable', //删除表格
						'insertparagraphbeforetable', //"表格前插入行"


						'directionalityltr', //从左向右输入
						'directionalityrtl', //从右向左输入
						'rowspacingtop', //段前距
						'rowspacingbottom', //段后距
						'background', //背景
						'template', //模板
						'scrawl', //涂鸦
						'drafts', // 从草稿箱加载
						'print',
						'preview', //预览
						'help', //帮助
						'fullscreen', //全屏
					]
				];
			}else if(toolbarTheme == "simple"){
				toolbarOptions = [
					[
						'undo', //撤销
						'redo', //重做
						'bold', //加粗
						'italic', //斜体
						'underline', //下划线
						'strikethrough', //删除线
						'fontborder', //字符边框
						'indent', //首行缩进
						'superscript', //上标
						'subscript', //下标
						'justifyleft', //居左对齐
						'justifyright', //居右对齐
						'justifycenter', //居中对齐
						'justifyjustify', //两端对齐
						'forecolor', //字体颜色
						'backcolor', //背景色
						'removeformat', //清除格式
						'formatmatch', //格式刷
						'autotypeset', //自动排版
						'touppercase', //字母大写
						'tolowercase', //字母小写
						'fontfamily', //字体
						'fontsize', //字号
						'paragraph', //段落格式
						'customstyle', //自定义标题
						'searchreplace', //查询替换
						'blockquote', //引用
						'pasteplain', //纯文本粘贴模式
						'selectall', //全选
						'cleardoc', //清空文档

						'link', //超链接
						'unlink', //取消链接
						'emotion', //表情
						'spechars', //特殊字符
						'insertorderedlist', //有序列表
						'insertunorderedlist', //无序列表
						'horizontal', //分隔线
						'date', //日期
						'time', //时间
						'insertimage', //多图上传
						'imagenone', //默认
						'imageleft', //左浮动
						'imageright', //右浮动
						'imagecenter', //居中
						'lineheight', //行间距
						'insertvideo', //视频
						'directionalityltr', //从左向右输入
						'directionalityrtl', //从右向左输入
						'rowspacingtop', //段前距
						'rowspacingbottom', //段后距
						'background', //背景
						'fullscreen', //全屏
					]
				];
			}else if(toolbarTheme == "none"){
				toolbarOptions = [];
			}
		var neditorOptions={
				  imageMaxSize:imgmaxsize,
				  videoMaxSize:videomaxsize,
			    //关闭字数统计
			      wordCount:wordCount,
			      maximumWords:maximumWords,
			      autoHeightEnabled:autoHeightEnabled,
			      catchRemoteImageEnable:catchRemoteImageEnable,
			      //关闭elementPath
			      elementPathEnabled:elementPathEnabled,
			      toolbars: toolbarOptions,
				allowDivTransToP:false
				
		}
		var width=htmlEditor.data("width");
		var height=htmlEditor.data("height");
		if(!width){
			width="100%";
		}
		if(!height){
			height=300;
		}
		
		neditorOptions['initialFrameWidth']=width;
		neditorOptions['initialFrameHeight']=height;
		var urlprefix=htmlEditor.data("urlprefix");
		if(!urlprefix){
			urlprefix=htmlEditor.data("imghost");
		}
		if(urlprefix){
			neditorOptions['imageUrlPrefix']=urlprefix;
			neditorOptions['scrawlUrlPrefix']=urlprefix;
			neditorOptions['videoUrlPrefix']=urlprefix;
			neditorOptions['fileUrlPrefix']=urlprefix;
		}else{
			var imageUrlPrefix=htmlEditor.data("imageurlprefix");
			if(imageUrlPrefix){
				neditorOptions['imageUrlPrefix']=imageurlprefix;
			}
			var scrawlUrlPrefix=htmlEditor.data("scrawlurlprefix");
			if(scrawlUrlPrefix){
				neditorOptions['scrawlUrlPrefix']=scrawlUrlPrefix;
			}
			var videoUrlPrefix=htmlEditor.data("videourlprefix");
			if(videoUrlPrefix){
				neditorOptions['videoUrlPrefix']=videoUrlPrefix;
			}
			var fileUrlPrefix=htmlEditor.data("fileurlprefix");
			if(fileUrlPrefix){
				neditorOptions['fileUrlPrefix']=fileUrlPrefix;
			}
		}
		
		
		
		
		that.processInitNEditor(htmlEditor,neditorOptions);
		},processInitNEditor:function(htmlEditor,neditorOptions){
			var that=this;
			var editorId=htmlEditor.attr("id");
			if(!editorId){
				editorId = "summernote_"+randomId(6);
				htmlEditor.attr("id",editorId);
				// alert("请设置编辑器的id属性");
				// return false;
			}
			
			
			var hiddenInputId=htmlEditor.data("hidden-input")||htmlEditor.data("hiddeninput");
			UE.delEditor(editorId);
			var neditor = UE.getEditor(editorId,neditorOptions);
			neditor.addListener("contentChange",function(){
	        	  if(hiddenInputId){
	        		  var hidden=$("#"+editorId).closest(".jbolt_page").find("#"+hiddenInputId);
	        		  if(hidden&&hidden.length>0){
	        			  var content=neditor.getContent();
	        			  hidden.val(content).change();
	        		  }
	        	  }
			});
			
			
			
		},initSummernoteEditor:function(htmlEditor){
			var that=this;
			var editorId=htmlEditor.attr("id");
			if(!editorId){
				editorId = "summernote_"+randomId(6);
				htmlEditor.attr("id",editorId);
				// alert("请设置编辑器的id属性");
				// return false;
			}
			var toolbarTheme = htmlEditor.data("toolbar")||"normal";
			var toolbar=null;
			if(toolbarTheme == "normal"){
				toolbar= [
					['style', ['style']],
					['font', ['bold', 'italic', 'underline', 'clear','strikethrough', 'superscript', 'subscript']],
				    ['fontsize', ['fontsize']],
				    ['color', ['color']],
				    ['para', ['ul', 'ol', 'paragraph','height']],
				    ['insert', ['hr','table','link', 'picture','video']],
				    ['misc',['fullscreen','codeview','undo','redo','help']]
				  ];
			}else if(toolbarTheme=="simple"){
				toolbar= [
					['font', ['bold', 'italic', 'underline', 'clear','strikethrough', 'superscript', 'subscript']],
				    ['insert', ['hr','picture']],
				    ['misc',['undo','redo','help']]
				  ];
			}else if(toolbarTheme=="none") {
				toolbar = [];
			}
			var width=htmlEditor.data("width");
			var height=htmlEditor.data("height")||300;
			var maxsize=htmlEditor.data("imgmaxsize")||htmlEditor.data("maxsize")||200;
			var placeholder=htmlEditor.data("placeholder");
			var summernoteOptions={lang:"zh-CN", 
				dialogsInBody:true,
				toolbar:toolbar, 
				callbacks: {
		          onImageUpload: function(files, editor, $editable) {
		        	  if(that.ing){
		        		     alert("有文件正在上传，请稍后~~");
		        	  }else{
		        		  that.ing=true;
		        		  var len=files.length;
		        		  var qiniuBucketSn = htmlEditor.data("bucket-sn");
		        		  var uploadTo = htmlEditor.data("upload-to")||"local";
		        		  if(uploadTo == "local"){
		        			  for(var i=0;i<len;i++){
		        				  if(files[i].size/1024>maxsize){
		        					  that.ing=false; 
		        					  LayerMsgBox.alert("图片文件不能大于"+maxsize+"k",2);
		        					  return false;
		        				  }
		        				  that.sendSummernoteFile(htmlEditor,files[i]);
		        			  }
		        		  }else if(uploadTo == "qiniu"){
		        			  that.sendSummernoteFileToQiniu(htmlEditor,files);
		        		  }else{
		        			  LayerMsgBox.alert("暂不支持上传"+uploadTo,2);
		        		  }
		            	  that.ing=false;
		        	  }
		                  
		             
		          },onChange: function(contents, $editable) {
		        	  var hiddenInputId=htmlEditor.data("hidden-input")||htmlEditor.data("hiddeninput");
		        	  if(hiddenInputId){
		        		  var hidden=$("#"+hiddenInputId);
		        		  if(hidden&&hidden.length>0){
		        			  hidden.val(contents);
		        		  }
		        	  }
		          },onPaste:function(e){
		        	  if(that.ing){
		        		  alert("有文件正在上传，请稍后~~");
		        	  }else{
		        		  that.parseIamge(e,htmlEditor);
		        	  }
		        	  
		          }
		      }};
			if(placeholder){
				summernoteOptions.placeholder=placeholder;
			}
			if(width){
				if(isNaN(width)){
					summernoteOptions.width=width;
				}else{
					summernoteOptions.width=width+"px";
				}
			}
			if(isNaN(height)){
				summernoteOptions.height=height;
			}else{
				summernoteOptions.height=height+"px";
			}
			htmlEditor.summernote(summernoteOptions);
			var disabled=htmlEditor.data("disabled");
			if(disabled){
				htmlEditor.summernote("disable");
			}
		},initEditor:function(htmlEditor){
			var that=this;
			var type=htmlEditor.data("editor");
			if(type=="summernote"){
				that.initSummernoteEditor(htmlEditor);
			}else if(type=="neditor"){
				that.initNEditor(htmlEditor);
			}else{
				LayerMsgBox.alert("data-editor类型错误",2);
			}
		},parseIamge:function(e,editor){
			 var that=this;
			 that.ing=true; 
			 var maxsize=editor.data("maxsize");
			 if(!maxsize){
				 maxsize=200;
			 }
			 var eve=e.originalEvent;
			/* var items=eve.clipboardData.items;
			 if(!items||items.length==0){
				 that.ing=false;
				 return false;
			 }
			for(var i in items){
				var item=items[i];
				console.log(item)
				if(item&&item.kind == "file"&&(item.type.match(/^image/))){
					if(item.type.indexOf("png")!=-1||item.type.indexOf("jpg")!=-1||item.type.indexOf("gif")!=-1){
						if(that.ing==false){
							return false;
						}
						that.changeToBolbDataUpload(editor,item);
					}
				}
			}*/
			 
			 var files=eve.clipboardData.files;
			 if(!files||files.length==0){
				 that.ing=false;
				 return false;
			 }
			 eve.stopPropagation();
			 eve.preventDefault();
			 var len = files.length;
      		 var uploadTo = editor.data("upload-to")||"local";
      		that.ing=true;
      		if(uploadTo == "local"){
      			 var file1;
     			  for(var i=0;i<len;i++){
     				 file1=files[i];
     				if(that.ing==false||!(file1.type.match(/^image/))){
     					return false;
     				}
     				if(file1.size/1024>maxsize){
     					that.ing=false; 
     					LayerMsgBox.alert("图片文件不能大于"+maxsize+"k",2);
     					return false;
     				}
     				that.sendSummernoteFile(editor,file1);
     			  }
     		  }else if(uploadTo == "qiniu"){
     			  that.sendSummernoteFileToQiniu(editor,files);
     		  }else{
     			  LayerMsgBox.alert("暂不支持上传"+uploadTo,2);
     		  }
      		that.ing=false;
      		 
//			for(var i =0;i<flen;i++){
//				var file=files[i];
//				if(that.ing==false||!(file.type.match(/^image/))){
//					return false;
//				}
//				if(file.size/1024>maxsize){
//		    		that.ing=false; 
//		    		LayerMsgBox.alert("剪贴板中图片文件不能大于"+maxsize+"k",2);
//		    		return false;
//				}
//				
//				that.sendSummernoteFile(editor,file);
//			}
			
		}/*,changeToBolbDataUpload:function(editor,file){
			    var that=this;
			    that.ing=true; 
			    var reader = new FileReader();
			    // 读取文件后将其显示在网页中
			    reader.onloadend = function(){
			    	var dataURI=this.result;
			    	var blob=dataURItoBlob(dataURI);
			    	if(blob.size/1024>200){
			    		that.ing=false; 
			    		alert("剪贴板中图片文件不能大于200k");
			    	}else{
			    		that.sendSummernoteFile(editor,blob);
			    	}
			    };
			    // 读取文件
			    reader.readAsDataURL( file );
		}*/,sendSummernoteFileToQiniu:function(editor,fileDatas){
			loadJBoltPlugin(['qiniu'], function(){
	      	  uploadFileToQiniu(editor,"img",fileDatas,null,null,null,true,null,editor.data("upload-loading"),function(editorObj,type,res){
					for(var i in res.data){
	      			  editor.summernote('insertImage',res.data[i].url);
	      		  }
	      	  });
			});
    },sendSummernoteFile:function(editor,file){
			var that=this;
			that.ing=true;
			var imgUploadUrl=editor.data("imguploadurl");
			if(!imgUploadUrl){
				imgUploadUrl=summernote_img_uploadurl;
			}else{
				imgUploadUrl=actionUrl(imgUploadUrl);
			}
			var fileInputName=editor.data("fileinputname");
			if(!fileInputName){
				fileInputName="file";
			}
			  var imghost=editor.data("imghost");
			  var timeout = editor.data("timeout")||60000;
			  var fd = new FormData();
			    fd.append(fileInputName, file);
			    $.ajax({
			        type:"post",
			        url: imgUploadUrl,
			        data: fd,
			        timeout : timeout, //超时时间设置，单位毫秒
			        cache:false, 
			        async:true, 
			        processData: false,
			        contentType: false,
			        success:function (res) {
			        	
			        	if(res.state=="ok"){
			        		if(res.data){
			        			if(res.data.indexOf("http://")!=-1||res.data.indexOf("https://")!=-1||res.data.indexOf("://")!=-1){
			        				editor.summernote('insertImage',res.data);
			        			}else{
			        				var imgUrl=actionUrl(res.data);
			        				if(imghost){
			        					if(imghost.charAt(imghost.length-1)!='/'){
			        						imghost=imghost+"/";
			        					}
			        					imgUrl=imghost+imgUrl;
			        				}
			        				editor.summernote('insertImage', imgUrl);
			        			}
			        			LayerMsgBox.success("上传成功",1000);
			        		}else{
			        			LayerMsgBox.success("上传异常",1000);
			        		}
			        	}else{
			        		LayerMsgBox.error(res.msg,1000);
			        	}
			        	
			        	that.ing=false;
			        	
			        },
			        error:function (err) {
			        	that.ing=false;
			        	LayerMsgBox.error("网络异常",1000);
			        }
			    });
			
		}
}
/**
 * radio工具类封装
 */
var RadioUtil={
		initRadioEvent:function(r,name,handler){
			  if(handler){
				  var exe_handler=eval(handler);
				  if(exe_handler&&typeof(exe_handler)=="function"){
					 r.find("input[type='radio'][name='"+name+"']").unbind("click").on("click",function(e){
						  //e.preventDefault();
						  //e.stopPropagation();
						  exe_handler(r,this.value);
					  });
				  }
			  }
		},
		initRadios:function(radios){
			if(!isOk(radios)){return false;}
			 var len=radios.length;
			 for(var i=0;i<len;i++){
				 this.initRadio(radios.eq(i));
			 }
		},
		initRadio:function(r){
			var that=this,
			  value=r.data("value")+"",
			  defaultValue=r.data("default")+"",
			  name=r.data("name"),
			  handler=r.data("handler"),
			  url=r.data("url"),
			  label=r.data("label");

//			  if(!value){value="";}else{value=value+""}
//			  if(!defaultValue){defaultValue="";}else{defaultValue=defaultValue+""}
			  if(url){
				  that.insertDatas(r,url,name,label,function(){
					  that.initRadioEvent(r,name,handler);
					  that.setChecked(r,name,value,defaultValue);
					  that.processDisabled(r);
					  
				  });
			  }else{
				  var hasInline=r.find(".radio-inline");
				  if(hasInline){
					  //处理样式
					  that.processRadioAlignAndWidth(r);
				  }
				  that.initRadioEvent(r,name,handler);
				  that.setChecked(r,name,value,defaultValue);
				  that.processDisabled(r);
			  }
		},processDisabled:function(r){
			var hasDisabled=r[0].hasAttribute("data-disabled");
			if(hasDisabled){
				var disabled=r.data("disabled");
				if(!(typeof(disabled)=="undefined")&&(disabled+"")!="false"){
					var name=r.data("name");
					var input=r.find("input[type='radio'][name='"+name+"']");
					input.attr("disabled","disabled");
				}
			}
			
		},
		  init:function(parentEle){
			  var parent=getRealParentJqueryObject(parentEle);
			  if(!isOk(parent)){return false;}
			  var radios=parent.find("[data-radio]");
			  if(!isOk(radios)){return false;}
			  this.initRadios(radios);
			
		  },
		  refresh:function(ele){
			if(!ele){return false;}
			var rbox,eleObj;
			if(isDOM(ele)){
				if(ele.hasAttribute("data-radio")){
					rbox=$(ele);
				}else{
					eleObj=$(ele);
					rbox=eleObj.closest("[data-radio]");
					if(ele.hasAttribute("tooltip")){
						eleObj.tooltip("hide");
					}
				}
			}else{
				var type=typeof(ele);
				if(type=="string"){
					if(ele.startWith("#")){
						rbox=$(ele);
					}else{
						rbox=$("#"+ele);
					}
				}else if(type=='object'){
					if(ele[0].hasAttribute("data-radio")){
						rbox=ele;
					}else{
						rbox=ele.closest("[data-radio]");
					}
				}
			}
			if(!isOk(rbox)){return false;}
			
			this.initRadio(rbox);
			
			
		  },
		  insertDatas:function(r,url,name,label,callback){
			  var that=this;
			  r.empty();
			  
			  var width=r.data("width");
			  var labelWidth="";
			  var radioWidth="";
			  if(width){
				  var arr=width.split(",");
				  labelWidth=arr[0];
				  radioWidth=arr[1];
			  }
			  var html='';
			  if(label){
				  var notNull = r.data("notnull");
				  var radioRule =r.data("rule");
				  var radioIsRequired=(radioRule&&typeof(notNull)=="undefined") || (radioRule && ( (typeof(notNull)=="boolean" && notNull==true) || (typeof(notNull)=="string" && notNull=="true")));
				  if(radioIsRequired && radioRule=="required"){
					  r.data("rule","radio").attr("data-rule","radio");
				  }
				 if(r.hasClass("row")){
					 if(labelWidth.indexOf("px")!=-1){
						 html= '<label class="col-auto col-form-label '+(radioIsRequired?"is_required":"")+'" style="width:'+labelWidth+'">'+label+'</label>';
					 }else{
						 html= '<label class="'+labelWidth+' col-form-label '+(radioIsRequired?"is_required":"")+'">'+label+'</label>';
					 }
				 }else{
					 if(labelWidth.indexOf("px")!=-1){
						 html= '<label class="col-auto '+(radioIsRequired?"is_required":"")+'" style="width:'+labelWidth+'">'+label+'</label>';
					 }else{
						 html= '<label class="'+labelWidth+' '+(radioIsRequired?"is_required":"")+'">'+label+'</label>';
					 }
				 }
				 r.append(html);
				 html='';
			  }
			  
			  var inline="";
			  var isInline=r.data("inline");
			  if(isInline){
				  inline="radio-inline";
			  }
			  var text_attr=r.data("text-attr");
	      		if(!text_attr){
	      			text_attr="text";
	      		}
	      		
	      		var value_attr=r.data("value-attr");
	      		if(!value_attr){
	      			value_attr="value";
	      		}
	      		var delimiter=r.data("delimiter");
	      		var textFormatHandler=r.data("text-format-handler");
	      		var paddingLeft = 15;
	      		if(!r.hasClass("row")){
	      			paddingLeft = 6;
	      		}
				Ajax.get(url,function(res){
					html+='<div class="'+radioWidth+'"  style="padding-top: 1px;padding-left:'+paddingLeft+'px">';
  					var list=res.data;
  					var nodotname=name.replace("\\.","_");
  					if(list&&list.length>0){
						var optionItem,text,value,therRadioId,ranid=randomId();
						if(r.data("text")){
							var firstValue=r.data("value");
							if(typeof(firstValue)=="undefined"){
								firstValue='';
							}
							therRadioId=nodotname+"_"+ranid+"_all";
							html+='<div class="radio radio-primary '+inline+'">'+
							'<input  id="'+therRadioId+'" type="radio" name="'+name+'" value="'+firstValue+'"/>'+
							'<label for="'+therRadioId+'">'+r.data("text")+'</label>'+
							'</div>';
						}
  						for(var i in list){
  							optionItem=list[i];
  							text=processOptionTextByFormatHandler(textFormatHandler,optionItem,text_attr,delimiter);
  							value=processOptionValue(optionItem,value_attr);
  							therRadioId=nodotname+"_"+ranid+"_"+i;
							html+='<div class="radio radio-primary '+inline+'">'+
  								'<input  id="'+therRadioId+'" type="radio" name="'+name+'" value="'+value+'"/>'+
  									'<label for="'+therRadioId+'">'+text+'</label>'+
  								'</div>';
  	  					}
  						html+="</div>";
  						r.append(html);
  						if(isInline){
  							//处理样式
  							that.processRadioAlignAndWidth(r);
  						}
  						if(callback){
  							callback();
  						}
  					}else{
  						html+="<span class='form-control-plaintext text-info'>[暂无可选项]</span></div>";
  						r.append(html);
  					}
				});
			  
		  },
		  processRadioAlignAndWidth:function(r){
			  var alignLeft=r.data("align-left");
			  if(alignLeft){
					  var rs=r.find(".radio.radio-inline");
					  if(isOk(rs)){
						  var max=0,w;
						  rs.each(function(){
							  w=$(this).outerWidth();
							  if(w>max){
								  max=w;
							  }
						  });
						  rs.css({"width":max+"px","max-width":max+"px"});
					  }
			  }
		  
		  },
		  setChecked:function(parentEle,name,value,defaultValue){
			  var parent = getRealParentJqueryObject(parentEle);
			  if(typeof(value)!="undefined"&&value!="undefined"&&value.toString().length>0){
				  value=value+'';
			  }else{
				  value='';
			  }
			if(typeof(defaultValue)!="undefined"&&defaultValue!="undefined"&&defaultValue.toString().length>0){
				defaultValue=defaultValue+'';
			}else{
				defaultValue='';
			}
			  if(value){
				  parent.find("input[type='radio'][name='"+name+"'][value='"+value+"']").click();
			  }else{
				  if(defaultValue){
					  if(defaultValue=="all"){
						  parent.find("input[type='radio'][name='"+name+"'][data-all]").click();
					  }else if(defaultValue=="options_first"){
						  parent.find("input[type='radio'][name='"+name+"']:first").click();
					  }else if(defaultValue=="options_last"){
						  parent.find("input[type='radio'][name='"+name+"']:last").click();
					  }else{
						  parent.find("input[type='radio'][name='"+name+"'][value='"+defaultValue+"']").click();
					  }
					 
				  }
			  }
		  },getCheckedRadio:function(name,parentEle){
			  var checkedRadio;
				 if(parentEle){
					 var parent=getRealParentJqueryObject(parentEle);
					 if(isOk(parent)){
						 checkedRadio=parent.find("input[type='radio'][name='"+name+"']:checked");
					 }
				 }else{
					 checkedRadio=jboltBody.find("input[type='radio'][name='"+name+"']:checked");
				 }
				 return checkedRadio;
		  },getCheckedValue:function(name,parentEle){
			 var that=this,checkRadiovalue;
			 var checkedRadio=that.getCheckedRadio(name,parentEle);
			 if(isOk(checkedRadio)){
				 checkRadiovalue=checkedRadio.val();
			 }
			 return checkRadiovalue;
		  },getCheckedText:function(name,parentEle){
				 var that=this,checkRadioText;
				 var checkedRadio=that.getCheckedRadio(name,parentEle);
				 if(isOk(checkedRadio)){
					 var label=checkedRadio.parent().find("label");
					 if(isOk(label)){
						 checkRadioText=label.text();
					 }
				 }
				 return checkRadioText;
			  }
}


//弹出tips
var LayerTipsUtil={
		initTip:function(tip){
			 var trigger=tip.data("trigger");
			 if(trigger&&trigger=="click"){
				 var tipsIndex=0;
				 tip.off("click").on("click",function(e){
					 e.stopPropagation();
					 e.preventDefault();
					  var tipsMsg=$(this).data("content");
					  tipsIndex=layer.tips(tipsMsg, this, {
						  tips: [4, '#3595CC'],
						  time: 30000
						});
					  
					  $("#layui-layer"+tipsIndex).on("click",function(e){
							 e.stopPropagation();
							 e.preventDefault();
						 });
						 jboltBody.on("click",function(){
							  layer.close(tipsIndex);
						 });
						 
				  });
				
				 
			 }else{
				 var tipsIndex=0;
				 tip.off("mouseenter").on("mouseenter",function(){
					  var tipsMsg=$(this).data("content");
					  tipsIndex=layer.tips(tipsMsg, this, {
						  tips: [4, '#3595CC'],
						  time: 30000
						});
				  }).off("mouseleave").on("mouseleave",function(){
					  layer.close(tipsIndex);
				  });
			 }
		  
		},
		initTips:function(tips){
			if(!isOk(tips)){return false;}
			var that=this;
			var len=tips.length;
			for(var i=0;i<len;i++){
				that.initTip(tips.eq(i));
			}
		},
		  init:function(parentEle){
			  var parent=getRealParentJqueryObject(parentEle);
			  if(!isOk(parent)){return false;}
			  var tips=parent.find("[data-tipsbtn]");
			  if(!isOk(tips)){return false;}
			  this.initTips(tips);
		  }
}

/**
 * layerPhoto弹出层组件工具类
 */
var LayerPhotoUtil={
		init:function(){
			 var that=this;
			 jboltBody.on("click","[data-photobtn]",function(e){
					  e.preventDefault();
					  e.stopPropagation();
					  var btn= $(this);
					  var url=null;
					  var target=btn.data("target");
					  if(this.tagName.toLowerCase()=="a"){
						  url=btn.attr("href");
					  }else if(this.tagName.toLowerCase()=="img"){
						url=btn.attr("src");
					}
					if(!url){
						url=btn.data("url");
					}
					if(url){
						var datas=null;
						var album=btn.data("album")||btn.data("ablum");
						if(album){
							datas=that.getalbum(album,url);
							if(!datas){
								datas=[{
						    	      "src":url, //原图地址
					    	    }];
							}
						}else{
							datas=[{
					    	      "src":url, //原图地址
				    	    }];
						}
						var layerPhotoptions={
							    photos: {
							    	  "title": "JBolt图片查看器", 
							    	  "start": 0, //初始显示的图片序号，默认0
							    	  "data": datas
							    	}
							    ,anim: 5 //0-6的选择，指定弹出图片动画类型，默认随机（请注意，3.0之前的版本用shift参数）
							  };
						if(target&&target=="parent"){
							parent.layer.photos(layerPhotoptions);
						}else{
							layer.photos(layerPhotoptions);
						}
						 
					}else{
						alert("页面存在未设置图片地址的 photobtn");
					}
					 
				  });
		},
		getalbum:function(album,myurl){
			 var photoBtns=jboltBody.find("[data-photobtn][data-album='"+album+"'],[data-photobtn][data-ablum='"+album+"']");
			 if(!isOk(photoBtns)){return null;}
			 
			 var psUrls=new Array();
			 psUrls.push({src:myurl});
			 photoBtns.each(function(){
				 var btn= $(this);
				  var url=null;
				  if(this.tagName.toLowerCase()=="a"){
					  url=btn.attr("href");
				  }else if(this.tagName.toLowerCase()=="img"){
					url=btn.attr("src");
					}
					if(!url){
						url=btn.data("url");
					}
					if(url!=myurl){
						psUrls.push({src:url});
					}
			 });
			 
			 return psUrls;
		}
}
/**
 * 自动Ajax加载内容的Portal
 */
;(function($){
		$.extend($.fn, {
			ajaxPortal:function(insertTypeStr,url,replaceOldUrl,callback,failcallback){
				return this.each(function(){
					var portal=$(this);
					var l_url="";
					if(url){
						l_url=url;
					}else{
						l_url=portal.data("url");
					}
					if(l_url.indexOf("?")!=-1){
						l_url=l_url+"&t="+new Date().getTime();
					}else{
						l_url=l_url+"?t="+new Date().getTime();
					}
					var autoload=portal.data("autoload");
					var triggerload=portal.data("triggerload");
					if(typeof(autoload)=="undefined"){
						autoload=true;
					}
					if(!autoload && triggerload){
						autoload=true;
					}
					var insertType="replace";
					if(typeof(insertTypeStr)=="boolean"){
						if(insertTypeStr){
							insertType="replace";
						}else{
							insertType="append";
						}
					}else if(insertTypeStr=="prepend"){
						insertType="prepend";
					}else if(insertTypeStr=="append"){
						insertType="append";
					}else if(insertTypeStr=="replace"){
						insertType="replace";
					}
					if(autoload){
						switch (insertType) {
						case "prepend":
							portal.prepend("<div class='m-2 p-2 text-center ajaxPortalLoading'><div class='spinner-grow text-secondary' style='width: 3rem; height: 3rem;' role='status'><span class='sr-only'>Loading...</span></div></div>");
							break;
						case "append":
							portal.append("<div class='m-2 p-2 text-center ajaxPortalLoading'><div class='spinner-grow text-secondary' style='width: 3rem; height: 3rem;' role='status'><span class='sr-only'>Loading...</span></div></div>");
							break;
						case "replace":
							AjaxPortalUtil.triggerPortalJBoltPageCloseHandler(portal);
							portal.html("<div class='m-2 p-2 text-center ajaxPortalLoading'><div class='spinner-grow text-secondary' style='width: 3rem; height: 3rem;' role='status'><span class='sr-only'>Loading...</span></div></div>");
							break;
						default:
							portal.html("<div class='m-2 p-2 text-center ajaxPortalLoading'><div class='spinner-grow text-secondary' style='width: 3rem; height: 3rem;' role='status'><span class='sr-only'>Loading...</span></div></div>");
							break;
						}
						l_url=actionUrl(l_url);
						if(l_url){
							l_url=processEleUrlByLinkOtherParamEle(portal,l_url);
							if(!l_url){
								console.error("AjaxPortal data-link-para-ele配置无法处理URL");
								return false;
							}

							var formId = portal.data("conditions-form");
							if(formId){
								var form=$("#"+formId);
								if(!isOk(form)){
									LayerMsgBox.alert("ajaxPortal绑定data-conditions-form未找到",2);
									return;
								}
								l_url = urlWithFormData(l_url,form);
							}
						}
						var dataType=portal.data("type");
						if(dataType&&dataType=="json"){
							var tpl=portal.data("tpl");
							if(tpl){
								var tplObj=g(tpl);
								var tplc=null;
								if(tplObj.tagName == "SCRIPT"){
									tplc = tplObj.innerHTML;
								}else if(tplObj.tagName == "TEXTAREA"){
									tplc = tplObj.value;
								}else{
									LayerMsgBox.alert("AjaxPortal配置data-tpl模板指定的组件只能是Script和Textarea",2);
									return false;
								}
								if(!tplc){
									LayerMsgBox.alert("AjaxPortal配置data-tpl模板中未发现可用模板数据",2);
									return false;
								}
								
								if(tplc){

									Ajax.get(l_url,function(res){
										var html = juicer(tplc,{res:res});
										AjaxPortalUtil.processContent(portal,html,res,url,replaceOldUrl,insertType,callback);
										if(isOk(portal.pages)==false){
											AjaxPortalUtil.initPage(portal,res.data);
										}
									},function(xhr){
										 if(xhr.responseText){
											 portal.find(".ajaxPortalLoading").remove();
											 LayerMsgBox.alert(xhr.responseText,2);
										 }else{
											 AjaxPortalUtil.processError(portal,insertType,failcallback);
										 }
									});
								}
							}
							
						}else{
							$.ajax({
								  type:"GET",
								  url: l_url,
								  beforeSend:function(xhr) {
				                        xhr.setRequestHeader("AJAX-PORTAL","true");
				                  },
								  dataType: "html",
								  success:function(html){
									  AjaxPortalUtil.processContent(portal,html,null,url,replaceOldUrl,insertType,callback);
								  },error:function(xhr){
									  if(xhr.responseText){
										  	 AjaxPortalUtil.processContent(portal,xhr.responseText,null,url,replaceOldUrl,insertType,callback);
										 }else{
											 AjaxPortalUtil.processError(portal,insertType,failcallback);
										 }
								  }
								});
						}
						
					
					}
				});
			}
		});
	})(jQuery);
/**
 * ajaxPortal后执行的初始化
 * @param portalId
 * @returns
 */
function afterAjaxPortal(portal){
	portalLoadRequirePluginAndInit(portal);
	$('.tooltip.show').remove();
	SelectUtil.initAutoSetValue(portal);
	FileUploadUtil.init(portal);
	ImgUploadUtil.init(portal);
	SwitchBtnUtil.init(portal);
	FormDate.init(portal);
	CityPickerUtil.init(portal);
	LayerTipsUtil.init(portal);
	HtmlEditorUtil.init(portal);
	ImageViewerUtil.init(portal);
	RadioUtil.init(portal);
	CheckboxUtil.init(portal);
	AutocompleteUtil.init(portal);
	Select2Util.init(portal);
	SelectUtil.init({parent:portal});
	MasterSlaveUtil.init(portal);
	MultipleFileInputUtil.init(portal);
	AjaxPortalUtil.init(portal);
	JSTreeUtil.init(portal);
	JBoltInputUtil.init(portal);
	RangeSliderUtil.init(portal);
	JBoltInputWithClearBtnUtil.init(portal);
	JBoltInputWithCalculatorUtil.init(portal);
	initToolTip(portal);
	initPopover(portal);
	JBoltTableUtil.init(portal);
	JBoltTreeTableUtil.init(portal);
	JBoltTabViewUtil.initUI(portal);
	TextareaUtil.initUI(portal);
	LayerMsgBox.closeLoadNow();
	//处理自动刷新
	processAutoRefreshTabContent(portal);
	findRequiredAndStarIt(portal);
}


//处理自动刷新设置
function processAutoRefreshTabContent(portal){
//	var withTabs=isWithtabs();
	if(!jboltWithTabs){
		return false;
	}
	if(!(portal.hasClass("jbolt_tabcontent")&&portal.hasClass("active"))){
		return false;
	}
	var jboltPage=portal.find(".jbolt_page");
	if(!isOk(jboltPage)){
		return false;
	}
	var autoRefresh=jboltPage.data("auto-refresh");
	if(autoRefresh){
		autoRefresh=autoRefresh*1000;
		setTimeout(function(){
			portal.ajaxPortal(true);
		}, autoRefresh);
	}
}
/**
 * 判断是文件File数据
 * @param obj
 * @returns
 */
function isFileData(obj){
	return obj&&obj.constructor==File;
}
/**
 * 检测文件大小
 * @param file 
 * @param maxSize kb单位
 * @returns
 */
function validateFileMaxSize(file,maxSize){
	var fileSize=0;
	if(isFileData(file)){
		fileSize = file.size;
	}else{
		fileSize=file.files[0].size;
	}
	var gt=(fileSize>maxSize);
	fileSize=(fileSize/1024).toFixed(1);
	  var formateSize=fileSize+"KB";
	  if(fileSize>1024){
		  formateSize=((fileSize/1024).toFixed(1))+"M";
	  }

	  if(gt){
		  var maxText = "";
		  var mmx=(maxSize/1024).toFixed(1);
		  if(mmx>1024){
			  mmx = (maxSize/1024/1024).toFixed(1);
			  maxText = mmx+"MB";
		  }else{
			  maxText = mmx+"KB";
		  }

		  LayerMsgBox.alert("您选择的文件["+formateSize+"],上传限制不能超过:"+maxText,2);}
    return gt;
}

//限制上传文件的类型和大小
function validateExcel(file,maxSize){
	  // 返回 KB，保留小数点后两位
	  var fileName;
	  if(isFileData(file)){
			fileName = file.name;
		}else{
			fileName = file.files[0].name;
	  }
	  if(!/.(xls|xlsx)$/.test(fileName.toLowerCase())){
		  LayerMsgBox.alert("文件类型必须是xls,xlsx中的一种",2);
		  return false;
	  }
	  if(validateFileMaxSize(file,maxSize)){
		  return false;
	  }
	  return true;
}
//限制上传文件的类型和大小
function validateNormal(file,maxSize){
	// 返回 KB，保留小数点后两位
	var fileName;
	  if(isFileData(file)){
			fileName = file.name;
		}else{
			fileName = file.files[0].name;
	  }
	if(!/.(txt|xls|xlsx|jpg|jpeg|png|gif|bmp|rar|zip|pdf|mp4|wmv|flv|rmvb|mpg|mkv|mov|mp3|wav|ogg|midi|mac|acc|doc|docx|ppt|pptx|ppts)$/.test(fileName.toLowerCase())){
		LayerMsgBox.alert("此文件类型不允许上传",2);
		return false;
	}
	if(validateFileMaxSize(file,maxSize)){
		return false;
	}
	return true;
}

//限制上传文件的类型和大小
function validateAudio(file,maxSize){
	// 返回 KB，保留小数点后两位
	var fileName;
	  if(isFileData(file)){
			fileName = file.name;
		}else{
			fileName = file.files[0].name;
	  }
	if(!/.(mp3|wav|ogg|midi|mac|acc)$/.test(fileName.toLowerCase())){
		LayerMsgBox.alert("此文件类型不允许上传，只允许mp3|wav|ogg|midi|mac|acc",2);
		return false;
	}
	if(validateFileMaxSize(file,maxSize)){
		return false;
	}
	return true;
}
//限制上传文件的类型和大小
function validateVideo(file,maxSize){
	// 返回 KB，保留小数点后两位
	var fileName;
	  if(isFileData(file)){
			fileName = file.name;
		}else{
			fileName = file.files[0].name;
	  }
	if(!/.(mp4|wmv|flv|rmvb|mpg|mkv|mov)$/.test(fileName.toLowerCase())){
		LayerMsgBox.alert("此文件类型不允许上传,只允许mp4|wmv|flv|rmvb|mpg|mkv|mov",2);
		return false;
	}
	if(validateFileMaxSize(file,maxSize)){
		return false;
	}
	return true;
}
//限制上传文件的类型和大小
function validateSelfFileAccept(file,maxSize,accept){
	  // 返回 KB，保留小数点后两位
	var fileName;
	  if(isFileData(file)){
			fileName = file.name;
		}else{
			fileName = file.files[0].name;
	  }
	  var tt=eval("/.("+accept+")$/");
	  if(!tt.test(fileName.toLowerCase())){
		  LayerMsgBox.alert("此文件类型不允许上传,只允许"+accept,2);
		  return false;
	  }
	  if(validateFileMaxSize(file,maxSize)){
		  return false;
	  }
	  return true;
}
/**
 * 判断是不是img
 */
function isImg(fileName){
	return (/.(jpg|jpeg|png|gif|bmp|webp)$/.test(fileName.toLowerCase()));
}

/**
 * 判断是不是pdf
 */
function isPdf(fileName){
	return (/.(pdf)$/.test(fileName.toLowerCase()));
}
//限制上传文件的类型和大小
function validateImg(file,maxSize){
    // 返回 KB，保留小数点后两位
	var fileName;
	if(isFileData(file)){
		fileName = file.name;
	}else{
		fileName = file.files[0].name;
	}
    if(isImg(fileName)==false){
  	  	 LayerMsgBox.alert("图片类型必须是jpg|jpeg|png|gif|bmp|webp中的一种",2);
           return false;
     }
    if(validateFileMaxSize(file,maxSize)){
  	  return false;
    }
    return true;
}

function getRealAccept(accept){
	var values;
	switch(accept){
	case "img":
		values="image/*";
		break;
	case "csv":
		values="text/csv";
		break;
	case "excel":
		values="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.ms-excel";
		break;
	case "xls":
		values="application/vnd.ms-excel";
		break;
	case "xlsx":
		values="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		break;
	case "word":
		values="application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document";
		break;
	case "doc":
		values="application/msword";
		break;
	case "docx":
		values="application/vnd.openxmlformats-officedocument.wordprocessingml.document";
		break;
	case "powerpoint":
		values=".ppt,.pptx,.pps,.ppsx";
		break;
	case "ppt":
		values=".ppt";
		break;
	case "pptx":
		values=".pptx";
		break;
	case "pps":
		values=".pps";
		break;
	case "ppsx":
		values=".ppsx";
		break;
	case "video":
		values="video/*";
		break;
	case "audio":
		values="audio/*";
		break;
	case "pdf":
		values="application/pdf";
		break;
	case "mp4":
		values="video/mp4";
		break;
	case "amp4":
		values="audio/mp4";
		break;
	case "flv":
		values=".flv";
		break;
	case "mp3":
		values="audio/mp3";
		break;
	case "wav":
		values="audio/wav";
		break;
	case "zip":
		values="application/zip";
		break;
	case "rar":
		values=".rar";
		break;
	case "7z":
		values=".7z";
		break;
	default:
		if(accept.indexOf(",")==-1){
			values=accept;
		}else{
			values=getRealAcceptSplit(accept);
		}
		break;
}

return values;
}
function getRealAcceptSplit(accept){
	if(accept.indexOf(",") ==-1){
		return accept;
	}
	var arrs=accept.split(",");

	if(notOk(arrs)){
		return accept;
	}
	var rets=[];
	var s;
	for(var i in arrs){
		s = getRealAccept(arrs[i]);
		if(s){
			rets.push(s);
		}
	}

	if(notOk(rets)){
		return accept;
	}

	return rets.join(",");

}
/**
 * 批量 验证files
 * @param files
 * @param accept
 * @param maxSize
 * @returns
 */
function validateFileDatas(fileDatas,accept,maxSize){
	if(!fileDatas||fileDatas.length==0){
		return false;
	}
	var passValidate=true;
	
	$.each(fileDatas,function(i,fileData){
		passValidate = validateFile(fileData,accept,maxSize);
		if(!passValidate){
			return false;
		}
	});
	
	return passValidate;
	
}
/**
 * 验证file
 * @param file
 * @param accept
 * @param maxSize
 * @returns
 */
function validateFile(file,accept,maxSize){
	  	  var ele;
		  if(isFileData(file)){
			    ele = file;
			}else{
				ele = file[0].files[0];
		  }
		//默认10M
		if(maxSize){
			maxSize = maxSize * 1024;
		}else{
			maxSize = 10485760;
		}
	  	var passValidate=true;
	  	if(accept){
			switch (accept) {
			case "img":
				passValidate=validateImg(ele,maxSize);
				break;
			case "excel":
				passValidate=validateExcel(ele,maxSize);
				break;
			case "file":
				passValidate=validateNormal(ele,maxSize);
				break;
			case "all":
				passValidate=!validateFileMaxSize(ele,maxSize);
				break;
			case "video":
				passValidate=validateVideo(ele,maxSize);
				break;
			case "audio":
				passValidate=validateAudio(ele,maxSize);
				break;
			default:
				passValidate=validateSelfFileAccept(ele,maxSize,accept);
				break;
			}
		}else{
			passValidate=validateNormal(ele,maxSize);
		}
	  	return passValidate;
}



//建立一個可存取到該file的url
function getObjectURL(ele) {
	return getObjectURLByFileData(ele.files[0]) ;
}
function getObjectURLByFileData(fileData) {
	  var url = null ;
	  if (window.createObjectURL!=undefined) { // basic
		  url = window.createObjectURL(fileData);
	  } else if (window.URL!=undefined) { // mozilla(firefox)
		  url = window.URL.createObjectURL(fileData);
	  } else if (window.webkitURL!=undefined) { // webkit or chrome
		  url = window.webkitURL.createObjectURL(fileData) ;
	  }
	  return url ;
}

//专门图片上传组件
var ImgUploadUtil={
		  tpl:'<p class="j_img_uploder_msg"><span class="j_file_name"></span><i class="fa fa-remove j_text_danger j_remove_file"></i></p>',
		  input_tpl:'<div class="j_upload_img_input"><input id="${inputId}" data-boxid="${boxId}" type="file" {@if multi}multiple="multiple"{@/if} accept="image/*"  {@if rule}data-rule="${rule}"{@/if}  {@if tips}data-tips="${tips}"{@/if} ></div>',
		  init:function(parentEle){
				var parent=getRealParentJqueryObject(parentEle);
				if(!isOk(parent)){return false;}
				var imgBoxs=parent.find(".j_img_uploder,[data-imguploader]:not(.j_img_uploder)");
				if(!isOk(imgBoxs)){return false;}
				var len=imgBoxs.length;
				for(var i=0;i<len;i++){
					this.initSingle(imgBoxs.eq(i));
				}
				
		  },
		  initSingle:function(box){
			  if(!box.hasClass("j_img_uploder")){
				  box.addClass("j_img_uploder");
			  }
			  	var that=this;
			    var rule=box.data("rule");
				var tips=box.data("tips");
				var area=box.data("area");
				var multi=box.data("multi")||false;
				if(area){
					var arr=area.split(",");
					box.css({
						"width":arr[0],
						"height":arr[1]
					})
				}
				var boxId=box.attr("id");
				var ranId=randomId();
				var inputId="ipt_"+ranId;
				if(!boxId){
					boxId="box_"+ranId;
					box.attr("id",boxId);
					box.data("inputid",inputId).attr("data-inputid",inputId);
				}
				box.html(juicer(that.tpl,{}));
				jboltBody.append(juicer(that.input_tpl,{boxId:boxId,inputId:inputId,rule:rule,tips:tips,multi:multi}));
				var value=box.data("value");
				if(value&&typeof(value)=="string"&&value!="assets/img/uploadimg.png"){
					if(value.indexOf("\\")!=-1){
						value=value.replace(/\\/g,"/");
					}
					var bg="#999 url('"+value+"') center center no-repeat";
					box.css({
						"background":bg,
						"background-size":"100%"
					});
					box.find("p.j_img_uploder_msg").show();
				}
				
				box.off("click").on("click",function(e){
					e.preventDefault();
					e.stopPropagation();
					var innerInputId=$(this).data("inputid");
					var fileInput=jboltBody.find(".j_upload_img_input>input#"+innerInputId);
					if(isOk(fileInput)){
						fileInput.trigger("click");
					}
					return false;
				});
					
					// onchange事件
					jboltBody.find("input[type='file']#"+box.data("inputid")).off("change").on("change",function(event){
					var fileInput=$(this);
					 var files = event.target.files; 
					 if(files.length>0){
						 if(multi){
							 that.changeFiles(box,fileInput,files);
						 }else{
							 that.changeFile(box,fileInput,files[0]);
						 }
					 }
				});
				box.find(".j_img_uploder_msg").on("click",function(e){
					e.preventDefault();
					e.stopPropagation();
					return false;
				});	
				box.find(".j_remove_file").on("click",function(e){
					e.preventDefault();
					e.stopPropagation();
					var removefile=$(this);
					that.removeFile(removefile);
					return false;
				});
				
				
				if(multi){
					var hiddeninput=box.data("hidden-input")||box.data("hiddeninput");
					if(!hiddeninput){
						return;
					}
					var imgbox=box.data("imgbox");
					if(!imgbox){
						return;
					}
					var hiddenEle=$("#"+hiddeninput);
					if(!isOk(hiddenEle)){
						return;
					}
					var imgboxEle=$("#"+imgbox);
					if(!isOk(imgboxEle)){
						return;
					}
					var hiddenValues=$.trim(hiddenEle.val());
					if(!hiddenValues){
						return;
					}

					var valueAttr=box.data("value-attr");

					if(valueAttr && valueAttr=="id"){
						var loadJBoltFileUrl = (box.data("load-jboltfile-url")||"admin/jboltfileChange?ids=")+hiddenValues;
						Ajax.get(loadJBoltFileUrl,function(res){
							var datas = res.data;
							if(datas){
								if(!isArray(datas)){
									datas = [datas];
								}
								imgboxEle.empty();
								var imgstyle=imgboxEle.data("imgstyle")||"";
								$.each(datas,function(i,jboltFile){
									imgboxEle.append("<li data-imgbox='"+imgbox+"' data-remove-file='"+jboltFile.id+"' data-hiddeninput='"+hiddeninput+"'><img data-photobtn data-album='"+imgbox+"' style='"+imgstyle+"' src='"+jboltFile.localUrl+"'/><div class='optbox' ><i title='查看大图' class='fa fa-eye' onclick='lookUploadImgPhoto(this)'></i><i title='删除' onclick='removeUploadImgBoxLi(this)' class='fa fa-trash'></i><i onclick='imgGotoLeft(this)' class='fa fa-arrow-left' title='左移'></i><i title='右移' onclick='imgGotoRight(this)' class='fa fa-arrow-right'></i></div></li>").show();
								});
							}
						});
					}else{
						var imgs=new Array();
						if(hiddenValues.indexOf(",")==-1){
							imgs.push(hiddenValues);
						}else{
							imgs = hiddenValues.split(",");
						}

						if(!isOk(imgs)){
							return;
						}
						imgboxEle.empty();
						var imgstyle=imgboxEle.data("imgstyle")||"";
						$.each(imgs,function(i,img){
							imgboxEle.append("<li data-imgbox='"+imgbox+"' data-remove-file='"+real_image(img)+"' data-hiddeninput='"+hiddeninput+"'><img data-photobtn data-album='"+imgbox+"' style='"+imgstyle+"' src='"+real_image(img)+"'/><div class='optbox' ><i title='查看大图' class='fa fa-eye' onclick='lookUploadImgPhoto(this)'></i><i title='删除' onclick='removeUploadImgBoxLi(this)' class='fa fa-trash'></i><i onclick='imgGotoLeft(this)' class='fa fa-arrow-left' title='左移'></i><i title='右移' onclick='imgGotoRight(this)' class='fa fa-arrow-right'></i></div></li>").show();
						});
					}
					

					
				}

		  },
		  removeFile:function(removeBtn){
			  var uploder=removeBtn.closest(".j_img_uploder");
			  var removehandler=uploder.data("remove-handler")||uploder.data("removehandler");
			  var removeConfirm=uploder.data("remove-confirm");

			  var rf=function(){
				  var inputId=uploder.data("inputid");
				  if(inputId){
					  var fileInput=jboltBody.find(".j_upload_img_input>input[type='file']#"+inputId);
					  if(isOk(fileInput)){
						  fileInput.val("");
					  }
				  }
				  uploder.find("span.j_file_name").text("");
				  uploder.find("p.j_img_uploder_msg").hide();
				  uploder.css({
					  "background":"url('assets/img/uploadimg.png') center center no-repeat",
					  "background-size":"80%"
				  });
				  var hiddeninput=uploder.data("hidden-input")||uploder.data("hiddeninput");
				  if(hiddeninput){
					  $("#"+hiddeninput).val("");
				  }
				  if(removehandler){
					  var exe=eval(removehandler);
					  if(exe){
						  exe(uploder);
					  }
				  }
			  }
			  var removeUrl = uploder.data("remove-url");
			  if(removeUrl && removeUrl.indexOf("[file]")!=-1){
				  var hiddeninputId=uploder.data("hidden-input")||uploder.data("hiddeninput");
				  if(hiddeninputId){
					  var hiinput = $("#"+hiddeninputId);
					  if(isOk(hiinput)){
						  var hipt_value = hiinput.val();
						  if(hipt_value){
							  removeUrl = removeUrl.replace("[file]",hipt_value);
						  }
					  }
				  }
			  }
			  if(removeConfirm){
				  var cfMsg;
				  if(typeof(removeConfirm)=="string"){
					  cfMsg = removeConfirm;
				  }else{
					  cfMsg = "确认移除已选图片？";
				  }
				  LayerMsgBox.confirm(cfMsg,function(){
					 if(removeUrl){
						 LayerMsgBox.loading("正在删除...",4000);
						 Ajax.get(removeUrl,function(res){LayerMsgBox.closeLoadingNow();rf();LayerMsgBox.success("删除成功",500)});
					 }else{
						 rf();
					 }
				  });
			  }else{
				  if(removeUrl){
					  LayerMsgBox.loading("正在删除...",4000);
					  Ajax.get(removeUrl,function(res){LayerMsgBox.closeLoadingNow();rf();LayerMsgBox.success("删除成功",500)});
				  }else{
					  rf();
				  }
			  }


		  },
		  changeFiles:function(uploder,fileInput,fileDatas){
				var maxSize=uploder.data("maxsize");
				var fileValue=fileInput.val();
				var handler=uploder.data("handler");
				
				if(fileValue){
					if(!validateFileDatas(fileDatas,"img",maxSize)){
						fileInput.val("");
						return false;
					}
				}else{
					fileInput.val("");
					uploder.find("span.j_file_name").text("");
					uploder.find("p.j_img_uploder_msg").hide();
					uploder.css({
						"background":"url('assets/img/uploadimg_multi.png') center center no-repeat",
						"background-size":"80%"
					});
				}
				if(handler){
					var url=null;
					if(handler=="uploadMultipleFile"){
						url=uploder.data("url");
						if(!url){
							LayerMsgBox.alert("未设置文件上传地址 data-url",2);
							return;
						}
					}
					if(url){
						url = processEleUrlByLinkOtherParamEle(uploder,url);
					}
					LayerMsgBox.loading("处理中",10000);
					var hiddeninputId=uploder.data("hidden-input")||uploder.data("hiddeninput");
					var imgbox=uploder.data("imgbox");
					var limit=uploder.data("limit")||9;
					if(imgbox){
						var imgs=$("#"+imgbox+" img");
						if((isOk(imgs)&&(imgs.length>=limit||(imgs.length+fileDatas.length>limit)))||fileDatas.length>limit){
							LayerMsgBox.alert("最多上传["+limit+"]张图片",5)
							fileInput.val("");
							uploder.find("span.j_file_name").text("");
							uploder.find("p.j_img_uploder_msg").hide();
							uploder.css({
								"background":"url('assets/img/uploadimg_multi.png') center center no-repeat",
								"background-size":"80%"
							});
							return false;
						}
					}
					
					var fileName=uploder.data("filename");
					if(!fileName){
						fileName="file";
					}
					var ucallback=uploder.data("upload-success-handler")||uploder.data("upload-success-callback");
					var failcallback=uploder.data("upload-fail-handler")||uploder.data("upload-fail-callback");
					var hiddeninput;
					if(hiddeninputId){
						hiddeninput=$("#"+hiddeninputId);
						if(isOk(hiddeninput)){
							var timeout=uploder.data("timeout");
							if(timeout){
								hiddeninput.data("timeout",timeout).attr("data-timeout",timeout);
							}
							if(!hiddeninput[0].hasAttribute("data-value-attr")){
								var valueAttr=uploder.data("value-attr");
								if(valueAttr){
									hiddeninput.data("value-attr",valueAttr).attr("data-value-attr",valueAttr);
								}
							}
						}
					}
					var loading=uploder.data("loading");
					
					if(handler=="uploadMultipleFile"){
							uploadFile("img",url,fileName,fileDatas,hiddeninput,null,null,true,imgbox,loading,function(type,res){
								fileInput.val("");
								if(ucallback){
									var execa_handler=eval(ucallback);
									  if(execa_handler&&typeof(execa_handler)=="function"){
										  execa_handler(uploder,type,fileInput,res);
									}
								}
							},function(type,res){
								if(failcallback){
									LayerMsgBox.closeLoadingNow();
									var execa_handler=eval(failcallback);
									if(execa_handler&&typeof(execa_handler)=="function"){
										execa_handler(uploder,type,fileInput,res);
									}
								}else{
									LayerMsgBox.alert(res.msg||"上传失败",2);
								}
							});
					}else if(handler=="uploadMultipleFileToQiniu"){
						loadJBoltPlugin(['qiniu'], function(){
							uploadFileToQiniu(uploder,"img",fileDatas,hiddeninput,null,null,true,imgbox,loading,function(type,res){
								fileInput.val("");
								if(ucallback){
									var execa_handler=eval(ucallback);
									  if(execa_handler&&typeof(execa_handler)=="function"){
										  execa_handler(uploder,type,fileInput,res);
									}
								}
							},function(type,res){
								if(failcallback){
									LayerMsgBox.closeLoadingNow();
									var execa_handler=eval(failcallback);
									if(execa_handler&&typeof(execa_handler)=="function"){
										execa_handler(uploder,type,fileInput,res);
									}
								}else{
									LayerMsgBox.alert(res.msg||"上传失败",2);
								}
							});
						});
					}else{
						var exe_handler=eval(handler);
						  if(exe_handler&&typeof(exe_handler)=="function"){
							  exe_handler(uploder,"img",fileInput);
						}
					}
					
				}
		  
		  },
		  changeFile:function(uploder,fileInput,fileData){
				var maxSize=uploder.data("maxsize");
				var fileValue=fileInput.val();
				var hiddeninput=uploder.data("hidden-input")||uploder.data("hiddeninput");
				var handler=uploder.data("handler");
				if(hiddeninput&&handler&&handler!="uploadMultipleFile"){
					$("#"+hiddeninput).val("");
				}
				
				if(fileValue){
					if(validateFile(fileInput,"img",maxSize)){
						var arr=fileValue.split('\\');
						var fileName=arr[arr.length-1];
						if(handler&&handler!="uploadMultipleFile"){
							uploder.find("span.j_file_name").text(fileName).attr("title",fileName);
							uploder.find("p.j_img_uploder_msg").show();
							
							//出预览图
							var imgDataUrl=getObjectURLByFileData(fileData);
							if(imgDataUrl){
								uploder.css({
									"background":"#999 url('"+imgDataUrl+"') center center no-repeat",
									"background-size":"100%"
								});
							}
						}
						uploder.closest(".form-group").removeClass("has-error");
						
							
					}else{
						fileInput.val("");
						uploder.find("span.j_file_name").text("");
						uploder.find("p.j_img_uploder_msg").hide();
						uploder.css({
							"background":"url('assets/img/uploadimg.png') center center no-repeat",
							"background-size":"80%"
						});
						return false;
					}
				}else{
					fileInput.val("");
					uploder.find("span.j_file_name").text("");
					uploder.find("p.j_img_uploder_msg").hide();
					uploder.css({
						"background":"url('assets/img/uploadimg.png') center center no-repeat",
						"background-size":"80%"
					});
				}
				if(handler){
					var url=uploder.data("url");
					if(!url && handler=="uploadFile"){
						LayerMsgBox.alert("未设置文件上传地址 data-url",2);
						return;
					}
					if(url){
						url=processEleUrlByLinkOtherParamEle(uploder,url);
					}

					LayerMsgBox.loading("处理中",1000);
					var hiddeninputId=uploder.data("hidden-input")||uploder.data("hiddeninput");
					var imgbox=uploder.data("imgbox");
					var limit=uploder.data("limit")||9;
					if(imgbox){
						var imgs=$("#"+imgbox+" img");
						if(isOk(imgs)&&imgs.length>=limit){
							LayerMsgBox.alert("最多上传["+limit+"]张图片",5)
							fileInput.val("");
							uploder.find("span.j_file_name").text("");
							uploder.find("p.j_img_uploder_msg").hide();
							uploder.css({
								"background":"url('assets/img/uploadimg.png') center center no-repeat",
								"background-size":"80%"
							});
							return false;
						}
					}
					
					var fileName=uploder.data("filename");
					if(!fileName){
						fileName="file";
					}
					var ucallback=uploder.data("upload-success-handler")||uploder.data("upload-success-callback");
					var failcallback=uploder.data("upload-fail-handler")||uploder.data("upload-fail-callback");
					var hiddeninput;
					if(hiddeninputId){
						hiddeninput=$("#"+hiddeninputId);
						if(isOk(hiddeninput)){
							var timeout=uploder.data("timeout");
							if(timeout){
								hiddeninput.data("timeout",timeout).attr("data-timeout",timeout);
							}
							if(!hiddeninput[0].hasAttribute("data-value-attr")){
								var valueAttr=uploder.data("value-attr");
								if(valueAttr){
									hiddeninput.data("value-attr",valueAttr).attr("data-value-attr",valueAttr);
								}
							}
						}
					}
					var loading=uploder.data("loading");
					
					//如果是本地上传 执行uploadFile
					if(handler=="uploadFile"){
						uploadFile("img",url,fileName,fileData,hiddeninput,null,null,false,imgbox,loading,function(type,res){
							if(ucallback){
								var execa_handler=eval(ucallback);
								  if(execa_handler&&typeof(execa_handler)=="function"){
									  execa_handler(uploder,type,fileInput,res);
								}
							}
							
						},function(type,res){
							if(failcallback){
								LayerMsgBox.closeLoadingNow();
								var execa_handler=eval(failcallback);
								if(execa_handler&&typeof(execa_handler)=="function"){
									execa_handler(uploder,type,fileInput,res);
								}
							}else{
								LayerMsgBox.alert(res.msg||"上传失败",2);
							}
						});
					}else if(handler == 'uploadFileToQiniu'){
						loadJBoltPlugin(['qiniu'], function(){
							//如果是上传到七牛 执行uploadFileToQiniu
							uploadFileToQiniu(uploder,"img",fileData,hiddeninput,null,null,false,imgbox,loading,function(type,res){
								if(ucallback){
									var execa_handler=eval(ucallback);
									  if(execa_handler&&typeof(execa_handler)=="function"){
										  execa_handler(uploder,type,fileInput,res);
									}
								}
								
							},function(type,res){
								if(failcallback){
									LayerMsgBox.closeLoadingNow();
									var execa_handler=eval(failcallback);
									if(execa_handler&&typeof(execa_handler)=="function"){
										execa_handler(uploder,type,fileInput,res);
									}
								}else{
									LayerMsgBox.alert(res.msg||"上传失败",2);
								}
							});
						});
					}else{
						var exe_handler=eval(handler);
						  if(exe_handler&&typeof(exe_handler)=="function"){
							  exe_handler(uploder,"img",fileInput);
						}
					}
					
				}
		  }
}

function processImagesHiddenInputValue(uploader,imgboxId,hiddenInputId){
	var imgBox=$("#"+imgboxId);
	var hiddenInput=$("#"+hiddenInputId);
	var value="";
	var valueAttr=uploader.data("value-attr");
	if(valueAttr){
		var lis=imgBox.find("li");
		var length=lis.length;
		var lindex=length-1;
		var li;
		if(length>0){
			lis.each(function(i){
				li=$(this);
				value=value+(li.data("remove-file")||li.data("id")||li.data("path"));
				if(i<lindex){
					value=value+",";
				}
			});
		}
	}else{
		var imgs=imgBox.find("img");
		var length=imgs.length;
		var lindex=length-1;
		var img;
		if(length>0){
			imgs.each(function(i){
				img=$(this);
				value=value+(img.data("path")||img.attr("src"));
				if(i<lindex){
					value=value+",";
				}
			});

		}
	}
	
	
	hiddenInput.val(value);
}

function processFilesHiddenInputValue(fileboxId,hiddenInputId){
	if(!hiddenInputId){return;}
	var fileBox=$("#"+fileboxId);
	var hiddenInput=$("#"+hiddenInputId);
	var value="";
	var lis=fileBox.find("li");
	if(!isOk(lis)){
		lis=fileBox.find(".file");
	}
	var length=lis.length;
	var lindex=length-1;
	var li,a;
	if(length>0){
		lis.each(function(i){
			li=$(this);
			a=li.find("a");
			value=value+(a.data("path")||a.attr("href"));
			if(i<lindex){
				value=value+",";
			}
		});
		
	}
	
	
	hiddenInput.val(value);
}

function removeUploadImgBoxLi(ele){
	var remove=$(ele);
	var li=remove.closest("li");
	var a = li.find("a");
	var imgboxId=li.data("imgbox");
	var ul = $("#"+imgboxId);
	if(!isOk(ul)){
		ul = li.parent();
	}
	var confirm=ul.data("remove-confirm");
	var hiddenInputId=li.data("hidden-input")||li.data("hiddeninput");

	var btn = $(".j_img_uploder[data-imgbox='"+imgboxId+"']");
	var removeUrl;
	if(isOk(btn)){
		removeUrl = btn.data("remove-url");
		if(removeUrl && removeUrl.indexOf("[file]")!=-1){
			var file = li.data("remove-file")||li.data("id")||li.data("path")||remove.data("remove-file")||remove.data("id")||remove.data("path");
			if(!file){
				if(isOk(a)){
					file = a.data("remove-file")||a.data("id")||a.data("path");
				}
				if(!file){
					file = li.find("img").attr("src");
					console.log("data-remove-url中存在[file]，需要文件上携带data-remove-file 或者 data-path 或者 data-id,如果不设置，直接启用img的src属性");
				}
			}
			removeUrl = removeUrl.replace("[file]",file);
		}
	}


	var type= typeof(confirm);
	var confirmMsg="确定删除？";
	var rf=function(){
		if(removeUrl){
			LayerMsgBox.loading("删除中...",4000);
			Ajax.get(removeUrl,function(res){
				li.remove();
				processImagesHiddenInputValue(btn,imgboxId,hiddenInputId);
				LayerMsgBox.success("删除成功",500);
			});
		}else{
			li.remove();
			processImagesHiddenInputValue(btn,imgboxId,hiddenInputId);
			LayerMsgBox.success("删除成功",500);
		}


	}
	if(type!="undefined"){
		if(type=="string"){
			confirmMsg = confirm;
		}
		LayerMsgBox.confirm(confirmMsg,rf);
	}else{
		rf();
	}
	
	
	
}

function removeUploadFileBoxLi(ele){
	var remove=$(ele);
	var li=remove.closest("li");
	if(!isOk(li)){
		li=remove.closest(".file");
	}
	var a = li.find("a");
	var fileboxId=li.data("filebox");
	var ul = $("#"+fileboxId);
	if(!isOk(ul)){
		ul = li.parent();
	}
	var btn = $(".j_upload_file_box[data-filebox='"+fileboxId+"']");
	var removeUrl;
	if(isOk(btn)){
		removeUrl = ul.data("remove-url")||btn.data("remove-url");
		if(removeUrl && removeUrl.indexOf("[file]")!=-1){
			var file = li.data("remove-file")||li.data("id")||li.data("path")||remove.data("remove-file")||remove.data("id")||remove.data("path");
			if(!file){
				if(isOk(a)){
					file = a.data("remove-file")||a.data("id")||a.data("path");
				}
				if(!file){
					LayerMsgBox.alert("data-remove-url中存在[file]，需要文件上携带data-remove-file 或者 data-path 或者 data-id",2);
					return;
				}
			}
			removeUrl = removeUrl.replace("[file]",file);
		}
	}
	var confirm=ul.data("remove-confirm")||btn.data("remove-confirm");
	var hiddenInputId=li.data("hidden-input")||li.data("hiddeninput")||btn.data("hidden-input");
	var type= typeof(confirm);
	var confirmMsg="确定删除？";
	var rf=function(){
		if(removeUrl){
			LayerMsgBox.loading("删除中...",4000);
			Ajax.get(removeUrl,function(res){
				li.remove();
				processFilesHiddenInputValue(fileboxId,hiddenInputId);
				LayerMsgBox.success("删除成功",500);
			});
		}else{
			li.remove();
			processFilesHiddenInputValue(fileboxId,hiddenInputId);
			LayerMsgBox.success("删除成功",500);
		}
	}
	if(type!=undefined && type!="undefined" ){
		if(type=="string"){
			confirmMsg = confirm;
		}
		LayerMsgBox.confirm(confirmMsg,rf);
	}else{
		rf();
	}
	
	
	
}
 function lookUploadImgPhoto(ele){
	var remove=$(ele);
	var li=remove.closest("li");
	var img=li.find("img");
	img.click();
}

function imgGotoLeft(i){
	var fa=$(i);
	var li=fa.closest("li");
	var prev=li.prev();
	if(prev){
		var newLi=li.clone();
		prev.before(newLi);
		var hiddenInputId=li.data("hidden-input")||li.data("hiddeninput");
		var imgboxId=li.data("imgbox");
		li.remove();
		var uploader = $(".j_img_uploder[data-imgbox='"+imgboxId+"']");
		processImagesHiddenInputValue(uploader,imgboxId,hiddenInputId);
		
		
	}else{
		layer.msg("已经是第一个",{time:1000});
	}
	
}
function imgGotoRight(i){
	var fa=$(i);
	var li=fa.closest("li");
	var next=li.next();
	if(next){
		var newLi=li.clone();
		next.after(newLi);
		var hiddenInputId=li.data("hidden-input")||li.data("hiddeninput");
		var imgboxId=li.data("imgbox");
		li.remove();
		var uploader = $(".j_img_uploder[data-imgbox='"+imgboxId+"']");
		processImagesHiddenInputValue(uploader,imgboxId,hiddenInputId);
	}else{
		layer.msg("已经是最后一个",{time:1000});
	}
}

function isFileList(fileDatas){
	return fileDatas&&fileDatas.constructor==FileList;
}
/**
 * 处理图片上传回到
 * @param res
 * @param hiddeninput
 * @param isMultiple
 * @param imgbox
 * @param isLocal
 * @returns
 */
function processImgUploadCallback(res,hiddeninput,isMultiple,imgbox,isLocal){
	var datas=res.data;
	var tmpImgUrl;
	var path;
	var fileId;
	var isJsonObj = false;
	if(typeof(datas)=="string"){
		path=datas;
		tmpImgUrl=real_image(datas);
	}else{
		var valueAttr = "id";
		if(isMultiple&&imgbox){
			var uploader = $(".j_img_uploder[data-imgbox='"+imgbox+"']");
			valueAttr = uploader.data("value-attr")|| (isLocal?"id":'url');
		}else{
			valueAttr = hiddeninput.data("value-attr")|| (isLocal?"id":'url');
		}
		if(isArray(datas)){
			if(typeof(datas[0])=="string"){
				tmpImgUrl=new Array();
				path=new Array();
				$.each(datas,function(i,furl){
					path.push(furl);
					tmpImgUrl.push(real_image(furl));
				});
			}else{
				isJsonObj= true;
				tmpImgUrl=new Array();
				path=new Array();
				fileId = new Array();
				$.each(datas,function(i,fdata){
					path.push(fdata.url||fdata.localUrl||fdata.imgUrl);
					fileId.push(fdata[valueAttr]||"");
					tmpImgUrl.push(real_image(fdata.url||fdata.localUrl||fdata.imgUrl))
				});
			}
		}else{
			isJsonObj= true;
			path=datas.url||datas.imgUrl;
			tmpImgUrl=real_image((datas.url||datas.imgUrl));
			fileId = datas[valueAttr]||"";
		}
	}
	var hinput=hiddeninput;
	var hiddeninputId=hiddeninput.attr("id");
	var hvalue=$.trim(hinput.val());
	if(isMultiple&&imgbox){
		var imgBoxObj = $("#"+imgbox);
		var imgstyle=imgBoxObj.data("imgstyle")||"";
		if(isArray(tmpImgUrl)){
			var imgHtmls = "";
			if(isJsonObj){
				$.each(tmpImgUrl,function(i,bimgurl){
					imgHtmls = imgHtmls + "<li data-imgbox='"+imgbox+"' data-remove-file='"+fileId[i]+"' data-hiddeninput='"+hiddeninputId+"'><img   data-photobtn data-album='"+imgbox+"' style='"+imgstyle+"' data-path='"+path[i]+"' src='"+bimgurl+"'/><div class='optbox' ><i title='查看大图' class='fa fa-eye' onclick='lookUploadImgPhoto(this)'></i><i title='删除' onclick='removeUploadImgBoxLi(this)' class='fa fa-trash'></i><i onclick='imgGotoLeft(this)' class='fa fa-arrow-left' title='左移'></i><i title='右移' onclick='imgGotoRight(this)' class='fa fa-arrow-right'></i></div></li>";
				});
				imgBoxObj.append(imgHtmls).show();
				if(!hvalue){
					hvalue=fileId.join(",");
				}else{
					hvalue=hvalue+","+fileId.join(",");
				}
			}else{
				$.each(tmpImgUrl,function(i,bimgurl){
					imgHtmls = imgHtmls + "<li data-imgbox='"+imgbox+"' data-remove-file='"+path[i]+"' data-hiddeninput='"+hiddeninputId+"'><img   data-photobtn data-album='"+imgbox+"' style='"+imgstyle+"' data-path='"+path[i]+"' src='"+bimgurl+"'/><div class='optbox' ><i title='查看大图' class='fa fa-eye' onclick='lookUploadImgPhoto(this)'></i><i title='删除' onclick='removeUploadImgBoxLi(this)' class='fa fa-trash'></i><i onclick='imgGotoLeft(this)' class='fa fa-arrow-left' title='左移'></i><i title='右移' onclick='imgGotoRight(this)' class='fa fa-arrow-right'></i></div></li>";
				});
				imgBoxObj.append(imgHtmls).show();
				if(!hvalue){
					hvalue=path.join(",");
				}else{
					hvalue=hvalue+","+path.join(",");
				}
			}

		}else{
			if(isJsonObj) {
				imgBoxObj.append("<li data-imgbox='" + imgbox + "'  data-remove-file='"+fileId+"' data-hiddeninput='" + hiddeninputId + "'><img data-photobtn data-album='" + imgbox + "' style='" + imgstyle + "' data-path='" + path + "' src='" + tmpImgUrl + "'/><div class='optbox' ><i title='查看大图' class='fa fa-eye' onclick='lookUploadImgPhoto(this)'></i><i title='删除' onclick='removeUploadImgBoxLi(this)' class='fa fa-trash'></i><i onclick='imgGotoLeft(this)' class='fa fa-arrow-left' title='左移'></i><i title='右移' onclick='imgGotoRight(this)' class='fa fa-arrow-right'></i></div></li>").show();
				if(!hvalue){
					hvalue=fileId;
				}else{
					hvalue=hvalue+","+fileId;
				}
			}else{
				imgBoxObj.append("<li data-imgbox='" + imgbox + "'  data-remove-file='"+path+"'  data-hiddeninput='" + hiddeninputId + "'><img data-photobtn data-album='" + imgbox + "' style='" + imgstyle + "' data-path='" + path + "' src='" + tmpImgUrl + "'/><div class='optbox' ><i title='查看大图' class='fa fa-eye' onclick='lookUploadImgPhoto(this)'></i><i title='删除' onclick='removeUploadImgBoxLi(this)' class='fa fa-trash'></i><i onclick='imgGotoLeft(this)' class='fa fa-arrow-left' title='左移'></i><i title='右移' onclick='imgGotoRight(this)' class='fa fa-arrow-right'></i></div></li>").show();
				if(!hvalue){
					hvalue=path;
				}else{
					hvalue=hvalue+","+path;
				}
			}

		}
		
	}else{
		if(isJsonObj){
			if(isArray(fileId)){
				hvalue=fileId.join(",");
			}else{
				hvalue=fileId;
			}
		}else{
			if(isArray(path)){
				hvalue=path.join(",");
			}else{
				hvalue=path;
			}
		}

	}
	hinput.val(hvalue);
}

/**
 * 得到文件名
 * @param o
 * @returns
 */
function getFileName(o){
	return o.substring(o.lastIndexOf("/")+1);  
}
/**
 * 得到后缀
 * @param o
 * @returns
 */
function getFileExtension(o){
    return o.substring(o.lastIndexOf(".")+1);  
}
/**
 * 处理上传回调
 * @param res
 * @param hiddeninput
 * @param filenameInput
 * @param isMultiple
 * @param filebox
 * @returns
 */
function processFileUploadCallback(res,hiddeninput,filenameInput,isMultiple,fileboxId){
	if(!isMultiple){
		if(hiddeninput){
			var valueAttr=hiddeninput.data("value-attr");
			if(valueAttr){
				hiddeninput.val(res.data[valueAttr]);
			}else{
				if(typeof(res.data) == "string"){
					hiddeninput.val(res.data);
				}else{
					hiddeninput.val(res.data.fileUrl||res.data.localUrl||res.data.url);
				}
			}
		}
		if(filenameInput && typeof(res.data) == "object"){
			$("#"+filenameInput).val(res.data.fileName||"");
		}else{
			$("#"+filenameInput).val("");
		}
		return;
	}
	var filebox;
	if(fileboxId){
		filebox = $("#"+fileboxId);
	}
	var datas=res.data;
	var tmpFilePaths;
	var tmpFileNames,tmpFileName;
	var path,valueAttr;
	if(typeof(datas)=="string"){
		path=datas;
		tmpFilePaths=real_image(datas);
		tmpFileNames=getFileName(path);
	}else{
		if(hiddeninput){
			valueAttr=hiddeninput.data("value-attr");
		}else{
			if(isOk(filebox)){
				valueAttr = filebox.data("path-attr");
			}
		}
		if(isArray(datas)){
			if(typeof(datas[0])=="string"){
				tmpFilePaths=new Array();
				tmpFileNames=new Array();
				path=new Array();
				$.each(datas,function(i,furl){
					path.push(furl);
					tmpFilePaths.push(real_image(furl))
					tmpFileNames.push(getFileName(furl));
				});
			}else{
				tmpFilePaths=new Array();
				tmpFileNames=new Array();
				path=new Array();
				
				$.each(datas,function(i,fdata){
					path.push(valueAttr?fdata[valueAttr]:(fdata.localUrl||fdata.url||fdata.fileUrl));
					tmpFilePaths.push(real_image(fdata.localUrl||fdata.url||fdata.fileUrl));
					tmpFileName = fdata.fileName?fdata.fileName:getFileName(fdata.localUrl||fdata.url||fdata.fileUrl);
					if(tmpFileName.indexOf("/")!=-1){
						tmpFileName=getFileName(tmpFileName);
					}
					tmpFileNames.push(tmpFileName);
				});
			}
		}else{
			path=valueAttr?datas[valueAttr]:(datas.fileUrl||datas.localUrl||datas.url);
			tmpFilePaths=real_image((datas.fileUrl||datas.localUrl||datas.url));
			tmpFileNames=datas.fileName?datas.fileName:getFileName(path);
			if(tmpFileNames.indexOf("/")!=-1){
				tmpFileNames=getFileName(tmpFileNames);
			}
		}
	}
	var hiddeninputId=hiddeninput?(hiddeninput.attr("id")):"";
	var hvalue=hiddeninput?($.trim(hiddeninput.val())):"";
	if(fileboxId){
		var filebox = $("#"+fileboxId);
		var tplId = filebox.data("tpl");
		var tplContent;
		if(tplId){
			var tplObj = g(tplId);
			var tplContent;
			if(tplObj.tagName == "SCRIPT"){
				tplContent = tplObj.innerHTML;
			}else if(tplObj.tagName == "TEXTAREA"){
				tplContent = tplObj.value;
			}else{
				LayerMsgBox.alert("多文件上传组件 data-tpl模板指定的组件只能是Script和Textarea",2);
				return;
			}
			if(!tplContent){
				LayerMsgBox.alert("多文件上传组件 data-tpl指定模板内容为空",2);
				return;
			}
		}
		var box = $(".j_upload_file_box[data-filebox='"+fileboxId+"']");
		var filestyle=filebox.data("filestyle")||"";
		var imgviewerEnable = box.data("imgviewer")||false;
		var pdfviewerEnable = box.data("pdfviewer")||false;
		var dialogviewerEnable = box.data("dialogviewer")||false;
		var dialogviewerUrl = box.data("dialogviewer-url");
		var pdfviewerArea = box.data("pdfviewer-area")||"1280,80%";
		var dialogviewerArea = box.data("dialogviewer-area")||"1280,80%";
		var pdfviewerUrl= box.data("pdfviewer-url");
		if(isArray(tmpFilePaths)){
			if(tplContent){
				var datas = [];
				$.each(tmpFilePaths,function(i,bfileurl){
					datas.push({path:path[i],url:bfileurl,fileName:tmpFileNames[i],fileSuffix:getFileExtension(tmpFileNames[i]),isImg:isImg(tmpFileNames[i]),isPdf:isPdf(tmpFileNames[i])});
				});
				var tpl_html = "<div>"+juicer(tplContent,{fileboxId:fileboxId,hiddeninputId:hiddeninputId,datas:datas})+"</div>";
				if(tpl_html){
					var tplHtmlObj = $(tpl_html);
					var fileObjs = tplHtmlObj.find(".file");
					fileObjs.data("filebox",fileboxId).attr("data-filebox",fileboxId).data("hiddeninput",hiddeninputId).attr("data-hiddeninput",hiddeninputId);
					var imgviewers = tplHtmlObj.find(".file>[data-imgviewer]");
					ImageViewerUtil.initViewers(imgviewers);
					filebox.append(fileObjs).show();
				}else{
					var imgviewerInfo="";
					var pdfviewerInfo="";
					var dialogviewerInfo="";
					var ahref;
					var randKey="li_"+randomId(6);
					$.each(tmpFilePaths,function(i,bfileurl){
						imgviewerInfo="";
						ahref="";
						pdfviewerInfo=""
						dialogviewerInfo="";
						if(isImg(bfileurl) && imgviewerEnable){
							imgviewerInfo = " data-imgviewer data-album='"+fileboxId+"' "
							ahref=bfileurl;
						}else if(isPdf(bfileurl)  && pdfviewerEnable && pdfviewerUrl){
							if(pdfviewerUrl.indexOf("[file]")!=-1){
								ahref =pdfviewerUrl.replace("[file]",bfileurl);
							}else{
								ahref =pdfviewerUrl + bfileurl;
							}
							pdfviewerInfo = ' data-openpage="dialog" data-btn="close" data-area="'+pdfviewerArea+'" ';
						}else if(dialogviewerEnable){
							dialogviewerInfo = ' data-openpage="dialog" data-btn="close" data-area="'+dialogviewerArea+'" ';
							if(dialogviewerUrl){
								if(dialogviewerUrl.indexOf("[file]")!=-1){
									ahref =dialogviewerUrl.replace("[file]",bfileurl);
								}else{
									ahref =dialogviewerUrl + bfileurl;
								}
							}else{
								ahref=bfileurl;
							}
						}else{
							ahref=bfileurl;
						}
						filebox.append("<li data-randkey='"+randKey+"'  data-filebox='"+fileboxId+"' data-remove-file='"+path[i]+"' data-hiddeninput='"+hiddeninputId+"'><a "+ dialogviewerInfo +imgviewerInfo + pdfviewerInfo + " href='"+ahref+"' style='"+filestyle+"' target='_blank' data-path='"+path[i]+"'>"+tmpFileNames[i]+"</a><i title='删除' onclick='removeUploadFileBoxLi(this)' class='fa fa-trash text-danger ml-3 hand'></i></li>").show();
					});

					var imgviewers = filebox.find("li[data-randkey='"+randKey+"']>[data-imgviewer]");
					ImageViewerUtil.initViewers(imgviewers);

				}
			}else{
				var imgviewerInfo="";
				var pdfviewerInfo="";
				var dialogviewerInfo="";
				var ahref;
				var randKey="li_"+randomId(6);
				$.each(tmpFilePaths,function(i,bfileurl){
					imgviewerInfo="";
					ahref="";
					pdfviewerInfo=""
					dialogviewerInfo=""
					if(isImg(bfileurl) && imgviewerEnable){
						imgviewerInfo = " data-imgviewer data-album='"+fileboxId+"' "
						ahref=bfileurl;
					}else if(isPdf(bfileurl)  && pdfviewerEnable && pdfviewerUrl){
						if(pdfviewerUrl.indexOf("[file]")!=-1){
							ahref =pdfviewerUrl.replace("[file]",bfileurl);
						}else{
							ahref =pdfviewerUrl + bfileurl;
						}
						pdfviewerInfo = ' data-openpage="dialog" data-btn="close" data-area="'+pdfviewerArea+'" ';
					}else if(dialogviewerEnable){
						dialogviewerInfo = ' data-openpage="dialog" data-btn="close" data-area="'+dialogviewerArea+'" ';
						if(dialogviewerUrl){
							if(dialogviewerUrl.indexOf("[file]")!=-1){
								ahref =dialogviewerUrl.replace("[file]",bfileurl);
							}else{
								ahref =dialogviewerUrl + bfileurl;
							}
						}else{
							ahref=bfileurl;
						}
					}else{
						ahref=bfileurl;
					}
					filebox.append("<li data-randkey='"+randKey+"' data-filebox='"+fileboxId+"' data-remove-file='"+path[i]+"' data-hiddeninput='"+hiddeninputId+"'><a "+dialogviewerInfo+imgviewerInfo + pdfviewerInfo +" href='"+ahref+"' style='"+filestyle+"' target='_blank' data-path='"+path[i]+"' >"+tmpFileNames[i]+"</a><i title='删除' onclick='removeUploadFileBoxLi(this)' class='fa fa-trash text-danger ml-3  hand'></i></li>").show();
				});

				var imgviewers = filebox.find("li[data-randkey='"+randKey+"']>[data-imgviewer]");
				ImageViewerUtil.initViewers(imgviewers);
			}
			
			if(hiddeninput){
				if(!hvalue){
					hvalue=path.join(",");
				}else{
					hvalue=hvalue+","+path.join(",");
				}
			}
		}else{
			if(tplContent){
				var datas = [];
				datas.push({path:path,url:tmpFilePaths,fileName:tmpFileNames,fileSuffix:getFileExtension(tmpFileNames),isImg:isImg(tmpFileNames),isPdf:isPdf(tmpFileNames)});
				var tpl_html = "<div>"+juicer(tplContent,{fileboxId:fileboxId,hiddeninputId:hiddeninputId,datas:datas})+"</div>";
				if(tpl_html){
					var tplHtmlObj = $(tpl_html);
					var fileObjs = tplHtmlObj.find(".file");
					fileObjs.data("filebox",fileboxId).attr("data-filebox",fileboxId).data("hiddeninput",hiddeninputId).attr("data-hiddeninput",hiddeninputId);
					var imgviewers = tplHtmlObj.find(".file>[data-imgviewer]");
					ImageViewerUtil.initViewers(imgviewers);
					filebox.append(fileObjs).show();
				}else{

					var imgviewerInfo="";
					var pdfviewerInfo="";
					var dialogviewerInfo="";
					var ahref;
					if(isImg(bfileurl) && imgviewerEnable){
						imgviewerInfo = " data-imgviewer data-album='"+fileboxId+"' "
						ahref=tmpFilePaths;
					}else if(isPdf(bfileurl)  && pdfviewerEnable && pdfviewerUrl){
						if(pdfviewerUrl.indexOf("[file]")!=-1){
							ahref =pdfviewerUrl.replace("[file]",tmpFilePaths);
						}else{
							ahref =pdfviewerUrl + tmpFilePaths;
						}
						pdfviewerInfo = ' data-openpage="dialog" data-btn="close" data-area="'+pdfviewerArea+'" ';
					}else if(dialogviewerEnable){
						dialogviewerInfo = ' data-openpage="dialog" data-btn="close" data-area="'+dialogviewerArea+'" ';
						if(dialogviewerUrl){
							if(dialogviewerUrl.indexOf("[file]")!=-1){
								ahref =dialogviewerUrl.replace("[file]",tmpFilePaths);
							}else{
								ahref =dialogviewerUrl + tmpFilePaths;
							}
						}else{
							ahref=tmpFilePaths;
						}
					}else{
						ahref=tmpFilePaths;
					}
					filebox.append("<li data-filebox='"+fileboxId+"' data-remove-file='"+path+"' data-hiddeninput='"+hiddeninputId+"'><a "+dialogviewerInfo+imgviewerInfo + pdfviewerInfo  +" href='"+ahref+"' style='"+filestyle+"' target='_blank' data-path='"+path+"'>"+tmpFileNames+"</a><i title='删除' onclick='removeUploadFileBoxLi(this)' class='fa fa-trash text-danger ml-3 hand'></i></li>").show();
					var imgviewers = filebox.find("li:last>[data-imgviewer]");
					ImageViewerUtil.initViewers(imgviewers);
				}
			}else{

				var imgviewerInfo="";
				var pdfviewerInfo="";
				var dialogviewerInfo="";
				var ahref;
				if(isImg(bfileurl) && imgviewerEnable){
					imgviewerInfo = " data-imgviewer data-album='"+fileboxId+"' "
					ahref=tmpFilePaths;
				}else if(isPdf(bfileurl)  && pdfviewerEnable && pdfviewerUrl){
					if(pdfviewerUrl.indexOf("[file]")!=-1){
						ahref =pdfviewerUrl.replace("[file]",tmpFilePaths);
					}else{
						ahref =pdfviewerUrl + tmpFilePaths;
					}
					pdfviewerInfo = ' data-openpage="dialog" data-btn="close" data-area="'+pdfviewerArea+'" ';
				}else if(dialogviewerEnable){
					dialogviewerInfo = ' data-openpage="dialog" data-btn="close" data-area="'+dialogviewerArea+'" ';
					if(dialogviewerUrl){
						if(dialogviewerUrl.indexOf("[file]")!=-1){
							ahref =dialogviewerUrl.replace("[file]",tmpFilePaths);
						}else{
							ahref =dialogviewerUrl + tmpFilePaths;
						}
					}else{
						ahref=tmpFilePaths;
					}
				}else{
					ahref=tmpFilePaths;
				}

				filebox.append("<li data-filebox='"+fileboxId+"' data-remove-file='"+path+"' data-hiddeninput='"+hiddeninputId+"'><a "+dialogviewerInfo+imgviewerInfo + pdfviewerInfo  +" href='"+ahref+"' style='"+filestyle+"' target='_blank' data-path='"+path+"' >"+tmpFileNames+"</a><i title='删除' onclick='removeUploadFileBoxLi(this)' class='fa fa-trash text-danger ml-3 hand'></i></li>").show();
				var imgviewers = filebox.find("li:last>[data-imgviewer]");
				ImageViewerUtil.initViewers(imgviewers);
			}
			if(hiddeninput){
				if(!hvalue){
					hvalue=path;
				}else{
					hvalue=hvalue+","+path;
				}
			}
		}
		
	}else{
		if(hiddeninput){
			if(isArray(path)){
				hvalue=path.join(",");
			}else{
				hvalue=path;
			}
		}
	}
	if(hiddeninput){
		hiddeninput.val(hvalue);
	}
}
/**
 * 上传到七牛服务器
 * @param uploader
 * @param type img or file
 * @param fileDatas
 * @param hiddeninput
 * @param filenameInput
 * @param sizeinput
 * @param isMultiple
 * @param imgbox
 * @param loading
 * @param successCallback
 * @param failCallback
 * @returns
 */
function uploadFileToQiniu(uploader,type,fileDatas,hiddeninput,filenameInput,sizeinput,isMultiple,imgbox,loading,successCallback,failCallback){
	if(isMultiple&&fileDatas.length==0){return false;}
	if(!isMultiple&&!fileDatas.size){return false;}
	var sn = uploader.data("bucket-sn")||"default";//未指定就用默认配置的七牛账号
	Ajax.get("admin/qiniu/uploadParas?sn="+sn,function(res){
		if(res.data){
			uploader.completeCount = 0;
			uploader.totalSize = isMultiple?fileDatas.length:1;
			uploader.qiniuFiles=new Array(uploader.totalSize);
			_uploadFileToQiniu(uploader,type,fileDatas,loading,res.data,isMultiple,function(){
				try {
					var qiniuFiles = uploader.qiniuFiles;
					if (!isMultiple && sizeinput) {
						$("#" + sizeinput).val(qiniuFiles[0].fileSize);
					}
					if ((type == "img") && hiddeninput && isOk(qiniuFiles)) {
						processImgUploadCallback({data: qiniuFiles}, hiddeninput, isMultiple, imgbox,false);
					} else if ((type == "file") && hiddeninput && isOk(qiniuFiles)) {
						processFileUploadCallback({data: (isMultiple ? (uploader.totalSize == 1 ? qiniuFiles[0] : qiniuFiles) : qiniuFiles[0])}, hiddeninput, filenameInput, isMultiple, imgbox);
					}
					if (successCallback) {
						var exe_callback = eval(successCallback);
						if (exe_callback && typeof (exe_callback) == "function") {
							exe_callback(uploader,type, {data: qiniuFiles});
						}
					}
				}catch (e){
					if (failCallback) {
						var exe_callback = eval(failCallback);
						if (exe_callback && typeof (exe_callback) == "function") {
							exe_callback(uploader,type);
						}
					}else{
						LayerMsgBox.alert("上传失败",2);
					}
				}finally{
					LayerMsgBox.success("上传成功",1000);
				}
				
			});
		}else{
			LayerMsgBox.alert("获取七牛upload token失败",2);
		}
	});
}

function _uploadFileToQiniu(uploader,type,fileDatas,loading,qiniuUploadPara,isMultiple,callback){
	var timeout=uploader.data("timeout")||jboltAjaxTimeout;
	LayerMsgBox.loading(loading?loading:"上传中",timeout);
	
	var fileKey = uploader.data("file-key");
	if(!fileKey){
		fileKey = date(new Date(),"yyyyMMdd_"+randomId());
	}else{
		fileKey = fileKey.replaceAll("[dateTime]",date(new Date(),"yyyyMMddHHmmss"));
		fileKey = fileKey.replaceAll("[date]",date(new Date(),"yyyyMMdd"));
		fileKey = fileKey.replaceAll("[randomId]",randomId());
	}
    var config = {
	      useCdnDomain: true,
	      disableStatisticsReport: false,
	      retryCount: 6,
	      region:getQiniuRegion(qiniuUploadPara.region)
	    };
	var putExtra = {
	      fname: "",
	      params: {},
	      mimeType: null
	    };
	var fileData,totalSize = uploader.totalSize;
	for(var i=0;i<totalSize;i++){
		if(isMultiple){
			fileData = (totalSize==1?fileDatas[0]:fileDatas[i]);
		}else{
			fileData = (totalSize==1?fileDatas:fileDatas[i]);
		}
		__uploadFileToQiniu(uploader,i,fileData,fileKey,qiniuUploadPara,type,putExtra,config,callback);
	}
	
    
}

function __uploadFileToQiniu(uploader,index,fileData,fileKey,qiniuUploadPara,type,putExtra, config,callback){
	var upkey;
	if(fileKey.indexOf("[filename]")!=-1){
		upkey = fileKey.replaceAll("[filename]",fileData.name);
	}else{
		upkey = fileKey+"/"+fileData.name;
	}
	upkey = upkey.replaceAll("//","/");
	var fileSize=parseInt((fileData.size/1024).toFixed(0));
	var observable = qiniu.upload(fileData, upkey, qiniuUploadPara.token, putExtra, config);
	var subscr = observable.subscribe(function(res){
    	//var percent = parseInt(res.total.percent);
    },function(res){
    	alert("上传七牛出现异常:"+JSON.stringify(res));
    },function(res){
    	uploader.completeCount = uploader.completeCount + 1;
    	var url = "";
    	if(qiniuUploadPara.domain.endWith("/")){
    		if(res.key.startWith("/")){
    			res.key = res.key.subString(1);
    		}
    		url = qiniuUploadPara.domain+res.key;
    	}else{
    		if(res.key.startWith("/")){
    			url = qiniuUploadPara.domain+res.key
    		}else{
    			url = qiniuUploadPara.domain+"/"+res.key
    		}
    		
    	}
    	uploader.qiniuFiles[index]={url:url,fileSize:fileSize,fileName:res.key};
    	if(uploader.completeCount == uploader.totalSize && callback){
    		callback();
    	}
    }) 
    
}


function getQiniuRegion(region){
	var ret='';
	switch(region){
		case "z0":
			ret = qiniu.region.z0;
			break;
		case "z1":
			ret = qiniu.region.z1;
			break;
		case "z2":
			ret = qiniu.region.z2;
			break;
		case "na0":
			ret = qiniu.region.na0;
			break;
		case "as0":
			ret = qiniu.region.as0;
			break;
	}
	return ret;
}

/**
 * 上传到本地服务器
 * @param type
 * @param url
 * @param name
 * @param fileDatas
 * @param hiddeninput
 * @param filenameInput
 * @param sizeinput
 * @param isMultiple
 * @param imgbox
 * @param loading
 * @param successCallback
 * @param failCallback
 * @returns
 */
function uploadFile(type,url,name,fileDatas,hiddeninput,filenameInput,sizeinput,isMultiple,imgbox,loading,successCallback,failCallback){
	if(!fileDatas){return false;}
	if(isMultiple&&fileDatas.length==0){return false;}
	if(!isMultiple&&!fileDatas.size){return false;}
	var timeout=jboltAjaxTimeout;
	if(isOk(hiddeninput)){
		timeout=hiddeninput.data("timeout")||jboltAjaxTimeout;
	}
	LayerMsgBox.loading(loading?loading:"上传中",timeout);
	 var fileSize=isMultiple?0:parseInt((fileDatas.size/1024).toFixed(0));
	 var fd = new FormData();
	 if(isMultiple){
		 $.each(fileDatas,function(i,fileData){
			 fd.append(name, fileData);
		 });
	 }else{
		 fd.append(name, fileDatas);
	 }
	    url=actionUrl(url);

	    $.ajax({
	        type:"POST",
	        url: url,
	        data: fd,
	        timeout : timeout, //超时时间设置，单位毫秒
	        cache:false, 
	        async:true, 
	        processData: false,
	        contentType: false,
	        success:function (res) {
	        	if(res.state=="ok"){
	        		if(sizeinput){
	        			$("#"+sizeinput).val(fileSize);
	        		}
	        		if((type=="img")&&hiddeninput&&res.data){
	        			processImgUploadCallback(res,hiddeninput,isMultiple,imgbox,true);
	        		}else if((type=="file")&&res.data){
	        			processFileUploadCallback(res,hiddeninput,filenameInput,isMultiple,imgbox);
					}
	        		if(successCallback){
	        			  var exe_callback=eval(successCallback);
						  if(exe_callback&&typeof(exe_callback)=="function"){
							  exe_callback(type,res);
						  }
	        			}
	        		LayerMsgBox.success("上传成功",1000);
	        	}else{
					if(failCallback){
						LayerMsgBox.closeLoadingNow();
						var exe_callback=eval(failCallback);
						if(exe_callback&&typeof(exe_callback)=="function"){
							exe_callback(type,res);
						}
					}else{
						LayerMsgBox.alert(res.msg,2);
					}

	        	}
	        },
	        error:function (err) {
	        	LayerMsgBox.error("网络异常",1000);
	        }
	    });
}

function getBase64Image(img) {
 var canvas = document.createElement("canvas");
 canvas.width = img.width;
 canvas.height = img.height;
 var ctx = canvas.getContext("2d");
 ctx.drawImage(img, 0, 0, img.width, img.height);
 var dataURL = canvas.toDataURL("image/png");
 return dataURL;
}

function dataURItoBlob(dataURI) {
    var byteString = atob(dataURI.split(',')[1]);
    var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];
    var ab = new ArrayBuffer(byteString.length);
    var ia = new Uint8Array(ab);
    for (var i = 0; i < byteString.length; i++) {
        ia[i] = byteString.charCodeAt(i);
    }
    return new Blob([ab], {type: mimeString });
}
/**
 * 上传组件封装
 */
var FileUploadUtil={
		tpl:'<button id="${buttonId}" data-inputid="${inputId}" type="button" class="j_upload_file ${btnClass}" style="{@if width}width:${width}px;{@/if}{@if height}height:${height}px;{@/if}{@if fontSize}font-size:${fontSize}px;{@/if}"><div class="placeholder"><i class="fa fa-upload {@if placeholder}mr-2{@/if}"></i>${placeholder}</div></button><div class="j_upload_file_box_msg"><span class="j_file_name"></span><i class="fa fa-remove text-danger j_remove_file"></i></div>',
		input_tpl:'<div class="j_upload_file_input"><input data-btnid="${buttonId}" id="${inputId}"  {@if multi}multiple="multiple"{@/if}  type="file" {@if accept}accept="${accept}"{@/if} {@if rule}data-rule="${rule}"{@/if} {@if tips}data-tips="${tips}"{@/if}  /></div>',
		initFileBoxUI:function(fileBoxs){
			if(!isOk(fileBoxs)){return false;}
			
			var len=fileBoxs.length;
			var box,placeholder,tmpplaceholder,width,height,accept,newAccepts,multi,buttonId,inputId,ranId,value;
			for(var i=0;i<len;i++){
				placeholder="选择文件";
				newAccepts="";
				width=0;
				height="100%";
				box=fileBoxs.eq(i);
				if(!box.hasClass("j_upload_file_box")){
					box.addClass("j_upload_file_box");
				}
				accept=box.data("accept");
				if(accept){
					newAccepts=getRealAccept(accept);
				}
				width=box.data("width")||width;
				height=box.data("height")||height;
				tmpplaceholder=box.data("placeholder");
				if(tmpplaceholder){
					tmpplaceholder = $.trim(tmpplaceholder);
					if(tmpplaceholder=="none"){
						placeholder="";
					}else{
						placeholder=tmpplaceholder;
					}
				}
				multi=box.data("multi")||false;
				ranId=randomId();
				buttonId="jufbtn_"+ranId;
				inputId="jufipt_"+ranId;
				box.html(juicer(this.tpl,{buttonId:buttonId,inputId:inputId,width:width,height:height,placeholder:placeholder,fontSize:box.data("font-size"),btnClass:(box.data("btn-class")||"btn btn-outline-primary")}));
				jboltBody.append(juicer(this.input_tpl,{buttonId:buttonId,inputId:inputId,rule:box.data("rule"),tips:box.data("tips"),multi:multi,accept:newAccepts}));
				if(!multi){
					this.initValueMsg(box);
				}
				this.initFileBoxDatas(box);
			}
			
		},
		initValueMsg:function(box){
			var showResult=box.data("show-result");
			if(typeof(showResult)=="boolean" && !showResult){return;}
			var msgBox = box.find("div.j_upload_file_box_msg");
			if(notOk(msgBox)){return;}
			var value=box.data("value");
			if(!value){
				return;
			}
			var valueAttr=box.data("value-attr");
			if(value.indexOf(".")!=-1){
				var fileName = getFileName(value);
				if(fileName){
					msgBox.find("span.j_file_name").text(fileName);
					msgBox.addClass("show");
				}
			}
		},
		//初始化已存在的数据
		initExistFileLi:function(filebox,fileboxId,hiddeninputId){
			var fileObjs = filebox.find(".file");
			if(isOk(fileObjs)){
				fileObjs.data("filebox",fileboxId).attr("data-filebox",fileboxId);
				if(hiddeninputId){
					fileObjs.data("hiddeninput",hiddeninputId).attr("data-hiddeninput",hiddeninputId);
				}
			}
		},
		initFileBoxDatas:function(box){
			var fileboxId = box.data("filebox");
			if(!fileboxId){
				return;
			}
			var filebox = $("#"+fileboxId);
			if(!isOk(filebox)){
				return;
			}
			var hiddeninputId = box.data("hidden-input")||box.data("hiddeninput");
			//初始化已存在的数据
			this.initExistFileLi(filebox,fileboxId,hiddeninputId);
			var filesJsonStr = filebox.data("files");
			if(!filesJsonStr){
				return;
			}
			var filestyle=filebox.data("filestyle")||"";
			var files=[];
			if(typeof(filesJsonStr)=="string"){
				if(filesJsonStr.indexOf("[")==-1 && filesJsonStr.indexOf(":")==-1){
					var filesJson = JSON.parse(filesJsonStr);
					var pathAttr = filebox.data("path-attr")||"path";
					var urlAttr = filebox.data("url-attr")||"url";
					var filenameAttr = filebox.data("filename-attr")||"fileName";
					var jsonLength = filesJson.length;
					var tempFileJson;
					for(var i =0;i<jsonLength;i++){
						tempFileJson = filesJson[i];
						files.push({path:tempFileJson[pathAttr],url:tempFileJson[urlAttr],fileName:tempFileJson[filenameAttr],fileSuffix:getFileExtension(tempFileJson[filenameAttr]),isImg:isImg(tempFileJson[filenameAttr]),isPdf:isPdf(tempFileJson[filenameAttr])});
					}
					
				}else if(filesJsonStr.indexOf(",")!=-1 && filesJsonStr.indexOf(":")==-1){
					var farr = filesJsonStr.split(",");
					for(var i in farr){
						files.push({path:farr[i],url:farr[i],fileName:getFileName(farr[i]),fileSuffix:getFileExtension(farr[i]),isImg:isImg(farr[i]),isPdf:isPdf(farr[i])});
					}
				}else{
					files.push({path:filesJsonStr,url:filesJsonStr,fileName:getFileName(filesJsonStr),fileSuffix:getFileExtension(filesJsonStr),isImg:isImg(filesJsonStr),isPdf:isPdf(filesJsonStr)});
				}
			}else if(isArray(filesJsonStr)){
				var filesJson = filesJsonStr;
				var pathAttr = filebox.data("path-attr")||"path";
				var urlAttr = filebox.data("url-attr")||"url";
				var filenameAttr = filebox.data("filename-attr")||"fileName";
				var jsonLength = filesJson.length;
				var tempFileJson;
				for(var i =0;i<jsonLength;i++){
					tempFileJson = filesJson[i];
					files.push({path:tempFileJson[pathAttr],url:tempFileJson[urlAttr],fileName:tempFileJson[filenameAttr],fileSuffix:getFileExtension(tempFileJson[filenameAttr]),isImg:isImg(tempFileJson[filenameAttr]),isPdf:isPdf(tempFileJson[filenameAttr])});
				}
			}
			
			if(files.length==0){
				return;
			}
			var tplId = filebox.data("tpl");
			var tplContent;
			if(tplId){
				var tplObj = g(tplId);
				var tplContent;
				if(tplObj.tagName == "SCRIPT"){
					tplContent = tplObj.innerHTML;
				}else if(tplObj.tagName == "TEXTAREA"){
					tplContent = tplObj.value;
				}else{
					LayerMsgBox.alert("多文件上传组件 data-tpl模板指定的组件只能是Script和Textarea",2);
					return;
				}
				if(!tplContent){
					LayerMsgBox.alert("多文件上传组件 data-tpl指定模板内容为空",2);
					return;
				}
			}
			var imgviewerEnable = box.data("imgviewer")||false;
			var pdfviewerEnable = box.data("pdfviewer")||false;
			var dialogviewerEnable = box.data("dialogviewer")||false;
			var dialogviewerUrl = box.data("dialogviewer-url");
			var pdfviewerArea = box.data("pdfviewer-area")||"1280,80%";
			var dialogviewerArea = box.data("dialogviewer-area")||"1280,80%";
			var pdfviewerUrl= box.data("pdfviewer-url");
			var hvaluepath=[];
			if(tplContent){
				var tpl_html = "<div>"+juicer(tplContent,{fileboxId:fileboxId,hiddeninputId:hiddeninputId,datas:files})+"</div>";
				var tplHtmlObj = $(tpl_html);
				tplHtmlObj.find(".file").data("filebox",fileboxId).attr("data-filebox",fileboxId).data("hiddeninput",hiddeninputId).attr("data-hiddeninput",hiddeninputId);
				filebox.append(tplHtmlObj.html()).show();
			}else{
				var imgviewerInfo="";
				var pdfviewerInfo="";
				var dialogviewerInfo="";
				var ahref;
				$.each(files,function(i,file){
					imgviewerInfo="";
					ahref="";
					pdfviewerInfo=""
					dialogviewerInfo=""
					if(file.isImg && imgviewerEnable){
						imgviewerInfo = " data-imgviewer data-album='"+fileboxId+"' "
						ahref=file.url;
					}else if(file.isPdf && pdfviewerEnable && pdfviewerUrl){
						if(pdfviewerUrl.indexOf("[file]")!=-1){
							ahref =pdfviewerUrl.replace("[file]",file.url);
						}else{
							ahref =pdfviewerUrl + file.url;
						}
					}else if(dialogviewerEnable){
						dialogviewerInfo = ' data-openpage="dialog" data-btn="close" data-area="'+dialogviewerArea+'" ';
						if(dialogviewerUrl){
							if(dialogviewerUrl.indexOf("[file]")!=-1){
								ahref =dialogviewerUrl.replace("[file]",file.url);
							}else{
								ahref =dialogviewerUrl + file.url;
							}
						}else{
							ahref=file.url;
						}
					}else{
						ahref=file.url;
					}
					filebox.append("<li data-filebox='"+fileboxId+"' data-hiddeninput='"+hiddeninputId+"'><a "+dialogviewerInfo+imgviewerInfo + pdfviewerInfo  +" href='"+ahref+"' style='"+filestyle+"' target='_blank' data-path='"+file.path+"' >"+file.fileName+"</a><i title='删除' onclick='removeUploadFileBoxLi(this)' class='fa fa-trash text-danger ml-3 hand'></i></li>").show();
				});
			}
			var imgviewers = filebox.find("[data-imgviewer]");
			if(isOk(imgviewers)){
				ImageViewerUtil.initViewers(imgviewers);
			}
			$.each(files,function(i,file){
				hvaluepath.push(file.path);
			});
			if(hvaluepath.length>0){
				var hiinput = $("#"+hiddeninputId);
				if(isOk(hiinput)){
					var value = $.trim(hiinput.val());
					if(!value){
						hiinput.val(hvaluepath.join(","));
					}
				}
			}
			
		},
		//处理单个
		oneFileBoxProcess:function(box,file){
			var fileInput = file;
			var that=this;
			var fileValue=file.val();
			var arr=fileValue.split('\\');
			var fileName=arr[arr.length-1];
			box.find("span.j_file_name").text(fileName);
			var multi=box.data("multi")||false;
			var showResult=box.data("show-result");
			if(!multi&&(typeof(showResult)=="undefined" || (typeof(showResult)=="boolean" && showResult))){
				box.find("div.j_upload_file_box_msg").addClass("show");
			}
			//box.find(".j_upload_file").addClass("j_reupload");
			box.closest(".form-group").removeClass("has-error");
			var imgpreview=box.data("imgpreview");
			//如果是图片 而且设置了要出预览 就出预览图
			if(imgpreview){
				if(isImg(fileName)){
					var imgpreviewBox=$(imgpreview);
					if(imgpreviewBox&&imgpreviewBox.length){
						var url=getObjectURL(file[0]);
						if(url){
							imgpreviewBox.html("<img src='"+url+"'/>");
						}
					}
					
				}
			}
			
			
			var handler=box.data("handler");
			if(handler){
				var url=box.data("url");
				if(!url && handler=="uploadFile"){
					that.clearIt(box);
					LayerMsgBox.alert("未设置文件上传地址 data-url",2);
					return;
				}
				if(url){
					url = processEleUrlByLinkOtherParamEle(box,url);
				}
				
				var hiddeninputId=box.data("hidden-input")||box.data("hiddeninput");
				var sizeinput=box.data("size-input")||box.data("sizeinput");
				var fileNameInput=box.data("filename-input")||box.data("filenameinput");
				var fileName=box.data("filename");
				if(!fileName){
					fileName="file";
				}
				var ucallback=box.data("upload-success-handler")||box.data("upload-success-callback");
				var failcallback=box.data("upload-fail-handler")||box.data("upload-fail-callback");
				var hiddeninput;
				if(hiddeninputId){
					hiddeninput=$("#"+hiddeninputId);
					if(isOk(hiddeninput)){
						var timeout=box.data("timeout");
						if(timeout){
							hiddeninput.data("timeout",timeout).attr("data-timeout",timeout);
						}
						if(!hiddeninput[0].hasAttribute("data-value-attr")){
							var valueAttr=box.data("value-attr");
							if(valueAttr){
								hiddeninput.data("value-attr",valueAttr).attr("data-value-attr",valueAttr);
							}
						}
					}
				}
				var loading=box.data("loading");
				
				if(handler=="uploadFile"){
					uploadFile("file",url,fileName,file[0].files[0],hiddeninput,fileNameInput,sizeinput,null,null,loading,function(type,res){
						if(ucallback){
							var execa_handler=eval(ucallback);
							if(execa_handler&&typeof(execa_handler)=="function"){
								execa_handler(box,type,fileInput,res);
							}
						}
					},function(type,res){
						if(failcallback){
							LayerMsgBox.closeLoadingNow();
							var execa_handler=eval(failcallback);
							if(execa_handler&&typeof(execa_handler)=="function"){
								execa_handler(box,type,fileInput,res);
							}
						}else{
							LayerMsgBox.alert(res.msg||"上传异常",2);
						}
					});
				}else if(handler=="uploadFileToQiniu"){
					loadJBoltPlugin(['qiniu'], function(){
						uploadFileToQiniu(box,"file",file[0].files[0],hiddeninput,fileNameInput,sizeinput,false,null,loading,function(type,res){
							if(ucallback){
								var execa_handler=eval(ucallback);
								if(execa_handler&&typeof(execa_handler)=="function"){
									execa_handler(box,type,fileInput,res);
								}
							}
						},function(type,res){
							if(failcallback){
								LayerMsgBox.closeLoadingNow();
								var execa_handler=eval(failcallback);
								if(execa_handler&&typeof(execa_handler)=="function"){
									execa_handler(box,type,fileInput,res);
								}
							}else{
								LayerMsgBox.alert(res.msg||"上传异常",2);
							}
						});
					});
				}else if(handler=="clear"){
					that.clearIt(box);
				}else{
					var exe_handler=eval(handler);
					  if(exe_handler&&typeof(exe_handler)=="function"){
						  exe_handler(box,"file",fileInput,fileValue);
					}
				}
				
			}
		
		},
		changeFile:function(box,fileInput){
			var that=this;
			var accept=box.data("accept");
			var maxSize=box.data("maxsize");
			var fileValue=fileInput.val();
			if(fileValue){
				if(validateFile(fileInput,accept,maxSize)){
					var confirmInfo=box.data("confirm");
					if(confirmInfo){
						LayerMsgBox.confirm(confirmInfo,function(){
							that.oneFileBoxProcess(box,fileInput);
						},function(){
							that.clearIt(box);
						});
					}else{
						that.oneFileBoxProcess(box,fileInput);
					}
				}else{
					that.clearIt(box);
					return false;
				}
			}else{
			that.clearIt(box);
			}
		},
		changeFiles:function(uploder,fileInput,fileDatas){
				var maxSize=uploder.data("maxsize");
				var fileValue=fileInput.val();
				var handler=uploder.data("handler");
				if(fileValue){
					if(!validateFileDatas(fileDatas,"file",maxSize)){
						fileInput.val("");
						return false;
					}
				}else{
					fileInput.val("");
				}
				if(handler){
					var url=uploder.data("url");
					if(!url && handler=="uploadMultipleFile"){
						that.clearIt(box);
						LayerMsgBox.alert("未设置文件上传地址 data-url",2);
						return;
					}
					if(url){
						url = processEleUrlByLinkOtherParamEle(uploder,url);
					}
					
					LayerMsgBox.loading("处理中",10000);
					var hiddeninputId=uploder.data("hidden-input")||uploder.data("hiddeninput");
					var filebox=uploder.data("filebox");
					var limit=uploder.data("limit")||9;
					if(filebox){
						var files=$("#"+filebox+" li.file");
						if((isOk(files)&&(files.length>=limit||(files.length+fileDatas.length>limit))) || fileDatas.length>limit){
							LayerMsgBox.alert("最多上传["+limit+"]个文件",5)
							uploder.find("input[type='file']").val("");
							return false;
						}
					}
					
					var fileName=uploder.data("filename");
					if(!fileName){
						fileName="file";
					}
					var ucallback=uploder.data("upload-success-handler")||uploder.data("upload-success-callback");
					var failcallback=uploder.data("upload-fail-handler")||uploder.data("upload-fail-callback");
					var hiddeninput;
					if(hiddeninputId){
						hiddeninput=$("#"+hiddeninputId);
						if(isOk(hiddeninput)){
							var timeout=uploder.data("timeout");
							if(timeout){
								hiddeninput.data("timeout",timeout).attr("data-timeout",timeout);
							}
							if(!hiddeninput[0].hasAttribute("data-value-attr")){
								var valueAttr=uploder.data("value-attr");
								if(valueAttr){
									hiddeninput.data("value-attr",valueAttr).attr("data-value-attr",valueAttr);
								}
							}
						}
					}
					var loading=uploder.data("loading");
					
					
					if(handler=="uploadMultipleFile"){
						uploadFile("file",url,fileName,fileDatas,hiddeninput,null,null,true,filebox,loading,function(type,res){
							fileInput.val("");
							if(ucallback){
								var execa_handler=eval(ucallback);
								  if(execa_handler&&typeof(execa_handler)=="function"){
									  execa_handler(uploder,type,fileInput,res);
								}
							}
						},function(type,res){
							fileInput.val("");
							if(failcallback){
								LayerMsgBox.closeLoadingNow();
								var execa_handler=eval(failcallback);
								if(execa_handler&&typeof(execa_handler)=="function"){
									execa_handler(uploder,type,fileInput,res);
								}
							}else{
								LayerMsgBox.alert(res.msg||"上传异常",2);
							}
						});
					}else if(handler=="uploadMultipleFileToQiniu"){
						loadJBoltPlugin(['qiniu'], function(){
							uploadFileToQiniu(uploder,"file",fileDatas,hiddeninput,null,null,true,filebox,loading,function(type,res){
								fileInput.val("");
								if(ucallback){
									var execa_handler=eval(ucallback);
									  if(execa_handler&&typeof(execa_handler)=="function"){
										  execa_handler(uploder,type,fileInput,res);
									}
								}
							},function(type,res){
								fileInput.val("");
								if(failcallback){
									LayerMsgBox.closeLoadingNow();
									var execa_handler=eval(failcallback);
									if(execa_handler&&typeof(execa_handler)=="function"){
										execa_handler(uploder,type,fileInput,res);
									}
								}else{
									LayerMsgBox.alert(res.msg||"上传异常",2);
								}
							});
						});
					}else{
						var exe_handler=eval(handler);
						  if(exe_handler&&typeof(exe_handler)=="function"){
							  exe_handler(uploder,type,fileInput);
						}
					}
					
				}
		},
		initFileBoxEvent:function(fileBoxs){
			if(!isOk(fileBoxs)){
				return false;
			}
			var that=this,fbox,fbtn;
			//box的点击效果
			fileBoxs.each(function(){
				fbox=$(this);
				fbtn=fbox.find("button");
				fbtn.off("click").on("click",function(e){
					e.preventDefault();
					e.stopPropagation();
					var innerInputId=$(this).data("inputid");
					var fileInput=jboltBody.find(".j_upload_file_input>input#"+innerInputId);
					if(isOk(fileInput)){
						fileInput.trigger("click");
					}
					return false;
				});
				
				// onchange事件
				jboltBody.find("input[type='file']#"+fbtn.data("inputid")).off("change").on("change",function(event){
					var files = event.target.files; 
					var file=$(this);
					var btnId=file.data("btnid");
					var linkBtn=jboltBody.find("button#"+btnId);
					var box=linkBtn.closest(".j_upload_file_box");
					if(files.length>0){
						var multi=box.data("multi")||false;
						if(multi){
							that.changeFiles(box,file,files);
						}else{
							that.changeFile(box,file);
						}

						var finallyHandler = box.data("finally-handler");
						if(finallyHandler && finallyHandler =="clear"){
							that.clearIt(box);
						}else{
							var exe_finallyHandler=eval(finallyHandler);
							if(exe_finallyHandler&&typeof(exe_finallyHandler)=="function"){
								exe_finallyHandler(box,file);
							}
						}
						
						
					}else{
						that.clearIt(box);
					}
					
					
				});
			});
			
			fileBoxs.find(".j_remove_file").on("click",function(){
				var removefile=$(this);
				var box=removefile.closest(".j_upload_file_box");
				that.clearIt(box);
			});
			
		},
		init:function(parentEle){
			var that=this;
			var parent=getRealParentJqueryObject(parentEle);
			if(!isOk(parent)){return false;}
			
			//得到符合条件的组件 进行初始化
			var fileBoxs=parent.find(".j_upload_file_box,[data-fileuploader]");
			if(isOk(fileBoxs)){
				//初始化UI
				that.initFileBoxUI(fileBoxs);
				//初始化事件
				that.initFileBoxEvent(fileBoxs);
			}	
			
		},
		//清空选择文件
		clearIt:function(uploder){
			var rf=function(){
				var inputId=uploder.find("button").data("inputid");
				var fileInput=jboltBody.find("input[type='file']#"+inputId);
				fileInput.val("");
				uploder.find("span.j_file_name").text("");
				uploder.find("div.j_upload_file_box_msg").removeClass("show");
				uploder.find(".j_upload_file").removeClass("j_reupload");
				var hiddenInputId = uploder.data("hidden-input");
				if(hiddenInputId){
					var hiddenInput = $("#"+hiddenInputId);
					if(isOk(hiddenInput)){
						hiddenInput.val("");
						uploder.hiddenInput = hiddenInput;
					}
				}
				var imgpreview=uploder.data("imgpreview");
				if(imgpreview){
					imgpobj=getRealJqueryObject(imgpreview);
					if(isOk(imgpobj)){
						imgpobj.empty();
						uploder.imgpreview = imgpreview;
					}
				}

				var removehandler=uploder.data("remove-handler");
				if(removehandler){
					var rhexe=eval(removehandler);
					if(rhexe){
						exe(uploder);
					}
				}
			}

			var removeConfirm = uploder.data("remove-confirm");
			var removeUrl = uploder.data("remove-url");
			if(removeUrl && removeUrl.indexOf("[file]")!=-1){
				var hiddeninputId=uploder.data("hidden-input")||uploder.data("hiddeninput");
				if(hiddeninputId){
					var hiinput = $("#"+hiddeninputId);
					if(isOk(hiinput)){
						var hipt_value = hiinput.val();
						if(hipt_value){
							removeUrl = removeUrl.replace("[file]",hipt_value);
						}
					}
				}
			}
			if(removeConfirm){
				var cfMsg;
				if(typeof(removeConfirm)=="string"){
					cfMsg = removeConfirm;
				}else{
					cfMsg = "确认删除已选文件？";
				}
				LayerMsgBox.confirm(cfMsg,function(){
					if(removeUrl){
						LayerMsgBox.loading("正在删除...",4000);
						Ajax.get(removeUrl,function(res){LayerMsgBox.closeLoadingNow();rf();LayerMsgBox.success("删除成功",500)});
					}else{
						rf();
					}
				});
			}else{
				if(removeUrl){
					LayerMsgBox.loading("正在删除...",4000);
					Ajax.get(removeUrl,function(res){LayerMsgBox.closeLoadingNow();rf();LayerMsgBox.success("删除成功",500)});
				}else{
					rf();
				}
			}


		}
}




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
		confirm:function(msg,handler,cancelHandler){
			layer.confirm(msg, {icon: 3, title:'请选择'}, function(index){
				if(handler){
					handler();
				}
				layer.close(index);
			},function(index){
				if(cancelHandler){
					cancelHandler();
				}
				layer.close(index);
			});
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
		prompt:function(title,defaultMsg,handler,type){
			if(type==undefined && type!=0){
				type=2;
			}
			var i=layer.prompt({title: title,value:(defaultMsg?defaultMsg:""),formType: type}, function(text, index){
				if(handler){
					handler(index,text);
				}
			});
			return i;
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
		close:function(index){
			layer.close(index);
		},
		closeAll:function(type){
			if(type){
				layer.closeAll(type);
			}else{
				layer.closeAll();
			}
		},
		closeLoading:function(){
			setTimeout(function(){
				layer.closeAll('dialog'); //关闭加载层
			}, 500);
		},
		closeLoadingNow:function(){
				layer.closeAll('dialog'); //关闭加载层
		},
		load:function(type,time){
			var index=null;
			if(time){
				index=layer.load(type,{time:time});
			}else{
				index=layer.load(type);
			}
			return index;
		},
		closeLoad:function(){
			setTimeout(function(){
				layer.closeAll('loading'); //关闭加载层
			}, 200);
		},
		closeLoadNow:function(){
				layer.closeAll('loading'); //关闭加载层
		}

}

/**
 * ajax封装
 */
var jboltAjaxTimeout=60000;
var Ajax={
		uploadBase64File:function(url,base64Data,fileName,success,error,sync,timeout){
			var blob=dataURItoBlob(base64Data);
			this.uploadBlob(url,blob,fileName,success,error,sync,timeout);
		},
		uploadBlob:function(url,blob,fileName,success,error,sync,timeout){
			var formData=new FormData();
			formData.append("file",blob,fileName);
			this.uploadFormData(url,formData,success,error,sync,timeout);
		},
		uploadFormData:function(url,formData,success,error,sync,timeout){
		    var async=true;
		    if(sync){async=false;}
		    url=actionUrl(url);
			$.ajax({
				url:url,
				type:"post",
				processData: false,
                contentType: false,
				timeout : timeout?timeout:jboltAjaxTimeout, //超时时间设置，单位毫秒
				async:async,
				data:formData,
				success:function(data){
					if(data.state=="ok"){
						processAjaxResultNeedConfirmOr(window.processAjaxResultNeedConfirmOrEle,data,function(){
							if(success){
								success(data);
							}
						});
					}else{
						if(data.msg&&data.msg=="jbolt_system_locked"){
							showJboltLockSystem();
						}else if(data.msg&&data.msg=="jbolt_nologin"){
							showReloginDialog();
						}else if(data.msg&&data.msg=="jbolt_terminal_offline"){
							showReloginDialog();
							LayerMsgBox.alert("当前用户已在其它终端登录，本机已下线",2);
						}else{
							LayerMsgBox.closeLoadingNow();
							if(error){
								LayerMsgBox.alert(data.msg?data.msg:"上传异常",2);
								error(data);
							}else{
								LayerMsgBox.alert(data.msg,2);
							}
						}
						
					}
				},
				error:function(){
					var responseJSON = xhr.responseJSON;
					var defaultMsg = "网络通讯异常";
					var msg;
					if(responseJSON){
						msg = responseJSON.msg||defaultMsg;
					}else{
						msg = defaultMsg;
					}
					LayerMsgBox.alert(msg,2);
					if(error){
						error();
					}
					
				}
				
			});
			jboltAjaxTimeout=60000;
		},
		  post:function(url,data,success,error,sync,timeout,dateType){
			    var async=true;
			    if(sync){async=false;}
			    url=actionUrl(url);
			    var contentType = "application/x-www-form-urlencoded";
			    if(typeof data =="string"){
			    	contentType = "application/json";
			    }
				$.ajax({
					url:url,
					type:"post",
					contentType:contentType,
					dataType:dateType?dateType:"json",
					timeout : timeout?timeout:jboltAjaxTimeout, //超时时间设置，单位毫秒
					async:async,
					data:data,
					success:function(data){
						if(data.state=="ok"){
							processAjaxResultNeedConfirmOr(window.processAjaxResultNeedConfirmOrEle,data,function(){
								if(success){
									success(data);
								}
							});
						}else{
							if(data.msg&&data.msg=="jbolt_system_locked"){
								showJboltLockSystem();
							}else if(data.msg&&data.msg=="jbolt_nologin"){
								showReloginDialog();
							}else if(data.msg&&data.msg=="jbolt_terminal_offline"){
								showReloginDialog();
								LayerMsgBox.alert("当前用户已在其它终端登录，本机已下线",2);
							}else{
								LayerMsgBox.closeLoadingNow();
								if(error){
									LayerMsgBox.alert(data.msg?data.msg:"请求异常",2);
									error(data);
								}else{
									LayerMsgBox.alert(data.msg,2);
								}
							}
							
						}
					},
					error:function(){
						var responseJSON = xhr.responseJSON;
						var defaultMsg = "网络通讯异常";
						var msg;
						if(responseJSON){
							msg = responseJSON.msg||defaultMsg;
						}else{
							msg = defaultMsg;
						}
						LayerMsgBox.alert(msg,2);
						if(error){
							error();
						}
						
					}
					
				});
				jboltAjaxTimeout=60000;
			},
			getWithForm:function(formEle,url,success,error,sync,timeout){
				var form=getRealJqueryObject(formEle);
				if(!isOk(form)){
					LayerMsgBox.alert("请指定正确的formEle参数",2);
					return false;
				}
				url=urlWithFormData(url,form);
				this.get(url,success,error,sync,timeout);
			},
			get:function(url,success,error,sync,timeout,dateType){
				var async=true;
			    if(sync){async=false;}
			    url=actionUrl(url);
				$.ajax({
					url:url,
					type:"get",
					dataType:dateType?dateType:"json",
					timeout : timeout?timeout:jboltAjaxTimeout, //超时时间设置，单位毫秒
					async:async,
					success:function(data){
						if(data.state=="ok"){
							processAjaxResultNeedConfirmOr(window.processAjaxResultNeedConfirmOrEle,data,function(){
								if(success){
									success(data);
								}
							});
						}else{
							if(data.msg&&data.msg=="jbolt_system_locked"){
								showJboltLockSystem();
							}else if(data.msg&&data.msg=="jbolt_nologin"){
								showReloginDialog();
							}else if(data.msg&&data.msg=="jbolt_terminal_offline"){
								showReloginDialog();
								LayerMsgBox.alert("当前用户已在其它终端登录，本机已下线",2);
							}else{
								LayerMsgBox.closeLoadingNow();
								if(error){
									LayerMsgBox.alert(data.msg?data.msg:"请求异常",2);
									error(data);
								}else{
									LayerMsgBox.alert(data.msg,2);
								}
							}
						}
					},
					error:function(xhr, status, e){
							var responseJSON = xhr.responseJSON;
							var defaultMsg = "网络通讯异常";
							var msg;
							if(responseJSON){
								msg = responseJSON.msg||defaultMsg;
							}else{
								msg = defaultMsg;
							}
							LayerMsgBox.alert(msg,2);
							if(error){
								error();
							}
					}
					
				});
				jboltAjaxTimeout=60000;
			}
}
/**
 * 处理option value取值
 * @param data
 * @param value_attr
 * @returns
 */
function processOptionValue(data,value_attr){
	var value=data[value_attr];
	if(!value&&(value=="undefined"||value==undefined)){
		if(value_attr!="value"){
			 value=data["value"];
			 if(!value&&(value=="undefined"||value==undefined)){
				 value=data["id"];
			 }
		}else{
			value=data["id"];
		}
		
	}
	return value;
}
/**
 * options数据 格式化处理
 * @param handler
 * @param data
 * @param text_attr
 * @param delimiter
 * @returns
 */
function processOptionTextByFormatHandler(handler,data,text_attr,delimiter){
	if(!handler){return processOptionText(data,text_attr,delimiter);}
	var exe_handler=eval(handler);
	if(exe_handler&&typeof(exe_handler)=="function"){
		return exe_handler(data);
	}
	return processOptionText(data,text_attr,delimiter);
}
/**
 * 处理option text取值
 * @param data
 * @param text_attr
 * @returns
 */
function processOptionText(data,text_attr,delimiter){
	var text="";
	if(text_attr.indexOf(",")==-1){
		text=data[text_attr];
		if(!text&&(text=="undefined"||text==undefined)){
			if(text_attr!="text"){
				text=data["text"];
				if(!text&&(text=="undefined"||text==undefined)){
					text=data["name"];
					if(!text&&(text=="undefined"||text==undefined)){
						text=data["title"];
					}
				}
			}else{
				if(!text&&(text=="undefined"||text==undefined)){
					text=data["name"];
					if(!text&&(text=="undefined"||text==undefined)){
						text=data["title"];
					}
				}
			}
			
		}
		return text;
	}
	var attrs=text_attr.split(",");
	var t,len=attrs.length;
	for(var i=0;i<len;i++){
		t=data[attrs[i]];
		if(!delimiter){
			text=text+"["+(t?t:"-")+"]";
		}else{
			text=text+(t?t:"*")+((i<len-1)?delimiter:"");
		}
	}
	return text;
}
/**
 * 检测是否是可以输入赋值的控件
 * @param ele
 * @returns
 */
var formInputValueControls=["input","textarea","select"];
function isFormInputValueControl(ele){
	var checkEle=ele,tagName;
	if(!isDOM(ele)){
		checkEle=ele[0];
	}
	tagName=checkEle.tagName.toLowerCase();
	return formInputValueControls.indexOf(tagName)!=-1;
	
}

function syncOtherInput(value,inputEle){
	var input=$(inputEle);
	if(isOk(input)){
		var input;
		input.each(function(){
			input=$(this);
			if(!$.trim(input.val())){
				input.val(value).trigger("change");
				FormChecker.checkIt(input);
			}
		});
	}
}
/**
   * select工具类
   */
  var SelectUtil={
		   //执行选中数据后额外将值赋值给指定的元素
		  setValueToOther:function(select){
			  var setvaluetoId=select.data("setvalueto");
			  if(setvaluetoId){
				  var setvaluetoEle=$("#"+setvaluetoId);
				  if(isOk(setvaluetoEle)){
					  var selectType=select.data("select-type");
					  var values=select.val();
					  if(selectType&&selectType=="select2"){
						  var selectText=select.data("text");
						  if(selectText){
							  var option=select.find("option").first();
							  if(option.is(":selected")){
								  if(values&&values.length>0){
									  values.shift();
								  }
							  }
						  }
					  }
					  if(isFormInputValueControl(setvaluetoEle)){
						  setvaluetoEle.val(values?values:"");
					  }else{
						  setvaluetoEle.text(values?values:"");
					  }
					  if(selectType&&selectType=="select2"){
						  var selectText=select.data("text");
						  setTimeout(function(){
							  var reg = new RegExp("&nbsp;","g")
							  select.next().find("li.select2-selection__choice").each(function(){
								  var li=$(this);
								  if(selectText&&li.attr("title")==selectText){
									  li.remove();
								  }else{
									  var html=li.html();
									  html=html.replace(reg,"");
									  html=html.replace("├","");
									  li.html(html);
								  }
								  
							  });
						  },60);
					  }
				  }
			  }else{
				  LayerMsgBox.alert("请配置data-setvalueto属性",2);
			  }
			  
		  },
		  setTextToOther:function(select){
			  var settexttoId=select.data("settextto");
			  if(settexttoId){
				  var settexttoEle=$("#"+settexttoId);
				  if(isOk(settexttoEle)){
					  var selectType=select.data("select-type");
					var texts=select.find("option:selected");
					var reg = new RegExp("&nbsp;","g");
					var selectText=select.data("text");
					  if(selectType&&selectType=="select2"){
						  
						  setTimeout(function(){
							  select.next().find("li.select2-selection__choice").each(function(){
								  var li=$(this);
								  if(selectText&&li.attr("title")==selectText){
									  li.remove();
								  }else{
									  var html=li.html();
									  html=html.replace(reg,"");
									  html=html.replace("├","");
									  li.html($.trim(html));
								  }
								  
							  });
						  },60);
						  
						  
						  
					  }
					  if(isOk(texts)){
						  var isInputValueEle=isFormInputValueControl(settexttoEle);
						  var len=texts.length;
						  if(selectText&&len==1&&selectText==texts.text()){
							  if(isInputValueEle){
								  settexttoEle.val("");
							  }else{
								  settexttoEle.text("");
							  }
							  
							  return;
						  }
							  var toT=new Array(),tempT;
							  texts.each(function(){
								  tempT=$(this).text();
								  if(selectText&&selectText==tempT){
									  return;
								  }
								  tempT=tempT.replace(reg,"");
								  tempT=tempT.replace("├","");
								  tempT=$.trim(tempT);
								  toT.push(tempT);
							  });
							  if(isOk(toT)){
								  var textResult=toT;
								  if(toT.length==1){
									  textResult=toT[0];
								  }else{
									  textResult=toT.join(",");
								  }
								  if(isInputValueEle){
									  settexttoEle.val(textResult);
								  }else{
									  settexttoEle.text(textResult);
								  }
							  }
					  }else{
						  if(isInputValueEle){
							  settexttoEle.val("");
						  }else{
							  settexttoEle.text("");
						  }
					  }
					  
						 
					   
				  }
			  }else{
				  LayerMsgBox.alert("请配置data-settextto属性",2);
			  }
			  
		  
		  },
		  //同步属性到其他组件
		  syncAttrToOther:function(select){
			 var syncInputs=select.data("sync-ele")||select.data("sync-input")||select.data("syncele")||select.data("syncinput");
			 if(!isOk(syncInputs)){return;}
			 var optionsDatas=select.data("option-datas");
			var syncmust=select.data("sync-must");
			if(typeof(syncmust)=="undefined"){
				syncmust=true;
			}
			 if(!isOk(optionsDatas)){
				//如果选择不是数据 就清空
				$(syncInputs).each(function(){
					if(isFormEle(this.tagName)){
						if(this.value){
							if(syncmust){
								this.value='';
							}
						}else{
							this.value='';
						}
					}else{
						if(this.innerText){
							if(syncmust){
								this.innerText='';
							}
						}else{
							this.innerText='';
						}
					}
				});
				return;
			 }
			 
			var firstText=select.data("text");
			var selectedIndex=select.prop('selectedIndex');
		
			if(selectedIndex<=0){
				if(firstText||selectedIndex<0){
					//如果选择不是数据 就清空
					$(syncInputs).each(function(){
						if(isFormEle(this.tagName)){
							if(this.value){
								if(syncmust){
									this.value='';
								}
							}else{
								this.value='';
							}
							
						}else{
							if(this.innerText){
								if(syncmust){
									this.innerText='';
								}
							}else{
								this.innerText='';
							}
						}
					});
					return;
				}
			}else{
				if(firstText){
					selectedIndex=selectedIndex-1;
				}
			}
			
			 var ele,attr,val,optiondata = optionsDatas[selectedIndex];
			 $(syncInputs).each(function(){
				 ele=$(this);
				 if(optiondata){
					 attr=ele.data("sync-attr")||ele.data("value-attr");
					 if(attr){
						 val=optiondata[attr];
					 }
				 }else{
					 val=''; 
				 }
				 
				 if(typeof(val)=="undefined"){
					 val='';
				 }
				 if(isFormEle(this.tagName)){
					 if(this.value){
							if(syncmust){
								this.value=val;
							}
						}else{
							this.value=val;
						}
				 }else{
					 if(this.innerText){
							if(syncmust){
								this.innerText=val;
							}
						}else{
							this.innerText=val;
						}
				 }
				
			 });
			 
		  },
		  processItems:function(html,newList,list,appendHandler,textFormatHandler,text_attr,value_attr,delimiter,onlyleaf,level){
			  var that=this;
			   /*var text,value;
			   for(var i in list){
					   text=processOptionText(list[i],text_attr);
					   value=processOptionValue(list[i],value_attr);
  						var option = '<option value="'+value+'">&nbsp;&nbsp;&nbsp;&nbsp; ├'+text+'</option>';
  						if(appendHandler){
							option=appendHandler(option,list[i]);
						}
  						html+=option;
  					}*/
				var text,value,optionItem,option_items;
				var levelText="&nbsp;&nbsp;&nbsp;&nbsp;";
				var smallLevelText="&nbsp;&nbsp;&nbsp;";
				if(level>2){
					for(var i=0;i<(level-2);i++){
						levelText=levelText+smallLevelText;
					}
				}
				levelText=levelText+"├";
				
				for(var i in list){
					optionItem=list[i];
					option_items=optionItem.items;
					var hasItems=option_items&&option_items.length>0;
					text=levelText+processOptionTextByFormatHandler(textFormatHandler,optionItem,text_attr,delimiter);
					value=processOptionValue(optionItem,value_attr);
					var option = '<option value="'+value+'">'+text+'</option>';
					if(hasItems&&onlyleaf){
						option='<optgroup data-value="'+value+'" label="'+text+'">';
					}else{
						option='<option value="'+value+'">'+text+'</option>';
					}
					if(appendHandler){
						option=appendHandler(option,optionItem);
					}
					html+=option;
					newList.push(optionItem);
					if(hasItems){
						html=that.processItems(html,newList,option_items,appendHandler,textFormatHandler,text_attr,value_attr,delimiter,onlyleaf,(level+1));
						if(onlyleaf){
							html+="</optgroup>";
						}
					}
				}
					return html;
		},processChangeEventHandler:function(_thisSelect,setting){
			var that=this;
			if(setting&&setting.handler){
				if(setting.handler=="setValueToOther"){
					that.setValueToOther(_thisSelect);
					return true;
				}
				if(setting.handler=="setTextToOther"){
					that.setTextToOther(_thisSelect);
					return true;
				}
				if(setting.handler=="syncAttrToOther"){
					that.syncAttrToOther(_thisSelect);
					return true;
				}
				if(setting.handler=="refreshPortal" || setting.handler=="changePortal"){
					processRefreshAjaxPortalHandler(_thisSelect);
					return true;
				}
				if(setting.handler=="refreshJstree"){
					var jstreeId = _thisSelect.data("jstree-id");
					refreshJstreeHandler(jstreeId);
					return true;
				}

				//处理额外设置的setvalueto settextto	
				that.processSetValueOrTextToOther(_thisSelect);
				setting.handler(_thisSelect);
				return true;
			}
			//如果没有setting handler 就指定 data-handler
			var handler=_thisSelect.data("handler");
			if(handler){
				if(handler=="setValueToOther"){
					that.setValueToOther(_thisSelect);
					return true;
				}
				if(handler=="setTextToOther"){
					that.setTextToOther(_thisSelect);
					return true;
				}
				if(handler=="syncAttrToOther"){
					that.syncAttrToOther(_thisSelect);
					return true;
				}
				if(handler=="refreshPortal" || handler=="changePortal"){
					processRefreshAjaxPortalHandler(_thisSelect);
					return true;
				}

				if(handler=="refreshJstree"){
					var jstreeId = _thisSelect.data("jstree-id");
					refreshJstreeHandler(jstreeId);
					return true;
				}

				//处理额外设置的setvalueto settextto
				that.processSetValueOrTextToOther(_thisSelect);
				that.syncAttrToOther(_thisSelect);
				try{
					var exe_handler=eval(handler);
					if(exe_handler&&typeof(exe_handler)=="function"){
						exe_handler(_thisSelect);
					}
				}catch(e){
					//console.log(e.msg);
				}
				
			}else{
				//处理额外设置的setvalueto settextto	
				that.processSetValueOrTextToOther(_thisSelect);
				that.syncAttrToOther(_thisSelect);
				
				
				var jbolttableHandler=_thisSelect.data("jbolttable-handler");
				if(jbolttableHandler){
						var table=_thisSelect.closest("table");
						if(isOk(table)){
							var jboltTable=table.jboltTable("inst");
							if(jboltTable){
								var td=_thisSelect.closest("td");
								var selectedIndex=_thisSelect.prop("selectedIndex");
								var data={};
								if(_thisSelect.data("text")){
									selectedIndex=selectedIndex-1;
								}
								if(selectedIndex>=0){
									var datas=_thisSelect.data("option-datas");
									data=datas[selectedIndex];
								}
								var isChangeColumns  =  td.data("jbolttable-handler-ischangecolumns");
								if(!isChangeColumns){
									jboltTable.me.processColConfigChangeColumns(jboltTable,td,data);
								}
								var jsonData=jboltTable.tableListDatas[td.closest("tr").data("index")];
								jbolttableHandler(jboltTable,td,_thisSelect.find("option:selected").text(),_thisSelect.val(),jsonData,data);
							}
							
						}
				}
			}
				
		},processSetValueOrTextToOther:function(_thisSelect ){
			var that=this,
			setValueTo=_thisSelect.data("setvalueto"),
			setTextTo=_thisSelect.data("settextto");
			if(setValueTo){
				that.setValueToOther(_thisSelect);
			}
			if(setTextTo){
				that.setTextToOther(_thisSelect);
			}
		},
		refresh:function(select){
			var _thisSelect=getRealParentJqueryObject(select);
			if(isOk(_thisSelect)){
				var setting=_thisSelect.data("setting");
				LayerMsgBox.loading("正在刷新数据...",10000);
				this.readAndInsertItems(_thisSelect,setting,true);
			}
		},
		readAndInsertItems:function(_thisSelect,setting,refreshing){
				   var that=this;
		      		_thisSelect.empty();
		      		_thisSelect.data("setting",setting);
		      		if(_thisSelect.data("text")){
		      			var firstValue=_thisSelect.data("value");
		      			if(typeof(firstValue)=="undefined"){
		      				firstValue='';
		      			}
		      			_thisSelect.append('<option data-empty="true" value="'+firstValue+'">'+_thisSelect.data("text")+'</option>');
		      		}
		      		var url=null;
		      		if(setting&&setting.url){
		      			url=setting.url;
		      		}else{
		      			url=_thisSelect.data("url");
		      		}
		      		
		      		if(url){
		      			url=actionUrl(url);
		      			url=processEleUrlByLinkOtherParamEle(_thisSelect,url,false);
		      			if(!url){
		      				return false;
		      			}
		      			url=processJBoltTableEleUrlByLinkColumn(_thisSelect,url);
		      			if(!url){
		      				return false;
		      			}
		      			$.ajax({
			      			type:"GET",
			      			url:url,
			      			dataType:"json",
			      			timeout : 10000, //超时时间设置，单位毫秒
			      			context:_thisSelect,
			      			success:function(result){
			      				if(refreshing){
			    					LayerMsgBox.closeLoading();
			    				}
			      				that.processSetOptionsHandler(_thisSelect,setting,refreshing,result);
			      				if(refreshing){
			      					if("select2" == _thisSelect.data("select-type")){
			      						FormChecker.checkIt(_thisSelect,true,true);
			      					}
			      				}
			      				that.processDefaultValue(_thisSelect);
			      			}
			      		});
		      		}
		},
		processDefaultValue:function(_thisSelect){
			var defaultValue = _thisSelect.data("default");
			var text=_thisSelect.data("text");
			var value = _thisSelect.val();
			var datas = _thisSelect.data("option-datas");
			if(text && (typeof(value)=="undefined" || (typeof(value)=="string") && notOk(value)) && isOk(datas) && typeof(defaultValue)!="undefined"){
				if(defaultValue == "options_first"){
					_thisSelect[0].selectedIndex=1; 
					_thisSelect.change();
				}else if(defaultValue == "options_last"){
					_thisSelect[0].selectedIndex=datas.length;
					_thisSelect.change();
				}else if(defaultValue.toString().startWith("options_")){
					defaultValue = defaultValue.toString().replace("options_","");
					if(!isNaN(defaultValue)){
						_thisSelect[0].selectedIndex=parseInt(defaultValue);
						_thisSelect.change();
					}
				}else{
					_thisSelect.val(defaultValue).change();
				}
			}
		},
		processOneSelectByOptions:function(_thisSelect,setting){
			_thisSelect.empty();
      		if(_thisSelect.data("text")){
      			_thisSelect.append('<option data-empty="true" value="'+_thisSelect.data("value")+'">'+_thisSelect.data("text")+'</option>');
      		}
			var options=_thisSelect.data("options");
  			if(!options){
  				return;
  			}
  			if(isArray(options)){
  				this.processSetOptionsHandler(_thisSelect,setting,false,{state:"ok",data:options});
  				return;
  			}
  			 
			var arr;
  			if(options.indexOf(",")!=-1){
  				arr=options.split(",");
  			}else{
  				arr=[options];
  			}
  			if(!isOk(arr)){
  				return;
  			}
  			var temp,tempArr,tempObj,objArr=new Array();
  			for(var i in arr){
  				temp=arr[i];
  				if(temp){
  					if(temp.indexOf(":")!=-1){
  						tempArr=temp.split(":");
  						tempObj={text:tempArr[0],value:tempArr[1]};
  					}else{
  						tempObj={text:temp,value:temp};
  					}
  					objArr.push(tempObj);
  				}
  			}
  		this.processSetOptionsHandler(_thisSelect,setting,false,{state:"ok",data:objArr});
		},
		processSetOptionsHandler:function(_thisSelect,setting,refreshing,result){
			    var that=this;
			    var selectedValue=_thisSelect.data("select");
	      		if(refreshing){
	      			selectedValue=_thisSelect.val();
	      		}
	      		var text_attr=_thisSelect.data("text-attr");
	      		if(!text_attr){
	      			text_attr="text";
	      		}
	      		var value_attr=_thisSelect.data("value-attr");
	      		if(!value_attr){
	      			value_attr="value";
	      		}
	      		var delimiter=_thisSelect.data("delimiter");
	      		var appendHandler = _thisSelect.data("append-handler");
	      		if(appendHandler){
					appendHandler=eval(appendHandler);
				}
	      		var onlyleaf=_thisSelect.data("onlyleaf");
	      		
	      		
				if(result.state=="ok"){
					var html="";
					var list=result.data;
					var newList=new Array();
					//_thisSelect.data("option-datas",list);
					var text,value,optionItem,option_items,hasItems,textFormatHandler=_thisSelect.data("text-format-handler");;
					for(var i in list){
						optionItem=list[i];
						option_items=optionItem.items;
						hasItems=option_items&&option_items.length>0;
						text=processOptionTextByFormatHandler(textFormatHandler,optionItem,text_attr,delimiter);
						value=processOptionValue(optionItem,value_attr);
						var option;
						if(hasItems&&onlyleaf){
							option='<optgroup data-value="'+value+'" label="'+text+'">';
						}else{
							option='<option value="'+value+'">'+text+'</option>';
						}
						if(appendHandler){
						option=appendHandler(option,optionItem);
						}
						html+=option;
						newList.push(optionItem);
						if(hasItems){
							html=that.processItems(html,newList,option_items,appendHandler,textFormatHandler,text_attr,value_attr,delimiter,onlyleaf,2);
							if(onlyleaf){
								html+="</optgroup>";
							}
						}
					}
					
					_thisSelect.data("option-datas",newList);
					_thisSelect.append(html);
					if(selectedValue||(typeof(selectedValue)=="boolean")){
						selectedValue=selectedValue.toString();
						if(selectedValue.indexOf(",")!=-1){
  						var arr=selectedValue.split(",");
  						_thisSelect.val(arr);
  					}else{
  						_thisSelect.val(selectedValue);
  					}
						if(!_thisSelect.val()){
							var selectOptions=_thisSelect.find("option");
							if(isOk(selectOptions)){
								_thisSelect.val(selectOptions.eq(0).val());
							}
						}
					}else if(selectedValue==0||selectedValue=="0"){
						var selectOptions=_thisSelect.find("option[value='0']");
						if(isOk(selectOptions)){
							_thisSelect.val(selectedValue);
						}
					}
					
					
				
					_thisSelect.change();
				}
				var selectType=_thisSelect.data("select-type");
				if(selectType){
					if(selectType=="select2"){
						Select2Util.initAutoLoadSelect(_thisSelect);
					}else if(selectType=="bootstrap"){
						var muil=_thisSelect[0].hasAttribute("multiple");
						var opts={
							actionsBox:muil
						}
						_thisSelect.selectpicker(opts);
					}
				}
	    		that.processRefresh(_thisSelect,setting);
		},
		/**
		 * 处理刷新
		 */
		processRefresh:function(_thisSelect,setting){
			var that=this;
				if(_thisSelect.data("refresh")){
					var parent=_thisSelect.closest("div.input-group");
	    			if(isOk(parent)){
	    				var exist=parent.find(".fa-refresh");
	    				if(isOk(exist)){
	    					return false;
	    				}
	    			}
	    			var type=_thisSelect.data("select-type");
	    			var refreshBtn;
	    			if(type&&type=="select2"){
	    				$('.tooltip.show').remove();
	    				var select2=_thisSelect.parent().find(".select2");
	    				select2.addClass("hasRefresh");
	    				var arrow=select2.find(".select2-selection__arrow");
	    				refreshBtn=$('<span class="refresh"><i class="fa fa-refresh"></i></span>');
	    				select2.append(refreshBtn);
	    			}else{
	    				var inputGroup=_thisSelect.parent();
	    				if(isOk(inputGroup) && !inputGroup.hasClass("input-group")){
	    					var informInline = _thisSelect.closest(".form-inline");
	    					if(isOk(informInline)){
	    						_thisSelect.wrap('<div class="d-inline-block align-middle"><div class="input-group"></div></div>');
	    					}else{
	    						_thisSelect.wrap('<div class="input-group"></div>');
	    						
	    					}
	    					
	    				}
//	    				_thisSelect.closest(".form-group").removeClass("form-group").addClass("input-group mb-3");
	    				refreshBtn=$('<div class="input-group-append hand jb_select_refresh"><span class="input-group-text"><i class="fa fa-refresh"></i></span></div>');
	    				_thisSelect.after(refreshBtn);
	    			}
	    			refreshBtn.click(function(e){
	    				e.preventDefault();
	   				  	e.stopPropagation();
	    				LayerMsgBox.loading("正在刷新数据...",10000);
	    				$('.tooltip.show').remove();
	    				that.readAndInsertItems(_thisSelect,setting,true);
	    				return false;
	    			});
	    		}
		},
		/**
		 * 查找
		 */
		findSelect:function(setting){
			var select=null;
			if(setting){
	      		if(setting.selectId){
	      			if(setting.parent){
	      				if(typeof setting.parent=="object"){
	      					if(isDOM(setting.parent)){
	      						select=$(setting.parent).find("#"+setting.selectId);
	      					}else{
	      						select=setting.parent.find("#"+setting.selectId);
	      					}
	      				}else{
	      					if(setting.parent.indexOf("#")!=-1){
	          					select=$(setting.parent).find("#"+setting.selectId);
	          				}else{
	          					select=$("#"+setting.parent).find("#"+setting.selectId);
	          				}
	      				}
	      				
	      			}else{
	      				select=$("#"+setting.selectId);
	      			}
	      			
	      		}else{
	      			if(setting.parent){
	      				if(typeof setting.parent=="object"){
	      					if(isDOM(setting.parent)){
	      						select=$(setting.parent).find("select[data-autoload]");
	      					}else{
	      						select=setting.parent.find("select[data-autoload]");
	      					}
	      					
	      				}else{
	  	    				if(setting.parent.indexOf("#")!=-1){
	  	    					select=$(setting.parent).find("select[data-autoload]");
	  	    				}else{
	  	    					select=$("#"+setting.parent).find("select[data-autoload]");
	  	    				}
	      				}
	      			}else{
	      				select=$("select[data-autoload]"); 
	      			}
	      		}
	      	}else{
	      		select=$("select[data-autoload]");
	      	}
			return select;
		},
		  /**
		   * 处理select
		   * setting selectId parent callback
		   */
		   init:function(setting){
			   var that=this;
		       var selects=that.findSelect(setting);
		       if(selects&&selects.length>0){
		    	   //循环处理 这样写性能高一点
		    	   var len=selects.length;
		    	   for(var i=0;i<len;i++){
		    		   that.processOneSelect(selects.eq(i),setting);
		    	   }
		       }
		    },
		    initSelect:function(select){
		    	this.processOneSelect(select)
		    },
		    /**
		     * 处理单个select
		     */
		    processOneSelect:function(_thisSelect,setting){
	      		var that=this;
	      		//处理二级联动事件绑定
	      		that.processLinkAge(_thisSelect,setting);
	      		//
	      		if((_thisSelect.data("url")||(setting&&setting.url))){
	      			//读取并渲染数据
	  	    		that.readAndInsertItems(_thisSelect,setting,false);
	      		}else if(_thisSelect.data("options")){
	      			that.processOneSelectByOptions(_thisSelect,setting);
	      		}else{
	      			var selectedValue=_thisSelect.data("select");
	      			if(selectedValue){
	      				_thisSelect.val(selectedValue);
	      			}
	      		}
		    },
		    /**
		     * 处理联动
		     */
		    processLinkAge:function(_thisSelect,setting){
		    	var that=this;
		    	var islinkage=_thisSelect.data("linkage");
	      		if(islinkage){
	      			_thisSelect.unbind("change");
	      		}
	      		_thisSelect.on("change",function(){
	      			var beforechange=_thisSelect.data("beforechange");
	      			if(beforechange){
						var exe=eval(beforechange);
						if(exe){
							exe(_thisSelect);
						}
					}
	      			//change事件
	      			that.processChangeEventHandler(_thisSelect,setting);
	      			
	      			var sonIds=_thisSelect.data("sonid"),sonId,sonSelect,sonIdArr,srcUrl,url,sonLen,linkage_attr,linkageValue=_thisSelect.val();
	      			if(islinkage&&sonIds){
	      				sonIdArr=sonIds.split(",");
	      				sonLen=sonIdArr.length;
	      				linkage_attr=_thisSelect.data("linkage-attr");
	      				if(linkage_attr){
	      					//如果设置了linkage-attr 需要得到对应的值
	      					var selectIndex=_thisSelect[0].options.selectedIndex;
	      					var optionDatas=_thisSelect.data("option-datas");
	      					if(optionDatas&&optionDatas.length>0){
	      						if(_thisSelect.data("text")){
		      						selectIndex=selectIndex-1;
		      					}
	      						if(selectIndex!=-1){
	      							linkageValue=optionDatas[selectIndex][linkage_attr];
	      						}else{
	      							linkageValue="";
	      						}
	      					}else{
	      						linkageValue="";
	      					}
	      				}
	      				for(var i=0;i<sonLen;i++){
	      					sonId=sonIdArr[i];
	      					sonSelect=$("#"+sonId);
	      					sonSelect.data("parent-value",linkageValue).attr("data-parent-value",linkageValue);
	      					srcUrl=sonSelect.data("origin-url")||sonSelect.data("src-url")||sonSelect.data("srcurl")||sonSelect.data("url");
		      				if(srcUrl){
		      					var tempEndStr=srcUrl[srcUrl.length-1];
		      					if(tempEndStr=="="||tempEndStr=="-"||tempEndStr=="/"){
		      						url=srcUrl+linkageValue;
		      					}else{
		      						url=srcUrl+"/"+linkageValue;
		      					}
		      				}
		      				var parentBox=_thisSelect.closest(".jbolt_page");
		      				if(isOk(parentBox)){
		      					SelectUtil.init({parent:parentBox,selectId:sonId,url:url});
		      				}else{
		      					SelectUtil.init({selectId:sonId,url:url});
		      				}
	      				}
	      			}
	      			_thisSelect.trigger("linkChange");
	      		});
		    },
  /**
   * 设置select选中值
   * @param id
   * @param value
   */
   setValue:function(id,value,defaultValue){
	   if(value!=undefined&&value!="undefined"&&value!=''){
			  value=value+'';
		}
		if(defaultValue!=undefined&&defaultValue!="undefined"&&defaultValue!=''){
			defaultValue=defaultValue+'';
		}
		var select=$("#"+id);
		if(isOk(select)){
			  if(value){
				  $("#"+id).val(value).change();
			  }else{
				  if(defaultValue){
					  $("#"+id).val(defaultValue).change();
				  }
			  }
		}
	

  },initAutoSetValue:function(parentEle){
	  var parent=getRealParentJqueryObject(parentEle);
	  if(isOk(parent)){
		  parent.find("select[data-autosetvalue]").each(function(){
			  var select=$(this);
			  var value=select.data("autosetvalue");
			  select.val(""+value).change();
		  });
	  }
	 
  }
  }
 
/**
 * 删除tr
 * @param tr
 * @param table
 * @returns
 */
function removeTableRow(tr,table){
	var index=tr.index();
	if(!table){
		table=tr.closest("table");
	}
	var jbolt_view=table.closest(".jbolt_table_view");
	if(isOk(jbolt_view)&&!table.hasClass('jbolt_main_table')){
		table=jbolt_view.find(".jbolt_table_box>.jbolt_table_body>table[data-jbolttable].jbolt_main_table");
	}
	var isJBoltTable=table[0].hasAttribute("data-jbolttable");
	var isTreeTable=table[0].hasAttribute("data-jbolttreetable");
	//如果是JBoltTable组件单独处理
	if(isJBoltTable&&!isTreeTable){//只是JBoltTable
		//处理JBoltTable
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			jboltTable.me.removeRow(jboltTable,index);
		}
	}else if(isJBoltTable&&isTreeTable){//即是JBoltTable 也是JBoltTreeTable
		//处理JBoltTable
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			jboltTable.me.deleteTreeTableRow(jboltTable,index);
		}
	}else if(isTreeTable&&!isJBoltTable){//只是JBoltTreeTable
		deleteTreeTableRow(table,tr);
	}else{
		tr.remove();
	}

}
  
/**
 * 删除一行tr
 */
function removeTr(obj){
	var tr=$(obj).closest("tr");
	if(isOk(tr)){
		removeTableRow(tr);
	}else{
		LayerMsgBox.alert("removeTr-找不到所在TR",2);
	}
}

/**
 * 
 *	刷新当前JBoltLayer
 * @returns
 */
function refreshJBoltLayer(){
	var jboltLayer=$(".jbolt_layer");
	if(isOk(jboltLayer)){
		jboltLayer.find("[data-ajaxportal]").ajaxPortal(true);
	}
}

/**
 * 删除一行tr
 */
function removeByKey(obj){
	var removeKey=$(obj).data("removekey");
	$("[data-removekey='"+removeKey+"']").remove();
}

/**
 * 删除一行并且带着级联下级
 * @returns
 */
function removeTrCascade(ele){
	var tr=ele.closest("tr");
	if(!tr){return false;}
	var id=tr.data("id");
	if(!id){return false;}
	var table=tr.closest("table");
	removeTableRow(tr, table);
	var trs=table.find("tbody>tr[data-pid='"+id+"']");
	if(isOk(trs)){
		var len=trs.length;
		for(var i=0;i<len;i++){
			removeTrCascade(trs.eq(i));
		}
	}
}

function trChangeToUp(currentTr,prevTr,jboltTable){
	var isArr=isArray(currentTr);
	if(isArr){
		var size=currentTr.length;
		var tplTr;
		var showArr=new Array();
		for(var i=0;i<size;i++){
			tplTr=currentTr[i];
			tplTr.insertBefore(prevTr); //插入到当前<tr>前一个元素前
			if(!tplTr.is(":hidden")){
				tplTr.addClass("sortActive");
				showArr.push(tplTr);
			}
		}
		if(isOk(showArr)){
			var len= showArr.length;
			var tplTr;
			setTimeout(function(){
				for(var i=0;i<len;i++){
					tplTr=showArr[i];
					tplTr.removeClass("sortActive");
				}
			},1000)
		}
		
		
		
	}else{
		if (currentTr.index() >0) { 
			currentTr.insertBefore(prevTr); //插入到当前<tr>前一个元素前
			currentTr.addClass("sortActive");
			setTimeout(function(){
				currentTr.removeClass("sortActive");
			},1000);
		} 
	}
	
	if(jboltTable&&jboltTable.fixedColumnTables){
		jboltTable.me.processColumnFixed(jboltTable);
		setTimeout(function(){
			jboltTable.fixedColumnTables.find("tbody>tr.sortActive").removeClass("sortActive");
		},1000);
	}
}

function processToChangeUpByPrevPage(currentTr){
	var table=currentTr.closest("table");
	var pageId=table.data("page");
	var mainPagination;
	if(pageId){
		mainPagination=jboltBody.find("#"+pageId);
	}else{
		mainPagination=jboltBody.find(".pages>.mainPagination");
	}
	if(mainPagination){
		//找到分页组件 去判断有没有页码 有的时候判断不是第一页的就可以跳转上一页了
		var current=mainPagination.find(".pagination>a.current:not(.prev):not(.next)");
		if(isOk(current)){
			var text=parseInt(current.text());
			if(text>1){
				var prev=mainPagination.find(".pagination>a.prev");
				if(isOk(prev)){
					prev.click();
				}
			}
		}
	}
}

function processToChangeDownByNextPage(currentTr){
	var table=currentTr.closest("table");
	var pageId=table.data("page");
	var mainPagination;
	if(pageId){
		mainPagination=jboltBody.find("#"+pageId);
	}else{
		mainPagination=jboltBody.find(".pages>.mainPagination");
	}
	if(mainPagination){
		//找到分页组件 去判断有没有页码 有的时候判断不是第一页的就可以跳转上一页了
		var current=mainPagination.find(".pagination>a.current:not(.prev):not(.next)");
		var last=mainPagination.find(".pagination>a:not(.prev):not(.next):last");
		if(isOk(current)&&isOk(last)){
			var cp=parseInt(current.text());
			var lp=parseInt(last.text());
			if(cp<lp){
				var next=mainPagination.find(".pagination>a.next");
				if(isOk(next)){
					next.click();
				}
			}
		}
	}
}
//上移 
function trMoveUp(currentTr) { 
	var table=currentTr.closest("table");
	var tableFixed=table.closest(".jbolt_table_fixed");
	var index=currentTr.index();
	if(isOk(tableFixed)){
		table=table.closest(".jbolt_table_box").find(".jbolt_table_body>table.jbolt_main_table");
		currentTr=table.find("tbody>tr:eq("+index+")");
	}
	if(index==0){
		processToChangeUpByPrevPage(currentTr);
		return false;
	}
	var isJBoltTable=table[0].hasAttribute("data-jbolttable")&&table.hasClass("jbolt_table");
	var isTreeTable=table[0].hasAttribute("data-jbolttreetable");
	//如果是JBoltTable组件单独处理
	if(isJBoltTable&&!isTreeTable){//只是JBoltTable
		//处理JBoltTable
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			jboltTable.me.moveUpRow(jboltTable,currentTr);
		}
//	}else if(isJBoltTable&&isTreeTable){//即是JBoltTable 也是JBoltTreeTable
//		//处理JBoltTable
//		var jboltTable=table.jboltTable("inst");
//		if(jboltTable){
//			jboltTable.me.moveUpTreeTableRow(jboltTable,index);
//		}
	}else if(isTreeTable&&!isJBoltTable){//只是JBoltTreeTable
		moveUpTreeTableRow(table,currentTr);
	}else{
		trChangeToUp(currentTr,currentTr.prev());
	}
  
} 
function trChangeToDown(currentTr,nextTr,jboltTable){
	var isArr=isArray(currentTr);
	if(isArr){
		var size=currentTr.length;
		var tplTr;
		var showArr=new Array();
		var lastIndex=size-1;
		for(var i=lastIndex;i>=0;i--){
			tplTr=currentTr[i];
			tplTr.insertAfter(nextTr); //插入到当前<tr>前一个元素前
			if(!tplTr.is(":hidden")){
				tplTr.addClass("sortActive");
				showArr.push(tplTr);
			}
		}
		if(isOk(showArr)){
			var len=showArr.length;
			setTimeout(function(){
				for(var i=0;i<len;i++){
					showArr[i].removeClass("sortActive")
				}
			},1000)
		}
		
	}else{
		if(currentTr.index()>=0) {
			currentTr.insertAfter(nextTr); //插入到当前<tr>前一个元素前
			currentTr.addClass("sortActive");
			setTimeout(function(){
				currentTr.removeClass("sortActive");
			},1000);
		} 
	}
	
	if(jboltTable&&jboltTable.fixedColumnTables){
		jboltTable.me.processColumnFixed(jboltTable);
		setTimeout(function(){
			jboltTable.fixedColumnTables.find("tbody>tr.sortActive").removeClass("sortActive");
		},1000);
	}
	

}
// 下移 
function trMoveDown(currentTr) { 
	var table=currentTr.closest("table");
	var tableFixed=table.closest(".jbolt_table_fixed");
	var index=currentTr.index();
	var lastIndex=currentTr.closest("tbody").find("tr:last").index();
	if(isOk(tableFixed)){
		table=table.closest(".jbolt_table_box").find(".jbolt_table_body>table.jbolt_main_table");
		currentTr=table.find("tbody>tr:eq("+index+")");
	}
	if(index==lastIndex){
		processToChangeDownByNextPage(currentTr);
		return false;
	}
	var isJBoltTable=table[0].hasAttribute("data-jbolttable")&&table.hasClass("jbolt_table");
	var isTreeTable=table[0].hasAttribute("data-jbolttreetable");
	//如果是JBoltTable组件单独处理
	if(isJBoltTable&&!isTreeTable){//只是JBoltTable
		//处理JBoltTable
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			jboltTable.me.moveDownRow(jboltTable,currentTr);
		}
//	}else if(isJBoltTable&&isTreeTable){//即是JBoltTable 也是JBoltTreeTable
//		//处理JBoltTable
//		var jboltTable=table.jboltTable("inst");
//		if(jboltTable){
//			jboltTable.me.moveDownTreeTableRow(jboltTable,index);
//		}
	}else if(isTreeTable&&!isJBoltTable){//只是JBoltTreeTable
		moveDownTreeTableRow(table,currentTr);
	}else{
		trChangeToDown(currentTr,currentTr.next());
	}
}

/**
 * 处理刷新ajaxPortal的handler
 * @param ele
 */
function processRefreshAjaxPortalHandler(ele){
	var portalId=ele.data("portal")||ele.data("portalid");
	if(portalId){
		if(portalId == "parentPortal"){
			if(ele){
				var parentPortal= ele.closest("[data-ajaxportal]");
				if(isOk(parentPortal)){
					parentPortal.ajaxPortal(true);
				}
			}
		}else{
			$("#"+portalId).ajaxPortal(true);
		}
	}
}

/**
 * ajaxbtn处理Util封装
 */
var AjaxBtnUtil ={
		init:function(parentEle){
			this.initBy("[data-ajaxbtn]",parentEle);
		},
		initBy:function(cssSelector,parentEle){
			 var parent=getRealParentJqueryObject(parentEle);
			 if(!isOk(parent)){return false;}
			 var that=this;
			 cssSelector=cssSelector||"[data-ajaxbtn]";
			 parent.on("click",cssSelector,function(e){
				  e.preventDefault();
				  e.stopPropagation();
				  if(this.hasAttribute("disabled")||this.hasAttribute("data-disabled")){
					  return false;
				  }
				  var action=$(this);
				  var url=action.attr("href");
				  if(!url){
					  url=action.data("url");
				  }
				  if(!url){
					  LayerMsgBox.alert("请设置URL地址",2);
					  return false;
				  }
				  var checkHandler=action.data("check-handler");
				  if(checkHandler){
					  var checkResult;
					  var exeCheck_handler=eval(checkHandler);
						if(exeCheck_handler&&typeof(exeCheck_handler)=="function"){
							checkResult=exeCheck_handler(action);
							if(!checkResult){
								return false;
							}
							if(typeof(checkResult)!="boolean"){
								if(isArray(checkResult)){
									url=url+checkResult.join(",");
								}else{
									url=url+checkResult;
								}
							}
						}
				  }
				  //处理URL 和关联的参数元素值
				  url=actionUrl(url);
				  url=processEleUrlByLinkOtherParamEle(action,url);
				  
				  var dataconfirm=action.data("confirm");
				  if(!dataconfirm&&cssSelector==".jbolt_table_delbtn"){
					  dataconfirm="确定删除此项?";
				  }
				  if(dataconfirm){
					  LayerMsgBox.confirm(dataconfirm, function(){
						  that.exeFun(action,url);
					  });
				  }else{
					  that.exeFun(action,url);
				  }
			  });
		},
		 exeFun:function(action,url){
			  var dataloading=action.data("loading");
			  var timeout=action.data("timeout")||0;
			  var nomsg=action.data("nomsg");
			  var that=this;
			  LayerMsgBox.loading(dataloading,jboltAjaxTimeout);
			  var successhandler=action.data("success-handler")||action.data("handler");
			  var failhandler=action.data("fail-handler");
			  var failDo=function(data){
					that.exeTheHandler(failhandler,action,data);
			  }
			  //开始执行ajax
			  Ajax.get(url,function(data){
				  LayerMsgBox.closeLoadingNow();
				  if(nomsg){
					  that.exeTheHandler(successhandler,action,data);
				  }else{
					  LayerMsgBox.success(data.msg?data.msg:"操作成功",600,function(){
						  that.exeTheHandler(successhandler,action,data);
					  });
				  }
			  },failhandler?failDo:null,false,timeout);
		  },exeTheHandler:function(handler,action,data){
			  if(handler){
				  disposeTooltip(action);
				  if(handler=="removeTr"){
					  removeTr(action);
				  }else if(handler=="removeTrCascade"){
					  removeTrCascade(action);
				  }else if(handler=="removeByKey"){
					  removeByKey(action);
				  }else if(handler=="jboltTablePageToFirst"){
					  loadJBoltPlugin(['jbolttable'], function(){
						  jboltTablePageToFirst(action);
				  	  });
				  }else if(handler=="jboltTablePageToLast"){
					  loadJBoltPlugin(['jbolttable'], function(){
						  jboltTablePageToLast(action);
				  	  });
				  }else if(handler=="refreshJBoltTable"||handler=="jboltTableRefresh"){
					  loadJBoltPlugin(['jbolttable'], function(){
						  refreshJBoltTable(action);
					  });
				  }else if(handler=="refreshJBoltMainTable"||handler=="jboltMainTableRefresh"){
					  loadJBoltPlugin(['jbolttable'], function(){
						  refreshJBoltMainTable(action);
					  });
				  }else if(handler=="refreshCurrentAjaxPortal"){
					  refreshCurrentAjaxPortal(action);
				  }else if(handler=="removeJBoltTableCheckedTr"){
					  loadJBoltPlugin(['jbolttable'], function(){
						  removeJBoltTableCheckedTr(action,false);
					  });
				  }else if(handler=="jboltTableRemoveCheckedRow"){
					  loadJBoltPlugin(['jbolttable'], function(){
						  jboltTableRemoveCheckedRow(action,false);
					  });
				  }else if(handler=="moveUp"){
					  trMoveUp(action.closest("tr"));
				  }else if(handler=="moveDown"){
					  trMoveDown(action.closest("tr"));
				  }else  if(handler=="refreshPortal"){
					  processRefreshAjaxPortalHandler(action);
				  }else if(handler=="refreshJstree"){
					  var jstreeId = action.data("jstree-id");
					  refreshJstreeHandler(jstreeId);
				  }else{
					  var exe_handler=eval(handler);
					  if(exe_handler&&typeof(exe_handler)=="function"){
						  exe_handler(action,data);
						}
				  }
			  }
		  }
}




/**
 * OpenPageBtn处理Util封装
 */
var OpenPageBtnUtil ={
		init:function(parentEle){
			this.initBy("[data-openpage]",parentEle);
		},
		initBy:function(cssSelector,parentEle){
			 var parent=getRealParentJqueryObject(parentEle);
			 if(!isOk(parent)){return false;}
			 var that=this;
			 cssSelector=cssSelector||"[data-openpage]";
			 parent.on("click",cssSelector,function(e){
				  e.preventDefault();
				  e.stopPropagation();
				  if(this.hasAttribute("disabled")||this.hasAttribute("data-disabled")){
					  return false;
				  }
				  var action=$(this);
				  var orignUrl=action.data("origin-url")||action.data("orign-url")||action.data("orignurl")||action.data("src-url")||action.data("srcurl")||action.attr("href")||action.data("url")||action.data("contentid")||action.data("content-id")||action.data("content")||action.data("content-func");
				  if(!orignUrl){
					  LayerMsgBox.alert("请设置URL地址",2);
					  return false;
				  }
				  if(!this.hasAttribute("data-origin-url")&&!this.hasAttribute("data-orign-url")&&!this.hasAttribute("data-orignurl")&&!this.hasAttribute("data-src-url")&&!this.hasAttribute("data-srcurl")){
					  action.data("origin-url",orignUrl).attr("data-origin-url",orignUrl);
				  }
				  var openpage=action.data("openpage");
				  if(!openpage || (openpage&&openpage!='dialog'&&openpage!='jboltlayer')){
					  var checkHandler=action.data("check-handler");
					  if(checkHandler){
						  var checkResult;
						  var exeCheck_handler=eval(checkHandler);
							if(exeCheck_handler&&typeof(exeCheck_handler)=="function"){
								checkResult=exeCheck_handler(action);
								if(!checkResult){
									return false;
								}
								if(isArray(checkResult)){
									orignUrl=orignUrl+checkResult.join(",");
								}else{
									orignUrl=orignUrl+checkResult;
								}
								 if(this.hasAttribute("href")){
									 action.attr("href",orignUrl);
								 }else{
									 action.data("url",orignUrl).attr("data-url",orignUrl);
								 }
							}
					  }
				  }
				  
				  var dataconfirm=action.data("confirm");
				  if(dataconfirm&&openpage!="jboltlayer"){
					  LayerMsgBox.confirm(dataconfirm, function(){
						  that.exeFun(action);
					  });
				  }else{
					  that.exeFun(action);
				  }
				  return false;
			  });
		},
		 exeFun:function(action){
			 var openpage=action.data("openpage");
			 var target=action.data("target");
			 if(openpage){
				 var blankUrl = action.attr("href")||action.data("url");
				 switch(openpage){
					 case "blank":
						 var a=document.createElement("a");
						 blankUrl=actionUrl(blankUrl);
						 blankUrl=processEleUrlByLinkOtherParamEle(action,blankUrl);
						 blankUrl=processJBoltTableEleUrlByLinkColumn(action,blankUrl);
						 a.href=blankUrl;
						 a.target="_blank";
						 a.click();
						 break;
					 case "jboltlayer":
						 if(target&&target=="parent"){
							 parent.JBoltLayerUtil.openBy(action);
						 }else{
							 JBoltLayerUtil.openBy(action);
						 }
						 break;
					 case "dialog":
						 DialogUtil.openBy(action);
						 break;
					 case "self":
//						 if(isWithtabs()){
						 if(target&&target=="parent"){
							 if(parent.jboltWithTabs){
								 blankUrl=actionUrl(blankUrl);
								 blankUrl=processEleUrlByLinkOtherParamEle(action,blankUrl);
								 blankUrl=processJBoltTableEleUrlByLinkColumn(action,blankUrl);
								 parent.JBoltTabUtil.currentTabGo(blankUrl);
							 }else{
								 parent.JBoltPjaxUtil.openByType(action);
							 }
						 }else{
							 if(jboltWithTabs){
								 blankUrl=actionUrl(blankUrl);
								 blankUrl=processEleUrlByLinkOtherParamEle(action,blankUrl);
								 blankUrl=processJBoltTableEleUrlByLinkColumn(action,blankUrl);
								 JBoltTabUtil.currentTabGo(blankUrl);
							 }else{
								 JBoltPjaxUtil.openByType(action);
							 }
						 }
						 break;
					 case "ajaxportal":
						 if(target&&target=="parent"){
							 parent.AjaxPortalUtil.openBy(action);
						 }else{
							 AjaxPortalUtil.openBy(action);
						 }
						 break;
					 case "iframe":
						 action.data("open-type",2).attr("data-open-type",2);
						 if(target&&target=="parent"){
							 if(parent.jboltWithTabs){
								 parent.JBoltTabUtil.openByType(action);
							 }else{
								 parent.JBoltPjaxUtil.openByType(action);
							 }
						 }else{
							 if(jboltWithTabs){
								 JBoltTabUtil.openByType(action);
							 }else{
								 JBoltPjaxUtil.openByType(action);
							 }
						 }
						 
					 	break;
				 }
			 }else{
//				 if(isWithtabs()){
				 if(target&&target=="parent"){
					 if(parent.jboltWithTabs){
						 parent.JBoltTabUtil.openByType(action);
					 }else{
						 parent.JBoltPjaxUtil.openByType(action);
					 }
				 }else{
					 if(jboltWithTabs){
						 JBoltTabUtil.openByType(action);
					 }else{
						 JBoltPjaxUtil.openByType(action);
					 }
				 }
				
			 }
		  }
}
/**
 * 处理url上的请求类型
 * @param url
 * @param type
 * @param mustWithType
 * @returns
 */
function processUrlRqType(url,type,mustWithType){
	if(url){
		//全地址需要判断是否是本站地址
		if(url.startWith("http://")||url.startWith("https://")||url.startWith("://")){
			var newUrl=url.replace("http://","").replace("https://","").replace("www","");
			if(newUrl.startWith("://")){
				newUrl=newUrl.substring(3);
			}
			if(!newUrl.startWith(JBoltBaseTagHrefNoSchema)){
				return url;
			}
		}
		if(url.indexOf("?")!=-1){
			url=url+"&"+"_jb_rqtype_="+type;
		}else if(url.indexOf("-")!=-1){
			url=url+"-_jb_rqtype_"+type;
		}else{
			var lastChar=url.charAt(url.length-1);
			if(lastChar=='/'){
				url=url.substring(0,url.length-1);
				url=url+"?"+"_jb_rqtype_="+type;
			}else{
				var lastSpIndex=url.lastIndexOf("/");
				if(lastSpIndex==-1){
					url=url+"?"+"_jb_rqtype_="+type;
				}else{
					var req=url.substring(lastSpIndex+1);
					if(isNaN(req)){
						url=url+"?"+"_jb_rqtype_="+type;
					}else{
						url=url+"-_jb_rqtype_"+type;
					}
				}
			}
			
		}
	}
	return url;
}


//PageOpt初始化工具
var PageOptUtil={
		  init:function(){
			  var self=this;
			  DialogUtil.initBy("[data-dialogbtn]");
			  AjaxBtnUtil.initBy("[data-ajaxbtn]");
			  AjaxPortalUtil.initBy("[data-portalbtn]");
		  }
}

//table初始化工具
var TableUtil={
		  init:function(){
			  //初始化删除按钮
			  AjaxBtnUtil.initBy(".jbolt_table_delbtn");
			  //初始化修改按钮
			  DialogUtil.initBy(".jbolt_table_editbtn");
			  //初始化新增按钮
			  DialogUtil.initBy(".jbolt_table_addbtn");
		  }
}

//表单中的时间选择组件
var FormDate={
		destory:function(ele){
			laydate.destory(ele);
		},
		hide:function(parentEle){
			if(!parentEle){
				jboltBody.find(".layui-laydate").remove();
				return;
			}
			var parent=getRealParentJqueryObject(parentEle);
			if(!isOk(parent)){return false;}
			var keyEles = parent.find("input[data-date][lay-key]");
			if(isOk(keyEles)){
				var keyEle,index,lad;
				keyEles.each(function(){
					  keyEle=$(this);
					  index =keyEle.attr("lay-key");
					  lad = jboltBody.find("#layui-laydate"+index);
					  if(isOk(lad)){
						  lad.remove();
						  keyEle.removeAttr("lay-key");
					  }
				}) ;
			}
			 
		},
		  initDate:function(dateEle){
			  var editable=dateEle.data("editable");
			  if(!editable){
				  dateEle.attr("readonly","readonly");
			  }
			  var dateType=dateEle.data("type");
			  if(!dateType){
				  dateType="date";
			  }
			  var datefmt=dateEle.data("fmt");
			  if(!datefmt){
				  switch (dateType) {
				  case "date":
					  datefmt="yyyy-MM-dd";
					  break;
				  	case "time":
					  datefmt="HH:mm";
					  break;
					case "datetime":
						datefmt="yyyy-MM-dd HH:mm";
						break;
					default:
						datefmt="yyyy-MM-dd";
						break;
					}
			  }
			  var id=dateEle.attr("id");
			  if(!id){
				  id=dateEle.attr("name");
				  if(id){
					  id=id.replace(".","_")+"_"+randomId();
				  }else{
					  id="date_"+randomId();
				  }
				  dateEle.attr("id",id);
			  }
			  var minutes=dateEle.data("minutes");
			  var changeHandler=dateEle.data("change-handler");
			  var doneHandler=dateEle.data("done-handler");
			  var small=dateEle.data("small");
			 dateEle.removeAttr("lay-key","");
			 var theme=dateEle.data("theme");
			 var triggerType=dateEle.data("trigger")||"click";
			 var range=dateEle.data("range");
			 
			 var dateOptions={
					 elem:"#"+id,
					 type:dateType, //日期格式
					 format:datefmt,
					 trigger:triggerType,
					 theme:theme,
					 range:range,
					 ready:function(date){
						 var layDate=$(".layui-laydate");
						 if(small){
							 layDate.addClass("small");
						 }
						 if(datefmt.indexOf("ss")==-1&&dateType!="month"&&dateType!="year"){
							 //没有秒就去掉
							 layDate.addClass("noseconds");
							 layDate.find(".laydate-time-list>li:eq(2)").remove();  //清空秒
						 }
						 if(minutes){

							 var minutesArr=minutes.split(",");
							 if(datefmt=="HH:mm"||datefmt=="HH:mm:ss"){
								 var box= layDate.find(".laydate-time-list li ol").eq(1);
								 box.find("li").each(function(){
									 var value=this.innerText;
									 var inArr=$.inArray(value, minutesArr);
									 if(inArr==-1){
										 $(this).remove();
									 }
								 });
							 }else if(datefmt.indexOf("HH:mm")!=-1){
								 layDate.find(".laydate-btns-time").on("click",function(){
									 var box= layDate.find(".laydate-time-list li ol").eq(1);
									 box.find("li").each(function(){
										 var value=this.innerText;
										 var inArr=$.inArray(value, minutesArr);
										 if(inArr==-1){
											 $(this).remove();
										 }
									 });
								 });
							 }
						 
						 }
						 
						 
						 if(small){
							 var eleTop=dateEle.offset().top;
							 	 layDate.css({
								 "top":(eleTop-265)+"px"
							 	});
							 
						 }
						 
					 },
					 change: function(value, date, endDate){
						 if(changeHandler){
							 if(typeof(changeHandler)&&changeHandler=="checkme"){
								 FormChecker.checkIt(dateEle);
							 }else{
								 var change_handler=eval(changeHandler);
								 if(change_handler&&typeof(change_handler)=="function"){
									 change_handler(dateEle,value);
								 }
							 }
						 }
					 },
					 done: function(value, date, endDate){
						 if(doneHandler){
							 if(typeof(doneHandler)&&doneHandler=="checkme"){
								 FormChecker.checkIt(dateEle);
							 }else{
								 var done_handler=eval(doneHandler);
								if(done_handler&&typeof(done_handler)=="function"){
									done_handler(dateEle,value);
								}
							 }
						 }
						
					},
				 };
			 if(dateEle[0].hasAttribute("data-min")){
				 var min=dateEle.data("min");
				 dateOptions.min=min;
			 }
			 if(dateEle[0].hasAttribute("data-max")){
				 var max=dateEle.data("max");
				 dateOptions.max=max;
			 }
			 laydate.render(dateOptions); 
		  
		  },
		  initDates:function(dates){
			  if(!isOk(dates)){return false;}
			  var len=dates.length;
			  for(var i=0;i<len;i++){
				 this.initDate(dates.eq(i)); 
			  }
		  },
		  init:function(parentEle){
			  var parent=getRealParentJqueryObject(parentEle);
			  if(!isOk(parent)){return false;}
			  var dates=parent.find("input[data-date]");
			  if(!isOk(dates)){return false;}
			  this.initDates(dates);
		  }
}

function jsonObjectToUrlParam(data) {
    var _result = [];
    for (var key in data) {
      var value = data[key];
      if(typeof(value)!=undefined&&typeof(value)!="undefined"&&value!='undefined'&&value!=null){
    	  if (value.constructor == Array) {
    		  value.forEach(function(_value) {
    			  _result.push(key + "=" + _value);
    		  });
    	  } else {
    		  _result.push(key + '=' + value);
    	  }
      }
    }
    return _result.join('&');
  }


//弹出dialog类库
var DialogUtil={
		initBy:function(cssSelector,parentEle){
			var parent=getRealParentJqueryObject(parentEle);
			if(!isOk(parent)){return false;}
			var that=this;
			parent.on("click",cssSelector,function(e){
				  e.preventDefault();
				  e.stopPropagation();
				  that.openBy(this);
				  /*var target=trigger.data("target");
				  if(target=="parent"){
				  	parent.DialogUtil.openBy(trigger);
				  }else if(target=="outparent"){
				  	parent.parent.DialogUtil.openBy(trigger);
				  }else{
					  that.openBy(trigger);
				  }*/
				  return false;
			  });
		},
		openBy:function(ele){
			 var action=getRealJqueryObject(ele);
			 if(!isOk(action)){LayerMsgBox.alert("DialogUtil.openBy(ele)参数异常",2);return false;}
				  disposeTooltip(action);
				  var target=action.data("target");
				  if(target){
					  action.data("target","").attr("data-target","");
					  if(target=="parent"){
					  	parent.DialogUtil.openBy(action);
					  	action.data("parent","parent");
					  }else if(target=="outparent"){
					  	parent.parent.DialogUtil.openBy(action);
					  }
					  action.data("target",target).attr("data-target",target);
					  return false;
				  }
			
			 
			  var url=action.data("url");
			  if(!url){
				  url=action.attr("href");
				  if(url=="javascript:void(0)"){
					  url=null;
				  }
			  }
			  var contentid=action.data("contentid")||action.data("content-id");
			  var contentFunc=action.data("content-func");
			  var content=action.data("content");
			  if(contentFunc){
				  var content_exe_func=eval(contentFunc);
				  if(content_exe_func && typeof(content_exe_func)=="function"){
					  content = content_exe_func();
				  }
			  }
			  if(!url&&!contentid&&!content){LayerMsgBox.alert("没有设置dialog的加载URL或者内容", 2); return false;}
			  var checkHandler=action.data("check-handler");
			  if(checkHandler){
				  var checkResult;
				  var exeCheck_handler=eval(checkHandler);
					if(exeCheck_handler&&typeof(exeCheck_handler)=="function"){
						checkResult=exeCheck_handler(action);
						if(typeof(checkResult)=="boolean" && checkResult==false){
							return false;
						}
						if(typeof(checkResult)!="boolean"){
							if(isArray(checkResult)){
								url=url+checkResult.join(",");
							}else{
								url=url+checkResult;
							}
						}
					}
			  }
			  var title=action.data("dialog-title")||action.attr("dialog-title")||action.data("title")||action.attr("title")||action.text();
			  var keepOpen=action.data("keep-open");
			  if(typeof(keepOpen)=="boolean" && keepOpen){
				      var dialogKey=action.data("dialog-key");
					  var iframe;
					  if(dialogKey){
						  iframe=jboltBody.find(".layui-layer iframe[data-dialog-key='"+dialogKey+"']");
					  }else{
						  iframe=jboltBody.find(".layui-layer iframe");
					  }
					  if(isOk(iframe)){
						  	  if(dialogKey){
						  		  var iframeKey=iframe.data("dialog-key");
						  		  if(dialogKey==iframeKey){
						  			  url=actionUrl(url);
									  url=processEleUrlByLinkOtherParamEle(action,url,false);
									  url=processJBoltTableEleUrlByLinkColumn(action,url);
									  url=processUrlRqType(url,"dialog");
									  iframe.attr("src",url);
									  layer.title(title);
									  return false;
						  		  }
						  	  }else{
						  		  url=actionUrl(url);
								  url=processEleUrlByLinkOtherParamEle(action,url,false);
								  url=processJBoltTableEleUrlByLinkColumn(action,url);
								  url=processUrlRqType(url,"dialog");
								  iframe.attr("src",url);
								  layer.title(title);
								  return false;
						  	  }
						  	 
					  }else{
						  var oldIframe=jboltBody.find(".layui-layer iframe");
					      if(isOk(oldIframe) && dialogKey){
							  var iframeKey=oldIframe.data("dialog-key");
					  		  if(dialogKey!=iframeKey){
							  		layer.closeAll();
					  		  }
						  }
					  }
			  }
			  
			  var handler=action.data("handler");
			  var closeHandler=action.data("close-handler");
			  var dialog_area=action.data("area");
			  var w="800px";
			  var h="500px";
			  if(dialog_area){
				  var area=dialog_area.split(",");
				  var ww=area[0];
				  var hh=area[1];
				  if(ww.indexOf("px")!=-1||ww.indexOf("%")!=-1){
					  w=ww;
				  }else{
					  w=ww+"px";
				  }
				  if(hh.indexOf("px")!=-1||hh.indexOf("%")!=-1){
					  h=hh;
				  }else{
					  h=hh+"px";
				  }
			  }
			  if(w.indexOf("%")==-1&&w.indexOf("px")!=-1){
				  var pw=Number(w.replace("px",""));
				  if(pw>jboltWindowWidth){
					  w=(jboltWindowWidth-10)+"px";
				  }
			  }
			  if(h.indexOf("%")==-1&&h.indexOf("px")!=-1){
				  var ph=Number(h.replace("px",""));
				  if(ph>jboltWindowHeight){
					  h=(jboltWindowHeight-10)+"px";
				  }
			  }
			  var dialog_scroll=action.data("scroll");
			  if(dialog_scroll==undefined||dialog_scroll=="undefined"||(typeof(dialog_scroll)=="string"&&dialog_scroll!="no")||(typeof(dialog_scroll)=="boolean"&&dialog_scroll==true)){
				  dialog_scroll="yes";
			  }else{
				  dialog_scroll="no";
			  }
			  var fs=action.data("fs");
			  if(fs&&(fs=="true"||fs==true)){
				  dialog_scroll="yes";
			  }
			  //close dialog and refresh parent
			  var cdrfp=action.data("cdrfp");
			  if(cdrfp==undefined||cdrfp=="undefined"||cdrfp==""){
				  cdrfp=false;
			  }
			  var portalId=action.data("portalid")||action.data("portal-id")||action.data("portal");
		      var btn=action.data("btn");
		      var action_id=action.attr("id");
			  if(!action_id){
				  action_id="dialogbtn_"+randomId();
				  action.attr("id",action_id);
			  }
			  var btnAlign=action.data("btn-align");
			  var shade=action.data("shade");
			  var shadeClose=action.data("shadeclose");
			  var offset=action.data("offset");
			  if(!offset){
				  offset="auto";
			  }else if(offset.indexOf(",")!=-1){
				 var oarr=offset.split(",");
				 if(!oarr || (oarr&&oarr.length!=2)){
					 offset="auto";
				 }else{
					 var ox=oarr[0];
					 var oy=oarr[1];
					if(typeof(ox)=="string"&&ox.indexOf("%")!=-1){
						var p=Number(ox.replace("%",""));
						ox=(parseInt(jboltWindowWidth*p/100))+"px";
					}else if(typeof(ox)=="string"&&ox.indexOf("%")==-1){
						ox=Number(ox)+"px";
					}
					
					if(typeof(oy)=="string"&&oy.indexOf("%")!=-1){
						var p=Number(oy.replace("%",""));
						oy=(parseInt(jboltWindowHeight*p/100))+"px";
					}else if(typeof(oy)=="string"&&oy.indexOf("%")==-1){
						oy=Number(oy)+"px";
					}
					offset=[oy,ox];
				 }
			  }else if(offset=="multi"){
				 var lastLayer = jboltBody.find(".layui-layer:last");
				 if(isOk(lastLayer)){
					var lastOffset = lastLayer.offset();
					offset=[lastOffset.top+40,lastOffset.left+40];
				 }else{
					 offset="auto";
				 }
			  }
			  var successHandler=action.data("success-handler");
		      this.openNewDialog({
		    	  ele:action,
		    	  title:title,
		    	  width:w,
		    	  height:h,
		    	  url:url,
		    	  offset:offset,
		    	  shade:shade,
		    	  shadeClose:shadeClose,
		    	  scroll:dialog_scroll,
		    	  btn:btn,
		    	  btnAlign:btnAlign,
		    	  handler:handler,
		    	  closeHandler:closeHandler,
		    	  cdrfp:cdrfp,
		    	  fs:fs,
		    	  successHandler:successHandler,
		    	  portalId:portalId,
		    	  contentId:contentid,
		    	  content:content
		      });
		  },openNewDialog:function(options){
			  if(options.fs){
				  options.width="100%";
				  options.height="100%";
			  }else{
				  if(options.width){
					  if(options.width.indexOf("%")==-1&&options.width.indexOf("px")==-1){
						  options.width=options.width+"px";
					  }
				  }else{
					  options.width="800px";
				  }
				  if(options.height){
					  if(options.height.indexOf("%")==-1&&options.height.indexOf("px")==-1){
						  options.height=options.height+"px";
					  }
				  }else{
					  options.width="500px";
				  }
			  }
			  var btn=[];
			  var dbtn=options.btn;
			  if(!dbtn||dbtn=="yes"||dbtn=="true"){
		    	  btn=["确定", '关闭'];
		      }else if(dbtn&&dbtn=="no"){
		    	  btn=[];
		      }else if(dbtn&&dbtn=="close"){
		    	  btn=["确定", '关闭'];
		      }else if(dbtn&&dbtn.indexOf(",")!=-1){
		    	  btn=dbtn.split(",");
		      }
			  var type=2;
			  if(!(options.url)&&(options.contentId||options.content)){
				  type=1;
			  }
			  var content="";
			  if(type==1){
				  if(options.contentId){
					  content=$("#"+options.contentId).html();
				  }else if(options.content){
					  if(options.content=="this"){
						  content=options.ele.html();
						  content='<div class="p-3 text-break">'+content+'</div>';
					  }else{
						  content='<div class="p-3 text-break">'+options.content+'</div>';
					  }
				  }
			  }else {
				  var url=actionUrl(options.url);
				  if(isOk(options.ele)){
					  url=processEleUrlByLinkOtherParamEle(options.ele,url,false);
					  if(!url){
						  return false;
					  }
					  url=processJBoltTableEleUrlByLinkColumn(options.ele,url);
					  if(!url){
							return false;
						}
				  }
				  url=processUrlRqType(url,"dialog");
				  content=[url,options.scroll];
			  }
			  
			  shade=(options.shade!=undefined||options.shade=="")?options.shade:0.3;
			  shadeClose=(options.shadeClose!=undefined)?options.shadeClose:false;
			  var btn_align=options.btnAlign?options.btnAlign:"right";
			  var btnAlign='r';
			  switch (btn_align) {
				  case "left":
					  btnAlign='l';
					  break;
				  case "center":
					  btnAlign='c';
					  break;
				  case "right":
					  btnAlign='r';
					  break;
			}
			  var that=this;
			  var layerOptions={
					  type: type,
					  title: (options.title||"新窗口"),
					  shadeClose:shadeClose,
					  shade: shade,
					  maxmin:true,
					  offset:options.offset?options.offset:"auto",
					  area: [options.width, options.height],
					  content:content,
					  btn:btn, 
					  btnAlign:btnAlign,
					  success:function(obj,index){
//						  if(type==2){
//							  var iframeWin = window[$(".layui-layer-iframe").find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
////							  iframeWin.focus();
//						  }
						  if(type==2 && options.ele){
							  var dialogKey=options.ele.data("dialog-key");
							  if(dialogKey){
								  var iframe=obj.find("iframe");
								  if(iframe){
									  iframe.data("dialog-key",dialogKey).attr("data-dialog-key",dialogKey);
								  }
							  }
							 
						  }
						  
						  if(options.successHandler){
							  that.exeBindHandler(options.successHandler,options);
						  }
					  },
					  yes:function(index,layero){
						  //点击确定按钮处理
						  if(type==2){
//							  var iframeWin = window[$(".layui-layer-iframe").find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
							  var iframeWin = window[layero.find('iframe')[0]['name']]; 
							  iframeWin.submitThisForm(function(){
								  LayerMsgBox.close(index);
								  if(options.handler){
									  options.exeOkHandler=true;
									  that.exeBindHandler(options.handler,options);
								  }
							  });
						  }
					  },end:function(){
						  if(options.exeOkHandler){return false;}
						 if(options.cdrfp){
							 if(options.closeHandler){
								 var handlerType=typeof(options.closeHandler);
								 if(handlerType=="string"){
									 that.exeBindHandler(options.closeHandler,options);
								 }else if(handlerType=="function"){
									 options.closeHandler();
								 }
							 }else{
								 refreshPjaxContainer();
							 }
						 }else{
							 if(options.closeHandler){
								 var handlerType=typeof(options.closeHandler);
								 if(handlerType=="string"){
									 that.exeBindHandler(options.closeHandler,options);
								 }else if(handlerType=="function"){
									 options.closeHandler();
								 }
							 }
						 }
					  }
				  };
			  if(options.zIndex){
				  layerOptions.zIndex=options.zIndex;
			  }
			  if(options&&options.id){
				  layerOptions.id=options.id;
			  }else if(options.ele){
				  layerOptions.id = options.ele.attr("id")+"_layer";
			  }else{
				  layerOptions.id = randomId()+"_layer";
			  }
			  var lindex=layer.open(layerOptions);
			  var layObj=$("#layui-layer"+lindex);
			  if(dbtn&&dbtn=="close"){
				  layObj.find("a.layui-layer-btn0").hide();
			  }
			  if(dbtn&&dbtn=="no"){
				  layObj.find(".layui-layer-btn").remove();
			  }
			  
//			  layObj.data("trigger-actionid",options.ele.attr("id"));
			  if(options.ele){
				  layObj.data("trigger-action",options.ele);
				  if(options.ele.data("in-editable-td")){
					  layObj.data("link-editable-td",options.ele.closest("td"));
				  }
			  }
		  },
		  getCurrent:function(){
			  return jboltBody.find(".layui-layer").first();
		  },
		  getByIndex:function(index){
			  return jboltBody.find("#layui-layer"+index);
		  },getCurrentTriggerEle:function(){
			  var layerDialog=this.getCurrent();
			  if(isOk(layerDialog)){
				  return layerDialog.data("trigger-action");
//				 var triggerId = layerDialog.data("trigger-actionid");
//				 if(triggerId){
//					 return jboltBody.find("#"+triggerId);
//				 }
			  }
			  return null;
		  },exeBindHandler:function(handler,options){
				if(handler=="refreshPortal"){
					  if(options.portalId){
						  LayerMsgBox.success("操作成功",500,function(){
							  if(options.portalId == "parentPortal"){
								  if(options.ele){
									  var parentPortal= options.ele.closest("[data-ajaxportal]");
									  if(isOk(parentPortal)){
										  parentPortal.ajaxPortal(true);
									  }
								  }
							  }else{
								  $("#"+options.portalId).ajaxPortal(true);
							  }
						  });
					  }else{
						  LayerMsgBox.alert("没有配置data-portalid",2);
					  }
				 }else if(handler=="refreshJstree"){
					var jstreeId = options.jstreeId||(options.ele?options.ele.data("jstree-id"):null);
					refreshJstreeHandler(jstreeId);
				  }else if(handler=="jboltTablePageToFirst"){
					  loadJBoltPlugin(['jbolttable'], function(){
						  jboltTablePageToFirst(options.ele);
					  });
				  }else if(handler=="jboltTablePageToLast"){
					  loadJBoltPlugin(['jbolttable'], function(){
						  jboltTablePageToLast(options.ele);
					  });
				  }else if(handler=="refreshJBoltTable"||handler=="jboltTableRefresh"){
					  loadJBoltPlugin(['jbolttable'], function(){
						  refreshJBoltTable(options.ele);
					  });
				  }else if(handler=="refreshJBoltMainTable"||handler=="jboltMainTableRefresh"){
					  loadJBoltPlugin(['jbolttable'], function(){
						  refreshJBoltMainTable(options.ele);
					  });
				  }else if(handler=="refreshCurrentAjaxPortal"){
					  refreshCurrentAjaxPortal(options.ele);
				  }else if(handler=="removeJBoltTableCheckedTr"){
					  loadJBoltPlugin(['jbolttable'], function(){
						  removeJBoltTableCheckedTr(options.ele,false);
					  });
				  }else if(handler=="jboltTableRemoveCheckedRow"){
					  loadJBoltPlugin(['jbolttable'], function(){
						  jboltTableRemoveCheckedRow(options.ele,false);
					  });
				  }else{
					  var exe_handler=eval(handler);
						if(exe_handler&&typeof(exe_handler)=="function"){
							exe_handler();
						}
				  }
			}
}



/**
 * dialog里提交form 让parent中的jbolttable按照表单去查询数据
 * 使用json conditions出发表格重新查询加载数据
 * @param formId
 * @param tableId
 * @returns
 */
function jboltTableLoadByDialogForm(formId,tableId){
	if(!tableId){
		var triggerEle = parent.DialogUtil.getCurrentTriggerEle();
		if(!triggerEle){
			LayerMsgBox.alert("jboltTableLoadByDialogForm指定tableId参数异常-1，无法定位",2);
			return false;
		}
		var jbolt_tableid=triggerEle.data("jbolt-table-id");
		if(!jbolt_tableid){
			LayerMsgBox.alert("jboltTableLoadByDialogForm指定tableId参数异常-2，无法定位",2);
			return false;
		}
		tableId=jbolt_tableid;
	}
	var form=$("#"+formId);
	if(!isOk(form)){
		LayerMsgBox.alert("jboltTableLoadByDialogForm指定formId参数异常，无法定位",2);
		return false;
	}
	var conditions=form.serializeJSON();
	return parent.jboltTableReadByConditions(tableId,conditions);
}
/**
 * 时间日期格式化
 */
Date.prototype.Format = function (fmt) { 
	var o = {
			"M+": this.getMonth() + 1, 
			"d+": this.getDate(), 
			"H+": this.getHours(), 
			"m+": this.getMinutes(),
			"s+": this.getSeconds(), 
			"q+": Math.floor((this.getMonth() + 3) / 3),
			"S": this.getMilliseconds()  
	};
	if (/(y+)/.test(fmt)){
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	} 
	for (var k in o){
		if (new RegExp("(" + k + ")").test(fmt)){
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
		}
	}
	return fmt;
}

/********************************************************************
 ************************表单验证 开始******************************
 ********************************************************************/  
  /**
   * 判断对象是否为array
   * @param obj
   * @returns {Boolean}
   */
  function isArray(obj){ 
  	return $.isArray(obj); 
  } 
  
  function isEmpty(something){
  	return (something=="undefined"||something==null||something==""||something==false);
  	}

  function isNotEmpty(something){
  	return ((something!=null&&something!="undefined"&&something!="")||something==true);
  	}

  function showFormEleErrorStyle(ele,msg,noshakeAndFocus){
	  if(msg&&(ele.is(":hidden")||(ele.is("input")&&ele.attr("type")=="hidden"))){
		  LayerMsgBox.error("<span class='j_text_danger'>"+msg+"</span>",1000);
		  return false;
	  }
	  var target=ele;
	  var isS2=false;
	  if(ele.is("select")){
		  var type=ele.data("select-type");
		  if(type&&type=="select2"){
			  target=ele.next().find(".select2-selection");
			  isS2=true;
		  }
	  }
	  if(msg){
		  target.tooltip({ boundary: 'window',container:"body",title:msg}).tooltip("show");
		  $("#"+target.attr("aria-describedby")).addClass("error").find(".tooltip-inner").text(msg);
	  }
	  
	  if(isS2){
		  target.removeClass("border-success").addClass("border-danger");
	  }else{
		  target.removeClass("is-valid").addClass("is-invalid");
	  }
	  if(!noshakeAndFocus){
		  ele.focus();
		  target.shake(1,5,300);
	  }
  }
  function removeFormEleErrorStyle(ele){
	  var target=ele;
	  var isS2=false;
	  if(ele.is("select")){
		  var type=ele.data("select-type");
		  if(type&&type=="select2"){
			  target=ele.next().find(".select2-selection");
			  isS2=true;
		  }
	  }
	  var showSuccessResult = target.data("show-success-result");
	  if(isS2){
		  target.removeClass("border-danger");
		  if(typeof(showSuccessResult)=="undefined" || (typeof(showSuccessResult)=="boolean" && showSuccessResult==true)){
			  target.addClass("border-success");
		  }
	  }else{
		  target.removeClass("is-invalid");
		  if(typeof(showSuccessResult)=="undefined" || (typeof(showSuccessResult)=="boolean" && showSuccessResult==true)){
			  target.addClass("is-valid");	
		  }
	  }
	  target.tooltip("dispose");
  }
  function removeFormEleAllStyle(ele){
	  var target=ele;
	  var isS2=false;
	  if(ele.is("select")){
		  var type=ele.data("select-type");
		  if(type&&type=="select2"){
			  target=ele.next().find(".select2-selection");
			  isS2=true;
		  }
	  }
	  if(isS2){
		  target.removeClass("border-danger").removeClass("border-success");
	  }else{
		  target.removeClass("is-invalid").removeClass("is-valid");	
	  }
	  target.tooltip("dispose");
	}

//摇摆摇摆摇摆起来
  jQuery.fn.shake = function(intShakes /*Amount of shakes*/, intDistance /*Shake distance*/, intDuration /*Time duration*/,callback) {
	  this.each(function() {
          var jqNode = $(this);
          if(jqNode.data("noshake")){
        	  return true;
          }
          jqNode.css({position: 'relative'});
          for (var x=1; x<=intShakes; x++) {
              jqNode.animate({ left: (intDistance * -1) },(((intDuration / intShakes) / 4)))
              .animate({ left: intDistance },((intDuration/intShakes)/2))
              .animate({ left: 0 },(((intDuration/intShakes)/4)));
          }
      });
	  if(callback){
		  callback();
	  }
      return this;
  }
  function processCheckTab(input){
  	var tabpanel=input.closest("div[role='tabpanel']");
  	if(isOk(tabpanel)){
  		var id=tabpanel.attr("id");
  		$(".nav.nav-tabs .nav-item.nav-link[href='#"+id+"']").tab("show");
  	}
  }
  
  function showItCheckFailResult(input,msg){
	  if(input.is("input")&&input.attr("type")=="hidden"){return;}
  		  showFormEleErrorStyle(input,msg,true);
  }
  
  function showItCheckSuccessResult(input){
	  	if(input.is("input")&&input.attr("type")=="hidden"){return;}
    	removeFormEleErrorStyle(input);
	  }
//调用正则表达式验证
  function TestRgexp(re, s) {
      return re.test(s);
  }
  var vcity={ 11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",
          21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",
          33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",
          42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",
          51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",
          63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"
         };
  
var IdCardNoCheckUtil={
		//检查号码是否符合规范，包括长度，类型
		isCardNo : function(card)
		{
		    //身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X
		    var reg = /(^\d{15}$)|(^\d{17}(\d|X)$)/;
		    if(!reg.test(card))
		    {
		        return false;
		    }

		    return true;
		},

		//取身份证前两位,校验省份
		checkProvince  : function(card)
		{
		    var province = card.substr(0,2);
		    if(vcity[province] == undefined)
		    {
		        return false;
		    }
		    return true;
		},

		//检查生日是否正确
		checkBirthday  : function(card)
		{
		    var len = card.length;
		    //身份证15位时，次序为省（3位）市（3位）年（2位）月（2位）日（2位）校验位（3位），皆为数字
		    if(len == '15')
		    {
		        var re_fifteen = /^(\d{6})(\d{2})(\d{2})(\d{2})(\d{3})$/;
		        var arr_data = card.match(re_fifteen);
		        var year = arr_data[2];
		        var month = arr_data[3];
		        var day = arr_data[4];
		        var birthday = new Date('19'+year+'/'+month+'/'+day);
		        return this.verifyBirthday('19'+year,month,day,birthday);
		    }
		    //身份证18位时，次序为省（3位）市（3位）年（4位）月（2位）日（2位）校验位（4位），校验位末尾可能为X
		    if(len == '18')
		    {
		        var re_eighteen = /^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X)$/;
		        var arr_data = card.match(re_eighteen);
		        var year = arr_data[2];
		        var month = arr_data[3];
		        var day = arr_data[4];
		        var birthday = new Date(year+'/'+month+'/'+day);
		        return this.verifyBirthday(year,month,day,birthday);
		    }
		    return false;
		},

		//校验日期
		verifyBirthday  : function(year,month,day,birthday)
		{
		    var now = new Date();
		    var now_year = now.getFullYear();
		    //年月日是否合理
		    if(birthday.getFullYear() == year && (birthday.getMonth() + 1) == month && birthday.getDate() == day)
		    {
		        //判断年份的范围（3岁到100岁之间)
		        var time = now_year - year;
		        if(time >= 3 && time <= 100)
		        {
		            return true;
		        }
		        return false;
		    }
		    return false;
		},

		//校验位的检测
		checkParity  : function(card)
		{
		    //15位转18位
		    card = this.changeFivteenToEighteen(card);
		    var len = card.length;
		    if(len == '18')
		    {
		        var arrInt = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2);
		        var arrCh = new Array('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2');
		        var cardTemp = 0, i, valnum;
		        for(i = 0; i < 17; i ++)
		        {
		            cardTemp += card.substr(i, 1) * arrInt[i];
		        }
		        valnum = arrCh[cardTemp % 11];
		        if (valnum == card.substr(17, 1))
		        {
		            return true;
		        }
		        return false;
		    }
		    return false;
		},

		//15位转18位身份证号
		changeFivteenToEighteen  : function(card)
		{
		    if(card.length == '15')
		    {
		        var arrInt = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2);
		        var arrCh = new Array('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2');
		        var cardTemp = 0, i;  
		        card = card.substr(0, 6) + '19' + card.substr(6, card.length - 6);
		        for(i = 0; i < 17; i ++)
		        {
		            cardTemp += card.substr(i, 1) * arrInt[i];
		        }
		        card += arrCh[cardTemp % 11];
		        return card;
		    }
		    return card;
		}
		  
}

/**
 * 校验身份证号
 */
function checkIdCardNo(card){
	    //是否为空
	    if(card === '')
	    {
	        return '身份证号不能为空';
	    }
	    //校验长度，类型
	    if(IdCardNoCheckUtil.isCardNo(card) === false)
	    {
	        return '身份证号码不正确';
	    }
	    //检查省份
	    if(IdCardNoCheckUtil.checkProvince(card) === false)
	    {
	        return '身份证号码不正确';
	    }
	    //校验生日
	    if(IdCardNoCheckUtil.checkBirthday(card) === false)
	    {
	        return '身份证号码生日不正确';
	    }
	    //检验位的检测
	    if(IdCardNoCheckUtil.checkParity(card) === false)
	    {
	        return '身份证号码校验位不正确';
	    }
	    return null;
  }
//验证规则map
  var jboltFormCheckRuleMap=[
      {type:"number",method:function(value){return (!isNaN(value));}},//数字校验
      {type:"pznumber",method:function(value){return (!isNaN(value)&&value*1>=0);}},//验证正数和0
      {type:"pzint",method:function(value){return ((TestRgexp(/^-?[0-9]\d*$/, value))&&(value*1>=0));}},//验证正数和0
      {type:"pnumber",method:function(value){return (!isNaN(value)&&value*1>0);}},//验证正数
      {type:"int",method:function(value){return TestRgexp(/^-?[0-9]\d*$/, value);}},//整数校验
      {type:"pint",method:function(value){return TestRgexp(/^[0-9]*[1-9][0-9]*$/, value);}},//正整数校验
      {type:"email",method:function(value){return TestRgexp(/\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/, value);}},//Email校验
      {type:"filepath",method:function(value){return TestRgexp(/^([a-zA-Z]){1}:(\\[^\/\\:\*\?\"<>]+)*(\\)?$/, value);}},//URL校验
      {type:"url",method:function(value){return TestRgexp(/^([hH][tT]{2}[pP]:\/\/|[hH][tT]{2}[pP][sS]:\/\/)(([A-Za-z0-9-~]+)\.)+([A-Za-z0-9-~\/])+$/, value);}},//URL校验
      {type:"url_nohttp",method:function(value){return TestRgexp(/^((https|http|ftp|rtsp|mms){0,1}(:\/\/){0,1})(([A-Za-z0-9-~]+)\.)+([A-Za-z0-9-~\/])+$/, value);}},//URL校验
      {type:"date",method:function(value){return TestRgexp(/^[1-9]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$/,value);}},//日期验证 2019-01-01
      {type:"time",method:function(value){return TestRgexp(/^(20|21|22|23|[0-1]\d):[0-5]\d:[0-5]\d$/,value);}},//时间验证 12:59:59
      {type:"time_hm",method:function(value){return TestRgexp(/^(20|21|22|23|[0-1]\d):[0-5]\d$/,value);}},//时间验证 12:59
      {type:"datetime",method:function(value){return TestRgexp(/^[1-9]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\s+(20|21|22|23|[0-1]\d):[0-5]\d:[0-5]\d$/,value);}},//完整日期时间验证 2019-01-01 12:22:23
      {type:"datetime_hm",method:function(value){return TestRgexp(/^[1-9]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\s+(20|21|22|23|[0-1]\d):[0-5]\d$/,value);}},//日期时间验证 2019-01-01 12:22
      {type:"money",method:function(value){return TestRgexp(/(^[1-9]\d*(\.\d{1,2})?$)|(^0(\.\d{1,2})?$)/,value);}},//金额 保留2位小数
      {type:"money_4",method:function(value){return TestRgexp(/(^[1-9]\d*(\.\d{1,4})?$)|(^0(\.\d{1,4})?$)/,value);}},//金额 保留4位 小数
      {type:"phone",method:function(value){return TestRgexp(/^1(3|4|5|6|7|8|9)\d{9}$/,value);}},//手机
      {type:"tel",method:function(value){return TestRgexp(/^(\(\d{3,4}\)|\d{3,4}-|\s)?\d{7,14}$/,value);}},//座机电话
      {type:"zh_cn",method:function(value){return TestRgexp(/[\u4e00-\u9fa5]/,value);}},//中文
      {type:"ip",method:function(value){return TestRgexp(/(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)/,value);}},//ip地址
      {type:"postalcode",method:function(value){return TestRgexp(/\d{6}/,value);}},//邮政编码
      {type:"idcardno",method:function(input,value){return checkIdCardNo(input,value);}},//15or18位身份证
      {type:"letter_num",method:function(value){return TestRgexp(/^\w+$/,value);}},//字母、数字、下划线
      {type:"letter",method:function(value){return TestRgexp(/^[A-Za-z]+$/,value);}},//字母
      {type:"proportion",method:function(value){return TestRgexp(/^[1-9]\d*:[1-9]\d*$/,value);}},//字母
      {type:"proportion_multi",method:function(value){
    	  if(!value){return false;}
    	  if(value.indexOf(":")==-1){
    		  return false;
    	  }
    	  var arr = value.toString().split(":");
    	  if(!isOk(arr)){
    		  return false;
    	  }
    	  var len = arr.length;
    	  if(len<2){
    		  return false;
    	  }
    	  var result = true;
    	  for(var i in arr){
    		  if(arr[i].length==0||isNaN(arr[i])){
    			  result = false;
    		  }
    	  }
    	  return result;
      }},
      {type:"password",method:function(value){
    	  if(!value){return false;}
    	  var len=value.length;
    	  return len>=6&&len<=16;
      }}
  ];
  
  function isDOM(item){
	  var isdom=false;
	  try{
		  item.is(":hidden");
	  }catch(ex){
		  isdom=true;
	  }
	  return isdom;
  }
  /**
   * 将自定义rule添加到jbolt rulemap中
   * @param rules
   * @returns
   */
  function addRuleToJBoltRuleMap(mineRuleMap){
	  if(isArray(mineRuleMap)&&isOk(mineRuleMap)){
		  jboltFormCheckRuleMap.push.apply(jboltFormCheckRuleMap,mineRuleMap);
	  }
  }
  /**
   * 表单元素设置为disabled
   * @param eleObj
   * @returns
   */
  function changeFormControlDisabled(eleObj){
	  var formControl = getRealJqueryObject(eleObj);
	  if(!isOk(formControl)){
		  return;
	  }
	  formControl.attr("disabled","disabled").attr("readonly","readonly");
	  removeFormEleAllStyle(formControl);
  }
  /**
   * 表单元素设置为enable
   * @param eleObj
   * @returns
   */
  function changeFormControlEnable(eleObj){
	  var formControl = getRealJqueryObject(eleObj);
	  if(!isOk(formControl)){
		  return;
	  }
	  formControl.removeAttr("disabled").removeAttr("readonly");
	  removeFormEleAllStyle(formControl);
  }
  /**
   * 表单元素设置为必填
   * @param eleObj
   * @returns
   */
  function changeFormControlRequired(eleObj){
	  changeFormControlRequiredState(eleObj,true);
  }
  /**
   * 表单元素设置为非必填
   * @param eleObj
   * @returns
   */
  function changeFormControlNotRequired(eleObj){
	  changeFormControlRequiredState(eleObj,false);
  }
  /**
   * 表单元素必填状态变更
   * @param eleObj
   * @returns
   */
  function changeFormControlRequiredState(eleObj,isRequired){
	  var formControl = getRealJqueryObject(eleObj);
	  if(!isOk(formControl)){
		  return;
	  }
	  if(isRequired){
		  formControl.data("notnull",true).attr("notnull",true);
		  requiredAndStarIt(formControl);
	  }else{
		  formControl.data("notnull",false).attr("notnull",false);
		  removeRequiredAndStar(formControl);
	  }
	  removeFormEleAllStyle(formControl);
  }
  
  /**
   * 表单验证器
   */
  var FormChecker={
		  init:function(){
			  var that=this;
			  jboltBody.on("input","[data-rule]:not(select)",function(){
				  var input=$(this);
				  if(!input.data("ajax-check-url")){
					  input.data("noshake",true);
					  that.checkIt(input);
					  input.data("noshake",false);
				  }
			  });
			  jboltBody.on("change","select[data-rule]",function(){
				  var select=$(this),target;
				  var type=select.data("select-type");
				  if(type&&type=="select2"){
					  target=select.next().find(".select2-selection");
				  }
					  if(select.data("firstchanged")){
		        		  select.data("noshake",true);
		        		  if(isOk(target)){
		        			  target.data("noshake",true);
		        		  }
						  that.checktype(select);
						  select.data("noshake",false);
						  if(isOk(target)){
							  target.data("noshake",false);
						  }
					  }else{
						  select.data("firstchanged",true);
						  if(isOk(target)){
							  target.data("firstchanged",true);
						  }
					  }
			  });
			  that.initInputCheckWithAjax();
			  
		  },
		  initInputCheckWithAjax:function(){
			  jboltBody.on("blur","input[data-ajax-check-url]",function(e){
				  e.preventDefault();
				  e.stopPropagation();
				  var input=$(this),val=$.trim(input.val()),url=input.data("ajax-check-url");
				  if(val&&url){
					  LayerMsgBox.loading("校验中...",10000);
					  url=actionUrl(url);
					  $.ajax({
							url:url,
							type:"post",
							dataType:"json",
							timeout : 10000,
							data:{"data":val},
							success:function(res){
								if(res.state=="ok"){
									showItCheckSuccessResult(input);
									LayerMsgBox.closeLoading();
								}else{
									if(res.msg){
										if(res.msg=="jbolt_system_locked"){
											showJboltLockSystem();
										}else if(res.msg=="jbolt_nologin"){
											showReloginDialog();
										}else if(res.msg&&res.msg=="jbolt_terminal_offline"){
											showReloginDialog();
											LayerMsgBox.alert("当前用户已在其它终端登录，本机已下线",2);
										}else{
											showItCheckFailResult(input,res.msg);
											LayerMsgBox.error(res.msg,1500);
										}
									}else{
										LayerMsgBox.error("校验异常",1500);
									}
								}
							},
							error:function(){
								showItCheckFailResult(input);
								LayerMsgBox.error("网络通讯异常",1500);
							}
							
						});
					  
				  }
				  return false;
			  });  
		  },
		  checkEle:function(checkObj){
			  return this.checkIt(checkObj,true,true);
		  },
  //检查form表单中的input textarea select
		   checkIt:function(checkObj,focusItIfError,ifEmptyAlwaysShowError){
			   var self=this;
			   var input=getRealJqueryObject(checkObj);
			   var error=true;
		          if(input.is(":disabled")){
		        	  return true;
		          }
		          if(!ifEmptyAlwaysShowError){
			          //增加检测优化
			          if(!input.val()){
			        	  removeFormEleAllStyle(input);
			        	  return true;
			          }
		          }
		          if(input.data("rule")){
		              var flag = self.checktype(input);
//		              var next=input.next().hasClass("input-group-append");
//		              var prev=input.prev().hasClass("input-group-prepend");
		              var ta=input.is("textarea");
		              if (!flag) {
		              	error=false;
		              	
		              	if(input.is("input")&&input.attr("type")=="hidden"){return;}
		              	input.removeClass("is-valid").addClass("is-invalid");	
//		              	input.parents(".form-group").removeClass("bdc-success").addClass("bdc-danger");
		              	
		              	processCheckTab(input);
		                  input.shake(2,10,400);
		                  if(focusItIfError){
		                	  input.focus();
		                  }
		                  
		                  if(input.is("input")&&input.attr("type")=="file"){return;}
		              }else{
		              	error=true;
		              	if(input.is("input")&&input.attr("type")=="hidden"){return;}
		              	input.removeClass("is-invalid").addClass("is-valid");	
//		              	input.parents(".form-group").removeClass("bdc-danger").addClass("bdc-success");
		              	if(input.is("input")&&input.attr("type")=="file"){return;}
		              }
		          }
		          return error;
		   },
  check:function(form){
	  var checkForm=getRealJqueryObject(form);
	  var self=this;
      var error=true;
      checkForm.find("input,textarea,select,[data-rule='checkbox'],[data-rule='radio']").each(function(){
          if(!error){
              return error;
          }
          var input=$(this);
          if(input.is(":disabled")){
        	  return true;
          }
          if(typeof(input.data("rule"))!="undefined"){
              var flag = self.checktype(input);
              if (!flag) {
              	error=false;
//              	
//              	if(input.is("input")&&input.attr("type")=="hidden"){return;}
//              	input.parents(".form-group").removeClass("bdc-success").addClass("bdc-danger");
//              	
//              	processCheckTab(input);
//                  input.shake(2,10,400);
//                  input.focus();
//                  if(input.is("input")&&input.attr("type")=="file"){return;}
                  
              }else{
              	error=true;
//              	if(input.is("input")&&input.attr("type")=="hidden"){return;}
//              	
//              	input.parents(".form-group").removeClass("bdc-danger").addClass("bdc-success");
//              	if(input.is("input")&&input.attr("type")=="file"){return;}
              }
          }
      });
     
      return error;
  },
  checkCheckboxRequired:function(input,tips,show){
	  var self=this;
	  var name=input.data("name");
	 	 if(!name){
	 		 removeFormEleErrorStyle(input);
	 		 return false;
	 	 }
	 	var checked= $("input[type='checkbox'][name='"+name+"']:checked");
	 	if(checked&&checked.length>0){
	 		input.removeClass("bdc-danger").addClass("bdc-success");
	 		 removeFormEleErrorStyle(input);
	 		return true;
	 	}  
	 	input.removeClass("bdc-success").addClass("bdc-danger");
//	 	input.shake(2,10,400);
//	 	input.focus();
	 	if(!tips){
	 		tips="必须选择至少一个选项";
	 	}
	 	self.showMyTipsIfNeed(input,tips,show);
	 	return false;
  },
  checkSelectRequired:function(select,tips,show){
	  var self=this;
	  var value=select.val();
	  var selected=(value&&value!=""&&value!="0"&&value!="-1"&&value.length>0);
	  if(selected){
		  removeFormEleErrorStyle(select);
		  return true;
	  }  
	  if(!tips){
		  tips="必须选择一个选项";
	  }
	  self.showMyTipsIfNeed(select,tips,show);
	  return false;
  },
  checkRadioRequired:function(input,tips,show){
	  var self=this;
	  var name=input.data("name");
	 	 if(!name){
	 		 removeFormEleErrorStyle(input);
	 		 return false;
	 	 }
	 	var checked= $("input[type='radio'][name='"+name+"']:checked");
	 	if(checked&&checked.length>0){
	 		input.removeClass("bdc-danger").addClass("bdc-success");
	 		 removeFormEleErrorStyle(input);
	 		return true;
	 	}  
	 	input.removeClass("bdc-success").addClass("bdc-danger");
//	 	input.shake(2,10,400);
//	 	input.focus();
	 	if(!tips){
	 		tips="必须选择一个选项";
	 	}
	 	self.showMyTipsIfNeed(input,tips,show);
	 	return false;
  },
  //检查给定一个input textarea select的值
  checktype:function(input) {
	  var self=this;
      var type=input.data("rule");
      if(!type){return true;}
      var mytips=input.data("tips"),
        show=input.data("show"),
        notNull=input.data("notnull");
      var value=null;
     if(type=="checkbox"){
    	if(notNull==null||notNull==true||notNull=="true"){
    		return self.checkCheckboxRequired(input,mytips,show);
    	}else{
    		return true;
    	}
    		
     }else  if(type=="radio"){
    	 if(notNull==null||(typeof(notNull)=="boolean" && notNull==true)||notNull=="true"){
    		 return self.checkRadioRequired(input,mytips,show);
    	 }else{
    		 return true;
    	 }
     }else  if(type=="select"){
    	 if(notNull==null||(typeof(notNull)=="boolean" && notNull==true)||notNull=="true"){
    		 return self.checkSelectRequired(input,mytips,show);
    	 }else{
    		 return true;
    	 }
    	 
     }else{
    	 value=input.val();
     }
      
      if (show != null) {
          g(show).innerHTML = "";
      }
      //检测是否为空
  	if(typeof value=="string"){
  		value=value.trim();
  	}
          if (!value) {
              //检查配置是否需要进行非空检测
              if(notNull==null||(typeof(notNull)=="boolean" && notNull==true)||notNull=="true"){
                      if(!mytips){
                    	  if(input.attr("type")=="file"){
                    		  mytips="此文件必须上传";
                    	  }else{
                    		  mytips="必填项";
                    	  }
                      }
                      showFormEleErrorStyle(input,mytips);
                          if (show == null) {
//                        	  LayerMsgBox.error("<span class='j_text_danger'>"+mytips+"</span>",1500);
                        	 /* var pos=input.data("pos");
                        	  if(!pos){pos=2;}
                          	layer.tips(mytips,input,{
                          		tips: [pos, '#d9534f'],
                          		time:4000
                          	});*/
                          } else {
                              g(show).innerHTML = mytips;
                          }
                     
              return false;
              }else{
              return true;
              }
           }
      return self.checkTypes(input,type,value,mytips,show,notNull);

  },
  //检测数据类型
  checkTypes:function(input,type,value,mytips,show,notNull){
	  var self=this;
      //验证多个的时候
      var types=type.split(";");
      var checkFlag=true;
      var error=1;
      var success=2;
      var canNotCheck=3;
      var tsize=types.length;
      for(var i=0;i<tsize;i++){
          var type=types[i];
          var checkResult=self.checkSelf(input,type,value,mytips,show,notNull);
          if(checkResult!=canNotCheck){//判断能否处理 如果处理了 成功了继续下一个type 失败了则直接整个结束
              if(checkResult==success){
                  continue;
              }else{
                  checkFlag=false;
                  break;
              }
          }
          
          //如果上面不能处理 则进入比较处理
          checkResult=self.checkCompare(input,type,value,mytips,show,notNull);
          if(checkResult!=canNotCheck){//判断能否处理 如果处理了 成功了继续下一个type 失败了则直接整个结束
              if(checkResult==success){
                  continue;
              }else{
                  checkFlag=false;
                  break;
              }
          }
          
          
      }
      if(checkFlag){
    	  removeFormEleErrorStyle(input);
      }
      return checkFlag;
  },

  //检测比较
   checkCompare:function(input,type,value,mytips,show,notNull){
	   var self=this;
      var error=1;
      var success=2;
      var canNotCheck=3;
      var selfValue=self.getRealTypeValue(value);
      var compareValue=self.getCompareValue(type);
      if(type.indexOf("len=")!=-1){
          if(value.length==compareValue){
              return success;
          }
          if(!mytips){
        	  mytips="必须输入"+compareValue+"个字符";
          }
          self.showMyTipsIfNeed(input,mytips,show);
          return error;
      }else if(type.indexOf("len>=")!=-1){
          if(value.length>=compareValue){
              return success;
          }
          if(!mytips){
        	  mytips="至少输入"+compareValue+"个字符";
          }
          self.showMyTipsIfNeed(input,mytips,show);
          return error;
      }else if(type.indexOf("len>")!=-1){
          if(value.length>compareValue){
              return success;
          }
          if(!mytips){
        	  mytips="至少输入"+(compareValue+1)+"个字符";
          }
          self.showMyTipsIfNeed(input,mytips,show);
          return error;
      }else  if(type.indexOf("fix=")!=-1){
    	  var xxarr=value.split(".");
    	  //如果没有小数位 并且设置的就是=0 直接true
    	  if(xxarr.length==1&&compareValue==0){return success;}
    	  if(xxarr.length==2&&xxarr[1].length==compareValue){
    		  return success;
    	  }
          if(!mytips){
        	  mytips="小数位必须保留"+compareValue+"位";
          }
          self.showMyTipsIfNeed(input,mytips,show);
          return error;
      }else if(type.indexOf("fix>=")!=-1){
    	  var xxarr=value.split(".");
    	  //如果没有小数位 并且设置的就是>=0 直接true
    	  if(xxarr.length==1&&compareValue==0){return success;}
    	  if(xxarr.length==2&&xxarr[1].length>=compareValue){
    		  return success;
    	  }
          if(!mytips){
        	  mytips="小数位至少保留"+compareValue+"位";
          }
          self.showMyTipsIfNeed(input,mytips,show);
          return error;
      }else if(type.indexOf("fix>")!=-1){
    	  var xxarr=value.split(".");
    	  //如果没有小数位 并且设置的就是>0 直接true
    	  if(xxarr.length==2&&xxarr[1].length>compareValue){
    		  return success;
    	  }
          if(!mytips){
        	  mytips="小数位至少保留"+compareValue+"位";
          }
          self.showMyTipsIfNeed(input,mytips,show);
          return error;
      }else if(type.indexOf("sum=")!=-1){
    	  var xxarr=value.trim().split(":");
    	  var total = 0;
    	  for(var i in xxarr){
    		  total = total+Number(xxarr[i]);
    	  }
    	  if(total==compareValue){
    		  return success;
    	  }
    	  if(!mytips){
    		  mytips="比例值的和必须等于"+compareValue;
    	  }
    	  self.showMyTipsIfNeed(input,mytips,show);
    	  return error;
      }else if(type.indexOf("sum>=")!=-1){
    	  var xxarr=value.trim().split(":");
    	  var total = 0;
    	  for(var i in xxarr){
    		  total = total+Number(xxarr[i]);
    	  }
    	  if(total>=compareValue){
    		  return success;
    	  }
    	  if(!mytips){
    		  mytips="比例值的和必须大于等于"+compareValue;
    	  }
    	  self.showMyTipsIfNeed(input,mytips,show);
    	  return error;
      }else if(type.indexOf("sum>")!=-1){
    	 var xxarr=value.trim().split(":");
    	 var total = 0;
    	 for(var i in xxarr){
    		 total = total+Number(xxarr[i]);
    	 }
    	 if(total>compareValue){
    		 return success;
    	 }
          if(!mytips){
        	  mytips="比例值的和必须大于"+compareValue;
          }
          self.showMyTipsIfNeed(input,mytips,show);
          return error;
      }else if(type.indexOf(">=")!=-1){
      	if(selfValue>=compareValue){
      		return success;
      	}
        if(!mytips){
      	  mytips="值必须>="+compareValue;
        }
      	self.showMyTipsIfNeed(input,mytips,show);
      	return error;
      }else if(type.indexOf("len<=")!=-1){
    	  if(value.length<=compareValue){
    		  return success;
    	  }
    	  if(!mytips){
    		  mytips="最多可输入"+compareValue+"个字符";
    	  }
    	  self.showMyTipsIfNeed(input,mytips,show);
    	  return error;
      }else if(type.indexOf("len<")!=-1){
    	  if(value.length<compareValue){
    		  return success;
    	  }
    	  if(!mytips){
    		  mytips="最多可输入"+(compareValue-1)+"个字符";
    	  }
    	  self.showMyTipsIfNeed(input,mytips,show);
    	  return error;
      }else if(type.indexOf("fix<=")!=-1){
    	  var xxarr=value.split(".");
    	  if(xxarr.length==1){return success;}
    	  if(xxarr.length==2&&xxarr[1].length<=compareValue){
    		  return success;
    	  }
    	  
          if(!mytips){
        	  mytips="小数位最多保留"+compareValue+"位";
          }
          self.showMyTipsIfNeed(input,mytips,show);
          return error;
      }else if(type.indexOf("fix<")!=-1){
    	  var xxarr=value.split(".");
    	  if(xxarr.length==1){return success;}
    	  if(xxarr.length==2&&xxarr[1].length<compareValue){
    		  return success;
    	  }
    	  if(!mytips){
    		  mytips="小数位最多保留"+(compareValue-1)+"位";
    	  }
    	  self.showMyTipsIfNeed(input,mytips,show);
    	  return error;
      }else if(type.indexOf("sum<=")!=-1){
    	  var xxarr=value.trim().split(":");
    	  var total = 0;
    	  for(var i in xxarr){
    		  total = total+Number(xxarr[i]);
    	  }
    	  if(total<=compareValue){
    		  return success;
    	  }
    	  if(!mytips){
    		  mytips="比例值的和必须小于等于"+compareValue;
    	  }
    	  self.showMyTipsIfNeed(input,mytips,show);
    	  return error;
      }else if(type.indexOf("sum<")!=-1){
    	  var xxarr=value.trim().split(":");
    	  var total = 0;
    	  for(var i in xxarr){
    		  total = total+Number(xxarr[i]);
    	  }
    	  if(total<compareValue){
    		  return success;
    	  }
    	  if(!mytips){
    		  mytips="比例值的和必须小于"+compareValue;
    	  }
    	  self.showMyTipsIfNeed(input,mytips,show);
    	  return error;
      }else if(type.indexOf("<=")!=-1){
      	if(selfValue<=compareValue){
      		return success;
      	}
        if(!mytips){
      	  mytips="值必须<="+compareValue;
        }
      	self.showMyTipsIfNeed(input,mytips,show);
      	return error;
      }else if(type.indexOf(">")!=-1){
          if(selfValue>compareValue){
              return success;
          }
          if(!mytips){
        	  mytips="值必须>"+compareValue;
          }
          self.showMyTipsIfNeed(input,mytips,show);
          return error;
      }else if(type.indexOf("<")!=-1){
          if(selfValue<compareValue){
              return success;
          }
          if(!mytips){
        	  mytips="值必须小于"+compareValue;
          }
          self.showMyTipsIfNeed(input,mytips,show);
          return error;
      }else if(type.indexOf("!=")!=-1){
          if(selfValue!=compareValue){
              return success;
          }
          if(!mytips){
        	  mytips="值不能="+compareValue;
          }
          self.showMyTipsIfNeed(input,mytips,show);
          return error;
      }else if(type.indexOf("==")!=-1){
          if(selfValue==compareValue){
        	  removeFormEleErrorStyle(input);
              return success;
          }
          if(!mytips){
        	  if(type.indexOf("#")!=-1){
        		  mytips="值必须等于id为["+type.substring(type.indexOf("#")+1)+"]元素的值";
        	  }else{
        		  mytips="值必须="+compareValue;
        	  }
          }
          self.showMyTipsIfNeed(input,mytips,show);
          return error;
      }

      return canNotCheck;
  },
  //得到正确类型的值
  getRealTypeValue:function(value){
      if(!isNaN(value)){
          return Number(value);
      }
      return value;
  },
  //得到需要比较的值
  getCompareValue:function(type){
	  var self=this;
      if(type.indexOf("#")!=-1){
          var cid=type.substring(type.indexOf("#"));
          return self.getRealTypeValue($(cid).val());
      }else{
    	  if(type.indexOf("len<=")!=-1||type.indexOf("fix<=")!=-1||type.indexOf("sum<=")!=-1||type.indexOf("len>=")!=-1||type.indexOf("fix>=")!=-1||type.indexOf("sum>=")!=-1){
    		  return self.getRealTypeValue(type.substring(5));
    	  }
    	  if(type.indexOf("len=")!=-1||type.indexOf("fix=")!=-1||type.indexOf("sum=")!=-1||type.indexOf("len<")!=-1||type.indexOf("fix<")!=-1||type.indexOf("sum<")!=-1||type.indexOf("len>")!=-1||type.indexOf("fix>")!=-1||type.indexOf("sum>")!=-1){
    		  return self.getRealTypeValue(type.substring(4));
    	  }
      	if(type.indexOf("len")!=-1||type.indexOf("fix")!=-1||type.indexOf("sum")!=-1){
      		type=type.substring(3);
      	}
	      if(type.indexOf("=")!=-1){
	          return self.getRealTypeValue(type.substring(2));
	      }else{
	          return self.getRealTypeValue(type.substring(1));
	      }
      }
  },
  //显示提示信息
   showMyTipsIfNeed:function(input,mytips,show){
      if(mytips!=null){
    	  showFormEleErrorStyle(input,mytips);
                  if (show == null) {
//                      LayerMsgBox.error("<span class='j_text_danger'>"+mytips+"</span>",3000);
                	  /*var pos=input.data("pos");
                	  if(!pos){pos=2;}
                	  layer.tips(mytips,input,{
                		  tips: [pos, '#d9534f'],
                    	  time:4000
                	  });*/
                  } else {
                      g(show).innerHTML = mytips;
                  }
       }else{
    	   removeFormEleErrorStyle(input);
       }
  },
  

  //检测自身
   checkSelf:function(input,type,value,mytips,show,notNull){
	   var self=this;
      var error=1;
      var success=2;
      var canNotCheck=3;
      if(type=="string"){
          return success;
      }
      var process=false;
      for(var i=0;i<jboltFormCheckRuleMap.length;i++){
          if(type==jboltFormCheckRuleMap[i].type){
              process=true;
              var result = jboltFormCheckRuleMap[i].method.call(this,value);
              if (!result) {
            	  if(type=="idcardno"){
            		  self.showMyTipsIfNeed(input);
            		  return success;
            	  }
            	  if(!mytips){
            		  switch (type) {
            		  case "email":
            			  mytips="Email格式不正确";
            			  break;
            		  case "phone":
            			  mytips="手机号格式不正确";
            			  break;
            		  case "tel":
            			  mytips="电话号码格式不正确";
            			  break;
            		  case "money":
            			  mytips="必须为金额(价格)格式 默认最多小数点后保留2位";
            			  break;
            		  case "money_4":
            			  mytips="必须为金额(价格)格式 最多小数点后保留4位";
            			  break;
            		  case "number":
            			  mytips="必须为数字";
            			  break;
            		  case "pnumber":
            			  mytips="必须为正数";
            			  break;
            		  case "pznumber":
            			  mytips="必须为大于等于0的数字";
            			  break;
            		  case "pzint":
            			  mytips="必须为0或正整数";
            			  break;
            		  case "int":
            			  mytips="必须为整数";
            			  break;
            		  case "pint":
            			  mytips="必须为正整数";
            			  break;
            		  case "date":
            			  mytips="必须为日期格式 例：2019-01-01";
            			  break;
            		  case "time":
            			  mytips="必须为时间格式 例：12:59:59";
            			  break;
            		  case "time_hm":
            			  mytips="必须为时间格式 例：12:59";
            			  break;
            		  case "datetime":
            			  mytips="必须为完整日期时间格式 例：2019-01-01 12:59:59";
            			  break;
            		  case "datetime_hm":
            			  mytips="必须为日期时间格式 例：2019-01-01 12:59";
            			  break;
            		  case "idnum":
            			  mytips="必须为18位身份证号 例：370523198901033922";
            			  break;
            		  case "postalcode":
            			  mytips="必须为6位邮政编码 例：6473098";
            			  break;
            		  case "zh_cn":
            			  mytips="必须为中文";
            			  break;
            		  case "letter_num":
            			  mytips="只能输入英文字母、数字和下划线";
            			  break;
            		  case "letter":
            			  mytips="只能输入英文字母";
            			  break;
            		  case "ip":
            			  mytips="必须为IP地址正确格式 例：127.0.0.1";
            			  break;
            		  case "password":
            			  mytips="密码长度必须为6~16个字符";
            			  break;
            		  case "select":
            			  mytips="必须选择一项";
            			  break;
            		  case "proportion":
            			  mytips="比例格式不正确 例 2:10";
            			  break;
            		  case "proportion_multi":
            			  mytips="比例格式不正确 例 2:10 或者 2:5:5";
            			  break;
            		  default:
            			  mytips="格式不正确";
            			  break;
					}
            	  }
            	  self.showMyTipsIfNeed(input,mytips,show);
                  return error;
              }
              
              if(type=="idcardno"){
            	  self.showMyTipsIfNeed(input,result);
            	  return error;
              }
          }
      }
      return process?success:canNotCheck;
   
      
  }
  
  
  }
  /********************************************************************
   ************************表单验证 结束******************************
   ********************************************************************/  
/**
 * 处理点击一个左侧导航的JBoltMenuGroup
 * @param menuGroup
 * @returns
 */
function clickOneJBoltMenuGroup(menuGroup){
		var nav=menuGroup.closest("nav");
		var expansion=null;
		if(JBolt_Enable_Topnav){
			expansion=jboltAdminLeftNavs.find(".jbolt_admin_nav:not(.d-none).expansion");
		}else{
			expansion=jboltAdminLeftNavs.find(".jbolt_admin_nav.expansion");
		}
		if(!menuGroup.hasClass("l1link")){
//			if(jboltAdminLeftNavs.hasClass("allexpansion")){return false;}
			//已经存在展开的 就判断是不是自己 如果不是自己的就关掉
			if(isOk(expansion)){
				var expa,pkey,hpkey;
				expansion.each(function(){
					expa = $(this);
					pkey = expa.data("key");
					hpkey=nav.data("key");
					if(pkey!=hpkey){
						if(!expa.hasClass("single_link")){
							expa.removeClass("expansion");
							expa.find(".jbolt_menu_group i.jbicon.jb-arrowdown").removeClass("haschanged");
						}
					}
				});
			}
			
				var fa=menuGroup.find("i.jbicon.title_arrow");
				if(fa.hasClass("haschanged")){
					nav.removeClass("expansion");
					fa.removeClass("haschanged");
				}else{
					nav.addClass("expansion");
					fa.addClass("haschanged");
				}
		}else{
			if(isOk(expansion)){
				expansion.removeClass("expansion");
				expansion.find(".jbolt_menu_group i.jbicon.jb-arrowdown").removeClass("haschanged");
			}
			nav.addClass("expansion");
			nav.find(".jbolt_menu_group i.jbicon.title_arrow").addClass("haschanged");
		}
		
		setTimeout(function(){
			changeLeftNavScroll();
		}, 400);
		
	
  }
/**
 * 初始化左侧导航菜单
 */
function initAdminLeftNav(){
/*	var value=localStorage.getItem('allexpansion');
	if(value=="true"){
		var jbolt_admin=$(".jbolt_admin_left_navs");
		jbolt_admin.addClass("allexpansion");
		jbolt_admin.find(".jbolt_menu_group i.fa.fa-angle-up").removeClass("fa-angle-up").addClass("fa-angle-down");
	}*/
//	if(self!=top||typeof(JBolt_Left_Nav_width)=="undefined" || !JBolt_Left_Nav_width){return false;}
	if(typeof(JBolt_Left_Nav_width)=="undefined" || !JBolt_Left_Nav_width){return false;}
	var value=localStorage.getItem('jbolt_hideMenu');
	if(value=="true"){
		jboltBody.find(".jbolt_admin_nav.expansion").removeClass("expansion");
		jboltAdmin.addClass("hideMenu");
		jboltBody.find(".jbolt_toggle_Left_nav_btn").addClass("hidden");
		
		jboltAdmin.find(".jbolt_admin_left").css("width","60px");
		jboltAdmin.find(".jbolt_admin_logo_box").css("width","60px");
		jbolt_tabbar.css("left","60px");
		jboltAdminMain.css("left","60px");
		
	}else{
		jboltAdmin.addClass("normalMenu");
		jboltAdmin.find(".jbolt_admin_left").css("width",JBolt_Left_Nav_width+"px");
		jboltAdmin.find(".jbolt_admin_logo_box").css("width",JBolt_Left_Nav_width+"px");
		jbolt_tabbar.css("left",JBolt_Left_Nav_width+"px");
		jboltAdminMain.css("left",JBolt_Left_Nav_width+"px");
	}
	jboltBody.on("click",".jbolt_admin_left_navs li.has_items",function(){
		if(jboltAdmin.hasClass("hideMenu")){
			return false;
		}
		var li=$(this);
		var a=li.find("a");
		var nav=li.next("nav");
		var level=li.data("level");
		var expansionA,pkey;
		var expansion=jboltBody.find(".jbolt_admin_left_navs li.has_items[data-level='"+level+"'].expansion");
		if(isOk(expansion)){
			expansionA=expansion.find("a");
			pkey=expansionA.data("key");
		}
			var hpkey=a.data("key");
			var fa=a.find("i.jbicon.title_arrow");
			fa.toggleClass("haschanged");
			if(isOk(expansion)&&hpkey!=pkey){
				expansion.removeClass("expansion");
				expansionA.find("i.jbicon.title_arrow").removeClass("haschanged");
			}
			li.toggleClass("expansion");
			
			setTimeout(function(){
				changeLeftNavScroll();
			}, 400);
		return false;
	});
	//点击左侧导航中的菜单分组
	jboltBody.on("click",".jbolt_admin_left_navs>a[data-hasurl][data-open-type='1']",function(e){
		var expansion=jboltAdminLeftNavs.find(".jbolt_admin_nav.expansion");
		if(isOk(expansion)){
			expansion.removeClass("expansion");
			expansion.find(".jbolt_menu_group i.jbicon.jb-arrowdown").removeClass("haschanged");
		}
	});
	//点击左侧导航中的菜单分组
	jboltBody.on("click",".jbolt_admin_left_navs .jbolt_menu_group",function(e){
		clickOneJBoltMenuGroup($(this));
	});
	
	jboltBody.on("mouseenter",".jbolt_admin.hideMenu  nav.jbolt_admin_nav .jbolt_menu_group",function(e){
		var nav=$(this).closest("nav.jbolt_admin_nav");
		var menusBox=nav.find("ul.jbolt_admin_menus").first(),
		menusHeight=parseInt(menusBox.outerHeight()),
		top=parseInt(nav.offset().top),
		left=nav.width();
		var maxHeight=menusHeight;
		if(menusHeight-10>jboltWindowHeight-top){
			//如果自身高度超过了
			top=jboltWindowHeight-menusHeight;
			if(top<120){
				top=120;
			}
			maxHeight=jboltWindowHeight-top+60;
		}else{
			top=top+60;
		}
		menusBox.css({"left":left+"px","top":top+"px","max-height":maxHeight+"px"});
		var lh=(jboltWindowHeight-(jboltWindowHeight-top+60-menusBox.outerHeight()));
		var lt=nav.offset().top;
		var bo=(lt+60);
		if(lh<bo){
			if(lt<jboltWindowHeight&&bo>jboltWindowHeight){
				top=top+(bo-lh)-(bo-jboltWindowHeight);
			}else{
				top=top+(bo-lh);
			}
			
		}
		menusBox.css("top",top+"px");
		
	}).on("mouseleave",".jbolt_admin.hideMenu  nav.jbolt_admin_nav",function(e){
		var menusBox=$(this).find("ul.jbolt_admin_menus").first();
		menusBox.css({"max-height":"none","height":"auto"});
	});
	
	initLeftNavScroll();
	
}

function changeLeftNavScroll(){
	initLeftNavScroll();
}
function initLeftNavScroll(){
	/*if(isOk(jboltAdminLeftNavs)){
		jboltAdminLeftNavs.getNiceScroll().remove();
		var cursorcolor="rgba(255,255,255,1)";
		if(jboltAdmin.hasClass("jbolt_style_4")){
			cursorcolor="rgba(0,0,0,0.1)";
		}
		jboltAdminLeftNavs.niceScroll({
			zIndex:1200,
		    cursorcolor: cursorcolor,//#CC0071 光标颜色
		    cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
		    touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
		    cursorwidth: "4px", //像素光标的宽度
		    cursorborder: "1px", // 游标边框css定义
		    cursorborderradius: "4px",//以像素为光标边界半径
		    autohidemode: true //是否隐藏滚动条
		}).resize();
	}*/
	
}

function pjaxErrorProcess(errorCode) {
	JBoltPjaxUtil.sendPjax("admin/pjaxerror",mainPjaxContainerId,{errorCode:errorCode});
}

/**
 * URL 追加Form里的参数
 * @param url
 * @param formObj
 * @returns
 */
function urlWithFormData(url,formObj){
	var datas=formObj.serialize();
	var durl=datas.replaceAll("=undefined",'=');
	if(url.indexOf("?")!=-1){
		url=url+"&"+durl;
	}else{
		url=url+"?"+durl;
	}
	return url;
}

/**
 * url后追加jsonobj
 * @param url
 * @param paras
 * @returns
 */
function urlWithJsonData(url,paras){
	var purl=jsonObjectToUrlParam(paras);
	if(isOk(purl)){
		if(url.indexOf("?")==-1){
			url=url+"?"+purl;
		}else{
			url=url+"&"+purl;
		}
	}
	return url;
}

/**
 * Pjax提交表单
 * @param formEle
 * @returns
 */
function pjaxSubmitForm(formEle){
	var form=getRealJqueryObject(formEle);
	 if(isOk(form)){
		var url=form.attr("action");
		if(url){
			url=urlWithFormData(url,form);
			$.pjax({
				url: url
				, container: mainPjaxContainerId
			});
		}else{
			 LayerMsgBox.alert("Form 未配置action",2);	
		}
	 }else{
		 LayerMsgBox.alert("Form 参数异常",2);
	 }
	return false;
}

var pjaxErrorTpl='<div class="jbolt_page" data-dontchangeleftnav="true">'+
'<div class="jbolt_page_content"><div style="margin: 20px auto;max-width: 500px">'+
'<div class="alert alert-danger">404,您访问的资源不存在!</div></div></div></div>';

/**
 * PJAX工具类
 */
var JBoltPjaxUtil={
		openByType:function(nav){
			var url=nav.attr("href")||nav.data("url");
			if(!url||url.indexOf("javascript")!=-1){
				return false;
			}
			url=actionUrl(url);
			url=processEleUrlByLinkOtherParamEle(nav,url);
			//url=processJBoltTableEleUrlByLinkColumn(nav,url);
			var key=nav.data("key")||randomId();
			var text=$.trim(nav.data("text")||nav.data("title")||nav.attr("title")||nav.text()||"新标签页");
			var openType=nav.data("open-type");
			  if(!openType){
				  openType=1;
			  }
			  var openOptions=nav.data("open-option");
				if(!openOptions || (openOptions && !openOptions.hideLeftNav)){
					jboltAdmin.removeClass("fullMainContainer");
				}
			  if(typeof(openType)=="number"){
				  switch (openType) {
					  case 1://系统默认
						  JBoltPjaxUtil.sendPjax(url,mainPjaxContainerId);
						  break;
					  case 2://iframe
						  activeLeftNavByKey(key);
						  pjaxAdminLeftNavWithIframe(url,openOptions);
						  break;
					  case 3://Dialog
						  activeLeftNavByKey(key);
						  adminLeftNavshowInDialog(text,url,openOptions);
						  break;
					  case 4://jboltLayer
						  activeLeftNavByKey(key);
						  JBoltLayerUtil.openByNav(url,openOptions);
						  break;
					default://默认
						 JBoltPjaxUtil.sendPjax(url,mainPjaxContainerId);
						break;
				  }
			  }else if(typeof(openType)=="string"){
				  switch (openType) {
				  case "iframe"://iframe
					  activeLeftNavByKey(key);
					  pjaxAdminLeftNavWithIframe(url,openOptions);
					  break;
				  case "dialog"://Dialog
					  activeLeftNavByKey(key);
					  adminLeftNavshowInDialog(text,url,openOptions);
					  break;
				  case "jboltlayer"://jboltLayer
					  activeLeftNavByKey(key);
					  JBoltLayerUtil.openByNav(url,openOptions);
					  break;
				  default://默认
					  JBoltPjaxUtil.sendPjax(url,mainPjaxContainerId);
					  break;
			  }
		  }
		},
		/**
		 * 主动发起pjax请求
		 * @param url
		 * @param container
		 * @param extData
		 * @returns
		 */
		sendPjax:function(url, container, extData) {
		  url=actionUrl(url);
		  $.pjax({
			  url: url
			  , container: container?container:mainPjaxContainerId
			  , extData: extData
		  });
	    },
	    /**
	     * 执行pjax前 先把上一个jboltpage closehandler执行
	     */
	    triggerPjaxJBoltPageCloseHandler:function(jboltPage){
	    	var jboltPage =  jboltPage?jboltPage:(mainPjaxContainer.find(".jbolt_page[data-close-handler]"));
			var closeHandler = jboltPage.data("close-handler");
			if(closeHandler){
				if(closeHandler == "removeCommand" && JBoltWS){
					var commands = jboltPage.data("remove-command");
					JBoltWS.removeCommand(commands);
				}else{
					var exe_handler=eval(closeHandler);
					if(exe_handler&&typeof(exe_handler)=="function"){
						exe_handler(jboltPage);
					}
				}
				
			}
	    },initPjaxEvent:function(){
		if(!hasPjax){return;}
		var that = this;
		$.pjax.defaults.timeout=5000;
		jboltBody.on('pjax:start', function() {
			that.triggerPjaxJBoltPageCloseHandler();
			LayerMsgBox.load(3,5000);
			closeJBoltLayer();
			FormDate.hide();
		}).on('pjax:success', function() {
			JBoltPjaxUtil.afterPjax();
		}).on('pjax:end', function(){
			LayerMsgBox.closeLoadNow();
			LayerMsgBox.closeAll();
		}).on("pjax:error",function(e, status, error, options){
			if(!jboltWithTabs){
				e.preventDefault();
				var content = status?(status.responseText||pjaxErrorTpl):pjaxErrorTpl;
				mainPjaxContainer.empty().html(content);
				var key=$(e.relatedTarget).data("key");
				if(key){
					activeLeftNavByKey(key);
				}
				return false;
			}
		}).on('pjax:timeout', function(event) {
			LayerMsgBox.closeAll();
			event.preventDefault();
		}).on("pjax:popstate",function(pjax){//特殊处理浏览器后退事件
			if(pjax.direction=="back"){
				var isPjax=jboltBody.data("ispjax");
				if(!isPjax){
					refreshPjaxContainer();
				}
			}
		});


		//支持表单提交事件无刷新
		jboltBody.on('submit', 'form[data-pjaxsubmit]', function (event) {
			var needcheck=$(this).data("needcheck");
			if(needcheck){
				if(FormChecker.check(this)){
					$.pjax.submit(event, mainPjaxContainerId);
				}
			}else{
				$.pjax.submit(event, mainPjaxContainerId);
			}
			return false;
		});


		//定义pjax链接点击事件
//				jboltBody.pjax('a[data-pjax]:not([target]),[data-pjax] a[data-open-type="1"]:not([target])', mainPjaxContainerId);
		jboltBody.on("click",'a[data-pjax]:not([target]),.jbolt_admin_left_navs a[data-open-type="1"]:not([target])',function(e){
			e.preventDefault();
			e.stopPropagation();
			var nav=$(this),
				openOptions=nav.data("open-option"),
				url=nav.attr("href"),
				key=nav.data("key");
			if(key){
				activeLeftNavByKey(key);
			}
			if(!openOptions || (openOptions && !openOptions.hideLeftNav)){
				jboltAdmin.removeClass("fullMainContainer");
			}
			JBoltPjaxUtil.sendPjax(url,mainPjaxContainerId);
			return false;
		});
		jboltBody.on("click",".jbolt_admin_left_navs a[data-open-type='2']",function(e){
			e.preventDefault();
			e.stopPropagation();
			var nav=$(this),
				openOptions=nav.data("open-option"),
				url=nav.attr("href"),
				key=nav.data("key");
			if(key){
				activeLeftNavByKey(key);
			}
			if(!openOptions || (openOptions && !openOptions.hideLeftNav)){
				jboltAdmin.removeClass("fullMainContainer");
			}
			pjaxAdminLeftNavWithIframe(url,openOptions);
			return false;
		});
		jboltBody.on("click",".jbolt_admin_left_navs a[data-open-type='3']",function(e){
			e.preventDefault();
			e.stopPropagation();
			var nav=$(this),
				openOptions=nav.data("open-option"),
				text=$.trim(nav.text()),
				url=nav.attr("href"),
				key=nav.data("key");
			if(key){
				activeLeftNavByKey(key);
			}
			if(!openOptions || (openOptions && !openOptions.hideLeftNav)){
				jboltAdmin.removeClass("fullMainContainer");
			}
			adminLeftNavshowInDialog(text, url, openOptions)
			return false;
		});
		jboltBody.on("click",".jbolt_admin_left_navs a[data-open-type='4']",function(e){
			e.preventDefault();
			e.stopPropagation();
			var nav=$(this),
				openOptions=nav.data("open-option"),
				url=nav.attr("href"),
				key=nav.data("key");
			if(key){
				activeLeftNavByKey(key);
			}
			if(!openOptions || (openOptions && !openOptions.hideLeftNav)){
				jboltAdmin.removeClass("fullMainContainer");
			}
			JBoltLayerUtil.openByNav(url,openOptions);
			return false;
		});
	},
		/**
		 * 初始化 后台管理的Pjax
		 * @returns
		 */
		initAdminPjax:function (){
			this.initPjaxEvent();
			//pjax需要处理的
			var currentPageLoadByPjax=jboltBody.data("ispjax");
			if(!currentPageLoadByPjax){
				JBoltPjaxUtil.afterPjax();
			}
		},
		/**
		 * pajx end执行后 调用 初始化pjax内容里的一些组件
		 * @returns
		 */
		afterPjax:function(){
			pjaxLoadRequirePluginAndInit();
			//加载界面后需要选中打开左侧选项卡
			$('.tooltip.show').remove();
			initOpenLeftNav();
			SelectUtil.initAutoSetValue(mainPjaxContainer);
			FileUploadUtil.init(mainPjaxContainer);
			ImgUploadUtil.init(mainPjaxContainer);
			SwitchBtnUtil.init(mainPjaxContainer);
			FormDate.init(mainPjaxContainer);
			CityPickerUtil.init(mainPjaxContainer);
			LayerTipsUtil.init(mainPjaxContainer);
			HtmlEditorUtil.init(mainPjaxContainer);
			MasterSlaveUtil.init(mainPjaxContainer);
			ImageViewerUtil.init(mainPjaxContainer);
			RadioUtil.init(mainPjaxContainer);
			CheckboxUtil.init(mainPjaxContainer);
			AutocompleteUtil.init(mainPjaxContainer);
			Select2Util.init(mainPjaxContainer);
			SelectUtil.init({parent:mainPjaxContainer});
			MultipleFileInputUtil.init(mainPjaxContainer);
			AjaxPortalUtil.init(mainPjaxContainer);
			JSTreeUtil.init(mainPjaxContainer);
			JBoltInputUtil.init(mainPjaxContainer);
			RangeSliderUtil.init(mainPjaxContainer);
			JBoltInputWithClearBtnUtil.init(mainPjaxContainer);
			JBoltInputWithCalculatorUtil.init(mainPjaxContainer);
			initToolTip(mainPjaxContainer);
			initPopover(mainPjaxContainer);
			JBoltTableUtil.init(mainPjaxContainer);
			JBoltTreeTableUtil.init(mainPjaxContainer);
			JBoltTabViewUtil.initUI(mainPjaxContainer);
			TextareaUtil.initUI(mainPjaxContainer);
			findRequiredAndStarIt(mainPjaxContainer);
			
			var topnavs=$(".jbolt_admin_main_top>ul.jbolt_admin_topnavs>li.active");
			if(isOk(topnavs)){
				var topnavId=topnavs.data("id");
				activeTopnavFirstLeftMenusNotSingleLink(topnavId);
			}
		}
}


/**
 * 刷新当前显示的选项卡内容区域
 * @returns
 */
function refreshCurrentTabContent(){
	var tabContent=JBoltTabUtil.getCurrentTabContent();
	if(tabContent&&tabContent.length==1){
		LayerMsgBox.load(3);
		var iframe=tabContent.find("iframe.jbolt_main_iframe");
		if(isOk(iframe)){
			iframe.attr("src",iframe.attr("src"));
		}else{
			tabContent.ajaxPortal(true,null,true,function(){
				LayerMsgBox.closeLoadNow();
			});
		}
	}
}
/**
 * 修改当前tab选项卡标题
 * @param title
 * @returns
 */
function changeCurrentTabTitle(title){
	JBoltTabUtil.changeCurrentTabTitle(title);
}
/**
 * 修改指定tab选项卡标题
 * @param tab
 * @param title
 * @returns
 */
function changeTabTitle(tab,title){
	JBoltTabUtil.changeTabTitle(tab,title);
}


function refreshTheTabContent(tabContent){
	if(tabContent&&tabContent.length==1){
		LayerMsgBox.load(3);
		var iframe=tabContent.find("iframe.jbolt_main_iframe");
		if(isOk(iframe)){
			iframe.attr("src",iframe.attr("src"));
		}else{
			tabContent.ajaxPortal(true,null,true,function(){
				LayerMsgBox.closeLoadNow();
			});
		}
	}
}



function isIE() { //ie?
	 if (!!window.ActiveXObject || "ActiveXObject" in window)
	  return true;
	  else
	  return false;
}
/**
 * 刷新主区域
 * @returns
 */
function refreshPjaxContainer(ele){
	if(ele){
		var eleObj=getRealJqueryObject(ele);
		if(isOk(eleObj)){
			disposeTooltip(eleObj);
		}
	}
	if(window.self!=window.top){
		reloadCurrentPage();
	}else{
//		var withTabs=isWithtabs();
		if(jboltWithTabs){
			refreshCurrentTabContent();
		}else{
			
			var iframe=mainPjaxContainer.find("iframe.jbolt_main_iframe");
			if(isOk(iframe)){
				iframe.attr("src",iframe.attr("src"));
			}else{
				if(isIE()){
					reloadCurrentPage();
				}else{
					$.pjax.reload(mainPjaxContainerId);
				}
			}
		}
	}
	

}
/**
 * 检测打开的pjax页面 打开打开指定的nav
 * @returns
 */
function initOpenLeftNav(){
//	var withTabs=isWithtabs();
	var jbolt_page=null;
	if(jboltWithTabs){
		var tabContent=JBoltTabUtil.getCurrentTabContent();
		if(tabContent&&tabContent.length==1){
			jbolt_page=tabContent.find("div.jbolt_page[data-key]:first");
		}
	}else{
		jbolt_page=$("div.jbolt_page[data-key]:first");
	}
	if(jbolt_page&&jbolt_page.length==1){
		var key=jbolt_page.data("key");
		var dontchangeleftnav=jbolt_page.data("dontchangeleftnav");
		if(!dontchangeleftnav){
			openLeftNav(key);
		}
	}
	
}
/**
 * 通过data-key绑定 设置做到导航active状态
 * @param key
 * @returns
 */
function activeLeftNavByKey(key){
	var activeItem=jboltAdminLeftNavs.find("nav.jbolt_admin_nav a.active");
	if(activeItem.data("key")==key){
		return false;
	}
	activeItem.removeClass("active");
	var expansion;
	if(jboltHasTopNav){
		expansion=$(".jbolt_admin_nav:not(.d-none).expansion");
		if(isOk(expansion)){
			expansion.removeClass("expansion");
			expansion.find(".jbolt_menu_group i.jbicon.jb-arrowdown.haschanged").removeClass("haschanged");
		}
		jboltAdminLeftNavs.find(".jbolt_admin_nav:not(.d-none) li.has_items").removeClass("expansion").find("i.jbicon.jb-arrowdown.haschanged").removeClass("haschanged");
	}else{
		expansion=$(".jbolt_admin_nav.expansion");
		if(isOk(expansion)){
			expansion.removeClass("expansion");
			expansion.find(".jbolt_menu_group i.jbicon.jb-arrowdown.haschanged").removeClass("haschanged");
		}
		jboltAdminLeftNavs.find("li.has_items").removeClass("expansion").find("i.jbicon.jb-arrowdown.haschanged").removeClass("haschanged");
	}
	
	var item=jboltAdminLeftNavs.find("nav.jbolt_admin_nav a[data-key='"+key+"']").first();
	/*if(isOk(item)){
		item.addClass("active");
		var nav=item.closest("nav");
		if(isOk(nav)){
			var ul=item.closest(".jbolt_admin_menus");
			var li=ul.prev("li.has_items")
			if(isOk(li)){
				li.addClass("expansion");
			}
			nav.addClass("expansion");
			nav.find(".jbolt_menu_group i.jbicon.jb-arrowdown").addClass("haschanged");
			
		}
	}
	*/
	if(isOk(item)){
		item.addClass("active");
		var nav=item.closest("nav");
		if(isOk(nav)){
			nav.addClass("expansion");
			nav.find(".jbolt_menu_group i.jbicon.jb-arrowdown:not(.haschanged)").addClass("haschanged");
			var li=item.closest("li");
			processParentUlAndHasItemsLiExp(li,nav);
//			var jboltWithTabs=isWithtabs();
			if(jboltWithTabs){
				var topnavId=nav.data("topnavid");
				//判断有没有顶部菜单
				var the_topnav=$(".jbolt_admin_main_top>ul.jbolt_admin_topnavs>li[data-id='"+topnavId+"']");
				if(isOk(the_topnav)){
					//如果有顶部菜单了就需要切换选中nav所在
					activeTopnav(the_topnav);
				}
			}

			var openOptions=item.data("open-option");
			if(openOptions && openOptions.hideLeftNav){
				hideLeftAndFullMainConainer();
			}else{
				jboltAdmin.removeClass("fullMainContainer");
			}
		}
	}
	
}
/**
 * 去掉前缀第一个字符/
 * @param url
 * @returns
 */
function actionUrl(url){
	if(url&&url.charAt(0)=='/'&&url.charAt(1)!='/') {
		url=url.substring(1);
	}
	if(url&&url.indexOf("\\")!=-1){
		url=url.replace(/\\/g,"/");
	}
	return url;
}
/**
 * 打开指定URL的左侧nav
 * @param URL
 * @returns
 */
function openLeftNav(url){
	url=actionUrl(url);
	var activeItem=jboltAdminLeftNavs.find("nav.jbolt_admin_nav a.active");
	var activeSingleLink=jboltAdminLeftNavs.find("nav.jbolt_admin_nav.single_link.expansion");
	if(isOk(activeSingleLink) && activeItem.data("key")!=activeSingleLink.data("key")){
		activeSingleLink.removeClass("expansion");
	}
	if(activeItem.attr("href")==url){
		return false;
	}
	activeItem.removeClass("active");
	var expansion;
	if(jboltHasTopNav){
		expansion=$(".jbolt_admin_nav:not(.d-none).expansion");
		expansion.removeClass("expansion");
		expansion.find(".jbolt_menu_group i.jbicon.jb-arrowdown.haschanged").removeClass("haschanged");
		jboltAdminLeftNavs.find(".jbolt_admin_nav:not(.d-none) li.has_items").removeClass("expansion").find("i.jbicon.jb-arrowdown.haschanged").removeClass("haschanged");
	}else{
		expansion=$(".jbolt_admin_nav.expansion");
		expansion.removeClass("expansion");
		expansion.find(".jbolt_menu_group i.jbicon.jb-arrowdown.haschanged").removeClass("haschanged");
		jboltAdminLeftNavs.find("li.has_items").removeClass("expansion").find("i.jbicon.jb-arrowdown.haschanged").removeClass("haschanged");
	}
	
	
	var item=jboltAdminLeftNavs.find("nav.jbolt_admin_nav a[href='"+url+"']").first();
	if(isOk(item)){
		item.addClass("active");
		var nav=item.closest("nav");
		if(isOk(nav)){
			nav.addClass("expansion");
			nav.find(".jbolt_menu_group i.jbicon.jb-arrowdown:not(.haschanged)").addClass("haschanged");
			var li=item.closest("li");
			processParentUlAndHasItemsLiExp(li,nav);
			var topnavId=nav.data("topnavid");
			//判断有没有顶部菜单
			var the_topnav=$(".jbolt_admin_main_top>ul.jbolt_admin_topnavs>li[data-id='"+topnavId+"']");
			if(isOk(the_topnav)){
				//如果有顶部菜单了就需要切换选中nav所在
				activeTopnav(the_topnav);
			}
			
		}
	}
	
	
	
	
}
function processParentUlAndHasItemsLiExp(li,nav){
	var level=li.data("level");
	if(level<=2){return false;}
	var ul=li.closest("ul.jbolt_admin_menus");
	if(isOk(ul)){
		var hasItemsLi=ul.prev("li.has_items");
		if(isOk(hasItemsLi)){
			hasItemsLi.addClass("expansion").find("i.jbicon.jb-arrowdown:not(.haschanged)").addClass("haschanged");
			processParentUlAndHasItemsLiExp(hasItemsLi,nav);
		}
	}
}

/**
 * pjax前执行
 * @returns
 */
function pjaxJBoltPageCloseHandler(){
	var jboltPage=mainPjaxContainer.find(".jbolt_page[data-close-handler]");
	if(jboltPage&&jboltPage.length==1){
		jboltPage.on()
	}
}

/**
 * pjax后执行导入依赖插件
 * @returns
 */
function pjaxLoadRequirePluginAndInit(){
	var jboltPage=mainPjaxContainer.find(".jbolt_page[data-require-plugin],.jbolt_page[data-init-handler]");
	if(jboltPage&&jboltPage.length==1){
		jboltPageLoadRequirePluginAndInit(jboltPage);
	}
}
/**
 * 非pjax加载后后执行导入依赖插件
 * @returns
 */
function pageLoadRequirePluginAndInit(){
	var jboltPage=jboltBody.find(".jbolt_page[data-require-plugin],.jbolt_page[data-init-handler]");
	if(jboltPage&&jboltPage.length==1){
		jboltPageLoadRequirePluginAndInit(jboltPage);
	}
}

/**
 * 处理掉已经加载的插件
 * @param plugins
 * @returns
 */
function processLoadedPlugins(plugins){
	var newPlugins=new Array();
	for(var i in plugins){
		if(!loadedPlugins[plugins[i]]){
			newPlugins.push(plugins[i]);
		}
	}
	return newPlugins;
}
/**
 * 加载第三方插件
 * @param plugins
 * @param callback
 * @returns
 */
function loadJBoltPlugin(plugins,callback){
	if(plugins&&plugins.length>0){
		if(typeof(plugins)=="string"){
			if(plugins.indexOf(",")!=-1){
				plugins=plugins.split(",");
			}else{
				plugins=[plugins];
			}
		}
		if(plugins && isArray(plugins) && plugins.length>=2){
			plugins=JBoltArrayUtil.unique(plugins);
		}
		//去看看已加载列表中是否存在了 如果存在了直接过
		plugins=processLoadedPlugins(plugins);
		if(!plugins||plugins.length==0){
			if(callback){
				var exe_handler=eval(callback);
				if(exe_handler&&typeof(exe_handler)=="function"){
					exe_handler();
				}
			}
			return false;
		}
		var plugin,pluginName;
		var cssArr,jsArr,plen=plugins.length;
		var exeCallback=function(docallback){
			//执行callback
			var exe_handler=eval(docallback);
			if(exe_handler&&typeof(exe_handler)=="function"){
				exe_handler();
			}
		}
		var count=0;
		var loadJsCss=function(pluginName){
			var lplugin=jboltPlugins[pluginName];
			if(lplugin){
				var cssArr=lplugin['css'];
				var jsArr=lplugin['js'];
				if(isOk(cssArr)){
					AssetsLazyLoad.css(cssArr,function(){
						if(isOk(jsArr)){
							AssetsLazyLoad.js(jsArr,function(){
								count++;
								//加载后 push进去
								loadedPlugins[pluginName]=lplugin;
							});
						}else{
							count++;
							//加载后 push进去
							loadedPlugins[pluginName]=lplugin;
						}
					});
				}else{
					if(isOk(jsArr)){
						AssetsLazyLoad.js(jsArr,function(){
							count++;
							//加载后 push进去
							loadedPlugins[pluginName]=lplugin;
						});
					}
				}
			}else{
				count++;
			}
		}
		for(var i in plugins){
			loadJsCss(plugins[i]);
		}
		if(callback){
			var timer=setInterval(function(){
				if(count==plugins.length){
					clearInterval(timer);
					exeCallback(callback);
				}
			}, 10);
		}
	}
}

function jboltPageLoadRequirePluginAndInit(jboltPage){
	var requirePlugin=jboltPage.data("require-plugin");
	var initHandler=jboltPage.data("init-handler");
	if(requirePlugin){
		var plugins=requirePlugin.split(",");
		loadJBoltPlugin(plugins,initHandler);
	}else{
		var exe_handler=eval(initHandler);
		if(exe_handler&&typeof(exe_handler)=="function"){
			exe_handler(jboltPage);
		}
	}
	

}
/**
 * ajaxPortal加载后执行引入第三方插件
 * @param parent
 * @returns
 */
function portalLoadRequirePluginAndInit(parentEle){
	var parent=getRealParentJqueryObject(parentEle);
	if(!isOk(parent)){return false;}
	var jboltPage=parent.find(".jbolt_page[data-require-plugin]");
	if(jboltPage&&jboltPage.length==1){
		jboltPageLoadRequirePluginAndInit(jboltPage);
	}
	
}



/**
 * 分页提交form
 */
function jboltPageSubmitForm(pbox,pager,form,page){
	var pages=pager.closest(".pages");
	  if(!page){
		  var input=pages.find("#gonu");
		  if(input&&input.length>0){
			  page=input.val();
		  }else{
			  page=1;
		  }
	  }
	  var pageSize=pages.find("#pageSize").val();
	  form.append('<input type="hidden" name="page" value="'+page+'"/>')
	  form.append('<input type="hidden" name="pageSize" value="'+pageSize+'"/>')
//	  var action=baseUrl+"?page="+page;
//	  form.attr("action",action);
	  form.submit();
}
/**
 * 分页组件初始化
 * @param id
 * @param totalPage
 * @param pageNumber
 * @param formId
 * @returns
 */
function initPage(id,totalPage,pageNumber,formId){
//	var withTabs=isWithtabs();
	var pbox=null;
	var pager=$("#"+id);
	var pagerParent=pager.closest(".pages");
	var ajaxPortal=pager.closest("[data-ajaxportal]");
	if(ajaxPortal&&ajaxPortal.length==1){
		pbox=ajaxPortal;
	}else{
		if(jboltWithTabs){
			pbox=JBoltTabUtil.getCurrentTabContent();
		}else{
			pbox=mainPjaxContainer
			var inDialog=!(pbox&&pbox.length==1);
			if(inDialog){
				pbox=$("body .jbolt_page");
				var notNormalPage=!(pbox&&pbox.length==1);
				if(notNormalPage){
					pbox=jboltBody;
				}
			}
		}
	}
	if(!pbox){
		return false;
	}
	var form=pbox.find("#"+formId);
	var baseUrl = form.attr("action");
	loadJBoltPlugin(['pagination'], function(){
		pager.pagination(totalPage,{
			   num_edge_entries:1,
			   current_page:(pageNumber-1),
				callback:function(index,ct){
					if(isNaN(index)==false){
						var page=index+1;
						jboltPageSubmitForm(pbox,pager,form,page);
						return false;
					}
				}
		   });
		  
		pagerParent.find("#gonu").one("keydown",function(e){
			   if(e.keyCode==109||e.keyCode==189){
				   return false;
			   }
		   });
		pagerParent.find(".page-btn").one("click",function(){
			jboltPageSubmitForm(pbox,pager,form);
		});
		pagerParent.find("#pageSize").one("change",function(){
			jboltPageSubmitForm(pbox,pager,form,1);
		   });
	});
	
}
/**
 * topnav切换active状态
 * @param topnav
 * @returns
 */
function activeTopnav(topnav){
	var activeLi=topnav.parent().find("li.active");
	var hasActive=isOk(activeLi);
	var topnavId=topnav.data("id");
	if(hasActive&&topnavId==activeLi.data("id")){
		return;
	}
	if(hasActive){
		activeLi.removeClass("active");
	}
	topnav.addClass("active");
	jboltAdminLeftNavs.find("nav[data-topnavid='"+topnavId+"']").removeClass("d-none");
	jboltAdminLeftNavs.find("nav:not([data-topnavid='"+topnavId+"'])").addClass("d-none");
	activeTopnavFirstLeftMenusNotSingleLink(topnavId);
}

function activeTopnavFirstLeftMenusNotSingleLink(topnavId){
	var expansion_nav=jboltAdminLeftNavs.find("nav:not(.d-none).expansion");
	if(!isOk(expansion_nav)){
		var notExFirstNav=jboltAdminLeftNavs.find("nav:not(.d-none):first");
		if(isOk(notExFirstNav)){
			var menuGroup=notExFirstNav.find(".jbolt_menu_group");
			if(isOk(menuGroup)){
				clickOneJBoltMenuGroup(menuGroup);
			}
		}
	}
}
/**
 * 顶部导航判断如果没有选中就默认选中第一个
 * @returns
 */
function jboltAdminLayoutInitTopNavFirst(){
	if(JBolt_Enable_Topnav){
		//说明是有topnav 那就看看有没有选中
		var activetopnavs=$(".jbolt_admin_main_top>ul.jbolt_admin_topnavs>li.active");
		if(!isOk(activetopnavs)){
			var firstTopnav=$(".jbolt_admin_main_top>ul.jbolt_admin_topnavs>li:first");
			if(isOk(firstTopnav)){
				activeTopnav(firstTopnav);
			}
		}
	}
	
}
/**
 * 使用sessionStorage 去处理active tab
 * @returns
 */
function useSessionStorageToActiveTab(){
	var tab,hasActive=false;
	for(var i in jbolt_tabs_array){
		tab=jbolt_tabs_array[i];
		if(typeof(tab.active)=="boolean" && tab.active){
			hasActive = true;
			break;
		}
	}
	return hasActive; 
}
/**
 * 后台首页index加载后执行判断是否有topnav 有的话就找第一个里的第一个 没有就找全局第一个
 * @returns
 */
function jboltAdminIndexInitFirstLeftNav(){
//	var ok = useHashToActiveTab();
//	if(ok){
//		return;
//	}
	
	var ok = useSessionStorageToActiveTab();
	if(ok){
		return;
	}
	var nav=null;
	var topnavId=0;
	if(JBolt_Enable_Topnav){
		var topnavs=$(".jbolt_admin_main_top>ul.jbolt_admin_topnavs>li.active");
		if(!isOk(topnavs)){
			topnavs=$(".jbolt_admin_main_top>ul.jbolt_admin_topnavs>li:first");
		}
		if(isOk(topnavs)){
			topnavId=topnavs.data("id");
			nav=$(".jbolt_admin_left_navs .jbolt_admin_nav[data-topnavid='"+topnavId+"']").first();
		}
		if(!isOk(nav)){
			nav=$(".jbolt_admin_left_navs .jbolt_admin_nav").first();
			topnavId=nav.data("topnavid")||0;
		}
	}else{
		nav=$(".jbolt_admin_left_navs .jbolt_admin_nav").first();
	}
	if(isOk(nav)){
		var a=nav.find("a[data-hasurl]").first();
		if(a&&a.length>0){
			a.trigger("click");
			if(JBolt_Enable_Topnav&&topnavId==0){
				var topnav=$(".jbolt_admin_main_top>ul.jbolt_admin_topnavs>li[data-id='0']");
				if(isOk(topnav)){
					activeTopnav(topnav);
				}
			}
		}
	}
}
/**
 * 顶部topnav点击切换左侧menu事件处理
 * @returns
 */
function toggleLeftMenuEventByTopnav(){
	jboltBody.on("click",".jbolt_admin_main_top>ul.jbolt_admin_topnavs>li",function(){
		activeTopnav($(this));
	});
	//处理是否存在没有分配顶部分模块的菜单
	var othertopnav=$(".jbolt_admin_main_top>ul.jbolt_admin_topnavs>li[data-id='0']");
	if(isOk(othertopnav)){
		var notassignmenus=jboltAdminLeftNavs.find("nav[data-topnavid='0']");
		if(isOk(notassignmenus)){
			othertopnav.removeClass("d-none");
		}
	}
}
/**
 * 切换左侧导航菜单
 * @returns
 */
function toggleMenuEvent(){
	jboltBody.on("click",".jbolt_toggle_Left_nav_btn",function(){
		var toggleBtn=$(this);
		toggleBtn.toggleClass("hidden");
		disposeTooltip(toggleBtn);
		$(".jbolt_admin_nav.expansion").removeClass("expansion");
		jboltAdmin.toggleClass("hideMenu").toggleClass("normalMenu");
		jboltAdminLeftNavs.find("nav.jbolt_admin_nav a.active").removeClass("active");
		var hideMenu=jboltAdmin.hasClass("hideMenu");
		if(hideMenu){
			jboltAdmin.find(".jbolt_admin_left").css("width","60px");
			jboltAdmin.find(".jbolt_admin_logo_box").css("width","60px");
			jbolt_tabbar.css("left","60px");
			jboltAdminMain.css("left","60px");
		}else{
			jboltAdmin.find(".jbolt_admin_left").css("width",JBolt_Left_Nav_width+"px");
			jboltAdmin.find(".jbolt_admin_logo_box").css("width",JBolt_Left_Nav_width+"px");
			jbolt_tabbar.css("left",JBolt_Left_Nav_width+"px");
			jboltAdminMain.css("left",JBolt_Left_Nav_width+"px");
			
			initOpenLeftNav();
		}
		localStorage.setItem('jbolt_hideMenu', hideMenu);
		/*setTimeout(function(){
			changeLeftNavScroll();
		}, 400);*/
	});
}

function hideLeftMenu(){
	var toggleBtn=$(".jbolt_toggle_Left_nav_btn");
	toggleBtn.addClass("hidden");
	disposeTooltip(toggleBtn);
	$(".jbolt_admin_nav.expansion").removeClass("expansion");
	jboltAdmin.addClass("hideMenu").removeClass("normalMenu");
	jboltAdminLeftNavs.find("nav.jbolt_admin_nav a.active").removeClass("active");
	jboltAdmin.find(".jbolt_admin_left").css("width","60px");
	jboltAdmin.find(".jbolt_admin_logo_box").css("width","60px");
	jbolt_tabbar.css("left","60px");
	jboltAdminMain.css("left","60px");
	localStorage.setItem('jbolt_hideMenu', true);
}

function hideLeftAndFullMainConainer(){
	var toggleBtn=$(".jbolt_toggle_Left_nav_btn");
	toggleBtn.addClass("hidden");
	disposeTooltip(toggleBtn);
	$(".jbolt_admin_nav.expansion").removeClass("expansion");
	jboltAdmin.addClass("hideMenu fullMainContainer").removeClass("normalMenu");
	jboltAdminLeftNavs.find("nav.jbolt_admin_nav a.active").removeClass("active");
	jboltAdmin.find(".jbolt_admin_logo_box").css("width","60px");
	localStorage.setItem('jbolt_hideMenu', true);
}

/**
 * 自动触发window的resize
 * @returns
 */
/*function windowResize(){
	 if(document.createEvent) {
         var event = document.createEvent("HTMLEvents");
         event.initEvent("resize", true, true);
         window.dispatchEvent(event);
     } else if(document.createEventObject) {
         window.fireEvent("onresize");
     }
}*/
/**
 * 返回上一页
 * @returns
 */
function goback(){
	history.go(-1);
}
/**
 * 刷新当前页
 * @returns
 */
function reloadCurrentPage(){
	var url=self.location.href;
	/*if(url.indexOf("globalconfig")!=-1){
		self.location.href="admin";
	}else{*/
		history.go(0);
//	}
}
/**
 *退出
 * @returns
 */
function userLogout(){
	LayerMsgBox.confirm("确认注销并退出系统吗？",function(){
		self.location.href="admin/logout";
	});
	return false;
}

/**
 * 隐藏dialog按钮
 * @returns
 */
function hideParentLayerDialogBtn(index){
	if(index==0||index==1){
		parent.$(".layui-layer-btn .layui-layer-btn"+index).hide();
	}else{
		hideAllParentLayerDialogBtn();
	}
}
/**
 * 隐藏Layer上的所有按钮
 * @returns
 */
function hideAllParentLayerDialogBtn(){
	parent.$(".layui-layer-btn").hide();
}
/**
 * 绑定一个按钮给dialog的OK btn
 * @param keycode
 * @returns
 */
function bindKeycodeForDialogOkBtn(keycode){
	if(window.self!=window.top){
		parent.bindKeycodeForDialogOkBtn(keycode);
	}
	jboltBody.on("keyup",function(e){
		if(e.keyCode==keycode){
			if(window.self!=window.top){
				parent.$(".layui-layer-btn .layui-layer-btn0").click();
			}else{
				$(".layui-layer-btn .layui-layer-btn0").click()
			}
		}
	});
}
/**
 * 绑定回车键 给dialog okBtn
 * @param keycode
 * @returns
 */
function bindEnterForDialogOkBtn(keycode){
	bindKeycodeForDialogOkBtn(13);
}
/**
 * 绑定按键给dialog的close btn
 * @param keycode
 * @returns
 */
function bindKeycodeForDialogCloseBtn(keycode){
	if(window.self!=window.top){
		parent.bindKeycodeForDialogCloseBtn(keycode);
	}
	jboltWindow.on("keyup",function(e){
		if(e.keyCode==keycode){
			if(window.self!=window.top){
				parent.$(".layui-layer-btn .layui-layer-btn1").click();
			}else{
				$(".layui-layer-btn .layui-layer-btn1").click();
			}
			
		}
	});
}
/**
 * 绑定esc给dialog的close btn
 * @returns
 */
function bindEscForDialogCloseBtn(){
	bindKeycodeForDialogCloseBtn(27);
}
/**
 * 绑定dialog上的按钮和
 * @param btns
 * @returns
 */
function bindKeycodeForDialogBtnByBtnId(btns){
	var len=btns.length;
	if(window.self!=window.top){
		parent.bindKeycodeForDialogBtnByBtnId(btns);
	}
	jboltWindow.on("keyup",function(e){
		var btn;
		for(var i=0;i<len;i++){
			btn=btns[i];
			if(btn.key==e.keyCode){
				if(window.self!=window.top){
					parent.$(".layui-layer-btn").find("a#"+btn.id).click();
				}else{
					$(".layui-layer-btn").find("a#"+btn.id).click()
				}
			}
		}
	});
}
/**
 * 点击layer上的按钮 根据ID
 * @param btnId
 * @returns
 */
function clickLayerBtnById(btnId){
	parent.$(".layui-layer-btn").find("a#"+btnId).click();
}
/**
 * 点击Layer ok按钮
 * @returns
 */
function clickLayerOkBtn(){
	parent.$(".layui-layer-btn .layui-layer-btn0").click();
}
/**
 * 点击Layer close按钮
 * @returns
 */
function clickLayerCloseBtn(){
	parent.$(".layui-layer-btn .layui-layer-btn1").click();
}
/**
 * 修改按钮标题
 * @returns
 */
function changeParentLayerDialogBtnTitle(index,btnTitle){
	parent.$(".layui-layer-btn .layui-layer-btn"+index).text(btnTitle);
}
/**
 * 修改Dialog上OK按钮标题
 * @returns
 */
function changeParentLayerDialogOkBtnTitle(btnTitle){
	changeParentLayerDialogBtnTitle(0,btnTitle);
}
/**
 * 修改Dialog上Cancel按钮标题
 * @returns
 */
function changeParentLayerDialogCancelBtnTitle(btnTitle){
	changeParentLayerDialogBtnTitle(1,btnTitle);
}
/**
 * 得到按钮
 * @param index
 * @returns
 */
function getParentLayerDialogBtn(index){
	return parent.$(".layui-layer-btn .layui-layer-btn"+index);
}
/**
 * 得到所有按钮 
 * @returns
 */
function getParentLayerDialogBtns(){
	  return parent.$(".layui-layer-btn>a");
}
/**
 * 改变dialog 默认ok按钮的样式为正在提交
 * @returns
 */
function changeParentLayerDialogOkBtnStateToSubmiting(){
	if(self!=top){
		var btn = getParentLayerDialogBtn(0);
		changeBtnStateToSubmiting(btn);
	}
}
/**
 * 改变dialog 所有按钮的样式为disabled
 * @returns
 */
function changeParentLayerAllDialogBtnStateToDisabled(){
	if(self!=top){
		var btns = getParentLayerDialogBtns();
		if(isOk(btns)){
			btns.attr("disabled","disabled");
		}
	}
}

function changeParentLayerDialogOkBtnStateToDisabled(){
	if(self!=top){
		var btn = getParentLayerDialogBtn(0);
		if(isOk(btn)){
			btn.attr("disabled","disabled");
		}
	}
}
function cancelParentLayerDialogOkBtnStateToDisabled(){
	if(self!=top){
		var btn = getParentLayerDialogBtn(0);
		if(isOk(btn)){
			btn.removeAttr("disabled");
		}
	}
}
/**
 * 改变dialog 所有按钮的样式去掉disabled
 * @returns
 */
function cancelParentLayerAllDialogBtnStateToDisabled(){
	if(self!=top){
		var btns = getParentLayerDialogBtns();
		if(isOk(btns)){
			btns.removeAttr("disabled");
		}
	}
}
/**
 * 恢复所有按钮样式
 * @returns
 */
function cancelParentLayerDialogAllBtnStateToSubmiting(){
	if(self!=top){
		var btns = getParentLayerDialogBtns();
		if(isOk(btns)){
			btns.each(function(){
				cancelBtnStateToSubmiting($(this));
			});
		}
	}
}

/**
 * 改变dialog按钮的样式为正在提交
 * @returns
 */
function changeLayerDialogBtnStateToSubmiting(btn){
	changeBtnStateToSubmiting(btn);
	changeParentLayerAllDialogBtnStateToDisabled();
}
/**
 * 恢复dialog按钮的样式
 * @returns
 */
function cancelLayerDialogBtnStateToSubmiting(btn){
	cancelBtnStateToSubmiting(btn);
	cancelParentLayerAllDialogBtnStateToDisabled();
}

/**
 * 改变按钮的样式为正在提交
 * @returns
 */
function changeBtnStateToSubmiting(btn){
	if(isOk(btn)){
		var oldText = btn.text();
		if(oldText){
			btn.data("old-text",oldText).attr("data-old-text",oldText);
			btn.attr("disabled","disabled");
			btn.text("处理中...");
		}
	}
}

/**
 * 取消并恢复 dialog的OK按钮
 * @returns
 */
function cancelBtnStateToSubmiting(btn){
	if(isOk(btn)){
		btn.removeAttr("disabled");
		var oldText = btn.data("old-text");
		if(oldText){
			btn.text(oldText);
		}
	}
}
/**
 * 取消并恢复 dialog的OK按钮
 * @returns
 */
function cancelParentLayerDialogOkBtnStateToSubmiting(){
	if(self!=top){
		var btn = getParentLayerDialogBtn(0);
		cancelBtnStateToSubmiting(btn);
	}
}
/**
 * 显示dialog按钮
 * @returns
 */
function showParentLayerDialogBtn(index){
	  if(index){
		  parent.$(".layui-layer-btn .layui-layer-btn"+index).show();
	  }else{
		  parent.$(".layui-layer-btn").show();
	  }
	  
}
/**
 * 检测是否存在相同按钮
 * @param btnId
 * @returns
 */
function checkExistLayerDialogBtnById(btnId){
	return parent.$(".layui-layer .layui-layer-btn").find("a#"+btnId);
}
/**
 * 检测是否存在相同按钮
 * @param btnBox
 * @param title
 * @param cssClass
 * @returns
 */
function checkExistLayerDialogBtn(btnBox,title,cssClass){
	 var sameClassBtns=btnBox.find("a."+cssClass.replace(" ","."));
	 var existBtn=null;
	 if(sameClassBtns&&sameClassBtns.length>0){
		 sameClassBtns.each(function(){
			 var btn=$(this);
			 if($.trim(btn.text())==$.trim($("<span>"+title+"</span>").text())){
				 existBtn=btn;
				 return false;
			 }
		 });
	 }
	 return existBtn;
}
/**
 * 添加按钮
 * @param title
 * @param cssClass
 * @param clickFunc
 * @returns
 */
function addParentLayerDialogBtn(title,cssClass,clickFunc){
	var btnBox = parent.$(".layui-layer:last .layui-layer-btn");
	if(notOk(btnBox)){
		return false;
	}
	var existTitleBtn=checkExistLayerDialogBtn(btnBox,title,cssClass);
	var btnId;
	if(existTitleBtn){
		existTitleBtn.remove();
	}
	btnId="lay_btn_"+randomId();
	var btn="<a tabindex='-1' id='"+btnId+"' class='"+cssClass+"'>"+title+"</a>";
	var closeBtn = btnBox.find("a.layui-layer-btn1");
	if(isOk(closeBtn)){
		closeBtn.before(btn);
	}else{
		btnBox.append(btn);
	}
	var _btn = parent.$("body").find("#"+btnId);
	if(isOk(_btn)){
		_btn.on("click",function(e){
			e.preventDefault();
			e.stopPropagation();
			if(_btn.attr("disabled")){
				return false;
			}
			if(clickFunc){
				clickFunc(_btn,function(){
					cancelLayerDialogFormsSubmiting($("form"));
					cancelLayerDialogBtnStateToSubmiting(_btn);
				});
			}
		});
	}
	return btnId;
}

function changeLayerDialogFormSubmiting(formEle){
	var form=getRealJqueryObject(formEle);
	if(isOk(form)){
		form.data("submiting",true).attr("data-submiting",true);
	}
}
function cancelLayerDialogFormSubmiting(formEle){
	var form=getRealJqueryObject(formEle);
	if(isOk(form)){
		form.data("submiting",false).removeAttr("data-submiting");
	}
}
function cancelLayerDialogFormsSubmiting(forms){
	if(isOk(forms)){
		forms.data("submiting",false).removeAttr("data-submiting");
	}
}
/**
 * 通过ajax提交form
 * @param formEle
 * @param successCallback
 * @param failCallback
 * @returns
 */
function ajaxSubmitForm(formEle,successCallback,failCallback){
	var form=getRealJqueryObject(formEle);
	 if(isOk(form)){
		if(FormChecker.check(form)){
			var url=form.action;
			LayerMsgBox.loading("提交中...",10000);
			form.ajaxSubmit({
				type:"post",
				url:url,
				success:function(ret){
					if(ret.state=="ok"){
						if(successCallback){
							LayerMsgBox.success(ret.msg,500,function(){
								successCallback(ret);
							});
						}else{
							LayerMsgBox.success(ret.msg,500);
						}
					}else{
						LayerMsgBox.closeLoadingNow();
						if(failCallback){
							failCallback(ret);
						}else{
							LayerMsgBox.alert(ret.msg,2);
						}
					}
				}
			});
		}
	}

	return false;

}
/**
 * 得到真实的jquery object
 * @param ele
 * @returns
 */
function getRealJqueryObject(ele){
	if(!ele){
		return null;
	}
	var type=typeof(ele);
	var eleObj=null;
	if(type=="string"){
		var startChar=ele.charAt(0);
		if(startChar=='#'||startChar=='.'){
			eleObj=$(ele);
		}else{
			eleObj=$("#"+ele);
		}
	}else if(type=="object"){
	  if(isDOM(ele)){
		  eleObj=$(ele); 
	  }else{
		  eleObj=ele;
	  }
	}
	return eleObj;
}

/**
 * 获取一个指定ele的父元素jquery对象
 * @param parentEle
 * @returns
 */
function getRealParentJqueryObject(parentEle){
	 var parent=getRealJqueryObject(parentEle);
	return parent?parent:jboltBody;
}
/**
 * 提交表单并跳转回到回到指定页面
 * @param formEle 要提交的表单
 * @param url     成功后跳转的地址
 * @param params  参数 没有就填写null
 * @param tabInnerBack 如果是多选项卡模式的话 是否当前选项卡内部跳转而不是关闭
 * @param newTitleIfInner 如果是多选项卡模式的话 选择内部跳转的话 可以设置新标题
 * @returns
 */
function ajaxSubmitFormAndGoBack(formEle,url,params,tabInnerBack,newTitleIfInner){
	//ajax提交表单
	ajaxSubmitForm(formEle,function(){
//		var withTabMode=isWithtabs();
		//判断选项卡就关闭表单刷新列表tab
		if(jboltWithTabs){
			if(tabInnerBack){
				JBoltTabUtil.currentTabGo(url,params);
				if(newTitleIfInner){
					changeCurrentTabTitle(newTitleIfInner);
				}
			}else{
				closeCurrentAndReloadTiggerTab();
			}
		}else{
			//发送pjax请求 重新进入列表页面
			JBoltPjaxUtil.sendPjax(url,mainPjaxContainerId,params);
		}
	});
}

/**
 * 关闭当前tab
 * @returns
 */
function closeCurrentTab(){
	if(jboltWithTabs){
		JBoltTabUtil.closeCurrentTab();
	}
}


/**
 * 关闭当前tab 并且 刷新指定Tab
 * @param refreshTabKey
 * @param onlyRefreshTable
 * @param tableEle
 * @returns
 */
function closeCurrentAndReloadTiggerTab(refreshTabKey,onlyRefreshTable,tableEle){
	if(jboltWithTabs){
		var currentTab=JBoltTabUtil.getCurrentTab();
		if(isOk(currentTab)){
			if(refreshTabKey){
				JBoltTabUtil.showJboltTab(refreshTabKey,false,function(){
					JBoltTabUtil.close(currentTab);
					if(onlyRefreshTable||tableEle){
						refreshJBoltTable(tableEle);
					}else{
						refreshPjaxContainer();
					}

				});
			}else{
				var triggerTabKey=currentTab.data("trigger-tab-key");
				if(triggerTabKey&&JBoltTabUtil.isJboltTabExist(triggerTabKey)){
					JBoltTabUtil.showJboltTab(triggerTabKey,false,function(){
						JBoltTabUtil.close(currentTab);
						if(onlyRefreshTable||tableEle){
							refreshJBoltTable(tableEle);
						}else{
							refreshPjaxContainer();
						}
					});
				}else{
					JBoltTabUtil.close(currentTab);
					if(onlyRefreshTable||tableEle){
						refreshJBoltTable(tableEle);
					}else{
						refreshPjaxContainer();
					}
				}
				
			}
			
			
		}
	}else{
		if(onlyRefreshTable||tableEle){
			refreshJBoltTable(tableEle);
		}else{
			refreshPjaxContainer();
		}
	}
	
}
/**
 * form直接跳转提交 带CHECKER
 * @param formEle
 * @returns
 */
function pageFormSubmit(formEle){
	var form=getRealJqueryObject(formEle);
	if(isOk(form)&&FormChecker.check(form)){
		form.attr("onsubmit","return true;");
		form.submit();
	}
}
/**
 * form提交
 * @param formEle
 * @returns
 */
function formSubmit(formEle){
	var form=getRealJqueryObject(formEle);
	if(isOk(form)){
		form.submit();
	}
}

/**
 * form提交 带checker
 * @param formEle
 * @returns
 */
function formSubmitWithChecker(formEle){
	var form=getRealJqueryObject(formEle);
	if(isOk(form)&&FormChecker.check(form)){
		form.submit();
	}
}


/**
 * 提交form,切换刷新当前Tab
 * @param formEle
 * @returns
 */
function submitFormInCurrentTab(formEle,successCallback,failCallback){
//			var withTabMode=isWithtabs();
			if(jboltWithTabs){
				var form=getRealJqueryObject(formEle);
				if(isOk(form)){
					if(FormChecker.check(form)){
						var tabContent=JBoltTabUtil.getCurrentTabContent();
						if(tabContent&&tabContent.length==1){
							var url=form.attr("action");
							if(url){
								url=urlWithFormData(url,form);
								LayerMsgBox.load(3);
								tabContent.ajaxPortal(true,url,true,function(portal){
									LayerMsgBox.closeLoadNow();
									if(successCallback){
										successCallback(portal);
									}
								},function(portal){
									if(failCallback){
										failCallback(portal);
									}
								});
							}else{
								LayerMsgBox.alert("Form 未设置action",2);
							}
							
						}
					}
				}
		}else{
			ajaxSubmitForm(form,successCallback,failCallback);
		}
}
/**
 * 初始化admin后台的top部分里 关于style切换的东西
 * @returns
 */
function initJboltAdminTopStyleChange(){
	var jbolt_admin_main_top=$(".jbolt_admin_main_top");
	initToolTip(jbolt_admin_main_top);
	
}
/**
 * 判断是否是tabs多选项卡模式
 * @returns
 */
function isWithtabs(){
	var withTabs=$(".jbolt_admin.withtabs");
	return withTabs&&withTabs.length==1;
}
/**
 * 表单提交按照多选项卡模式下 ajaxPortal模式提交
 * @param form
 * @returns
 */
function tabContentFormSubmitWithAjaxPortal(form){
	var tabContent=JBoltTabUtil.getCurrentTabContent();
	if(isOk(tabContent)){
		formSubmitToAjaxPortal(form,tabContent);
	}
}
/**
 * 表单提交ajaxPortal模式提交
 * @param formEle
 * @param portal 多个可以传portal对象数组 也可以portalid数组或者逗号隔开的一个字符串
 * @returns
 */
function formSubmitToAjaxPortal(formEle,portal){
	var form=getRealJqueryObject(formEle);
	 if(isOk(form) && portal){
		var url=form.attr("action");
		if(url){
			url=urlWithFormData(url,form);
			LayerMsgBox.load(3);
			var p;
			if(isArray(portal)){
					for(var i in portal){
						if(portal.eq){
							p = portal.eq(i);
						}else{
							p = getRealJqueryObject(portal[i]);
						}	
						if(isOk(p)){
							p.ajaxPortal(true,url,true,function(){
								LayerMsgBox.closeLoadNow();
							});
						}
					}
			}else{
				if(typeof(portal)=="string" && portal.indexOf(",")!=-1){
					var portals = portal.split(",");
					for(var i in portals){
						p = getRealJqueryObject(portal[i]);
						if(isOk(p)){
							p.ajaxPortal(true,url,true,function(){
								LayerMsgBox.closeLoadNow();
							});
						}
					}
				}else{
					p = getRealJqueryObject(portal);
					if(isOk(p)){
						p.ajaxPortal(true,url,true,function(){
							LayerMsgBox.closeLoadNow();
						});
					}
				}
			}
			
		}else{
			 LayerMsgBox.alert("Form 未配置action",2);	
		}
	 }else{
		 LayerMsgBox.alert("参数异常",2);
	 }
	return false;
}
/**
 * 触发器 触发当前tab切换URL
 * @param url
 * @returns
 */
function  currentTabContentRedirectWithAjaxPortal(url){
	var currentTab = JBoltTabUtil.getCurrentTab();
	if(isOk(currentTab)){
		var backUrl=currentTab.data("url");
		currentTab.data("backurl",backUrl);
		currentTab.attr("data-backurl",backUrl);
	}

	var tabContent=JBoltTabUtil.getCurrentTabContent();
	if(isOk(tabContent)){
		LayerMsgBox.load(3);
		tabContent.ajaxPortal(true,url,true,function(){
			LayerMsgBox.closeLoadNow();
		});

	}
}
/**
 * 在多选项卡模式下 初始化 tabcontent查询中的主要form提交事件
 * @returns
 */
function initAdminAjaxPortalFormSubmitEvent(){
	   jboltBody.on('submit', '.jbolt_page_title form:not([data-ajaxportal-form]),form[data-submittabportal]:not([data-ajaxportal-form])', function (e) {
		   e.preventDefault();
		   e.stopPropagation();
		   var form=$(this);
		   var needcheck=form.data("needcheck");
		   if(needcheck==undefined||needcheck==null||needcheck=="undefined"||needcheck==""){
			   needcheck=true;
		   }
		   if(needcheck){
			   if(FormChecker.check(this)){
				  tabContentFormSubmitWithAjaxPortal(form);
			   }
		   }else{
			   tabContentFormSubmitWithAjaxPortal(form);
		   }
		   return false;
	    });
}
/**
 * 切换一个隐藏域
 * @param ids
 * @returns
 */
function toggleInputHidden(ids){
	if(ids.indexOf(",")!=-1){
		//多个
		var idsArr=ids.split(",");
		for(var i in idsArr){
			toggleOneInputHidden(idsArr[i]);
		}
	}else{
		toggleOneInputHidden(ids);
	}
}
function toggleOneInputHidden(id){
	var input=g(id);
	if(input.type=="hidden"){
		input.type="text";
	}else{
		input.type="hidden";
	}
}

/**
 * 内置多选项卡View工具类
 */
var JBoltTabViewUtil={
		initUI:function(parentEle){
			var parent=getRealParentJqueryObject(parentEle);
			if(!isOk(parent)){return false;}
			var that=this;
			var tabViews = parent.find(".jbolt_tab_view");
			if(isOk(tabViews)){
				var tv,tva,activeIndex=0;
				tabViews.each(function(){
					activeIndex=0;
					tv=$(this);
					tva = tv.find("jbolt_tab_links>.jbolt_tab_link.active");
					if(!isOk(tva)){
						//没有active的就得判断data-active-index=""
						activeIndex=tv.data("active-index");
						if(typeof(activeIndex)=="undefined" || activeIndex<=0){
							activeIndex=0;
						}
						that.active(tv,activeIndex);
					}
				});
			}
			
		},initTabLinkEvent:function(){
			jboltBody.on("click",".jbolt_tab_view .jbolt_tab_link",function(e){
				e.preventDefault();
				var link=$(this);
				var tabContentId=link.attr("href");
				if(!tabContentId){
					LayerMsgBox.alert('请设置选项卡href绑定内容区域ID',2);
					return false;
				}
				var tabView=link.closest(".jbolt_tab_view");
				var activeLink=tabView.find(".jbolt_tab_link.active");
				var handler=tabView.data("handler");
				var exe_handler=handler?(eval(handler)):null;
				if(isOk(activeLink)){
					//如果是同一个 就不操作任何事情
					var activeTabContentId=activeLink.attr("href");
					if(activeTabContentId==tabContentId){
						if(exe_handler && typeof(exe_handler)=="function"){
							exe_handler(tabContentId,link.index());
						}
						return false;
					}
					//如果不是 就把active的关掉
					activeLink.removeClass("active");
					tabView.find(activeTabContentId).removeClass("active");
				}
				link.addClass("active");
				tabView.find(tabContentId).addClass("active");
				if(exe_handler && typeof(exe_handler)=="function"){
					exe_handler(tabContentId,link.index());
				}
			});
		},active:function(tabView,tabIndex){
			var tabLink=tabView.find('.jbolt_tab_links>.jbolt_tab_link:eq('+tabIndex+')');
			if(isOk(tabLink)){
				tabLink.trigger("click");
			}
		}
}
 
/**
 * 主从表模块
 */
var MasterSlaveUtil={
		initJBoltTable:function(parentEle){
			   var parentE=getRealParentJqueryObject(parentEle);
			   if(!isOk(parentE)){return false;}
			   if(parentE.data("init-ms")){return false;}
			   parentE.data("init-ms",true);
				var pageContent=parentE.find(".jbolt_page_content");
				if(isOk(pageContent)){
					var jboltPage=pageContent.closest(".jbolt_page");
					var ph=jboltPage.height()-2;
					var title=jboltPage.find(".jbolt_page_title");
					if(isOk(title)){
						ph=ph-title.height()-parseInt(title.css("margin-bottom"));
					}
					pageContent.css({
						"height":ph+"px"
					});
				}
			   
			   var that=this;
			   var boxs=pageContent.find(".jbolttable_master_slave_box");
			   if(!isOk(boxs)){return false;}
			   var len=boxs.length;
			   for(var i=0;i<len;i++){
				   that.initJBoltTableMasterSlave(boxs.eq(i));
			   }
			
	},
	initJBoltTableMasterSlave:function(box){
			var jboltTables,tempTable,sizes=box.data("sizes");
			if(sizes){
				var ssa=sizes.split(":");
				var m=Number(ssa[0])*10;
				var s=Number(ssa[1])*10;
				sizes=[m,s];
			}else{
				sizes=[60,40];
			}
			var jboltTables,tempTable;
			var rid=randomId();
			var masterId="master_"+rid;
			var slaveId="slave_"+rid;
			box.find(".split.master").attr("id",masterId);
			box.find(".split.slave").attr("id",slaveId);
			loadJBoltPlugin(['splitjs','jbolttable'], function(){
				Split(["#"+masterId, "#"+slaveId],{
					direction:'vertical',
					sizes:sizes,
					gutterSize:6,
					onDragEnd:function(){
						jboltTables.each(function(){
							tempTable=$(this).jboltTable("inst");
							if(tempTable){
									tempTable.me.processTableFillBox(tempTable);
								}
							});
						}
				});
				
				jboltTables=box.find(".jbolt_table:not([data-jbolttable])").attr("data-jbolttable",true).jboltTable();
			});
	
			
	},
	   init:function(parentEle){
		   var parent=getRealParentJqueryObject(parentEle);
		   if(!isOk(parent)){return false;}
		   var that=this;
		   var boxs=parent.find(".jbolt_master_slave_box");
		   if(!isOk(boxs)){return false;}
		   var len=boxs.length;
		   for(var i=0;i<len;i++){
			   that.initHeight(boxs.eq(i));
		   }
		},
		initMasterTableEvent:function(){
			var that=this;
			jboltBody.on("click",".jbolt_page .jbolt_master_slave_box .jbolt_master_container table.table>tbody>tr",function(){
				var tr=$(this);
				tr.parent().find("tr.active").removeClass("active");
				tr.addClass("active");
				var id=tr.data("id");
				if(!id){
					LayerMsgBox.alert("数据TR绑定data-id为空",2);
				}else{
					LayerMsgBox.load(3);
					var portals=tr.closest(".jbolt_master_slave_box").find(".jbolt_slave_container [data-ajaxportal]");
					var size=portals.length;
					if(portals&&size){
						portals.each(function(i){
							var portal=$(this);
							var orign_url=portal.data("origin-url")||portal.data("orign-url")||portal.data("orignurl")||portal.data("srcurl")||portal.data("src-url");
							if(!orign_url){
								LayerMsgBox.alert("AjaxPortal组件需要设置data-origin-url",2);
								LayerMsgBox.closeLoadNow();
								return false;
							}else{
								var tempEndStr=orign_url[orign_url.length-1];
								var url=orign_url;
		      					if(tempEndStr=="="||tempEndStr=="-"||tempEndStr=="/"){
		      						url=url+id;
		      					}else{
		      						url=url+"/"+id;
		      					}
								portal.ajaxPortal(true,url,true,function(){
									if(i==size-1){
										LayerMsgBox.closeLoadNow();
									}
									that.initPortalHeight(portal);
								});
							}
						});
					}
					
				}
			});
		},initPortalHeight:function(portal){
			var jbolt_slave_container=portal.closest(".jbolt_slave_container");
			if(jbolt_slave_container&&jbolt_slave_container.length>0){
				var sheight=jbolt_slave_container.outerHeight()-45;
				jbolt_slave_container.find(".jbolt_slave_body").each(function(){
					var bo=$(this);
					var title=bo.parent().find(".jbolt_slave_top");
					if(title&&title.length>0){
						var titleHeight=title.outerHeight();
						bo.height(sheight-titleHeight);
					}else{
						bo.height(sheight);
					}
				});
				
				
			}
		},
		initHeight:function(box){
			var jboltPage=box.closest(".jbolt_page");
			var jbolt_page_content=jboltPage.find(".jbolt_page_content");
			if(isOk(jboltPage)&&isOk(jbolt_page_content)){
				var height=jboltPage.outerHeight()-2-parseInt(jboltPage.css("padding-bottom"))*2;
				var pageTitle=jboltPage.find(".jbolt_page_title");
				if(isOk(pageTitle)){
					height=height-parseInt(pageTitle.outerHeight())-parseInt(pageTitle.css("margin-bottom"));//10是margin-bottom
				}
				box.height(height);
			}
		}
	}
/**
 * 处理窗口resize时候jbolt_layer调整
 * @returns
 */
function resizeJBoltLayer(){
	JBoltLayerUtil.processLayerByWindowResize();
	/*var layerPortal=jboltBody.find(".jbolt_layer_portal");
	if(layerPortal&&layerPortal.length==1){
		var jblayer=layerPortal.closest("#jbolt_layer");
		if(jblayer&&jblayer.length==1){
			var triggerid=jblayer.data("triggerid");
			if(triggerid){
				var triggerEle=$("#"+triggerid);
				if(triggerEle&&triggerEle.length==1){
					var height=triggerEle.data("height");
					if(height){
						var newTop=jboltWindowHeight-height;
						if(newTop<0){
							newTop=0;
						}
						var top=newTop+"px";
						layerPortal.css({"top":top});
						var closeBtn=jblayer.find(".jbolt_layer_close");
						if(closeBtn&&closeBtn.length==1){
							closeBtn.css({"top":top});
						}
					}
				}
			}
		}
	
	}*/
}
var resizingJboltTableWidth=false;
function resizeJboltTableWidth(){
/*	if(resizingJboltTableWidth){return false;}
	resizingJboltTableWidth=true;
	var fillWidthTables=jboltBody.find(".jbolt_table_view table.jbolt_table.jbolt_main_table");
	if(isOk(fillWidthTables)){
		var len=fillWidthTables.length;
		fillWidthTables.each(function(i){
			var table=$(this);
			var jboltTable=table.jboltTable("inst");
			jboltTable.me.processTableColWidthAfterResize(jboltTable);
			if(i==len-1){
				resizingJboltTableWidth=false;
			}
		});
	}else{
		resizingJboltTableWidth=false;
	}*/
}
//窗口resize的时候 处理jboltTable 当height设置为fill的时候 调用resize
function resizeFillHeightJboltTable(){
	var jboltTables=jboltBody.find(".jbolt_table_view table.jbolt_table.jbolt_main_table[data-height='fill']");
	if(isOk(jboltTables)){
		jboltTables.each(function(){
			var table=$(this);
			var jboltTable=table.jboltTable("inst");
			if(jboltTable){
				jboltTable.me.resize(jboltTable);
			}
		});
	}
}
function resizeFillBoxHeightJboltTable(){
	var jboltTables=jboltBody.find(".jbolt_table_view table.jbolt_table.jbolt_main_table[data-height='fill_box']");
	if(isOk(jboltTables)){
		jboltTables.each(function(){
			var table=$(this);
			var jboltTable=table.jboltTable("inst");
			if(jboltTable){
				jboltTable.me.processTableFillBox(jboltTable);
			}
		});
	}
}
//var resizeTimer = null;
//窗口resize
function onwindowReisze(){
	jboltWindow.bind("resize",function(){
		jboltWindowWidth=jboltWindow.width();
		jboltWindowHeight=jboltWindow.height();
		JBoltLayerUtil.processLayerByWindowResize();
		JBoltInputUtil.hideJBoltInputLayer();
		resizeJboltTableWidth();
		resizeFillHeightJboltTable();
		resizeFillBoxHeightJboltTable();
		MasterSlaveUtil.init(mainPjaxContainer);
//		AutocompleteUtil.hideResult();
		/* if (resizeTimer){clearTimeout(resizeTimer);}
	        resizeTimer = setTimeout(function(){
	        	
	        } , 500);*/
	});
}


/**
 * 切换元素的可见属性
 * @param cssSelector
 * @param full
 * @returns
 */
function toggleVisiable(cssSelector,ele){
	var target,box;
	if(ele){
		var mele=getRealJqueryObject(ele);
		if(isOk(mele)){
			var box=mele.closest("[data-ajaxportal]");
			if(!isOk(box)){
				box=mele.closest(".jbolt_page");
				if(!isOk(box)){
					box=jboltBody;
				}
			}
		}
		if(isOk(box)){
			target=box.find(cssSelector);
		}else{
			target=$(cssSelector);
		}
	}else{
//		var withTabs=isWithtabs();
		if(jboltWithTabs){
			var currentTabContent=JBoltTabUtil.getCurrentTabContent();
			if(isOk(currentTabContent)){
				target=currentTabContent.find(cssSelector);
			}
		}else{
			if(isOk(mainPjaxContainer)){
				target=mainPjaxContainer.find(cssSelector);
			}else{
				target=jboltBody.find(cssSelector);
			}
		}
	}
	
	if(isOk(target)){
		if(target.is(":hidden")){
			target.show();
		}else{
			target.hide();
		}
	}
	
}

function processUnLockAndAfterLogin(){
	self.location.href=JBoltBaseTagHref;
}
var lockHtmlTpl='<div oncontextmenu="doNothing()" class="j_locksystem noselect" id="j_locksystem">'+
'<div class="j_lockmain"><h1>屏幕已锁</h1><div class="j_lockpassword">'+
'<input onkeyup="unlockSystem(event)" maxlength="40" type="password" id="unlockpwd" placeholder="输入密码 回车解锁" name="password"  autocomplete="off" /><div class="j_reloginbtn"><a onclick="showReloginDialog(false,processUnLockAndAfterLogin)" href="javascrip:void(0)">切换用户</a></div></div></div></div>';
var lockSystemTimer=null;
function checkLockSystem(){
	console.log("checkLockSystem")
	if(!lockSystemTimer){console.log("checkLockSystem lockSystemTimer");return false;}
	var j_locksystem=$("#j_locksystem");
	if(isOk(j_locksystem)){
		//如果已经有了 判断hidden
		j_locksystem.removeClass("__web-inspector-hide-shortcut__");
		j_locksystem.show();
		if(j_locksystem[0].style.visibility!="visible"){
			j_locksystem[0].style.visibility="visible";
		}
	}else{
		showJBoltLockSystemEle();
	}
	
}
/**
 * 显示锁屏界面
 * @returns
 */
function showJboltLockSystem(){
	if(window.self!=window.top){
		parent.showJboltLockSystem();
	}else{
		showJBoltLockSystemEle();	
		if(!lockSystemTimer){
			lockSystemTimer=setInterval(checkLockSystem, 1000);
		}
	}

}
/**
 * 显示锁屏组件
 * @returns
 */
function showJBoltLockSystemEle(){
	var j_locksystem=$("#j_locksystem");
	if(!(j_locksystem&&j_locksystem.length==1)){
		jboltBody.append(lockHtmlTpl);
		j_locksystem=$("#j_locksystem");
	}
	$("#unlockpwd").val("");
	j_locksystem.removeClass("__web-inspector-hide-shortcut__");
	FormDate.hide();
	j_locksystem.show();
}
/**
 * 解锁
 * @returns
 */
function unlockSystem(event){
	var keyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
	if(keyCode==13){
		var pwd=$("#unlockpwd").val();
		if(!($.trim(pwd))){
			alert('请输入登录密码');
		}else{
			// LayerMsgBox.loading("解锁中...",10000);
			$(".j_lockmain>h1").text("解锁中...");
			Ajax.post("/admin/unLockSystem",{"password":pwd},function(res){
				$(".j_lockmain>h1").text("解锁成功");
				setTimeout(function(){
					closeJboltLockSystem();
					LayerMsgBox.success("解锁成功",500,function(){
						$(".j_lockmain>h1").text("屏幕已锁");
					});
				},300);
			},function(res){
				$(".j_lockmain>h1").text("屏幕已锁");
				alert(res.msg);
			});
		}
	}
}
/**
 * 关闭锁屏界面
 * @returns
 */
function closeJboltLockSystem(){
	if(window.self!=window.top){
		parent.closeJboltLockSystem();
	}else{
		clearInterval(lockSystemTimer);
		lockSystemTimer=null;
		if(isOk(jboltAdmin)){
			//说明是在系统内部
			$("#unlockpwd").val("");
			$("#j_locksystem").hide();
		}else{
			reloadCurrentPage();
		}
		
	}
}
/**
 * 屏蔽右键
 * @returns
 */
function doNothing(){
	window.event.returnValue=false;
	return false;
}
/**
 * 处理内部组件初始化
 * @param parentEle
 * @returns
 */
function processInnerElesInit(parentEle){
	var parent=getRealParentJqueryObject(parentEle);
	$('.tooltip.show').remove();
	SwitchBtnUtil.init(parent);
	SelectUtil.initAutoSetValue(parent);
	FormDate.init(parent);
	LayerTipsUtil.init(parent);
	ImageViewerUtil.init(parent);
	RadioUtil.init(parent);
	CheckboxUtil.init(parent);
	AutocompleteUtil.init(parent);
	Select2Util.init(parent);
	SelectUtil.init({parent:parent});
	initToolTip(parent);
	JBoltInputUtil.init(parent);
	RangeSliderUtil.init(parent);
	JBoltInputWithClearBtnUtil.init(parent);
	JBoltInputWithCalculatorUtil.init(parent);
	FileUploadUtil.init(parent);
	ImgUploadUtil.init(parent);
}
/**
 * 初始化tooltip
 * @param parentEle
 * @returns
 */
function initToolTip(parentEle){
	var tips;
	if(parentEle){
		var parent=getRealParentJqueryObject(parentEle);
		if(isOk(parent)){
			tips=parent.find("[tooltip],[data-toggle='tooltip']");
		}
	}else{
		tips=jboltBody.find("[tooltip],[data-toggle='tooltip']");
	}
	if(isOk(tips)){
		tips.tooltip({ boundary: 'window',container:"body"});
	}
}
/**
 * 初始化Popover
 * @param parentEle
 * @returns
 */
function initPopover(parentEle){
	var pops;
	if(parentEle){
		var parent=getRealParentJqueryObject(parentEle);
		if(isOk(parent)){
			pops=parent.find("[popover],[data-toggle='popover']");
		}
	}else{
		pops=jboltBody.find("[popover],[data-toggle='popover']");
	}
	if(isOk(pops)){
		pops.popover({ boundary: 'window',container:"body"});
	}
}
/**
 * 销毁tooltip
 * @param parentEle
 * @returns
 */
function destroyToolTip(parentEle){
	if(parentEle){
		var parent=getRealParentJqueryObject(parentEle);
		if(isOk(parent)){
			parent.find("[tooltip],[data-toggle='tooltip']").tooltip("destroy");
		}
	}else{
		jboltBody.find("[tooltip],[data-toggle='tooltip']").tooltip("destroy");
	}
	 
}
/**
 * 重置tooltip 先销毁 再初始化
 * @param parentEle
 * @returns
 */
function resetToolTip(parentEle){
	destroyToolTip(parentEle);
	initToolTip(parentEle);
}
/**
 * 动态设置全局后台UI样式
 * @param styleName
 * @returns
 */
function changeUserJboltStyle(styleName){
	var jbolt_user_config_style_box=$("#jbolt_user_config_style_box");
	jbolt_user_config_style_box.find(".jbolt_config_style.active").removeClass("active");
	jbolt_user_config_style_box.find("#jbs_"+styleName).addClass("active");
	var className=jboltAdmin[0].className;
	var classArr=className.split(" ");
	var newClassName="";
	var oneClass="";
	for(var i in classArr){
		oneClass=classArr[i]; 
		if(oneClass!="default" && oneClass.indexOf("jbolt_style_")==-1){
			newClassName=newClassName+oneClass+" ";
		}
	}
	jboltAdmin[0].className=newClassName+styleName;
	//改变scroll
	changeLeftNavScroll();
}

/**
 * Select 2 组件
 */
var Select2Util={
		init:function(parentEle){
			this.initNotAutoLoadSelect(parentEle);
		},initNotAutoLoadSelect:function(parentEle){
			if(hasImportSelect2){
				 var parent=getRealParentJqueryObject(parentEle);
				 if(!isOk(parent)){return false;}
				 var selects=parent.find("select[data-select2]:not([data-autoload]):not([data-linkage])");
				 if(!isOk(selects)){return false;}
				 var that=this;
				 var select;
				 selects.each(function(){
					 select=$(this);
					 that.initAutoLoadSelect(select);
				 });
			}
		},
		initAutoLoadSelectById:function(selectId){
			var select=$("#"+selectId);
			if(isOk(select)){
				this.initAutoLoadSelect(select);
			}
		},
		initAutoLoadSelect:function(select){
			if(hasImportSelect2){
				var type=select.data("select-type");
				if(type&&type=="select2"){
					var placeholder=select.data("placeholder");
					if(!placeholder){
						placeholder=select.attr("placeholder");
						if(!placeholder){
							placeholder=select.data("text");
							if(!placeholder){
								placeholder="请选择";
							}
						}
					}
					var multiple=select[0].hasAttribute("multiple");
					select.select2({
						theme:"bootstrap",
						allowClear:true,
						closeOnSelect:!multiple,
						placeholder:placeholder,
						width:"auto"
					});
					processNoMasterTooltip();
			}
			}
		}
}

/**
 * 处理无用的tooltip
 * @returns
 */
function processNoMasterTooltip(){
	jboltBody.find(".tooltip.show").each(function(){
		var tooltip = $(this);
		var tooltipId=tooltip.attr("id");
		if(tooltipId){
			var master=jboltBody.find("[aria-describedby='"+tooltipId+"']")
			if(!isOk(master)){
				tooltip.remove();
			}
		}
	});
}
/**
 * 检测引入了select2的js
 * @returns
 */
function checkImportSelect2(){
	var hasjs=$("script[id='select2_js']");
	return (hasjs&&hasjs.length==1);
}
/**
 * 检测引入了bootstrap-select的js
 * @returns
 */
function checkImportBootstrapSelect(){
	var hasjs=$("script[id='bootstrap_select_js']");
	return (hasjs&&hasjs.length==1);
}
/**
 * 全局处理select2 宽度等问题
 * @returns
 */
function processGlobalSelect2(){
	hasImportSelect2=checkImportSelect2();
	if(hasImportSelect2){
		$.fn.modal.Constructor.prototype.enforceFocus = function () {};
//		$.fn.select2.defaults.set('width', '100%');
		$.fn.select2.defaults.set("theme", "bootstrap");
	}
}
/**
 * 全局处理bootstrap-select
 * @returns
 */
function processGlobalBootstrapSelect(){
	hasImportBootstrapSelect=checkImportBootstrapSelect();
	if(hasImportBootstrapSelect){
		$.fn.selectpicker.Constructor.BootstrapVersion = '4';
		$.fn.selectpicker.Constructor.DEFAULTS.style="btn-outline-secondary";
	}
}
/**
 * form提交让ajaxPortal提交
 * @param formEle
 * @returns
 */
function ajaxPortalSubmitWithForm(formEle){
	var form=getRealJqueryObject(formEle);
	if(!isOk(form)){
		LayerMsgBox.alert("指定Form不存在");
		return false;
	}
	var portalId = form.data("portal")||form.data("portal-id")||form.data("portalid");
	if(!portalId){
		LayerMsgBox.alert("指定Form上没有设置data-portal 指定提交哪个ajaxPortal不存在");
		return false;
	}
	var portal = getRealJqueryObject(portalId);
	if(!isOk(portal)){
		LayerMsgBox.alert("表单上指定的 data-portal 不存在");
		return false;
	}
	LayerMsgBox.loading("加载中...",10000);
	portal.ajaxPortal(true);
	return false;
}

var jboltPortalPageTpl='<div class="jbolt_portal_pages noselect">'+
'<div class="pages">'+
'<div class="mainPagination mb-1 mb-sm-0  d-block d-sm-inline-block text-center" id="${pageId}"></div>'+
'<div class="searchPage d-none d-sm-inline-block">'+
'<span class="page-go pl-3">到<input id="gonu" type="number" onblur="if(this.value&&this.value>=1){}else{this.value=1;}" min="1" max="1"  pattern="[0-9]*" class="current_page" value="1">页</span>'+
'<a tabindex="-1" href="javascript:;" class="page-btn">GO</a>'+
'<span class="page-sum">共&nbsp;<strong id="totalRow" class="allPage">1</strong>&nbsp;条&nbsp;<strong id="totalPage" class="allPage">1</strong>&nbsp;页</span>'+
'<select id="pageSize" class="mx-2" style="width:80px;height: 32px;margin-top:-1px;border-color:#e6e6e6;">'+
'{@each options as option}{@if option}<option {@if option==pageSize} selected="selected" {@/if} value="${option}">${option}条/页</option>{@/if}{@/each}'+
'</select>'+
'</div>'+
'</div>'+
'<div class="clearfix"></div>'+
'</div>';
var jboltPortalPageTpl_mini='<div class="jbolt_portal_pages noselect">'+
'<div class="pages">'+
'<div class="mainPagination mb-1 mb-sm-0 d-block text-center" id="${pageId}"></div>'+
'<div class="searchPage d-none">'+
'<input type="hidden" id="pageSize" value="${pageSize?pageSize:10}" />'+
'</div>'+
'</div>'+
'<div class="clearfix"></div>'+
'</div>';
/**
 * ajaxPortal
 */
var AjaxPortalUtil={
		load:function(portalEle){
			var portal=getRealJqueryObject(portalEle);
			if(isOk(portal)){
				portal.data("triggerload",true);
				portal.ajaxPortal(true);
			}
		},
		initPage:function(portal,pageInfo){
			var pageBoxId=portal.data("page-box");
			if(!pageBoxId){return false;}
			var jboltPage = portal.closest("jbolt_page");
			if(notOk(jboltPage)){
				jboltPage = jboltBody;
			}
			var pageBox = jboltPage.find("#"+pageBoxId);
			if(notOk(pageBox)){
				alert("ajaxPortal指定的data-page-box未找到");
				return false;
			}
			var that = this;
			var isMini=portal.data("page-mini");
			portal.data("totalpage",pageInfo.totalPage);
			var pageId=portal.data("page");
			if(!pageId){
				pageId = pageBox.attr("id")+"_pages";
				portal.data("page",pageId).attr("data-page",pageId);
			}
			var jbolt_portal_pages = pageBox.find(".jbolt_portal_pages");
			if(notOk(jbolt_portal_pages)){
				jbolt_portal_pages=that.initPageBoxContent(portal,pageBox,pageId,isMini);
			}
			var pager = jbolt_portal_pages.find("#"+pageId);
			jbolt_portal_pages.find("#gonu").val(pageInfo.pageNumber).attr("max",pageInfo.totalPage);
			jbolt_portal_pages.find("#totalPage").text(pageInfo.totalPage);
			jbolt_portal_pages.find("#totalRow").text(pageInfo.totalRow);
			jbolt_portal_pages.find("#pageSize").val(pageInfo.pageSize);
			if(isMini){
				jbolt_portal_pages.find("#gonu").hide();
				jbolt_portal_pages.find("#totalPage").hide();
				jbolt_portal_pages.find("#totalRow").hide();
				jbolt_portal_pages.find("#pageSize").hide();
			}
			
			loadJBoltPlugin(['pagination'], function(){
				pager.pagination(pageInfo.totalPage,{
					   num_edge_entries:1,
					   current_page:(pageInfo.pageNumber-1),
						callback:function(index,ct){
							if(isNaN(index)==false){
								var page=index+1;
								that.readByPage(portal,page);
								return false;
							}
						}
					});
			  });
			
			
			if(!portal.data("page-ok")){
				jbolt_portal_pages.find("#gonu").on("keydown",function(e){
					   if(e.keyCode==109||e.keyCode==189){
						   return false;
					   }
				   });
				jbolt_portal_pages.find("#pageSize").on("change",function(){
					that.readByPage(portal,1);
				   });
				
				jbolt_portal_pages.find(".page-btn").on("click",function(){
					that.readByPage(portal);
				});
			}
			//设置page初始化成功标识
			portal.data("page-ok",true);
		},
		readByPage:function(portalEle,page){
			var portal= getRealJqueryObject(portalEle);
			var pageBoxId=portal.data("page-box");
			if(!pageBoxId){return false;}
			var jboltPage = portal.closest("jbolt_page");
			if(notOk(jboltPage)){
				jboltPage = jboltBody;
			}
			var pageBox = jboltPage.find("#"+pageBoxId);
			if(notOk(pageBox)){
				LayerMsgBox.alert("ajaxPortal指定的data-page-box未找到",2);
				return false;
			}
			var formId = portal.data("conditions-form");
			var form=null;
			if(formId){
				form=$("#"+formId);
				if(notOk(form)){
					LayerMsgBox.alert("ajaxPortal绑定data-conditions-form未找到",2);
					return false;
				}
			}
			
			var jbolt_portal_pages = pageBox.find(".jbolt_portal_pages");
			if(notOk(jbolt_portal_pages)){
				LayerMsgBox.alert("ajaxPortal初始化分页组件异常",2);
				return false;
			}
			
			 if(!page){
				  var input=jbolt_portal_pages.find("#gonu");
				  if(input&&input.length>0){
					  page=input.val();
				  }else{
					  page=1;
				  }
			  }
			  var pageSize=jbolt_portal_pages.find("#pageSize").val();
			  var pageinput = form.find("input[name='page']");
			  if(notOk(pageinput)){
				  form.append('<input type="hidden" name="page" value="'+page+'"/>')
			  }else{
				  pageinput.val(page);
			  }
			  var pagesizeinput = form.find("input[name='pageSize']");
			  if(notOk(pagesizeinput)){
				  form.append('<input type="hidden" name="pageSize" value="'+pageSize+'"/>')
			  }else{
				  pagesizeinput.val(pageSize);
			  }
			  form.submit();
		},
		initPageBoxContent:function(portal,pageBox,pageId,isMini){
			
			portal.data("page",pageId).attr("data-page",pageId);
			var options=[];
			var pageSizeOptions=portal.data("pagesize-options");
			if(pageSizeOptions){
				if(typeof pageSizeOptions == "number"){
					options=[pageSizeOptions];
				}else{
					if(pageSizeOptions.indexOf(",")!=-1){
						options=pageSizeOptions.split(",");
					}else{
						options=[5,10,15,20,25,30,35,40,45,50];
					}
				}
				
			}else{
				options=[5,10,15,20,25,30,35,40,45,50];
			}
			var tempPageSize = portal.data("pagesize");
			var pageSize=(tempPageSize?tempPageSize:1)||10;
			var isMini=portal.data("page-mini");
			var pageHtml=juicer(isMini?jboltPortalPageTpl_mini:jboltPortalPageTpl,{pageId:pageId,pageSize:pageSize,options:options});
			var jbolt_portal_pages=$(pageHtml);
			pageBox.append(jbolt_portal_pages);
			return jbolt_portal_pages;
		},
		triggerPortalJBoltPageCloseHandler:function(portal,jboltPage){
			var jboltPage =  jboltPage?jboltPage:(portal.find(".jbolt_page[data-close-handler]"));
			var closeHandler = jboltPage.data("close-handler");
			if(closeHandler){
				if(closeHandler == "removeCommand" && JBoltWS){
					var commands = jboltPage.data("remove-command");
					JBoltWS.removeCommand(commands);
				}else{
					var exe_handler=eval(closeHandler);
					if(exe_handler&&typeof(exe_handler)=="function"){
						exe_handler(jboltPage);
					}
				}
				
			}
		},
		processError:function(portal,insertType,failcallback){
			switch (insertType) {
			case "prepend":
				portal.find(".ajaxPortalLoading").remove();
				LayerMsgBox.alert("请求资源不存在",2);
				break;
			case "append":
				portal.find(".ajaxPortalLoading").remove();
				LayerMsgBox.alert("请求资源不存在",2);
				break;
			case "replace":
				portal.empty().html('<div class="jbolt_page"><div class="jbolt_page_content"><div style="margin: 20px auto;max-width: 500px"><div class="alert alert-danger">404,您访问的资源不存在!</div></div></div></div>');
				break;
			default:
				portal.empty().html('<div class="jbolt_page"><div class="jbolt_page_content"><div style="margin: 20px auto;max-width: 500px"><div class="alert alert-danger">404,您访问的资源不存在!</div></div></div></div>');
				break;
			}
			
			if(failcallback){
				failcallback(portal);
			}
		},
		processContent:function(portal,html,json,url,replaceOldUrl,insertType,callback){
			  var htmlObj=$(html);
			  if(replaceOldUrl&&url){
					portal.data("url",url);
				}
				var portalId=portal.attr("id");
				if(!portalId){
					portalId=randomId();
					portal.attr("id",portalId);
				}
				LayerMsgBox.closeLoadingNow();
			  switch (insertType) {
				case "prepend":
					portal.find(".ajaxPortalLoading").remove();
					portal.prepend(htmlObj);
					afterAjaxPortal(htmlObj);
					break;
				case "append":
					portal.find(".ajaxPortalLoading").remove();
					portal.append(htmlObj);
					afterAjaxPortal(htmlObj);
					break;
				case "replace":
					portal.empty().append(htmlObj);
					afterAjaxPortal(portal);
					break;
				default:
					portal.empty().append(htmlObj);
					afterAjaxPortal(portal);
					break;
				}
				if(callback){
					callback(portal,htmlObj,json);
				}
				var handler=portal.data("handler");
				var exe_handler=eval(handler);
				  if(exe_handler&&typeof(exe_handler)=="function"){
					  exe_handler(portal,htmlObj,json);
				}
		},
		openBy:function(action){
			disposeTooltip(action);
			  var portalId=action.data("portalid")||action.data("portal");
			  var portal;
			  if(portalId=="self"){
				  portal = action.closest("[data-ajaxportal]");
			  }else{
				  portal = $("#"+portalId);
			  }
			  if(!isOk(portal)){
				  LayerMsgBox.alert("未找到与设置data-portalid匹配的portal",2);
				  return;
			  }
			  
			  var url=action.attr("href");
			  if(!url){
				  url=action.data("url");
				  if(!url){
					  url=portal.data("url");
				  }
			  }
			  if(!url){
				  LayerMsgBox.alert("请设置URL地址",2);
				  return;
			  }
			  
			  LayerMsgBox.load(3);
			  portal.ajaxPortal(true,url,true,function(){
				  LayerMsgBox.closeLoadNow();
			  });
		},
		initBy:function(cssSelector,parentEle){
			var parent=getRealParentJqueryObject(parentEle);
			if(!isOk(parent)){return false;}
			cssSelector=cssSelector||"[data-portalbtn]";
			var that=this;
			parent.on("click",cssSelector,function(e){
				  e.preventDefault();
				  e.stopPropagation();
				  that.openBy($(this));
			  });
		},
		init:function(parentEle){
			var portals;
			var parent=getRealParentJqueryObject(parentEle);
			if(isOk(parent)){
				portals=parent.find("[data-ajaxportal]");
			}
			if(isOk(portals)){
				portals.ajaxPortal(true);
			}
		},refresh:function(portalEle){
			var portal=getRealJqueryObject(portalEle);
			if(isOk(portal)){
				portal.ajaxPortal(true);
			}
		}
}

/**
 * 初始化imageviewer组件的事件绑定 点击关闭
 * @returns
 */
function processImgviewerEvent(){
	jboltBody.on("click",'.viewer-canvas',function(e){
		if($(e.target).hasClass("viewer-canvas")){
			$(".viewer-close").trigger("click");
		}
	});
}

//定义
$.getMultiScripts = function(arr, path) {
    var _arr = $.map(arr, function(scr) {
        return $.ajax({async:false,dataType:"script",url:((path||"") + scr )});
    });

    _arr.push($.Deferred(function( deferred ){
        $( deferred.resolve );
    }));

    return $.when.apply($, _arr);
}
/**
 * 初始化首次进入JBolt-Admin后台 全局类的初始化
 * @returns
 */
function initJboltAdmin(){
	//初始化左侧导航菜单
	initAdminLeftNav();
	//全局处理select2组件
	processGlobalSelect2();
	//全局处理bootstrap-select组件
	processGlobalBootstrapSelect();
	//TableUtil init 初始化table列表里的edit del按钮
	TableUtil.init();
	PageOptUtil.init();
	//切换左侧menu显示和隐藏
	toggleMenuEvent();
	//切换左侧topnav关联的menus
	toggleLeftMenuEventByTopnav();
	//JBoltLayer组件
	JBoltLayerUtil.init();
	
	//初始化主从表结构事件
	MasterSlaveUtil.initMasterTableEvent();
	//处理imgviewer组件事件
	processImgviewerEvent();
	//后台顶部的样式
	initJboltAdminTopStyleChange();
	//看图器
	LayerPhotoUtil.init();
	//初始化bootstrap默认的tab
	initBootstrapTab();
	//初始化juicer模板引擎
	initJuicer();
	//处理监听全局img onerror
	initImgError();
	//body点击事件处理一些额外
	initJboltBodyClick();
	//初始化为ajaxPortal添加右键菜单
	initAjaxPortalContextMenu();
	//初始化formChecker 可能需要加载用户的二开扩展ruleMap
	FormChecker.init();
	//异步下载按钮封装
	DownloadUtil.init();
	//全局jbolttablink处理event
	JBoltTabViewUtil.initTabLinkEvent();
	OpenPageBtnUtil.init();
	//全屏切换按钮初始化
	FullScreenBtnUtil.init();
	//初始化switchBtn的组件全局事件监听
	SwitchBtnUtil.initEvent();
	//textarea处理
	TextareaUtil.initEvent();
	//初始化tabs 从本地缓存
	initJBoltTabsFromSessionStorage();
//	if(jboltWithTabs){
//		window.onpopstate = function(e) {
//			useHashToActiveTab();
//		}
//	}
}

function useHashToActiveTab(){
	if(window.location.hash){
		var activeTabKey = window.location.hash.substring(1);
		var item=jboltAdminLeftNavs.find(".jbolt_admin_nav a[data-key='"+activeTabKey+"']");
		if(isOk(item)){
			if(!item.hasClass("active")){
				item.click();
				return true;
			}
		}
	} 
	return false;
}

/**
 * 执行锁屏
 * @param callback
 * @returns
 */
function exeLock(callback){
	var j_locksystem=$("#j_locksystem");
	if(!isOk(j_locksystem)||j_locksystem.is(":hidden")){
		Ajax.get("admin/lockSystem",function(){
			showJboltLockSystem();
			if(callback){
				callback();
			}
		});
	}
}
/**
 * 处理自动锁屏
 * @returns
 */
function processAutoLockScreen(){
	if(window.self!=window.top){return true;}
	var jbolt_ifvisible = jboltBody.find("script#jbolt_ifvisible");
	if(isOk(jbolt_ifvisible)){
		startIfVisible();
	}
	return true;
}
/**
 *开始执行ifvisible插件
 * @returns
 */
function startIfVisible(){
	var span=$("#JBOLT_AUTO_LOCK_SCREEN_SECONDS");
	if(isOk(span)){
		var seconds=$.trim(span.text());
		if(!isNaN(seconds)){
			JBOLT_AUTO_LOCK_SCREEN_SECONDS = Number(seconds);
		}
	}
	if(JBOLT_AUTO_LOCK_SCREEN_SECONDS>0&&JBOLT_AUTO_LOCK_SCREEN_SECONDS<=3600){
		ifvisible.setIdleDuration(JBOLT_AUTO_LOCK_SCREEN_SECONDS);
		ifvisible.idle(exeLock);
	}
}

/**
 * 初始化ajaxPortal里的右键菜单
 * @returns
 */
function initAjaxPortalContextMenu(){
	jboltBody.on("contextmenu","[data-ajaxportal][data-refresh]",function(e){
		e.preventDefault();
		e.stopPropagation();
		var portal=$(this);
		var pos=getMousePos(e);
		var refreshMenu=portal.find('.jbolt_ajaxportal_menu');
		if(!isOk(refreshMenu)){
			refreshMenu=$('<div class="dropdown-menu jbolt_ajaxportal_menu"><a tabindex="-1" class="dropdown-item" data-func="refresh" href="javascript:void(0)"><i class="fa fa-refresh mr-1"></i>刷新此区域</a></div>');
			portal.append(refreshMenu);
		}
		
		refreshMenu.on("click",function(e){
			e.preventDefault();
			e.stopPropagation();
			refreshMenu.hide();
			portal.ajaxPortal(true);
			return false;
		});
		refreshMenu.css({
			top:pos.y,
			left:pos.x
		});
		refreshMenu.show();
		return false;
	});
}
function hideAjaxPortalContextMenu(){
	$("div[data-ajaxportal]>.jbolt_ajaxportal_menu").hide();
}
//body点击事件处理一些额外
function initJboltBodyClick(){
	jboltBody.on("click",function(){
		JBoltInputUtil.hideJBoltInputLayer();
		hideAjaxPortalContextMenu();
	}).on("click","div[data-ajaxportal]",function(){
		hideAjaxPortalContextMenu();
	});
	
}
/**
 * 处理监听全局img onerror
 * @returns
 */
function initImgError(){
	//暂不实现
}
/**
 * 时间格式化为 yyyy-MM-dd HH:mm格式
 */
var date_ymdhm=function(s,defaultValue){
	return date(s,"yyyy-MM-dd HH:mm",defaultValue);
}
/**
 * 时间格式化为 yyyy-MM-dd HH:mm格式
 */
var date_ymdhms=function(s,defaultValue){
	return date(s,"yyyy-MM-dd HH:mm:ss",defaultValue);
}

/**
 * 时间格式化为 yyyy-MM-dd格式
 */
var date_ymd=function(s,defaultValue){
	return date(s,"yyyy-MM-dd",defaultValue);
}

/**
 * 时间格式化为 yyyy-MM格式
 */
var date_ym=function(s,defaultValue){
	return date(s,"yyyy-MM",defaultValue);
}
/**
 * 时间格式化为 HH:mm格式
 */
var date_hm=function(s,defaultValue){
	return date(s,"HH:mm",defaultValue);
}
/**
 * 时间格式化为 HH:mm:ss格式
 */
var date_hms=function(s,defaultValue){
	return date(s,"HH:mm:ss",defaultValue);
}

/**
 * 性别
 */
var sex=function(s,defaultValue){
	if(!s){
		return defaultValue?defaultValue:"-";
	}
	return s==1?"男":(s==2?"女":"-");
}

/**
 * 时间格式化为 HH:mm格式
 */
var time_hm=function(s,defaultValue){
	if(!s&&!defaultValue){return "";}
	if(s){
		if(s.indexOf(":")==-1){
			return "";
		}
		var sarr=s.split(":");
		if(sarr.length>=3){
			s=sarr[0]+":"+sarr[1];
		}
	}else if(!s||defaultValue){
		if(typeof(defaultValue)=="string"){
			if(defaultValue=="now"){
				var date=new Date();
				return date.getHours()+":"+date.getMinutes();
			}
			return defaultValue;
		}else{
			s=defaultValue;
		}
	}
	return s;
}
/**
 * 时间格式化为 HH:mm:ss格式
 */
var time_hms=function(s,defaultValue){
	if(!s&&!defaultValue){return "";}
	if(s){
		if(s.indexOf(":")==-1){
			return "";
		}
		var sarr=s.split(":");
		if(sarr.length>=3){
			s=sarr[0]+":"+sarr[1]+":"+sarr[2];
		}
	}else if(!s||defaultValue){
		if(typeof(defaultValue)=="string"){
			if(defaultValue=="now"){
				var date=new Date();
				return date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
			}
			return defaultValue;
		}else{
			s=defaultValue;
		}
	}
	return s;
}

/**
 * 时间格式化
 */
var date=function(s,fmt,defaultValue){
	if(!s&&!defaultValue){return "";}
	if(!s&&defaultValue){
		if(typeof(defaultValue)=="string"){
			if(defaultValue=="now"){
				return new Date().Format(fmt);
			}
			return defaultValue;
		}else{
			s=defaultValue;
		}
	}
	var type=typeof(s);
	if(type=="string"){
		s=new Date(s.replace(/-/g,'/'));
	}
	return s.Format(fmt);
}

/**
 * jbolt默认图片定义
 */
var jbolt_default_img_url="assets/img/uploadimg.png";
/**
 * juicer模板中使用计算获取真实URL地址
 */
var real_image=function(url){
	if(!url){
		return jbolt_default_img_url;
	}
	if(JBOLT_BASE_UPLOAD_PATH_PRE&&url.indexOf(JBOLT_BASE_UPLOAD_PATH_PRE)!=-1){
		url=url.replace(JBOLT_BASE_UPLOAD_PATH_PRE, "");
	}
	return actionUrl(url);

}



/*
 * 数字格式化 清除掉小数点后的无用的0
 * 参数说明：
 * number：要格式化的数字
 * decimals：保留几位小数
 * dec_point：小数点符号
 * thousands_sep：千分位符号
 * roundtag:舍入参数，默认 "ceil" 向上取,"floor"向下取,"round" 四舍五入
 * */
function numberFormat2(number, decimals, dec_point, thousands_sep,roundtag) {
	var value= numberFormat(number, decimals, dec_point, thousands_sep,roundtag);
	return removeNumberEndZero(value,false);
}
/*
 * 数字格式化 清除掉小数点后的无用的0 如果最后是0 转为空白不显示
 * 参数说明：
 * number：要格式化的数字
 * decimals：保留几位小数
 * dec_point：小数点符号
 * thousands_sep：千分位符号
 * roundtag:舍入参数，默认 "ceil" 向上取,"floor"向下取,"round" 四舍五入
 * */
function numberFormat3(number, decimals, dec_point, thousands_sep,roundtag) {
	var value= numberFormat2(number, decimals, dec_point, thousands_sep,roundtag);
	return value.toString()==="0"?"":value;
}
/*
 * 数字格式化
 * 参数说明：
 * number：要格式化的数字
 * decimals：保留几位小数
 * dec_point：小数点符号
 * thousands_sep：千分位符号
 * roundtag:舍入参数，默认 "ceil" 向上取,"floor"向下取,"round" 四舍五入
 * */
function numberFormat(number, decimals, dec_point, thousands_sep,roundtag) {
    number = (number + '').replace(/[^0-9+-Ee.]/g, '');
    roundtag = roundtag || "round"; //"ceil","floor","round"
    var n = !isFinite(+number) ? 0 : +number,
        prec = !isFinite(+decimals) ? 0 : Math.abs(decimals),
        sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep,
        dec = (typeof dec_point === 'undefined') ? '.' : dec_point,
        s = '',
        toFixedFix = function (n, prec) {

            var k = Math.pow(10, prec);
            return '' + parseFloat(Math[roundtag](parseFloat((n * k).toFixed(prec*2))).toFixed(prec*2)) / k;
        };
    s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.');
    var re = /(-?\d+)(\d{3})/;
    while (re.test(s[0])) {
        s[0] = s[0].replace(re, "$1" + sep + "$2");
    }

    if ((s[1] || '').length < prec) {
        s[1] = s[1] || '';
        s[1] += new Array(prec - s[1].length + 1).join('0');
    }
    return s.join(dec);
}


/**
 * 返回距离当前的时间差
 * @param tmpTime
 * @returns
 */
function prettyTime(tmpTime) {
	if(!tmpTime){return "刚刚";}
	var nowTime = new Date().getTime();//获取当前时间戳
	var timeType=typeof(tmpTime);
	var tmpTimeStamp;
	if(timeType=="object"){
		tmpTimeStamp=tmpTime.getTime();
	}else if(timeType=="string"){
		tmpTimeStamp=Date.parse(tmpTime.replace(/-/gi, "/"));
	}
    var mm=1000;//1000毫秒 代表1秒
    var minute = mm * 60;
    var hour = minute * 60;
    var day = hour * 24;
    var month = day * 30;
    var ansTimeDifference=0;//记录时间差
    var tmpTimeDifference = nowTime - tmpTimeStamp;//计算当前与需要计算的时间的时间戳的差值
    if (tmpTimeDifference < 0) {                //时间超出，不能计算
        return tmpTime;
    }
    //通过最开始强调的各个时间段用毫秒表示的数值，进行时间上的取整，为0的话，则没有到达
    var DifferebceMonth = tmpTimeDifference / month;    //进行月份取整
    var DifferebceWeek = tmpTimeDifference / (7 * day);//进行周取整
    var DifferebceDay = tmpTimeDifference / day;//进行天取整
    var DifferebceHour = tmpTimeDifference / hour;//进行小时取整
    var DifferebceMinute = tmpTimeDifference / minute;//进行分钟取整
    if (DifferebceMonth >= 1) {
        return tmpTime;                 //大于一个月 直接返回时间
    } else if (DifferebceWeek >= 1) {
        ansTimeDifference= parseInt(DifferebceWeek) + "个星期前";
    } else if (DifferebceDay >= 1) {
        ansTimeDifference = parseInt(DifferebceDay) + "天前";
    } else if (DifferebceHour >= 1) {
        ansTimeDifference = parseInt(DifferebceHour) + "个小时前";
    } else if (DifferebceMinute >= 1) {
        ansTimeDifference = parseInt(DifferebceMinute) + "分钟前";
    } else {
        ansTimeDifference = "刚刚";
    }
    return ansTimeDifference;
}
/**
 * 计算当前页面编号
 * @param pageNumber
 * @param pageSize
 * @param index
 * @returns
 */
function rownum(pageNumber,pageSize,index){
	if(!pageNumber){pageNumber=1;}
	if(!pageSize){pageSize=0;}
	return ((pageNumber-1)*pageSize)+(+index+1);
}

/**
 * tofixed
 */
function tofixed(number,count,removeAllZero,toZeroIfBlank){
	number=number?Number(number):0;
	count=count||0;
	var value=number?number.toFixed(count):0;
	if(removeAllZero){
		value=removeFixedNumberAllZero(value,count,toZeroIfBlank);
	}
	return value;
}
/**
 * 删除数字小数点后多余的0
 * @param value 数字值
 * @param toZeroIfBlank 转为0 默认false
 */
function removeNumberEndZero(value,toZeroIfBlank){
	if(typeof(value)=="undefined"){
		return toZeroIfBlank?0:value;
	}
	if(typeof(value)!="string"){
		value = value.toString();
	}
	if(value == "."){
		return toZeroIfBlank?0:"";
	}
	var size = value.length;
	if(size == 0){
		return toZeroIfBlank?0:value;
	}
	if(size>=2 && value.indexOf(".")!=-1){
		if(value.endWith(".")){
			value = value.substring(0,size-1);
		}else{
			var endIndex = -1;
			for(var i=size-1;i<size; i--){
				if(value.charAt(i)!='0'){
					endIndex = i;
					break;
				}
			}
			if(endIndex!=-1){
				if(value.charAt(endIndex)=='.'){
					value=value.substring(0,endIndex);
				}else{
					value=value.substring(0,endIndex+1);
				}
			}
		}
	}
	if(value.length == 0){
		return toZeroIfBlank?0:value;
	}
	if(value == "."){
		return toZeroIfBlank?0:"";
	}
	return value;
}

/**
 * 删除数字小数点后所有0 如果有
 * @param value 数字值
 * @param toFixedCount 小数点保留几位
 * @param toZeroIfBlank 转为0 默认false
 */
function removeFixedNumberAllZero(value,toFixedCount,toZeroIfBlank){
	if(typeof(value)=="undefined"){
		return toZeroIfBlank?0:value;
	}
	if(typeof(value)!="string"){
		value = value.toString();
	}
	if(value == "."){
		return toZeroIfBlank?0:"";
	}
	var size = value.length;
	if(size == 0){
		return toZeroIfBlank?0:value;
	}
	if(toFixedCount && value.length>0){
		var removeStr='.';
		for(var i=0;i<toFixedCount;i++){
			removeStr=removeStr+"0";
		}
		value=value.replace(removeStr,"");
	}
	if(value.length == 0){
		return toZeroIfBlank?0:value;
	}
	if(value == "."){
		return toZeroIfBlank?0:"";
	}
	return value;
}

/**
 * 字符串替换 函数
 * @param str
 * @param substr
 * @param replacement
 * @returns
 */
function replaceStr(str,substr,replacement){
	return str.replace(substr,replacement);
}
var colorClass=["primary","success","info","danger","warning","secondary"];
function colorClassByLevel(level){
	if(level<1||level>6){
		return "primary";
	}
	
	return colorClass[level-1];
}
var colorClass_checkstate=["secondary","info","success","danger"];
function colorClassByCheckState(state){
	if(state<1||state>4){
		return "secondary";
	}
	
	return colorClass_checkstate[state-1];
}

function colorClassByType(type){
	if(type<1||type>6){
		return "primary";
	}
	
	return colorClass[type-1];
}
var colorClass_Difficulty3=["success","primary","danger"];
var colorClass_Difficulty4=["success","primary","warning","danger"];
var colorClass_Difficulty5=["success","primary","info","warning","danger"];
function colorClassByDifficulty3(difficulty){
	if(difficulty<1||difficulty>3){
		return "info";
	}
	
	return colorClass_Difficulty3[difficulty-1];
}
function colorClassByDifficulty4(difficulty){
	if(difficulty<1||difficulty>4){
		return "info";
	}
	
	return colorClass_Difficulty4[difficulty-1];
}
function colorClassByDifficulty5(difficulty){
	if(difficulty<1||difficulty>5){
		return "secondary";
	}
	
	return colorClass_Difficulty5[difficulty-1];
}
var colorClass_byState=["secondary", "info", "danger","success", "warning", "dark"];
function colorClassByState(state){
	if(state<1||state>6){
		return "secondary";
	}
	
	return colorClass_byState[state-1];
}
function colorClassByServiceState(state){
	if(state<1||state>6){
		return "secondary";
	}
	
	return colorClass_byState[state-1];
}
var colorClass_priorityLevel=["priorityLevel_1","priorityLevel_2","priorityLevel_3","priorityLevel_4","priorityLevel_5","priorityLevel_6"];

function colorClassByPriorityLevel(level){
	if(level<1||level>6){
		return "primary";
	}
	return colorClass_priorityLevel[level-1];
}

/**
 * 初始化juicer模板引擎
 * @returns
 */
function initJuicer(){
	juicer.register("date",date);
	juicer.register("time_hm",time_hm);
	juicer.register("time_hms",time_hm);
	juicer.register("date_hm",date_hm);
	juicer.register("date_hms",date_hms);
	juicer.register("date_ym",date_ym);
	juicer.register("date_ymd",date_ymd);
	juicer.register("date_ymdhm",date_ymdhm);
	juicer.register("date_ymdhms",date_ymdhms);
	juicer.register("real_image",real_image);
	juicer.register("real_url",real_image);
	juicer.register("pretty_time",prettyTime);
	juicer.register("number_format",numberFormat);
	juicer.register("number_format2",numberFormat2);
	juicer.register("number_format3",numberFormat3);
	juicer.register("rownum",rownum);
	juicer.register("str_join",StrUtil.join);
	juicer.register("str_underline",StrUtil.underline);
	juicer.register("str_camel",StrUtil.camel);
	juicer.register("tofixed",tofixed);
	juicer.register("removeNumberZero",removeNumberEndZero);
	juicer.register("removeNumberEndZero",removeNumberEndZero);
	juicer.register("removeFixedNumberAllZero",removeFixedNumberAllZero);
	juicer.register("sex",sex);
	juicer.register("replaceStr",replaceStr);
	juicer.register("colorClassByLevel",colorClassByLevel);
	juicer.register("colorClassByType",colorClassByType);
	juicer.register("colorClassByCheckState",colorClassByCheckState);
	juicer.register("colorClassByDifficulty3",colorClassByDifficulty3);
	juicer.register("colorClassByDifficulty4",colorClassByDifficulty4);
	juicer.register("colorClassByDifficulty5",colorClassByDifficulty5);
	juicer.register("colorClassByState",colorClassByState);
	juicer.register("colorClassByServiceState",colorClassByServiceState);
	juicer.register("colorClassByPriorityLevel",colorClassByPriorityLevel);
	juicer.register("toJsonString",toJsonString);
}


function toJsonString(obj,needAttrs,fileUrlAttr){
	if(!obj){return "{}";}
	if(needAttrs&&needAttrs.length>0){
		var attr;
		var attrs = needAttrs.split(",");
		if(isArray(obj)){
			var tempObj;
			for(var i in obj){
				tempObj=obj[i];
				var newObj = {};
				for(var i in attrs){
					attr = attrs[i];
					if(attr){
						newObj[attr] = tempObj[attr];
					}
				}
				if(fileUrlAttr){
					newObj["isImg"] = isImg(tempObj[fileUrlAttr]);
					newObj["fileSuffix"] = getFileExtension(tempObj[fileUrlAttr]);
				}
			}
		}else{
			var newObj = {};
			for(var i in attrs){
				attr = attrs[i];
				if(attr){
					newObj[attr] = obj[attr];
				}
			}
			if(fileUrlAttr){
				newObj["isImg"] = isImg(tempObj[fileUrlAttr]);
				newObj["fileSuffix"] = getFileExtension(tempObj[fileUrlAttr]);
			}
			return JSON.stringify(newObj);
		}
		
	}
	return JSON.stringify(obj);
}
//初始化Bootstrap自带的tab组件JS
function initBootstrapTab(){
	jboltBody.on("click",".nav-tabs a.nav-link",function (e) {
		 e.preventDefault();
		  e.stopPropagation();
		  $(this).tab('show');
		  return false;
	});
}
/**
 * 判断是否引入pjax
 * @returns
 */
function isHasPjax(){
	var pjaxScript=$("#pjaxScript");
	return pjaxScript&&pjaxScript.length==1;
}
/**
 * 多文件上传
 */
var MultipleFileInputUtil={
		init:function(parentEle){
			 var parent=getRealParentJqueryObject(parentEle);
			 if(!isOk(parent)){return false;}
			 var inputs=parent.find("[data-multiplefileinput]");
			 if(!isOk(inputs)){return false;}
			 this.initInputs(inputs);
		},
		initInputs:function(inputs){
			 if(!isOk(inputs)){return false;}
			 var that=this;
			 loadJBoltPlugin(['fileinput'], function(){
				 var len=inputs.length;
				 for(var i=0;i<len;i++){
					 that.initInput(inputs.eq(i),false);
				 }
			 });
		},
		processHandler:function(input,handler,res){
			var exe_handler=eval(handler);
			if(exe_handler&&typeof(exe_handler)=="function"){
				exe_handler(input,res);
			}
		},
		changeSyncInputFiles:function(inputId,values){
			var findarr;
			for(var i in multipleUploadFiles){
				if(multipleUploadFiles[i]["key"]==inputId){
					for(var k in values){
						multipleUploadFiles[i]["value"].push(values[k]);
					}
					findarr=multipleUploadFiles[i]["value"];
					break;
				}
			}
			if(findarr){
				return findarr;
			}
			
//			var arr=new Array();
			var obj={"key":inputId,"value":values};
			multipleUploadFiles.push(obj);
			return obj["value"];
		},
		clearInputFiles:function(inputId){
			for(var i in multipleUploadFiles){
				if(multipleUploadFiles[i]["key"]==inputId){
					multipleUploadFiles[i]["value"]=[];
					break;
				}
			}
		},
		findInputFiles:function(inputId){
			if(multipleUploadFiles.length==0){
				var arr=new Array();
				var obj={"key":inputId,"value":arr};
				multipleUploadFiles.push(obj);
				return obj["value"];
			}else{
				var findarr;
				for(var i in multipleUploadFiles){
					if(multipleUploadFiles[i]["key"]==inputId){
						findarr=multipleUploadFiles[i]["value"];
						break;
					}
				}
				if(findarr){
					return findarr;
				}
				
				var arr=new Array();
				var obj={"key":inputId,"value":arr};
				multipleUploadFiles.push(obj);
				return obj["value"];
				
			}
		},
		setValueTo:function(input,res){
			var that=this;
			var setvalueto=input.data("setvalueto");
			if(!setvalueto){
				return false;
			}
			var inputId=input.attr("id");
			var sync=input.data("sync");
			var multipleFiles=null;
			if(sync){
				multipleFiles=that.changeSyncInputFiles(inputId,res.data);
			}else{
				multipleFiles=that.findInputFiles(inputId);
				multipleFiles.push(res.data);
			}
			
			if(multipleFiles&&multipleFiles.length>0){
				var urls=new Array();
				for(var i in multipleFiles){
					urls.push(multipleFiles[i].url);
					}
				var value=urls.join(",");
				$("#"+setvalueto).val(value).change();
			}
			
			
		},processRemoveSetValueTo:function(input,fileId){
			var that=this;
			var setvalueto=input.data("setvalueto");
			if(!setvalueto){
				return false;
			}
			//删除一条
			var inputId=input.attr("id");
			var sync=input.data("sync");
			var multipleFiles=that.findInputFiles(inputId);
			if(multipleFiles&&multipleFiles.length>0){
				var index=0;
				for(var i in multipleFiles){
					index=fileId.indexOf((sync?"_":"-")+multipleFiles[i].fileId);
						if(index!=-1){
							multipleFiles.splice(i,1);
						}
				}
				
				
				if(multipleFiles.length>0){
					var urls=new Array();
					for(var i in multipleFiles){
						urls.push(multipleFiles[i].url);
					}
					$("#"+setvalueto).val(urls.join(",")).change();
				}else{
					$("#"+setvalueto).val("").change();
				}
			}
			
		},processFileclearedSetValueTo:function(input){
			var that=this;
			var setvalueto=input.data("setvalueto");
			if(!setvalueto){
				return false;
			}
			var inputId=input.attr("id");
			$("#"+setvalueto).val("").change();
			that.clearInputFiles(inputId);
		},
		initEvent:function(input){
			var that=this;
			var handler=input.data("handler");
			if(handler){
				var sync=input.data("sync");
				if(sync){
					input.on('filebatchuploadsuccess', function(event, data, previewId, index) {
						if(handler=="setValueTo"){
							that.setValueTo(input,data.response);
						}else{
							that.processHandler(input,handler,data.response);
						}
					});
					 
				}else{
					input.on('fileuploaded', function(event, data, previewId, index) {
						if(handler=="setValueTo"){
							that.setValueTo(input,data.response);
						}else{
							that.processHandler(input,handler,data.response);
						}
					});
				}
			}
			//上传文件移除后
			input.on("filesuccessremove",function(e,id){
				if(handler&&handler=="setValueTo"){
					that.processRemoveSetValueTo(input,id);
				}
				var filesuccessremove=input.data("filesuccessremove");
				if(filesuccessremove){
					that.processHandler(input,filesuccessremove,id);
				}
			});
			//文件移除后
			input.on("fileremoved",function(e,id){
				if(handler&&handler=="setValueTo"){
					that.processRemoveSetValueTo(input,id);
				}
				var filesuccessremove=input.data("fileremoved");
				if(filesuccessremove){
					that.processHandler(input,filesuccessremove,id);
				}
			});
			//文件清空后
			input.on("filecleared",function(e){
				if(handler&&handler=="setValueTo"){
					that.processFileclearedSetValueTo(input);
				}
				var filecleared=input.data("filecleared");
				if(filecleared){
					that.processHandler(input,filecleared);
				}
			});
			
			var extraHandler=input.data("extrahandler");
			if(extraHandler){
				that.processHandler(input,extraHandler);
			}
		},
		_initInput:function(input,fielInputBox,options){
			if(fielInputBox&&fielInputBox.length==1){
				var newInput=input.clone();
				fielInputBox.before(newInput);
				fielInputBox.remove();
				newInput.fileinput(options);
				this.initEvent(newInput);
			}else{
				input.fileinput(options);
				this.initEvent(input);
			}
		},
		initInput:function(input,needLoadPlugin){
			var that=this;
			var chooseBtnText=input.data("choosebtntext");
			if(!chooseBtnText){
				chooseBtnText="请选择";
			}
			var maxFileCount=input.data("max-filecount");
			if(!maxFileCount){
				maxFileCount=9;
			}
			var maxFileSize=input.data("max-filesize");
			if(!maxFileSize){
				maxFileSize=200;
			}
			var theme=input.data("theme");
			if(!theme){
				theme="fa";
			}
			var sync=input.data("sync");
			var uploadAsync=true;
			if(sync){
				uploadAsync=false;
			}
			var uploadUrl=input.data("uploadurl");
			if(!uploadUrl){
				LayerMsgBox.alert("请设置多文件组件上传地址：data-uploadurl",2);
			}
			var options={
					enctype: 'multipart/form-data',
			        previewFileType: "any",
			        theme:theme,
			        uploadAsync:uploadAsync,
			        allowedPreviewTypes:['image', 'html', 'text', 'video', 'audio', 'flash', 'object'],
			        uploadUrl:uploadUrl,
			        append:true,
			        overwriteInitial:false,
			        maxFileCount:maxFileCount,
			        maxFileSize:maxFileSize,
			        initialPreviewAsData:true,
			        showSort:true,
			        showCaption:false,
			        showCancel:false,
			        language:"zh",
			        browseClass: "btn btn-success",
			        browseLabel: chooseBtnText,
			        removeClass: "btn btn-danger",
			        removeLabel: "清空",
			        uploadClass: "btn btn-info",
			        uploadLabel: "全部上传",
			        fileActionSettings: {                               // 在预览窗口中为新选择的文件缩略图设置文件操作的对象配置
			            showRemove: true,                                   // 显示删除按钮
			            showUpload: false,                                   // 显示上传按钮
			            showDownload: false,                            // 显示下载按钮
			            showZoom: true,                                    // 显示预览按钮
			            showDrag: true,                                        // 显示拖拽
			        }
			    };
			var deleteUrl=input.data("deleteurl");
			if(deleteUrl){
				options['deleteUrl']=deleteUrl;
			}
			var dataOptions=input.data("options");
			if(dataOptions){
				var dpoptionFunc=eval(dataOptions);
				if(dpoptionFunc&&typeof(dpoptionFunc)=="function"){
					var configOptions=dpoptionFunc();
					Object.assign(options,configOptions);
				}
				
			}
			var inputId=input.attr("id");
			var fielInputBox=input.closest(".file-input");
			if(needLoadPlugin){
				loadJBoltPlugin(['fileinput'], function(){
					that._initInput(input,fielInputBox,options);
				});
			}else{
				this._initInput(input,fielInputBox,options);
			}
			
		}
}
/**
 * JBolt-treeTable封装调用
 */
var JBoltTreeTableUtil={
		init:function(parentEle){
			var parent=getRealParentJqueryObject(parentEle);
			if(!isOk(parent)){return false;}
			var tables=parent.find("[data-jbolttreetable]");
			if(!isOk(tables)){return false;}
			this.initTables(tables);
		},
		initTables:function(tables){
			if(!isOk(tables)){return false;}
			var that=this;
			loadJBoltPlugin(["jbolttable"], function(){
				var len=tables.length;
				for(var i=0;i<len;i++){
					that.initTable(tables.eq(i),false);
				}
			});
		},
		initTable:function(table,needLoadPlugin){
			var that=this;
			if(needLoadPlugin){
				loadJBoltPlugin(['jbolttable'], function(){
					that._initTable(table);
				});
			}else{
				this._initTable(table);
			}
		},
		_initTable:function(table){
			table.jboltTreeTable();
		}
}
/**
 * JBolt-Table封装调用
 */
var JBoltTableUtil={
		init:function(parentEle){
			 var parent=getRealParentJqueryObject(parentEle);
			 if(!isOk(parent)){return false;}
			 var tables=parent.find("[data-jbolttable]:not([data-jbolttreetable])");
			 if(!isOk(tables)){return false;}
			 this.initTables(tables);
		},
		initTables:function(tables){
			if(!isOk(tables)){return false;}
			var that=this;
			loadJBoltPlugin(["jbolttable"], function(){
				var len=tables.length;
				for(var i=0;i<len;i++){
					that.initTable(tables.eq(i),false);
				}
			});
		},
		initTable:function(table,needLoadPlugin){
			var that=this;
			if(needLoadPlugin){
				loadJBoltPlugin(['jbolttable'], function(){
					that._initTable(table);
				});
			}else{
				this._initTable(table);
			}
		},
		_initTable:function(table){
			if(!window.jbolt_table_js_version){return null;}
			if(!table[0].hasAttribute("data-jbolttable")){
				table.attr("data-jbolttable",true);
			}
			table.jboltTable();
		},
		get:function(tableEle){
			if(!window.jbolt_table_js_version){return null;}
			return getJBoltTable(tableEle);
		},
		getInst:function(tableEle){
			if(!window.jbolt_table_js_version){return null;}
			return getJBoltTableInst(tableEle);
		}
}
/**
 * 深拷贝
 * @param obj
 * @returns
 */
function deepClone(obj) {
	//通过原型对象获取对象类型
	var type = Object.prototype.toString.call(obj); 
	var newObj;
	if(type ==='[object Array]'){
	//数组
	newObj =[];
		if(obj.length >0){
			for(var x=0;x<obj.length;x++){
			newObj.push(deepClone(obj[x]));
			}
		}
	}else if(type==='[object Object]'){
		//对象
		newObj = {};
	for(var x in obj) {
		newObj[x] = deepClone(obj[x]);
	}
	}else{
		//基本类型和方法可以直接赋值
		newObj = obj;
	}
	return newObj;
}

//封装的lobibox 取名JBoltNotifyBox
var JBoltNotifyBox={
		notify:function(type,options){
			var defaultOptions={width:260,size:'mini',delay:false,title:false,msg:"消息",position:"top right",img:false};
			var keys = Object.keys(options);
			if(keys && keys.length>0){
				for(var i in keys){
					defaultOptions[keys[i]] = options[keys[i]];
				}
			}
			Lobibox.notify(type,defaultOptions);
		},
		success:function(options){
			options.msg = options.msg||"成功";
			this.notify("success",options);
		},
		error:function(options){
			options.msg = options.msg||"失败";
			this.notify("error",options);
		},
		warning:function(options){
			options.msg = options.msg||"警告";
			this.notify("warning",options);
		},
		info:function(options){
			options.msg = options.msg||"消息";
			this.notify("info",options);
		}
}
//数组工具类
var JBoltArrayUtil={
		//删除指定值元素
		remove:function(array,value){
			if(isOk(array)&&typeof(value)!=undefined&&typeof(value)!="undefined"){
				$.each(array,function(index,item){
					if(item===value){
						array.splice(index,1);
						return false;
					}
				});
			}
		},
		//按照坐标替换数据
		replace:function(array,index,data){
			array.splice(index,1,data);
		},
		//按下标合并数据
		merge:function(array,index,data){
			if(typeof(data)=="undefined" || index<0 || index >= array.length){return;}
			var oldData=array[index];
			var keys=Object.keys(data);
			for(var i in keys){
				oldData[keys[i]]=data[keys[i]];
			}
		},
		//首插数据
		prepend:function(array,prependDatas){
			if(prependDatas){
				var datas=deepClone(prependDatas);
				if(isArray(datas)){
					var size=datas.length;
					var startIndex=size-1;
					for(var i=startIndex;i>=0;i--){
						array.unshift(datas[i]);
					}
				}else{
					array.unshift(datas);
				}
			}
			
		},
		//尾插数据
		append:function(array,appendDatas){
			if(appendDatas){
				var datas=deepClone(appendDatas);
				if(isArray(datas)){
					var size=datas.length;
					for(var i=0;i<size;i++){
						array.push(datas[i]);
					}
				}else{
					array.push(datas);
				}
			}
		},
		//按下标删除
		removeByIndex:function(array,index,count){
			array.splice(index,count?count:1);
		},
		//插入数据
		insert:function(array,index,insertDatas){
			if(array&&insertDatas){
				var datas=deepClone(insertDatas);
				if(array.length==0){
					this.append(array,datas);
				}else{
					if(index<0){//如果下标小于0 就首前插
						this.prepend(array,datas);
					}else if(index>=array.length){
						this.append(array,datas);
					}else{
						if(isArray(datas)){
							var size=datas.length;
							for(var i=0;i<size;i++){
								array.splice(index+i,0,datas[i]);
							}
						}else{
							array.splice(index,0,datas);
						}
					}
				}
			}
		},
		//根据数据里对象的某一属性值相等判断 删除
		removeObjectByAttr:function(array,attr,attrValue){
			var that=this;
			if(isOk(array)&&attr){
				$.each(array,function(index,item){
					if(item[attr]===attrValue){
//						array.splice(index,1);
						that.removeByIndex(array,index);
						return false;
					}
				});
			}
		},
		//改变数组里指定下标数据的指定属性值
		changeOneItemAttrValue:function(array,index,attr,attrValue){
			if(isOk(array)&&index>=0&&attr){
				var size=array.length;
				if(index<=size-1){
					array[index][attr]=attrValue;
				}
			}
		},
		//改变数组里指定属性值数据的指定属性值
		changeOneItemOtherAttrValueByAttr:function(array,attr,attrValue,otherAttr,otherAttrValue){
			if(isOk(array)){
				var theIndex=-1;
				$.each(array,function(index,item){
					if(item[attr]==attrValue){
						theIndex = index;
						return false;
					}
				});
				
				this.changeOneItemAttrValue(array,theIndex,otherAttr,otherAttrValue);
			}
		},
		//改变数组中所有对象的指定属性值
		changeItemsAttrValue:function(array,attr,attrValue){
			if(isOk(array)&&attr){
				$.each(array,function(index,item){
					item[attr]=attrValue;
				});
			}
		},unique:function(arr){
			//如果新数组的当前元素的索引值 == 该元素在原始数组中的第一个索引，则返回当前元素
			return arr.filter(function(item,index){
				return arr.indexOf(item,0) === index;
				});
		},
		moveUp:function(arr, moveIndexArr, toIndex){
			return this.move(arr,moveIndexArr,toIndex,true);
		},
		moveDown:function(arr, moveIndexArr, toIndex){
			return this.move(arr,moveIndexArr,toIndex,false);
		},
		move:function(arr, moveIndexArr, toIndex,isBeforAfter){
			var start,arr2,arr3,arr4,firstIndex=moveIndexArr[0],len=arr.length,lastIndex=moveIndexArr[moveIndexArr.length-1];//1,2, 3 ,4,5, 6,7 ,8
			if(isBeforAfter){
				start= arr.slice(0,toIndex);
				arr2 = arr.slice(moveIndexArr[0],moveIndexArr[moveIndexArr.length-1]+1);
				arr3 = arr.slice(toIndex,moveIndexArr[0]);
				arr4 = arr.slice(moveIndexArr[moveIndexArr.length-1]+1);
			}else{
				start= arr.slice(0,firstIndex);
				arr2 = arr.slice(moveIndexArr[moveIndexArr.length-1]+1,toIndex+1)
				arr3 = arr.slice(firstIndex,moveIndexArr[moveIndexArr.length-1]+1);
				arr4 = arr.slice(toIndex+1);
			}
			return start.concat(arr2).concat(arr3).concat(arr4);
		}
}

var StrUtil={
    //把字串连接起来，例如：StrUtil.join('a','b','c','d',';') => a;b;c;d
    join : function(){
        if(arguments.length<=2){ return arguments[0];}
        var datas = [];
        var delimter = arguments[arguments.length-1];
        for(var i=0;i<arguments.length-1;i++){
            if(/^\s*$/.test(arguments[i])){
            	continue;
            }
            datas.push(arguments[i]);
        }
        return datas.join(delimter);
    },
    //转为下划线：StrUtil.underline('userRole',true) => USER_ROLE
    //转为下划线：StrUtil.underline('userRole',false) => user_role
    underline : function(str,upper) {
        var ret = str.replace(/([A-Z])/g,"_$1");
        if(upper){
            return ret.toUpperCase();
        }else{
            return ret.toLowerCase();
        }
    },
    //转驼峰表示：StrUtil.camel('USER_ROLE',true) => UserRole
    //转驼峰表示：StrUtil.camel('USER_ROLE',false) => userRole
    camel : function(str,firstUpper) {
    	var ret;
    	if(str.indexOf("_")!=-1){
    		ret = str.toLowerCase();
    		ret = ret.replace( /_([\w+])/g, function( all, letter ) {
    	       return letter.toUpperCase();
    	    });
    	}else{
    		ret = str;
    	}
        if(firstUpper){
            ret = ret.replace(/\b(\w)(\w*)/g, function($0, $1, $2) {
                return $1.toUpperCase() + $2;
            });
        }
        return ret;
    }
}


/**
 * 初始化
 * @returns
 */
$(function(){
	//处理自动锁屏
	var success = processAutoLockScreen();
	if(!success){
		return false;
	}

//	var withtabs=isWithtabs();
	//全局初始化
	initJboltAdmin();
	//如果是需要pjax并且引入了pjax是单页加载模式
	if(needPjax&&hasPjax){
		if(jboltWithTabs){
			//pjax+多选项卡模式下
			JBoltTabUtil.init();
			JBoltTabUtil.addJboltTabWithoutContentUrl();
			//找到当前的一个选项卡内容区域 执行加载完成后的js
			var currentTabContent=JBoltTabUtil.getCurrentTabContent();
			if(currentTabContent&&currentTabContent.length==1){
				afterAjaxPortal(currentTabContent);
			}
			//多选项卡模式下的form提交会提交选项卡内容区域
			initAdminAjaxPortalFormSubmitEvent();
		}else{
			//初始化全局pjax
			JBoltPjaxUtil.initAdminPjax();
		}
	}else{
		pageLoadRequirePluginAndInit();
		//非pjax模式下 一般是在直接打开内页地址或者dialog中
		if(jboltWithTabs){
			JBoltTabUtil.init();
			initAdminAjaxPortalFormSubmitEvent();
		}else{
			JBoltPjaxUtil.initPjaxEvent();
		}
		//上来直接进入界面 需要各种组件的初始化
		SelectUtil.initAutoSetValue();
		FileUploadUtil.init();
		ImgUploadUtil.init();
		SwitchBtnUtil.init();
		FormDate.init();
		CityPickerUtil.init();
		LayerTipsUtil.init();
		HtmlEditorUtil.init();
		ImageViewerUtil.init();
		AutocompleteUtil.init();
		RadioUtil.init();
		CheckboxUtil.init();
		Select2Util.init();
		SelectUtil.init();
		MultipleFileInputUtil.init();
		AjaxPortalUtil.init();
		JSTreeUtil.init();
		JBoltInputUtil.init();
		RangeSliderUtil.init();
		JBoltInputWithClearBtnUtil.init();
		JBoltInputWithCalculatorUtil.init();
		initToolTip();
		initPopover();
		JBoltTableUtil.init();
		JBoltTreeTableUtil.init();
		JBoltTabViewUtil.initUI();
		TextareaUtil.initUI();
		findRequiredAndStarIt();
	}
		//window resize处理
		onwindowReisze();
		mainScrollEvent();
		JBoltClipboardUtil.init();
	});

function mainScrollEvent(){
	mainPjaxContainer.on("scroll",function(){
		AutocompleteUtil.hideResult(mainPjaxContainer);
		JBoltInputUtil.hideJBoltInputLayer();
	});
}

/**
 * 寻找表单 required项 并自动添加星标
 * @param parentEle
 * @returns
 */
function findRequiredAndStarIt(parentEle){
	if(!autoShowRequiredLabelStar){return false;}
	 var parent=getRealParentJqueryObject(parentEle);
	 if(!isOk(parent)){return false;}
	 var sl="[data-rule]:not([data-notnull='false']):not([type='hidden']):not([data-radio])";
	 var inputs=parent.find(sl);
	 if(isOk(inputs)){
		 inputs.each(function(i){
			 requiredAndStarIt(inputs.eq(i))
		 })
	 }
}
/**
 * 单个Input设置为isRequired
 * @param input
 * @returns
 */
function requiredAndStarIt(input){
	var pgroup,mlabels,mlabel,inputp,prevL;
	if(input[0].hasAttribute("data-checkbox")){
		mlabel=input.find("label:first");
		if(mlabel.parent().hasClass("checkbox")==false && !mlabel.hasClass("is_required")){
			mlabel.addClass("is_required");
		}
	}else{
		pgroup=input.closest(".form-group");
		if(isOk(pgroup)){
			mlabels=pgroup.find("label");
			if(isOk(mlabels)){
				if(input.hasClass("ac_input")){
					prevL=input.parent().prev();
				}else{
					prevL=input.prev();
				}
				if(isOk(prevL)){
					if(prevL[0].tagName=="LABEL"){
						mlabel=prevL;
					}else{
						inputp=input.parent();
					}
				}else{
					if(input.hasClass("ac_input")||input.parent().hasClass("ac_input_box")){
						inputp=input.parent().parent();
					}else{
						inputp=input.parent();
					}
				}
				
				if(isOk(inputp)&&inputp[0].tagName=="DIV"){
					if(inputp.hasClass("input-group")||inputp.hasClass("form-group")){
						mlabel=pgroup.find("label:first");
					}else{
						var pprev=inputp.prev();
						if(isOk(pprev)){
							if(pprev[0].tagName=="LABEL"){
								mlabel=pprev;
							}
						}
						
					}
				}
				
				if(isOk(mlabel) && !mlabel.hasClass("is_required")){
					mlabel.addClass("is_required");
				}
				
				
				
			}
		}else{
			pgroup=input.closest(".input-group");
			if(isOk(pgroup)){
				mlabel=pgroup.find(".input-group-text");
				if(isOk(mlabel) && !mlabel.hasClass("is_required")){
					mlabel.addClass("is_required");
				}
			}
		}
	}
}
/**
 * 单个Input 去掉isRequired
 * @param input
 * @returns
 */
function removeRequiredAndStar(input){
	var pgroup,mlabels,mlabel,inputp,prevL;
	 if(input[0].hasAttribute("data-checkbox")){
		 mlabel=input.find("label:first");
		 if(mlabel.parent().hasClass("checkbox")==false && mlabel.hasClass("is_required")){
			 mlabel.removeClass("is_required");
		 }
	 }else{
		 pgroup=input.closest(".form-group");
		 if(isOk(pgroup)){
			 mlabels=pgroup.find("label");
			 if(isOk(mlabels)){
				 if(input.hasClass("ac_input")){
					 prevL=input.parent().prev();
				 }else{
					 prevL=input.prev();
				 }
				 if(isOk(prevL)){
					 if(prevL[0].tagName=="LABEL"){
						 mlabel=prevL;
					 }else{
						 inputp=input.parent();
					 }
				 }else{
					 if(input.hasClass("ac_input")||input.parent().hasClass("ac_input_box")){
						 inputp=input.parent().parent();
					 }else{
						 inputp=input.parent();
					 }
				 }
				 
				 if(isOk(inputp)&&inputp[0].tagName=="DIV"){
					 if(inputp.hasClass("input-group")){
						 mlabel=pgroup.find("label:first");
					 }else{
						 var pprev=inputp.prev();
						 if(isOk(pprev)){
							 if(pprev[0].tagName=="LABEL"){
								 mlabel=pprev;
							 }
						 }
						 
					 }
				 }
				 
				 if(isOk(mlabel) && mlabel.hasClass("is_required")){
					 mlabel.removeClass("is_required");
				 }
				 
				 
				 
			 }
		 }else{
			 pgroup=input.closest(".input-group");
			 if(isOk(pgroup)){
				 mlabel=pgroup.find(".input-group-text");
				 if(isOk(mlabel) && mlabel.hasClass("is_required")){
					 mlabel.removeClass("is_required");
				 }
			 }
		 }
	 }
}

/**
 * 刷新当前ele所在的ajaxPortal
 * @param ele
 * @returns
 */
function refreshCurrentAjaxPortal(ele){
	var eleObj=getRealJqueryObject(ele);
	var portal=eleObj.closest("[data-ajaxportal]");
	if(isOk(portal)){
		portal.ajaxPortal(true);
	}
}
function showReloginDialog(showMsg,handler){
	if(window.self!=window.top){
		parent.showReloginDialog(showMsg,handler);
	}else{
		layer.closeAll();
		DialogUtil.openNewDialog({
			title:"请重新登录",
			width:"500",
			height:"400",
			offset:"auto",
			zIndex:20220610,
			url:"admin/relogin",
			handler:handler?handler:refreshPjaxContainer
		});
		if(showMsg || typeof(showMsg)=="undefined"){
			LayerMsgBox.error("当前登录已经失效，请重新登录系统!",2000);
		}
	}
}
/**
 * 表单重置
 * @param formEle
 * @param confirm
 * @returns
 */
function formReset(formEle,confirm){
	var exe=function(){
		if(isDOM(formEle)){
			formEle.reset();
		}else{
			var form=getRealJqueryObject(formEle);
			if(isOk(form)){
				form[0].reset();
			}
		}
	}
	if(confirm){
		LayerMsgBox.confirm("确认重置表单？",exe);
	}else{
		exe();
	}
	
}

//dom resize
;(function($, h, c) {
	var a = $([]), e = $.resize = $.extend($.resize, {}), i, k = "setTimeout", j = "resize", d = j
			+ "-special-event", b = "delay", f = "throttleWindow";
	e[b] = 350;
	e[f] = true;
	$.event.special[j] = {
		setup : function() {
			if (!e[f] && this[k]) {
				return false
			}
			var l = $(this);
			a = a.add(l);
			$.data(this, d, {
				w : l.width(),
				h : l.height()
			});
			if (a.length === 1) {
				g()
			}
		},
		teardown : function() {
			if (!e[f] && this[k]) {
				return false
			}
			var l = $(this);
			a = a.not(l);
			l.removeData(d);
			if (!a.length) {
				clearTimeout(i)
			}
		},
		add : function(l) {
			if (!e[f] && this[k]) {
				return false
			}
			var n;
			function m(s, o, p) {
				var q = $(this), r = $.data(this, d);
				r.w = o !== c ? o : q.width();
				r.h = p !== c ? p : q.height();
				n.apply(this, arguments)
			}
			if ($.isFunction(l)) {
				n = l;
				return m
			} else {
				n = l.handler;
				l.handler = m
			}
		}
	};
	function g() {
		i = h[k](function() {
			a.each(function() {
				var n = $(this), m = n.width(), l = n.height(), o = $
						.data(this, d);
				if (m !== o.w || l !== o.h) {
					n.trigger(j, [ o.w = m, o.h = l ])
				}
			});
			g()
		}, e[b])
	}
})(jQuery, this);

function showMsgCenterRedDot(){
	if(isOk(msgCenterRedDot)){
		msgCenterRedDot.show();
	}
}
function hideMsgCenterRedDot(){
	if(isOk(msgCenterRedDot)){
		msgCenterRedDot.hide();
	}
}
/**
 * 读取是否存在未读消息中心数据 包含sysNotice todo privateMessage
 * @returns
 */
function readUserMsgCenterUnreadInfo(callback){
	Ajax.get("admin/msgcenter/unreadInfo",function(res){
		if(res.data){
			showMsgCenterRedDot();
		}else{
			hideMsgCenterRedDot();
		}
		if(callback){
			callback();
		}
	});
}
/**
 * 初始化执行 用户消息中心读取
 * @returns
 */
function initReadUserMsgCenterUnreadInfo(){
	if(isOk(msgCenterRedDot)){
		//有这个组件才执行
		readUserMsgCenterUnreadInfo(function(){
			setInterval(readUserMsgCenterUnreadInfo,30000);
		});
	}
}

function jboltTableGetHiprintUrl(btnEle,dataUrl,isSingleLine){
	var btn = getRealJqueryObject(btnEle);
	var tableId = btn.data("jbolt-table-id");
	if(!tableId){
		LayerMsgBox.alert("按钮未绑定表格");
		return false;
	}
	var url = dataUrl;
	var checkResult = isSingleLine?jboltTableGetCheckedId(btn):jboltTableGetCheckedIds(btn);
	if(!checkResult){
		return false;
	}
	if(typeof(checkResult)!="boolean"){
		if(isArray(checkResult)){
			url=url+checkResult.join(",");
		}else{
			url=url+checkResult;
		}
	}
	//处理URL 和关联的参数元素值
	url=actionUrl(url);
	url=processEleUrlByLinkOtherParamEle(btn,url);
	return url;
}
/**
 * jboltTablehiprint 直接导出PDF
 * @param btnEle            点击的按钮
 * @param tplSn             hiprint 模板sn编号
 * @param dataUrl           json数据的url接口地址
 * @param isSingleLine      限制是否只能表格单条选择打印 默认支持多条
 * @param fileName          导出PDF文件名
 * @returns
 */
function jboltTableHiprintToPdf(btnEle,tplSn,dataUrl,isSingleLine,fileName){
	var url = jboltTableGetHiprintUrl(btnEle,dataUrl,isSingleLine);
	if(url){
		loadJBoltPlugin(['hiprint'], function(){
			jboltHiprintToPdf(tplSn,'url',url,fileName);
		});
	}
}
/**
 * JBoltTable表格打印选中的数据 
 * @param btnEle            点击的按钮
 * @param tplSn             hiprint 模板sn编号
 * @param dataUrl           json数据的url接口地址
 * @param isSingleLine      限制是否只能表格单条选择打印 默认支持多条
 * @returns
 */
function jboltTableHiprintWebPrint(btnEle,tplSn,dataUrl,isSingleLine){
	var url = jboltTableGetHiprintUrl(btnEle,dataUrl,isSingleLine);
	if(url){
		loadJBoltPlugin(['hiprint'], function(){
			jboltHiprintWebPrint(tplSn,'url',url);
		});
	}
}
/**
 * 执行web预览打印
 * @param tplSn
 * @param type 类型 url or json
 * @param printData 可以是一个URL接口地址 也可以是json
 * @returns
 */
function jboltHiprintWebPrint(tplSn,type,printData){
	if(!tplSn){
		LayerMsgBox.alert("请指定正确的模板SN编号",2);
		return;
	}
	//如果没设置printData 或者json空 报错
	if(!printData){
		LayerMsgBox.alert("请填写JSON数据",2);
		return;
	}
	
	if(type=="json" && (typeof(printData)!="object" || !jsonObjectValueIsOk(printData))){
		LayerMsgBox.alert("请填写JSON数据",2);
		return;
	}
	var printTemplate;
	LayerMsgBox.loading("执行中...",20000);
	Ajax.get("admin/hiprint/tpl/content?sn="+tplSn,function(res){
		if(!res.data){
			LayerMsgBox.alert("打印模板无效",2);
		}else{
			if(type == "url"){
				Ajax.get(printData,function(res2){
					if(res2.data){
						printTemplate = new hiprint.PrintTemplate({ template: JSON.parse(res.data)});
						printTemplate.print(res2.data);
						LayerMsgBox.closeLoadingNow();
					}else{
						LayerMsgBox.alert("未读取到有效打印数据",2);
					}
				});
			}else{
				printTemplate = new hiprint.PrintTemplate({ template: JSON.parse(res.data)});
				printTemplate.print(printData);
				LayerMsgBox.closeLoadingNow();
			}
		}
	});
}

/**
 * 执行web页面显示渲染
 * @param ele
 * @param tplSn
 * @param type
 * @param printData
 * @returns
 */
function jboltHiprintWebRender(ele,tplSn,type,printData){
	var renderBox=getRealJqueryObject(ele);
	if(!isOk(renderBox)){
		LayerMsgBox.alert("请指定正确的渲染容器ele",2);
		return;
	}
	if(!tplSn){
		LayerMsgBox.alert("请指定正确的模板SN编号",2);
		return;
	}
	//如果没设置printData 或者json空 报错
	if(!printData){
		LayerMsgBox.alert("请填写JSON数据",2);
		return;
	}
	
	if(type=="json" && (typeof(printData)!="object" || !jsonObjectValueIsOk(printData))){
		LayerMsgBox.alert("请填写JSON数据",2);
		return;
	}
	LayerMsgBox.loading("渲染中...",20000);
	var printTemplate;
	renderBox.empty();
	Ajax.get("admin/hiprint/tpl/content?sn="+tplSn,function(res){
		if(!res.data){
			LayerMsgBox.alert("打印模板无效",2);
		}else{
			if(type == "url"){
				Ajax.get(printData,function(res2){
					if(res2.data){
						printTemplate = new hiprint.PrintTemplate({ template: JSON.parse(res.data)});
						var htmlContent = printTemplate.getHtml(res2.data).html();
						if(htmlContent){
							renderBox.html(htmlContent);
							renderBox.data("printtemplate", printTemplate);
							renderBox.data("printdata", res2.data);
						}
						LayerMsgBox.closeLoadingNow();
					}else{
						LayerMsgBox.alert("未读取到有效打印数据",2);
					}
				});
			}else{
				printTemplate = new hiprint.PrintTemplate({ template: JSON.parse(res.data)});
				var htmlContent = printTemplate.getHtml(printData).html();
				if(htmlContent){
					renderBox.html(htmlContent);
					renderBox.data("printtemplate", printTemplate);
					renderBox.data("printdata", printData);
				}
				LayerMsgBox.closeLoadingNow();
			}
		}
	});
}

/**
 * 使用渲染区域html打印
 * @param ele
 * @returns
 */
function jboltHiprintWebPrintByHtml(ele){
	var renderBox=getRealJqueryObject(ele);
	if(!isOk(renderBox)){
		LayerMsgBox.alert("请指定正确的渲染容器ele",2);
		return;
	}
	LayerMsgBox.loading("执行中...",20000);
	var printTemplate = renderBox.data("printtemplate");
	if(printTemplate){
		var printData = renderBox.data("printdata");
		if(printData){
			printTemplate.print(printData);
			LayerMsgBox.closeLoadingNow();
		}else{
			LayerMsgBox.alert("未读取到有效打印数据",2);
		}
	}else{
		LayerMsgBox.closeLoadingNow();
	}
	
}
/**
 * 使用渲染区域html导出pdf
 * @param ele
 * @param fileName
 * @returns
 */
function jboltHiprintToPdfByHtml(ele,fileName){
	var renderBox=getRealJqueryObject(ele);
	if(!isOk(renderBox)){
		LayerMsgBox.alert("请指定正确的渲染容器ele",2);
		return;
	}
	LayerMsgBox.loading("执行中...",20000);
	fileName = (fileName?fileName:("导出PDF_"+new Date().Format("yyyyMMddHHmmss")));
	var printTemplate = renderBox.data("printtemplate");
	if(printTemplate){
		var printData = renderBox.data("printdata");
		if(printData){
			printTemplate.toPdf(printData,fileName);
			LayerMsgBox.closeLoadingNow();
		}else{
			LayerMsgBox.alert("未读取到有效数据",2);
		}
	}else{
		LayerMsgBox.closeLoadingNow();
	}
	
}
/**
 * 执行导出PDF
 * @param tplSn
 * @param type
 * @param printData
 * @param fileName
 * @returns
 */
function jboltHiprintToPdf(tplSn,type,printData,fileName){
	if(!tplSn){
		LayerMsgBox.alert("请指定正确的模板SN编号",2);
		return;
	}
	//如果没设置printData 或者json空 报错
	if(!printData){
		LayerMsgBox.alert("请填写JSON数据",2);
		return;
	}
	
	if(type=="json" && (typeof(printData)!="object" || !jsonObjectValueIsOk(printData))){
		LayerMsgBox.alert("请填写JSON数据",2);
		return;
	}
	var printTemplate;
	fileName = (fileName?fileName:("导出PDF_"+new Date().Format("yyyyMMddHHmmss")));
	LayerMsgBox.loading("导出PDF...",60000);
	Ajax.get("admin/hiprint/tpl/content?sn="+tplSn,function(res){
		if(!res.data){
			LayerMsgBox.alert("打印模板无效",2);
		}else{
			if(type == "url"){
				Ajax.get(printData,function(res2){
					if(res2.data){
						printTemplate = new hiprint.PrintTemplate({ template: JSON.parse(res.data)});
						printTemplate.toPdf(res2.data,fileName);
						LayerMsgBox.closeLoadingNow();
					}else{
						LayerMsgBox.alert("未读取到有效数据",2);
					}
				});
			}else{
				printTemplate = new hiprint.PrintTemplate({ template: JSON.parse(res.data)});
				printTemplate.toPdf(printData,fileName);
				LayerMsgBox.closeLoadingNow();
			}
		}
	});
	 
}
/**
 * 自由拖动
 * @param trigger
 * @param box
 * @param parent
 * @returns
 */
function freelyDragging(trigger,box,parent){
	trigger.on("mousedown",function(e){
		disposeTooltip(trigger);
		box.draging=true;
		parent.addClass("noselect");
	});
	parent.on("mousemove",function(e){
		if(box.draging){
			box.offset({
				top:e.clientY-30,
				left:e.clientX-25
			});
		}
	});
	parent.on("mouseup",function(e){
		disposeTooltip(trigger);
		box.draging=false;
		parent.removeClass("noselect");
	});
}


function hideJBoltPageLoading(){
	setTimeout(function(){
		$("#jbolt_page_loader_main").fadeOut(200);
	},500);
}