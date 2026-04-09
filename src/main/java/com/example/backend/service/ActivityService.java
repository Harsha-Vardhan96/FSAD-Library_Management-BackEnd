package com.example.backend.service;

import com.example.backend.entity.Activity;
import com.example.backend.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;

    public Activity logActivity(String action, String target, String category, String details) {
        Activity activity = new Activity();
        activity.setId(String.valueOf(System.currentTimeMillis()));
        activity.setAction(action);
        activity.setTarget(target);
        activity.setCategory(category != null ? category : "");
        activity.setDetails(details != null ? details : "");
        activity.setTimestamp(Instant.now().toString());
        
        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy, h:mm:ss a");
        activity.setWhen(sdf.format(new Date()).toLowerCase());
        
        return activityRepository.save(activity);
    }

    public List<Activity> getRecentActivities() {
        return activityRepository.findTop10ByOrderByTimestampDesc();
    }
}
