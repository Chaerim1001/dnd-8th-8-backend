package com.dnd.weddingmap.domain.wedding.controller;

import com.dnd.weddingmap.domain.oauth.CustomUserDetails;
import com.dnd.weddingmap.domain.wedding.dto.TotalBudgetDto;
import com.dnd.weddingmap.domain.wedding.dto.WeddingDayDto;
import com.dnd.weddingmap.domain.wedding.service.WeddingService;
import com.dnd.weddingmap.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wedding")
public class WeddingController {

  private final WeddingService weddingService;

  @PostMapping
  public ResponseEntity<SuccessResponse> registerWedding(
      @AuthenticationPrincipal CustomUserDetails user,
      @RequestBody @Valid WeddingDayDto weddingDayDto) {
    weddingService.registerWedding(user.getId(), weddingDayDto);
    return ResponseEntity.ok().body(SuccessResponse.builder().message("결혼정보 등록 성공").build());
  }

  @GetMapping("/day")
  public ResponseEntity<SuccessResponse> getWeddingDay(
      @AuthenticationPrincipal CustomUserDetails user) {
    WeddingDayDto weddingDayDto = weddingService.getWeddingDay(user.getId());
    return ResponseEntity.ok()
        .body(SuccessResponse.builder().message("결혼일 조회 성공").data(weddingDayDto).build());
  }

  @PutMapping("/day")
  public ResponseEntity<SuccessResponse> modifyWeddingDay(
      @AuthenticationPrincipal CustomUserDetails user,
      @RequestBody @Valid WeddingDayDto weddingDayDto) {
    weddingService.modifyWeddingDay(user.getId(), weddingDayDto);
    return ResponseEntity.ok().body(SuccessResponse.builder().message("결혼일 수정 성공").build());
  }

  @GetMapping("/budget")
  public ResponseEntity<SuccessResponse> getTotalBudget(
      @AuthenticationPrincipal CustomUserDetails user) {
    TotalBudgetDto totalBudgetDto = weddingService.getTotalBudget(user.getId());
    return ResponseEntity.ok()
        .body(SuccessResponse.builder().message("총 예산 조회 성공").data(totalBudgetDto).build());
  }

  @PutMapping("/budget")
  public ResponseEntity<SuccessResponse> modifyTotalBudget(
      @AuthenticationPrincipal CustomUserDetails user,
      @RequestBody @Valid TotalBudgetDto totalBudgetDto) {
    weddingService.modifyTotalBudget(user.getId(), totalBudgetDto);
    return ResponseEntity.ok().body(SuccessResponse.builder().message("총 예산 수정 성공").build());
  }
}
