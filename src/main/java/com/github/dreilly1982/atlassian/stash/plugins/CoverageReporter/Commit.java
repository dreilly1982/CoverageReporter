package com.github.dreilly1982.atlassian.stash.plugins.CoverageReporter;

import net.java.ao.Entity;
import net.java.ao.Preload;

import java.util.Map;

@Preload
public interface Commit extends Entity {
    String getCommitHash();
    void setCommitHash(String commitHash);
    Float getCoverage();
    void setCoverage(Float coverage);
}
