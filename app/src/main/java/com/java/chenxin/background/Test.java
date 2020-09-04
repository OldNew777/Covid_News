package com.java.chenxin.background;
import android.util.Log;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.*;

import okhttp3.OkHttp;

public class Test {
    public String url;
    private Map<String, String> _paramMap = new HashMap<String, String>();
    Socket socket;
    public static void main(String[] argv) {


    }
    public Test(){}
    private static class TrustAnyTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
    public String execute(String paramName){
        String msg = "msg: ";
        try{
//            String urlname = url + "?" + paramName + "=" + _paramMap.get(paramName);

            String urlname = "https://covid-dashboard.aminer.cn/api/event/5f05f3f69fced0a24b2f84ee?id=5f05f3f69fced0a24b2f84ee";
            URL realUrl = new URL(urlname);
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },new java.security.SecureRandom());
            HttpsURLConnection httpURLConnection = (HttpsURLConnection) realUrl.openConnection();
            httpURLConnection.setSSLSocketFactory(sc.getSocketFactory());
            httpURLConnection.setHostnameVerifier(new TrustAnyHostnameVerifier());
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(false);
            httpURLConnection.setUseCaches(true);
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            // 设置此 HttpURLConnection 实例是否应该自动执行 HTTP 重定向
            httpURLConnection.setInstanceFollowRedirects(true);
            httpURLConnection.connect();
            int code = httpURLConnection.getResponseCode();
            msg += "code:" + code;
            if(code == 200){
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) { // 循环从流中读取
                    msg += line + "\n";
                }
                reader.close(); // 关闭流
            }
//            Log.i("","###msg: " + msg);

        }
        catch(Exception e){
            msg += "erro:" + e.toString();
        }

        return msg;
    }

}

