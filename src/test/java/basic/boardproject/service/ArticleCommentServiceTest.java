package basic.boardproject.service;

import basic.boardproject.domain.Article;
import basic.boardproject.domain.ArticleComment;
import basic.boardproject.domain.UserAccount;
import basic.boardproject.dto.ArticleCommentDto;
import basic.boardproject.repository.ArticleCommentRepository;
import basic.boardproject.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;


@DisplayName("비즈니스 로직 - 댓글")
@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {

    @InjectMocks private ArticleCommentService sut;

    @Mock private ArticleCommentRepository articleCommentRepository;
    @Mock private ArticleRepository articleRepository;



    @DisplayName("게시글 ID로 조회하면, 해당하는 댓글 리스트를 반환한다.")
    @Test
    void givenArticleId_whenSearchingArticleComments_thenReturnsArticleComments() {

        // Given
        Long articleId = 1L;

        UserAccount userAccount = UserAccount.of("juju", "123123", null, null, null);
        given(articleRepository.findById(articleId)).willReturn(Optional.of(
                Article.of(userAccount,"title","content","#java"))
        );

        // When
        List<ArticleCommentDto> articleComments = sut.searchArticleComment(articleId);

        // Then
        assertThat(articleComments).isNotNull();
        then(articleRepository).should().findById(articleId);
    }

    @DisplayName("댓글 정보를 입력하면, 댓글을 저장한다.")
    @Test
    void givenArticleCommentInfo_whenSavingArticleComment_thenSavesArticleComment() {
        // Given
        given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(null);

        // When
        sut.saveArticleComment(ArticleCommentDto.of(LocalDateTime.now(), "JUJU", LocalDateTime.now(), "JUJU", "comment"));

        // Then
        then(articleCommentRepository).should().save(any(ArticleComment.class));
    }
}