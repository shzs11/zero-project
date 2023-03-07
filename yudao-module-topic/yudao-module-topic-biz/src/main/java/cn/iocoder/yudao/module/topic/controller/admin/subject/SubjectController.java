package cn.iocoder.yudao.module.topic.controller.admin.subject;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.topic.controller.admin.subject.vo.*;
import cn.iocoder.yudao.module.topic.dal.dataobject.subject.SubjectDO;
import cn.iocoder.yudao.module.topic.convert.subject.SubjectConvert;
import cn.iocoder.yudao.module.topic.service.subject.SubjectService;

@Tag(name = "管理后台 - 科目")
@RestController
@RequestMapping("/topic/subject")
@Validated
public class SubjectController {

    @Resource
    private SubjectService subjectService;

    @PostMapping("/create")
    @Operation(summary = "创建科目")
    @PreAuthorize("@ss.hasPermission('topic:subject:create')")
    public CommonResult<Long> createSubject(@Valid @RequestBody SubjectCreateReqVO createReqVO) {
        return success(subjectService.createSubject(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新科目")
    @PreAuthorize("@ss.hasPermission('topic:subject:update')")
    public CommonResult<Boolean> updateSubject(@Valid @RequestBody SubjectUpdateReqVO updateReqVO) {
        subjectService.updateSubject(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除科目")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('topic:subject:delete')")
    public CommonResult<Boolean> deleteSubject(@RequestParam("id") Long id) {
        subjectService.deleteSubject(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得科目")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('topic:subject:query')")
    public CommonResult<SubjectRespVO> getSubject(@RequestParam("id") Long id) {
        SubjectDO subject = subjectService.getSubject(id);
        return success(SubjectConvert.INSTANCE.convert(subject));
    }

    @GetMapping("/list")
    @Operation(summary = "获得科目列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('topic:subject:query')")
    public CommonResult<List<SubjectRespVO>> getSubjectList(@RequestParam("ids") Collection<Long> ids) {
        List<SubjectDO> list = subjectService.getSubjectList(ids);
        return success(SubjectConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得科目分页")
    @PreAuthorize("@ss.hasPermission('topic:subject:query')")
    public CommonResult<PageResult<SubjectRespVO>> getSubjectPage(@Valid SubjectPageReqVO pageVO) {
        PageResult<SubjectDO> pageResult = subjectService.getSubjectPage(pageVO);
        return success(SubjectConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出科目 Excel")
    @PreAuthorize("@ss.hasPermission('topic:subject:export')")
    @OperateLog(type = EXPORT)
    public void exportSubjectExcel(@Valid SubjectExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<SubjectDO> list = subjectService.getSubjectList(exportReqVO);
        // 导出 Excel
        List<SubjectExcelVO> datas = SubjectConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "科目.xls", "数据", SubjectExcelVO.class, datas);
    }

}
