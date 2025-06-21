package com.example.nagoyameshi.controller.admin;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.form.CategoryEditForm;
import com.example.nagoyameshi.form.CategoryRegisterForm;
import com.example.nagoyameshi.security.UserDetailsServiceImpl;
import com.example.nagoyameshi.security.WebSecurityConfig;
import com.example.nagoyameshi.service.CategoryService;

/**
 * {@link AdminCategoryController} の動作を検証するテストクラス。
 */
@WebMvcTest(AdminCategoryController.class)
@Import(WebSecurityConfig.class)
@AutoConfigureMockMvc
class AdminCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    // --- 一覧ページのテスト ------------------------------------------------------

    @Test
    @DisplayName("未ログインの場合は一覧ページからログインページにリダイレクトされる")
    void 未ログインの一覧ページアクセスはログインにリダイレクト() throws Exception {
        mockMvc.perform(get("/admin/categories"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("一般ユーザーは一覧ページにアクセスすると403エラー")
    void 一般ユーザーは一覧ページにアクセスできない() throws Exception {
        when(categoryService.findAllCategories(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 10), 0));

        mockMvc.perform(get("/admin/categories").with(user("user").roles("FREE_MEMBER")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("管理者は一覧ページを閲覧できる")
    void 管理者は一覧ページを閲覧できる() throws Exception {
        when(categoryService.findCategoriesByNameLike(any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 10), 0));

        mockMvc.perform(get("/admin/categories").param("keyword", "ラーメン").param("page", "0")
                .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/categories/index"));
    }

    // --- カテゴリ登録のテスト ----------------------------------------------------

    @Test
    @DisplayName("未ログインの場合はカテゴリを登録せずにログインページにリダイレクトする")
    void 未ログインのカテゴリ登録はログインにリダイレクト() throws Exception {
        mockMvc.perform(post("/admin/categories/create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("一般ユーザーはカテゴリを登録できず403エラー")
    void 一般ユーザーはカテゴリを登録できない() throws Exception {
        mockMvc.perform(post("/admin/categories/create").with(user("user").roles("FREE_MEMBER")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("管理者はカテゴリ登録後に一覧ページへリダイレクトされる")
    void 管理者はカテゴリ登録後に一覧ページへリダイレクトされる() throws Exception {
        when(categoryService.createCategory(any(CategoryRegisterForm.class)))
                .thenReturn(Category.builder().id(1L).name("name").build());

        mockMvc.perform(post("/admin/categories/create")
                        .with(user("admin").roles("ADMIN"))
                        .param("name", "name"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"));
    }

    // --- カテゴリ更新のテスト ----------------------------------------------------

    @Test
    @DisplayName("未ログインの場合はカテゴリを更新せずにログインページにリダイレクトする")
    void 未ログインのカテゴリ更新はログインにリダイレクト() throws Exception {
        mockMvc.perform(post("/admin/categories/1/update"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("一般ユーザーはカテゴリを更新できず403エラー")
    void 一般ユーザーはカテゴリを更新できない() throws Exception {
        mockMvc.perform(post("/admin/categories/1/update").with(user("user").roles("FREE_MEMBER")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("管理者はカテゴリ更新後に一覧ページへリダイレクトされる")
    void 管理者はカテゴリ更新後に一覧ページへリダイレクトされる() throws Exception {
        when(categoryService.findCategoryById(1L)).thenReturn(Optional.of(Category.builder().id(1L).build()));
        when(categoryService.updateCategory(any(Long.class), any(CategoryEditForm.class)))
                .thenReturn(Category.builder().id(1L).build());

        mockMvc.perform(post("/admin/categories/1/update")
                        .with(user("admin").roles("ADMIN"))
                        .param("name", "name"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"));
    }

    // --- カテゴリ削除のテスト ----------------------------------------------------

    @Test
    @DisplayName("未ログインの場合はカテゴリを削除せずにログインページにリダイレクトする")
    void 未ログインのカテゴリ削除はログインにリダイレクト() throws Exception {
        mockMvc.perform(post("/admin/categories/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("一般ユーザーはカテゴリを削除できず403エラー")
    void 一般ユーザーはカテゴリを削除できない() throws Exception {
        mockMvc.perform(post("/admin/categories/1/delete").with(user("user").roles("FREE_MEMBER")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("管理者はカテゴリ削除後に一覧ページへリダイレクトされる")
    void 管理者はカテゴリ削除後に一覧ページへリダイレクトされる() throws Exception {
        when(categoryService.findCategoryById(1L)).thenReturn(Optional.of(Category.builder().id(1L).build()));

        mockMvc.perform(post("/admin/categories/1/delete").with(user("admin").roles("ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"));
    }
}
