package com.example.colin.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Model.HttpHelp;
import Model.MomentAdapter;
import Model.MyHttp;
import Model.MyListView;
import Model.userHelp;

import java.util.ArrayList;
import java.util.List;

public class PYQActivity extends AppCompatActivity {

    TextView userName;
    MyListView moment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pyq);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //返回按钮
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.white_back);
        }

        //从主界面接收用户名
        Intent intent = getIntent();
        userName = findViewById(R.id.userName);
        userName.setText(intent.getStringExtra(userHelp.userName));

        // ListView加footer
        moment = findViewById(R.id.momentsList);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View footerView = inflater.inflate(R.layout.listview_footer, null);
        moment.addFooterView(footerView);

        //服务端获取朋友圈信息展示出来
        showMoment();
    }

    /*
     * 加载右上角相机Item
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pyq,menu);
        return true;
    }

    /*
     * actionBar
     * 左上角返回键 finish当前activity
     * 右上角相机 跳转编辑朋友圈界面
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_camera:
//                Toast.makeText(PYQActivity.this,"camera touched",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PYQActivity.this,PostMoment.class);
                intent.putExtra(userHelp.userName,userName.getText().toString());
                startActivityForResult(intent,1);
                break;
            case android.R.id.home:
//                Toast.makeText(PYQActivity.this,"home touched",Toast.LENGTH_SHORT).show();
                PYQActivity.this.finish();
                break;
            default:
        }
        return true;
    }

    /*
     *成功发送朋友圈后返回自动刷新
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        showMoment();
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 从服务端获取朋友圈信息
     * 调用showMomentsList展示
     */
    public void showMoment(){
        JSONObject getMoment = new JSONObject();
        try {
            getMoment.put(userHelp.requestCode,userHelp.requestCode_get_moment);//requestCode 1 获取朋友圈信息
            getMoment.put(userHelp.userName,userName.getText().toString());
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
                List<JSONObject> momentsList = new ArrayList<>();
                switch(msg.what){
                    case 1:
                        JSONArray jsonArray = (JSONArray) msg.obj;
                        Log.i("主线程hander收到朋友圈jsonArray",jsonArray.toString());
                        //JSONArray 转 List
                        for(int i=0 ; i < jsonArray.length() ;i++)
                        {
                            //获取每一个JsonObject对象
                            JSONObject moment = null;
                            try {
                                moment = jsonArray.getJSONObject(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.i("momentHandler","JSONArray 取 JSONObject 出错");
                            }
                            Log.i("jsonArray中取到的Json",moment.toString());
                            momentsList.add(moment);
                        }
                        Log.i("测试取到的List JSON",momentsList.toString());
                        showMomentsList(momentsList);
                        break;
                    default:
//                        Toast.makeText(PYQActivity.this, "网络连接失败" , Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        //发送http
        myHttp.Post(getMoment,momentHandler, HttpHelp.momentURL);
    }

    /**
     * 接收获取到的朋友圈信息
     * 用ListView展示出来
     * @param momentsList showMoment函数获取到到朋友圈信息
     */
    public void showMomentsList(List<JSONObject> momentsList){
        MomentAdapter momentAdapter = new MomentAdapter(PYQActivity.this, R.layout.single_moment,momentsList);
        moment.setAdapter(momentAdapter);
    }

}
