package com.example.matt.defweb;

/**
 * Created by Matt on 02/11/2014.
 * Class to define a definition object - like those stored in SQLite database
 */

public class Definition {
    private long id;
    private String def_name;
    private String img_loc;

    public long getId() {
        return id;
    }
    public void setId(long newId) {
        id = newId;
    }

    public String getDefName() {
        return def_name;
    }
    public void setDefName(String name) {
        def_name = name;
    }

    public String getImgLoc() {
        return img_loc;
    }
    public void setImgLoc(String imgLoc) {
        img_loc = imgLoc;
    }

    //Used by listview adapters
    @Override
    public String toString() {
        return def_name;
    }
}
