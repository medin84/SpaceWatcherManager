package com.confluence.plugins.watcher;

import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.google.gson.Gson;

import java.util.Collections;

class SettingsService {
    private final static String SETTINGS_KEY = "com.confluence.plugins.watcher:WATCHER-SPACE-GROUP-SETTINGS";

    private PluginSettings ps;

    SettingsService(PluginSettingsFactory psf) {
        ps = psf.createGlobalSettings();
    }

    SettingsModel get() {
        Object json = ps.get(SETTINGS_KEY);
        if (json == null) {
            return getEmptySettings();
        }
        return new Gson().fromJson(json.toString(), SettingsModel.class);
    }

    void set(SettingsModel sm) throws SettingsValidateException {
        validate(sm);
        ps.put(SETTINGS_KEY, new Gson().toJson(sm));
    }

    boolean hasSettings() {
        SettingsModel settings = get();
        return !settings.getSpaceSettings().isEmpty();
    }

    private SettingsModel getEmptySettings() {
        SettingsModel r = new SettingsModel();
        GlobalSettings gs = new GlobalSettings();
        gs.setExcludeGroups(Collections.emptySet());
        gs.setExcludeUsers(Collections.emptySet());
        r.setGlobalSettings(gs);
        r.setSpaceSettings(Collections.emptyList());
        return r;
    }

    private void validate(SettingsModel settings) throws SettingsValidateException {
        if (settings == null) {
            throw new SettingsValidateException("Settings is null");
        }

        if (settings.getGlobalSettings() == null) {
            throw new SettingsValidateException("Global settings is null");
        }

        if (settings.getSpaceSettings() == null) {
            throw new SettingsValidateException("Space settings is null");
        }

        for (SpaceSettings ss : settings.getSpaceSettings()) {
            if (ss.getSpaceKey() == null || ss.getSpaceKey().trim().isEmpty()) {
                throw new SettingsValidateException("Space key is blank");
            }
            if (ss.getGroups() == null || ss.getGroups().isEmpty()) {
                throw new SettingsValidateException("Groups is blank");
            }
            for (String groupName : ss.getGroups()) {
                if (groupName.trim().isEmpty()) {
                    throw new SettingsValidateException("Group name is blank");
                }
            }
        }
    }
}
