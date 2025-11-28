package ru.kuznetsov.shop.generator.scenario.entity.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.kuznetsov.shop.generator.scenario.AbstractScenario;
import ru.kuznetsov.shop.generator.service.UseCaseService;
import ru.kuznetsov.shop.generator.usecase.auth.GetTokenUseCase;
import ru.kuznetsov.shop.generator.usecase.auth.GetUserInfoUseCase;
import ru.kuznetsov.shop.generator.usecase.entity.order.SaveOrderUseCase;
import ru.kuznetsov.shop.generator.usecase.entity.product.GetProductCardsUseCasePageable;
import ru.kuznetsov.shop.generator.usecase.entity.product_category.GetAllCategoriesUseCase;
import ru.kuznetsov.shop.generator.usecase.entity.store.GetAllStoresUseCase;
import ru.kuznetsov.shop.represent.dto.ProductCardDto;
import ru.kuznetsov.shop.represent.dto.ProductCategoryDto;
import ru.kuznetsov.shop.represent.dto.StoreDto;
import ru.kuznetsov.shop.represent.dto.auth.TokenDto;
import ru.kuznetsov.shop.represent.dto.auth.UserDto;
import ru.kuznetsov.shop.represent.dto.order.BucketItemDto;
import ru.kuznetsov.shop.represent.dto.order.OrderDto;
import ru.kuznetsov.shop.represent.dto.util.ProductCardPage;

import java.util.*;

import static ru.kuznetsov.shop.generator.common.ConstValues.ADMIN_LOGIN;
import static ru.kuznetsov.shop.generator.common.ConstValues.ADMIN_PASSWORD;
import static ru.kuznetsov.shop.represent.enums.DeliveryType.ADDRESS;
import static ru.kuznetsov.shop.represent.enums.PaymentType.CASH;

@Component
public class CreateOrderScenario extends AbstractScenario {

    Logger logger = LoggerFactory.getLogger(CreateOrderScenario.class);

    protected CreateOrderScenario(UseCaseService useCaseService) {
        super(useCaseService);
    }

    private TokenDto getToken() {
        logger.info("Getting token");
        TokenDto token = runUseCaseWithReturn(new GetTokenUseCase(ADMIN_LOGIN, ADMIN_PASSWORD)).get(0);
        logger.info("Token String: {}", token.getToken());
        return token;
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

    private List<StoreDto> getStoreList(String tokenString) {
        logger.info("Getting store list");
        List<StoreDto> storeDtos = runUseCaseWithReturn(new GetAllStoresUseCase(tokenString));
        logger.info("Store List: {}", storeDtos.stream()
                .map(StoreDto::getName)
                .toList());
        return storeDtos;
    }

    private ProductCardPage getProductCards(String tokenString, String ownerId, Long categoryId, Integer pageNum, Integer pageSize, String sortBy, String sortDirection) {
        logger.info("Getting Product Card Details for owner ID: {}, category ID: {}, page: {}, page size: {} no sort",
                ownerId, categoryId, pageNum, pageSize);
        ProductCardPage productCardPage = runUseCaseWithReturn(
                new GetProductCardsUseCasePageable(
                        tokenString,
                        UUID.fromString(ownerId),
                        categoryId,
                        pageNum,
                        pageSize,
                        sortBy,
                        sortDirection
                )
        ).get(0);
        logger.info("Cards retrieved: {}", productCardPage.getContent().size());
        return productCardPage;
    }

    private void addRandomProductsToBucket(Set<BucketItemDto> bucketList, ProductCardPage productCardPage) {
        logger.info("Adding random products to bucket");

        Random random = new Random();
        int productAmount = random.nextInt(productCardPage.getNumberOfElements());

        for (int i = 0; i < productAmount; i++) {
            ProductCardDto productCardDto = productCardPage.getContent().get(random.nextInt(productAmount));
            Integer totalAmount = productCardDto.getStock().values().stream().reduce(0, Integer::sum);

            BucketItemDto bucket = new BucketItemDto();
            bucket.setProductId(productCardDto.getId());
            int amountToAdd = random.nextInt(totalAmount / 5);
            bucket.setAmount(amountToAdd);

            bucketList.add(bucket);
            logger.info("Product {} {} added to bucket", productCardDto.getId(), amountToAdd);
        }
    }

    @Override
    public void run() {
        logger.info("Start CreateOrderScenario");

        TokenDto token = getToken();
        String tokenString = token.getToken();

        List<ProductCategoryDto> productCategoryDtos = getProductCategories(tokenString);
        List<StoreDto> storeDtos = getStoreList(tokenString);

        String ownerId = storeDtos.get(0).getOwnerId();
        Long categoryId = productCategoryDtos.get(0).getId();

        int page = 0;
        int pageSize = 5;

        ProductCardPage productCardPage = getProductCards(
                tokenString,
                ownerId,
                categoryId,
                page,
                pageSize,
                null,
                null
        );

        Set<BucketItemDto> bucketList = new HashSet<>();

        addRandomProductsToBucket(bucketList, productCardPage);

        logger.info("Getting user");
        UserDto userDto = runUseCaseWithReturn(new GetUserInfoUseCase(tokenString)).get(0);
        logger.info("UserInfo: {}", userDto);

        logger.info("Creating order");

        OrderDto orderDto = new OrderDto();
        orderDto.setDeliveryType(ADDRESS);
        orderDto.setPaymentType(CASH);
        orderDto.setCustomerDeliveryAddress("Test delivery address");
        orderDto.setCustomerId(userDto.getId().toString());
        orderDto.setBucket(bucketList);

        OrderDto savedOrder = runUseCaseWithReturn(new SaveOrderUseCase(tokenString, orderDto)).get(0);
        logger.info("Order saved: {}", savedOrder);

        logger.info("Finished CreateOrderScenario");
    }
}
