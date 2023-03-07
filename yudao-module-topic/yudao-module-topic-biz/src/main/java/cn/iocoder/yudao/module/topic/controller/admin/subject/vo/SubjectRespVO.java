package cn.iocoder.yudao.module.topic.controller.admin.subject.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 科目 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SubjectRespVO extends SubjectBaseVO {

    @Schema(description = "科目编号", required = true, example = "18482")
    private Long id;

    @Schema(description = "创建时间", required = true)
    private LocalDateTime createTime;

}
