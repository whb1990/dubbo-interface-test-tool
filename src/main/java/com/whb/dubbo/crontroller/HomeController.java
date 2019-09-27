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


    @RequestMapping("/normalCnt")
    public String openNormalPage() {
        return "/pages/v3/normalCnt.html";
    }

    @RequestMapping("/caseCnt")
    public String openCasePage() {
        return "/pages/v3/caseCnt.html";
    }

    @RequestMapping("/easyCnt")
    public String openEasyPage() {
        return "/pages/v3/easyCnt.html";
    }

    @RequestMapping("/addJar")
    public String openAddJarPage() {
        return "/pages/v3/addJar.html";
    }

    @RequestMapping("/listJar")
    public String openListJarPage() {
        return "/pages/v3/listJar.html";
    }

    @RequestMapping("/editPom")
    public String openEditPomPage() {
        return "/pages/v3/editPom.html";
    }

    @RequestMapping("/listZk")
    public String openListZkPage() {
        return "/pages/v3/listZk.html";
    }

    @RequestMapping("/sys")
    public String openSysPage() {
        return "/pages/v3/sys.html";
    }
}
