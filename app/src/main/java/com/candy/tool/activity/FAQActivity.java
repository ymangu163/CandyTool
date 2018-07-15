package com.candy.tool.activity;

import android.widget.ExpandableListView;

import com.candy.tool.R;
import com.candy.tool.adapter.FaqAdapter;
import com.candy.tool.bean.Config;
import com.candy.tool.bean.FaqEntry;
import com.candy.tool.utils.CandyUtil;
import com.candy.tool.utils.GsonUtil;
import com.candy.tool.utils.StatConstant;
import com.candy.tool.utils.StatUtil;
import com.tool.librecycle.activity.BaseActivity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * File description
 *
 * @author gao
 * @date 2018/3/5
 */

public class FAQActivity extends BaseActivity {
    private ExpandableListView mFaqListView;
    private FaqAdapter mFaqAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_help;
    }

    @Override
    public void initViews() {
        mFaqListView = findViewById(R.id.help_expan_list);

        mFaqAdapter = new FaqAdapter(this);
        mFaqListView.setAdapter(mFaqAdapter);
    }

    @Override
    public void initData() {
        StatUtil.onEvent(StatConstant.HELP_ENTER);

        BmobQuery<Config> query = new BmobQuery<Config>();
        query.addQueryKeys("faq");

        query.getObject("4sbCWWWv", new QueryListener<Config>() {

            @Override
            public void done(Config object, BmobException e) {
                if (e != null) {
                    String faqJson = CandyUtil.getJsonFromAssets(FAQActivity.this,"faq.json");
                    List<FaqEntry> faqList = GsonUtil.gson2List(faqJson, FaqEntry.class);
                    mFaqAdapter.setFaqList(faqList);
                    return;
                }
                List<FaqEntry> faqList = GsonUtil.gson2List(object.getFaq(), FaqEntry.class);
                if (faqList != null) {
                    mFaqAdapter.setFaqList(faqList);
                }
            }
        });

    }
}
