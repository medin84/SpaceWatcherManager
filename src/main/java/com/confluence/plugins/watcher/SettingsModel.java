package com.confluence.plugins.watcher;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import java.util.List;

@JsonAutoDetect
public class SettingsModel {

    private GlobalSettings globalSettings;
    private List<SpaceSettings> spaceSettings;

    public GlobalSettings getGlobalSettings() {
        return globalSettings;
    }

    public void setGlobalSettings(GlobalSettings globalSettings) {
        this.globalSettings = globalSettings;
    }

    public List<SpaceSettings> getSpaceSettings() {
        return spaceSettings;
    }

    public void setSpaceSettings(List<SpaceSettings> spaceSettings) {
        this.spaceSettings = spaceSettings;
    }
}
