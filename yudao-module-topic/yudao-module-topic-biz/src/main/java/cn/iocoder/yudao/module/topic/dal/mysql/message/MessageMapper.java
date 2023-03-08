package cn.iocoder.yudao.module.topic.dal.mysql.message;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.topic.dal.dataobject.message.MessageDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.topic.controller.admin.message.vo.*;

/**
 * 错题 Mapper
 *
 * @author zero
 */
@Mapper
public interface MessageMapper extends BaseMapperX<MessageDO> {

    default PageResult<MessageDO> selectPage(MessagePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MessageDO>()
                .eqIfPresent(MessageDO::getId, reqVO.getId())
                .eqIfPresent(MessageDO::getUserId, reqVO.getUserId())
                .likeIfPresent(MessageDO::getUserName, reqVO.getUserName())
                .eqIfPresent(MessageDO::getTags, reqVO.getTags())
                .eqIfPresent(MessageDO::getSubjectId, reqVO.getSubjectId())
                .eqIfPresent(MessageDO::getDeptId, reqVO.getDeptId())
                .eqIfPresent(MessageDO::getIsPublic, reqVO.getIsPublic())
                .eqIfPresent(MessageDO::getCreator, reqVO.getCreator())
                .betweenIfPresent(MessageDO::getCreateDate, reqVO.getCreateDate())
                .betweenIfPresent(MessageDO::getUpdateDate, reqVO.getUpdateDate())
                .orderByDesc(MessageDO::getId));
    }

    default List<MessageDO> selectList(MessageExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<MessageDO>()
                .eqIfPresent(MessageDO::getId, reqVO.getId())
                .eqIfPresent(MessageDO::getUserId, reqVO.getUserId())
                .likeIfPresent(MessageDO::getUserName, reqVO.getUserName())
                .eqIfPresent(MessageDO::getTags, reqVO.getTags())
                .eqIfPresent(MessageDO::getSubjectId, reqVO.getSubjectId())
                .eqIfPresent(MessageDO::getDeptId, reqVO.getDeptId())
                .eqIfPresent(MessageDO::getIsPublic, reqVO.getIsPublic())
                .eqIfPresent(MessageDO::getCreator, reqVO.getCreator())
                .betweenIfPresent(MessageDO::getCreateDate, reqVO.getCreateDate())
                .betweenIfPresent(MessageDO::getUpdateDate, reqVO.getUpdateDate())
                .orderByDesc(MessageDO::getId));
    }

}
