package cn.iocoder.yudao.module.topic.convert.message;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.topic.controller.admin.message.vo.MessageCreateReqVO;
import cn.iocoder.yudao.module.topic.controller.admin.message.vo.MessageExcelVO;
import cn.iocoder.yudao.module.topic.controller.admin.message.vo.MessageRespVO;
import cn.iocoder.yudao.module.topic.controller.admin.message.vo.MessageUpdateReqVO;
import cn.iocoder.yudao.module.topic.dal.dataobject.message.MessageDO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-09T00:10:24+0800",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 1.8.0_301 (Oracle Corporation)"
)
public class MessageConvertImpl implements MessageConvert {

    @Override
    public MessageDO convert(MessageCreateReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        MessageDO.MessageDOBuilder messageDO = MessageDO.builder();

        messageDO.id( bean.getId() );
        messageDO.name( bean.getName() );
        messageDO.description( bean.getDescription() );
        messageDO.originalAnswer( bean.getOriginalAnswer() );
        messageDO.correctAnswer( bean.getCorrectAnswer() );
        messageDO.tags( bean.getTags() );
        messageDO.subjectId( bean.getSubjectId() );
        messageDO.isPublic( bean.getIsPublic() );

        return messageDO.build();
    }

    @Override
    public MessageDO convert(MessageUpdateReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        MessageDO.MessageDOBuilder messageDO = MessageDO.builder();

        messageDO.id( bean.getId() );
        messageDO.name( bean.getName() );
        messageDO.description( bean.getDescription() );
        messageDO.originalAnswer( bean.getOriginalAnswer() );
        messageDO.correctAnswer( bean.getCorrectAnswer() );
        messageDO.tags( bean.getTags() );
        messageDO.subjectId( bean.getSubjectId() );
        messageDO.isPublic( bean.getIsPublic() );

        return messageDO.build();
    }

    @Override
    public MessageRespVO convert(MessageDO bean) {
        if ( bean == null ) {
            return null;
        }

        MessageRespVO messageRespVO = new MessageRespVO();

        messageRespVO.setName( bean.getName() );
        messageRespVO.setDescription( bean.getDescription() );
        messageRespVO.setOriginalAnswer( bean.getOriginalAnswer() );
        messageRespVO.setCorrectAnswer( bean.getCorrectAnswer() );
        messageRespVO.setTags( bean.getTags() );
        messageRespVO.setSubjectId( bean.getSubjectId() );
        messageRespVO.setIsPublic( bean.getIsPublic() );
        messageRespVO.setId( bean.getId() );
        messageRespVO.setUserName( bean.getUserName() );
        messageRespVO.setDeptId( bean.getDeptId() );
        messageRespVO.setCreateDate( bean.getCreateDate() );
        messageRespVO.setUpdateDate( bean.getUpdateDate() );

        return messageRespVO;
    }

    @Override
    public List<MessageRespVO> convertList(List<MessageDO> list) {
        if ( list == null ) {
            return null;
        }

        List<MessageRespVO> list1 = new ArrayList<MessageRespVO>( list.size() );
        for ( MessageDO messageDO : list ) {
            list1.add( convert( messageDO ) );
        }

        return list1;
    }

    @Override
    public PageResult<MessageRespVO> convertPage(PageResult<MessageDO> page) {
        if ( page == null ) {
            return null;
        }

        PageResult<MessageRespVO> pageResult = new PageResult<MessageRespVO>();

        pageResult.setList( convertList( page.getList() ) );
        pageResult.setTotal( page.getTotal() );

        return pageResult;
    }

    @Override
    public List<MessageExcelVO> convertList02(List<MessageDO> list) {
        if ( list == null ) {
            return null;
        }

        List<MessageExcelVO> list1 = new ArrayList<MessageExcelVO>( list.size() );
        for ( MessageDO messageDO : list ) {
            list1.add( messageDOToMessageExcelVO( messageDO ) );
        }

        return list1;
    }

    protected MessageExcelVO messageDOToMessageExcelVO(MessageDO messageDO) {
        if ( messageDO == null ) {
            return null;
        }

        MessageExcelVO messageExcelVO = new MessageExcelVO();

        messageExcelVO.setId( messageDO.getId() );
        messageExcelVO.setName( messageDO.getName() );
        messageExcelVO.setDescription( messageDO.getDescription() );
        messageExcelVO.setOriginalAnswer( messageDO.getOriginalAnswer() );
        messageExcelVO.setCorrectAnswer( messageDO.getCorrectAnswer() );
        messageExcelVO.setUserName( messageDO.getUserName() );
        messageExcelVO.setTags( messageDO.getTags() );
        messageExcelVO.setSubjectId( messageDO.getSubjectId() );
        messageExcelVO.setDeptId( messageDO.getDeptId() );
        messageExcelVO.setIsPublic( messageDO.getIsPublic() );
        messageExcelVO.setCreateDate( messageDO.getCreateDate() );
        messageExcelVO.setUpdateDate( messageDO.getUpdateDate() );

        return messageExcelVO;
    }
}
