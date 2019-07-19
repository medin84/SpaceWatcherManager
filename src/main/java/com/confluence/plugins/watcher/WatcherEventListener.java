package com.confluence.plugins.watcher;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.confluence.event.events.user.UserCreateEvent;
import com.atlassian.crowd.event.directory.DirectoryUpdatedEvent;
import com.atlassian.crowd.event.group.GroupMembershipCreatedEvent;
import com.atlassian.crowd.event.group.GroupMembershipDeletedEvent;
import com.atlassian.crowd.event.group.GroupUpdatedEvent;
import com.atlassian.crowd.event.user.UserEditedEvent;
import com.atlassian.crowd.event.user.UserUpdatedEvent;
import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugin.spring.scanner.annotation.imports.ConfluenceImport;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;

// @Named
public class WatcherEventListener {

    // private static final Logger log = LoggerFactory.getLogger(WatcherEventListener.class);

/*    @ConfluenceImport
    private EventPublisher eventPublisher;
    private WatcherService wjs;

    @Inject
    public WatcherEventListener(@ComponentImport PluginSettingsFactory psf, EventPublisher eventPublisher, @ComponentImport ActiveObjects ao) {
        this.eventPublisher = eventPublisher;
        wjs = new WatcherService(new SettingsService(psf), ao);
    }*/

/*    @EventListener
    public void onUserCreateEvent(final UserCreateEvent event) {
        log("onUserCreateEvent");
        addUsername(event.getUser().getName());
    }*/

/*    @EventListener
    public void onUserUpdatedEvent(final UserUpdatedEvent event) {
        log("onUserUpdatedEvent");
        addUsername(event.getUser().getName());
    }*/

/*    @EventListener
    public void onUserEditedEvent(final UserEditedEvent event) {
        log("onUserEditedEvent");
        addUsername(event.getUser().getName());
    }*/

/*    @EventListener
    public void onDirectoryUpdatedEvent(final DirectoryUpdatedEvent event) {
        log("onDirectoryUpdatedEvent, " + event.getDirectory().getName());
    }*/

/*    @EventListener
    public void onGroupUpdatedEvent(final GroupUpdatedEvent event) {
        log("onGroupUpdatedEvent, " + event.getDirectory().getName());
    }*/

/*    @EventListener
    public void onGroupMembershipCreatedEvent(final GroupMembershipCreatedEvent event) {
        log("onGroupMembershipCreatedEvent, " + event.getGroupName());
        onChangeGroup(event.getGroupName());
    }*/

/*    @EventListener
    public void onGroupMembershipDeletedEvent(final GroupMembershipDeletedEvent event) {
        log("onGroupMembershipDeletedEvent, " + event.getGroupName());
    }*/

/*    private void onChangeGroup(String groupName) {
        wjs.addGroupMembers(groupName);
    }*/

/*    private void addUsername(String username) {
        wjs.addUser(username);
    }*/

    /*private void log(String message) {
        log.info(message);
        System.out.println("======================            " + message);
    }*/
}
