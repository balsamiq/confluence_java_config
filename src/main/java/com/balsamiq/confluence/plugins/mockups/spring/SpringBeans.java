package com.balsamiq.confluence.plugins.mockups.spring;

import com.atlassian.activeobjects.external.ActiveObjects;

import com.atlassian.bandana.BandanaManager;
import com.atlassian.confluence.compat.api.service.accessmode.AccessModeCompatService;
import com.atlassian.confluence.license.LicenseService;
import com.atlassian.confluence.mail.notification.NotificationManager;
import com.atlassian.confluence.pages.AttachmentManager;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.setup.BootstrapManager;
import com.atlassian.confluence.user.UserAccessor;
import com.atlassian.core.task.MultiQueueTaskManager;
import com.atlassian.mail.queue.MailQueue;
import com.atlassian.plugins.osgi.javaconfig.ExportOptions;
import com.atlassian.sal.api.UrlMode;
import com.atlassian.sal.api.lifecycle.LifecycleAware;
import com.atlassian.scheduler.SchedulerService;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.atlassian.user.GroupManager;
import com.atlassian.xwork.SimpleXsrfTokenGenerator;
import com.atlassian.xwork.XsrfTokenGenerator;
import com.atlassian.confluence.security.PermissionManager;
import com.balsamiq.confluence.plugins.mockups.MockupsLicenseManager;
import com.balsamiq.confluence.plugins.mockups.MockupsMPLicenseManager;
import com.atlassian.upm.api.license.PluginLicenseManager;
import com.balsamiq.confluence.plugins.mockups.ao.BasServiceImpl;
import com.balsamiq.confluence.plugins.mockups.mail.MailService;
import com.balsamiq.confluence.plugins.mockups.mail.MailServiceImpl;
import com.balsamiq.scheduler.*;
import org.osgi.framework.ServiceRegistration;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import com.atlassian.plugins.osgi.javaconfig.OsgiServices;
import org.springframework.context.annotation.Configuration;

import static com.balsamiq.confluence.plugins.mockups.UrlUtils.isMainDomain;

@Configuration
public class SpringBeans {
    @Bean
    public GroupManager groupManager() {
        return OsgiServices.importOsgiService(GroupManager.class);
    }

    @Bean
    public com.atlassian.sal.api.ApplicationProperties applicationProperties() {
        return OsgiServices.importOsgiService(com.atlassian.sal.api.ApplicationProperties.class);
    }

    @Bean
    public TemplateRenderer templateRenderer() {
        return OsgiServices.importOsgiService(TemplateRenderer.class);
    }

    @Bean
    public PluginLicenseManager pluginLicenseManager() {
        return OsgiServices.importOsgiService(PluginLicenseManager.class);
    }

    @Bean
    public ActiveObjects activeObjects() {
        return OsgiServices.importOsgiService(ActiveObjects.class);
    }

    @Bean
    public SchedulerService schedulerService() {
        return OsgiServices.importOsgiService(SchedulerService.class);
    }


    @Bean
    public AttachmentManager attachmentManager() {
        return OsgiServices.importOsgiService(AttachmentManager.class);
    }
    @Bean
    public SimpleXsrfTokenGenerator xsrfSimpleTokenGenerator() {
        return OsgiServices.importOsgiService(com.atlassian.xwork.SimpleXsrfTokenGenerator.class);
    }

    @Bean
    public XsrfTokenGenerator xsrfTokenGenerator() {
        return OsgiServices.importOsgiService(com.atlassian.xwork.XsrfTokenGenerator.class);
    }

    @Bean
    public PermissionManager permissionManager() {
        return OsgiServices.importOsgiService(PermissionManager.class);
    }

    @Bean
    public LicenseService licenseService() {
        return OsgiServices.importOsgiService(LicenseService.class);
    }

    @Bean
    public MailQueue mailQueue() {
        return OsgiServices.importOsgiService(MailQueue.class);
    }

