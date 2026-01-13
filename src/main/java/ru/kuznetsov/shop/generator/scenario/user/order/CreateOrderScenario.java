package ru.kuznetsov.shop.generator.scenario.user.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.kuznetsov.shop.generator.scenario.AbstractScenario;
import ru.kuznetsov.shop.generator.service.GateUseCaseService;
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

import static ru.kuznetsov.shop.generator.common.ConstValues.USER_LOGIN;
import static ru.kuznetsov.shop.generator.common.ConstValues.USER_PASSWORD;
import static ru.kuznetsov.shop.represent.enums.DeliveryType.ADDRESS;
import static ru.kuznetsov.shop.represent.enums.PaymentType.CASH;

@Component
public class CreateOrderScenario extends AbstractScenario {

    Logger logger = LoggerFactory.getLogger(CreateOrderScenario.class);

    protected CreateOrderScenario(GateUseCaseService gateUseCaseService) {
        super(gateUseCaseService);
    }

    @Override
    public void run() {
        logger.info("Start CreateOrderScenario");

        TokenDto token = getToken(USER_LOGIN, USER_PASSWORD);
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
        logger.info("Order saved: {}", savedOrder.getId());

        logger.info("Finished CreateOrderScenario");
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
        List<StoreDto> storeDtos = runUseCaseWithReturn(new GetAllStoresUseCase(tokenString, null, null, null));
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
        int numberOfElements = productCardPage.getNumberOfElements();
        int productAmount;

        if (numberOfElements == 1) {
            productAmount = 1;
        } else {
            productAmount = random.nextInt(1, numberOfElements);
        }

        for (int i = 0; i < productAmount; i++) {
            ProductCardDto productCardDto = productCardPage.getContent().get(random.nextInt(productAmount));
            Integer totalAmount = productCardDto.getStock().values().stream().reduce(0, Integer::sum);

            BucketItemDto bucket = new BucketItemDto();
            bucket.setProductId(productCardDto.getId());
            int amountToAdd = random.nextInt(totalAmount / 5);
            bucket.setAmount(amountToAdd);

            bucketList.add(bucket);
            logger.info("Product {} x{} added to bucket", productCardDto.getId(), amountToAdd);
        }
    }

}
