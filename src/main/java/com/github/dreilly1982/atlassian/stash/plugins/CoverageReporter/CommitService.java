package com.github.dreilly1982.atlassian.stash.plugins.CoverageReporter;

import com.atlassian.activeobjects.tx.Transactional;
import java.util.List;

@Transactional
public interface CommitService {
    Commit setCoverage(String commitHash, Float coverage);
    Float getCoverage(String commitHash);
}
