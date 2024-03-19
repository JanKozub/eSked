package org.jk.esked.app.backend.services;
import org.jk.esked.app.backend.model.Group;
import org.jk.esked.app.backend.repositories.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GroupService {
    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public void saveGroup(Group group) {
        groupRepository.save(group);
    }

    public String getGroupNameByGroupCode(int groupCode) {
        return groupRepository.getGroupNameByGroupCode(groupCode);
    }

    public List<String> getAllGroupNames() {
        return groupRepository.getAllGroupNames();
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

    public UUID getLeaderIdByName(String name) {
        return groupRepository.getLeaderIdByGroupName(name);
    }

    public UUID getLeaderIdByGroupCode(int groupCode) {
        return groupRepository.getLeaderIdByGroupCode(groupCode);
    }

    public void changeGroupAcceptedByGroupCode(int groupCode, boolean state) {
        groupRepository.changeGroupAcceptedByGroupCode(groupCode, state);
    }

    public boolean isGroupCreatedByUser(UUID userId) {
        return !groupRepository.getGroupsCreatedByUser(userId).isEmpty();
    }

//    public void synchronizeWGroup(UUID userId, int groupCode) {
//        if (userId.compareTo(getLeaderIdByName(groupCode)) != 0) {
//            if (userService.isEventsSynByUserId(userId))
//                synchronizeEvents(userId);
//
//            if (userService.isTableSyn(userId))
//                synchronizeTable(userId, groupCode);
//        }
//    }
//
//    private void synchronizeEvents(UUID userId) {
//        Collection<Event> groupEvents = eventService.getEvents(getLeaderIdByName(
//                getGroupNameByGroupCode(userService.getGroupCode(userId))));
//        Collection<Event> events = eventService.getEvents(userId);
//        for (Event parseEvent : groupEvents) {
//            if (events.stream().noneMatch(event -> event.getId().compareTo(parseEvent.getId()) == 0)) {
//                User user = new User();
//                user.setId(userId);
//                parseEvent.setUser(user);
//                parseEvent.setCheckedFlag(false);
//                eventService.saveEvent(parseEvent);
//            }
//        }
//    }
//
//    private void synchronizeTable(UUID userId, int groupCode) {
//        scheduleService.deleteAllScheduleEntriesForId(userId);
//        scheduleService.setScheduleEntries(userId, scheduleService.getScheduleEntriesByUserId(getLeaderIdByName(groupCode)));
//
//        Collection<Hour> hours = hourService.getHours(getLeaderIdByName(groupCode));
//        List<Hour> newHours = new ArrayList<>();
//        for (Hour hour : hours) {
//            Hour newHour = new Hour();
//            newHour.setHour(hour.getHour());
//            newHour.setData(hour.getData());
//            newHours.add(new ScheduleHour(userId, hour.hour(), hour.data()));
//        }
//        hourService.deleteAllHourByUserId(userId);
//        hourService.setScheduleHours(newHours);
//    }
}
