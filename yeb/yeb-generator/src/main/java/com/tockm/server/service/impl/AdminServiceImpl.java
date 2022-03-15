package com.tockm.server.service.impl;

import com.tockm.server.pojo.Admin;
import com.tockm.server.mapper.AdminMapper;
import com.tockm.server.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author tockm
 * @since 2021-12-31
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

}
