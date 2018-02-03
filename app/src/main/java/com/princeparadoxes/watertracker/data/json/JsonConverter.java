package com.princeparadoxes.watertracker.data.json;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.princeparadoxes.watertracker.ApplicationScope;
import com.squareup.moshi.Moshi;

import java.io.IOException;

import javax.inject.Inject;

import okio.BufferedSource;

@ApplicationScope
public class JsonConverter {

    private final Moshi mMoshi;
    private final Gson mGson;

    @Inject
    public JsonConverter(Moshi moshi, Gson gson) {
        this.mMoshi = moshi;
        this.mGson = gson;
    }

    public <T> String toJson(Class<T> clazz, T t) {
        return mMoshi.adapter(clazz).toJson(t);
//        return mGson.toJson(clazz);
    }

    public <T> T fromJson(Class<T> clazz, String json) throws JsonParseException {
        try {
            return mMoshi.adapter(clazz).fromJson(json);
//            return mGson.fromJson(json, clazz);
        } catch (IOException | JsonSyntaxException e) {
            throw new JsonParseException(e);
        }
    }

    public <T> T fromJson(Class<T> clazz, BufferedSource bufferedSource) throws JsonParseException {
        try {
            return mMoshi.adapter(clazz).fromJson(bufferedSource);
        } catch (IOException | JsonSyntaxException e) {
            throw new JsonParseException(e);
        }
    }

}
