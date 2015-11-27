package com.learn.shuip.yayashop.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.learn.shuip.yayashop.R;

;

/**
 * 作者：Create By Administrator on 15-11-24 in com.learn.shuip.yayashop.widget.
 * 邮箱：spd_heshuip@163.com;
 */
public class CustomEditText extends AppCompatEditText implements View.OnTouchListener,View.OnFocusChangeListener ,TextWatcher{

    private Drawable mClearTextIcon;;

    private OnFocusChangeListener mOnfocusChangeListener;

    private OnTouchListener mOnTouchListener;

    public CustomEditText(Context context) {
        super(context);
        init(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context){
        final Drawable drawable = ContextCompat.getDrawable(context, R.drawable.cleartext);
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrappedDrawable,getCurrentHintTextColor());
        mClearTextIcon = wrappedDrawable;
        mClearTextIcon.setBounds(0, 0, mClearTextIcon.getIntrinsicHeight(), mClearTextIcon.getIntrinsicHeight());
        setClearIconVisible(false);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        mOnTouchListener = l;
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        mOnfocusChangeListener = l;
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus){
            setClearIconVisible(getText().length() > 0);
        }else {
            setClearIconVisible(false);
        }
        if (mOnfocusChangeListener != null){
            mOnfocusChangeListener.onFocusChange(v,hasFocus);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int x = (int) event.getX();
        if (mClearTextIcon.isVisible() && x > getWidth() - getPaddingRight() - mClearTextIcon.getIntrinsicWidth()){
            if (event.getAction() == MotionEvent.ACTION_UP){
                setError(null);
                setText("");
            }
            return true;
        }
        return mOnTouchListener != null && mOnTouchListener.onTouch(v,event);
    }

    private void setClearIconVisible(boolean visible){
        Drawable right = visible ? mClearTextIcon : null;
        setCompoundDrawables(getCompoundDrawables()[0],getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (isFocused()){
            setClearIconVisible(text.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
