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

import java.io.File;
import java.io.IOException;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The AbstractChronosMojo class
 *
 * @author gazal
 *
 */
public abstract class AbstractChronosMojo extends AbstractMojo {

    protected ObjectMapper objectMapper = new ObjectMapper();

    @Parameter(defaultValue = "${session}", readonly = true)
    protected MavenSession session;

    @Parameter(defaultValue = "${mojoExecution}", readonly = true)
    protected MojoExecution execution;

    /**
     * Path to JSON file to write when processing Chronos config. Default is ${project.build.directory}/chronos.json
     */
    @Parameter(property = "finalChronosConfigFile", defaultValue = "${project.build.directory}/chronos.json")
    protected String finalChronosConfigFile;

    protected ChronosApp readApp(final String file) throws MojoExecutionException {
        try {
            return objectMapper.readValue(new File(file), new TypeReference<ChronosApp>() {
            });
        } catch (final IOException e) {
            throw new MojoExecutionException("unmarshalling Chronos config file from " + file + " failed", e);
        }
    }

    protected void writeApp(final ChronosApp app, final String file) throws MojoExecutionException {
        try {
            objectMapper.writeValue(new File(file), app);
        } catch (final IOException e) {
            throw new MojoExecutionException("marshalling Chronos config file to " + file + " failed", e);
        }
    }

}
