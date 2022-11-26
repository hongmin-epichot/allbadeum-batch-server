package kr.co.epichot.batch.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "`user`")
public class User {

  @Id
  private String id;
  private String email;
  private String name;
  private String phoneNumber;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public User(String id) {
    this.id = id;
  }
}
