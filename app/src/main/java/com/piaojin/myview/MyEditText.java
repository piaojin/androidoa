package com.piaojin.myview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import oa.piaojin.com.androidoa.R;

public class MyEditText extends EditText {

    private Drawable drawable;
    private String edit_str;
    //这个构造函数会被默认调用
    public MyEditText(Context context, AttributeSet attrs) {
        //这里构造方法也很重要，不加这个很多属性不能再XML里面定义
        super(context, attrs);
        init();
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyEditText(Context context) {
        super(context);
    }

    private void init(){
        setClearIconVisible(false);
        this.addTextChangedListener(new EditTextListener());
        this.setOnTouchListener(new MyTouchListener());
    }

    //绘制清除按钮
    private void setClearIconVisible(boolean visible){
        drawable=getResources().getDrawable(R.drawable.clear_text);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        if(visible){
            setCompoundDrawables(null, null, drawable, null);
        }else{
            setCompoundDrawables(null, null, null, null);
        }
    }

    private class EditTextListener implements TextWatcher{

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub
            MyEditText.this.edit_str=MyEditText.this.getText().toString();
            if(MyEditText.this.edit_str!=null&&!MyEditText.this.edit_str.equals("")){
                setClearIconVisible(true);
            }else{
                setClearIconVisible(false);
            }
        }
        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub

        }
        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
            // TODO Auto-generated method stub

        }
    }

    /**
     * * 当手指抬起的位置在clean的图标的区域
     * * 我们将此视为进行清除操作
     * * getWidth():得到控件的宽度
     * * event.getX():抬起时的坐标(改坐标是相对于控件本身而言的)
     * * getTotalPaddingRight():clean的图标左边缘至控件右边缘的距离
     * *getPaddingRight():clean的图标右边缘至控件右边缘的距离
     * * 于是:
     * * getWidth() - getTotalPaddingRight()表示:
     * * 控件左边到clean的图标左边缘的区域
     * * getWidth() - getPaddingRight()表示:
     * * 控件左边到clean的图标右边缘的区域
     * * 所以这两者之间的区域刚好是clean的图标的区域
     * */
    private class MyTouchListener implements OnTouchListener{

        @Override
        public boolean onTouch(View edit, MotionEvent event) {
            // TODO Auto-generated method stub
            if(event.getAction() == MotionEvent.ACTION_UP){
                if(MyEditText.this.drawable!=null){
                    boolean touchable=event.getX()>(getWidth()-getTotalPaddingRight())
                            &&event.getX()<(getWidth() - getPaddingRight());
                    if(touchable){
                        if(getHint()!=null){
                            MyEditText.this.setHint("    ");
                            MyEditText.this.setText("");
                        }else{
                            MyEditText.this.setText("    ");
                        }
                        setClearIconVisible(false);
                    }
                }
            }
            return false;
        }

    }
}
