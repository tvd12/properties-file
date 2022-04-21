package com.tvd12.properties.file.testing;

import com.tvd12.properties.file.annotation.Property;

import lombok.Getter;
import lombok.Setter;

public class ExampleRoom {

    @Setter
    @Getter
    protected int capacity;

    @Setter
    @Getter
    protected int id;

    @Setter
    @Getter
    protected int maxRoomVariablesAllowed;

    @Setter
    @Getter
    protected int maxSpectators;

    @Setter
    @Getter
    protected int maxUsers;

    @Setter
    @Getter
    protected int variablesCount;

    @Getter
    protected boolean active;

    @Setter
    @Getter
    protected boolean dynamic;

    @Setter
    @Getter
    protected boolean empty;

    @Setter
    @Getter
    protected boolean full;

    @Setter
    @Getter
    protected boolean game;

    @Setter
    @Getter
    protected boolean hidden;

    @Setter
    @Getter
    protected boolean passwordProtected;

    @Setter
    @Getter
    @Property("public")
    protected boolean isPublic;

    @Setter
    @Getter
    protected boolean useWordsFilter;

    @Setter
    @Getter
    protected String name;

    @Setter
    @Getter
    protected String password;

    @Setter
    @Getter
    protected String groupId;

    public void setOwner(Object owner) {}

    public <T> T getOwner() {return null;}

    public void setActive(boolean value) {
        this.active = value;
    }
}
