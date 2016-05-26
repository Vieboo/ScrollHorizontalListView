package vb.shlv.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GestureDetector mGesture;
    // 事件状态
    private final char FLING_NORMAL = 0;
    private final char FLING_CLICK = 1;
    private final char FLING_LEFT = 2;
    private final char FLING_RIGHT = 3;
    private char flingState = FLING_NORMAL;


    private ListView listview;
    private LinearLayout head;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGesture = new GestureDetector(this, mOnGesture);

        head = (LinearLayout) findViewById(R.id.head);
        head.setFocusable(true);
        head.setClickable(true);
        head.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

        List<String> dataList = new ArrayList<>();
        int i=0;
        while(i < 50) {
            i++;
            dataList.add("");
        }
        adapter = new ListAdapter(this, dataList, head);
        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);
        listview.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
    }

    /**
     * 手势
     */
    private GestureDetector.OnGestureListener mOnGesture = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float vX, float vY) {
            if(e2.getX()-e1.getX()>20){
                flingState = FLING_LEFT;

            }else if(e1.getX()-e2.getX()>20){
                flingState = FLING_RIGHT;
            }
            return false;
        }

        /** 滚动 */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,float distanceX, float distanceY) {
            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            flingState = FLING_CLICK;
            return false;
        }
    };


    class ListViewAndHeadViewTouchLinstener implements View.OnTouchListener {
        
        float lastX = 0;
        float lastY = 0;
        private boolean isClick = false;

        @Override
        public boolean onTouch(View arg0, MotionEvent ev) {
            //判断是否是点击
            float tempX = ev.getX();
            float tempY = ev.getY();
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = tempX;
                    lastY = tempY;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(Math.abs(lastX - tempX) > 2 || Math.abs(lastY - tempY) > 2) {
                        isClick = false;
                    }else {
                        isClick = true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if(Math.abs(lastX - tempX) > 2 || Math.abs(lastY - tempY) > 2) {
                        isClick = false;
                    }else {
                        isClick = true;
                    }
                    if(isClick && adapter.getTouchPosition() >= 0) {
                        Toast.makeText(HorizontalScrollXListActivity.this, "position--->" + adapter.getTouchPosition(), Toast.LENGTH_SHORT).show();
                        isClick = false;
                        adapter.setTouchPosition(-1);
                    }
                    break;
            }
            
            // 当在列头 和 listView控件上touch时，将这个touch的事件分发给 ScrollView
            HorizontalScrollView headSrcrollView = (HorizontalScrollView) head.findViewById(R.id.head_scroll);
            headSrcrollView.onTouchEvent(ev);
            mGesture.onTouchEvent(ev);
            return false;
        }

    }
}
