package com.tockm.server.service.impl;

import com.tockm.server.pojo.Position;
import com.tockm.server.mapper.PositionMapper;
import com.tockm.server.service.IPositionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 职位 服务实现类
 * </p>
 *
 * @author tockm
 * @since 2021-12-31
 */
@Service
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements IPositionService {

}
