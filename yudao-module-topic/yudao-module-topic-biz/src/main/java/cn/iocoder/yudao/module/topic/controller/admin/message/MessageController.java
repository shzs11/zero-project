package cn.iocoder.yudao.module.topic.controller.admin.message;

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

import cn.iocoder.yudao.module.topic.controller.admin.message.vo.*;
import cn.iocoder.yudao.module.topic.dal.dataobject.message.MessageDO;
import cn.iocoder.yudao.module.topic.convert.message.MessageConvert;
import cn.iocoder.yudao.module.topic.service.message.MessageService;

@Tag(name = "管理后台 - 错题")
@RestController
@RequestMapping("/topic/message")
@Validated
public class MessageController {

    @Resource
    private MessageService messageService;

    @PostMapping("/create")
    @Operation(summary = "创建错题")
    @PreAuthorize("@ss.hasPermission('topic:message:create')")
    public CommonResult<Long> createMessage(@Valid @RequestBody MessageCreateReqVO createReqVO) {
        return success(messageService.createMessage(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新错题")
    @PreAuthorize("@ss.hasPermission('topic:message:update')")
    public CommonResult<Boolean> updateMessage(@Valid @RequestBody MessageUpdateReqVO updateReqVO) {
        messageService.updateMessage(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除错题")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('topic:message:delete')")
    public CommonResult<Boolean> deleteMessage(@RequestParam("id") Long id) {
        messageService.deleteMessage(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得错题")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('topic:message:query')")
    public CommonResult<MessageRespVO> getMessage(@RequestParam("id") Long id) {
        MessageDO message = messageService.getMessage(id);
        return success(MessageConvert.INSTANCE.convert(message));
    }

    @GetMapping("/list")
    @Operation(summary = "获得错题列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('topic:message:query')")
    public CommonResult<List<MessageRespVO>> getMessageList(@RequestParam("ids") Collection<Long> ids) {
        List<MessageDO> list = messageService.getMessageList(ids);
        return success(MessageConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得错题分页")
    @PreAuthorize("@ss.hasPermission('topic:message:query')")
    public CommonResult<PageResult<MessageRespVO>> getMessagePage(@Valid MessagePageReqVO pageVO) {
        PageResult<MessageDO> pageResult = messageService.getMessagePage(pageVO);
        return success(MessageConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出错题 Excel")
    @PreAuthorize("@ss.hasPermission('topic:message:export')")
    @OperateLog(type = EXPORT)
    public void exportMessageExcel(@Valid MessageExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<MessageDO> list = messageService.getMessageList(exportReqVO);
        // 导出 Excel
        List<MessageExcelVO> datas = MessageConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "错题.xls", "数据", MessageExcelVO.class, datas);
    }

}
