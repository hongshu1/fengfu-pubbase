var spread = new GC.Spread.Sheets.Workbook(document.getElementById("ssWeek"), {
    tabStripVisible: false
});
GC.Spread.Common.CultureManager.culture("zh-cn");
var excelIo = new GC.Spread.Excel.IO();
var sheet = spread.getActiveSheet();
// 状态栏
var statusBar = new GC.Spread.Sheets.StatusBar.StatusBar(document.getElementById('statusBar'));
statusBar.bind(spread);

parmsDefaultWeek = {
    titles: ['序号', '排产层级', '产线名称', '存货编码', '客户部番', '部品名称'],
    columnWidth: [60, 120, 120, 120, 120, 120],
    days: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
    rowIndex: 0, // 表格初始位置行索引
    columnIndex: 0, // 表格初始位置列索引
    dataStartCol: 8, // 日期开始列索引前的列数
    totalArr: [] // 合计列的定位，有多个
};

inits2()

// init 表单
function inits2() {
    spreadSuspend();
    spread.options.allowUserDragFill = true; // 允许用户拖动填充区域
    spread.options.referenceStyle = GC.Spread.Sheets.ReferenceStyle.r1c1; // 此表单中单元格公式中的单元格和区域引用的样式
    sheet.options.colHeaderAutoText = GC.Spread.Sheets.HeaderAutoText["numbers"]; //列号换成数字
    sheet.options.isProtected = true; //设置表单保护以控制表单上的单元格是否可编辑

    sheet.options.allowCellOverflow = false; // 数据是否可以溢出到相邻的空单元格中
    sheet.options.protectionOptions = {
        allowResizeColumns: true, //用户是否可以改变列宽
        allowResizeRows: true, //用户是否可以改变行高
        allowSelectUnlockedCells: true, // 用户是否可以选中未被锁定的单元格
    };
    sheet.setRowCount(0);
    sheet.setColumnCount(0);

    clearRange(0, 0, sheet.getRowCount(GC.Spread.Sheets.SheetArea.viewport), sheet.getColumnCount(GC.Spread.Sheets.SheetArea.viewport))


    // 初始化天数
    let layDate = $("#layDate").val();
    if (layDate) {
        var _date = DateUtils.getDate(layDate);
        let dayMinus = getDaysBetweenDates(new Date, _date);
        let monthListWithDays = getMonthListWithDays(new Date, _date);//每个月的天数信息
        var count = Object.keys(monthListWithDays).length;// 共有多少个月
        sheet.setColumnCount(8 + dayMinus + count);

        loadUniversalHead();
        loadDay(_date, monthListWithDays);
        loadData(monthListWithDays);
        setTableBorder();

    }

    //添加事件
    addEvent();
    spreadResume();
}

// 加载通用头
function loadUniversalHead() {
    var _sheet = sheet;
    var _parmsDefaultWeek = parmsDefaultWeek;
    var _rowindex = _parmsDefaultWeek.rowIndex; // 初始行
    var _columnIndex = _parmsDefaultWeek.columnIndex; // 初始列
    var _dataStartCol = _parmsDefaultWeek.dataStartCol; // 日期开始列索引前的列数
    var _titles = _parmsDefaultWeek.titles;
    var _columnWidth = _parmsDefaultWeek.columnWidth;

    _sheet.addRows(0, 3); // 第1个参数为起始行的索引，第2个参数为要添加的行数

    // 通用头部
    for (let i = 0; i < _titles.length; i++) {
        _sheet.addSpan(_rowindex, _columnIndex + i, 3, 1, GC.Spread.Sheets.SheetArea.viewport);
        _sheet.getCell(_rowindex, _columnIndex + i).text(_titles[i])
        _sheet.setColumnWidth(_columnIndex + i, _columnWidth[i]);
    }

    sheet.getRange(0, -1, 3, -1).backColor("#f2f2f2");
    sheet.getRange(0, -1, 3, -1).hAlign(GC.Spread.Sheets.HorizontalAlign.center);
    sheet.getRange(0, -1, 3, -1).vAlign(GC.Spread.Sheets.VerticalAlign.center);
    sheet.getRange(0, -1, 3, -1, GC.Spread.Sheets.SheetArea.viewport).locked(true);
}

