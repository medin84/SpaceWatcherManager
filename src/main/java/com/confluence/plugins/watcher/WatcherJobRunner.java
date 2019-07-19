package com.confluence.plugins.watcher;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.sal.api.transaction.TransactionCallback;
import com.atlassian.sal.api.transaction.TransactionTemplate;
import com.atlassian.scheduler.JobRunner;
import com.atlassian.scheduler.JobRunnerRequest;
import com.atlassian.scheduler.JobRunnerResponse;

import javax.annotation.Nullable;
import javax.inject.Named;

@Named
public class WatcherJobRunner implements JobRunner {

    @ComponentImport
    private PluginSettingsFactory psf;
    @ComponentImport
    private ActiveObjects ao;
    @ComponentImport
    private final TransactionTemplate transactionTemplate;

    public WatcherJobRunner(PluginSettingsFactory psf, ActiveObjects ao, TransactionTemplate transactionTemplate) {
        this.psf = psf;
        this.ao = ao;
        this.transactionTemplate = transactionTemplate;
    }

    @Nullable
    @Override
    public JobRunnerResponse runJob(JobRunnerRequest request) {
        if (request.isCancellationRequested()) {
            return JobRunnerResponse.aborted("Job cancelled.");
        }

        WatcherService wjs = new WatcherService(new SettingsService(psf), ao);

        transactionTemplate.execute((TransactionCallback) () -> {
            wjs.addUsersToSpaceWatchersBySettings();
            return null;
        });

        return JobRunnerResponse.success();
    }
}
