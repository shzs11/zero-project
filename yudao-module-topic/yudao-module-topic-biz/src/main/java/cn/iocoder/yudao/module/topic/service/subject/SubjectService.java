package cn.iocoder.yudao.module.topic.service.subject;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.topic.controller.admin.subject.vo.*;
import cn.iocoder.yudao.module.topic.dal.dataobject.subject.SubjectDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 科目 Service 接口
 *
 * @author 芋道源码
 */
public interface SubjectService {

    /**
     * 创建科目
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSubject(@Valid SubjectCreateReqVO createReqVO);

    /**
     * 更新科目
     *
     * @param updateReqVO 更新信息
     */
    void updateSubject(@Valid SubjectUpdateReqVO updateReqVO);

    /**
     * 删除科目
     *
     * @param id 编号
     */
    void deleteSubject(Long id);

    /**
     * 获得科目
     *
     * @param id 编号
     * @return 科目
     */
    SubjectDO getSubject(Long id);

    /**
     * 获得科目列表
     *
     * @param ids 编号
     * @return 科目列表
     */
    List<SubjectDO> getSubjectList(Collection<Long> ids);

    /**
     * 获得科目分页
     *
     * @param pageReqVO 分页查询
     * @return 科目分页
     */
    PageResult<SubjectDO> getSubjectPage(SubjectPageReqVO pageReqVO);

    /**
     * 获得科目列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 科目列表
     */
    List<SubjectDO> getSubjectList(SubjectExportReqVO exportReqVO);

}
