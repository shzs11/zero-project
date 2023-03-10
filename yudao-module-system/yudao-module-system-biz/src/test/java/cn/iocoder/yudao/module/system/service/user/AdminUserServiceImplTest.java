package cn.iocoder.yudao.module.system.service.user;

import cn.hutool.core.util.RandomUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.ArrayUtils;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;
import cn.iocoder.yudao.module.infra.api.file.FileApi;
import cn.iocoder.yudao.module.system.controller.admin.user.vo.profile.UserProfileUpdatePasswordReqVO;
import cn.iocoder.yudao.module.system.controller.admin.user.vo.profile.UserProfileUpdateReqVO;
import cn.iocoder.yudao.module.system.controller.admin.user.vo.user.*;
import cn.iocoder.yudao.module.system.dal.dataobject.dept.DeptDO;
import cn.iocoder.yudao.module.system.dal.dataobject.dept.PostDO;
import cn.iocoder.yudao.module.system.dal.dataobject.dept.UserPostDO;
import cn.iocoder.yudao.module.system.dal.dataobject.tenant.TenantDO;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.system.dal.mysql.dept.UserPostMapper;
import cn.iocoder.yudao.module.system.dal.mysql.user.AdminUserMapper;
import cn.iocoder.yudao.module.system.enums.common.SexEnum;
import cn.iocoder.yudao.module.system.service.dept.DeptService;
import cn.iocoder.yudao.module.system.service.dept.PostService;
import cn.iocoder.yudao.module.system.service.permission.PermissionService;
import cn.iocoder.yudao.module.system.service.tenant.TenantService;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static cn.hutool.core.util.RandomUtil.randomBytes;
import static cn.hutool.core.util.RandomUtil.randomEle;
import static cn.iocoder.yudao.framework.common.util.collection.SetUtils.asSet;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.buildBetweenTime;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.buildTime;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.cloneIgnoreId;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertPojoEquals;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertServiceException;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.assertj.core.util.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Import(AdminUserServiceImpl.class)
public class AdminUserServiceImplTest extends BaseDbUnitTest {

    @Resource
    private AdminUserServiceImpl userService;

    @Resource
    private AdminUserMapper userMapper;
    @Resource
    private UserPostMapper userPostMapper;

    @MockBean
    private DeptService deptService;
    @MockBean
    private PostService postService;
    @MockBean
    private PermissionService permissionService;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private TenantService tenantService;
    @MockBean
    private FileApi fileApi;

    @Test
    public void testCreatUser_success() {
        // ????????????
        UserCreateReqVO reqVO = randomPojo(UserCreateReqVO.class, o -> {
            o.setSex(RandomUtil.randomEle(SexEnum.values()).getSex());
            o.setMobile(randomString());
            o.setPostIds(asSet(1L, 2L));
        });
        // mock ??????????????????
        TenantDO tenant = randomPojo(TenantDO.class, o -> o.setAccountCount(1));
        doNothing().when(tenantService).handleTenantInfo(argThat(handler -> {
            handler.handle(tenant);
            return true;
        }));
        // mock deptService ?????????
        DeptDO dept = randomPojo(DeptDO.class, o -> {
            o.setId(reqVO.getDeptId());
            o.setStatus(CommonStatusEnum.ENABLE.getStatus());
        });
        when(deptService.getDept(eq(dept.getId()))).thenReturn(dept);
        // mock postService ?????????
        List<PostDO> posts = CollectionUtils.convertList(reqVO.getPostIds(), postId ->
                randomPojo(PostDO.class, o -> {
                    o.setId(postId);
                    o.setStatus(CommonStatusEnum.ENABLE.getStatus());
                }));
        when(postService.getPostList(eq(reqVO.getPostIds()), isNull())).thenReturn(posts);
        // mock passwordEncoder ?????????
        when(passwordEncoder.encode(eq(reqVO.getPassword()))).thenReturn("yudaoyuanma");

        // ??????
        Long userId = userService.createUser(reqVO);
        // ??????
        AdminUserDO user = userMapper.selectById(userId);
        assertPojoEquals(reqVO, user, "password");
        assertEquals("yudaoyuanma", user.getPassword());
        assertEquals(CommonStatusEnum.ENABLE.getStatus(), user.getStatus());
        // ??????????????????
        List<UserPostDO> userPosts = userPostMapper.selectListByUserId(user.getId());
        assertEquals(1L, userPosts.get(0).getPostId());
        assertEquals(2L, userPosts.get(1).getPostId());
    }

