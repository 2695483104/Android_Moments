package model;

import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 自定义网络请求类
 * 在子线程中实现
 */
public class MyHttp {
    private URL url = null;
    /**
     * Post方法发送请求
     * @param para 请求要携带的参数
     * @param handler 请求结果返回的数据会包装成JSONArray 返回给主线程用于接收的Handler
     * @param urlString 要请求的地址 可以为空
     */
    public void Post(final JSONObject para, final Handler handler, final String urlString){

        try {
            if (urlString != null) {
                url = new URL(urlString);
            } else {
                url = new URL(HttpHelp.userURL);
            }
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }
        new Thread(){
            @Override
            public void run() {
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod(String.valueOf(HttpHelp.Method_POST));
                    httpURLConnection.setConnectTimeout(HttpHelp.TIME_OUT);
                    httpURLConnection.setReadTimeout(HttpHelp.TIME_OUT);
                    httpURLConnection.setRequestProperty(HttpHelp.request_roperty_content_type, HttpHelp.content_type_text_html);
                    httpURLConnection.setRequestProperty(HttpHelp.request_roperty_accept_character, HttpHelp.character_type_utf_8);
                    httpURLConnection.setRequestProperty(HttpHelp.request_roperty_contentType, HttpHelp.character_type_utf_8);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    if (para != null){
                        httpURLConnection.getOutputStream().write(para.toString().getBytes());
                    }
                    httpURLConnection.connect();
                    if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),HttpHelp.character_type_utf_8));
                        StringBuilder stringBuilder = new StringBuilder();
                        String inputStr;
                        while ((inputStr = bufferedReader.readLine()) != null){
                            stringBuilder.append(inputStr);
                        }
                        JSONArray jsonArray = new JSONArray(stringBuilder.toString());
                        Message responseJSON = new Message();
                        responseJSON.what = 1;
                        responseJSON.obj = jsonArray;
                        handler.sendMessage(responseJSON);
                        }else{
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    /**
     * 不指定URL地址的Post方法
     * 默认访问用户注册验证地址
     */
    public void Post(final JSONObject para, final Handler handler){
        Post(para,handler,null);
    }
}
