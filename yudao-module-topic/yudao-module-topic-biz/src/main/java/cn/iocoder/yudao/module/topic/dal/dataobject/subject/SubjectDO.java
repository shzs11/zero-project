package cn.iocoder.yudao.module.topic.dal.dataobject.subject;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 科目 DO
 *
 * @author 芋道源码
 */
@TableName("topic_subject")
@KeySequence("topic_subject_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDO extends BaseDO {

    /**
     * 科目编号
     */
    @TableId
    private Long id;
    /**
     * 科目名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;

}
