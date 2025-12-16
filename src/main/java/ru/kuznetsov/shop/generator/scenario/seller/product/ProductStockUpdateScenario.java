package ru.kuznetsov.shop.generator.scenario.seller.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.kuznetsov.shop.generator.scenario.AbstractScenario;
import ru.kuznetsov.shop.generator.service.UseCaseService;
import ru.kuznetsov.shop.generator.usecase.auth.GetUserInfoUseCase;
import ru.kuznetsov.shop.generator.usecase.entity.address.CreteAddressUseCase;
import ru.kuznetsov.shop.generator.usecase.entity.product.CreateProductBatchUseCase;
import ru.kuznetsov.shop.generator.usecase.entity.product.GetProductsByCategoryOrOwnerUseCase;
import ru.kuznetsov.shop.generator.usecase.entity.product_category.CreateCategoryUseCase;
import ru.kuznetsov.shop.generator.usecase.entity.product_category.GetAllCategoriesUseCase;
import ru.kuznetsov.shop.generator.usecase.entity.stock.CreateStockUseCase;
import ru.kuznetsov.shop.generator.usecase.entity.stock.GetStockUseCase;
import ru.kuznetsov.shop.generator.usecase.entity.store.CreteStoreUseCase;
import ru.kuznetsov.shop.generator.usecase.entity.store.GetAllStoresUseCase;
import ru.kuznetsov.shop.represent.dto.*;
import ru.kuznetsov.shop.represent.dto.auth.TokenDto;
import ru.kuznetsov.shop.represent.dto.auth.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static ru.kuznetsov.shop.generator.common.ConstValues.*;

@Component
public class ProductStockUpdateScenario extends AbstractScenario {

    private static final int STORE_AMOUNT = 2;
    private static final int CATEGORY_AMOUNT = 3;
    private static final int PRODUCT_AMOUNT = 10;
    private static final int STORE_MAX_AMOUNT = 150;

    Logger logger = LoggerFactory.getLogger(ProductStockUpdateScenario.class);

    protected ProductStockUpdateScenario(UseCaseService useCaseService) {
        super(useCaseService);
    }

    @Override
    public void run() {
        TokenDto token = getToken(SELLER_LOGIN, SELLER_PASSWORD);
        String tokenString = token.getToken();

        logger.info("Getting user");
        UserDto userDto = runUseCaseWithReturn(new GetUserInfoUseCase(tokenString)).get(0);
        logger.info("UserInfo: {}", userDto);

        UUID userId = userDto.getId();

        List<StoreDto> storeList = new ArrayList<>(getStoreList(tokenString, userId));
        List<ProductCategoryDto> productCategories = getProductCategories(tokenString);

        //Добавить категорий, если отсутствуют
        if (productCategories.isEmpty()) {
            productCategories = createProductCategories(tokenString, CATEGORY_AMOUNT);
        }

        List<String> categoryIdList = productCategories.stream()
                .map(ProductCategoryDto::getName)
                .toList();

        //Добавить магазины
        while (storeList.size() < STORE_AMOUNT) {
            AddressDto createdAddress = createAddress(tokenString);
            StoreDto createdStore = createStore(tokenString, createdAddress, userId);
            storeList.add(createdStore);
        }

        logger.info("Getting products");
        List<ProductDto> userProducts = runUseCaseWithReturn(new GetProductsByCategoryOrOwnerUseCase(tokenString, userId, null));
        logger.info("Products found: x{}", userProducts.size());

        //Сформировать список объектов для создания через /batch
        List<ProductDto> productsToCreate = new ArrayList<>();

        //Добавить товары
        while (userProducts.size() + productsToCreate.size() < PRODUCT_AMOUNT) {
            productsToCreate.add(getProduct(
                    categoryIdList.get(new Random().nextInt(categoryIdList.size())),
                    userId.toString()
            ));
        }

        if (!productsToCreate.isEmpty()) {
            userProducts.addAll(createProductBatch(tokenString, productsToCreate.toArray(new ProductDto[0])));
        }

        //Добавить запасы в магазины
        for (StoreDto store : storeList) {
            for (ProductDto product : userProducts) {
                List<StockDto> stockList = getStockList(tokenString, store.getId(), product.getId());

                if (stockList.isEmpty()) {
                    createStock(tokenString, store.getName(), product.getId(), STORE_MAX_AMOUNT);
                }
            }
        }
    }

