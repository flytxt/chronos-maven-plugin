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

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Utils class
 *
 * @author gazal
 *
 */
public class Utils {

    public static final String API_PATH = "/scheduler/iso8601";

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private Utils() {
    }

    public static ChronosApp readApp(final String file) throws MojoExecutionException {
        try {
            return OBJECT_MAPPER.readValue(new File(file), new TypeReference<ChronosApp>() {
            });
        } catch (final IOException e) {
            throw new MojoExecutionException("unmarshalling Chronos config file from " + file + " failed", e);
        }
    }

    public static void writeApp(final ChronosApp app, final String file) throws MojoExecutionException {
        try {
            OBJECT_MAPPER.writeValue(new File(file), app);
        } catch (final IOException e) {
            throw new MojoExecutionException("marshalling Chronos config file to " + file + " failed", e);
        }
    }
}
