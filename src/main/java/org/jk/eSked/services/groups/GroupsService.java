package org.jk.eSked.services.groups;

import org.jk.eSked.model.entry.Entry;
import org.jk.eSked.model.Group;

import java.util.Collection;
import java.util.UUID;

public interface GroupsService {

    int doesGroupExist(String name);

    void addEntryToGroup(boolean isAccepted, String name, int groupCode, UUID leaderId, int hour, int day, String subject, long createdDate);

    void deleteGroupEntry(int groupCode, int hour, int day);

    void deleteGroup(int groupCode);

    String getGroupName(int groupCode);

    Collection<Group> getGroups();

    Collection<Entry> getEntries(int groupCode);

    UUID getLeaderId(String name);

    UUID getLeaderId(int groupCode);

    boolean isGroupAccepted(int groupCode);

    void setGroupAccepted(int groupCode);
}
