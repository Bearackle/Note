package com.dinhhuan.note.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PmsBlockParam {
    Long pageId;
    String type;
    String content;
    String idBlock;
    Integer blockOrder;
}
