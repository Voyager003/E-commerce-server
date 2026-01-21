package ex.ecommerceserver.domain.member.application.dto;

import jakarta.validation.constraints.Size;

public record MemberUpdateRequest(
        @Size(min = 1, max = 50, message = "이름은 1자 이상 50자 이하여야 합니다")
        String name
) {
}
