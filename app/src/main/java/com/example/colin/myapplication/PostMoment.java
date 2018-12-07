package com.example.colin.myapplication;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
                Toast.makeText(PostMoment.this,"post touched",Toast.LENGTH_SHORT).show();


                break;
            case android.R.id.home:
//                Toast.makeText(PYQActivity.this,"home touched",Toast.LENGTH_SHORT).show();
                PostMoment.this.finish();
                break;

        }
        return true;
    }
}
