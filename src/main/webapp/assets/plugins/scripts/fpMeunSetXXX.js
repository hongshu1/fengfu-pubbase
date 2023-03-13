    var pageName = document.getElementById("pageName").value; // 获取页面名称
    var controlId = document.getElementById('fpMeunSet').getAttribute('controlId');//控件id
    var spread = new GC.Spread.Sheets.Workbook(document.getElementById(controlId));
    var activeSheet = spread.getActiveSheet();//获取工作薄
    var sheetname=activeSheet.name();
    function showDataSource(dataSource) {
        var dataSource=dataSource;
        activeSheet.autoGenerateColumns = true;
        activeSheet.setDataSource(dataSource);
        //判断数据源是否为空
        if (dataSource!=null&&dataSource!= undefined&&dataSource!="") {
            var arr = []
            var counts = activeSheet.getColumnCount(GC.Spread.Sheets.SheetArea.colHeader);
            for (var i = 0; i < counts; i++) {
                var jsonobj = {};
                var colName = activeSheet.getValue(0, i, GC.Spread.Sheets.SheetArea.colHeader);
                jsonobj.colname = colName;
                arr.push(jsonobj);
            }
            var colArr = JSON.stringify(arr)
            $.ajax({
                dataType: "json",
                type: "POST",
                data: {'cols': colArr, 'pagename': pageName, 'controlId': controlId, 'sheetname': sheetname},
                url: "http://"+location.host+"/web/a/fpcustmeunset/tsysFpcustmeunset/compareMeunSet",
                success: function (data) {
                    if (data.message == "一致") {//判断当前列表的栏目与设置栏目是否一致
                        showMeunSet();
                    }
                }, error: function (data) {
                    log(系统错误);
                }
            });
            //改变列宽时执行
            spread.bind(GC.Spread.Sheets.Events.ColumnWidthChanged, function (sender, args) {
                setTimeout(function () {
                    meunSet();
                }, 1000);
            });
            //拖动列时执行
            activeSheet.bind(GC.Spread.Sheets.Events.DragDropBlock, function (e, args) {
                setTimeout(function () {
                    meunSet();
                }, 1000);
            });
            //左键单击角标
            activeSheet.bind(GC.Spread.Sheets.Events.CellClick, function (sender, args) {
                if (args.sheetArea === GC.Spread.Sheets.SheetArea.corner) {
                    showMeun(pageName, controlId, sheetname);
                }
            });
        }
    }
    function showMeunSet() {//设置栏目
        $.ajax({
            dataType: "json",
            type: "POST",
            data: {'pagename': pageName, 'controlId': controlId, 'sheetname': sheetname},
            url: "http://"+location.host+"/web/a/fpcustmeunset/tsysFpcustmeunset/showMeunSet",
            success: function (data) {
                if (data.showMeunSet != null && data.showMeunSet != undefined) {//用户设置过栏目显示
                    spread.suspendPaint();
                    var arr = data.showMeunSet;
                    var colInfos = [];
                    for (var i = 0; i < data.showMeunSet.length; i++) {
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
                            activeSheet.getRange(-1, sort, -1, 1, GC.Spread.Sheets.SheetArea.viewport).hAlign(GC.Spread.Sheets.HorizontalAlign.left);
                        } else if (align == "center") {
                            activeSheet.getRange(-1, sort, -1, 1, GC.Spread.Sheets.SheetArea.viewport).hAlign(GC.Spread.Sheets.HorizontalAlign.center);
                        } else if (align == "right") {
                            activeSheet.getRange(-1, sort, -1, 1, GC.Spread.Sheets.SheetArea.viewport).hAlign(GC.Spread.Sheets.HorizontalAlign.right);
                        }
                        jsonobj.name = "" + arr[i].columns;
                        jsonobj.displayName = "" + arr[i].columnname;
                        jsonobj.visible = ck;
                        jsonobj.size = arr[i].width;
                        colInfos.push(jsonobj)
                    }
                    activeSheet.autoGenerateColumns = false;
                    //activeSheet.setDataSource(dataSource);
                    activeSheet.bindColumns(colInfos);
                    spread.resumePaint();
                }
            }, error: function (data) {
                log(系统错误);
            }
        });
    }

    //保存栏目设置
    function meunSet() {
        var count = activeSheet.getColumnCount(GC.Spread.Sheets.SheetArea.colHeader);
        var array = []
        for (var i = 0; i < count; i++) {
            var jsonobj = {};
            var colName = activeSheet.getValue(0, i, GC.Spread.Sheets.SheetArea.colHeader);
            var visible = activeSheet.getColumnVisible(i, GC.Spread.Sheets.SheetArea.colHeader);
            var width = activeSheet.getColumnWidth(i, GC.Spread.Sheets.SheetArea.colHeader);
            var align = activeSheet.getCell(0, i).hAlign();
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
            jsonobj.pagename=pageName;
            jsonobj.controlid=controlId;
            jsonobj.datasoure=sheetname;
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
            {data:jsonObjArr,dragDrop:drag,pagename:pageName,controlId:controlId,sheetname:sheetname},
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
       /* var count = activeSheet.getColumnCount(GC.Spread.Sheets.SheetArea.colHeader);
        var colArr=[];
        for (var i = 0; i < count; i++) {
            var colName = activeSheet.getValue(0, i, GC.Spread.Sheets.SheetArea.colHeader);
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