    @Test
    public void testCreatUser_max() {
        // ????????????
        UserCreateReqVO reqVO = randomPojo(UserCreateReqVO.class);
        // mock ??????????????????
        TenantDO tenant = randomPojo(TenantDO.class, o -> o.setAccountCount(-1));
        doNothing().when(tenantService).handleTenantInfo(argThat(handler -> {
            handler.handle(tenant);
            return true;
        }));

        // ????????????????????????
        assertServiceException(() -> userService.createUser(reqVO), USER_COUNT_MAX, -1);
    }

    @Test
    public void testUpdateUser_success() {
        // mock ??????
        AdminUserDO dbUser = randomAdminUserDO(o -> o.setPostIds(asSet(1L, 2L)));
        userMapper.insert(dbUser);
        userPostMapper.insert(new UserPostDO().setUserId(dbUser.getId()).setPostId(1L));
        userPostMapper.insert(new UserPostDO().setUserId(dbUser.getId()).setPostId(2L));
        // ????????????
        UserUpdateReqVO reqVO = randomPojo(UserUpdateReqVO.class, o -> {
            o.setId(dbUser.getId());
            o.setSex(RandomUtil.randomEle(SexEnum.values()).getSex());
            o.setMobile(randomString());
            o.setPostIds(asSet(2L, 3L));
        });
        // mock deptService ?????????
        DeptDO dept = randomPojo(DeptDO.class, o -> {
            o.setId(reqVO.getDeptId());
            o.setStatus(CommonStatusEnum.ENABLE.getStatus());
        });
        when(deptService.getDept(eq(dept.getId()))).thenReturn(dept);
        // mock postService ?????????
        List<PostDO> posts = CollectionUtils.convertList(reqVO.getPostIds(), postId ->
                randomPojo(PostDO.class, o -> {
                    o.setId(postId);
                    o.setStatus(CommonStatusEnum.ENABLE.getStatus());
                }));
        when(postService.getPostList(eq(reqVO.getPostIds()), isNull())).thenReturn(posts);

        // ??????
        userService.updateUser(reqVO);
        // ??????
        AdminUserDO user = userMapper.selectById(reqVO.getId());
        assertPojoEquals(reqVO, user);
        // ??????????????????
        List<UserPostDO> userPosts = userPostMapper.selectListByUserId(user.getId());
        assertEquals(2L, userPosts.get(0).getPostId());
        assertEquals(3L, userPosts.get(1).getPostId());
    }

    @Test
    public void testUpdateUserLogin() {
        // mock ??????
        AdminUserDO user = randomAdminUserDO(o -> o.setLoginDate(null));
        userMapper.insert(user);
        // ????????????
        Long id = user.getId();
        String loginIp = randomString();

        // ??????
        userService.updateUserLogin(id, loginIp);
        // ??????
        AdminUserDO dbUser = userMapper.selectById(id);
        assertEquals(loginIp, dbUser.getLoginIp());
        assertNotNull(dbUser.getLoginDate());
    }

    @Test
    public void testUpdateUserProfile_success() {
        // mock ??????
        AdminUserDO dbUser = randomAdminUserDO();
        userMapper.insert(dbUser);
        // ????????????
        Long userId = dbUser.getId();
        UserProfileUpdateReqVO reqVO = randomPojo(UserProfileUpdateReqVO.class, o -> {
            o.setMobile(randomString());
            o.setSex(RandomUtil.randomEle(SexEnum.values()).getSex());
        });

        // ??????
        userService.updateUserProfile(userId, reqVO);
        // ??????
        AdminUserDO user = userMapper.selectById(userId);
        assertPojoEquals(reqVO, user);
    }

