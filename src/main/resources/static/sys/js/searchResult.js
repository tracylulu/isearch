var ieFlag = false;
var searchTimeType = 'all';
var begin;
var end;
$(function () {
    initTime();
    initBrowser();
    $("#searchKey").textbox("setValue",searchParam);
    initResults();
    initBind();
});

function initBind() {
    $('body').bind('keydown', function (event) {
        if(event.keyCode=="13"){
            searchData(1);
        }
    });

    $(document).bind("click",function(e){
        var target  = $(e.target);    //e.target获取触发事件的元素
        /**以target为起点向上查找父（祖）元素，若父（祖）元素中包含#phone,#first中一个就不执行if中语句，即长度不为0
         **.closest()沿 DOM 树向上遍历(以数组形式入参)，直到找到已应用选择器的一个匹配为止，返回包含零个或一个元素的 jQuery 对象。
         **/
        if(target.closest(".optionsDiv,#timeVal").length == 0){
            $(".optionsDiv").css('display','none');
        };

        //e.stopPropagation();
    })

}

function searchData(curPage){
    logId = null;
    var searchKey = $("#searchKey").textbox("getValue");
    searchParam = searchKey;
    restTime();
    cleanData();
    $.ajax({
        type:"POST",
        url: context+"searchResult/searchData",
        data:{searchKey:searchKey,pageNumber:curPage,searchTime:"all",logType:"search"},
        dataType: 'json',
        success: function(data) {
            if(data != null && data.reqFlag){
                logId = data.logSearchId;
                if(data.reqTotal == 0){
                    $("#noResultsDiv").css("display","block");
                }else{
                    $("#pageDiv").css("display","block");
                    $("#totalAndTime").css("display","block");
                    $("#showTotal").text(data.reqTotal+"个结果");
                    var pageCount = Math.ceil(data.reqTotal/10);
                    initPageAssembly(pageCount,curPage);
                    //var searchResults = data.reqResults;
                    pageData = data.fivePageData;
                    var searchResults = data.fivePageData.page1;
                    var length = searchResults.length;
                    for(var i = 0;i<length;i++){
                        var appendHtml = "<div style='margin-top: 10px;'>";
                        if(searchResults[i]._source.filedir.substring(0,4) === 'http'){
                            appendHtml += "<div><a url='"+searchResults[i]._source.filedir+"' href='"+searchResults[i]._source.filedir+"' class='aStyle' target='_blank' onclick='clickLinkUrl(1,"+i+",this)'>"+searchResults[i]._source.filename+"</a></div>";
                        }else{
                            if(ieFlag){
                                appendHtml += "<div><a url='"+searchResults[i]._source.filedir+"' href='"+searchResults[i]._source.filedir+"' class='aStyle' target='_blank' onclick='clickLinkUrl(1,"+i+",this)'>"+searchResults[i]._source.filename+"</a></div>";
                            }else{
                                appendHtml += "<div><a url='"+searchResults[i]._source.filedir+"' href='javascript:void(0);' class='aStyle' onclick='clickLinkUrl(1,"+i+",this)'>"+searchResults[i]._source.filename+"</a></div>";
                            }
                        }
                        appendHtml += "<div class='contentStyle'>"+searchResults[i].allStr+"</div>";
                        appendHtml += "<div style='color: green;' onclick='divBorder(this);' class='dirStyle'>"+searchResults[i]._source.filedir+"</div></div>";
                        $("#searchResultsDiv").append(appendHtml);
                    }
                }
            }else{
                $("#errorDiv").css("display","block");
            }
        }
    });
}

