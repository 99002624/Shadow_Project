package com.example.textdisplay;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    Button B1;
    EditText E1;
  ViewGroup V1;
    private final static int START_DRAGGING = 0;
    private final static int STOP_DRAGGING = 1;
    private int status;
    int flag=0;
    float xAxis = 0f;
    float yAxis = 0f;
    float lastXAxis = 0f;
    float lastYAxis = 0f;
    int numberOfLines=0;
    int i=0;

//    RelativeLayout R1;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        B1 = (Button) findViewById(R.id.button);
        E1 = (EditText) findViewById(R.id.editTextTextPersonName);
//        E1.setVisibility(View.INVISIBLE);
        E1.setVisibility(View.GONE);
        V1 = (RelativeLayout) findViewById(R.id.Relative);
//        R1.setOnTouchListener((View.OnTouchListener) this);
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(150, 50);
//        layoutParams.leftMargin = 50;
//        layoutParams.topMargin = 50;
//        layoutParams.bottomMargin = -250;
//        layoutParams.rightMargin = -250;
//        E1.setLayoutParams(layoutParams);



        E1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent me) {
                if(me.getAction()==MotionEvent.ACTION_DOWN){
                    status = START_DRAGGING;
                    final float x = me.getX();
                    final float y = me.getY();
                    lastXAxis = x;
                    lastYAxis = y;
                    v.setVisibility(View.INVISIBLE);
                }else if(me.getAction()==MotionEvent.ACTION_UP){
                    status = STOP_DRAGGING;
                    flag=0;
                    v.setVisibility(View.VISIBLE);
                }else if(me.getAction()==MotionEvent.ACTION_MOVE){
                    if (status == START_DRAGGING){
                        flag=1;
                        v.setVisibility(View.VISIBLE);
                        final float x = me.getX();
                        final float y = me.getY();
                        final float dx = x - lastXAxis;
                        final float dy = y - lastYAxis;
                        xAxis += dx;
                        yAxis += dy;
                        v.setX((int)xAxis);
                        v.setY((int)yAxis);
                        v.invalidate();
                    }
                }
                return false;
            }
        });




//        B1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                i++;
////                Add_Line(i);
//                if(E1.getVisibility()==View.GONE){
//                    E1.setVisibility(View.VISIBLE);
//                }
////                else if(E1.getVisibility()==View.VISIBLE){
////                    E1.setVisibility(View.GONE);
////                }
//            }
//        });





    }
    public void kiran(View v) {
        if(E1.getVisibility()==View.GONE){
                   E1.setVisibility(View.VISIBLE);
        }
        // does something very interesting
    }

    public void Add_Line( int i) {

        int leftMargin = 500;
        int topMargin = 500;
        //leftMargin+=120;
        //topMargin+=130;
        RelativeLayout ll = (RelativeLayout)findViewById(R.id.Relative);
        // add edittext
       // int i =1;
        if(i==1) {
            EditText et = new EditText(this);
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(leftMargin, topMargin);
            et.setLayoutParams(p);
            et.setText("Text");
            et.setId(numberOfLines + 1);
            ll.addView(et);
            numberOfLines++;
        }
        if(i==2){
            EditText ete = new EditText(this);
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(leftMargin, topMargin);
            ete.setLayoutParams(p);
            ete.setText("Text123");
            ete.setId(numberOfLines + 1);
            ll.addView(ete);

        }

    }
}