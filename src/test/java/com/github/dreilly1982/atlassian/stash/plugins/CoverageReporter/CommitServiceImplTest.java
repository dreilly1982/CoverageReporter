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
import com.atlassian.activeobjects.test.TestActiveObjects;
import net.java.ao.EntityManager;
import net.java.ao.test.jdbc.Data;
import net.java.ao.test.jdbc.DatabaseUpdater;
import net.java.ao.test.junit.ActiveObjectsJUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(ActiveObjectsJUnitRunner.class)
@Data(CommitServiceImplTest.CommitServiceImplTestDatabaseUpdater.class)
public class CommitServiceImplTest {
    private EntityManager entityManager;
    private CommitServiceImpl commitService;
    private ActiveObjects ao;

    private static final String COMMIT_HASH = "FirstCommit";
    private static final String COVERAGE = "12.3";

    @Before
    public void setUp() throws Exception {
        assertNotNull(entityManager);
        ao = new TestActiveObjects(entityManager);
        commitService = new CommitServiceImpl(new TestActiveObjects(entityManager));
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testSetCoverage() throws Exception {
        final String coverage = "32.3";
        final String commitHash = "supercommithash";
        ao.migrate(Commit.class);
        assertEquals(1, ao.find(Commit.class).length);

        final Commit commit = commitService.setCoverage(commitHash, coverage);
        assertFalse(commit.getID() == 0);

        ao.flushAll();

        final Commit[] commits = ao.find(Commit.class);
        assertEquals(2, commits.length);
        assertEquals(coverage, commits[1].getCoverage());
    }

    @Test
    public void testGetCoverage() throws Exception {
        final String coverage = commitService.getCoverage(COMMIT_HASH);
        assertEquals(COVERAGE, coverage);
    }

    public static class CommitServiceImplTestDatabaseUpdater implements DatabaseUpdater {
        @Override
        public void update(EntityManager em) throws Exception {
            em.migrate(Commit.class);

            final Commit commit = em.create(Commit.class);
            commit.setCommitHash(COMMIT_HASH);
            commit.setCoverage(COVERAGE);
            commit.save();
        }
    }
}