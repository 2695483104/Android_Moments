package Model;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyHttp {

    URL url = null;
    public void Post(final JSONObject para, final Handler handler, final String urlString){

        //创建url对象
        try {
            Log.i("指定URL","创建URL对象");
            if (urlString != null) {
                url = new URL(urlString);
            } else {
                Log.i("无指定URLPost","创建默认URL对象");
                url = new URL("http://172.20.10.2:8080/android_http_servers/User");
            }
        }catch (MalformedURLException e) {
            e.printStackTrace();
            Log.i("创建url对象异常", e.toString());
        }

        //新建子线程 在子线程中访问服务器
        new Thread(){
            @Override
            public void run() {
                try {
                    // 打开一个HttpURLConnection连接
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    // 设置为Post请求,POST要大写
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setRequestMethod("GET");
                    // 设置连接主机超时时间
                    httpURLConnection.setConnectTimeout(5 * 1000);
                    //设置从主机读取数据超时
                    httpURLConnection.setReadTimeout(5 * 1000);
                    //设置编码格式utf-8 防止中文乱码 已在服务器端设置 注释掉也行
                    httpURLConnection.setRequestProperty("Content-type", "text/html");
                    httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
                    httpURLConnection.setRequestProperty("contentType", "utf-8");
                    //设置可以写请求的内容 上传
                    httpURLConnection.setDoInput(true);//下载
                    httpURLConnection.setDoOutput(true);//上传
                    //将请求内容写入
                    if (para != null){
                        httpURLConnection.getOutputStream().write(para.toString().getBytes());
                    }
                    // 开始连接
                    httpURLConnection.connect();
                    Log.i("子线程http请求",httpURLConnection.toString());

                    //判断请求是否成功
                    if(httpURLConnection.getResponseCode() == 200){
                        Log.i("http","请求成功");
                        /*
                        获取返回的数据
                        InputStream inputStream = httpURLConnection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"utf-8");
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        */
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8"));


                        //将返回的数据InputStream转换为JSONObject
                        StringBuilder stringBuilder = new StringBuilder();
                        String inputStr;
                        while ((inputStr = bufferedReader.readLine()) != null){
                            stringBuilder.append(inputStr);
                        }
                        Log.i("子线程收到数据转stringBuilder",stringBuilder.toString());
                        JSONArray jsonArray = new JSONArray(stringBuilder.toString());;
                        Log.i("收到的数据转JSONArray",jsonArray.toString());
                        //返回JSONObject到主线程
                        Message responseJSON = new Message();
                        responseJSON.what = 1;
                        responseJSON.obj = jsonArray;
                        handler.sendMessage(responseJSON);
                        }else{
                        Log.i("http请求失败ResponseCode",httpURLConnection.getResponseCode()+"");
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Log.i("子线程IO/JSON报异常",e.toString());
                }
            }
        }.start();
    }
    public void Post(final JSONObject para, final Handler handler){
        Post(para,handler,null);
    }
}
