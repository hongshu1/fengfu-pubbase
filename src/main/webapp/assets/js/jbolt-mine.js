/****这个文件是用来提供个人扩展和编写js使用的文件****/
/**
 * 可在此自行扩展juicer注册函数
 * 扩展方式参考jbolt-admin.js-initJuicer()方法
 * 此方法已经自动被jbolt-admin.js-initJuicer调用
 * @returns
 */

var juicer_bool_html = function (text, val) {
    var html = '<span class="badge badge-${val} d-block my-1">${text}</span>';

    val = juicer('${val|colorClassByLevel}', {val: + val + 1});

    return juicer(html, {text: text, val: val});
};

var juicer_html = function (text, val) {
    var html = '<span class="badge badge-${val} d-block my-1">${text}</span>';

    val = juicer('${val|colorClassByLevel}', {val: val});

    return juicer(html, {text: text, val: val});
};

var bracket = function (val) {
    if (val) {
        return ' (' + val + ')';
    }
    return '';
};

/**
 * 显示文本
 * @param val
 * @param hideText 1. 隐藏文本 0. 否
 */
var qrcode = function (val, hideText) {
    if (!val) {
        return '';
    }

    var text = hideText ? '' : val;

    var html = '<img data-imgviewer src="/common/qrcode?code=${val}" style="width:30px;height: 30px;" tooltip data-title="点击查看大图" />${text}';

    return  juicer(html, {val: val, text: text});
};

function number_format1(val, decimals) {
    return val ? numberFormat(val, decimals) : '';
}

function iseffective(val) {
    if (typeof (val) === 'boolean') {
        val = val ? '1' : '0';
    }

    switch (val) {
        case '0':
            return juicer_bool_html('未生效', val);
        case '1':
            return juicer_bool_html('已生效', val);
        default:
            return "";
    }
}

function btnviewtype(val) {
    switch (val) {
        case 1:
            return juicer_html('列表', val);
        case 2:
            return juicer_html('表单', val);
        default:
            return '';
    }
}

function position(val) {
    switch (val) {
        case 1:
            return juicer_html('工具栏', val);
        case 2:
            return juicer_html('列表内', val);
        default:
            return '';
    }
}

function roletype(val) {
    switch (val) {
        case 1:
            return juicer_html('角色', val);
        case 2:
            return juicer_html('用户', val);
        default:
            return '';
    }
}

function auditstatus(val) {
    switch (val) {
        case 0:
            return juicer_html('待审核', val++);
        case 1:
            return juicer_html('审核通过', val++);
        case 2:
            return juicer_html('审核不通过', val++);
        default:
            return '';
    }
}
function iSource(val) {
    switch (val) {
        case 1:
            return "MES";
        case 2:
            return "U8";
        default:
            return '';
    }
}

function iSex(val) {
    switch (val) {
        case 1:
            return "男";
        case 2:
            return "女";
        default:
            return '';
    }
}

function cOrderStatusDescByAuditStatus(val) {
    switch (val) {
        case 0:
            return "已保存";
        case 1:
            return "待审批";
        case 2:
            return "已审批";
        case 3:
            return "审批不通过";
        default:
            return '';
    }
}

function iOrderStatus(val) {
    switch (val) {
        case 1:
            return "已保存";
        case 2:
            return "待审批";
        case 3:
            return "已审批";
        case 4:
            return "审批不通过";
        case 5:
            return "已发货";
        case 6:
            return "已核对";
        case 7:
            return "已关闭";
        default:
            return '11';
    }
}

function isLargeAmountExpense(val) {
    switch (val) {
        case '0':
            return juicer_bool_html('否', val);
        case '1':
            return juicer_bool_html('是', val);
        default:
            return "";
    }
}

function proposalmSourceType(val) {
    switch (val) {
        case 1:
            return juicer_html('费用预算', val);
        case 2:
            return juicer_html('投资计划', val);
        default:
            return '未知';
    }
}

function isscheduled(val) {
    switch (val) {
        case 0:
            return juicer_bool_html('计划外', val);
        case 1:
            return juicer_bool_html('计划内', val);
        case 2:
            return juicer_bool_html('提前实施', val);
        default:
            return "";
    }
}

