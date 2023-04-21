var spread = new GC.Spread.Sheets.Workbook(document.getElementById('ss'), { sheetCount: 1 });
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
spreadSuspend();
var excelIO = new GC.Spread.Excel.IO();
spread.options.allowUserDragFill = true;//允许用户拖放数据范围
spread.options.referenceStyle = GC.Spread.Sheets.ReferenceStyle.r1c1;
var statusBar = new GC.Spread.Sheets.StatusBar.StatusBar(document.getElementById('statusBar'));
statusBar.bind(spread);//状态栏
//资源列表
// var resourcesListJSON = '${resourcesListJSONString}';
// var resourcesList = eval("("+resourcesListJSON+")");
// console.log(resourcesList)
// var layerZIndex = 20000000;//弹窗层叠优先级
var parmsDefault = {		//参数集合
    resourcesList: [],//资源列表
    isExpand: false,		//是否已设置折叠
    countday: 93,			//展示天数
    monthday: [],			//每月天数
    rowindex: 10,			//物料表格初始位置行索引
    columnindex: 0,			//物料表格初始位置列索引
    dataStartCol: 9,		//日期开始列索引前的列数
    workTimeRow: 0,			//设备工时行索引
    workTimeCol: 6,			//设备工时列索引
    separateRowNum: 2,		//设备与数据相隔行数
    totalListedArr: [],		//TotalListed行索引
    subAssyArr: [],			//SubAssy行索引
    equipData: {},			//设备汇总
    invRowCount: 26,		//每个料号数据行数
    selectDataList: {},		//查询条件
    data: [],				//数据
    invRowIndex: {},		//各料号所在行
    currentRow: 0,			//当前选中的单元格行
    currentCol: 0,			//当前选中的单元格列
    assyDate: '',			//组立最后锁定时间
    transferDate: '',		//冲压最后锁定时间
    lockedRC: [],			//禁止编辑的RC坐标数组
};
var parmsArr = [ObjectUtils.deepClone(parmsDefault)];
spread.getSheet(0).options.colHeaderAutoText = GC.Spread.Sheets.HeaderAutoText["blank"];//列号换成空白
spread.getSheet(0).setRowCount(500);
spread.getSheet(0).setColumnCount(115);
spread.getSheet(0).options.isProtected = true;//表单设为保护状态
spread.getSheet(0).options.protectionOptions = {allowFilter: true};//保护状态允许筛选
spread.getSheet(0).getDefaultStyle().locked = true;//默认样式设为非锁定
spread.getSheet(0).options.protectionOptions.allowResizeColumns = true;//保护状态允许调整行列
spread.getSheet(0).options.protectionOptions.allowResizeRows = true;//保护状态允许调整行列
parmsArr[0]['spread'] = spread;
parmsArr[0]['activeSheet'] = spread.getSheet(0);
parmsArr[0]['nowDate'] = new Date('${@DateUtils.getDate()}'+' 00:00:00');//当天日期
var parms = parmsArr[0];
console.log(parms)
function Refreshexcel() {
    console.log(222)
}
//初始化天数
loadDay(parms);
// //添加事件
// addEvent(parms);
spreadResume()
/**
 * 初始化天数
 * @param parms
 */
