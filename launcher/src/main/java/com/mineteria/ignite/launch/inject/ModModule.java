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
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.mineteria.ignite.api.config.path.ConfigPath;
import com.mineteria.ignite.api.config.path.ConfigsPath;
import com.mineteria.ignite.api.mod.ModContainer;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.nio.file.Path;

public final class ModModule extends AbstractModule {
  private final ModContainer container;
  private final Class<?> target;

  public ModModule(final @NonNull ModContainer container, final @NonNull Class<?> target) {
    this.container = container;
    this.target = target;
  }

  @Override
  protected void configure() {
    this.bind(this.target).in(Scopes.SINGLETON);

    this.bind(ModContainer.class).toInstance(this.container);
    this.bind(Logger.class).toInstance(this.container.getLogger());

    this.bind(Path.class)
      .annotatedWith(ConfigPath.class)
      .toProvider(ConfigPathProvider.class)
      .in(Scopes.SINGLETON);
  }

  /* package */ static final class ConfigPathProvider implements Provider<Path> {
    private final Path configs;
    private final ModContainer container;

    @Inject
    public ConfigPathProvider(final @NonNull @ConfigsPath Path configs, final @NonNull ModContainer container) {
      this.configs = configs;
      this.container = container;
    }

    @Override
    public final @NonNull Path get() {
      return this.configs.resolve(this.container.getId());
    }
  }
}
