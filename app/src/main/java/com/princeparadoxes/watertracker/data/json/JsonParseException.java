package com.princeparadoxes.watertracker.data.json;

import java.io.IOException;

public class JsonParseException extends IOException {

    public JsonParseException(Exception e) {
        super(e);
    }
}
