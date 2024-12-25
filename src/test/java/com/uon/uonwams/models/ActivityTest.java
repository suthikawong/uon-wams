package com.uon.uonwams.models;

import com.uon.uonwams.config.ActivityType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActivityTest {

    @Test
    void testCreateActivity() {
        int activityId = 99;
        String activityName = "test activity";
        ActivityType activityType = ActivityType.ATSR;
        String description = "test";
        int responsibleUserId = 99;
        String responsibleUser = "test";
        String year = "All Year";
        int duration = 2;
        int noOfInstances = 80;

        Activity activity = new Activity(activityId, activityName, activityType, description, responsibleUserId, responsibleUser, year, duration, noOfInstances);

        assertEquals(activityId, activity.getActivityId());
        assertEquals(activityName, activity.getActivityName());
        assertEquals(activityType, activity.getActivityType());
        assertEquals(description, activity.getDescription());
        assertEquals(responsibleUserId, activity.getResponsibleUserId());
        assertEquals(responsibleUser, activity.getResponsibleUser());
        assertEquals(year, activity.getYear());
        assertEquals(duration, activity.getDuration());
        assertEquals(noOfInstances, activity.getNoOfInstances());
    }

    @Test
    void testCalculateActivityATSRType() {
        int duration = 2;
        int noOfInstances = 80;
        int totalHours = duration * noOfInstances;
        Activity activity = new Activity(99, "test activity", ActivityType.ATSR, "test", 99, null, "All Year", 2, 80);
        assertEquals(totalHours, activity.getHours());
        assertEquals(totalHours, activity.getATSR());
        assertEquals(totalHours * 1.2, activity.getTS());
        assertEquals(0, activity.getTLR());
        assertEquals(0, activity.getSA());
        assertEquals(0, activity.getOther());
    }

    @Test
    void testCalculateActivityTLRType() {
        int duration = 2;
        int noOfInstances = 80;
        int totalHours = duration * noOfInstances;
        Activity activity = new Activity(99, "test activity", ActivityType.TLR, "test", 99, null, "All Year", 2, 80);
        assertEquals(totalHours, activity.getHours());
        assertEquals(0, activity.getATSR());
        assertEquals(0, activity.getTS());
        assertEquals(totalHours, activity.getTLR());
        assertEquals(0, activity.getSA());
        assertEquals(0, activity.getOther());
    }

    @Test
    void testCalculateActivitySAType() {
        int duration = 2;
        int noOfInstances = 80;
        int totalHours = duration * noOfInstances;
        Activity activity = new Activity(99, "test activity", ActivityType.SA, "test", 99, null, "All Year", 2, 80);
        assertEquals(totalHours, activity.getHours());
        assertEquals(0, activity.getATSR());
        assertEquals(0, activity.getTS());
        assertEquals(0, activity.getTLR());
        assertEquals(totalHours, activity.getSA());
        assertEquals(0, activity.getOther());
    }

    @Test
    void testCalculateActivityOtherType() {
        int duration = 2;
        int noOfInstances = 80;
        int totalHours = duration * noOfInstances;
        Activity activity = new Activity(99, "test activity", ActivityType.OTHER, "test", 99, null, "All Year", 2, 80);
        assertEquals(totalHours, activity.getHours());
        assertEquals(0, activity.getATSR());
        assertEquals(0, activity.getTS());
        assertEquals(0, activity.getTLR());
        assertEquals(0, activity.getSA());
        assertEquals(totalHours, activity.getOther());
    }
}