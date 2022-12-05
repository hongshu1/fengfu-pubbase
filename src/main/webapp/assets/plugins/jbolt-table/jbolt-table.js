var jbolt_table_js_version="3.1.6";
var hasInitJBoltEditableTableKeyEvent=false;
var JBoltCurrentEditableAndKeyEventTable=null;
function clearJBoltCurrentEditableAndKeyEventTable(){
	JBoltCurrentEditableAndKeyEventTable=null;
}
function changeJBoltCurrentEditableAndKeyEventTable(table){
	JBoltCurrentEditableAndKeyEventTable=table;
}

/**
 * 表格选中行转为上一行
 * @param tableEle
 */
function jboltTableActivePrevTr(tableEle){
	var table=getJBoltTableInst(tableEle);
	if(!isOk(table)){
		LayerMsgBox.alert("表格配置异常，无法找到对应表格",2);
		return;
	}
	table.me.activePrevTr(table);
}

/**
 * 表格选中行转为下一行
 * @param tableEle
 */
function jboltTableActiveNextTr(tableEle){
	var table=getJBoltTableInst(tableEle);
	if(!isOk(table)){
		LayerMsgBox.alert("表格配置异常，无法找到对应表格",2);
		return;
	}
	table.me.activeNextTr(table);
}

/**
 * 获取指定行的json数据
 * @param tableEle
 * @param rowOrIndex
 * @returns {null|*}
 */
function jboltTableGetRowJsonData(tableEle,rowOrIndex){
	var table=getJBoltTableInst(tableEle);
	if(!isOk(table)){
		if(!dontShowError){
			LayerMsgBox.alert("表格配置异常，无法找到对应表格",2);
		}
		return null;
	}
	return table.me.getRowJsonData(table,rowOrIndex);
}
/**
 * 移除表格的指定id 的keep selected item
 * @param tableEle
 * @param removeId
 * @returns {null}
 */
function jboltTableRemoveKeepSelectedItem(tableEle,removeId){
	var table=getJBoltTableInst(tableEle);
	if(!isOk(table)){
		if(!dontShowError){
			LayerMsgBox.alert("表格配置异常，无法找到对应表格",2);
		}
		return null;
	}
	var ele = getRealJqueryObject(tableEle);
	if(ele[0].hasAttribute("tooltip")){
		disposeTooltip(ele);
	}
	table.me.removeFromKeepSelectedItemsBox(table,removeId);
}

/**
 * 获取所有keep selected 数据 json数据
 * @param tableEle
 * @param needAttrs
 * @param dontShowError
 * @returns {boolean|*}
 */
function jboltTableGetKeepSelectedDatas(tableEle,needAttrs,dontShowError){
	var table=getJBoltTableInst(tableEle);
	if(!isOk(table)){
		if(!dontShowError){
			LayerMsgBox.alert("表格配置异常，无法找到对应表格",2);
		}
		return null;
	}
	return table.me.getKeepSelectedDatas(table,needAttrs);
}

/**
 * 获取所有 keep selected数据ids
 * @param tableEle
 * @param dontShowError
 * @returns {boolean|*}
 */
function jboltTableGetKeepSelectedIds(tableEle,dontShowError){
	var table=getJBoltTableInst(tableEle);
	if(!isOk(table)){
		if(!dontShowError){
			LayerMsgBox.alert("表格配置异常，无法找到对应表格",2);
		}
		return null;
	}
	return table.me.getKeepSelectedIds(table);
}

/**
 * 获取所有 keep selected数据的显示文本texts
 * @param tableEle
 * @param dontShowError
 * @returns {boolean|*}
 */
function jboltTableGetKeepSelectedTexts(tableEle,dontShowError){
	var table=getJBoltTableInst(tableEle);
	if(!isOk(table)){
		if(!dontShowError){
			LayerMsgBox.alert("表格配置异常，无法找到对应表格",2);
		}
		return null;
	}
	return table.me.getKeepSelectedTexts(table);
}

/**
 * 处理完成可编辑表格当前正在编辑的单元格 完成回显
 * @param tableEle
 * @param dontProcessExtraSomthing
 */
function finishEditingCells(tableEle,dontProcessExtraSomthing){
	var table=getJBoltTableInst(tableEle);
	if(!isOk(table)){
		LayerMsgBox.alert("表格配置异常，无法找到对应表格",2);
		return false;
	}
	if(!table.editable){
		LayerMsgBox.alert("只要可编辑表格才能使用processEditingTds",2);
		return false;
	}
	return table.me.processEditingTds(table,dontProcessExtraSomthing);
}

/**
 * 检测可编辑表格选中的tr里的单元格required
 * @param tableEle
 * @returns {boolean}
 */
function checkEditableCheckedTrCellRequired(tableEle) {
	var table=getJBoltTableInst(tableEle);
	if(!isOk(table)){
		LayerMsgBox.alert("表格配置异常，无法找到对应表格",2);
		return false;
	}
	if(!table.editable){
		LayerMsgBox.alert("只要可编辑表格才能使用checkEditableCheckedTrCellRequired",2);
		return false;
	}
	return table.me.checkEditableCheckedTrCellRequired(table);
}

/**
 * 检测可编辑表格单元格required
 * @param tableEle
 * @param trs 非必填 限定tr范围
 * @returns {boolean}
 */
function checkEditableCellRequired(tableEle,trs) {
	var table=getJBoltTableInst(tableEle);
	if(!isOk(table)){
		LayerMsgBox.alert("表格配置异常，无法找到对应表格",2);
		return false;
	}
	if(!table.editable){
		LayerMsgBox.alert("只要可编辑表格才能使用checkEditableCellRequired",2);
		return false;
	}
	//处理还在编辑状态的单元格 回显
	table.me.processEditingTds(table);
	return table.me.checkEditableCellRequired(table,trs);
}

/**
 * 重新更改设置表格可编辑配置options
 * @param newOptions
 */
function changeJBoltTableEditableOptions(tableEle,newOptions){
	var table=getJBoltTableInst(tableEle);
	if(!isOk(table)){
		LayerMsgBox.alert("表格配置异常，无法找到对应表格",2);
		return false;
	}
	if(!table.editable){
		LayerMsgBox.alert("只要可编辑表格才能使用changeJBoltTableEditableOptions",2);
		return false;
	}
	if(!jsonObjectValueIsOk(newOptions)){
		LayerMsgBox.alert("请指定有效的配置options",2);
		return false;
	}
	Object.assign(table.editableOptions,newOptions);
	return true;
}
/**
 * 表格tr 上移
 * @param trEle
 * @param forceTrChange
 * @returns
 */
function jboltTableTrMoveUp(trEle,forceTrChange){
	var trEleObj = getRealJqueryObject(trEle);
	if(!isOk(trEleObj)){
		LayerMsgBox.alert("jboltTableTrMoveDown(元素)中的元素未找到",2);
		return false;
	}
	var isTr = trEleObj[0].tagName == "TR";
	var tr = isTr?trEleObj:(trEleObj.closest("tr"));
	if(!isOk(tr)){
		LayerMsgBox.alert("jboltTableTrMoveDown(元素)中的元素必须在tr中或者就是tr",2);
		return false;
	}
	var table = getJBoltTableInst(trEleObj);
	if(!isOk(table)){
		LayerMsgBox.alert("表格配置异常，无法找到对应表格",2);
		return false;
	}
	return table.me.moveUpRow(table,tr,forceTrChange);
}
/**
 * 表格tr 下移
 * @param trEle
 * @param forceTrChange
 * @returns
 */
function jboltTableTrMoveDown(trEle,forceTrChange){
	var trEleObj = getRealJqueryObject(trEle);
	if(!isOk(trEleObj)){
		LayerMsgBox.alert("jboltTableTrMoveDown(元素)中的元素未找到",2);
		return false;
	}
	var isTr = trEleObj[0].tagName == "TR";
	var tr = isTr?trEleObj:(trEleObj.closest("tr"));
	if(!isOk(tr)){
		LayerMsgBox.alert("jboltTableTrMoveDown(元素)中的元素必须在tr中或者就是tr",2);
		return false;
	}
	var table = getJBoltTableInst(trEleObj);
	if(!isOk(table)){
		LayerMsgBox.alert("表格配置异常，无法找到对应表格",2);
		return false;
	}
	return table.me.moveDownRow(table,tr,forceTrChange);
}
/**
 * jbolttable表格隐藏box
 * @param tableEle
 * @param boxType
 * @returns
 */
function jboltTableHideBox(tableEle,boxType){
	var table=getJBoltTableInst(tableEle);
	if(!isOk(table)){
		LayerMsgBox.alert("表格配置异常，无法找到对应表格",2);
		return false;
	}
	return table.me.hideTableBox(table,boxType);
}
/**
 * jbolttable表格隐藏headbox
 * @param tableEle
 * @returns
 */
function jboltTableHideHeadbox(tableEle){
	return jboltTableHideBox(tableEle,"headbox");
}
/**
 * jbolttable表格隐藏footBox
 * @param tableEle
 * @returns
 */
function jboltTableHideFootbox(tableEle){
	return jboltTableHideBox(tableEle,"footbox");
}
/**
 * jbolttable表格隐藏leftbox
 * @param tableEle
 * @returns
 */
function jboltTableHideLeftbox(tableEle){
	return jboltTableHideBox(tableEle,"leftbox");
}
/**
 * jbolttable表格隐藏rightbox
 * @param tableEle
 * @returns
 */
function jboltTableHideRightbox(tableEle){
	return jboltTableHideBox(tableEle,"rightbox");
}

/**
 * jbolttable表格显示box
 * @param tableEle
 * @param boxType
 * @returns
 */
function jboltTableShowBox(tableEle,boxType){
	var table=getJBoltTableInst(tableEle);
	if(!isOk(table)){
		LayerMsgBox.alert("表格配置异常，无法找到对应表格",2);
		return false;
	}
	return table.me.showTableBox(table,boxType);
}

/**
 * jbolttable表格显示 headbox
 * @param tableEle
 * @returns
 */
function jboltTableShowHeadbox(tableEle){
	return jboltTableShowBox(tableEle,"headbox");
}
/**
 * jbolttable表格显示 footbox
 * @param tableEle
 * @returns
 */
function jboltTableShowFootbox(tableEle){
	return jboltTableShowBox(tableEle,"footbox");
}
/**
 * jbolttable表格显示 leftbox
 * @param tableEle
 * @returns
 */
function jboltTableShowLeftbox(tableEle){
	return jboltTableShowBox(tableEle,"leftbox");
}
/**
 * jbolttable表格显示 rightbox
 * @param tableEle
 * @returns
 */
function jboltTableShowRightbox(tableEle){
	return jboltTableShowBox(tableEle,"rightbox");
}
/**
 * jbolttable表格 toggle切换 显示/隐藏box
 * @param tableEle
 * @param boxType
 * @returns
 */
function jboltTableToggleBox(tableEle,boxType){
	var table=getJBoltTableInst(tableEle);
	if(!isOk(table)){
		LayerMsgBox.alert("表格配置异常，无法找到对应表格",2);
		return false;
	}
	return table.me.toggleTableBox(table,boxType);
}

/**
 * jbolttable表格toggle headbox
 * @param tableEle
 * @returns
 */
function jboltTableToggleHeadbox(tableEle){
	return jboltTableToggleBox(tableEle,"headbox");
}
/**
 * jbolttable表格显示 footbox
 * @param tableEle
 * @returns
 */
function jboltTableToggleFootbox(tableEle){
	return jboltTableToggleBox(tableEle,"footbox");
}
/**
 * jbolttable表格显示 leftbox
 * @param tableEle
 * @returns
 */
function jboltTableToggleLeftbox(tableEle){
	return jboltTableToggleBox(tableEle,"leftbox");
}
/**
 * jbolttable表格显示 rightbox
 * @param tableEle
 * @returns
 */
function jboltTableToggleRightbox(tableEle){
	return jboltTableToggleBox(tableEle,"rightbox");
}
/**
 * 获取可编辑表格的可提交数据
 * 包含需要删除 需要更新 需要保存 和关联forms所有数据
 * @param tableEle
 * @returns
 */
function jboltTableGetSubmitData(tableEle){
	var table=getJBoltTableInst(tableEle);
	if(!isOk(table)){
		LayerMsgBox.alert("表格配置异常，无法找到对应表格",2);
		return false;
	}
	var canSubmit=table.me.checkCanSubmit(table);
	if(!canSubmit){return false;}
	return table.me.getSubmitData(table,true);
}
/**
 * 获取可编辑表格的可提交数据 多表格
 * 包含需要删除 需要更新 需要保存 和关联forms所有数据
 * @param tableEles
 * @returns
 */
function jboltTableGetSubmitDataMulti(tableEles){
	var tables=new Array();
	$.each(tableEles,function(i,ele){
		var table=getJBoltTableInst(ele);
		if(isOk(table)){
			tables.push(table);
		}
	});
	if(!isOk(tables)){
		LayerMsgBox.alert("表格配置异常，无法找到对应表格",2);
		return false;
	}
	var canSubmit=true;
	$.each(tables,function(i,table){
		canSubmit=table.me.checkCanSubmit(table);
		if(!canSubmit){return false;}
	});
	if(!canSubmit){return false;}
	return tables[0].me.getSubmitDatas(tables,true);
}
/**
 * 显示设置列的dialog组件
 * @returns
 */
function jboltTableShowColumnConfigDialog(tableEle){
	var table=getJBoltTable(tableEle);
	if(!isOk(table)){
		LayerMsgBox.alert("表格配置异常，无法找到对应表格",2);
		return false;
	}
	var jboltTable=table.jboltTable("inst");
	if(!jboltTable){
		LayerMsgBox.alert("表格配置异常，无法找到对应表格",2);
		return false;
	}
	//找找有没有定义这个字段
	var visibleColumnsConfig = jboltTable.visibleColumnsConfig;
	if(!isOk(visibleColumnsConfig)){
		visibleColumnsConfig = processJboltTableVisibleColumnsByThead(jboltTable);
	}
	if(!isOk(visibleColumnsConfig)){
		LayerMsgBox.alert("表格配置异常，thead中th 未设置data-column",2);
		return false;
	}

	var tpl = '<div class="jbolttable_vscolumns" style="padding:20px 20px 20px 30px;">'+
		'<div class="btn-group btn-group-sm text-center mb-3" role="group" aria-label="btn-group">'+
		'<button type="button" class="btn btn-outline-primary" onclick="CheckboxUtil.checkAll(\'visibleColumn\')">全选</button>'+
		'<button type="button" class="btn btn-outline-info" onclick="CheckboxUtil.uncheckAll(\'visibleColumn\')">全不选</button>'+
		'<button type="button" class="btn btn-outline-danger" onclick="CheckboxUtil.convertCheckAll(\'visibleColumn\')">反选</button>'+
		'</div><div class="form-group" data-checkbox data-rule="checkbox" data-name="visibleColumn" data-value="${checkedDatas}">{@each columns as column,index}<div class="checkbox checkbox-primary"><input  id="vs_${column.name}" type="checkbox" name="visibleColumn"   value="${column.index}"/><label for="vs_${column.name}">${column.text}</label></div>{@/each}</div></div>';
	var checkedDatas = [];
	for(var i in visibleColumnsConfig){
		if(!visibleColumnsConfig[i].hidden){
			checkedDatas.push(visibleColumnsConfig[i].index);
		}
	}
	var checkedStr="";
	if(isOk(checkedDatas)){
		checkedStr = checkedDatas.join(",");
	}
	var contentHtml =juicer(tpl,{columns:visibleColumnsConfig,checkedDatas:checkedStr});
	layer.open({
		type:1,
		title:"设置表格显示列",
		content:contentHtml,
		area:["300px","600px"],
		btnAlign:"c",
		btn:["确定设置","关闭窗口"],
		success:function(obj,index){
			CheckboxUtil.init(obj.find(".jbolttable_vscolumns"))
		},yes:function(index,obj){
			var colIndex=CheckboxUtil.getCheckedValue("visibleColumn",obj);
			if(!isOk(colIndex)){
				LayerMsgBox.alert("至少选择一列显示",2);
				return;
			}

			jboltTable.me.changeVisibleColumnsByColIndex(jboltTable,colIndex);
			layer.close(index)
		}
	});

	return true;
}

function processJboltTableVisibleColumnsByThead(jboltTable){
	var columns = jboltTable.thead.find("th[data-column][data-col-index]");
	if(!isOk(columns)){return null;}
	var visibleColumnsConfig=[];
	var col,text,name,index,checked;
	columns.each(function(){
		col = $(this);
		index = col.data("col-index");
		name = col.data("column")||("col_"+index);
		text = $.trim(col[0].innerText);
		hidden = col.is(":hidden");
		if(text){
			visibleColumnsConfig.push({name:name,index:index,text:text,hidden:hidden});
		}
	});
	if(isOk(visibleColumnsConfig)){
		jboltTable.visibleColumnsConfig = visibleColumnsConfig;
	}
	return visibleColumnsConfig;
}
/**
 * 表格菜单清空过滤条件
 * @param tableEle
 * @returns
 */
function jboltTableMenuClearFilter(tableEle){
	var table=getJBoltTable(tableEle);
	if(!isOk(table)){
		LayerMsgBox.alert("菜单配置异常，无法找到对应表格",2);
		return false;
	}
	var jboltTable=table.jboltTable("inst");
	if(!jboltTable){
		LayerMsgBox.alert("菜单配置异常，无法找到对应表格",2);
		return false;
	}
	jboltTable.menus.find(".jbolttable_filter_items").empty();
	return true;
}
/**
 * 获取单元格里选择的数据
 * @param td
 * @returns
 */
function jboltTableGetCellSelectText(td){
	var selectText = getSelectText();
	var text=$.trim(td[0].innerText);
	if(!text && !selectText){
		return null;
	}
	if(selectText && text.indexOf(selectText)!=-1){
		text = selectText;
	}

	return text;
}
/**
 * 主动提交表格绑定的查询表单
 * @param submitConditionsForm
 * @returns
 */
function jboltTableSubmitConditionsForm(tableEle){
	var table=getJBoltTable(tableEle);
	if(!isOk(table)){
		LayerMsgBox.alert("菜单配置异常，无法找到对应表格",2);
		return false;
	}
	var jboltTable=table.jboltTable("inst");
	if(!jboltTable){
		LayerMsgBox.alert("菜单配置异常，无法找到对应表格",2);
		return false;
	}
	return jboltTable.me.submitConditionsForm(jboltTable);
}
/**
 * 表格菜单 filterbox执行查询
 * @param tableEle
 * @returns
 */
function jboltTableMenuFilter(tableEle){
	var table=getJBoltTable(tableEle);
	if(!isOk(table)){
		LayerMsgBox.alert("菜单配置异常，无法找到对应表格",2);
		return false;
	}
	var jboltTable=table.jboltTable("inst");
	if(!jboltTable){
		LayerMsgBox.alert("菜单配置异常，无法找到对应表格",2);
		return false;
	}
	var needPaging  = isOk(table.data("page"));
	if(needPaging){
		var menuPage = jboltTable.menus.find("input[name='pageSize']");
		if(isOk(menuPage)){
			pageSize = menuPage.val();
		}
		if(!pageSize){
			var tempPageSize  =  table.data("pagesize");
			if(typeof(tempPageSize)=="undefined"){
				pageSize = 15;
			}else if(tempPageSize <=0){
				pageSize = 15;
			}else{
				pageSize = tempPageSize;
			}
		}
		jboltTable.data("conditions-pagesize",pageSize).attr("data-conditions-pagesize",pageSize);
	}
	var conditions = {isMenuFilter:true,paging:needPaging};
	var filteritems = jboltTable.menus.find(".jbolttable_filter_items>.jbolttable_filter_item");
	var itemsJson=new Array();
	if(isOk(filteritems)){
		var hasValue=false,filterItem,columnSelect,comparisonSelect,valueInput,column,type,comparison,value;
		filteritems.each(function(){
			filterItem=$(this);
			columnSelect = filterItem.find("select[name='column']");
			column = columnSelect.val();
			if(column){
				type = columnSelect.find("option:selected").data("type");
				comparisonSelect = filterItem.find("select[name='comparison']");
				comparison = comparisonSelect.val();
				valueInput = filterItem.find("[name='value'].is_current");
				if(valueInput.is("img")){
					value = valueInput.data("value");
					hasValue=true;
				}else{
					if(FormChecker.checkIt(valueInput)){
						value = $.trim(valueInput.val());
						if(value.toString().length>0){
							hasValue=true;
						}
					}
				}
				if(column&&comparison&&hasValue){
					itemsJson.push({column:column,type:type,comparison:comparison,value:value});
				}
			}
		});
	}
	conditions["filterItems"] = itemsJson;
	return jboltTable.me.readByConditions(jboltTable,conditions);
}

/**
 * 表格关键词内容过滤
 * @param tableEle
 * @param keywords
 * @param include
 * @param pageSize
 * @returns
 */
function jboltTableMenuFilterByKeywords(tableEle,keywords,include,pageSize){
	var table=getJBoltTable(tableEle);
	if(!isOk(table)){
		LayerMsgBox.alert("菜单配置异常，无法找到对应表格",2);
		return false;
	}
	var jboltTable=table.jboltTable("inst");
	if(!jboltTable){
		LayerMsgBox.alert("菜单配置异常，无法找到对应表格",2);
		return false;
	}
	var needPaging = pageSize > 0;
	if(!needPaging){
		needPaging = isOk(table.data("page"));
	}
	if(needPaging){
		var menuPage = jboltTable.menus.find("input[name='pageSize']");
		if(isOk(menuPage)){
			pageSize = menuPage.val();
		}
		if(!pageSize){
			var tempPageSize  =  table.data("pagesize");
			if(typeof(tempPageSize)=="undefined"){
				pageSize = 15;
			}else if(tempPageSize <= 0){
				pageSize = 15;
			}else{
				pageSize = tempPageSize;
			}
		}
		jboltTable.data("conditions-pagesize",pageSize).attr("data-conditions-pagesize",pageSize);
	}
	return jboltTable.me.readByConditions(jboltTable,{isMenuFilter:true,keywords:keywords,include:include,paging:needPaging});
}
/**
 * JBoltTable菜单增加筛选条件
 * @param tableEle
 * @param column
 * @param comparison
 * @param value
 * @returns
 */
function jboltTableMenuAddFilterItem(tableEle,column,comparison,value){
	var table=getJBoltTable(tableEle);
	if(!isOk(table)){
		LayerMsgBox.alert("菜单配置异常，无法找到对应表格",2);
		return false;
	}
	var jboltTable=table.jboltTable("inst");
	if(!jboltTable){
		LayerMsgBox.alert("菜单配置异常，无法找到对应表格",2);
		return false;
	}
	if(!jboltTable.menuFilterItemTpl){
		jboltTable.menuFilterItemTpl = jboltTable.menus.find(".jbolttable_filter_item_tpl");
	}
	var tplHtml=jboltTable.menuFilterItemTpl.html();
	var item=$("<div class='jbolttable_filter_item d-flex mb-1'></div>");
	item.html(tplHtml);
	jboltTable.menus.find(".jbolttable_filter_items").append(item);

	var columnSelect=item.find("select[name='column']");
	if(!column){
		if(jboltTable.currentMenuTd){
			var colIndex=jboltTable.currentMenuTd.data("col-index");
			if(colIndex>=0){
				var theadTh = jboltTable.thead.find("th[data-col-index='"+colIndex+"']");
				if(isOk(theadTh)){
					column=theadTh.data("column");
				}
			}
		}
	}
	var hasColumn = false;
	if(column&&column!="index"&&column!="optcol"){
		columnSelect.val(column);
		hasColumn=true;
	}
	var comparisonSelect=item.find("select[name='comparison']");
	//比较关系
	var selectedColumnOption,type;
	if(!comparison){
		if(hasColumn){
			selectedColumnOption=columnSelect.find("option:selected");
			type = selectedColumnOption.data("type");
			var columnSelectComparison = selectedColumnOption.data("comparison");
			if(!columnSelectComparison){

				switch(type){
					case 1://字符串
						comparison = "like";
						break;
					case 2://数字 int
						comparison = "eq";
						break;
					case 3://数字 小数
						comparison = "eq";
						break;
					case 4://日期
						comparison = "ge";
						break;
					case 5://日期+时间
						comparison = "ge";
						break;
					case 6://时间
						comparison = "ge";
						break;
					case 7://boolean
						comparison = "eq";
						break;
				}

			}else{
				comparison = columnSelectComparison;
			}
		}else{
			comparison = "eq";
		}
	}
	comparisonSelect.val(comparison);

	item.find("[name='value']").removeClass("is_current");
	if(!value && jboltTable.menuInTbody && jboltTable.currentMenuTd){
		var imgbtn = jboltTable.currentMenuTd.find("img[data-switchbtn]");
		if(isOk(imgbtn)){
			value = imgbtn.data("value");
		}else{
			value = $.trim(jboltTable.currentMenuTd.text());
		}
	}
	var valueInput;
	if(hasColumn && isOk(selectedColumnOption)){
		switch(type){
			case 1://字符串
				valueInput = item.find("input[name='value']");
				valueInput.attr("type","text").removeAttr("readonly").data("rule","required").attr("data-rule","required").val(value?value:"");
				break;
			case 2://数字 int
				valueInput = item.find("input[name='value']");
				valueInput.attr("type","number").removeAttr("readonly").data("rule","int").attr("data-rule","int").val(value?value:"");
				break;
			case 3://数字 小数
				valueInput = item.find("input[name='value']");
				valueInput.attr("type","number").removeAttr("readonly").data("rule","number").attr("data-rule","number").val(value?value:"");
				break;
			case 4://日期
				valueInput = item.find("input[name='value']");
				valueInput.attr("type","text").data("type","date").attr("data-type","date").val(value?value:"");
				FormDate.initDate(valueInput);
				break;
			case 5://日期+时间
				valueInput = item.find("input[name='value']");
				valueInput.attr("type","text").data("type","datetime").attr("data-type","datetime").val(value?value:"");
				FormDate.initDate(valueInput);
				break;
			case 6://时间
				valueInput = item.find("input[name='value']");
				valueInput.attr("type","text").data("type","time").attr("data-type","time").val(value?value:"");
				FormDate.initDate(valueInput);
				break;
			case 7://boolean
				valueInput = item.find("img[name='value']");
				if(typeof(value)=="boolean"){
					valueInput.data("value",value).attr("data-value",value);
				}else{
					valueInput.data("value",value=="true").attr("data-value",value=="true");
				}
				SwitchBtnUtil.initBtn(valueInput);
				break;
		}

	}else{
		valueInput = item.find("input[name='value']");
		valueInput.attr("type","text").removeAttr("readonly").data("rule","required").attr("data-rule","required").val(value?value:"");
	}
	valueInput.addClass("is_current");

	jboltTableMenuOffsetTopChange(jboltTable);

	return true;
}

function jboltTableMenuOffsetTopChange(tableEle){
	var table=getJBoltTable(tableEle);
	if(!isOk(table)){
		return false;
	}
	var jboltTable=table.jboltTable("inst");
	if(!jboltTable){
		return false;
	}
	var me = jboltTable.menus.find(".dropdown-menu");
	var menuHeight = parseInt(me.css("height"));
	if(menuHeight>0){
		var cm_pos=jboltTable.menus.offset();
		if(cm_pos.top + menuHeight > jboltWindowHeight){
			cm_pos.top = jboltWindowHeight - menuHeight - 30;
		}
		jboltTable.menus.css(cm_pos);
	}
}
function getCurrentEditableAndKeyEventJBoltTables(){
	var pbox;
	if(jboltWithTabs){
		pbox=JBoltTabUtil.getCurrentTabContent();
	}else{
		pbox=jboltBody;
	}
	var tables=pbox.find("table[data-jbolttable][data-editable='true'].jbolt_main_table:not([data-shortcutkey-disabled='true'])");
	if(!isOk(tables)){
		return false;
	}
	var jboltTableTmp,tableArray=new Array();
	tables.each(function(){
		jboltTableTmp=$(this).jboltTable("inst");
		if(jboltTableTmp&&jboltTableTmp.editable){
			tableArray.push(jboltTableTmp);
		}
	});
	return tableArray;
}
function getCurrentEditableAndKeyEventJBoltTable(){
	if(!JBoltCurrentEditableAndKeyEventTable){
		var pbox;
		if(jboltWithTabs){
			pbox=JBoltTabUtil.getCurrentTabContent();
		}else{
			pbox=jboltBody;
		}
		var table=pbox.find("table[data-jbolttable][data-editable='true'][data-editable-focus='true'].jbolt_main_table:not([data-shortcutkey-disabled='true']):eq(0)");
		if(!isOk(table)){
			table=pbox.find("table[data-jbolttable][data-editable='true'].jbolt_main_table:not([data-shortcutkey-disabled='true']):eq(0)");
		}
		if(isOk(table)){
			var jboltTable=table.jboltTable("inst");
			if(jboltTable&&jboltTable.editable){
				JBoltCurrentEditableAndKeyEventTable=jboltTable;
			}
		}
	}
	return JBoltCurrentEditableAndKeyEventTable;
}
/**
 * 设置表格单元格数据
 * @param tableEle 表格或者关联元素 能找到表格的ele
 * @param tr       修改的单元格所在行
 * @param column   列名
 * @param text     显示文本
 * @param value    值
 * @param dontExeValueChangeHandler   改变单元格的值 但是不触发任何changeColumns和handler事件
 * @returns
 */
function jboltTableSetCell(tableEle,tr,column,text,value,dontExeValueChangeHandler){
	var table=getJBoltTable(tableEle);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			return jboltTable.me.setCell(jboltTable,tr,column,text,value,dontExeValueChangeHandler);
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}
/**
 * 设置表格单元格是否可编辑
 * @param tableEle 表格或者关联元素 能找到表格的ele
 * @param tr       修改的单元格所在行
 * @param column   列名
 * @param editable     是否可编辑
 * @param falseClear    如果false是否清空数据
 * @param clearValue    如果false并且清空的话 使用什么值清空 如果设置的 就赋值这个 不设置就直接空
 * @param dontExeValueChangeHandler   改变单元格的值 但是不触发任何changeColumns和handler事件
 * @returns
 */
function jboltTableSetCellEditable(tableEle,tr,column,editable,falseClear,clearValue,dontExeValueChangeHandler){
	var table=getJBoltTable(tableEle);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			return jboltTable.me.setCellEditable(jboltTable,tr,column,editable,falseClear,clearValue,dontExeValueChangeHandler);
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}
/**
 * 批量设置表格单元格是否可编辑
 * @param tableEle 表格或者关联元素 能找到表格的ele
 * @param tr       修改的单元格所在行
 * @param column   列名
 * @param columnsJsonData 数组格式 [{column:"colname",falseClear:"否清空数据",clearValue:"用什么值替换清空"}]
 * @param dontExeValueChangeHandler   改变单元格的值 但是不触发任何changeColumns和handler事件
 * @returns
 */
function jboltTableSetCellsEditable(tableEle,tr,columnsJsonData,dontExeValueChangeHandler){
	var table=getJBoltTable(tableEle);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			var columnJson,success=false;
			for(var i in columnsJsonData){
				columnJson=columnsJsonData[i];
				success=jboltTable.me.setCellEditable(jboltTable,tr,columnJson.column,columnJson.editable,columnJson.falseClear,columnJson.clearValue,dontExeValueChangeHandler);
				if(!success){
					return false;
				}
			}
			return true;
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}

/**
 * 设置表格单元格数据
 * @param tableEle 表格或者关联元素 能找到表格的ele
 * @param tr       修改的单元格所在行
 * @param columnsJsonData 数组格式 [{column:"colname",text:"显示文本",value:"值"}]
 * @param dontExeValueChangeHandler 改变单元格的值 但是不触发任何changeColumns和handler事件
 * @returns
 */
function jboltTableSetCells(tableEle,tr,columnsJsonData,dontExeValueChangeHandler){
	var table=getJBoltTable(tableEle);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			var columnJson,success=false;
			for(var i in columnsJsonData){
				columnJson=columnsJsonData[i];
				success=jboltTable.me.setCell(jboltTable,tr,columnJson.column,columnJson.text,columnJson.value,dontExeValueChangeHandler);
				if(!success){
					return false;
				}
			}
			return true;
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}
/**
 * 关闭jbolttable的菜单
 * @param ele
 * @returns
 */
function closeJBoltTableMenu(ele){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			return jboltTable.me.closeMenu(jboltTable);
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}
/**
 * 方向键left处理
 * @param table
 * @param e
 * @returns
 */
function jboltEditableFocusTurnLeftByDir(table,e){
	if(!table.isEditableLock){return false;}
	var that=table.me;
	var trigger=table.editableOptions.trigger;
	var ele=$(e.target);
	if(ele.hasClass("jbt_editor")){
		var td=ele.closest("td");
		that.changeTdFocus(table,td);
		if(e.ctrlKey){
			e.preventDefault();
			e.stopPropagation();
			var prevTd=table.tbody.focusTd.prevTd;
			if(prevTd){
				that.changeTdFocus(table,prevTd);
				prevTd.trigger(trigger);
			}
		}
	}else{
		e.preventDefault();
		e.stopPropagation();
		var td=table.tbody.focusTd;
		if(!isOk(td)){
			that.changeTdFocus(table);
			td=table.tbody.focusTd;
			td.trigger(trigger);
		}else{
			var prevTd=table.tbody.focusTd.prevTd;
			if(prevTd){
				that.changeTdFocus(table,prevTd);
				prevTd.trigger(trigger);
			}
		}
	}
}
/**
 * 方向键down处理
 * @param table
 * @param e
 * @returns
 */
function jboltEditableFocusTurnDownByDir(table,e){
	if(!table.isEditableLock){return false;}
	var that=table.me;
	var trigger=table.editableOptions.trigger;
	var ele=$(e.target);
	//可编辑td里组件上
	if(ele.hasClass("jbt_editor")){
		var td=ele.closest("td");
		that.changeTdFocus(table,td);
		if(e.ctrlKey){
			e.preventDefault();
			e.stopPropagation();
			if(e.altKey){
				jboltEditableFocusTurnTableByDir(table,false);
			}else{
				var nextTr=td.parent().next();
				if(isOk(nextTr)){
					var nextTd=nextTr.find("td[data-col-index='"+td.data("col-index")+"']");
					if(nextTd){
						that.changeTdFocus(table,nextTd);
						nextTd.trigger(trigger);
					}
				}
			}
		}

	}else{
		e.preventDefault();
		e.stopPropagation();
		if(e.altKey){
			jboltEditableFocusTurnTableByDir(table,false);
		}else{
			var td=table.tbody.focusTd;
			if(!isOk(td)){
				that.changeTdFocus(table);
				td=table.tbody.focusTd;
				td.trigger(trigger);
			}else{
				var nextTr=td.parent().next();
				if(isOk(nextTr)){
					var nextTd=nextTr.find("td[data-col-index='"+td.data("col-index")+"']");
					if(nextTd){
						that.changeTdFocus(table,nextTd);
						nextTd.trigger(trigger);
					}
				}
			}
		}
	}
}
/**
 * 多实例情况 焦点转移到上一个或者下一个表格
 * @returns
 */
function jboltEditableFocusTurnTableByDir(currentTable,up){
	var tables=getCurrentEditableAndKeyEventJBoltTables();
	if(isOk(tables)){
		var size=tables.length;
		var tableId=currentTable.attr("id");
		var jump=false;
		$.each(tables,function(i,table){
			if(table.attr("id")==tableId){
				if(up&&i>0){
					changeJBoltCurrentEditableAndKeyEventTable(tables[i-1]);
					jump=true;
					return false;
				}

				if(!up&&i<size-1){
					changeJBoltCurrentEditableAndKeyEventTable(tables[i+1]);
					jump=true;
					return false;
				}
			}
		});
		if(jump){
			JBoltCurrentEditableAndKeyEventTable.isEditableLock=true;
			JBoltCurrentEditableAndKeyEventTable.me.processEditingTds(JBoltCurrentEditableAndKeyEventTable);
			var td=JBoltCurrentEditableAndKeyEventTable.tbody.focusTd;
			if(isOk(td)){
				JBoltCurrentEditableAndKeyEventTable.me.changeTdFocus(JBoltCurrentEditableAndKeyEventTable,td);
			}else{
				JBoltCurrentEditableAndKeyEventTable.me.changeTdFocus(JBoltCurrentEditableAndKeyEventTable);
				td=JBoltCurrentEditableAndKeyEventTable.tbody.focusTd;
			}
			td.trigger(JBoltCurrentEditableAndKeyEventTable.editableOptions.trigger);
		}
	}
}

/**
 * 方向键up处理
 * @param table
 * @param e
 * @returns
 */
function jboltEditableFocusTurnUpByDir(table,e){
	if(!table.isEditableLock){return false;}
	var that=table.me;
	var trigger=table.editableOptions.trigger;
	var ele=$(e.target);
	if(ele.hasClass("jbt_editor")){
		var td=ele.closest("td");
		that.changeTdFocus(table,td);
		if(e.ctrlKey){
			e.preventDefault();
			e.stopPropagation();
			if(e.altKey){
				jboltEditableFocusTurnTableByDir(table,true);
			}else{
				var prevTr=td.parent().prev();
				if(isOk(prevTr)){
					var prevTd=prevTr.find("td[data-col-index='"+td.data("col-index")+"']");
					if(prevTd){
						that.changeTdFocus(table,prevTd);
						prevTd.trigger(trigger);
					}
				}
			}
		}

	}else{
		e.preventDefault();
		e.stopPropagation();
		if(e.altKey){
			jboltEditableFocusTurnUpTableByDir(table,true);
		}else{
			var td=table.tbody.focusTd;
			if(!isOk(td)){
				that.changeTdFocus(table);
				td=table.tbody.focusTd;
				td.trigger(trigger);
			}else{
				var prevTr=td.parent().prev();
				if(isOk(prevTr)){
					var prevTd=prevTr.find("td[data-col-index='"+td.data("col-index")+"']");
					if(prevTd){
						that.changeTdFocus(table,prevTd);
						prevTd.trigger(trigger);
					}
				}
			}
		}
	}
}
/**
 * 方向键right处理
 * @param table
 * @param e
 * @returns
 */
function jboltEditableFocusTurnRightByDir(table,e){
	if(!table.isEditableLock){return false;}
	var that=table.me;
	var trigger=table.editableOptions.trigger;
	var ele=$(e.target);
	//可编辑td里组件上 回车或者tab 自动处理数据回填
	if(ele.hasClass("jbt_editor")){
		var td=ele.closest("td");
		that.changeTdFocus(table,td);
		if(e.ctrlKey){
			e.preventDefault();
			e.stopPropagation();
			var nextTd=table.tbody.focusTd.nextTd;
			if(nextTd){
				that.changeTdFocus(table,nextTd);
				//处理是否需要跳转焦点到右侧
				var success=that.processFocusChangeToExtraForm(table,td);
				if(success){
					table.isEditableLock=false;
					return false;
				}
				nextTd.trigger(trigger);
			}
		}
	}else{
		e.preventDefault();
		e.stopPropagation();
		var td=table.tbody.focusTd;
		if(!isOk(td)){
			that.changeTdFocus(table);
			td=table.tbody.focusTd;
			td.trigger(trigger);
		}else{
			var nextTd=table.tbody.focusTd.nextTd;
			if(nextTd){
				that.changeTdFocus(table,nextTd);
				nextTd.trigger(trigger);
			}
		}
	}
}

/**
 * 列批量赋值
 * @param tableEle
 * @param columnsJsonData {column:xxx,text:xxx,value:xxx}
 * @param dontExeValueChangeHandler
 * @returns
 */
function jboltTableBatchSetColumns(tableEle,columnsJsonData,dontExeValueChangeHandler){
	if(isOk(columnsJsonData)){
		var table=getJBoltTable(tableEle);
		if(isOk(table)){
			var jboltTable=table.jboltTable("inst");
			if(jboltTable){
				jboltTable.me.processEditingTds(jboltTable);
				var trs = jboltTable.tbody.find("tr[data-index]");
				if(isOk(trs)){
					var tr,columnJson;
					trs.each(function(){
						tr=$(this);
						for(var i in columnsJsonData){
							columnJson=columnsJsonData[i];
							jboltTable.me.setCell(jboltTable,tr,columnJson.column,columnJson.text,columnJson.value,dontExeValueChangeHandler);
						}
					});
					return;
				}
				return;
			}
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
}
/**
 * 列批量赋值
 * @param tableEle
 * @param text
 * @param value
 * @param dontExeValueChangeHandler
 * @returns
 */
function jboltTableBatchSetColumn(tableEle,column,text,value,dontExeValueChangeHandler){
	if(column){
		var table=getJBoltTable(tableEle);
		if(isOk(table)){
			var jboltTable=table.jboltTable("inst");
			if(jboltTable){
				jboltTable.me.processEditingTds(jboltTable);
				var trs = jboltTable.tbody.find("tr[data-index]");
				if(isOk(trs)){
					var tr;
					trs.each(function(){
						tr=$(this);
						jboltTable.me.setCell(jboltTable,tr,column,text,value,dontExeValueChangeHandler);
					});
					return;
				}
				return;
			}
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
}
/**
 * 列批量赋值
 * @param tableEle
 * @param useTd
 * @param dontExeValueChangeHandler
 * @returns
 */
function jboltTableColumnBatchAssignUseTd(tableEle,useTd,dontExeValueChangeHandler){
	var table=getJBoltTable(tableEle);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			if(confirm("确认使用此单元格数据批量赋值此列吗？")){
				jboltTable.me.processEditingTd(jboltTable,useTd);
				var text = useTd.data("text")||"";
				var value = useTd.data("value")||"";
				var colIndex= useTd.data("col-index");
				var trIndex= useTd.closest("tr").data("index");
				var theadTh=jboltTable.thead.find("tr>th[data-col-index='"+colIndex+"']");
				var column = useTd.data("column")||(isOk(theadTh)?theadTh.data("column"):"");
				if(column){
					var trs = jboltTable.tbody.find("tr[data-index!='"+trIndex+"']");
					if(isOk(trs)){
						var tr;
						trs.each(function(){
							tr=$(this);
							jboltTable.me.setCell(jboltTable,tr,column,text,value,dontExeValueChangeHandler);
						});
						return;
					}
				}
			}
			return;
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
}
/**
 * 回车键处理
 * @param e
 * @returns
 */
function jboltEditableEnterKeyProcess(jboltTable,e){
	//按了alt键 alt Enter或者ctrl alt enter
	if(e.altKey&&!e.shiftKey){
		e.preventDefault();
		e.stopPropagation();
		if(e.ctrlKey){
			//如果按键是ctrl+alt+回车键 弹出提示 是否批量设置列值
			var td=jboltTable.tbody.focusTd;
			if(isOk(td)){
				jboltTableColumnBatchAssignUseTd(jboltTable,td);
			}
		}else{
			//如果按键是alt+回车键 判断执行选中
			jboltTable.isEditableLock=true;
			jboltTable.me.processEditingTds(jboltTable);
			var td=jboltTable.tbody.focusTd;
			if(isOk(td)){
				jboltTable.me.changeTdFocus(jboltTable,td);
			}else{
				jboltTable.me.changeTdFocus(jboltTable);
				td=jboltTable.tbody.focusTd;
			}
			td.trigger(jboltTable.editableOptions.trigger);
		}
	}else if(!e.altKey&&!e.shiftKey&&!e.ctrlKey){
		var ele=$(e.target);
		//如果是内置textarea 就不做任何操作 使用默认行为
		if(ele.hasClass("jbt_editor") && ele.is("TEXTAREA")){
			e.stopPropagation();
			return false;
		}
		//ctrl alt shift都没按 只按enter
		//那就判断当前元素
		processEditableKeyEventDefaultEditorOk(jboltTable,e);
	}
}
/**
 * 处理直接按enter和tab的默认行为
 * @param table
 * @param e
 * @returns
 */
function processEditableKeyEventDefaultEditorOk(table,e){
	if(!table.isEditableLock){return false;}
	var that=table.me;
	var trigger=table.editableOptions.trigger;
	var ele=$(e.target);
	//可编辑td里组件上 回车或者tab 自动处理数据回填
	if(ele.hasClass("jbt_editor")){
		e.preventDefault();
		e.stopPropagation();
		var td=ele.closest("td");
		that.changeTdFocus(table,td);
		var nextTd=table.tbody.focusTd.nextTd;
		if(nextTd){
			that.changeTdFocus(table,nextTd);
			//处理是否需要跳转焦点到右侧
			var success=that.processFocusChangeToExtraForm(table,td);
			if(success){
				table.isEditableLock=false;
			}else{
				nextTd.trigger(trigger);
			}
		}else{
			//处理是否需要跳转焦点到右侧
			var success=that.processFocusChangeToExtraForm(table,td);
			if(success){
				table.isEditableLock=false;
				that.processCreateNewEmptyTrAndFocusFirstEditableTd(table,td.parent());
			}else{
				that.processCreateNewEmptyTrAndFocusFirstEditableTd(table,td.parent(),trigger);
			}
		}
	}else{
		if(isOk(ele.closest("table.jbolt_table"))){
			e.preventDefault();
			e.stopPropagation();
			var td=table.tbody.focusTd;
			if(isOk(td)){
				var nextTd=td.nextTd;
				if(nextTd){
					that.changeTdFocus(table,nextTd);
					nextTd.trigger(trigger);
				}else{
					that.processCreateNewEmptyTrAndFocusFirstEditableTd(table,td.parent(),trigger);
				}
			}else{
				that.changeTdFocus(table);
				td=table.tbody.focusTd;
				td.trigger(trigger);
			}
		}else{
			//处理还在编辑状态的单元格 回显
			that.processEditingTds(table);
		}
	}
}
/**
 * tab处理
 * @param jboltTable
 * @param e
 * @returns
 */
function jboltEditableTabKeyProcess(jboltTable,e){
	//ctrl alt shift都没按 只按tab
	if(!e.ctrlKey&&!e.altKey&&!e.shiftKey){
		//那就判断当前元素
		processEditableKeyEventDefaultEditorOk(jboltTable,e);
	}
}
/**
 * 初始化 表格键盘事件处理
 * @returns
 */
function initJBoltEditableKeyEvent(){
	if(hasInitJBoltEditableTableKeyEvent){
		return;
	}
	hasInitJBoltEditableTableKeyEvent = true;
	var that=this;
	//点击body 只要不在tableview里 就执行可编辑表格的解锁 还原 清空焦点处理
	jboltBody.on("click",function(e){
		var ee=$(e.target);
		var inview=ee.closest(".jbolt_table_view");
		if(!isOk(inview)){
			//如果点了不是表格内区域的话 就要去掉当前引用表格等待下次唤起
			clearJBoltCurrentEditableAndKeyEventTable();
			var jboltTables=getCurrentEditableAndKeyEventJBoltTables();
			if(jboltTables){
				$.each(jboltTables,function(i,table){
					//解锁
					table.isEditableLock=false;
					//还原
					table.me.processEditingTds(table,true);
					//清空焦点
					table.me.clearTdFocus(table);
					table.data("editable-focus",false).attr("data-editable-focus",false);
				});
			}
		}
	});
	jboltBody.on("keydown",function(e){
		var currentTable=getCurrentEditableAndKeyEventJBoltTable();
		if(currentTable){
			switch(e.which){
				case 37://左方向
					jboltEditableFocusTurnLeftByDir(currentTable,e);
					break;
				case 38://上方向
					jboltEditableFocusTurnUpByDir(currentTable,e);
					break;
				case 39://右方向
					jboltEditableFocusTurnRightByDir(currentTable,e);
					break;
				case 40://下方向
					jboltEditableFocusTurnDownByDir(currentTable,e);
					break;
				case 13://回车
					jboltEditableEnterKeyProcess(currentTable,e);
					break;
				case 9://tab
					jboltEditableTabKeyProcess(currentTable,e);
					break;
			}
		}
	});

}
/**
 * Jbolt-table组件的封装
 */
function checkTableBodyHasScrollBar(table,direction){
	var ele=table.table_body[0];
	if(direction=='v'){
		return Math.abs(ele.offsetWidth - ele.clientWidth)>0;
	}else if(direction=='h'){
		return Math.abs(ele.offsetHeight - ele.clientHeight)>0;
	}
}

/**
 * 表格辅助录入数据save到当前编辑tr上
 * @param formEle
 * @param confirm
 * @param dontProcessIfNotExistActiveTr
 * @returns
 */
function jboltTableSaveFormToTableCurrentActiveTr(formEle,confirm,dontProcessIfNotExistActiveTr){
	var form=getRealJqueryObject(formEle);
	if(!isOk(form)){
		LayerMsgBox.alert("未找到有效的Form表单");
		return false;
	}
	if(FormChecker.check(form)){
		var table=getJBoltTable(form);
		if(isOk(table)){
			var jboltTable=table.jboltTable("inst");
			if(jboltTable){
				return jboltTable.me.saveFormToTableCurrentActiveTr(jboltTable,form,confirm,dontProcessIfNotExistActiveTr);
			}
		}
		LayerMsgBox.alert("表格组件配置异常",2);
	}
	return false;
}

/**
 * 复制选中行 并且前插
 * @param ele
 * @returns
 */
function jboltTableCopyCheckedRowInsertBefore(ele){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			return jboltTable.me.copyCheckedRowInsertBefore(jboltTable);
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}
/**
 * 复制选中行 并且指定插入
 * @param ele
 * @returns
 */
function jboltTableCopyCheckedRowPrepend(ele){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			return jboltTable.me.copyCheckedRowPrepend(jboltTable);
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}
/**
 * 复制选中行 并且后插
 * @param ele
 * @returns
 */
function jboltTableCopyCheckedRowInsertAfter(ele){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			return jboltTable.me.copyCheckedRowInsertAfter(jboltTable);
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}
/**
 * 复制选中行 并且插入到最后
 * @param ele
 * @returns
 */
function jboltTableCopyCheckedRowAppend(ele){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			return jboltTable.me.copyCheckedRowAppend(jboltTable);
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}
/**
 * 前端快速过滤
 * @param ele
 * @param keywords
 * @param colIndexArr 指定列执行过滤
 * @returns
 */
function jboltTableFilterByKeywords(ele,keywords,colIndexArr){
	if(!((event && event.which==13) ||  !keywords)){
		return false;
	}
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			return jboltTable.me.filterByKeywords(jboltTable,keywords,colIndexArr);
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}
/**
 * JBolt表格treetable 所有节点全部展开
 * @param ele
 * @returns
 */
function jboltTableExpandAll(ele){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			return jboltTable.me.expandAll(jboltTable);
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}
/**
 * JBolt表格treetable 所有节点全部闭合
 * @param ele
 * @returns
 */
function jboltTableCollapseAll(ele){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			return jboltTable.me.collapseAll(jboltTable);
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}
/**
 * 可编辑表格里Autocomplete_with_dialog选择数据后处理
 * @param text
 * @param value
 * @param data
 * @returns
 */
function jboltTableProcessTdByDialogChooser(text,value,data){
	var layerDialog=DialogUtil.getCurrent();
	if(!isOk(layerDialog)){
		LayerMsgBox.alert("配置异常 无法获取有效的Dialog组件",2);
		return false;
	}
	var editingTd=layerDialog.data("link-editable-td");
	if(!isOk(editingTd)){
		LayerMsgBox.alert("配置异常 无法获取有效的表格TD单元格",2);
		return false;
	}

	var table=editingTd.closest(".jbolt_table_view").find(".jbolt_table_box>.jbolt_table_body>table[data-jbolttable].jbolt_main_table");
	if(!isOk(table)){
		LayerMsgBox.alert("配置异常 无法获取有效的JBoltTable组件",2);
		return false;
	}
	var jboltTable=table.jboltTable("inst");
	if(!jboltTable){
		LayerMsgBox.alert("配置异常 无法获取有效的JBoltTable组件",2);
		return false;
	}
	var vchandler=editingTd.data("dialog-choose-choose-handler");
	if(vchandler&&typeof(vchandler)=="function"){
		var jsonData=jboltTable.tableListDatas[editingTd.closest("tr").data("index")];
		var ischangecolumns = editingTd.data("jbolttable-handler-ischangecolumns");
		if(!ischangecolumns){
			jboltTable.me.processColConfigChangeColumns(jboltTable,editingTd,data);
		}
		vchandler(jboltTable,editingTd,text,value,jsonData,data);
	}
	jboltTable.me.processEditableTdChooseData(jboltTable,editingTd,text,value,true);
}
/**
 * 打开dialog选择数据 并批量插入
 * @param ele
 * @returns
 */
function jboltTableChooseAndInsert(ele,confirm,multi){
	var action=getRealJqueryObject(ele);
	if(!isOk(action)){LayerMsgBox.alert("参数异常",2);return false;}
	var tableId=action.data("jbolt-table-id");
	if(tableId){
		table=$("#"+tableId);
	}else{
		table=action.closest(".jbolt_table_view").find(".jbolt_table_box>.jbolt_table_body>table[data-jbolttable].jbolt_main_table");
	}
	if(!isOk(table)){
		LayerMsgBox.alert("配置异常 未绑定任何JBoltTable组件",2);
		return false;
	}
	var jboltTable=table.jboltTable("inst");
	if(!jboltTable){
		LayerMsgBox.alert("配置异常 无法获取有效的JBoltTable组件",2);
		return false;
	}
	DialogUtil.openBy(action);
}
/**
 * 弹出选择器选择数据后提交
 * @param action
 * @param datas
 * @param limitChecked
 * @param insertToBefore
 * @param keepId
 * @param dontProcessChange
 * @param forceTrChange
 * @returns
 */
function jboltTableInsertRowsByDialogChooser(action,datas,insertType,keepId,dontProcessChange,forceTrChange){
	if(!isOk(action)){LayerMsgBox.alert("actionEle 参数异常",2);return false;}
	if(!datas){
		LayerMsgBox.alert("请选择至少一行数据",2);
		return false;
	}
	if(isArray(datas)&&datas.length==0){
		LayerMsgBox.alert("请选择至少一行数据",2);
		return false;
	}

	var tableId=action.data("jbolt-table-id");
	if(tableId){
		table=$("#"+tableId);
	}else{
		table=action.closest(".jbolt_table_view").find(".jbolt_table_box>.jbolt_table_body>table[data-jbolttable].jbolt_main_table");
	}
	if(!isOk(table)){
		LayerMsgBox.alert("actionEle 配置异常 未绑定任何JBoltTable组件",2);
		return false;
	}
	var result=false;
	if(insertType){
		switch(insertType){
			case "prepend":
				result=jboltTablePrependRow(table,datas,keepId,dontProcessChange,forceTrChange);
				break;
			case "append":
				result=jboltTableAppendRow(table,datas,keepId,dontProcessChange,forceTrChange);
				break;
			case "before":
				result=jboltTableInsertRowBeforeChecked(table,datas,keepId,dontProcessChange,forceTrChange);
				break;
			case "after":
				result=jboltTableInsertRowAfterChecked(table,datas,keepId,dontProcessChange,forceTrChange);
				break;
			case "replace":
				result=jboltTableReplaceCheckedRow(table,datas,true,keepId,dontProcessChange,forceTrChange);
				break;
			case "merge":
				result=jboltTableReplaceCheckedRow(table,datas,false,keepId,dontProcessChange,forceTrChange);
				break;
			default:
				result=jboltTableInsertRow(table,datas,keepId,dontProcessChange,forceTrChange);
				break;
		}
	}else{
		result = jboltTableInsertRow(table,datas,keepId,dontProcessChange,forceTrChange);
	}
	return result;
}

/**
 * 表格所在区域最大化
 * @param ele
 * @returns
 */
function jboltTableMaximize(ele){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			var result=jboltTable.me.maximize(jboltTable);
			if(result&&ele.tagName!="TABLE"){
				if(jboltTable.table_view.hasClass("maximize")){
					ele.innerHTML='<i class="fa fa-window-maximize"></i> 恢复';
				}else{
					ele.innerHTML='<i class="fa fa-window-maximize"></i> 最大化';
				}
			}

			return result;
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;

}

/**
 * 使用json conditions出发表格重新查询加载数据
 * @param ele 绑定有data-table-id属性的组件或者tableId 字符串等
 * @param conditions
 * @returns
 */
function jboltTableReadByConditions(ele,conditions){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			return jboltTable.me.readByConditions(jboltTable,conditions);
		}
	}
	return false;
}

/**
 * 提交可编辑表格
 * @param ele 表格id或者元素对象
 * @returns
 */
function jboltTableSubmit(ele){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			return jboltTable.me.submit(jboltTable);
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}
/**
 * 提交可编辑表格 多表格
 * @param ele 表格id或者元素对象 数组
 * @param url url地址
 * @param successCallback 成功回调
 * @param failCallback 失败回调
 * @returns
 */
function jboltTableSubmitMulti(arr,url,successCallback,failCallback){
	var tables=new Array();
	$.each(arr,function(i,ele){
		var table=getJBoltTableInst(ele);
		if(isOk(table)){
			tables.push(table);
		}
	});
	if(isOk(tables)){
		return tables[0].me.submitMulti(tables,url,successCallback,failCallback);
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}
/**
 * 是否全选
 * @param ele
 * @returns
 */
function jboltTableIsCheckedAll(ele){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			return jboltTable.me.isCheckboxCheckedAll(jboltTable);
		}
	}
	return false;
}
/**
 * 全选所有checkbox
 * @param ele
 * @returns
 */
function jboltTableCheckAll(ele){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			CheckboxUtil.checkAll("jboltTableCheckbox",jboltTable.table_view);
		}
	}
}
/**
 * 取消全选所有checkbox
 * @param ele
 * @returns
 */
function jboltTableUncheckAll(ele){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			CheckboxUtil.uncheckAll("jboltTableCheckbox",jboltTable.table_view);
		}
	}
}
/**
 * 反选所有checkbox
 * @param ele
 * @returns
 */
function jboltTableConvertCheckAll(ele){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			CheckboxUtil.convertCheckAll("jboltTableCheckbox",jboltTable.table_body);
			CheckboxUtil.convertCheckAll("jboltTableCheckbox",jboltTable.fixedColumnTables);
			var checkboxs=jboltTable.table_view.find("thead tr>th input[type='checkbox'][name='jboltTableCheckbox']");
			if(isOk(checkboxs)){
				if(jboltTableIsCheckedAll(ele)){
					CheckboxUtil.checkIt(checkboxs);
				}else{
					CheckboxUtil.uncheckIt(checkboxs);
				}
			}
		}
	}
}


/**
 * 删除选中的行
 * @param ele
 * @param confirm
 * @param callback
 * @returns
 */
function removeJBoltTableCheckedTr(ele,confirm,callback){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			var action=getRealJqueryObject(ele);
			if(isOk(action)){
				var success = checkMasterTableId(action);
				if(!success){
					return false;
				}
			}
			jboltTable.me.removeJBoltTableCheckedTr(jboltTable,confirm,callback);

		}
	}
	return true;
}


/**
 * 检测jbolttable是否选中了一条数据数据
 * 返回id
 * @param table
 * @param dontShowError
 * @returns
 */
function jboltTableGetCheckedId(ele,dontShowError){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			if(ele&&typeof(ele)!="string"){
				var msg,eleObj=getRealJqueryObject(ele),eleit=eleObj[0];
				if(isOk(eleObj)&&eleit.tagName!='TABLE'){
					if(eleit.hasAttribute("data-nomsg")){
						dontShowError=true;
					}else{
						if(eleit.hasAttribute("data-check-errormsg")){
							msg = eleit.getAttribute("data-check-errormsg");
						}
					}
				}
			}
			return jboltTable.me.getCheckedId(jboltTable,dontShowError,msg);
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}

/**
 * 检测jbolttable是否选中了一条数据数据
 * 返回Json
 * @param ele
 * @param needAttrs
 * @param dontShowError
 * @returns
 */
function jboltTableGetCheckedData(ele,needAttrs,dontShowError){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			return jboltTable.me.getCheckedData(jboltTable,needAttrs,dontShowError);
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}
/**
 * 检测jbolttable是否选中并返回所有id
 * @param table
 * @param dontShowError
 * @returns
 */
function jboltTableGetCheckedIds(ele,dontShowError){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			return jboltTable.me.getCheckedIds(jboltTable,dontShowError);
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}
/**
 * 检测jbolttable是否选中并返回所有指定的column
 * @param table
 * @param column
 * @param dontShowError
 * @returns
 */
function jboltTableGetCheckedCols(ele,column,dontShowError){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			return jboltTable.me.getCheckedCols(jboltTable,column,dontShowError);
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}
/**
 * 检测jbolttable是否选中并返回所有data-text
 * @param table
 * @param dontShowError
 * @returns
 */
function jboltTableGetCheckedTexts(ele,dontShowError){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			return jboltTable.me.getCheckedTexts(jboltTable,dontShowError);
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}
/**
 * jbolttable设置选中checkbox
 * @param ele
 * @param ids
 * @returns
 */
function jboltTableSetCheckedIds(ele,ids){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			return jboltTable.me.setCheckedIds(jboltTable,ids);
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}
/**
 * jbolttable设置选中checkbox
 * @param ele
 * @param id
 * @returns
 */

function jboltTableSetCheckedId(ele,id){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			return jboltTable.me.setCheckedId(jboltTable,id);
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}

/**
 * 删除选中的行
 * @param ele
 * @param confirm
 * @param callback
 * @returns
 */
function jboltTableRemoveCheckedRow(ele,confirm,callback){
	var action=getRealJqueryObject(ele);
	if(isOk(action)){
		var success = checkMasterTableId(action);
		if(!success){
			return false;
		}
	}
	var isCheckedNone=jboltTableIsCheckedNone(ele);
	if(isCheckedNone){
		LayerMsgBox.alert("请至少选择一行数据",2);
	}else{
		var ids=jboltTableGetCheckedIds(ele,true);
		if(ids){
			LayerMsgBox.confirm("所选数据中包含数据库已存数据，确定删除吗？",function(){
				removeJBoltTableCheckedTr(ele,false,callback);
			});
		}else{
			removeJBoltTableCheckedTr(ele,confirm,callback);
		}
		return true;

	}
	return false;

}

/**
 * tbody最后添加空行
 * @param ele
 * @param forceTrChange
 * @returns
 */
function jboltTableAppendEmptyRow(ele,forceTrChange){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			var action=getRealJqueryObject(ele);
			if(isOk(action)){
				var success = checkMasterTableId(action);
				if(!success){
					return false;
				}
			}
			if(!jboltTable.isEmpty){
				var tr=jboltTable.tbody.find("tr:last");
				if(isOk(tr)){
					return jboltTable.me.insertEmptyRow(jboltTable,tr,false,forceTrChange);
				}
			}
			return jboltTable.me.insertEmptyRow(jboltTable,null,false,forceTrChange);
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}

/**
 * tbody最前添加空行
 * @param ele
 * @param forceTrChange
 * @returns
 */
function jboltTablePrependEmptyRow(ele,forceTrChange){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			var action=getRealJqueryObject(ele);
			if(isOk(action)){
				var success = checkMasterTableId(action);
				if(!success){
					return false;
				}
			}
			if(!jboltTable.isEmpty){
				var tr=jboltTable.tbody.find("tr:first");
				if(isOk(tr)){
					return jboltTable.me.insertEmptyRow(jboltTable,tr,true,forceTrChange);
				}
			}
			return jboltTable.me.insertEmptyRow(jboltTable,null,false,forceTrChange);
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}
/**
 * 检测是否需要检查masterId
 * @param action
 * @returns
 */
function checkMasterTableId(actionEle){
	var action=getRealJqueryObject(actionEle);
	var needCheckMaster = action.data("check-master")||false;
	if(needCheckMaster){
		var masterId = action.data("master-id");
		if(!masterId){
			LayerMsgBox.alert("请先选择主表数据",2);
			return false;
		}
	}
	return true;
}
/**
 * 插入一空行
 * 默认在特定组件所在tr后面 如果没有的话 就找第一个空白tr
 * @param ele
 * @param forceTrChange 强制行改变状态
 * @returns
 */
function jboltTableInsertEmptyRow(ele,forceTrChange){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			var action=getRealJqueryObject(ele),tr;
			if(isOk(action)){
				var success = checkMasterTableId(action);
				if(!success){
					return false;
				}
				//如果是在表格里触发的
				if(isOk(action.closest("table[data-jbolttable]"))){
					tr = action.closest("tr");
				}else if(isOk(action.closest(".jbolt_table_fixed"))){
					var fixtr = action.closest("tr");
					var fixTrIndex = fixtr.index();
					tr=jboltTable.tbody.find("tr:nth-child("+(fixTrIndex+1)+")");
				}
			}
			return jboltTable.me.insertEmptyRow(jboltTable,tr,false,forceTrChange);
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}



/**
 * 选中行后插入一行
 * @param ele
 * @param forceTrChange
 * @returns
 */
function jboltTableInsertEmptyRowAfterChecked(ele,forceTrChange){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			var tr=jboltTable.me.getCheckedTr(jboltTable);
			if(isOk(tr)){
				var action=getRealJqueryObject(ele);
				if(isOk(action)){
					var success = checkMasterTableId(action);
					if(!success){
						return false;
					}
				}
				return jboltTable.me.insertEmptyRow(jboltTable,tr,false,forceTrChange);
			}
			return false;
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}
/**
 * 选中行前插入一行
 * @param ele
 * @param forceTrChange
 * @returns
 */
function jboltTableInsertEmptyRowBeforeChecked(ele,forceTrChange){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			var tr=jboltTable.me.getCheckedTr(jboltTable);
			if(isOk(tr)){
				var action=getRealJqueryObject(ele);
				if(isOk(action)){
					var success = checkMasterTableId(action);
					if(!success){
						return false;
					}
				}
				return jboltTable.me.insertEmptyRow(jboltTable,tr,true,forceTrChange);
			}
			return false;
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}



/**
 * 插入带数据行 data可以是一条也可以是一个数组
 * 默认找到空行第一个插入
 * @param ele  表格元素或者选择器
 * @param data 数据
 * @param keepId 是否保持新插入数据里的ID
 * @param dontProcessChange 是否处理行和列的标红change
 * @param forceTrChange 强制本行标红提交
 * @returns
 */
function jboltTableInsertRow(ele,data,keepId,dontProcessChange,forceTrChange){
	if(!isOk(data)){
		return jboltTableInsertEmptyRow(ele,forceTrChange);
	}
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			var action=getRealJqueryObject(ele),tr;
			if(isOk(action)){
				var success = checkMasterTableId(action);
				if(!success){
					return false;
				}
				//如果是在表格里触发的
				if(isOk(action.closest("table[data-jbolttable]"))){
					tr = action.closest("tr");
				}else if(isOk(action.closest(".jbolt_table_fixed"))){
					var fixtr = action.closest("tr");
					var fixTrIndex = fixtr.index();
					tr=jboltTable.tbody.find("tr:nth-child("+(fixTrIndex+1)+")");
				}
			}
			if(typeof(dontProcessChange)=="undefined"){
				dontProcessChange = false;
				if(keepId){
					dontProcessChange=true;
				}
			}
			return jboltTable.me.insertRows(jboltTable,data,tr,false,keepId,dontProcessChange,forceTrChange);
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}

/**
 * Tbody最后append数据行
 * @param ele
 * @param data
 * @param keepId
 * @param dontProcessChange
 * @param forceTrChange
 * @returns
 */
function jboltTableAppendRow(ele,data,keepId,dontProcessChange,forceTrChange){
	if(!isOk(data)){
		return jboltTableAppendEmptyRow(ele,forceTrChange);
	}
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			var action=getRealJqueryObject(ele);
			if(isOk(action)){
				var success = checkMasterTableId(action);
				if(!success){
					return false;
				}
			}
			if(typeof(dontProcessChange)=="undefined"){
				dontProcessChange = false;
				if(keepId){
					dontProcessChange=true;
				}
			}
			if(!jboltTable.isEmpty){
				var tr=jboltTable.tbody.find("tr:last");
				if(isOk(tr)){
					return jboltTable.me.insertRows(jboltTable,data,tr,false,keepId,dontProcessChange,forceTrChange);
				}
			}
			return jboltTable.me.insertRows(jboltTable,data,null,false,keepId,dontProcessChange,forceTrChange);
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}

/**
 * tbody最前添加数据行
 * @param ele                可以找到表格的属性、元素等
 * @param keepId             是否保留数据里的ID
 * @param dontProcessChange  不处理tr td标红处理
 * @param forceTrChange      强制tr标红 dontProcessChange不是true才有效
 * @returns
 */
function jboltTablePrependRow(ele,data,keepId,dontProcessChange,forceTrChange){
	if(!isOk(data)){
		return jboltTablePrependEmptyRow(ele,forceTrChange);
	}
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			var action=getRealJqueryObject(ele);
			if(isOk(action)){
				var success = checkMasterTableId(action);
				if(!success){
					return false;
				}
			}
			if(typeof(dontProcessChange)=="undefined"){
				dontProcessChange = false;
				if(keepId){
					dontProcessChange=true;
				}
			}
			if(!jboltTable.isEmpty){
				var tr=jboltTable.tbody.find("tr:first");
				if(isOk(tr)){
					return jboltTable.me.insertRows(jboltTable,data,tr,true,keepId,dontProcessChange,forceTrChange);
				}
			}
			return jboltTable.me.insertRows(jboltTable,data,null,false,keepId,dontProcessChange,forceTrChange);
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}

/**
 * 在选择数据后插入带数据行 data可以是一条也可以是一个数组
 * @param ele
 * @param data
 * @param keepId
 * @param dontProcessChange
 * @param forceTrChange
 * @returns
 */
function jboltTableInsertRowAfterChecked(ele,data,keepId,dontProcessChange,forceTrChange){
	if(!isOk(data)){
		return jboltTableInsertEmptyRowAfterChecked(ele,forceTrChange);
	}
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			var action=getRealJqueryObject(ele);
			if(isOk(action)){
				var success = checkMasterTableId(action);
				if(!success){
					return false;
				}
			}
			var tr=jboltTable.me.getCheckedTr(jboltTable);
			if(isOk(tr)){
				if(typeof(dontProcessChange)=="undefined"){
					dontProcessChange = false;
					if(keepId){
						dontProcessChange=true;
					}
				}
				return jboltTable.me.insertRows(jboltTable,data,tr,false,keepId,dontProcessChange,forceTrChange);
			}
			return false;
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}

/**
 * 直接替换选中数据行 data只能是一个数据
 * @param ele
 * @param data
 * @param replaceAllData 数据是否全部清空替换 否则是merge合并数据
 * @param keepId
 * @param dontProcessChange
 * @param forceTrChange
 * @returns
 */
function jboltTableReplaceCheckedRow(ele,data,replaceAllData,keepId,dontProcessChange,forceTrChange){
	if(!isOk(data)){
		LayerMsgBox.alert("请选择数据",2);
		return false;
	}
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			var action=getRealJqueryObject(ele);
			if(isOk(action)){
				var success = checkMasterTableId(action);
				if(!success){
					return false;
				}
			}
			var tr=jboltTable.me.getCheckedTr(jboltTable);
			if(isOk(tr)){
				if(typeof(dontProcessChange)=="undefined"){
					dontProcessChange = false;
					if(keepId){
						dontProcessChange=true;
					}
				}
				return jboltTable.me.replaceRow(jboltTable,data,tr,replaceAllData,keepId,dontProcessChange,forceTrChange);
			}
			return false;
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}
/**
 * 选中行前插入一行 带数据
 * @param ele
 * @param data
 * @param keepId
 * @param dontProcessChange
 * @param forceTrChange
 * @returns
 */
function jboltTableInsertRowBeforeChecked(ele,data,keepId,dontProcessChange,forceTrChange){
	if(!isOk(data)){
		return jboltTableInsertEmptyRowBeforeChecked(ele,forceTrChange);
	}
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			var action=getRealJqueryObject(ele);
			if(isOk(action)){
				var success = checkMasterTableId(action);
				if(!success){
					return false;
				}
			}
			var tr=jboltTable.me.getCheckedTr(jboltTable);
			if(isOk(tr)){
				if(typeof(dontProcessChange)=="undefined"){
					dontProcessChange = false;
					if(keepId){
						dontProcessChange=true;
					}
				}
				return jboltTable.me.insertRows(jboltTable,data,tr,true,keepId,dontProcessChange,forceTrChange);
			}
			return false;
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}












/**
 * 删除所在行
 * @param ele
 * @returns
 */
function jboltTableRemoveRow(ele){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		if(!table.data("editable")){
			LayerMsgBox.alert("表格组件配置异常 data-editable=\"true\"",2);
			return false;
		}
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			var action=getRealJqueryObject(ele);
			if(isOk(action)){
				var success = checkMasterTableId(action);
				if(!success){
					return false;
				}
			}
			if(isOk(action)&&(isOk(action.closest("table[data-jbolttable]"))||isOk(action.closest(".jbolt_table_fixed")))){
				var tr=action.closest("tr");
//				var tempid=tr.data("tempid");
				var id=tr.data("id");
				if(id){
					LayerMsgBox.confirm("此行数据并非新加临时数据，已经存在数据库中，确认删除？",function(){
						jboltTable.me.removeRow(jboltTable,tr.data("index"));
					});
				}else{
					jboltTable.me.removeRow(jboltTable,tr.data("index"));
				}
				return true;
			}
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}

/**
 * 得到表格所有指定列的数据
 * @param ele              表格选择器
 * @param attrName         哪一列 列名
 * @param withBlankDatas   是否带着空值 默认不带
 * @returns
 */
function getJboltTableOneColumnDatas(tableId,attrName,withBlankDatas){
	return jboltTableGetOneColumnDatas(tableId,attrName,withBlankDatas);
}
/**
 * 得到表格所有JSON数据
 * @param tableId
 * @returns
 */
function getJboltTableAllDatas(tableId,needAttrs){
	return jboltTableGetAllDatas(tableId,needAttrs);
}
/**
 * 清空表格
 * @param tableId
 * @returns
 */
function clearJboltTable(tableId){
	return jboltTableClear(tableId);
}
/**
 * 得到checkbox|radio选中的id
 * @param tableId
 * @param dontShowError
 * @returns
 */
function getJboltTableCheckedId(tableId,dontShowError){
	return jboltTableGetCheckedId(tableId,dontShowError);
}
/**
 * checkbox|radio设置选中的id
 * @param tableEle
 * @param id
 * @returns
 */
function setJboltTableCheckedId(tableEle,id){
	return jboltTableSetCheckedId(tableEle,id);
}

/**
 * 得到checkbox|radio选中的ids
 * @param tableId
 * @param dontShowError
 * @returns
 */
function getJboltTableCheckedIds(tableId,dontShowError){
	return jboltTableGetCheckedIds(tableId,dontShowError);
}
/**
 * 得到checkbox|radio选中的 指定column
 * @param tableId
 * @param column
 * @param dontShowError
 * @returns
 */
function getJboltTableCheckedCols(tableId,column,dontShowError){
	return jboltTableGetCheckedCols(tableId,column,dontShowError);
}
/**
 * 得到checkbox|radio选中的 text
 * @param tableId
 * @param column
 * @param dontShowError
 * @returns
 */
function getJboltTableCheckedTexts(tableId,column,dontShowError){
	return jboltTableGetCheckedTexts(tableId,column,dontShowError);
}

/**
 * checkbox|radio设置选中的ids
 * @param tableEle
 * @param ids
 * @returns
 */
function setJboltTableCheckedIds(tableEle,ids){
	return jboltTableSetCheckedIds(tableEle,ids);
}
/**
 * 得到checkbox|radio选中的json数据
 * @param tableId
 * @param needAttrs
 * @returns
 */
function getJboltTableCheckedDatas(tableId,needAttrs){
	return jboltTableGetCheckedDatas(tableId,needAttrs);
}


/**
 * 得到checkbox|radio选中的单行json数据
 * @param tableId
 * @param needAttrs
 * @returns
 */
function getJboltTableCheckedData(tableId,needAttrs){
	return jboltTableGetCheckedData(tableId,needAttrs);
}


/**
 * 检测jbolttable是否一个没选
 * @param table
 * @returns
 */
function jboltTableIsCheckedNone(ele){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			return jboltTable.me.isCheckedNone(jboltTable);
		}
	}
	return false;
}
function getJBoltTableInst(ele){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		return table.jboltTable("inst");
	}
	return null;
}

function getJBoltTable(ele){
	var table;
	if(ele){
		//ele可能是绑定表格的按钮也可能是表格ID字符串
		table=getRealJboltTableByEle(ele);
	}else{
		var pbox;
//		if(isWithtabs()){
		if(jboltWithTabs){
			pbox=JBoltTabUtil.getCurrentTabContent();
		}else{
			pbox=jboltBody;
		}
		table=pbox.find(".jbolt_table_view>.jbolt_table_box>.jbolt_table_body>table[data-jbolttable].jbolt_main_table");
	}
	return table;
}


function getRealJboltTableByEle(ele){
	var action=getRealJqueryObject(ele);
	var table;
	if(isOk(action)){
		if(action[0].tagName=="TABLE" && action.hasClass("jbolt_table")){
			table=action;
		}else{
			var tableId=action.data("jbolt-table-id");
			if(tableId){
				table=$("#"+tableId);
			}else{
				table=action.closest("table.jbolt_table");
				//如果存在 就判断是不是fixed
				if(isOk(table)){
					if(!table.hasClass(".jbolt_main_table")){
						//不是主table
						var tableBox=table.closest(".jbolt_table_box");
						if(isOk(tableBox)){
							table=tableBox.find(".jbolt_main_table");
						}
					}
				}else{
					table=action.closest(".jbolt_table_view").find(".jbolt_table_box>.jbolt_table_body>table[data-jbolttable].jbolt_main_table");
				}
			}
		}

	}
	return table;
}
/**
 * 得到选中的行数
 * @param table
 * @returns
 */
function jboltTableGetCheckedCount(ele){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			return jboltTable.me.getCheckedCount(jboltTable);
		}
	}
	return 0;
}
/**
 * 得到选中的行数
 * @param ele
 * @param needAttrs 需要什么字段
 * @param dontShowError
 * @returns
 */
function jboltTableGetCheckedDatas(ele,needAttrs,dontShowError){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			return jboltTable.me.getCheckedDatas(jboltTable,needAttrs,dontShowError);
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}
/**
 * 得到表格所有行数据
 * @param ele              表格选择器
 * @param attrName         哪一列 列名
 * @param withBlankDatas   是否带着空值 默认不带
 * @returns
 */
function jboltTableGetOneColumnDatas(ele,attrName,withBlankDatas){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			return jboltTable.me.getOneColumnDatas(jboltTable,attrName,withBlankDatas);
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}

/**
 * 得到表格所有行数据
 * @param ele
 * @param needAttrs 需要什么字段
 * @returns
 */
function jboltTableGetAllDatas(ele,needAttrs){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			return jboltTable.me.getAllDatas(jboltTable,needAttrs);
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}
/**
 * 清空表格所有行数据
 * @param ele
 * @returns
 */
function jboltTableClear(ele){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			return jboltTable.me.clear(jboltTable);
		}
	}
	LayerMsgBox.alert("表格组件配置异常",2);
	return false;
}
/**
 * 重置slave区域
 * @param masterTable
 * @returns
 */
function resetJBolttableSlaveBox(masterTable){
	var box=masterTable.closest(".jbolttable_master_slave_box");
	if(!isOk(box)){return false;}
	var master=masterTable.closest(".split.master");
	if(!isOk(master)){return false;}
	var id=0;
	var slave=box.find(".split.slave");
	if(!isOk(slave)){return false;}
	var elm,elsrcurl;
	slave.find("[data-masterid],input[type='hidden'][data-masterid-hidden='true'],[data-url][data-origin-url],[data-url][data-orign-url],[href][data-origin-url],[href][data-orign-url],[href][data-orignurl],[data-url][data-srcurl],[data-url][data-src-url],[href][data-srcurl],[href][data-src-url]").each(function(){
		elm=$(this);
		if(elm.is("input") && elm.attr("type")=="hidden" && elm.data("masterid-hidden")){
			elm.val(id).change();
		}else{
			elsrcurl=elm.data("origin-url")||elm.data("orign-url")||elm.data("orignurl")||elm.data("srcurl")||elm.data("src-url");
			if(elsrcurl && elsrcurl.indexOf("[masterId]")!=-1){
				elsrcurl=elsrcurl.replaceAll("[masterId]",id);
				if(elm.data("url")){
					elm.data("url",elsrcurl).attr("data-url",elsrcurl);
				}
				if(elm.attr("href")){
					elm.attr("href",elsrcurl);
				}
			}else{
				elsrcurl = elm.data("masterid");
				if(elsrcurl && elsrcurl == "[masterId]"){
					elm.data("masterid",id).attr("data-masterid",id);
				}
			}
		}
	});
	var tables=slave.find(".jbolt_main_table");
	if(isOk(tables)){
		var tt;//,tableSrcUrl,tableUrl;
		tables.each(function(){
			tt=$(this).jboltTable("inst");
//			tableSrcUrl=tt.data("orign-url")||tt.data("srcurl");
//			tableUrl=tableSrcUrl.replaceAll("[masterId]",id);
//			tt.data("url",tableUrl).data("data-url",tableUrl);
			tt.data("inited",false);
			tt.me.readByPage(tt,1,function(){
				tt.data("inited",true);
			});
		});
	}
	var protals=slave.find("[data-ajaxportal]");
	if(isOk(protals)){
//		var srcUrl,portal,url;
		var portal;
		protals.each(function(){
			portal=$(this);
//			srcUrl=portal.data("orign-url")||portal.data("srcurl");
//			url=srcUrl.replaceAll("[masterId]",id);
			portal.ajaxPortal(true,portal.data("url"),true);
		});
	}

}
/**
 * 主从样式 主表点击 驱动从表区域table和ajaxportal加载
 * @param tr
 * @param id
 * @param masterOtherParams
 * @param tableCallback
 * @param ajaxPortalCallback
 * @returns
 */
function masterTableTrTriggerShowSlave(ele,id,masterOtherParams,tableCallback,ajaxPortalCallback){
	var tr=$(ele);
	var box=tr.closest(".jbolttable_master_slave_box");
	if(!isOk(box)){return false;}
	var trDataIndex=tr.data("index");
	var masterTable=tr.closest(".jbolt_table_view").find(".jbolt_main_table");
	masterTable.find("tbody>tr.active").removeClass("active");
	masterTable.find("tbody>tr[data-index='"+trDataIndex+"']").addClass("active");
	var masterJboltTable=masterTable.jboltTable("inst");
	if(masterJboltTable&&isOk(masterJboltTable.fixedColumnTables)){
		var fixtable,tempTr;
		masterJboltTable.fixedColumnTables.each(function(){
			fixtable=$(this);
			fixtable.find("tbody>tr.active").removeClass("active");
			tempTr=fixtable.find("tbody>tr[data-index='"+trDataIndex+"']");
			if(isOk(tempTr)){
				tempTr.addClass("active");
			}
		});
	}
	var slave=box.find(".split.slave");
	if(!isOk(slave)){return false;}

	var elm,elsrcurl;
	slave.find("[data-masterid],input[type='hidden'][data-masterid-hidden='true'],[data-url][data-origin-url],[data-url][data-orign-url],[href][data-origin-url],[href][data-orign-url],[href][data-orignurl],[data-url][data-srcurl],[data-url][data-src-url],[href][data-srcurl],[href][data-src-url]").each(function(){
		elm=$(this);
		if(elm.is("input") && elm.attr("type")=="hidden" && elm.data("masterid-hidden")){
			elm.val(id).change();
		}else{
			elsrcurl=elm.data("origin-url")||elm.data("orign-url")||elm.data("orignurl")||elm.data("srcurl")||elm.data("src-url");
			if(elsrcurl.indexOf("[masterId]")!=-1){
				elsrcurl=elsrcurl.replaceAll("[masterId]",id);
				if(elm.data("url")){
					elm.data("url",elsrcurl).attr("data-url",elsrcurl);
				}
				if(elm.attr("href")){
					elm.attr("href",elsrcurl);
				}
			}else{
				elsrcurl = elm.data("masterid");
				if(elsrcurl && elsrcurl == "[masterId]"){
					elm.data("masterid",id).attr("data-masterid",id);
				}
			}
		}
	});
	masterData = masterJboltTable.tableListDatas[trDataIndex];
	var tables=slave.find(".jbolt_main_table");
	if(isOk(tables)){
		var tt,masterData;//,tableSrcUrl,tableUrl;
		tables.each(function(){
			tt=$(this).jboltTable("inst");
			tt.data("master-id",id).attr("data-master-id",id);
			if(tt.toolbar){
				tt.toolbar.find("a,button,input,select").data("master-id",id).attr("data-master-id",id);
			}
			tt.data("inited",false);
			if(tableCallback){
				tt.me.readByPage(tt,1,function(ctable){
					tt.data("inited",true);
					if(typeof(tableCallback)=="function"){
						tableCallback(ctable,masterJboltTable,masterData,masterOtherParams);
					}else if(typeof(tableCallback)=="string"){
						var execall_handler=eval(tableCallback);
						if(execall_handler&&typeof(execall_handler)=="function"){
							execall_handler(ctable,masterJboltTable,masterData,masterOtherParams);
						}
					}
				});
			}else{
				tt.me.readByPage(tt,1,function(){
					tt.data("inited",true);
				});
			}

		});
	}
	var protals=slave.find("[data-ajaxportal]");
	if(isOk(protals)){
//		var srcUrl,portal,url;
		var portal;
		protals.each(function(){
			portal=$(this);
			portal.data("master-id",id).attr("data-master-id",id);
			if(ajaxPortalCallback){
				portal.ajaxPortal(true,portal.data("url"),true,function(){
					if(typeof(ajaxPortalCallback)=="function"){
						ajaxPortalCallback(portal,masterJboltTable,masterData,masterOtherParams);
					}else if(typeof(ajaxPortalCallback)=="string"){
						var aexecall_handler=eval(ajaxPortalCallback);
						if(aexecall_handler&&typeof(aexecall_handler)=="function"){
							aexecall_handler(portal,masterJboltTable,masterData,masterOtherParams);
						}
					}
				});
			}else{
				portal.ajaxPortal(true,portal.data("url"),true);
			}

		});
	}



}
/**
 * treeTable中tr下移 处理都带着下级
 * @param table
 * @param tr
 * @returns
 */
function moveDownTreeTableRow(table,tr){
	//得到下面的一个
	var nextTr=tr.next();
	if(!isOk(nextTr)){
		return false;
	}

	var tbody=tr.closest("tbody");
	var downArray=new Array();
	processTreeTableTrAllNodes(downArray,tbody,tr);
	var currentLastTr=(downArray.length==1)?tr:downArray[downArray.length-1];
	nextTr=currentLastTr.next();
	if(!isOk(nextTr)){
		return false;
	}

	var upArray=new Array();
	processTreeTableTrAllNodes(upArray,tbody,nextTr);
	var lastUpTr=(upArray.length==1)?nextTr:upArray[upArray.length-1];
	trChangeToDown(downArray,lastUpTr,table);

}
/**
 * treeTable中tr上移 处理都带着下级
 * @param table
 * @param tr
 * @returns
 */
function moveUpTreeTableRow(table,tr){
	//得到上面的一个
	var prevTr=tr.prev();
	if(!isOk(prevTr)){
		return false;
	}
	var prevTrId=prevTr.data("id");
	var trPid=tr.data("pid");
	if(prevTrId==trPid){
		//如果上一个已经到了自己的爸爸 就不处理了
		return false;
	}
	var lastPrevTr=tr.prevAll("[data-pid='"+trPid+"']:first");
	if(isOk(lastPrevTr)){
		var tbody=tr.closest("tbody");
		var upArray=new Array();
		processTreeTableTrAllNodes(upArray,tbody,tr);
		trChangeToUp(upArray,lastPrevTr);
	}
}
/**
 * 递归得到左右子tr
 * @param upArray
 * @param tbody
 * @param tr
 * @returns
 */
function processTreeTableTrAllNodes(upArray,tbody,tr){
	upArray.push(tr);
	var isParent=tr[0].hasAttribute("data-parent") || tr.hasClass("hasItems");
	if(isParent){//如果是一个
		var sons=tbody.find("tr[data-pid='"+tr.data("id")+"']");
		if(isOk(sons)){
			var size=sons.length;
			for(var i=0;i<size;i++){
				processTreeTableTrAllNodes(upArray,tbody,sons.eq(i));
			}
		}
	}
}
/**
 * 删除一行以及所有下级
 * @param table
 * @param tr
 * @returns
 */
function deleteTreeTableRow(table,tr){
	var isParent=tr[0].hasAttribute("data-parent");
	var isSon=tr[0].hasAttribute("data-son");
	var tbody=tr.closest("tbody");
	if(isParent){
		var sons=tbody.find("tr[data-pid='"+tr.data("id")+"']");
		if(isOk(sons)){
			var size=sons.length;
			for(var i=0;i<size;i++){
				deleteTreeTableRow(table,sons.eq(i))
			}
		}
	}
	tr.remove();
	if(isSon){
		var pid=tr.data("pid");
		if(pid){
			var leaveSons=tbody.find("tr[data-pid='"+pid+"']");
			//还有其他子节点 就不处理了 如果没有子节点了就处理pid
			if(!isOk(leaveSons)){
				var ptr=tbody.find("tr[data-id='"+pid+"']");
				if(isOk(ptr)){
					ptr.removeAttr("data-parent");
					ptr.find("td[data-parent-td='true']").removeAttr('data-parent-td').find("i.fa.parent_flag").remove();
				}
			}
		}
	}
}
/**
 * 删除table
 * @param ele
 * @param confirm
 * @returns
 */
function jboltTableRemove(ele,confirm){
	removeJBoltTable(ele,confirm);
}
/**
 * 刷新table
 * @param ele
 * @param confirm
 * @returns
 */
function jboltTableRefresh(ele,confirm,refreshEditableOptions){
	refreshJBoltTable(ele,confirm,refreshEditableOptions);
}
/**
 * 刷新tables 多个
 * @param eles
 * @param confirm
 * @returns
 */
function jboltTableRefreshs(eles,confirm,refreshEditableOptions){
	refreshJBoltTables(eles,confirm,refreshEditableOptions);
}

/**
 * 更新额外数据
 * @param table
 * @param tr
 * @param data
 * @param columns
 * @returns
 */
function jboltTableUpdateOtherColumns(table,tr,data,columns){
	if(!isOk(columns)){
		return false;
	}
	var size=columns.length;
	var tempTheadTh,onlyHandler=false,column,changeColumn,willChangeValue=false,hasColumnValue=false,columnName,columnType,handler,editable=null,editableType,editableBy,useArr,camelColumn,colConfig,text,value,handlerResult,colIndex=-1,tempTd,changeTextAttr,changeValueAttr;
	for(var i=0;i<size;i++){
		column=columns[i];
		columnType=typeof(column);
		onlyHandler=false;
		if(columnType=="string"){
			columnName=column;
			camelColumn=StrUtil.camel(columnName);
			changeTextAttr=camelColumn;
			changeValueAttr=camelColumn;
			hasColumnValue=false;
			willChangeValue=true;
		}else if(columnType=="object"){
			editable=(typeof(column.editable)!=undefined)?column.editable:null;
			columnName=column.column;
			camelColumn=StrUtil.camel(columnName);
			handler=column.handler;
			if(column.use){
				if(column.use.indexOf(":")!=-1){
					useArr=column.use.split(":");
					if(isOk(useArr)){
						changeTextAttr=useArr[0];
						changeValueAttr=useArr[1];
					}else{
						LayerMsgBox.alert("changeColumns配置中的use属性 格式:  text 或者 text:value",2);
						return false;
					}
				}else{
					changeTextAttr=column.use;
					changeValueAttr=column.use;
				}
				hasColumnValue=false;
				willChangeValue=true;
			}else if(column.asText||column.asValue){
				//如果没有use 用了asText asValue
				if(!column.asText){
					column.asText=column.asValue;
				}
				if(!column.asValue){
					column.asValue=column.asText;
				}
				hasColumnValue=false;
				changeTextAttr=column.asText;
				changeValueAttr=column.asValue;
				willChangeValue=true;
			}else if(typeof(column.value)!=undefined&&typeof(column.value)!="undefined"){
				hasColumnValue=true;
				changeValueAttr=null;
				changeTextAttr=null;
				willChangeValue=true;
			}else if(typeof(handler)!=undefined&&typeof(handler)=="function"){
				hasColumnValue=false;
				changeValueAttr=camelColumn;
				changeTextAttr=camelColumn;
				willChangeValue=true;
				onlyHandler=true;
			}else{
				hasColumnValue=false;
				willChangeValue=false;
			}
		}

		if(table.columnIndexMap){
			colIndex=table.columnIndexMap[columnName];
			if(typeof(colIndex)=="undefined"){
				colIndex=-1;
			}
		}
		if(colIndex>=0){
			tempTd=tr.find("td[data-col-index='"+colIndex+"']");
			if(isOk(tempTd)){
				if(willChangeValue){
					text=hasColumnValue?column.value:(data?data[changeTextAttr]:"");
					value=hasColumnValue?column.value:(data?data[changeValueAttr]:"");
					if(handler){
						handlerResult=handler(text,value);
						if(isArray(handlerResult)){
							text=handlerResult[0];
							value=handlerResult[1];
						}else{
							text=handlerResult;
							value=handlerResult;
						}
					}
					if(typeof(text)=="undefined"){
						text="";
					}
					if(typeof(value)=="undefined"){
						value="";
					}
					tempTd.attr("data-text",text).data("text",text);
					tempTd.attr("data-value",value).data("value",value);
					tempTd.html(text);
					colConfig=table.editableOptions.cols[columnName];
					if(colConfig&&colConfig.editable){
						table.me.processEditableTdChooseData(table,tempTd,text,value);
					}else{
						if(data&&data.hasOwnProperty(columnName)){
							JBoltArrayUtil.changeOneItemAttrValue(table.tableListDatas,tr.data("index"),columnName,value);
						}else {
							JBoltArrayUtil.changeOneItemAttrValue(table.tableListDatas,tr.data("index"),camelColumn,value);
						}
					}
				}
				if(editable!=null){
					editableType=typeof(editable);
					if(editableType=="boolean"){
						tempTd.attr("data-editable",editable).data("editable",editable);
//						if(colConfig){
//							colConfig.editable=editable;
//						}
					}else if(editableType=="function"){
						editableBy=editable(data)?true:false;
						tempTd.attr("data-editable",editableBy).data("editable",editableBy);
//						if(colConfig){
//							colConfig.editable=editableBy;
//						}
					}
				}
			}
		}else{
			if(willChangeValue){
				text=hasColumnValue?column.value:(data?data[changeTextAttr]:"");
				value=hasColumnValue?column.value:(data?data[changeValueAttr]:"");
				if(handler){
					handlerResult=handler(text,value);
					if(isArray(handlerResult)){
						text=handlerResult[0];
						value=handlerResult[1];
					}else{
						text=handlerResult;
						value=handlerResult;
					}
				}
				if(typeof(text)=="undefined"){
					text="";
				}
				if(typeof(value)=="undefined"){
					value="";
				}
				JBoltArrayUtil.changeOneItemAttrValue(table.tableListDatas,tr.data("index"),columnName,value);
				//如果summaryTriggerColumns 里有这一列 就可以触发
				if(isOk(column.triggerSummaryColumns)){
					table.me.processTriggerOtherColumnSummary2(table,tr,column.column,column.triggerSummaryColumns);
				}else{
					if(table.summaryTriggerColumns && table.summaryTriggerColumns[column.column]){
						colConfig=table.editableOptions.cols[column.column];
						if(colConfig && isOk(colConfig.triggerSummaryColumns)){
							table.me.processTriggerOtherColumnSummary2(table,tr,column.column,colConfig.triggerSummaryColumns);
						}else{
							table.me.processTbodySummarysInTr(table,tr);
						}
					}
				}

			}



		}
	}
	//console.log(table.tableListDatas[tr.data("index")])
}

/**
 * 刷新tables 多个
 * @param eles                     表格元素 数组
 * @param confirm                  确认信息
 * @param refreshEditableOptions   是否重新刷新可编辑表格的配置 boolean类型或者 直接给一个新的可编辑表格的配置
 * @returns
 */
function refreshJBoltTables(eles,confirm,refreshEditableOptions){
	if(confirm){
		if(typeof(confirm)=="boolean"){
			confirm="确认刷新数据？";
		}
		LayerMsgBox.confirm(confirm,function(){
			$.each(eles,function(i,item){
				refreshJBoltTable(item,false,refreshEditableOptions);
			});
		});
	}else{
		$.each(eles,function(i,item){
			refreshJBoltTable(item,false,refreshEditableOptions);
		});
	}
}
/**
 * 表格设置可编辑配置
 * @param ele
 * @param options
 * @returns
 */
function jboltTableSetEditableOptions(ele,options){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			jboltTable.me.setEditableOptions(jboltTable,options);
		}
	}
}
/**
 * 刷新table
 * @param ele                      表格元素
 * @param confirm                  确认信息
 * @param refreshEditableOptions   是否重新刷新可编辑表格的配置 boolean类型或者 直接给一个新的可编辑表格的配置
 * @returns
 */
function refreshJBoltTable(ele,confirm,refreshEditableOptions){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			var action=getRealJqueryObject(ele);
			if(isOk(action)){
				var success = checkMasterTableId(action);
				if(!success){
					return false;
				}
			}
			jboltTable.resetCellWidthAfterAjax=false;
			if(confirm){
				if(typeof(confirm)=="boolean"){
					confirm="确认刷新数据?";
				}
				LayerMsgBox.confirm(confirm,function(){
					jboltTable.me.refresh(jboltTable,refreshEditableOptions);
				});
			}else{
				jboltTable.me.refresh(jboltTable,refreshEditableOptions);
			}
		}
	}
}

/**
 * 删除tables 多个
 * @param eles
 * @param confirm
 * @returns
 */
function removeJBoltTables(eles,confirm){
	if(confirm){
		LayerMsgBox.confirm(confirm,function(){
			$.each(eles,function(i,item){
				removeJBoltTable(item,false);
			});
		});
	}else{
		$.each(eles,function(i,item){
			removeJBoltTable(item,false);
		});
	}
}
/**
 * 删除table
 * @param ele
 * @param confirm
 * @returns
 */
function removeJBoltTable(ele,confirm){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			if(confirm){
				LayerMsgBox.confirm(confirm,function(){
					jboltTable.me.remove(jboltTable);
				});
			}else{
				jboltTable.me.remove(jboltTable);
			}
		}
	}
}
/**
 * 刷新table 主table
 * @param ele                      表格元素
 * @param confirm                  确认信息
 * @param refreshEditableOptions   是否重新刷新可编辑表格的配置 boolean类型或者 直接给一个新的可编辑表格的配置
 * @returns
 */
function refreshJBoltMainTable(ele,confirm,refreshEditableOptions){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var mainTableId=table.data("jbolt-maintable-id");
		if(mainTableId){
			refreshJBoltTable(mainTableId,confirm,refreshEditableOptions);
		}

	}
}
/**
 * 刷新table 主table
 * @param ele                      表格元素
 * @param confirm                  确认信息
 * @param refreshEditableOptions   是否重新刷新可编辑表格的配置 boolean类型或者 直接给一个新的可编辑表格的配置
 * @returns
 */
function jboltMainTableRefresh(ele,confirm,refreshEditableOptions){
	refreshJBoltMainTable(ele,confirm,refreshEditableOptions);
}

/**
 * 刷新table 通过tableId
 * @param tableId                  表格元素ID
 * @param confirm                  确认信息
 * @param refreshEditableOptions   是否重新刷新可编辑表格的配置 boolean类型或者 直接给一个新的可编辑表格的配置
 * @returns
 */
function refreshJBoltTableById(tableId,confirm,refreshEditableOptions){
	var table=$("#"+tableId);
	if(isOk(table)){
		refreshJBoltTable(table,confirm,refreshEditableOptions);
	}
}


/**
 * 刷新table 到第一页
 * @param ele
 * @returns
 */
function jboltTablePageToFirst(ele){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			jboltTable.me.jboltTablePageToFirst(jboltTable);
		}
	}
}

/**
 * 刷新table 到指定页
 * @param ele
 * @param pageNumber
 * @returns
 */
function jboltTablePageTo(ele,pageNumber){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			jboltTable.me.jboltTablePageTo(jboltTable,pageNumber);
		}
	}
}

/**
 * 表格 跳转到下一页
 * @param ele
 * @param toFirstPageIfLast 最后一页时 是否下一页自动跳转到首页
 * @returns
 */
function jboltTablePageToNext(ele,toFirstPageIfLast){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			jboltTable.me.jboltTablePageToNext(jboltTable,toFirstPageIfLast);
		}
	}
}

/**
 * 表格 跳转到上一页
 * @param ele
 * @param toLastPageIfFirst 如果已经到第一页 是否循环跳转到尾页
 * @returns
 */
function jboltTablePageToPrev(ele,toLastPageIfFirst){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			jboltTable.me.jboltTablePageToPrev(jboltTable,toLastPageIfFirst);
		}
	}
}

/**
 * table 到最后一页
 * @param ele
 * @returns
 */
function jboltTablePageToLast(ele){
	var table=getJBoltTable(ele);
	if(isOk(table)){
		var jboltTable=table.jboltTable("inst");
		if(jboltTable){
			jboltTable.me.jboltTablePageToLast(jboltTable);
		}
	}
}
/**
 * 得到滚动条宽度
 * @param ele
 * @returns
 */
function getScrollBarWidth(ele){
	return Math.abs(ele.offsetWidth - ele.clientWidth);
}
/**
 * 得到滚动条高度
 * @param ele
 * @returns
 */
function getScrollBarHeight(ele){
	return Math.abs(ele.offsetHeight - ele.clientHeight);
}
;(function($){
	var jboltTablePageTpl='<div class="jbolt_table_pages noselect">'+
		'<div class="pages">'+
		'<div class="mainPagination mb-1 mb-sm-0  d-block d-sm-inline-block text-center" id="${pageId}"></div>'+
		'<div class="searchPage d-none d-sm-inline-block">'+
		'<span class="page-go pl-3">到<input style="width:50px;" id="gonu" data-rule="pint" data-tips="请输入正整数" type="text" oninput="return FormChecker.checkIt(this)" min="1" maxlength="6"  pattern="[0-9]*" class="current_page" value="1">页</span>'+
		'<a tabindex="-1" href="javascript:;" class="page-btn">GO</a>'+
		'<span class="page-sum">共&nbsp;<strong id="totalRow" class="allPage">1</strong>&nbsp;条&nbsp;<strong id="totalPage" class="allPage">1</strong>&nbsp;页</span>'+
		'<select id="pageSize" class="mx-2" style="width:80px;height: 32px;margin-top:-1px;border-color:#e6e6e6;">'+
		'{@each options as option}{@if option}<option {@if option==pageSize} selected="selected" {@/if} value="${option}">${option}条/页</option>{@/if}{@/each}'+
		'</select>'+
		'</div>'+
		'</div>'+
		'<div class="clearfix"></div>'+
		'</div>';
	var jboltTablePageTpl_mini='<div class="jbolt_table_pages noselect">'+
		'<div class="pages">'+
		'<div class="mainPagination mb-1 mb-sm-0 d-block text-center" id="${pageId}"></div>'+
		'<div class="searchPage d-none">'+
		'<input type="hidden" id="pageSize" value="${pageSize?pageSize:10}" />'+
		'</div>'+
		'</div>'+
		'<div class="clearfix"></div>'+
		'</div>';

	var JboltColSortTpl='<span class="jbolt_table_col_sort"><i class="sort sort_asc" title="升序"></i><i class="sort sort_desc" title="降序"></i><span>';
	var JboltColSort_asc_Tpl='<span class="jbolt_table_col_sort"><i class="sort sort_asc active" title="升序"></i><i class="sort sort_desc" title="降序"></i><span>';
	var JboltColSort_desc_Tpl='<span class="jbolt_table_col_sort"><i class="sort sort_asc" title="升序"></i><i class="sort sort_desc active" title="降序"></i><span>';
	var prependTplMap={
		"th_checkbox":'<th rowspan="[rowspan]" data-width="50" style="min-width:50px;max-width:50px;width:50px;text-align:center;" data-column="checkbox"><div class="jbolt_table_checkbox"><input type="checkbox" name="jboltTableCheckbox" data-ptype="thead" /><span><i class="fa fa-check"></i></span></div></th>',
		"td_checkbox":'<td style="min-width:50px;max-width:50px;width:50px;text-align:center;"><div class="jbolt_table_checkbox"><input type="checkbox" name="jboltTableCheckbox" data-ptype="tbody" /><span><i class="fa fa-check"></i></span></div></td>',
		"th_radio":'<th rowspan="[rowspan]" data-width="50"  style="min-width:50px;max-width:50px;width:50px;text-align:center;" data-column="radio"><div class="jbolt_table_radio"><input type="radio" name="jboltTableRadio" data-ptype="thead" /></div></th>',
		"td_radio":'<td data-width="50"  style="min-width:50px;max-width:50px;width:50px;text-align:center;"><div class="jbolt_table_radio"><input type="radio" name="jboltTableRadio" data-ptype="tbody" /></div></td>'
	};
	var jb_methods = {
		resize:function(table){
			this.setTableHeight(table);
		},
		checkCanUpdateOneColumn:function(table,td){
			//检测是否满足可编辑表格 submit条件
			if(!table.editable){LayerMsgBox.alert("表格没有设置为可编辑[data-editable='true']",2);return false;}
			if(!table.editableOptions){LayerMsgBox.alert("表格没有设置可编辑参数[data-editable-option='']",2);return false;}
			if(!isOk(td)){
				LayerMsgBox.alert("checkCanUpdateOneColumn函数需要传入第二个参数[td]",2);
				return false;
			}
			var submitOption=table.editableOptions["submit"];
			if(!submitOption){LayerMsgBox.alert("表格可编辑参数[data-editable-option]中未设置submit信息",2);return false;}
			if(!submitOption.type||submitOption.type!='cell'){
				LayerMsgBox.alert("表格可编辑参数[data-editable-option]中的[submit]配置里[type]参数应设置为[type:'cell']",2);
				return false;
			}
			if(!submitOption.url){
				LayerMsgBox.alert("表格可编辑参数[data-editable-option]中的[submit]配置里未设置[url]参数，不能执行Submit操作",2);
				return false;
			}

			var tr=td.parent();
			var id=tr.data("id");
			if(!id){
				LayerMsgBox.alert("所在行[tr]未配置data-id属性",2);
				return false;
			}
			return this.checkTdRequiredIsValid(table,td);
		},
		checkTdRequiredIsValid:function(table,td){
			//判断是否required 通过校验
			if(!td.data("changed")||!td.data("required")){
				return true;
			}
			var value=td.data("value");
			if(typeof(value)!=undefined && typeof(value)!='undefined' && isOk(value.toString())){
				return true;
			}
			td.addClass("is-invalid");
			if(table.requiredImgbg){
				td.addClass("imgbg");
			}
			return false;
		},
		checkCanSubmit:function(table){
			//检测是否满足可编辑表格 submit条件
			if(!table.editable){LayerMsgBox.alert("表格没有设置为可编辑[data-editable='true']，不能执行Submit操作",2);return false;}
			if(!table.editableOptions){LayerMsgBox.alert("表格没有设置可编辑参数[data-editable-option='']，不能执行Submit操作",2);return false;}
			var submitOption=table.editableOptions["submit"];
			if(!submitOption){LayerMsgBox.alert("表格可编辑参数[data-editable-option]中未设置submit信息，不能执行Submit操作",2);return false;}
			if(!submitOption.type){
				submitOption.type="all";
			}
			if(submitOption.type=="cell"){
				LayerMsgBox.alert("表格可编辑参数[data-editable-option]中的[submit]中配置异常,[type:all]时才可以整体提交，不能执行Submit操作",2);
				return false;
			}
			if(submitOption.type!="multi"&&!submitOption.url){
				LayerMsgBox.alert("表格可编辑参数[data-editable-option]中的[submit]配置里未设置[url]参数，不能执行Submit操作",2);
				return false;
			}

			//处理还在编辑状态的单元格 回显
			this.processEditingTds(table);

			//执行checkForm
			var withForm=table.editableOptions.submit.withForm;
			if(withForm){
				var formType=typeof(withForm);
				if(formType=="string"){
					if(!FormChecker.check(withForm)){
						return false;
					}
				}else if(formType=="object"){
					//如果是数组
					if(isArray(withForm)){
						var checkFail=false;
						$.each(withForm,function(i,item){
							if(!FormChecker.check(item)){
								checkFail=true;
								return false;
							}
						});
						if(checkFail){
							return false;
						}
					}else{
						if(!FormChecker.check(withForm)){
							return false;
						}
					}
				}
			}
			var extraColumnForm=table.editableOptions.extraColumnForm;
			if(extraColumnForm){
				var success=jboltTableSaveFormToTableCurrentActiveTr(extraColumnForm,false,true);
				if(!success){
					return false;
				}
			}

			if(table.editableOptions&&table.editableOptions.required){
				if(table.isEmpty){
					LayerMsgBox.alert("表格数据不能为空",2);
					return false;
				}
				//执行item表格 每一个修改了的行里是否有必填的没有填写
				var changeTrs = table.tbody.find("tr[data-changed='true']");
				if(notOk(changeTrs)){
					LayerMsgBox.alert("表格中无有效可提交数据",2);
					return false;
				}
			}


			//执行item表格 每一个修改了的行里是否有必填的没有填写
			return this.checkEditableCellRequired(table);
		},
		/**
		 * 选中行的trs 单元格判断required
		 * @param table
		 * @returns {boolean}
		 */
		checkEditableCheckedTrCellRequired:function(table){
			var trs=this.getCheckedTrs(table);
			if(notOk(trs)){
				return true;
			}
			//处理还在编辑状态的单元格 回显
			this.processEditingTds(table);
			return this.checkEditableCellRequired(table,trs);
		},
		/**
		 * 单元格判断required
		 * @param table
		 * @param trs
		 * @returns {boolean}
		 */
		checkEditableCellRequired:function(table,trs){
			table.tbody.find("td.is-invalid").removeClass("is-invalid").removeClass("imgbg");
			var assignTr = isOk(trs);
			//执行item表格 每一个修改了的行里是否有必填的没有填写
			trs=assignTr?trs:table.tbody.find("tr[data-changed='true']");
			if(isOk(trs)){
				var tds,tempTr,tempTd,tempText,hasInvalid=false,count=0,size=trs.length;
				$.each(trs,function(i,item){
					tempTr = $(item);
					if(!assignTr && !tempTr.data("changed")){
						return true;
					}
					tds=tempTr.find("td[data-required='true'][data-value='']");
					if(isOk(tds)){
						tds.addClass("is-invalid");
						if(table.requiredImgbg){
							tds.addClass("imgbg");
						}
						hasInvalid=true;
						count=count+tds.length;
					}
					tds=tempTr.find("td[data-required='true']:not([data-value])");
					if(isOk(tds)){
						tds.each(function(){
							tempTd=$(this);
							tempText=$.trim(tempTd[0].innerText);
							if(tempText.length==0){
								tempTd.addClass("is-invalid");
								if(table.requiredImgbg){
									tempTd.addClass("imgbg");
								}
								hasInvalid=true;
								count++;
							}
						});

					}
				});
				if(hasInvalid){
					LayerMsgBox.error("表格中存在["+count+"]个未处理必填项",1000);
					return false;
				}
			}
			return true;
		},
		getSubmitUpdateData:function(table){
			if(table.isEmpty){return null;}
			//获取到需要更新的信息
			var trs=table.tbody.find("tr[data-changed='true'][data-needupdate='true']");
			if(!isOk(trs)){return null;}
			var datas=new Array();
			trs.each(function(){
				datas.push(table.tableListDatas[$(this).data("index")]);
			});
			return datas;
		},
		getSubmitSaveData:function(table){
			if(table.isEmpty){return null;}
			//获取到需要保存的数据
			var trs=table.tbody.find("tr[data-changed='true'][data-needsave='true']");
			if(!isOk(trs)){return null;}
			var datas=new Array();
			var trIndex=-1,trLen=trs.length,saveTrData;
			trs.each(function(){
				trIndex=$(this).data("index");
				if(trIndex>=0||trIndex<=trLen-1){
					saveTrData = table.tableListDatas[trIndex];
					if(saveTrData){
						datas.push(saveTrData);
					}
				}
			});
			return datas;
		},
		getSubmitFormData:function(table){
			var withForm=table.editableOptions.submit.withForm;
			var formData=null,tempFormData=null;
			if(withForm){
				var type=typeof(withForm),arrayItemType;
				if(type=="string"){
					formData=$("#"+withForm).serializeJSON();
				}else if(type=="object"){
					if(isArray(withForm)){
						formData={};
						$.each(withForm,function(i,item){
							arrayItemType=typeof(item);
							if(arrayItemType=="object"){
								if(isDOM(item)){
									tempFormData=$(item).serializeJSON();
								}else{
									tempFormData=item.serializeJSON();
								}
							}else if(arrayItemType=="string"){
								tempFormData=$("#"+item).serializeJSON();
							}

							if(tempFormData){
								Object.assign(formData,tempFormData);
							}
						});
					}else{
						if(isDOM(withForm)){
							formData=$(withForm).serializeJSON();
						}else{
							formData=withForm.serializeJSON();
						}

					}

				}
			}
			return formData;
		},
		getSubmitParamsData:function(table){
			var params=table.editableOptions.submit.params;
			var type=typeof(params);
			var paramsData=null;
			if(type){
				if(type=="function"){
					paramsData=params();
					if(typeof(paramsData)=="undefined" || !isOk(paramsData)){
						paramsData=null;
					}
				}else{
					paramsData=params;
				}
			}
			return paramsData;
		},
		getSubmitCommonAttrData:function(table){
			var commonAttr=table.editableOptions.submit.commonAttr;
			var type=typeof(commonAttr);
			var commonAttrData=null;
			if(type){
				if(type=="function"){
					commonAttrData=commonAttr();
					if(typeof(commonAttrData)=="undefined" || !isOk(commonAttrData)){
						commonAttrData=null;
					}
				}else{
					commonAttrData=commonAttr;
				}
			}
			return commonAttrData;
		},
		getSubmitRemoveAttrData:function(table){
			var removeAttr=table.editableOptions.submit.removeAttr;
			var type=typeof(removeAttr);
			var removeAttrData=null;
			if(type){
				if(type=="function"){
					remmoveAttrData=removeAttr();
					if(typeof(removeAttrData)=="undefined" || !isOk(removeAttrData)){
						removeAttrData=null;
					}
				}else{
					removeAttrData=removeAttr;
				}
			}
			return removeAttrData;
		},
		//数据里插入公共attr
		processCommonAttrIntoData:function(datas,commonAttr){
			var keys;
			$.each(datas,function(i,data){
				keys=Object.keys(commonAttr);
				if(isOk(keys)){
					$.each(keys,function(i,key){
						data[key]=commonAttr[key];
					});
				}
			});
		},
		//数据里插入公共attr
		processRemoveDataAttr:function(datas,removeAttr){
			var keys;
			$.each(datas,function(i,data){
				keys=Object.keys(removeAttr);
				if(isOk(keys)){
					$.each(keys,function(i,key){
						delete data[key];
					});
				}
			});
		},
		getSubmitData:function(table,returnJsonData){
			var deleteIds=table.submit_delete_ids;
			var updateData=this.getSubmitUpdateData(table);
			var saveData=this.getSubmitSaveData(table);
			var formData=this.getSubmitFormData(table);
			var paramsData=this.getSubmitParamsData(table);
			var commonAttr=this.getSubmitCommonAttrData(table);

			if(commonAttr){
				//如果存在共享all attr的话 就挨个处理 save update
				var all=commonAttr['all'];
				if(isOk(all)){
					//如果不是数组 就说明save update 都指定一样的
					if(isOk(saveData)){
						this.processCommonAttrIntoData(saveData,all);
					}
					if(isOk(updateData)){
						this.processCommonAttrIntoData(updateData,all);
					}
				}
				//如果存在共享save attr的话 就挨个处理save
				var save=commonAttr['save'];
				if(isOk(save)&&isOk(saveData)){
					this.processCommonAttrIntoData(saveData,save);
				}
				//如果存在共享update attr的话 就挨个处理update
				var update=commonAttr['update'];
				if(isOk(update)&&isOk(updateData)){
					this.processCommonAttrIntoData(updateData,update);
				}
			}
			var removeAttr=this.getSubmitRemoveAttrData(table);
			if(removeAttr){
				this.processRemoveDataAttr(saveData,removeAttr);
				this.processRemoveDataAttr(updateData,removeAttr);
			}
			var postData={
				"delete":deleteIds,
				"update":updateData,
				"save":saveData,
				"form":formData,
				"params":paramsData
			}
			if(returnJsonData){
				return postData;
			}
			return JSON.stringify(postData);
		},
		getSubmitDatas:function(tables,toJsonData){
			var datas={};
			var that=this;
			$.each(tables,function(i,table){
				datas[table.editableOptions.submit.name]=that.getSubmitData(table,true);
			});
			if(toJsonData){
				return datas;
			}
			return JSON.stringify(datas);
		},
		submitMulti:function(tables,url,successCallback,failCallback,refreshEditableOptions){
			var that=this;
			var canSubmit=true;
			$.each(tables,function(i,table){
				canSubmit=that.checkCanSubmit(table);
				if(!canSubmit){return false;}
			});
			if(!canSubmit){return false;}
			//可编辑表格 提交数据

			var postData=that.getSubmitDatas(tables);
			LayerMsgBox.loading("提交中...",20000);
			Ajax.post(url,{"jboltTables":postData},function(res){
				LayerMsgBox.closeLoadingNow();
				if(successCallback){
					successCallback(res);
				}else{
					LayerMsgBox.success("提交成功",600,function(){
						that.refresh(table,refreshEditableOptions);
					});
				}
			},function(res){
				if(failCallback){
					failCallback(res);
				}else{
					LayerMsgBox.alert(res.msg?res.msg:"表格数据提交失败",2);
				}
			});
			return true;

		},
		submit:function(table,refreshEditableOptions){
			//可编辑表格 提交数据
			var canSubmit=this.checkCanSubmit(table);
			if(!canSubmit){return false;}
			var submitOption=table.editableOptions["submit"];
			var that=this;
			var postData=that.getSubmitData(table);
			var successCallback=submitOption.success;
			var failCallback=submitOption.fail;
			LayerMsgBox.loading("提交中...",20000);
			Ajax.post(submitOption.url,{"jboltTable":postData},function(res){
				LayerMsgBox.closeLoadingNow();
				if(successCallback){
					successCallback(res,table);
				}else{
					LayerMsgBox.success("提交成功",600,function(){
						that.refresh(table,refreshEditableOptions);
					});
				}
			},function(res){
				if(failCallback){
					failCallback(res,table);
				}else{
					LayerMsgBox.alert(res.msg?res.msg:"表格数据提交失败",2);
				}
			});
			return true;
		},
		//获得单元格可提交更新数据
		getUpdateOneColumnData:function(table,td){
			var id=td.parent().data("id");
			var value=td.data("value");
			var submitattr=td.data("submitattr");
			if(!submitattr){
				var th= table.thead.find("th[data-col-index='"+td.data("col-index")+"']");
				if(isOk(th)){
					submitattr = StrUtil.camel(th.data("column"));
				}
			}
			var postData={};
			var idConfig=table.editableOptions.cols["id"];
			postData[(idConfig&&idConfig.submitAttr)?idConfig.submitAttr:"id"]=id;
			postData[submitattr]=value;
			return postData;
		},
		//最后更新单元格和tr状态为同步成功
		updateOneColumnChangeSuccess:function(table,td,data){
			var beTextFormat=td.data("beTextFormat");
			var tr=td.parent();
			var orignText;
			var switchBtn=td.find("img[data-switchbtn]");
			if(beTextFormat){
				if(isOk(switchBtn)){
					orignText=switchBtn.data("value");
				}else{
					orignText=td.data("text");
				}
			}else{
				if(isOk(switchBtn)){
					orignText=switchBtn.data("value");
				}else{
					orignText=td[0].innerText;
				}
			}
//				if(beTextFormat){
//					var switchBtn=td.find("img[data-switchbtn]");
//					if(isOk(switchBtn)){
//						orignText=switchBtn.data("value");
//					}else{
//						orignText=td.data("text");
//					}
//				}else{
//					var	tplId=table.data("rowtpl");
//					var tplContent=g(tplId).innerHTML;
//					var tempTrHtml=juicer(tplContent,{datas:[data],formData:table.formData});
//					var tempTr=$(tempTrHtml);
//					//处理Tbody添加列操作 比如在第一列添加checkbox等
//					var columnPrepend=table.data("column-prepend");
//					if(columnPrepend&&table.prependColumnType){
//						var aftertd=tempTr.find("td:nth-child("+(table.prependColumnIndex+1)+")");
//						if(isOk(aftertd)){
//							aftertd.before(this.getColumnPrependElement(table.prependColumnType,"td"));
//						}
//					}
//					var colIndex=td.index();
//					var tempTd=tempTr.find("td:eq("+colIndex+")");
//					var switchBtn=tempTd.find("img[data-switchbtn]");
//					if(isOk(switchBtn)){
//						orignText=switchBtn.data("value");
//					}else{
//						orignText=tempTd.text();
//					}
//				}

			td.data("origin-text",orignText).attr("data-origin-text",orignText);
			td.data("changed",false).removeAttr("data-changed").attr("data-syncdb",true).data("syncdb",true);
			tr.data("changed",false).removeAttr("data-changed").attr("data-syncdb",true).data("syncdb",true);
			this.processEditableTrChangedStatusByTd(table,td);
		},
		updateOneColumn:function(table,td,successCallback){
			//可编辑表格 提交单元格数据
			var canUpdate=this.checkCanUpdateOneColumn(table,td);
			if(!canUpdate){return false;}
			var submitOption=table.editableOptions["submit"];
			var that=this;
			var postData=that.getUpdateOneColumnData(table,td);
			var callback=submitOption.success;
			LayerMsgBox.loading("提交中...",20000);
			Ajax.post(submitOption.url,postData,function(res){
				LayerMsgBox.closeLoadingNow();
				//把状态改成更新成功
				that.updateOneColumnChangeSuccess(table,td,res.data);
				if(successCallback){
					successCallback(res,table,td);
				}

				if(callback){
					callback(res,table,td);
				}else{
					LayerMsgBox.success("提交成功",600);
				}
			});
			return true;
		},
		activeNextTr:function(table){
			if(table.isEmpty){
				LayerMsgBox.alert("表格内无数据",2);
				return;
			}
			if(typeof(table.activeTrIndex)=="undefined" || table.activeTrIndex<0){
				LayerMsgBox.alert("表格尚未选中任何数据",2);
				return;
			}
			if(typeof(table.activeTrIndex)=="number" && table.activeTrIndex>=table.tableListDatas.length-1){
				LayerMsgBox.alert("已经选中表格最后一行数据",2);
				return;
			}
			var tr = table.find("tbody>tr[data-index='"+(table.activeTrIndex+1)+"']");
			if(isOk(tr)){
				tr.click();
			}
		},
		activePrevTr:function(table){
			if(table.isEmpty){
				LayerMsgBox.alert("表格内无数据",2);
				return;
			}
			if(typeof(table.activeTrIndex)=="undefined" || table.activeTrIndex<0){
				LayerMsgBox.alert("表格尚未选中任何数据",2);
				return;
			}
			if(typeof(table.activeTrIndex)=="number" && table.activeTrIndex<=0){
				LayerMsgBox.alert("已经选中表格第一行数据",2);
				return;
			}
			var tr = table.find("tbody>tr[data-index='"+(table.activeTrIndex-1)+"']");
			if(isOk(tr)){
				tr.click();
			}
		},
		//删除选中checkbox或者radio的行
		removeJBoltTableCheckedTr:function(table,confirm,callback){
			var that=this;
			var doRemoveFunc = function(){
				var columnprepend=table.data("column-prepend");
				if(!columnprepend){
					columnprepend="checkbox";
				}
				if(columnprepend=="checkbox"||columnprepend.indexOf(":checkbox")!=-1 || columnprepend=="radio"||columnprepend.indexOf(":radio")!=-1){
					var cks=table.tbody.find("tr>td input[type='checkbox'][name='jboltTableCheckbox']:checked,tr>td input[type='radio'][name='jboltTableRadio']:checked");
					if(isOk(cks)){
						var cksLen=cks.length;
						var ids = [];
						var tr,id,index;
						cks.each(function(i){
							tr = cks.eq(i).closest("tr");
							index = tr.data("index");
							id = tr.data("id");
							that.removeRow(table,index,(i<(cksLen-1)));
							if(id){
								ids.push(id);
							}
						});
						//重新summary计算
						that.reProcessEditableTfootSummarys(table);

						if(callback){
							callback(table,ids);
						}
					}
				}
			}

			if(confirm){
				var msg = ((typeof(confirm)=="boolean")?"确定删除所选数据吗？":confirm);
				LayerMsgBox.confirm(msg,function(){
					doRemoveFunc();
				});
			}else{
				doRemoveFunc();
			}
			return true;
		},
		getCheckedCount:function(table){
			var that=this;
			var columnprepend=table.data("column-prepend");
			if(!columnprepend){
				columnprepend="checkbox";
			}

			if(columnprepend=="checkbox"||columnprepend.indexOf(":checkbox")!=-1){
				return that.getCheckboxCheckedCount(table);
			}

			if(columnprepend=="radio"||columnprepend.indexOf(":radio")!=-1){
				return that.getRadioCheckedCount(table);
			}
		},
		isCheckedNone:function(table){
			var that=this;
			var columnprepend=table.data("column-prepend");
			if(!columnprepend){
				columnprepend="checkbox";
			}

			if(columnprepend=="checkbox"||columnprepend.indexOf(":checkbox")!=-1){
				return that.isCheckboxCheckedNone(table);
			}

			if(columnprepend=="radio"||columnprepend.indexOf(":radio")!=-1){
				return that.isRadioCheckedNone(table);
			}

		},
		setCheckedIds:function(table,ids){
			if(!isOk(ids)){
				return false;
			}
			var id,ele;
			for(var i in ids){
				id=ids[i];
				ele=table.tbody.find("tr[data-id='"+id+"']>td>.jbolt_table_checkbox>input");
				if(isOk(ele)){
					if(!ele[0].checked){
						ele.click();
					}
				}
			}
			return true;
		},
		setCheckedId:function(table,id){
			if(!id){
				return false;
			}
			var ele=table.tbody.find("tr[data-id='"+id+"']>td>.jbolt_table_checkbox>input,tr[data-id='"+id+"']>td>.jbolt_table_radio>input");
			if(isOk(ele)){
				if(!ele[0].checked){
					if(table.prependColumnType == "radio"){
						setTimeout(function(){ele.trigger("click");},50);
					}else{
						ele.trigger("click");
					}
				}
			}
			return true;
		},
		//获得选中数据的id（支持多条）
		getCheckedIds:function(table,dontShowError){
			var chrds=this.getCheckedEles(table);
			if(!isOk(chrds)){
				if(!dontShowError){
					LayerMsgBox.alert("请至少选择一行数据",2);
				}
				return false;
			}
			var firstId=chrds.eq(0).closest("tr").data("id");
			if(!firstId){
				if(!dontShowError){
					LayerMsgBox.alert("请在数据所在的TR上增加data-id属性",2);
				}
				return false;
			}

			var ids=new Array();
			chrds.each(function(i){
				ids.push(chrds.eq(i).closest("tr").data("id"));
			});
			return isOk(ids)?ids:false;
		},
		//获得选中数据的列
		getCheckedCols:function(table,column,dontShowError){
			var checkedDatas=this.getCheckedDatas(table,null,dontShowError);
			if(!isOk(checkedDatas)){
				return false;
			}
			var texts=new Array();
			$.each(checkedDatas,function(i,json){
				texts.push(json[column]||json[StrUtil.camel(column)]);
			});
			return isOk(texts)?texts:false;
		},
		//获得选中数据的列text
		getCheckedTexts:function(table,dontShowError){
			var chrds=this.getCheckedEles(table);
			if(!isOk(chrds)){
				if(!dontShowError){
					LayerMsgBox.alert("请至少选择一行数据",2);
				}
				return false;
			}
			var firstText=chrds.eq(0).closest("tr").data("text");
			if(typeof(firstText)=="undefined"){
				if(!dontShowError){
					LayerMsgBox.alert("请在数据所在的TR上增加data-text属性",2);
				}
				return false;
			}

			var texts=new Array();
			chrds.each(function(i){
				texts.push(chrds.eq(i).closest("tr").data("text")||"");
			});
			return isOk(texts)?texts:false;
		},
		clear:function(table){
			table.tbody.empty();
			table.tableListDatas=[];
			this.processAfterRemoveRow(table);
		},
		//获得表格一列属性的所有制数组
		getOneColumnDatas:function(table,attrName,withBlankDatas){
			if(table.editable){
				//处理还在编辑状态的单元格 回显
				this.processEditingTds(table);
			}
			var datas=table.tableListDatas;
			if(notOk(datas)||notOk(attrName)){
				return null;
			}
			var attrdatas=new Array();
			var colData;
			$.each(datas,function(i){
				colData = datas[i][attrName];
				if(typeof(colData)=="undefined"){
					if(withBlankDatas){
						attrdatas.push(colData);
					}
				}else{
					attrdatas.push(colData);
				}
			});
			return isOk(attrdatas)?attrdatas:null;
		},
		//获得表格所有数据
		getAllDatas:function(table,needAttrs){
			if(table.editable){
				//处理还在编辑状态的单元格 回显
				this.processEditingTds(table);
			}
			var datas=table.tableListDatas;
			if(!isOk(datas)){
				return null;
			}
			if(isOk(needAttrs)){
				var jsons=new Array(),newJsonData,newArr,dataSize=datas.length;
				$.each(datas,function(k){
					newJsonData={};
					$.each(needAttrs,function(i,item){
						if(item.indexOf(":")==-1){
							newJsonData[item]=datas[k][item];
						}else{
							newArr=item.split(":");
							newJsonData[newArr[1]]=datas[k][newArr[0]];
						}
					});
					jsons.push(newJsonData);
				});
				return isOk(jsons)?jsons:null;
			}
			return deepClone(datas);
		},
		getKeepSelectedDatas:function(table,needAttrs){
			if(!table.keepSelectedItemsEnable){
				return null;
			}
			var datas=table.selectedItemsDatas;
			if(!isOk(datas)){
				return null;
			}
			if(isOk(needAttrs)){
				var jsons=new Array(),newJsonData,newArr,dataSize=datas.length;
				$.each(datas,function(k){
					newJsonData={};
					$.each(needAttrs,function(i,item){
						if(item.indexOf(":")==-1){
							newJsonData[item]=datas[k][item];
						}else{
							newArr=item.split(":");
							newJsonData[newArr[1]]=datas[k][newArr[0]];
						}
					});
					jsons.push(newJsonData);
				});
				return isOk(jsons)?jsons:null;
			}
			return deepClone(datas);
		},
		getKeepSelectedTexts:function(table,needAttrs){
			if(!table.keepSelectedItemsEnable){
				return null;
			}
			var datas=table.selectedItemsDatas;
			if(!isOk(datas)){
				return null;
			}
			var texts=[];
			$.each(datas,function(i,json){
				texts.push((json[table.selectedItemTextAttr]||json[table.selectedItemTextAttr.toUpperCase()]||""));
			});
			return isOk(texts)?texts:null;
		},
		getKeepSelectedIds:function(table){
			if(!table.keepSelectedItemsEnable){
				return null;
			}
			return table.selectedItemsIds;
		},
		//获得选中数据的数据的json（支持多条）
		getCheckedDatas:function(table,needAttrs,dontShowError){
			var chrds=this.getCheckedEles(table);

			if(!isOk(chrds)){
				if(!dontShowError){
					LayerMsgBox.alert("请至少选择一行数据",2);
				}
				return null;
			}
			var datas=table.tableListDatas;

			if(!isOk(datas)){
				return null;
			}

			var jsons=new Array(),tempJsonData,dataSize=datas.length;
			if(needAttrs&&needAttrs.length>0){
				var newJsonData,newArr;
				chrds.each(function(i){
					if(i>=dataSize){
						return false;
					}
					tempJsonData=datas[chrds.eq(i).closest("tr").data("index")];
					newJsonData={};
					$.each(needAttrs,function(i,item){
						if(item.indexOf(":")==-1){
							newJsonData[item]=tempJsonData[item];
						}else{
							newArr=item.split(":");
							newJsonData[newArr[1]]=tempJsonData[newArr[0]];
						}
					});
					jsons.push(newJsonData);
				});
			}else{
				chrds.each(function(i){
					tempJsonData=datas[chrds.eq(i).closest("tr").data("index")];
					jsons.push(deepClone(tempJsonData));
				});
			}
			return isOk(jsons)?jsons:null;
		},
		//获得选中数据的数据的json（只能一行）
		getCheckedData:function(table,needAttrs,dontShowError){
			var chrds=this.getCheckedEles(table);

			if(!isOk(chrds)){
				if(!dontShowError){
					LayerMsgBox.alert("请选择一行数据",2);
				}
				return null;
			}
			if(chrds.length>1){
				if(!dontShowError) {
					LayerMsgBox.alert("最多选择一行数据", 2);
				}
				return null;
			}
			var datas=table.tableListDatas;

			if(!isOk(datas)){
				return null;
			}
			var rowIndex=chrds.eq(0).closest("tr").data("index");
			if(rowIndex >= datas.length){
				return null;
			}
			var jsonData=datas[rowIndex];
			if(needAttrs&&needAttrs.length>0){
				var newJsonData={},newArr;
				$.each(needAttrs,function(i,item){
					if(item.indexOf(":")==-1){
						newJsonData[item]=jsonData[item];
					}else{
						newArr=item.split(":");
						newJsonData[newArr[1]]=jsonData[newArr[0]];
					}
				});
				jsonData=newJsonData;
			}

			return deepClone(jsonData);
		},
		//得到一行的json数据
		getRowJsonData:function(table,rowOrIndex){
			var datas=table.tableListDatas;
			if(!isOk(datas)){return null;}
			var dataIndex=typeof(rowOrIndex)=="number"?rowOrIndex:rowOrIndex.data("index");
			return datas[dataIndex];
		},
		getCheckedEles:function(table){
			if(table.editable){
				//处理回显
				this.processEditingTds(table);
			}
			var columnprepend=table.data("column-prepend");
			if(!columnprepend){
				columnprepend="checkbox";
			}
			var chrds,tr;
			if(columnprepend=="checkbox"||columnprepend.indexOf(":checkbox")!=-1){
				chrds=table.tbody.find("tr>td input[type='checkbox'][name='jboltTableCheckbox']:checked");
			}else if(columnprepend=="radio"||columnprepend.indexOf(":radio")!=-1){
				chrds=table.tbody.find("tr>td input[type='radio'][name='jboltTableRadio']:checked");
			}
			return chrds;
		},
		//获取选中了一个Tr
		getCheckedTr:function(table,dontShowError){
			var chrds=this.getCheckedEles(table);
			if(!isOk(chrds)){
				if(!dontShowError){
					LayerMsgBox.alert("请选择一行数据",2);
				}
				return false;
			}
			if(chrds.length>1){
				if(!dontShowError){
					LayerMsgBox.alert("最多选择一行数据",2);
				}
				return false;
			}
			var tr=chrds.eq(0).closest("tr");
			if(!isOk(tr)){
				return false;
			}
			return tr;
		},
		//获取选中了多个Tr
		getCheckedTrs:function(table,dontShowError){
			var chrds=this.getCheckedEles(table);
			if(!isOk(chrds)){
				if(!dontShowError){
					LayerMsgBox.alert("请至少选择一行数据",2);
				}
				return false;
			}
			var trs=new Array();
			chrds.each(function(i){
				trs.push(chrds.eq(i).closest("tr"));
			});
			return isOk(trs)?trs:false;
		},
		saveFormToTableCurrentActiveTr:function(table,form,confirm,dontProcessIfNotExistActiveTr){
			//保存辅助录入数据到当前tr上
			if(table.currentChooseActiveTr){
				var that=this;
				var ext=function(){
					var jsonData=formToJson(form);
					table.currentChooseActiveTr.data("has-extra-column-data",true).attr("data-has-extra-column-data",true);
					that.processReplaceRowTableListData(table,table.currentChooseActiveTr,jsonData,false);
					that.processEditableTrChangedStatus(table,table.currentChooseActiveTr);
				}
				var confirmType=typeof(confirm);
				if(confirmType == undefined){
					confirm=true;
				}
				if(confirm){
					LayerMsgBox.confirm(confirmType=="string"?confirm:"确认保存表单录入信息吗？",ext);
				}else{
					ext();
				}
				return true;
			}

			if(!dontProcessIfNotExistActiveTr){
				LayerMsgBox.alert("未选中数据行，不能执行此操作",2);
				return false;
			}
			return true;
		},
		//复制选中行 并前插到最前面
		copyCheckedRowPrepend:function(table){
			return this.copyCheckedRowInsertBefore(table,true);
		},
		copyEditableTrData:function(table,trOrIndex,isDeleteDataId){
			//复制一行数据
			var trIndex=typeof(trOrIndex)=="number"?trOrIndex:trOrIndex.data("index");
			var copyData=table.tableListDatas[trIndex];
			if(isOk(copyData)){
				copyData=deepClone(copyData);
				if(isDeleteDataId){
					var idConfig=table.editableOptions.cols["id"];
					delete copyData[(idConfig&&idConfig.submitAttr)?idConfig.submitAttr:"id"];
				}
				return copyData;
			}
			return null;
		},
		//复制选中行 并前插
		copyCheckedRowInsertBefore:function(table,prepend,fromTr){
			var checkedTrs;
			if(isOk(fromTr)){
				checkedTrs=[fromTr];
			}else{
				checkedTrs=this.getCheckedTrs(table);
			}
			if(!isOk(checkedTrs)){
				return false;
			}
			var canInsertTr=this.checkCanInsertNewTr(table,1);
			if(!canInsertTr){
				return false;
			}
			var firstTr=prepend?table.tbody.find("tr:first"):checkedTrs[0];
			var size=checkedTrs.length;
			var newTr,radio,checkbox,tempTd,colIndex=-1,switchBtn,copyData;
			for(var i=0;i<size;i++){
				newTr=checkedTrs[i].clone();
				copyData=this.copyEditableTrData(table,newTr,true);
				newTr.data("id",'').removeAttr("data-id");
				radio=newTr.find("td>.jbolt_table_radio>input[type='radio']:checked");
				if(isOk(radio)){
					radio[0].checked=false;
				}
				newTr.removeClass("active").data("changed",true).attr("data-changed",true);
				newTr.find("td[data-focus]").data("focus",false).removeAttr("data-focus");
				newTr.find("td").data("origin-text",'').attr("data-origin-text",'');
				newTr.insertBefore(firstTr);
				checkbox=newTr.find("td>.jbolt_table_checkbox>input[type='checkbox']:checked");
				if(isOk(checkbox)){
					checkbox.click();
					checkbox.closest("td").data("focus",false).removeAttr("data-focus");
				}


				processInnerElesInit(newTr);

				this.processCopyTrTdChanged(table,newTr);
				//处理index column
				this.processTableIndexColumn(table);
				//处理tableListDatas
				this.processInsertRowTableListData(table,newTr,copyData,true);
				//处理统计计算
				this.reProcessEditableAllSummarysAfterInsertDataRows(table,newTr);
			}
			return true;
		},
		processCopyTrTdChanged:function(table,tr){
			var columns=Object.keys(table.columnIndexMap);
			var tempKey,colIndex,tempTd,tdText,orignText,colConfig,submitAttr,value,switchBtn;
			for(var i in columns){
				tempKey=columns[i];
				if(tempKey){
					colIndex=table.columnIndexMap[tempKey];
					if(colIndex!=undefined&&colIndex>=0){
						colConfig=table.editableOptions.cols[tempKey];
						if(colConfig&&colConfig.editable){
							tempTd=tr.find("td[data-col-index='"+colIndex+"']");
							if(isOk(tempTd)){
								tdText=tempTd[0].innerText;
								tempTd.data("column",tempKey).attr("data-column",tempKey);
								tempTd.data("changed",true).attr("data-changed",true);
								if(tdText){
									submitAttr=colConfig.submitAttr||StrUtil.camel(tempKey);
									if(submitAttr){
										tempTd.data("submitattr",submitAttr).attr("data-submitattr",submitAttr);
									}
									tempTd.data("text",tdText).attr("data-text",tdText);
									value=tempTd.data("value");
									if(typeof(value)=="undefined"){
										tempTd.data("value",tdText).attr("data-value",tdText);
									}
								}else{
									switchBtn=isOk(tempTd.find("img[data-switchbtn]"));
									if(switchBtn){
										value=switchBtn.data("value");
										tempTd.data("value",value).attr("data-value",value);
									}
								}

							}
						}
					}
				}
			}
		},
		//复制选中行 并后插到最后面
		copyCheckedRowAppend:function(table){
			return this.copyCheckedRowInsertAfter(table,true);
		},
		//复制行 并后插到后面
		copyRowInsertAfter:function(table,tr,append){
			return this.copyCheckedRowInsertAfter(table,append,tr);
		},
		//复制行 并后插到前面
		copyRowInsertBefore:function(table,tr,prepend){
			return this.copyCheckedRowInsertBefore(table,prepend,tr);
		},
		//复制选中行 并后插
		copyCheckedRowInsertAfter:function(table,append,fromTr){
			var checkedTrs;
			if(isOk(fromTr)){
				checkedTrs = [fromTr];
			}else{
				checkedTrs = this.getCheckedTrs(table);
			}
			if(!isOk(checkedTrs)){
				return false;
			}
			var canInsertTr=this.checkCanInsertNewTr(table,1);
			if(!canInsertTr){
				return false;
			}
			var size=checkedTrs.length;
			var firstTr=append?table.tbody.find("tr:last"):checkedTrs[size-1];
			var newTr,radio,checkbox,copyData;
			for(var i=size-1;i>=0;i--){
				newTr=checkedTrs[i].clone();
				copyData=this.copyEditableTrData(table,newTr,true);
				newTr.data("id",'').removeAttr("data-id");
				radio=newTr.find("td>.jbolt_table_radio>input[type='radio']:checked");
				if(isOk(radio)){
					radio[0].checked=false;
				}

				newTr.removeClass("active").data("changed",true).attr("data-changed",true);
				newTr.find("td[data-focus]").data("focus",false).removeAttr("data-focus");
				newTr.find("td").data("origin-text",'').attr("data-origin-text",'');
				newTr.insertAfter(firstTr);
				checkbox=newTr.find("td>.jbolt_table_checkbox>input[type='checkbox']:checked");
				if(isOk(checkbox)){
					checkbox.click();
					checkbox.closest("td").data("focus",false).removeAttr("data-focus");
				}
				processInnerElesInit(newTr);
				this.processCopyTrTdChanged(table,newTr);
				//处理index column
				this.processTableIndexColumn(table);
				//处理tableListDatas
				this.processInsertRowTableListData(table,newTr,copyData,true);
				//处理统计计算
				this.reProcessEditableAllSummarysAfterInsertDataRows(table,newTr);
			}
			return true;
		},
		getCheckedTrIndex:function(table){
			var checkedTr = this.getCheckedTr(talbe)
			if(!checkedTr){
				return false
			}
			return checkedTr.data("index");
		},
		getCheckedTrsIndex:function(table){
			var checkedTrs = this.getCheckedTrs(talbe)
			if(!checkedTrs){
				return false
			}
			var indexs=[];
			checkedTrs.each(function(){
				indexs.push($(this).data("index"));
			});
			return indexs;
		},
		//获取选中了一个数据
		getCheckedId:function(table,dontShowError,checkedErrorMsg){
			var tr=this.getCheckedTr(table,dontShowError);
			if(!tr){
				return false;
			}
			var id=tr.data("id");
			if(!id){
				if(!dontShowError){
					LayerMsgBox.alert(checkedErrorMsg?checkedErrorMsg:"请在数据所在的TR上增加data-id属性",2);
				}
				return false;
			}
			return id;
		},
		//新增 插入空行
		insertEmptyRow:function(table,tr,insertToBefore,forceTrChange){
			var canInsertTr=this.checkCanInsertNewTr(table,1);
			if(canInsertTr){
				//处理thead里的checkbox uncheck
				this.processUnCheckTheadCheckbox(table);
				var insertEmptyData={};
				var insertDefaultValues=table.editableOptions.insertDefaultValues;
				if(insertDefaultValues){
					insertEmptyData=deepClone(insertDefaultValues);
				}
				var tempTr=this.insertRowData(table,tr,insertEmptyData,false,insertToBefore);
				this.processTableIndexColumn(table);
				this.processInsertRowTableListData(table,tempTr,insertEmptyData,insertToBefore);
				this.initEditableHSummarys(table,tempTr);
				this.processTfootSummarys(table);
				//处理change状态
				this.processNewInsertTrEditableTdsChanged(table,tempTr,insertEmptyData,forceTrChange);
				//处理新插入的行重新设置宽度
				this.resizeTrByOldWidth(table,tempTr);
				return tempTr;
			}
			return false;
		},
		processInsertRowTableListData:function(table,insertRows,data,insertToBefore){
			if(isOk(insertRows)){
				var firstTr=insertRows.eq(0);
				if(isOk(firstTr)){
					var dataIndex=-2;
					var firstTrIndex=firstTr.data("index");
					if(firstTrIndex==0){//prepend
						dataIndex=-1;//插入到现有数据的最前端
					}else{
						dataIndex=firstTrIndex;
					}
					//插入数据
					this.processTableListDataInsert(table,data,dataIndex);
				}

			}
		},
		//新增 插入空行
		insertEmptyRows:function(table,count,tr,insertToBefore){
			if(count<=0){return false;}
			var tmpTr,data,datas=new Array();
			var insertDefaultValues=table.editableOptions.insertDefaultValues;
			for(var i=0;i<count;i++){
				if(insertDefaultValues){
					data=deepClone(insertDefaultValues);
				}else{
					data={};
				}
				datas.push(data);
			}
			tmpTr=this.insertRowData(table,tr,datas,true,insertToBefore);
			if(isOk(tmpTr)){
				processInnerElesInit(tmpTr);
				//处理index column
				this.processTableIndexColumn(table);
				this.processInsertRowTableListData(table,tmpTr,datas,insertToBefore);
				this.initEditableHSummarys(table,tmpTr);
				//处理change状态
				this.processNewInsertTrEditableTdsChanged(table,tmpTr,datas);
				//处理新插入的行重新设置宽度
				this.resizeTrByOldWidth(table,tmpTr);
			}
		},
		getFirstCanInsertTempTr:function(table){
			//获取可以插入数据的 第一个
			var tr=table.tbody.find("tr:not([data-changed='true']):not([data-id]):first");
			var prev;
			if(isOk(tr)){
				prev=tr.prev();
			}
			if(isOk(prev)){
				return prev;
			}
			return tr;
		},
		checkCanInsertNewTr:function(table,insertCount){
			var maxRowCount = 100;
			var tableEditableOptions=table.editableOptions;
			if(tableEditableOptions&&(typeof(tableEditableOptions.maxRowCount)=="number")){
				maxRowCount=tableEditableOptions.maxRowCount;
			}
			if(!insertCount){insertCount=1;}
			//如果插入数量大于最大值肯定false
			if(maxRowCount>0 && insertCount>maxRowCount){
				LayerMsgBox.alert("不能超过表格配置最大行数:"+maxRowCount+"行",2);
				return false;
			}
			if(table.isEmpty){return true;}
			var trLen = table.table_body.find("table.jbolt_main_table>tbody>tr").length;
			//如果已经存在tr 存在量+插入量如果大于maxRowCount 也不行
			if(maxRowCount>0 && trLen+insertCount>maxRowCount){
				LayerMsgBox.alert("不能超过表格配置最大行数:"+maxRowCount+"行",2);
				return false;
			}
			return true;
		},
		processNewInsertTrEditableTdsChanged:function(table,tmpTr,datas,forceTrChange){
			if(!table.editable){return false;}
			//处理新插入的行数据可编辑changed效果
			var tr,that=this,data;
			if(!isArray(datas)){
				datas=[datas];
			}
			var columns=Object.keys(table.columnIndexMap);
			var index=-1;
			$.each(tmpTr,function(i,item){
				if(item.tagName){
					index++;
					data=datas[index];
					tr=$(item);
					//处理一个行的新插入的changed切换
					that.processOneTrNewInsertTdChanged(table,tr,columns,data);
					//处理这一行的changeed状态
					that.processEditableTrChangedStatus(table,tr,forceTrChange);
				}
			});
			return true;
		},
		processOneTrNewInsertTdChanged:function(table,tr,columns,data){
			if(!isOk(data)){
				return false;
			}
			var tempKey,colIndex,tempTd,tdText,orignText,colConfig,submitAttr,dataValue;
			for(var i in columns){
				tempKey=columns[i];
				if(tempKey){
					colIndex=table.columnIndexMap[tempKey];
					if(colIndex!=undefined&&colIndex>=0){
						tempTd=tr.find("td[data-col-index='"+colIndex+"']");
						if(isOk(tempTd)){
							tdText=tempTd[0].innerText;
							if(tdText){
								colConfig=table.editableOptions.cols[tempKey];
								if(colConfig){
									submitAttr=colConfig.submitAttr||StrUtil.camel(tempKey);
									if(submitAttr){
										dataValue=data[submitAttr];
										if(dataValue){
											tempTd.data("value",dataValue).attr("data-value",dataValue);
										}
										tempTd.data("submitattr",submitAttr).attr("data-submitattr",submitAttr);
									}

									if(colConfig.editable){
										tempTd.data("origin-text","").attr("data-origin-text","");
										tempTd.data("changed",true).attr("data-changed",true);
									}
								}

								tempTd.data("text",tdText).attr("data-text",tdText);
								tempTd.data("column",tempKey).attr("data-column",tempKey);
							}

						}
					}
				}
			}
			return true;
		},
		//新增 插入数据行
		insertRows:function(table,datas,tr,insertToBefore,keepId,dontProcessNewInsertChanges,forceTrChange){
			if(!isOk(datas)){return false;}

			//判断是否可以继续插入行
			var canInsertTr=this.checkCanInsertNewTr(table,datas.length);
			if(!canInsertTr){return false;}
			if(!isOk(tr)){
				tr=this.getFirstCanInsertTempTr(table);
			}
			var insertDefaultValues=table.editableOptions.insertDefaultValues;
			var newDatas;
			if(insertDefaultValues){
				newDatas=new Array();
				if(isArray(datas)){
					var size=datas.length;
					var insertEmptyData;
					for(var i=0;i<size;i++){
						insertEmptyData={};
						newDatas.push(Object.assign(insertEmptyData,deepClone(insertDefaultValues),datas[i]));
					}
				}else{
					insertEmptyData={};
					newDatas.push(Object.assign(insertEmptyData,deepClone(insertDefaultValues),datas));
				}
			}else{
				newDatas = datas;
			}
			var tmpTr=this.insertRowData(table,tr,newDatas,true,insertToBefore,keepId);
			if(isOk(tmpTr)){
				processInnerElesInit(tmpTr);
				//处理index column
				this.processTableIndexColumn(table);
				this.processInsertRowTableListData(table,tmpTr,newDatas,insertToBefore);
				//初始化统计行列
				this.initEditableHSummarys(table,tmpTr);
				//处理统计计算
				this.reProcessEditableAllSummarysAfterInsertDataRows(table,tmpTr);
				if(!dontProcessNewInsertChanges){
					//处理change状态
					this.processNewInsertTrEditableTdsChanged(table,tmpTr,newDatas,forceTrChange);
				}
				//处理新插入的行重新设置宽度
				this.resizeTrByOldWidth(table,tmpTr);
			}
			return tmpTr;
		},
		/**
		 * 设置单元格数据
		 * @param table    当前表格
		 * @param tr       当前行
		 * @param column   这一行里的哪个列名 会根据这个找到td
		 * @param text     td的text和data-text属性的值
		 * @param value    td的data-value属性值
		 * @param dontExeValueChangeHandler   改变这个单元格的值 但是不触发任何changeColumns和handler事件
		 *
		 */
		setCell:function(table,tr,column,text,value,dontExeValueChangeHandler){
			//设置单元格数据
			var dataIndex = table.columnIndexMap[column];
			if(typeof(dataIndex)!=undefined && typeof(dataIndex)!="undefined" && dataIndex>=0){
				var td = tr.find("td[data-col-index='"+dataIndex+"']");
				return this.processEditableTdChooseData(table,td,text,value,dontExeValueChangeHandler);
			}
			return false;
		},
		setCellEditable:function(table,tr,column,editable,falseClear,clearValue,dontExeValueChangeHandler){
			//设置单元格数据
			var dataIndex = table.columnIndexMap[column];
			if(typeof(dataIndex)!=undefined  && typeof(dataIndex)!="undefined" && dataIndex>=0){
				var td = tr.find("td[data-col-index='"+dataIndex+"']");
				if(isOk(td)){
					td.data("editable",(editable||false)).attr("data-editable",editable||false);
					if(falseClear){
						if(typeof(clearValue)=="undefined"){
							clearValue = "";
						}
						return this.processEditableTdChooseData(table,td,clearValue,clearValue,dontExeValueChangeHandler);
					}
					return true;
				}
			}
			return false;
		},
		setColEditable:function(table,column,editable){
			//设置单元格数据
			var dataIndex = table.columnIndexMap[column];
			if(typeof(dataIndex)!=undefined  && typeof(dataIndex)!="undefined" && dataIndex>=0){
				var tds = table.tbody.find("td[data-col-index='"+dataIndex+"']");
				if(isOk(tds)){
					tds.data("editable",(editable||false)).attr("data-editable",editable||false);
					return true;
				}
			}
			return false;
		},
		//删除行
		removeRow:function(table,rowOrIndex,dontProcessUI){
			var row,type=typeof(rowOrIndex);
			if(type=="number"){
				row = table.table_body.find("table.jbolt_main_table>tbody>tr[data-index='"+rowOrIndex+"']");
			}else{
				row = rowOrIndex;
			}
			if(!isOk(row)){return false;}
			var trIndex=row.data("index");
			row.remove();
			if(table.left_fixed){
				table.left_fixed.body.table.find("tbody>tr[data-index='"+trIndex+"']").remove();
			}
			if(table.right_fixed){
				table.right_fixed.body.table.find("tbody>tr[data-index='"+trIndex+"']").remove();
			}
			if(!table.deleteRowIndexs){
				table.deleteRowIndexs=[];
			}
			table.deleteRowIndexs.push(trIndex);
			if(!dontProcessUI){
				this.processAfterRemoveRow(table);
			}
			//最后判断处理一下editable 表格的提交数据
			return this.processEditableAjaxSubmitDataAfterRemoveRow(table,row);
		},
		processTableListDataRemove:function(table){
			var deleteIndexs=table.deleteRowIndexs;
			if(isOk(deleteIndexs)&&isOk(table.tableListDatas)){
				var size=deleteIndexs.length;
				if(size==1){
					JBoltArrayUtil.removeByIndex(table.tableListDatas,deleteIndexs[0]);
				}else{
					var start=size-1;
					for(var i=start;i>=0;i--){
						JBoltArrayUtil.removeByIndex(table.tableListDatas,deleteIndexs[i]);
					}

				}

				table.deleteRowIndexs=[];
			}

//				console.log("---processTableListDataRemove---")
//				console.log(table.tableListDatas)
		},
		processTableListDataInsert:function(table,datas,dataIndex){
			if(!datas){return;}
			if(!isArray(datas)){
				datas=[datas];
			}
			if(!isArray(table.tableListDatas) || !table.tableListDatas){
				table.tableListDatas=[];
			}
			var size=table.tableListDatas.length;
			if(size==0){
				table.tableListDatas=datas;
			}else{
				JBoltArrayUtil.insert(table.tableListDatas,dataIndex,datas);
			}
//				console.log("---processTableListDataInsert---")
//				console.log(table.tableListDatas)
		},
		processReplaceRowTableListData:function(table,trOrIndex,data,replaceAllData){
			if(typeof(data)=="undefined"){return;}
			if(isOk(table.tableListDatas)){
				var dataIndex=typeof(trOrIndex)=="number"?trOrIndex:trOrIndex.data("index");
				if(replaceAllData){
					JBoltArrayUtil.replace(table.tableListDatas,dataIndex,data);
				}else{
					JBoltArrayUtil.merge(table.tableListDatas,dataIndex,data);
				}
			}
//				console.log("---processReplaceRowTableListData---")
//				console.log(table.tableListDatas)
		},
		processEditableAjaxSubmitDataAfterRemoveRow:function(table,row){
			//最后判断处理一下editable 表格的提交数据
			if(!table.editable){return false;}
			var rowId=row.data("id");
			//如果是来自数据库的数据
			if(rowId){
				//先加入到待删除数据库数据集合里
				if(!table.submit_delete_ids){
					table.submit_delete_ids=new Array();
				}
				table.submit_delete_ids.push(rowId);
				return true;
			}
			return false;
		},
		processAfterRemoveRow:function(table){
			this.processEmptyTableBody(table);
			this.processTrDataIndex(table);
			this.processOneAutoThWidthByTrChange(table);
			//删除表格绑定数据
			this.processTableListDataRemove(table);
			this.processTableIndexColumn(table);
			//如果选中了
			if(table.isEmpty){
				//处理thead里的checkbox uncheck
				this.processUnCheckTheadCheckbox(table);
			}
			$('.tooltip.show').remove();
			//重新summary计算
			this.reProcessEditableTfootSummarys(table);
			//reize的时候判断scroll相关的处理了
			this.refreshMainTableVScroll(table);
			//如果有横向滚动条 处理一下样式
			this.refreshFixedColumnHScroll(table);
			this.reScrollFixedColumnBox(table);
		},
		processTableDatasChangeToUp:function(table,upIndex,targetIndex){
			if(isOk(table.tableListDatas)){
//					console.log(table.tableListDatas)
				table.tableListDatas = JBoltArrayUtil.moveUp(table.tableListDatas,[upIndex],targetIndex);
//					console.log(table.tableListDatas)
				this.processTableIndexColumn(table);
			}
		},
		processTableDatasChangeToDown:function(table,downIndex,targetIndex){
			if(isOk(table.tableListDatas)){
//					console.log(table.tableListDatas)
				table.tableListDatas = JBoltArrayUtil.moveDown(table.tableListDatas,[downIndex],targetIndex);
//					console.log(table.tableListDatas)
				this.processTableIndexColumn(table);
			}
		},
		//tr上移
		moveUpRow:function(table,tr,forceTrChange){
			if(table.isTreeTable){
				var trPid=tr.data("pid");
				var trLevel=tr.data("level");
				var lastPrevTr=tr.prevAll("[data-level='"+trLevel+"'][data-pid='"+trPid+"']:first");
				if(!isOk(lastPrevTr)){
					return false;
				}

				var upArray=new Array();
				processTreeTableTrAllNodes(upArray,table.tbody,tr);
				trChangeToUp(upArray,lastPrevTr,table);
//					var upIndex= upArray[0].data("index");
//					var lastIndex = lastPrevTr.eq(0).data("index");
//					this.processTableDatasChangeToUp(table,upIndex,lastIndex);
			}else{
				var prevTr = tr.prev();
				trChangeToUp(tr,prevTr,table);
				this.processTableDatasChangeToUp(table,tr.data("index"),prevTr.data("index"));
				if(table.editable){
					this.processEditableTrChangedStatus(table,tr,forceTrChange);
					this.processEditableTrChangedStatus(table,prevTr,forceTrChange);
				}
//					if(table.left_fixed){
//						var leftTr=table.left_fixed.body.table.find("tbody>tr:eq("+trIndex+")");
//						if(isOk(leftTr)){
//							trChangeToUp(leftTr,leftTr.prev());
//						}
//					}
//					if(table.right_fixed){
//						var rightTr=table.right_fixed.body.table.find("tbody>tr:eq("+trIndex+")");
//						if(isOk(rightTr)){
//							trChangeToUp(rightTr,rightTr.prev());
//						}
//					}
			}

		},
		//tr下移
		moveDownRow:function(table,tr,forceTrChange){
			if(table.isTreeTable){
				var trPid=tr.data("pid");
				var trLevel=tr.data("level");
				var lastNextTr=tr.nextAll("[data-level='"+trLevel+"'][data-pid='"+trPid+"']:first");
				if(!isOk(lastNextTr)){
					return false;
				}

				var downArray=new Array();
				processTreeTableTrAllNodes(downArray,table.tbody,tr);

				var upArray=new Array();
				processTreeTableTrAllNodes(upArray,table.tbody,lastNextTr);
				var currentLastTr=(upArray.length==1)?lastNextTr:upArray[upArray.length-1];
				trChangeToDown(downArray,currentLastTr,table);
			}else{
				var nextTr=tr.next();
				trChangeToDown(tr,nextTr,table);
				this.processTableDatasChangeToDown(table,tr.data("index"),nextTr.data("index"));
				if(table.editable){
					this.processEditableTrChangedStatus(table,tr,forceTrChange);
					this.processEditableTrChangedStatus(table,nextTr,forceTrChange);
				}
//					if(table.left_fixed){
//						var leftTr=table.left_fixed.body.table.find("tbody>tr:eq("+trIndex+")");
//						if(isOk(leftTr)){
//							trChangeToDown(leftTr,leftTr.next());
//						}
//					}
//					if(table.right_fixed){
//						var rightTr=table.right_fixed.body.table.find("tbody>tr:eq("+trIndex+")");
//						if(isOk(rightTr)){
//							trChangeToDown(rightTr,rightTr.next());
//						}
//					}
			}
		},
		//得到实例
		inst:function(tableId){
			if(!tableId&&this.hasClass("jbolt_table")){
				tableId=this.attr("id");
			}
			return JBoltTableInts[tableId];
		},
		//设置实例
		put:function(tableId,table){
			JBoltTableInts[tableId]=table;
		},

		//删除实例
		remove:function(table){
			var tableId;
			if(!table){
				tableId=this.attr("id");
			}else{
				if(typeof(table)=="string"){
					tableId=table;
				}else{
					tableId=table.attr("id");
				}
			}

			if(tableId){
				var ist = JBoltTableInts[tableId];
				if(ist){
					delete JBoltTableInts[tableId];
				}
				ist.table_view.remove();
			}
		},
		/**
		 * 处理toolbar 或者其他地方绑定过来的按钮 比如右上角的添加按钮
		 */
		processBindEleTableId:function(table){
			var tableId=table.attr("id");
			var bind_elements=table.data("bind-elements");
			if(bind_elements){
				$(bind_elements).data("jbolt-table-id",tableId).attr("data-jbolt-table-id",tableId);
				$(bind_elements).find("a,button").data("jbolt-table-id",tableId).attr("data-jbolt-table-id",tableId);
			}
			if(isOk(table.toolbar)){
				table.toolbar.find("button,a").data("jbolt-table-id",tableId).attr("data-jbolt-table-id",tableId);
			}
			var jboltPage=table.closest(".jbolt_page");
			if(isOk(jboltPage)){
				jboltPage.find(".jbolt_page_title a:not([data-jbolt-table-id]),.jbolt_page_title button:not([data-jbolt-table-id])").data("jbolt-table-id",tableId).attr("data-jbolt-table-id",tableId);
			}
		},
		checkTableThead:function(table){
			var thead=table.find("thead");
			if(!isOk(thead)){
				return "Table表格不能缺少thead";
			}
			var trs=thead.find("tr");
			if(!isOk(trs)){
				return "Table表格Thead中必须有tr";
			}
			var ths=thead.find("tr>th");
			if(!isOk(ths)){
				LayerMsgBox.alert("Table表格Thead中必须有th",2);
				return false;
			}
		},
		initTableByJsonOption:function(table,tableOptionStr){
			if(!isOk(tableOptionStr)){
				var isAjax=table.data("ajax");
				table.isAjax=(typeof(isAjax)=="undefined"?false:isAjax);
				return;
			}
			var optionFunc=eval(tableOptionStr);
			if(!optionFunc||typeof(optionFunc)!="function"){
				LayerMsgBox.alert("表格data-option设置异常，值应为一个function",2);
				return;
			}
			var tableOptions=optionFunc(table);
			if(!tableOptions){
				LayerMsgBox.alert("表格data-option设置function 返回值不正确",2);
				return;
			}
			table.options = tableOptions;
			if(tableOptions.ajax || tableOptions.url){
				table.isAjax = true;
				table.data("ajax",true).attr("data-ajax",true);
				table.data("url",tableOptions.url?tableOptions.url:"").attr("data-url",tableOptions.url?tableOptions.url:"");
			}
			this.initTableColumnByJsonOption(table,tableOptions);
			this.initRowTplContent(table,tableOptions);
			if(tableOptions.width){
				table.data("width",tableOptions.width).attr("data-width",tableOptions.width);
			}
			if(tableOptions.height){
				table.data("height",tableOptions.height).attr("data-height",tableOptions.height);
			}
			if(tableOptions.form){
				table.data("conditions-form",tableOptions.form).attr("data-conditions-form",tableOptions.form);
			}
			if(tableOptions.toolbar){
				table.data("toolbar",tableOptions.toolbar).attr("data-toolbar",tableOptions.toolbar);
			}
			if(tableOptions.headbox){
				table.data("headbox",tableOptions.headbox).attr("data-headbox",tableOptions.headbox);
			}
			if(tableOptions.leftbox){
				table.data("leftbox",tableOptions.leftbox).attr("data-leftbox",tableOptions.leftbox);
			}
			if(tableOptions.rightbox){
				table.data("rightbox",tableOptions.rightbox).attr("data-rightbox",tableOptions.rightbox);
			}
			if(tableOptions.footbox){
				table.data("footbox",tableOptions.footbox).attr("data-footbox",tableOptions.footbox);
			}
			if(typeof(tableOptions.page)=="boolean" && tableOptions.page){
				var pageId="table_page_"+randomId();
				table.data("page",pageId).attr("data-page",pageId);
			}else if(typeof(tableOptions.page)=="string" && tableOptions.page){
				table.data("page",tableOptions.page).attr("data-page",tableOptions.page);
			}

			if(tableOptions.editable){
				table.data("editable",true).attr("data-editable",true);
				table.data("editable-option",tableOptions.editable);
			}
			if(tableOptions.menu){
				this.initTableMenuOptions(table,tableOptions.menu)
			}

		},
		initTableMenuOptions:function(table,menuOptions){
			this.processTableMenuFilterBoxCustom(table,menuOptions.menus);
			table.menuOptions=menuOptions;
		},
		initTableColumnByJsonOption:function(table,tableOptions){
			if(!tableOptions){
				tableOptions = table.options;
			}
			if(tableOptions.columnResize){
				table.data("column-resize",true).attr("data-column-resize",tableOptions.columnResize);
			}
			if(!isOk(tableOptions.cols)){
				return;
			}
			var cols = tableOptions.cols;
			var thead = table.find("thead");
			if(isOk(thead)){
				thead.empty();
			}else{
				thead = $("<thead></thead>");
				table.prepend(thead);
			}

			var str="<tr>"
			var sortColumnsArr=[],dataSort,columnPrependArr=[],columnPrependStr,leftFixedColArr=[],rightFixedColArr=[];
			$.each(cols,function(i,col){
				if(col.fixed){
					if(col.fixed == "left"){
						leftFixedColArr.push(i+1);
					}else if(col.fixed == "right"){
						rightFixedColArr.push(i+1);
					}
				}
				if(col.column=="checkbox" || col.column=="radio"){
					columnPrependArr.push(col.index?col.index:1);
					columnPrependArr.push(col.column);
					columnPrependArr.push(col.rowspan?col.rowspan:1);
					columnPrependArr.push(col.clickChecked?true:false);
					columnPrependArr.push(col.linkParent?true:false);
					columnPrependArr.push(col.linkSon?true:false);
					columnPrependStr=columnPrependArr.join(":");
					table.data("column-prepend",columnPrependStr).attr("data-column-prepend",columnPrependStr);
					return true;
				}


				str=str+"<th "+(typeof(col.minWidth)=="number"?" data-min-width='"+col.minWidth+"'":'')+" "+(typeof(col.width)=="number"?" data-width='"+col.width+"'":'')+" data-column='"+col.column+"'>"+col.title+"</th>";
				if(col.sort){
					sortColumnsArr.push(col.column);
					if(typeof(col.sort)=="object"){
						if(col.sort.isDefault){
							table.data("default-sort-column",col.column).attr("data-default-sort-column",col.column);
							dataSort = col.column+":"+(col.sort.type?col.sort.type:"asc");
							table.data("sort",dataSort).attr("data-sort",dataSort);
						}
					}
				}
			});
			str+="</tr>";
			thead.append(str);
			if(isOk(sortColumnsArr)){
				var sortColumns=sortColumnsArr.join(",");
				table.data("sortable-columns",sortColumns).attr("data-sortable-columns",sortColumns);
			}

			if(isOk(leftFixedColArr)){
				var leftFixedCols = leftFixedColArr.join(",");
				table.data("fixed-columns-left",leftFixedCols).attr("data-fixed-columns-left",leftFixedCols);
			}
			if(isOk(rightFixedColArr)){
				var rightFixedCols = rightFixedColArr.join(",");
				table.data("fixed-columns-right",rightFixedCols).attr("data-fixed-columns-right",rightFixedCols);
			}
		},
		//初始化表格的rowTplContent
		initRowTplContent:function(table,tableOptions){
			if(!tableOptions){
				tableOptions = table.options;
			}
			if(tableOptions.rowtpl){
				table.data("rowtpl",tableOptions.rowtpl).attr("data-rowtpl",tableOptions.rowtpl);
				return;
			}
			if(!isOk(tableOptions.cols)){
				return;
			}
			var cols = tableOptions.cols;
			var str ='{@each datas as data,index}';
			str = str+ '<tr data-id="${data.'+(tableOptions.primaryKey?tableOptions.primaryKey:"id")+'}">';
			$.each(cols,function(i,col){
				if(col.column=="checkbox" || col.column == "radio"){
					return true;
				}
				if(col.column == "index"){
					if(col.valueTpl){
						str = str + "<td>" + col.valueTpl + "</td>";
					}else{
						str = str + "<td>${pageNumber,pageSize,index|rownum}</td>";
					}
					return true;
				}
				if(col.valueTpl){
					str = str + "<td>" + col.valueTpl + "</td>";
				}else{
					str = str + "<td>${data." + ( col.valueAttr?col.valueAttr:StrUtil.camel(col.column) ) + "}</td>";
				}
			});
			str+="</tr>"
			str+="{@/each}";
			table.rowtplContent = str;
		},
		hideTableBox:function(table,type){
			var box;
			switch(type){
				case "headbox":
					box=table.headbox;
					break;
				case "footbox":
					box=table.footbox;
					break;
				case "leftbox":
					box=table.leftbox;
					break;
				case "rightbox":
					box=table.rightbox;
					break;
			}
			if(!isOk(box)){
				LayerMsgBox.alert("没找到对应Box",2);
				return;
			}
			box.hide();
			box.addClass("d-none");
			this.resize(table);
		},
		showTableBox:function(table,type){
			var box;
			switch(type){
				case "headbox":
					box=table.headbox;
					break;
				case "footbox":
					box=table.footbox;
					break;
				case "leftbox":
					box=table.leftbox;
					break;
				case "rightbox":
					box=table.rightbox;
					break;
			}
			if(!isOk(box)){
				LayerMsgBox.alert("没找到对应Box",2);
				return;
			}
			box.show();
			box.removeClass("d-none");
			this.resize(table);
		},
		toggleTableBox:function(table,type){
			var box;
			switch(type){
				case "headbox":
					box=table.headbox;
					break;
				case "footbox":
					box=table.footbox;
					break;
				case "leftbox":
					box=table.leftbox;
					break;
				case "rightbox":
					box=table.rightbox;
					break;
			}
			if(!isOk(box)){
				LayerMsgBox.alert("没找到对应Box",2);
				return;
			}
			box.toggle();
			box.toggleClass("d-none");
			this.resize(table);
		},
		//初始化
		init: function (tables) {
			var that=this;
			LayerMsgBox.load(3,15000);
			return tables.each(function(){
				var table=$(this);
				var tableId=table.attr("id");
				if(!tableId){
					tableId="jbolt_table_"+randomId();
					table.attr("id",tableId);
				}else{
					var existTable=that.inst(tableId);
					if(existTable){
						LayerMsgBox.closeLoadNow();
						return true;
					}
				}
				var tableJsonOption = table.data("option");
				if(isOk(tableJsonOption)){
					//通过纯JSON配置
					that.initTableByJsonOption(table,tableJsonOption);
				}else{
					var isAjax=table.data("ajax");
					table.isAjax=(typeof(isAjax)=="undefined"?false:isAjax);
				}
				//判断表格缺陷
				var errorMsg=that.checkTableThead(table);
				if(errorMsg){
					LayerMsgBox.closeLoadNow();
					LayerMsgBox.alert(errorMsg,2);
					return false;
				}

				var theme=table.data("theme");
				if(!theme){
					theme="jbolttable";
					table.attr("data-theme",theme).data("theme",theme);
				}
				table.addClass("table table-bordered table-default");
				if(theme=="bootstrap"){

				}else{
//						table.removeClass("table table-bordered table-hover table-striped table-dark table-borderless table-sm table-primary table-secondary table-success table-danger table-warning table-info table-light");
				}
				table.theme=theme;

				table.thead=table.find("thead");
				if(isOk(table.thead)){
					table.hasHeader=true;
					table.thead.trs=table.thead.find("tr");
					var tableTheadHeight = table.thead.data("height");
					if(tableTheadHeight){
						table.thead._height = parseInt(tableTheadHeight);
						table.thead.css("height",table.thead._height+"px");
					}else{
						if(table.hasClass("thin")){
							table.thead._height=(table.theme=="bootstrap"?32:31)*table.thead.trs.length;
						}else if(table.hasClass("middle")){
							table.thead._height=(table.theme=="bootstrap"?37:36)*table.thead.trs.length;
						}else{
							table.thead._height=(table.theme=="bootstrap"?42:41)*table.thead.trs.length;
						}
						if(table.thead.trs.length>=3){
							if(table.data("editable")){
								table.thead._height = table.thead._height - table.thead.trs.length;
							}else{
								table.thead._height = table.thead._height - table.thead.trs.length + 1 ;
							}
						}
					}
				}else{
					table.hasHeader=false;
				}
				table.tbody=table.find("tbody");
				table.hasBody=false;
				if(isOk(table.tbody)){
					table.hasBody=true;
				}else{
					table.append("<tbody></tbody>");
					table.tbody=table.find("tbody");
					table.hasBody=true;
				}
				table.tfoot=table.find("tfoot");
				table.tfootFixed=table.data("tfoot-fixed")||false;
				if(isOk(table.tfoot)){
					table.hasFooter=true;
					table.tfoot.trs=table.tfoot.find("tr");
					var tableTfootHeight = table.tfoot.data("height");
					if(tableTfootHeight){
						table.tfoot._height = parseInt(tableTfootHeight);
						table.tfoot.css("height",table.tfoot._height+"px");
					}else {
						if (table.hasClass("thin")) {
							table.tfoot._height = (table.theme == "bootstrap" ? 28 : 29) * table.tfoot.trs.length;
						} else if (table.hasClass("middle")) {
							table.tfoot._height = (table.theme == "bootstrap" ? 37 : 36) * table.tfoot.trs.length;
						} else {
							table.tfoot._height = (table.theme == "bootstrap" ? 42 : 41) * table.tfoot.trs.length;
						}
					}

				}else{
					table.hasFooter=false;
					table.tfootFixed=false;
				}

				table.addClass("jbolt_table text-nowrap novscroll jbolt_main_table");
				var jboltInputLayer = table.closest(".jbolt_input_layer");
				var inJBoltInputLayer = isOk(jboltInputLayer);
				var table_body;
				if(inJBoltInputLayer){
					table_body = jboltInputLayer.find(".jbolt_table_body");
				}else{
					table_body = table.closest(".jbolt_table_body");
				}

				if(!isOk(table_body)){
					//如果有用这个的 就给他清除掉
					var table_parent=table.parent();
					if(table_parent.hasClass("table-responsive")){
						table_parent.removeClass("table-responsive").addClass("jbolt_table_body");
						table_parent.data("theme",table.theme).attr("data-theme",table.theme);
						table_body = table_parent;
					}else{
						table.wrap("<div data-theme='"+table.theme+"' class='jbolt_table_body'></div>");
						table_body=table.closest(".jbolt_table_body");
					}
				}else{
					table_body.attr("data-theme",table.theme).data("theme",table.theme);
				}
				var table_box;
				if(inJBoltInputLayer){
					table_box = jboltInputLayer.find(".jbolt_table_box");
				}else{
					table_box = table_body.closest(".jbolt_table_box");
				}
				if(!isOk(table_box)){
					table_body.wrap("<div data-theme='"+table.theme+"' class='jbolt_table_box'></div>");
					table_box=table_body.closest(".jbolt_table_box");
				}else{
					table_box.attr("data-theme",table.theme).data("theme",table.theme);
				}

				var table_view;
				if(inJBoltInputLayer){
					table_view = jboltInputLayer.find(".jbolt_table_view");
				}else{
					table_view = table_box.closest(".jbolt_table_view");
				}
				if(!isOk(table_view)){
					table_box.wrap("<div data-theme='"+table.theme+"' class='jbolt_table_view'></div>");
					table_view=table_box.closest(".jbolt_table_view");
				}else{
					table_view.attr("data-theme",table.theme).data("theme",table.theme);
				}

				var toolbar=table_view.find(".jbolt_table_toolbar");
				if(!isOk(toolbar)){
					var toolbarId=table.data('toolbar');
					if(toolbarId){
						var pbox=table_view.closest("[data-ajaxportal]");
						if(!isOk(pbox)){
							if(jboltWithTabs){
//										if(isWithtabs()){
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
						if(isOk(pbox)){
							toolbar=pbox.find("#"+toolbarId);
						}else{
							toolbar=$("#"+toolbarId);
						}
					}
					if(isOk(toolbar)){
						toolbar.insertBefore(table_box);
						toolbar.attr("data-theme",table.theme).data("theme",table.theme);
					}
				}


				var pbox=table_view.closest("[data-ajaxportal]");
				if(!isOk(pbox)){
//						if(isWithtabs()){
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



				//headbox
				var headbox=table_view.find(".jbolt_table_headbox");
				if(!isOk(headbox)){
					var headboxId=table.data('headbox');
					if(headboxId){
						if(isOk(pbox)){
							headbox=pbox.find("#"+headboxId);
						}else{
							headbox=$("#"+headboxId);
						}
					}
					if(isOk(headbox)){
						//如果设置了toolbar始终在top位置
						var toolbaralwaysontop=table.data("toolbar-alwaysontop");
						if(toolbaralwaysontop){
							if(isOk(toolbar)){
								toolbar.after(headbox);
							}else{
								table_view.prepend(headbox);
							}
						}else{
							table_view.prepend(headbox);
						}
						headbox.attr("data-theme",table.theme).data("theme",table.theme);

					}
				}


				//leftbox
				var leftbox=table_view.find(".jbolt_table_leftbox");
				if(!isOk(leftbox)){
					var leftboxId=table.data('leftbox');
					if(leftboxId){
						if(isOk(pbox)){
							leftbox=pbox.find("#"+leftboxId);
						}else{
							leftbox=$("#"+leftboxId);
						}
					}
					if(isOk(leftbox)){
						leftbox.attr("data-theme",table.theme).data("theme",table.theme);
						if(table.hasClass("thin")){
							leftbox.addClass("thin");
						}else if(table.hasClass("middle")){
							leftbox.addClass("middle");
						}else if(table.hasClass("tbody_lh20")){
							leftbox.addClass("tbody_lh20");
						}
						table_box.before(leftbox);

						var left_body=leftbox.find(".jb_body");
						if(isOk(left_body)){
							var minus_height=0;
							var left_header=leftbox.find(".jb_header");
							var left_footer=leftbox.find(".jb_footer");
							if(isOk(left_header)){
								minus_height=minus_height+left_header.outerHeight();
							}
							if(isOk(left_footer)){
								minus_height=minus_height+left_footer.outerHeight();
							}
							left_body.css({"height":"calc(100% - "+minus_height+"px)"});
						}
					}
				}



				//rightbox
				var rightbox=table_view.find(".jbolt_table_rightbox");
				if(!isOk(rightbox)){
					var rightboxId=table.data('rightbox');
					if(rightboxId){
						if(isOk(pbox)){
							rightbox=pbox.find("#"+rightboxId);
						}else{
							rightbox=$("#"+rightboxId);
						}
					}
					if(isOk(rightbox)){
						rightbox.attr("data-theme",table.theme).data("theme",table.theme);
						if(table.hasClass("thin")){
							rightbox.addClass("thin");
						}else if(table.hasClass("middle")){
							rightbox.addClass("middle");
						}else if(table.hasClass("tbody_lh20")){
							rightbox.addClass("tbody_lh20");
						}
						table_box.before(rightbox);

						var right_body=rightbox.find(".jb_body");
						if(isOk(right_body)){
							var minus_height=0;
							var right_header=rightbox.find(".jb_header");
							var right_footer=rightbox.find(".jb_footer");
							if(isOk(right_header)){
								minus_height=minus_height+right_header.outerHeight();
							}
							if(isOk(right_footer)){
								minus_height=minus_height+right_footer.outerHeight();
							}
							right_body.css({"height":"calc(100% - "+minus_height+"px)"});
						}
					}
				}

				//footbox
				var footbox=table_view.find(".jbolt_table_footbox");
				if(!isOk(footbox)){
					var footboxId=table.data('footbox');
					if(footboxId){
						if(isOk(pbox)){
							footbox=pbox.find("#"+footboxId);
						}else{
							footbox=$("#"+footboxId);
						}
					}
					if(isOk(footbox)){
						table_box.after(footbox);
						footbox.attr("data-theme",table.theme).data("theme",table.theme);
					}
				}
				if(table.hasClass("border-dark")){
					table_view.addClass("border-dark");
					table_box.addClass("border-dark");
					table_body.addClass("border-dark");
					if(isOk(toolbar)){
						toolbar.addClass("border-dark");
					}
					if(isOk(footbox)){
						footbox.addClass("border-dark");
					}
				}else if(table.hasClass("border-secondary")){
					table_view.addClass("border-secondary");
					table_box.addClass("border-secondary");
					table_body.addClass("border-secondary");
					if(isOk(toolbar)){
						toolbar.addClass("border-secondary");
					}
					if(isOk(footbox)){
						footbox.addClass("border-secondary");
					}
				}else if(table.hasClass("border-gray")){
					table_view.addClass("border-gray");
					table_box.addClass("border-gray");
					table_body.addClass("border-gray");
					if(isOk(toolbar)){
						toolbar.addClass("border-gray");
					}
					if(isOk(footbox)){
						footbox.addClass("border-gray");
					}
				}
				table.table_view=table_view;
				table.table_box=table_box;
				table.table_body=table_body;
				table.toolbar=toolbar;
				table.headbox=headbox;
				table.footbox=footbox;
				table.leftbox=leftbox;
				table.rightbox=rightbox;

				var autoload=table.data("autoload");
				var autoloadType=typeof(autoload);
				if((autoloadType=="undefined")||(autoloadType=="string"&&!autoload)){
					autoload=true;
				}
				table.autoload=autoload;
				table.isMainTable=true;
				//初始化表格的数据集合为空
				table.tableListDatas=[];
				table.me=that;
				//put到实例中
				that.put(tableId,table);
				table.data("inited",false);
				if(table.isAjax){
					table.resetCellWidthAfterAjax=true;
					that.processTableMainBeforeAjax(table);
					if(table.autoload){
						//处理ajax读取数据后 再执行处理表格
						that.ajaxLoadTableData(table,function(){
							table.resetCellWidthAfterAjax=true;
							that.reProcessTableMain(table,function(){
								table.data("inited",true);
							});

							//触发行补全
							//table.trigger("fillMinCountRows");
							that.initEditableCheckboxDataInfo(table);
							that.initEditableSwitchBtnDataInfo(table);
						});
					}else{
						table.data("inited",true);
					}
				}else{
					//处理table样式与事件主入口
					that.processTableMain(table);
					table.data("inited",true);
					//触发行补全
					//table.trigger("fillMinCountRows");
					that.processEditableTableFillMinCountRows(table);
				}

			});
		},
		processPagesToJboltPage:function(table){
			//如果没用 判断是不是用错了 直接使用了_page.html
			var pages=table.table_box.find(".pages"),jbolt_table_pages;
			if(isOk(pages)){
				jbolt_table_pages=pages.closest(".jbolt_table_pages");
				if(!isOk(jbolt_table_pages)){
					pages.attr("class","pages").wrap('<div data-theme="'+table.theme+'" class="jbolt_table_pages noselect"></div>');
				}
			}else{
				//如果没有 就说明压根没在view里 就得找找外面了
				var pageId=table.data("page");
				if(pageId){
					pages=table.closest(".jbolt_page").find("#"+pageId).closest(".pages");
				}else{
					pages=table.table_box.parent().find(".pages");
				}
				if(isOk(pages)){
					jbolt_table_pages=pages.closest(".jbolt_table_pages");
					if(!isOk(jbolt_table_pages)){
						pages.attr("class","pages").wrap('<div data-theme="'+table.theme+'" class="jbolt_table_pages noselect"></div>');
						jbolt_table_pages=pages.closest(".jbolt_table_pages");
					}
					table.table_box.append(jbolt_table_pages)
				}
			}

			if(isOk(jbolt_table_pages)){
				var input=jbolt_table_pages.find("#gonu");
				if(isOk(input)){
					pageNumber=Math.abs(parseInt(input.val()));
				}else{
					pageNumber=1;
				}
				var pageSize=Math.abs(parseInt(jbolt_table_pages.find("#pageSize").val()));
				var totalPage=Math.abs(parseInt(jbolt_table_pages.find("#totalPage").text()));
				table.data("pagenumber",pageNumber);
				table.data("pagesize",pageSize);
				table.data("totalpage",totalPage);
			}
		},
		getColumnPrependElement:function(columnType,tagName,rowspan){
			var html=prependTplMap[tagName+"_"+columnType];
			if(tagName=="th"){
				rowspan=rowspan?rowspan:1;
				if(html){
					html=html.replace("[rowspan]",rowspan);
				}else{
					html='';
				}
			}
			return html;
		},
		getTbodyColumnPrependElement:function(columnType){
			var result="";
			switch (columnType) {
				case "checkbox":
					retult="<td><input type='checkbox' data-type='tbody_checkbox'/></td>";
					break;
				case "radio":
					retult="<td><input type='radio' data-type='tbody_radio'/></td>";
					break;
			}
			return result;
		},
		initTableColumnPrepend:function(table){
			//处理Thead添加列操作 比如在第一列添加checkbox radio等
			var columnPrepend=table.data("column-prepend");
			if(!columnPrepend){return false;}
			var that=this,columnIndex=0,columnType,rowspan=0,clickRowAndChecked=false,linkParent=false,linkSon=false;
			if(columnPrepend.indexOf(":")!=-1){
				var arr=columnPrepend.split(":");
				if(isNaN(arr[0])){
					//不是数字
					columnType=arr[0];
					if(arr.length>=2){
						if(!isNaN(arr[1])){
							rowspan=parseInt(arr[1]);
							if(arr.length>=3){
								clickRowAndChecked = (arr[2]=='true');
								if(arr.length>=4){
									linkParent = (arr[3]=='true');
									if(arr.length==5){
										linkSon = (arr[4]=='true');
									}
								}
							}
						}else{
							clickRowAndChecked = (arr[1]=='true');
							if(arr.length>=3){
								linkParent = (arr[2]=='true');
								if(arr.length==4){
									linkSon = (arr[3]=='true');
								}
							}
						}
					}
				}else{
					columnIndex=parseInt(arr[0])-1;
					columnType=arr[1];
					if(arr.length>=3){
						if(!isNaN(arr[2])){
							rowspan=parseInt(arr[2]);
							if(arr.length>=4){
								clickRowAndChecked = (arr[3]=='true');
								if(arr.length>=5){
									linkParent = (arr[4]=='true');
									if(arr.length==6){
										linkSon = (arr[5]=='true');
									}
								}
							}
						}else{
							clickRowAndChecked = (arr[2]=='true');
							if(arr.length>=4){
								linkParent = (arr[3]=='true');
								if(arr.length==5){
									linkSon = (arr[4]=='true');
								}
							}
						}

					}
				}

			}else{
				columnType=columnPrepend;
			}
			table.prependColumnIndex=columnIndex;
			table.prependColumnType=columnType;
			table.prependRowspan=rowspan;
			table.clickRowAndChecked=clickRowAndChecked;
			table.linkParent=linkParent;
			table.linkSon=linkSon;

		},
		processTableColumnPrependInNormalTable:function(table){
			//处理表格添加checkbox和radio 在普通表格里调用
			//初始化
			this.initTableColumnPrepend(table);
			//处理table thead 列补充类型 额外添加的
			this.processTableTheadColumnPrepend(table);
			//处理table tbody 列补充类型 额外添加的
			this.processTableTbodyColumnPrepend(table);
		},
		processTableTbodyColumnPrepend:function(table){
			//处理Tbody添加列操作 比如在第一列添加checkbox等
			if(!table.prependColumnType || table.isEmpty){return false;}
			var tds=table.tbody.find("tr>td:nth-child("+(table.prependColumnIndex+1)+")");
			if(isOk(tds)){
				tds.before(this.getColumnPrependElement(table.prependColumnType,"td"));
				this.processColumnPrependDisabled(table);
			}

		},
		processColumnPrependDisabled:function(table){
			if(!table.prependColumnType || table.isEmpty){return false;}
			table.tbody.find("tr.disabled>td:nth-child("+(table.prependColumnIndex+1)+")>.jbolt_table_"+table.prependColumnType+">input").attr("disabled","disabled").closest("td").attr("title","禁选").tooltip({ boundary: 'window',container:"body"});
		},
		processTrsColumnPrependDisabled:function(table,trs){
			if(!table.prependColumnType || notOk(trs)){return false;}
			var size = trs.length;
			for(var i=0;i<size;i++){
				this.processTrColumnPrependDisabled(table,trs.eq(i));
			}
		},
		processTrColumnPrependDisabled:function(table,tr){
			if(!table.prependColumnType || notOk(tr)){return false;}
			if(!tr.hasClass("disabled")){return;}
			tr.find("td:nth-child("+(table.prependColumnIndex+1)+")>.jbolt_table_"+table.prependColumnType+">input").attr("disabled","disabled").closest("td").attr("title","禁选").tooltip({ boundary: 'window',container:"body"});
		},
		processTableTrsColumnPrepend:function(table,trs){
			//处理Tbody添加列操作 比如在第一列添加checkbox等
			if(!table.prependColumnType || table.isEmpty || !isOk(trs)){return false;}
			var tds=trs.find("td:nth-child("+(table.prependColumnIndex+1)+")");
			if(isOk(tds)){
				tds.before(this.getColumnPrependElement(table.prependColumnType,"td"));
				tds.prev().data("col-index",table.prependColumnIndex).attr("data-col-index",table.prependColumnIndex);
				this.processTrsColumnPrependDisabled(table,trs);
			}
		},
		processTableTheadColumnPrepend:function(table,initPrepend){
			if(initPrepend){
				//初始化
				this.initTableColumnPrepend(table);
			}
			if(!table.prependColumnType || !isOk(table.thead)){return false;}
			var th=table.thead.find("tr:first>th:eq("+table.prependColumnIndex+")");
			if(isOk(th)){
				if(!table.prependRowspan){
					var firstth=table.find("thead>tr:first>th:eq(0)");
					table.prependRowspan=isOk(firstth)?(firstth.attr("rowspan")||1):1;
				}
				th.before(this.getColumnPrependElement(table.prependColumnType,"th",table.prependRowspan));
			}
		},
		/**
		 * 处理table样式与事件主入口
		 */
		processTableMain:function(table){
			var that=this;
			//如果勿用了_page.html没用_jbolt_table_page.html会自动处理
			that.processPagesToJboltPage(table);
			//处理tableHeader中的列头排序特效
			that.processTableHeaderColSort(table);
			//处理table 列补充类型 额外添加的
			that.processTableColumnPrependInNormalTable(table);
			//处理thIndex
			that.processTableColIndex(table,true,true);
			//处理宽高
			that.processTableWidthAndHeight(table);
			//处理单元格宽度与样式
			that.processTableStyle(table);
			//关联的条件查询form处理
			that.processConditionsForm(table);
			//设置其他table绑定tableId
			that.processOtherTableBindTableId(table);
			//设置绑定组件的tableId
			that.processBindEleTableId(table);
			//处理fixed column中的元素
			that.afterFixedColumn(table);
			//处理普通表格的tableListDatas
			that.processNormalTableListDatas(table);
			//processEditable
			that.processEditable(table);
			//处理选中checkbox数据
			that.processTableKeepSelectedItems(table);
			//处理事件
			that.processTableEvent(table);
			setTimeout(function(){
				//处理空的tbody
				that.processEmptyTableBody(table);
			}, 1000);
			LayerMsgBox.closeLoadNow();
		},
		//处理表格选中保持数据
		processTableKeepSelectedItems:function(table){
			var keepSelectedItemsEnable = table.data("keep-selected-items");
			if(typeof(keepSelectedItemsEnable)=="undefined"){
				keepSelectedItemsEnable = false;
			}
			if(!keepSelectedItemsEnable){
				return;
			}
			table.keepSelectedItemsEnable = keepSelectedItemsEnable;
			//如果开启了之后 就需要获取值
			var selectedItemsBoxId = table.data("selected-items-box");
			if(!selectedItemsBoxId){
				JBoltMsgBox.alert("未配置选中数据显示的区域data-selected-items-box",2);
				return;
			}
			var selectedItemsBox = jboltBody.find("#"+selectedItemsBoxId);
			if(notOk(selectedItemsBox)){
				JBoltMsgBox.alert("data-selected-items-box设置的ID无效",2);
				return;
			}
			table.selectedItemsBox = selectedItemsBox;

			var selectedItemsTplId = table.data("selected-items-tpl");
			if(!selectedItemsTplId){
				JBoltMsgBox.alert("未配置选中数据的渲染模板data-selected-items-tpl",2);
				return;
			}
			var selectedItemsTplBox = g(selectedItemsTplId);
			if(!selectedItemsTplBox){
				JBoltMsgBox.alert("选中数据的渲染模板配置有误data-selected-items-tpl",2);
				return;
			}
			var selectedItemsTpl;
			if(selectedItemsTplBox.tagName=="TEXTAREA"){
				selectedItemsTpl = selectedItemsTplBox.val();
			}else if(selectedItemsTplBox.tagName=="SCRIPT"){
				selectedItemsTpl = selectedItemsTplBox.innerText;
			}
			if(!selectedItemsTpl){
				JBoltMsgBox.alert("选中数据的渲染模板配置有误 无任何有效模板数据",2);
				return;
			}
			table.selectedItemsTpl = selectedItemsTpl;

			table.selectedItemValueAttr = table.data("selected-item-value-attr")||"id";
			table.selectedItemTextAttr = table.data("selected-item-text-attr")||"name";
			var selectedItemsTotalSpanId = table.data("selected-items-total");
			if(selectedItemsTotalSpanId){
				var selectedItemsTotalSpan = jboltBody.find("#"+selectedItemsTotalSpanId);
				if(isOk(selectedItemsTotalSpan)){
					selectedItemsTotalSpan.text("0");
					table.selectedItemsTotal = selectedItemsTotalSpan;
				}
			}

		},
		//处理普通表格携带的数据转tableListDatas
		processNormalTableListDatas:function(table){
			var tableDatas=table.data("tabledatas");
			if(!tableDatas){return;}
			if(window[tableDatas]){
				var exe_handler=eval(tableDatas);
				if(exe_handler&&typeof(exe_handler)=="function"){
					table.tableListDatas = exe_handler();
				}
			}else{
				var jsonDatas=JSON.parse(tableDatas);
				if(isOk(jsonDatas)){
					table.tableListDatas=jsonDatas;
				}
			}
		},
		//重新设置默认排序列样式
		reProcessTableHeaderColDefaultSort:function(table){
			var thead=table.find("thead");
			var sortableColumns=table.data("sortable-columns");
			if(!sortableColumns){
				return false;
			}
			var th,sortColumn,sortType,defaultSort=table.data("sort");
			if(defaultSort){
				var dsarr=defaultSort.split(":");
				sortColumn=dsarr[0];
				sortType=dsarr[1];
			}
			table.table_box.find(".jbolt_table_header>table>thead>tr>th i.sort.active").removeClass("active");
			th=thead.find("tr>th[data-column='"+sortColumn+"']");
			if(isOk(th)){
				var thColIndex=th.data("col-index");
				table.table_box.find(".jbolt_table_header>table>thead>tr>th[data-col-index='"+thColIndex+"']").find(".sort.sort_"+sortType).addClass("active");
				th.find(".sort.sort_"+sortType).addClass("active");
			}
		},
		//处理初始化列头排序样式
		processTableHeaderColSort:function(table){
			var thead=table.find("thead");
			var sortableColumns=table.data("sortable-columns");
			if(!sortableColumns){
				return false;
			}
			//如果table上设置了可以排序的列 就去找绑定的这些列 设置样式
			var columnsArr=sortableColumns.split(",");
			if(!isOk(columnsArr)){return false;}
			var len=columnsArr.length;
			var th,sortColumn,sortType,defaultSort=table.data("sort");
			if(defaultSort){
				var dsarr=defaultSort.split(":");
				sortColumn=dsarr[0];
				sortType=dsarr[1];
			}
			for(var i=0;i<len;i++){
				th=thead.find("tr>th[data-column='"+columnsArr[i]+"']");
				if(isOk(th)){
					th.addClass("sort_col");
					if(sortColumn==columnsArr[i]){
						if(sortType=="asc"){
							th.append(JboltColSort_asc_Tpl);
						}else if(sortType=="desc"){
							th.append(JboltColSort_desc_Tpl);
						}
					}else{
						th.append(JboltColSortTpl);
					}
				}
			}


		},
		processTheadColIndex:function(table){
			this.processTableColIndex(table,true,false);
			if(!table.theadWithColIndexThs){
				table.theadWithColIndexThs=table.thead.find("th[data-col-index]");
			}
		},
		processNotRowSpanFirstTr:function(tbody,startIndex,endIndex){
			var tempTr;
			for(var i=startIndex;i<=endIndex;i++){
				tempTr=tbody.find("tr:nth-child("+i+")");
				if(isOk(tempTr)){
					tempTr.addClass("notRowSpanFirstTr");
				}
			}
		},
		//初始化列索引 这里还得考虑复杂表头的递归处理
		processTableColIndex:function(table,processHead,processBody){
			var that=this,thead=table.thead,tfoot=table.tfoot,tbody=table.tbody,trs=thead.find("tr"),tfootTrs,currentTr,trLen,newThs,thLen,currentTh;
			if(!isOk(trs)){
				LayerMsgBox.alert("尚未提供有效的Thead",2);
				return;
			}
			//几个tr
			trLen=trs.length;
			currentTr=trs.eq(0);
			var columnMap=processHead?{"columnToIndex":{},"indexToColumn":{},"indexToColumnText":{}}:null,tempColumnTh,tempColumnTh_column;
			if(trLen==1){
				newThs=currentTr.find("th");
				if(isOk(newThs)){
					thLen=newThs.length;
					for(var i=0;i<thLen;i++){
						if(processHead){
							tempColumnTh=newThs.eq(i);
							tempColumnTh_column=tempColumnTh.data("column");
							tempColumnTh.data("col-index",i).attr("data-col-index",i);
							if(tempColumnTh_column&&tempColumnTh_column!='index'&&tempColumnTh_column!='checkbox'&&tempColumnTh_column!='radio'){
								columnMap["columnToIndex"][tempColumnTh_column]=i;
								columnMap["indexToColumn"]["col_"+i]=tempColumnTh_column;
							}
							columnMap["indexToColumnText"]["col_"+i]=$.trim(tempColumnTh[0].innerText||("col_"+(i+1)));
							if(isOk(tfoot)){
								tfoot.find("tr>th:nth-child("+(i+1)+")").data("col-index",i).attr("data-col-index",i);
							}
						}
						if(processBody){
							var rowspans=tbody.find("tr>td[rowspan]:nth-child(1)");
							if(isOk(rowspans)){
								var tempTr,tempTd,trIndex,rowspan,startIndex,endIndex;
								rowspans.each(function(){
									tempTd=$(this);
									tempTr=$(this).closest("tr");
									tempTr.find("td:nth-child("+(i+1)+")").data("col-index",i).attr("data-col-index",i);
									trIndex=tempTr.index();
									rowspan=Number(tempTd.attr("rowspan"));
									startIndex=trIndex+1;
									endIndex=startIndex=rowspan;
									that.processNotRowSpanFirstTr(tbody,startIndex,endIndex);
								});
							}else{
								tbody.find("tr>td:nth-child("+(i+1)+")").data("col-index",i).attr("data-col-index",i);
							}
						}
					}

					if(processHead&&columnMap){
						table.columnIndexMap=columnMap["columnToIndex"];
						table.indexColumnMap=columnMap["indexToColumn"];
						table.indexColumnTextMap=columnMap["indexToColumnText"];
					}


				}
				return;
			}

			newThs=currentTr.find("th");
			if(isOk(newThs)){
				thLen=newThs.length;
				//循环TH 这里需要注意 有colspan的就要处理下级了
				var thIndex=0;//当前th的index
				var endIndex=0;
				var fixedIndex=1;//处理fixed
				var nextTrIndex=0,ncolspan,nrowspan=1;

				for(var i=0;i<thLen;i++){
					currentTh = newThs.eq(i);
					if(processHead){
						currentTh.data("fixed-col-index",fixedIndex).attr("data-fixed-col-index",fixedIndex);
					}
					if(currentTh[0].hasAttribute("colspan")){
						ncolspan=parseInt(currentTh.attr("colspan"));
						if(currentTh[0].hasAttribute("rowspan")){
							nrowspan = parseInt(currentTh.attr("rowspan"));
						}
						endIndex=thIndex+ncolspan-1;

						nextTrIndex = currentTr.index()+nrowspan;
						that.processColSpanNextTrThColIndex(table,thead,tbody,tfoot,nextTrIndex,thIndex,endIndex,fixedIndex,columnMap,processHead);
						if(processBody){
							for(var j=thIndex;j<=endIndex;j++){
								tbody.find("tr>td:nth-child("+(j+1)+")").data("col-index",j).attr("data-col-index",j);
								tbody.find("tr>td:nth-child("+(j+1)+")").data("fixed-col-index",fixedIndex).attr("data-fixed-col-index",fixedIndex);
							}
						}
						thIndex=endIndex+1;
					}else{
						if(processHead){
							currentTh.data("col-index",thIndex).attr("data-col-index",thIndex);
							tempColumnTh_column=currentTh.data("column");
							if(tempColumnTh_column&&tempColumnTh_column!='index'&&tempColumnTh_column!='checkbox'&&tempColumnTh_column!='radio'){
								columnMap["columnToIndex"][tempColumnTh_column]=thIndex;
								columnMap["indexToColumn"]["col_"+thIndex]=tempColumnTh_column;
							}
							columnMap["indexToColumnText"]["col_"+thIndex]=$.trim(currentTh[0].innerText||("col_"+(thIndex+1)));
							if(isOk(tfoot)){
								tfoot.find("tr>th:nth-child("+(thIndex+1)+")").data("col-index",thIndex).attr("data-col-index",thIndex);
							}

						}
						if(processBody){
							tbody.find("tr>td:nth-child("+(thIndex+1)+")").data("col-index",thIndex).attr("data-col-index",thIndex);
							tbody.find("tr>td:nth-child("+(thIndex+1)+")").data("fixed-col-index",fixedIndex).attr("data-fixed-col-index",fixedIndex);
						}
						thIndex=thIndex+1;
						endIndex=endIndex+1;
					}

					fixedIndex++;
				}

				if(processHead&&columnMap){
					table.columnIndexMap=columnMap["columnToIndex"];
					table.indexColumnMap=columnMap["indexToColumn"];
					table.indexColumnTextMap=columnMap["indexToColumnText"];

				}
			}



		},
		getTrOkThs:function(tr,start,end){
			var count = end-start+1;
			var thsArray = [];
			var hasCount=0;
			var first=tr.find("th:not(.processed):first");
			var ths;
			if(first.index()==0){
				ths=tr.find("th:lt("+count+"):not(.processed)");
			}else{
				ths=tr.find("th:gt("+(first.index()-1)+"):lt("+count+"):not(.processed)");
			}
			ths.each(function(i,th){
				if(th.hasAttribute("colspan")){
					hasCount = hasCount +parseInt(th.getAttribute("colspan"));
				}else{
					hasCount = hasCount +1;
				}
				if(hasCount<=count){
					thsArray.push($(th));
				}
			});
			return thsArray;
		},
		/**
		 * 处理下级tr里的th
		 */
		processColSpanNextTrThColIndex:function(table,thead,tbody,tfoot,trIndex,startColIndex,endColIndex,fixedIndex,columnMap,processHead){
			var that=this,tr=thead.find("tr:eq("+trIndex+")");
			if(!isOk(tr)){return false;}
			var ths=this.getTrOkThs(tr,startColIndex,endColIndex);
			if(!isOk(ths)){return false;}
			var thIndex=0,tempTh,tempColumnTh_column,size=ths.length,i=startColIndex;
			for(var thIndex=0;thIndex<size;thIndex++){
				tempTh=ths[thIndex];
				if(processHead) {
					tempTh.data("fixed-col-index", fixedIndex).attr("data-fixed-col-index", fixedIndex);
					tempTh.addClass("processed");
				}
				if(tempTh[0].hasAttribute("colspan")){
					that.processColSpanNextTrThColIndex(table,thead,tbody,tfoot,trIndex+1,i,i+parseInt(tempTh.attr("colspan"))-1,fixedIndex,columnMap,processHead);
					i=i+parseInt(tempTh.attr("colspan"));
					continue;
				}
				if(processHead){
					tempTh.data("col-index",i).attr("data-col-index",i);
					tempColumnTh_column=tempTh.data("column");
					if(tempColumnTh_column&&tempColumnTh_column!='index'&&tempColumnTh_column!='checkbox'&&tempColumnTh_column!='radio'){
						columnMap["columnToIndex"][tempColumnTh_column]=i;
						columnMap["indexToColumn"]["col_"+i]=tempColumnTh_column;
					}
					columnMap["indexToColumnText"]["col_"+i]=$.trim(tempTh[0].innerText||("col_"+(i+1)));
					if(isOk(tfoot)){
						tfoot.find("tr>th:nth-child("+(i+1)+")").data("col-index",i).attr("data-col-index",i);
					}
				}
				i++;

			}
		},
		processTbodyColIndex:function(table){
			if(!table.isEmpty){
				this.processTableColIndex(table,false,true);
			}
		},
		/**
		 * 处理table样式与事件主入口
		 */
		processTableMainBeforeAjax:function(table){
			var that=this;
			//初始化分页组件占位box
			that.addJboltPageBox(table);
			//处理tableHeader中的列头排序特效
			that.processTableHeaderColSort(table);
			//处理table thead 列补充类型 额外添加的
			that.processTableTheadColumnPrepend(table,true);
			//处理 thead colIndex
			that.processTheadColIndex(table);
			//设置宽高数据
			that.processTableWidthAndHeight(table);
			//处理单元格宽度高度
			that.processCellWidthAndHeight(table);
			//重新设置tableHeight
//				that.setTableHeight(table);
			//处理fixedHeader fixedFooter
			that.processHeaderAndFooterFixed(table);
			//关联的条件查询form处理
			that.processConditionsForm(table);
			//设置其他table绑定tableId
			that.processOtherTableBindTableId(table);
			//设置绑定组件的tableId
			that.processBindEleTableId(table);
			//判断数据为空
			that.processEmptyTableBody(table);
			//processEditable
			that.processEditable(table);
			//处理选中checkbox数据
			that.processTableKeepSelectedItems(table);
			//处理事件重新绑定
			that.processTableEvent(table);
			//处理自定义 ajax前的handler
			that.initTableBeforeAjaxHandler(table);
			LayerMsgBox.closeLoadNow();

		},
		initTableBeforeAjaxHandler:function(table){
			var handler = table.data("before-ajax-handler");
			if(handler){
				table.beforeAjaxHandler = handler;
			}
		},
		processOtherTableBindTableId:function(table){
			var tableId=table.attr("id");
			table.table_view.find("table:not(.jbolt_main_table)").data("jbolt-table-id",tableId);
		},
		/**
		 * 处理关联绑定的数据
		 */
		processConditionsForm:function(table){
			var that=this;
			var conditionsForm=table.data("conditions-form");
			if(conditionsForm!=undefined&&conditionsForm!=""&&conditionsForm!="undefined"){
				if(table.isAjax){
					$("#"+conditionsForm).on("submit",function(e){
						e.preventDefault();
						e.stopPropagation();
						if(FormChecker.check(this)){
							table.data("sortColumn","").data("sortType","");
							that.reProcessTableHeaderColDefaultSort(table);

							table.jsonConditions=null;
							table.readByJsonConditions=false;
							table.scrollToTop=true;
							table.resetCellWidthAfterAjax=false;
							that.readByPage(table,1);
						}
						return false;
					});
				}else{
					var cfor=$("#"+conditionsForm);
					if(isOk(cfor)){
						if(!cfor[0].hasAttribute("data-pjaxsubmit")){
							cfor.data("pjaxsubmit",true).attr("data-pjaxsubmit",true).attr("method","get");
						}
					}
				}
			}
		},
		/**
		 * 根据json conditions条件参数查询
		 */
		readByConditions:function(table,conditions){
			table.jsonConditions=conditions;
			table.scrollToTop=true;
			table.resetCellWidthAfterAjax=false;
			this.readByPage(table,1);
			return true;
		},
		/**
		 * 查询表单提交
		 */
		submitConditionsForm:function(table){
			table.jsonConditions=null;
			table.readByJsonConditions=false;
			table.scrollToTop=true;
			table.resetCellWidthAfterAjax=false;
			this.readByPage(table,1);
			return true;
		},
		/**
		 * 处理当新数据重置后 重新处理组件事件
		 */
		afterAjaxSetTableDataInitAutoEle:function(table){
			processInnerElesInit(table.table_box);
		},
		/**
		 * 本地渲染的table 最后执行这个
		 */
		afterFixedColumn:function(table){
			var fixedCols=table.table_box.find(".jbolt_table_fixed");
			if(isOk(fixedCols)){
				processInnerElesInit(fixedCols);
			}

		},
		/**
		 * ajax数据切换后重置样式
		 */
		processAllTableStyleReset:function(table){
			var leftScroll=table.table_body.scrollLeft();
			var topScroll=table.table_body.scrollTop();
			table.table_box.find(".jbolt_table_header table").css("margin-left","0px");
			table.table_box.find(".jbolt_table_footer table").css("margin-left","0px");
			var tableTheadHeight = table.thead.data("height");
			if(tableTheadHeight) {
				table.table_box.find(".jbolt_table_body table").css("margin-top", (0 - table.thead._height- 1) + "px");
			}else{
				table.table_box.find(".jbolt_table_body table").css("margin-top", (0 - table.thead._height) + "px");
			}
			table.table_body.scrollLeft(leftScroll);
			if(table.hasHeader){
				table.fixed_header.table.css("margin-left",(0-leftScroll)+"px");
			}
			if(table.hasFooter&&table.tfootFixed){
				table.fixed_footer.table.css("margin-left",(0-leftScroll)+"px");
			}
			if(table.table_body.sortRankAfter){
				table.table_body.scrollTop(0);
			}else{
				if(table.table_body.needKeepScrollLeft){
					if(table.table_body.scroll_left>0){
						table.table_body.scrollLeft(leftScroll);
					}
				}else{
					if(leftScroll>0){
						table.table_body.scrollLeft(0);
					}
				}
				if(table.table_body.needKeepScrollTop){
					if(table.table_body.scroll_top>0){
						table.table_body.scrollTop(topScroll);
					}
				}else{
					if(topScroll>0){
						table.table_body.scrollTop(0);
					}
				}
			}
			table.table_body.needKeepScrollLeft=false;
			table.table_body.needKeepScrollTop=false;
			table.scrollToTop=false;
			table.table_body.sortRankAfter=false;

			if(table.editable){
				this.processNewTrCellStyle(table);
			}
		},
		reProcessEditableTableMainAfterRowChange:function(table){
			var that=this;
			//处理thIndex
			that.processTbodyColIndex(table);
			//处理单元格宽度
			that.processOneAutoThWidthByTrChange(table);
			//判断数据为空
			that.processEmptyTableBody(table);
			//处理左侧fixed
			that.processColumnFixedLeft(table);
			//处理右侧fixed
			that.processColumnFixedRight(table);
			//处理右侧滚动条 纵向
			that.refreshMainTableVScroll(table);
			//处理下方滚动条 横向
			that.refreshFixedColumnHScroll(table);
			//设置其他table绑定tableId
			that.processOtherTableBindTableId(table);
			//处理fixed的滚动位置
			that.reScrollFixedColumnBox(table);
		},
		reProcessTableMainAfterRowChange:function(table){
			var that=this;
			//处理左侧fixed
			that.processColumnFixedLeft(table);
			//处理右侧fixed
			that.processColumnFixedRight(table);
		},
		/**
		 * 再次刷新数据后要重新渲染表格关键数据和样式
		 */
		reProcessTableMain:function(table,callback){
			var that=this;
			//判断数据为空
			that.processEmptyTableBody(table);
			//补齐行
			that.processEditableTableFillMinCountRows(table);
			//处理thIndex
			that.processTbodyColIndex(table);
			//处理treetable
			that.processTreeTable(table);
			//处理thead里的checkbox uncheck
			that.processUnCheckTheadCheckbox(table);
			//初始化处理tbody中的横向Summarys
			that.initEditableTbodyHSummarys(table);

			//处理额外数据
			that.processExtraData(table);
			//处理ajax加载数据后重新刷新绑定必要组件自动化
			that.afterAjaxSetTableDataInitAutoEle(table);
			// if((!table.isEmpty&&table.isTreeTable&&table.tableListDatas.length<20) || (!table.isEmpty&&!table.isTreeTable&&table.tableListDatas.length<100)){
			// 	that.processTableStyleAfterAjax(table,callback);
			// }else{
			// 	setTimeout(function(){
			// 	},100);
			// }
			that.processTableStyleAfterAjax(table,function(){
				if(table.editableOptions){
					//处理tbody中的横向summarys计算
					if(table.editableOptions.hsummaryFirstByAutoCalc){
						that.processTbodyHSummarys(table);
					}
					//处理tfoot summary
					if(table.editableOptions.vsummaryFirstByAutoCalc){
						that.processTfootSummarys(table);
					}
				}
			},callback);
			//重新设置当前页面 已选数据的 选中状态
			that.processTableReKeepSelectedItems(table);
		},
		processTableReKeepSelectedItems:function(table){
			if(!table.keepSelectedItemsEnable || notOk(table.selectedItemsIds)){return;}
			var that = this;
			that.setCheckedIds(table,table.selectedItemsIds);
		},
		processTableStyleAfterAjax:function(table,summaryCallback,callback){
			var that = this;
			var tableFastMode = table.data("fast-mode")||false;
			if(tableFastMode){
				setTimeout(function(){

					//处理单元格宽度
					//that.processCellWidthAfterAjax(table);

					setTimeout(function(){
						if(summaryCallback){
							summaryCallback();
						}
						//重新设置tableHeight
						that.setTableHeight(table);
						//处理恢复样式
						that.processAllTableStyleReset(table);
						//处理左侧fixed
						that.processColumnFixedLeft(table);
						//处理右侧fixed
						that.processColumnFixedRight(table);
						//处理右侧滚动条 纵向
						that.refreshMainTableVScroll(table);
						//处理下方滚动条 横向
						that.refreshFixedColumnHScroll(table);

						//处理fixed的滚动位置
						that.reScrollFixedColumnBox(table);
						//设置其他table绑定tableId
						that.processOtherTableBindTableId(table);
						setTimeout(function(){
							//处理ajax每次读取加载完事件
							that.processAjaxSuccessCallback(table);
							if(callback){
								callback();
							}
						},100);
					},100);


				},100);
			}else{

					//处理单元格宽度
					that.processCellWidthAfterAjax(table);
					if(summaryCallback){
						summaryCallback();
					}
					//重新设置tableHeight
					that.setTableHeight(table);
					//处理恢复样式
					that.processAllTableStyleReset(table);
					//处理左侧fixed
					that.processColumnFixedLeft(table);
					//处理右侧fixed
					that.processColumnFixedRight(table);
					//处理右侧滚动条 纵向
					that.refreshMainTableVScroll(table);
					//处理下方滚动条 横向
					that.refreshFixedColumnHScroll(table);

					//处理fixed的滚动位置
					that.reScrollFixedColumnBox(table);
					//设置其他table绑定tableId
					that.processOtherTableBindTableId(table);
					//处理ajax每次读取加载完事件
					that.processAjaxSuccessCallback(table);
					if(callback){
						callback();
					}
			}

		},
		processTreeTable:function(table){
			if(table.isEmpty){return;}
			//处理treeTable
			var treeTable=table.data("treetable");
			if(typeof(treeTable)=="undefined"||treeTable.toString() == ''){
				table.isTreeTable=false;
				return false;
			}
			table.isTreeTable=true;
			//读取解析展开的第几级
			var openLevel=1;
			//读取解析哪一列可以控制树展开关闭
			var optColIndex=1;
			var type=typeof(treeTable);
			if(type=='number'){
				openLevel=parseInt(treeTable)||1;
			}else{
				if(treeTable=="all"){
					openLevel="all";
				}else if(treeTable.indexOf(":")!=-1){
					var opt=treeTable.split(":");
					if(opt[0]=="all"){
						openLevel="all";
					}else{
						openLevel=parseInt(opt[0]);
					}
					optColIndex=parseInt(opt[1]);
				}
			}
			table.treeTableOptColIndex = optColIndex;
			this.processTrLevels(table);
			//默认就是显示第一层 其他的都关闭
			if(typeof(openLevel)=="number"&&openLevel>1){
				this.processTrOpenByLevel(table,openLevel);
			}else if(openLevel=="all"){
				this.expandAll(table);
			}

			//处理指定列的单元格 parent
			this.processParentTrOptBtn(table,optColIndex);

		},
		processParentTrOptBtn:function(table,optColIndex){
			optColIndex=optColIndex||1;
			table.tbody.find("tr>td:nth-child("+optColIndex+")").addClass("text-left");
			table.tbody.find("tr.parent.hasItems>td:nth-child("+optColIndex+")").prepend('<div class="parent_flag_box"><i class="fa fa-caret-right"></i></div>');
			table.tbody.find("tr:not(.hasItems).son>td:nth-child("+optColIndex+")").prepend('<div class="parent_flag_box"><i class="fa son">•</i></div>');
			table.tbody.find("tr:not(.hasItems).parent>td:nth-child("+optColIndex+")").prepend('<div class="parent_flag_box"><i class="fa"></i></div>');
//				table.tbody.find("tr[data-hasitems!='true'][data-level='1']>td:nth-child("+optColIndex+")").prepend('<div class="parent_flag_box"><i class="fa"></i></div>');
			var level,box;
			table.tbody.find("tr[data-level]>td>.parent_flag_box").each(function(){
				box=$(this);
				level=box.closest("tr").data("level")||1;
				if(level>1){
					box.width(16+(level-1)*24);
				}
			});
		},
		processParentTrsOptBtn:function(table,trs){
			var optColIndex=table.treeTableOptColIndex||1;
			trs.find("td:nth-child("+optColIndex+")").addClass("text-left");
			var tr,level;
			$.each(trs,function(index,item){
				tr=$(item);
				level = tr.data("level")||1;
				if(tr.data("hasitems")){
					tr.addClass("parent hasItems son");
					tr.find("td:nth-child("+optColIndex+")").prepend('<div style="width:'+(16+(level-1)*24)+'px" class="parent_flag_box"><i class="fa fa-caret-right"></i></div>');
				}else if(level>1){
					tr.addClass("son");
					tr.find("td:nth-child("+optColIndex+")").prepend('<div style="width:'+(16+(level-1)*24)+'px" class="parent_flag_box"><i class="fa son">•</i></div>');
				}else  if(level==1){
					tr.addClass("parent son");
					tr.find("td:nth-child("+optColIndex+")").prepend('<div style="width:'+(16+(level-1)*24)+'px" class="parent_flag_box"><i class="fa">•</i></div>');
				}


			});

//				var tr,level,box;
//				trs.each(function(){
//					tr=$(this);
//					tr.find("td:nth-child("+optColIndex+")").addClass("text-left");
//					if(tr.hasClass("parent") && tr.hasClass("hasItems")){
//						tr.find("td:nth-child("+optColIndex+")").prepend('<div class="parent_flag_box"><i class="fa fa-caret-right"></i></div>');
//					}
//					if(tr.hasClass("son") && !tr.hasClass("hasItems")){
//						tr.find("td:nth-child("+optColIndex+")").prepend('<div class="parent_flag_box"><i class="fa son">•</i></div>');
//					}
//					if(tr.hasClass("parent") && !tr.hasClass("hasItems")){
//						tr.find("td:nth-child("+optColIndex+")").prepend('<div class="parent_flag_box"><i class="fa"></i></div>');
//					}
//
//					box=tr.find("td>.parent_flag_box");
//					if(isOk(box)){
//						level=tr.data("level")||1;
//						if(level>1){
//							box.width(16+(level-1)*24);
//						}
//					}
//				});

		},
		collapseAll:function(table){
			//闭合所有父节点
			table.table_box.find("table>tbody>tr.son.show").removeClass("show").addClass("d-none");
			table.table_box.find("table>tbody>tr.parent.hasItems.expand").removeClass("expand");
		},
		collapse:function(table,tr){
			if(!isOk(tr)){
				return;
			}
			//闭合自身与全部下级
			tr.removeClass("expand");
			var id=tr.data("id");
			var sontrs=tr.nextAll("tr[data-pid='"+id+"']");
			if(!isOk(sontrs)){
				return;
			}
			var that=this;
			var tr,trId,fixTable,fixtr;
			$.each(sontrs,function(i,trEle){
				tr=$(trEle);
				trId=tr.data("id");
				tr.removeClass("show").addClass("d-none");
				if(tr.hasClass("expand")){
					that.collapse(table,tr);
				}
			});
		},
		//前端关键词过滤
		filterByKeywords:function(table,keywords,colIndexArr){
			table.useInputFilter = false;
			keywords=keywords?keywords.trim():null;
			if(table.fixedColumnTables){
				table.fixedColumnTables.hide();
			}
			if(keywords){
				var keys='',temptr,hasKeywords=false;
				table.find("tbody>tr").each(function(){
					temptr=$(this);
					temptr.find("span.jb_filter_highlight,span.jb_fspan").each(function(){
						this.outerHTML=this.innerText;
					});
					if(isOk(colIndexArr)){
						//如果指定了列 那就只判断列里有没有
						hasKeywords=false;
						$.each(colIndexArr,function(i,colIndex){
							temptr.find("td[data-col-index='"+(colIndex-1)+"']").each(function(){
								if(this.innerText&&this.innerText.indexOf(keywords)!=-1){
									//如果指定td里包含 再去执行高亮
									filterAndHighlight(this,keywords);
									if(hasKeywords==false){
										hasKeywords=true;
									}
								}
							});
						});
						if(hasKeywords){
							temptr.addClass("show jb_filteritem").removeClass("d-none");
							table.useInputFilter = true;
						}else{
//								this.style.display="none";
							temptr.removeClass("show jb_filteritem").addClass("d-none");
						}
					}else{
						if(this.innerText&&this.innerText.indexOf(keywords)!=-1){
//								this.style.display="table-row";
							temptr.addClass("show jb_filteritem").removeClass("d-none");
							filterAndHighlight(this,keywords);
							table.useInputFilter = true;
						}else{
//								this.style.display="none";
							temptr.removeClass("show jb_filteritem").addClass("d-none");
						}

					}


				});

			}else{
				table.find("tbody>tr").addClass("show").removeClass("d-none jb_filteritem").find("span.jb_filter_highlight,span.jb_fspan").each(function(){
					this.outerHTML=this.innerText;
				});
			}

			this.processColumnFixed(table);
		},
		//展开所有节点
		expandAll:function(table){
			table.table_box.find("table>tbody>tr.son").addClass("show").removeClass("d-none processColumnFixed");
			table.table_box.find("table>tbody>tr.parent.hasItems").addClass("expand");
		},
		trTreeFaLoading:function(tr){
			var fa = tr.find("td>.parent_flag_box>i.fa");
			if(isOk(fa)){
				fa.removeClass("fa-caret-right").addClass("fa-spinner fa-pulse");
				LayerMsgBox.loading("加载中",5000);
			}

		},
		trTreeFaCloseLoading:function(tr,timeout){
			LayerMsgBox.closeLoadingNow();
			var fa = tr.find("td>.parent_flag_box>i.fa");
			if(isOk(fa)){
				if(timeout){
					setTimeout(function(){
						fa.addClass("fa-caret-right").removeClass("fa-spinner fa-pulse");
					}, 300);
				}else{
					fa.addClass("fa-caret-right").removeClass("fa-spinner fa-pulse");
				}
			}
		},
		expand:function(table,tr){
			if(!isOk(tr)){
				return;
			}
			this.trTreeFaLoading(tr);
			//开启自身与下一级
			tr.addClass("expand");
			var id=tr.data("id");
			var sontrs;
			if(table.useInputFilter){
				sontrs = tr.nextAll("tr[data-pid='"+id+"'].jb_filteritem");
			}else{
				sontrs = tr.nextAll("tr[data-pid='"+id+"']");
			}
			var process=false;
			if(!isOk(sontrs)){
				if(!table.treeTableCloneTbody){
					this.trTreeFaCloseLoading(tr,false);
					return;
				}
				if(table.useInputFilter){
					sontrs = table.treeTableCloneTbody.find("tr[data-pid='"+id+"'].jb_filteritem");
				}else{
					sontrs = table.treeTableCloneTbody.find("tr[data-pid='"+id+"']");
				}
				if(!isOk(sontrs)){
					this.trTreeFaCloseLoading(tr,false);
					return;
				}
				var isCheckedAll = false;
				if(table.prependColumnType && table.prependColumnType == "checkbox"){
					isCheckedAll = this.isCheckboxCheckedAll(table);
				}
				this.processTableTrsColumnPrepend(table,sontrs);
				tr.after(sontrs);
				if(isCheckedAll){
					CheckboxUtil.checkAll("jboltTableCheckbox",table.table_box);
				}
				this.processParentTrsOptBtn(table,sontrs);
				processInnerElesInit(sontrs);
				this.processTrDataIndex(table);

				process=true;
			}
			sontrs.addClass("show").removeClass("d-none");
			if(process){
				this.processTreeTableColumnFixed(table,tr,sontrs,"left");
				this.processTreeTableColumnFixed(table,tr,sontrs,"right");
				this.trTreeFaCloseLoading(tr,true);
			}else{
				this.trTreeFaCloseLoading(tr,false);
			}
		},
		processTreeTableColumnFixed:function(table,tr,sontrs,dir){
			if((dir=="left" && !table.left_fixed) || (dir=="right" && !table.right_fixed)){
				return;
			}
			var trIndex = tr.data("index");
			var ftr;
			if(dir=="left"){
				ftr = table.left_fixed.body.table.find("tbody>tr[data-index='"+trIndex+"']");
			}else if(dir=="right"){
				ftr = table.right_fixed.body.table.find("tbody>tr[data-index='"+trIndex+"']");
			}
			if(!isOk(ftr)){
				return;
			}
			//得到需要左侧显示的fixedColumns 列序号
			var indexArr=this.getColumnFixedIndexArr(table,dir);
			if(!indexArr||indexArr.length==0){return false;}
			var jbolt_table_fixed_trs=this.cloneTreeTableBoxContent(sontrs,dir,indexArr);
			jbolt_table_fixed_trs.find("input[type='radio'][name='jboltTableRadio'],input[type='radio'][name='jboltTableRadio_fixedheader']").attr("name","jboltTableRadio_fixed");
			initToolTip(jbolt_table_fixed_trs);
			ftr.after(jbolt_table_fixed_trs);
		},
		processTrLevels:function(table){
			var firstTrHasLevel = table.tbody.find("tr[data-level][data-hasitems]:eq(0)");
			if(isOk(firstTrHasLevel)){
				table.tbody.find("tr[data-hasitems='true']").addClass("parent hasItems");
				table.tbody.find("tr[data-level!='1']").addClass("son");
				//有设置 就不用自动设置了
				return;
			}
			var level1_trs=table.tbody.find("tr[data-pid='0'],tr[data-pid='']");
			if(!isOk(level1_trs)){
				return;
			}
			var that=this;
			//第一级
			var tr;
			$.each(level1_trs,function(i,trEle){
				tr=$(trEle);
				tr.data("level",1).attr("data-level",1);
				tr.addClass("parent");
				that.processSonTrLevels(table,tr,2);
			});
		},processSonTrLevels:function(table,ptr,level){
			var id=ptr.data("id");
			var sontrs=ptr.nextAll("tr[data-pid='"+id+"']");
			if(level>2){
				ptr.addClass("son");
			}
			if(!isOk(sontrs)){
				return;
			}
			ptr.addClass("parent hasItems");
			var that=this;
			var tr;
			$.each(sontrs,function(i,trEle){
				tr=$(trEle);
				tr.data("level",level).attr("data-level",level);
				that.processSonTrLevels(table,tr,level+1);
			});
		},
		processTrOpenByLevel:function(table,maxOpenLevel){
			if(maxOpenLevel<2){return;}
			var xlevel='',xlevel2='';
			for(var level=2;level<=maxOpenLevel;level++){
				if(xlevel){
					xlevel=xlevel+",tr.son[data-level='"+level+"']";
				}else{
					xlevel="tr.son[data-level='"+level+"']";
				}
			}
			for(var level=1;level<maxOpenLevel;level++){
				if(xlevel2){
					xlevel2=xlevel2+",tr.parent[data-level='"+level+"']";
				}else{
					xlevel2="tr.parent[data-level='"+level+"']";
				}
			}
			var trs=table.tbody.find(xlevel);
			if(isOk(trs)){
				trs.addClass("show").removeClass("d-none");
			}

			var trs2=table.tbody.find(xlevel2);
			if(isOk(trs2)){
				trs2.addClass("expand");
			}


		},
		//处理tableExtraData
		processExtraData:function(table){
			var hasExtraData=(table.jsonData&&isOk(table.jsonData.extraData));
			if(!hasExtraData){
				return false;
			}
			var jboltPage = table.closest(".jbolt_page");
			if(!isOk(jboltPage)){
				console.log("若要UI组件上使用extraData里的值，jbolttable应放在jbolt_page中",2);
				return false;
			}
			var extraDataEles=jboltPage.find("[data-extradata]");
			if(!isOk(extraDataEles)){
				console.log("若要UI组件上使用extraData里的值，jbolttable所在的jbolt_page中应有声明data-extradata的组件",2);
				return false;
			}
			var extraData=table.jsonData.extraData;
			var valueHandler=table.data("extradata-handler")
			if(valueHandler){
				valueHandler=eval(valueHandler);
			}
			var isFormEle,extraEle,extraAttr,extraValue,tagName;
			//遍历循环从extraData中取值
			extraDataEles.each(function(){
				tagName=this.tagName;
				isFormEle=(tagName=="INPUT" || tagName=="TEXTAREA" || tagName=="SELECT");
				extraEle=$(this);
				if(hasExtraData){
					extraAttr=extraEle.data("extradata");
					//th上data-extradata的值 与extraData中的数据Key保持一致
					extraValue=extraData[extraAttr];
					if(typeof(extraValue)=="undefined"){
						extraValue="";
					}
				}else{
					extraValue="";
				}
				if(valueHandler){
					extraValue=valueHandler(table,extraEle,extraData,extraValue,isFormEle);
				}
				if(typeof(extraValue)=="undefined"){
					extraValue="";
				}
				if(isFormEle){
					extraEle.val(extraValue).change();
				}else{
					extraEle.html(extraValue);
				}
			});
		},
		processLeftRightFootBoxTableInit:function(table){
			var tableId=table.attr("id");
			if(table.leftbox){
				var lefttables=table.leftbox.find("table.jbolt_table[data-url][data-rowtpl]:not([data-jbolttable])");
				if(isOk(lefttables)){
					lefttables.attr("data-jbolttable",true).data("jbolttable",true);
					lefttables.attr("data-jbolt-maintable-id",tableId).data("jbolt-maintable-id",tableId);
					lefttables.jboltTable();
				}
			}
			if(table.rightbox){
				var righttables=table.rightbox.find("table.jbolt_table[data-url][data-rowtpl]:not([data-jbolttable])");
				if(isOk(righttables)){
					righttables.attr("data-jbolttable",true).data("jbolttable",true);
					righttables.attr("data-jbolt-maintable-id",tableId).data("jbolt-maintable-id",tableId);
					righttables.jboltTable();
				}
			}
			if(table.footbox){
				var foottables=table.footbox.find("table.jbolt_table[data-url][data-rowtpl]:not([data-jbolttable])");
				if(isOk(foottables)){
					foottables.attr("data-jbolttable",true).data("jbolttable",true);
					foottables.attr("data-jbolt-maintable-id",tableId).data("jbolt-maintable-id",tableId);
					foottables.jboltTable();
				}
			}

		},
		initEditableTbodyHSummarys:function(table){
			if(table.editable&&table.isAjax&&isOk(table.hsummarys)&&!table.isEmpty){
				this.initEditableHSummarys(table);
			}
		},
		//处理所有横向的统计计算列
		processTbodyHSummarys:function(table){
			if(!isOk(table.hsummarys)||table.isEmpty){return false;}
			var summarytds=table.tbody.find("tr>td[data-summary='true']");
			if(!isOk(summarytds)){return false;}
			var that=this,summaryTd;
			summarytds.each(function(){
				summaryTd=$(this);
				that.processTbodyHSummaryTd(table,summaryTd,false);
			});
		},
		processTbodySummarysInTr:function(table,tr){
			if(!isOk(table.hsummarys)||table.isEmpty||!isOk(tr)){return false;}
			var summaryTds=tr.find("td[data-summary='true'][data-col-index]");
			if(!isOk(summaryTds)){
				return false;
			}
			var that=this,summaryTd;
			summaryTds.each(function(){
				summaryTd=$(this);
				that.processTbodyHSummaryTd(table,summaryTd,true);
			});
		},
		processTfootSummary:function(table,summaryThColIndex){
			if(!table.hasFooter||typeof(summaryThColIndex)=="undefined"||summaryThColIndex<0){return false;}
			var summaryTh=table.tfoot.find("tr>th[data-summary='true'][data-col-index='"+summaryThColIndex+"']");
			if(isOk(summaryTh)){
				this.processTfootSummaryTh(table,summaryTh);
			}
		},
		processTfootSummaryTh:function(table,summaryTh){
			var summaryColumnName=summaryTh.data("column"),callTd,value=0,count=0,tempValue,max=0,min=0,type;
			var summaryColIndex=summaryTh.data("col-index");
			if(!summaryColumnName){
				var theadTh=table.thead.find("tr>th[data-col-index='"+summaryColIndex+"']");
				if(isOk(theadTh)){
					summaryColumnName=theadTh.data("column");
				}
			}
			if(summaryColumnName){
				if(isOk(table.tableListDatas)){
					var len = table.tableListDatas.length;
					var data;
					for(var i = 0 ; i < len ;i++ ){
						data=table.tableListDatas[i];
						tempValue = data[summaryColumnName]||data[StrUtil.camel(summaryColumnName)];
						if(typeof(tempValue)!="undefined" && typeof(tempValue)!="number"){
							try{
								tempValue = Number(tempValue);
							}catch(e){
								tempValue = 0;
							}
						}
						if(tempValue){
							value = value+tempValue;
							count++;
						}
						if(tempValue>max){
							max=tempValue;
						}
						if(tempValue<min){
							min=tempValue;
						}
					}
				}
			}else{

				var tds=table.tbody.find("tr>td[data-col-index='"+summaryColIndex+"']");
				if(!isOk(tds)){
					return false;
				}
				tds.each(function(){
					callTd=$(this);
					tempValue=$.trim(callTd[0].innerText);
					if(tempValue){
						if(tempValue.indexOf(",")!=-1){
							tempValue=tempValue.replaceAll(",","");
						}
						tempValue=Number(tempValue);
						if(tempValue!=0){
							value=value+tempValue;
							count++;
						}
						if(tempValue>max){
							max=tempValue;
						}
						if(tempValue<min){
							min=tempValue;
						}
					}
				});
			}

			var roundtag=summaryTh.data("roundtag")||"round";
			var toFixed=summaryTh.data("tofixed")||0;
			var removeZero=summaryTh.data("removezero")||false;
			var removeEndZero=summaryTh.data("removeendzero")||false;
			var formula=summaryTh.data("formula")||"sum";
			var toFixedValue=0;
			if(formula=="sum"){
				toFixedValue=this.calValue(value,roundtag,toFixed);
			}else if(formula=="max"){
				toFixedValue=this.calValue(max,roundtag,toFixed);
			}else if(formula=="min"){
				toFixedValue=this.calValue(min,roundtag,toFixed);
			}else if(formula=="avg"){
				if(count==0){
					value=0;
					toFixedValue=0;
				}else{
					value=(value*1.0)/count;
					toFixedValue=this.calValue(max,roundtag,toFixed);
				}
			}else{
				toFixedValue=this.calValue(max,roundtag,toFixed);
			}
			toFixedValue=toFixedValue?toFixedValue:0;
			summaryTh.attr("data-value",toFixedValue).data("value",toFixedValue);
			if(value){
				value=numberFormat(value,toFixed,'.',',',roundtag);
				if(toFixed&&removeZero){
					value=removeFixedNumberAllZero(value,toFixed);
					if(removeEndZero){
						value=removeNumberEndZero(value);
					}
				}else if(removeZero){
					value=removeNumberEndZero(value);
				}else{
					if(removeEndZero){
						value=removeNumberEndZero(value);
					}
				}
			}else{
				value=0;
			}
			summaryTh.text(value);
			var syncval=summaryTh.data("syncval");
			if(syncval){
				$(syncval).val(toFixedValue).trigger("change");
			}

			var handler=summaryTh.data("handler");
			if(handler){
				summaryTh.trigger("exeSummaryHandler");
//					var exe_handler=eval(handler);
//					if(exe_handler&&typeof(exe_handler)=="function"){
//						exe_handler(toFixedValue);
//					}
			}
		},
		callByFormula:function(table,summaryTd,formula,roundtag,toFixed){
			var value=0;
			var oldformula=formula,formulaValueMap={},fstr="",keys,tempKey,colIndex,tdValue,tdText,tempTd,tr=summaryTd.parent();
			fstr=formula.replaceAll("+",",");
			fstr=fstr.replaceAll("-",",");
			fstr=fstr.replaceAll("*",",");
			fstr=fstr.replaceAll("/",",");
			fstr=fstr.replaceAll(">",",");
			fstr=fstr.replaceAll("<",",");
			fstr=fstr.replaceAll("==",",");
			fstr=fstr.replaceAll("!=",",");
			fstr=fstr.replaceAll("(","");
			fstr=fstr.replaceAll(")","");
			fstr=fstr.replaceAll(" ","");
			fstr=fstr.replaceAll("?",",");
			fstr=fstr.replaceAll(":",",");
			var farr=fstr.split(",");
			/*for(var i in farr){
					formulaValueMap[farr[i]]=0;
				}*/

//				keys=Object.keys(formulaValueMap);
//				console.log(farr)
			var tempEle,tempEleValue,haserror=false,tempKeyPre,switchBtn,switchBtnValue,checkbox,checkboxValue;
			if(farr.length>1){
				farr.sort(function(x,y){
					if(x&&y){
						return y.length - x.length;
					}
					return 0;
				});
			}
			for(var i in farr){
				tempKey=farr[i];
				if(tempKey){
					if(isNaN(tempKey)){
						if(tempKey.startWith("#")){
							//说明是外部数据
							tempEle=$(tempKey);
							if(isOk(tempEle)){
								//存在就好处理
								tempEleValue=$.trim(tempEle.val());
								if(!tempEleValue){
									tempEleValue=tempEle.data("value");
									if(!tempEleValue){
										tempEleValue=0;
									}
								}
								//不是数字
								if(isNaN(tempEleValue)){
									if(tempEleValue.endWith("%")){
										tempEleValue=tempEleValue.replace("%","");
										if(!isNaN(tempEleValue)){
											tempEleValue=Number(tempEleValue)/100;
										}
									}
								}
								if(isNaN(tempEleValue)){
									console.error("计算表达式"+oldformula+"中关联ID的元素:"+tempKey+"的值不是数字："+tempEleValue);
									haserror=true;
									return false;
								}
								if(formula.indexOf("/"+tempKey)!=-1&&tempEleValue==0){
									console.error("计算表达式"+oldformula+"中关联ID的元素:"+tempKey+"值为0，除数不能为0");
									haserror=true;
									return false;
								}
								formula=formula.replaceAll(tempKey,tempEleValue);
							}else{
								console.error("计算表达式"+oldformula+"中关联ID的元素找不到:"+tempKey);
								haserror=true;
								return false;
							}

						}else{
							var trIndex=tr.data("index");
							var trData = table.tableListDatas[trIndex];
							var colIndex;
							if(tempKey.indexOf("?")!=-1&&tempKey.indexOf(":")!=-1){
								tempKeyPre=tempKey.split("?")[0];
								if(trData&&trData.hasOwnProperty(tempKeyPre)){
									tdValue=trData[tempKeyPre];
									tdValue = tempKey.replaceAll(tempKeyPre,tdValue);
								}else{
									if(typeof(table.columnIndexMap[tempKeyPre])=="undefined"){
										haserror=true;
										console.error("计算表达式"+oldformula+"中:"+tempKeyPre+"变量未找到");
										return false;
									}else{
										colIndex=table.columnIndexMap[tempKeyPre];
										if(colIndex!=undefined&&colIndex>=0){
											tempTd=tr.find("td[data-col-index='"+colIndex+"']");
											if(isOk(tempTd)){
												switchBtn=tempTd.find("img[data-switchbtn]");
												checkbox=tempTd.find("input[type='checkbox'].jbt_editor_checkbox");
												if(isOk(switchBtn)){
													switchBtnValue=switchBtn.data("value");
													if(tempKeyPre){
														switchBtnValue=tempKey.replace(tempKeyPre,switchBtnValue);
													}
													tdValue=eval(switchBtnValue);
												}else if(isOk(checkbox)){
													checkboxValue=checkbox.is(":checked");
													if(tempKeyPre){
														checkboxValue=tempKey.replace(tempKeyPre,checkboxValue);
													}
													tdValue=eval(checkboxValue);
												}else{
													tdText=$.trim(tempTd[0].innerText);
													if(tdText){
														tdValue=Number(tdText.replaceAll(",",""));
													}else{
														tdValue=Number(tempTd.data("value"));
													}
												}
												if(!tdValue){
													tdValue=0;
												}

											}else{
												tdValue=0;
											}
										}else{
											if(tempKey.indexOf("?")!=-1&&tempKey.indexOf(":")!=-1){
											}else{
												haserror=true;
												console.error("计算表达式"+oldformula+"中计算列找不到:"+tempKey);
												return false;
												//如果找不到 就去当前行的json数据里找一找
											}
										}

									}
								}
							}else{
								if(trData&&trData.hasOwnProperty(tempKey)){
									tdValue=trData[tempKey];
								}else{
									if(typeof(table.columnIndexMap[tempKey])=="undefined"){
										haserror=true;
										console.error("计算表达式"+oldformula+"中:"+tempKey+"变量未找到");
										return false;
									}else{
										colIndex=table.columnIndexMap[tempKey];
										if(colIndex!=undefined&&colIndex>=0){
											tempTd=tr.find("td[data-col-index='"+colIndex+"']");
											if(isOk(tempTd)){
												tdText=$.trim(tempTd[0].innerText);
												if(tdText){
													tdValue=Number(tdText.replaceAll(",",""));
												}else{
													tdValue=Number(tempTd.data("value"));
												}
												if(!tdValue){
													tdValue=0;
												}

											}else{
												tdValue=0;
											}
										}else{
											if(tempKey.indexOf("?")!=-1&&tempKey.indexOf(":")!=-1){
											}else{
												haserror=true;
												console.error("计算表达式"+oldformula+"中计算列找不到:"+tempKey);
												return false;
												//如果找不到 就去当前行的json数据里找一找
											}
										}
									}
								}
							}
							var valueType = typeof(tdValue);
							if(valueType=="undefined"){
								tdValue = 0;
							}else if(valueType == "string"){
								tdValue=tdValue.replaceAll(",","");
								tdValue = eval(tdValue);
								if(typeof(tdValue)=="undefined"){
									tdValue = 0;
								}
							}
							formula=formula.replaceAll(tempKey,tdValue);
//							if(colIndex!=undefined&&colIndex>=0){
//								tempTd=tr.find("td[data-col-index='"+colIndex+"']");
//								if(isOk(tempTd)){
//									switchBtn=tempTd.find("img[data-switchbtn]");
//									checkbox=tempTd.find("input[type='checkbox'].jbt_editor_checkbox");
//									if(isOk(switchBtn)){
//										switchBtnValue=switchBtn.data("value");
//										if(tempKeyPre){
//											switchBtnValue=tempKey.replace(tempKeyPre,switchBtnValue);
//										}
//										tdValue=eval(switchBtnValue);
//									}else if(isOk(checkbox)){
//										checkboxValue=checkbox.is(":checked");
//										if(tempKeyPre){
//											checkboxValue=tempKey.replace(tempKeyPre,checkboxValue);
//										}
//										tdValue=eval(checkboxValue);
//									}else{
//										tdText=$.trim(tempTd[0].innerText);
//										if(tdText){
//											tdValue=Number(tdText.replaceAll(",",""));
//										}else{
//											tdValue=Number(tempTd.data("value"));
//										}
//									}
//									if(!tdValue){
//										tdValue=0;
//									}
//
//								}else{
//									tdValue=0;
//								}
//								formula=formula.replaceAll(tempKey,tdValue);
//							}else{
//								if(tempKey.indexOf("?")!=-1&&tempKey.indexOf(":")!=-1){
//								}else{
//									haserror=true;
//									console.error("计算表达式"+oldformula+"中计算列找不到:"+tempKey);
//									return false;
//									//如果找不到 就去当前行的json数据里找一找
//								}
//							}
						}

					}
				}
			}
			if(haserror){
				value=0
			}else{
				try{
					value=eval(formula);
					if(isNaN(value)){
						console.error("计算失败:计算表达式"+oldformula+"替换后的表达式:"+formula+"="+value+"\n计算结果不是正确数字");
						value=0;
					}
				}catch(e){
					console.error("计算失败:计算表达式"+oldformula+"替换后的表达式:"+formula);
					console.error(e);
					value=0;
				}
			}
			value=this.calValue(value,roundtag,toFixed);
			return value;
		},calValue:function(value,roundtag,toFixed){
			roundtag = roundtag || "round"; //"ceil","floor","round"
			var prec = !isFinite(+toFixed) ? 0 : Math.abs(toFixed);
			var k = Math.pow(10, prec);
			var value;
			if(roundtag=="none"){
				value=parseFloat((parseFloat((value * k).toFixed(prec*2))).toFixed(prec*2)) / k;
			}else{
				value=parseFloat(Math[roundtag](parseFloat((value * k).toFixed(prec*2))).toFixed(prec*2)) / k;
			}
			return value;
		},
		//处理tbody中所有横向的td上的summary计算
		processTbodyHSummaryTd:function(table,summaryTd,withVSummary){
			//判断是否是自动处理summary 扫描到立马执行
			var autoSummary = summaryTd.data("summary-auto");
			if(typeof(autoSummary)=="undefined"){
				autoSummary= true;
			}else if(typeof(autoSummary)=="string"){
				autoSummary = (autoSummary === "true");
			}
			if(!autoSummary && !summaryTd.triggerSummaryByOther){
				//如果设置不自动 并且这次没有被主动触发 就什么都不干
				return;
			}
			var formula=summaryTd.data("formula");
			if(!formula){
				summaryTd.attr("data-value",0).data("value",0);
				summaryTd.text("0");
				return;
			}
			var roundtag=summaryTd.data("roundtag")||"round";
			var toFixed=summaryTd.data("tofixed")||0;
			var removeZero=summaryTd.data("removezero")||false;
			var removeEndZero=summaryTd.data("removeendzero")||false;
			var toFixedValue=0,value=this.callByFormula(table,summaryTd,formula,roundtag,toFixed);
			toFixedValue=value;
			summaryTd.attr("data-value",toFixedValue).data("value",toFixedValue);
			//修改table.tableListDatas里的数据
			this.processChangeTableListDatasColumnValue(table,summaryTd,Number(toFixedValue));
			if(value){
				value=numberFormat(value,toFixed,'.',',',roundtag);
				if(toFixed&&removeZero){
					value=removeFixedNumberAllZero(value,toFixed);
					if(removeEndZero){
						value=removeNumberEndZero(value);
					}
				}else if(removeZero){
					value=removeNumberEndZero(value);
				}else{
					if(removeEndZero){
						value=removeNumberEndZero(value);
					}
				}

			}else{
				value=0;
			}
			summaryTd.text(value);
			//如果指定要立马执行纵向计算的话 就执行
			if(withVSummary){
				//横向的一个处理完还得判断是否存在纵向计算
				if(summaryTd.data("hasvsummary")){
					this.processTfootSummary(table,summaryTd.data("col-index"));
				}
			}

			var syncval=summaryTd.data("syncval");
			if(syncval){
				$(syncval).val(toFixedValue).trigger("change");
			}
			//handler
			var handler=summaryTd.data("handler");
			if(handler){
				summaryTd.trigger("exeSummaryHandler");
//					var exe_handler=eval(handler);
//					if(exe_handler&&typeof(exe_handler)=="function"){
//						exe_handler(table,summaryTd,toFixedValue);
//					}
			}
			//触发其他指定列summary
			this.processTriggerOtherColumnSummary(table,summaryTd);
			//changeColumns
			var jsonData=table.tableListDatas[summaryTd.closest("tr").data("index")];
			this.processColConfigChangeColumns(table,summaryTd,jsonData);
		},
		//处理Tfoot summarys
		processTfootSummarys:function(table){
			if(!table.hasFooter){return false;}
			if(!isOk(table.tfoot)){return false;}
			var summaryths=table.tfoot.find("tr>th[data-summary='true']");
			if(!isOk(summaryths)){return false;}
			if(table.isEmpty){
				summaryths.text("0");
			}else{
				var that=this,summaryTh;
				summaryths.each(function(){
					summaryTh=$(this);
					that.processTfootSummaryTh(table,summaryTh);
				});
			}
		},
		colConfigAutoInitHandler:function(table,td,colConfig){
			if(colConfig.type=="auto" && colConfig.initHandler){
				var dataIndex=td.parent().data("index");
				var trJsonData = table.tableListDatas[dataIndex];
				colConfig.initHandler(table,td,trJsonData,deepClone(colConfig));
			}
		},
		//通过colConfig配置 将td转为tdEditor
		getTdEditorHtml:function(table,currentTd,col_config,tdValue,tdText){
			var tpl='<div {@if tooltip} tooltip data-title="${tooltip}" {@/if} class="jbt_edit_ele ${parentCssClass}">'+
				'{@if type==="input"}'+
				'<input {@if readOnly}readonly="readonly"{@/if} {@if focusChangeToExtraForm} data-focus-changeto-extra-form="true" {@/if} {@if jbe_id}id="${jbe_id}"{@/if} data-with-clearbtn="true" {@if ajaxCheckUrl}data-ajax-check-url="${ajaxCheckUrl}"{@/if} class="jbt_editor ${cssClass}" {@if cssStyle} style="${cssStyle}" {@/if} autocomplete="off" type="text"  data-tips="${ruleTips}" data-rule="{@if rule}${rule};{@/if}len<=${maxLength?maxLength:20}{@if min!=undefined&&min!="undefined"};>=${min}{@/if}{@if max!=undefined&&max!="undefined"};<=${max}{@/if}" data-notnull="false" maxLength="${maxLength}" placeholder ="${placeholder }" name="${jbe_col_key}" value="${tdText}"/>'+
				'{@else if type==="textarea" }'+
				'<textarea {@if readOnly}readonly="readonly"{@/if} {@if focusChangeToExtraForm} data-focus-changeto-extra-form="true" {@/if} data-with-clearbtn="true" {@if ajaxCheckUrl}data-ajax-check-url="${ajaxCheckUrl}"{@/if}  class="jbt_editor ${cssClass}" style="height:${height}px;${cssStyle}" autocomplete="off" type="text" data-tips="${ruleTips}" data-rule="{@if rule}${rule};{@/if}len<=${maxLength?maxLength:40}" data-notnull="false" maxLength="${maxLength?maxLength:40}" placeholder ="${placeholder}" name="${jbe_col_key}">${tdText}</textarea>'+
				'{@else if type==="input_color" }'+
				'<input class="jbt_editor ${cssClass}" {@if cssStyle} style="${cssStyle}" {@/if}  autocomplete="off" type="color" data-tips="${ruleTips}" data-rule="{@if rule}${rule};{@/if}len<=${maxLength?maxLength:20}{@if min!=undefined&&min!="undefined"};>=${min}{@/if}{@if max!=undefined&&max!="undefined"};<=${max}{@/if}" data-notnull="false" maxLength="${maxLength?maxLength:20}" placeholder ="${placeholder}" name="${jbe_col_key}" value="${tdText}"/>'+
				'{@else if type==="input_number" }'+
				'<input {@if readOnly}readonly="readonly"{@/if} {@if focusChangeToExtraForm} data-focus-changeto-extra-form="true" {@/if} data-with-clearbtn="true" {@if ajaxCheckUrl}data-ajax-check-url="${ajaxCheckUrl}"{@/if}  class="jbt_editor ${cssClass}" {@if cssStyle} style="${cssStyle}" {@/if}  autocomplete="off" type="text" data-tips="${ruleTips}" data-rule="{@if rule}${rule};{@/if}len<=${maxLength?maxLength:20}{@if min!=undefined&&min!="undefined"};>=${min}{@/if}{@if max!=undefined&&max!="undefined"};<=${max}{@/if}" data-notnull="false" maxLength="${maxLength?maxLength:20}" placeholder ="${placeholder}" name="${jbe_col_key}" value="${tdText}"/>'+
				'{@else if type==="input_ranger" }'+
				'<span class="rangerText">${tdText}</span><input oninput="$(this).prev().text(this.value)" {@if readOnly}readonly="readonly"{@/if} class="jbt_editor ${cssClass}" {@if cssStyle} style="${cssStyle}" {@/if}  autocomplete="off" type="range" {@if min} min="${min}" {@/if} {@if min} min="${min}" {@/if}  {@if max} max="${max}" {@/if}  data-tips="${ruleTips}" data-rule="{@if rule}${rule};{@/if}len<=${maxLength?maxLength:20}{@if min!=undefined&&min!="undefined"};>=${min}{@/if}{@if max!=undefined&&max!="undefined"};<=${max}{@/if}" data-notnull="false" maxLength="${maxLength?maxLength:20}" placeholder ="${placeholder}" name="${jbe_col_key}" value="${tdText}"/>'+
				'{@else if type==="datetime" || type==="date"  || type==="time" || type==="year" || type==="month"}'+
				'<input {@if readOnly}readonly="readonly"{@/if} {@if focusChangeToExtraForm} data-focus-changeto-extra-form="true" {@/if} {@if ajaxCheckUrl}data-ajax-check-url="${ajaxCheckUrl}"{@/if}  class="jbt_editor ${cssClass}" {@if cssStyle} style="${cssStyle}" {@/if}  autocomplete="off" type="text" data-tips="${ruleTips}" data-rule="{@if rule}${rule};{@/if}len<=${maxLength?maxLength:20}" data-notnull="false" maxLength="${maxLength?maxLength:20}" placeholder ="${placeholder }" data-date data-type="${type}" {@if range}data-range="${range}"{@/if} data-trigger="focus" name="${jbe_col_key}" data-fmt="${pattern}" value="${tdText}"/>'+
				'{@else if type==="week" }'+
				'<input {@if readOnly}readonly="readonly"{@/if} {@if focusChangeToExtraForm} data-focus-changeto-extra-form="true" {@/if} {@if ajaxCheckUrl}data-ajax-check-url="${ajaxCheckUrl}"{@/if}  class="jbt_editor ${cssClass}" {@if cssStyle} style="${cssStyle}" {@/if}  autocomplete="off" type="week" data-tips="${ruleTips}" data-rule="{@if rule}${rule};{@/if}len<=${maxLength?maxLength:20}" data-notnull="false" maxLength="${maxLength?maxLength:20}" placeholder ="${placeholder }" name="${jbe_col_key}" value="${tdText}"/>'+
				'{@else if type==="autocomplete" }'+
				'<input data-mustmatch="${mustMatch}" {@if matchCase}data-match-case="${matchCase}"{@/if} {@if limit}data-limit="${limit}"{@/if} {@if readOnly}readonly="readonly"{@/if} {@if focusChangeToExtraForm} data-focus-changeto-extra-form="true" {@/if} data-trigger="click" data-delimiter="${delimiter}" {@if textAsValue}data-textasvalue="${textAsValue}"{@/if} {@if ajaxCheckUrl}data-ajax-check-url="${ajaxCheckUrl}"{@/if}  {@if linkPara} data-link-para-ele="${linkPara}" {@/if} {@if linkColumn} data-link-column="${linkColumn}" {@/if} class="jbt_editor ${cssClass}" {@if cssStyle} style="${cssStyle}" {@/if}  data-autocomplete data-hidden-input="${jbe_acpl_hiddenId}" autocomplete="off" type="text" data-tips="${ruleTips}" data-rule="{@if rule}${rule};{@/if}len<=${maxLength?maxLength:40}" data-notnull="false" maxLength="${maxLength?maxLength:40}" placeholder ="${placeholder }"  value="${tdText}" data-url="${url}"  {@if textAttr}data-text-attr="${textAttr}"{@/if}  {@if header}data-header="${header}"{@/if} {@if textAlign}data-text-align="${textAlign}"{@/if}   {@if width}data-width="${width}"{@/if} {@if height}data-height="${height}"{@/if}  {@if columnAttr}data-column-attr="${columnAttr}"{@/if} {@if valueAttr}data-value-attr="${valueAttr}"{@/if}/>'+
				'<input class="jbt_editor_hidden" type="hidden" id="${jbe_acpl_hiddenId}" name="${jbe_col_key}" value="${tdValue}" />'+
				'{@else if type==="jboltinput" }'+
				'<input {@if onlyAttrFilter}data-onlyattrfilter="${onlyAttrFilter}"{@/if} {@if onlyleaf}data-onlyleaf="${onlyleaf?true:false}"{@/if} {@if onlytype}data-onlytype="${onlytype?true:false}"{@/if} {@if readOnly}readonly="readonly"{@/if} {@if focusChangeToExtraForm} data-focus-changeto-extra-form="true" {@/if}  {@if textAsValue}data-textasvalue="${textAsValue}"{@/if}  {@if linkPara} data-link-para-ele="${linkPara}" {@/if} {@if linkColumn} data-link-column="${linkColumn}" {@/if} class="jbt_editor ${cssClass}" {@if cssStyle} style="${cssStyle}" {@/if}   data-jboltinput data-zero-clear="true" data-load-type="${loadType}" {@if openType} data-open-type="${openType}" {@/if} data-hidden-input="${jbe_jbi_hiddenId}" autocomplete="off" type="text" {@if contentId}data-content-id="${contentId}"{@/if} data-tips="${ruleTips}" data-rule="{@if rule}${rule};len<=${maxLength?maxLength:20}{@/if}" data-notnull="false" maxLength="${maxLength?maxLength:20}" placeholder ="${placeholder }"  value="${tdText}" {@if new_url}data-url="${new_url}"{@/if}   {@if width}data-width="${width}"{@/if} {@if height}data-height="${height}"{@/if} {@if filterHandler}data-filter-handler="${filterHandler}"{@/if} />'+
				'<input class="jbt_editor_hidden" type="hidden" id="${jbe_jbi_hiddenId}" name="${jbe_col_key}" value="${tdValue}" />'+
				'{@else if type==="select" }'+
				'<select data-select-type="${theme}" {@if focusChangeToExtraForm} data-focus-changeto-extra-form="true" {@/if} {@if linkPara} data-link-para-ele="${linkPara}" {@/if} {@if linkColumn} data-link-column="${linkColumn}" {@/if} class="jbt_editor ${cssClass}" {@if cssStyle} style="${cssStyle}" {@/if} data-tips="${ruleTips}" data-rule="select{@if rule};${rule}{@/if}" data-notnull="false" name="${jbe_col_key}" data-autoload {@if refresh} data-refresh="${refresh}" {@/if} {@if onlyleaf} data-onlyleaf="${onlyleaf}" {@/if} {@if delimiter} data-delimiter="${delimiter}" {@/if} data-url="${url}" {@if textAttr} data-text-attr="${textAttr}" {@/if} {@if valueAttr} data-value-attr="${valueAttr}" {@/if}  data-text="=请选择="  data-value="" data-default="${defaultValue}" data-select="${tdValue}"></select>'+
				'{@else if type==="dialogbtn" && withDialog }'+
				'<button  class="jbt_editor {@if cssClass}${cssClass}{@else}btn btn-light{@/if}" {@if cssStyle} style="${cssStyle}" {@/if} data-in-editable-td="true" onclick="DialogUtil.openBy(this)" data-btn="${dialog.btn}" {@if linkColumn} data-link-column="${linkColumn}" {@/if}  data-link-para-ele="{@if jbe_link_ele_id}#${jbe_link_ele_id}{@/if}{@if linkPara}{@if jbe_link_ele_id},{@/if}${linkPara}{@/if}" data-area="${dialog.area}"  data-title="${dialog.title}"   data-url="${dialog.url}">{@if icon}<i class="${icon} mr-1"></i>{@else}<i class="fa fa-search mr-1"></i>{@/if}${text?text:"按钮"}</button>'+
				'<input class="jbt_editor_hidden" type="hidden" id="${jbe_hidden_id}" name="${jbe_col_key}" value="${tdValue}" />'+
				'{@/if}'+
				'{@if withDialog && type != "dialogbtn"}'+
				'<div class="ac_append">'+
				'<button class="btn btn-light" data-in-editable-td="true" onclick="DialogUtil.openBy(this)" data-btn="${dialog.btn}" {@if linkColumn} data-link-column="${linkColumn}" {@/if}  data-link-para-ele="{@if jbe_link_ele_id}#${jbe_link_ele_id}{@/if}{@if linkPara}{@if jbe_link_ele_id},{@/if}${linkPara}{@/if}" data-area="${dialog.area}"  data-title="${dialog.title}"   data-url="${dialog.url}"><i class="fa fa-search mr-1"></i></button>'+
				'</div>'+
				'{@/if}'+
				'</div>';
			var colConfig;
			if(col_config.type == "auto"){
				var tdoptions = currentTd.data("col-options");
				if(!isOk(tdoptions)){
					this.colConfigAutoInitHandler(table,currentTd,col_config);
					tdoptions = currentTd.data("col-options");
				}
				colConfig = deepClone(tdoptions);
				if(!colConfig.editable){
					return "";
				}
			}else{
				colConfig = col_config;
			}
			colConfig.tdText=tdText;
			colConfig.tdValue=tdValue||tdText;
			this.processEditorColConfigInitByType(colConfig);

			if(colConfig.type=="autocomplete"){
				if(typeof(colConfig.mustMatch)=="undefined"){
					colConfig.mustMatch=true;
				}
			}

			if(colConfig.dialog){
				colConfig.withDialog=true;
				colConfig.parentCssClass="with_append";
				colConfig.dialog.area=colConfig.dialog.area||"980,600";
				colConfig.dialog.title=colConfig.dialog.title||"请选择";
				colConfig.dialog.btn=colConfig.dialog.btn||"选择,取消";
			}
			if(colConfig.changeColumns && (!colConfig.hasOwnProperty("handler")&&!colConfig.ischangecolumns)){
				colConfig.ischangecolumns=true;
				colConfig.handler=function(table,td,text,value,jsonData,chooseData){
					jboltTableUpdateOtherColumns(table,td.parent(),chooseData?chooseData:jsonData,colConfig.changeColumns);
				}
			}
			if(col_config.type == "auto"){
				currentTd.data("col-options",colConfig);
			}
			var html=juicer(tpl,colConfig);
			var editorEle=$(html);
			if(colConfig&&colConfig.hasOwnProperty("handler")){
				if(colConfig.type=="select" || colConfig.type=="autocomplete" || colConfig.type=="jboltinput"|| colConfig.type=="dialogbtn"){
					currentTd.data("dialog-choose-choose-handler",colConfig.handler).data("jbolttable-handler-ischangecolumns",colConfig.ischangecolumns);
					editorEle.find("input[data-autocomplete],input[data-jboltinput],select").data("jbolttable-handler",colConfig.handler).data("jbolttable-handler-ischangecolumns",colConfig.ischangecolumns);
				}else{
					currentTd.data("editable-value-change-handler",colConfig.handler).data("jbolttable-handler-ischangecolumns",colConfig.ischangecolumns);
				}
			}
			if(colConfig.type=="select"&&colConfig.options){
				editorEle.find("select").data("options",colConfig.options);
			}
			return editorEle;
		},
		//处理可编辑表格 td转editor
		processEditableEditor:function(table,currentTd,colConfig){
			//设置为正在编辑状态
			currentTd.attr("data-editing","true");
			currentTd.attr("data-column",colConfig.columnKey).data("column",colConfig.columnKey);
			currentTd.attr("data-submitattr",colConfig.submitAttr).data("submitattr",colConfig.submitAttr);
			currentTd.attr("data-required",colConfig.required).data("required",colConfig.required);
			//获取这一个单元格td现在具体的值 显示的text
			var tdValue=currentTd.data("value"),tdText=currentTd.data("text")||currentTd[0].innerText,editInput,editInputTempVal,tempHiddenInput,
				//td转editor
				editor=this.getTdEditorHtml(table,currentTd,colConfig,tdValue,tdText);
			currentTd.html(editor);
			//获取到可操作和输入的控件editor
			editInput=currentTd.find(".jbt_editor");
			var colTooltip = colConfig.tooltip;
			var tdcolOpts = currentTd.data("col-options");
			if(colConfig.type == "auto"){
				if(tdcolOpts){
					colTooltip = tdcolOpts.tooltip||'';
				}
			}
			if(colTooltip){
				initToolTip(currentTd);
			}
			editInput.data("tdtext",tdText);
			if(editInput.is("button")){
				editInput.val(tdText);
			}
			this.processEditFormatAndVal(table,currentTd,editInput,tdText);
			var configType = colConfig.type;
			if(configType == "auto"){
				if(tdcolOpts){
					configType = tdcolOpts.type||"input";
				}
			}
			//判断类型 给予默认处理
			switch(configType){
				case "input":
				case "price":
				case "amount":
				case "money":
				case "weight":
				case "age":
				case "input_number":
					JBoltInputWithClearBtnUtil.initInput(editInput);
					//携带计算器
					if(colConfig.withCalculator){
						JBoltInputWithCalculatorUtil.initInput(editInput,true);
					}
					break;
				case "datetime":
				case "date":
				case "time":
				case "year":
				case "month":
					FormDate.initDate(editInput);
					break;
				case "week":
					break;
				case "autocomplete":
					AutocompleteUtil.initInput(editInput);
					break;
				case "jboltinput":
					JBoltInputUtil.initInput(editInput);
					break;
				case "select":
					SelectUtil.initSelect(editInput);
					break;
			}
			//新增加 去掉传递
			editInput.on("focus",function(e){
				e.preventDefault();
				e.stopPropagation();
				return false;
			}).on("click",function(e){
				e.preventDefault();
				e.stopPropagation();
				return false;
			});
			editInput.focus().trigger("click");
			//最后将td原来的text属性给data-text 保存好
			currentTd.attr("data-text",tdText).data("text",tdText);
			/*var tr=currentTd.closest("tr");
				var tempId=tr.data("tempid");
				var dataid=tr.data("id");
				if(!tempId){
					if(dataid){
						tempId=dataid+"_jbtmp_"+randomId();
					}else{
						tempId="jbtmp_"+randomId();
					}

					tr.attr("data-tempid",tempId).data("tempid",tempId);
				}*/

			if(colConfig.tooltip){
				currentTd.find(".jbt_edit_ele[tooltip]").tooltip("show");
			}
		},
		processEditFormatAndVal:function(table,currentTd,editInput,tdText){
			if(editInput.is("button")){return false;}
			//处理转为editor后的取值
			var editInputTempVal=editInput.val();
			editInput.val("");
			editInputTempVal=this.processEditableTdEditorFormat(table,currentTd,editInputTempVal,tdText);
			if(editInputTempVal){
				editInput.val(editInputTempVal);
			}else{
				editInput.val("");
			}
		},
		processChangeTableListDatasColumnValue:function(table,td,value){
			//单元格编辑完成后执行数据变更
			var currentTdIndex=td.data("col-index");
			var thColumn,attrName;
			if(typeof(currentTdIndex)!="undefined" && currentTdIndex>=0 ){
				thColumn=table.indexColumnMap["col_"+currentTdIndex];
			}
			if(!thColumn){
				alert("表格配置异常 第"+td.index()+"列thead>th未设置data-column");
				return false;
			}
			attrName=td.data("submitattr");
			var colConfig=table.editableOptions.cols[thColumn];
			if(!attrName){
				if(colConfig){
					attrName=colConfig.submitAttr;
				}
				if(!attrName){
					if(thColumn.indexOf("_")!=-1){
						attrName=StrUtil.camel(thColumn);
					}else{
						attrName=thColumn;
					}
				}
			}

			if(!attrName){
				alert("表格的可编辑配置项cols里列的配置缺少submitAttr配置");
				return false;
			}
			var tr=td.parent();
			var dataIndex=tr.data("index");
			if(typeof(value)=="string"&&colConfig&&colConfig.rule){
				if(colConfig.type=="input_number"||colConfig.type=="input_ranger"|| colConfig.rule.indexOf("int")!=-1 || colConfig.rule.indexOf("number")!=-1){
					value=Number(value);
				}
			}
			JBoltArrayUtil.changeOneItemAttrValue(table.tableListDatas,dataIndex,attrName,value);
//				console.log(table.tableListDatas)
			td.processValue=value;
			return true;
		},
		processEditableTdChooseData:function(table,editingTd,text,value,dontExeValueChangeHandler){
			var that=this;
			var orignText=editingTd.data("origin-text");
			if(typeof(orignText)=="undefined"){
				orignText="";
			}
//				var editing = editingTd.data("editing");
//				var oldText = editing?"":(editingTd.data("text")||"");
			//单元格编辑完成后执行数据变更
			var success=this.processChangeTableListDatasColumnValue(table,editingTd,value);
			if(!success){
				return false;
			}

			var textHtmlFormatValue=editingTd.processValue;
			if(typeof(textHtmlFormatValue)=="number"&&text&&!isNaN(text)){
				if(text.length<19){
					text = Number(text);
				}
			}

			//修复data-value=0的问题
			if(typeof(editingTd.processValue)=="number"&&editingTd.processValue==0){
				if(typeof(text) == undefined || typeof(text)=="undefined" || text==""){
					editingTd.processValue="";
				}
			}
			if(typeof(text)!="number" && !text && typeof(textHtmlFormatValue)=="number" && textHtmlFormatValue==0){
				textHtmlFormatValue = "";
			}
			//单元格赋值
			editingTd.data("value",textHtmlFormatValue).attr("data-value",textHtmlFormatValue);
			text=that.processEditableTdTextFormat(table,editingTd,text,textHtmlFormatValue);
			editingTd.data("text",text).attr("data-text",text);
			var html=that.processEditableTdHtmlFormat(table,editingTd,text,textHtmlFormatValue);
			editingTd.html(html);
			editingTd.removeAttr("data-editing");
			if(value&&editingTd.hasClass("is-invalid")){
				editingTd.removeClass("is-invalid").removeClass("imgbg");
				editingTd.tooltip("dispose");
			}
			if(text.toString()==orignText.toString()){
				editingTd.data("changed",false).removeAttr("data-changed");
			}else{
				editingTd.data("changed",true).attr("data-changed",true);
				editingTd.data("syncdb",false).removeAttr("data-syncdb");
			}
//				if(isNaN(orignText)){
//				}else{
//				/*	if(Number(orignText)==0&&(Number(text)==0||text==""||text=="0"||text==undefined)){
//						editingTd.data("changed",false).removeAttr("data-changed");
//						editingTd.text(orignText);
//					}else */
//					if(text.toString()===orignText.toString()){
//						editingTd.data("changed",false).removeAttr("data-changed");
//					}else{
//						editingTd.data("changed",true).attr("data-changed",true);
//						editingTd.data("syncdb",false).removeAttr("data-syncdb");
//					}
//
//				}



			//处理所在TR 样式变更
			that.processEditableTrChangedStatusByTd(table,editingTd);
			var columnName = editingTd.data("column");
			if(!columnName){
				if(table.indexColumnMap){
					var currentTdIndex=editingTd.data("col-index");
					columnName=table.indexColumnMap["col_"+currentTdIndex];
				}
			}
			//如果summaryTriggerColumns 里有这一列 就可以触发
			if(columnName && table.summaryTriggerColumns[columnName]){
				//处理这一行的tbody summary
				var editTr = editingTd.parent();
				that.processTbodySummarysInTr(table,editTr);
			}
			//触发其他指定列summary
			that.processTriggerOtherColumnSummary(table,editingTd);
			//处理这一列的tfoot summary
			that.processTfootSummary(table,editingTd.data("col-index"));
			//处理单元格自动提交更新数据
			that.checkAndProcessOneColumnUpdate(table,editingTd);
			if(!dontExeValueChangeHandler){
				var vchandler=editingTd.data("editable-value-change-handler");
				if(vchandler&&typeof(vchandler)=="function"){
					var jsonData=table.tableListDatas[editingTd.closest("tr").data("index")];
					var ischangecolumns = editingTd.data("jbolttable-handler-ischangecolumns");
					if(!ischangecolumns){
						that.processColConfigChangeColumns(table,editingTd,jsonData);
					}
					vchandler(table,editingTd,text,value,jsonData);
				}
			}
			//editingTd.data("origin-text",'').attr("data-origin-text",'');
			return true;
		},
		processColConfigChangeColumns:function(table,editingTd,data){
			//handler里处理changeColumns
			var columnName = editingTd.data("column");
			if(!columnName){
				if(table.indexColumnMap){
					var currentTdIndex=editingTd.data("col-index");
					columnName=table.indexColumnMap["col_"+currentTdIndex];
				}
			}
			if(columnName){
				var colConfig=table.editableOptions.cols[columnName];
				if(colConfig&&colConfig.changeColumns){
					jboltTableUpdateOtherColumns(table,editingTd.parent(),data,colConfig.changeColumns);
//						console.log(table.tableListDatas)
				}
			}
		},
		processFocusChangeToExtraForm:function(table,editingTd){
			//处理焦点跳转到右侧form
			if(table.extraColumnForm){
				if(table.indexColumnMap){
					var currentTdIndex=editingTd.data("col-index");
					var thColumn=table.indexColumnMap["col_"+currentTdIndex];
					if(thColumn){
						var colConfig=table.editableOptions.cols[thColumn];
						if(colConfig&&colConfig.focusChangeToExtraForm){
							if(editingTd.data("editing")){
								this.processEditingTd(table,editingTd);
							}
							var firstControl=table.extraColumnForm.find(".form-control:not(':disabled'):not(':hidden'):eq(0)");
							if(isOk(firstControl)){
								firstControl.focus().focus();
								return true;
							}
						}
					}
				}
			}
			return false;
		},
		processEditingTd:function(table,editingTd){
			//input转td
			editingTd.find(".jbt_edit_ele[tooltip]").tooltip("dispose");
			var that=this,value="",text="",editingInputBox=editingTd.find(".jbt_editor"),hiddeninput;
			if(isOk(editingInputBox)){
				if(editingInputBox[0].tagName=="SELECT"){
					text=editingInputBox.find("option:selected").text();
					if(editingInputBox.data("text")===text){
						text="";
					}
					value=editingInputBox.val();

				}else if(editingInputBox[0].tagName=="INPUT"&&(editingInputBox.hasClass("ac_input")||editingInputBox[0].hasAttribute("data-jboltinput"))){
					if(editingInputBox.hasClass("ac_input")){
						var exeResult=editingInputBox.data("exe-result");
						if(exeResult){
							text=editingInputBox.val();
						}else{
							var mm = editingInputBox.data("mustmatch");
							if(typeof(mm)=="undefined"){
								mm=true;
							}
							if(mm){
								text=editingInputBox.data("tdtext")||editingInputBox.val();
							}else{
								text=editingInputBox.val();
							}

						}
					}else{
						text=editingInputBox.val();
					}
					hiddeninput=editingInputBox.data("hidden-input")||editingInputBox.data("hiddeninput");
					value=editingInputBox.closest(".jbt_edit_ele").find("#"+hiddeninput).val();
					if(editingInputBox.hasClass("ac_input")){
						//autocomplete组件需要额外处理ac_results
						var acrId=editingInputBox.data("acresult");
						if(acrId){
							$("#"+acrId).remove();
						}
					}
					if(value==''||value.length==0){
						if(editingInputBox.data("textasvalue")){
							value=(text!=""&&text.length>0)?text:"";
						}else{
							var mm = editingInputBox.data("mustmatch");
							if(typeof(mm)=="undefined"){
								mm=true;
							}
							if(mm){
								text="";
								value="";
							}else{
								value=(text!=""&&text.length>0)?text:"";
							}

						}
					}
				}else if(editingInputBox[0].tagName=="BUTTON"){
					text=editingInputBox.val();
					if(editingInputBox.data("textasvalue")){
						value=(text!=""&&text.length>0)?text:"";
					}else{
						value=text;
					}
				}else{
					text=editingInputBox.val();
					value=text;
				}
				if(FormChecker.checkIt(editingInputBox,true)){
					that.processEditableTdChooseData(table,editingTd,text,value);
					return true;
				}

				that.changeTdFocus(table,editingTd);
				return false;
			}else{
				//单元格编辑完成后执行数据变更
				var success=this.processChangeTableListDatasColumnValue(table,editingTd,value);
				if(!success){
					return false;
				}
				editingTd.data("value",value).attr("data-value",value);
				text=that.processEditableTdTextFormat(table,editingTd,text,value);
				editingTd.data("text",text).attr("data-text",text);
				var html=that.processEditableTdHtmlFormat(table,editingTd,text,value);
				editingTd.html(html);
				editingTd.removeAttr("data-editing");
				editingTd.data("changed",false).removeAttr("data-changed");
				editingTd.data("syncdb",false).removeAttr("data-syncdb");
				//处理所在TR 样式变更
				that.processEditableTrChangedStatusByTd(table,editingTd);
				//处理这一列的tfoot summary
				that.processTfootSummary(table,editingTd.data("col-index"));
				//触发其他指定列summary
				that.processTriggerOtherColumnSummary(table,editingTd);

				var vchandler=editingTd.data("editable-value-change-handler");
				if(vchandler&&typeof(vchandler)=="function"){
					var jsonData=table.tableListDatas[editingTd.closest("tr").data("index")];
					var ischangecolumns = editingTd.data("jbolttable-handler-ischangecolumns");
					if(!ischangecolumns){
						that.processColConfigChangeColumns(table,editingTd,jsonData);
					}
					vchandler(table,editingTd,text,value,jsonData);
				}
			}
			return true;
		},processEditableTdTextFormat:function(table,td,text,value){
			var column=td.data("column");
			var result=text;
			td.data("beTextFormat",false);
			if(column){
				var colConfig=table.editableOptions.cols[column];
				if(colConfig&&colConfig.textFormat){
					var dataIndex=td.parent().data("index");
					var trJsonData = table.tableListDatas[dataIndex];
					result=colConfig.textFormat(table,td,text,value,trJsonData);
					td.data("beTextFormat",true);
				}
			}
			return result;
		},processEditableTdHtmlFormat:function(table,td,text,value){
			var column=td.data("column");
			var html=text;
			if(column){
				var colConfig=table.editableOptions.cols[column];
				if(colConfig&&colConfig.htmlFormat){
					var dataIndex=td.parent().data("index");
					var trJsonData = table.tableListDatas[dataIndex];
					html=colConfig.htmlFormat(table,td,text,value,trJsonData);
				}
			}
			return html;
		},processEditableTdEditorFormat:function(table,td,value,text){
			var column=td.data("column");
			td.data("text-before-editor-format",text);
			var result=value;
			if(column){
				var colConfig=table.editableOptions.cols[column];
				if(colConfig&&colConfig.editorFormat){
					var dataIndex=td.parent().data("index");
					var trJsonData = table.tableListDatas[dataIndex];
					result=colConfig.editorFormat(table,td,text,value,trJsonData);
				}
			}
			return result;
		},
		//处理editiing状态的tds
		processEditingTds:function(table,dontProcessExtraSomthing){
			if(!dontProcessExtraSomthing){
				FormDate.hide(table.table_view);
			}
			var that=this;
			var editingTds=table.table_box.find("table>tbody>tr>td[data-col-index][data-editing='true']");
			if(isOk(editingTds)){
				var editingTd,fail=false,result;
				editingTds.each(function(){
					editingTd=$(this);
					result=that.processEditingTd(table,editingTd);
					if(!result&&!fail){
						fail=true;
					}
				});
				return !fail;
			}
			return true;
		},
		checkAndProcessOneColumnUpdate:function(table,td){
			//每个可编辑单元格恢复不可编辑状态的时候 需要判断是否需要提交更新这个字段
			if(table.editable&&td.data("changed")){
				//首先你得是changed数据才有机会更新 然后看submit配置里是不是cell
				if(table.editableOptions&&table.editableOptions.submit){
					var type=table.editableOptions.submit.type;
					if(type&&type=="cell"){
						var tr=td.parent();
						if(tr.data("id")){
							this.updateOneColumn(table,td);
						}
					}
				}
			}
		},
		processEditableTrChangedStatusByTd:function(table,editingTd){
			var currentTr=editingTd.parent();
			this.processEditableTrChangedStatus(table,currentTr);
		},
		processEditableTrChangedStatus:function(table,currentTr,forceTrChange){
			if(forceTrChange){
				currentTr.data("force-change",true).attr("data-force-change",true);
			}else{
				forceTrChange = currentTr.data("force-change")||false;
			}
			//处理当前编辑行的data-changed状态
			var trOldChanged=currentTr.data("changed");
			var result;

			var changedTds=currentTr.find("td[data-changed='true']");
			var trHasExtraColumnData=currentTr.data("has-extra-column-data");
			if(isOk(changedTds)||trHasExtraColumnData||forceTrChange){
				result=true;
			}else{
				result=false;
			}

			var syncResult;
			var syncdbTds=currentTr.find("td[data-syncdb='true']");
			if(isOk(syncdbTds)){
				syncResult=true;
			}else{
				syncResult=false;
			}

			if(syncResult){
				currentTr.data("syncdb",syncResult).attr("data-syncdb",syncResult);
			}else{
				currentTr.data("syncdb",false).removeAttr("data-syncdb");
			}
			currentTr.data("needupdate",false).removeAttr("data-needupdate");
			currentTr.data("needsave",false).removeAttr("data-needsave");

			if(result){
				currentTr.data("changed",result).attr("data-changed",result);
				currentTr.data("syncdb",false).removeAttr("data-syncdb");
				if(!trOldChanged){
					this.processTdsColumnAndSubmitInfoByTr(table,currentTr);
				}
				var id=currentTr.data("id");
				if(id){
					currentTr.data("needupdate",true).attr("data-needupdate",true);
				}else{
					currentTr.data("needsave",true).attr("data-needsave",true);
				}
			}else{
				currentTr.data("changed",false).removeAttr("data-changed");
			}



		},
		processEditableTableFillMinCountRows:function(table){
			if(!table.editable){return false;}
			var minCount=table.editableOptions.initRowCount||0;
			if(!minCount){return false;}
			//做补全之前 先处理empty问题
			this.processEmptyTableBody(table);
			//处理补全可编辑表格
			var newCount=0;
			var realCount=0;
			if(table.isEmpty){
				newCount=minCount;
			}else{
				realCount=table.tbody.find("tr").length;
				if(realCount<minCount){
					newCount=minCount-realCount;
				}
			}
			if(newCount>0){
				//进行补充处理
				this.insertEmptyRows(table,newCount);
			}
		},
		reProcessEditableTfootSummarys:function(table){
			if(!table.editable||!table.editableOptions||!table.editableOptions.cols){return false;}
			if(table.hasFooter&&isOk(table.vsummarys)){
				//处理tfoot summary
				this.processTfootSummarys(table);
			}
		},
		reProcessEditableAllSummarys:function(table){
			if(!table.editable||!table.editableOptions||!table.editableOptions.cols){return false;}
			//处理tbody中的横向summarys计算
			this.processTbodyHSummarys(table);
			//处理tfoot summary
			this.processTfootSummarys(table);
		},
		reProcessEditableAllSummarysAfterInsertDataRows:function(table,trs){
			if(!table.editable||!table.editableOptions||!table.editableOptions.cols){return false;}
			if(!isOk(trs)){return false;}
			var that=this;
			if(table.hasFooter&&isOk(table.hsummarys)){
				//处理tbody summary
				var tr;
				trs.each(function(){
					tr=$(this);
					that.processTbodySummarysInTr(table,tr);
				});
			}
			if(table.hasFooter&&isOk(table.vsummarys)){
				//处理tfoot summary
				this.processTfootSummarys(table);
			}
		},
		initEditableSummarys:function(table,colConfigs){
			//初始化表格的summary信息 为自动统计计算做准备
			if(!colConfigs||colConfigs.length==0){return false;}
			var vs=new Array();
			var hs=new Array();
			var summaryTriggerColumns={};
			var temp,tempSummary,key,colIndex,arrayItem;
			var keys=Object.keys(colConfigs);
			for(var i in keys){
				key=keys[i];
				temp=colConfigs[key];
				if(temp&&temp.summary){
					if(isArray(temp.summary)){
						for(var i in temp.summary){
							arrayItem=temp.summary[i];
							if(arrayItem.dir&&arrayItem.formula){
								tempSummary={
									formula:arrayItem.formula,
									column:key,
									auto:((typeof(arrayItem.auto)=="undefined")?true:arrayItem.auto),
									submitAttr:temp.submitAttr,
									colIndex:-1,
									roundtag:(arrayItem.roundtag?arrayItem.roundtag:"round"),
									tofixed:(arrayItem.tofixed?arrayItem.tofixed:0),
									removezero:(arrayItem.removezero?arrayItem.removezero:false),
									handler:arrayItem.handler,
									syncval:arrayItem.syncval
								};

								if(table.columnIndexMap){
									colIndex=table.columnIndexMap[key];
									if(colIndex!=undefined&&colIndex>=0){
										tempSummary.colIndex=colIndex;
									}
								}
								if(arrayItem.dir=="h"){
									tempSummary['hasVSummary']=true;
									hs.push(tempSummary);
								}else if(arrayItem.dir=="v"){
									vs.push(tempSummary);
								}
								if(arrayItem.formula!="sum" && arrayItem.formula!="max" && arrayItem.formula!="min" && arrayItem.formula!="avg"){
									//如果不是触发器 判断一下6871
									for(var j in keys){
										if(typeof(summaryTriggerColumns[keys[j]])=="undefined"){
											if(arrayItem.formula.indexOf(keys[j])!=-1){
												//如果公式里有它 就是个触发器
												summaryTriggerColumns[keys[j]]=true;
											}
										}
									}
								}

							}
						}

					}else if(temp.summary.dir&&temp.summary.formula){
						tempSummary={
							formula:temp.summary.formula,
							column:key,
							auto:((typeof(temp.summary.auto)=="undefined")?true:temp.summary.auto),
							submitAttr:temp.submitAttr,
							colIndex:-1,
							roundtag:(temp.summary.roundtag?temp.summary.roundtag:"round"),
							tofixed:(temp.summary.tofixed?temp.summary.tofixed:0),
							removezero:(temp.summary.removezero?temp.summary.removezero:0),
							handler:temp.summary.handler,
							syncval:temp.summary.syncval
						};
						if(table.columnIndexMap){
							colIndex=table.columnIndexMap[key];
							if(colIndex!=undefined&&colIndex>=0){
								tempSummary.colIndex=colIndex;
							}
						}
						if(temp.summary.dir=="h"){
							hs.push(tempSummary);
						}else if(temp.summary.dir=="v"){
							vs.push(tempSummary);
						}
						if(typeof(summaryTriggerColumns[key])=="undefined"){
							//如果不是触发器 判断一下6871
							if(temp.summary.formula.indexOf(key)!=-1){
								//如果公式里有它 就是个触发器
								summaryTriggerColumns[key]=true;
							}
						}
						if(temp.summary.formula!="sum" && temp.summary.formula!="max" && temp.summary.formula!="min" && temp.summary.formula!="avg"){
							//如果不是触发器 判断一下6871
							for(var j in keys){
								if(typeof(summaryTriggerColumns[keys[j]])=="undefined"){
									if(temp.summary.formula.indexOf(keys[j])!=-1){
										//如果公式里有它 就是个触发器
										summaryTriggerColumns[keys[j]]=true;
									}
								}
							}
						}
					}
				}else if(temp&&temp.changeColumns){
					var changeColumn,changeColumnName;
					for(var i in temp.changeColumns){
						changeColumn = temp.changeColumns[i];
						if(typeof(changeColumn) == "string"){
							changeColumnName = changeColumn;
						}else if(typeof(changeColumn) == "object"){
							changeColumnName = changeColumn.column;
						}
						if(typeof(summaryTriggerColumns[changeColumnName])=="undefined"){
							summaryTriggerColumns[changeColumnName]=true;
						}
					}
				}
			}
			if(isOk(vs)){
				table.vsummarys=vs;
				this.initEditableVSummarys(table);
			}
			if(isOk(hs)){
				table.hsummarys=hs;
				if(!table.isAjax){
					this.initEditableHSummarys(table);
				}
			}
			table.summaryTriggerColumns = summaryTriggerColumns;
		},
		initEditableVSummarys:function(table){
			//初始化 纵向 tfoot上的summary
			var vs=table.vsummarys;
			if(!isOk(vs)){
				return false;
			}
			var colIndex=-1,v,th;
			for(var i in vs){
				v=vs[i];
				colIndex=v.colIndex;
				if(colIndex>=0){
					//说明存在的配置
					th=table.tfoot.find("tr>th[data-col-index='"+colIndex+"']");
					if(isOk(th)){
						th.attr("data-summary",true).data("summary",true);
						th.attr("data-formula",v.formula).data("formula",v.formula);
						th.attr("data-tofixed",v.tofixed).data("tofixed",v.tofixed);
						th.attr("data-removezero",v.removezero).data("removezero",v.removezero);
						th.attr("data-removeendzero",v.removeendzero).data("removeendzero",v.removeendzero);
						th.attr("data-roundtag",v.roundtag).data("roundtag",v.roundtag);
						if(v.handler){
							th.attr("data-handler",v.handler).data("handler",v.handler);
							th.off("exeSummaryHandler").on("exeSummaryHandler",function(){
								var edTh = $(this);
								var edtr = edTh.closest("tr");
								var thValue = edTh.data("value");
								var handler=edTh.data("handler");
								if(handler && typeof(handler)=="function"){
									handler(table,edtr,edTh,thValue);
								}
							});
						}
						if(v.syncval){
							th.attr("data-syncval",v.syncval).data("syncval",v.syncval);
						}
					}
				}
			}
		},
		initEditableHSummarys:function(table,tr){
			//初始化 tbody里横向 上的summary
			var hs=table.hsummarys;
			if(!isOk(hs)){
				return false;
			}
			var colIndex=-1,h,td;
			for(var i in hs){
				h=hs[i];
				colIndex=h.colIndex;
				if(colIndex>=0){
					//说明存在的配置
					if(isOk(tr)){
						td=tr.find("td[data-col-index='"+colIndex+"']");
					}else{
						td=table.tbody.find("tr>td[data-col-index='"+colIndex+"']");
					}
					if(isOk(td)){
						if(h.hasVSummary){
							td.attr("data-hasvsummary",true).data("hasvsummary",true);
						}
						td.attr("data-summary",true).data("summary",true);
						td.attr("data-summary-auto",h.auto).data("summary-auto",h.auto);
						td.attr("data-formula",h.formula).data("formula",h.formula);
						td.attr("data-tofixed",h.tofixed).data("tofixed",h.tofixed);
						td.attr("data-removezero",h.removezero).data("removezero",h.removezero);
						td.attr("data-removeendzero",h.removeendzero).data("removeendzero",h.removeendzero);
						td.attr("data-roundtag",h.roundtag).data("roundtag",h.roundtag);
						if(h.handler){
							td.attr("data-handler",true).data("handler",true);
							td.off("exeSummaryHandler").on("exeSummaryHandler",function(){
								var edtd = $(this);
								var edtr = edtd.closest("tr");
								var trdataIndex=edtr.data("index");
								var trJsonData=isOk(table.tableListDatas)?table.tableListDatas[trdataIndex]:null;
								h.handler(table,edtr,edtd,edtd.data("value"),trJsonData);
							});
						}
						if(h.syncval){
							td.attr("data-syncval",h.syncval).data("syncval",h.syncval);
						}
					}
				}
			}

		},
		//初始化表格可编辑cols配置
		initTableEditableOptionCols:function(table){
			//根据thead中的data-column列 默认设置
			var columnThs=table.thead.find("tr>th[data-column!='']");
			if(!columnThs){return null;}
			var th,dataType,editable=false,column,cols={},type="input";
			columnThs.each(function(){
				th=$(this);
				column=th.data("column");
				if(column){
					editable=th.data("editable");
					if(typeof(editable)=="boolean"&&editable==false){
						return true;
					}
					dataType=th.data("type");
					column=column.toLowerCase();
					if(column=="price"||column=="amount"||column=="age"||column=="money"||column=="weight"){
						type=dataType||column;
						cols[column]={
							type:type,
							submitAttr:column
						}
					}else if(column.indexOf("date")!=-1||column.indexOf("time")!=-1){
						type=dataType||"datetime";
						cols[column]={
							type:type,
							submitAttr:StrUtil.camel(column)
						}
					}else if(column!="index"&&column!="optcol"&&column!="checkbox"&&column!="radio"){
						type="input";
						cols[column]={
							type:type,
							submitAttr:StrUtil.camel(column)
						}
					}
				}

			});
			return cols;
		},
		initEditableCheckbox:function(table,colConfigs){
			if(!colConfigs||colConfigs.length==0){return false;}
			var temp,column,checkbox,editingTd,that=this,result,submitattr;
			table.table_box.on("change","table>tbody>tr>td[data-col-index] input[type='checkbox'][name!='jboltTableCheckbox']",function(e){
				checkbox=$(this);
				editingTd=checkbox.closest("td");
				that.processCheckboxTd(table,editingTd,checkbox,this.checked);
			});
		},
		initEditableCheckboxDataInfo:function(table){
			if(!table.editable){return false;}
			var colConfigs=table.editableOptions.cols;
			if(!colConfigs||colConfigs.length==0){return false;}
			var temp,column,checkbox,editingTd,that=this,result,submitattr;
			table.table_box.find("table>tbody>tr>td[data-col-index] input[type='checkbox'][name!='jboltTableCheckbox']").each(function(){
				checkbox=$(this);
				column=this.name;
				if(column){
					temp=colConfigs[column];
					if(temp&&temp.type=="checkbox"){
						if(column.indexOf("_")!=-1){
							submitattr=StrUtil.camel(column);
						}else{
							submitattr=column;
						}
						editingTd=checkbox.closest("td");
						result=this.checked?"true":"false";
						editingTd.data("value",result).attr("data-value",result);
						editingTd.data("text",result).attr("data-text",result);
						editingTd.data("origin-text",result).attr("data-origin-text",result);
						editingTd.data("column",column).attr("data-column","column");
						editingTd.data("submitattr",submitattr).attr("data-column",submitattr);
					}
				}
			});
		},
		initEditableSwitchBtnDataInfo:function(table){
			if(!table.editable){return false;}
			var colConfigs=table.editableOptions.cols;
			if(!colConfigs||colConfigs.length==0){return false;}
			var temp,column,switchbtn,editingTd,that=this,result,submitattr;
			table.table_box.find("table>tbody>tr>td[data-col-index] img[data-switchbtn]").each(function(){
				switchbtn=$(this);
				column=this.name;
				if(column){
					temp=colConfigs[column];
					if(temp&&temp.type=="switchbtn"){
						if(column.indexOf("_")!=-1){
							submitattr=StrUtil.camel(column);
						}else{
							submitattr=column;
						}
						editingTd=switchbtn.closest("td");
						result=switchbtn.data("value");
						editingTd.data("value",result).attr("data-value",result);
						editingTd.data("text",result).attr("data-text",result);
						editingTd.data("origin-text",result).attr("data-origin-text",result);
						editingTd.data("column",column).attr("data-column","column");
						editingTd.data("submitattr",submitattr).attr("data-column",submitattr);
					}
				}
			});
		},
		initTableEditableRequiredStyle:function(table,cols){
			if(!table.columnIndexMap){return false;}
			//初始化可编辑表格里的可编辑和必填列的样式问题
			var keys=Object.keys(table.columnIndexMap);
			var empKey,colConfig,th,colIndex=-1,requiredCellClass;
			for(var i in keys){
				empKey=keys[i];
				colConfig=cols[empKey];
				colIndex=-1;
				if(colConfig&&colConfig.editable){
					colIndex=table.columnIndexMap[empKey];
					if(colIndex>=0){
						//必填列就得上面列头加红星了
						th=table.table_box.find("table>thead>tr>th[data-col-index='"+colIndex+"']");
						if(isOk(th)){
							if(colConfig.required){
								th.addClass("is_required");
								if(!table.isEmpty&&!table.isAjax){
									//可编辑列 就得加底色了
									var tds = table.table_box.find("table>tbody>tr>td[data-col-index='"+colIndex+"']");
									if(isOk(tds)){
										tds.data("editable",true).attr("data-editable",true);
										tds.data("required",true).attr("data-required",true);
										requiredCellClass=colConfig.requiredCellClass||table.editableOptions.requiredCellClass;
										if(requiredCellClass){
											tds.addClass(requiredCellClass);
										}
									}
								}
							}else{
								if(!table.isEmpty&&!table.isAjax){
									//可编辑列 就得加底色了
									var tds = table.table_box.find("table>tbody>tr>td[data-col-index='"+colIndex+"']");
									if(isOk(tds)){
										tds.data("editable",true).attr("data-editable",true);
										if(table.editableOptions.unrequiredCellClass){
											tds.addClass(table.editableOptions.unrequiredCellClass);
										}
									}
								}
							}
						}
					}

				}
			}

			if(!table.isEmpty&&!table.isAjax){
				uneditableCellCalss=table.editableOptions.uneditableCellCalss;
				if(uneditableCellCalss){
					var cssSelector= "table>tbody>tr>td:not([data-editable='true'])";
					if(table.editableOptions.keepCellStyleCols){
						var colIndex=-1;
						$.each(table.editableOptions.keepCellStyleCols,function(i,index){
							if(typeof(index)=="number"){
								cssSelector=cssSelector+":not([data-col-index='"+(index-1)+"'])";
							}else if(typeof(index)=="string"){
								colIndex=table.columnIndexMap[index];
								if(colIndex>=0){
									cssSelector=cssSelector+":not([data-col-index='"+colIndex+"'])";
								}
							}
						});
					}
					var tds = table.table_box.find(cssSelector);
					if(isOk(tds)){
						tds.addClass(uneditableCellCalss).data("editable",false).attr("data-editable",false);
					}
				}
			}
		},
		refreshEditableConfigOptions:function(table){
			var editable=table.data("editable");
			if(!editable){table.editable=false;return false;}
			table.submit_delete_ids=null;
			//开始获取关于editor的json配置
			var options=null;
			var optionsStr=table.data("editable-option");
			if(optionsStr){
				if(typeof(optionsStr)=="object"){
					options = optionsStr;
				}else{
					var optionFunc=eval(optionsStr);
					if(optionFunc&&typeof(optionFunc)=="function"){
						options=optionFunc(table);
					}else{
						LayerMsgBox.alert("表格data-editable-option设置异常，值应为一个function",2);
						return false;
					}
				}
			}else{
				options={};

				var trigger=table.data("editable-trigger");
				if(trigger){
					options.trigger=trigger;
				}
				options.initRowCount=table.data("editable-initrowcount")||0;
				options.maxRowCount=table.data("editable-maxrowcount")||100;
				var submitStr=table.data("editable-submit");
				if(submitStr){
					var submitFunc=eval(submitStr);
					if(submitFunc&&typeof(submitFunc)=="function"){
						options.submit=submitFunc(table);
					}else{
						LayerMsgBox.alert("表格data-editable-submit设置异常，值应为一个function",2);
						return false;
					}
				}


				var colsStr=table.data("editable-cols");
				if(colsStr){
					var colsFunc=eval(colsStr);
					if(colsFunc&&typeof(colsFunc)=="function"){
						options.cols=colsFunc();
					}else{
						LayerMsgBox.alert("表格data-editable-cols，值应为一个function",2);
						return false;
					}
				}

				var editablesummaryTriggerStr=table.data("editable-resummary-trigger");
				if(editablesummaryTriggerStr){
					options.summaryTrigger=editablesummaryTriggerStr;
				}

			}



			if(!options){LayerMsgBox.alert("表格data-editable-option设置异常",2);return false;}
			if(!options.cols){
				options.cols=this.initTableEditableOptionCols(table);
			}
			if(!options.cols){LayerMsgBox.alert("表格data-editable-option中未设置cols",2);return false;}
			if(!options.trigger||(options.trigger!="click"&&options.trigger!="dblclick")){
				options.trigger="dblclick";
			}

			table.editable=true;
			//是否启用required校验的背景图效果
			var requiredImgbg=table.data("required-imgbg");
			if(typeof(requiredImgbg)=="undefined"){
				requiredImgbg=true;
			}
			table.requiredImgbg=requiredImgbg;
			//处理每个cols配置中的可编辑属性
			var optcolConfig;
			for(var i in options.cols){
				optcolConfig=options.cols[i];
				if(typeof(optcolConfig.editable)=="undefined"){
					optcolConfig.editable=isOk(optcolConfig.type);
				}
			}
			table.editableOptions=options;
			//辅助录入初始化事件
			if(table.editableOptions.extraColumnForm){
				table.extraColumnForm=getRealJqueryObject(table.editableOptions.extraColumnForm);
			}
			//初始化表格的summary信息 为自动统计计算做准备
			this.initEditableSummarys(table,table.editableOptions.cols);
			//初始化列对应表头 required样式 普通表格初始化 ajax的单独地方
			this.initTableEditableRequiredStyle(table,table.editableOptions.cols);
			this.processEditableReSummaryTrigger(table);
			this.processEditableColConfigAndSortColIndex(table);
			return true;
		},
		//处理可编辑表格
		processEditable:function(table){
			var editable=table.data("editable");
			if(!editable){table.editable=false;return false;}
			initJBoltEditableKeyEvent();
			table.submit_delete_ids=null;
			//开始获取关于editor的json配置
			var options=null;
			var optionsStr=table.data("editable-option");
			if(optionsStr){
				if(typeof(optionsStr)=="object"){
					options = optionsStr;
				}else{
					var optionFunc=eval(optionsStr);
					if(optionFunc&&typeof(optionFunc)=="function"){
						options=optionFunc(table);
					}else{
						LayerMsgBox.alert("表格data-editable-option设置异常，值应为一个function",2);
						return false;
					}
				}
			}else{
				options={};

				var trigger=table.data("editable-trigger");
				if(trigger){
					options.trigger=trigger;
				}
				options.initRowCount=table.data("editable-initrowcount")||0;
				options.maxRowCount=table.data("editable-maxrowcount")||100;
				var submitStr=table.data("editable-submit");
				if(submitStr){
					var submitFunc=eval(submitStr);
					if(submitFunc&&typeof(submitFunc)=="function"){
						options.submit=submitFunc(table);
					}else{
						LayerMsgBox.alert("表格data-editable-submit设置异常，值应为一个function",2);
						return false;
					}
				}


				var colsStr=table.data("editable-cols");
				if(colsStr){
					var colsFunc=eval(colsStr);
					if(colsFunc&&typeof(colsFunc)=="function"){
						options.cols=colsFunc();
					}else{
						LayerMsgBox.alert("表格data-editable-cols，值应为一个function",2);
						return false;
					}
				}

				var editablesummaryTriggerStr=table.data("editable-resummary-trigger");
				if(editablesummaryTriggerStr){
					options.summaryTrigger=editablesummaryTriggerStr;
				}

			}



			if(!options){LayerMsgBox.alert("表格data-editable-option设置异常",2);return false;}
			if(!options.cols){
				options.cols=this.initTableEditableOptionCols(table);
			}
			if(!options.cols){LayerMsgBox.alert("表格data-editable-option中未设置cols",2);return false;}
			if(!options.trigger||(options.trigger!="click"&&options.trigger!="dblclick")){
				options.trigger="dblclick";
			}

			table.editable=true;
			//是否启用required校验的背景图效果
			var requiredImgbg=table.data("required-imgbg");
			if(typeof(requiredImgbg)=="undefined"){
				requiredImgbg=true;
			}
			table.requiredImgbg=requiredImgbg;
			var hasautocomplete=false;
			//处理每个cols配置中的可编辑属性
			var optcolConfig;
			for(var i in options.cols){
				optcolConfig=options.cols[i];
				if(typeof(optcolConfig.editable)=="undefined"){
					optcolConfig.editable=isOk(optcolConfig.type);
				}
				if(!hasautocomplete&&optcolConfig.type=="autocomplete"){
					hasautocomplete=true;
				}
			}
			if(typeof(options.vsummaryFirstByAutoCalc)=="undefined"){
				//如果没有 默认是true
				options.vsummaryFirstByAutoCalc=true;
			}
			if(typeof(options.hsummaryFirstByAutoCalc)=="undefined"){
				//如果没有 默认是false
				options.hsummaryFirstByAutoCalc=false;
			}
			table.editableOptions=options;
			if(hasautocomplete){
				loadJBoltPlugin(['autocomplete']);
			}
			//辅助录入初始化事件
			if(table.editableOptions.extraColumnForm){
				table.extraColumnForm=getRealJqueryObject(table.editableOptions.extraColumnForm);
//					table.extraColumnForm.on("savetotr",function(){
//						jboltTableSaveFormToTableCurrentActiveTr(table.extraColumnForm);
//					});
				/*table.extraColumnForm.on("keydown",function(e){
						if(e.ctrlKey&&e.altKey){
							if(e.which==13){
								e.preventDefault();
								e.stopPropagation();
								var td=table.tbody.find("tr>td[data-focus='true']");
								if(isOk(td)){
									table.isEditableLock=true;
									td.trigger(options.trigger);
//									that.changeTdFocus(table,td);
								}
								return false;
							}
						}
					});*/
			}
			var that=this;
			var rowClickActive=table.data("row-click-active");
			var rowClickHandler=table.data("row-click-handler");


			//拿到配置的列数据json
			var cols=options.cols;
			//初始化表格 checkbox
			that.initEditableCheckbox(table,cols);
			//初始化表格的summary信息 为自动统计计算做准备
			that.initEditableSummarys(table,cols);
			//初始化列对应表头 required样式 普通表格初始化 ajax的单独地方
			that.initTableEditableRequiredStyle(table,cols);
			//定义出下面用的几个变量
			var trigger = options.trigger,colConfig,currentTd,currentTdColIndex,tempColTh,tempColKey,tempSubmitAttr,editingTd,editingInput,orignText,currentTr;
			//绑定td单击事件 出现指定的编辑器
			table.table_box.on("click",function(e){
				var tt=$(e.target);
				var pp=tt.closest(".jbolt_table_rightbox,.jbolt_table_rightbox,.jbolt_table_footbox,.jbolt_table_toolbar");
				//处理editing的td恢复原状
				that.processEditingTds(table,isOk(pp));
			}).on("mouseover","table[data-editable='true']>tbody>tr>td[data-col-index][data-submitattr][data-required='true'].is-invalid",function(e){
				var target=$(this);
				target.tooltip({ boundary: 'window',container:"body",title:"必填项"}).tooltip("show");
				$("#"+target.attr("aria-describedby")).addClass("error");
			}).on("click","table[data-editable='true']>tbody>tr>td[data-col-index]",function(e){
				that.changeTdFocus(table,$(this));
			}).on(trigger,"table[data-editable='true']>tbody>tr>td[data-col-index]:not([data-editing='true'])",function(e){
				//关闭打开的菜单先
				that.closeMenu(table);
				changeJBoltCurrentEditableAndKeyEventTable(table);
				//双击未编辑的表格事件
				var targetName=e.target.tagName;
				//input和select组件 直接返回不执行
				if((targetName =="INPUT" && e.target.type!="checkbox" && e.target.type!="radio") || targetName=="SELECT" || (targetName=="IMG"&& e.target.hasAttribute("data-switchbtn"))){return;}
				//处理editing的td恢复原状
				var result=that.processEditingTds(table);
				if(!result){
					return;
				}
				if(table.willCopy && trigger=="click"){
					return;
				}
				currentTd=$(this);
				currentTr=currentTd.parent();
				if((rowClickActive||rowClickHandler)&&!currentTr.hasClass("active")){
					currentTr.trigger("click");
				}
				var hasEditableAttr=currentTd[0].hasAttribute("data-editable");
				var tdSelfEditable=null;
				if(hasEditableAttr){
					tdSelfEditable=currentTd.data("editable");
					if(!tdSelfEditable){
						return;
					}
				}
				currentTdColIndex=currentTd.data("col-index");
				tempColTh=table.thead.find("th[data-col-index='"+currentTdColIndex+"'][data-column]");
				if(!isOk(tempColTh)){return;}
				tempColKey=tempColTh.data("column");
				if(!tempColKey){
					return;
				}
				colConfig=table.editableOptions.cols[tempColKey];
				if(!colConfig){
					//未设置tempColKey下的的配置
					return;
				}
				if(!colConfig.type){
					return;
				}
				if(!colConfig.editable){
					if(!hasEditableAttr||(hasEditableAttr&&tdSelfEditable!=null&&tdSelfEditable==false)){
						e.preventDefault();
						e.stopPropagation();
						return;
					}
				}

				//双击未编辑的表格事件
				e.preventDefault();
				e.stopPropagation();
				if(!colConfig.submitAttr){
					if(tempColKey.indexOf("_")!=-1){
						colConfig.submitAttr=StrUtil.camel(tempColKey);
					}else{
						colConfig.submitAttr=tempColKey;
					}
				}
				if(typeof(colConfig.required)=="undefined"||colConfig.required=='undefined'){
					colConfig.required=false;
				}
				colConfig.columnKey=tempColKey;
				colConfig.jbe_col_key=colConfig.submitAttr;
				orignText=currentTd.data("origin-text");
				var originTextType=typeof(orignText);
				if(originTextType=="undefined"&&!orignText&&!currentTd[0].hasAttribute("data-origin-text")){
					currentTd.attr("data-origin-text",currentTd[0].innerText).data("origin-text",currentTd[0].innerText);
				}
				//处理点击的td转editor
				that.processEditableEditor(table,currentTd,colConfig);
				table.isEditableLock=true;
				return false;
			}).on("blur","table[data-editable='true']>tbody>tr>td[data-col-index]>.jbt_edit_ele>input,table>tbody>tr>td[data-col-index]>.jbt_edit_ele>textarea",function(e){
				//blur后删掉编辑器恢复td 同步数据
				e.preventDefault();
				e.stopPropagation();
//					editingInput=$(this);
//					currentTd=editingInput.closest("td");
//					if(isOk(currentTd)){
//						currentTd.html(currentTd.data("text"));
//					}
				return false;
				/*				}).on("change","table>tbody>tr>td[data-col-index]>.jbt_edit_ele>input,table>tbody>tr>td[data-col-index]>.jbt_edit_ele>textarea",function(e){
					//输入中的时候 同步text数据
					e.preventDefault();
					e.stopPropagation();
					editingInputBox=$(this);
					currentTd=editingInputBox.closest("td");
					currentTd.data("editing",true);
					currentTd.data("text",editingInputBox.val());
					return false;
*/
			}).on("click","table[data-editable='true']>tbody>tr>td[data-col-index]>.jbt_edit_ele>.jbt_editor",function(e){
				//点击编辑器组件的时候 不能操作其他
				e.stopPropagation();
			}).on("click","table[data-editable='true']>tbody>tr>td[data-col-index]>.jbt_edit_ele",function(e){
				//点击编辑器组件的时候 不能操作其他
				e.preventDefault();
				e.stopPropagation();
				return false;
			});
			//.on("fillMinCountRows",function(e){
			//先行处理好 补全缺失的rows
			//	that.processEditableTableFillMinCountRows(table);
			//});

//				that.processEditableKeyEvent(table,trigger);
			//处理绑定外部elements 触发重新计算
			that.processEditableReSummaryTrigger(table);

			that.processEditableColConfigAndSortColIndex(table);

		},
		processColConfigInitByType:function(colConfig){
			if(typeof(colConfig.range)=="boolean" && colConfig.range){
				colConfig.range = "~";
			}
			switch (colConfig.type) {
				case "auto":
					colConfig.maxLength=colConfig.maxLength||40;
					colConfig.placeholder=colConfig.placeholder||"请输入";
					break;
				case "input":
					colConfig.maxLength=colConfig.maxLength||40;
					colConfig.placeholder=colConfig.placeholder||"请输入";
					break;
				case "textarea":
					colConfig.maxLength=colConfig.maxLength||200;
					colConfig.placeholder=colConfig.placeholder||"请输入";
					colConfig.height = colConfig.height||100;
					break;
				case "input_color":
					colConfig.maxLength=colConfig.maxLength||40;
					colConfig.placeholder=colConfig.placeholder||"颜色值";
					break;
				case "money":
					colConfig.type="input_number";
					colConfig.maxLength=colConfig.maxLength||11;
					colConfig.min=colConfig.min||0;
					colConfig.rule=colConfig.rule||'pznumber';
					colConfig.placeholder=colConfig.placeholder||"价格";
					break;
				case "weight":
					colConfig.type="input_number";
					colConfig.maxLength=colConfig.maxLength||11;
					colConfig.min=colConfig.min||0;
					colConfig.rule=colConfig.rule||'pznumber';
					colConfig.placeholder=colConfig.placeholder||"重量";
					break;
				case "price":
					colConfig.type="input_number";
					colConfig.maxLength=colConfig.maxLength||11;
					colConfig.min=colConfig.min||0;
					colConfig.rule=colConfig.rule||'pznumber';
					colConfig.placeholder=colConfig.placeholder||"单价";
					break;
				case "amount":
					colConfig.type="input_number";
					colConfig.maxLength=colConfig.maxLength||11;
					colConfig.min=colConfig.min||0;
					colConfig.rule=colConfig.rule||'pznumber';
					colConfig.placeholder=colConfig.placeholder||"数量";
					break;
				case "age":
					colConfig.type="input_number";
					colConfig.maxLength=colConfig.maxLength||3;
					colConfig.min=colConfig.min||0;
					colConfig.max=colConfig.max||100;
					colConfig.rule=colConfig.rule||'pint';
					colConfig.placeholder=colConfig.placeholder||"年龄";
					break;
				case "input_number":
					colConfig.maxLength=colConfig.maxLength||11;
					colConfig.rule=colConfig.rule||'pznumber';
					colConfig.placeholder=colConfig.placeholder||"请输入";
					break;
				case "input_ranger":
					colConfig.maxLength=colConfig.maxLength||11;
					colConfig.rule=colConfig.rule||'pznumber';
					colConfig.placeholder=colConfig.placeholder||"请选择";
					break;
				case "date":
					colConfig.pattern=colConfig.pattern||"yyyy-MM-dd";
					if(colConfig.range){
						colConfig.maxLength=colConfig.maxLength||(colConfig.pattern.length*2+(typeof(colConfig.range)=="boolean"?3:(colConfig.range.length+2)));
						colConfig.placeholder=colConfig.placeholder||"日期范围";
					}else{
						colConfig.maxLength=colConfig.maxLength||colConfig.pattern.length;
						colConfig.placeholder=colConfig.placeholder||"日期";
					}
					break;
				case "datetime":
					colConfig.pattern=colConfig.pattern||"yyyy-MM-dd HH:mm";
					if(colConfig.range){
						colConfig.maxLength=colConfig.maxLength||(colConfig.pattern.length*2+(typeof(colConfig.range)=="boolean"?3:(colConfig.range.length+2)));
						colConfig.placeholder=colConfig.placeholder||"日期时间范围";
					}else{
						colConfig.maxLength=colConfig.maxLength||colConfig.pattern.length;
						colConfig.placeholder=colConfig.placeholder||"日期时间";
					}
					break;
				case "time":
					colConfig.pattern=colConfig.pattern||"HH:mm";
					if(colConfig.range){
						colConfig.maxLength=colConfig.maxLength||(colConfig.pattern.length*2+(typeof(colConfig.range)=="boolean"?3:(colConfig.range.length+2)));
						colConfig.placeholder=colConfig.placeholder||"时间范围";
					}else{
						colConfig.maxLength=colConfig.maxLength||colConfig.pattern.length;
						colConfig.placeholder=colConfig.placeholder||"时间";
					}
					break;
				case "year":
					colConfig.pattern=colConfig.pattern||"yyyy";
					if(colConfig.range){
						colConfig.maxLength=colConfig.maxLength||(colConfig.pattern.length*2+(typeof(colConfig.range)=="boolean"?3:(colConfig.range.length+2)));
						colConfig.placeholder=colConfig.placeholder||"年范围";
					}else{
						colConfig.maxLength=colConfig.maxLength||colConfig.pattern.length;
						colConfig.placeholder=colConfig.placeholder||"年";
					}
					break;
				case "month":
					colConfig.pattern=colConfig.pattern||"yyyy-MM";
					if(colConfig.range){
						colConfig.maxLength=colConfig.maxLength||(colConfig.pattern.length*2+(typeof(colConfig.range)=="boolean"?3:(colConfig.range.length+2)));
						colConfig.placeholder=colConfig.placeholder||"年-月范围";
					}else{
						colConfig.maxLength=colConfig.maxLength||colConfig.pattern.length;
						colConfig.placeholder=colConfig.placeholder||"年-月";
					}
					break;
				case "week":
					colConfig.maxLength=colConfig.maxLength||10;
					colConfig.placeholder=colConfig.placeholder||"年-周";
					break;
				case "autocomplete":
					colConfig.delimiter=colConfig.delimiter||'_';
					colConfig.maxLength=colConfig.maxLength||40;
					colConfig.placeholder=colConfig.placeholder||"请选择";
					break;
				case "select":
					if(!colConfig.theme){
						colConfig.theme="select";
					}
					break;
				case "select2":
					colConfig.type="select";
					colConfig.theme="select2";
					break;
				case "jboltinput":
					colConfig.maxLength=colConfig.maxLength||20;
					colConfig.loadType=colConfig.loadType||(colConfig.url?"ajaxportal":"html");
					colConfig.placeholder=colConfig.placeholder||"请选择";
					break;
				default:
					break;
			}
		},
		processEditorColConfigInitByType:function(colConfig){
			switch (colConfig.type) {
				case "input":
					if(colConfig.dialog){
						colConfig.jbe_id="ipt_"+randomId();
						colConfig.jbe_link_ele_id=colConfig.jbe_id;
					}
					break;
				case "dialogbtn":
					if(colConfig.dialog){
						colConfig.jbe_hidden_id="ipt_"+randomId();
						colConfig.jbe_link_ele_id=colConfig.jbe_hidden_id;
					}
					break;
				case "autocomplete":
					colConfig.jbe_acpl_hiddenId="ac_"+randomId();
					if(colConfig.dialog){
						colConfig.jbe_link_ele_id=colConfig.jbe_acpl_hiddenId;
					}
					break;
				case "jboltinput":
					colConfig.jbe_jbi_hiddenId="jbih_"+randomId();
					if(colConfig.dialog){
						colConfig.jbe_link_ele_id=colConfig.jbe_jbi_hiddenId;
					}
					if(colConfig.tdValue&&colConfig.url&&colConfig.loadType=="jstree"){
						var newUrl=colConfig.url;
						var lastChar=newUrl.charAt(newUrl.length-1);
						if(newUrl.indexOf("?")!=-1){
							if(lastChar==='?'||lastChar==='&'){//问号结尾的URL
								newUrl=newUrl+"selectId="+colConfig.tdValue;
							}else if(lastChar==='/'){//有问号 但是/结尾的URL
								newUrl=newUrl.substring(0,newUrl.length-1)+"&selectId="+colConfig.tdValue;
							}else{
								newUrl=newUrl+"&selectId="+colConfig.tdValue;
							}
						}else if(newUrl.indexOf("-")!=-1){
							if(lastChar==='-'){
								newUrl=newUrl+colConfig.tdValue;
							}else{
								newUrl=newUrl+"-"+colConfig.tdValue;
							}
						}else{
							newUrl=newUrl+"/"+colConfig.tdValue;
						}
						colConfig.new_url=newUrl;
					}else{
						colConfig.new_url=colConfig.url;
					}
					break;
				default:
					break;
			}
		},
		processEditableColConfigAndSortColIndex:function(table){
			if(table.columnIndexMap){
				var cols=table.editableOptions.cols;
				var keys=Object.keys(table.columnIndexMap);
				var arr=new Array();
				var empKey,colConfig;
				for(var i in keys){
					empKey=keys[i];
					colConfig=cols[empKey];
					if(colConfig){
						if(colConfig.editable){
							arr.push(table.columnIndexMap[empKey]);
						}

						if(!colConfig.submitAttr){
							if(empKey.indexOf("_")!=-1){
								colConfig.submitAttr=StrUtil.camel(empKey);
							}else{
								colConfig.submitAttr=empKey;
							}
						}

						this.processColConfigInitByType(colConfig);



					}
				}
				table.tabEventSortColIndexs=arr;
			}


		},
		//处理绑定外部elements 触发重新计算
		processEditableReSummaryTrigger:function(table){
			if(!table.editableOptions.summaryTrigger){
				return;
			}
			var that=this;
			$(table.editableOptions.summaryTrigger).unbind("input").bind("input",function(){
				that.reProcessEditableAllSummarys(table);
			});
		},
		processEditableKeyEvent:function(table,trigger){
			if(!table.editable){return false;}
			var that=this;
			jboltBody.on("click",function(e){
				var ee=$(e.target);
				var inview=ee.closest(".jbolt_table_view");
				if(!isOk(inview)){
					table.isEditableLock=false;
					that.processEditingTds(table,true);
					that.clearTdFocus(table);
				}
			});
			jboltBody.on("keydown",function(e){
				if(e.ctrlKey&&e.altKey&&e.which==13){
					//如果按键是ctrl+alert+回车键 执行跳转当前选中
					var td=table.tbody.focusTd;
					if(isOk(td)){
						table.isEditableLock=true;
						e.preventDefault();
						e.stopPropagation();
						table.isEditableLock=true;
						td.trigger(trigger);
						return false;
					}
				}
				//如果不按alt键 一般是tab键 回车键 上下左右方向键和ctrl+回车键
				if(table.isEditableLock&&!e.altKey){
					var ele=$(e.target);
					var tagName=e.target.tagName;
					if(e.which==37){
						//可编辑td里组件上 回车或者tab 自动处理数据回填
						if(ele.hasClass("jbt_editor")){
							var td=ele.closest("td");
							that.changeTdFocus(table,td);
							if(e.ctrlKey){
								e.preventDefault();
								e.stopPropagation();
								var prevTd=table.tbody.focusTd.prevTd;
								if(prevTd){
									that.changeTdFocus(table,prevTd);
									prevTd.trigger(trigger);
								}
							}
						}else{
							e.preventDefault();
							e.stopPropagation();
							var td=table.tbody.focusTd;
							if(!isOk(td)){
								that.changeTdFocus(table);
								td=table.tbody.focusTd;
								td.trigger(trigger);
							}else{
								var prevTd=table.tbody.focusTd.prevTd;
								if(prevTd){
									that.changeTdFocus(table,prevTd);
									prevTd.trigger(trigger);
								}
							}
						}



					}else if(e.which==39){

						//可编辑td里组件上 回车或者tab 自动处理数据回填
						if(ele.hasClass("jbt_editor")){
							var td=ele.closest("td");
							that.changeTdFocus(table,td);
							if(e.ctrlKey){
								e.preventDefault();
								e.stopPropagation();
								var nextTd=table.tbody.focusTd.nextTd;
								if(nextTd){
									that.changeTdFocus(table,nextTd);
									//处理是否需要跳转焦点到右侧
									var success=that.processFocusChangeToExtraForm(table,td);
									if(success){
										table.isEditableLock=false;
										return false;
									}
									nextTd.trigger(trigger);
								}
							}
						}else{
							e.preventDefault();
							e.stopPropagation();
							var td=table.tbody.focusTd;
							if(!isOk(td)){
								that.changeTdFocus(table);
								td=table.tbody.focusTd;
								td.trigger(trigger);
							}else{
								var nextTd=table.tbody.focusTd.nextTd;
								if(nextTd){
									that.changeTdFocus(table,nextTd);
									nextTd.trigger(trigger);
								}
							}
						}

					}else if(e.which==38){
						//可编辑td里组件上
						if(ele.hasClass("jbt_editor")){
							var td=ele.closest("td");
							that.changeTdFocus(table,td);
							if(e.ctrlKey){
								e.preventDefault();
								e.stopPropagation();
								var prevTr=td.parent().prev();
								if(isOk(prevTr)){
									var prevTd=prevTr.find("td[data-col-index='"+td.data("col-index")+"']");
									if(prevTd){
										that.changeTdFocus(table,prevTd);
										prevTd.trigger(trigger);
									}
								}
							}

						}else{
							e.preventDefault();
							e.stopPropagation();
							var td=table.tbody.focusTd;
							if(!isOk(td)){
								that.changeTdFocus(table);
								td=table.tbody.focusTd;
								td.trigger(trigger);
							}else{
								var prevTr=td.parent().prev();
								if(isOk(prevTr)){
									var prevTd=prevTr.find("td[data-col-index='"+td.data("col-index")+"']");
									if(prevTd){
										that.changeTdFocus(table,prevTd);
										prevTd.trigger(trigger);
									}
								}
							}
						}


					}else if(e.which==40){
						//可编辑td里组件上
						if(ele.hasClass("jbt_editor")){
							var td=ele.closest("td");
							that.changeTdFocus(table,td);
							if(e.ctrlKey){
								e.preventDefault();
								e.stopPropagation();
								var nextTr=td.parent().next();
								if(isOk(nextTr)){
									var nextTd=nextTr.find("td[data-col-index='"+td.data("col-index")+"']");
									if(nextTd){
										that.changeTdFocus(table,nextTd);
										nextTd.trigger(trigger);
									}
								}
							}

						}else{
							e.preventDefault();
							e.stopPropagation();
							var td=table.tbody.focusTd;
							if(!isOk(td)){
								that.changeTdFocus(table);
								td=table.tbody.focusTd;
								td.trigger(trigger);
							}else{
								var nextTr=td.parent().next();
								if(isOk(nextTr)){
									var nextTd=nextTr.find("td[data-col-index='"+td.data("col-index")+"']");
									if(nextTd){
										that.changeTdFocus(table,nextTd);
										nextTd.trigger(trigger);
									}
								}
							}
						}
					}else if(e.which==13){
						e.preventDefault();
						e.stopPropagation();

						//可编辑td里组件上 回车或者tab 自动处理数据回填
						if(ele.hasClass("jbt_editor")){
							var td=ele.closest("td");
							that.changeTdFocus(table,td);
							var nextTd=table.tbody.focusTd.nextTd;
							if(nextTd){
								that.changeTdFocus(table,nextTd);
								//处理是否需要跳转焦点到右侧
								var success=that.processFocusChangeToExtraForm(table,td);
								if(success){
									table.isEditableLock=false;
									return false;
								}
								nextTd.trigger(trigger);
							}else{
								//处理是否需要跳转焦点到右侧
								var success=that.processFocusChangeToExtraForm(table,td);
								if(success){
									table.isEditableLock=false;
									that.processCreateNewEmptyTrAndFocusFirstEditableTd(table,td.parent());
									return false;
								}
								that.processCreateNewEmptyTrAndFocusFirstEditableTd(table,td.parent(),trigger);
							}
						}else{
							var td=table.tbody.focusTd;
							if(!isOk(td)){
								that.changeTdFocus(table);
								td=table.tbody.focusTd;
								td.trigger(trigger);
							}else{
								var nextTd=td.nextTd;
								if(nextTd){
									that.changeTdFocus(table,nextTd);
									nextTd.trigger(trigger);
								}else{
									that.processCreateNewEmptyTrAndFocusFirstEditableTd(table,td.parent(),trigger);
								}
							}
						}
					}else if(e.which==9){
						e.preventDefault();
						e.stopPropagation();

						//可编辑td里组件上 回车或者tab 自动处理数据回填
						if(ele.hasClass("jbt_editor")){
							var td=ele.closest("td");
							that.changeTdFocus(table,td);
							var nextTd=table.tbody.focusTd.nextTd;
							if(nextTd){
								that.changeTdFocus(table,nextTd);
								//处理是否需要跳转焦点到右侧
								var success=that.processFocusChangeToExtraForm(table,td);
								if(success){
									table.isEditableLock=false;
									return false;
								}
								nextTd.trigger(trigger);
							}else{
								//处理是否需要跳转焦点到右侧
								var success=that.processFocusChangeToExtraForm(table,td);
								if(success){
									table.isEditableLock=false;
									that.processCreateNewEmptyTrAndFocusFirstEditableTd(table,td.parent());
									return false;
								}
								that.processCreateNewEmptyTrAndFocusFirstEditableTd(table,td.parent(),trigger);
							}
						}else{
							var td=table.tbody.focusTd;
							if(!isOk(td)){
								that.changeTdFocus(table);
								td=table.tbody.focusTd;
								td.trigger(trigger);
							}else{
								var nextTd=table.tbody.focusTd.nextTd;
								if(nextTd){
									that.changeTdFocus(table,nextTd);
									nextTd.trigger(trigger);
								}else{
									that.processCreateNewEmptyTrAndFocusFirstEditableTd(table,td.parent(),trigger);
								}
							}
						}

					}
				}else if(e.ctrlKey&&e.shiftKey&e.which==13){
					table.isEditableLock=false;
					that.clearTdFocus(table);
				}else{
					//按下alt
					if(!e.ctrlKey&&!e.shiftKey&&e.altKey&&e.which==13){
						table.isEditableLock=true;
						that.processEditingTds(table);
						if(table.isEditableLock){
							var td=table.tbody.focusTd;
							if(!isOk(td)){
								that.changeTdFocus(table);
								td=table.tbody.focusTd;
								td.trigger(trigger);
							}else{
								that.changeTdFocus(table,td);
								td.trigger(trigger);
							}
						}else{
							that.clearTdFocus(table);
						}
					}
				}




			});
		},
		processCreateNewEmptyTrAndFocusFirstEditableTd:function(table,tr,trigger){
			var autoCreateEmptyTr=table.data("auto-create-empty-tr");
			if(typeof(autoCreateEmptyTr)=="undefined"){
				autoCreateEmptyTr=true;
			}
			if(typeof(table.editableOptions.autoCreateEmptyTr)=="boolean"){
				autoCreateEmptyTr=table.editableOptions.autoCreateEmptyTr;
			}
			if(autoCreateEmptyTr){
				//跑到最后一行最后一个可编辑单元格 需要创建下一行并且跳转到第一个去
				var tempTr=this.insertEmptyRow(table,tr);
				if(tempTr){
					var firstColIndex=0;
					if(isOk(table.tabEventSortColIndexs)){
						firstColIndex=table.tabEventSortColIndexs[0];
					}
					var activeTd=tempTr.find("td[data-col-index='"+firstColIndex+"']");
					if(isOk(activeTd)){
						this.changeTdFocus(table,activeTd);
						if(trigger){
							activeTd.trigger(trigger);
						}
					}
				}
			}else{
				this.processEditingTds(table);
			}

		},
		clearTdFocus:function(table){
			//切换td的focus状态
			var hasFocusTd=table.table_box.find("table[data-editable='true']>tbody>tr>td[data-col-index][data-focus='true']");
			if(isOk(hasFocusTd)){
				hasFocusTd.data("focus",false).removeAttr("data-focus");
			}
		},
		changeTdFocus:function(table,td){
			if(table.isEmpty){return false;}
			this.clearTdFocus(table);
			table.data("editable-focus",false).attr("data-editable-focus",false);
			table.isEditableLock=false;
			if(!isOk(td)){
				if(isOk(table.tabEventSortColIndexs)){
					var colIndex=table.tabEventSortColIndexs[0];
					td=table.tbody.find("tr>td[data-col-index='"+colIndex+"']:eq(0)");
				}else{
					td=table.tbody.find("tr>td[data-col-index]:eq(0)");
				}
			}
			td.attr("data-focus",true).data("focus",true);
			var incolIndexArr=false,next,prev,firstColIndex=-1,nextIndex=-1,prevIndex=-1,tr=td.parent(),nextTr,prevTr,lastColIndex=-1;
			var currentTdIndex=td.data("col-index");

			if(isOk(table.tabEventSortColIndexs)){
				var size=table.tabEventSortColIndexs.length
				lastColIndex=table.tabEventSortColIndexs[size-1];
				firstColIndex=table.tabEventSortColIndexs[0];
				prevIndex=-1;
				nextIndex=-1;
				incolIndexArr=false;
				//td.needCreateNextTr=false;
				for(var i=0;i<size;i++){
					if(table.tabEventSortColIndexs[i]==currentTdIndex){
						incolIndexArr=true;
						//如果不是一行最后一个可编辑 就找本行下一个
						if(i<size-1){
							nextIndex=table.tabEventSortColIndexs[i+1];
							next=tr.find("td[data-col-index='"+nextIndex+"']");
						}else if(i==size-1){
							nextIndex=firstColIndex;
							nextTr=tr.next();
							if(isOk(nextTr)){
								next=nextTr.find("td[data-col-index='"+nextIndex+"']");
							}else{
								//td.needCreateNextTr=autoCreateEmptyTr;
							}
						}



						if(i>0){
							prevIndex=table.tabEventSortColIndexs[i-1];
							prev=tr.find("td[data-col-index='"+prevIndex+"']");
						}else if(i==0){
							prevIndex=lastColIndex;
							prevTr=tr.prev();
							if(isOk(prevTr)){
								prev=prevTr.find("td[data-col-index='"+prevIndex+"']");
							}
						}
						break;
					}
				}
				if(!incolIndexArr){
					//如果focusTd 并不在可用colIndex中 就往后找第一个
					var trFirstTd=tr.find("td[data-col-index]:first");
					var trLastTd=tr.find("td[data-col-index]:last");
					var trFirstTdColIndex=trFirstTd.data("col-index");
					var trLastTdColIndex=trLastTd.data("col-index");
					//先找next
					if(currentTdIndex==trLastTdColIndex){
						//说明自己就是最后一个td了
						nextIndex=firstColIndex;
						nextTr=tr.next();
						if(isOk(nextTr)){
							next=nextTr.find("td[data-col-index='"+nextIndex+"']");
						}else{
							//td.needCreateNextTr=autoCreateEmptyTr;
						}
						//上一个找找 如果这一行最后一个不是可编辑的最后一个 那么上一个就是最后一个可编辑
						if(trLastTdColIndex!=table.tabEventSortColIndexs[size-1]){
							prevIndex=table.tabEventSortColIndexs[i-1];
						}else{
							prevIndex=table.tabEventSortColIndexs[size-2];
						}
						prev=tr.find("td[data-col-index='"+prevIndex+"']");
					}else if(currentTdIndex==trFirstTdColIndex){
						//说明自己就是第一个td了 那就找第一个可编辑表格就好了
						nextIndex=firstColIndex;
						next=tr.find("td[data-col-index='"+nextIndex+"']");

						prevIndex=lastColIndex;
						prevTr=tr.prev();
						if(isOk(prevTr)){
							prev=prevTr.find("td[data-col-index='"+prevIndex+"']");
						}
					}else{
						var nextSortColIndex=-1;
						var findSuccess=false;
						for(nextIndex=currentTdIndex;nextIndex<=trLastTdColIndex;nextIndex++){
							for(var i=0;i<size;i++){
								if(table.tabEventSortColIndexs[i]==nextIndex){
									nextSortColIndex=i;
									findSuccess=true;
									break;
								}
							}
							if(findSuccess){
								break;
							}

						}
						if(findSuccess){
							//找到了合适的 就他了
							next=tr.find("td[data-col-index='"+nextIndex+"']");
							//那么他的前一个就是
						}else{
							nextIndex=firstColIndex;
							nextTr=tr.next();
							if(isOk(nextTr)){
								next=nextTr.find("td[data-col-index='"+nextIndex+"']");
							}else{
								//td.needCreateNextTr=autoCreateEmptyTr;
							}
						}

						if(nextSortColIndex==0){
							prevIndex=lastColIndex;
							prevTr=tr.prev();
							if(isOk(prevTr)){
								prev=prevTr.find("td[data-col-index='"+prevIndex+"']");
							}
						}else if(nextSortColIndex==-1){
							//没找到任何
							prevIndex=table.tabEventSortColIndexs[size-1];
							prev=tr.find("td[data-col-index='"+prevIndex+"']");

						}else{
							prevIndex=table.tabEventSortColIndexs[nextSortColIndex-1];
							prev=tr.find("td[data-col-index='"+prevIndex+"']");
						}


					}
				}
			}else{
				prev=td.prev();
				if(!isOk(prev)){
					tr=tr.prev();
					if(isOk(tr)){
						prev=tr.find("td[data-col-index]:last");
					}
				}

				next=td.next();
				if(!isOk(next)){
					tr=tr.next();
					if(isOk(tr)){
						next=tr.find("td[data-col-index]:first");
					}
				}
			}
			table.tbody.focusTd=td;
			if(isOk(prev)){
				table.tbody.focusTd.prevTd=prev;
			}
			if(isOk(next)){
				table.tbody.focusTd.nextTd=next;
			}
			this.clearAllEditableFocus(table);
		},
		clearAllEditableFocus:function(table){
			var pbox,that=this;
			if(jboltWithTabs){
				pbox=JBoltTabUtil.getCurrentTabContent();
			}else{
				pbox=jboltBody;
			}
			var tables=pbox.find("table[data-jbolttable][data-ajax='true'][data-editable='true'][data-editable-focus='true'].jbolt_main_table:not([data-shortcutkey-disabled='true'])");
			if(isOk(tables)){
				var jbt;
				tables.each(function(){
					jbt=$(this).jboltTable("inst");
					if(jbt){
						that.clearTdFocus(jbt);
						jbt.data("editable-focus",false).attr("data-editable-focus",false);
						jbt.isEditableLock=false;
						that.processEditingTds(jbt,false);
					}
				});
			}

			if(!table.isEditableLock){
				table.isEditableLock=true;
			}
			table.data("editable-focus",true).attr("data-editable-focus",true);
		},
		//处理 checkbox操作可编辑td  switchBtn同样可用
		processCheckboxTd:function(table,editingTd,checkbox,result){
			return this.processSwitchBtnTd(table,editingTd,checkbox,result);
		},
		//处理switchBtn操作可编辑td  checkbox同样可用
		processSwitchBtnTd:function(table,editingTd,btn,result){
			//单元格编辑完成后执行数据变更
			var success=this.processChangeTableListDatasColumnValue(table,editingTd,result);
			if(!success){
				return false;
			}
			var that=this;
			var orignText=editingTd.data("origin-text");
			if(typeof(orignText)=='undefined'&&!orignText&&!editingTd[0].hasAttribute("data-origin-text")){
				orignText=!result;
				editingTd.attr("data-origin-text",orignText).data("origin-text",orignText);
			}
			editingTd.data("text",result).attr("data-text",result);
			editingTd.data("value",result).attr("data-value",result);
			if(result.toString()===orignText.toString()){
				editingTd.data("changed",false).removeAttr("data-changed");
			}else{
				editingTd.data("changed",true).attr("data-changed",true);
			}
			var colConfig,column=editingTd.data("column");
			if(!column){
				var colIndex=editingTd.data("col-index");
				var theadTh=table.thead.find("tr>th[data-col-index='"+colIndex+"']");
				if(isOk(theadTh)){
					var column=theadTh.data("column");
					if(column){
						editingTd.data("column",column).attr("data-column",column);
						if(table.editableOptions){
							//拿到配置的列数据json
							var cols=table.editableOptions.cols;
							if(cols){
								var submitAttr=column;
								colConfig=cols[column];
								if(colConfig){
									if(colConfig.submitAttr){
										submitAttr=colConfig.submitAttr;
									}
									if(colConfig.editable){
										if(typeof(colConfig.required)=="undefined"||colConfig.required=='undefined'){
											colConfig.required=false;
										}
										editingTd.attr("data-required",colConfig.required).data("required",colConfig.required);
									}
								}
								editingTd.attr("data-submitattr",submitAttr).data("submitattr",submitAttr);

							}
						}
					}
				}
			}else{
				if(table.editableOptions){
					var cols=table.editableOptions.cols;
					if(cols){
						colConfig=cols[column];
					}
				}
			}
			//触发其他指定列summary
			this.processTriggerOtherColumnSummary(table,editingTd);
			//改变自己的同时 修改同行
			this.processEditableTrChangedStatusByTd(table,editingTd);
			this.checkAndProcessOneColumnUpdate(table,editingTd);


			if(colConfig&&(colConfig.type=="checkbox"||colConfig.type=="switchbtn")&&typeof(colConfig.handler)=="function"){
				var jsonData=table.tableListDatas[editingTd.closest("tr").data("index")];
				this.processColConfigChangeColumns(table,editingTd,jsonData);
				colConfig.handler(table,editingTd,jsonData,btn,result);
			}
			return true;
		},
		processTriggerOtherColumnSummary2:function(table,tr,column,triggerSummaryColumns){
			var that = this;
			if(isOk(triggerSummaryColumns)){
				var sumaryTdIndex=-1,sumaryTd;
				$.each(triggerSummaryColumns,function(i,col){
					sumaryTdIndex=table.columnIndexMap[col];
					if(sumaryTdIndex!=-1){
						sumaryTd=tr.find("td[data-col-index='"+sumaryTdIndex+"']");
						if(isOk(sumaryTd)){
							sumaryTd.triggerSummaryByOther=true;
							try{
								that.processTbodyHSummaryTd(table,sumaryTd);
							}catch(e){
								sumaryTd.triggerSummaryByOther=false;
							}
							sumaryTd.triggerSummaryByOther=false;
							that.processTfootSummary(table,sumaryTdIndex);

						}
					}
				});
			}
		},
		processTriggerOtherColumnSummary:function(table,editingTd){
			var column=editingTd.data("column");
			if(!column){
				return;
			}
			var colConfig=table.editableOptions.cols[column];
			if(colConfig&&isOk(colConfig.triggerSummaryColumns)){
				var tr=editingTd.parent();
				this.processTriggerOtherColumnSummary2(table,tr,column,colConfig.triggerSummaryColumns);
			}
		},
		//一点tr被changed 就执行把其他td都设置column required属性
		processTdsColumnAndSubmitInfoByTr:function(table,tr){
			if(!tr.data("changed")){return false;}
			var tds=tr.find("td[data-col-index]:not([data-submitattr])");
			if(!isOk(tds)){return true;}
			var that=this;
			var td;
			tds.each(function(){
				td=$(this);
				that.processTdColumnAndSubmitInfo(table,td);
			});

		},
		processTdColumnAndSubmitInfo:function(table,td){
			var colIndex=td.data("col-index");
			var theadTh=table.thead.find("tr>th[data-col-index='"+colIndex+"']");
			if(isOk(theadTh)){
				var column=theadTh.data("column");
				if(column){
					td.attr("data-column",column).data("column",column);
					if(table.editableOptions){
						//拿到配置的列数据json
						var cols=table.editableOptions.cols;
						if(cols){
							var submitAttr=column;
							var colConfig=cols[column];
							if(colConfig){
								if(colConfig.submitAttr){
									submitAttr=colConfig.submitAttr;
								}
								if(typeof(colConfig.required)=="undefined"||colConfig.required=='undefined'){
									colConfig.required=false;
								}
								td.attr("data-required",colConfig.required).data("required",colConfig.required);
							}
							td.attr("data-submitattr",submitAttr).data("submitattr",submitAttr);
						}
					}
					var swbtn=td.find("img[data-switchbtn]");
					if(isOk(swbtn)){
						var result=swbtn.data("value");
						td.data("text",result).attr("data-text",result);
						td.data("value",result).attr("data-value",result);
					}
				}
			}
		},
		maximize:function(table){
			table.table_view.toggleClass("maximize");
			jboltAdmin.toggleClass("jboltTableMaximize");
			return true;
		},
		setEditableOptions:function(table,options){
			var editable=table.data("editable");
			if(!editable){table.editable=false;return false;}
			table.submit_delete_ids=null;

			if(!options){LayerMsgBox.alert("可编辑表格setEditableOptions设置异常",2);return false;}
			if(!options.cols){LayerMsgBox.alert("可编辑表格中未设置cols",2);return false;}
			if(!options.trigger||(options.trigger!="click"&&options.trigger!="dblclick")){
				options.trigger="dblclick";
			}

			table.editable=true;
			//是否启用required校验的背景图效果
			var requiredImgbg=table.data("required-imgbg");
			if(typeof(requiredImgbg)=="undefined"){
				requiredImgbg=true;
			}
			table.requiredImgbg=requiredImgbg;
			//处理每个cols配置中的可编辑属性
			var optcolConfig;
			for(var i in options.cols){
				optcolConfig=options.cols[i];
				if(typeof(optcolConfig.editable)=="undefined"){
					optcolConfig.editable=isOk(optcolConfig.type);
				}
			}
			table.editableOptions=options;
			//辅助录入初始化事件
			if(table.editableOptions.extraColumnForm){
				table.extraColumnForm=getRealJqueryObject(table.editableOptions.extraColumnForm);
			}
			//初始化表格的summary信息 为自动统计计算做准备
			this.initEditableSummarys(table,table.editableOptions.cols);
			//初始化列对应表头 required样式 普通表格初始化 ajax的单独地方
			this.initTableEditableRequiredStyle(table,table.editableOptions.cols);
			this.processEditableReSummaryTrigger(table);
			this.processEditableColConfigAndSortColIndex(table);
			return true;
		},
		changeVisibleColumnsByColIndex:function(table,colIndex){
			var configs = table.visibleColumnsConfig;
			//通过指定的colIndex设置显示隐藏
			if(!isOk(configs)){
				processJboltTableVisibleColumnsByThead(table);
				configs = table.visibleColumnsConfig;
			}
			if(isOk(configs)){
				var assignColIndex = isOk(colIndex);
				var showSize=0;
				for(var i in configs){
					table.table_box.find("thead>tr>th[data-col-index='"+configs[i].index+"']").addClass("d-none");
					table.table_box.find("tbody>tr>td[data-col-index='"+configs[i].index+"']").addClass("d-none");
					table.table_box.find("tfoot>tr>th[data-col-index='"+configs[i].index+"']").addClass("d-none");
					if(assignColIndex){
						configs[i].hidden=true;
						for(var k in colIndex){
							if(configs[i].index == colIndex[k]){
								configs[i].hidden=false;
							}
						}
					}
				}

				for(var i in configs){
					if(configs[i].hidden==false){
						table.table_box.find("thead>tr>th[data-col-index='"+configs[i].index+"']").removeClass("d-none");
						table.table_box.find("tbody>tr>td[data-col-index='"+configs[i].index+"']").removeClass("d-none");
						table.table_box.find("tfoot>tr>th[data-col-index='"+configs[i].index+"']").removeClass("d-none");
						showSize++;
					}
				}
				var allSize=configs.length;
				var oldWidth = table.data("width");
				var originWidth = table.data("origin-width")||oldWidth;
				if(oldWidth == "auto"){
					if(originWidth == "auto"){
						table.table_box.find("table").data("width","auto").attr("data-width","auto").data("origin-width",originWidth).attr("data-origin-width",originWidth);
					}else if(originWidth == "fill"){
						if(showSize < allSize){
							table.table_box.find("table").data("width","auto").attr("data-width","auto").data("origin-width",originWidth).attr("data-origin-width",originWidth);
						}else{
							table.table_box.find("table").data("width","fill").attr("data-width","fill").data("origin-width",originWidth).attr("data-origin-width",originWidth);
						}
					}else{
						table.table_box.find("table").data("width", "auto").attr("data-width", "auto");
					}
				}else if(oldWidth == "fill"){
					if(originWidth == "auto") {
						table.table_box.find("table").data("width", "auto").attr("data-width", "auto").data("origin-width", "auto").attr("data-origin-width", originWidth);
					}else if(originWidth == "fill"){
						if(showSize < allSize){
							table.table_box.find("table").data("width","auto").attr("data-width","auto").data("origin-width",originWidth).attr("data-origin-width",originWidth);
						}else{
							table.table_box.find("table").data("width","fill").attr("data-width","fill").data("origin-width",originWidth).attr("data-origin-width",originWidth);
						}
					}else{
						table.table_box.find("table").data("width", "auto").attr("data-width", "auto");
					}
				}else{
					table.table_box.find("table").data("width", "auto").attr("data-width", "auto");

				}
				//设置宽高数据
				this.processTableWidthAndHeight(table);
//					this.processCellWidthAndHeight(table);
				//处理左侧fixed
				this.processColumnFixedLeft(table);
				//处理右侧fixed
				this.processColumnFixedRight(table);
				//处理下方滚动条 横向
				this.refreshFixedColumnHScroll(table);
				//处理fixed的滚动位置
				this.reScrollFixedColumnBox(table);

			}
		},
		refresh:function(table,refreshEditableOptions){
			//刷新当前数据 第二个参数可以是boolean 控制是否重新加载options 也可以直接给一个新的options
			if(!table){
				var jboltTable=this.jboltTable("inst");
				if(refreshEditableOptions){
					if(typeof(refreshEditableOptions)=="boolean"){
						jboltTable.me.refreshEditableConfigOptions(jboltTable);
					}else{
						jboltTable.me.setEditableOptions(jboltTable,refreshEditableOptions);
					}
				}
				if(jboltTable.isAjax){
					jboltTable.me.readByPage(jboltTable);
				}else{
					jboltTable.me.tableSubmitForm(jboltTable);
				}
			}else{
				if(refreshEditableOptions){
					if(typeof(refreshEditableOptions)=="boolean"){
						this.refreshEditableConfigOptions(table);
					}else{
						this.setEditableOptions(table,refreshEditableOptions);
					}
				}
				if(table.isAjax){
					this.readByPage(table);
				}else{
					this.tableSubmitForm(table);
				}

			}
		},
		/**
		 * 根据需要 提交表格绑定查询表单 非ajax表格使用
		 */
		tableSubmitForm:function(table,pageNumber){
			resetJBolttableSlaveBox(table);
			var formId=table.data("conditions-form");
			var form;
			if(!formId){
				formId="jb_form_"+randomId();
				table.data("conditions-form",formId).attr("data-conditions-form",formId);
				form=$("<form class='form-inline d-none' id='"+formId+"' method='post'  data-pjaxsubmit></form>");
				jboltBody.append(form);
			}
			if(!isOk(form)){
				var pbox=null;
				var ajaxPortal=table.closest("[data-ajaxportal]");
				if(isOk(ajaxPortal)){
					pbox=ajaxPortal;
				}else{
					//					var withTabs=isWithtabs();
					if(jboltWithTabs){
						pbox=table.closest(".jbolt_tabcontent");
					}else{
						pbox=$(mainPjaxContainer);
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
					$.error("表格所在页面未规范使用布局");
					return false;
				}
				form=pbox.find("#"+formId);

			}
			if(isOk(form)){
				var pages=table.table_box.find(".pages");
				if(isOk(pages)){
					//如果有分页 就得带着分页
					if(!pageNumber){
						pageNumber=table.data("pagenumber");
						if(!pageNumber){
							pageNumber=1;
						}
					}

					var pageSize=pages.find("#pageSize").val();
					form.append('<input type="hidden" name="page" value="'+pageNumber+'"/>');
					form.append('<input type="hidden" name="pageSize" value="'+pageSize+'"/>');
				}

				var sortColumn=table.data("sort-column");
				var sortType=table.data("sort-type");
				if(sortColumn&&sortType){
					form.append('<input type="hidden" name="sortColumn" value="'+sortColumn+'"/>');
					form.append('<input type="hidden" name="sortType" value="'+sortType+'"/>');
				}
				form.submit();
			}
		},
		//跳转到第一页
		jboltTablePageToFirst:function(table){
			if(!table){
				var jboltTable=this.jboltTable("inst");
				if(jboltTable.isAjax){
					jboltTable.scrollToTop=true;
					jboltTable.resetCellWidthAfterAjax=false;
					jboltTable.me.readByPage(jboltTable,1);
				}else{
					jboltTable.me.tableSubmitForm(jboltTable,1);
				}
			}else{
				if(table.isAjax){
					table.scrollToTop=true;
					table.resetCellWidthAfterAjax=false;
					this.readByPage(table,1);
				}else{
					this.tableSubmitForm(table,1);
				}
			}
		},
		//跳转到指定页
		jboltTablePageTo:function(table,pageNumber){
			if(!pageNumber){pageNumber = 1;}
			if(!table){
				var jboltTable=this.jboltTable("inst");
				if(jboltTable.isAjax){
					jboltTable.scrollToTop=true;
					jboltTable.resetCellWidthAfterAjax=false;
					jboltTable.me.readByPage(jboltTable,pageNumber);
				}else{
					jboltTable.me.tableSubmitForm(jboltTable,pageNumber);
				}
			}else{
				if(table.isAjax){
					table.scrollToTop=true;
					table.resetCellWidthAfterAjax=false;
					this.readByPage(table,pageNumber);
				}else{
					this.tableSubmitForm(table,pageNumber);
				}
			}
		},
		//跳转到下一页
		jboltTablePageToNext:function(table,toFirstPageIfLast){
			if(!table){
				var jboltTable=this.jboltTable("inst");
				var totalpage = jboltTable.data("totalpage")||1;
				var pageNumber = jboltTable.data("pagenumber")||1;
				if(!toFirstPageIfLast && pageNumber>=totalpage){
					LayerMsgBox.alert("当前已经是最后一页",2);
					return;
				}
				if(toFirstPageIfLast && pageNumber>=totalpage){
					pageNumber = 1;
				}else{
					pageNumber = pageNumber+1;
				}

				if(jboltTable.isAjax){
					jboltTable.scrollToTop=true;
					jboltTable.resetCellWidthAfterAjax=false;
					jboltTable.me.readByPage(jboltTable,pageNumber);
				}else{
					jboltTable.me.tableSubmitForm(jboltTable,pageNumber);
				}
			}else{
				var totalpage = table.data("totalpage")||1;
				var pageNumber = table.data("pagenumber")||1;
				if(!toFirstPageIfLast && pageNumber>=totalpage){
					LayerMsgBox.alert("当前已经是最后一页",2);
					return;
				}
				if(toFirstPageIfLast && pageNumber>=totalpage){
					pageNumber = 1;
				}else{
					pageNumber = pageNumber+1;
				}
				if(table.isAjax){
					table.scrollToTop=true;
					table.resetCellWidthAfterAjax=false;
					this.readByPage(table,pageNumber);
				}else{
					this.tableSubmitForm(table,pageNumber);
				}
			}
		},
		//跳转到上一页
		jboltTablePageToPrev:function(table,toLastPageIfFirst){
			if(!table){
				var jboltTable=this.jboltTable("inst");
				var totalpage = jboltTable.data("totalpage")||1;
				var pageNumber = jboltTable.data("pagenumber")||1;
				if(!toLastPageIfFirst && pageNumber==1){
					LayerMsgBox.alert("当前已经是第一页",2);
					return;
				}
				if(toLastPageIfFirst && pageNumber==1&&totalpage>1){
					pageNumber = totalpage;
				}else{
					pageNumber = pageNumber-1;
				}
				if(jboltTable.isAjax){
					jboltTable.scrollToTop=true;
					jboltTable.resetCellWidthAfterAjax=false;
					jboltTable.me.readByPage(jboltTable,pageNumber);
				}else{
					jboltTable.me.tableSubmitForm(jboltTable,pageNumber);
				}
			}else{
				var totalpage = table.data("totalpage")||1;
				var pageNumber = table.data("pagenumber")||1;
				if(!toLastPageIfFirst && pageNumber==1){
					LayerMsgBox.alert("当前已经是第一页",2);
					return;
				}
				if(toLastPageIfFirst && pageNumber==1&&totalpage>1){
					pageNumber = totalpage;
				}else{
					pageNumber = pageNumber-1;
				}
				if(table.isAjax){
					table.scrollToTop=true;
					table.resetCellWidthAfterAjax=false;
					this.readByPage(table,pageNumber);
				}else{
					this.tableSubmitForm(table,pageNumber);
				}
			}
		},
		//跳转到最后页
		jboltTablePageToLast:function(table){
			if(!table){
				var jboltTable=this.jboltTable("inst");
				var totalPage=jboltTable.data("totalpage");

				if(!totalPage){totalPage=1;}
				jboltTable.data("tolastpage",true);
				if(jboltTable.isAjax){
					jboltTable.scrollToTop=true;
					jboltTable.resetCellWidthAfterAjax=false;
					jboltTable.me.readByPage(jboltTable,totalPage);
				}else{
					jboltTable.me.tableSubmitForm(jboltTable,totalPage);
				}
			}else{
				var totalPage=table.data("totalpage");

				if(!totalPage){totalPage=1;}
				table.data("tolastpage",true);
				if(table.isAjax){
					table.scrollToTop=true;
					table.resetCellWidthAfterAjax=false;
					this.readByPage(table,totalPage);
				}else{
					this.tableSubmitForm(table,totalPage);
				}
			}
		},
		exeBeforeAjaxHandler:function(table){
			if(table.beforeAjaxHandler){
				if(typeof(table.beforeAjaxHandler) == "function"){
					table.beforeAjaxHandler(table);
				}else{
					var tableBeforeAjaxExeFunc = eval(table.beforeAjaxHandler);
					if(tableBeforeAjaxExeFunc && typeof(tableBeforeAjaxExeFunc)=="function"){
						tableBeforeAjaxExeFunc(table);
					}
				}
			}
		},
		/**
		 * 按照分页读取
		 */
		readByPage:function(table,pageNumber,callback){
			this.closeMenu(table);
			if(table.jsonConditions){
				table.readByJsonConditions=true;
				table.resetCellWidthAfterAjax=false;
			}else{
				table.readByJsonConditions=false;
			}
			table.activeTrIndex = null;
			table.activeTrId    = null;
			//执行ajax前的处理
			this.exeBeforeAjaxHandler(table);
			resetJBolttableSlaveBox(table);
			var that=this;
			var jbolt_table_pages=table.table_box.find(".jbolt_table_pages");
			if(!pageNumber){
				var input=jbolt_table_pages.find("#gonu");
				if(isOk(input)){
					pageNumber=Math.abs(parseInt(input.val()));
				}else{
					var pageCurrent = jbolt_table_pages.find(".pagination a.current:not(.prev):not(.next)");
					if(isOk(pageCurrent)){
						pageNumber=parseInt(pageCurrent.text());
						if(isNaN(pageNumber)){
							pageNumber=1;
						}
					}else{
						pageNumber=1;
					}
				}
			}
			var oldPageSize = Math.abs(parseInt(jbolt_table_pages.find("#pageSize").val()));
			var pageSize=oldPageSize;
			if(table.readByJsonConditions && table.jsonConditions){
				pageSize = table.jsonConditions.pageSize;
				if(pageSize){
					delete table.jsonConditions["pageSize"];
				}else{
					pageSize = table.data("conditions-pagesize");
				}
			}
			if(!pageSize){
				pageSize = oldPageSize||15;
			}
			var totalPage=Math.abs(parseInt(jbolt_table_pages.find("#totalPage").text()));
			var oldPageNumber=table.data("pagenumber");
			var page=table.data("page");
			if(!page||!oldPageNumber){
				oldPageNumber=1;
			}
			table.data("pagenumber",pageNumber);
			table.data("pagesize",pageSize).attr("data-pagesize",pageSize);
			table.data("totalpage",totalPage);
			var leftScroll=table.table_body.scrollLeft();
			var topScroll=table.table_body.scrollTop();
			var sortColumn=table.data("sort-column");
			table.table_body.scroll_top=topScroll;
			table.table_body.scroll_left=leftScroll;
			that.ajaxLoadTableData(table,function(){
				table.table_body.needKeepScrollLeft=false;
				table.table_body.needKeepScrollTop=false;
				table.scrollToTop=false;
				table.table_body.sortRankAfter=false;
				if(table.table_body.scroll_left||table.table_body.scroll_top){
//						  if(oldPageNumber&&oldPageNumber==pageNumber){
//							  table.table_body.needKeepScrollLeft=true;
//						  }
					if(table.table_body.scroll_left){
						table.table_body.needKeepScrollLeft=true;
					}
					if(table.table_body.scroll_top&&oldPageNumber&&oldPageNumber==pageNumber&&!table.scrollToTop){
						table.table_body.needKeepScrollTop=true;
					}
					if(sortColumn){
						table.table_body.sortRankAfter=true;
					}
				}
				//处理可编辑表格 加载数据后处理项
				var editable=table.data("editable");
				if(editable){
					table.submit_delete_ids=null;
					//触发行补全
					//	table.trigger("fillMinCountRows");
					that.processEditableTableFillMinCountRows(table);
				}
				that.reProcessTableMain(table);
				that.initEditableCheckboxDataInfo(table);
				that.initEditableSwitchBtnDataInfo(table);
				that.changeVisibleColumnsByColIndex(table);
				if(callback){
					if(typeof(callback)=="function"){
						callback(table);
					}else if(typeof(callback)=="string"){
						var execall_handler=eval(callback);
						if(execall_handler&&typeof(execall_handler)=="function"){
							execall_handler(table);
						}
					}
				}
			});
		},
		processUnCheckTheadCheckbox:function(table){
			//处理uncheck checkbox 表格里的
			var theadCheckboxs=table.table_box.find("table>thead>tr>th[data-col-index]>.jbolt_table_checkbox>input[type='checkbox']");
			if(isOk(theadCheckboxs)){
				CheckboxUtil.uncheckIt(theadCheckboxs);
			}
		},
		//添加占位pagebox
		addJboltPageBox:function(table){
			var pageId=table.data("page");
			if(!pageId){return false;}
			var pageHtml='';
			var options=[];
			var pageSizeOptions=table.data("pagesize-options");
			if(pageSizeOptions){
				if(typeof pageSizeOptions == "number"){
					options=[pageSizeOptions];
				}else{
					if(pageSizeOptions.indexOf(",")!=-1){
						options=pageSizeOptions.split(",");
					}else{
						options=[5,10,15,20,30,40,50,100];
					}
				}

			}else{
				options=[5,10,15,20,30,40,50,100];
			}
			var pageSize=table.data("pagesize")||15;
			var isMini=table.data("page-mini");
			pageHtml=juicer(isMini?jboltTablePageTpl_mini:jboltTablePageTpl,{pageId:pageId,pageSize:pageSize,options:options});
			var jbolt_table_pages=$(pageHtml);
			table.table_box.append(jbolt_table_pages);
			return jbolt_table_pages;
		},
		//初始化分页组件
		initJboltTablePage:function(table,pageInfo){
			var pageId=table.data("page");
			if(!pageId){return false;}
			var isMini=table.data("page-mini");
			table.data("totalpage",pageInfo.totalPage);
			var jbolt_table_pages=table.table_box.find(".jbolt_table_pages");
			if(!isOk(jbolt_table_pages)){
				jbolt_table_pages=addJboltPageBox(table);
			}
			var pager=jbolt_table_pages.find("#"+pageId);
			jbolt_table_pages.find("#gonu").val(pageInfo.pageNumber).attr("max",pageInfo.totalPage).data("rule","pint;<="+pageInfo.totalPage).data("tips","请输入正整数 并且小于等于"+pageInfo.totalPage);
			jbolt_table_pages.find("#totalPage").text(pageInfo.totalPage);
			jbolt_table_pages.find("#totalRow").text(pageInfo.totalRow);
			jbolt_table_pages.find("#pageSize").val(pageInfo.pageSize);
			if(isMini){
				jbolt_table_pages.find("#gonu").hide();
				jbolt_table_pages.find("#totalPage").hide();
				jbolt_table_pages.find("#totalRow").hide();
				jbolt_table_pages.find("#pageSize").hide();
			}

			var that=this;

			loadJBoltPlugin(['pagination'], function(){
				pager.pagination(pageInfo.totalPage,{
					num_edge_entries:1,
					current_page:(pageInfo.pageNumber-1),
					callback:function(index,ct){
						if(isNaN(index)==false){
							var page=index+1;
							that.readByPage(table,page);
							return false;
						}
					}
				});
			});


			if(!table.data("page-ok")){
				jbolt_table_pages.find("#gonu").on("keydown",function(e){
					if(e.keyCode==109||e.keyCode==189){
						return false;
					}
				});
				jbolt_table_pages.find("#pageSize").on("change",function(){
					that.readByPage(table,1);
				});

				jbolt_table_pages.find(".page-btn").on("click",function(){
					var pageGonum = jbolt_table_pages.find("#gonu");
					if(isOk(pageGonum)){
						if(FormChecker.checkIt(pageGonum[0])){
							that.readByPage(table);
						}
					}else{
						that.readByPage(table);
					}

				});
			}
			table.page=jbolt_table_pages;
			jbolt_table_pages.attr("data-theme",table.theme).data("theme",table.theme);

			//设置page初始化成功标识
			table.data("page-ok",true);

		},
		processInsertDefaultColumnValues:function(table,datas){
			table.changeTrs=[];
			table.changeTrColumns={};
			if(!table.editable){return datas;}
			var insertDefaultValues=table.editableOptions.insertDefaultValues;
			if(insertDefaultValues){
				var newDatas=new Array();
				var size=datas.length;
				var insertEmptyData,data,tempData,col;
				var columns = Object.keys(insertDefaultValues);
				for(var i=0;i<size;i++){
					insertEmptyData={};
					data = datas[i];
					tempData = Object.assign(insertEmptyData,deepClone(insertDefaultValues),data);
					newDatas.push(tempData);
					for(var j in columns){
						col = columns[j];
						if(tempData[col]===insertDefaultValues[col] && tempData[col]!==data[col]){
							if(table.changeTrs.indexOf(i)==-1){
								table.changeTrs.push(i);
							}
							if(!table.changeTrColumns["tr_"+i]){
								table.changeTrColumns["tr_"+i]=new Array();
							}
							table.changeTrColumns["tr_"+i].push(col);
						}
					}
				}
				return newDatas;
			}else{
				return datas;
			}
		},
		/**
		 * 渲染之前处理数据
		 * @param table
		 */
		processTableListBeforeRender:function(table){
			if(notOk(table.tableListDatas)){
				return;
			}
			var ajaxDataHandler=table.data("ajax-success-data-handler");
			if(!ajaxDataHandler){
				return;
			}
			var exeHandler = eval(ajaxDataHandler);
			if(exeHandler&&typeof(exeHandler)=="function"){
				exeHandler(table,table.tableListDatas);
			}
		},
		/**
		 * 添加多条数据
		 */
		addRowDatas:function(table,data,formData){
			var appendEle=table.tbody;
			//处理table.rowTplContent
			var processSuccess = this.processTableRowTplContent(table);
			if(!processSuccess){
				return false;
			}
			var tplContent = table.rowtplContent;

			if(data){
				var appendHtml="";
				//如果带着extraData 说明不是标准的list或者page对象
				if(data.tableData&&data.extraData){
					var datas=data.tableData;
					var extraData=data.extraData;
					//如果直接传数据数据 就直接渲染
					if(isArray(datas)){
						table.tableListDatas=this.processInsertDefaultColumnValues(table,datas);
						this.processTableListBeforeRender(table);
						appendHtml = juicer(tplContent,{datas:table.tableListDatas,formData:formData,extraData:extraData})
						//appendEle.append();
					}else if(datas.pageSize&&datas.totalRow){
						table.tableListDatas=this.processInsertDefaultColumnValues(table,datas.list);
						this.processTableListBeforeRender(table);
						//说明是分页数据
						appendHtml = juicer(tplContent,{datas:table.tableListDatas,pageNumber:datas.pageNumber,pageSize:datas.pageSize,formData:formData,extraData:extraData});
						//appendEle.append();
					}
				}else{
					//如果直接传数据数据 就直接渲染
					if(isArray(data)){
						table.tableListDatas=this.processInsertDefaultColumnValues(table,data);
						this.processTableListBeforeRender(table);
						appendHtml = juicer(tplContent,{datas:table.tableListDatas,formData:formData});
						//appendEle.append();
					}else if(data.pageSize&&data.totalRow){
						table.tableListDatas=this.processInsertDefaultColumnValues(table,data.list);
						this.processTableListBeforeRender(table);
						//说明是分页数据
						appendHtml = juicer(tplContent,{datas:table.tableListDatas,pageNumber:data.pageNumber,pageSize:data.pageSize,formData:formData});
						//appendEle.append();
					}
				}
				if(appendHtml){
					//如果是树表
					if(table.isTreeTable){
						var appendHtmlObj = $("<tbody>"+appendHtml+"</tbody>");
						var trs = appendHtmlObj.find("tr");
						var len=trs.length;
						if(len>100){
							table.treeTableCloneTbody = appendHtmlObj;
							var tableOpt =  table.data("treetable");
							var trsSelector="";
							if(typeof(tableOpt) == "number"){
								if(tableOpt>1){
									for(var i=1;i<=tableOpt;i++){
										trsSelector+="tr[data-level='"+i+"']";
										if(i<tableOpt){
											trsSelector += ",";
										}
									}
									trs = table.treeTableCloneTbody.find(trsSelector);
									if(trs.length>100){
										tableOpt=1;
										trs = table.treeTableCloneTbody.find("tr[data-level='1']");
									}
								}else{
									trs = table.treeTableCloneTbody.find("tr[data-level='1']");
								}

							}else if(tableOpt.indexOf("all")!=-1){
								var newtableOpt=2;
								for(var i=1;i<=newtableOpt;i++){
									trsSelector+="tr[data-level='"+i+"']";
									if(i<newtableOpt){
										trsSelector += ",";
									}
								}
								trs = table.treeTableCloneTbody.find(trsSelector);
								if(trs.length>100){
									newtableOpt=1;
									trs = table.treeTableCloneTbody.find("tr[data-level='1']");
								}

								tableOpt = tableOpt.replaceAll("all",newtableOpt);
							}
							table.data("treetable",tableOpt).attr("data-treetable",tableOpt);
						}
						appendEle.append(trs);
					}else{
						appendEle.append(appendHtml);
					}
				}
			}else{
				table.tableListDatas=[];
			}
			this.processEmptyTableBody(table);
			var that=this;
			if(!table.isEmpty && table.editable && isOk(table.changeTrs)){
				setTimeout(function(){
					var tr;
					var columns1=Object.keys(table.columnIndexMap);
					var columns2;
					var colt,columns=null;
					for(var i in table.changeTrs){
						colt=null;
						columns=new Array();
						columns2=table.changeTrColumns["tr_"+table.changeTrs[i]];
						for(var j in columns1){
							colt=columns1[j];
							if(columns2.indexOf(StrUtil.camel(colt)) !=-1 || columns2.indexOf(colt) !=-1 ){
								columns.push(colt);
							}
						}
						tr = table.find("tbody>tr[data-index='"+table.changeTrs[i]+"']");
						//处理一个行的新插入的changed切换
						that.processOneTrNewInsertTdChanged(table,tr,columns,table.tableListDatas[table.changeTrs[i]]);
						//处理这一行的changeed状态
						that.processEditableTrChangedStatus(table,tr,true);
					}
				}, 100);
			}
		},
		processTrDataIndex:function(table){
			if(!table.isEmpty){
				table.table_box.find("table.jbolt_main_table>tbody>tr").each(function(i){
					$(this).data("index",i).attr("data-index",i);
				});

				if(table.left_fixed){
					table.left_fixed.find("table.jbolt_table>tbody>tr").each(function(i){
						$(this).data("index",i).attr("data-index",i);
					});
				}
				if(table.right_fixed){
					table.right_fixed.find("table.jbolt_table>tbody>tr").each(function(i){
						$(this).data("index",i).attr("data-index",i);
					});
				}
			}
		},
		removeInsertDataId:function(table,datas){
			var primaryKey = "id";
			if(table.editableOptions.cols.id && table.editableOptions.cols.id.submitAttr){
				primaryKey = table.editableOptions.cols.id.submitAttr;
			}
			if(isArray(datas)){
				for(var i in datas){
					if(datas[i][primaryKey]){
						delete datas[i][primaryKey];
					}
				}
			}else{
				if(datas[primaryKey]){
					delete datas[primaryKey];
				}
			}

		},
		/**
		 * 插入一行数据
		 */
		insertRowData:function(table,tr,data,dontProcessEleInit,insertToBefore,keepId){
			//处理table.rowTplContent
			var processSuccess = this.processTableRowTplContent(table);
			if(!processSuccess){
				return false;
			}
			//没指定就是false
			if(typeof(keepId)=="undefined"){
				keepId=false;
			}
			var tplContent = table.rowtplContent;
			var html;
			if(isArray(data)){
				if(!keepId){
					this.removeInsertDataId(table,data);
				}
				html=juicer(tplContent,{datas:data,formData:table.formData});
			}else{
				if(data.tableData&&data.extraData){
					var datas=data.tableData;
					var extraData=data.extraData;
					//如果直接传数据数据 就直接渲染
					if(!keepId){
						this.removeInsertDataId(table,datas);
					}
					if(isArray(datas)){
						html=juicer(tplContent,{datas:datas,formData:table.formData,extraData:extraData});
					}else{
						html=juicer(tplContent,{datas:[datas],formData:table.formData,extraData:extraData});
					}
				}else{
					if(!keepId){
						this.removeInsertDataId(table,data);
					}
					html=juicer(tplContent,{datas:[data],formData:table.formData});
				}
			}
			if(html){
				if(table.isEmpty){
					table.tbody.empty();
				}
				var newTr=$(html);
				if(!keepId){
					newTr.data("id","").removeAttr("data-id");
				}
				//处理Tbody添加列操作 比如在第一列添加checkbox等
				var columnPrepend=table.data("column-prepend");
				if(columnPrepend&&table.prependColumnType){
					var td=newTr.find("td:nth-child("+(table.prependColumnIndex+1)+")");
					if(isOk(td)){
						td.before(this.getColumnPrependElement(table.prependColumnType,"td"));
						this.processTrColumnPrependDisabled(table,newTr);
					}
				}

//					var tempId="jbtmp_"+randomId();
//					newTr.attr("data-tempid",tempId).data("tempid",tempId);
				if(isOk(tr)){
					if(insertToBefore){
						tr.before(newTr);
					}else{
						tr.after(newTr);
					}
				}else{
					table.tbody.append(newTr);
				}
				this.processEmptyTableBody(table);
				if(!dontProcessEleInit){
					//处理这一行里的组件初始化
					processInnerElesInit(newTr);
				}
				return newTr;
			}
			return false;
		},
		processTableIndexColumn:function(table){
			if(table.isEmpty){return false;}
			this.processTrDataIndex(table);
			if(table.editable){
				//处理index 列重新排序 但是还没有考虑分页信息计算
				this.reProcessEditableTableMainAfterRowChange(table);
			}else{
				this.reProcessTableMainAfterRowChange(table);
			}
			//重新排序号
			var colIndex=-1;
			//如果设置了indexColumn就在设置列里找
			if(table.editable && table.editableOptions.indexColumn){
				colIndex=table.editableOptions.indexColumn-1;
			}else{
				//如果没设置indexColumn 就去thead里找data-column='index'的列
				var indexTh=table.thead.find("tr>th[data-column='index']");
				if(isOk(indexTh)){
					colIndex = indexTh.data("col-index");
				}
			}

			if(colIndex<0){return false;}
			var tds=table.table_box.find("table.jbolt_main_table>tbody>tr>td[data-col-index='"+colIndex+"']");
			if(!isOk(tds)){return false;}
			var tempTd,temIndexSpan;
			tds.each(function(i){
				tempTd=$(this);
				temIndexSpan=tempTd.find("[data-role='index']");
				if(isOk(temIndexSpan)){
					temIndexSpan.text(i+1);
				}else{
					tempTd.text(i+1);
				}
			});
			//处理fixedtable中的index
			if(table.left_fixed){
				tds=table.left_fixed.find("table.jbolt_table>tbody>tr>td[data-col-index='"+colIndex+"']");
				if(!isOk(tds)){return false;}
				tds.each(function(i){
					$(this).text(i+1);
				});
			}
			if(table.editable){
				this.processNewTrCellStyle(table);
			}
		},
		processNewTrCellStyle:function(table){
			if(table.isEmpty || !table.editable){return false;}
			if(!table.columnIndexMap){return false;}
			var cols = table.editableOptions.cols;
			//初始化可编辑表格里的可编辑和必填列的样式问题
			var keys=Object.keys(table.columnIndexMap);
			var empKey,colConfig,th,colIndex=-1,requiredCellClass;
			for(var i in keys){
				empKey=keys[i];
				colConfig=cols[empKey];
				colIndex=-1;
				if(colConfig&&colConfig.editable){
					colIndex=table.columnIndexMap[empKey];
					if(colIndex>=0){
						//必填列就得上面列头加红星了
						th=table.table_box.find("table>thead>tr>th[data-col-index='"+colIndex+"']");
						if(isOk(th)){
							if(colConfig.required){
								if(!table.isEmpty){
									//可编辑列 就得加底色了
									var tds = table.table_box.find("table>tbody>tr>td[data-col-index='"+colIndex+"']:not([data-editable='false'])");
									if(isOk(tds)){
										tds.data("editable",true).attr("data-editable",true);
										tds.data("required",true).attr("data-required",true);
										requiredCellClass=colConfig.requiredCellClass||table.editableOptions.requiredCellClass;
										if(requiredCellClass){
											tds.addClass(requiredCellClass);
										}
									}
								}
							}else{
								if(!table.isEmpty){
									//可编辑列 就得加底色了
									var tds = table.table_box.find("table>tbody>tr>td[data-col-index='"+colIndex+"']:not([data-editable='false'])");
									if(isOk(tds)){
										tds.data("editable",true).attr("data-editable",true);
										if(table.editableOptions.unrequiredCellClass){
											tds.addClass(table.editableOptions.unrequiredCellClass);
										}
									}
								}
							}
						}
					}

				}
			}

			if(!table.isEmpty){
				uneditableCellCalss=table.editableOptions.uneditableCellCalss;
				if(uneditableCellCalss){
					var cssSelector= "table>tbody>tr>td:not([data-editable='true'])";
					if(table.editableOptions.keepCellStyleCols){
						var colIndex=-1;
						$.each(table.editableOptions.keepCellStyleCols,function(i,index){
							if(typeof(index)=="number"){
								cssSelector=cssSelector+":not([data-col-index='"+(index-1)+"'])";
							}else if(typeof(index)=="string"){
								colIndex=table.columnIndexMap[index];
								if(colIndex>=0){
									cssSelector=cssSelector+":not([data-col-index='"+colIndex+"'])";
								}
							}
						});
					}
					var tds = table.table_box.find(cssSelector);
					if(isOk(tds)){
						tds.addClass(uneditableCellCalss).data("editable",false).attr("data-editable",false);
					}
				}
			}


		},
		/**
		 * 改变切换一行数据
		 */
//			updateRowData:function(table,tr,data){
//				this.insertRowData(table,tr,data);
//				tr.remove();
//				this.processTableIndexColumn(table);
//			},
		processTableRowTplContent:function(table){
			if(!table.rowtplContent){
				var	tplId=table.data("rowtpl");
				if(!tplId){
					LayerMsgBox.alert("JBoltTable表格未配置data-rowtpl模板ID信息",2);
					return false;
				}
				var tplObj = g(tplId);
				var tplContent;
				if(tplObj.tagName == "SCRIPT"){
					tplContent = tplObj.innerHTML;
				}else if(tplObj.tagName == "TEXTAREA"){
					tplContent = tplObj.value;
				}else{
					LayerMsgBox.alert("JBoltTable表格配置data-rowtpl模板指定的组件只能是Script和Textarea",2);
					return false;
				}
				if(!tplContent){
					LayerMsgBox.alert("JBoltTable表格配置data-rowtpl模板中未发现可用模板数据",2);
					return false;
				}
				table.rowtplContent = tplContent;
			}
			return true;
		},
		replaceRow:function(table,data,tr,replaceAllData,keepId,dontProcessChange,forceTrChange){
			if(!isOk(data)){
				LayerMsgBox.alert("替换数据不能为空",2);
				return false;
			}
			if(!isOk(tr)){
				LayerMsgBox.alert("未指定被替换的TR",2);
				return false;
			}
			if(isArray(data)){
				data=data[0];
			}
			//处理table.rowTplContent
			var processSuccess = this.processTableRowTplContent(table);
			if(!processSuccess){
				return false;
			}
			//没指定就是false
			if(typeof(keepId)=="undefined"){
				keepId=false;
			}
			if(!keepId){
				this.removeInsertDataId(table,data);
			}
			var html=juicer(table.rowtplContent,{datas:[data],formData:table.formData});
			if(html){
				var newTr=$(html);
				if(!keepId){
					newTr.data("id","").removeAttr("data-id");
				}
				//处理Tbody添加列操作 比如在第一列添加checkbox等
				var columnPrepend=table.data("column-prepend");
				var hasCheckedele=false;
				if(columnPrepend&&table.prependColumnType){
					var td=newTr.find("td:nth-child("+(table.prependColumnIndex+1)+")");
					if(isOk(td)){
						hasCheckedele=true;
						td.before(this.getColumnPrependElement(table.prependColumnType,"td"));
						this.processTrColumnPrependDisabled(table,newTr);
					}
				}

				tr.html(newTr.html());
				if(hasCheckedele){
					if(columnPrepend.indexOf("checkbox")!=-1){
						var chk=tr.find("div.jbolt_table_checkbox>input[type='checkbox'][name='jboltTableCheckbox']");
						if(isOk(chk)){
							chk.click();
						}
					}else if(columnPrepend.indexOf("radio")!=-1){
						var rdo=tr.find("div.jbolt_table_radio>input[type='radio'][name='jboltTableRadio']");
						if(isOk(rdo)){
							rdo.click();
						}
					}
				}
				//处理这一行里的组件初始化
				processInnerElesInit(tr);

				//处理index column
				this.processTableIndexColumn(table);
				//处理tableListDatas
				this.processReplaceRowTableListData(table,tr,data,replaceAllData);
				//初始化统计行列
				this.initEditableHSummarys(table,tr);
				//处理统计计算
				this.reProcessEditableAllSummarysAfterInsertDataRows(table,tr);
				if(!dontProcessChange){
					//处理change状态
					this.processNewInsertTrEditableTdsChanged(table,tr,data,forceTrChange);
				}
				//处理新插入的行重新设置宽度
				this.resizeTrByOldWidth(table,tr);
				return true;
			}
			return false;
		},
		/**
		 * 重新刷新当前数据 如果有分页还要处理分页
		 */
		setTableDataAfterAjax:function(table,data,formData){
			if(table.data("tolastpage") || notOk(data)){
				return false;
			}
			table.jsonData={};
			table.tableListDatas=[];
			table.formData={};
			if(!formData){
				formData=table.jsonConditions;
			}
			var that=this;
			//添加行数据
			that.addRowDatas(table,data,formData);
			//处理table tbody 列补充类型 额外添加的
			that.processTableTbodyColumnPrepend(table);
			if(data.tableData&&data.extraData&&data.tableData.pageSize){
				//处理分页组件变化
				var pageInfo={pageNumber:data.tableData.pageNumber,pageSize:data.tableData.pageSize,totalPage:data.tableData.totalPage,totalRow:data.tableData.totalRow,firstPage:data.tableData.firstPage,lastPage:data.tableData.lastPage};
				that.initJboltTablePage(table,pageInfo);
			}else{
				if(data.pageSize){
					//处理分页组件变化
					var pageInfo={pageNumber:data.pageNumber,pageSize:data.pageSize,totalPage:data.totalPage,totalRow:data.totalRow,firstPage:data.firstPage,lastPage:data.lastPage};
					that.initJboltTablePage(table,pageInfo);
				}
			}
			table.jsonData=data;
			table.formData=formData;
		},
		showLoading:function(table){
			var loading=table.table_box.find(".jbolt_table_loading");
			if(!isOk(loading)){
				loading=$('<div class="jbolt_table_loading noselect"><span class="loading_msg"><i class="fa fa-spinner fa-pulse mr-2"></i>数据加载中...</span></div>');
				table.table_box.append(loading);
			}
			loading.show();
		},
		hideLoading:function(table){
			table.table_box.find('.jbolt_table_loading').hide();
		},
		processTheadOldWidth:function(table){
			var th,width,dataW,ths=table.thead.find("tr>th[data-col-index]");
			if(isOk(ths)){
				ths.each(function(){
					th=$(this);
					width=th.outerWidth();
					/*dataW=th.data("min-width")||th.data("width");
						if(width<dataW){
							width=dataW;
						}*/
					th.data("old-width",width).attr("data-old-width",width);
				});
			}
		},
		reScrollFixedColumnBox:function(table){
			table.fixedColumnTables=table.table_box.find(".jbolt_table_fixed>.jbolt_table_body>table");
			var leftScroll=table.table_body.scrollLeft();
			if(table.hasHeader){
				table.fixed_header.table.css("margin-left",(0-leftScroll)+"px");
			}
			if(table.hasFooter&&table.tfootFixed){
				table.fixed_footer.table.css("margin-left",(0-leftScroll)+"px");
			}
			var topScroll=table.table_body.scrollTop();
			if(isOk(table.fixedColumnTables)){
				var tableTheadHeight = table.thead.data("height");
				if(tableTheadHeight) {
					table.fixedColumnTables.css("margin-top", (0 - table.thead._height - 2 - topScroll) + "px");
				}else{
					table.fixedColumnTables.css("margin-top", (0 - table.thead._height - topScroll) + "px");
				}
			}
		},
		/**
		 * ajax加载table的数据
		 */
		ajaxLoadTableData:function(table,callback){
			var that=this;
			var url=table.data("url");
			if(!url){
				LayerMsgBox.alert("请指定表格数据加载地址：data-url属性",2);
				return false;
			}
			if(table.left_fixed){
				table.left_fixed.remove();
				table.left_fixed=null;
			}
			if(table.right_fixed){
				table.right_fixed.remove();
				table.right_fixed=null;
			}
			if(table.fixedColumnTables){
				table.fixedColumnTables=null;
			}
			//先把fixed的div删掉
			table.table_box.find(".jbolt_table_fixed").remove();

			//因为同一设置old-width了
			//that.processTheadOldWidth(table);


			that.showLoading(table);
			var page=table.data("page");
			if(page){
				var pageNumber=table.data("pagenumber");
				var pageSize=table.data("pagesize");
				if(!pageSize){
					pageSize=15;
				}
				if(!pageNumber){
					pageNumber=1;
				}
				if(table.readByJsonConditions&&table.jsonConditions){
					table.jsonConditions.page=pageNumber;
					table.jsonConditions.pageSize=pageSize;
				}else{
					if(url.indexOf("?")!=-1){
						url=url+"&page="+pageNumber+"&pageSize="+pageSize;
					}else{
						url=url+"?page="+pageNumber+"&pageSize="+pageSize;
					}
				}
			}
			//处理 sort 参数
			var sortColumn=table.data("sort-column");
			var sortType=table.data("sort-type");
			if(sortColumn&&sortColumn){
				if(table.readByJsonConditions&&table.jsonConditions){
					table.jsonConditions.sortColumn=sortColumn;
					table.jsonConditions.sortType=sortType;
				}else{
					if(url.indexOf("?")!=-1){
						url=url+"&sortColumn="+sortColumn+"&sortType="+sortType;
					}else{
						url=url+"?sortColumn="+sortColumn+"&sortType="+sortType;
					}
				}
			}


			if(table.readByJsonConditions&&table.jsonConditions){
				var postData=table.jsonConditions.isMenuFilter?JSON.stringify(table.jsonConditions):table.jsonConditions;
				Ajax.post(url,postData,function(res){
					that.ajaxLoadDataWithoutFormCallback(table,res,callback);
				});
			}else{
				//执行ajax加载 要考虑绑定了查询条件的需要带着 用post
				var conditionsForm=table.data("conditions-form");
				if(conditionsForm!=undefined&&conditionsForm!=""&&conditionsForm!="undefined"){
					var ajaxCallback=function(res){
						//如果分页了 并且 要求是调用跳转到最后一页 并且TM最后一页满了之后 新增加新页面后totalPage变更了
						//就要根据请求反馈信息拿到最后一页 重新调用一次查询最后一页
						if(table.data("page")){
							var toLastPage=table.data("tolastpage");
							if(toLastPage){
								table.data("tolastpage",false);
								table.removeAttr("data-tolastpage");
								var nowTotalPage=res.data.totalPage;
								var totalPage=table.data("totalpage");
								if(nowTotalPage!=totalPage){
									table.data("pagenumber",res.data.pageNumber);
									table.data("pagesize",res.data.pageSize);
									table.data("totalpage",nowTotalPage);
									//说明新增了一页
									that.readByPage(table,nowTotalPage);
									return true;
								}
							}
						}
						table.tbody.empty();
						table.isEmpty=true;
						//设置表格数据和分页数据
						var formData=$("#"+conditionsForm).serializeJSON();
						that.setTableDataAfterAjax(table,res.data,formData);
						if(callback){
							callback();
						}
						that.hideLoading(table);
						if(res.msg){
							LayerMsgBox.alert(res.msg,7);
						}
					}
					if(!table.data("inited")){
						var form=$("#"+conditionsForm);
						var initFormData=form.serializeJSON();
						var selects=form.find("select[name][data-autoload][data-select]");
						if(isOk(selects)){
							var tmpSelect,dataSelect,dataName,dataValue,value;
							selects.each(function(){
								tmpSelect=$(this);
								dataName=tmpSelect.attr("name");
								dataValue=tmpSelect.attr("value");
								value=tmpSelect.val();
								dataSelect=tmpSelect.data("select");
								//如果选中的值不是默认值
								if(initFormData[dataName]!=dataSelect&&dataSelect!=value){
									initFormData[dataName]=dataSelect;
								}
							});
						}
//						var formDataStr = Object.keys(initFormData).map(function (key) {
//								var value=initFormData[key];
//								if(value&&value!="undefined"){
//									value=encodeURIComponent(value);
//								}else{
//									value='';
//								}
//							return encodeURIComponent(key) + "=" + value;
//							}).join("&");
//						if(url.indexOf("?")!=-1){
//							url=url+"&"+formDataStr;
//						}else{
//							url=url+"?"+formDataStr;
//						}
						url=urlWithJsonData(url,initFormData);
						Ajax.get(url,function(res){
							ajaxCallback(res);
						},function(res){
							that.hideLoading(table);
						});
					}else{
						Ajax.getWithForm(conditionsForm,url,function(res){
							ajaxCallback(res);
						},function(res){
							that.hideLoading(table);
						});
					}

				}else{
					Ajax.get(url,function(res){
						that.ajaxLoadDataWithoutFormCallback(table,res,callback);
					},function(res){
						that.hideLoading(table);
					});
				}
			}
		},
		ajaxLoadDataWithoutFormCallback:function(table,res,callback){
			var that=this;
			//如果分页了 并且 要求是调用跳转到最后一页 并且TM最后一页满了之后 新增加新页面后totalPage变更了
			//就要根据请求反馈信息拿到最后一页 重新调用一次查询最后一页
			if(table.data("page")){
				var toLastPage=table.data("tolastpage");
				if(toLastPage){
					table.data("tolastpage",false);
					table.removeAttr("data-tolastpage");
					var nowTotalPage=res.data.totalPage;
					var totalPage=table.data("totalpage");
					if(nowTotalPage!=totalPage){
						table.data("pagenumber",res.data.pageNumber);
						table.data("pagesize",res.data.pageSize);
						table.data("totalpage",nowTotalPage);
						//说明新增了一页
						that.readByPage(table,nowTotalPage);
						return true;
					}
				}
			}
			table.tbody.empty();
			table.isEmpty=true;
			//设置表格数据和分页数据
			var formData={};
			if(table.readByJsonConditions&&table.jsonConditions){
				formData = table.jsonConditions;
			}
			that.setTableDataAfterAjax(table,res.data,formData);
			if(callback){
				callback();
			}
			that.hideLoading(table);
		},
		/**
		 * 处理空数据样式
		 */
		processEmptyTableBody:function(table){
			var trs=table.tbody.find("tr");
			if(!isOk(trs)){
//					table.tbody.append('<tr><td colspan="100%" class="text-center"><div class="alert alert-warning d-inline-block px-5 py-1 my-2" style="font-size:16px;"><i class="fa fa-warning mr-1"></i>暂无数据</div></td></tr>');
				if(isOk(table.left_fixed)||isOk(table.right_fixed)){
					table.table_box.find(".jbolt_table_fixed").hide();
				}
				table.isEmpty=true;
				table.table_body.addClass("isEmpty");
				if(table.hasFooter){
					table.tfoot.css("visibility","hidden");
				}
			}else{
				if(isOk(table.left_fixed)||isOk(table.right_fixed)){
					table.table_box.find(".jbolt_table_fixed:not(.jbolt_table_fixed_hide)").show();
				}
				table.isEmpty=false;
				table.table_body.removeClass("isEmpty");
				if(table.hasFooter){
					table.tfoot.css("visibility","visible");
				}
			}
		},
		//处理宽度高度
		processTableWidthAndHeight:function(table){
			this.initTableDataSet(table);
			this.setTableWidth(table);
			this.setTableHeight(table);
		},
		//初始化默认值
		initTableDataSet:function(table){
			var width=table.data("width");
			if(!width||(typeof(width)=="string"&&width=="100%")){
				width="fill";
				table.data("width","fill").attr("data-width","fill");
			}
			//fill状态下
			var thWidth,th,styleWidth;
			var autoThs=table.thead.find("th[data-col-index][data-width='auto'],th[data-col-index][data-min-width],th[data-col-index]:not([data-width])");
			var hasAutoTh=isOk(autoThs);
			table.thead.find("th").each(function(){
				th=$(this);
				thWidth=th.data("width");
				if(!thWidth){
					styleWidth=this.style.width;
					if(!styleWidth){
						thWidth="auto";
					}else if(styleWidth.indexOf("px")!=-1){
						thWidth=parseInt(styleWidth);
					}
					th.data("width",thWidth).attr("data-width",thWidth);
					if(thWidth=="auto"&&this.hasAttribute("data-col-index")&&!this.hasAttribute("data-min-width")){
						th.data("min-width","100").attr("data-min-width","100");
					}
				}else if(typeof(thWidth)=="number"&&this.hasAttribute("data-col-index")){
					if(hasAutoTh){
						th.attr("data-nochange","true").data("nochange",true);
					}
				}
			});
			//不设置就是fill
			var height=table.data("height");
			if(!height){
				var maxHeight=table.data("max-height");
				if(!maxHeight){
					table.data("height","fill").attr("data-height","fill");
				}
			}
		},
		//设置宽度
		setTableWidth:function(table){
			var width=table.data("width");
			if(width){
				var wtype=typeof(width);
				if(wtype=="number"){
					width=width+"px";
					table.table_view.css("width",width);
				}else if(wtype=="string"&&width=="auto"){
					table.table_view.css({"width":"100%","max-width":"100%"});
				}else if(wtype=="string"&&width=="fill"){
					table.table_view.css({"width":"100%","max-width":"100%"});
				}else if(wtype=="string"&&width.indexOf("%")!=-1&&width!="100%"){
					table.table_view.css({"width":width,"max-width":width});
				}else{
					table.table_view.css({"width":width,"max-width":width});
				}
			}
		},
		//处理主从结构高度
		processMasterSlaveBoxHeight:function(table){
			mainPjaxContainer.css({
				"overflow":"auto"
			});
			var box=table.closest(".jbolttable_master_slave_box");
			if(!isOk(box)){return false;}
			mainPjaxContainer.css({
				"overflow":"hidden"
			});
			table.data("in-master-slave",true);
			var jboltPage=box.closest(".jbolt_page");
			var height=jboltPage.height();
			var pageTitle=jboltPage.find(".jbolt_page_title");
			if(isOk(pageTitle)){
				height=height-parseInt(pageTitle.outerHeight());
			}
			height=height-18;
			box.css({
				"height":height,
				"max-height":height
			});
		},
		//处理fill_box
		processTableFillBox:function(table){
			if(table.data("height")!="fill_box"){return false;}
			var parent=table.table_view.parent();
			var totalHeight=parent.height();
			var height=this.processTableFillHeight(table,totalHeight,true);
			table.table_body.css("height",height+"px");
			table.table_body.css("max-height",height+"px");
			return height;
		},
		//设置高度
		setTableHeight:function(table){
			var that=this,
				height=table.data("height");
			maxheight=table.data("max-height")
			leftrightbox_height=0;
			if(!height&&maxheight){
				height=maxheight;
			}
			if(height){
				var htype=typeof(height);
				if(htype=="string"&&(height=="100%"||height=="fill")){
					var fillHeight=that.getFillHeight(table);
					if(!maxheight){
						table.table_body.css("height",fillHeight+"px");
					}
					table.table_body.css("max-height",fillHeight+"px");
					leftrightbox_height=fillHeight;
				}else if(htype=="string"&&height=="fill_box"){
					leftrightbox_height=that.processTableFillBox(table);
				}else if(htype=="string"&&height.indexOf("%")!=-1){
					var fillHeight=that.getFillHeight(table);
					var numberHeightPre=Number(height.replace("%",""))/100;
					var tableHeight=parseInt(fillHeight*numberHeightPre);
					if(!maxheight){
						table.table_body.css("height",tableHeight+"px");
					}
					table.table_body.css("max-height",tableHeight+"px");
					leftrightbox_height=tableHeight;
				}else{
					if(!(typeof(height)=="string" && height== 'auto')){
						var thead=table.find("thead"),
							thh=thead.outerHeight();
						height=height-thh-4;
						if(!maxheight){
							table.table_body.css("height",height+"px");
						}
						table.table_body.css("max-height",height+"px");
						leftrightbox_height=height;
					}

				}
				if(table.leftbox||table.rightbox){
					var pages=table.page||table.table_view.find('.jbolt_table_pages');
					if(isOk(pages)){
						leftrightbox_height=leftrightbox_height+pages.outerHeight();
					}
					if(table.hasHeader){
						leftrightbox_height=leftrightbox_height+(table.thead.outerHeight()||0);
					}
					if(table.hasFooter&&table.tfootFixed){
						leftrightbox_height=leftrightbox_height+(table.tfoot.outerHeight()||0);
					}
					leftrightbox_height=leftrightbox_height+1;
//						if(table.leftbox){
//							table.leftbox.css("height",leftrightbox_height+"px").css("max-height",leftrightbox_height+"px");
//						}
//						if(table.rightbox){
//							table.rightbox.css("height",leftrightbox_height+"px").css("max-height",leftrightbox_height+"px");
//						}
					that.resizeTableLeftAndRightBoxHeight(table,leftrightbox_height);
				}
				that.refreshMainTableVScroll(table);
			}

		},
		resizeTableLeftAndRightBoxHeight:function(table,leftrightbox_height){
			if(table.leftbox){
				var left_body=table.leftbox.find(".jb_body");
				if(isOk(left_body)){
					var minus_height=0;
					var left_header=table.leftbox.find(".jb_header");
					var left_footer=table.leftbox.find(".jb_footer");
					if(isOk(left_header)){
						minus_height=minus_height+left_header.outerHeight();
					}
					if(isOk(left_footer)){
						minus_height=minus_height+left_footer.outerHeight();
					}


					var leftHeight=leftrightbox_height-minus_height;
					left_body.css({"height":leftHeight+"px","max-height":leftHeight+"px"});
				}else{
					table.leftbox.css({"height":leftrightbox_height+"px","max-height":leftrightbox_height+"px"});
				}
			}

			if(table.rightbox){
				var right_body=table.rightbox.find(".jb_body");
				if(isOk(right_body)){
					var minus_height=0;
					var right_header=table.rightbox.find(".jb_header");
					var right_footer=table.rightbox.find(".jb_footer");
					if(isOk(right_header)){
						minus_height=minus_height+right_header.outerHeight();
					}
					if(isOk(right_footer)){
						minus_height=minus_height+right_footer.outerHeight();
					}
					var rightHeight=leftrightbox_height-minus_height;
					right_body.css({"height":rightHeight+"px","max-height":rightHeight+"px"});
				}else{
					table.rightbox.css({"height":leftrightbox_height+"px","max-height":leftrightbox_height+"px"});
				}
			}
		},
		getFillHeightInMasterSlaveBox:function(table){
			var splitBox=table.table_view.closest(".split");
			if(isOk(splitBox)){
				var thh=parseInt(table.thead.outerHeight())+1;
				return parseInt(splitBox.height())-thh;
			}
		},
		getFillHeight:function(table){
			//判断在master slavebox中
			var jbolttable_master_slave_box=table.table_view.closest(".jbolttable_master_slave_box");
			if(isOk(jbolttable_master_slave_box)){
				return this.getFillHeightInMasterSlaveBox(table);
			}
			var isMax=table.table_view.hasClass("maximize");
			var that=this,height=0;
			if(isMax){
				height=table.table_view.height()-2;
			}else{
				//判断是不是在jbolt_page中
				var jboltPage=table.table_view.closest(".jbolt_page");
				if(isOk(jboltPage)){
					height=0;
					if(window.self!=window.top){
						height=Math.floor(jboltWindowHeight-parseInt(jboltPage.css("padding-bottom"))*2-2);
					}else{
						var jboltLayerPortal=jboltPage.closest(".jbolt_layer_portal");
						if(isOk(jboltLayerPortal)){
							height=Math.floor(jboltLayerPortal.height()-2-parseInt(mainPjaxContainer.css("padding-bottom"))*2);
						}else{
							height=Math.floor(mainPjaxContainer.height()-2-parseInt(mainPjaxContainer.css("padding-bottom"))*2);
						}
					}
					var pageTitle=jboltPage.find(".jbolt_page_title");
					if(isOk(pageTitle)){
						height=height-parseInt(pageTitle.outerHeight())-parseInt(pageTitle.css("margin-bottom"));//10是margin-bottom
					}
				}else{
					height=0;
					if(window.self!=window.top){
						height=parseInt(jboltWindowHeight-32);
					}else{
						var jboltLayerPortal=jboltPage.closest(".jbolt_layer_portal");
						if(isOk(jboltLayerPortal)){
							height=Math.floor(jboltLayerPortal.height()-parseInt(mainPjaxContainer.css("padding-bottom"))*2);
						}else{
							height=jboltWindowHeight-parseInt(mainPjaxContainer.css("padding-bottom"))*2;
							var jbolt_admin_main_top=$(".jbolt_admin_main_top");
							if(isOk(jbolt_admin_main_top)){
								height=parseInt(height-jbolt_admin_main_top.outerHeight());
							}
						}

					}
				}
			}
			height=this.processTableFillHeight(table,height,false);
			return height;
		},
		//通过已经固定高度开始处理减去其他非表格核心body元素的高度
		processTableFillHeight:function(table,height,isFillBox){
			if(typeof(isFillBox)=="undefined"){
				isFillBox=false;
			}
			var page_box=table.table_box.find(".jbolt_table_pages");
			if(isOk(page_box)){
				var pagerHeight=Math.ceil(page_box.outerHeight(true));
				if(pagerHeight<44){
					pagerHeight=44;
				}
				height=height-pagerHeight-2;
			}else{
				height = height -2;
			}
			if(isOk(table.toolbar)){
				var toolbarHeight=Math.ceil(table.toolbar.outerHeight());
				height=height-toolbarHeight;
			}
			if(isOk(table.headbox) && table.headbox.is(":visible")){
				var tableHeadboxHeight=Math.ceil(table.headbox.outerHeight());
				height=height-tableHeadboxHeight;
			}
			if(isOk(table.footbox) && table.footbox.is(":visible")){
				var tableFootboxHeight=Math.ceil(table.footbox.outerHeight());
				height=height-tableFootboxHeight;
			}

			var eles=table.data("height-elements");
			if(isOk(eles)){
				jboltPage.find(eles).each(function(){
					height=height-Math.ceil($(this).outerHeight());
				});
			}
			if(table.hasHeader){
				height=height-table.thead.outerHeight();
			}
			if(table.hasFooter&&table.tfootFixed&&isOk(table.tfoot)){
				height=height-table.tfoot.outerHeight();
			}
			if(isFillBox){
				var preveles=table.table_view.prevAll(":not(script,style,textarea.jb_tpl_box)");
				if(isOk(preveles)){
					preveles.each(function(){
						height=height-Math.ceil($(this).outerHeight(true));
					});
				}
				var nexteles=table.table_view.nextAll(":not(script,style,textarea.jb_tpl_box)");
				if(isOk(nexteles)){
					nexteles.each(function(){
						height=height-Math.ceil($(this).outerHeight(true));
					});
				}
				height = height - 1;
			}else{
				if(jboltWithTabs){
					height = height + 10;
				}
			}
			if(isOk(table.leftbox) || isOk(table.rightbox)) {
				height = height -2;
			}


			height=Math.floor(height);
			return height;
		},
		isSetRightFixedColumn:function(table){
			//判断是否设置了fixedColumn
			return  table.data("fixed-columns-right");
		},
		isSetFixedColumn:function(table){
			//判断是否设置了fixedColumn
			return (table.data("fixed-columns-left")||table.data("fixed-columns-right"));
		},
		/**
		 * ajax加载数据或者渲染本地Table后执行的样式处理任务
		 */
		processTableStyle:function(table){
			var that=this;
			//处理treetable
			that.processTreeTable(table);
			//处理单元格宽度
			that.processCellWidthAndHeight(table);
			//处理fixed Header Footer
			that.processHeaderAndFooterFixed(table);
			//处理fixedColumn
			that.processColumnFixed(table);
			//处理空的tbody
			that.processEmptyTableBody(table);
		},
		processColumnFixed:function(table){
			//处理fixedColumn left
			this.processColumnFixedLeft(table);
			//处理fixedColumn right
			this.processColumnFixedRight(table);
			//如果有横向滚动条 处理一下样式
			this.refreshFixedColumnHScroll(table);
			//处理fixed的滚动位置
			this.reScrollFixedColumnBox(table);
		},
		processFixedColumnTableTrHoverEvent:function(table){
			//fixed table的houver事件处理
			table.table_box.on("mouseenter",".jbolt_table_fixed>.jbolt_table_body>table>tbody>tr",function(){
				var tr=$(this);
				var index=tr.index();
				table.tbody.find("tr:eq("+index+")").addClass("hover");
				if(isOk(table.fixedColumnTables)){
					table.fixedColumnTables.find("tbody>tr:eq("+index+")").addClass("hover");
				}
			}).on("mouseleave",".jbolt_table_fixed>.jbolt_table_body>table>tbody>tr",function(){
				var tr=$(this);
				var index=tr.index();
				table.tbody.find("tr:eq("+index+")").removeClass("hover");
				if(isOk(table.fixedColumnTables)){
					table.fixedColumnTables.find("tbody>tr:eq("+index+")").removeClass("hover");
				}
			});
		},
		processMainTableTrHoverEvent:function(table){
			//主table的hover事件
			table.on("mouseenter","tbody>tr",function(){
				var tr=$(this);
				tr.addClass("hover");
				var index=tr.index();
				if(isOk(table.fixedColumnTables)){
					table.fixedColumnTables.find("tbody>tr:eq("+index+")").addClass("hover");
				}
			}).on("mouseleave","tbody>tr",function(){
				var tr=$(this);
				tr.removeClass("hover");
				var index=tr.index();
				if(isOk(table.fixedColumnTables)){
					table.fixedColumnTables.find("tbody>tr:eq("+index+")").removeClass("hover");
				}
			});
		},
		processMainTableBodyScrollAndResizeEvent:function(table){
			//处理主table的scroll和resize
			var that=this;
			table.table_body.on("scroll",function(e){
				var leftScroll=table.table_body.scrollLeft();
				if(table.hasHeader){
					table.fixed_header.table.css("margin-left",(0-leftScroll)+"px");
				}
				if(table.hasFooter&&table.tfootFixed){
					table.fixed_footer.table.css("margin-left",(0-leftScroll)+"px");
				}
				var topScroll=table.table_body.scrollTop();
				if(isOk(table.fixedColumnTables)){
					var tableTheadHeight = table.thead.data("height");
					if(tableTheadHeight) {
						table.fixedColumnTables.css("margin-top", (0 - table.thead._height - 2 - topScroll) + "px");
					}else{
						table.fixedColumnTables.css("margin-top", (0 - table.thead._height - topScroll) + "px");
					}
				}
				AutocompleteUtil.hideResult();
			}).on("resize",function(e){
				e.preventDefault();
				e.stopPropagation();
				that.processTableColWidthAfterResize(table);
				//reize的时候判断scroll相关的处理了
				that.refreshMainTableVScroll(table);
				//如果有横向滚动条 处理一下样式
				that.refreshFixedColumnHScroll(table);
				AutocompleteUtil.hideResult();
				return false;
			});
		},
		/**
		 * ajax加载数据前或者渲染本地Table后执行的事件处理任务
		 */
		processTableEvent:function(table){
			var that=this;
			//处理主table的scroll和resize
			that.processMainTableBodyScrollAndResizeEvent(table);
			//处理主table的tr hover绑定
			that.processMainTableTrHoverEvent(table);
			//处理table tr hover
			that.processFixedColumnTableTrHoverEvent(table);
			//处理tableheader上的拖拽列宽和点击列头排序事件
			that.processTableColWidthResizeAndColSortEvent(table);
			//处理tablebox上的拖拽调整宽度高度事件
			that.processTableBoxResizeEvent(table);
			//处理columnPrepend点击事件
			that.processTableColumnPrependEvent(table);
			//处理fixedColumn上的滚轮事件
			that.processFixedColumnScrollEvent(table);
			//处理columnprepend row点击选中
			that.processColumnprependCheckedByRowClick(table);
			//处理row click active handler
			that.processTableRowClickActiveAndHandler(table);
			//处理点击treeTable 展开和闭合事件
			that.processTreeTableEvent(table);
			//处理menus
			that.processTableMenuAndEvent(table);
			//处理首次加载处理完成回调
			that.processInitCallback(table);
			//处理复制和粘贴数据事件tabletoexcel
			that.processCopyTableToExcelEvent(table);
			//处理复制和粘贴数据事件exceltotable
			that.processCopyExcelToTableEvent(table);

		},
		//复制选中的数据
		copyTableSelectedItemsData:function(table,withHeader){
			if(!table.hasCopyItems){
				LayerMsgBox.alert("表格中没有选中的数据",2);
				return;
			}
//				table.indexColumnTextMap;
			//拿到需要复制的列
			var colIndexArr = [];
			var colHeaderTextArr = [];
			var tempTh,tempIndex,tempThText;
			table.thead.find("tr>th.jbt_copy_th").each(function(){
				tempTh = $(this);
				tempIndex = tempTh.data("col-index")+'';
				colIndexArr.push(tempIndex);
				if(withHeader){
					if(table.indexColumnTextMap){
						colHeaderTextArr.push(table.indexColumnTextMap["col_"+tempIndex]);
					}else{
						tempThText=$.trim(tempTh[0].innerText||"col_"+tempIndex);
						colHeaderTextArr.push(tempThText);
					}
				}
			});
			if(colIndexArr.length==0){
				LayerMsgBox.alert("表格中没有选中的数据",2);
				return;
			}
			var copyAllDatas = [];
			if(withHeader){
				copyAllDatas.push(colHeaderTextArr.join("\t"));
			}
			table.tbody.find("tr.jbt_copy_tr").each(function(){
				var copyTrDatas = [];
				var tr = $(this);
				var td,tempText,tempIndex;
				tr.find("td[data-col-index]").each(function(){
					td = $(this);
					tempIndex = td.data("col-index")+'';
					if(colIndexArr.includes(tempIndex)){
						if(td.hasClass("jbt_copy_td")){
							tempText  = $.trim(td[0].innerText||"");
							copyTrDatas.push(tempText);
						}else{
							copyTrDatas.push("");
						}
					}
				});
				copyAllDatas.push(copyTrDatas.join("\t"));
			});
			var dataStr = copyAllDatas.join("\r\n");
			var textareaHtml = "<textarea id='jbolt_copy_temp_textarea'>"+dataStr+"</textarea>";
			jboltBody.prepend(textareaHtml);
			var textarea = jboltBody.find("#jbolt_copy_temp_textarea");
			textarea.select();
			document.execCommand('copy');
			textarea.remove();
			LayerMsgBox.success("复制成功",1000);
		},
		//创建出表格的复制
		createTableCopyDataOptBox:function(table){
			var tableId = table.attr("id");
			var that = this;
			var box = '<div data-table-id="'+tableId+'"  class="jbolt_table_copy_opt jbolt_drag_box">'+
				'<div class="jbolt_drag_trigger" tooltip data-title="点我拖动改变位置"><i class="d-inline-block text-dark jbicon2 jbi-drag" style="font-size:30px;"></i></div>'+
				'<ul><li data-func="copyData" tooltip data-title="只复制数据"><i class="jbicon2 jbi-file-copy"></i></li>'+
				'<li data-func="copyDataWithHeader" tooltip data-title="复制数据_带表头"><i class="jbicon2 jbi-file-copy-fill"></i></li>'+
				'<li data-func="clear" tooltip data-title="清空选择"><i class="jbicon2 jbi-clear"></i></li>'+
				'</ul></div>';
			var boxObj = $(box);
			table.table_view.prepend(boxObj);
			initToolTip(boxObj);
			boxObj.on("click","ul>li",function(){
				var func = $(this).data("func");
				if(func == "copyData"){
					that.copyTableSelectedItemsData(table);
				}else if(func == "copyDataWithHeader"){
					that.copyTableSelectedItemsData(table,true);
				}else if(func == "clear"){
					that.clearTableWillCopyState(table);
				}
			});
			var drapTrigger = boxObj.find(".jbolt_drag_trigger");
			//自由拖动
			freelyDragging(drapTrigger,boxObj,table.table_view);
			return boxObj;
		},
		//显示表格copy操作按钮
		showTableCopyOpt:function(table){
			if(!table.copyDataOptBox){
				table.copyDataOptBox = this.createTableCopyDataOptBox(table);
			}
			table.copyDataOptBox.show();
		},
		//隐藏表格copy操作按钮
		hideTableCopyOpt:function(table){
			if(!table.copyDataOptBox){
				table.copyDataOptBox = this.createTableCopyDataOptBox(table);
			}
			table.copyDataOptBox.hide();
		},
		//处理表格copy状态
		processTableCopyState:function(table){
			table.hasCopyItems = isOk(table.thead.find("tr>th.jbt_copy_th"));
			if(table.hasCopyItems){
				this.showTableCopyOpt(table);
				this.processTableCopyTr(table);
			}else{
				this.hideTableCopyOpt(table);
			}
		},
		processTableCopyTr:function(table){
			if(table.hasCopyItems){
				var tempTr,tds;
				table.tbody.find("tr").each(function(){
					tempTr = $(this);
					tds = tempTr.find("td.jbt_copy_td");
					if(isOk(tds)){
						tempTr.addClass("jbt_copy_tr")
					}
				});
			}
		},
		//处理单元格表格复制数据到excel
		processCopyTableToExcelEvent:function(table){
			var copyToExcel = table.data("copy-to-excel");
			if(typeof(copyToExcel)=="undefined"){
				copyToExcel = false;
			}
			if(!copyToExcel){
				return;
			}
			var that = this;
			table.hasCopyItems = false;
			table.willCopy = false;
			//按下ctrl 锁定表格
			jboltBody.on("keydown",function(e){
				table.willCopy = !e.shiftKey && !e.ctrlKey && e.altKey;
				if(table.willCopy){
					table.table_box.addClass("noselect");
				}else{
					table.table_box.removeClass("noselect");
				}
			});

			jboltBody.on("keyup",function(e){
				if(e.which==18){
					table.willCopy = false;
					table.table_box.removeClass("noselect");
				}else if(e.which==27){
					that.clearTableWillCopyState(table);
				}
			});
			table.table_view.on("click","table.jbolt_table>tbody>tr>td",function(e){
				var td=$(this);
				var colIndex = td.data("col-index");
				var trIndex = td.parent().data("index");
				if(table.willCopy){
					table.table_view.find("table>tbody>tr[data-index='"+trIndex+"']>td[data-col-index='"+colIndex+"']").addClass("jbt_copy_td");
//						td.addClass("jbt_copy_td");
				}else{
					table.table_view.find("table>tbody>tr[data-index='"+trIndex+"']>td[data-col-index='"+colIndex+"']").removeClass("jbt_copy_td");
//						td.removeClass("jbt_copy_td");
				}
				that.processTableCopyTh(table,td);
				that.processTableCopyState(table);
			});
			table.table_view.on("dblclick","table.jbolt_table>thead>tr>th[data-col-index]",function(e){
				var th=$(this);
				var colIndex = th.data("col-index");
				if(table.willCopy){
					table.table_view.find("table.jbolt_table>tbody>tr>td[data-col-index='"+colIndex+"']").addClass("jbt_copy_td");
					table.table_view.find("table.jbolt_table>thead>tr>th[data-col-index='"+colIndex+"']").addClass("jbt_copy_th");
				}else{
					table.table_view.find("table.jbolt_table>tbody>tr>td[data-col-index='"+colIndex+"']").removeClass("jbt_copy_td");
					table.table_view.find("table.jbolt_table>thead>tr>th[data-col-index='"+colIndex+"']").removeClass("jbt_copy_th");
				}
				that.processTableCopyState(table);
			});
			table.table_view.on("click","table.jbolt_table>thead>tr>th[data-col-index]",function(e){
				if(table.willCopy){
					var th=$(this);
					var colIndex = th.data("col-index");
					table.table_view.find("table.jbolt_table>tbody>tr>td[data-col-index='"+colIndex+"']").toggleClass("jbt_copy_td");
					that.processTableCopyThByColIndex(table,th.data("col-index"));
					that.processTableCopyState(table);
				}
			});


			this.processMouseSelectTableCellToCopy(table);
		},
		processMouseSelectTableCellToCopy:function(table){
			table.firstindexrow=-1;
			table.firstindexcol=-1;
			table.curindexrow=-1;
			table.curindexcol=-1;
			table.isMouseDown = false;
			var that = this;
			//添加点击事件
			table.table_box.on("mousedown","table.jbolt_table>tbody>tr>td",function (e) {
				if(table.willCopy){
					table.isMouseDown = true;
					var currentTD = $(this);
					var currentTR = currentTD.parent();
					table.firstindexrow = currentTR.data("index");
					table.firstindexcol=currentTD.data("col-index");
					table.curindexrow = table.firstindexrow;
					table.curindexcol = table.firstindexcol;
					currentTD.addClass("jbt_copy_td");
					table.copySelectAreaStartPoint = {x:e.clientX,y:e.clientY};
					table.copySelectArea = {left:e.clientX,top:e.clientY,width:0,height:0};
				}
			}).on("mouseenter","table.jbolt_table>tbody>tr>td",function (e) {
				if (table.willCopy&&table.isMouseDown) {
					var currentTD = $(this);
					var currentTR = currentTD.parent();
					table.curindexrow = currentTR.data("index");
					table.curindexcol=currentTD.data("col-index");
					var minrow = table.curindexrow>table.firstindexrow?table.firstindexrow:table.curindexrow;
					var mincol = table.curindexcol>table.firstindexcol?table.firstindexcol:table.curindexcol;
					var maxrow = table.curindexrow>table.firstindexrow?table.curindexrow:table.firstindexrow;
					var maxcol = table.curindexcol>table.firstindexcol?table.curindexcol:table.firstindexcol;
					if(e.buttons==2){
						for(var i=minrow;i<=maxrow;i++){
							for(var j=mincol;j<=maxcol;j++){
								$("tr[data-index='"+i+"']>td[data-col-index='"+j+"']").removeClass("jbt_copy_td");
								that.processTableCopyThByColIndex(table,j);
							}
						}
						e.preventDefault();
						e.stopPropagation();
						return false;
					}else{
						for(var i=minrow;i<=maxrow;i++){
							for(var j=mincol;j<=maxcol;j++){
								$("tr[data-index='"+i+"']>td[data-col-index='"+j+"']").addClass("jbt_copy_td");
								that.showTableCopyThByColIndex(table,j);
							}
						}
						e.preventDefault();
						e.stopPropagation();
						return false;
					}
				}
			}).on("contextmenu","table.jbolt_table>tbody>tr>td",function(e){
				if(table.willCopy) {
					e.preventDefault();
					e.stopPropagation();
					return false;
				}
			}).on("mousemove","table.jbolt_table>tbody>tr>td",function (e) {
				if(table.willCopy&&table.isMouseDown) {
					var x=e.clientX,y=e.clientY;
					table.copySelectArea.width=Math.abs(x-table.copySelectAreaStartPoint.x);
					table.copySelectArea.height=Math.abs(y-table.copySelectAreaStartPoint.y);

					if(x<table.copySelectAreaStartPoint.x){
						table.copySelectArea.left = x;
					}
					if(y<table.copySelectAreaStartPoint.y){
						table.copySelectArea.top = y;
					}
					that.drawTableCopySelectArea(table);
				}else{
					that.clearTableCopySelectArea(table)
				}
			});

			jboltBody.on("mouseup",function (e) {
				if(table.willCopy&&table.isMouseDown) {
					e.preventDefault();
					e.stopPropagation();
					that.processTableCopyState(table);
					that.clearTableCopySelectArea(table);
					table.isMouseDown = false;
					table.firstindexrow=-1;
					table.firstindexcol=-1;
					table.curindexrow=-1;
					table.curindexcol=-1;
					return false;
				}
			}).on("mousemove",".jbt_copy_select_area_layer",function(e){
				if(table.willCopy&&table.isMouseDown) {
					var x=e.clientX,y=e.clientY;
					table.copySelectArea.width=Math.abs(x-table.copySelectAreaStartPoint.x);
					table.copySelectArea.height=Math.abs(y-table.copySelectAreaStartPoint.y);

					if(x<table.copySelectAreaStartPoint.x){
						table.copySelectArea.left = x;
					}
					if(y<table.copySelectAreaStartPoint.y){
						table.copySelectArea.top = y;
					}
					that.drawTableCopySelectArea(table);
				}else{
					that.clearTableCopySelectArea(table)
				}
			});
		},
		drawTableCopySelectArea:function(table){
			if(table.willCopy&&table.isMouseDown && table.copySelectArea) {
				if(!table.copySelectAreaLayer){
					table.copySelectAreaLayer = this.createCopySelectAreaLayer(table);
				}
				table.copySelectAreaLayer.css(table.copySelectArea).show();
			}
		},
		createCopySelectAreaLayer:function(table){
			var box = $("<div class='jbt_copy_select_area_layer'></div>");
			jboltBody.append(box);
			return box;
		},
		clearTableCopySelectArea:function(table){
			if(table.copySelectAreaLayer){
				table.copySelectAreaLayer.hide();
			}
		},
		//处理有选中items的th
		processTableCopyTh:function(table,td){
			var colIndex = td.data("col-index");
			var tds = table.tbody.find("tr>td[data-col-index='"+colIndex+"'].jbt_copy_td");
			if(isOk(tds)){
				table.table_view.find("table.jbolt_table>thead>tr>th[data-col-index='"+colIndex+"']").addClass("jbt_copy_th");
			}
		},
		showTableCopyThByColIndex:function(table,colIndex){
			var th = table.thead.find("tr>th[data-col-index='"+colIndex+"']");
			if(isOk(th)){
				th.addClass("jbt_copy_th");
			}
			if(table.fixed_header){
				table.fixed_header.table.find("thead>tr>th[data-col-index='"+colIndex+"']").addClass("jbt_copy_th");
			}
		},
		//处理有选中items的th
		processTableCopyThByColIndex:function(table,colIndex){
			var tds = table.tbody.find("tr>td[data-col-index='"+colIndex+"'].jbt_copy_td");
			var th = table.thead.find("tr>th[data-col-index='"+colIndex+"']");
			if(isOk(tds)){
				if(isOk(th)){
					th.addClass("jbt_copy_th");
				}
				if(table.fixed_header){
					table.fixed_header.table.find("thead>tr>th[data-col-index='"+colIndex+"']").addClass("jbt_copy_th");
				}
			}else{
				if(isOk(th)){
					th.removeClass("jbt_copy_th");
				}
				if(table.fixed_header){
					table.fixed_header.table.find("thead>tr>th[data-col-index='"+colIndex+"']").removeClass("jbt_copy_th");
				}
			}
		},
		//清空表格willCopy状态
		clearTableWillCopyState:function(table){
			if(table.hasCopyItems){
				table.table_view.find("table.jbolt_table>tbody>tr>td.jbt_copy_td").removeClass("jbt_copy_td");
				table.table_view.find("table.jbolt_table>thead>tr>th.jbt_copy_th").removeClass("jbt_copy_th");
			}
			this.hideTableCopyOpt(table);
			table.willCopy = false;
			table.hasCopyItems = false;
			table.table_box.removeClass("noselect");
			table.copyDataOptBox.css({
				left:"3px",
				top:"43px"
			})
		},
		processCopyExcelToTableEvent:function(table){
			var copyFromExcel = table.data("copy-from-excel");
			if(typeof(copyFromExcel)=="undefined"){
				copyFromExcel = false;
			}
			if(!copyFromExcel){
				return;
			}
			var that = this;
			if(!table.editable){return;}
			table[0].addEventListener('paste',function(event){
				if(!table.tbody.focusTd){
					return;
				}
				var html = event.clipboardData.getData('text/html');
				if(!html){
					return;
				}
				var doc = new DOMParser().parseFromString(html,'text/html');
				if(!doc){
					return;
				}
				// 加载所有的行
				var trs = Array.from(doc.querySelectorAll('table tr'));
				if(isOk(trs)){
					//要新加入的长度
					var trLen = trs.length;
					//选中行index
					var trIndex=table.tbody.focusTd.parent().data("index");
					var currentTrs = table.table_body.find("table.jbolt_main_table>tbody>tr");
					//现在已经有多少行
					var endIndex = currentTrs.length-1;
					//如果现有行数不够 就得增加新空行等待复制数据进去
					var needCount = (trLen-1+trIndex) - endIndex;
					if(needCount){
						var emptyDatas=[];
						for(var i=0;i<needCount;i++){
							//执行插入新行
							var result = that.insertEmptyRow(table);
							if(!isOk(result)){
								event.preventDefault();
								event.stopPropagation();
								return;
							}
						}

					}

					var tdIndex = table.tbody.focusTd.data("col-index");
					var excelTr,excelTd,excelValue,tdLen,targetTr,targetTd;
					for(var i = 0;i<trLen;i++){
						excelTr = trs[i];
						targetTr = table.tbody.find("tr[data-index='"+(trIndex+i)+"']");
						if(isOk(targetTr)){
							tds=excelTr.children;
							tdLen=tds.length;
							if(tdLen){
								for(var k=0;k<tdLen;k++){
									excelTd = tds[k];
									excelValue = excelTd.innerText;
									targetTd = targetTr.find("td[data-col-index='"+(tdIndex+k)+"']")
									if(isOk(targetTd)){
										that.processEditableTdChooseData(table,targetTd,excelValue,excelValue);
									}
								}
							}
						}

					}
				}
			})
		},
		closeMenu:function(table){
			//判断并关闭右键菜单
			if(isOk(table.menus)){
				table.menus.hide();
				FormDate.hide(table.menus);
				if(table.currentMenuTd){
					table.currentMenuTd.removeClass("jb_current_menu_td");
					table.currentMenuTd=null;
				}
			}
		},
		getMenuTplByTheme:function(theme){
			if(theme=="button"){
				return '<div class="jbolt_table_menus theme_button left noselect" style="{@if width}width:${width}px;{@/if}"><a href="javascript:void(0)" class="closebtn" onclick="closeJBoltTableMenu(this)" title="关闭菜单"><i class="jbicon jb-close"></i></a>{@each menus as menu,index}{@if menu.br}<br/>{@else}<a tabindex="-1" data-menu-index="${+index}" class="jbolt_table_menu btn-sm ${menu.cssClass?menu.cssClass:"btn btn-light"}" style="${menu.cssStyle}">{@if menu.icon}<i class="${menu.icon} mr-1"></i>{@/if}<span class="jbolt_table_menu_text">${menu.text}</span></a>{@/if}{@/each}</div>';
			}
			return '<div class="jbolt_table_menus theme_list noselect" style="{@if width}width:${width}px;{@/if}"><a href="javascript:void(0)" class="closebtn" onclick="closeJBoltTableMenu(this)" title="关闭菜单"><i class="jbicon jb-close"></i></a><div class="dropdown-menu" style="{@if width}width:${width}px;{@/if}">{@each menus as menu,index}{@if menu.divider||menu.br}<div class="dropdown-divider"></div>{@else if menu.custom}<div class="jbolttable_custombox ${menu.cssClass?menu.cssClass:""}" style="${menu.cssStyle}">$${menu.html}</div>{@else}<a tabindex="-1" data-menu-index="${+index}" class="jbolt_table_menu dropdown-item ${menu.cssClass?menu.cssClass:"btn btn-light"}" style="${menu.cssStyle}">{@if menu.icon}<i class="${menu.icon} mr-1"></i>{@/if}<span class="jbolt_table_menu_text">${menu.text}</span></a>{@/if}{@/each}</div>';
		},
		processTableMenuOptions:function(table){
			if(isOk(table.menuOptions)){
				return true;
			}
			var menuOptionsStr=table.data("menu-option");
			if(typeof(menuOptionsStr)=="undefined"||!isOk(menuOptionsStr)){
				return false;
			}
			var optionFunc=eval(menuOptionsStr);
			if(!optionFunc||typeof(optionFunc)!="function"){
				LayerMsgBox.alert("表格data-menu-option设置异常，值应为一个function",2);
				return false;
			}
			var menuOptions = optionFunc(table);

			if(!isOk(menuOptions)){
				LayerMsgBox.alert("表格data-menu-option设置function 返回值不正确",2);
				return false;
			}
			this.processTableMenuFilterBoxCustom(table,menuOptions.menus);
			table.menuOptions=menuOptions;
			return true;
		},
		processTableMenuFilterBoxCustom:function(table,meuns){
			if(!isOk(meuns)){return;}
			var tempMenu,tpl,cus_html,pageSize;
			for(var i in meuns){
				tempMenu = meuns[i];
				if(tempMenu.custom && tempMenu.tpl){
					tpl=g(tempMenu.tpl).innerHTML;
					if(tpl){
						if(tempMenu.data){
							cus_html = juicer(tpl,tempMenu.data);
						}else{
							if(tempMenu.tpl == "filterbox_page_tpl"){
								pageSize=table.data("pagesize")||15;
								cus_html = juicer(tpl,{pageSize:pageSize});
							}else{
								cus_html = juicer(tpl,{});
							}
						}
						if(cus_html){
							tempMenu.html=cus_html;
						}
					}
				}
			}
		},
		processTableMenuAndEvent:function(table){
			var checkSuccess = this.processTableMenuOptions(table);
			if(!checkSuccess || table.willCopy){
				return false;
			}
			var theme = table.menuOptions.theme||"list";
			var tpl = this.getMenuTplByTheme(theme);
			var menuHtml=juicer(tpl,{menus:table.menuOptions.menus,width:table.menuOptions.width});
			if(!menuHtml){
				LayerMsgBox.alert("表格data-menu-option参数配置无效",2);
				return false;
			}
			var that=this;
			var menuObj=$(menuHtml);
			table.table_view.append(menuObj);
			table.menus=menuObj;
			initToolTip(table.menus);
			table.table_box.off("contextmenu").on("contextmenu",function(e){
				e.preventDefault();
				e.stopPropagation();
				FormDate.hide();
				if(table.currentMenuTd){
					table.currentMenuTd.removeClass("jb_current_menu_td");
				}
				table.currentMenuTd=null;
				that.processEditingTds(table);
				var cm_position=table.menuOptions.position||"td";
				var ctd=null;
				var inTd=false;
				var inThead=false;
				var inTbody=false;
				if(e.target.tagName=="TD"){
					ctd=$(e.target);
					inTd=true;
					inTbody = isOk(ctd.closest("tbody"));
					if(inTbody){
						ctd.addClass("jb_current_menu_td");
					}
				}else if(e.target.tagName=="TH"){
					ctd=$(e.target);
					inThead = isOk(ctd.closest("thead"));
				}
				var cm_pos=(cm_position=="td" && inTd)?ctd.offset():{top:e.clientY,left:e.clientX};
				var tableWidth = parseInt(table.menus.css("width"));
				if(cm_pos.left+tableWidth>jboltWindowWidth){
					if(table.menuOptions.theme =="list"){
						if(cm_position=="td"){
							cm_pos.left = cm_pos.left - tableWidth + parseInt(ctd.css("width"));
						}else{
							cm_pos.left = cm_pos.left - tableWidth;
						}
					}else{
						table.menus.removeClass("left").addClass("right");
						if(cm_position=="td"){
							cm_pos.left = cm_pos.left - tableWidth + parseInt(ctd.css("width"));
						}else{
							cm_pos.left = cm_pos.left - tableWidth;
						}
					}
				}
				var tableHeight = parseInt(table.menus.find(".dropdown-menu").css("height"));
				if(tableHeight==0){
					table.menus.show();
					tableHeight = parseInt(table.menus.find(".dropdown-menu").css("height"));
					table.menus.hide();
				}
				if(cm_pos.top + tableHeight > jboltWindowHeight){
					cm_pos.top = jboltWindowHeight - tableHeight-30;
				}
				that.closeMenu(table);
				table.menus.css(cm_pos).fadeIn(150);
				table.currentMenuTd=ctd;
				table.menuInThead=inThead;
				table.menuInTbody=inTbody;
				that.changeTdFocus(table,ctd);
				var ctr=null;
				if(isOk(ctd)){
					ctr=ctd.parent();
				}
				if(inTd){
					var dataIndex=ctr.data("index");
					var trJsonData=isOk(table.tableListDatas)?table.tableListDatas[dataIndex]:null;
					$.each(table.menuOptions.menus,function(i,item){
						if(item.visible && typeof(item.visible)=="function"){
							var menu=menuObj.find("a[data-menu-index='"+i+"']");
							if(isOk(menu)){
								menu.icon=menu.find("i");
								menu.text=menu.find("span.jbolt_table_menu_text");
								var visible=item.visible(table,ctr,ctd,trJsonData,menu,inThead,inTbody);
								if(visible){
									menu.show();
								}else{
									menu.hide();
								}
							}
						}
					});
				}else{
					$.each(table.menuOptions.menus,function(i,item){
						if(item.visible && typeof(item.visible)=="function"){
							var menu=menuObj.find("a[data-menu-index='"+i+"']");
							if(isOk(menu)){
								menu.icon=menu.find("i");
								menu.text=menu.find("span.jbolt_table_menu_text");
								var visible=item.visible(table,ctr,ctd,null,menu,inThead,inTbody);
								if(visible){
									menu.show();
								}else{
									menu.hide();
								}
							}
						}
					});
				}

				return false;
			});
			table.menus.find(".jbolttable_custombox").off("click").on("click",function(e){
				e.preventDefault();
				e.stopPropagation();
				return false;
			});
			table.menus.find(".jbolttable_custombox").off("dblclick").on("dblclick",function(e){
				e.preventDefault();
				e.stopPropagation();
				return false;
			});
			table.menus.find(".jbolttable_custombox").off("contextmenu").on("contextmenu",function(e){
				e.stopPropagation();
			});
			table.menus.find("a.jbolt_table_menu").off("click").on("click",function(e){
				e.preventDefault();
				e.stopPropagation();
				var ajt_index=$(this).data("menu-index");
				var ajt_menu=table.menuOptions.menus[ajt_index];
				if(isOk(ajt_menu)&&ajt_menu.func&&typeof(ajt_menu.func)=="function"){
					var ajt_td=table.currentMenuTd;
					var returnResult;
					if(ajt_td){
						var ajt_tr=ajt_td.parent();
						if(table.menuInTbody){
							var ajt_data_index=ajt_tr.data("index");
							var ajt_data=isOk(table.tableListDatas)?table.tableListDatas[ajt_data_index]:null;
							returnResult=ajt_menu.func(table,ajt_tr,ajt_td,ajt_data,table.menuInThead,table.menuInTbody);
						}else{
							returnResult=ajt_menu.func(table,ajt_tr,ajt_td,null,table.menuInThead,table.menuInTbody);
						}
					}else{
						returnResult=ajt_menu.func(table,null,null,null,table.menuInThead,table.menuInTbody);
					}
					if(typeof(returnResult)=="undefined" || (typeof(returnResult)=="boolean") && returnResult == true){
						that.closeMenu(table);
					}
				}
				return false;
			}).on("contextmenu",function(){return false;});

			table.menus.on("change",".jbolttable_filter_items select[name='column']",function(e){
				var columnSelect =$(this);
				var option = columnSelect.find("option:selected");
				var type = -1;
				if(isOk(option)){
					type = option.data("type");
				}
				var item = columnSelect.closest(".jbolttable_filter_item");
				item.find("[name='value']").removeClass("is_current");
				var valueInput;
				if(type){
					switch(type){
						case 1://字符串
							valueInput = item.find("input[name='value']");
							valueInput.attr("type","text").removeAttr("readonly").data("rule","required").attr("data-rule","required").val("");
							break;
						case 2://数字 int
							valueInput = item.find("input[name='value']");
							valueInput.attr("type","number").removeAttr("readonly").data("rule","int").attr("data-rule","int").val("");
							break;
						case 3://数字 小数
							valueInput = item.find("input[name='value']");
							valueInput.attr("type","number").removeAttr("readonly").data("rule","number").attr("data-rule","number").val("");
							break;
						case 4://日期
							valueInput = item.find("input[name='value']");
							valueInput.attr("type","text").data("type","date").attr("data-type","date").val("");
							FormDate.initDate(valueInput);
							break;
						case 5://日期+时间
							valueInput = item.find("input[name='value']");
							valueInput.attr("type","text").data("type","datetime").attr("data-type","datetime").val("");
							FormDate.initDate(valueInput);
							break;
						case 6://时间
							valueInput = item.find("input[name='value']");
							valueInput.attr("type","text").data("type","time").attr("data-type","time").val("");
							FormDate.initDate(valueInput);
							break;
						case 7://boolean
							valueInput = item.find("img[name='value']");
							break;
					}
				}else{
					valueInput = item.find("input[name='value']");
					valueInput.attr("type","text").removeAttr("readonly").data("rule","required").attr("data-rule","required").val("");
				}

				valueInput.addClass("is_current");
			});
			jboltBody.on("click",function(e){
				that.closeMenu(table);
			});
			jboltBody.on("dblclick",function(e){
				that.closeMenu(table);
			});
			jboltBody.on("contextmenu",function(e){
				that.closeMenu(table);
			});
			return true;
		},
		processTreeTableEvent:function(table){
			var treeTable=table.data("treetable");
			if(typeof(treeTable)=="undefined" || treeTable.toString() == ''){
				table.isTreeTable=false;
				return false;
			}
			table.isTreeTable=true;
			var that=this,pfb,tr,isExpand,tempTable,isMainTable,otherTable,otherTr,otherId;
			table.table_box.on("click","table>tbody>tr.parent.hasItems>td>.parent_flag_box",function(e){
				e.preventDefault();
				e.stopPropagation();
				pfb=$(this);
				tr=pfb.closest("tr");
				otherId=tr.data("id");
				tempTable=tr.closest("table");
				isMainTable=tempTable.hasClass("jbolt_main_table");
				isExpand=tr.hasClass("expand");
				if(isMainTable){
					if(isExpand){
						//闭合自己以及所有下级
						that.collapse(table,tr);
						//如果存在fixedTable
						if(table.fixedColumnTables){
							table.fixedColumnTables.each(function(){
								otherTable=$(this);
								otherTr=otherTable.find("tbody>tr[data-id='"+otherId+"']");
								if(isOk(otherTr)){
									that.collapse(otherTable,otherTr);
								}
							});
						}
					}else{
						//展开自己
						that.expand(table,tr);
						if(table.fixedColumnTables){
							table.fixedColumnTables.each(function(){
								otherTable=$(this);
								otherTr=otherTable.find("tbody>tr[data-id='"+otherId+"']");
								if(isOk(otherTr)){
									that.expand(otherTable,otherTr);
								}
							});
						}
					}
				}else{
					otherTr=table.tbody.find("tr[data-id='"+otherId+"']");
					if(isExpand){
						//闭合自己以及所有下级
						that.collapse(table,otherTr);
						if(table.fixedColumnTables){
							table.fixedColumnTables.each(function(){
								otherTable=$(this);
								otherTr=otherTable.find("tbody>tr[data-id='"+otherId+"']");
								if(isOk(otherTr)){
									that.collapse(otherTable,otherTr);
								}
							});
						}
					}else{
						//展开自己
						that.expand(table,otherTr);
						if(table.fixedColumnTables){
							table.fixedColumnTables.each(function(){
								otherTable=$(this);
								otherTr=otherTable.find("tbody>tr[data-id='"+otherId+"']");
								if(isOk(otherTr)){
									that.expand(otherTable,otherTr);
								}
							});
						}
					}
				}

				return false;
			});
		},

		processTableRowClickActiveAndHandler:function(table){
			//处理row click active handler
			var rowClickActive=table.data("row-click-active");
			var rowDblClickActive=table.data("row-dblclick-active");
			var rowClickHandler=table.data("row-click-handler");
			var rowDblClickHandler=table.data("row-dblclick-handler");
			var that=this;
			if(rowClickActive||rowClickHandler){
				var tempTr,trIndex,trId,jsonData;
				table.table_box.on("click","table>tbody>tr",function(e){
					if(e.target.tagName=="A"||e.target.tagName=="BUTTON"||e.target.parentNode.tagName=="A"||e.target.parentNode.tagName=="BUTTON"){
						e.preventDefault();
					}else{

						tempTr=$(this);
						if(rowClickHandler&&isOk(table.currentChooseActiveTr)&&isOk(table.extraColumnForm)){
							//存在右侧辅助录入区域
							that.saveFormToTableCurrentActiveTr(table,table.extraColumnForm);
						}
						table.currentChooseActiveTr=tempTr;
						trIndex=tempTr.index();
						if(rowClickActive){
							// table.table_box.find("table>tbody>tr.active").removeClass("active");
							// table.table_box.find("table>tbody>tr:nth-child("+(trIndex+1)+")").addClass("active");
							table.table_box.find("table>tbody>tr.active").removeClass("active");
							var tr = table.table_box.find("table>tbody>tr:nth-child("+(trIndex+1)+")");
							tr.addClass("active");
							table.activeTrIndex = trIndex;
							table.activeTrId    = tr.data("id");
						}
						if(rowClickHandler){
							var exe_handler=eval(rowClickHandler);
							if(exe_handler&&typeof(exe_handler)=="function"){
								trId=tempTr.data("id");
								jsonData=that.getRowJsonData(table,tempTr);
								exe_handler(table,tempTr,trId,deepClone(jsonData));
							}
						}

					}
				});
			}


			if(rowDblClickActive||rowDblClickHandler){
				var tempTr1,trIndex1,trId1,jsonData1;
				table.table_box.on("dblclick","table>tbody>tr",function(e){
					if(e.target.tagName=="A"||e.target.tagName=="BUTTON"||e.target.parentNode.tagName=="A"||e.target.parentNode.tagName=="BUTTON"){
						e.preventDefault();
					}else{

						tempTr1=$(this);
						if(rowDblClickActive&&isOk(table.currentChooseActiveTr)&&isOk(table.extraColumnForm)){
							//存在右侧辅助录入区域
							that.saveFormToTableCurrentActiveTr(table,table.extraColumnForm);
						}
						table.currentChooseActiveTr=tempTr1;
						trIndex1=tempTr1.index();
						if(rowDblClickActive){
							table.table_box.find("table>tbody>tr.active").removeClass("active");
							table.table_box.find("table>tbody>tr:nth-child("+(trIndex1+1)+")").addClass("active");
						}
						if(rowDblClickHandler){
							if(typeof(rowDblClickHandler) == "string" && rowDblClickHandler=="edit"){
								var editBtn = tempTr1.find("td [data-role='edit']:eq(0)");
								if(!isOk(editBtn)){
									editBtn = tempTr1.find("td .jbolt_table_editbtn:eq(0)");
									if(!isOk(editBtn)){
										if(table.prependColumnType){
											if(table.prependColumnType == "checkbox"){
												CheckboxUtil.uncheckAll("jboltTableCheckbox",table.table_box);
											}
											var trCheckbox = tempTr1.find("td>.jbolt_table_checkbox>input[type='checkbox'][name='jboltTableCheckbox']");
											if(isOk(trCheckbox)){
												CheckboxUtil.checkIt(trCheckbox);
											}
											if(table.toolbar){
												editBtn = table.toolbar.find("td [data-role='edit']:eq(0)");
												if(!isOk(editBtn)){
													editBtn = table.toolbar.find(".jbolt_table_editbtn:eq(0)");
												}
											}
										}
									}
								}
								if(isOk(editBtn)){
									editBtn.trigger("click");
								}else{
									LayerMsgBox.alert("请指定编辑按钮:data-role='edit'或者.jbolt_table_editbtn",2);
								}
							}else{
								var exe_handler=eval(rowDblClickHandler);
								if(exe_handler&&typeof(exe_handler)=="function"){
									trId1=tempTr1.data("id");
									jsonData1=that.getRowJsonData(table,tempTr1);
									exe_handler(table,tempTr1,trId1,deepClone(jsonData1));
								}
							}
						}

					}
				});
			}
		},
		processColumnprependCheckedByRowClick:function(table){
			if(!table.clickRowAndChecked){return false;}
			//处理columnprepend row点击选中
			table.table_box.on("click","table>tbody>tr",function(e){
				var tagName=e.target.tagName;
				if(tagName=="TD" || (tagName=="DIV" && (e.target.className=="jbolt_table_radio"||e.target.className=="jbolt_table_checkbox"))){
					e.preventDefault();
					e.stopPropagation();
					var tr=$(this);
					var trIndex=tr.data("index");
					if(table.prependColumnType=="checkbox"){
						var chk=table.tbody.find("tr[data-index='"+trIndex+"']>td>div.jbolt_table_checkbox>input[type='checkbox'][name='jboltTableCheckbox']");
						if(isOk(chk)){
							chk.click();
						}
					}else if(table.prependColumnType=="radio"){
						var rdo=table.tbody.find("tr[data-index='"+trIndex+"']>td>div.jbolt_table_radio>input[type='radio'][name='jboltTableRadio']");
						if(isOk(rdo)){
							rdo.click();
						}
					}
					return false;
				}
			});

		},
		processAjaxSuccessCallback:function(table){
			//处理ajax每次读取加载完事件
			if(table.isAjax){
				var handler=table.data("ajax-success-handler");
				if(handler){
					var exe_handler=eval(handler);
					if(exe_handler&&typeof(exe_handler)=="function"){
						exe_handler(table);
					}
				}
				//处理左右下box里的非自动初始化的表格初始化
				this.processLeftRightFootBoxTableInit(table);
			}

		},
		processInitCallback:function(table){
			//处理首次加载处理完成回调
			var handler=table.data("init-handler");
			if(handler){
				var exe_handler=eval(handler);
				if(exe_handler&&typeof(exe_handler)=="function"){
					exe_handler(table);
				}
			}
		},
		processFixedColumnScrollEvent:function(table){
			//处理fixedColumn上的滚轮事件
			table.table_box.on("mousewheel",".jbolt_table_fixed",function(e){
				e.preventDefault();
				e.stopPropagation();
				if(table.table_body.data("mousewheel_ing")){return false;}
				table.table_body.data("mousewheel_ing",true);
				var topScroll=table.table_body.scrollTop();
				topScroll=topScroll-e.originalEvent.wheelDelta;
				table.table_body.animate({"scrollTop":topScroll},200,function(){
					table.table_body.data("mousewheel_ing",false);
				});
				return false;
			});
		},
		checkTreeTableParents:function(table,clicktable,theadCheckboxs,isMainTable,isFixedTable,tr,colIndex){
			var that=this;
			var level=tr.data("level");
			var pid=tr.data("pid");
			if(level>1&&pid>0){
				if(isMainTable&&!isFixedTable){
					//说明还有上级需要处理
					var parentCheckbox=table.tbody.find("tr[data-id='"+pid+"'][data-level='"+(level-1)+"']>td[data-col-index='"+colIndex+"']>.jbolt_table_checkbox>input[type='checkbox']");
					if(isOk(parentCheckbox)){
						CheckboxUtil.checkIt(parentCheckbox);
					}

					//存在fixedTable
					if(isOk(table.fixedColumnTables)){
						var fixedCheckbox=table.fixedColumnTables.find("tbody>tr[data-id='"+pid+"'][data-level='"+(level-1)+"']>td[data-col-index='"+colIndex+"']>.jbolt_table_checkbox>input[type='checkbox']");
						if(isOk(fixedCheckbox)){
							CheckboxUtil.checkIt(fixedCheckbox);
						}
					}
					if(isOk(parentCheckbox)){
						parentCheckbox.each(function(){
							that.checkTreeTableParents(table,clicktable,theadCheckboxs,isMainTable,isFixedTable,$(this).closest("tr"),colIndex);
						});
					}

				}else if(!isMainTable&&isFixedTable){
					//说明还有上级需要处理
					var parentCheckbox=clicktable.find("tbody>tr[data-id='"+pid+"'][data-level='"+(level-1)+"']>td[data-col-index='"+colIndex+"']>.jbolt_table_checkbox>input[type='checkbox']");
					if(isOk(parentCheckbox)){
						CheckboxUtil.checkIt(parentCheckbox);
					}
					var mainparentCheckbox=table.tbody.find("tr[data-id='"+pid+"'][data-level='"+(level-1)+"']>td[data-col-index='"+colIndex+"']>.jbolt_table_checkbox>input[type='checkbox']");
					if(isOk(mainparentCheckbox)){
						CheckboxUtil.checkIt(mainparentCheckbox);
					}

					if(isOk(mainparentCheckbox)){
						mainparentCheckbox.each(function(){
							that.checkTreeTableParents(table,clicktable,theadCheckboxs,isMainTable,isFixedTable,$(this).closest("tr"),colIndex);
						});
					}
				}
			}
		},
		checkTreeTableSons:function(table,clicktable,theadCheckboxs,isMainTable,isFixedTable,tr,colIndex){
			var that=this;
			var level=tr.data("level");
			var pid=tr.data("id");
			if(isMainTable&&!isFixedTable){
				//说明还有上级需要处理
				var parentCheckbox=table.tbody.find("tr[data-pid='"+pid+"'][data-level='"+(level+1)+"']>td[data-col-index='"+colIndex+"']>.jbolt_table_checkbox>input[type='checkbox']");
				if(isOk(parentCheckbox)){
					CheckboxUtil.checkIt(parentCheckbox);
				}

				//存在fixedTable
				if(isOk(table.fixedColumnTables)){
					var fixedCheckbox=table.fixedColumnTables.find("tbody>tr[data-pid='"+pid+"'][data-level='"+(level+1)+"']>td[data-col-index='"+colIndex+"']>.jbolt_table_checkbox>input[type='checkbox']");
					if(isOk(fixedCheckbox)){
						CheckboxUtil.checkIt(fixedCheckbox);
					}
				}
				if(isOk(parentCheckbox)){
					parentCheckbox.each(function(){
						that.checkTreeTableSons(table,clicktable,theadCheckboxs,isMainTable,isFixedTable,$(this).closest("tr"),colIndex);
					});
				}

			}else if(!isMainTable&&isFixedTable){
				//说明还有上级需要处理
				var parentCheckbox=clicktable.find("tbody>tr[data-pid='"+pid+"'][data-level='"+(level+1)+"']>td[data-col-index='"+colIndex+"']>.jbolt_table_checkbox>input[type='checkbox']");
				if(isOk(parentCheckbox)){
					CheckboxUtil.checkIt(parentCheckbox);
				}
				var mainparentCheckbox=table.tbody.find("tr[data-pid='"+pid+"'][data-level='"+(level+1)+"']>td[data-col-index='"+colIndex+"']>.jbolt_table_checkbox>input[type='checkbox']");
				if(isOk(mainparentCheckbox)){
					CheckboxUtil.checkIt(mainparentCheckbox);
				}

				if(isOk(mainparentCheckbox)){
					mainparentCheckbox.each(function(){
						that.checkTreeTableSons(table,clicktable,theadCheckboxs,isMainTable,isFixedTable,$(this).closest("tr"),colIndex);
					});
				}
			}
		},
		uncheckTreeTableSons:function(table,clicktable,theadCheckboxs,isMainTable,isFixedTable,tr,colIndex){
			var level=tr.data("level");
			var pid=tr.data("id");
			var that=this;
			if(isMainTable&&!isFixedTable){
				//下需要处理
				var sonCheckbox=table.tbody.find("tr[data-pid='"+ pid +"'][data-level='"+(level+1)+"']>td[data-col-index='"+colIndex+"']>.jbolt_table_checkbox>input[type='checkbox']");
				if(isOk(sonCheckbox)){
					CheckboxUtil.uncheckIt(sonCheckbox);
				}

				//存在fixedTable
				if(isOk(table.fixedColumnTables)){
					var fixedCheckbox=table.fixedColumnTables.find("tbody>tr[data-pid='"+pid+"'][data-level='"+(level+1)+"']>td[data-col-index='"+colIndex+"']>.jbolt_table_checkbox>input[type='checkbox']");
					if(isOk(fixedCheckbox)){
						CheckboxUtil.uncheckIt(fixedCheckbox);
					}
				}

				if(isOk(sonCheckbox)){
					sonCheckbox.each(function(){
						that.uncheckTreeTableSons(table,clicktable,theadCheckboxs,isMainTable,isFixedTable,$(this).closest("tr"),colIndex);
					});
				}
			}else if(!isMainTable&&isFixedTable){
				//下需要处理
				var sonCheckbox=clicktable.find("tbody>tr[data-pid='"+ pid +"'][data-level='"+(level+1)+"']>td[data-col-index='"+colIndex+"']>.jbolt_table_checkbox>input[type='checkbox']");
				if(isOk(sonCheckbox)){
					CheckboxUtil.uncheckIt(sonCheckbox);
				}
				var mainsonCheckbox=table.tbody.find("tr[data-pid='"+ pid +"'][data-level='"+(level+1)+"']>td[data-col-index='"+colIndex+"']>.jbolt_table_checkbox>input[type='checkbox']");
				if(isOk(mainsonCheckbox)){
					CheckboxUtil.uncheckIt(mainsonCheckbox);
				}

				if(isOk(mainsonCheckbox)){
					mainsonCheckbox.each(function(){
						that.uncheckTreeTableSons(table,clicktable,theadCheckboxs,isMainTable,isFixedTable,$(this).closest("tr"),colIndex);
					});
				}

			}
		},
		/**
		 * 处理columnprepend组件事件
		 */
		processTableColumnPrependEvent:function(table){
//				var ajax=table.data("ajax");
			var hasColumnPrepend=table.data("column-prepend");
			//ajax加载table 但是没设置这个 就直接略过
//				if(ajax&&!hasColumnPrepend){
			if(!hasColumnPrepend){
				return false;
			}
			var tableCheckedChangeHandler=table.data("checked-change-handler");
			var exe_checked_handler=null;
			if(tableCheckedChangeHandler){
				var exe_handler=eval(tableCheckedChangeHandler);
				if(exe_handler&&typeof(exe_handler)=="function"){
					exe_checked_handler = exe_handler;
				}
			}
			var rowClickActive=table.data("row-click-active");
			var rowClickHandler=table.data("row-click-handler");
			/*if(!ajax){
					var theadPrepend=table.find("thead>tr>th>.jbolt_table_checkbox,thead>tr>th>.jbolt_table_radio");
					if(!isOk(theadPrepend)){
						return false;
					}
				}*/
			var that=this;
			var exeChangeHandler=function(isAll,ele){
				if(exe_checked_handler){
					var tdOrTh = isAll?ele.closest("th"):ele.closest("td");
					var tr = tdOrTh.parent();
					var trJsonData = isAll?null:table.tableListDatas[tr.data("index")];
					exe_checked_handler(isAll,table,tr,tdOrTh,ele,trJsonData);
				}
			}
			//绑定change后的样式变更
			table.table_box.on("change",".jbolt_table_checkbox>input[type='checkbox']",function(e){
				var checkedEle=$(this);

				if(this.checked){
					checkedEle.parent().addClass("checked");
				}else{
					checkedEle.parent().removeClass("checked");
				}
			});
			//绑定thead全选和全不选
			table.table_box.on("click","table>thead>tr>th>.jbolt_table_checkbox>input[type='checkbox']",function(e){
				var name=this.name;
				if(this.checked){
					CheckboxUtil.checkAll(name,table.table_box);
				}else{
					CheckboxUtil.uncheckAll(name,table.table_box);
				}
				var checkedEle=$(this);
				exeChangeHandler(true,checkedEle);

				var columnprependactive = table.data("column-prepend-active");
				if(columnprependactive){
					if(this.checked){
						table.table_box.find("table>tbody>tr").addClass("active");
					}else{
						table.table_box.find("table>tbody>tr").removeClass("active");
					}
				}
				if(table.keepSelectedItemsEnable){
					//如果开启了选中数据渲染的开关 就得执行处理handler
					that.processChangeCurrentPageAllSelectedItems(table,this.checked);
				}
			});
			//绑定thead radio点击
			table.table_box.on("click","table>thead>tr>th>.jbolt_table_radio>input[type='radio']",function(e){
				var jbolt_table_radio=$(this).parent();
				var colIndex=jbolt_table_radio.closest("th").data("col-index");
				var name=this.name;
				if(this.checked){
					var mainRadio,fixedHeaderRadio,fixedRadio,mainBodyRadio,fixedBodyRadio;
					if(name=="jboltTableRadio"){
						//点击主表表头radio
						fixedHeaderRadio=table.table_box.find("table>thead>tr>th[data-col-index='"+colIndex+"']>.jbolt_table_radio>input[type='radio'][name='jboltTableRadio_fixedheader']");
						fixedRadio=table.table_box.find("table>thead>tr>th[data-col-index='"+colIndex+"']>.jbolt_table_radio>input[type='radio'][name='jboltTableRadio_fixed']");
						mainBodyRadio=table.tbody.find("tr>th[data-col-index='"+colIndex+"']>.jbolt_table_radio>input[type='radio'][name='jboltTableRadio']");
						fixedBodyRadio=table.fixedColumnTables.find("tbody>tr>th[data-col-index='"+colIndex+"']>.jbolt_table_radio>input[type='radio'][name='jboltTableRadio_fixed']");
					}else if(name=="jboltTableRadio_fixed"){
						//点击fixed表头radio
						fixedHeaderRadio=table.table_box.find("table>thead>tr>th[data-col-index='"+colIndex+"']>.jbolt_table_radio>input[type='radio'][name='jboltTableRadio_fixedheader']");
						mainRadio=table.find("thead>tr>th[data-col-index='"+colIndex+"']>.jbolt_table_radio>input[type='radio'][name='jboltTableRadio']");
						mainBodyRadio=table.tbody.find("tr>th[data-col-index='"+colIndex+"']>.jbolt_table_radio>input[type='radio'][name='jboltTableRadio']");
						fixedBodyRadio=table.fixedColumnTables.find("tbody>tr>th[data-col-index='"+colIndex+"']>.jbolt_table_radio>input[type='radio'][name='jboltTableRadio_fixed']");
					}else if(name=="jboltTableRadio_fixedheader"){
						//点击主表fixed表头radio
						mainRadio=table.find("thead>tr>th[data-col-index='"+colIndex+"']>.jbolt_table_radio>input[type='radio'][name='jboltTableRadio']");
						fixedRadio=table.table_box.find("table>thead>tr>th[data-col-index='"+colIndex+"']>.jbolt_table_radio>input[type='radio'][name='jboltTableRadio_fixed']");
						mainBodyRadio=table.tbody.find("tr>th[data-col-index='"+colIndex+"']>.jbolt_table_radio>input[type='radio'][name='jboltTableRadio']");
						fixedBodyRadio=table.fixedColumnTables.find("tbody>tr>th[data-col-index='"+colIndex+"']>.jbolt_table_radio>input[type='radio'][name='jboltTableRadio_fixed']");
					}

					if(isOk(fixedHeaderRadio)){
						fixedHeaderRadio[0].checked=true;
						fixedHeaderRadio.attr("checked","checked");
					}

					if(isOk(fixedRadio)){
						fixedRadio[0].checked=true;
						fixedRadio.attr("checked","checked");
					}

					if(isOk(mainRadio)){
						mainRadio[0].checked=true;
						mainRadio.attr("checked","checked");
					}

					if(isOk(mainBodyRadio)){
						mainBodyRadio.each(function(){
							this.checked=false;
						});
						mainBodyRadio.removeAttr("checked");
					}

					if(isOk(fixedBodyRadio)){
						fixedBodyRadio.each(function(){
							this.checked=false;
						});
						fixedBodyRadio.removeAttr("checked");
					}
				}

				var checkedEle=$(this);
				exeChangeHandler(true,checkedEle);

				var columnprependactive = table.data("column-prepend-active");
				if(columnprependactive){
					if(this.checked){
						table.table_box.find("table>tbody>tr").removeClass("active");
					}
				}


			});
			//绑定tbody中的checkbox
			table.table_box.on("click","table>tbody>tr>td>.jbolt_table_checkbox>input[type='checkbox']",function(e){
				if(rowClickActive||rowClickHandler||table.clickRowAndChecked){
					e.stopPropagation();
				}

				var me=$(this);
				var jbolt_table_checkbox=me.parent();
				var tr = jbolt_table_checkbox.closest("tr");
				var rowIndex=tr.data("index");
				var colIndex=jbolt_table_checkbox.closest("td").data("col-index");
				var ptable=me.closest("table.jbolt_main_table");
				var isMainTable=isOk(ptable);
				var pfixed=me.closest(".jbolt_table_fixed");
				var isFixedTable=isOk(pfixed);
				//thead里的checkbox 指定列
				var theadCheckboxs=table.table_box.find("table>thead>tr>th[data-col-index='"+colIndex+"']>.jbolt_table_checkbox>input[type='checkbox']");
				//如果选中了
				if(this.checked){
					me.checkAll = that.isCheckboxCheckedAll(table);
					if(me.checkAll){
						CheckboxUtil.checkIt(theadCheckboxs);
					}
				}else{//没选中就去掉全选
					CheckboxUtil.uncheckIt(theadCheckboxs);
					me.checkAll=false;
				}
				//如果是mainTable不是悬浮table
				if(isMainTable&&!isFixedTable){
					//存在fixedTable
					if(isOk(table.fixedColumnTables)){
						var fixedCheckbox=table.fixedColumnTables.find("tbody>tr[data-index='"+rowIndex+"']>td[data-col-index='"+colIndex+"']>.jbolt_table_checkbox>input[type='checkbox']");
						if(isOk(fixedCheckbox)){
							if(this.checked){
								CheckboxUtil.checkIt(fixedCheckbox);
							}else{//没选中就去掉全选
								CheckboxUtil.uncheckIt(fixedCheckbox);
							}
						}
					}
				}else
					//如果是悬浮table
				if(isFixedTable&&!isMainTable){
					var mainTableCheckbox=table.tbody.find("tr[data-index='"+rowIndex+"']>td[data-col-index='"+colIndex+"']>.jbolt_table_checkbox>input[type='checkbox']");
					if(isOk(mainTableCheckbox)){
						if(this.checked){
							CheckboxUtil.checkIt(mainTableCheckbox);
							if(that.isCheckboxCheckedAll(table)){
								CheckboxUtil.checkIt(theadCheckboxs);
							}
						}else{//没选中就去掉全选
							CheckboxUtil.uncheckIt(mainTableCheckbox);
						}

					}

				}


				if(table.linkParent){
					if(this.checked){
						that.checkTreeTableParents(table,ptable,theadCheckboxs,isMainTable,isFixedTable,tr,colIndex);
					}
				}
				if(table.linkSon){
					if(this.checked){
						that.checkTreeTableSons(table,ptable,theadCheckboxs,isMainTable,isFixedTable,tr,colIndex);
					}else{
						that.uncheckTreeTableSons(table,ptable,theadCheckboxs,isMainTable,isFixedTable,tr,colIndex);
					}
				}


				exeChangeHandler(false,me);

				var columnprependactive = table.data("column-prepend-active");
				if(columnprependactive){
					if(this.checked){
						table.table_box.find("table>tbody>tr[data-index='"+rowIndex+"']").addClass("active");
					}else{
						table.table_box.find("table>tbody>tr[data-index='"+rowIndex+"']").removeClass("active");
					}
				}
				if(table.keepSelectedItemsEnable){
					//如果开启了选中数据渲染的开关 就得执行处理handler
					that.processChangeSelectedItems(table,tr,rowIndex,me,this.checked);
				}
			});
			//绑定tbody中的radio
			table.table_box.on("click","table>tbody>tr>td>.jbolt_table_radio>input[type='radio']",function(e){
				if(rowClickActive||rowClickHandler||table.clickRowAndChecked){
					e.stopPropagation();
				}
				var me=$(this);
				var jbolt_table_radio=me.parent();
				var tr = jbolt_table_radio.closest("tr");
				var rowIndex=tr.data("index");
				var colIndex=jbolt_table_radio.closest("td").data("col-index");
				var ptable=me.closest("table.jbolt_main_table");
				var isMainTable=isOk(ptable);
				var pfixed=me.closest(".jbolt_table_fixed");
				var isFixedTable=isOk(pfixed);



				var fixedHeaderRadio=table.table_box.find("table>thead>tr>th[data-col-index='"+colIndex+"']>.jbolt_table_radio>input[type='radio'][name='jboltTableRadio_fixedheader']");
				if(isOk(fixedHeaderRadio)){
					fixedHeaderRadio[0].checked=false;
					fixedHeaderRadio.removeAttr("checked");
				}
				//如果是mainTable不是悬浮table
				if(isMainTable&&!isFixedTable){
					//存在fixedTable
					if(isOk(table.fixedColumnTables)){
						var fixedRadio=table.fixedColumnTables.find("tbody>tr[data-index='"+rowIndex+"']>td[data-col-index='"+colIndex+"']>.jbolt_table_radio>input[type='radio']");
						if(isOk(fixedRadio)){
							fixedRadio[0].checked=true;
							fixedRadio.attr("checked","checked");
						}
					}

				}else if(isFixedTable&&!isMainTable){
					var mainTableRadio=table.tbody.find("tr[data-index='"+rowIndex+"']>td[data-col-index='"+colIndex+"']>.jbolt_table_radio>input[type='radio']");
					if(isOk(mainTableRadio)){
						mainTableRadio[0].checked=true;
						mainTableRadio.attr("checked","checked");
					}

				}
				exeChangeHandler(false,me);
				var columnprependactive = table.data("column-prepend-active");
				if(columnprependactive) {
					table.table_box.find("table>tbody>tr.active").removeClass("active");
					table.table_box.find("table>tbody>tr[data-index='"+rowIndex+"']").addClass("active");
				}
			});


		},
		processChangeCurrentPageAllSelectedItems:function(table,checked){
			if(table.isEmpty){return;}
			var that=this;
			$.each(table.tableListDatas,function(i,trJsonData){
				if(checked){
					that.addToSelectedItemsBox(table,trJsonData);
				}else{
					var value = trJsonData[table.selectedItemValueAttr]||trJsonData[table.selectedItemValueAttr.toUpperCase()];
					if(typeof(value)=="undefined"){
						value="";
					}
					that.removeFromSelectedItemsBox(table,value+"");
				}
			});
		},
		removeFromKeepSelectedItemsBox:function(table,removeId){
			this.removeFromSelectedItemsBox(table,removeId+"");
			var mainTableCheckbox=table.table_box.find("tr[data-id='"+removeId+"']>td>.jbolt_table_checkbox>input[type='checkbox']");
			if(isOk(mainTableCheckbox)) {
				CheckboxUtil.uncheckIt(mainTableCheckbox);
			}
		},
		processChangeSelectedItems:function(table,tr,dataIndex,checkbox,checked){
			if(table.isEmpty){return;}
			var trJsonData = table.tableListDatas[dataIndex];
			if(checked){
				this.addToSelectedItemsBox(table,trJsonData);
			}else{
				this.removeFromSelectedItemsBox(table,tr.data("id")+"");
			}
		},
		//添加选中的数据到itemsbox
		addToSelectedItemsBox:function(table,trJsonData){
			var value = trJsonData[table.selectedItemValueAttr]||trJsonData[table.selectedItemValueAttr.toUpperCase()];
			if(typeof(value)=="undefined"){
				value="";
			}
			var text  = trJsonData[table.selectedItemTextAttr]||trJsonData[table.selectedItemTextAttr.toUpperCase()];
			if(typeof(text)=="undefined"){
				LayerMsgBox.alert("data-selected-item-text-attr配置有误",2);
				return;
			}
			if(typeof(table.selectedItemsIds)=="undefined"){
				table.selectedItemsIds = [];
			}
			if(typeof(table.selectedItemsDatas)=="undefined"){
				table.selectedItemsDatas = [];
			}
			var exist = isOk(table.selectedItemsBox.find(".item[data-id='"+value+"']"));
			if(!exist){
				if(table.selectedItemsIds.length==0){
					table.selectedItemsIds.push(value);
					table.selectedItemsDatas.push(trJsonData);
					var html = juicer(table.selectedItemsTpl,{datas:[trJsonData]});
					var item = $(html);
					table.selectedItemsBox.append(item);
					initToolTip(item);
				}else{
					if(!table.selectedItemsIds.includes(value)){
						table.selectedItemsIds.push(value);
						table.selectedItemsDatas.push(trJsonData);
						var html = juicer(table.selectedItemsTpl,{datas:[trJsonData]});
						var item = $(html);
						table.selectedItemsBox.append(item);
						initToolTip(item);
					}
				}
			}
			if(table.selectedItemsTotal){
				table.selectedItemsTotal.text(table.selectedItemsIds.length);
			}
		},
		//从itemsbox移除指定数据
		removeFromSelectedItemsBox:function(table,id){
			if(typeof(table.selectedItemsIds)=="undefined"){
				table.selectedItemsIds = [];
			}
			if(typeof(table.selectedItemsDatas)=="undefined"){
				table.selectedItemsDatas = [];
			}
			var item = table.selectedItemsBox.find(".item[data-id='"+id+"']");
			var exist = isOk(item);
			if(exist){
				item.remove();
			}
			var removeIndex = JBoltArrayUtil.remove(table.selectedItemsIds,id);
			if(removeIndex!=-1){
				JBoltArrayUtil.removeByIndex(table.selectedItemsDatas,removeIndex,1);
			}
			if(table.selectedItemsTotal){
				table.selectedItemsTotal.text(table.selectedItemsIds.length);
			}
		},
		getCheckboxCheckedCount:function(table){
			return table.tbody.find("tr>td>.jbolt_table_checkbox>input[type='checkbox'][name='jboltTableCheckbox']:checked").length;
		},
		getRadioCheckedCount:function(table){
			return table.tbody.find("tr>td>.jbolt_table_radio>input[type='radio'][name='jboltTableRadio']:checked").length;
		},
		isCheckboxCheckedAll:function(table){
			return table.tbody.find("tr").length==this.getCheckboxCheckedCount(table);
		},
		isCheckboxCheckedOne:function(table){
			return this.getCheckboxCheckedCount(table)==1;
		},
		isCheckboxCheckedNone:function(table){
			return this.getCheckboxCheckedCount(table)==0;
		},
		isRadioCheckedOne:function(table){
			return this.getRadioCheckedCount(table)==1;
		},
		isRadioCheckedNone:function(table){
			return this.getRadioCheckedCount(table)==0;
		},
		isCheckboxCheckedMulti:function(table){
			return this.getCheckboxCheckedCount(table)>1;
		},
		processTableThResizeAndSortStyleEvent:function(e){
			if(e.target.tagName=="TH"){
				//说明鼠标在TH上了
				var th=$(e.target);
				th.data("can-sort",false).attr("data-can-sort",false);
				var tr=th.parent(),resizing=th.data("resizing");
				var width=th.outerWidth()-10,left=th.offset().left;
				var newWidth=e.clientX-left;
				if(th.hasClass("resize_col") && th[0].hasAttribute("data-col-index")){
					if(!resizing){
						if(newWidth>=width){
							th.data("can-resize",true).attr("data-can-resize",true);
							if(tr.css("cursor")!="col-resize"){
								tr.css("cursor","col-resize");
							}
						}else{
							th.data("can-resize",false).attr("data-can-resize",false);
							if(th.hasClass("sort_col")){
								tr.css("cursor","pointer");
								th.data("can-sort",true).attr("data-can-sort",true);
							}else{
								if(tr.css("cursor")!="auto"){
									tr.css("cursor","auto");
								}
							}
						}
					}
				}else if(th.hasClass("sort_col")){
					tr.css("cursor","pointer");
					th.data("can-sort",true).attr("data-can-sort",true);
				}else{
					tr.css("cursor","auto");
					th.data("can-resize",false).attr("data-can-resize",false).data("can-sort",false).attr("data-can-sort",false);
				}

			}
		},
		//table四周box调整
		processTableBoxResizeEvent:function(table){
			//指定位置按下之后 处理leftbox的启动状态
			table.leftbox.on("mousedown",".jb_header",function(e){
				if(!table.leftbox.canResize){
					var left = table.leftbox.offset().left;
					var width = table.leftbox.width();
					var right = left+width;
					var newLeft = right-10;
					var currentMouseLeft = e.clientX;
					if(currentMouseLeft>=newLeft && currentMouseLeft<=right){
						jboltBody.addClass("noselect");
						jboltBody.css("cursor","col-resize");
						table.leftbox.canResize = true;
						table.leftbox.css("border-right","1px solid black");
						table.leftbox.css("width",(currentMouseLeft-left+1)+"px");
					}
				}else{
					jboltBody.css("cursor","auto");
				}
			});
			table.leftbox.on("mousemove",".jb_header",function(e){
					var left = table.leftbox.offset().left;
					var width = table.leftbox.width();
					var right = left+width;
					var newLeft = right-4;
					var currentMouseLeft = e.clientX;
					if(currentMouseLeft>=newLeft && currentMouseLeft<=right){
						jboltBody.css("cursor","col-resize");
						if(table.leftbox.canResize){
							jboltBody.addClass("noselect");
							table.leftbox.css("width",(currentMouseLeft-left+1)+"px");
						}
					}
			});

			table.leftbox.on("mouseleave",".jb_header",function(e){
				if(!table.leftbox.canResize){
					jboltBody.removeClass("noselect");
					jboltBody.css("cursor","auto");
				}
			});

			jboltBody.on("mousemove",function(e){
				if(table.leftbox.canResize){
					var left = table.leftbox.offset().left;
					var width = table.leftbox.width();
					var right = left+width;
					var newLeft = right-4;
					var currentMouseLeft = e.clientX;
					if(currentMouseLeft>=left){
						jboltBody.css("cursor","col-resize");
						jboltBody.addClass("noselect");
						table.leftbox.css("width",(currentMouseLeft-left+1)+"px");
					}
				}
			});

			jboltBody.on("mouseup",function(e){
				if(table.leftbox.canResize){
					jboltBody.removeClass("noselect");
					jboltBody.css("cursor","auto");
					table.leftbox.canResize = false;
					table.leftbox.css("border-right","1px solid #ededed");
				}
			});
		},
		processTableColWidthResizeAndColSortEvent:function(table){
			//处理tableheader上的拖拽列宽和点击列头排序事件
			var that=this;
			if(table.data("column-resize")){
				table.table_box.on("mousemove",function(e){
					var resizingTh=table.table_box.find("table>thead>tr>th[data-resizing='true'],table>tfoot>tr>th[data-resizing='true']");
					if(isOk(resizingTh)){
						if(table.right_fixed){
							table.right_fixed.addClass("jbolt_table_fixed_hide");
						}
						var width=resizingTh.outerWidth()-10,left=resizingTh.offset().left;
						var newWidth=e.clientX-left;
						var dataWidth=resizingTh.data("width");
						var dataMinWidth=resizingTh.data("min-resize-width")||resizingTh.data("min-width")||50;
						if(dataMinWidth){
							dataWidth=dataMinWidth;
						}
						if(newWidth>dataWidth){
							that.resizeTheadTh(table,resizingTh.data("col-index"),newWidth);
						}

					}else{
						that.processTableThResizeAndSortStyleEvent(e);
					}
				}).on("mousedown",".jbolt_table_header>table>thead>tr>th.resize_col,.jbolt_table_footer>table>tfoot>tr>th.resize_col",function(e){
					if(isFormEle(e.target.tagName)){
						return true;
					}
					var th=$(this);
					var canResize=th.data("can-resize");
					th.data("resizing",canResize?true:false).attr("data-resizing",canResize?true:false);
					if(canResize){
						var cursor=th.parent().css("cursor");
						if(cursor=="col-resize"){
							table.table_box.find("table.jbolt_table.novscroll").addClass("col_last_need_border_right");
						}

						var colIndex=th.data("col-index");
						var sameCols=table.table_box.find("table>thead>tr>th[data-col-index='"+colIndex+"'],table>tfoot>tr>th[data-col-index='"+colIndex+"'],table>tbody>tr>td[data-col-index='"+colIndex+"']");
						if(isOk(sameCols)){
							sameCols.addClass("resizing");
						}
					}
					return false;
				}).on("mouseup",function(e){
					e.preventDefault();
					var currentBox=$(this);
					currentBox.find('.jbolt_table_header>table>thead>tr>th').data("resizing",false).data("can-resize",false).attr("data-resizing",false).attr("data-can-resize",false);
					currentBox.find('.jbolt_table_footer>table>tfoot>tr>th').data("resizing",false).data("can-resize",false).attr("data-resizing",false).attr("data-can-resize",false);
					//如果有横向滚动条 处理一下样式
					that.refreshFixedColumnHScroll(table);
					var sameCols=currentBox.find("table>thead>tr>th.resizing,table>tfoot>tr>th.resizing,table>tfoot>tr>th[data-resizing='true'],table>tbody>tr>td.resizing");
					if(isOk(sameCols)){
						sameCols.removeClass("resizing");
					}
				})

				table.table_view.on("dblclick","table>thead>tr>th.resize_col,table>tfoot>tr>th.resize_col",function(e){
					var th = $(this);
					e.preventDefault();
					e.stopPropagation();
					that.processColAutoWidth(table,th.data("col-index"));
					return false;
				});
			}else{
				table.table_box.on("mousemove",function(e){
					that.processTableThResizeAndSortStyleEvent(e);
				})
			}

			table.table_box.on("click",".jbolt_table_header>table>thead>tr>th[data-can-sort='true']",function(e){
				if(table.willCopy){
					//如果是在willCopy状态 不操作
					return;
				}
				e.preventDefault();
				e.stopPropagation();
				if(e.target.tagName=="I"){
					var i=$(e.target),type,th=i.closest("th");
					if(i.hasClass("sort_asc")){
						type="asc";
					}else if(i.hasClass("sort_desc")){
						type="desc";
					}
					that.sortByTheadTh(table,th,type);
				}else{
					that.sortByTheadTh(table,$(this));
				}
				return false;
			});
			if(!table.isAjax){
				table.table_box.on("mouseenter",".jbolt_table_header>table>thead>tr",function(e){
					if(e.target.tagName=="TH"){
						//说明鼠标在TH上了
						var th=$(e.target),tr=th.parent();
						var width=th.outerWidth()-10,left=th.offset().left;
						var newWidth=e.clientX-left;
						if(newWidth<width){
							tr.css("cursor","pointer");
							th.data("can-sort",true).attr("data-can-sort",true);
						}
					}
				});
			}
		},
		processColAutoWidth:function(table,colIndex){
			table.table_view.find("table>thead>tr>th[data-col-index='"+colIndex+"'],table>tfoot>tr>th[data-col-index='"+colIndex+"'],table>tbody>tr>td[data-col-index='"+colIndex+"']")
				.css({
					"width": "none",
					"min-width":"none",
					"max-width":"none"
				});
			var td = table.tbody.find("tr:first>td[data-col-index='"+colIndex+"']");
			if(isOk(td)){
				this.resizeTheadTh(table,colIndex,td.outerWidth());
			}else{
				var th = table.thead.find("tr>th[data-col-index='"+colIndex+"']");
				if(isOk(th)){
					this.resizeTheadTh(table,colIndex,th.outerWidth());
				}
			}

		},
		//排序查询
		sortByColumn:function(table,column,theSortType){
			var col=table.thead.find("th[data-column='"+column+"'],th[data-sort-column='"+column+"']");
			this.sortByTheadTh(table,col,theSortType);
		},
		//排序查询
		sortByColIndex:function(table,colIndex,theSortType){
			var col=table.thead.find("th[data-col-index='"+colIndex+"']");
			this.sortByTheadTh(table,col,theSortType);
		},
		//取消排序查询
		cancelSort:function(table){
			table.data("sort-column","").removeAttr("data-sort-column");
			table.table_box.find("i.sort.active").removeClass("active");
			table.data("sort-column","").removeAttr("data-sort-column").data("sort-type","").removeAttr("data-sort-type");
			table.resetCellWidthAfterAjax=false;
			if(table.isAjax){
				this.readByPage(table,1);
			}else{
				this.tableSubmitForm(table,1);
			}
		},
		//排序查询
		sortByTheadTh:function(table,col,theSortType){
			if(!isOk(col)){return;}
			var sort_column_name=col.data("sort-column")||col.data("column");
			var colIndex=col.data("col-index");
			table.data("sort-column",sort_column_name).attr("data-sort-column",sort_column_name);
			var defaultSortColumn=table.data("default-sort-column");
			if(defaultSortColumn&&sort_column_name==defaultSortColumn&&!theSortType){
				//如果table的默认排序字段就是当前要排序的字段 并且当前排序的字段已经是倒序排列了 就需要切换为正序
				var activeType=col.find("i.sort.active");
				if(isOk(activeType)){
					if(activeType.hasClass("sort_desc")){
						theSortType="asc";
					}
				}
			}
			if(theSortType){
				//指定要用什么方式了
				var sameCols=table.table_box.find("table>thead>tr>th[data-col-index='"+colIndex+"']");
				table.table_box.find("i.sort.active").removeClass("active");
				if(theSortType=="desc"){
					table.data("sort-type","desc").attr("data-sort-type","desc");
					sameCols.find("i.sort.sort_desc").addClass("active");
				}else if(theSortType=="asc"){
					table.data("sort-type","asc").attr("data-sort-type","asc");
					sameCols.find("i.sort.sort_asc").addClass("active");
				}
			}else{
				var sortType=col.find("i.sort.active");
				var sameCols=table.table_box.find("table>thead>tr>th[data-col-index='"+colIndex+"']");
				table.table_box.find("i.sort.active").removeClass("active");
				if(isOk(sortType)){
					if(sortType.hasClass("sort_asc")){
						table.data("sort-type","desc").attr("data-sort-type","desc");
						sameCols.find("i.sort.sort_desc").addClass("active");
					}else if(sortType.hasClass("sort_desc")){
//							table.data("sort-type","asc");
//							sameCols.find("i.sort.sort_asc").addClass("active");
						table.data("sort-column","").removeAttr("data-sort-column").data("sort-type","").removeAttr("data-sort-type");
					}
				}else{
					sameCols.find("i.sort.sort_asc").addClass("active");
					table.data("sort-type","asc").attr("data-sort-type","asc");
				}

			}
			table.resetCellWidthAfterAjax=false;
			if(table.isAjax){
				this.readByPage(table,1);
			}else{
				this.tableSubmitForm(table,1);
			}
		},
		//重新设置tbody中宽度
		resizeTrByOldWidth:function(table,tr){
			var columns=Object.keys(table.columnIndexMap);
			var colIndex=-1;
			var td,that=this,width=100;
			$.each(columns,function(i,column){
				colIndex=table.columnIndexMap[column];
				width=table.table_box.find(".jbolt_table_header>table>thead>tr>th[data-col-index='"+colIndex+"']").outerWidth();
				that.resizeTheadTh(table,colIndex,width);
			});

		},
		resizeTheadTh:function(table,thColIndex,width){
			table.resetCellWidthAfterAjax=false;
			if(width){
				table.table_box.find("table>thead>tr>th[data-col-index='"+thColIndex+"'],table>tfoot>tr>th[data-col-index='"+thColIndex+"']").css({
					"width":width+"px",
					"min-width":width+"px",
					"max-width":width+"px"
				}).data("old-width",width).attr("data-old-width",width);
				// table.table_box.find().css({
				// 	"width":width+"px",
				// 	"min-width":width+"px",
				// 	"max-width":width+"px"
				// }).data("old-width",width).attr("data-old-width",width);
				table.table_box.find("table>tbody>tr:not(.notRowSpanFirstTr)>td[data-col-index='"+thColIndex+"']").css({
					"width":width+"px",
					"min-width":width+"px",
					"max-width":width+"px"
				}).data("old-width",width).attr("data-old-width",width);
			}
		},
		//刷新主table的v scroll样式
		refreshMainTableVScroll:function(mainTable){
			var that=this;
			var thh=parseInt(mainTable.find("thead").outerHeight())+1;
			var hasVScroll=checkTableBodyHasScrollBar(mainTable, "v");
			if(hasVScroll){
				mainTable.table_box.find("table").removeClass("novscroll");
			}else{
				mainTable.table_box.find("table").addClass("novscroll");
			}
			that.processHeaderRight(mainTable);
			var tableDataHeight=mainTable.data("height");
			var isFillHeight=(typeof(tableDataHeight)=="string"&&tableDataHeight=="fill");
			var tableH=parseInt(mainTable.height());
			var bodyH=parseInt(mainTable.table_body.height());
			if(isFillHeight){
				if(hasVScroll){
					mainTable.table_box.find(".jbolt_table_body>table").addClass("noBottomBorder");
				}else{
					mainTable.table_box.find(".jbolt_table_body>table").removeClass("noBottomBorder");
				}
			}else{

				if(tableH< bodyH){
					mainTable.table_box.find(".jbolt_table_body>table").removeClass("noBottomBorder");
				}else{
					mainTable.table_box.find(".jbolt_table_body>table").addClass("noBottomBorder");
				}
			}



		},
		//刷新主table的h scroll样式
		refreshFixedColumnHScroll:function(mainTable){
			var that=this;
			var hasHScroll=checkTableBodyHasScrollBar(mainTable, "h");
			var hasVScroll=checkTableBodyHasScrollBar(mainTable, "v");
			var tmh=parseInt(mainTable.find("thead").outerHeight())+1;
			var mainTbodyHeight=parseInt(mainTable.table_body.height());
			var tableH=parseInt(mainTable.height());
			var tableDataHeight=mainTable.data("height");
			var isFillHeight=(typeof(tableDataHeight)=="string"&&tableDataHeight=="fill");
			//如果是height fill的情况
			if(isFillHeight){
				if(hasHScroll){
					var scrollHeight=getScrollBarHeight(mainTable.table_body[0]);
					mainTbodyHeight=mainTbodyHeight-scrollHeight;
				}else{
					if(!hasVScroll){
						mainTbodyHeight=tableH-tmh;
					}
				}

			}else{
				if(hasHScroll){
					var scrollHeight=getScrollBarHeight(mainTable.table_body[0]);
					mainTbodyHeight=mainTbodyHeight-scrollHeight;
				}
			}


			//左侧处理
			if(isOk(mainTable.left_fixed)){
				//处理fixed 区域高度
				mainTable.left_fixed.body.css({
					"max-height":mainTbodyHeight+"px",
					"height":"auto"
				});
				if(hasHScroll){
					mainTable.left_fixed.removeClass("jbolt_table_fixed_hide");
				}else{
					mainTable.left_fixed.addClass("jbolt_table_fixed_hide");
				}
			}
			//右侧处理
			if(isOk(mainTable.right_fixed)){
				//处理fixed 区域高度
				mainTable.right_fixed.body.css({
					"max-height":mainTbodyHeight+"px",
					"height":"auto"
				});
				if(hasVScroll){
					var scrollWidth=getScrollBarWidth(mainTable.table_body[0]);
					mainTable.right_fixed.css({
						"right":scrollWidth+"px"
					});
					var jbolt_table_fixed_right_mend=mainTable.right_fixed.header.find(".jbolt_table_fixed_right_mend");
					if(!isOk(jbolt_table_fixed_right_mend)){
						mainTable.right_fixed.header.append("<div class='jbolt_table_fixed_right_mend' style='width:"+scrollWidth+"px;right:"+(0-scrollWidth)+"px'></div>");
					}
				}else{
					mainTable.right_fixed.css({
						"right":"-1px"
					});
					mainTable.right_fixed.header.find(".jbolt_table_fixed_right_mend").remove();
				}

				if(hasHScroll){
					mainTable.right_fixed.removeClass("jbolt_table_fixed_hide");
				}else{
					mainTable.right_fixed.addClass("jbolt_table_fixed_hide");
				}

			}
		},
		getColumnFixedIndexArr:function(table,fixed_columns_dir){
			var indexArr=[];
			if(table.isEmpty){return indexArr;}
			var fixed_columns=table.data("fixed-columns-"+fixed_columns_dir),indexArr;
			if(fixed_columns){
				if(typeof(fixed_columns)=="number"){
					indexArr=[fixed_columns];
				}else{
					indexArr=fixed_columns.split(",");
				}
				//右侧用负数的处理
				if(isOk(indexArr)&&fixed_columns_dir=="right"){
					var lastTd=table.tbody.find("tr>td[data-col-index]:last");
					var lastColIndex=lastTd.data("col-index")+2;
					var tempAttr=new Array();
					$.each(indexArr,function(i,item){
						if(item<0){
							tempAttr.push(parseInt(item) + lastColIndex);
						}else{
							tempAttr.push(item);
						}
					});
					indexArr=tempAttr;
				}
			}
			return indexArr;
		},
		cloneTreeTableBoxContent:function(trs,dir,indexArr){
			var newTrs = trs.clone();
			var size=indexArr.length;
			var tbody_td_selector="td:not(";
			//就一行的话 好说
			for(var i=0;i<size;i++){
				tbody_td_selector=tbody_td_selector+(i>0?",":"")+":nth-child("+indexArr[i]+")";
			}
			tbody_td_selector=tbody_td_selector+")";
			newTrs.find(tbody_td_selector).remove();
			return newTrs;
		},
		cloneTableBoxContent:function(table_box,dir,indexArr){
			var that=this;
			var className="jbolt_table_fixed_"+dir;
			//从原数据处克隆后处理
			var fixedColumnBox=$('<div class="jbolt_table_fixed"></div>');
			fixedColumnBox.addClass(className);
			var jbolt_table_header=table_box.find(".jbolt_table_header:first").clone();
			var jbolt_table_body=table_box.find(".jbolt_table_body:first").clone();
			var size=indexArr.length;
			var trs=jbolt_table_header.find("thead>tr");
			var trlen=trs.length;
			if(trlen==1){
				var thead_th_selector="table>thead>tr>th:not(",
					tbody_td_selector="table>tbody>tr>td:not(";
				//就一行的话 好说
				for(var i=0;i<size;i++){
					thead_th_selector=thead_th_selector+(i>0?",":"")+":nth-child("+indexArr[i]+")";
					tbody_td_selector=tbody_td_selector+(i>0?",":"")+":nth-child("+indexArr[i]+")";
				}
				thead_th_selector=thead_th_selector+")";
				tbody_td_selector=tbody_td_selector+")";
				jbolt_table_header.find(thead_th_selector).remove();
				jbolt_table_body.find(thead_th_selector).remove();
				jbolt_table_body.find(tbody_td_selector).remove();
			}else{
				//如果多行
				var thead_th_selector1="table>thead>tr>th:not(",
					tbody_td_selector1="table>tbody>tr>td:not(";
				//就一行的话 好说
				for(var i=0;i<size;i++){
					thead_th_selector1=thead_th_selector1+(i>0?",":"")+"[data-fixed-col-index='"+indexArr[i]+"']";
					tbody_td_selector1=tbody_td_selector1+(i>0?",":"")+"[data-fixed-col-index='"+indexArr[i]+"']";
				}
				thead_th_selector1=thead_th_selector1+")";
				tbody_td_selector1=tbody_td_selector1+")";
				jbolt_table_header.find(thead_th_selector1).remove();
				jbolt_table_body.find(thead_th_selector1).remove();
				jbolt_table_body.find(tbody_td_selector1).remove();
			}

			//这里去掉fixed_right上的resize_col 为了不让 右侧的fixed column控制宽度
			if(dir=="right"){
				jbolt_table_header.find("table>thead>tr>th.resize_col,table>tfoot>tr>th.resize_col").removeClass("resize_col");
			}

			fixedColumnBox.append(jbolt_table_header);
			fixedColumnBox.append(jbolt_table_body);
			jbolt_table_header.table=jbolt_table_header.find("table");
			var header_width=jbolt_table_header.table.css("width");
			if(header_width&&typeof(header_width)=="string"&&header_width.indexOf("px")!=-1){
				jbolt_table_header.table.css({"width":"auto"});
			}
			jbolt_table_body.table=jbolt_table_body.find("table");
			var body_width=jbolt_table_body.table.css("width");
			if(body_width&&typeof(body_width)=="string"&&body_width.indexOf("px")!=-1){
				jbolt_table_body.table.css({"width":"auto"});
			}
			that.clearTableData(jbolt_table_header.table);
			that.clearTableData(jbolt_table_body.table);
			fixedColumnBox.header=jbolt_table_header;
			fixedColumnBox.body=jbolt_table_body;
			return fixedColumnBox;
		},
		processColumnFixedLeft:function(table){
			if(table.left_fixed){
				table.left_fixed.remove();
				table.left_fixed=null;
				table.fixedColumnTables=null;
			}
			if(table.isEmpty){
				return;
			}

			var that=this;
			//得到需要左侧显示的fixedColumns 列序号
			var indexArr=that.getColumnFixedIndexArr(table,"left");
			if(!indexArr||indexArr.length==0){return false;}
			//创建一个左侧覆盖区域 用于存放左侧需要fixed效果的columns
			var jbolt_table_fixed=that.cloneTableBoxContent(table.table_box,"left",indexArr);
			//append
			table.table_box.append(jbolt_table_fixed);
			jbolt_table_fixed.find("table").css("margin-left","0px");
			jbolt_table_fixed.find("input[type='radio'][name='jboltTableRadio'],input[type='radio'][name='jboltTableRadio_fixedheader']").attr("name","jboltTableRadio_fixed");

			initToolTip(jbolt_table_fixed);
			table.left_fixed=jbolt_table_fixed;
		},
		processColumnFixedRight:function(table){
			if(table.right_fixed){
				table.right_fixed.remove();
				table.right_fixed=null;
				table.fixedColumnTables=null;
			}
			if(table.isEmpty){
				return;
			}
			var that=this;
			//得到需要左侧显示的fixedColumns 列序号
			var indexArr=that.getColumnFixedIndexArr(table,"right");
			if(!indexArr||indexArr.length==0){return false;}

			//创建一个左侧覆盖区域 用于存放左侧需要fixed效果的columns
			var jbolt_table_fixed=that.cloneTableBoxContent(table.table_box,"right",indexArr);
			jbolt_table_fixed.addClass("jbolt_table_fixed_hide");
			jbolt_table_fixed.find("table").css("margin-left","0px");
			jbolt_table_fixed.find("input[type='radio'][name='jboltTableRadio'],input[type='radio'][name='jboltTableRadio_fixedheader']").attr("name","jboltTableRadio_fixed");
			//append
			table.table_box.append(jbolt_table_fixed);
			initToolTip(jbolt_table_fixed);
			table.right_fixed=jbolt_table_fixed;
		},
		processHeaderRight:function(table){
			//获得滚动条宽度，用来设置clone出来的thead里的右侧样式
			var scrollbarW=getScrollBarWidth(table.table_body[0]);
			if(scrollbarW){
				table.table_box.find(".jbolt_table_header").css("padding-right",scrollbarW+"px");
			}else{
				table.table_box.find(".jbolt_table_header").css("padding-right","0px");
			}
		},processFooterRight:function(table){
			//获得滚动条宽度，用来设置clone出来的tfoot里的右侧样式
			var scrollbarW=getScrollBarWidth(table.table_body[0]);
			if(scrollbarW){
				table.table_box.find(".jbolt_table_footer").css("padding-right",scrollbarW+"px");
			}else{
				table.table_box.find(".jbolt_table_footer").css("padding-right","0px");
			}
		},clearTableData:function(table){
			table.removeAttr("data-jbolttable");
			table.removeAttr("data-fixed-columns-left");
			table.removeAttr("data-fixed-columns-right");
			table.removeClass("jbolt_main_table");
			table.removeAttr("id");
		},
		cloneHFTable:function(table){
			var clazzs=table.attr("class");
			var tableWidth=table.data("width");
			var newTable=$("<table data-width='"+tableWidth+"' data-theme='"+table.theme+"' class='"+clazzs+"'></table>");
			newTable.removeClass("jbolt_main_table");
			return newTable;
		},
		processHeaderAndFooterFixed:function(table){
			if(!table.hasHeader&&!table.hasFooter){
				return false;
			}
			//要去复制一份儿
			var that=this;
			var columnResize=table.data("column-resize");
			if(table.hasHeader){
				var headerTable=this.cloneHFTable(table),
					thead=table.thead.clone();
				headerTable.append(thead);
				if(columnResize){
					thead.find("tr>th").addClass("resize_col");
				}
				var jboltTableHeader=$("<div data-theme='"+table.theme+"' class='jbolt_table_header'></div>");
				jboltTableHeader.append(headerTable);
				//在原区域上方插入
				table.table_body.before(jboltTableHeader);
				var tableTheadHeight = table.thead.data("height");
				if(tableTheadHeight) {
					//把原来的thead隐藏掉
					table.css("margin-top","-"+(table.thead._height+1)+"px");
				}else{
					//把原来的thead隐藏掉
					table.css("margin-top","-"+table.thead._height+"px");
				}

				table.table_box.addClass("fixedHeader");
				that.processHeaderRight(table);
				initToolTip(thead);
				//赋值给table
				table.fixed_header=jboltTableHeader;
				table.fixed_header.table=headerTable;
				table.fixed_header.table.find("input[type='radio']").attr("name","jboltTableRadio_fixedheader")
			}
			if(table.hasFooter&&table.tfootFixed){
				var footerTable=this.cloneHFTable(table),
					tfoot=table.tfoot.clone();
				footerTable.append(tfoot);
				if(columnResize){
					tfoot.find("tr>th").addClass("resize_col");
				}
				var jboltTableFooter=$("<div data-theme='"+table.theme+"' class='jbolt_table_footer'></div>");
				jboltTableFooter.append(footerTable);
				//在原区域下方插入
				table.table_body.after(jboltTableFooter);
				//把原来的thead隐藏掉
				table.tfoot.remove();
				table.table_box.addClass("fixedFooter");
				that.processFooterRight(table);
				initToolTip(tfoot);
				//赋值给table
				table.fixed_footer=jboltTableFooter;
				table.fixed_footer.table=footerTable;
				table.tfoot = table.fixed_footer.table.find("tfoot");
			}

		},
		processTableColWidthAfterResize:function(table){
			//原来有纵向滚动 但是现在没了
			this.processCellWidthAndHeight(table);
		},
		//处理一行header的样式
		processTheadWidthCompletion:function(table){
			//如果存在auto列 就判断现在满了没有
			var that=this,avgWidth=60,th,avgthsSize,avgths,laveaWidth=0;
			var hasVScroll=checkTableBodyHasScrollBar(table, "v");
			var scrollWidth=hasVScroll?getScrollBarWidth(table.table_body[0]):0;
			if(hasVScroll){
				//有滚动条的时候处理单独的
				table.tableBodyLeaveWidth=table.tableBodyLeaveWidth-scrollWidth-1;
			}
			avgths=table.thead.find("tr>th[data-col-index][data-width='auto'],tr>th[data-col-index]:not([data-width]),tr>th[data-col-index]:not([data-nochange]):not([data-column='index']):not([data-column='checkbox']):not([data-column='optcol'])");
			if(isOk(avgths)){
				avgthsSize=avgths.length;
				if(table.tableBodyLeaveWidth<200&&avgthsSize>=2){
					avgWidth=100;
				}else{
					avgWidth=Math.floor(table.tableBodyLeaveWidth/avgthsSize);
					laveaWidth=table.tableBodyLeaveWidth-avgWidth*avgthsSize;
					var tbs=table.closest(".jbolt_page").find("[data-jbolttable]");
					if(tbs.length==1){
						if(table.thead.trs.length>1){
							laveaWidth=laveaWidth-2;
						}
					}else{
						if(table.thead.trs.length>2){
							laveaWidth=laveaWidth-2;
						}else if(table.thead.trs.length>1){
							laveaWidth=laveaWidth-1;
						}
					}
				}
				var avgMinThWidth,real_avgWidth,dataWidth;
				avgths.each(function(i){
					th=$(this);
					real_avgWidth=avgWidth;
					if(i==avgthsSize-1 && laveaWidth>0){
						real_avgWidth=real_avgWidth+laveaWidth;
					}
					dataWidth=th.data("min-width")||th.data("width");
					if(dataWidth<100){
						dataWidth=100;
					}
					if(real_avgWidth<dataWidth){
						real_avgWidth=dataWidth;
					}
					that.resizeTheadTh(table,th.data("col-index"),real_avgWidth);
				});
			}
		},
		/**
		 * 处理单元格宽高和样式
		 */
		processCellWidthAndHeight:function(table){
			//这里得到了表格外框的宽度 然后需要看看是不是要把表格列设置均匀分布
			var that=this;
			var ths=table.thead.find("th[data-col-index]");
			var thsize=ths.length;
			var mins=1;
			//表格还剩多少没分配宽度
			table.tableBodyLeaveWidth=table.table_body.width()-mins;
			for(var i=0;i<thsize;i++){
				//处理非auto列的宽度 一次性设置好 不能变更的 剩下的下面自动化处理
				that.setOneColumnWidth(table,i,ths.eq(i));
			}
			var width=table.data("width");
			if(width&&width!="auto"){
				//处理补全
				that.processTheadWidthCompletion(table);
			}

			//如果是fill的就得等width设置好再设置height
			if(!table.isAjax){
				that.processCellHeight(table);
			}
			that.processTheadThHeight(table);
			that.processTfootThHeight(table);
			//开始判断样式了
			var hasHScroll=checkTableBodyHasScrollBar(table,'h');
			table.table_box.find("table").removeClass("col_last_need_border_right");
			if(hasHScroll){
				//原来有纵向滚动 但是现在没了
				table.table_box.find("table").addClass("col_last_need_border_right");
			}
		},
		/*processHeadThAndBodyTbFinalWidth:function(table){
				var that=this;
				var thead=table.find("thead"),tbody=table.find("tbody"),ths=thead.find("tr>th[data-col-index]:not(.jbolt_table_last_th)");
				if(!isOk(ths)){return false;}
				var thSize=ths.length,thColIndex,th,real_width,width,totalWidth=0,autoThColIndex=new Array();
				for(var i=0;i<thSize;i++){
					th=ths.eq(i);
					thColIndex=th.data("col-index");
					real_width=th[0].offsetWidth;
					var leftBorderWidth=parseInt(th.css("border-left-width"));
					var rightBorderWidth=parseInt(th.css("border-right-width"));
					real_width=real_width-leftBorderWidth;
					width=th.data("width");
					if(!width||width=="auto"){
						autoThColIndex.push(thColIndex);
					}else{
						if(real_width<width){
							real_width=width;
						}
						totalWidth=totalWidth+real_width;
						that.resizeTheadTh(table,thColIndex,real_width);
					}

				}
				var autoThInfo={totalWidth:totalWidth,autoThColIndex:autoThColIndex};
				return autoThInfo;
			},*/
		/*processHeadThAndBodyTbAutoWidth:function(table){
				var that=this;
				var thead=table.find("thead"),tbody=table.find("tbody"),ths=thead.find("tr>th[data-col-index][data-width='auto']:not(.jbolt_table_last_th)");
				if(!isOk(ths)){return false;}
				tbody.hide();
				var thSize=ths.length,thColIndex,th,real_width;
				for(var i=0;i<thSize;i++){
					th=ths.eq(i);
					thColIndex=th.data("col-index");
					real_width=th.outerWidth();
					that.resizeTheadTh(table,thColIndex,real_width);
				}
				tbody.show();
			},*/
		/**
		 * 处理tbody单元格宽 ajax之后
		 */
		processCellWidthAfterAjax:function(table){
			//这里得到了表格外框的宽度 然后需要看看是不是要把表格列设置均匀分布
			var oldhasVScroll=checkTableBodyHasScrollBar(table,'v');
			var oldhasHScroll=checkTableBodyHasScrollBar(table,'h');
			var that=this;
			var th,thwidth,realWidth,allWitdh=0,thlen=table.theadWithColIndexThs.length;
			for(var i=0;i<thlen;i++){
				th=table.theadWithColIndexThs.eq(i);
				thwidth=th.data("old-width");
				allWitdh+=thwidth;
				that.resizeTheadTh(table,th.data("col-index"),thwidth);
			}
			that.processCellHeight(table);
			if(table.data("width")=="auto"){
				return;
			}
			var hasVScroll=checkTableBodyHasScrollBar(table,'v');
			var inited=table.data("inited");
			//width保持不变
			if(table.resetCellWidthAfterAjax||!inited){
				var scrollBarWidth=getScrollBarWidth(table.table_body[0]);
				var lw=table.outerWidth()-(table.table_body.outerWidth()-1);
//				var avgths=table.thead.find("tr>th[data-col-index][data-width='auto'],tr>th[data-col-index]:not([data-width]),tr>th[data-col-index]:not([data-nochange]):not([data-column='index']):not([data-column='checkbox']):not([data-column='optcol'])");
				var avgths=table.thead.find("tr>th[data-col-index][data-width='auto'],tr>th[data-col-index]:not([data-width])");
				if(isOk(avgths)){
					var avgth=avgths.eq(0);
					if(lw>=0){
						if(hasVScroll){
							realWidth=avgth.outerWidth()-scrollBarWidth-lw;
						}else{
							realWidth=avgth.outerWidth()-lw+1;
						}
					}else{
						if(hasVScroll){
							realWidth=avgth.outerWidth()-lw-scrollBarWidth;
						}else{
							realWidth=avgth.outerWidth()-lw+scrollBarWidth;
						}
					}
					var minwidth=avgth.data("min-width")||avgth.data("width")||100;
					if(realWidth&&realWidth>=minwidth){
						that.resizeTheadTh(table,avgth.data("col-index"),realWidth);
					}
				}
			}

			allWitdh=allWitdh+2;
			var tableWidth=table.table_body.width();
//				console.log(allWitdh+":"+tableWidth)
			if(allWitdh<tableWidth){
				table.table_box.find("table").addClass("col_last_need_border_right");
			}else{
				table.table_box.find("table").removeClass("col_last_need_border_right");
			}
			/*	//开始判断样式了
				var hasVScroll=checkTableBodyHasScrollBar(table,'v');
				if(!hasVScroll){
					alert(oldhasHScroll+":"+hasHScroll)
					if(hasHScroll ||(oldhasHScroll&&!hasHScroll)){
						table.table_box.find("table").removeClass("col_last_need_border_right");
						return;
					}
					table.table_box.find("table").addClass("col_last_need_border_right");
					return;
				}

				var hasHScroll=checkTableBodyHasScrollBar(table,'h');
				if(hasHScroll || (oldhasHScroll&&!hasHScroll)){
					table.table_box.find("table").removeClass("col_last_need_border_right");
				}*/
		},
		processOneAutoThWidthByTrChange:function(table){
			if(table.data("width")=="auto"){
				return;
			}
			var that=this;
			var hasVScroll=checkTableBodyHasScrollBar(table, "v");
			var hasHScroll=checkTableBodyHasScrollBar(table, "h");
			var thead=table.find("thead");
			if(!hasVScroll&&!hasHScroll){
				var isAuto=table[0].style.width="auto";
				if(isAuto){
					var pr=parseInt(table.fixed_header.css("padding-right"));
					var cha=table.table_body.width()-table.width();
					if(cha>=10||cha<=-10){
						var lastTh=thead.find("tr>th[data-col-index][data-width='auto']:first");
						if(!isOk(lastTh)){
							lastTh=thead.find("tr>th[data-col-index]:not([data-width]):first");
						}
						if(isOk(lastTh)){
							var lastThWidth=lastTh.outerWidth()+cha;
							that.resizeTheadTh(table,lastTh.data("col-index"),lastThWidth);
						}
						return;
					}
				}
			}else if(hasVScroll&&!hasHScroll){
				var isAuto=table[0].style.width="auto";
				if(isAuto){
					var pr=parseInt(table.fixed_header.css("padding-right"));
					var cha=table.table_body.width()-table.width();
					if(pr==0&&(cha<=5||cha>=-5)){
						var lastTh=thead.find("tr>th[data-col-index][data-width='auto']:first");
						if(!isOk(lastTh)){
							lastTh=thead.find("tr>th[data-col-index]:not([data-width]):first");
						}
						if(isOk(lastTh)){
							var lastThWidth=lastTh.outerWidth()-17;
							that.resizeTheadTh(table,lastTh.data("col-index"),lastThWidth);
						}
					}
				}

			}
		},
		processCellHeight:function(table){
			if(!table.isEmpty){
				var tr,height,trs=table.tbody.find("tr"),len=trs.length;
				for(var i=0;i<len;i++){
					tr = trs.eq(i);
					tr.data("index",i).attr("data-index",i);
					height=tr.find("td:not([rowspan]):first").outerHeight()+"px";
					tr.find("td:first,td:last").css({
						"height":height,
						"min-height":height,
						"max-height":height,
					});
				}

			}
		},
		processTheadThHeight:function(table){
			var th,thHeight;
			table.find("thead>tr>th").each(function(){
				th=$(this);
				thHeight=th.outerHeight()+"px";
				th.css({
					"height":thHeight,
					"min-height":thHeight,
					"max-height":thHeight,
				});
			});
		},
		processTfootThHeight:function(table){
			var th,thHeight;
			table.find("tfoot>tr>th").each(function(){
				th=$(this);
				thHeight=th.outerHeight()+"px";
				th.css({
					"height":thHeight,
					"min-height":thHeight,
					"max-height":thHeight,
				});
			});
		},
		setOneAutoWidthColumnWidth:function(table,index,th,minWidth){
			var width=th.data("width"),isAutoTh=(typeof(width)=="string"&&width=="auto"),nthTdIndex=index+1,maxWidth;
			if(!isAutoTh){
				return false;
			}
			if(table.data("width")=="auto"){
				minWidth=th.data("min-width")||100;
			}else{
				minWidth=th.data("min-width")||minWidth;
			}
			width=minWidth;
			maxWidth=th.data("max-width")||width;
			th.css({
				"min-width":minWidth,
				"max-width":maxWidth,
				"width":width
			});
			if(!table.isAjax){
				table.tbody.find("tr>td[data-col-index='"+index+"']").css({
					"min-width":minWidth,
					"max-width":maxWidth,
					"width":width
				});
			}

			if(isOk(table.tfoot)){
				table.tfoot.find("tr>th[data-col-index='"+index+"']").css({
					"min-width":minWidth,
					"max-width":maxWidth,
					"width":width
				});
			}


		},
		//按顺序设置一列宽度
		setOneColumnWidth:function(table,index,th){
			var width=th.data("width");
			var minwidth=th.data("min-width");
			if(minwidth){
				width=minwidth;
			}
			if(!width||(typeof(width)=="string"&&width=="auto")){
				return false;
			}
			var column=th.data("column");
			if((column&&(column=="index"||column=="checkbox"||column=="radio"||column=='optcol'))||th[0].hasAttribute("data-nochange")){
				table.tableBodyLeaveWidth=table.tableBodyLeaveWidth-width;
			}
			this.resizeTheadTh(table,th.data("col-index"),width);
		},
		//ajax执行完后 重新设置新添加的cell的宽
		setOneColumnWidthAfterAjax:function(th,index,table){
			var nthTdIndex=index+1,th=table.thead.find("tr>th[data-col-index='"+index+"']");
			var width=th[0].offsetWidth;
			table.tbody.find("tr>td[data-col-index='"+index+"']").css({
				"min-width":width,
				"max-width":width,
				"width":width
			});
			if(isOk(table.tfoot)){
				table.tfoot.find("tr>th[data-col-index='"+index+"']").css({
					"min-width":width,
					"max-width":width,
					"width":width
				});
			}
		}
	};
	$.fn.jboltTable = function (method) {
		// 方法调用
		if (jb_methods[method]) {
			return jb_methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
		} else if (typeof method === 'object' || !method) {
			return jb_methods.init(this);
		} else {
			$.error('Method' + method + 'does not exist on jQuery.jboltTable');
		}
	};
})(jQuery);

/**
 * jboltTreeTable组件封装
 */
;(function($){
	var jb_methods = {
		init: function (tables) {
			var that=this;
			return tables.each(function(){
				var table=$(this);
				table.removeClass("table-striped");
				table.addClass("jbolt_tree_table text-nowrap");
				var tbody=table.find("tbody");
				var tdIndex=table.data("parent-td-index");
				if(typeof(tdIndex)=="undefined"||tdIndex=="undefined"){
					tdIndex=0;
				}
				tbody.find("tr[data-parent]>td:nth-child("+tdIndex+")").data("parent-td",true).attr("data-parent-td","true").append('<div class="parent_flag_box"><i class="fa fa-caret-right"></i></div>');

				var expandall_trigger=table.data("expandall-trigger");
				if(expandall_trigger){
					$(expandall_trigger).off("click").on("click",function(e){
						e.preventDefault();
						e.stopPropagation();
						table.jboltTreeTable('expandAll');
						return false;
					});
				}
				var collapseall_trigger=table.data("collapseall-trigger");
				if(collapseall_trigger){
					$(collapseall_trigger).off("click").on("click",function(e){
						e.preventDefault();
						e.stopPropagation();
						table.jboltTreeTable('collapseAll');
						return false;
					});
				}

				//默认开启全部
				var expandall=table[0].hasAttribute("data-expandall");
				if(expandall){
					table.jboltTreeTable('expandAll');
				}
				//如果也定义了jbolttable 就执行这个
				if(table[0].hasAttribute("data-jbolttable")){
					table.jboltTable();
				}
//				table.data("inited",true);

				table.on("click",".parent_flag_box",function(e){
					e.preventDefault();
					e.stopPropagation();
					var td=$(this).closest("td");
					var tr=td.closest("tr");
					if(td.hasClass("expand")){
						//闭合
						that.collapseAll(tr);
					}else{
						//展开
						that.expand(tr);
					}
					return false;
				});
			});
		},
		expand: function (tr) {
			var dataId=tr.data("id");
			tr.find("td[data-parent-td]").addClass("expand");
			tr.nextAll("tr[data-pid='"+dataId+"']").show();
		},
		expandAll: function (tr) {
			var that=this;
			if(tr){
				//先把自己展开
				that.expand(tr);
				if(tr[0].hasAttribute("data-parent")){
					var dataId=tr.data("id");
					//得到自己的下级节点
					var trs=tr.nextAll("tr[data-pid='"+dataId+"']");
					if(isOk(trs)){
						var theTr;
						trs.each(function(){
							theTr=$(this);
							//循环子节点 挨个打开
							that.expand(theTr);
							//递归调用 继续找下级
							that.expandAll(theTr);
						});
					}

				}
			}else{
				that.find("tbody>tr").show();
				that.find("tbody>tr>td[data-parent-td]").addClass("expand");
			}
		},
		collapse: function (tr) {
			var dataId=tr.data("id");
			tr.nextAll("tr[data-pid='"+dataId+"']").hide();
			tr.find("td[data-parent-td]").removeClass("expand");

		},
		collapseAll:function (tr) {
			var that=this;
			if(tr){
				var dataId=tr.data("id");
				if(tr[0].hasAttribute("data-parent")){
					//如果是个parent就找到所有子节点
					var trs=tr.nextAll("tr[data-pid='"+dataId+"']");
					if(trs&&trs.length>0){
						var theTr;
						//如果有下级就逐级关闭
						trs.each(function(){
							//循环子节点 挨个关闭
							theTr=$(this);
							that.collapse(theTr);
							//然后递归调用 排除下级
							that.collapseAll(theTr);
						});
					}
				}
				that.collapse(tr);
			}else{
				that.find("tr[data-son]").hide();
				that.find("td[data-parent-td]").removeClass("expand");
			}
		},
	};

	$.fn.jboltTreeTable = function (method) {
		// 方法调用
		if (jb_methods[method]) {
			return jb_methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
		} else if (typeof method === 'object' || !method) {
			return jb_methods.init(this);
		} else {
			$.error('Method' + method + 'does not exist on jQuery.jboltTreeTable');
		}
	};
})(jQuery);