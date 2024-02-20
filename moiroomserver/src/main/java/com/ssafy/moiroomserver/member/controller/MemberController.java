package com.ssafy.moiroomserver.member.controller;

import static com.ssafy.moiroomserver.global.constants.SuccessCode.*;

import com.ssafy.moiroomserver.global.constants.SuccessCode;
import com.ssafy.moiroomserver.global.dto.ApiResponse;
import com.ssafy.moiroomserver.member.dto.*;
import com.ssafy.moiroomserver.member.dto.MemberInfo.AddMemberRequest;
import com.ssafy.moiroomserver.member.dto.MemberInfo.MemberInfoRes;
import com.ssafy.moiroomserver.member.dto.MemberInfo.MemberTokenRequest;
import com.ssafy.moiroomserver.member.entity.Member;
import com.ssafy.moiroomserver.member.service.CharacteristicService;
import com.ssafy.moiroomserver.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final CharacteristicService characteristicService;

    /**
     * 회원 정보 수정
     *
     * @param infoModifyMemberRequest 수정 시 입력할 데이터
     */
    @PatchMapping
    public ApiResponse<?> modifyMemberInfo(HttpServletRequest request, @ModelAttribute MemberInfo.ModifyMemberRequest infoModifyMemberRequest) {
        memberService.modifyMemberInfo(request, infoModifyMemberRequest);

        return ApiResponse.success(MODIFY_MEMBER_INFO);
    }

    /**
     * 카카오 회원 로그인
     *
     * @param accountDto
     * @return
     */
    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody AddMemberRequest accountDto) {
        memberService.login(accountDto);
        return ApiResponse.success(SuccessCode.LOGIN_MEMBER);
    }

    /**
     * 카카오 회원 토큰 업데이트
     * 재로그인 시 카카오 토큰 정보 업데이트
     *
     * @return
     */
    @PatchMapping("/{memberId}")
    public ApiResponse<?> modifyMemberToken(@PathVariable Long memberId,
                                            @RequestBody MemberTokenRequest tokenDto) {
        memberService.modifyMemberToken(memberId, tokenDto);
        return ApiResponse.success(SuccessCode.MODIFY_MEMBER_TOKEN);
    }

    /**
     * 회원 정보 조회 -> 단순히 pk를 가져와서 조회하는 api
     *
     * @param memberId
     * @return
     */
    @GetMapping("/{memberId}")
    public ApiResponse<?> getMemberById(@PathVariable Long memberId) {
        Member member = memberService.getMemberById(memberId);
        return ApiResponse.success(SuccessCode.GET_MEMBER_BY_ID, member);
    }

    /**
     * 로그아웃 api
     *
     * @param socialId
     * @param provider
     * @return
     */
    @PostMapping("/logout")
    public ApiResponse<?> logout(@RequestParam Long socialId, @RequestParam String provider) {
        memberService.logout(socialId, provider);
        return ApiResponse.success(SuccessCode.LOGOUT_MEMBER);
    }
    
    /**
     * 특징 및 관심사 추가 및 수정
     *
     * @param request
     * @param infoAddModifyReq 추가 및 수정 시 입력할 데이터
     */
    @PostMapping("/characteristic")
    public ApiResponse<?> addCharacteristic(HttpServletRequest request, @RequestBody CharacteristicAndInterestInfo.AddCharactAndInterInfoRequest infoAddModifyReq) {
        characteristicService.addCharacteristic(request, infoAddModifyReq);

        return ApiResponse.success(ADD_ALL_CHARACTER_INFO);
    }

    /**
     * 회원 상세 정보 조회
     * @param request
     * @return
     */
    @GetMapping
    public ApiResponse<?> getMemberInfoDetail(HttpServletRequest request) {
        MemberInfoRes dto = memberService.getMemberInfoDetail(request);
        return ApiResponse.success(SuccessCode.GET_MEMBER_BY_ID, dto);
    }
}