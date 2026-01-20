package ex.ecommerceserver.domain.product.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    ELECTRONICS("전자기기"),
    CLOTHING("의류"),
    FOOD("식품"),
    BOOKS("도서"),
    HOME("홈/리빙"),
    BEAUTY("뷰티"),
    SPORTS("스포츠"),
    ETC("기타");

    private final String displayName;
}
