package kr.co.epichot.batch.config;

import kr.co.epichot.batch.batch.UserSyncTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableBatchProcessing
@RequiredArgsConstructor
public class UserSyncBatchConfig {

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;

  @Bean
  public Job userSyncJob(Step userSyncStep) {
    return jobBuilderFactory.get("userSyncJob")
        .incrementer(new RunIdIncrementer())
        .flow(userSyncStep)
        .end()
        .build();
  }

  @Bean
  public Step userSyncStep(UserSyncTasklet userSyncTasklet) {
    return stepBuilderFactory.get("userSyncStep")
        .tasklet(userSyncTasklet)
        .build();
  }
}
