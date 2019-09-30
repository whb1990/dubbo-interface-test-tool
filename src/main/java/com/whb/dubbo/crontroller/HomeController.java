package com.whb.dubbo.crontroller;

import com.whb.dubbo.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dubbo/home")
public class HomeController {

    @Autowired
    private MenuService menuService;

    /**
     * 首页
     *
     * @param model
     * @return
     */
    @RequestMapping("/index")
    public String index(Model model) {
        return index("f16001100", model);
    }

    @RequestMapping("/main")
    public String index(String mid, Model model) {
        Integer menuId = Integer.valueOf(mid.substring(1));
        String path = menuService.getUrl(menuId);
        String menuHtml = menuService.getHtml();
        model.addAttribute("mid", mid);
        model.addAttribute("menuHtml", menuHtml);
        return path;
    }

    /**
     * 普通模式
     *
     * @return
     */
    @RequestMapping("/normalCnt")
    public String openNormalPage() {
        return "/pages/v3/normalCnt.html";
    }

    /**
     * 用例模式
     *
     * @return
     */
    @RequestMapping("/caseCnt")
    public String openCasePage() {
        return "/pages/v3/caseCnt.html";
    }

    /**
     * 极简模式
     *
     * @return
     */
    @RequestMapping("/easyCnt")
    public String openEasyPage() {
        return "/pages/v3/easyCnt.html";
    }

    /**
     * 增加依赖
     *
     * @return
     */
    @RequestMapping("/addJar")
    public String openAddJarPage() {
        return "/pages/v3/addJar.html";
    }

    /**
     * 依赖列表
     *
     * @return
     */
    @RequestMapping("/listJar")
    public String openListJarPage() {
        return "/pages/v3/listJar.html";
    }

    /**
     * 编辑依赖
     *
     * @return
     */
    @RequestMapping("/editPom")
    public String openEditPomPage() {
        return "/pages/v3/editPom.html";
    }

    /**
     * 注册中心管理
     *
     * @return
     */
    @RequestMapping("/listZk")
    public String openListZkPage() {
        return "/pages/v3/listZk.html";
    }

    /**
     * 系统配置
     *
     * @return
     */
    @RequestMapping("/sys")
    public String openSysPage() {
        return "/pages/v3/sys.html";
    }
}
