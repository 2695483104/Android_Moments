package model;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 重写ListView, 覆盖onMeasure()方法
 * 解决ListView在NestedScrollView冲突
 * ListView只展示一行的问题
 */
public class MyListView extends ListView {

    public MyListView(Context context) {
        super(context);
    }
    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     *重写onMeasure方法
     *达到使ListView适应NestedScrollView的效果
     * MeasureSpec.makeMeasureSpec参数的mode选择EXACTLY 不然会多次调用getView()方法
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
