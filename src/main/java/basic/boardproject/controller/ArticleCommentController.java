package basic.boardproject.controller;

import basic.boardproject.dto.UserAccountDto;
import basic.boardproject.dto.request.ArticleCommentRequest;
import basic.boardproject.dto.security.BoardPrincipal;
import basic.boardproject.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public String postNewArticleComment(
            ArticleCommentRequest articleCommentRequest,
            @AuthenticationPrincipal BoardPrincipal boardPrincipal
    ) {

        articleCommentService.saveArticleComment(articleCommentRequest.toDto(boardPrincipal.toDto()));

        return "redirect:/articles/" + articleCommentRequest.articleId();
    }

    /**
     * 댓글 삭제
     * */
    @PostMapping("/{commentId}/delete")
    public String postNewArticleComment(
            @PathVariable Long commentId, Long articleId,
            @AuthenticationPrincipal BoardPrincipal boardPrincipal
    ) {

        articleCommentService.deleteArticleComment(commentId, boardPrincipal.getUsername());

        return "redirect:/articles/" + articleId;
    }

}