function initResults(){
    if(searchFlag){
        if(total == 0){
            $("#noResultsDiv").css("display","block");
            $("#pageDiv").css("display","none");
            $("#totalAndTime").css("display","none");
        }else{
            var pageCount = Math.ceil(total/10);
            initPageAssembly(pageCount,1);
            var initData = pageData.page1;
            //var length = results.length;
            var length = initData.length;
            for(var i = 0;i<length;i++){
                var appendHtml = "<div style='margin-top: 10px;'>";
                if(initData[i]._source.filedir.substring(0,4) === 'http'){
                    appendHtml += "<div ><a url='"+initData[i]._source.filedir+"' href='"+initData[i]._source.filedir+"' class='aStyle' target='_blank' onclick='clickLinkUrl(1,"+i+",this)' >"+initData[i]._source.filename+"</a></div>";
                }else{
                    if(ieFlag){
                        appendHtml += "<div ><a url='"+initData[i]._source.filedir+"' href='"+initData[i]._source.filedir+"' class='aStyle' target='_blank' onclick='clickLinkUrl(1,"+i+",this)' >"+initData[i]._source.filename+"</a></div>";
                    }else{
                        appendHtml += "<div ><a url='"+initData[i]._source.filedir+"' href='javascript:void(0);' class='aStyle'  onclick='clickLinkUrl(1,"+i+",this)' >"+initData[i]._source.filename+"</a></div>";
                    }
                }
                appendHtml += "<div class='contentStyle'>"+initData[i].allStr+"</div>";
                appendHtml += "<div style='color: green;' onclick='divBorder(this);' class='dirStyle'>"+initData[i]._source.filedir+"</div></div>";
                $("#searchResultsDiv").append(appendHtml);
            }
        }
    }else{
        $("#pageDiv").css("display","none");
        $("#errorDiv").css("display","block");
        $("#totalAndTime").css("display","none");
    }
}

function initPageAssembly(pageCount,curPage){
    $(".pagination").bootstrapPaginator({
        //设置版本号
        bootstrapMajorVersion: 3,
        // 显示第几页
        currentPage: curPage,
        // 总页数
        totalPages: pageCount,
        /*shouldShowPage:function (type, page, current) {
            if(type === "first" || type === "last"){
                return true;
            }else{
                return true;
            }
        },*/
        itemContainerClass:function (type, pg, current) {
            if(type === "first" || type === "last"){
                return "showFlag";
            }else{
                return pg === current ? "active":"";
            }
        },
        //当单击操作按钮的时候, 执行该函数, 调用ajax渲染页面
        onPageClicked: function (event,originalEvent,type,page) {
            var curPageDta = pageData[('page'+page)];
            var searchKey = $("#searchKey").textbox("getValue");
            //alert(page);
            //alert(typeof(curPageDta));
            //alert(curPageDta);
            if(curPageDta != undefined && curPageDta != null && curPageDta.length > 0 && searchKey == searchParam){
                $("#searchResultsDiv").empty();
                //initPageAssembly(pageCount,page);
                var length = curPageDta.length;
                for(var i = 0;i<length;i++){
                    var appendHtml = "<div style='margin-top: 10px;'>";
                    if(curPageDta[i]._source.filedir.substring(0,4) === 'http'){
                        appendHtml += "<div ><a url='"+curPageDta[i]._source.filedir+"' href='"+curPageDta[i]._source.filedir+"' class='aStyle' target='_blank' onclick='clickLinkUrl("+page+","+i+",this)'>"+curPageDta[i]._source.filename+"</a></div>";
                    }else{
                        if(ieFlag){
                            appendHtml += "<div ><a url='"+curPageDta[i]._source.filedir+"' href='"+curPageDta[i]._source.filedir+"' class='aStyle' target='_blank' onclick='clickLinkUrl("+page+","+i+",this)'>"+curPageDta[i]._source.filename+"</a></div>";
                        }else {
                            appendHtml += "<div ><a url='"+curPageDta[i]._source.filedir+"' href='javascript:void(0);' class='aStyle'  onclick='clickLinkUrl("+page+","+i+",this)'>"+curPageDta[i]._source.filename+"</a></div>";
                        }
                    }
                    appendHtml += "<div class='contentStyle'>"+curPageDta[i].allStr+"</div>";
                    appendHtml += "<div style='color: green;' onclick='divBorder(this);' class='dirStyle'>"+curPageDta[i]._source.filedir+"</div></div>";
                    $("#searchResultsDiv").append(appendHtml);
                }

            }else{
                searchParam = searchKey;
                //var time = $("#searchTime").val();
                $("#searchResultsDiv").empty();
                $("#noResultsDiv").css("display","none");
                $("#errorDiv").css("display","none");
                $.ajax({
                    type:"POST",
                    url: context+"searchResult/searchData",
                    data:{searchKey:searchKey,pageNumber:page,searchTime:searchTimeType,startTime:begin,endTime:end,logType:"click",clickType:type,clickSearchId:logId},
                    dataType: 'json',
                    success: function(data) {
                        if(data != null && data.reqFlag){
                            if(data.reqTotal == 0){
                                $("#pageDiv").css("display","none");
                                $("#totalAndTime").css("display","none");
                                $("#errorDiv").css("display","block");
                            }else{
                                var length = data.reqResults.length;
                                if(length == 0){
                                    $("#pageDiv").css("display","none");
                                    $("#totalAndTime").css("display","none");
                                    $("#errorDiv").css("display","block");
                                }else{
                                    $("#showTotal").text(data.reqTotal+"个结果");
                                    var count = Math.ceil(data.reqTotal/10);
                                    initPageAssembly(count,page);
                                    //var searchResults = data.reqResults;
                                    pageData = data.fivePageData;
                                    var searchResults = data.fivePageData['page'+page]
                                    for(var i = 0;i<length;i++){
                                        var appendHtml = "<div style='margin-top: 10px;'>";
                                        if(searchResults[i]._source.filedir.substring(0,4) === 'http'){
                                            appendHtml += "<div ><a url='"+searchResults[i]._source.filedir+"' href='"+searchResults[i]._source.filedir+"' class='aStyle' target='_blank' onclick='clickLinkUrl("+page+","+i+",this)'>"+searchResults[i]._source.filename+"</a></div>";
                                        }else{
                                            if(ieFlag){
                                                appendHtml += "<div ><a url='"+searchResults[i]._source.filedir+"' href='"+searchResults[i]._source.filedir+"' class='aStyle' target='_blank' onclick='clickLinkUrl("+page+","+i+",this)'>"+searchResults[i]._source.filename+"</a></div>";
                                            }else {
                                                appendHtml += "<div ><a url='"+searchResults[i]._source.filedir+"' href='javascript:void(0);' class='aStyle'  onclick='clickLinkUrl("+page+","+i+",this)'>"+searchResults[i]._source.filename+"</a></div>";
                                            }
                                        }
                                        appendHtml += "<div class='contentStyle'>"+searchResults[i].allStr+"</div>";
                                        appendHtml += "<div style='color: green;' onclick='divBorder(this);' class='dirStyle'>"+searchResults[i]._source.filedir+"</div></div>";
                                        $("#searchResultsDiv").append(appendHtml);
                                    }
                                }
                            }
                        }else{
                            $("#errorDiv").css("display","block");
                            $("#pageDiv").css("display","none");
                            $("#totalAndTime").css("display","none");
                        }
                    }
                });
            }

        }
    })
}

