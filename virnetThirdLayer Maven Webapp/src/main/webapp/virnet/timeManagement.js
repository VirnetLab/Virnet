
function showInChart(){	
	body_content = $("#content");
	body_content.empty();
	
	h = $("<h></h>");
	h.html("课时管理");
	h.attr("class", "tittle col-lg-12 col-md-12 col-sm-12 col-xs-12");
	h.appendTo(body_content);
	
	c_btn_new = $("<button></button>");	
	c_btn_new.html("< 课时安排 >");
	c_btn_new.attr("class", "btn button-new");
	c_btn_new.attr("onclick", "");
	c_btn_new.appendTo(body_content);
	
	createDateSelector().appendTo(body_content);
	
	$('.form_date').datetimepicker({
	    language:  'zh-CN',
	    weekStart: 1,
	    todayBtn:  1,
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		minView: 2,
		forceParse: 0
	});
	
	createWeekSelector(body_content);
	
	createCourseTable().appendTo(body_content);
	
	c_btn_s = $("<button></button>");	
	c_btn_s.html("< 在表格内显示 >");
	c_btn_s.attr("class", "btn button-new");
	c_btn_s.attr("onclick", "showContent('time-management', 0);");
	c_btn_s.appendTo(body_content);
	
	user = new userControl();
	
	username = user.getUser();
	
	queryCourseTableInfo(username);
	
	queryCourseTable(username, 0, 0);

}

function showCourseTableWithWeek(){
	var week = $("select").val();
	
	var date = $("#courseTable-date").val();
	
	if(week == "" && date == ""){
		week = 0;
		date = 0;
	}
	else if(week == "" && date != ""){
		week = -1;
	}
	else{
		date = 0;
	}
	
	user = new userControl();
	username = user.getUser();
	
	queryCourseTable(username, date, week);
}

/**
 * 
 * @param user
 * @param date if xxxx-xx-xx, think as choosing the date 
 * @param week if 0, think as default week, if -1, think as using date
 */
function queryCourseTable(user, date, week){
	$.ajax({
		url : "showTimeArrange.action",
	    data : {
			user : user,
			date : date,
			week : week
	    },    
	    type : 'post',      
	    dataType : 'json',    
	    success : function(data){
	    	$("#course-table").replaceWith(createCourseTable());
	    	
	    	$("select").val(data["week"]);
	    	$("#courseTable-date").val(data["date"]);
	    	
	    	datalist = data["data"];
	    	s = datalist.length;
	    	
	    	for(var i = 0; i < s; i++){
	    		//process each class
	    		classname = datalist[i]["class"];
	    		timelist = datalist[i]["timelist"];
	    		
	    		l = timelist.length;
	    		for(var j = 0; j < l; j++){
	    			$("#courseTable-" + timelist[j]).attr("class", "courseTable-selected");
	    			$("#courseTable-" + timelist[j]).html(classname);
	    		}
	    			    		
	    	}
	    },
	    error : function(data){
	    	alert("error");
	    }
	});
}

function queryCourseTableInfo(user){
	$.ajax({
		url : "courseTableInfo.action",
	    data : {
			user : user,
	    },    
	    type : 'post',      
	    dataType : 'json',    
	    success : function(data){
	    	totalweek = data["totalweek"];
	
	    	var select = $("select");
	    	
	    	for(var i = 0; i < totalweek; i++){	    		
	    		var option = $("<option></option>");
	    		option.attr("value", (i + 1));
	    		option.html("第" + (i + 1) + "周");
	    		option.appendTo(select);
	    		option = null;
	    	}
	    },
	    error : function(data){
	    	
	    }
	});
}

