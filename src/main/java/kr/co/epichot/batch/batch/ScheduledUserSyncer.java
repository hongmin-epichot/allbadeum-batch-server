package kr.co.epichot.batch.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledUserSyncer {

  private final Job syncUpdatedUsersJob;
  private final JobLauncher jobLauncher;

  private JobParameters jobParameters = null;

  public ScheduledUserSyncer(@Qualifier("syncUpdatedUsersJob") Job syncUpdatedUsersJob,
      JobLauncher jobLauncher) {
    this.syncUpdatedUsersJob = syncUpdatedUsersJob;
    this.jobLauncher = jobLauncher;
  }

  @Scheduled(fixedDelay = 1000 * 60 * 30)
  public void sync() throws JobParametersInvalidException {
    JobParametersIncrementer incrementer = syncUpdatedUsersJob.getJobParametersIncrementer();
    if (incrementer == null) {
      throw new JobParametersInvalidException("JobParametersIncrementer does not exist.");
    }

    jobParameters = incrementer.getNext(jobParameters);
    try {
      jobLauncher.run(syncUpdatedUsersJob, jobParameters);
    } catch (JobExecutionException e) {
      e.printStackTrace();
    }
  }
}