// 初始化天数
function loadDay(_date, monthListWithDays) {
    var _sheet = sheet;
    var _parms = parmsDefaultWeek;
    var _rowindex = _parms.rowIndex; // 初始行
    var _columnIndex = _parms.columnIndex; // 初始列
    var _dataStartCol = _parms.dataStartCol; // 日期开始列索引前的列数
    let _day = new Date().getDate();

    let column = 0;
    let index = 0;
    for (let key in monthListWithDays) {
        let monthListWithDay = monthListWithDays[key]; // 每月天数

        // 设置年
        if (index === 0) {
            _sheet.addSpan(_rowindex, _columnIndex + _dataStartCol, 1, monthListWithDay + 1, GC.Spread.Sheets.SheetArea.viewport)
            _sheet.getCell(_rowindex, _columnIndex + _dataStartCol).text(key + "月")
            _sheet.addSpan(_rowindex + 1, _columnIndex + _dataStartCol + monthListWithDay, 2, 1, GC.Spread.Sheets.SheetArea.viewport)
            _sheet.getCell(_rowindex + 1, _columnIndex + _dataStartCol + monthListWithDay).text("合计")
            _parms.totalArr.push(_columnIndex + _dataStartCol + monthListWithDay);
        } else {
            _sheet.addSpan(_rowindex, _columnIndex + _dataStartCol + column, 1, monthListWithDay + 1, GC.Spread.Sheets.SheetArea.viewport)
            _sheet.getCell(_rowindex, _columnIndex + _dataStartCol + column).text(key + "月")

            _sheet.addSpan(_rowindex + 1, _columnIndex + _dataStartCol + column + monthListWithDay, 2, 1, GC.Spread.Sheets.SheetArea.viewport)
            _sheet.getCell(_rowindex + 1, _columnIndex + _dataStartCol + column + monthListWithDay).text("合计")
            _parms.totalArr.push(_columnIndex + _dataStartCol + column + monthListWithDay);
        }

        // 设置月
        for (let day = 0; day <= monthListWithDay; day++) {
            if (monthListWithDay === day) break;
            if (index === 0) {
                _sheet.getCell(_rowindex + 1, _columnIndex + _dataStartCol + day).text((_day + day) + "日")
                let week = _parms.days[DateUtils.getDate(key + "-" + (_day + day)).getDay()];
                _sheet.getCell(_rowindex + 2, _columnIndex + _dataStartCol + day).text(week)
                if (week === 'Sat' || week === 'Sun') {
                    _sheet.getCell(_rowindex + 2, _columnIndex + _dataStartCol + day).backColor("#00ffff");
                }
            } else {
                let week = _parms.days[DateUtils.getDate(key + "-" + (day + 1)).getDay()]
                _sheet.getCell(_rowindex + 1, _columnIndex + _dataStartCol + column + day).text((day + 1) + "日")
                _sheet.getCell(_rowindex + 2, _columnIndex + _dataStartCol + column + day).text(week)
                if (week === 'Sat' || week === 'Sun') {
                    _sheet.getCell(_rowindex + 2, _columnIndex + _dataStartCol + column + day).backColor("#00ffff");
                }
            }
        }


        column = column + monthListWithDay + 1;
        index++;
    }


}

//设置表格边框
function setTableBorder() {
    const range = sheet.getRange(0, 0, sheet.getRowCount(), sheet.getColumnCount()); // 获取要设置边框的单元格范围
    range.setBorder(new GC.Spread.Sheets.LineBorder('black', GC.Spread.Sheets.LineStyle.thin), {
        all: GC.Spread.Sheets.LineStyle.thin
    });
}

/**
 * 添加事件
 * @param parms
 */
