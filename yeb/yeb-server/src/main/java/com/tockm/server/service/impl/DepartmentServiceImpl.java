package com.tockm.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tockm.server.mapper.DepartmentMapper;
import com.tockm.server.pojo.Department;
import com.tockm.server.service.IDepartmentService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author tockm
 * @since 2021-12-31
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

}
