package Model;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class myHttp {


    public void Post(final JSONObject para, final Handler handler){

        //新建子线程 在子线程中访问服务器
        new Thread(){
            @Override
            public void run() {
                try {
                    //创建url对象
                    URL url = new URL("http://192.168.0.104:8080/android_http_servers/Userlogin");
                    // 打开一个HttpURLConnection连接
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    // 设置为Post请求,POST要大写
                    httpURLConnection.setRequestMethod("POST");
//				    httpURLConnection.setRequestMethod("GET");
                    // 设置连接主机超时时间
                    httpURLConnection.setConnectTimeout(5 * 1000);
                    //设置从主机读取数据超时
                    httpURLConnection.setReadTimeout(5 * 1000);
                    //设置编码格式utf-8 防止中文乱码 已在服务器端设置 注释掉也行
//				    httpURLConnection.setRequestProperty("Content-type", "text/html");
//		    		httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
//			    	httpURLConnection.setRequestProperty("contentType", "utf-8");
                    //设置可以写请求的内容 上传
//				    httpURLConnection.setDoInput(true);//下载
                    httpURLConnection.setDoOutput(true);//上传
                    //将请求内容写入
                    httpURLConnection.getOutputStream().write(para.toString().getBytes());
//                    Log.i("httpURLConnection请求", httpURLConnection.toString());
                    // 开始连接
                    httpURLConnection.connect();

                    //TODO shanchu
                    System.out.print(httpURLConnection.toString());

                    //判断请求是否成功
                    if(httpURLConnection.getResponseCode() == 200){
                        Log.i("http","请求成功");
                        /*
                        //获取返回的数据
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
                        Log.i("收到的数据",stringBuilder.toString());
                        JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                        Log.i("收到的数据转JSON",jsonObject.toString());
                        Message responseJSON = new Message();
                        responseJSON.what = 1;
                        responseJSON.obj = jsonObject;
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
//        Log.i("线程外JSON",jsonObject.toString());

    }


}
