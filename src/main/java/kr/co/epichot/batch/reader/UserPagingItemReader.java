package kr.co.epichot.batch.reader;

import java.util.Collections;
import java.util.List;
import kr.co.epichot.batch.batch.LoguinClient;
import kr.co.epichot.batch.batch.PagedModel;
import kr.co.epichot.batch.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPagingItemReader extends AbstractPagingItemReader<User> {

  private final LoguinClient loguinClient;

  @Override
  protected void doReadPage() {
    PagedModel<User> userPagedModel = loguinClient.getAllUsers(getPage(), getPageSize()).block();
    if (userPagedModel == null) {
      this.results = Collections.emptyList();
      return;
    }

    this.results = (List<User>) userPagedModel.getItems();
  }

  @Override
  protected void doJumpToPage(int itemIndex) {

  }
}
