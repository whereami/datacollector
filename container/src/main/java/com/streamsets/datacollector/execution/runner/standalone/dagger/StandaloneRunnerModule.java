/**
 * Copyright 2015 StreamSets Inc.
 *
 * Licensed under the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.streamsets.datacollector.execution.runner.standalone.dagger;

import com.streamsets.datacollector.execution.Runner;
import com.streamsets.datacollector.execution.runner.common.AsyncRunner;
import com.streamsets.datacollector.execution.runner.standalone.StandaloneRunner;
import com.streamsets.pipeline.lib.executor.SafeScheduledExecutorService;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;

import javax.inject.Named;

@Module(injects = Runner.class, library = true, complete = false)
public class StandaloneRunnerModule {

  private final String name;
  private final String rev;
  private final String user;

  private final ObjectGraph objectGraph;

  public StandaloneRunnerModule(String user, String name, String rev, ObjectGraph objectGraph) {
    this.name = name;
    this.rev = rev;
    this.user = user;
    this.objectGraph = objectGraph;
  }

  @Provides
  public StandaloneRunner provideRunner() {
    return new StandaloneRunner(user, name, rev, objectGraph);
  }

  @Provides
  public Runner provideAsyncRunner(StandaloneRunner runner,
                                        @Named("runnerExecutor") SafeScheduledExecutorService asyncExecutor) {
    return new AsyncRunner(runner, asyncExecutor);
  }
}
