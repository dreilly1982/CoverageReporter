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

import com.atlassian.plugins.rest.common.security.AnonymousAllowed;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static com.github.dreilly1982.atlassian.stash.plugins.CoverageReporter.Helpers.firstOf;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.sal.api.transaction.TransactionCallback;

import java.util.ArrayList;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A resource of message.
 */
@Path("/commits")
public class CommitAPI {
    private final ActiveObjects ao;

    CommitAPI(ActiveObjects ao) {
        this.ao = checkNotNull(ao);
    }

    @GET
    @AnonymousAllowed
    @Path("/{commitHash}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getMessage(@PathParam("commitHash") final String commitHash) {
        Commit commit = firstOf(ao.find(Commit.class, "\"COMMIT_HASH\" = ?", commitHash));
        String coverage;
        if (commit != null) {
            coverage = commit.getCoverage();
        } else {
            coverage = "0";
        }
        return Response.ok(new CommitsModel(commitHash, coverage)).build();
    }

    @POST
    @AnonymousAllowed
    @Path("/{commitHash}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response setCoverage(@PathParam("commitHash") final String commitHash, final String coverage) {
        return ao.executeInTransaction(new TransactionCallback<Response>() {
            @Override
            public Response doInTransaction() {
                Commit existing = firstOf(ao.find(Commit.class, "\"COMMIT_HASH\" = ?", commitHash));
                if (existing != null) {
                    ao.delete(existing);
                }
                Commit commit = ao.create(Commit.class);
                commit.setCommitHash(commitHash);
                commit.setCoverage(coverage);
                commit.save();
                return Response.status(Response.Status.CREATED).build();
            }
        });
    }
}