package com.whb.dubbo.crontroller;

import com.alibaba.fastjson.JSON;
import com.whb.dubbo.dto.CaseModelDTO;
import com.whb.dubbo.dto.ResultDTO;
import com.whb.dubbo.model.CaseModel;
import com.whb.dubbo.service.CaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用例控制器
 */
@RestController
@RequestMapping("/dubbo/case")
@Slf4j
public class CaseController {

    @Autowired
    private CaseService caseService;


    @RequestMapping("/doSave")
    public ResultDTO<CaseModel> doSave(@NotNull CaseModelDTO dto) {
        log.info("CaseController.doSave({})", JSON.toJSONString(dto));
        ResultDTO<CaseModel> resultDTO;
        try {
            CaseModel model = new CaseModel();
            BeanUtils.copyProperties(dto, model);
            resultDTO = caseService.save(model);
        } catch (Exception e) {
            resultDTO = ResultDTO.createExceptionResult(e, CaseModel.class);
        }
        return resultDTO;
    }

    @RequestMapping("/doList")
    public String doList(CaseModelDTO dto) {

        log.info("CaseController.doList({})", JSON.toJSONString(dto));
        try {
            List<Object> list = caseService.listAll();
            List<CaseModel> ret = list.stream().map(l -> {
                CaseModel model = new CaseModel();
                BeanUtils.copyProperties(l, model);
                return model;
            }).collect(Collectors.toList());

            return JSON.toJSONString(ret);
        } catch (Exception e) {
            return "[]";
        }
    }
}