function addEvent(parms) {
    //值变化事件
    sheet.bind(GC.Spread.Sheets.Events.ValueChanged, function (e, info) {
        let row = info.row, col = info.col;
    });

    //活动工作表切换
    spread.bind(GC.Spread.Sheets.Events.ActiveSheetChanged, function (sender, args) {
        let sheet = spread.getActiveSheet();
        let sheetName = sheet.name();
        let sheetNo = parseInt(sheetName.substring(5));
        while (parmsArr.length < sheetNo) {
            //新增页签
            let parmsDefaultNew = ObjectUtils.deepClone(parmsDefaultWeek);
            parmsDefaultNew['spread'] = spread;
            parmsDefaultNew['nowDate'] = DateUtils.getDate("#(layDate??)");//new Date('${@DateUtils.getDate()}');
            parmsArr.push(parmsDefaultNew);
        }
        if (parmsArr[sheetNo - 1]['activeSheet'] == undefined || parmsArr[sheetNo - 1]['activeSheet'] == null) {
            //新增页签设置
            spread.getSheetFromName(sheetName).options.colHeaderAutoText = GC.Spread.Sheets.HeaderAutoText["numbers"];//列号换成数字
            spread.getSheetFromName(sheetName).setRowCount(500);
            spread.getSheetFromName(sheetName).setColumnCount(115);
            spread.getSheetFromName(sheetName).options.isProtected = true;//表单设为保护状态
            spread.getSheetFromName(sheetName).options.protectionOptions = {allowFilter: true};//保护状态允许筛选
            spread.getSheetFromName(sheetName).getDefaultStyle().locked = true;//默认样式设为非锁定
            spread.getSheetFromName(sheetName).options.protectionOptions.allowResizeColumns = true;//保护状态允许调整行列
            spread.getSheetFromName(sheetName).options.protectionOptions.allowResizeRows = true;//保护状态允许调整行列
            parmsArr[sheetNo - 1]['activeSheet'] = spread.getSheetFromName(sheetName);
            parmsArr[sheetNo - 1]['nowDate'] = DateUtils.getDate("#(layDate??)");//new Date('${@DateUtils.getDate()}');
        }
    });

    //双击事件
    sheet.bind(GC.Spread.Sheets.Events.CellDoubleClick, function (sender, args) {
        var sheet = args.sheet, row = args.row, col = args.col;
        let rc = 'R' + (row + 1) + 'C' + (col + 1);
        if (args.sheetArea === GC.Spread.Sheets.SheetArea.colHeader) {
            //console.log("列标题双击.");
        }
        if (args.sheetArea === GC.Spread.Sheets.SheetArea.rowHeader) {
            //console.log("行标题双击.");
        }
        if (args.sheetArea === GC.Spread.Sheets.SheetArea.corner) {
            //console.log("角头双击.");
        }
        //双击单元格
        if (args.sheetArea === GC.Spread.Sheets.SheetArea.viewport) {
            if (parms.lockedRC.indexOf(rc) > -1) {
                //console.log("双击单元格["+rc+"]"+args.sheetArea);
                // js.showMessage('禁止修改', '警告', 'warning', 3000);
                //js.alert('禁止修改！', {icon: 2});
                LayerMsgBox.error("禁止修改！", 2000);
            }
        }
    });

    // 数据导出事件
    document.getElementById('exportExcel').onclick = function () {
        var fileName = "年度生产计划排产.xlsx"
        if (fileName.substr(-5, 5) !== '.xlsx') {
            fileName += '.xlsx';
        }
        var json = spread.toJSON();
        excelIo.save(json, function (blob) {
            saveAs(blob, fileName);
        }, function (e) {
            console.log(e);
        });
    };

    document.getElementById('loadExcel').onclick = function () {
        // 创建一个隐藏的 input 元素
        var input = document.createElement('input');
        input.type = 'file';
        input.accept = '.xlsx';
        input.style.display = 'none';
        input.onchange = function () {
            var files = input.files;
            if (files.length > 0) {
                var file = files[0];

                excelIo.open(file, function (json) {
                        var workbookObj = json;
                        spread.fromJSON(workbookObj);
                    }, function (e) {
                        // process error
                        alert(e.errorMessage);
                    }, {
                        "openMode": 0,
                        "includeStyles": false,
                        "includeFormulas": true,
                        "frozenColumnsAsRowHeaders": false,
                        "frozenRowsAsColumnHeaders": false,
                        "fullRecalc": false,
                        "dynamicReferences": true,
                        "calcOnDemand": false,
                        "includeUnusedStyles": false
                    }
                );
            }
        }

        document.body.appendChild(input);
        input.click();

        // 需要手动删除 input 元素，防止内存泄漏
        document.body.removeChild(input);
    };
}

