package com.example.nagoyameshi.controller.admin;

// テストで使用する静的メソッドをインポート
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalTime;
import java.util.List;

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
import org.springframework.http.MediaType;
import com.example.nagoyameshi.service.CategoryService;
import com.example.nagoyameshi.service.CategoryRestaurantService;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.form.RestaurantEditForm;
import com.example.nagoyameshi.form.RestaurantRegisterForm;
import com.example.nagoyameshi.security.UserDetailsServiceImpl;
import com.example.nagoyameshi.security.WebSecurityConfig;
import com.example.nagoyameshi.service.AdminRestaurantService;

/**
 * {@link AdminRestaurantController} の動作を検証するテストクラス。
 * 管理者用機能が適切に制御されているか確認する。
 */
@WebMvcTest(AdminRestaurantController.class)
@Import(WebSecurityConfig.class)
@AutoConfigureMockMvc
class AdminRestaurantControllerTest {

    /** HTTP リクエストを模擬するためのオブジェクト */
    @Autowired
    private MockMvc mockMvc;

    /** サービス層をモック化して登録 */
    @MockitoBean
    private AdminRestaurantService adminRestaurantService;

    /** カテゴリ情報を扱うサービスをモック化して登録 */
    @MockitoBean
    private CategoryService categoryService;

    /** 中間テーブル操作サービスをモック化して登録 */
    @MockitoBean
    private CategoryRestaurantService categoryRestaurantService;

    /** 認証処理に必要なサービスもモック化して登録 */
    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    // --- 一覧ページのテスト ----------------------------------------------------

