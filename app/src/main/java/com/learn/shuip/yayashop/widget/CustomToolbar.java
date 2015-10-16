package com.learn.shuip.yayashop.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.internal.widget.TintManager;
import android.support.v7.internal.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.learn.shuip.yayashop.R;

/**
 * Created by Administrator on 15-10-12.
 */
public class CustomToolbar extends Toolbar{

    private final static String TAG = "CustomToolbar";
    private LayoutInflater mInflater;

    private EditText mSearchView;
    private TextView mTextTitle;
    private ImageButton mRightButton;
    private ImageButton mLeftButton;
    private View mView;

    private TintManager mTintManager;

    public CustomToolbar(Context context) {
        this(context, null);
    }

    public CustomToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
        setContentInsetsRelative(10, 10);
        if(attrs !=null) {
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.CustomToolbar, defStyleAttr, 0);
            mTintManager = a.getTintManager();

            final Drawable rightIcon = a.getDrawable(R.styleable.CustomToolbar_rightButtonIcon);
            if (rightIcon != null) {
                setRightButtonIcon(rightIcon);
            }

            boolean isShowSearchView = a.getBoolean(R.styleable.CustomToolbar_isShowSearchView,false);
            if(isShowSearchView){
                showSearchView();
                hideTitleView();
            }
            a.recycle();
        }
    }

    private void initView() {
        if(mView == null) {
            mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.toolbar, null);

            mTextTitle = (TextView) mView.findViewById(R.id.toolbar_title);
            mSearchView = (EditText) mView.findViewById(R.id.toolbar_searchview);
            mRightButton = (ImageButton) mView.findViewById(R.id.toolbar_rightButton);
            mLeftButton = (ImageButton) mView.findViewById(R.id.toolbar_leftButton);

            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
            addView(mView, lp);
        }
    }


    public void  setRightButtonIcon(Drawable icon){
        if(mRightButton !=null){
            mRightButton.setImageDrawable(icon);
            mRightButton.setVisibility(VISIBLE);
        }
    }

    public  void setRightButtonOnClickListener(OnClickListener listener){
        mRightButton.setOnClickListener(listener);
    }

    @Override
    public void setTitle(int resId) {
        setTitle(getContext().getText(resId));
    }
    @Override
    public void setTitle(CharSequence title) {
        initView();
        if(mTextTitle != null) {
            mTextTitle.setText(title);
            showTitleView();
        }
    }

    @Override
    public void setNavigationIcon(int resId) {
        setNavigationIcon(mTintManager.getDrawable(resId));
    }

    @Override
    public void setNavigationIcon(Drawable icon) {
        initView();
        if (mLeftButton != null){
            if (icon != null){
                mLeftButton.setImageDrawable(icon);
                mLeftButton.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void setNavigationOnClickListener(OnClickListener listener) {
        if (mLeftButton != null){
            mLeftButton.setOnClickListener(listener);
        }
    }

    public void showNavigationIcon(){
        if (mLeftButton != null){
            mLeftButton.setVisibility(View.VISIBLE);
        }
    }

    public  void showSearchView(){
        if(mSearchView !=null)
            mSearchView.setVisibility(VISIBLE);
    }

    public void hideSearchView(){
        if(mSearchView !=null)
            mSearchView.setVisibility(GONE);
    }

    public void showTitleView(){
        if(mTextTitle !=null)
            mTextTitle.setVisibility(VISIBLE);
    }

    public void hideTitleView() {
        if (mTextTitle != null)
            mTextTitle.setVisibility(GONE);

    }
}
