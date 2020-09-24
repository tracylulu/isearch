package com.h3c.isearch.interceptor;

import com.h3c.isearch.util.Constant;
import com.h3c.sso.entity.RequestTicket;
import com.h3c.sso.entity.ResponseTicket;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: iSearch
 * @description: SSO拦截器
 * @author: zhanghao
 * @create: 2020-06-28 15:16
 **/
public class SsoInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse rsp = (HttpServletResponse)response;

        //检查Session
        if (req.getSession().getAttribute("UserIdentity")==null) {

            //Session中无认证信息，检查请求参数是否包含ResponseTicket
            String responseTicket = req.getParameter(Constant.ssoLoginResponsetParam);
//            System.out.println("responseTicket =========== " +responseTicket);

            //非空则通过Key反序列化ResponseTicket
            if(responseTicket!=null) {

                ResponseTicket respTkt = null;
                try {
                    //调用工具类的com.h3c.sso.entity.ResponseTicket.deserialize(String str, String key)
                    respTkt = ResponseTicket.deserialize(responseTicket, Constant.ssoKey);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(respTkt!=null&&respTkt.getUserIdentity()!=null&&!"".equals(respTkt.getUserIdentity())) {

                    System.out.println("responseTicket From SSO : \n"+respTkt.toString());

                    //取出UserIdentity存入Session
                    String userAcct = respTkt.getUserIdentity();
                    req.getSession(false).setAttribute("userId", userAcct);
                    req.getSession(false).setAttribute("UserIdentity", userAcct);
                }

//				chain.doFilter(request, response);

                //去除请求URL中的ResponseTicket串，防止通过地址栏越过SSO取得ResponseTicket串使子站点获取到认证信息
                String newURI = removeResponseTicketFromURI(req.getRequestURI());

                rsp.sendRedirect(newURI);
                return false;

            } else {//未取到ResponseTicket，则准备构造RequestTicket字符串请求SSO

                String requestTicketStr = null;
                try {

                    //获得RequestTicket实例，设值，序列化
                    RequestTicket requestTicket = RequestTicket.getInstance();
                    requestTicket.setSiteId(Constant.sitMainId);
                    requestTicket.setReturnUrl(Constant.sitMainUrl);
                    requestTicketStr = requestTicket.serialize(Constant.ssoKey);

                    //构造URL重定向到SSO的登陆页
                    rsp.sendRedirect(Constant.ssoLoginUrl+"?"+Constant.ssoLoginRequestParam+"="+requestTicketStr);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        } else {
            //chain.doFilter(request, response);
            return true;
        }


    }

    private String removeResponseTicketFromURI(String uri) {

        int length = uri.length();

        int begin = uri.indexOf(Constant.ssoLoginResponsetParam);

        int end = uri.indexOf("&", begin);

        if(begin<0) {
            return uri;
        } else {
            if(end<0) {
                if(uri.charAt(begin-1)!='?') {
                    return uri.substring(0, begin-1);
                } else {
                    return uri.substring(0, begin-1);
                }
            } else {
                if(uri.charAt(begin-1)!='?') {
                    String part1 = uri.substring(0, begin-1);
                    String part2 = uri.substring(end, length);
                    return part1+part2;

                } else {
                    String part1 = uri.substring(0, begin);
                    String part2 = uri.substring(end+1, length);
                    return part1+part2;
                }
            }
        }

    }
}
