package com.game.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerController {

    @GetMapping(value = "/rest/players")
    public String getPlayersList(@RequestParam(name = "name", required = false) String name,
                                 @RequestParam(name = "title", required = false) String title,
                                 @RequestParam(name = "race", required = false) String race,
                                 @RequestParam(name = "profession", required = false) String profession,
                                 @RequestParam(name = "after", required = false) String after,
                                 @RequestParam(name = "before", required = false) String before,
                                 @RequestParam(name = "banned", required = false) String banned,
                                 @RequestParam(name = "minExperience", required = false) String minExperience,
                                 @RequestParam(name = "maxExperience", required = false) String maxExperience,
                                 @RequestParam(name = "minLevel", required = false) String minLevel,
                                 @RequestParam(name = "maxLevel", required = false) String maxLevel,
                                 @RequestParam(name = "order", required = false, defaultValue = "ID") String order,
                                 @RequestParam(name = "pageNumber", required = false, defaultValue = "0") String pageNumber,
                                 @RequestParam(name = "pageSize", required = false, defaultValue = "3") String pageSize,
                                 Model model) {

        System.out.println("--------------getPlayersList----------------");
        System.out.println("name=" + name);
        System.out.println("title=" + title);
        System.out.println("race=" + race);
        System.out.println("profession=" + profession);
        System.out.println("after=" + after);
        System.out.println("before=" + before);
        System.out.println("banned=" + banned);
        System.out.println("minExperience=" + minExperience);
        System.out.println("maxExperience=" + maxExperience);
        System.out.println("minLevel=" + minLevel);
        System.out.println("maxLevel=" + maxLevel);
        System.out.println("order=" + order);
        System.out.println("pageNumber=" + pageNumber);
        System.out.println("pageSize=" + pageSize);
        System.out.println("------------------------------");

        return "index";
    }

    @GetMapping(value ="/rest/players/count")
    public String getPlayersCount(@RequestParam(name = "name", required = false) String name,
                                 @RequestParam(name = "title", required = false) String title,
                                 @RequestParam(name = "race", required = false) String race,
                                 @RequestParam(name = "profession", required = false) String profession,
                                 @RequestParam(name = "after", required = false) String after,
                                 @RequestParam(name = "before", required = false) String before,
                                 @RequestParam(name = "banned", required = false) String banned,
                                 @RequestParam(name = "minExperience", required = false) String minExperience,
                                 @RequestParam(name = "maxExperience", required = false) String maxExperience,
                                 @RequestParam(name = "minLevel", required = false) String minLevel,
                                 @RequestParam(name = "maxLevel", required = false) String maxLevel,
                                 Model model) {

        System.out.println("-------------------getPlayersCount------------------");
        System.out.println("name=" + name);
        System.out.println("title=" + title);
        System.out.println("race=" + race);
        System.out.println("profession=" + profession);
        System.out.println("after=" + after);
        System.out.println("before=" + before);
        System.out.println("banned=" + banned);
        System.out.println("minExperience=" + minExperience);
        System.out.println("maxExperience=" + maxExperience);
        System.out.println("minLevel=" + minLevel);
        System.out.println("maxLevel=" + maxLevel);
        System.out.println("------------------------------");

        return "index";
    }

    @RequestMapping(value ="/rest/players/{id}")
    public String getPlayer(@RequestParam(name = "id", required = true) String id,
                                  Model model) {

        System.out.println("----------------getPlayer---------------------");
        System.out.println("id=" + id);
        System.out.println("------------------------------");

        return "index";
    }
}
