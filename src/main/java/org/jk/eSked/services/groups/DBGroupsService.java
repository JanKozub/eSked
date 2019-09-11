package org.jk.eSked.services.groups;

import org.jk.eSked.dao.GroupsDao;
import org.jk.eSked.model.Group;
import org.jk.eSked.model.ScheduleHour;
import org.jk.eSked.model.event.Event;
import org.jk.eSked.model.event.ScheduleEvent;
import org.jk.eSked.services.events.EventService;
import org.jk.eSked.services.hours.HoursService;
import org.jk.eSked.services.schedule.ScheduleService;
import org.jk.eSked.services.users.UserService;
import org.springframework.stereotype.Service;

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
    private final HoursService hoursService;

    public DBGroupsService(GroupsDao groupsDao, ScheduleService scheduleService, EventService eventService, UserService userService, HoursService hoursService) {
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
    public Collection<String> getGroupsNames() {
        return groupsDao.getGroupsNames();
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
    public void synchronizeWGroup(UUID userId, int groupCode) {
        if (userId.compareTo(getLeaderId(groupCode)) != 0) {
            if (userService.isEventsSyn(userId)) {
                Collection<Event> groupEvents = eventService.getAllEvents(getLeaderId( //EVENTS
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
            if (userService.isTableSyn(userId)) {
                scheduleService.deleteScheduleEntries(userId);//ENTRIES
                scheduleService.setScheduleEntries(userId, scheduleService.getScheduleEntries(getLeaderId(groupCode)));

                Collection<ScheduleHour> hours = hoursService.getHours(getLeaderId(groupCode)); //HOURS
                List<ScheduleHour> newHours = new ArrayList<>();
                for (ScheduleHour hour : hours) {
                    newHours.add(new ScheduleHour(userId, hour.getHour(), hour.getData()));
                }
                hoursService.deleteScheduleHours(userId);
                hoursService.setScheduleHours(newHours);
            }
        }
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
}