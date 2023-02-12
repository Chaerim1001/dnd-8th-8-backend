package com.dnd.wedding.domain.checklist.checklistsubitem.dto;

import com.dnd.wedding.domain.checklist.checklistsubitem.ChecklistSubItem;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChecklistSubItemDto {

  private Long id;
  @NotBlank(message = "contents can't be null")
  private String contents;
  private Boolean isChecked;

  public ChecklistSubItemDto(ChecklistSubItem checklistSubItem) {
    this.id = checklistSubItem.getId();
    this.contents = checklistSubItem.getContents();
    this.isChecked = checklistSubItem.getIsChecked();
  }
}
