package cn.iocoder.yudao.module.topic.service.subject;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.topic.controller.admin.subject.vo.*;
import cn.iocoder.yudao.module.topic.dal.dataobject.subject.SubjectDO;
import cn.iocoder.yudao.module.topic.dal.mysql.subject.SubjectMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import javax.annotation.Resource;
import org.springframework.context.annotation.Import;
import java.util.*;
import java.time.LocalDateTime;

import static cn.hutool.core.util.RandomUtil.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.buildBetweenTime;
import static cn.iocoder.yudao.module.topic.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
* {@link SubjectServiceImpl} 的单元测试类
*
* @author 芋道源码
*/
@Import(SubjectServiceImpl.class)
public class SubjectServiceImplTest extends BaseDbUnitTest {

    @Resource
    private SubjectServiceImpl subjectService;

    @Resource
    private SubjectMapper subjectMapper;

    @Test
    public void testCreateSubject_success() {
        // 准备参数
        SubjectCreateReqVO reqVO = randomPojo(SubjectCreateReqVO.class);

        // 调用
        Long subjectId = subjectService.createSubject(reqVO);
        // 断言
        assertNotNull(subjectId);
        // 校验记录的属性是否正确
        SubjectDO subject = subjectMapper.selectById(subjectId);
        assertPojoEquals(reqVO, subject);
    }

    @Test
    public void testUpdateSubject_success() {
        // mock 数据
        SubjectDO dbSubject = randomPojo(SubjectDO.class);
        subjectMapper.insert(dbSubject);// @Sql: 先插入出一条存在的数据
        // 准备参数
        SubjectUpdateReqVO reqVO = randomPojo(SubjectUpdateReqVO.class, o -> {
            o.setId(dbSubject.getId()); // 设置更新的 ID
        });

        // 调用
        subjectService.updateSubject(reqVO);
        // 校验是否更新正确
        SubjectDO subject = subjectMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, subject);
    }

    @Test
    public void testUpdateSubject_notExists() {
        // 准备参数
        SubjectUpdateReqVO reqVO = randomPojo(SubjectUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> subjectService.updateSubject(reqVO), SUBJECT_NOT_EXISTS);
    }

    @Test
    public void testDeleteSubject_success() {
        // mock 数据
        SubjectDO dbSubject = randomPojo(SubjectDO.class);
        subjectMapper.insert(dbSubject);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbSubject.getId();

        // 调用
        subjectService.deleteSubject(id);
       // 校验数据不存在了
       assertNull(subjectMapper.selectById(id));
    }

    @Test
    public void testDeleteSubject_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> subjectService.deleteSubject(id), SUBJECT_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetSubjectPage() {
       // mock 数据
       SubjectDO dbSubject = randomPojo(SubjectDO.class, o -> { // 等会查询到
           o.setName(null);
           o.setDescription(null);
           o.setCreateTime(null);
       });
       subjectMapper.insert(dbSubject);
       // 测试 name 不匹配
       subjectMapper.insert(cloneIgnoreId(dbSubject, o -> o.setName(null)));
       // 测试 description 不匹配
       subjectMapper.insert(cloneIgnoreId(dbSubject, o -> o.setDescription(null)));
       // 测试 createTime 不匹配
       subjectMapper.insert(cloneIgnoreId(dbSubject, o -> o.setCreateTime(null)));
       // 准备参数
       SubjectPageReqVO reqVO = new SubjectPageReqVO();
       reqVO.setName(null);
       reqVO.setDescription(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<SubjectDO> pageResult = subjectService.getSubjectPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbSubject, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetSubjectList() {
       // mock 数据
       SubjectDO dbSubject = randomPojo(SubjectDO.class, o -> { // 等会查询到
           o.setName(null);
           o.setDescription(null);
           o.setCreateTime(null);
       });
       subjectMapper.insert(dbSubject);
       // 测试 name 不匹配
       subjectMapper.insert(cloneIgnoreId(dbSubject, o -> o.setName(null)));
       // 测试 description 不匹配
       subjectMapper.insert(cloneIgnoreId(dbSubject, o -> o.setDescription(null)));
       // 测试 createTime 不匹配
       subjectMapper.insert(cloneIgnoreId(dbSubject, o -> o.setCreateTime(null)));
       // 准备参数
       SubjectExportReqVO reqVO = new SubjectExportReqVO();
       reqVO.setName(null);
       reqVO.setDescription(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       List<SubjectDO> list = subjectService.getSubjectList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbSubject, list.get(0));
    }

}
