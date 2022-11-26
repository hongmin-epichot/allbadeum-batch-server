package kr.co.epichot.batch.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledUserSyncer {

  private final Job job;
  private final JobLauncher jobLauncher;

  private JobParameters jobParameters = null;

  @Scheduled(fixedDelay = 1000 * 60 * 10)
  public void sync() throws JobParametersInvalidException {
    JobParametersIncrementer incrementer = job.getJobParametersIncrementer();
    if (incrementer == null) {
      throw new JobParametersInvalidException("JobParametersIncrementer does not exist.");
    }

    jobParameters = incrementer.getNext(jobParameters);
    try {
      jobLauncher.run(job, jobParameters);
    } catch (JobExecutionException e) {
      e.printStackTrace();
    }
  }
}
