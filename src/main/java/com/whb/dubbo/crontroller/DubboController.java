package com.whb.dubbo.crontroller;

import com.alibaba.fastjson.JSON;
import com.whb.dubbo.dto.ConnectDTO;
import com.whb.dubbo.dto.MethodModelDTO;
import com.whb.dubbo.dto.ResultDTO;
import com.whb.dubbo.dto.UrlModelDTO;
import com.whb.dubbo.model.ServiceModel;
import com.whb.dubbo.service.ClassService;
import com.whb.dubbo.service.ConnectService;
import com.whb.dubbo.service.TelnetService;
import com.whb.dubbo.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/dubbo/dubbo")
@Slf4j
public class DubboController {

    @Autowired
    private ConnectService connectService;

    @Autowired
    private ClassService classService;

    @Autowired
    private TelnetService telnetService;

    /**
     * 极简模式的发送
     *
     * @param dto
     * @return
     */
    @RequestMapping("/doSendWithTelnet")
    public ResultDTO<String> doSendWithTelnet(@NotNull ConnectDTO dto) {
        log.info("DubboController.doSendWithTelnet({})", JSON.toJSONString(dto));
        ResultDTO<String> resultDTO;
        try {
            resultDTO = telnetService.send(dto);
        } catch (Exception e) {
            resultDTO = ResultDTO.createExceptionResult(e, String.class);
        }
        return resultDTO;
    }

    /**
     * 普通模式的发送
     *
     * @param dto
     * @return
     */
    @RequestMapping("/doSend")
    public ResultDTO<String> doSend(@NotNull ConnectDTO dto) {
        log.info("DubboController.doSend({})", JSON.toJSONString(dto));
        ResultDTO<String> resultDTO;
        try {
            resultDTO = connectService.send(dto);
        } catch (Exception e) {
            resultDTO = ResultDTO.createExceptionResult(e, String.class);
        }
        return resultDTO;
    }

    /**
     * 普通模式--获取参数列表
     *
     * @param dto
     * @return
     */
    @RequestMapping("/doListParams")
    public ResultDTO<String> doListParams(@NotNull MethodModelDTO dto) {
        log.info("DubboController.doListParams({})", JSON.toJSONString(dto));
        ResultDTO<String> resultDTO;
        try {
            resultDTO = classService.generateMethodParamsJsonString(dto);
        } catch (Exception e) {
            resultDTO = ResultDTO.createExceptionResult(e, String.class);
        }
        return resultDTO;
    }

    /**
     * 普通模式--获取方法列表
     *
     * @param dto
     * @return
     */
    @RequestMapping("/doListMethods")
    public ResultDTO<Object> doListMethods(@NotNull ConnectDTO dto) {
        log.info("DubboController.doListMethods({})", dto.getProviderKey());
        ResultDTO<Object> resultDTO = new ResultDTO<>();
        try {
            List<MethodModelDTO> models = classService.listMethods(dto);
            if (CollectionUtils.isEmpty(models)) {
                resultDTO = ResultDTO.createErrorResult(StringUtil.format("no methods for {}.",
                        dto.getServiceName()), Object.class);
            } else {
                log.info("methods: {}", JSON.toJSONString(models));
                resultDTO.setData(models);
                resultDTO.setSuccess(true);
            }
        } catch (Exception e) {
            resultDTO = ResultDTO.createExceptionResult(e, Object.class);
            resultDTO.setMsg("occur an error when get methods : " + resultDTO.getMsg());
        }
        return resultDTO;
    }

    /**
     * 普通模式--获取提供者列表
     *
     * @param dto
     * @return
     */
    @RequestMapping("/doListProviders")
    public ResultDTO<Object> doListProviders(@NotNull ConnectDTO dto) {
        log.info("DubboController.doListProviders({} {} {})", dto.getServiceName(), dto.getVersion(), dto.getGroup());
        ResultDTO<Object> resultDTO = new ResultDTO<>();
        try {
            List<UrlModelDTO> models = connectService.listProviders(dto);
            if (CollectionUtils.isEmpty(models)) {
                resultDTO = ResultDTO.createErrorResult(StringUtil.format("no provider for {} in this zookeeper registry.",
                        dto.getServiceName()), Object.class);
            } else {
                log.info("providers: {}", JSON.toJSONString(models));
                resultDTO.setData(models);
                resultDTO.setSuccess(true);
            }
        } catch (Exception e) {
            resultDTO = ResultDTO.createExceptionResult(e, Object.class);
            resultDTO.setMsg("occur an error when get provider : " + resultDTO.getMsg());
        }
        return resultDTO;
    }

    /**
     * 普通模式--连接到zk
     *
     * @param conn
     * @return
     */
    @RequestMapping("/doConnect")
    public ResultDTO<Object> doConnect(@NotNull String conn) {
        log.debug("DubboController.doConnect({})", conn);
        ResultDTO<Object> resultDTO = new ResultDTO<>();
        try {
            List<ServiceModel> models = connectService.connect(conn);
            if (CollectionUtils.isEmpty(models)) {
                resultDTO = ResultDTO.createErrorResult("no provider for this this zookeeper registry.", Object.class);
            } else {
                resultDTO.setData(models);
                resultDTO.setSuccess(true);
            }
        } catch (Exception e) {
            resultDTO = ResultDTO.createExceptionResult(e, Object.class);
        }
        return resultDTO;
    }
}
