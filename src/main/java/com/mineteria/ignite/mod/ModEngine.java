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
package com.mineteria.ignite.mod;

import com.google.gson.stream.JsonReader;
import com.mineteria.ignite.IgniteCore;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.asm.mixin.Mixins;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class ModEngine {
  private final Set<String> mods = new HashSet<>();
  private final List<ModResource> modResources = new ArrayList<>();
  private final List<ModContainer> modContainers = new ArrayList<>();
  private final ModLocator locator = new ModLocator();

  private final IgniteCore core;

  public ModEngine(final @NonNull IgniteCore core) {
    this.core = core;
  }

  /**
   * Locates and populates the mod resources list.
   */
  public void locateResources() {
    this.core.getLogger().info("Locating mod resources...");

    this.modResources.addAll(this.locator.locateResources());

    this.core.getLogger().info("Located [{}] mod(s).", this.modResources.size());
  }

  /**
   * Load the mods and initializes them from the resources list.
   */
  public void loadCandidates() {
    this.core.getLogger().info("Loading mod candidates...");

    for (final ModResource resource : this.getCandidates()) {
      final Path resourcePath = resource.getPath();

      this.core.getLogger().debug("Scanning mod candidate '{}' for mod configuration!", resourcePath);

      try (final JarFile jarFile = new JarFile(resourcePath.toFile())) {
        final JarEntry jarEntry = jarFile.getJarEntry(this.locator.getMetadataPath());
        if (jarEntry == null) {
          core.getLogger().debug("'{}' does not contain any mod metadata so it is not a mod. Skipping...", jarFile);
          continue;
        }

        final JsonReader reader = new JsonReader(new InputStreamReader(jarFile.getInputStream(jarEntry), StandardCharsets.UTF_8));
        final ModConfig config = this.core.getGson().fromJson(reader, ModConfig.class);

        if (this.mods.contains(config.getId())) {
          this.core.getLogger().warn("The mod '" + config.getId() + "' is already loaded! Skipping...");
          continue;
        }

        this.modContainers.add(new ModContainer(resource, config));
        this.mods.add(config.getId());
      } catch (final IOException exception) {
        this.core.getLogger().warn("Failed to open '{}'!", resourcePath);
      }
    }

    this.core.getLogger().info("Loaded [{}] mod(s).", this.getContainers().size());
  }

  public void loadContainers() {
    this.core.getLogger().info("Applying mod transformations...");

    for (final ModContainer container : this.getContainers()) {
      final ModConfig config = container.getConfig();

      // Add the mixin configurations.
      for (final String mixinConfig : config.getMixins()) {
        Mixins.addConfiguration(mixinConfig);
      }

      this.core.getLogger().info("Applied [{}] transformations.", container);
    }
  }

  public @NonNull List<ModResource> getCandidates() {
    return this.modResources;
  }

  public @NonNull List<ModContainer> getContainers() {
    return this.modContainers;
  }
}
