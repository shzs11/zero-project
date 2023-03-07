package cn.iocoder.yudao.module.topic.service.subject;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.topic.controller.admin.subject.vo.*;
import cn.iocoder.yudao.module.topic.dal.dataobject.subject.SubjectDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.topic.convert.subject.SubjectConvert;
import cn.iocoder.yudao.module.topic.dal.mysql.subject.SubjectMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.topic.enums.ErrorCodeConstants.*;

/**
 * 科目 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class SubjectServiceImpl implements SubjectService {

    @Resource
    private SubjectMapper subjectMapper;

    @Override
    public Long createSubject(SubjectCreateReqVO createReqVO) {
        // 插入
        SubjectDO subject = SubjectConvert.INSTANCE.convert(createReqVO);
        subjectMapper.insert(subject);
        // 返回
        return subject.getId();
    }

    @Override
    public void updateSubject(SubjectUpdateReqVO updateReqVO) {
        // 校验存在
        validateSubjectExists(updateReqVO.getId());
        // 更新
        SubjectDO updateObj = SubjectConvert.INSTANCE.convert(updateReqVO);
        subjectMapper.updateById(updateObj);
    }

    @Override
    public void deleteSubject(Long id) {
        // 校验存在
        validateSubjectExists(id);
        // 删除
        subjectMapper.deleteById(id);
    }

    private void validateSubjectExists(Long id) {
        if (subjectMapper.selectById(id) == null) {
            throw exception(SUBJECT_NOT_EXISTS);
        }
    }

    @Override
    public SubjectDO getSubject(Long id) {
        return subjectMapper.selectById(id);
    }

    @Override
    public List<SubjectDO> getSubjectList(Collection<Long> ids) {
        return subjectMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<SubjectDO> getSubjectPage(SubjectPageReqVO pageReqVO) {
        return subjectMapper.selectPage(pageReqVO);
    }

    @Override
    public List<SubjectDO> getSubjectList(SubjectExportReqVO exportReqVO) {
        return subjectMapper.selectList(exportReqVO);
    }

}