    @Test
    public void testUpdateUserPassword_success() {
        // mock ??????
        AdminUserDO dbUser = randomAdminUserDO(o -> o.setPassword("encode:tudou"));
        userMapper.insert(dbUser);
        // ????????????
        Long userId = dbUser.getId();
        UserProfileUpdatePasswordReqVO reqVO = randomPojo(UserProfileUpdatePasswordReqVO.class, o -> {
            o.setOldPassword("tudou");
            o.setNewPassword("yuanma");
        });
        // mock ??????
        when(passwordEncoder.encode(anyString())).then(
                (Answer<String>) invocationOnMock -> "encode:" + invocationOnMock.getArgument(0));
        when(passwordEncoder.matches(eq(reqVO.getOldPassword()), eq(dbUser.getPassword()))).thenReturn(true);

        // ??????
        userService.updateUserPassword(userId, reqVO);
        // ??????
        AdminUserDO user = userMapper.selectById(userId);
        assertEquals("encode:yuanma", user.getPassword());
    }

    @Test
    public void testUpdateUserAvatar_success() throws Exception {
        // mock ??????
        AdminUserDO dbUser = randomAdminUserDO();
        userMapper.insert(dbUser);
        // ????????????
        Long userId = dbUser.getId();
        byte[] avatarFileBytes = randomBytes(10);
        ByteArrayInputStream avatarFile = new ByteArrayInputStream(avatarFileBytes);
        // mock ??????
        String avatar = randomString();
        when(fileApi.createFile(eq( avatarFileBytes))).thenReturn(avatar);

        // ??????
        userService.updateUserAvatar(userId, avatarFile);
        // ??????
        AdminUserDO user = userMapper.selectById(userId);
        assertEquals(avatar, user.getAvatar());
    }

    @Test
    public void testUpdateUserPassword02_success() {
        // mock ??????
        AdminUserDO dbUser = randomAdminUserDO();
        userMapper.insert(dbUser);
        // ????????????
        Long userId = dbUser.getId();
        String password = "yudao";
        // mock ??????
        when(passwordEncoder.encode(anyString())).then(
                (Answer<String>) invocationOnMock -> "encode:" + invocationOnMock.getArgument(0));

        // ??????
        userService.updateUserPassword(userId, password);
        // ??????
        AdminUserDO user = userMapper.selectById(userId);
        assertEquals("encode:" + password, user.getPassword());
    }

    @Test
    public void testUpdateUserStatus() {
        // mock ??????
        AdminUserDO dbUser = randomAdminUserDO();
        userMapper.insert(dbUser);
        // ????????????
        Long userId = dbUser.getId();
        Integer status = randomCommonStatus();

        // ??????
        userService.updateUserStatus(userId, status);
        // ??????
        AdminUserDO user = userMapper.selectById(userId);
        assertEquals(status, user.getStatus());
    }

    @Test
    public void testDeleteUser_success(){
        // mock ??????
        AdminUserDO dbUser = randomAdminUserDO();
        userMapper.insert(dbUser);
        // ????????????
        Long userId = dbUser.getId();

        // ????????????
        userService.deleteUser(userId);
        // ????????????
        assertNull(userMapper.selectById(userId));
        // ??????????????????
        verify(permissionService, times(1)).processUserDeleted(eq(userId));
    }

    @Test
    public void testGetUserByUsername() {
        // mock ??????
        AdminUserDO dbUser = randomAdminUserDO();
        userMapper.insert(dbUser);
        // ????????????
        String username = dbUser.getUsername();

        // ??????
        AdminUserDO user = userService.getUserByUsername(username);
        // ??????
        assertPojoEquals(dbUser, user);
    }

    @Test
    public void testGetUserByMobile() {
        // mock ??????
        AdminUserDO dbUser = randomAdminUserDO();
        userMapper.insert(dbUser);
        // ????????????
        String mobile = dbUser.getMobile();

        // ??????
        AdminUserDO user = userService.getUserByMobile(mobile);
        // ??????
        assertPojoEquals(dbUser, user);
    }

