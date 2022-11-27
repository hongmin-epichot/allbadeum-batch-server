package kr.co.epichot.batch.batch;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import kr.co.epichot.batch.entity.User;
import kr.co.epichot.batch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSyncTasklet implements Tasklet {

  private final static LocalDateTime UPDATED_AFTER_MIN = LocalDateTime.of(2022, 1, 1, 0, 0);
  private final LoguinClient loguinClient;
  private final JobExplorer jobExplorer;
  private final UserRepository userRepository;

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {

    LocalDateTime updatedAfter = getLastExecutionTime();
    if (updatedAfter == null) {
      updatedAfter = UPDATED_AFTER_MIN;
    }

    List<User> updatedUsers = loguinClient.getUpdatedUsers(updatedAfter).block();
    if (updatedUsers == null || updatedUsers.isEmpty()) {
      return null;
    }

    userRepository.saveAll(updatedUsers);
    return RepeatStatus.FINISHED;
  }

  private LocalDateTime getLastExecutionTime() {
    JobInstance jobInstance = jobExplorer.getLastJobInstance("userSyncJob");
    if (jobInstance == null) {
      return null;
    }

    JobExecution jobExecution = jobExplorer.getLastJobExecution(jobInstance);
    if (jobExecution == null) {
      return null;
    }

    Date startTime = jobExecution.getStartTime();
    if (startTime == null) {
      return null;
    }
    return startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
  }
}