function auditstate(val) {
    switch (val) {
        case 0:
            return juicer_html('未审核', ++val);
        case 1:
            return juicer_html('已审核', ++val);
        default:
            return '未知';
    }
}
function careertype(val){
    switch (val){
        case '1':
            return juicer_html('COLD',++val);
        case '2':
            return juicer_html('HOT', ++val);
        case '3':
            return juicer_html('变矩器',++val);
        case '4':
            return juicer_html('锻造', ++val);
        case '5':
            return juicer_html('马达', ++val);
        case '6':
            return juicer_html('其它', ++val);
        default:
            return '未知';
    }
}
function investmentdistinction(val){
    switch (val){
        case '1':
            return juicer_html('能力扩大',++val);
        case '2':
            return juicer_html('品质向上', ++val);
        case '3':
            return juicer_html('改善合理化',++val);
        case '4':
            return juicer_html('更新', ++val);
        case '5':
            return juicer_html('环境改善', ++val);
        case '6':
            return juicer_html('厂房建筑', ++val);
        case '7':
            return juicer_html('其他', ++val);
        default:
            return '未知';
    }
}
function assettype(val){
    switch (val) {
        case '1':
            return juicer_html('土地',++val);
        case '2':
            return juicer_html('房屋及建筑物', ++val);
        case '3':
            return juicer_html('公共设施',++val);
        case '4':
            return juicer_html('生产设备', ++val);
        case '5':
            return juicer_html('检验设备', ++val);
        case '6':
            return juicer_html('模检治具', ++val);
        case '7':
            return juicer_html('运输设备', ++val);
        case '8':
            return juicer_html('办公设备', ++val);
        case '9':
            return juicer_html('事务用品', ++val);
        case '10':
            return juicer_html('软件', ++val);
        case '11':
            return juicer_html('长期待摊', ++val);
        default:
            return '未知';
    }
}
function spriorreport(val){
    switch (val){
        case '0':
            return juicer_html('否', ++val);
        case '1':
            return juicer_html('是',++val);
        default:
            return '未知';
    }
}

function paymentprogress(val){
    switch (val){
        case '1':
            return juicer_html('计划',++val);
        case '2':
            return juicer_html('禀议', ++val);
        case '3':
            return juicer_html('发注', ++val);
        default:
            return '未知';
    }
}
function edittype(val){
    switch (val){
        case '1':
            return juicer_html('新增',++val);
        case '2':
            return juicer_html('超支', ++val);
        case '3':
            return juicer_html('节约',++val);
        case '4':
            return juicer_html('取消', ++val);
        case '5':
            return juicer_html('提前', ++val);
        case '6':
            return juicer_html('上年延后至本年', ++val);
        case '7':
            return juicer_html('本年延后至明年', ++val);
        default:
            return '未知';
    }
}

function simport(val) {
    switch (val) {
        case '0':
            return juicer_bool_html('否', ++val);
        case '1':
            return juicer_bool_html('是', ++val);
        default:
            return "";
    }
}

function proposaldType(val) {
    switch (val) {
        case 1:
            return juicer_html('原禀议', val);
        case 2:
            return juicer_html('新增', val);
        default:
            return '未知';
    }
}

function purchaseState(val) {
    switch (val) {
        case 1:
            return juicer_html('未推送', val);
        case 2:
            return juicer_html('已推送', val);
        default:
            return '未知';
    }
}

function serviceType(val) {
    switch (val) {
        case 1:
            return juicer_html('费用预算', val);
        case 2:
            return juicer_html('投资计划', val);
        default:
            return '未知';
    }
}

function proposalChooseType(val){
    switch (val) {
        case 1:
            return juicer_html('费用预算', val);
        case 2:
            return juicer_html('投资计划', val);
        case 3:
            return juicer_html('追加禀议', val);
        default:
            return '未知';
    }
}

function BudgetType(val) {
    switch (val) {
        case 1:
            return juicer_html('全年预算', val);
        case 2:
            return juicer_html('下期修改', val);
        case 3:
            return juicer_html('实绩预测', val);
        case 4:
            return juicer_html('实绩', val);
        case 5:
            return juicer_html('差异(②-①)', val);
        case 6:
            return juicer_html('差异(③-①)', val);
        case 7:
            return juicer_html('差异(③-②)', val);
        default:
            return "";
    }
}

