package ex.ecommerceserver.domain.product.domain;

import ex.ecommerceserver.global.exception.BusinessException;
import ex.ecommerceserver.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @Nested
    @DisplayName("Product 생성")
    class Create {

        @Test
        @DisplayName("유효한 정보로 Product 객체를 생성할 수 있다")
        void create_ValidInfo_Success() {
            // given
            String name = "테스트 상품";
            String description = "테스트 설명";
            int price = 10000;
            int stockQuantity = 100;
            Category category = Category.ELECTRONICS;

            // when
            Product product = Product.create(name, description, price, stockQuantity, category);

            // then
            assertThat(product.getName()).isEqualTo(name);
            assertThat(product.getDescription()).isEqualTo(description);
            assertThat(product.getPrice().value()).isEqualTo(price);
            assertThat(product.getStock().quantity()).isEqualTo(stockQuantity);
            assertThat(product.getCategory()).isEqualTo(category);
            assertThat(product.getStatus()).isEqualTo(ProductStatus.ACTIVE);
        }

        @Test
        @DisplayName("상품명이 null이면 예외가 발생한다")
        void create_NullName_ThrowsException() {
            // when & then
            assertThatThrownBy(() -> Product.create(null, "설명", 10000, 100, Category.ELECTRONICS))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_PRODUCT_NAME);
        }

        @Test
        @DisplayName("상품명이 빈 문자열이면 예외가 발생한다")
        void create_BlankName_ThrowsException() {
            // when & then
            assertThatThrownBy(() -> Product.create("   ", "설명", 10000, 100, Category.ELECTRONICS))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_PRODUCT_NAME);
        }
    }

    @Nested
    @DisplayName("재고 관리")
    class StockManagement {

        @Test
        @DisplayName("재고를 감소시킬 수 있다")
        void decreaseStock_ValidAmount_Success() {
            // given
            Product product = Product.create("상품", "설명", 10000, 100, Category.ELECTRONICS);

            // when
            product.decreaseStock(30);

            // then
            assertThat(product.getStock().quantity()).isEqualTo(70);
            assertThat(product.getStatus()).isEqualTo(ProductStatus.ACTIVE);
        }

        @Test
        @DisplayName("재고가 0이 되면 OUT_OF_STOCK 상태로 변경된다")
        void decreaseStock_ZeroStock_StatusChangesToOutOfStock() {
            // given
            Product product = Product.create("상품", "설명", 10000, 50, Category.ELECTRONICS);

            // when
            product.decreaseStock(50);

            // then
            assertThat(product.getStock().quantity()).isZero();
            assertThat(product.getStatus()).isEqualTo(ProductStatus.OUT_OF_STOCK);
        }

        @Test
        @DisplayName("재고를 증가시킬 수 있다")
        void increaseStock_ValidAmount_Success() {
            // given
            Product product = Product.create("상품", "설명", 10000, 100, Category.ELECTRONICS);

            // when
            product.increaseStock(50);

            // then
            assertThat(product.getStock().quantity()).isEqualTo(150);
        }

        @Test
        @DisplayName("품절 상태에서 재입고하면 ACTIVE 상태로 변경된다")
        void increaseStock_OutOfStock_StatusChangesToActive() {
            // given
            Product product = Product.create("상품", "설명", 10000, 10, Category.ELECTRONICS);
            product.decreaseStock(10);
            assertThat(product.getStatus()).isEqualTo(ProductStatus.OUT_OF_STOCK);

            // when
            product.increaseStock(20);

            // then
            assertThat(product.getStock().quantity()).isEqualTo(20);
            assertThat(product.getStatus()).isEqualTo(ProductStatus.ACTIVE);
        }
    }

    @Nested
    @DisplayName("상태 관리")
    class StatusManagement {

        @Test
        @DisplayName("판매 중단된 상품은 활성화할 수 없다")
        void activate_DiscontinuedProduct_ThrowsException() {
            // given
            Product product = Product.create("상품", "설명", 10000, 100, Category.ELECTRONICS);
            product.discontinue();

            // when & then
            assertThatThrownBy(product::activate)
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.CANNOT_ACTIVATE_DISCONTINUED_PRODUCT);
        }

        @Test
        @DisplayName("비활성 상품을 활성화할 수 있다")
        void activate_InactiveProduct_Success() {
            // given
            Product product = Product.create("상품", "설명", 10000, 100, Category.ELECTRONICS);
            product.deactivate();
            assertThat(product.getStatus()).isEqualTo(ProductStatus.INACTIVE);

            // when
            product.activate();

            // then
            assertThat(product.getStatus()).isEqualTo(ProductStatus.ACTIVE);
        }
    }

    @Nested
    @DisplayName("상품 수정")
    class Update {

        @Test
        @DisplayName("상품 정보를 수정할 수 있다")
        void update_ValidInfo_Success() {
            // given
            Product product = Product.create("상품", "설명", 10000, 100, Category.ELECTRONICS);
            String newName = "수정된 상품";
            String newDescription = "수정된 설명";
            int newPrice = 20000;
            Category newCategory = Category.CLOTHING;

            // when
            product.update(newName, newDescription, newPrice, newCategory);

            // then
            assertThat(product.getName()).isEqualTo(newName);
            assertThat(product.getDescription()).isEqualTo(newDescription);
            assertThat(product.getPrice().value()).isEqualTo(newPrice);
            assertThat(product.getCategory()).isEqualTo(newCategory);
        }
    }
}
