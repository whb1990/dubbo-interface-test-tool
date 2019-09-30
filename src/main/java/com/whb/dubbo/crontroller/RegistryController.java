package com.whb.dubbo.crontroller;

import com.alibaba.fastjson.JSON;
import com.whb.dubbo.dto.ResultDTO;
import com.whb.dubbo.model.RegistryModel;
import com.whb.dubbo.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 注册中心管理控制器
 */
@RestController
@RequestMapping("/dubbo/zk")
@Slf4j
public class RegistryController {

    @Autowired
    private ConfigService configService;

    /**
     * 获取zk列表
     *
     * @return
     */
    @RequestMapping("/doListZk")
    public String doListZk() {
        log.info("RegistryController.doListZk()");
        String result;
        try {
            List<RegistryModel> models = configService.listRegistry();
            result = JSON.toJSONString(models);
        } catch (Exception e) {
            result = "[]";
        }
        return result;
    }

    /**
     * 获取注册中心地址列表
     *
     * @return
     */
    @RequestMapping("/doListRegistry")
    public ResultDTO<Object> doListRegistry() {
        log.info("RegistryController.doListRegistry()");
        ResultDTO<Object> resultDTO = new ResultDTO<>();
        try {
            List<RegistryModel> models = configService.listRegistry();
            resultDTO.setData(models);
            resultDTO.setSuccess(true);
        } catch (Exception e) {
            resultDTO = ResultDTO.createExceptionResult("occur an error when list registry address : ", e, Object.class);
        }
        return resultDTO;
    }

    /**
     * 新增注册中心
     *
     * @param dto
     * @return
     */
    @RequestMapping("/addRegistry")
    public ResultDTO<RegistryModel> addRegistry(@NotNull RegistryModel dto) {
        log.info("RegistryController.addRegistry({})", JSON.toJSONString(dto));
        ResultDTO<RegistryModel> resultDTO;
        try {
            resultDTO = configService.addRegistry(dto);
        } catch (Exception e) {
            resultDTO = ResultDTO.createExceptionResult(e, RegistryModel.class);
        }
        return resultDTO;
    }

    /**
     * 删除注册中心
     *
     * @param dto
     * @return
     */
    @RequestMapping("/delRegistry")
    public ResultDTO<RegistryModel> delRegistry(@NotNull RegistryModel dto) {
        log.info("RegistryController.delRegistry({})", JSON.toJSONString(dto));
        ResultDTO<RegistryModel> resultDTO;
        try {
            resultDTO = configService.delRegistry(dto);
        } catch (Exception e) {
            resultDTO = ResultDTO.createExceptionResult(e, RegistryModel.class);
        }
        return resultDTO;
    }
}
