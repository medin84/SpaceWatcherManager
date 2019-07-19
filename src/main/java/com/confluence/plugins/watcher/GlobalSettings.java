package com.confluence.plugins.watcher;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import java.util.List;
import java.util.Set;

@JsonAutoDetect
public class GlobalSettings {

    private Set<String> excludeGroups;
    private Set<String> excludeUsers;

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
