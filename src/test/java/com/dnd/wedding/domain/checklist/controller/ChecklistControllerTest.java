package com.dnd.wedding.domain.checklist.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.wedding.docs.springrestdocs.AbstractRestDocsTests;
import com.dnd.wedding.domain.checklist.checklistitem.dto.ChecklistItemApiDto;
import com.dnd.wedding.domain.checklist.checklistitem.dto.ChecklistItemDto;
import com.dnd.wedding.domain.checklist.checklistitem.service.ChecklistItemService;
import com.dnd.wedding.domain.checklist.checklistsubitem.dto.ChecklistSubItemDto;
import com.dnd.wedding.domain.checklist.checklistsubitem.service.ChecklistSubItemService;
import com.dnd.wedding.domain.checklist.service.ChecklistService;
import com.dnd.wedding.domain.jwt.JwtTokenProvider;
import com.dnd.wedding.domain.member.Member;
import com.dnd.wedding.domain.member.Role;
import com.dnd.wedding.domain.member.service.MemberService;
import com.dnd.wedding.domain.oauth.OAuth2Provider;
import com.dnd.wedding.global.WithMockOAuth2User;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ChecklistController.class)
class ChecklistControllerTest extends AbstractRestDocsTests {

  static final String ACCESS_TOKEN_PREFIX = "Bearer ";

  @MockBean
  ChecklistService checklistService;
  @MockBean
  ChecklistItemService checklistItemService;
  @MockBean
  ChecklistSubItemService checklistSubItemService;
  @MockBean
  MemberService memberService;
  @MockBean
  JwtTokenProvider jwtTokenProvider;

  private final ChecklistItemDto checklistItemDto = ChecklistItemDto.builder()
      .id(1L)
      .title("title")
      .checkDate(LocalDate.now())
      .startTime(LocalTime.MIN)
      .endTime(LocalTime.MAX)
      .place("place")
      .memo("memo")
      .build();

  private final ChecklistSubItemDto checklistSubItemDto1 = ChecklistSubItemDto.builder()
      .id(1L)
      .contents("contents 1")
      .isChecked(false)
      .build();
  private final ChecklistSubItemDto checklistSubItemDto2 = ChecklistSubItemDto.builder()
      .id(2L)
      .contents("contents 2")
      .isChecked(false)
      .build();

  private final ChecklistItemApiDto checklistItemApiDto1 = ChecklistItemApiDto.builder()
      .checklistItem(checklistItemDto)
      .checklistSubItems(List.of(checklistSubItemDto1, checklistSubItemDto2))
      .build();

  @Test
  @WithMockOAuth2User
  @DisplayName("사용자 체크리스트 조회")
  void getChecklist() throws Exception {
    String url = "/api/v1/checklist";

    Member member = Member.builder()
        .id(1L)
        .name("test")
        .email("test@example.com")
        .profileImage("test.png")
        .role(Role.USER)
        .oauth2Provider(OAuth2Provider.GOOGLE)
        .build();

    // given
    given(memberService.findMember(anyLong())).willReturn(Optional.ofNullable(member));
    given(checklistService.findChecklist(anyLong())).willReturn(List.of(checklistItemApiDto1));

    // when
    ResultActions result = mockMvc.perform(get(url)
        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "ACCESS_TOKEN"));

    // then
    result.andExpect(status().isOk())
        .andDo(
            document("checklist/checklist",
                responseFields(
                    beneathPath("data").withSubsectionId("data"),
                    fieldWithPath("checklistItem.id").description("체크리스트 아이템 아이디").type(
                        JsonFieldType.NUMBER),
                    fieldWithPath("checklistItem.title").description("체크리스트 아이템 제목").type(
                        JsonFieldType.STRING),
                    fieldWithPath("checklistItem.checkDate").description(
                        "체크리스트 아이템 일정 날짜 (yyyy-mm-dd)").type(
                        JsonFieldType.STRING),
                    fieldWithPath("checklistItem.startTime").description(
                        "체크리스트 아이템 일정 시작 시간 (hh:mm:ss)").type(
                        JsonFieldType.STRING),
                    fieldWithPath("checklistItem.endTime").description(
                        "체크리스트 아이템 일정 종료 시간 (hh:mm:ss)").type(
                        JsonFieldType.STRING),
                    fieldWithPath("checklistItem.place").description("체크리스트 아이템 일정 장소").type(
                        JsonFieldType.STRING),
                    fieldWithPath("checklistItem.memo").description("체크리스트 아이템 메모").type(
                        JsonFieldType.STRING),
                    fieldWithPath("checklistSubItems[].id").description("체크리스트 서브 아이템 아이디")
                        .type(
                            JsonFieldType.NUMBER),
                    fieldWithPath("checklistSubItems[].contents").description("체크리스트 서브 아이템 내용")
                        .type(
                            JsonFieldType.STRING),
                    fieldWithPath("checklistSubItems[].isChecked").description(
                        "체크리스트 서브 아이템 체크 여부").type(
                        JsonFieldType.BOOLEAN)
                )
            ));
  }
}