    @Test
    @DisplayName("未ログインの場合は一覧ページからログインページにリダイレクトされる")
    void 未ログインの一覧ページアクセスはログインにリダイレクト() throws Exception {
        mockMvc.perform(get("/admin/restaurants"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("一般ユーザーは一覧ページにアクセスすると403エラー")
    void 一般ユーザーは一覧ページにアクセスできない() throws Exception {
        when(adminRestaurantService.getRestaurants(anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 10), 0));

        mockMvc.perform(get("/admin/restaurants").with(user("user").roles("FREE_MEMBER")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("管理者は一覧ページを閲覧できる")
    void 管理者は一覧ページを閲覧できる() throws Exception {
        when(adminRestaurantService.getRestaurants(anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 10), 0));

        mockMvc.perform(get("/admin/restaurants").param("keyword", "sushi").param("page", "0")
                .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/restaurants/index"));
    }

    // --- 詳細ページのテスト ----------------------------------------------------

    @Test
    @DisplayName("未ログインの場合は詳細ページからログインページにリダイレクトされる")
    void 未ログインの詳細ページアクセスはログインにリダイレクト() throws Exception {
        mockMvc.perform(get("/admin/restaurants/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("一般ユーザーは詳細ページにアクセスすると403エラー")
    void 一般ユーザーは詳細ページにアクセスできない() throws Exception {
        when(adminRestaurantService.getRestaurant(1L))
                .thenReturn(Restaurant.builder()
                        .id(1L)
                        .categoriesRestaurants(List.of())
                        .build());

        mockMvc.perform(get("/admin/restaurants/1").with(user("user").roles("FREE_MEMBER")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("管理者は詳細ページを閲覧できる")
    void 管理者は詳細ページを閲覧できる() throws Exception {
        when(adminRestaurantService.getRestaurant(1L))
                .thenReturn(Restaurant.builder()
                        .id(1L)
                        .categoriesRestaurants(List.of())
                        .build());

        mockMvc.perform(get("/admin/restaurants/1").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/restaurants/show"));
    }

    // --- 登録ページのテスト ----------------------------------------------------

    @Test
    @DisplayName("未ログインの場合は登録ページからログインページにリダイレクトされる")
    void 未ログインの登録ページアクセスはログインにリダイレクト() throws Exception {
        mockMvc.perform(get("/admin/restaurants/register"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("一般ユーザーは登録ページにアクセスすると403エラー")
    void 一般ユーザーは登録ページにアクセスできない() throws Exception {
        mockMvc.perform(get("/admin/restaurants/register").with(user("user").roles("FREE_MEMBER")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("管理者は登録ページを閲覧できる")
    void 管理者は登録ページを閲覧できる() throws Exception {
        mockMvc.perform(get("/admin/restaurants/register").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/restaurants/register"));
    }

    // --- 店舗登録機能のテスト -------------------------------------------------

    @Test
    @DisplayName("未ログインの場合は店舗を登録せずにログインページにリダイレクトする")
    void 未ログインの店舗登録はログインにリダイレクト() throws Exception {
        mockMvc.perform(post("/admin/restaurants/create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("一般ユーザーは店舗を登録できず403エラー")
    void 一般ユーザーは店舗を登録できない() throws Exception {
        mockMvc.perform(post("/admin/restaurants/create").with(user("user").roles("FREE_MEMBER")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("管理者は店舗登録後に一覧ページへリダイレクトされる")
    void 管理者は店舗登録後に一覧ページへリダイレクトされる() throws Exception {
        when(adminRestaurantService.createRestaurant(any(RestaurantRegisterForm.class))).thenReturn(Restaurant.builder().id(1L).build());
        when(adminRestaurantService.isValidPrices(any(), any())).thenReturn(true);
        when(adminRestaurantService.isValidBusinessHours(any(), any())).thenReturn(true);

        mockMvc.perform(multipart("/admin/restaurants/create")
                        .with(user("admin").roles("ADMIN"))
                        .param("name", "name")
                        .param("description", "desc")
                        .param("lowestPrice", "1000")
                        .param("highestPrice", "2000")
                        .param("postalCode", "1234567")
                        .param("address", "Nagoya")
                        .param("openingTime", "10:00")
                        .param("closingTime", "20:00")
                        .param("seatingCapacity", "10")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/restaurants"));
    }

    // --- 編集ページのテスト ----------------------------------------------------

    @Test
    @DisplayName("未ログインの場合は編集ページからログインページにリダイレクトされる")
    void 未ログインの編集ページアクセスはログインにリダイレクト() throws Exception {
        mockMvc.perform(get("/admin/restaurants/1/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("一般ユーザーは編集ページにアクセスすると403エラー")
    void 一般ユーザーは編集ページにアクセスできない() throws Exception {
        when(adminRestaurantService.getRestaurant(1L)).thenReturn(Restaurant.builder().id(1L).build());

        mockMvc.perform(get("/admin/restaurants/1/edit").with(user("user").roles("FREE_MEMBER")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("管理者は編集ページを閲覧できる")
    void 管理者は編集ページを閲覧できる() throws Exception {
        when(adminRestaurantService.getRestaurant(1L)).thenReturn(Restaurant.builder().id(1L)
                .name("name")
                .description("desc")
                .lowestPrice(1000)
                .highestPrice(2000)
                .postalCode("1234567")
                .address("Nagoya")
                .openingTime(LocalTime.of(10, 0))
                .closingTime(LocalTime.of(20, 0))
                .seatingCapacity(10)
                .build());

        mockMvc.perform(get("/admin/restaurants/1/edit").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/restaurants/edit"));
    }

    // --- 店舗更新機能のテスト -------------------------------------------------

    @Test
    @DisplayName("未ログインの場合は店舗を更新せずにログインページにリダイレクトする")
    void 未ログインの店舗更新はログインにリダイレクト() throws Exception {
        mockMvc.perform(post("/admin/restaurants/1/update"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("一般ユーザーは店舗を更新できず403エラー")
    void 一般ユーザーは店舗を更新できない() throws Exception {
        mockMvc.perform(post("/admin/restaurants/1/update").with(user("user").roles("FREE_MEMBER")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("管理者は店舗更新後に詳細ページへリダイレクトされる")
    void 管理者は店舗更新後に詳細ページへリダイレクトされる() throws Exception {
        when(adminRestaurantService.update(any(Long.class), any(RestaurantEditForm.class)))
                .thenReturn(Restaurant.builder().id(1L).build());

        mockMvc.perform(multipart("/admin/restaurants/1/update")
                        .with(user("admin").roles("ADMIN"))
                        .param("name", "name")
                        .param("description", "desc")
                        .param("lowestPrice", "1000")
                        .param("highestPrice", "2000")
                        .param("postalCode", "1234567")
                        .param("address", "Nagoya")
                        .param("openingTime", "10:00")
                        .param("closingTime", "20:00")
                        .param("seatingCapacity", "10")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/restaurants/1"));
    }

    // --- 店舗削除機能のテスト -------------------------------------------------

    @Test
    @DisplayName("未ログインの場合は店舗を削除せずにログインページにリダイレクトする")
    void 未ログインの店舗削除はログインにリダイレクト() throws Exception {
        mockMvc.perform(post("/admin/restaurants/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("一般ユーザーは店舗を削除できず403エラー")
    void 一般ユーザーは店舗を削除できない() throws Exception {
        mockMvc.perform(post("/admin/restaurants/1/delete").with(user("user").roles("FREE_MEMBER")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("管理者は店舗削除後に一覧ページへリダイレクトされる")
    void 管理者は店舗削除後に一覧ページへリダイレクトされる() throws Exception {
        mockMvc.perform(post("/admin/restaurants/1/delete").with(user("admin").roles("ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/restaurants"));
    }
}
