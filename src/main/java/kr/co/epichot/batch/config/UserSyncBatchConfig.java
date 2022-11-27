package kr.co.epichot.batch.config;

import javax.persistence.EntityManagerFactory;
import kr.co.epichot.batch.batch.UserSyncTasklet;
import kr.co.epichot.batch.entity.User;
import kr.co.epichot.batch.reader.UserPagingItemReader;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
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
  private final EntityManagerFactory entityManagerFactory;

  @Bean
  public Job syncUpdatedUsersJob(@Qualifier("syncUpdatedUsersStep") Step syncUpdatedUsersStep) {
    return jobBuilderFactory.get("userSyncJob")
        .incrementer(new RunIdIncrementer())
        .flow(syncUpdatedUsersStep)
        .end()
        .build();
  }

  @Bean
  public Step syncUpdatedUsersStep(UserSyncTasklet userSyncTasklet) {
    return stepBuilderFactory.get("userSyncStep")
        .tasklet(userSyncTasklet)
        .build();
  }

  @Bean
  public Job syncAllUsersJob(@Qualifier("syncAllUsersStep") Step syncAllUsersStep) {
    return jobBuilderFactory.get("syncAllUsersJob")
        .incrementer(new RunIdIncrementer())
        .flow(syncAllUsersStep)
        .end()
        .build();
  }

  @Bean
  public Step syncAllUsersStep(UserPagingItemReader userPagingItemReader,
      JpaItemWriter<User> userJpaItemWriter) {
    return stepBuilderFactory.get("syncAllUsersStep")
        .<User, User>chunk(10)
        .reader(userPagingItemReader)
        .writer(userJpaItemWriter)
        .build();
  }

  @Bean
  public JpaItemWriter<User> userJpaItemWriter() throws Exception {
    JpaItemWriter<User> itemWriter = new JpaItemWriterBuilder<User>()
        .entityManagerFactory(entityManagerFactory)
        .build();
    itemWriter.afterPropertiesSet();
    return itemWriter;
  }
}