    @Test
    public void testGetUserPage() {
        // mock ??????
        AdminUserDO dbUser = initGetUserPageData();
        // ????????????
        UserPageReqVO reqVO = new UserPageReqVO();
        reqVO.setUsername("tu");
        reqVO.setMobile("1560");
        reqVO.setStatus(CommonStatusEnum.ENABLE.getStatus());
        reqVO.setCreateTime(buildBetweenTime(2020, 12, 1, 2020, 12, 24));
        reqVO.setDeptId(1L); // ?????????1L ??? 2L ????????????
        // mock ??????
        List<DeptDO> deptList = newArrayList(randomPojo(DeptDO.class, o -> o.setId(2L)));
        when(deptService.getDeptListByParentIdFromCache(eq(reqVO.getDeptId()), eq(true))).thenReturn(deptList);

        // ??????
        PageResult<AdminUserDO> pageResult = userService.getUserPage(reqVO);
        // ??????
        assertEquals(1, pageResult.getTotal());
        assertEquals(1, pageResult.getList().size());
        assertPojoEquals(dbUser, pageResult.getList().get(0));
    }

    @Test
    public void testGetUserList_export() {
        // mock ??????
        AdminUserDO dbUser = initGetUserPageData();
        // ????????????
        UserExportReqVO reqVO = new UserExportReqVO();
        reqVO.setUsername("tu");
        reqVO.setMobile("1560");
        reqVO.setStatus(CommonStatusEnum.ENABLE.getStatus());
        reqVO.setCreateTime(buildBetweenTime(2020, 12, 1, 2020, 12, 24));
        reqVO.setDeptId(1L); // ?????????1L ??? 2L ????????????
        // mock ??????
        List<DeptDO> deptList = newArrayList(randomPojo(DeptDO.class, o -> o.setId(2L)));
        when(deptService.getDeptListByParentIdFromCache(eq(reqVO.getDeptId()), eq(true))).thenReturn(deptList);

        // ??????
        List<AdminUserDO> list = userService.getUserList(reqVO);
        // ??????
        assertEquals(1, list.size());
        assertPojoEquals(dbUser, list.get(0));
    }

    /**
     * ????????? getUserPage ?????????????????????
     */
    private AdminUserDO initGetUserPageData() {
        // mock ??????
        AdminUserDO dbUser = randomAdminUserDO(o -> { // ???????????????
            o.setUsername("tudou");
            o.setMobile("15601691300");
            o.setStatus(CommonStatusEnum.ENABLE.getStatus());
            o.setCreateTime(buildTime(2020, 12, 12));
            o.setDeptId(2L);
        });
        userMapper.insert(dbUser);
        // ?????? username ?????????
        userMapper.insert(cloneIgnoreId(dbUser, o -> o.setUsername("dou")));
        // ?????? mobile ?????????
        userMapper.insert(cloneIgnoreId(dbUser, o -> o.setMobile("18818260888")));
        // ?????? status ?????????
        userMapper.insert(cloneIgnoreId(dbUser, o -> o.setStatus(CommonStatusEnum.DISABLE.getStatus())));
        // ?????? createTime ?????????
        userMapper.insert(cloneIgnoreId(dbUser, o -> o.setCreateTime(buildTime(2020, 11, 11))));
        // ?????? dept ?????????
        userMapper.insert(cloneIgnoreId(dbUser, o -> o.setDeptId(0L)));
        return dbUser;
    }

    @Test
    public void testGetUser() {
        // mock ??????
        AdminUserDO dbUser = randomAdminUserDO();
        userMapper.insert(dbUser);
        // ????????????
        Long userId = dbUser.getId();

        // ??????
        AdminUserDO user = userService.getUser(userId);
        // ??????
        assertPojoEquals(dbUser, user);
    }

    @Test
    public void testGetUserListByDeptIds() {
        // mock ??????
        AdminUserDO dbUser = randomAdminUserDO(o -> o.setDeptId(1L));
        userMapper.insert(dbUser);
        // ?????? deptId ?????????
        userMapper.insert(cloneIgnoreId(dbUser, o -> o.setDeptId(2L)));
        // ????????????
        Collection<Long> deptIds = singleton(1L);

        // ??????
        List<AdminUserDO> list = userService.getUserListByDeptIds(deptIds);
        // ??????
        assertEquals(1, list.size());
        assertEquals(dbUser, list.get(0));
    }

