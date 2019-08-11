package org.jk.eSked.services;

import org.jk.eSked.model.event.Event;
import org.jk.eSked.model.event.ScheduleEvent;
import org.jk.eSked.services.events.EventService;
import org.jk.eSked.services.groups.GroupsService;
import org.jk.eSked.services.schedule.ScheduleService;
import org.jk.eSked.services.users.UserService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public class GroupSynService {
    private final ScheduleService scheduleService;
    private final EventService eventService;
    private final GroupsService groupsService;
    private final UserService userService;

    public GroupSynService(ScheduleService scheduleService, EventService eventService, UserService userService, GroupsService groupsService) {
        this.scheduleService = scheduleService;
        this.eventService = eventService;
        this.groupsService = groupsService;
        this.userService = userService;
    }

    public void SynchronizeWGroup(UUID userId, int groupCode) {
        if (groupCode != 0) {
            scheduleService.deleteScheduleEntries(userId);
            scheduleService.setScheduleEntries(userId, groupsService.getEntries(userService.getGroupCode(userId)));

            if (userService.isSynWGroup(userId)) {
                Collection<Event> groupEvents = eventService.getAllEvents(groupsService.getLeaderId(
                        groupsService.getGroupName(userService.getGroupCode(userId))));
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
}
