package com.whb.dubbo.crontroller;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.whb.dubbo.dto.PomDTO;
import com.whb.dubbo.dto.ResultDTO;
import com.whb.dubbo.exception.RRException;
import com.whb.dubbo.model.PomModel;
import com.whb.dubbo.service.PomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dubbo/pom")
@Slf4j
public class PomController {

    @Autowired
    private PomService pomService;

    /**
     * load jars.
     *
     * @return
     */
    @RequestMapping("/doLoad")
    public ResultDTO<String> doLoad() {
        log.info("PomController.doLoad");
        ResultDTO<String> resultDTO = null;
        try {
            resultDTO = pomService.loadJars("");
        } catch (Exception e) {
            resultDTO = ResultDTO.createExceptionResult(e, String.class);
        }
        return resultDTO;

    }

    /**
     * parse the upload content to pom model and fork another process to invoke cmd/shell command to download the jars at background.
     *
     * @param pom
     * @return
     */
    @RequestMapping("/doParse")
    public ResultDTO<PomDTO> doParse(String pom) {
        log.info("PomController.doParse({})", pom);
        ResultDTO<PomDTO> resultDTO;
        try {
            if (StringUtils.isEmpty(pom)) {
                throw new RRException("the pom content can't be blank.");
            }
            // convert the pom
            pom = org.apache.commons.text.StringEscapeUtils.unescapeXml(pom);
            log.debug("pom after escape was {}", pom);
            PomDTO dto = new PomDTO();
            dto.setPom(pom);
            resultDTO = pomService.invoke(dto);
        } catch (Exception e) {
            resultDTO = ResultDTO.createExceptionResult(e, PomDTO.class);
        }
        return resultDTO;
    }

    /**
     * invoke the mvn command to download the jars again.
     *
     * @return
     */
    @RequestMapping("/doReparse")
    public ResultDTO<PomDTO> doReparse() {
        log.info("PomController.doReparse({})");
        ResultDTO<PomDTO> resultDTO;
        try {
            resultDTO = pomService.invoke();
        } catch (Exception e) {
            resultDTO = ResultDTO.createExceptionResult(e, PomDTO.class);
        }
        return resultDTO;
    }

    @RequestMapping("/doMsg")
    public ResultDTO<String> getRealTimeMsg(String requestId) {
        log.info("PomController.getRealTimeMsg({})", requestId);
        ResultDTO<String> resultDTO;
        try {
            if (StringUtils.isEmpty(requestId)) {
                resultDTO = ResultDTO.createErrorResult("ERROR", String.class);
            } else {
                resultDTO = pomService.getRealTimeMsg(requestId);
            }
        } catch (Exception e) {
            resultDTO = ResultDTO.createExceptionResult(e, String.class);
        }
        return resultDTO;
    }

    @RequestMapping("/doListJars")
    public String doListJars(PomDTO dto) {
        log.info("PomController.doListJars({})", JSON.toJSONString(dto));
        String result;
        try {
            List<PomModel> models = pomService.listJars(dto);
            result = JSON.toJSONString(models);
        } catch (Exception e) {
            result = "[]";
        }
        return result;
    }

    @RequestMapping("/doLoadPomFile")
    public ResultDTO<String> doLoadPomFile() {
        log.info("PomController.doLoadPomFile");
        ResultDTO<String> resultDTO;
        try {
            String content = pomService.loadPomFile(null);
            resultDTO = ResultDTO.handleSuccess("SUCCESS", content);
        } catch (Exception e) {
            resultDTO = ResultDTO.createExceptionResult(e, String.class);
        }
        return resultDTO;
    }

    @RequestMapping("/doOverridePomFile")
    public ResultDTO<Boolean> doOverridePomFile(String content) {
        log.info("PomController.doOverridePomFile");
        ResultDTO<Boolean> resultDTO;
        try {
            Boolean flag = pomService.overridePomFile("", content);
            resultDTO = ResultDTO.handleSuccess("SUCCESS", flag);
        } catch (Exception e) {
            resultDTO = ResultDTO.createExceptionResult(e, Boolean.class);
        }
        return resultDTO;
    }
}
