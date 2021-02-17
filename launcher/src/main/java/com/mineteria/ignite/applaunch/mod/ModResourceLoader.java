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
package com.mineteria.ignite.applaunch.mod;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.mineteria.ignite.api.mod.ModConfig;
import com.mineteria.ignite.api.mod.ModContainer;
import com.mineteria.ignite.api.mod.ModResource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class ModResourceLoader {
  private final Gson gson = new Gson();

  public final @NonNull List<ModContainer> loadResources(final @NonNull ModEngine engine) {
    final List<ModContainer> containers = new ArrayList<>();

    for (final ModResource resource : engine.getResources()) {
      final Path resourcePath = resource.getPath();

      try (final JarFile jarFile = new JarFile(resourcePath.toFile())) {
        final JarEntry jarEntry = jarFile.getJarEntry(engine.getResourceLocator().getMetadataPath());
        if (jarEntry == null) {
          engine.getLogger().debug("'{}' does not contain any mod metadata so it is not a mod. Skipping...", jarFile);
          continue;
        }

        final JsonReader reader = new JsonReader(new InputStreamReader(jarFile.getInputStream(jarEntry), StandardCharsets.UTF_8));
        final ModConfig config = this.gson.fromJson(reader, ModConfig.class);
        if (config.getId() == null || config.getVersion() == null) {
          engine.getLogger().error("Attempted to load '{}', but found an invalid configuration! Skipping...", jarFile.getName());
          continue;
        }

        if (engine.hasContainer(config.getId())) {
          engine.getLogger().warn("The mod '" + config.getId() + "' is already loaded! Skipping...");
          continue;
        }

        final Logger logger = LogManager.getLogger(config.getId());
        containers.add(new ModContainer(logger, resource, config));
      } catch (final IOException exception) {
        engine.getLogger().warn("Failed to open '{}'!", resourcePath);
      }
    }

    return containers;
  }
}
