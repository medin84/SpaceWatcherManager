package com.confluence.plugins.watcher;

import net.java.ao.Entity;
import net.java.ao.schema.NotNull;

public interface WatcherUserEntity extends Entity {
    @NotNull
    String getName();

    void setName(String name);

    String getSpaceKey();

    void setSpaceKey(String spaceKey);

    boolean isChecked();

    void setChecked(boolean isChecked);
}