    /**
     * ????????????????????????????????????????????????
     */
    @Test
    public void testImportUserList_01() {
        // ????????????
        UserImportExcelVO importUser = randomPojo(UserImportExcelVO.class, o -> {
        });
        // mock ?????????????????????
        doThrow(new ServiceException(DEPT_NOT_FOUND)).when(deptService).validateDeptList(any());

        // ??????
        UserImportRespVO respVO = userService.importUserList(newArrayList(importUser), true);
        // ??????
        assertEquals(0, respVO.getCreateUsernames().size());
        assertEquals(0, respVO.getUpdateUsernames().size());
        assertEquals(1, respVO.getFailureUsernames().size());
        assertEquals(DEPT_NOT_FOUND.getMsg(), respVO.getFailureUsernames().get(importUser.getUsername()));
    }

    /**
     * ????????????????????????????????????
     */
    @Test
    public void testImportUserList_02() {
        // ????????????
        UserImportExcelVO importUser = randomPojo(UserImportExcelVO.class, o -> {
            o.setStatus(randomEle(CommonStatusEnum.values()).getStatus()); // ?????? status ?????????
            o.setSex(randomEle(SexEnum.values()).getSex()); // ?????? sex ?????????
        });
        // mock deptService ?????????
        DeptDO dept = randomPojo(DeptDO.class, o -> {
            o.setId(importUser.getDeptId());
            o.setStatus(CommonStatusEnum.ENABLE.getStatus());
        });
        when(deptService.getDept(eq(dept.getId()))).thenReturn(dept);
        // mock passwordEncoder ?????????
        when(passwordEncoder.encode(eq("yudaoyuanma"))).thenReturn("java");

        // ??????
        UserImportRespVO respVO = userService.importUserList(newArrayList(importUser), true);
        // ??????
        assertEquals(1, respVO.getCreateUsernames().size());
        AdminUserDO user = userMapper.selectByUsername(respVO.getCreateUsernames().get(0));
        assertPojoEquals(importUser, user);
        assertEquals("java", user.getPassword());
        assertEquals(0, respVO.getUpdateUsernames().size());
        assertEquals(0, respVO.getFailureUsernames().size());
    }

    /**
     * ??????????????????????????????????????????
     */
    @Test
    public void testImportUserList_03() {
        // mock ??????
        AdminUserDO dbUser = randomAdminUserDO();
        userMapper.insert(dbUser);
        // ????????????
        UserImportExcelVO importUser = randomPojo(UserImportExcelVO.class, o -> {
            o.setStatus(randomEle(CommonStatusEnum.values()).getStatus()); // ?????? status ?????????
            o.setSex(randomEle(SexEnum.values()).getSex()); // ?????? sex ?????????
            o.setUsername(dbUser.getUsername());
        });
        // mock deptService ?????????
        DeptDO dept = randomPojo(DeptDO.class, o -> {
            o.setId(importUser.getDeptId());
            o.setStatus(CommonStatusEnum.ENABLE.getStatus());
        });
        when(deptService.getDept(eq(dept.getId()))).thenReturn(dept);

        // ??????
        UserImportRespVO respVO = userService.importUserList(newArrayList(importUser), false);
        // ??????
        assertEquals(0, respVO.getCreateUsernames().size());
        assertEquals(0, respVO.getUpdateUsernames().size());
        assertEquals(1, respVO.getFailureUsernames().size());
        assertEquals(USER_USERNAME_EXISTS.getMsg(), respVO.getFailureUsernames().get(importUser.getUsername()));
    }

    /**
     * ?????????????????????????????????
     */
    @Test
    public void testImportUserList_04() {
        // mock ??????
        AdminUserDO dbUser = randomAdminUserDO();
        userMapper.insert(dbUser);
        // ????????????
        UserImportExcelVO importUser = randomPojo(UserImportExcelVO.class, o -> {
            o.setStatus(randomEle(CommonStatusEnum.values()).getStatus()); // ?????? status ?????????
            o.setSex(randomEle(SexEnum.values()).getSex()); // ?????? sex ?????????
            o.setUsername(dbUser.getUsername());
        });
        // mock deptService ?????????
        DeptDO dept = randomPojo(DeptDO.class, o -> {
            o.setId(importUser.getDeptId());
            o.setStatus(CommonStatusEnum.ENABLE.getStatus());
        });
        when(deptService.getDept(eq(dept.getId()))).thenReturn(dept);

        // ??????
        UserImportRespVO respVO = userService.importUserList(newArrayList(importUser), true);
        // ??????
        assertEquals(0, respVO.getCreateUsernames().size());
        assertEquals(1, respVO.getUpdateUsernames().size());
        AdminUserDO user = userMapper.selectByUsername(respVO.getUpdateUsernames().get(0));
        assertPojoEquals(importUser, user);
        assertEquals(0, respVO.getFailureUsernames().size());
    }

