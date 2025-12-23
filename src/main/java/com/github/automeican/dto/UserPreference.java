package com.github.automeican.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@lombok.Data
public class UserPreference {
    @Schema(description = "用户偏好")
    private List<String> likes;
    @Schema(description = "用户忌口")
    private List<String> restrictions;
    @Schema(description = "用户黑名单")
    private List<String> blacklist;
    @Schema(description = "用户名")
    private String accountName;
    @Schema(description = "预约日期")
    private String orderDate;
}
