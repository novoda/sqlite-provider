package com.novoda.sqliteprovider.demo.domain;

import android.text.TextUtils;

import java.io.Serializable;

public class UseCaseInfo implements Serializable {

    private final String uri;
    private final String sql;

    public UseCaseInfo(String uri, String sql) {
        this.uri = uri;
        this.sql = sql;
    }

    public String getUri() {
        return uri;
    }

    public String getSql() {
        return sql;
    }

    public boolean isValid() {
        return !TextUtils.isEmpty(uri) && !TextUtils.isEmpty(sql);
    }

    public static UseCaseInfo getNullSafe() {
        return new UseCaseInfo("", "");
    }
}