    private List<StoreDto> getStoreList(String tokenString, UUID ownerId) {
        logger.info("Getting store list");
        List<StoreDto> storeDtos = runUseCaseWithReturn(new GetAllStoresUseCase(tokenString, ownerId, null, null));
        logger.info("Store List: {}", storeDtos.stream()
                .map(StoreDto::getName)
                .toList());
        return storeDtos;
    }

    private AddressDto createAddress(String tokenString) {
        logger.info("Creating address for user");
        Random random = new Random();

        AddressDto newAddress = new AddressDto(
                "City-" + random.nextInt(100000),
                "Street-" + random.nextInt(100000),
                String.valueOf(random.nextInt(100000))
        );

        AddressDto createdAddress = runUseCaseWithReturn(new CreteAddressUseCase(tokenString, newAddress)).get(0);
        logger.info("Address ID: {}", createdAddress.getId());
        return createdAddress;
    }

    private StoreDto createStore(String tokenString, AddressDto address, UUID ownerId) {
        logger.info("Creating store for user");
        Random random = new Random();

        StoreDto newStore = new StoreDto(
                "Store-" + random.nextInt(100000),
                address.getId(),
                "",
                ownerId.toString()
        );

        logger.info("Store ID: {}", newStore.getId());
        return runUseCaseWithReturn(new CreteStoreUseCase(tokenString, newStore)).get(0);
    }

    private ProductDto getProduct(String category, String ownerId) {
        Random random = new Random();

        return new ProductDto(
                "Product-" + random.nextInt(100000),
                "Created by generator",
                random.nextInt(1000),
                category,
                ownerId
        );
    }

    private List<ProductDto> createProductBatch(String tokenString, ProductDto[] products) {
        logger.info("Creating product batch");
        List<ProductDto> productDtos = runUseCaseWithReturn(new CreateProductBatchUseCase(tokenString, products));

        logger.info("Products IDs: {}", productDtos.stream().map(ProductDto::getId).toList());
        return productDtos;
    }

    private List<ProductCategoryDto> getProductCategories(String tokenString) {
        logger.info("Getting Product Categories");
        List<ProductCategoryDto> productCategoryDtos = runUseCaseWithReturn(new GetAllCategoriesUseCase(tokenString));
        logger.info("Product Category List: {}", productCategoryDtos
                .stream()
                .map(ProductCategoryDto::getName)
                .toList());
        return productCategoryDtos;
    }

    private List<ProductCategoryDto> createProductCategories(String tokenString, int amount) {
        logger.info("Creating product categories");

        List<ProductCategoryDto> categoryDtoList = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            categoryDtoList.add(
                    runUseCaseWithReturn(new CreateCategoryUseCase(tokenString,createProductCategory())).get(0)
            );
        }

        return categoryDtoList;
    }

    private List<StockDto> getStockList(String tokenString, Long storeId, Long productId) {
        logger.info("Getting Stock List for store {} and product {}", storeId, productId);
        List<StockDto> stockDtos = runUseCaseWithReturn(new GetStockUseCase(tokenString, storeId, productId));

        logger.info("Stock found: x{}", stockDtos.size());
        return stockDtos;
    }

    private ProductCategoryDto createProductCategory() {
        Random random = new Random();

        return new ProductCategoryDto(
                "Сгенерированная категория - " + random.nextInt(1000),
                "Сгенерировано генератором"
        );
    }

    private StockDto createStock(String tokenString, String storeName, Long productId, int amount) {
        logger.info("Creating stock for store {} and product {}", storeName, productId);
        Random random = new Random();

        StockDto stockDto = new StockDto(
                random.nextInt(amount),
                productId,
                null,
                storeName,
                null,
                false,
                null
        );

        StockDto stock = runUseCaseWithReturn(new CreateStockUseCase(tokenString, stockDto)).get(0);
        logger.info("Stock created: {}", stock.getId());
        return stockDto;
    }
}
