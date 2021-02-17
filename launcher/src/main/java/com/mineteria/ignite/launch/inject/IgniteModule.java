/*
 * This file is part of Ignite, licensed under the MIT License (MIT).
 *
 * Copyright (c) Mineteria <https://mineteria.com/>
 * Copyright (c) contributors
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
package com.mineteria.ignite.launch.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.mineteria.ignite.api.Ignite;
import com.mineteria.ignite.api.Platform;
import com.mineteria.ignite.api.config.path.ConfigsPath;
import com.mineteria.ignite.api.config.path.ModsPath;
import com.mineteria.ignite.applaunch.IgniteBootstrap;
import com.mineteria.ignite.launch.IgnitePlatform;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.nio.file.Path;

public final class IgniteModule extends AbstractModule {
  @Override
  protected void configure() {
    this.requestStaticInjection(Ignite.class);

    this.bind(Platform.class).to(IgnitePlatform.class).in(Scopes.SINGLETON);

    this.bind(Path.class)
      .annotatedWith(ModsPath.class)
      .toProvider(ModsPathProvider.class)
      .in(Scopes.SINGLETON);

    this.bind(Path.class)
      .annotatedWith(ConfigsPath.class)
      .toProvider(ConfigsPathProvider.class)
      .in(Scopes.SINGLETON);
  }

  /* package */ static final class ModsPathProvider implements Provider<Path> {
    @Override
    public final @NonNull Path get() {
      return IgniteBootstrap.MOD_TARGET_PATH;
    }
  }

  /* package */ static final class ConfigsPathProvider implements Provider<Path> {
    @Override
    public final @NonNull Path get() {
      return IgniteBootstrap.CONFIG_TARGET_PATH;
    }
  }
}
