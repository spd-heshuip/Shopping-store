package com.learn.shuip.yayashop.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.internal.widget.TintTypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.learn.shuip.yayashop.R;

/**
 * Created by Administrator on 15-10-28.
 */
public class CustomAddSubNumberView extends LinearLayout implements View.OnClickListener{

    private LayoutInflater mInflater;

    private Button mAddButton;
    private Button mSubButton;
    private TextView mNumberText;

    private int Value;
    private int MaxValue;
    private int MinValue;

    private OnButtonClickListener mOnButtonClickListener;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_button){
            numAdd();
            if (mOnButtonClickListener != null){
                mOnButtonClickListener.onAddButtonClick(v,Value);
            }
        }else if (v.getId() == R.id.sub_button){
            numSub();
            if (mOnButtonClickListener != null){
                mOnButtonClickListener.onSubButtonClick(v,Value);
            }
        }
    }

    public interface OnButtonClickListener{
        void onAddButtonClick(View view, int value);
        void onSubButtonClick(View view, int value);
    }

    public CustomAddSubNumberView(Context context) {
        this(context,null);
    }

    public CustomAddSubNumberView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomAddSubNumberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
        initView();

        if (attrs != null){
            TintTypedArray a = TintTypedArray.obtainStyledAttributes(context,
                    attrs, R.styleable.CustomAddSubNumberView, defStyleAttr, 0);

            int val = a.getInt(R.styleable.CustomAddSubNumberView_value, 0);
            setValue(val);

            int maxval = a.getInt(R.styleable.CustomAddSubNumberView_maxvalue, 0);
            setMaxValue(maxval);

            int minval =a.getInt(R.styleable.CustomAddSubNumberView_minvalue, 0);
            setMinValue(minval);

            Drawable numTextBackground = a.getDrawable(R.styleable.CustomAddSubNumberView_numberTextBackground);
            setNumberTextBackground(numTextBackground);

            Drawable addButtonBackground = a.getDrawable(R.styleable.CustomAddSubNumberView_addButtonBackground);
            setAddButtonBackground(addButtonBackground);

            Drawable subButtonBackground = a.getDrawable(R.styleable.CustomAddSubNumberView_subButtonBackground);
            setSubButtonBackground(subButtonBackground);

            a.recycle();
        }

    }

    private void initView(){
        View view = mInflater.inflate(R.layout.custom_add_sub_layout, this, true);
        mAddButton = (Button) view.findViewById(R.id.add_button);
        mSubButton = (Button) view.findViewById(R.id.sub_button);
        mNumberText = (TextView) view.findViewById(R.id.number_text);

        mAddButton.setOnClickListener(this);
        mSubButton.setOnClickListener(this);
    }

    private void numAdd(){
        int val = getValue();
        if (val < MaxValue)
            this.Value = val + 1;
        mNumberText.setText(Value + "");
    }

    private void numSub(){
        int val = getValue();
        if (val > MinValue)
            this.Value = val - 1;
        mNumberText.setText(Value + "");
    }

    public int getValue() {
        String val = mNumberText.getText().toString();
        if (!TextUtils.isEmpty(val)){
            this.Value = Integer.parseInt(val);
        }
        return Value;
    }

    public void setValue(int value) {
        mNumberText.setText(value + "");
        Value = value;
    }

    public int getMaxValue() {
        return MaxValue;
    }

    public void setMaxValue(int maxValue) {
        MaxValue = maxValue;
    }

    public int getMinValue() {
        return MinValue;
    }

    public void setMinValue(int minValue) {
        MinValue = minValue;
    }

    public void setAddButtonBackground(Drawable drawable){
        if (drawable != null){
            mAddButton.setBackground(drawable);
        }
    }

    public void setSubButtonBackground(Drawable drawable){
        if (drawable != null){
            mSubButton.setBackground(drawable);
        }
    }

    public void setNumberTextBackground(Drawable drawable){
        if (drawable != null){
            mNumberText.setBackground(drawable);
        }
    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.mOnButtonClickListener = listener;
    }
}
