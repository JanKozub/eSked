package org.jk.eSked.services.groups;

import org.jk.eSked.dao.GroupsDao;
import org.jk.eSked.model.Group;
import org.jk.eSked.model.entry.Entry;
import org.jk.eSked.model.entry.GroupEntry;
import org.jk.eSked.model.event.Event;
import org.jk.eSked.model.event.ScheduleEvent;
import org.jk.eSked.services.events.EventService;
import org.jk.eSked.services.schedule.ScheduleService;
import org.jk.eSked.services.users.UserService;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class DBGroupsService implements GroupsService {
    private final GroupsDao groupsDao;
    private final ScheduleService scheduleService;
    private final EventService eventService;
    private final UserService userService;

    public DBGroupsService(GroupsDao groupsDao, ScheduleService scheduleService, EventService eventService, UserService userService) {
        this.groupsDao = groupsDao;
        this.scheduleService = scheduleService;
        this.eventService = eventService;
        this.userService = userService;
    }

    private int doesGroupExist(String name) {
        Collection<GroupEntry> entries = groupsDao.getGroups();
        for (GroupEntry entry : entries) {
            if (entry.getName().equals(name)) {
                return entry.getGroupCode();
            }
        }
        return 0;
    }

    @Override
    public void addGroup(UUID userId, String name, int groupCode, long createdDate) {
        Collection<Entry> entries = scheduleService.getEntries(userId);
        for (Entry entry : entries) {
            groupsDao.addEntryToGroup(false, name, groupCode, userId, entry.getHour(), entry.getDay(), entry.getSubject(), createdDate);
        }
    }

    @Override
    public void addEntryToGroup(boolean isAccepted, String name, int groupCode, UUID userId, int hour, int day, String subject, long createdDate) {
        groupsDao.addEntryToGroup(isAccepted, name, groupCode, userId, hour, day, subject, createdDate);
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
    public Collection<String> getGroupsNames() {
        return groupsDao.getGroupsNames();
    }

    @Override
    public Collection<Group> getGroups() {
        Collection<GroupEntry> groupEntries = groupsDao.getGroups();
        int lastGroupCode = 0;
        List<Group> groups = new ArrayList<>();
        for (GroupEntry entry : groupEntries) {
            if (entry.getGroupCode() != lastGroupCode) {
                lastGroupCode = entry.getGroupCode();
                groups.add(new Group(entry.isAccepted(), entry.getName(), entry.getGroupCode(), entry.getLeaderId(), entry.getCreatedDate()));
            }
        }
        return groups;
    }

    @Override
    public void deleteGroup(int groupCode) {
        groupsDao.deleteGroup(groupCode);
    }

    @Override
    public void deleteGroupEntry(int groupCode, int hour, int day) {
        groupsDao.deleteGroupEntry(groupCode, hour, day);
    }

    private Collection<Entry> getEntries(int groupCode) {
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
    public void setGroupAccepted(int groupCode) {
        groupsDao.setGroupAccepted(groupCode, true);
    }

    @Override
    public void setGroupDeclined(int groupCode) {
        groupsDao.setGroupDeclined(groupCode);
    }

    @Override
    public void synchronizeWGroup(UUID userId, int groupCode) {
        if (groupCode != 0) {
            scheduleService.deleteScheduleEntries(userId);
            scheduleService.setScheduleEntries(userId, getEntries(userService.getGroupCode(userId)));

            if (userService.isSynWGroup(userId)) {
                Collection<Event> groupEvents = eventService.getAllEvents(getLeaderId(
                        getGroupName(userService.getGroupCode(userId))));
                Collection<Event> events = eventService.getAllEvents(userId);
                for (Event parseEvent : groupEvents) {
                    if (events.stream().noneMatch(streamEvent -> parseEvent.getId().equals(streamEvent.getId()))) {
                        eventService.addEvent(new ScheduleEvent(userId, parseEvent.getId(), parseEvent.getDateTimestamp(),
                                parseEvent.getHour(), parseEvent.getEventType(), parseEvent.getTopic(),
                                parseEvent.getCreatedDateTimestamp()));
                    }
                }
            }
        }
    }

    @Override
    public boolean hasEntiries(UUID userId) {
        return !scheduleService.getScheduleEntries(userId).isEmpty();
    }

    @Override
    public boolean doesCreatedGroup(UUID userId) {
        Collection<Group> groups = getGroups();

        for (Group group : groups) {
            if (group.getLeaderId().compareTo(userId) == 0) return true;
        }

        return false;
    }
}