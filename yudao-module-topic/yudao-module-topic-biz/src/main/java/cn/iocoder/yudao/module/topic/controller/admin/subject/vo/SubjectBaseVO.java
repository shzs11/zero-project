package cn.iocoder.yudao.module.topic.controller.admin.subject.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
* 科目 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class SubjectBaseVO {

    @Schema(description = "科目名称", required = true, example = "语文")
    @NotNull(message = "科目名称不能为空")
    private String name;

    @Schema(description = "描述", example = "你猜")
    private String description;

}
