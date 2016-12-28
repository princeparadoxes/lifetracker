package com.princeparadoxes.watertracker;

import com.princeparadoxes.watertracker.data.DataService;
import com.princeparadoxes.watertracker.data.db.DBService;
import com.princeparadoxes.watertracker.data.sp.ProjectPreferences;

public interface ProjectGraph {
    void inject(ProjectApplication app);

    DataService provideDataService();

    DBService provideDBService();

    ProjectPreferences provideProjectPreferences();

}