    @Test
    public void testValidateUserExists_notExists() {
        assertServiceException(() -> userService.validateUserExists(randomLongId()), USER_NOT_EXISTS);
    }

    @Test
    public void testValidateUsernameUnique_usernameExistsForCreate() {
        // ????????????
        String username = randomString();
        // mock ??????
        userMapper.insert(randomAdminUserDO(o -> o.setUsername(username)));

        // ?????????????????????
        assertServiceException(() -> userService.validateUsernameUnique(null, username),
                USER_USERNAME_EXISTS);
    }

    @Test
    public void testValidateUsernameUnique_usernameExistsForUpdate() {
        // ????????????
        Long id = randomLongId();
        String username = randomString();
        // mock ??????
        userMapper.insert(randomAdminUserDO(o -> o.setUsername(username)));

        // ?????????????????????
        assertServiceException(() -> userService.validateUsernameUnique(id, username),
                USER_USERNAME_EXISTS);
    }

    @Test
    public void testValidateEmailUnique_emailExistsForCreate() {
        // ????????????
        String email = randomString();
        // mock ??????
        userMapper.insert(randomAdminUserDO(o -> o.setEmail(email)));

        // ?????????????????????
        assertServiceException(() -> userService.validateEmailUnique(null, email),
                USER_EMAIL_EXISTS);
    }

    @Test
    public void testValidateEmailUnique_emailExistsForUpdate() {
        // ????????????
        Long id = randomLongId();
        String email = randomString();
        // mock ??????
        userMapper.insert(randomAdminUserDO(o -> o.setEmail(email)));

        // ?????????????????????
        assertServiceException(() -> userService.validateEmailUnique(id, email),
                USER_EMAIL_EXISTS);
    }

    @Test
    public void testValidateMobileUnique_mobileExistsForCreate() {
        // ????????????
        String mobile = randomString();
        // mock ??????
        userMapper.insert(randomAdminUserDO(o -> o.setMobile(mobile)));

        // ?????????????????????
        assertServiceException(() -> userService.validateMobileUnique(null, mobile),
                USER_MOBILE_EXISTS);
    }

    @Test
    public void testValidateMobileUnique_mobileExistsForUpdate() {
        // ????????????
        Long id = randomLongId();
        String mobile = randomString();
        // mock ??????
        userMapper.insert(randomAdminUserDO(o -> o.setMobile(mobile)));

        // ?????????????????????
        assertServiceException(() -> userService.validateMobileUnique(id, mobile),
                USER_MOBILE_EXISTS);
    }

    @Test
    public void testValidateOldPassword_notExists() {
        assertServiceException(() -> userService.validateOldPassword(randomLongId(), randomString()),
                USER_NOT_EXISTS);
    }

    @Test
    public void testValidateOldPassword_passwordFailed() {
        // mock ??????
        AdminUserDO user = randomAdminUserDO();
        userMapper.insert(user);
        // ????????????
        Long id = user.getId();
        String oldPassword = user.getPassword();

        // ?????????????????????
        assertServiceException(() -> userService.validateOldPassword(id, oldPassword),
                USER_PASSWORD_FAILED);
        // ????????????
        verify(passwordEncoder, times(1)).matches(eq(oldPassword), eq(user.getPassword()));
    }

