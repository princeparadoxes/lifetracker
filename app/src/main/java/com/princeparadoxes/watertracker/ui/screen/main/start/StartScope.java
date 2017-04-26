package com.princeparadoxes.watertracker.ui.screen.main.start;

import com.princeparadoxes.watertracker.ProjectComponent;
import com.princeparadoxes.watertracker.ui.screen.main.water.WaterFragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface StartScope {

    @StartScope
    @dagger.Component(dependencies = ProjectComponent.class)
    interface Component {
        void inject(StartFragment fragment);
    }

}
