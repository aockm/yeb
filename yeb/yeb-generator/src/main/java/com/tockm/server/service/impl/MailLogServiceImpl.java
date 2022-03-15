package com.tockm.server.service.impl;

import com.tockm.server.pojo.MailLog;
import com.tockm.server.mapper.MailLogMapper;
import com.tockm.server.service.IMailLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 邮件日志 服务实现类
 * </p>
 *
 * @author tockm
 * @since 2021-12-31
 */
@Service
public class MailLogServiceImpl extends ServiceImpl<MailLogMapper, MailLog> implements IMailLogService {

}
