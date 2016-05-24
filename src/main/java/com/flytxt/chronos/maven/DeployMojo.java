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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.InputStreamEntity;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.IOUtil;

/**
 * The DeployMojo class
 *
 * @author gazal
 *
 */
@Mojo(name = "deploy", defaultPhase = LifecyclePhase.DEPLOY)
public class DeployMojo extends AbstractChronosMojo {

    /**
     * URL of the chronos host as specified in pom.xml.
     */
    @Parameter(property = "chronosHost", required = true)
    private String chronosHost;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        FileInputStream fileInputStream = null;
        try {
            try {
                fileInputStream = new FileInputStream(finalChronosConfigFile);
            } catch (final FileNotFoundException e) {
                throw new MojoExecutionException("reading Chronos config file from " + finalChronosConfigFile + " failed", e);
            }
            final InputStreamEntity entity = new InputStreamEntity(fileInputStream);
            entity.setContentType("application/json");
            HttpResponse response;
            try {
                response = Request.Post(chronosHost + Utils.API_PATH).body(entity).execute().returnResponse();
            } catch (final IOException e) {
                throw new MojoExecutionException("posting to Chronos at " + chronosHost + " failed", e);
            }
            if (response.getStatusLine().getStatusCode() >= 400) {
                final StringWriter writer = new StringWriter();
                try {
                    IOUtil.copy(response.getEntity().getContent(), writer);
                } catch (UnsupportedOperationException | IOException e) {
                }
                throw new MojoExecutionException("error response from Chronos, status: " + response.getStatusLine() + " body: " + writer.toString());
            }
            getLog().info("app deployed to chronos");
        } finally {
            if (null != fileInputStream) {
                try {
                    fileInputStream.close();
                } catch (final IOException e) {
                }
            }
        }
    }

}
