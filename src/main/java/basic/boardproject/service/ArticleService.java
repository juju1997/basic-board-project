package basic.boardproject.service;

import basic.boardproject.domain.Article;
import basic.boardproject.domain.type.SearchType;
import basic.boardproject.dto.ArticleDto;
import basic.boardproject.dto.ArticleWithCommentsDto;
import basic.boardproject.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    /**
     * Article 검색
     * */
    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
        if (searchKeyword == null || searchKeyword.isBlank()) {
            return articleRepository.findAll(pageable).map(ArticleDto::from);
        }

        return switch (searchType) {
            case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::from);
            case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::from);
            case ID -> articleRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable).map(ArticleDto::from);
            case HASHTAG -> articleRepository.findByHashtag(searchKeyword, pageable).map(ArticleDto::from);
            case NICKNAME -> articleRepository.findByUserAccount_NicknameContaining("#" + searchKeyword, pageable).map(ArticleDto::from);
        };
    }

    /**
     * Article 단건 조회, 해당 Article 의 댓글 조회
     * */
    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleWithCommentsDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
    }

    /**
     * Article 저장
     * */
    public void saveArticle(ArticleDto dto) {
        articleRepository.save(dto.toEntity());
    }


    /**
     * Article 수정
     * */
    public void updateArticle(ArticleDto dto) {
        try {
            Article article = articleRepository.getReferenceById(dto.id());
            if (dto.title() != null ){ article.setTitle(dto.title()); }
            if (dto.title() != null ){ article.setContent(dto.content()); }
            article.setHashtag(dto.hashtag());
            // save를 안해도 영속성 컨텐츠에서 dto를 인식하여 자동으로 트랜잭션으로 묶고 커밋한다.
        } catch (EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습니다 - dto: {}", dto);
        }
    }

    /**
     * Article 삭제
     * */
    public void deleteArticle(long articleId) {
        articleRepository.deleteById(articleId);
    }

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticlesViaHashtag(String hashtag, Pageable pageable) {
        if (hashtag == null || hashtag.isBlank()) {
            return Page.empty(pageable);
        }

        return articleRepository.findByHashtag(hashtag, pageable).map(ArticleDto::from);
    }

    public List<String> getHashtags() {
        return articleRepository.findAllHashtagNames();
    }
}
