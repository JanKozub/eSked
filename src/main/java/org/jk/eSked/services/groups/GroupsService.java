package org.jk.eSked.services.groups;

import org.jk.eSked.model.Group;

import java.util.Collection;
import java.util.UUID;

public interface GroupsService {

    void addGroup(UUID userId, String name, int groupCode, long createdDate);

    void addEntryToGroup(boolean isAccepted, String name, int groupCode, UUID userId, int hour, int day, String subject, long createdDate);

    void deleteGroupEntry(int groupCode, int hour, int day);

    void deleteGroup(int groupCode);

    String getGroupName(int groupCode);

    Collection<String> getGroupsNames();

    Collection<Group> getGroups();

    UUID getLeaderId(String name);

    UUID getLeaderId(int groupCode);

    void setGroupAccepted(int groupCode);

    void setGroupDeclined(int groupCode);

    void synchronizeWGroup(UUID userId, int groupCode);

    boolean hasEntiries(UUID userId);

    boolean doesCreatedGroup(UUID userId);
}
