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

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.atlassian.activeobjects.external.ActiveObjects;

import static com.google.common.base.Preconditions.checkNotNull;

@Path("/commits")
public class CommitAPI {
    private final ActiveObjects ao;
    private CommitService commitService;

    public CommitAPI(ActiveObjects ao) {
        this.ao = checkNotNull(ao);
        this.commitService = new CommitServiceImpl(ao);
    }

    @GET
    @AnonymousAllowed
    @Path("/{commitHash}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getCoverage(@PathParam("commitHash") final String commitHash) {
        String coverage = commitService.getCoverage(commitHash);
        return Response.ok(new CommitsModel(commitHash, coverage)).build();
    }

    @POST
    @AnonymousAllowed
    @Path("/submit")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setCoverage(final CommitsModel commit) {
        commitService.setCoverage(commit.getCommitHash(), commit.getCoverage());
        return Response.status(Response.Status.CREATED).build();
    }
}