function loadDay(parms) {
    var spreadNS = GC.Spread.Sheets;
    parms.activeSheet.setRowCount(3, spreadNS.SheetArea.colHeader);
    //折叠列
    // if(!parms.isExpand){
    //     parms.activeSheet.columnOutlines.group(parms.columnindex+2, 4);
    //     let groupInfo = parms.activeSheet.columnOutlines.find(parms.columnindex+2, 0);
    //     if(groupInfo){
    //         parms.activeSheet.columnOutlines.expandGroup(groupInfo, false);
    //     }
    //     parms.isExpand = true;
    // }
    //日期前面头部信息初始化
    parms.activeSheet.addSpan(0, 0, 3, 1, GC.Spread.Sheets.SheetArea.colHeader);
    parms.activeSheet.addSpan(0, 1, 3, 1, GC.Spread.Sheets.SheetArea.colHeader);
    parms.activeSheet.addSpan(0, 2, 3, 1, GC.Spread.Sheets.SheetArea.colHeader);
    parms.activeSheet.addSpan(0, 3, 3, 1, GC.Spread.Sheets.SheetArea.colHeader);
    parms.activeSheet.addSpan(0, 4, 3, 1, GC.Spread.Sheets.SheetArea.colHeader);
    parms.activeSheet.addSpan(0, 5, 3, 1, GC.Spread.Sheets.SheetArea.colHeader);
    parms.activeSheet.setValue(0, 0, "序号", GC.Spread.Sheets.SheetArea.colHeader);
    parms.activeSheet.setValue(0, 1, "排产层级", GC.Spread.Sheets.SheetArea.colHeader);
    parms.activeSheet.setValue(0, 2, "产线名称", GC.Spread.Sheets.SheetArea.colHeader);
    parms.activeSheet.setValue(0, 3, "存货编码", GC.Spread.Sheets.SheetArea.colHeader);
    parms.activeSheet.setValue(0, 4, "客户部番", GC.Spread.Sheets.SheetArea.colHeader);
    parms.activeSheet.setValue(0, 5, "部品名称", GC.Spread.Sheets.SheetArea.colHeader);
    // parms.activeSheet.setValue(0, 6, "排产层级", GC.Spread.Sheets.SheetArea.colHeader);
    // parms.activeSheet.setValue(0, 7, "排产层级", GC.Spread.Sheets.SheetArea.colHeader);
    // parms.activeSheet.setValue(0, 8, "排产层级", GC.Spread.Sheets.SheetArea.colHeader);
    // parms.activeSheet.setValue(0, 9, "排产层级", GC.Spread.Sheets.SheetArea.colHeader);
    parms.activeSheet.setColumnWidth(parms.columnindex, 60);
    parms.activeSheet.setColumnWidth(parms.columnindex+1, 100);
    parms.activeSheet.setColumnWidth(parms.columnindex+2, 120);
    parms.activeSheet.setColumnWidth(parms.columnindex+3, 120);
    parms.activeSheet.setColumnWidth(parms.columnindex+4, 150);
    parms.activeSheet.setColumnWidth(parms.columnindex+5, 200);
    parms.activeSheet.setColumnWidth(parms.columnindex+6, 100);
    // parms.activeSheet.getCell(-1, 0).width(100);

}
/**
 * 添加事件
 * @param parms
 */
