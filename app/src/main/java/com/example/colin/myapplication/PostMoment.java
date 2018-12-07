package com.example.colin.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Model.MyHttp;

public class PostMoment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_moment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.postToolbar);
        setSupportActionBar(toolbar);
        //返回按钮
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.black_back);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_post:
                //TODO 发朋友圈
                Toast.makeText(PostMoment.this,"Sending ... ",Toast.LENGTH_SHORT).show();
                uploadMoment();
                break;
            case android.R.id.home:
//                Toast.makeText(PYQActivity.this,"home touched",Toast.LENGTH_SHORT).show();
                PostMoment.this.finish();
                break;

        }
        return true;
    }

    //发朋友圈到服务器
    public void uploadMoment(){
        //获取朋友圈文字
        TextView momentText = findViewById(R.id.momentText);
        String text = momentText.getText().toString();

        //新建要发送到JSON
        JSONObject moment = new JSONObject();
        //接收从上一个界面传来到用户名
        Intent intent = getIntent();
        try {
            moment.put("requestCode",2);//requestCode 2 上传朋友圈信息
            //传入用户名！
            moment.put("userName",intent.getStringExtra("userName"));
            //传入文本
            moment.put("text",text);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //发送
        MyHttp myHttp = new MyHttp();
        Handler uploadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
//                        Toast.makeText(MainActivity.this,"inHandler",Toast.LENGTH_SHORT).show();
                switch(msg.what){
                    case 1:
                        JSONArray jsonArray = (JSONArray) msg.obj;
                        JSONObject response = null;
                        try {
                            response = jsonArray.getJSONObject(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("post主线程hander收到的数据",response.toString());
                        //朋友圈发送成功情况
                        Boolean postSuccess = false;
                        //获取JSONObject中用发朋友圈返回的结果
                        try {
                            postSuccess = response.getBoolean("postResult");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("upload Handle","JSONObject.getBoolean获取发朋友圈返回结果异常");
                        }
                        //朋友圈发送成功提示 返回上一层 失败提示
                        if (postSuccess){
                            Toast.makeText(PostMoment.this,"post Success",Toast.LENGTH_SHORT).show();
                        }else{

                            Toast.makeText(PostMoment.this,"post failed",Toast.LENGTH_SHORT).show();
                        }
                }
                return false;
            }
        });
        //发送http
        String url = "http://172.20.10.2:8080/android_http_servers/Moments";
        myHttp.Post(moment,uploadHandler,url);

    }
}
