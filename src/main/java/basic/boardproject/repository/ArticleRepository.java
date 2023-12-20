package basic.boardproject.repository;

import basic.boardproject.domain.Article;
import basic.boardproject.domain.QArticle;
import basic.boardproject.repository.querydsl.ArticleRepositoryCustom;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        ArticleRepositoryCustom,
        QuerydslPredicateExecutor<Article>,
        QuerydslBinderCustomizer<QArticle> {

    // Containing 을 붙히면 LIKE 검색
    Page<Article> findByTitleContaining(String title, Pageable pageable);
    Page<Article> findByContentContaining(String content, Pageable pageable);
    Page<Article> findByUserAccount_UserIdContaining(String userId, Pageable pageable);
    Page<Article> findByHashtag(String hashtag, Pageable pageable);
    Page<Article> findByUserAccount_NicknameContaining(String nickName, Pageable pageable);

    void deleteByIdAndUserAccount_UserId(Long articleId, String userId);

    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {
        // 선택적인 Field 필터링
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.title, root.content, root.hashtag, root.createdBy, root.createdAt);
        // bindings.bind(root.title).first(StringExpression::likeIgnoreCase); // Like '{v}'
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase); // Like '%{v}%'
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase); // Like '%{v}%'
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase); // Like '%{v}%'
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase); // Like '%{v}%'
        bindings.bind(root.createdAt).first(DateTimeExpression::eq); // = '{v}'
    }
}
