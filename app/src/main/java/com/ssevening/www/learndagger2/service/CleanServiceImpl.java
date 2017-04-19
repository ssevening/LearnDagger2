package com.ssevening.www.learndagger2.service;

import android.util.Log;

/**
 * Created by Pan on 2017/4/19.
 */

public class CleanServiceImpl implements CleanService {

    public String clean() {
        Log.d("cleanService", "Please enjoy the clean service!!!!!");
        return "Please enjoy the clean service!!!!!";
    }
}
