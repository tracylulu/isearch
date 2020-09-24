$(function () {
    //initRangeTable();
    initBind();
});

function initBind(){
    $('body').bind('keydown', function (event) {
        if(event.keyCode=="13"){
            searchResult();
        }
    });
}

function initRangeTable() {
    $('#searchRangeTable').datagrid({
        //url:'globalQuery/searchRangeTable',
        //method:'get',
        fitColumns:true,
        nowrap:false,
        scrollbarSize:0,
        columns:[[
            {field:'name',title:'数据源名称',width:'40%',align:'left',halign:'center'},
            {field:'addr',title:'数据源地址',width:'60%',align:'left',halign:'center'}
        ]]
    });

    var data = [
        {name:'Comware文件服务器',addr:'\\\\10.153.3.50\\软件平台3\\COMWAREV700R001'},
        {name:'Comware文件服务器',addr:'\\\\10.153.3.50\\软件平台3\\V7DEV'},
        {name:'Comware文件服务器',addr:'\\\\H3cbjnt15-fs\\软件平台\\h3crnd01-fs\\软件部公共培训资料库'},
        {name:'Comware文件服务器',addr:'\\\\H3cbjnt15-fs\\软件平台\\h3crnd01-fs\\软件部规范'},
        {name:'Comware文件服务器',addr:'\\\\H3cbjnt15-fs\\软件平台\\h3crnd01-fs\\软件部内部培训资料库'},
        {name:'Comware文件服务器',addr:'\\\\H3cbjnt15-fs\\软件平台2\\h3c-fs-spare01\\CodeAuth\\经验总结\\每周展播'},
        {name:'Comware开发部站点',addr:'http://rdsp.h3c.com/comware/SitePages/Home.aspx'},
        {name:'配置管理之窗',addr:'http://rdsp.h3c.com/cmoplatform/_layouts/15/start.aspx'},
        {name:'Comware技术部信息',addr:'http://sft/info/'},
        {name:'产品资料文件服务器',addr:'\\\\H3c-infoserver'}
    ];
    $('#searchRangeTable').datagrid('loadData',data);
}

/*function searchResult(){
    var searchParam = $("#searchParam").val();
    window.location.href = context+"searchResult/getSearchResultView?searchParam="+encodeURI(searchParam);
}*/


//发送POST请求跳转到指定页面
function httpPost(URL, PARAMS) {
    var temp = document.createElement("form");
    temp.action = URL;
    temp.method = "post";
    temp.style.display = "none";

    for (var x in PARAMS) {
        var opt = document.createElement("textarea");
        opt.name = x;
        opt.value = PARAMS[x];
        temp.appendChild(opt);
    }

    document.body.appendChild(temp);
    temp.submit();

    return temp;
}

function searchResult(){
	var searchParam = $("#searchParam").val();
    var params = {
        "searchParam": searchParam
    };

    httpPost(context + "searchResult/getSearchResultView", params);
}
