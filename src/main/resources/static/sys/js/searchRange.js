$(function () {
    initRangeTable();
});

function initRangeTable() {
    $('#searchRangeTable').datagrid({
        url:context+'searchRange/searchRangeTable',
        method:'post',
        fitColumns:true,
        nowrap:false,
        scrollbarSize:0,
        columns:[[
            {field:'datasource_name',title:'数据源名称',width:'25%',align:'left',halign:'center'},
            {field:'datasource_addr',title:'数据源地址',width:'65%',align:'left',halign:'center'},
            {field:'datasource_status',title:'接入状态',width:'10%',align:'left',halign:'center'}
        ]]
    });

    /*var data = [
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
    $('#searchRangeTable').datagrid('loadData',data);*/
}