/**
 * 清除区域值，公式，样式
 * @param parms
 * @param row
 * @param col
 * @param rowCount
 * @param colCount
 */
function clearRange(row, col, rowCount, colCount) {
    sheet.getRange(row, col, rowCount, colCount).clear(GC.Spread.Sheets.StorageType.style);//清空样式
    sheet.getRange(row, col, rowCount, colCount).clear(GC.Spread.Sheets.StorageType.data);//清空值
    sheet.setArrayFormula(row, col, rowCount, colCount, '');//清空公式
    sheet.conditionalFormats.removeRuleByRange(row, col, rowCount, colCount);//清除条件格式
    sheet.removeSpan(0, 0, GC.Spread.Sheets.SheetArea.viewport);
    clearCellMerge(sheet, row, col, rowCount, colCount);
}

/*工作簿暂停活性*/
function spreadSuspend() {
    spread.suspendPaint();//渲染前失活(提升性能)
    spread.suspendCalcService();//暂停计算服务
}

/*工作簿开启活性*/
function spreadResume() {
    spread.resumeCalcService();//开启计算服务
    spread.resumePaint();//渲染后激活
}

// 刷新
function refreshexcel() {
    LayerMsgBox.loading("刷新中...", 20000);
    inits2()
    LayerMsgBox.closeLoading();
    LayerMsgBox.success("刷新成功", 3000);
}

// 加载数据
function loadData(monthListWithDays) {
    var _sheet = sheet;
    let _totalArr = parmsDefaultWeek.totalArr;
    var jsonData = [
        {
            "name": "张三",
            "age": 25,
            "age1": 22,
            "gender": "男",
            "email": "zhangsan@example.com"
        },
        {
            "name": "李四",
            "age": 30,
            "age1": 56,
            "gender": "男",
            "email": "lisi@example.com"
        },
        {
            "name": "小红",
            "age": 22,
            "age1": 90,
            "gender": "女",
            "email": "xiaohong@example.com"
        }
    ];
    _sheet.addRows(3, jsonData.length); // 第1个参数为起始行的索引，第2个参数为要添加的行数
    for (let i = 0; i < jsonData.length; i++) {
        let _rowIndex = i + 3;
        let _rowData = jsonData[i];
        _sheet.setValue(_rowIndex, 0, _rowData.name);
        _sheet.setValue(_rowIndex, 1, _rowData.age);
        _sheet.setValue(_rowIndex, 2, _rowData.age1);
        _sheet.setValue(_rowIndex, 3, _rowData.gender);
        _sheet.setValue(_rowIndex, 4, _rowData.email);
    }

    for (let i = 4; i <= jsonData.length + 3; i++) {
        let index = 0;
        for (const key in monthListWithDays) {
            let totalArrElement = _totalArr[index];
            let monthListWithDay = monthListWithDays[key];
            let diff = totalArrElement - monthListWithDay + 1;
            _sheet.getCell(i - 1, totalArrElement).formula("=SUM(R" + i + "C" + diff + ":R" + i + "C" + totalArrElement + ")");
            index++;
        }
    }


    // 按列自动合并公共头部数据
    var range = new GC.Spread.Sheets.Range(3, 0, jsonData.length, parmsDefaultWeek.titles.length);
    _sheet.autoMerge(range,
        GC.Spread.Sheets.AutoMerge.AutoMergeDirection.column,
        GC.Spread.Sheets.AutoMerge.AutoMergeMode.free,
        GC.Spread.Sheets.SheetArea.viewport,
        GC.Spread.Sheets.AutoMerge.SelectionMode.merged);

    _sheet.getRange(3, -1, jsonData.length, -1).hAlign(GC.Spread.Sheets.HorizontalAlign.center);
    _sheet.getRange(3, -1, jsonData.length, -1).vAlign(GC.Spread.Sheets.VerticalAlign.center);
}

/**
 * 从 SpreadJS 中获取数据
 * @return {array} dataList - 表格数据数组，每个元素为一行数据
 */
