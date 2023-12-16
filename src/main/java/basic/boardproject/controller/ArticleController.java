package basic.boardproject.controller;

import basic.boardproject.domain.type.SearchType;
import basic.boardproject.dto.response.ArticleCommentResponse;
import basic.boardproject.dto.response.ArticleResponse;
import basic.boardproject.dto.response.ArticleWithCommentResponse;
import basic.boardproject.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/articles")
@Controller
public class ArticleController {

    private final ArticleService articleService;

    /**
     * 게시글 목록
     * */
    @GetMapping
    public String articles(
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable page,
            ModelMap map) {
        map.addAttribute("articles", articleService.searchArticles(searchType, searchValue, page).map(ArticleResponse::from));

        return "articles/index";
    }

    /**
     * 게시글 상세 조회
     * */
    @GetMapping("/{articleId}")
    public String article(@PathVariable Long articleId, ModelMap map) {
        ArticleWithCommentResponse article = ArticleWithCommentResponse.from(articleService.getArticle(articleId));
        map.addAttribute("article", article);
        map.addAttribute("articleComments", article.articleCommentResponses());

        return "articles/detail";
    }


}
