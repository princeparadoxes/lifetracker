package com.princeparadoxes.watertracker.data;

import com.princeparadoxes.watertracker.ApplicationScope;
import com.princeparadoxes.watertracker.data.api.RestService;
import com.princeparadoxes.watertracker.data.db.DBService;
import com.princeparadoxes.watertracker.data.sp.ProjectPreferences;

import javax.inject.Inject;

@ApplicationScope
public class DataService {

    private final DBService mDBService;
    private final RestService mRestService;
    private final ProjectPreferences mPreferences;

    @Inject
    public DataService(DBService dbService,
                       RestService restService,
                       ProjectPreferences preferences) {
        this.mDBService = dbService;
        this.mRestService = restService;
        this.mPreferences = preferences;
    }

}
