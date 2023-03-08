package cn.iocoder.yudao.module.topic.controller.admin.message.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
* 错题 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class MessageBaseVO {

    @Schema(description = "错题题目", required = true, example = "张三")
    @NotNull(message = "错题题目不能为空")
    private String name;

    private Long id;

    @Schema(description = "题目描述", required = true, example = "随便")
    @NotNull(message = "题目描述不能为空")
    private String description;

    @Schema(description = "错题原答案", required = true)
    @NotNull(message = "错题原答案不能为空")
    private String originalAnswer;

    @Schema(description = "错题正确答案", required = true)
    @NotNull(message = "错题正确答案不能为空")
    private String correctAnswer;

    @Schema(description = "错题所有标签", required = true)
    @NotNull(message = "错题所有标签不能为空")
    private String tags;

    @Schema(description = "科目编号", required = true, example = "22249")
    @NotNull(message = "科目编号不能为空")
    private Long subjectId;

    @Schema(description = "是否发布", required = true)
    @NotNull(message = "是否发布不能为空")
    private Byte isPublic;

}
