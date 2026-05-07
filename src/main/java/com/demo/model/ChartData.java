package com.demo.model;

import java.util.List;

public record ChartData(
        // Alive vs Not Alive (doughnut)
        List<String> statusLabels,
        List<Long> statusValues,

        // Top 10 users by process count (horizontal bar)
        List<String> userLabels,
        List<Long> userValues,

        // Top 10 commands by process count (bar)
        List<String> commandLabels,
        List<Long> commandValues
) {
}

