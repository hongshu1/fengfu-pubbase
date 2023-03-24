var spread5 = new GC.Spread.Sheets.Workbook(document.getElementById("ss"), {
    tabStripVisible: false
});
GC.Spread.Common.CultureManager.culture("zh-cn");
var excelIo = new GC.Spread.Excel.IO();
var sheet = spread5.getActiveSheet();
// 状态栏
var statusBar = new GC.Spread.Sheets.StatusBar.StatusBar(document.getElementById('statusBar'));
statusBar.bind(spread5);

const parmsDefault = {
    titles: ['序号', '客户名称', '机型', '存货编码', '客户部番', '客户名称'],
    columnTitle: ["seq", "name", "type", "code", "param", "username"],
    columnWidth: [60, 120, 120, 120, 120, 120],
    rowIndex: 0, // 表格初始位置行索引
    columnIndex: 0, // 表格初始位置列索引
    dataStartCol: 9, // 日期开始列索引前的列数
};

inits()

// init 表单
function inits() {
    spreadSuspend();
    spread5.options.allowUserDragFill = true; // 允许用户拖动填充区域
    spread5.options.referenceStyle = GC.Spread.Sheets.ReferenceStyle.r1c1; // 此表单中单元格公式中的单元格和区域引用的样式

    sheet.getDefaultStyle().locked = true; //默认样式设为非锁定
    sheet.options.colHeaderAutoText = GC.Spread.Sheets.HeaderAutoText["numbers"]; //列号换成数字
    sheet.options.isProtected = false; //设置表单保护以控制表单上的单元格是否可编辑
    sheet.options.allowCellOverflow = false; // 数据是否可以溢出到相邻的空单元格中
    sheet.options.protectionOptions = {
        allowResizeColumns: true, //用户是否可以改变列宽
        allowResizeRows: true //用户是否可以改变行高
    };
    sheet.setRowCount(0);
    sheet.setColumnCount(0);

    clearRange(0, 0, sheet.getRowCount(GC.Spread.Sheets.SheetArea.viewport), sheet.getColumnCount(GC.Spread.Sheets.SheetArea.viewport))

    // 初始化天数
    let layYear = $("#layYear").val();
    if (layYear) {
        sheet.setColumnCount(25);
        loadUniversalHead();
        loadDay();
        setTableBorder(25);
        loadData();
    }

    //添加事件
    addEvent();
    spreadResume();
}

// 加载通用头
function loadUniversalHead() {
    var _sheet = sheet;
    let parms = parmsDefault
    var _rowindex = parms.rowIndex; // 初始行
    var _columnIndex = parms.columnIndex; // 初始列
    var _titles = parms.titles;
    var _columnWidth = parms.columnWidth;

    _sheet.addRows(0, 3); // 第1个参数为起始行的索引，第2个参数为要添加的行数

    // 通用头部
    for (let i = 0; i < _titles.length; i++) {
        _sheet.addSpan(_rowindex, _columnIndex + i, 3, 1);
        _sheet.getCell(_rowindex, _columnIndex + i)
            .text(_titles[i])
        _sheet.setColumnWidth(_columnIndex + i, _columnWidth[i]);
    }

    _sheet.getCell(_rowindex, _columnIndex + _titles.length).text('年份');
    _sheet.getCell(_rowindex + 1, _columnIndex + _titles.length).text('月份');
    _sheet.getCell(_rowindex + 2, _columnIndex + _titles.length).text('客户行事历');
    _sheet.setColumnWidth(_columnIndex + _titles.length, 150);

    sheet.getRange(0, -1, 3, -1).backColor("#f2f2f2");
    sheet.getRange(0, -1, 3, -1).hAlign(GC.Spread.Sheets.HorizontalAlign.center);
    sheet.getRange(0, -1, 3, -1).vAlign(GC.Spread.Sheets.VerticalAlign.center);
    sheet.getRange(0, -1, 3, -1, GC.Spread.Sheets.SheetArea.viewport).locked(true);
}

