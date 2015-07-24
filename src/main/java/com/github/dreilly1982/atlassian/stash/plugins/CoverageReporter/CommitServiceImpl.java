/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Don Reilly<dreilly1982@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.dreilly1982.atlassian.stash.plugins.CoverageReporter;

import com.atlassian.activeobjects.external.ActiveObjects;
import net.java.ao.DBParam;
import net.java.ao.Query;
import static com.github.dreilly1982.atlassian.stash.plugins.CoverageReporter.Helpers.firstOf;

import static com.google.common.base.Preconditions.checkNotNull;

public class CommitServiceImpl implements CommitService {
    private final ActiveObjects ao;

    public CommitServiceImpl(ActiveObjects ao) {
        this.ao = checkNotNull(ao);
    }

    @Override
    public Commit setCoverage(String commitHash, String coverage) {
        Commit commit = firstOf(ao.find(Commit.class, Query.select().where("COMMIT_HASH = ?", commitHash)));
        if (commit == null) {
            commit = ao.create(Commit.class, new DBParam("COMMIT_HASH", commitHash));
        } else {
            commit.setCommitHash(commitHash);
        }
        commit.setCoverage(coverage);
        commit.save();
        return commit;
    }

    @Override
    public String getCoverage(String commitHash) {
        Commit commit = firstOf(ao.find(Commit.class, Query.select().where("COMMIT_HASH = ?", commitHash)));
        String coverage = commit == null ? "0" : commit.getCoverage();
        return coverage;
    }
}
