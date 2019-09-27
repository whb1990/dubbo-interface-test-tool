package com.whb.dubbo.service.impl;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.whb.dubbo.cache.MethodCaches;
import com.whb.dubbo.cache.RedisResolver;
import com.whb.dubbo.cache.UrlCaches;
import com.whb.dubbo.context.Constant;
import com.whb.dubbo.dto.ResultDTO;
import com.whb.dubbo.exception.RRException;
import com.whb.dubbo.model.CaseModel;
import com.whb.dubbo.service.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Joey
 * @date 2018/6/29 15:20
 */
@Service("caseService")
public class CaseServiceImpl implements CaseService {

    @Autowired
    private RedisResolver redisResolver;

    private static final AtomicLong counter = new AtomicLong();

    /**
     * save the case.
     *
     * @param model
     * @return
     */
    @Override
    public ResultDTO<CaseModel> save(@NotNull CaseModel model) {

        if (StringUtils.isEmpty(model.getProviderKey())) {
            throw new RRException("获取不到提供者！");
        }
        if (StringUtils.isEmpty(model.getMethodKey())) {
            throw new RRException("获取不到方法！");
        }

        model.setAddress(UrlCaches.get(model.getProviderKey()).getUrl().getAddress());
        model.setInterfaceName(UrlCaches.get(model.getProviderKey()).getUrl().getParameter(Constants.INTERFACE_KEY ));
        model.setMethodText(MethodCaches.get(model.getMethodKey()).getMethodText());
        model.setCaseId(counter.getAndAdd(1));
        model.setInsertTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        redisResolver.rPush(Constant.DOE_CASE_KEY, model);

        // TODO
        // save to db.

        return ResultDTO.createSuccessResult("SUCCESS", model, CaseModel.class);
    }

    /**
     * list all case.
     *
     * @return
     */
    @Override
    public List<Object> listAll() {

        List<Object> list = redisResolver.lGet(Constant.DOE_CASE_KEY, 0, -1);

        if (CollectionUtils.isEmpty(list)) {

            // TODO
            // get from db and put them to cache.

        }

        return list;
    }
}
