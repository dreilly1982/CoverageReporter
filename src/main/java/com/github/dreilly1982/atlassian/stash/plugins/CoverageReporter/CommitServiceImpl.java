package com.github.dreilly1982.atlassian.stash.plugins.CoverageReporter;

import com.atlassian.activeobjects.external.ActiveObjects;
import net.java.ao.Query;

import static com.google.common.base.Preconditions.checkNotNull;

public class CommitServiceImpl implements CommitService {
    private final ActiveObjects ao;

    public CommitServiceImpl(ActiveObjects ao) {
        this.ao = checkNotNull(ao);
    }

    @Override
    public Commit setCoverage(String commitHash, Float coverage) {
        final Commit commit = ao.create(Commit.class);
        commit.setCommitHash(commitHash);
        commit.setCoverage(coverage);
        commit.save();
        return commit;
    }

    @Override
    public Float getCoverage(String commitHash) {
        Commit commit = ao.find(Commit.class, Query.select().where("COMMIT_HASH = ?", commitHash))[0];
        Float coverage = commit == null ? 0 : commit.getCoverage();
        return coverage;
    }
}
