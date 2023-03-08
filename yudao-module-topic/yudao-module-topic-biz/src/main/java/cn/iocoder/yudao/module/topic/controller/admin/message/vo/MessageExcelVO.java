package cn.iocoder.yudao.module.topic.controller.admin.message.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 错题 Excel VO
 *
 * @author zero
 */
@Data
public class MessageExcelVO {

    @ExcelProperty("错题号")
    private Long id;

    @ExcelProperty("错题题目")
    private String name;

    @ExcelProperty("题目描述")
    private String description;

    @ExcelProperty("错题原答案")
    private String originalAnswer;

    @ExcelProperty("错题正确答案")
    private String correctAnswer;

    @ExcelProperty("用户账号")
    private String userName;

    @ExcelProperty("错题所有标签")
    private String tags;

    @ExcelProperty("科目编号")
    private Long subjectId;

    @ExcelProperty("部门编号")
    private Long deptId;

    @ExcelProperty("是否发布")
    private Byte isPublic;

    @ExcelProperty("创建时间")
    private LocalDateTime createDate;

    @ExcelProperty("修改时间")
    private LocalDateTime updateDate;

}
