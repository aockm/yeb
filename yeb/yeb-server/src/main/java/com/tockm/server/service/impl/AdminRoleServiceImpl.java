package com.tockm.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tockm.server.mapper.AdminRoleMapper;
import com.tockm.server.pojo.AdminRole;
import com.tockm.server.service.IAdminRoleService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理员角色中间表 服务实现类
 * </p>
 *
 * @author tockm
 * @since 2021-12-31
 */
@Service
public class AdminRoleServiceImpl extends ServiceImpl<AdminRoleMapper, AdminRole> implements IAdminRoleService {

}
