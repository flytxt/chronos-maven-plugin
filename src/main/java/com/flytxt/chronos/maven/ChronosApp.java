/**
 * © Copyright 2015 Flytxt BV. ALL RIGHTS RESERVED.
 *
 * All rights, title and interest (including all intellectual property rights) in this software and any derivative works based upon or derived from this software
 * belongs exclusively to Flytxt BV. Access to this software is forbidden to anyone except current employees of Flytxt BV and its affiliated companies who have
 * executed non-disclosure agreements explicitly covering such access. While in the employment of Flytxt BV or its affiliate companies as the case may be,
 * employees may use this software internally, solely in the course of employment, for the sole purpose of developing new functionalities, features, procedures,
 * routines, customizations or derivative works, or for the purpose of providing maintenance or support for the software. Save as expressly permitted above,
 * no license or right thereto is hereby granted to anyone, either directly, by implication or otherwise. On the termination of employment, the license granted
 * to employee to access the software shall terminate and the software should be returned to the employer, without retaining any copies.
 *
 * This software is (i) proprietary to Flytxt BV; (ii) is of significant value to it; (iii) contains trade secrets of Flytxt BV; (iv) is not publicly available;
 * and (v) constitutes the confidential information of Flytxt BV. Any use, reproduction, modification, distribution, public performance or display of this software
 * or through the use of this software without the prior, express written consent of Flytxt BV is strictly prohibited and may be in violation of applicable laws.
 */
package com.flytxt.chronos.maven;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The ChronosApp class
 *
 * @author gazal
 *
 */
public class ChronosApp implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String command;

    private Integer retries;

    private Boolean shell;

    private Container container;

    private String cpus;

    private String mem;

    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(final String command) {
        this.command = command;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(final Integer retries) {
        this.retries = retries;
    }

    public Boolean getShell() {
        return shell;
    }

    public void setShell(final Boolean shell) {
        this.shell = shell;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(final Container container) {
        this.container = container;
    }

    public String getCpus() {
        return cpus;
    }

    public void setCpus(final String cpus) {
        this.cpus = cpus;
    }

    public String getMem() {
        return mem;
    }

    public void setMem(final String mem) {
        this.mem = mem;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(final String name, final Object value) {
        this.additionalProperties.put(name, value);
    }

    public class Container {

        private String type;

        private String image;

        private Boolean forcePullImage;

        private String network;

        @JsonIgnore
        private final Map<String, Object> additionalProperties = new HashMap<>();

        public String getType() {
            return type;
        }

        public void setType(final String type) {
            this.type = type;
        }

        public String getImage() {
            return image;
        }

        public void setImage(final String image) {
            this.image = image;
        }

        public Boolean getForcePullImage() {
            return forcePullImage;
        }

        public void setForcePullImage(final Boolean forcePullImage) {
            this.forcePullImage = forcePullImage;
        }

        public String getNetwork() {
            return network;
        }

        public void setNetwork(final String network) {
            this.network = network;
        }

        @JsonAnyGetter
        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        @JsonAnySetter
        public void setAdditionalProperty(final String name, final Object value) {
            this.additionalProperties.put(name, value);
        }
    }
}