function AuditStatus(val, way) {
    switch (way) {
        case 1:
            switch (val) {
                case 0:
                case 3:
                    return juicer_html('已保存', ++val);
                case 1:
                    return juicer_html('待审核', ++val);
                case 2:
                    return juicer_html('已审核', ++val);
                default:
                    return "审核-未知";
            }
        case 2:
            switch (val) {
                case 0:
                case 3:
                    return juicer_html('已保存', ++val);
                case 1:
                    return juicer_html('待审批', ++val);
                case 2:
                    return juicer_html('审批通过', ++val);
                default:
                    return "审批-未知";
            }
        default:
            return '未知';
    }
}

function EffectiveStatus(val) {
    switch (val) {
        case 1:
            return juicer_html('未生效', ++val);
        case 2:
            return juicer_html('已生效', ++val);
        case 3:
            return juicer_html('已失效', ++val);
        case 4:
            return juicer_html('已作废', ++val);
        default:
            return "";
    }
}

function iBudgetType1(val) {
    switch (val) {
        case 1:
            return juicer_html('日程', ++val);
        case 2:
            return juicer_html('付款', ++val);
        default:
            return "";
    }
}
function iBudgetType2(val) {
    switch (val) {
        case 1:
            return juicer_html('全年预算a', ++val);
        case 2:
            return juicer_html('下半年修订b', ++val);
        case 3:
            return juicer_html('实绩预测d', ++val);
        case 4:
            return juicer_html('实绩e', ++val);
        case 5:
            return juicer_html('差异A（b-a）', ++val);
        case 6:
            return juicer_html('差异E（d-a）', ++val);
        case 7:
            return juicer_html('差异F（d-b）', ++val);
        case 8:
            return juicer_html('差异G（e-a）', ++val);
        case 9:
            return juicer_html('差异H（e-b）', ++val);
        case 10:
            return juicer_html('差异J（e-d）', ++val);
        default:
            return "";
    }
}

function iBudgetTypeInvestmentGroupSummary(val) {
    switch (val) {
        case 1:
            return juicer_html('全年预算', ++val);
        case 2:
            return juicer_html('下半年修订', ++val);
        case 3:
            return juicer_html('实绩', ++val);
        default:
            return "";
    }
}

var kFormat = function (val) {
	if (val == 0) {
		return '-';
	}
	return val ? numberFormat((val / 1000), 2) : '';
};

function cItemTypeSituation(val) {
    switch (val) {
        case '1':
            return juicer_html('立项', ++val);
        case '2':
            return juicer_html('修订', ++val);
        case '3':
            return juicer_html('实绩', ++val);
        default:
            return "";
    }
}

var finishStatus = function (val) {
    switch (val) {
	    case 1:
	        return juicer_html('未完成', ++val);
	    case 2:
	        return juicer_html('已完成', ++val);
	    default:
	        return "";
    }
};

var isubitem = function(val){
    switch (val) {
	    case 0:
	        return "否";
	    case 1:
	        return "是";
	    default:
	        return "";
    }
}

function iPurchaseRefType(val) {
    switch (val) {
        case 1:
            return juicer_bool_html('禀议书', val);
        case 2:
            return juicer_bool_html('预算', val);
        default:
            return "";
    }
}

var previewUrl = function (url) {
    url = getUrl(url.replace('/home', ''));

    return getBaseUrl('8012') + '/onlinePreview?url=' + encodeURIComponent(window.btoa(url));
};

var getUrl = function (uri) {
    return location.protocol + '//' + location.host + '/' + uri;
};

var getBaseUrl = function (port) {
    if (location.port) {
        return location.protocol + '//' + location.host.replace(location.port, port);
    }
    return location.protocol + "//kkfileview.kephon.com";
};

var icontrastnum = function(val){
    switch (val) {
	    case 1:
	        return "①";
	    case 2:
	        return "②";
	    case 3:
	        return "③";
	    default:
	        return "";
    }
}
function iPeriodContrastBudgetType(val) {
    switch (val) {
        case 1:
            return juicer_html('全年预算a', ++val);
        case 2:
            return juicer_html('下半年修订b', ++val);
        case 3:
            return juicer_html('实绩预测d', ++val);
        case 4:
            return juicer_html('实绩e', ++val);
        case 5:
            return juicer_html('差异②-①）', ++val);
        case 6:
            return juicer_html('差异③-①', ++val);
        case 7:
            return juicer_html('差异③-②', ++val);
        default:
            return "";
    }
}

