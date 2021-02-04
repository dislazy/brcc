/*
 * Copyright (C) 2020 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.brcc.utils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baidu.brcc.model.R;
import com.baidu.brcc.model.RList;
import com.baidu.brcc.utils.gson.GsonUtils;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

@Service
public class OkHttpClientUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(OkHttpClientUtils.class);
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    private OkHttpClient okHttpClient = null;

    public OkHttpClientUtils(long readTimeOut, long connectionTimeOut) {
        okHttpClient = new okhttp3.OkHttpClient
                .Builder()
                .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
                .connectTimeout(connectionTimeOut, TimeUnit.MILLISECONDS)
                .build();
    }

    public <T> R<T> get(
            String url,
            Class<T> type,
            Map<String, Object> param,
            Map<String, String> header
    ) throws IOException {
        Request.Builder builder = new Request.Builder().url(addParam(url, param));
        if (header != null && !header.isEmpty()) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                String key = entry.getKey();
                if (key == null) {
                    continue;
                }
                String value = entry.getValue();
                builder.addHeader(key, value);
            }
        }
        Request request = builder.build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            ResponseBody body = response.body();
            String result = body.string();
            return GsonUtils.toRObject(result, type);
        }
    }

    public <T> RList<T> getList(
            String url,
            Class<T> type,
            Map<String, Object> param,
            Map<String, String> header
    ) throws IOException {
        Request.Builder builder = new Request.Builder().url(addParam(url, param));
        if (header != null && !header.isEmpty()) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                String key = entry.getKey();
                if (key == null) {
                    continue;
                }
                String value = entry.getValue();
                builder.addHeader(key, value);
            }
        }
        Request request = builder.build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            ResponseBody body = response.body();
            String result = body.string();
            return GsonUtils.toRList(result, type);
        }
    }

    public <T> R<T> postJson(String url,
                             Class<T> type,
                             String body,
                             Map<String, Object> param,
                             Map<String, String> header) throws IOException {
        Request.Builder builder = new Request.Builder().url(addParam(url, param));
        if (header != null && !header.isEmpty()) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                String key = entry.getKey();
                if (key == null) {
                    continue;
                }
                String value = entry.getValue();
                builder.addHeader(key, value);
            }
        }

        RequestBody bodyx = RequestBody.create(JSON, body);
        Request request = builder.post(bodyx).build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            ResponseBody responseBody = response.body();
            String result = responseBody.string();
            return GsonUtils.toRObject(result, type);
        }
    }

    private HttpUrl addParam(String url, Map<String, Object> param) {
        HttpUrl httpUrl = HttpUrl.parse(url);
        if (param == null || param.isEmpty()) {
            return httpUrl;
        }
        HttpUrl.Builder builder = httpUrl.newBuilder();
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            String key = entry.getKey();
            if (key == null) {
                continue;
            }
            Object value = entry.getValue();
            builder.removeAllQueryParameters(key)
                    .addQueryParameter(key, value == null ? "" : value.toString());
        }
        return builder.build();
    }
}