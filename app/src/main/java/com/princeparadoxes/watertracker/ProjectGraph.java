package com.princeparadoxes.watertracker;

import com.princeparadoxes.watertracker.data.db.DBService;
import com.princeparadoxes.watertracker.data.db.repository.DBDrinkRepository;
import com.princeparadoxes.watertracker.data.repository.DrinkRepository;
import com.princeparadoxes.watertracker.data.sp.ProjectPreferences;

public interface ProjectGraph {
    void inject(ProjectApplication app);

    DBService provideDBService();

    DBDrinkRepository provideDBDrinkRepositoty();

    DrinkRepository provideDrinkRepository();

    ProjectPreferences provideProjectPreferences();

}
