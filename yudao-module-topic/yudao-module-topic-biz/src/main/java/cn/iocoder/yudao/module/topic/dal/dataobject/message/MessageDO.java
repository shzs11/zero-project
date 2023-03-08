package cn.iocoder.yudao.module.topic.dal.dataobject.message;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 错题 DO
 *
 * @author zero
 */
@TableName("topic_message")
@KeySequence("topic_message_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDO extends BaseDO {

    /**
     * 错题号
     */
    @TableId
    private Long id;
    /**
     * 错题题目
     */
    private String name;
    /**
     * 题目描述
     */
    private String description;
    /**
     * 错题原答案
     */
    private String originalAnswer;
    /**
     * 错题正确答案
     */
    private String correctAnswer;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户账号
     */
    private String userName;
    /**
     * 错题所有标签
     */
    private String tags;
    /**
     * 科目编号
     */
    private Long subjectId;
    /**
     * 部门编号
     */
    private Long deptId;
    /**
     * 是否发布
     */
    private Byte isPublic;
    /**
     * 创建时间
     */
    private LocalDateTime createDate;
    /**
     * 修改时间
     */
    private LocalDateTime updateDate;

}
