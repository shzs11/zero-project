package cn.iocoder.yudao.module.topic.controller.admin.subject.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 科目 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class SubjectExcelVO {

    @ExcelProperty("科目编号")
    private Long id;

    @ExcelProperty("科目名称")
    private String name;

    @ExcelProperty("描述")
    private String description;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
