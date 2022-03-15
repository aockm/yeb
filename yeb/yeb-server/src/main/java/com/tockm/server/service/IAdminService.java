package com.tockm.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tockm.server.pojo.Admin;
import com.tockm.server.pojo.RespBean;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 * @author tockm
 * @since 2021-12-31
 */
public interface IAdminService extends IService<Admin> {

    RespBean login(String username, String password, HttpServletRequest request);

    Admin getAdminByUserName(String username);
}
