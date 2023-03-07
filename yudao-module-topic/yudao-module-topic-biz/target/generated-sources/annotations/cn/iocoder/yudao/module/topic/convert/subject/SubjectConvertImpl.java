package cn.iocoder.yudao.module.topic.convert.subject;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.topic.controller.admin.subject.vo.SubjectCreateReqVO;
import cn.iocoder.yudao.module.topic.controller.admin.subject.vo.SubjectExcelVO;
import cn.iocoder.yudao.module.topic.controller.admin.subject.vo.SubjectRespVO;
import cn.iocoder.yudao.module.topic.controller.admin.subject.vo.SubjectUpdateReqVO;
import cn.iocoder.yudao.module.topic.dal.dataobject.subject.SubjectDO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-07T14:30:47+0800",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 1.8.0_301 (Oracle Corporation)"
)
public class SubjectConvertImpl implements SubjectConvert {

    @Override
    public SubjectDO convert(SubjectCreateReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        SubjectDO.SubjectDOBuilder subjectDO = SubjectDO.builder();

        subjectDO.name( bean.getName() );
        subjectDO.description( bean.getDescription() );

        return subjectDO.build();
    }

    @Override
    public SubjectDO convert(SubjectUpdateReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        SubjectDO.SubjectDOBuilder subjectDO = SubjectDO.builder();

        subjectDO.id( bean.getId() );
        subjectDO.name( bean.getName() );
        subjectDO.description( bean.getDescription() );

        return subjectDO.build();
    }

    @Override
    public SubjectRespVO convert(SubjectDO bean) {
        if ( bean == null ) {
            return null;
        }

        SubjectRespVO subjectRespVO = new SubjectRespVO();

        subjectRespVO.setName( bean.getName() );
        subjectRespVO.setDescription( bean.getDescription() );
        subjectRespVO.setId( bean.getId() );
        subjectRespVO.setCreateTime( bean.getCreateTime() );

        return subjectRespVO;
    }

    @Override
    public List<SubjectRespVO> convertList(List<SubjectDO> list) {
        if ( list == null ) {
            return null;
        }

        List<SubjectRespVO> list1 = new ArrayList<SubjectRespVO>( list.size() );
        for ( SubjectDO subjectDO : list ) {
            list1.add( convert( subjectDO ) );
        }

        return list1;
    }

    @Override
    public PageResult<SubjectRespVO> convertPage(PageResult<SubjectDO> page) {
        if ( page == null ) {
            return null;
        }

        PageResult<SubjectRespVO> pageResult = new PageResult<SubjectRespVO>();

        pageResult.setList( convertList( page.getList() ) );
        pageResult.setTotal( page.getTotal() );

        return pageResult;
    }

    @Override
    public List<SubjectExcelVO> convertList02(List<SubjectDO> list) {
        if ( list == null ) {
            return null;
        }

        List<SubjectExcelVO> list1 = new ArrayList<SubjectExcelVO>( list.size() );
        for ( SubjectDO subjectDO : list ) {
            list1.add( subjectDOToSubjectExcelVO( subjectDO ) );
        }

        return list1;
    }

    protected SubjectExcelVO subjectDOToSubjectExcelVO(SubjectDO subjectDO) {
        if ( subjectDO == null ) {
            return null;
        }

        SubjectExcelVO subjectExcelVO = new SubjectExcelVO();

        subjectExcelVO.setId( subjectDO.getId() );
        subjectExcelVO.setName( subjectDO.getName() );
        subjectExcelVO.setDescription( subjectDO.getDescription() );
        subjectExcelVO.setCreateTime( subjectDO.getCreateTime() );

        return subjectExcelVO;
    }
}
