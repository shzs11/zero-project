package cn.iocoder.yudao.module.topic.service.message;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.topic.controller.admin.message.vo.*;
import cn.iocoder.yudao.module.topic.dal.dataobject.message.MessageDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.topic.convert.message.MessageConvert;
import cn.iocoder.yudao.module.topic.dal.mysql.message.MessageMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.topic.enums.ErrorCodeConstants.*;

/**
 * 错题 Service 实现类
 *
 * @author zero
 */
@Service
@Validated
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageMapper messageMapper;

    @Override
    public Long createMessage(MessageCreateReqVO createReqVO) {
        // 插入
        MessageDO message = MessageConvert.INSTANCE.convert(createReqVO);
        messageMapper.insert(message);
        // 返回
        return message.getId();
    }

    @Override
    public void updateMessage(MessageUpdateReqVO updateReqVO) {
        // 校验存在
        validateMessageExists(updateReqVO.getId());
        // 更新
        MessageDO updateObj = MessageConvert.INSTANCE.convert(updateReqVO);
        messageMapper.updateById(updateObj);
    }

    @Override
    public void deleteMessage(Long id) {
        // 校验存在
        validateMessageExists(id);
        // 删除
        messageMapper.deleteById(id);
    }

    private void validateMessageExists(Long id) {
        if (messageMapper.selectById(id) == null) {
            throw exception(MESSAGE_NOT_EXISTS);
        }
    }

    @Override
    public MessageDO getMessage(Long id) {
        return messageMapper.selectById(id);
    }

    @Override
    public List<MessageDO> getMessageList(Collection<Long> ids) {
        return messageMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<MessageDO> getMessagePage(MessagePageReqVO pageReqVO) {
        return messageMapper.selectPage(pageReqVO);
    }

    @Override
    public List<MessageDO> getMessageList(MessageExportReqVO exportReqVO) {
        return messageMapper.selectList(exportReqVO);
    }

}
