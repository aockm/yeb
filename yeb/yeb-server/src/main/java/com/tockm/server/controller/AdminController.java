package com.tockm.server.controller;


import com.sun.org.glassfish.gmbal.ParameterNames;
import com.tockm.server.pojo.Admin;
import com.tockm.server.pojo.RespBean;
import com.tockm.server.service.IAdminService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 管理员表 前端控制器
 * </p>
 *
 * @author tockm
 * @since 2021-12-31
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    IAdminService adminService;

    @RequestMapping("/hello")
    public String test(){
        return "hello";
    }

    @GetMapping("/test/{id}")
    public RespBean tet(@PathVariable(name = "id")int id){
        Admin user = adminService.getById(id);
        System.out.println(user);
        System.out.println("=====enable"+user.getEnabled());
        return RespBean.success("success",user);
    }

}
