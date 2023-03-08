package cn.iocoder.yudao.module.topic.service.message;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.topic.controller.admin.message.vo.*;
import cn.iocoder.yudao.module.topic.dal.dataobject.message.MessageDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 错题 Service 接口
 *
 * @author zero
 */
public interface MessageService {

    /**
     * 创建错题
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createMessage(@Valid MessageCreateReqVO createReqVO);

    /**
     * 更新错题
     *
     * @param updateReqVO 更新信息
     */
    void updateMessage(@Valid MessageUpdateReqVO updateReqVO);

    /**
     * 删除错题
     *
     * @param id 编号
     */
    void deleteMessage(Long id);

    /**
     * 获得错题
     *
     * @param id 编号
     * @return 错题
     */
    MessageDO getMessage(Long id);

    /**
     * 获得错题列表
     *
     * @param ids 编号
     * @return 错题列表
     */
    List<MessageDO> getMessageList(Collection<Long> ids);

    /**
     * 获得错题分页
     *
     * @param pageReqVO 分页查询
     * @return 错题分页
     */
    PageResult<MessageDO> getMessagePage(MessagePageReqVO pageReqVO);

    /**
     * 获得错题列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 错题列表
     */
    List<MessageDO> getMessageList(MessageExportReqVO exportReqVO);

}