function createCourseTable(){
	c_table = $("<table></table>");
	c_table.attr("class", "table table-striped table-bordered table-hover table-responsive course-table");
	c_table.attr("id", "course-table");
	
	c_thead = $("<thead></thead>");
	c_thead.appendTo(c_table);
	
	c_tr = $("<tr></tr>");
	c_tr.appendTo(c_thead);
	
	c_thead_content = new Array("", "一", "二", "三", "四", "五", "六", "日");
	for(var i = 0; i < 8; i++){
		c_th = $("<th></th>");
		c_th.html(c_thead_content[i]);
		if(i == 0){
			c_th.attr("class", "courseTable-th col-lg-2 col-md-2 col-sm-2 col-xs-2");
		}
		else{
			c_th.attr("class", "courseTable-th");
		}
		
		c_th.appendTo(c_tr);
	}
	
	c_tbody = $("<tbody></tbody>");
	c_tbody.appendTo(c_table);
	
	c_tbody_content = new Array("8 : 00 ~ 10 : 00", "10 : 00 ~ 12 : 00", "12 : 00 ~ 14 : 00", "14 : 00 ~ 16 : 00", "16 : 00 ~ 18 : 00", "18 : 00 ~ 20 : 00", "20 : 00 ~ 22 : 00", "22 : 00 ~ 24 : 00");
	for(var i = 0; i < 8; i++){
		c_tb_tr = $("<tr></tr>");
		c_tb_tr.appendTo(c_tbody);
		
		c_th = $("<th></th>");
		c_th.html(c_tbody_content[i]);
		c_th.attr("class", "courseTable-th");
		c_th.appendTo(c_tb_tr);
		
		for(var j = 0; j < 7; j++){
			c_td = $("<td></td>");
			c_td.appendTo(c_tb_tr);
			c_td.html();
			c_td.attr("id", "courseTable-" + i + (j + 1));
		}
	}
	
	return c_table;
}

function createDateSelector(){
	c_d_div = $("<div></div>");
	c_d_div.attr("class", "col-lg-4 col-md-4");
		
		c_d_select = $("<div></div>");
		c_d_select.appendTo(c_d_div);
		c_d_select.attr("class", "input-group has-success date form_date");
		c_d_select.attr("data-date", "");
		c_d_select.attr("data-date-format", "dd MM yyyy");
		c_d_select.attr("data-link-field", "courseTable-date");
		c_d_select.attr("data-link-format", "yyyy-mm-dd");
		
			c_d_s_input = $("<input></input>");
			c_d_s_input.appendTo(c_d_select);
			c_d_s_input.attr("class", "form-control");
			c_d_s_input.attr("size", "16");
			c_d_s_input.attr("type", "text");
			c_d_s_input.attr("readonly", "");
			c_d_s_input.attr("id", "courseTable-date");
			c_d_s_input.attr("onchange", "courseTable_input_click()");
			c_d_s_input.attr("placeholder", "选择日期");
			
			c_d_s_span1 = $("<span></span>");
			c_d_s_span1.appendTo(c_d_select);
			c_d_s_span1.attr("class", "input-group-addon");
				
				c_d_s_s1_span = $("<span></span>");
				c_d_s_s1_span.appendTo(c_d_s_span1);
				c_d_s_s1_span.attr("class", "glyphicon glyphicon-remove");
				
			c_d_s_span2 = $("<span></span>");
			c_d_s_span2.appendTo(c_d_select);
			c_d_s_span2.attr("class", "input-group-addon");
				
				c_d_s_s2_span = $("<span></span>");
				c_d_s_s2_span.appendTo(c_d_s_span2);
				c_d_s_s2_span.attr("class", "glyphicon glyphicon-calendar");		
		
		c_d_input = $("<input></input>");
		c_d_input.appendTo(c_d_div);
		c_d_input.attr("type", "hidden"); 
		c_d_input.attr("id", "courseTable-date");	
	
	return c_d_div;
}

function courseTable_input_click(){
	$("select").select2("val", "0");
}

function createWeekSelector(father){
	var c_w_div = $("<div></div>");
	
	var select = $("<select></select>");
	select.attr("class", "form-control select select-success select-block mbl");
	
	var option = $("<option></option>");
	option.attr("value", 0);
	option.html("默认课表");
	option.appendTo(select);
	
	select.appendTo(c_w_div);
	
	c_w_div.appendTo(father);
	
	select.select2({dropdownCssClass: 'dropdown-inverse'});
	select.on("change", function(){
		$("#courseTable-date").val("");
	});
	
	var button = $("<button></button>");
	
	button.attr("class", "btn btn-success");
	button.html("<i class='icon-arrow-right'></i>.");
	button.attr("onclick", "showCourseTableWithWeek()");
	button.appendTo(c_w_div);
	
	select = null;
	c_w_div = null;
	option = null;
	button = null;
}