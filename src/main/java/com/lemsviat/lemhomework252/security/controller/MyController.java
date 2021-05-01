package com.lemsviat.lemhomework252.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyController {
    @GetMapping("/")
    public String getInfoForUsers() {
       return "view_for_all_users";
    }

    @GetMapping("/moderators_info")
    public String getInfoForModerators() {
        return "view_for_moderators";
    }

    @GetMapping("/admins_info")
    public String getInfoForAdmins() {
        return "view_for_admins";
    }

    @GetMapping("/error")
    public String getError() {
        return "error";
    }

}
