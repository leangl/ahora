package com.lanacion.ahora.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapterFactory;
import com.lanacion.ahora.Ahora;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class DataStore {

    private static DataStore sInstance;

    private static final String DATA = "data";
    private static final String TIMESTAMP = "timestamp";

    private final Context mCtx;
    private Gson mGson;

    private DataStore() {
        mCtx = Ahora.getInstance();
    }

    public static final DataStore getInstance() {
        if (sInstance == null) {
            sInstance = new DataStore();
        }
        return sInstance;
    }

    public <T> Set<T> getAll(Class<T> type) {
        SharedPreferences prefs = getSharedPreferences(type);
        Map<String, ?> all = prefs.getAll();

        Set<T> result = new HashSet<>(all.size());
        for (Object value : all.values()) {
            String data = (String) value;
            JsonObject element = parseStringData(data);
            result.add(fromJson(type, element));
        }
        return result;
    }

    public <T> T getObject(Object key, Class<T> type) throws ObjectNotFoundException {
        return fromJson(type, getRootEntry(key, type));
    }

    public boolean contains(Object object) {
        return contains(object, object.getClass());
    }

    public boolean contains(Object key, Class<?> type) {
        return getSharedPreferences(type).contains(resolveKey(key));
    }

    public long getTimestamp(Object key, Class<?> type) throws ObjectNotFoundException {
        return getRootEntry(key, type).get(TIMESTAMP).getAsLong();
    }

    private JsonObject getRootEntry(Object key, Class<?> type) throws ObjectNotFoundException {
        SharedPreferences prefs = getSharedPreferences(type);
        String data = prefs.getString(resolveKey(key), null);
        if (data == null) {
            throw new ObjectNotFoundException(type, key);
        }
        return parseStringData(data);
    }

    private JsonObject parseStringData(String data) {
        return new JsonParser().parse(data).getAsJsonObject();
    }

    private <T> T fromJson(Class<T> type, JsonObject element) {
        return getParser().fromJson(element.get(DATA), type);
    }

    private String resolveKey(Object key) {
        return key.hashCode() + "";
    }

    /**
     * Used when object implements hashCode and equals.
     * <p>
     * TODO should include Class parameter to avoid persisting subtypes into different stores
     *
     * @param object
     */
    public void putObject(Object object) {
        putObject(object, object);
    }

    /**
     * When calling this method be sure to implement hashCode correctly!!!
     *
     * @param objects
     * @param type
     * @param <T>
     */
    public <T> void putAll(Collection<T> objects, Class<T> type) {

        SharedPreferences.Editor editor = getSharedPreferences(type).edit();

        for (T object : objects) {
            JsonObject root = new JsonObject();
            root.add(DATA, getParser().toJsonTree(object));
            root.addProperty(TIMESTAMP, System.currentTimeMillis());
            editor.putString(resolveKey(object), root.toString());
        }

        editor.commit();
    }

    /**
     * Used when object does not implements hashCode and equal and uses an additional key
     * <p>
     * TODO should include Class parameter to avoid persisting subtypes into different stores
     *
     * @param object
     */
    public void putObject(Object key, Object object) {
        SharedPreferences prefs = getSharedPreferences(object.getClass());

        JsonObject root = new JsonObject();
        root.add(DATA, getParser().toJsonTree(object));
        root.addProperty(TIMESTAMP, System.currentTimeMillis());

        if (!prefs.edit().putString(resolveKey(key), root.toString()).commit()) {
            throw new RuntimeException("Not persisted!");
        }
    }

    public <T> void deleteAll(Class<T> type) {
        getSharedPreferences(type).edit().clear().commit();
    }

    public <T> void delete(Class<T> type, Object key) {
        getSharedPreferences(type).edit().remove(resolveKey(key)).commit();
    }

    private SharedPreferences getSharedPreferences(Class<?> type) {
        return mCtx.getSharedPreferences(type.getCanonicalName(), Context.MODE_PRIVATE);
    }

    private List<TypeAdapterFactory> mAdapters = new ArrayList<>();

    public <T> void registerTypeWithSubtypes(Class<T> type, Class<? extends T>... subtypes) {
        RuntimeTypeAdapterFactory<T> adapter = RuntimeTypeAdapterFactory.of(type, "_type_");
        for (Class<? extends T> subtype : subtypes) {
            adapter.registerSubtype(subtype);
        }
        mAdapters.add(adapter);

        mGson = null; //invalidate current parser
    }

    public Gson getParser() {
        if (mGson == null) {
            GsonBuilder builder = new GsonBuilder();
            for (TypeAdapterFactory adapter : mAdapters) {
                builder.registerTypeAdapterFactory(adapter);
            }
            mGson = builder.create();
        }
        return mGson;
    }

    public static class ObjectNotFoundException extends Exception {
        public ObjectNotFoundException(Class<?> type, Object key) {
            super("Object not found - TYPE: " + type.getCanonicalName() + "; KEY: " + key.toString());
        }
    }

}
