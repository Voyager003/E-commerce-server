package ex.ecommerceserver.domain.member.api;

import ex.ecommerceserver.domain.member.application.MemberApplication;
import ex.ecommerceserver.domain.member.application.dto.MemberResponse;
import ex.ecommerceserver.domain.member.application.dto.MemberUpdateRequest;
import ex.ecommerceserver.global.security.jwt.LoginMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberApi {

    private final MemberApplication memberApplication;

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMyInfo(@LoginMember Long memberId) {
        MemberResponse response = memberApplication.getMyInfo(memberId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/me")
    public ResponseEntity<MemberResponse> updateMyInfo(
            @LoginMember Long memberId,
            @Valid @RequestBody MemberUpdateRequest request
    ) {
        MemberResponse response = memberApplication.updateMyInfo(memberId, request);
        return ResponseEntity.ok(response);
    }
}
