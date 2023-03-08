package cn.iocoder.yudao.module.topic.controller.admin.message.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 错题 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MessageRespVO extends MessageBaseVO {

    @Schema(description = "错题号", required = true, example = "15054")
    private Long id;

    @Schema(description = "用户账号", required = true, example = "赵六")
    private String userName;

    @Schema(description = "部门编号", required = true, example = "14885")
    private Long deptId;

    @Schema(description = "创建时间", required = true)
    private LocalDateTime createDate;

    @Schema(description = "修改时间", required = true)
    private LocalDateTime updateDate;

}
