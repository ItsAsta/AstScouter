package org.rspeer.gui;

import com.allatori.annotations.DoNotRename;
import com.google.gson.annotations.Expose;

@DoNotRename
public class Profile {
    @Expose
    public String profileName = "yoo";
    @Expose
    public int gpThreshold;
    @Expose
    public boolean hopWorlds;
    @Expose
    public boolean openPacks;


    @Override
    public String toString() {
        return profileName;
    }
}
