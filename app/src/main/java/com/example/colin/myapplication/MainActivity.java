package com.example.colin.myapplication;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import Model.myHttp;


public class MainActivity extends AppCompatActivity {
    ImageView icon;
    TextView registerIcon;
    LinearLayout loginLayout;
    LinearLayout registerLayout;
    Button submitButton;
    TextView swapText;
    Boolean isLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init view
        icon = findViewById(R.id.icon);
        registerIcon = findViewById(R.id.registerIcon);
        loginLayout = findViewById(R.id.loginLayout);
        registerLayout = findViewById(R.id.registerLayout);
        submitButton = findViewById(R.id.Button);
        swapText = findViewById(R.id.swap);
        //设置登录&注册页面切换按钮点击事件
        swapText.setOnClickListener(new swapOnClickListener());
        //登录&注册 按钮事件
        submitButton.setOnClickListener(new submitOnClickListener());
    }

    //登录&注册切换方法
    class swapOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(isLogin){
                //is login layout now, exchange to register layout
                toRegisterLayout();
            }else{
                //is register layout now, exchange to login layout
                toLoginLayout();
            }
        }
    }
    public void toLoginLayout(){
        icon.setVisibility(View.VISIBLE);
        registerIcon.setVisibility(View.GONE);
        loginLayout.setVisibility(View.VISIBLE);
        registerLayout.setVisibility(View.GONE);
        submitButton.setText("LOGIN");
        swapText.setText("REGISTER");
        isLogin = true;
    }
    public void toRegisterLayout(){
        icon.setVisibility(View.GONE);
        registerIcon.setVisibility(View.VISIBLE);
        loginLayout.setVisibility(View.GONE);
        registerLayout.setVisibility(View.VISIBLE);
        submitButton.setText("REGISTER");
        swapText.setText("LOGIN");
        isLogin = false;
    }
    //登录注册实现
    class submitOnClickListener implements  View.OnClickListener{
        @Override
        public void onClick(View v){
            if(isLogin){
                Log.i("按钮","登录点击");
                //Verification
                EditText username = findViewById(R.id.name);
                final EditText password = findViewById(R.id.password);
                //登录信息存入JSON
                JSONObject user = new JSONObject();
                try {
                    user.put("requestCode",1);//requestCode 1 登录验证
                    user.put("username", username.getText().toString() );
                    user.put("password",password.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("登录","登录信息存入JSON异常");
                }

                Log.i("登录","准备发送登录信息："+user.toString());
                //发送服务器
                //http请求
                myHttp myHttp = new myHttp();
                //handler方法
                Handler loginHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
//                        Toast.makeText(MainActivity.this,"inHandler",Toast.LENGTH_SHORT).show();
                        switch(msg.what){
                            case 1:
                                JSONObject response = (JSONObject) msg.obj;
                                // 在这里进行UI操作，将结果显示到界面上
                                Log.i("主线程hander收到的数据",response.toString());
                                //用户合法性
                                Boolean legalUser = false;
                                //获取JSONObject中用户合法性数据
                                try {
                                    legalUser = response.getBoolean("legalUser");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.i("登录Handle","JSONObject.getBoolean获取用户合法性异常");
                                }
                                //合法用户跳转朋友圈 非法用户提示用户名密码错误并清除密码文本框
                                if (legalUser){
                                    //TODO 跳转朋友圈界面
                                    password.setText("");
                                    Toast.makeText(MainActivity.this,"Login Success",Toast.LENGTH_SHORT).show();
                                }else{
                                    password.setText("");
                                    Toast.makeText(MainActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                                }
                        }
                        return false;
                    }
                });
                //发送http
                myHttp.Post(user,loginHandler);

            }else{
                Log.i("按钮","注册点击");
                //TODO register
                final EditText regName = findViewById(R.id.registerName);
                final EditText regPassword = findViewById(R.id.registerPassword);
                final EditText regPhone = findViewById(R.id.registerPhone);
                JSONObject newUser = new JSONObject();
                //注册信息存入JSON
                try {
                    newUser.put("requestCode",2);//requestCode 2 注册
                    newUser.put("username",regName.getText().toString());
                    newUser.put("password",regPassword.getText().toString());
                    newUser.put("phone",regPhone.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("注册","注册信息存入JSON异常");
                }
                Log.i("注册","准备发送注册信息："+newUser.toString());
                //TODO 注册上传
                //发送服务器
                //http请求
                myHttp myHttp = new myHttp();
                //handler方法
                Handler registerHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
//                        Toast.makeText(MainActivity.this,"inHandler",Toast.LENGTH_SHORT).show();
                        switch(msg.what){
                            case 1:
                                JSONObject response = (JSONObject) msg.obj;
                                // 在这里进行UI操作，将结果显示到界面上
                                Log.i("主线程hander收到的数据",response.toString());
                                //注册成功？
                                Boolean legalUser = false;
                                //获取JSONObject中用户合法性数据
                                try {
                                    legalUser = response.getBoolean("registerResult");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.i("注册Handle","JSONObject.getBoolean获取注册信息异常");
                                }
                                //注册成功提并转登录页面 失败提示
                                if (legalUser){
                                    toLoginLayout();
                                    EditText username = findViewById(R.id.name);
                                    username.setText(regName.getText());
                                    regName.setText("");
                                    regPassword.setText("");
                                    regPhone.setText("");
                                    Toast.makeText(MainActivity.this,"Register Success",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(MainActivity.this,"Register fail",Toast.LENGTH_SHORT).show();
                                }
                        }
                        return false;
                    }
                });
                //发送http
                myHttp.Post(newUser,registerHandler);
            }
        }
    }
}
