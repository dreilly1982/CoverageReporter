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
import com.atlassian.plugin.PluginParseException;
import com.atlassian.plugin.web.ContextProvider;
import com.google.common.collect.ImmutableMap;
import com.atlassian.stash.pull.PullRequest;
import java.util.Map;
import static com.google.common.base.Preconditions.checkNotNull;

public class CoverageContextProvider implements ContextProvider {
    private CommitService commitService;
    private final ActiveObjects ao;

    public CoverageContextProvider(ActiveObjects ao) {
        this.ao = checkNotNull(ao);
        this.commitService = new CommitServiceImpl(this.ao);
    }

    @Override
    public void init(Map<String, String> params) throws PluginParseException {}

    @Override
    public Map<String, Object> getContextMap(Map<String, Object> context) {
        return ImmutableMap.<String, Object>builder()
                .put("coverage", commitService.getCoverage(((PullRequest) context.get("pullRequest")).getFromRef().getLatestCommit()))
                .build();
    }
}
