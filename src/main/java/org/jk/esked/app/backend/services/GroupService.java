package org.jk.esked.app.backend.services;

import org.jk.esked.app.backend.model.entities.Event;
import org.jk.esked.app.backend.model.entities.Group;
import org.jk.esked.app.backend.model.entities.Hour;
import org.jk.esked.app.backend.model.entities.User;
import org.jk.esked.app.backend.repositories.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserService userService;
    private final EventService eventService;
    private final ScheduleService scheduleService;
    private final HourService hourService;

    public GroupService(GroupRepository groupRepository, UserService userService, EventService eventService, ScheduleService scheduleService, HourService hourService) {
        this.groupRepository = groupRepository;
        this.userService = userService;
        this.eventService = eventService;
        this.scheduleService = scheduleService;
        this.hourService = hourService;
    }

    public void saveGroup(Group group) {
        groupRepository.save(group);
    }

    public Group getGroupByGroupCode(int groupCode) {
        return groupRepository.getGroupByGroupCode(groupCode);
    }

    public List<Integer> getAllGroupCodes() {
        return groupRepository.getAllGroupCodes();
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public void deleteGroupByGroupCode(int groupCode) {
        groupRepository.deleteGroupByGroupCode(groupCode);
    }

    public UUID getLeaderIdByGroupCode(int groupCode) {
        return groupRepository.getLeaderIdByGroupCode(groupCode);
    }

    public void changeGroupAcceptedByGroupCode(int groupCode, boolean state) {
        groupRepository.changeGroupAcceptedByGroupCode(groupCode, state);
    }

    public void synchronizeWGroup(UUID userId, int groupCode) {
        if (userService.getUserById(userId).getGroupCode() == 0) return;

        if (userId.compareTo(getLeaderIdByGroupCode(groupCode)) != 0) {
            if (userService.isEventsSynByUserId(userId))
                synchronizeEvents(userId);

            if (userService.isTableSyn(userId))
                synchronizeTable(userId, groupCode);
        }
    }

    private void synchronizeEvents(UUID userId) {
        List<Event> groupEvents = eventService.getEventsByUserId(getLeaderIdByGroupCode(userService.getGroupCodeByUserId(userId)));
        List<Event> events = eventService.getEventsByUserId(userId);
        for (Event parseEvent : groupEvents) {
            if (events.stream().noneMatch(event -> event.getId().compareTo(parseEvent.getId()) == 0)) {
                User user = new User();
                user.setId(userId);
                parseEvent.setUser(user);
                parseEvent.setCheckedFlag(false);
                eventService.saveEvent(parseEvent);
            }
        }
    }

    private void synchronizeTable(UUID userId, int groupCode) {
        scheduleService.deleteAllScheduleEntriesForId(userId);
        scheduleService.setScheduleEntries(userId, scheduleService.getScheduleEntriesByUserId(getLeaderIdByGroupCode(groupCode)));

        List<Hour> hours = hourService.getHourByUserId(getLeaderIdByGroupCode(groupCode));
        List<Hour> newHours = new ArrayList<>();
        for (Hour hour : hours) {
            Hour newHour = new Hour();
            User user = new User();
            user.setId(userId);

            newHour.setUser(user);
            newHour.setHour(hour.getHour());
            newHour.setData(hour.getData());

            newHours.add(newHour);
        }
        hourService.deleteAllHourByUserId(userId);
        hourService.setScheduleHours(newHours);
    }
}
