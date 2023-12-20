package basic.boardproject.controller;

import basic.boardproject.dto.UserAccountDto;
import basic.boardproject.dto.request.ArticleCommentRequest;
import basic.boardproject.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/comments")
public class ArticleCommentController {

    private final ArticleCommentService articleCommentService;

    /**
     * 댓글 추가
     * */
    @PostMapping("/new")
    public String postNewArticleComment(ArticleCommentRequest articleCommentRequest) {

        // TODO : 인증 정보 추가
        articleCommentService.saveArticleComment(articleCommentRequest.toDto(UserAccountDto.of(
                "juju", "123123", "juju@mail.com", "juju", "memo"
        )));

        return "redirect:/articles/" + articleCommentRequest.articleId();
    }

    /**
     * 댓글 삭제
     * */
    @PostMapping("/{commentId}/delete")
    public String postNewArticleComment(@PathVariable Long commentId, Long articleId) {

        articleCommentService.deleteArticleComment(commentId);

        return "redirect:/articles/" + articleId;
    }

}
