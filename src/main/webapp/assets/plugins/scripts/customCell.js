/**
 * 日期选择器
 * @constructor
 */
function DatePickerCellType() {
}
DatePickerCellType.prototype = new GC.Spread.Sheets.CellTypes.Base();
DatePickerCellType.prototype.createEditorElement = function () {
    //Create input presenter.
    var input = document.createElement("input");
    return input;
};
DatePickerCellType.prototype.activateEditor = function (editorContext, cellStyle, cellRect) {
    //Initialize input editor.
    if (editorContext) {
        $editor = $(editorContext);
        GC.Spread.Sheets.CellTypes.Base.prototype.activateEditor.apply(this, arguments);
        $editor.datepicker({
            changeMonth: true,
            changeYear: true
        });
        $editor.css("position", "absolute");
        $editor.attr("gcUIElement", "gcEditingInput");
        $(".ui-datepicker").attr("gcUIElement", "gcEditingInput");
    }
}
DatePickerCellType.prototype.deactivateEditor = function (editorContext) {
    //Remove input editor when end editor status.
    if (editorContext) {
        var element = editorContext;
        $(element).datepicker("hide");
        $(element).datepicker("destroy");
    }
    GC.Spread.Sheets.CellTypes.Base.prototype.deactivateEditor.apply(this, arguments)
};
DatePickerCellType.prototype.setEditorValue = function (editor, value) {
    //Sync value from Cell value to editor value.
    $(editor).datepicker("setDate", value);
};
DatePickerCellType.prototype.getEditorValue = function (editor) {
    //Sync value from editor value to cell value.
    return $(editor).datepicker("getDate");
};
DatePickerCellType.prototype.updateEditor = function (editorContext, cellStyle, cellRect) {
    if (editorContext) {
        $editor = $(editorContext);
        $editor.css("width", cellRect.width - 1);
        $editor.css("height", cellRect.height - 3);
    }
};


/**
 * 年月选择器
 * @constructor
 */
function YearMonthPickerCellType() {
}
YearMonthPickerCellType.prototype = new GC.Spread.Sheets.CellTypes.Base();
YearMonthPickerCellType.prototype.createEditorElement = function () {
    //Create input presenter.
    var input = document.createElement("input");
    return input;
};
YearMonthPickerCellType.prototype.activateEditor = function (editorContext, cellStyle, cellRect) {
    //Initialize input editor.
    if (editorContext) {
        $editor = $(editorContext);
        GC.Spread.Sheets.CellTypes.Base.prototype.activateEditor.apply(this, arguments);
        $editor.datepicker({
            changeMonth: true,
            changeYear: true,
            beforeShow: function (input,inst) {
                $("#ui-datepicker-div").addClass( "month");
            },
            showButtonPanel: true,
            beforeShow:function(input,inst){
                $("#ui-datepicker-div").addClass( "month");
            },
            onClose:function(dateText, inst) {
                var month = $("#ui-datepicker-div .ui-datepicker-month option:selected").val();//得到选中的月份值
                var year = $("#ui-datepicker-div .ui-datepicker-year option:selected").val();//得到选中的年份值
                var daystr = $editor[0].value.substring($editor[0].value.indexOf('/')+1,$editor[0].value.length);
                var day = daystr.substring(0,daystr.indexOf('/'));
                $editor[0].value = (parseInt(month)+1)+'/'+day+'/'+year;
            }
        });
        $editor.css("position", "absolute");
        $editor.attr("gcUIElement", "gcEditingInput");
        $(".ui-datepicker").attr("gcUIElement", "gcEditingInput");
    }
}
YearMonthPickerCellType.prototype.deactivateEditor = function (editorContext) {
    //Remove input editor when end editor status.
    if (editorContext) {
        var element = editorContext;
        $(element).datepicker("hide");
        $(element).datepicker("destroy");
    }
    GC.Spread.Sheets.CellTypes.Base.prototype.deactivateEditor.apply(this, arguments)
};
YearMonthPickerCellType.prototype.setEditorValue = function (editor, value) {
    //Sync value from Cell value to editor value.
    $(editor).datepicker("setDate", value);
};
YearMonthPickerCellType.prototype.getEditorValue = function (editor) {
    //Sync value from editor value to cell value.
    return $(editor).datepicker("getDate");
};
YearMonthPickerCellType.prototype.updateEditor = function (editorContext, cellStyle, cellRect) {
    if (editorContext) {
        $editor = $(editorContext);
        $editor.css("width", cellRect.width - 1);
        $editor.css("height", cellRect.height - 3);
    }
};


