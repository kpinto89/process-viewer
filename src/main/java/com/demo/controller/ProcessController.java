package com.demo.controller;

import com.demo.service.ProcessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProcessController {

    private final ProcessService processService;

    public ProcessController(ProcessService processService) {
        this.processService = processService;
    }

    @GetMapping("/")
    public String processes(Model model) {
        model.addAttribute("processes", processService.listProcesses());
        return "processes";
    }
}

