package com.h3c.isearch.util;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Map;




///////////////////////////////


import org.apache.http.HttpResponse;






/**
 * @program: isearch-base
 * @description:
 * @author: zhanghao
 * @create: 2020-05-26 16:32
 **/
public class HttpUtil {

    public static Map<String,Object> sendPost(String Url,String params){
        Map<String,Object> resultMap = null;
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(Url);
            if("prod".equals(Constant.esEnvironment)){
                httpPost.addHeader("Authorization", "Basic " + Base64.getUrlEncoder().encodeToString((Constant.httpAuthKey + ":" + Constant.httpAuthSecret).getBytes()));
            }
            httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json");
           /* httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(params),
                    ContentType.create("text/json", "UTF-8")));*/
            httpPost.setEntity(new StringEntity(params,
                    ContentType.create("text/json", "UTF-8")));
            client = HttpClients.createDefault();
            response = client.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            //System.out.println(result);
            //JSONObject jsonObject = JSON.parseObject(result);
            resultMap = (Map<String, Object>) JSONObject.parse(result);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultMap;
    }

}
