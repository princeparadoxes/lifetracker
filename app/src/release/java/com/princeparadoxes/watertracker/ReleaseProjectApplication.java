package com.princeparadoxes.watertracker;


public class ReleaseProjectApplication extends ProjectApplication {

    private static final String YANDEX_METRICA_API_KEY = "c880d341-3503-48a7-9cf6-2eacf9c62d6c";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void buildComponentAndInject() {
        super.buildComponentAndInject();
        component().inject(this);
    }

}