function divBorder(elem) {
    $(elem).css("border","1px solid #808080");
}

function cleanData(){
    $("#searchResultsDiv").empty();
    $("#totalAndTime").css("display","none");
    $("#pageDiv").css("display","none");
    $("#noResultsDiv").css("display","none");
    $("#errorDiv").css("display","none");
}

/*function changeSearchTime() {
    var searchKey = $("#searchKey").textbox("getValue");
    var time = $("#searchTime").val();
    cleanData();
    $.ajax({
        type:"POST",
        url: context+"searchResult/searchData",
        data:{searchKey:searchKey,pageNumber:1,searchTime:time,logType:"click",clickType:"changTime",clickSearchId:logId},
        dataType: 'json',
        success: function(data) {
            if(data != null && data.reqFlag){
                if(data.reqTotal == 0){
                    //$("#noResultsDiv").css("display","block");
                    $("#totalAndTime").css("display","block");
                    $("#showTotal").text("0个结果");
                }else{
                    $("#pageDiv").css("display","block");
                    $("#totalAndTime").css("display","block");
                    $("#showTotal").text(data.reqTotal+"个结果");
                    var pageCount = Math.ceil(data.reqTotal/10);
                    initPageAssembly(pageCount,1);
                    var searchResults = data.reqResults;
                    var length = searchResults.length;
                    for(var i = 0;i<length;i++){
                        var appendHtml = "<div style='margin-top: 10px;'>";
                        if(searchResults[i]._source.filedir.substring(0,4) === 'http'){
                            appendHtml += "<div><a  url='"+searchResults[i]._source.filedir+"' href='"+searchResults[i]._source.filedir+"' class='aStyle' target='_blank' onclick='clickLinkUrl(1,"+i+",this)'>"+searchResults[i]._source.filename+"</a></div>";
                        }else{
                            if(ieFlag){
                                appendHtml += "<div><a  url='"+searchResults[i]._source.filedir+"' href='"+searchResults[i]._source.filedir+"' class='aStyle' target='_blank' onclick='clickLinkUrl(1,"+i+",this)'>"+searchResults[i]._source.filename+"</a></div>";
                            }else{
                                appendHtml += "<div><a  url='"+searchResults[i]._source.filedir+"' href='javascript:void(0);' class='aStyle' onclick='clickLinkUrl(1,"+i+",this)'>"+searchResults[i]._source.filename+"</a></div>";
                            }
                        }
                        appendHtml += "<div class='contentStyle'>"+searchResults[i].allStr+"</div>";
                        appendHtml += "<div style='color: green;' onclick='divBorder(this);' class='dirStyle'>"+searchResults[i]._source.filedir+"</div></div>";
                        $("#searchResultsDiv").append(appendHtml);
                    }
                }
            }else{
                $("#errorDiv").css("display","block");
            }
        }
    });
}*/

