package com.cheda.skysevents.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.cheda.skysevents.model.SkyEvent;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Cheda on 2017-05-07.
 */

public class SharedPreference {
    public static final String PREFS_NAME ="SKYSEVENTS_APP";
    public static final String FAVORITES = "Events_Favorite";
    public SharedPreference() {
        super();
    }
// These four methods are used for maintaining favorites.

    public void saveFavorites(Context context,List<SkyEvent> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);
        editor.putString(FAVORITES, jsonFavorites);
        editor.commit();
//        editor.apply();
    }
    public void addFavorite(Context context, SkyEvent events) {
        List<SkyEvent> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<>();
        favorites.add(events);
        saveFavorites(context, favorites);
    }
    public void removeFavorite(Context context,SkyEvent events) {
        ArrayList<SkyEvent> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(events);
            saveFavorites(context, favorites);

        }
    }
    public ArrayList<SkyEvent> getFavorites(Context context) {
        SharedPreferences settings;
        List<SkyEvent> favorites;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            SkyEvent[] favoriteItems = gson.fromJson(jsonFavorites,SkyEvent[].class);
            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<>(favorites);
        } else
            return null;
        return (ArrayList<SkyEvent>) favorites;
    }
}
