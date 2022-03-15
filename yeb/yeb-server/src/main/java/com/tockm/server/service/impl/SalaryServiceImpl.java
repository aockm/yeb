package com.tockm.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tockm.server.mapper.SalaryMapper;
import com.tockm.server.pojo.Salary;
import com.tockm.server.service.ISalaryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 工资表 服务实现类
 * </p>
 *
 * @author tockm
 * @since 2021-12-31
 */
@Service
public class SalaryServiceImpl extends ServiceImpl<SalaryMapper, Salary> implements ISalaryService {

}
