package kr.co.epichot.batch.batch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Collection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@NoArgsConstructor
public class PagedModel<T> {

  @JsonInclude(Include.ALWAYS)
  private Collection<T> items;
  private long pageNumber;
  private long pageSize;
  private long totalCount;
  private long totalPages;

  public PagedModel(Collection<T> items, long pageNumber, long pageSize, long totalCount,
      long totalPages) {
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
    this.totalCount = totalCount;
    this.totalPages = totalPages;
    this.items = items;
  }

  public static <T> PagedModel<T> of(Page<T> items) {
    return new PagedModel<T>(items.getContent(), items.getNumber(), items.getSize(),
        items.getTotalElements(), items.getTotalPages());
  }

  public static <T> PagedModel<T> of(Collection<T> items, long pageNumber, long pageSize,
      long totalCount, long totalPages) {
    return new PagedModel<T>(items, pageNumber, pageSize, totalCount, totalPages);
  }

}
