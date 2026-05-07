package com.demo.controller;

import com.demo.model.ProcessQuery;
import com.demo.model.ProcessResult;
import com.demo.service.ProcessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProcessController {

    private final ProcessService processService;

    public ProcessController(ProcessService processService) {
        this.processService = processService;
    }

    @GetMapping("/")
    public String processes(
            @RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "pid") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            @RequestParam(defaultValue = "5") Integer refresh,
            Model model
    ) {
        ProcessQuery query = new ProcessQuery(q, sort, dir, refresh);
        ProcessResult result = processService.getProcesses(query);

        model.addAttribute("processes", result.processes());
        model.addAttribute("summary", result.summary());
        model.addAttribute("query", result.query());
        model.addAttribute("chartData", result.chartData());
        return "processes";
    }
}

