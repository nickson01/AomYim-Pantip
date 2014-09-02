package com.nantaphop.pantipfanapp.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import com.nantaphop.pantipfanapp.BaseApplication;
import com.nantaphop.pantipfanapp.service.PantipRestClient;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.jsoup.Connection;

/**
 * Created by nantaphop on 08-Aug-14.
 */
@EFragment
public class BaseFragment extends Fragment {
    @App
    protected BaseApplication app;
    @Bean
    protected PantipRestClient client;
    private Activity activity;

    @Override
    public void onAttach(Activity activity) {
        this.activity = activity;
        super.onAttach(activity);
    }


    public Activity getAttachedActivity() {
        return activity;
    }
}