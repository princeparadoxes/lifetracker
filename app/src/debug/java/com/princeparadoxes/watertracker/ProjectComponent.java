package com.princeparadoxes.watertracker;

import com.princeparadoxes.watertracker.data.DebugDataModule;

import dagger.Component;

@ApplicationScope
@Component(modules = {ProjectModule.class, DebugDataModule.class})
public interface ProjectComponent extends DebugProjectGraph {
    /**
     * An initializer that creates the graph from an application.
     */
    final class Initializer {
        static ProjectComponent init(ProjectApplication application) {
            return DaggerProjectComponent.builder()
                    .projectModule(new ProjectModule(application))
                    .build();
        }
        private Initializer() {} // No instances.
    }

    void inject(ProjectApplication debugApp);
}
