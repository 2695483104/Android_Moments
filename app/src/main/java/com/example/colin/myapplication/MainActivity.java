package com.example.colin.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import model.MyHttp;
import model.UserHelp;


public class MainActivity extends AppCompatActivity {
    ImageView icon;
    TextView registerIcon;
    LinearLayout loginLayout;
    LinearLayout registerLayout;
    Button submitButton;
    TextView swapText;
    Boolean isLogin = true;

    TextInputLayout registerNameLayout;
    EditText registerName = null;
    TextInputLayout registerPasswordLayout;
    EditText registerPassword = null;
    TextInputLayout registerPhoneLayout;
    EditText registerPhone = null;

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
        registerNameLayout = findViewById(R.id.registerNameLayout);
        registerName = findViewById(R.id.registerName);
        registerPasswordLayout = findViewById(R.id.registerPasswordLayout);
        registerPassword = findViewById(R.id.registerPassword);
        registerPhoneLayout = findViewById(R.id.registerPhoneLayout);
        registerPhone = findViewById(R.id.registerPhone);
        //设置登录&注册页面切换按钮点击事件
        swapText.setOnClickListener(new swapOnClickListener());
        //登录&注册 按钮事件
        submitButton.setOnClickListener(new submitOnClickListener());
        //注册验证
        registerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registerNameCheck();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        registerPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registerPasswordCheck();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        registerPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registerPhoneCheck();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    /**
     * 登录&注册界面切换方法
     */
    class swapOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(isLogin){
                toRegisterLayout();
            }else{
                toLoginLayout();
            }
        }
    }

    /**
     * 展示设置登录界面所需的控件及文本 隐藏其他
     */
    public void toLoginLayout(){
        icon.setVisibility(View.VISIBLE);
        registerIcon.setVisibility(View.GONE);
        loginLayout.setVisibility(View.VISIBLE);
        registerLayout.setVisibility(View.GONE);
        submitButton.setText(R.string.login_text_activity_main);
        swapText.setText(R.string.register_text_activity_main);
        isLogin = true;
    }

    /**
     * 展示注册界面所需的控件及文本 隐藏其他
     */
    public void toRegisterLayout(){
        registerName.setText("");
        registerPassword.setText("");
        registerPhone.setText("");
        icon.setVisibility(View.GONE);
        registerIcon.setVisibility(View.VISIBLE);
        loginLayout.setVisibility(View.GONE);
        registerLayout.setVisibility(View.VISIBLE);
        submitButton.setText(R.string.register_text_activity_main);
        swapText.setText(R.string.login_text_activity_main);
        isLogin = false;
        registerName.requestFocus();
        registerNameLayout.setErrorEnabled(false);
        registerPasswordLayout.setErrorEnabled(false);
        registerPhoneLayout.setErrorEnabled(false);
    }


    /**
     * 对注册输入的姓名进行验证
     * @return 输入框输入的正确性
     */
    public boolean registerNameCheck(){
        if(registerName.length()==0){
            registerNameLayout.setErrorEnabled(true);
            registerNameLayout.setError(getString(R.string.name_input_check_error_info));
        }else{
            registerNameLayout.setErrorEnabled(false);
        }
        return !registerNameLayout.isErrorEnabled();
    }

    /**
     * 对注册输入的密码进行验证
     * @return 输入框输入的正确性
     */
    public boolean registerPasswordCheck() {
        if(registerPassword.length() < 6){
            registerPasswordLayout.setErrorEnabled(true);
            registerPasswordLayout.setError(getString(R.string.passwd_input_check_error_info));

        }else{
            registerPasswordLayout.setErrorEnabled(false);
        }
        return !registerPasswordLayout.isErrorEnabled();
    }

    /**
     * 对注册输入的姓名进行验证
     * @return 输入框输入的正确性
     */
    public boolean registerPhoneCheck() {
        if(registerPhone.length() != 11){
            registerPhoneLayout.setErrorEnabled(true);
            registerPhoneLayout.setError(getString(R.string.phone_input_check_error_info));
        }else{
            registerPhoneLayout.setErrorEnabled(false);
        }
        return !registerPhoneLayout.isErrorEnabled();
    }

    /**
     * 按钮点击事件
     * 登录和注册的实现函数
     */
    class submitOnClickListener implements  View.OnClickListener{
        @Override
        public void onClick(View v){
            if(isLogin){
                final EditText username = findViewById(R.id.name);
                final EditText password = findViewById(R.id.password);
                JSONObject user = new JSONObject();
                try {
                    user.put(UserHelp.requestCode, UserHelp.requestCode_login);

                    user.put(UserHelp.userName, username.getText().toString() );
                    user.put(UserHelp.password,password.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MyHttp myHttp = new MyHttp();
                Handler loginHandler = new Handler(new Handler.Callback() {
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
                                Boolean legalUser = false;
                                try {
                                    legalUser = Objects.requireNonNull(response).getBoolean(UserHelp.legalUser);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (legalUser){
                                    Intent intent = new Intent(MainActivity.this,PYQActivity.class);
                                    intent.putExtra(UserHelp.userName,username.getText().toString());
                                    startActivity(intent);
                                    password.setText("");
                                    Toast.makeText(MainActivity.this,R.string.login_success_toast_activity_main,Toast.LENGTH_SHORT).show();
                               }else{
                                    password.setText("");
                                    Toast.makeText(MainActivity.this,R.string.login_fail_toast_activity_main,Toast.LENGTH_SHORT).show();
                                }
                            default:
                        }
                        return false;
                    }
                });
                //发送http
                myHttp.Post(user,loginHandler);

            }else if ( registerNameCheck() && registerPasswordCheck() && registerPhoneCheck() ){
                registerName = findViewById(R.id.registerName);
                registerPassword = findViewById(R.id.registerPassword);
                registerPhone = findViewById(R.id.registerPhone);
                JSONObject newUser = new JSONObject();
                try {
                    newUser.put(UserHelp.requestCode,UserHelp.requestCode_register);
                    newUser.put(UserHelp.userName,registerName.getText().toString());
                    newUser.put(UserHelp.password,registerPassword.getText().toString());
                    newUser.put(UserHelp.phone,registerPhone.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MyHttp myHttp = new MyHttp();
                Handler registerHandler = new Handler(new Handler.Callback() {
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
                                Boolean legalUser = false;
                                try {
                                    legalUser = Objects.requireNonNull(response).getBoolean(UserHelp.registerResult);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (legalUser){
                                    toLoginLayout();
                                    EditText username = findViewById(R.id.name);
                                    username.setText(registerName.getText());
                                    registerName.setText("");
                                    registerPassword.setText("");
                                    registerPhone.setText("");
                                    Toast.makeText(MainActivity.this,R.string.register_success_toast_activity_main,Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(MainActivity.this,R.string.register_fail_toast_activity_main,Toast.LENGTH_SHORT).show();
                                }
                                break;
                            default:
                        }
                        return false;
                    }
                });
                myHttp.Post(newUser,registerHandler);
            }else{
                Toast.makeText(MainActivity.this, R.string.register_input_check_error_info , Toast.LENGTH_SHORT).show();
            }
        }
    }
}