function restTime(){
    //var options = $("#searchTime").find("option").attr("selected",false);
    //$("#searchTime").find("option").eq(1).attr("selected",true);
    //$("#searchTime").val("all");
    searchTimeType = 'all';
    initDateBox();
    $("#timeVal").text("全部");

}

function clickLinkUrl(pageNumber,rankNumber,obj){
    var linkUrl = $(obj).attr('url');
    $.ajax({
        type:"POST",
        url: context+"searchResult/insertClickLog",
        data:{pageNumber:pageNumber,clickType:"clickLink",rankNumber:(rankNumber+1),linkUrl:linkUrl,clickSearchId:logId},
        dataType: 'json',
        success: function(data) {
        }
    });
    if(linkUrl.substring(0,4) !== 'http' && !ieFlag ){
        /*$.messager.alert('该浏览器不支持远程打开文件',linkUrl,function () {
            console.info(1);
        });*/

        //$.messager.defaults = { ok: "拷贝地址", cancel: "取消" };
        $.messager.confirm({
            width: 400,
            ok: "拷贝地址",
            cancel: "取消",
            title: '当前浏览器安全机制不允许打开远程文件',
            msg: "IE11以及IE低版本浏览器可以通过超链接直接访问远程文件夹文件，对于谷歌浏览器和火狐浏览器受安全机制限制，需要用户手工通过浏览器访问。请您点击【拷贝地址】按钮，并在新的地址栏粘贴即可访问对应的远程文件夹文件。",
            fn: function (r) {
                if(r){
                    $("#copyUrl").val(linkUrl);
                    $("#copyUrl").attr("type","text");
                    var copyUrl = document.getElementById("copyUrl");
                    copyUrl.select(); // 选择对象
                    document.execCommand("Copy"); // 执行浏览器复制命令
                    $("#copyUrl").attr("type","hidden");
                }
            }
        });

    }
}

function initBrowser(){
    var userAgent = navigator.userAgent;
    var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1; //判断是否IE<11浏览器
    var isEdge = userAgent.indexOf("Edge") > -1 && !isIE; //判断是否IE的Edge浏览器
    var isIE11 = userAgent.indexOf('Trident') > -1 && userAgent.indexOf("rv:11.0") > -1;
    if(isIE || isEdge || isIE11) {
        ieFlag = true;
    }
}

function showTime(){
    var isHide = $(".optionsDiv").css('display');
    if(isHide === 'none'){
        $(".optionsDiv").css('display','block');
    }else{
        $(".optionsDiv").css('display','none');
    }
}

