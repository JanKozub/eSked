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
    private final ScheduleEntryService scheduleEntryService;
    private final HourService hourService;

    public GroupService(GroupRepository groupRepository, UserService userService, EventService eventService, ScheduleEntryService scheduleEntryService, HourService hourService) {
        this.groupRepository = groupRepository;
        this.userService = userService;
        this.eventService = eventService;
        this.scheduleEntryService = scheduleEntryService;
        this.hourService = hourService;
    }

    public void saveGroup(Group group) {
        groupRepository.save(group);
    }

    public Group findByGroupCode(int groupCode) {
        return groupRepository.findByGroupCode(groupCode);
    }

    public List<Integer> findAllGroupCodes() {
        return groupRepository.findAllGroupCodes();
    }

    public List<Group> findAllGroupsByAccepted(boolean accepted) {
        return groupRepository.findAllGroupsByAccepted(accepted);
    }

    public void deleteByGroupCode(int groupCode) {
        groupRepository.deleteByGroupCode(groupCode);
    }

    public UUID findLeaderIdByGroupCode(int groupCode) {
        return groupRepository.findLeaderIdByGroupCode(groupCode);
    }

    public void changeAcceptedByGroupCode(int groupCode, boolean state) {
        groupRepository.changeAcceptedByGroupCode(groupCode, state);
    }

    public void synchronizeWGroup(UUID userId, int groupCode) {
        if (userService.findById(userId).getGroupCode() == 0) return;

        if (userId.compareTo(findLeaderIdByGroupCode(groupCode)) != 0) {
            if (userService.isEventsSynById(userId))
                synchronizeEvents(userId);

            if (userService.isTableSynById(userId))
                synchronizeTable(userId, groupCode);
        }
    }

    private void synchronizeEvents(UUID userId) {
        List<Event> groupEvents = eventService.getEventsByUserId(findLeaderIdByGroupCode(userService.findGroupCodeById(userId)));
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
        scheduleEntryService.deleteAllByUserId(userId);
        scheduleEntryService.setAllByUserId(userId, scheduleEntryService.getAllByUserId(findLeaderIdByGroupCode(groupCode)));

        List<Hour> hours = hourService.getHourByUserId(findLeaderIdByGroupCode(groupCode));
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
