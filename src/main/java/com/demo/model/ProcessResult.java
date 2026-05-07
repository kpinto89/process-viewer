package com.demo.model;

import java.util.List;

public record ProcessResult(ProcessQuery query, ProcessSummary summary, List<ProcessInfo> processes) {
}

