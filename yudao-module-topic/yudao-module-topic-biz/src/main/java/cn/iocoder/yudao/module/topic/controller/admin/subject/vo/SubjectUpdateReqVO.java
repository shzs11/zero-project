package cn.iocoder.yudao.module.topic.controller.admin.subject.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 科目更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SubjectUpdateReqVO extends SubjectBaseVO {

    @Schema(description = "科目编号", required = true, example = "18482")
    @NotNull(message = "科目编号不能为空")
    private Long id;

}