function moDocType(val) {
    switch (val) {
        case 0:
            return "已保存";
        case 1:
            return "未安排人员";
        case 2:
            return "已安排人员";
        case 3:
            return "生成备料单";
        case 4:
            return "待生产";
        case 5:
            return "生产中";
        case 6:
            return "已完工";
        case 7:
            return "已关闭";
        case 8:
            return "已取消";
        default:
            return "";
    }
}

function moDociType(val) {
    switch (val) {
        case 1:
            return "APS";
        case 2:
            return "手工新增";
        default:
            return "";
    }
}

/*
 * 数字格式化 清除掉小数点后的无用的0 如果最后是0 转为自定义字符显示
 * 参数说明：
 * number：要格式化的数字
 * decimals：保留几位小数
 * dec_point：小数点符号
 * thousands_sep：千分位符号
 * roundtag:舍入参数，默认 "ceil" 向上取,"floor"向下取,"round" 四舍五入
 * defaultChar: 为0时显示的字符
 * */
function numberFormatZeroDefaultChar(number, decimals, dec_point, thousands_sep,roundtag,defaultChar) {
	var value= numberFormat2(number, decimals, dec_point, thousands_sep,roundtag);
	return value.toString()==="0"?defaultChar:value;
}

function initMineJuicer(){
	//格式 juicer.register("模板函数名定义（自己取名）",具体模板函数实现);
	//举例 juicer.register("date_ymdhm",date_ymdhm);
	//测试效果用例
	/*var jb_sum=function(a,b){
		return a+b;
	}
	juicer.register("jb_sum",jb_sum);
	alert(juicer("${a,b|jb_sum}",{a:1,b:1}));*/

	//下面写你自己的吧........
    juicer.register('bracket', bracket);
    juicer.register('qrcode', qrcode);
    juicer.register('number_format1', number_format1);
    juicer.register('iseffective', iseffective);
    juicer.register('btnviewtype', btnviewtype);
    juicer.register('position', position);
    juicer.register('roletype', roletype);
    juicer.register('auditstatus', auditstatus);
    juicer.register('iSource', iSource);
    juicer.register('iSex', iSex);
    juicer.register('cOrderStatusDescByAuditStatus', cOrderStatusDescByAuditStatus);
    juicer.register('iOrderStatus', iOrderStatus);
    juicer.register('isLargeAmountExpense', isLargeAmountExpense);
    juicer.register('proposalmSourceType', proposalmSourceType);
    juicer.register('isscheduled', isscheduled);
    juicer.register('auditstate', auditstate);
    juicer.register('careertype', careertype);
    juicer.register('investmentdistinction', investmentdistinction);
    juicer.register('assettype', assettype);
    juicer.register('spriorreport', spriorreport);
    juicer.register('paymentprogress', paymentprogress);
    juicer.register('edittype', edittype);
    juicer.register('simport', simport);
    juicer.register('proposaldType', proposaldType);
    juicer.register('purchaseState', purchaseState);
    juicer.register('serviceType', serviceType);
    juicer.register('proposalChooseType', proposalChooseType);
    juicer.register('BudgetType', BudgetType);
    juicer.register('AuditStatus', AuditStatus);
    juicer.register('EffectiveStatus', EffectiveStatus);
    juicer.register('iBudgetType1', iBudgetType1);
    juicer.register('iBudgetType2', iBudgetType2);
    juicer.register('iBudgetTypeInvestmentGroupSummary', iBudgetTypeInvestmentGroupSummary);
    juicer.register('kFormat', kFormat);
    juicer.register('cItemTypeSituation', cItemTypeSituation);
    juicer.register('finishStatus', finishStatus);
    juicer.register('isubitem', isubitem);
    juicer.register('iPurchaseRefType', iPurchaseRefType);
    juicer.register('numberFormatZeroDefaultChar', numberFormatZeroDefaultChar);
    juicer.register('previewUrl', previewUrl);
    juicer.register('icontrastnum', icontrastnum);
    juicer.register('iPeriodContrastBudgetType', iPeriodContrastBudgetType);
    juicer.register('moDocType',moDocType);
    juicer.register('moDociType',moDociType)
}

