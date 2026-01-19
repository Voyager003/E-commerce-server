package ex.ecommerceserver.domain.member.application;

import ex.ecommerceserver.domain.member.application.dto.MemberResponse;
import ex.ecommerceserver.domain.member.application.dto.MemberUpdateRequest;
import ex.ecommerceserver.domain.member.domain.Member;
import ex.ecommerceserver.domain.member.entity.MemberEntity;
import ex.ecommerceserver.domain.member.repository.MemberRepository;
import ex.ecommerceserver.global.exception.BusinessException;
import ex.ecommerceserver.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberApplication {

    private final MemberRepository memberRepository;

    public MemberResponse getMyInfo(Long memberId) {
        Member member = findMemberById(memberId);
        return MemberResponse.from(member);
    }

    @Transactional
    public MemberResponse updateMyInfo(Long memberId, MemberUpdateRequest request) {
        MemberEntity entity = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        Member member = entity.toDomain();
        member.updateProfile(request.name());
        entity.update(member);

        return MemberResponse.from(entity.toDomain());
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .map(MemberEntity::toDomain)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
