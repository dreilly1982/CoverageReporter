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

package ut.com.github.dreilly1982.atlassian.stash.plugins.CoverageReporter;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.activeobjects.test.TestActiveObjects;
import com.github.dreilly1982.atlassian.stash.plugins.CoverageReporter.CommitAPI;
import com.github.dreilly1982.atlassian.stash.plugins.CoverageReporter.CommitsModel;

import net.java.ao.DBParam;
import org.junit.Before;
import org.junit.Test;
import javax.ws.rs.core.Response;

import static org.junit.Assert.*;

import net.java.ao.EntityManager;
import net.java.ao.test.jdbc.Data;
import net.java.ao.test.jdbc.DatabaseUpdater;
import net.java.ao.test.junit.ActiveObjectsJUnitRunner;

import com.github.dreilly1982.atlassian.stash.plugins.CoverageReporter.Commit;
import org.junit.runner.RunWith;

@RunWith(ActiveObjectsJUnitRunner.class)
@Data(CommitAPITest.CommitAPITestDatabaseUpdater.class)
public class CommitAPITest {

    private EntityManager entityManager;
    private ActiveObjects ao;
    private CommitAPI resource;

    private static final String COMMIT_HASH = "FirstCommit";
    private static final String COVERAGE = "12.3";

    @Before
    public void setUp() throws Exception {
        assertNotNull(entityManager);
        ao = new TestActiveObjects(entityManager);
        resource = new CommitAPI(ao);
    }

    @Test
    public void testGetCoverage() throws Exception {
        Response response = resource.getCoverage(COMMIT_HASH);
        final CommitsModel message = (CommitsModel) response.getEntity();
        String coverage = message.getCoverage();
        assertEquals(COVERAGE, coverage);
    }

    @Test
    public void testGetCoverage_null() throws Exception {
        Response response = resource.getCoverage("non_existent_commit");
        final CommitsModel message = (CommitsModel) response.getEntity();
        String coverage = message.getCoverage();
        assertEquals("0", coverage);
    }

    @Test
    public void testSetCoverage() throws Exception {
        final String testCommitHash = "TestCommit";
        final String testCoverage = "12.5";

        final CommitsModel commit = new CommitsModel(testCommitHash, testCoverage);
        Response response = resource.setCoverage(commit);
        assertEquals(Response.status(Response.Status.CREATED).build().getStatus(), response.getStatus());

        response = resource.getCoverage(testCommitHash);
        final CommitsModel message = (CommitsModel) response.getEntity();
        String coverage = message.getCoverage();
        assertEquals(testCoverage, coverage);
    }

    @Test
    public void testSetCoverage_overwrite() throws Exception {
        final String testCoverage = "13.25";

        Response response = resource.getCoverage(COMMIT_HASH);
        CommitsModel commit = (CommitsModel) response.getEntity();
        String coverage = commit.getCoverage();
        assertEquals(COVERAGE, coverage);

        commit = new CommitsModel(COMMIT_HASH, testCoverage);
        response = resource.setCoverage(commit);
        assertEquals(Response.status(Response.Status.CREATED).build().getStatus(), response.getStatus());

        response = resource.getCoverage(COMMIT_HASH);
        commit = (CommitsModel) response.getEntity();
        coverage = commit.getCoverage();
        assertEquals(testCoverage, coverage);
    }

    public static class CommitAPITestDatabaseUpdater implements DatabaseUpdater {
        @Override
        public void update(final EntityManager em) throws Exception {
            em.migrate(Commit.class);
            Commit commit = em.create(Commit.class, new DBParam("COMMIT_HASH", COMMIT_HASH));
            commit.setCoverage(COVERAGE);
            commit.save();
        }
    }
}