function changeTime(timeType,timeVal) {
    $(".optionsDiv").css('display','none');
    var startTime = $("#startTime").datebox("getValue");
    var endTime = $("#endTime").datebox("getValue");
    if("custom" === timeType){
        //自定义
        var startDate = new Date(Date.parse(startTime));
        var endDate = new Date(Date.parse(endTime));
        if(startDate > endDate){
            $("#startTime").datebox("setValue",endTime);
            $("#endTime").datebox("setValue",startTime);
            $("#timeVal").text(endTime +"~"+startTime);
            begin = endTime;
            end = startTime;
        }else{
            $("#timeVal").text(startTime +"~"+endTime);
            begin = startTime;
            end = endTime;
        }
    }else{
        $("#timeVal").text(timeVal);
    }
    searchTimeType = timeType;

    var searchKey = $("#searchKey").textbox("getValue");
    searchParam = searchKey;
    //var time = $("#searchTime").val();
    cleanData();
    $.ajax({
        type:"POST",
        url: context+"searchResult/searchData",
        data:{searchKey:searchKey,pageNumber:1,searchTime:timeType,startTime:startTime,endTime:endTime,logType:"click",clickType:"changTime",clickSearchId:logId},
        dataType: 'json',
        success: function(data) {
            if(data != null && data.reqFlag){
                if(data.reqTotal == 0){
                    //$("#noResultsDiv").css("display","block");
                    $("#totalAndTime").css("display","block");
                    $("#showTotal").text("0个结果");
                }else{
                    $("#pageDiv").css("display","block");
                    $("#totalAndTime").css("display","block");
                    $("#showTotal").text(data.reqTotal+"个结果");
                    var pageCount = Math.ceil(data.reqTotal/10);
                    initPageAssembly(pageCount,1);
                    //var searchResults = data.reqResults;
                    pageData = data.fivePageData;
                    var searchResults = data.fivePageData.page1;
                    var length = searchResults.length;
                    for(var i = 0;i<length;i++){
                        var appendHtml = "<div style='margin-top: 10px;'>";
                        if(searchResults[i]._source.filedir.substring(0,4) === 'http'){
                            appendHtml += "<div><a  url='"+searchResults[i]._source.filedir+"' href='"+searchResults[i]._source.filedir+"' class='aStyle' target='_blank' onclick='clickLinkUrl(1,"+i+",this)'>"+searchResults[i]._source.filename+"</a></div>";
                        }else{
                            if(ieFlag){
                                appendHtml += "<div><a  url='"+searchResults[i]._source.filedir+"' href='"+searchResults[i]._source.filedir+"' class='aStyle' target='_blank' onclick='clickLinkUrl(1,"+i+",this)'>"+searchResults[i]._source.filename+"</a></div>";
                            }else{
                                appendHtml += "<div><a  url='"+searchResults[i]._source.filedir+"' href='javascript:void(0);' class='aStyle' onclick='clickLinkUrl(1,"+i+",this)'>"+searchResults[i]._source.filename+"</a></div>";
                            }
                        }
                        appendHtml += "<div class='contentStyle'>"+searchResults[i].allStr+"</div>";
                        appendHtml += "<div style='color: green;' onclick='divBorder(this);' class='dirStyle'>"+searchResults[i]._source.filedir+"</div></div>";
                        $("#searchResultsDiv").append(appendHtml);
                    }
                }
            }else{
                $("#errorDiv").css("display","block");
            }
        }
    });

}

function initTime() {
    $('#startTime').datebox('calendar').calendar({
        validator : function(date){
            var now = new Date();
            var d1 = new Date(now.getFullYear(),now.getMonth(),now.getDate());
            return  date<= d1;
            //return false;
        }
    });
    $('#endTime').datebox('calendar').calendar({
        validator : function(date){
            var now = new Date();
            var d1 = new Date(now.getFullYear(),now.getMonth(),now.getDate());
            return  date<= d1;
            //return false;
        }
    });

    initDateBox();

}

function initDateBox(){
    var nowDate = new Date();
    var nowYear = nowDate.getFullYear();
    var nowMonth = nowDate.getMonth()+1;
    var nowDay = nowDate.getDate();
    nowMonth = nowMonth<10? "0"+nowMonth:nowMonth;
    nowDay = nowDay<10? "0"+nowDay:nowDay;
    end = nowYear+"-"+nowMonth+"-"+nowDay;
    $('#endTime').datebox('setValue',end);

    nowDate.setDate(nowDate.getDate()-10);//获取AddDayCount天后的日期
    var startYear = nowDate.getFullYear();
    var startMonth = nowDate.getMonth()+1;
    var startDay = nowDate.getDate();
    startMonth = startMonth<10? "0"+startMonth:startMonth;
    startDay = startDay<10? "0"+startDay:startDay;
    begin = startYear+"-"+startMonth+"-"+startDay;
    $('#startTime').datebox('setValue',begin);
}