function addEvent(parms) {
    //值变化事件
    parms.activeSheet.bind(GC.Spread.Sheets.Events.ValueChanged, function(e,info) {
        let row = info.row, col = info.col;
    });
    //活动工作表切换
    spread.bind(GC.Spread.Sheets.Events.ActiveSheetChanged, function (sender, args) {
        let sheet = spread.getActiveSheet();
        let sheetName = sheet.name();
        let sheetNo = parseInt(sheetName.substring(5));
        while (parmsArr.length < sheetNo){
            //新增页签
            let parmsDefaultNew = ObjectUtils.deepClone(parmsDefault);
            parmsDefaultNew['spread'] = spread;
            parmsDefaultNew['nowDate'] = new Date('${@DateUtils.getDate()}');
            parmsArr.push(parmsDefaultNew);
        }
        if(parmsArr[sheetNo-1]['activeSheet'] == undefined || parmsArr[sheetNo-1]['activeSheet'] == null){
            //新增页签设置
            spread.getSheetFromName(sheetName).options.colHeaderAutoText = GC.Spread.Sheets.HeaderAutoText["numbers"];//列号换成数字
            spread.getSheetFromName(sheetName).setRowCount(500);
            spread.getSheetFromName(sheetName).setColumnCount(115);
            spread.getSheetFromName(sheetName).options.isProtected = true;//表单设为保护状态
            spread.getSheetFromName(sheetName).options.protectionOptions = {allowFilter: true};//保护状态允许筛选
            spread.getSheetFromName(sheetName).getDefaultStyle().locked = true;//默认样式设为非锁定
            spread.getSheetFromName(sheetName).options.protectionOptions.allowResizeColumns = true;//保护状态允许调整行列
            spread.getSheetFromName(sheetName).options.protectionOptions.allowResizeRows = true;//保护状态允许调整行列
            parmsArr[sheetNo-1]['activeSheet'] = spread.getSheetFromName(sheetName);
            parmsArr[sheetNo-1]['nowDate'] = new Date('${@DateUtils.getDate()}');
        }
    });
    //双击事件
    parms.activeSheet.bind(GC.Spread.Sheets.Events.CellDoubleClick, function (sender, args) {
        var sheet = args.sheet, row = args.row, col = args.col;
        let rc = 'R'+(row+1)+'C'+(col+1);
        if(args.sheetArea === GC.Spread.Sheets.SheetArea.colHeader){
            console.log("列标题双击.");
        }
        if(args.sheetArea === GC.Spread.Sheets.SheetArea.rowHeader){
            console.log("行标题双击.");
        }
        if(args.sheetArea === GC.Spread.Sheets.SheetArea.corner){
            console.log("角头双击.");
        }
        //双击单元格
        if(args.sheetArea === GC.Spread.Sheets.SheetArea.viewport){
            if(parms.lockedRC.indexOf(rc) > -1){
                console.log("双击单元格["+rc+"]"+args.sheetArea);
                // js.showMessage('禁止修改', '警告', 'warning', 3000);
                js.alert('禁止修改！', {icon: 2});
            }
        }
    });
    //开始编辑
    // parms.activeSheet.bind(GC.Spread.Sheets.Events.EditStarting, function (e, info) {
    // 	let row = info.row, col = info.col;
    // });
    // //结束编辑
    // parms.activeSheet.bind(GC.Spread.Sheets.Events.EditEnded, function (e, info) {
    // 	let row = info.row, col = info.col;
    // });
}
/**
 * 加载数据
 * @param parms
 */
function loadData(parms) {
    //设置组立最后锁定时间
    if(!StringUtils.isBlack(parms['assyDate'])){
        $("#assyDate").val(parms['assyDate']);
        $("#unassyDate").val(parms['assyDate']);
    }
    //设置冲压最后锁定时间
    if(!StringUtils.isBlack(parms['transferDate'])){
        $("#transferDate").val(parms['transferDate']);
        $("#untransferDate").val(parms['transferDate']);
    }
    //记录各料号所在行号
    parms['invRowIndex'] = {};
    for (let i = 0; i < parms.data.length; i++){
        //记录各料号所在行号
        parms['invRowIndex'][parms.data[i]['InvCode']] = parms.rowindex+3+parms.invRowCount*i;
    }
    for (let i = 0; i < parms.data.length; i++){
        let head = parms.data[i];
        //ResourceType(deptcode.assy 组立、deptcode.transfer 冲压、deptcode.purchase 采购、deptcode.material 卷料)
        if(head['DeptCode'] == parms['deptcode']['assy']){//组立
            setAssyHead(parms,head,parms.rowindex+3+parms.invRowCount*i,parms.columnindex);
            setAssyDetail(parms,head,parms.rowindex+3+parms.invRowCount*i,parms.columnindex+parms.dataStartCol);
        }else if(head['DeptCode'] == parms['deptcode']['transfer']){//冲压
            setTransferHead(parms,head,parms.rowindex+3+parms.invRowCount*i,parms.columnindex);
            setTransferDetail(parms,head,parms.rowindex+3+parms.invRowCount*i,parms.columnindex+parms.dataStartCol);
        }else if(head['DeptCode'] == parms['deptcode']['purchase']){//采购
            setPurchaseHead(parms,head,parms.rowindex+3+parms.invRowCount*i,parms.columnindex);
            setPurchaseDetail(parms,head,parms.rowindex+3+parms.invRowCount*i,parms.columnindex+parms.dataStartCol);
        }else if(head['DeptCode'] == parms['deptcode']['material']){//卷料
            setMaterialHead(parms,head,parms.rowindex+3+parms.invRowCount*i,parms.columnindex);
            setMaterialDetail(parms,head,parms.rowindex+3+parms.invRowCount*i,parms.columnindex+parms.dataStartCol);
        }
    }
    //设置数据区域
    // settingDataRange(parms);
}