package kr.co.epichot.batch.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserSyncController {

  private final Job syncAllUsersJob;
  private final JobLauncher jobLauncher;
  private JobParameters jobParameters = null;

  public UserSyncController(@Qualifier("syncAllUsersJob") Job syncAllUsersJob,
      JobLauncher jobLauncher) {
    this.syncAllUsersJob = syncAllUsersJob;
    this.jobLauncher = jobLauncher;
  }

  @PostMapping("/sync/all")
  public ResponseEntity<Void> syncAllUsers() throws JobParametersInvalidException {
    JobParametersIncrementer incrementer = syncAllUsersJob.getJobParametersIncrementer();
    if (incrementer == null) {
      throw new JobParametersInvalidException("JobParametersIncrementer does not exist.");
    }

    jobParameters = incrementer.getNext(jobParameters);
    try {
      jobLauncher.run(syncAllUsersJob, jobParameters);
    } catch (JobExecutionException e) {
      e.printStackTrace();
    }

    return ResponseEntity.ok().build();
  }

}
