    var fpMeunSetObj = {};//插件参数对象
    // fpMeunSetObj.pageName = document.getElementById("pageName").value; // 获取页面名称
    // fpMeunSetObj.controlId = document.getElementById('fpMeunSet').getAttribute('controlId');//控件id
    // fpMeunSetObj.spread = new GC.Spread.Sheets.Workbook(document.getElementById(fpMeunSetObj.controlId));
    // fpMeunSetObj.activeSheet = fpMeunSetObj.spread.getActiveSheet();//获取工作薄
    // fpMeunSetObj.sheetname=fpMeunSetObj.activeSheet.name();

    function fpMeunSetInit(pageName,spreadId,spread) {
        //初始化
        fpMeunSetObj.pageName = pageName;
        fpMeunSetObj.controlId = spreadId;
        fpMeunSetObj.spread = spread;
        fpMeunSetObj.activeSheet = spread.getActiveSheet();
        fpMeunSetObj.sheetname = fpMeunSetObj.activeSheet.name();
    }

    function fpMeunSetIsInit(){
        //校验必要参数
        if(fpMeunSetObj == null || fpMeunSetObj == '{}' || Object.keys(fpMeunSetObj).length == 0
        || fpMeunSetObj.pageName == null || fpMeunSetObj.pageName == ''
            || fpMeunSetObj.controlId == null || fpMeunSetObj.controlId == ''
            || fpMeunSetObj.spread == null || fpMeunSetObj.activeSheet == null
            || fpMeunSetObj.sheetname == null || fpMeunSetObj.sheetname == ''){
            return false;
        }
        return true;
    }

    function showDataSource(dataSource) {
        if(!fpMeunSetIsInit()){
            alert('参数未初始化!');
            return;
        }
        for (let j = 0; j < dataSource.length; j++) {
            let newdata = {
                // _fpMeunSet_checkBox: null,
                ...dataSource[j]
            };
            dataSource[j] = newdata;
        }
        var dataSource=dataSource;
        fpMeunSetObj.activeSheet.autoGenerateColumns = true;
        fpMeunSetObj.activeSheet.setDataSource(dataSource);
        //判断数据源是否为空
        if (dataSource!=null&&dataSource!= undefined&&dataSource!="") {
            var arr = []
            var counts = fpMeunSetObj.activeSheet.getColumnCount(GC.Spread.Sheets.SheetArea.colHeader);
            for (var i = 0; i < counts; i++) {
                var jsonobj = {};
                var colName = fpMeunSetObj.activeSheet.getValue(0, i, GC.Spread.Sheets.SheetArea.colHeader);
                jsonobj.colname = colName;
                arr.push(jsonobj);
            }
            var colArr = JSON.stringify(arr);
            $.ajax({
                dataType: "json",
                type: "POST",
                async: false,
                data: {'cols': colArr, 'pagename': fpMeunSetObj.pageName, 'controlId': fpMeunSetObj.controlId, 'sheetname': fpMeunSetObj.sheetname},
                url: "http://"+location.host+"/web/a/fpcustmeunset/tsysFpcustmeunset/compareMeunSet",
                success: function (data) {
                    if (data.message == "一致") {//判断当前列表的栏目与设置栏目是否一致
                        showMeunSet(dataSource);
                    }else{
                        //addCheckBox(fpMeunSetObj.spread,fpMeunSetObj.activeSheet,dataSource);
                    }
                }, error: function (data) {
                    log('系统错误');
                }
            });
            //改变列宽时执行
            fpMeunSetObj.spread.bind(GC.Spread.Sheets.Events.ColumnWidthChanged, function (sender, args) {
                setTimeout(function () {
                    meunSet();
                }, 100);
            });
            //拖动列时执行
            fpMeunSetObj.activeSheet.bind(GC.Spread.Sheets.Events.DragDropBlock, function (e, args) {
                setTimeout(function () {
                    meunSet();
                }, 100);
            });
            //左键单击角标
            fpMeunSetObj.activeSheet.bind(GC.Spread.Sheets.Events.CellClick, function (sender, args) {
                if (args.sheetArea === GC.Spread.Sheets.SheetArea.corner) {
                    showMeun(fpMeunSetObj.pageName, fpMeunSetObj.controlId, fpMeunSetObj.sheetname);
                }
            });
        }
    }
    function showMeunSet(dataSource) {//设置栏目
        $.ajax({
            dataType: "json",
            type: "POST",
            async: false,
            data: {'pagename': fpMeunSetObj.pageName, 'controlId': fpMeunSetObj.controlId, 'sheetname': fpMeunSetObj.sheetname},
            url: "http://"+location.host+"/web/a/fpcustmeunset/tsysFpcustmeunset/showMeunSet",
            success: function (data) {
                if (data.showMeunSet != null && data.showMeunSet != undefined) {//用户设置过栏目显示
                    fpMeunSetObj.spread.suspendPaint();
                    var arr = data.showMeunSet;
                    var colInfos = [];
                    for (var i = 0; i < data.showMeunSet.length; i++) {
                        if(dataSource && !dataSource[0].hasOwnProperty(arr[i].columns)){
                            continue;
                        }
                        var jsonobj = {};
                        var ck = arr[i].visible;
                        if (ck == 1) {
                            ck = true;
                        } else {
                            ck = false;
                        }
                        var align = "" + arr[i].textalign;
                        var sort = arr[i].sort;
                        if (align == "left") {
                            fpMeunSetObj.activeSheet.getRange(-1, sort, -1, 1, GC.Spread.Sheets.SheetArea.viewport).hAlign(GC.Spread.Sheets.HorizontalAlign.left);
                        } else if (align == "center") {
                            fpMeunSetObj.activeSheet.getRange(-1, sort, -1, 1, GC.Spread.Sheets.SheetArea.viewport).hAlign(GC.Spread.Sheets.HorizontalAlign.center);
                        } else if (align == "right") {
                            fpMeunSetObj.activeSheet.getRange(-1, sort, -1, 1, GC.Spread.Sheets.SheetArea.viewport).hAlign(GC.Spread.Sheets.HorizontalAlign.right);
                        }
                        jsonobj.name = "" + arr[i].columns;
                        jsonobj.displayName = "" + arr[i].columnname;
                        jsonobj.visible = ck;
                        jsonobj.size = arr[i].width;
                        colInfos.push(jsonobj)
                    }
                    fpMeunSetObj.activeSheet.autoGenerateColumns = false;
                    //fpMeunSetObj.activeSheet.setDataSource(dataSource);
                    fpMeunSetObj.activeSheet.bindColumns(colInfos);
                    //addCheckBox(fpMeunSetObj.spread,fpMeunSetObj.activeSheet,dataSource);
                    fpMeunSetObj.spread.resumePaint();
                }
            }, error: function (data) {
                log('系统错误');
            }
        });
    }

    //保存栏目设置
    function meunSet() {
        var count = fpMeunSetObj.activeSheet.getColumnCount(GC.Spread.Sheets.SheetArea.colHeader);
        var array = []
        for (var i = 0; i < count; i++) {
            var jsonobj = {};
            var colName = fpMeunSetObj.activeSheet.getValue(0, i, GC.Spread.Sheets.SheetArea.colHeader);
            var visible = fpMeunSetObj.activeSheet.getColumnVisible(i, GC.Spread.Sheets.SheetArea.colHeader);
            var width = fpMeunSetObj.activeSheet.getColumnWidth(i, GC.Spread.Sheets.SheetArea.colHeader);
            var align = fpMeunSetObj.activeSheet.getCell(0, i).hAlign();
            var align1 = ""
            if (align == 0) {
                align1 = "left";
            } else if (align == 1) {
                align1 = "center";
            } else if (align == 2) {
                align1 = "right";
            } else {
                align1 = "left";
            }
            jsonobj.pagename=fpMeunSetObj.pageName;
            jsonobj.controlid=fpMeunSetObj.controlId;
            jsonobj.datasoure=fpMeunSetObj.sheetname;
            jsonobj.tag="";
            jsonobj.sort = i;
            jsonobj.columns = colName;
            jsonobj.columnname = colName;
            jsonobj.visible = visible;
            jsonobj.textalign = align1;
            jsonobj.width = width;
            array.push(jsonobj)
        }
        var jsonObjArr = JSON.stringify(array)
        //log(jsonObjArr)
        let result = {result: 'false'};
        var drag = "drag";
        js.ajaxSubmit(
            "http://"+location.host+"/web/a/fpcustmeunset/tsysFpcustmeunset/save",
            {data:jsonObjArr,dragDrop:drag,pagename:fpMeunSetObj.pageName,controlId:fpMeunSetObj.controlId,sheetname:fpMeunSetObj.sheetname},
            function (res) {
                if (res.result == "false") {
                    js.showMessage(res.message, "", "error", 2000);
                } else {
                    // js.showMessage(res.message, "", "success", 2000);
                }
                result.result = res.result;
                result.message = res.message;
            }, "json", false, js.text('loading.message')
        );
        return result;
    }

    //显示栏目显示设置弹窗
    function showMeun(pname,control,sname) {
       /* var count = fpMeunSetObj.activeSheet.getColumnCount(GC.Spread.Sheets.SheetArea.colHeader);
        var colArr=[];
        for (var i = 0; i < count; i++) {
            var colName = fpMeunSetObj.activeSheet.getValue(0, i, GC.Spread.Sheets.SheetArea.colHeader);
            colArr.push(colName)
        }*/
        layer.open({
            type: 2,
            title: '栏目显示设置',
            maxmin: false,
            area: ['40%', '56%'],
            scrollbar:false,
            content: "http://"+location.host+"/web/a/fpcustmeunset/tsysFpcustmeunset/showMeun?pname="+pname+"&control="+control+"&sname="+sname,
            btn: ['关闭'],
            success: function (layero, index) {
                // 获取子页面的iframe
                //var iframe = window['layui-layer-iframe' + index];
                // 向子页面的全局函数child传参
                //iframe.child(pname,control,sname,colArr);
                var body = layer.getChildFrame('body', index);//获取子页面内容
                var iframeWin = window[layero.find('iframe')[0]['name']];
            },
            end: function () {

            }
        });
    }


    /**
     * 添加复选框
     * @param parms
     */
    function addCheckBox(spread,activeSheet,data) {
        if(data == null || data.length <= 0){
            return;
        }
        let indexCol = 0;
        for (let i=0;i<Object.keys(data[0]).length;i++){
            let colHeaderName = activeSheet.getValue(0,i,GC.Spread.Sheets.SheetArea.colHeader);
            // log(colHeaderName);
            if(colHeaderName == '_fpMeunSet_checkBox'){
                break;
            }
            indexCol++;
        }
        if(indexCol > Object.keys(data[0]).length-1){
            return;
        }
        //添加列（它们将成为未绑定的列）。
        activeSheet.setCellType(0, indexCol, new MyCheckBoxCellType(), GC.Spread.Sheets.SheetArea.colHeader);
        for (var i = 0; i < data.length ; i++) {
            var c = new GC.Spread.Sheets.CellTypes.CheckBox();
            c.textAlign(GC.Spread.Sheets.CellTypes.CheckBoxTextAlign.right);
            activeSheet.setCellType(i, indexCol, c);
        }

        spread.bind(GC.Spread.Sheets.Events.ButtonClicked, function(e, args) {
            var sheet = args.sheet,
                row = args.row,
                col = args.col;
            var cellType = sheet.getCellType(row, col);
            if (cellType instanceof GC.Spread.Sheets.CellTypes.CheckBox) {
                var colHeaderCell = cellType = sheet.getCell(0, col, GC.Spread.Sheets.SheetArea.colHeader);
                if (colHeaderCell.cellType() instanceof MyCheckBoxCellType) {
                    var checkStatus = true;
                    for (var i = 0; i < sheet.getRowCount(); i++) {
                        var cell = sheet.getCell(i, col);
                        if (cell.cellType() instanceof GC.Spread.Sheets.CellTypes.CheckBox && !cell.value()) {
                            checkStatus = false;
                            break;
                        }
                    }
                    colHeaderCell.tag(checkStatus);
                    sheet.repaint();
                }
            }
        });
    }

