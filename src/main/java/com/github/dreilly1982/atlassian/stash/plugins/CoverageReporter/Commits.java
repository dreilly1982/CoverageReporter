package com.github.dreilly1982.atlassian.stash.plugins.CoverageReporter;

import com.atlassian.plugins.rest.common.security.AnonymousAllowed;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * A resource of message.
 */
@Path("/commits/{commitHash}")
public class Commits {

    @GET
    @AnonymousAllowed
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getMessage() {

        return Response.ok(new CommitsModel("Hello World")).build();
    }
}