package com.h3c.isearch.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @program: iSearch
 * @description:
 * @author: zhanghao
 * @create: 2020-06-28 17:13
 **/
@PropertySource("classpath:sso.properties")
@Component
public class Constant {

    public static String ssoLoginUrl;

    public static String ssoLogoutUrl;

    public static String ssoLoginRequestParam;

    public static String ssoLoginResponsetParam;

    public static String ssoKey;

    public static String sitMainId;

    public static String sitMainUrl;

    public static String httpAuthKey;

    public static String httpAuthSecret;

    public static String esEnvironment;

    public static String esUrl;

    @Value("${sso.main.login.url}")
    public void setSsoLoginUrl(String ssoLoginUrl) {
        Constant.ssoLoginUrl = ssoLoginUrl;
    }

    @Value("${sso.main.logout.url}")
    public void setSsoLogoutUrl(String ssoLogoutUrl) {
        Constant.ssoLogoutUrl = ssoLogoutUrl;
    }

    @Value("${sso.main.login.request.param}")
    public void setSsoLoginRequestParam(String ssoLoginRequestParam) {
        Constant.ssoLoginRequestParam = ssoLoginRequestParam;
    }

    @Value("${sso.main.login.response.param}")
    public void setSsoLoginResponsetParam(String ssoLoginResponsetParam) {
        Constant.ssoLoginResponsetParam = ssoLoginResponsetParam;
    }

    @Value("${sso.main.auth.key}")
    public void setSsoKey(String ssoKey) {
        Constant.ssoKey = ssoKey;
    }

    @Value("${site.main.siteid}")
    public void setSitMainId(String sitMainId) {
        Constant.sitMainId = sitMainId;
    }

    @Value("${site.main.url}")
    public void setSitMainUrl(String sitMainUrl) {
        Constant.sitMainUrl = sitMainUrl;
    }

    @Value("${ES.Key:}")
    public void setHttpAuthKey(String httpAuthKey) {
        Constant.httpAuthKey = httpAuthKey;
    }

    @Value("${ES.Secret:}")
    public void setHttpAuthSecret(String httpAuthSecret) {
        Constant.httpAuthSecret = httpAuthSecret;
    }

    @Value("${spring.profiles.active}")
    public void setEsEnvironment(String esEnvironment) {
        Constant.esEnvironment = esEnvironment;
    }

    @Value("${Es.Url}")
    public void setEsUrl(String esUrl) {
        Constant.esUrl = esUrl;
    }
}
