package com.princeparadoxes.watertracker.data.source.db;

import android.app.Application;

import com.princeparadoxes.watertracker.ApplicationScope;

import javax.inject.Inject;

import io.realm.Realm;

@ApplicationScope
public class DBService {


    @Inject
    public DBService(Application application) {
        Realm.init(application);
    }

}
