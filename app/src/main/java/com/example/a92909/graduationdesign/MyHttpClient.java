package com.example.a92909.graduationdesign;


import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyHttpClient {
    private OkHttpClient client = new OkHttpClient();

    private Request.Builder requestBuilder = new Request.Builder();

    private RequestBody body;

    private String url;

    private String method;

    public MyHttpClient() {
    }

    public MyHttpClient(String url, String method) {
        this.url = url;
        this.method = method;
    }

    public MyHttpClient(String url, String method, Map<String, String> headers) {
        this(url, method);
        //添加请求头
        Iterator<Map.Entry<String, String>> entries = headers.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }
    }

    public Response request() {
        Request request = getRequest();
        Call call = client.newCall(request);
        Response response = null;
        try{
            response = call.execute();
        }catch (IOException e){
            e.printStackTrace();
        }
        return response;
    }

    public void setParams(Map<String, Object> params) {
        if (method.equals("GET")) {
            this.url += "?";
            Iterator<Map.Entry<String, Object>> entries = params.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, Object> entry = entries.next();
                this.url = this.url + (entry.getKey() + "=" + entry.getValue());
            }
        } else {
            JSONObject jsonObject = new JSONObject(params);
            String jsonStr = jsonObject.toString();
            MediaType mediaType = MediaType.parse("application/json");
            body = RequestBody.create(mediaType, jsonStr);
        }
    }

    private Request getRequest(){
        Request request;
        if (method.equals("GET")){
            request = requestBuilder.url(url).build();
        }else {
            request = requestBuilder.url(url).post(body).build();
        }
        return request;
    }
}
