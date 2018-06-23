package com.candy.tool.view;


import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import com.candy.tool.R;


public class TabTitleView extends LinearLayout implements Checkable {

    private boolean checked = false;
    private CheckedTextView textView;
    private Context mContext;

    public TabTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init("");
    }

    public TabTitleView(Context context, CharSequence text) {
        super(context, null);
        mContext = context;
        init(text);
    }

    private void init(CharSequence text) {
        textView = new CheckedTextView(getContext());
        textView.setText(text);
        textView.setTextSize(16);
        textView.setSingleLine();
        textView.setEllipsize(TextUtils.TruncateAt.END);
        //noinspection deprecation

        if (mContext.getString(R.string.title_user_candy).equals(text)) {
//            int vipIcon = VpnData.isVipUser() ? R.drawable.ic_rocket_checked : R.drawable.tab_roket_selector;
//            textView.setCompoundDrawablePadding(RobotUtil.getPixel(getContext(), 6));
//            textView.setCompoundDrawablesWithIntrinsicBounds(vipIcon, 0, 0, 0);
//            textView.setTextColor(getResources().getColorStateList(R.color.tab_title_user_color));
        } else {
//            textView.setCompoundDrawablePadding(RobotUtil.getPixel(getContext(), 6));
//            textView.setCompoundDrawablesWithIntrinsicBounds(vipIcon, 0, 0, 0);
        }
        textView.setTextColor(getResources().getColorStateList(R.color.tab_title_user_color));

        textView.setGravity(Gravity.BOTTOM);

        setGravity(Gravity.CENTER);
        addView(textView);
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
        this.textView.setChecked(checked);
        this.invalidate();
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        setChecked(!checked);
    }
}