    @Bean
    public BandanaManager bandanaManager() {
        return OsgiServices.importOsgiService(BandanaManager.class);
    }

    @Bean
    public PageManager pageManager() {
        return OsgiServices.importOsgiService(PageManager.class);
    }

    @Bean
    public BootstrapManager bootstrapManager() {
        return OsgiServices.importOsgiService(BootstrapManager.class);
    }

    @Bean
    public NotificationManager notificationManager() {
        return OsgiServices.importOsgiService(NotificationManager.class);
    }

    @Bean
    public AccessModeCompatService accessModeCompatService() {
        return OsgiServices.importOsgiService(AccessModeCompatService.class);
    }

    @Bean
    public MultiQueueTaskManager multiQueueTaskManager() {
        return OsgiServices.importOsgiService(MultiQueueTaskManager.class);
    }

    @Bean
    public UserAccessor userAccessor() {
        return OsgiServices.importOsgiService(UserAccessor.class);
    }

    // BALSAMIQ

    @Bean
    public MockupsMPLicenseManager marketplaceLicenseManager(PluginLicenseManager p_licenseManager, com.atlassian.sal.api.ApplicationProperties applicationProperties) {
        String baseUrl = applicationProperties.getBaseUrl(UrlMode.ABSOLUTE);
        return new MockupsMPLicenseManager(p_licenseManager, isMainDomain(baseUrl,"balsamiqstaff") || isMainDomain(baseUrl, "localhost"));
    }

    @Bean
    public MockupsLicenseManager balsamiqLicenseManager(MockupsMPLicenseManager p_licenseManager,
                                                        LicenseService p_licenseService,
                                                        BandanaManager p_bandanaManager,
                                                        GroupManager p_groupManager,
                                                        com.atlassian.sal.api.ApplicationProperties p_applicationProperties)
    {
        return new MockupsLicenseManager(p_licenseManager, p_licenseService, p_bandanaManager, p_groupManager, p_applicationProperties);
    }

    @Bean
    public MailServiceImpl mailServiceImpl(MultiQueueTaskManager taskManager) {
        return new MailServiceImpl(taskManager);
    }

    @Bean
    public BasServiceImpl basServiceImpl(ActiveObjects ao,
                                         PageManager pagerManager,
                                         AttachmentManager attachmentManager,
                                         com.atlassian.confluence.setup.BootstrapManager bootstrapManager,
                                         NotificationManager notificationManager,
                                         AccessModeCompatService accessModeCompatService,
                                         MailService mailService,
                                         com.atlassian.sal.api.ApplicationProperties appProps,
                                         PermissionManager permissionManager,
                                         MockupsLicenseManager licenseManager,
                                         UserAccessor userAccessor) {
        return new BasServiceImpl(ao, pagerManager, attachmentManager, bootstrapManager, notificationManager, accessModeCompatService, mailService, appProps, permissionManager, licenseManager, userAccessor);
    }

    @Bean
    public JobRunnerNotificationImpl jobRunnerNotification(BasServiceImpl basService) {
        return new JobRunnerNotificationImpl(basService);
    }

    @Bean
    public JobRunner_BAS_GardeningImpl jobRunnerBasGardening(BasServiceImpl basService) {
        return new JobRunner_BAS_GardeningImpl(basService);
    }

    @Bean
    public ScheduleServiceImpl scheduleServiceImpl(SchedulerService schedulerService,
                                                   JobRunnerNotification jobRunner,
                                                   JobRunner_BAS_Gardening jobRunner_bas_gardening) {
        return new ScheduleServiceImpl(schedulerService, jobRunner, jobRunner_bas_gardening);
    }

    @Bean
    public LifecycleAware launcher(ScheduleServiceImpl mySchedulerService) {
        return new Launcher(mySchedulerService);
    }

    @Bean
    public FactoryBean<ServiceRegistration> exportLauncherService(final Launcher schedulerLauncher) {
        return OsgiServices.exportOsgiService(schedulerLauncher, ExportOptions.as(LifecycleAware.class));
    }
}