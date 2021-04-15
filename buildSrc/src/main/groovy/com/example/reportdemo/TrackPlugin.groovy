package com.example.reportdemo

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project


class TrackPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        boolean disableSensorsAnalyticsPlugin = false
        Properties properties = new Properties()
        if (project.rootProject.file('gradle.properties').exists()) {
            properties.load(project.rootProject.file('gradle.properties').newDataInputStream())
            disableSensorsAnalyticsPlugin=Boolean.parseBoolean(properties.getProperty("sensorsAnalytics.disablePlugin", "false"))
        }
        if(!disableSensorsAnalyticsPlugin){
            AppExtension appExtension = project.extensions.findByType(AppExtension.class)
            appExtension.registerTransform(new TrackTransform())
        }else {
            System.out.println("你已经关闭了埋点")
        }
    }
}