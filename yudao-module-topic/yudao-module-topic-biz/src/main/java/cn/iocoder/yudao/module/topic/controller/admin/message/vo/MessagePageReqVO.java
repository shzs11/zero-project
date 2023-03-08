package cn.iocoder.yudao.module.topic.controller.admin.message.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 错题分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MessagePageReqVO extends PageParam {

    @Schema(description = "错题号", example = "15054")
    private Long id;

    @Schema(description = "用户id", example = "31497")
    private Long userId;

    @Schema(description = "用户账号", example = "赵六")
    private String userName;

    @Schema(description = "错题所有标签")
    private String tags;

    @Schema(description = "科目编号", example = "22249")
    private Long subjectId;

    @Schema(description = "部门编号", example = "14885")
    private Long deptId;

    @Schema(description = "是否发布")
    private Byte isPublic;

    @Schema(description = "创建者")
    private String creator;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createDate;

    @Schema(description = "修改时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] updateDate;

}
