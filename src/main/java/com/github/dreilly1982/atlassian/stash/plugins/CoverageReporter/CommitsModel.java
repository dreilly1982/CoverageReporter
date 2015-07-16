package com.github.dreilly1982.atlassian.stash.plugins.CoverageReporter;

import com.atlassian.activeobjects.external.ActiveObjects;
import static com.google.common.base.Preconditions.checkNotNull;

import javax.xml.bind.annotation.*;
@XmlRootElement(name = "commit")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommitsModel {

    @XmlElement(name = "commitHash")
    private String commitHash;

    @XmlElement(name = "coverage")
    private Float coverage;

    public CommitsModel() {
    }

    public CommitsModel(String commitHash) {
        this.commitHash = commitHash;
    }

    public void getCoverage() {    }

    public void setMessage(String message) {
        this.message = message;
    }
}