    @Test
    public void testUserListByPostIds() {
        // ????????????
        Collection<Long> postIds = asSet(10L, 20L);
        // mock user1 ??????
        AdminUserDO user1 = randomAdminUserDO(o -> o.setPostIds(asSet(10L, 30L)));
        userMapper.insert(user1);
        userPostMapper.insert(new UserPostDO().setUserId(user1.getId()).setPostId(10L));
        userPostMapper.insert(new UserPostDO().setUserId(user1.getId()).setPostId(30L));
        // mock user2 ??????
        AdminUserDO user2 = randomAdminUserDO(o -> o.setPostIds(singleton(100L)));
        userMapper.insert(user2);
        userPostMapper.insert(new UserPostDO().setUserId(user2.getId()).setPostId(100L));

        // ??????
        List<AdminUserDO> result = userService.getUserListByPostIds(postIds);
        // ??????
        assertEquals(1, result.size());
        assertEquals(user1, result.get(0));
    }

    @Test
    public void testGetUserList() {
        // mock ??????
        AdminUserDO user = randomAdminUserDO();
        userMapper.insert(user);
        // ?????? id ?????????
        userMapper.insert(randomAdminUserDO());
        // ????????????
        Collection<Long> ids = singleton(user.getId());

        // ??????
        List<AdminUserDO> result = userService.getUserList(ids);
        // ??????
        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
    }

    @Test
    public void testGetUserMap() {
        // mock ??????
        AdminUserDO user = randomAdminUserDO();
        userMapper.insert(user);
        // ?????? id ?????????
        userMapper.insert(randomAdminUserDO());
        // ????????????
        Collection<Long> ids = singleton(user.getId());

        // ??????
        Map<Long, AdminUserDO> result = userService.getUserMap(ids);
        // ??????
        assertEquals(1, result.size());
        assertEquals(user, result.get(user.getId()));
    }

    @Test
    public void testGetUserListByNickname() {
        // mock ??????
        AdminUserDO user = randomAdminUserDO(o -> o.setNickname("??????"));
        userMapper.insert(user);
        // ?????? nickname ?????????
        userMapper.insert(randomAdminUserDO(o -> o.setNickname("??????")));
        // ????????????
        String nickname = "???";

        // ??????
        List<AdminUserDO> result = userService.getUserListByNickname(nickname);
        // ??????
        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
    }

    @Test
    public void testGetUserListByStatus() {
        // mock ??????
        AdminUserDO user = randomAdminUserDO(o -> o.setStatus(CommonStatusEnum.DISABLE.getStatus()));
        userMapper.insert(user);
        // ?????? status ?????????
        userMapper.insert(randomAdminUserDO(o -> o.setStatus(CommonStatusEnum.ENABLE.getStatus())));
        // ????????????
        Integer status = CommonStatusEnum.DISABLE.getStatus();

        // ??????
        List<AdminUserDO> result = userService.getUserListByStatus(status);
        // ??????
        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
    }

    @Test
    public void testValidateUserList_success() {
        // mock ??????
        AdminUserDO userDO = randomAdminUserDO().setStatus(CommonStatusEnum.ENABLE.getStatus());
        userMapper.insert(userDO);
        // ????????????
        List<Long> ids = singletonList(userDO.getId());

        // ?????????????????????
        userService.validateUserList(ids);
    }

    @Test
    public void testValidateUserList_notFound() {
        // ????????????
        List<Long> ids = singletonList(randomLongId());

        // ??????, ???????????????
        assertServiceException(() -> userService.validateUserList(ids), USER_NOT_EXISTS);
    }

    @Test
    public void testValidateUserList_notEnable() {
        // mock ??????
        AdminUserDO userDO = randomAdminUserDO().setStatus(CommonStatusEnum.DISABLE.getStatus());
        userMapper.insert(userDO);
        // ????????????
        List<Long> ids = singletonList(userDO.getId());

        // ??????, ???????????????
        assertServiceException(() -> userService.validateUserList(ids), USER_IS_DISABLE,
                userDO.getNickname());
    }

    // ========== ???????????? ==========

    @SafeVarargs
    private static AdminUserDO randomAdminUserDO(Consumer<AdminUserDO>... consumers) {
        Consumer<AdminUserDO> consumer = (o) -> {
            o.setStatus(randomEle(CommonStatusEnum.values()).getStatus()); // ?????? status ?????????
            o.setSex(randomEle(SexEnum.values()).getSex()); // ?????? sex ?????????
        };
        return randomPojo(AdminUserDO.class, ArrayUtils.append(consumer, consumers));
    }

}
