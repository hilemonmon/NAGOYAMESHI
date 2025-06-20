package com.example.nagoyameshi.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
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
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return restaurantRepository.save(restaurant);
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
        restaurant.setUpdatedAt(LocalDateTime.now());

        return restaurantRepository.save(restaurant);
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
