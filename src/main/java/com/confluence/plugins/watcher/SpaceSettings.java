package com.confluence.plugins.watcher;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import java.util.List;
import java.util.Set;

@JsonAutoDetect
public class SpaceSettings {

    private String spaceKey;
    private Set<String> groups;
    private Set<String> excludeGroups;
    private Set<String> excludeUsers;

    public String getSpaceKey() {
        return spaceKey;
    }

    public void setSpaceKey(String spaceKey) {
        this.spaceKey = spaceKey;
    }

    public Set<String> getGroups() {
        return groups;
    }

    public void setGroups(Set<String> groups) {
        this.groups = groups;
    }

    public Set<String> getExcludeGroups() {
        return excludeGroups;
    }

    public void setExcludeGroups(Set<String> excludeGroups) {
        this.excludeGroups = excludeGroups;
    }

    public Set<String> getExcludeUsers() {
        return excludeUsers;
    }

    public void setExcludeUsers(Set<String> excludeUsers) {
        this.excludeUsers = excludeUsers;
    }

    public boolean isInGroups(List<String> userGroups) {
        if (groups == null || groups.isEmpty()) {
            return false;
        }

        return groups.stream().anyMatch(g -> userGroups.stream().anyMatch(g::equalsIgnoreCase));
    }

    public boolean isExcludedUser(String username) {
        if (excludeUsers == null || excludeUsers.isEmpty()) {
            return false;
        }

        return excludeUsers.stream().anyMatch(username::equalsIgnoreCase);
    }

    public boolean isInExcludedGroups(List<String> userGroups) {
        if (excludeGroups == null || excludeGroups.isEmpty()) {
            return false;
        }

        return excludeGroups.stream().anyMatch(g -> userGroups.stream().anyMatch(g::equalsIgnoreCase));
    }
}
