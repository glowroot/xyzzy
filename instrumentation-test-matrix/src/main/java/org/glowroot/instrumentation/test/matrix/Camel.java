/**
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.glowroot.instrumentation.test.matrix;

import static org.glowroot.instrumentation.test.matrix.JavaVersion.JAVA6;
import static org.glowroot.instrumentation.test.matrix.JavaVersion.JAVA7;
import static org.glowroot.instrumentation.test.matrix.JavaVersion.JAVA8;

public class Camel {

    private static final String MODULE_PATH = "instrumentation/camel";

    public static void main(String[] args) throws Exception {
        if (args.length == 1 && args[0].contentEquals("short")) {
            runShort();
        } else {
            runAll();
        }
    }

    static void runShort() throws Exception {
        run("1.4.0");
        runJava7("2.14.0");
        runJava8("2.18.0");
    }

    static void runAll() throws Exception {
        run("1.4.0");
        run("1.5.0");
        run("1.6.0");
        run("1.6.1");
        run("1.6.2");
        run("1.6.3");
        run("1.6.4");
        run("2.0.0");
        run("2.1.0");
        run("2.2.0");
        run("2.3.0");
        run("2.4.0");
        run("2.5.0");
        run("2.6.0");
        for (int i = 0; i <= 5; i++) {
            run("2.7." + i);
        }
        for (int i = 0; i <= 6; i++) {
            run("2.8." + i);
        }
        for (int i = 0; i <= 8; i++) {
            run("2.9." + i);
        }
        for (int i = 0; i <= 7; i++) {
            run("2.10." + i);
        }
        for (int i = 0; i <= 4; i++) {
            run("2.11." + i);
        }
        for (int i = 0; i <= 5; i++) {
            run("2.12." + i);
        }
        for (int i = 0; i <= 4; i++) {
            run("2.13." + i);
        }
        for (int i = 0; i <= 4; i++) {
            runJava7("2.14." + i);
        }
        for (int i = 0; i <= 6; i++) {
            runJava7("2.15." + i);
        }
        for (int i = 0; i <= 5; i++) {
            runJava7("2.16." + i);
        }
        for (int i = 0; i <= 7; i++) {
            runJava7("2.17." + i);
        }
        for (int i = 0; i <= 5; i++) {
            runJava8("2.18." + i);
        }
        for (int i = 0; i <= 5; i++) {
            runJava8("2.19." + i);
        }
        for (int i = 0; i <= 4; i++) {
            runJava8("2.20." + i);
        }
        for (int i = 0; i <= 5; i++) {
            runJava8("2.21." + i);
        }
        for (int i = 0; i <= 3; i++) {
            runJava8("2.22." + i);
        }
        for (int i = 0; i <= 1; i++) {
            runJava8("2.23." + i);
        }
    }

    private static void run(String version) throws Exception {
        Util.updateLibVersion(MODULE_PATH, "camel.version", version);
        Util.runTests(MODULE_PATH, JAVA8, JAVA7, JAVA6);
    }

    private static void runJava7(String version) throws Exception {
        Util.updateLibVersion(MODULE_PATH, "camel.version", version);
        Util.runTests(MODULE_PATH, JAVA8, JAVA7);
    }

    private static void runJava8(String version) throws Exception {
        Util.updateLibVersion(MODULE_PATH, "camel.version", version);
        Util.runTests(MODULE_PATH, JAVA8);
    }
}