// 初始化天数
function loadDay() {
    var _sheet = sheet;
    let parms = parmsDefault
    var _rowindex = parms.rowIndex; // 初始行
    var _columnIndex = parms.columnIndex; // 初始列
    var _date = DateUtils.getDate($("#layYear").val());
    var titleLength = parms.titles.length;

    // 设置年
    _sheet.addSpan(_rowindex, _columnIndex + titleLength + 2, 1, 13)
    var _fullYear = _date.getFullYear();
    _sheet.getCell(_rowindex, _columnIndex + titleLength + 2).text(_fullYear + "年")

    // 设置月
    for (let i = 1; i <= 12; i++) {
        // 获取当前月份的天数
        let strTurnDate = DateUtils.strTurnDate(_fullYear + "-" + StringUtils.formatZero(i, 2) + "-01");
        let countDays = DateUtils.getCountDays(strTurnDate);

        _sheet.getCell(_rowindex + 1, _columnIndex + titleLength + 1 + i).text(i + "月");
        _sheet.getCell(_rowindex + 2, _columnIndex + titleLength + 1 + i).text(countDays + "");
    }
    // 设置合计列
    let _totalColumn = _columnIndex + titleLength + 1 + 12 + 1;
    _sheet.getCell(_rowindex + 1, _totalColumn).text("合计");
    _sheet.getCell(_rowindex + 2, _totalColumn).formula("=SUM(R3C" + (_totalColumn - 12 - 1) + ":R3C" + (_totalColumn - 1) + ")");

    // 设置下一年
    let _nextFullYear = _fullYear + 1;
    _sheet.addSpan(_rowindex, _totalColumn + 1, 1, 4)
    _sheet.getCell(_rowindex, _totalColumn + 1).text(_nextFullYear + "年")

    // 设置下一年的月
    for (let i = 1; i <= 3; i++) {
        // 获取当前月份的天数
        let strTurnDate = DateUtils.strTurnDate(_nextFullYear + "-" + StringUtils.formatZero(i, 2) + "-01");
        let countDays = DateUtils.getCountDays(strTurnDate);

        _sheet.getCell(_rowindex + 1, _totalColumn + i).text(i + "月");
        _sheet.getCell(_rowindex + 2, _totalColumn + i).text(countDays + "");
    }

    // 设置下一年的合计列
    let _nextTotalColumn = _totalColumn + 3 + 1;
    _sheet.getCell(_rowindex + 1, _nextTotalColumn).text("合计");
    _sheet.getCell(_rowindex + 2, _nextTotalColumn).formula("=SUM(R3C" + (_nextTotalColumn - 2) + ":R3C" + (_nextTotalColumn) + ")");


}

//设置表格边框
function setTableBorder(layYear) {
    const range = sheet.getRange(0, 0, 3, layYear); // 获取要设置边框的单元格范围
    range.setBorder(new GC.Spread.Sheets.LineBorder('black', GC.Spread.Sheets.LineStyle.thin), {
        all: GC.Spread.Sheets.LineStyle.thin
    });
}

/**
 * 添加事件
 * @param parms
 */
function addEvent() {
    //值变化事件
    sheet.bind(GC.Spread.Sheets.Events.ValueChanged, function (e, info) {
        let row = info.row, col = info.col;
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
        var json = spread5.toJSON();
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
                        spread5.fromJSON(workbookObj);
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
    spread5.suspendPaint();//渲染前失活(提升性能)
    spread5.suspendCalcService();//暂停计算服务
}

/*工作簿开启活性*/
function spreadResume() {
    spread5.resumeCalcService();//开启计算服务
    spread5.resumePaint();//渲染后激活
}

// 刷新
function refreshexcel() {
    LayerMsgBox.loading("刷新中...", 20000);
    inits()
    LayerMsgBox.closeLoading();
    LayerMsgBox.success("刷新成功", 3000);
}

// 加载数据
function loadData() {
    var _sheet = spread5.getActiveSheet();
    var jsonData = [
        {
            "name": "张三",
            "age": 25,
            "gender": "男",
            "email": "zhangsan@example.com"
        },
        {
            "name": "李四",
            "age": 30,
            "gender": "男",
            "email": "lisi@example.com"
        },
        {
            "name": "小红",
            "age": 22,
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
        _sheet.setValue(_rowIndex, 2, _rowData.gender);
        _sheet.setValue(_rowIndex, 3, _rowData.email);
    }

    // 按列自动合并公共头部数据
    var range = new GC.Spread.Sheets.Range(3, 0, jsonData.length, parmsDefault.titles.length);
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
    // 定义表格列名数组
    let columnTitle = ["seq", "name", "type", "code", "param", "username"];
    // 定义表格数据数组
    let dataList = [];
    // 获取表格数据对象
    let data = spread5.getActiveSheet().toJSON();
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

