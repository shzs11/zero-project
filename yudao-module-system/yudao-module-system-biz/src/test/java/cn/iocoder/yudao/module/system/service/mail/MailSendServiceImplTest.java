package cn.iocoder.yudao.module.system.service.mail;

import cn.hutool.core.map.MapUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.test.core.ut.BaseMockitoUnitTest;
import cn.iocoder.yudao.framework.test.core.util.RandomUtils;
import cn.iocoder.yudao.module.system.dal.dataobject.mail.MailAccountDO;
import cn.iocoder.yudao.module.system.dal.dataobject.mail.MailTemplateDO;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.system.mq.message.mail.MailSendMessage;
import cn.iocoder.yudao.module.system.mq.producer.mail.MailProducer;
import cn.iocoder.yudao.module.system.service.member.MemberService;
import cn.iocoder.yudao.module.system.service.user.AdminUserService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import java.util.HashMap;
import java.util.Map;

import static cn.hutool.core.util.RandomUtil.randomEle;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertServiceException;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class MailSendServiceImplTest extends BaseMockitoUnitTest {

    @InjectMocks
    private MailSendServiceImpl mailSendService;

    @Mock
    private AdminUserService adminUserService;
    @Mock
    private MemberService memberService;
    @Mock
    private MailAccountService mailAccountService;
    @Mock
    private MailTemplateService mailTemplateService;
    @Mock
    private MailLogService mailLogService;
    @Mock
    private MailProducer mailProducer;

    /**
     * ????????????????????????????????????????????????
     */
    @Test
    @Disabled
    public void testDemo() {
        MailAccount mailAccount = new MailAccount()
//                .setFrom("????????? <ydym_test@163.com>")
                .setFrom("ydym_test@163.com") // ????????????
                .setHost("smtp.163.com").setPort(465).setSslEnable(true) // SMTP ?????????
                .setAuth(true).setUser("ydym_test@163.com").setPass("WBZTEINMIFVRYSOE"); // ??????????????????
        String messageId = MailUtil.send(mailAccount, "7685413@qq.com", "??????", "??????", false);
        System.out.println("???????????????" + messageId);
    }

    @Test
    public void testSendSingleMailToAdmin() {
        // ????????????
        Long userId = randomLongId();
        String templateCode = RandomUtils.randomString();
        Map<String, Object> templateParams = MapUtil.<String, Object>builder().put("code", "1234")
                .put("op", "login").build();
        // mock adminUserService ?????????
        AdminUserDO user = randomPojo(AdminUserDO.class, o -> o.setMobile("15601691300"));
        when(adminUserService.getUser(eq(userId))).thenReturn(user);

        // mock MailTemplateService ?????????
        MailTemplateDO template = randomPojo(MailTemplateDO.class, o -> {
            o.setStatus(CommonStatusEnum.ENABLE.getStatus());
            o.setContent("????????????{code}, ?????????{op}");
            o.setParams(Lists.newArrayList("code", "op"));
        });
        when(mailTemplateService.getMailTemplateByCodeFromCache(eq(templateCode))).thenReturn(template);
        String content = RandomUtils.randomString();
        when(mailTemplateService.formatMailTemplateContent(eq(template.getContent()), eq(templateParams)))
                .thenReturn(content);
        // mock MailAccountService ?????????
        MailAccountDO account = randomPojo(MailAccountDO.class);
        when(mailAccountService.getMailAccountFromCache(eq(template.getAccountId()))).thenReturn(account);
        // mock MailLogService ?????????
        Long mailLogId = randomLongId();
        when(mailLogService.createMailLog(eq(userId), eq(UserTypeEnum.ADMIN.getValue()), eq(user.getEmail()),
                eq(account), eq(template), eq(content), eq(templateParams), eq(true))).thenReturn(mailLogId);

        // ??????
        Long resultMailLogId = mailSendService.sendSingleMailToAdmin(null, userId, templateCode, templateParams);
        // ??????
        assertEquals(mailLogId, resultMailLogId);
        // ????????????
        verify(mailProducer).sendMailSendMessage(eq(mailLogId), eq(user.getEmail()),
                eq(account.getId()), eq(template.getNickname()), eq(template.getTitle()), eq(content));
    }

    @Test
    public void testSendSingleMailToMember() {
        // ????????????
        Long userId = randomLongId();
        String templateCode = RandomUtils.randomString();
        Map<String, Object> templateParams = MapUtil.<String, Object>builder().put("code", "1234")
                .put("op", "login").build();
        // mock memberService ?????????
        String mail = randomEmail();
        when(memberService.getMemberUserEmail(eq(userId))).thenReturn(mail);

        // mock MailTemplateService ?????????
        MailTemplateDO template = randomPojo(MailTemplateDO.class, o -> {
            o.setStatus(CommonStatusEnum.ENABLE.getStatus());
            o.setContent("????????????{code}, ?????????{op}");
            o.setParams(Lists.newArrayList("code", "op"));
        });
        when(mailTemplateService.getMailTemplateByCodeFromCache(eq(templateCode))).thenReturn(template);
        String content = RandomUtils.randomString();
        when(mailTemplateService.formatMailTemplateContent(eq(template.getContent()), eq(templateParams)))
                .thenReturn(content);
        // mock MailAccountService ?????????
        MailAccountDO account = randomPojo(MailAccountDO.class);
        when(mailAccountService.getMailAccountFromCache(eq(template.getAccountId()))).thenReturn(account);
        // mock MailLogService ?????????
        Long mailLogId = randomLongId();
        when(mailLogService.createMailLog(eq(userId), eq(UserTypeEnum.MEMBER.getValue()), eq(mail),
                eq(account), eq(template), eq(content), eq(templateParams), eq(true))).thenReturn(mailLogId);

        // ??????
        Long resultMailLogId = mailSendService.sendSingleMailToMember(null, userId, templateCode, templateParams);
        // ??????
        assertEquals(mailLogId, resultMailLogId);
        // ????????????
        verify(mailProducer).sendMailSendMessage(eq(mailLogId), eq(mail),
                eq(account.getId()), eq(template.getNickname()), eq(template.getTitle()), eq(content));
    }

    /**
     * ???????????????????????????????????????
     */
    @Test
    public void testSendSingleMail_successWhenMailTemplateEnable() {
        // ????????????
        String mail = randomEmail();
        Long userId = randomLongId();
        Integer userType = randomEle(UserTypeEnum.values()).getValue();
        String templateCode = RandomUtils.randomString();
        Map<String, Object> templateParams = MapUtil.<String, Object>builder().put("code", "1234")
                .put("op", "login").build();
        // mock MailTemplateService ?????????
        MailTemplateDO template = randomPojo(MailTemplateDO.class, o -> {
            o.setStatus(CommonStatusEnum.ENABLE.getStatus());
            o.setContent("????????????{code}, ?????????{op}");
            o.setParams(Lists.newArrayList("code", "op"));
        });
        when(mailTemplateService.getMailTemplateByCodeFromCache(eq(templateCode))).thenReturn(template);
        String content = RandomUtils.randomString();
        when(mailTemplateService.formatMailTemplateContent(eq(template.getContent()), eq(templateParams)))
                .thenReturn(content);
        // mock MailAccountService ?????????
        MailAccountDO account = randomPojo(MailAccountDO.class);
        when(mailAccountService.getMailAccountFromCache(eq(template.getAccountId()))).thenReturn(account);
        // mock MailLogService ?????????
        Long mailLogId = randomLongId();
        when(mailLogService.createMailLog(eq(userId), eq(userType), eq(mail),
                eq(account), eq(template), eq(content), eq(templateParams), eq(true))).thenReturn(mailLogId);

        // ??????
        Long resultMailLogId = mailSendService.sendSingleMail(mail, userId, userType, templateCode, templateParams);
        // ??????
        assertEquals(mailLogId, resultMailLogId);
        // ????????????
        verify(mailProducer).sendMailSendMessage(eq(mailLogId), eq(mail),
                eq(account.getId()), eq(template.getNickname()), eq(template.getTitle()), eq(content));
    }

    /**
     * ???????????????????????????????????????
     */
    @Test
    public void testSendSingleMail_successWhenSmsTemplateDisable() {
        // ????????????
        String mail = randomEmail();
        Long userId = randomLongId();
        Integer userType = randomEle(UserTypeEnum.values()).getValue();
        String templateCode = RandomUtils.randomString();
        Map<String, Object> templateParams = MapUtil.<String, Object>builder().put("code", "1234")
                .put("op", "login").build();
        // mock MailTemplateService ?????????
        MailTemplateDO template = randomPojo(MailTemplateDO.class, o -> {
            o.setStatus(CommonStatusEnum.DISABLE.getStatus());
            o.setContent("????????????{code}, ?????????{op}");
            o.setParams(Lists.newArrayList("code", "op"));
        });
        when(mailTemplateService.getMailTemplateByCodeFromCache(eq(templateCode))).thenReturn(template);
        String content = RandomUtils.randomString();
        when(mailTemplateService.formatMailTemplateContent(eq(template.getContent()), eq(templateParams)))
                .thenReturn(content);
        // mock MailAccountService ?????????
        MailAccountDO account = randomPojo(MailAccountDO.class);
        when(mailAccountService.getMailAccountFromCache(eq(template.getAccountId()))).thenReturn(account);
        // mock MailLogService ?????????
        Long mailLogId = randomLongId();
        when(mailLogService.createMailLog(eq(userId), eq(userType), eq(mail),
                eq(account), eq(template), eq(content), eq(templateParams), eq(false))).thenReturn(mailLogId);

        // ??????
        Long resultMailLogId = mailSendService.sendSingleMail(mail, userId, userType, templateCode, templateParams);
        // ??????
        assertEquals(mailLogId, resultMailLogId);
        // ????????????
        verify(mailProducer, times(0)).sendMailSendMessage(anyLong(), anyString(),
                anyLong(), anyString(), anyString(), anyString());
    }

    @Test
    public void testValidateMailTemplateValid_notExists() {
        // ????????????
        String templateCode = RandomUtils.randomString();
        // mock ??????

        // ????????????????????????
        assertServiceException(() -> mailSendService.validateMailTemplate(templateCode),
                MAIL_TEMPLATE_NOT_EXISTS);
    }

    @Test
    public void testValidateTemplateParams_paramMiss() {
        // ????????????
        MailTemplateDO template = randomPojo(MailTemplateDO.class,
                o -> o.setParams(Lists.newArrayList("code")));
        Map<String, Object> templateParams = new HashMap<>();
        // mock ??????

        // ????????????????????????
        assertServiceException(() -> mailSendService.validateTemplateParams(template, templateParams),
                MAIL_SEND_TEMPLATE_PARAM_MISS, "code");
    }

    @Test
    public void testValidateMail_notExists() {
        // ????????????
        // mock ??????

        // ????????????????????????
        assertServiceException(() -> mailSendService.validateMail(null),
                MAIL_SEND_MAIL_NOT_EXISTS);
    }

    @Test
    public void testDoSendMail_success() {
        try (MockedStatic<MailUtil> mailUtilMock = mockStatic(MailUtil.class)) {
            // ????????????
            MailSendMessage message = randomPojo(MailSendMessage.class, o -> o.setNickname("??????"));
            // mock ??????????????????????????????
            MailAccountDO account = randomPojo(MailAccountDO.class, o -> o.setMail("7685@qq.com"));
            when(mailAccountService.getMailAccountFromCache(eq(message.getAccountId())))
                    .thenReturn(account);

            // mock ????????????????????????
            String messageId = randomString();
            mailUtilMock.when(() -> MailUtil.send(argThat(mailAccount -> {
                assertEquals("?????? <7685@qq.com>", mailAccount.getFrom());
                assertTrue(mailAccount.isAuth());
                assertEquals(account.getUsername(), mailAccount.getUser());
                assertEquals(account.getPassword(), mailAccount.getPass());
                assertEquals(account.getHost(), mailAccount.getHost());
                assertEquals(account.getPort(), mailAccount.getPort());
                assertEquals(account.getSslEnable(), mailAccount.isSslEnable());
                return true;
            }), eq(message.getMail()), eq(message.getTitle()), eq(message.getContent()), eq(true)))
                    .thenReturn(messageId);

            // ??????
            mailSendService.doSendMail(message);
            // ??????
            verify(mailLogService).updateMailSendResult(eq(message.getLogId()), eq(messageId), isNull());
        }
    }

    @Test
    public void testDoSendMail_exception() {
        try (MockedStatic<MailUtil> mailUtilMock = mockStatic(MailUtil.class)) {
            // ????????????
            MailSendMessage message = randomPojo(MailSendMessage.class, o -> o.setNickname("??????"));
            // mock ??????????????????????????????
            MailAccountDO account = randomPojo(MailAccountDO.class, o -> o.setMail("7685@qq.com"));
            when(mailAccountService.getMailAccountFromCache(eq(message.getAccountId())))
                    .thenReturn(account);

            // mock ????????????????????????
            Exception e = new NullPointerException("?????????");
            mailUtilMock.when(() -> MailUtil.send(argThat(mailAccount -> {
                        assertEquals("?????? <7685@qq.com>", mailAccount.getFrom());
                        assertTrue(mailAccount.isAuth());
                        assertEquals(account.getUsername(), mailAccount.getUser());
                        assertEquals(account.getPassword(), mailAccount.getPass());
                        assertEquals(account.getHost(), mailAccount.getHost());
                        assertEquals(account.getPort(), mailAccount.getPort());
                        assertEquals(account.getSslEnable(), mailAccount.isSslEnable());
                        return true;
                    }), eq(message.getMail()), eq(message.getTitle()), eq(message.getContent()), eq(true)))
                    .thenThrow(e);

            // ??????
            mailSendService.doSendMail(message);
            // ??????
            verify(mailLogService).updateMailSendResult(eq(message.getLogId()), isNull(), same(e));
        }
    }

}
