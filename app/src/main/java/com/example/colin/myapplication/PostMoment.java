package com.example.colin.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import model.HttpHelp;
import model.MyHttp;
import model.UserHelp;

public class PostMoment extends AppCompatActivity {

    Boolean shareToQzone = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_moment);
        Toolbar toolbar = findViewById(R.id.postToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.black_back);
        }
        final ImageView qzone = findViewById(R.id.qzone);
        qzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!shareToQzone){
                    qzone.setImageResource(R.drawable.qzone_yes);
                    shareToQzone = true;
                }else{
                    qzone.setImageResource(R.drawable.qzone_no);
                    shareToQzone = false;
                }
            }
        });
    }

    /**
     * 加载编辑朋友圈界面右上角 post按钮
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post,menu);
        return true;
    }

    /**
     * actionBar
     * 左边返回按钮 finish当前activity
     * 右边Post 发送朋友圈
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_post:
                // 发朋友圈
                Toast.makeText(PostMoment.this,R.string.posting_moment_toast_activity_post_moment,Toast.LENGTH_SHORT).show();
                uploadMoment();
                this.finish();
                break;
            case android.R.id.home:
                PostMoment.this.finish();
                break;

        }
        return true;
    }

    /**
     * 发朋友圈信息到服务器
     */
    public void uploadMoment(){
        TextView momentText = findViewById(R.id.momentText);
        String text = momentText.getText().toString();
        JSONObject moment = new JSONObject();
        Intent intent = getIntent();
        try {
            moment.put(UserHelp.requestCode,UserHelp.requestCode_post_moment);
            moment.put(UserHelp.userName,intent.getStringExtra(UserHelp.userName));
            moment.put(UserHelp.text,text);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MyHttp myHttp = new MyHttp();
        Handler uploadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch(msg.what){
                    case 1:
                        JSONArray jsonArray = (JSONArray) msg.obj;
                        JSONObject response = null;
                        try {
                            response = jsonArray.getJSONObject(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Boolean postSuccess = false;
                        try {
                            postSuccess = Objects.requireNonNull(response).getBoolean(UserHelp.postResult);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (postSuccess){
                            Intent result = new Intent();
                            result.putExtra("postSuccess","true");
                            PostMoment.this.setResult(1,result);
                            PostMoment.this.finish();
                        }else{
                            Toast.makeText(PostMoment.this,R.string.moment_post_fail_toast_activity_post_moment,Toast.LENGTH_SHORT).show();
                        }
                }
                return false;
            }
        });
        myHttp.Post(moment,uploadHandler,HttpHelp.momentURL);

    }
}
