package com.demo;

import com.demo.model.ProcessQuery;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProcessQueryTests {

    @Test
    void defaultsAndBoundsAreApplied() {
        ProcessQuery query = new ProcessQuery(null, "unsupported", "unknown", 120);

        assertEquals("", query.getQ());
        assertEquals("pid", query.getSort());
        assertEquals("asc", query.getDir());
        assertEquals(60, query.getRefresh());
    }

    @Test
    void nextDirTogglesForCurrentSort() {
        ProcessQuery query = new ProcessQuery("java", "command", "asc", 5);

        assertEquals("desc", query.nextDir("command"));
        assertEquals("asc", query.nextDir("pid"));
    }
}

