package com.princeparadoxes.watertracker;

import com.princeparadoxes.watertracker.data.source.db.DBService;
import com.princeparadoxes.watertracker.data.source.db.repository.DBDrinkRepository;
import com.princeparadoxes.watertracker.data.repository.DrinkRepository;
import com.princeparadoxes.watertracker.data.source.sp.ProjectPreferences;

public interface ProjectGraph {
    void inject(ProjectApplication app);

    DBService provideDBService();

    DBDrinkRepository provideDBDrinkRepositoty();

    DrinkRepository provideDrinkRepository();

    ProjectPreferences provideProjectPreferences();

}
