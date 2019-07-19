package com.confluence.plugins.watcher;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.confluence.mail.notification.NotificationManager;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.spaces.SpaceManager;
import com.atlassian.confluence.user.ConfluenceUser;
import com.atlassian.confluence.user.UserAccessor;
import com.atlassian.spring.container.ContainerManager;
import com.atlassian.user.Group;
import net.java.ao.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WatcherService {

    private static final Logger log = LoggerFactory.getLogger(WatcherService.class);

    private UserAccessor userAccessor;
    private ActiveObjects ao;
    private SettingsService settingsService;

    public WatcherService(SettingsService settingsService, ActiveObjects ao) {
        this.ao = ao;
        this.settingsService = settingsService;
        this.userAccessor = (UserAccessor) ContainerManager.getComponent("userAccessor");
    }

    /*
    Проходим по настройкам пространства и смотрим пользователей по указанным группам.
    Если по условиям пользователь не в списке исключения или он уже не проверен ранее (CHECKED),
    добавляем пользователя в наблюдение пространства и ставим галочку проверен.
    */
    void addUsersToSpaceWatchersBySettings() {
        if (!settingsService.hasSettings()) {
            log("No configured settings");
            return;
        }

        NotificationManager notificationManager = (NotificationManager) ContainerManager.getComponent("notificationManager");
        SpaceManager spaceManager = (SpaceManager) ContainerManager.getComponent("spaceManager");
        SettingsModel settings = settingsService.get();
        GlobalSettings gs = settings.getGlobalSettings();

        for (SpaceSettings cs : settings.getSpaceSettings()) {
            Space space = spaceManager.getSpace(cs.getSpaceKey());
            if (space == null) {
                log("Space [" + cs.getSpaceKey() + "] not exists.");
                continue;
            }

            for (String groupName : cs.getGroups()) {
                Group group = userAccessor.getGroup(groupName);
                if (group == null) {
                    log("Group [" + groupName + "] not exists. SpaceSettings [" + cs.getSpaceKey() + "]");
                    continue;
                }

                Iterable<ConfluenceUser> groupMembers = userAccessor.getMembers(group);
                for (ConfluenceUser user : groupMembers) {
                    Query query = Query.select().where("NAME = ? AND SPACE_KEY = ?", user.getName(), cs.getSpaceKey());
                    WatcherUserEntity[] watcherUserEntities = ao.find(WatcherUserEntity.class, query);

                    if (watcherUserEntities.length > 0) {
                        log("User [" + user.getName() + "] already been processed for space [" + cs.getSpaceKey() + "]");
                        continue;
                    }

                    List<String> groupNames = userAccessor.getGroupNames(user);
                    boolean isInGlobalExcludedGroup = gs.isInExcludedGroups(groupNames);
                    boolean isInGlobalExcludedUsers = gs.isExcludedUser(user.getName());

                    // Check user exclusion
                    if (isInGlobalExcludedGroup ||
                            isInGlobalExcludedUsers ||
                            cs.isExcludedUser(user.getName()) ||
                            cs.isInExcludedGroups(groupNames)) {
                        log("User [" + user.getName() + "] is in exclude list. SpaceSettings [" + cs.getSpaceKey() + "]");
                        continue;
                    }

                    boolean isWatching = notificationManager.isUserWatchingPageOrSpace(user, space, null);

                    if (isWatching) {
                        log("User [" + user.getName() + "] already watchers. SpaceSettings [" + cs.getSpaceKey() + "]");
                        continue;
                    }

                    notificationManager.addSpaceNotification(user, space);

                    log("User [" + user.getName() + "] added to space [" + space.getKey() + "] as watcher");

                    // Add user to checked users list
                    Map<String, Object> fields = new HashMap<>();
                    fields.put("NAME", user.getName());
                    fields.put("SPACE_KEY", space.getKey());
                    fields.put("CHECKED", true);
                    ao.create(WatcherUserEntity.class, fields);
                }
            }
        }
    }

/*    void addGroupMembers(String groupName) {
        if (!settingsService.hasSettings()) {
            return;
        }

        Group group = userAccessor.getGroup(groupName);
        if (group == null) {
            log("Group not exists: " + groupName);
            return;
        }

        Iterable<ConfluenceUser> groupUsers = userAccessor.getMembers(group);
        for (ConfluenceUser user : groupUsers) {
            addUser(user.getName());
        }
    }*/

    /*
    Перед добавлением в бд, надо проверить пользователя
    Пройтись по настройкам к каким пространствам пользователь может быть добавлен наблюдателем
    Не исключаемый ли пользователь
    // !!!
    !!!Не надо доавлять пользователей по событиям изменения групп
    Проверяй во время запуска по расписанию пользователей из указанных групп и добавляй пользователей
    в наблюдение если не в наблюдателях
    и ставь CHECKED=true после
    */
/*    void addUser(String username) {
        if (!settingsService.hasSettings() || username == null || username.isEmpty()) {
            return;
        }

        List<String> params = new ArrayList<>();
        List<String> spaceKeys = new ArrayList<>();
        List<SpaceSettings> spaceSettings = settingsService.get().getSpaceSettings();
        for (SpaceSettings cs : spaceSettings) {
            params.add("?");
            spaceKeys.add(cs.getSpaceKey());
        }

        Query query = Query.select().where("NAME = ? AND CHECKED = false AND SPACE_KEY IN (" + String.join(",", params) + ")", username, spaceKeys.toArray());
        WatcherUserEntity[] entities = ao.find(WatcherUserEntity.class, query);
        if (entities.length > 0) {
            // already added
            log("User already added: " + username);
            return;
        }

        Map<String, Object> fields = new HashMap<>();
        fields.put("NAME", username);
        fields.put("CHECKED", false);
        // fields.put("SPACE_KEY", ?);
        ao.create(WatcherUserEntity.class, fields);

        log("User added: " + username);
    }*/

    /*void addUsersToSpaceWatchers() {
        if (!settingsService.hasSettings()) {
            log("No configured settings");
            return;
        }

        // Search unchecked users
        Query query = Query.select().where("CHECKED = false");
        WatcherUserEntity[] userEntities = ao.find(WatcherUserEntity.class, query);

        if (userEntities.length == 0) {
            log("No users");
            return;
        }

        NotificationManager notificationManager = (NotificationManager) ContainerManager.getComponent("notificationManager");
        SpaceManager spaceManager = (SpaceManager) ContainerManager.getComponent("spaceManager");
        SettingsModel settings = settingsService.get();
        GlobalSettings gs = settings.getGlobalSettings();

        for (WatcherUserEntity userEntity : userEntities) {
            ConfluenceUser user = userAccessor.getUserByName(userEntity.getName());
            if (user == null) {
                log("Not found confluence user by name: " + userEntity.getName());
                continue;
            }

            for (SpaceSettings cs : settings.getSpaceSettings()) {
                Space space = spaceManager.getSpace(cs.getSpaceKey());
                if (space == null) {
                    log("Space [" + cs.getSpaceKey() + "] not exists.");
                    continue;
                }

                if (userEntity.getSpaceKey() == null || userEntity.getSpaceKey().isEmpty()) {
                    userEntity.setSpaceKey(space.getKey());
                } else if (!userEntity.getSpaceKey().equalsIgnoreCase(space.getKey())) {
                    // skip
                    log("Skip: " + userEntity.getSpaceKey() + " != " + space.getKey());
                    continue;
                }

                List<Group> groupList = new ArrayList<>();
                for (String groupName : cs.getGroups()) {
                    Group group = userAccessor.getGroup(groupName);
                    if (group != null) {
                        groupList.add(group);
                    }
                }

                if (groupList.isEmpty()) {
                    log("Groups [" + String.join(", ", cs.getGroups()) + "] not exists.");
                    continue;
                }

                List<String> groupNames = userAccessor.getGroupNames(user);
                boolean isInGlobalExcludedGroup = gs.isInExcludedGroups(groupNames);
                boolean isInGlobalExcludedUsers = gs.isExcludedUser(user.getName());

                if (isInGlobalExcludedGroup ||
                        isInGlobalExcludedUsers ||
                        !cs.isInGroups(groupNames) ||
                        cs.isExcludedUser(user.getName()) ||
                        cs.isInExcludedGroups(groupNames)) {
                    log("Skip user: " + user.getName());
                    continue;
                }

                List<String> addedToSpaceWatcherUserNames = new ArrayList<>();
                boolean isWatching = notificationManager.isUserWatchingPageOrSpace(user, space, null);

                if (!isWatching) {
                    notificationManager.addSpaceNotification(user, space);
                    addedToSpaceWatcherUserNames.add(user.getName());
                } else {
                    log("User already watchers: " + user.getName());
                }

                if (!addedToSpaceWatcherUserNames.isEmpty()) {
                    log("Added to space [" + space.getKey() + "] as watcher users: " + String.join(",", addedToSpaceWatcherUserNames));
                } else {
                    log("No added user to watchers");
                }
            }

            userEntity.setChecked(true);
            userEntity.save();
        }
    }*/

    private void log(String message) {
        log.info(message);
        // System.out.println("======================            " + message);
    }
}