function getData() {
    let columnTitle = ["seq", "name", "type", "code", "param", "username"];
    let dataList = [];
    let data = sheet.toJSON();
    let tableData = data.data.dataTable;

    // 从第4行开始遍历表格数据，前三行为标题和表头信息
    for (let i = 3; i < Object.keys(tableData).length; i++) {
        let tableDatum = tableData[i];
        let result = {};
        // 遍历行数据，根据列名数组解析出每行中对应的值
        for (let key in tableDatum) {
            result[columnTitle[key]] = tableDatum[key].value;
        }
        // 将当前行数据存入数据数组中
        dataList.push(result)
    }

    // 打印表格数据数组
    console.log(JSON.stringify(dataList))
    // 返回表格数据数组
    return dataList;

}

// 保存数据
function saveData() {

}

// 获取当天到截止时间的每个月份的天数
function getMonthListWithDays(startDate, endDate) {
    const start = startDate;
    const end = endDate;

    // 存储结果的对象
    const result = {};

    const startYear = start.getFullYear();
    const startMonth = start.getMonth() + 1;

    // 判断两个日期是否为同一年月
    if (startYear === end.getFullYear() && start.getMonth() === end.getMonth()) {
        const yearMonthStr = `${startYear}-${startMonth < 10 ? '0' : ''}${startMonth}`;
        result[yearMonthStr] = getDaysBetweenDates(start, end);
        return result;
    }

    // 循环生成年月信息
    while (start < end) {
        const year = start.getFullYear();
        const month = start.getMonth() + 1;

        const monthStr = `${year}-${month < 10 ? '0' : ''}${month}`;

        if (year === startYear && startMonth == month) {
            // 根据开始日期到该月底之间的天数，替换掉 result 中该月份的天数
            result[monthStr] = new Date(year, month, 0).getDate() - start.getDate() + 1;
        } else {
            result[monthStr] = new Date(year, month, 0).getDate()
        }

        // 将日期增加一个月
        start.setMonth(start.getMonth() + 1);
    }

    // 添加最后一个月份
    const lastYear = end.getFullYear();
    const lastMonth = end.getMonth() + 1;
    const lastMonthStr = `${lastYear}-${lastMonth < 10 ? '0' : ''}${lastMonth}`;

    // 根据结束日期到该月初之间的天数，替换掉 result 中该月份的天数
    const lastDaysInMonth = end.getDate();
    result[lastMonthStr] = lastDaysInMonth;

    return result;
}

// 计算两个日期相差天数
function getDaysBetweenDates(start, end) {
    if (start.getFullYear() === end.getFullYear() && start.getMonth() === end.getMonth() && start.getDate() === end.getDate()) {
        return 1;
    }

    // 将日期对象转换为时间戳（单位为毫秒）
    var timestamp1 = start.getTime();
    var timestamp2 = end.getTime();

    // 计算时间差的毫秒数
    var diff = Math.abs(timestamp2 - timestamp1);

    // 将时间差转换为天数
    var days = Math.ceil(diff / (1000 * 60 * 60 * 24));

    // 返回计算结果
    return days + 1; // 包含起始日期和结束日期，因此结果要加1
}

// 清除所有单元格合并
function clearCellMerge(sheet, startRow, startColumn, endRow, endColumn) {
    // 遍历指定区域内的所有单元格
    for (var row = startRow; row <= endRow; row++) {
        for (var column = startColumn; column <= endColumn; column++) {
            // 获取当前单元格的合并信息
            var span = sheet.getSpan(row, column);
            if (span) { // 如果单元格已经合并过，就清除合并
                sheet.removeSpan(row, column, GC.Spread.Sheets.SheetArea.viewport);
            }
        }
    }
}

// 锁定
function lock() {
    var _sheet = sheet;
    _sheet.getRange(-1, 2, -1, 1, GC.Spread.Sheets.SheetArea.viewport).locked(true); // 目前无用，参照api 但失效
    _sheet.getRange(4, 8, 1, 11, GC.Spread.Sheets.SheetArea.viewport).backColor("#f0f0f0");
}

// 解锁
function unlock() {
    var _sheet = sheet;
    _sheet.getRange(-1, 2, -1, 1, GC.Spread.Sheets.SheetArea.viewport).locked(false); // 目前无用，参照api 但失效
    _sheet.getRange(4, 8, 1, 11, GC.Spread.Sheets.SheetArea.viewport).backColor("#ffffff");
}







