package com.example.colin.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Model.MomentAdapter;
import Model.MyHttp;
import Model.MyListView;;

import java.util.Arrays;
import java.util.List;

public class PYQActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pyq);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //返回按钮
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.white_back);
        }

        //从主界面接收用户名
        Intent intent = getIntent();
        TextView userName = findViewById(R.id.userName);
        userName.setText(intent.getStringExtra("username"));



        //服务端获取朋友圈信息

        JSONObject getMoment = new JSONObject();
        try {
            getMoment.put("userName",userName.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("请求moment","用户名信息存入JSON异常");
        }
        Log.i("请求moment","准备发送的信息："+getMoment.toString());
        //http请求
        MyHttp myHttp = new MyHttp();
        //handler方法
        Handler momentHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
//                        Toast.makeText(MainActivity.this,"inHandler",Toast.LENGTH_SHORT).show();
                switch(msg.what){
                    case 1:
                        JSONObject moments = (JSONObject) msg.obj;
                        // 在这里进行UI操作，将结果显示到界面上
                        Log.i("主线程hander收到的朋友圈数据",moments.toString());

                        //收到JSONObject，将它转换为JSONArray


                            Toast.makeText(PYQActivity.this,"get moments Success",Toast.LENGTH_SHORT).show();

                            Toast.makeText(PYQActivity.this,"get moments fail",Toast.LENGTH_SHORT).show();

                        break;
                    default:
                        Toast.makeText(PYQActivity.this, "网络连接失败" , Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        //发送http
        String url = "http://172.20.10.2:8080/android_http_servers/GetMoments";
        myHttp.Post(getMoment,momentHandler,url);

//传入网络图片地址  

//        try {  
//            URL url = new URL("http://news.xinhuanet.com/photo/2012-02/09/122675973_51n.jpg");  
//            HttpURLConnection conn= (HttpURLConnection) url.openConnection();  
//            conn.setRequestMethod("GET");  
//            conn.setConnectTimeout(5*1000);  
//            conn.connect();  
//            InputStream in=conn.getInputStream();  
//            ByteArrayOutputStream bos=new ByteArrayOutputStream();  
//            byte[] buffer=new byte[1024];  
//            int len = 0;  
//            while((len=in.read(buffer))!=-1){  
//                bos.write(buffer,0,len);  
//            }  
//            byte[] dataImage=bos.toByteArray();  
//            bos.close();  
//            in.close();  
//            Bitmap bitmap=BitmapFactory.decodeByteArray(dataImage, 0, dataImage.length);  
//            //Drawable drawable=BitmapDrawable.  
////            imageView.setImageBitmap(bitmap);  
//        } catch (Exception e) {  
//            // TODO Auto-generated catch block  
//            e.printStackTrace();  
//            Toast.makeText(getApplicationContext(), "图片加载失败", 1).show();  
//        }





//        ListView moment = findViewById(R.id.momentsList);
        MyListView moment = findViewById(R.id.momentsList);
        List<String> list = Arrays.asList("ax","b","c","d","e","f","ax","b","c","d","e","f");
//        ArrayAdapter<String> myAdapter1 = new ArrayAdapter<String>(PYQActivity.this, R.layout.single_moment,list);
        MomentAdapter momentAdapter = new MomentAdapter(PYQActivity.this, R.layout.single_moment,list);
        moment.setAdapter(momentAdapter);





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pyq,menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_camera:
            //TODO 发朋友圈界面
                Toast.makeText(PYQActivity.this,"camera touched",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PYQActivity.this,PostMoment.class);
                startActivity(intent);
                break;
            case android.R.id.home:
//                Toast.makeText(PYQActivity.this,"home touched",Toast.LENGTH_SHORT).show();
                PYQActivity.this.finish();
                break;

        }
        return true;
    }
}
