package com.candy.tool.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.candy.tool.R;
import com.candy.tool.bean.Feedback;
import com.candy.tool.utils.StatConstant;
import com.candy.tool.utils.StatUtil;
import com.tool.librecycle.activity.BaseActivity;
import com.tool.librecycle.utils.ToastUtils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * File description
 *
 * @author gao
 * @date 2018/6/2
 */

public class FeedbackActivity extends BaseActivity implements View.OnClickListener {

    private EditText mContenEt;
    private EditText mEmailEt;

    @Override
    public int getLayoutId() {
        return R.layout.layout_feedback;
    }

    @Override
    public void initViews() {
        mContenEt = findViewById(R.id.feedback_content_et);
        mEmailEt = findViewById(R.id.email_et);
        final TextView submitTv = findViewById(R.id.feedback_submit);
        submitTv.setOnClickListener(this);
        mContenEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    submitTv.setEnabled(true);
                } else if (start == 0) {
                    submitTv.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void initData() {
        StatUtil.onEvent(StatConstant.FEEDBACK_ENTER);
    }

    @Override
    public void onClick(View v) {
        String feedbackContent = mContenEt.getText().toString();
        if (TextUtils.isEmpty(feedbackContent)
                || feedbackContent.length() < 8) {
            ToastUtils.showToastForShort(FeedbackActivity.this, R.string.feedback_more);
            return;
        }
        String emailStr = mEmailEt.getText().toString().trim();
        if (!TextUtils.isEmpty(emailStr) && !Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            ToastUtils.showToastForShort(FeedbackActivity.this, R.string.fb_wrong_format_email);
            return;
        }
        Feedback feedback = new Feedback();
        feedback.setContent(feedbackContent);
        feedback.setEmail(emailStr);
        saveFeedback(feedback);
    }

    private void saveFeedback(Feedback feedback) {
        feedback.save(new SaveListener<String>() {

            @Override
            public void done(String objectId, BmobException exception) {
                if (exception != null) {
                    ToastUtils.showToastForShort(FeedbackActivity.this, getString(R.string.recommend_fail));
                    return;
                }
                ToastUtils.showToastForShort(FeedbackActivity.this, getString(R.string.feedbak_success));
                mContenEt.setText("");
                mContenEt.setText("");
            }
        });
    }
}
