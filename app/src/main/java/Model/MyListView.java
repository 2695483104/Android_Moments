package Model;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class MyListView extends ListView {

    public MyListView(Context context) {
        super(context);
    }
    //函数重载要写 不然会找不到资源文件 "Binary XML file line # : Error inflating class”
    //构造函数问题：自定义一个View，必须派生实现基类View的三个构造函数
    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //设置不滚动
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        改变了的listview的高度获取方式
        /*
        size:表示父布局提供给你的大小参考
        mode:表示规格，有EXACTLY、AT_MOST、UNSPECIFIED三种。
        1. EXACTLY 表示父视图希望子视图的大小应该是由specSize的值来决定的，系统默认会按照这个规则来设置子视图的大小，开发人员当然也可以按照自己的意愿设置成任意的大小。
        2. AT_MOST 表示子视图最多只能是specSize中指定的大小，开发人员应该尽可能小得去设置这个视图，并且保证不会超过specSize。系统默认会按照这个规则来设置子视图的大小，开发人员当然也可以按照自己的意愿设置成任意的大小。
        3. UNSPECIFIED 表示开发人员可以将视图按照自己的意愿设置成任意的大小，没有任何限制。这种情况比较少见，不太会用到。

        Integer.MAX_VALUE >> 2：表示父布局给的参考的大小无限大。（listview无边界）
        MeasureSpec.AT_MOST：表示根据布局的大小来确定listview最终的高度，也就是有多少内容就显示多高。
        */
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
