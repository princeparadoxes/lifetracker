package com.princeparadoxes.watertracker;

import com.princeparadoxes.watertracker.data.source.db.service.DrinkDatabaseService;
import com.princeparadoxes.watertracker.data.repository.DrinkRepository;
import com.princeparadoxes.watertracker.data.source.sp.ProjectPreferences;
import com.princeparadoxes.watertracker.domain.DomainDependencies;

public interface ProjectGraph extends DomainDependencies {
    void inject(ProjectApplication app);

    DrinkDatabaseService provideDrinkDatabaseService();

    ProjectPreferences provideProjectPreferences();

}
