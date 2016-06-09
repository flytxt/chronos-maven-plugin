/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Flytxt Mobile Solutions
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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

    /**
     * Timeout (in seconds) to chronos as specified in pom.xml.
     */
    @Parameter(property = "timeout", defaultValue = "30")
    private int timeout;

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
                response = Request.Post(chronosHost + Utils.API_PATH).body(entity).socketTimeout(timeout * 1000).execute().returnResponse();
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
