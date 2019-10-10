package org.jk.eSked.backend.service.user;

import org.jk.eSked.backend.dao.GroupsDao;
import org.jk.eSked.backend.model.Event;
import org.jk.eSked.backend.model.Group;
import org.jk.eSked.backend.model.schedule.ScheduleHour;
import org.jk.eSked.backend.repositories.GroupDB;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class GroupService implements GroupDB {
    private final GroupsDao groupsDao;
    private final ScheduleService scheduleService;
    private final EventService eventService;
    private final UserService userService;
    private final HoursService hoursService;

    public GroupService(GroupsDao groupsDao, ScheduleService scheduleService, EventService eventService, UserService userService, HoursService hoursService) {
        this.groupsDao = groupsDao;
        this.scheduleService = scheduleService;
        this.eventService = eventService;
        this.userService = userService;
        this.hoursService = hoursService;
    }

    @Override
    public void addGroup(Group group) {
        groupsDao.addGroup(group);
    }

    @Override
    public String getGroupName(int groupCode) {
        return groupsDao.getGroupName(groupCode);
    }

    @Override
    public Collection<String> getGroupNames() {
        return groupsDao.getGroupNames();
    }

    @Override
    public Collection<Integer> getGroupCodes() {
        return groupsDao.getGroupCodes();
    }

    @Override
    public Collection<Group> getGroups() {
        return groupsDao.getGroups();
    }

    @Override
    public void deleteGroup(int groupCode) {
        groupsDao.deleteGroup(groupCode);
    }

    @Override
    public UUID getLeaderId(String name) {
        return groupsDao.getLeaderIdName(name);
    }

    @Override
    public UUID getLeaderId(int groupCode) {
        return groupsDao.getLeaderIdCode(groupCode);
    }

    @Override
    public void setGroupAccepted(int groupCode) {
        groupsDao.setGroupAccepted(groupCode, true);
    }

    @Override
    public boolean doesCreatedGroup(UUID userId) {
        Collection<Group> groups = getGroups();

        for (Group group : groups) {
            if (group.getLeaderId().compareTo(userId) == 0) return true;
        }

        return false;
    }

    @Override
    public boolean hasEntries(UUID userId) {
        return scheduleService.getScheduleEntries(userId).size() > 0;
    }

    @Override
    public void synchronizeWGroup(UUID userId, int groupCode) {
        if (userId.compareTo(getLeaderId(groupCode)) != 0) {
            if (userService.isEventsSyn(userId))
                synchronizeEvents(userId);

            if (userService.isTableSyn(userId))
                synchronizeTable(userId, groupCode);
        }
    }

    private void synchronizeEvents(UUID userId) {
        Collection<Event> groupEvents = eventService.getEvents(getLeaderId(
                getGroupName(userService.getGroupCode(userId))));
        Collection<Event> events = eventService.getEvents(userId);
        for (Event parseEvent : groupEvents) {
            if (events.stream().noneMatch(event -> event.getEventId().compareTo(parseEvent.getEventId()) == 0)) {
                parseEvent.setUserId(userId);
                parseEvent.setCheckedFlag(false);
                eventService.addEvent(parseEvent);
            }
        }
    }

    private void synchronizeTable(UUID userId, int groupCode) {
        scheduleService.deleteScheduleEntries(userId);
        scheduleService.setScheduleEntries(userId, scheduleService.getScheduleEntries(getLeaderId(groupCode)));

        Collection<ScheduleHour> hours = hoursService.getHours(getLeaderId(groupCode));
        List<ScheduleHour> newHours = new ArrayList<>();
        for (ScheduleHour hour : hours) {
            newHours.add(new ScheduleHour(userId, hour.getHour(), hour.getData()));
        }
        hoursService.deleteScheduleHours(userId);
        hoursService.setScheduleHours(newHours);
    }
}
