package basic.boardproject.repository;

import basic.boardproject.domain.ArticleComment;
import basic.boardproject.domain.QArticleComment;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ArticleCommentRepository extends
        JpaRepository<ArticleComment,Long>,
        QuerydslPredicateExecutor<ArticleComment>,
        QuerydslBinderCustomizer<QArticleComment> {

    List<ArticleComment> findByArticle_Id(Long articleId);

    @Override
    default void customize(QuerydslBindings bindings, QArticleComment root) {
        // 선택적인 Field 필터링
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.content, root.createdBy, root.createdAt);
        // bindings.bind(root.content).first(StringExpression::likeIgnoreCase); // Like '{v}'
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase); // Like '%{v}%'
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase); // Like '%{v}%'
        bindings.bind(root.createdAt).first(DateTimeExpression::eq); // = '{v}'
    }
}
