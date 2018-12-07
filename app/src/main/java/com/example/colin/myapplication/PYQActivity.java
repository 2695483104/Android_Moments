package com.example.colin.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import Model.MomentAdapter;
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

        TextView newTitel = findViewById(R.id.newTitle);
        ImageView userIcon = findViewById(R.id.userIcon);
        //TODO toolbar 背景图片取不到
        ImageView backGround = findViewById(R.id.image);
        //TODO 头像在 toolbar下方 渐变


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        //服务端获取朋友圈信息





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
