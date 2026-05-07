package com.demo;

import com.demo.model.ProcessInfo;
import com.demo.model.ProcessQuery;
import com.demo.model.ProcessResult;
import com.demo.service.ProcessService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ProcessViewerApplicationTests {

    @Autowired
    private ProcessService processService;

    @Test
    void contextLoads() {
    }

    @Test
    void listProcessesReturnsCollection() {
        List<ProcessInfo> processes = processService.listProcesses();
        assertNotNull(processes);
    }

    @Test
    void getProcessesReturnsSummaryAndList() {
        ProcessResult result = processService.getProcesses(new ProcessQuery("", "pid", "asc", 5));
        assertNotNull(result);
        assertNotNull(result.summary());
        assertNotNull(result.processes());
    }
}

