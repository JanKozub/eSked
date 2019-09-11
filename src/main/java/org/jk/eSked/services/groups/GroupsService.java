package org.jk.eSked.services.groups;

import org.jk.eSked.model.Group;

import java.util.Collection;
import java.util.UUID;

public interface GroupsService {

    void addGroup(Group group);

    void deleteGroup(int groupCode);

    String getGroupName(int groupCode);

    Collection<String> getGroupsNames();

    Collection<Group> getGroups();

    UUID getLeaderId(String name);

    UUID getLeaderId(int groupCode);

    void setGroupAccepted(int groupCode);

    void synchronizeWGroup(UUID userId, int groupCode);

    boolean doesCreatedGroup(UUID userId);

    boolean hasEntries(UUID userId);
}
