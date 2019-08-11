package org.jk.eSked.services.groups;

import org.jk.eSked.dao.GroupsDao;
import org.jk.eSked.model.entry.Entry;
import org.jk.eSked.model.entry.GroupEntry;
import org.jk.eSked.model.Group;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class DBGroupsService implements GroupsService {
    private final GroupsDao groupsDao;

    public DBGroupsService(GroupsDao groupsDao) {
        this.groupsDao = groupsDao;
    }

    @Override
    public int doesGroupExist(String name) {
        Collection<GroupEntry> entries = groupsDao.getGroups();
        for (GroupEntry entry : entries) {
            if (entry.getName().equals(name)) {
                return entry.getGroupCode();
            }
        }
        return 0;
    }

    @Override
    public void addEntryToGroup(boolean isAccepted, String name, int groupCode, UUID leaderId, int hour, int day, String subject, long createdDate) {
        groupsDao.addEntryToGroup(isAccepted, name, groupCode, leaderId, hour, day, subject, createdDate);
    }

    @Override
    public String getGroupName(int groupCode) {
        Collection<GroupEntry> entries = groupsDao.getGroupEntries(groupCode);
        if (entries.size() > 0)
            for (GroupEntry groupEntry : entries) {
                if (groupEntry.getGroupCode() == groupCode) return groupEntry.getName();
            }
        return "brak";
    }

    @Override
    public Collection<Group> getGroups() {
        Collection<GroupEntry> groupEntries = groupsDao.getGroups();
        int lastGroupCode = 0;
        List<Group> groups = new ArrayList<>();
        for (GroupEntry entry : groupEntries) {
            if (entry.getGroupCode() != lastGroupCode) {
                lastGroupCode = entry.getGroupCode();
                groups.add(new Group(entry.isAccepted(), entry.getName(), entry.getGroupCode()));
            }
        }
        return groups;
    }

    @Override
    public Collection<Entry> getEntries(int groupCode) {
        Collection<GroupEntry> groupEntries = groupsDao.getGroupEntries(groupCode);
        List<Entry> entries = new ArrayList<>();
        for (GroupEntry groupEntry : groupEntries) {
            Entry entry = new Entry(groupEntry.getHour(), groupEntry.getDay(), groupEntry.getSubject(),
                    groupEntry.getCreatedDate().atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
            entries.add(entry);
        }
        return entries;
    }

    @Override
    public void deleteGroup(int groupCode) {
        groupsDao.deleteGroup(groupCode);
    }

    @Override
    public void deleteGroupEntry(int groupCode, int hour, int day) {
        groupsDao.deleteGroupEntry(groupCode, hour, day);
    }

    @Override
    public UUID getLeaderId(String name) {
        int code = doesGroupExist(name);
        if (code != 0) {
            for (GroupEntry groupEntry : groupsDao.getGroupEntries(code)) {
                if (groupEntry.getName().equals(name)) return groupEntry.getLeaderId();
            }
        }
        return null;
    }

    @Override
    public UUID getLeaderId(int groupCode) {
        if (groupCode != 0) {
            for (GroupEntry groupEntry : groupsDao.getGroupEntries(groupCode)) {
                if (groupEntry.getGroupCode() == groupCode) return groupEntry.getLeaderId();
            }
        }
        return null;
    }

    @Override
    public boolean isGroupAccepted(int groupCode) {
        return groupsDao.getGroupEntries(groupCode).stream().findAny().orElseThrow().isAccepted();
    }

    @Override
    public void setGroupAccepted(int groupCode) {
        groupsDao.setGroupAccepted(groupCode, true);
    }
}