function jboltTableGetSpecCols(ele, colName) {
    var datas = jboltTableGetAllDatas(ele, [colName]);
    if (datas && datas.length > 0) {
        var values = [];
        $.each(datas, function (val, row) {
            values.push(row[colName]);
        });
        return values.join(',');
    }
    return null;
}

function getTableCheckedCount(table, checkboxName) {
    return table.tbody.find("tr>td>.jbolt_table_checkbox>input[type='checkbox'][name='" + checkboxName + "']:checked").length
}

function getTableRowCount(table) {
    return table.tbody.find("tr").length;
}

var getDictMap = function (key, callback) {
    Ajax.post('/admin/dictionary/map', {key: key}, function (res) {
        if (res.state === 'ok') {
            callback(res.data);
        }
    });
};

/**
 * 自行扩展前端表单校验规则
 * 在jbolt-admin.js的ruleMap基础上加载这里的扩展
 * 此方法已经自动被jbolt-admin.js-ForkChecker.init()调用
 * @returns
 */
function initMineRuleMap(){
var mineFormCheckRuleMap=[
	//格式 {type:"具体规则名称",method:function(value){//具体校验实现 可以自行书写判断 也可用正则 return (!isNaN(value));}}
	//举例 {type:"int",method:function(value){return TestRgexp(/^-?[0-9]\d*$/, value);}},//整数校验
	//下面写你的吧


	];
	//将自己定义的Rule加入到JBolt自身Rule管理器中
	addRuleToJBoltRuleMap(mineFormCheckRuleMap);
}

/**
 * 添加和维护自己二开使用的其它官方未集成的js库 js插件 组件
 * @param plugins
 * @returns
 */
function initMinePlugins(){
	//在这里可以添加自己使用到的第三方组件的js 组件和插件
	var myPlugins={
			//"myplugin":{"js":['assets/plugins/myplugin/myplugin.js'],"css":['assets/plugins/myplugin/myplugin.css']}
	};

	//将自己定义的插件加入到JBolt自身插件管理器中
	addPluginsToJBoltPluginMgr(myPlugins);
}

//调用juicer模板初始化
initMineJuicer();
//调用注册二开使用的其它使用到的第三方插件
initMinePlugins();
//调用RuleMap增加自己的规则
initMineRuleMap();

function showSwitchOrgDialog(showMsg, handler) {
    if (window.self !== window.top) {
        parent.showSwitchOrgDialog(showMsg, handler);
    } else {
        layer.closeAll();

        DialogUtil.openNewDialog({
            title: "切换登录组织",
            width: "500",
            height: "400",
            offset: "auto",
            zIndex: 20220610,
            url: "admin/switchOrg",
            handler: handler ? handler : refreshPjaxContainer
        });
    }
}

/**
 * 自定义Ajax提交表单处理
 *
 * @param url               接口地址
 * @param para              参数
 * @param successCallback   成功回调
 * @param failCallback      失败回调
 */
function postWithCallback(url, para, successCallback, failCallback) {
    var index = parent.LayerMsgBox.loading('正在处理...', 10);

    Ajax.post(url, para, function (ret) {
        parent.LayerMsgBox.close(index);

        if (ret.state === 'ok') {
            successCallback(ret.data);
        } else {
            if (failCallback) {
                failCallback();
            } else {
                LayerMsgBox.alert(ret.msg, 2);
            }
        }
    }, function () {
        parent.LayerMsgBox.close(index);
    });
}

function zoomPage() {
    var wid = $(window).width(), len;
    if (wid >= 1024 && wid < 1400) {
        len = wid / 1400;
        var $html = $("html");
        $html.css("zoom", len);
        $html.css({"-moz-transform": "scale(" + len + ")"}, {"-moz-transform-origin": "0 0"});
    }
}

function closeHandler() {
    parent.layer.close(parent.layer.getFrameIndex(window.name));
    parent.refreshPjaxContainer();
}

