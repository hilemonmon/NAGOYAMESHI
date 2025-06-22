package com.example.nagoyameshi.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.form.RestaurantEditForm;
import com.example.nagoyameshi.form.RestaurantRegisterForm;
import com.example.nagoyameshi.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;

/**
 * {@link AdminRestaurantService} の実装クラス。
 * 画像ファイルの保存も行う。
 */
@Service
@RequiredArgsConstructor
public class AdminRestaurantServiceImpl implements AdminRestaurantService {

    /** 画像保存先ディレクトリ */
    private static final Path STORAGE_DIR = Paths.get("src/main/resources/static/storage");

    private final RestaurantRepository restaurantRepository;
    private final CategoryRestaurantService categoryRestaurantService;
    private final RegularHolidayRestaurantService regularHolidayRestaurantService;

    /** {@inheritDoc} */
    @Override
    public Page<Restaurant> getRestaurants(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            return restaurantRepository.findAll(pageable);
        }
        return restaurantRepository.findByNameContaining(keyword, pageable);
    }

    /** {@inheritDoc} */
    @Override
    public Restaurant getRestaurant(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
    }

    /** {@inheritDoc} */
    @Override
    public Restaurant create(RestaurantRegisterForm form) {
        String filename = storeImage(form.getImageFile());

        Restaurant restaurant = Restaurant.builder()
                .name(form.getName())
                .image(filename)
                .description(form.getDescription())
                .lowestPrice(form.getLowestPrice())
                .highestPrice(form.getHighestPrice())
                .postalCode(form.getPostalCode())
                .address(form.getAddress())
                .openingTime(form.getOpeningTime())
                .closingTime(form.getClosingTime())
                .seatingCapacity(form.getSeatingCapacity())
                .build();
        Restaurant saved = restaurantRepository.save(restaurant);
        // カテゴリが選択されていれば中間テーブルを作成
        if (form.getCategoryIds() != null) {
            categoryRestaurantService.createCategoriesRestaurants(form.getCategoryIds(), saved);
        }
        // 定休日が選択されていれば中間テーブルを作成
        if (form.getRegularHolidayIds() != null) {
            regularHolidayRestaurantService.createRegularHolidaysRestaurants(form.getRegularHolidayIds(), saved);
        }
        return saved;
    }

    /** {@inheritDoc} */
    @Override
    public Restaurant createRestaurant(RestaurantRegisterForm form) {
        // create() と同一の処理を行う
        return create(form);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isValidPrices(Integer lowestPrice, Integer highestPrice) {
        if (lowestPrice == null || highestPrice == null) {
            return false;
        }
        return highestPrice >= lowestPrice;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isValidBusinessHours(java.time.LocalTime opening, java.time.LocalTime closing) {
        if (opening == null || closing == null) {
            return false;
        }
        return closing.isAfter(opening);
    }

    /** {@inheritDoc} */
    @Override
    public Restaurant update(Long id, RestaurantEditForm form) {
        Restaurant restaurant = getRestaurant(id);

        if (form.getImageFile() != null && !form.getImageFile().isEmpty()) {
            String filename = storeImage(form.getImageFile());
            restaurant.setImage(filename);
        }

        restaurant.setName(form.getName());
        restaurant.setDescription(form.getDescription());
        restaurant.setLowestPrice(form.getLowestPrice());
        restaurant.setHighestPrice(form.getHighestPrice());
        restaurant.setPostalCode(form.getPostalCode());
        restaurant.setAddress(form.getAddress());
        restaurant.setOpeningTime(form.getOpeningTime());
        restaurant.setClosingTime(form.getClosingTime());
        restaurant.setSeatingCapacity(form.getSeatingCapacity());

        Restaurant saved = restaurantRepository.save(restaurant);
        // 既存のカテゴリ情報をフォーム内容と同期
        categoryRestaurantService.syncCategoriesRestaurants(form.getCategoryIds(), saved);
        // 定休日情報をフォーム内容と同期
        regularHolidayRestaurantService.syncRegularHolidaysRestaurants(form.getRegularHolidayIds(), saved);
        return saved;
    }

    /** {@inheritDoc} */
    @Override
    public void delete(Long id) {
        restaurantRepository.deleteById(id);
    }

    /**
     * 画像を保存し、保存したファイル名を返す。
     * 画像が指定されていない場合は null を返す。
     *
     * @param file アップロードされた画像
     * @return 保存したファイル名
     */
    private String storeImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            if (!Files.exists(STORAGE_DIR)) {
                Files.createDirectories(STORAGE_DIR);
            }
            String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
            String filename = UUID.randomUUID().toString() + (ext != null ? "." + ext : "");
            Path dest = STORAGE_DIR.resolve(filename);
            file.transferTo(dest);
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
}