/**
 * 全选复选框
 * @constructor
 */
function MyCheckBoxCellType() {
    GC.Spread.Sheets.CellTypes.CheckBox.apply(this);
    this.caption("全选");
}
MyCheckBoxCellType.prototype = new GC.Spread.Sheets.CellTypes.CheckBox();
var basePaint = GC.Spread.Sheets.CellTypes.CheckBox.prototype.paint;
MyCheckBoxCellType.prototype.paint = function(ctx, value, x, y, width, height, style, context) {
    //var tag = context.sheet.getColumn(context.col,context.sheetArea).tag();
    var tag = context.sheet.getTag(context.row, context.col, context.sheetArea);
    if (tag !== true) {
        tag = false;
    }
    basePaint.apply(this, [ctx, tag, x, y, width, height, style, context]);
};
MyCheckBoxCellType.prototype.getHitInfo = function(x, y, cellStyle, cellRect, context) {
    if (context) {
        return {
            x: x,
            y: y,
            row: context.row,
            col: context.col,
            cellRect: cellRect,
            sheetArea: context.sheetArea,
            isReservedLocation: true,
            sheet: context.sheet
        };
    }
    return null;
};
MyCheckBoxCellType.prototype.processMouseUp = function(hitInfo) {
    var sheet = hitInfo.sheet,
        row = hitInfo.row,
        col = hitInfo.col,
        sheetArea = hitInfo.sheetArea;
    if (sheet.getCell(0, 0, GC.Spread.Sheets.SheetArea.colHeader).locked() && sheet.options.isProtected) {
        return;
    }
    var tag = sheet.getTag(row, col, sheetArea);
    //var tag = sheet.getColumn(col,sheetArea).tag();
    if (tag === undefined || tag === null) { //first time
        sheet.setTag(row, col, true, sheetArea);
        //sheet.getColumn(col,sheetArea).tag(true)
    } else {
        sheet.setTag(row, col, !tag, sheetArea);
        //sheet.getColumn(col,sheetArea).tag(!tag)
    }
    //add your code here
    tag = sheet.getTag(row, col, sheetArea);
    //tag = sheet.getColumn(col,sheetArea).tag();
    // sheet.setValue(-1, 0, tag);
    sheet.suspendPaint();
    for (var i = 0; i < sheet.getRowCount(); i++) {
        var cell = sheet.getCell(i, col);
        if (cell.cellType() instanceof GC.Spread.Sheets.CellTypes.CheckBox) {
            cell.value(tag);
        }
    }
    sheet.resumePaint();
};



/**
 * 自定义单元格类型，使用{text,value}的数据格式
 * @constructor
 */
function FullNameCellType() {
}
FullNameCellType.prototype = new GC.Spread.Sheets.CellTypes.Base();
FullNameCellType.prototype.paint = function (ctx, value, x, y, w, h, style, context) {
    if (value) {
        GC.Spread.Sheets.CellTypes.Base.prototype.paint.apply(this, [ctx, value.text, x, y, w, h, style, context]);
    }
};


/**
 * 超出显示省略号...
 * @constructor
 */
function EllipsisTextCellType() {
}
EllipsisTextCellType.prototype = new GC.Spread.Sheets.CellTypes.Text();
EllipsisTextCellType.prototype.paint = function (ctx, value, x, y, w, h, style, context) {
    ctx.font = style.font;
    value = fittingString(ctx, value, w - 2);
    GC.Spread.Sheets.CellTypes.Text.prototype.paint.apply(this, arguments);
};
function fittingString(c, str, maxWidth) {
    var width = c.measureText(str).width;
    var ellipsis = '…';
    var ellipsisWidth = c.measureText(ellipsis).width;
    if (width <= maxWidth || width <= ellipsisWidth) {
        return str;
    } else {
        var len = str.length;
        while (width >= maxWidth - ellipsisWidth && len-- > 0) {
            str = str.substring(0, len);
            width = c.measureText(str).width;
        }
        return str + ellipsis;
    }
}