;(function ($) {

    $.fn.tablesMergeCell = function(options, mergecel) {
        var defaultsettings= {
            automatic:true,
            cols: null,        // 用数组表示列的索引,从0开始,然后根据指定列来处理(合并)相同内容单元格
            rows: null
        };
        var opts = $.extend(defaultsettings, options);
        return this.each(function() {
            var cols = opts.cols,
                rows = opts.rows;
            if(rows==null){
                for ( var i = cols.length - 1; cols[i] != undefined; i--) {
                    tablesMergeCell($(this), mergecel, cols[i]);
                }
            }else{
                for ( var i = cols.length - 1,k=opts.rows.length-1; cols[i] != undefined; i--,k--) {
                    tablesMergeCell($(this), mergecel, cols[i], k);
                }
            }

            dispose($(this));
        });

        // 如果对javascript的closure和scope概念比较清楚, 这是个插件内部使用的private方法
        function tablesMergeCell($table, mergecel, colIndex,rowIndex) {
            $table.data('col-content', '');     // 存放单元格内容
            $table.data('col-rowspan', 1);      // 存放计算的rowspan值 默认为1
            $table.data('col-td', $());         // 存放发现的第一个与前一行比较结果不同td(jQuery封装过的), 默认一个"空"的jquery对象
            $table.data('trNum', $('tbody tr', $table).length);     // 要处理表格的总行数, 用于最后一行做特殊处理时进行判断之用
            // 我们对每一行数据进行"扫面"处理 关键是定位col-td, 和其对应的rowspan
            $('tbody tr', $table).each(function(index) {
                var $tr = $(this);
                // td:eq中的colIndex即列索引
                var $td = $('td:eq(' + colIndex + ')', $tr);

                var currentContent;
                if(mergecel){
                    currentContent = $td.attr("data-mergecel");
                }else{
                    currentContent = $td.html();
                }
                if(opts.automatic){     // 内容
                    // 第一次时走此分支
                    if ($table.data('col-content') == '') {
                        $table.data('col-content', currentContent);
                        $table.data('col-td', $td);
                    } else {
                        // 上一行与当前行内容相同
                        if ($table.data('col-content') == currentContent) {
                            addRowspan();   // 上一行与当前行内容相同则col-rowspan累加, 保存新值
                        }else{
                            newRowspan();   // 上一行与当前行内容不同
                        }
                    }
                }else{      // 指定
                    if(opts.rows.length>0){
                        if(opts.rows[0].length==undefined){
                            for(var i=0; i<opts.rows.length; i++){
                                customRowspan(opts.rows[i],opts.rows.length);
                            }
                        }else{
                            for(var i=0; i<opts.rows[rowIndex].length; i++){
                                customRowspan(opts.rows[rowIndex][i],opts.rows[rowIndex].length);
                            }
                        }
                    }
                }
                function customRowspan(val,len){
                    if(index==val){
                        if ($table.data('col-content') == '') {
                            if(currentContent==''){
                                currentContent = true;
                            }
                            $table.data('col-content', currentContent);
                            $td.attr('rowspan', len);
                        }else{
                            $td.hide();
                        }
                    }
                }
                function addRowspan(){
                    var rowspan = $table.data('col-rowspan') + 1;
                    $table.data('col-rowspan', rowspan);
                    // 值得注意的是 如果用了$td.remove()就会对其他列的处理造成影响
                    $td.hide();
                    // 最后一行的情况比较特殊一点
                    // 比如最后2行 td中的内容是一样的, 那么到最后一行就应该把此时的col-td里保存的td设置rowspan
                    if (++index == $table.data('trNum')) {
                        $table.data('col-td').attr('rowspan', $table.data('col-rowspan'));
                    }
                }
                function newRowspan(){
                    // col-rowspan默认为1, 如果统计出的col-rowspan没有变化, 不处理
                    if ($table.data('col-rowspan') != 1) {
                        $table.data('col-td').attr('rowspan', $table.data('col-rowspan'));
                    }
                    // 保存第一次出现不同内容的td, 和其内容, 重置col-rowspan
                    $table.data('col-td', $td);
                    if(mergecel){
                        $table.data('col-content', $td.attr("data-mergecel"));
                    }else{
                        $table.data('col-content', $td.html());
                    }
                    $table.data('col-rowspan', 1);
                }
            });
        }
        // 同样是个private函数 清理内存之用
        function dispose($table) {
            $table.removeData();
        }
    };

})(jQuery);

$(function(){
//初始化调用加载
    zoomPage();
});
