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

import model.HttpHelp;
import model.MomentAdapter;
import model.MyHttp;
import model.MyListView;
import model.UserHelp;

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
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.white_back);
        }
        Intent intent = getIntent();
        userName = findViewById(R.id.userName);
        userName.setText(intent.getStringExtra(UserHelp.userName));
        moment = findViewById(R.id.momentsList);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View footerView = inflater.inflate(R.layout.listview_footer, null);
        moment.addFooterView(footerView);
        showMoment();
    }

    /**
     * 加载右上角相机Item
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pyq,menu);
        return true;
    }

    /**
     * actionBar
     * 左上角返回键 finish当前activity
     * 右上角相机 跳转编辑朋友圈界面
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_camera:
                Intent intent = new Intent(PYQActivity.this,PostMoment.class);
                intent.putExtra(UserHelp.userName,userName.getText().toString());
                startActivityForResult(intent,1);
                break;
            case android.R.id.home:
                PYQActivity.this.finish();
                break;
            default:
        }
        return true;
    }

    /**
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
            getMoment.put(UserHelp.requestCode,UserHelp.requestCode_get_moment);
            getMoment.put(UserHelp.userName,userName.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyHttp myHttp = new MyHttp();
        Handler momentHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                List<JSONObject> momentsList = new ArrayList<>();
                switch(msg.what){
                    case 1:
                        JSONArray jsonArray = (JSONArray) msg.obj;
                        for(int i=0 ; i < jsonArray.length() ;i++)
                        {
                            JSONObject moment = null;
                            try {
                                moment = jsonArray.getJSONObject(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            momentsList.add(moment);
                        }
                        showMomentsList(momentsList);
                        break;
                    default:
                }
                return false;
            }
        });
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
