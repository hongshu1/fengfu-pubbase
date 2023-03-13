/**
 * js常用工具类
 */

/**
 * 方法作用：【格式化时间】
 * 使用方法
 * 示例：
 *      使用方式一：
 *      var now = new Date();
 *      var nowStr = now.dateFormat("yyyy-MM-dd hh:mm:ss");
 *      使用方式二：
 *      new Date().dateFormat("yyyy年MM月dd日");
 *      new Date().dateFormat("MM/dd/yyyy");
 *      new Date().dateFormat("yyyyMMdd");
 *      new Date().dateFormat("yyyy-MM-dd hh:mm:ss");
 * @param format {date} 传入要格式化的日期类型
 * @returns {2015-01-31 16:30:00}
 */
Date.prototype.dateFormat = function (format){
    var o = {
        "M+" : this.getMonth()+1, //month
        "d+" : this.getDate(), //day
        "h+" : this.getHours(), //hour
        "m+" : this.getMinutes(), //minute
        "s+" : this.getSeconds(), //second
        "q+" : Math.floor((this.getMonth()+3)/3), //quarter
        "S" : this.getMilliseconds() //millisecond
    }
    if(/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    }
    for(var k in o) {
        if(new RegExp("("+ k +")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
        }
    }
    return format;
}
/***********************************************************************
 *                           日期时间工具类                            *
 *                     注：调用方式，DateUtils.方法名                   *
 * ********************************************************************/
var parsePatterns = [
    "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH", "yyyy-MM",
    "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM/dd HH", "yyyy/MM",
    "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM.dd HH", "yyyy.MM",
    "yyyy年MM月dd日", "yyyy年MM月dd日 HH时mm分ss秒", "yyyy年MM月dd日 HH时mm分", "yyyy年MM月dd日 HH时", "yyyy年MM月",
    "yyyy"
];
var DateUtils = {
    /*
     * 方法作用：【取传入日期是星期几】
     * 使用方法：dateUtil.nowFewWeeks(new Date());
     * @param date{date} 传入日期类型
     * @returns {星期四，...}
     */
    nowFewWeeks:function(date){
        if(date instanceof Date){
            var dayNames = new Array("星期天","星期一","星期二","星期三","星期四","星期五","星期六");
            return dayNames[date.getDay()];
        } else{
            return "Param error,date type!";
        }
    },
    /*
     * 方法作用：【字符串转换成日期】
     * 使用方法：dateUtil.strTurnDate("2010-01-01");
     * @param str {String}字符串格式的日期，传入格式：yyyy-mm-dd(2015-01-31)
     * @return {Date}由字符串转换成的日期
     */
    strTurnDate:function(str){
        var   re   =   /^(\d{4})\S(\d{1,2})\S(\d{1,2})$/;
        var   dt;
        if   (re.test(str)){
            dt   =   new   Date(RegExp.$1,RegExp.$2   -   1,RegExp.$3);
        }
        return dt;
    },
    //字符串转日期格式，strDate要转为日期格式的字符串
    getDate:function(str){
        if (StringUtils.isBlack(str)){
            return null;
        }
        try {
            return new Date(str);
        } catch (e) {
            return null;
        }
        return new Date();
    },
    /*
     * 方法作用：【计算2个日期之间的天数】
     * 传入格式：yyyy-mm-dd(2015-01-31)
     * 使用方法：dateUtil.dayMinus(startDate,endDate);
     * @startDate {Date}起始日期
     * @endDate {Date}结束日期
     * @return endDate - startDate的天数差
     */
    dayMinus:function(startDate, endDate){
        if(startDate instanceof Date && endDate instanceof Date){
            var days = Math.floor((endDate-startDate)/(1000 * 60 * 60 * 24));
            return days;
        }else{
            return "Param error,date type!";
        }
    },
    /**
     * 得到日期字符串 ，转换格式（yyyy-MM-dd）
     */
    formatDate: function(date, pattern) {
        if(date == null){
            date = new Date();
        }
        if(date != null){
            let myyear = date.getFullYear();
            let mymonth = date.getMonth() + 1;
            let myweekday = date.getDate();
            if (mymonth < 10) {
                mymonth = "0" + mymonth;
            }
            if (myweekday < 10) {
                myweekday = "0" + myweekday;
            }
            return (myyear + "-" + mymonth + "-" + myweekday);
        }
        return null;
    },
    /**
     * 得到日期字符串 ，转换格式（yyyy-MM）
     */
    formatDateYYYYMM: function(date, pattern) {
        if(date == null){
            date = new Date();
        }
        if(date != null){
            let myyear = date.getFullYear();
            let mymonth = date.getMonth() + 1;
            let myweekday = date.getDate();
            if (mymonth < 10) {
                mymonth = "0" + mymonth;
            }
            if (myweekday < 10) {
                myweekday = "0" + myweekday;
            }
            return (myyear + "-" + mymonth);
        }
        return null;
    },
    /**
     * 日期，在原有日期基础上，增加days天数，默认增加1天
     */
    addDate: function(date, days) {
        if (StringUtils.isBlack(days)) {
            days = 1;
        }
        var newDate = new Date(date.valueOf());
        newDate.setDate(date.getDate() + days);
        return newDate;
    },
    /**
     * 日期，在原有日期基础上，增加 month月份，默认增加1月
     */
    addMonth: function(date, month){
        var num = month;
        var d = new Date(date.valueOf());

        var thisMonth = d.getMonth()+1;
        var thisYear = d.getFullYear();
        var thisDay = d.getDate();
        var thisHour = d.getHours();
        var thsiMinu = d.getMinutes();
        var thisSecon = d.getSeconds();

        var addCount = thisMonth + num;
        var diffMonthCount = parseInt(addCount/12);    //取整

        if((thisMonth + num) == 12*diffMonthCount){    //如果是本年
            if((thisMonth + num) == 12){
                diffMonthCount = 0;
            }else{
                diffMonthCount = diffMonthCount-1;
            }
        }
        if(thisMonth + num > 12){    //如果是大于一年
            thisYear += diffMonthCount;
        }
        thisMonth = (addCount)-12*diffMonthCount;
        if(thisMonth < 10){
            thisMonth = "0"+thisMonth;
        }
        if(thisHour < 10){
            thisHour = "0"+thisHour;
        }
        if(thsiMinu < 10){
            thsiMinu = "0"+thsiMinu;
        }
        if(thisSecon < 10){
            thisSecon = "0"+thisSecon;
        }

        var thatDate = new Date(thisYear,thisMonth,0); //当天数为0 js自动处理为上一月的最后一天
        var thatDay = thatDate.getDate();//指定年月的当月最大天数

        var diffDay = thatDay - thisDay;
        if((thatDay - thisDay) > 0 || (thatDay - thisDay) < 0){
            thisDay = thatDay;
        }
        var dateStr = "";
        if(thsiMinu == 0 && thisSecon == 0){
            dateStr = thisYear + "-" + thisMonth + "-" + thisDay;
        }else{
            dateStr = thisYear + "-" + thisMonth + "-" + thisDay +" " +thisHour+":"+ thsiMinu+":"+thisSecon;
        }
        return new Date(dateStr);
    },
    /**
     * 获取一个月的天数
     * @param curDate
     */
    getCountDays: function(curDate) {
        let newDate = new Date(curDate.valueOf());
        var curMonth = newDate.getMonth();
        newDate.setMonth(curMonth + 1);
        newDate.setDate(0);
        return newDate.getDate();
    }
};

/***********************************************************************
 *                           加载工具类                                *
 *                     注：调用方式，LoadUtils.方法名                   *
 * ********************************************************************/
var LoadUtils = {
    /*
     * 方法说明：【动态加载js文件css文件】
     * 使用方法：loadUtil.loadjscssfile("http://libs.baidu.com/jquery/1.9.1/jquery.js","js")
     * @param fileurl 文件路径，
     * @param filetype 文件类型，支持传入类型，js、css
     */
    loadjscssfile:function(fileurl,filetype){
        if(filetype == "js"){
            var fileref = document.createElement('script');
            fileref.setAttribute("type","text/javascript");
            fileref.setAttribute("src",fileurl);
        }else if(filetype == "css"){

            var fileref = document.createElement('link');
            fileref.setAttribute("rel","stylesheet");
            fileref.setAttribute("type","text/css");
            fileref.setAttribute("href",fileurl);
        }
        if(typeof fileref != "undefined"){
            document.getElementsByTagName("head")[0].appendChild(fileref);
        }else{
            alert("loadjscssfile method error!");
        }
    }
};

/***********************************************************************
 *                           字符串操作工具类                          *
 *                     注：调用方式，StringUtils.方法名                   *
 * ********************************************************************/
var StringUtils = {
    /*
     * 判断字符串是否为空
     * @param str 传入的字符串
     * @returns {}
     */
    isEmpty: function (str) {
        if (str != undefined && str != null && str.length > 0) {
            return true;
        } else {
            return false;
        }
    },
    /*
     * 判断字符串是否为空
     * @param str 传入的字符串
     * @returns {}
     */
    isBlack: function (str) {
        if (str == undefined || str == null || str.length <= 0 || (str+'').toLowerCase() == 'null'
            || (str+'').toLowerCase() == 'undefined') {
            return true;
        } else {
            return false;
        }
    },
    /*
     * 判断两个字符串子否相同
     * @param str1
     * @param str2
     * @returns {Boolean}
     */
    isEquals: function (str1, str2) {
        if (str1 == str2) {
            return true;
        } else {
            return false;
        }
    },
    /*
     * 忽略大小写判断字符串是否相同
     * @param str1
     * @param str2
     * @returns {Boolean}
     */
    isEqualsIgnorecase: function (str1, str2) {
        if (str1.toUpperCase() == str2.toUpperCase()) {
            return true;
        } else {
            return false;
        }
    },
    /**
     * 判断是否是数字
     * @param value
     * @returns {Boolean}
     */
    isNum: function (value) {

        if (value != null && value.length > 0 && isNaN(value) == false) {
            return true;
        } else {
            return false;
        }
    },
    /**
     * 判断是否是中文
     * @param str
     * @returns {Boolean}
     */
    isChine: function (str) {
        var reg = /^([u4E00-u9FA5]|[uFE30-uFFA0])*$/;
        if (reg.test(str)) {
            return false;
        }
        return true;
    },
    /**
     * 数字位数不足时前面补0，返回数字字符串
     * @param num 需要补零的数值
     * @param len 补零后的总位数
     * @return String
     */
    formatZero: function (num, len) {
        if(String(num).length > len) {
            return num;
        }
        return (Array(len).join(0) + num).slice(-len);
    },
    excelHeadStringToNum: function (a){
        var str=a.toLowerCase().split("");
        var num=0;
        var al = str.length;
        var getCharNumber = function(charx){
            return charx.charCodeAt() -96;
        };
        var numout = 0;
        var charnum = 0;
        for(var i = 0; i < al; i++){
            charnum = getCharNumber(str[i]);
            numout += charnum * Math.pow(26, al-i-1);
        };
        return numout;
    },
    excelHeadNumToString:function (numm){
        var stringArray = [];
        stringArray.length = 0;
        var numToStringAction = function(nnum){
            var num = nnum - 1;
            var a = parseInt(num / 26);
            var b = num % 26;
            stringArray.push(String.fromCharCode(64 + parseInt(b+1)));
            if(a>0){
                numToStringAction(a);
            }
        }
        numToStringAction(numm);
        return stringArray.reverse().join("");
    }
};


/***********************************************************************
 *                           对象操作工具类                          *
 *                     注：调用方式，ObjectUtils.方法名                   *
 * ********************************************************************/
var ObjectUtils = {
    /**
     * 深克隆
     * @param {Object} obj
     */
    deepClone: function (obj){
        return JSON.parse(JSON.stringify(obj))
    },
    /**
     * 深克隆
     * @param {Object} obj
     */
    cloneObj: function(obj) {
        if (typeof obj !== 'object') {
            return obj;
        } else {
            var newobj = obj.constructor === Array ? [] : {};
            for (var i in obj) {
                newobj[i] = typeof obj[i] === 'object' ? ObjectUtils.cloneObj(obj[i]) : obj[i];
            }
            return newobj;
        }
    }
}



//Date扩展
Date.prototype.Format = function (fmt) { //author: meizz
    if (StringUtils.isBlack(fmt)) {
        fmt = "yyyy-MM-dd";
    }
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}