package com.example.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {
    //每个item横向间距
    private int mHorizontalSpacing = dp2x(16);
    private int mVerticalSpacing = dp2x(8);
    //每个item竖间距
    String tag = "flowlayout";
    //记录这行已经用了多少宽
    int lineWidethUsed = 0;
    //一行的行高
    int lineHeight = 0;
    private List<List<View>> allLines;
    private List<Integer> allHeights;
    public FlowLayout(Context context) {
        super(context);
        initMeasureParams();

    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMeasureParams();
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void initMeasureParams(){
        allLines = new ArrayList<>();
        allHeights = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        lineWidethUsed=0;
        lineHeight=0;
        allLines.clear();
        allHeights.clear();
        initMeasureParams();

        Log.d(tag,"re");
        //度量所有的子View
        int childCount = getChildCount();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int paddingTop = getPaddingTop();
        int selfWidth = MeasureSpec.getSize(widthMeasureSpec);
        int selfHeight = MeasureSpec.getSize(heightMeasureSpec);
        int parentNeedWidth = 0;//子View要求父View的宽
        int parentNeedHeight = 0;
        List<View> lineView = new ArrayList<>();//保存一行中所有的View

        for(int i = 0;i<childCount;i++){
            View childView = getChildAt(i);
            LayoutParams childLP = childView.getLayoutParams();
            int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,paddingLeft+paddingRight,childLP.width);
            int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,paddingTop+paddingBottom,childLP.height);
            childView.measure(childWidthMeasureSpec,childHeightMeasureSpec);

            //获取子View的宽高
            int childMeasureWidth = childView.getMeasuredWidth();
            int childMeasureHeight = childView.getMeasuredHeight();
            if (childMeasureWidth+lineWidethUsed+mHorizontalSpacing>selfWidth){

                allLines.add(lineView);

                allHeights.add(lineHeight);

                parentNeedHeight = parentNeedHeight+lineHeight+mVerticalSpacing;
                parentNeedWidth = Math.max(parentNeedWidth,lineWidethUsed+mHorizontalSpacing);
                Log.d(tag,lineView.size()+"");

                lineView = new ArrayList<>();
                lineWidethUsed = 0;
                lineHeight = 0;
            }

            lineView.add(childView);
            if (i==11){
                allLines.add(lineView);
                allHeights.add(lineHeight);
                lineWidethUsed=0;
                lineHeight=0;

            }
            Log.d(tag,lineView.size()+"hang"+i);

            lineWidethUsed = lineWidethUsed+childMeasureWidth+mHorizontalSpacing;
            lineHeight = Math.max(lineHeight,childMeasureHeight);


        }


        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int realWidth = (widthMode==MeasureSpec.EXACTLY)?selfWidth:parentNeedWidth;
        int realHeight = (heightMode==MeasureSpec.EXACTLY)?selfHeight:parentNeedHeight;

        //度量和保存自己的size
        setMeasuredDimension(realWidth,realHeight);

    }

    @Override
    protected void onLayout(boolean bool, int l, int t, int r, int b) {
        Log.d(tag,"lay");
        int lineCount = allLines.size();
        int cuL = getPaddingLeft();
        int cuT = getPaddingTop();
        Log.d(tag,allLines.size()+"xsadasdasdasd");
        for (int i =0;i<lineCount;i++)
            List<View> lineViews = allLines.get(i);
            int height = allHeights.get(i);
            for (int j =0;j<lineViews.size();j++){

                View view = lineViews.get(j);
                int left = cuL;
                int top = cuT;
                int right = left+view.getMeasuredWidth();
                int bottom = top+view.getMeasuredHeight();
                view.layout(left,top,right,bottom);
                cuL = right+mHorizontalSpacing;
            }
            cuT = cuT + height + mVerticalSpacing;
            cuL = getPaddingLeft();
        }

    }
    public static int dp2x(int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp, Resources.getSystem().getDisplayMetrics());
    }

}
