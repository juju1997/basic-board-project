package basic.boardproject.repository.querydsl;

import java.util.List;

public interface HashtagRepositoryCustom {
    List<String> findAllHashtagNames();
}