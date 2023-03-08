package cn.iocoder.yudao.module.topic.convert.message;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.topic.controller.admin.message.vo.*;
import cn.iocoder.yudao.module.topic.dal.dataobject.message.MessageDO;

/**
 * 错题 Convert
 *
 * @author zero
 */
@Mapper
public interface MessageConvert {

    MessageConvert INSTANCE = Mappers.getMapper(MessageConvert.class);

    MessageDO convert(MessageCreateReqVO bean);

    MessageDO convert(MessageUpdateReqVO bean);

    MessageRespVO convert(MessageDO bean);

    List<MessageRespVO> convertList(List<MessageDO> list);

    PageResult<MessageRespVO> convertPage(PageResult<MessageDO> page);

    List<MessageExcelVO> convertList02(List<MessageDO> list);

}
