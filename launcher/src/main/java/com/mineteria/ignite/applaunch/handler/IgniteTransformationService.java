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
package com.mineteria.ignite.applaunch.handler;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.mineteria.ignite.api.mod.ModResource;
import com.mineteria.ignite.applaunch.IgniteBootstrap;
import com.mineteria.ignite.applaunch.util.IgniteConstants;
import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.ITransformationService;
import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.IncompatibleEnvironmentException;
import net.minecraftforge.accesstransformer.AccessTransformerEngine;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.asm.launch.MixinBootstrap;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class IgniteTransformationService implements ITransformationService {
  @Override
  public final @NonNull String name() {
    return "ignite_platform";
  }

  @Override
  public void initialize(final @NonNull IEnvironment environment) {
    MixinBootstrap.init();
  }

  @Override
  public void beginScanning(final @NonNull IEnvironment environment) {
    // No-op
  }

  @Override
  public final @NonNull List<Map.Entry<String, Path>> runScan(final @NonNull IEnvironment environment) {
    IgniteBootstrap.getInstance().getModEngine().locateResources();
    IgniteBootstrap.getInstance().getModEngine().loadResources();

    final List<Map.Entry<String, Path>> launchResources = new ArrayList<>();
    for (final ModResource resource : IgniteBootstrap.getInstance().getModEngine().getResources()) {
      final String atFiles = resource.getManifest().getMainAttributes().getValue(IgniteConstants.AT);
      if (atFiles != null) {
        for (final String atFile : atFiles.split(",")) {
          if (!atFile.endsWith(".cfg")) continue;

          AccessTransformerEngine.INSTANCE.addResource(resource.getFileSystem().getPath(IgniteConstants.META_INF).resolve(atFile), atFile);
        }
      }

      final Map.Entry<String, Path> entry = Maps.immutableEntry(resource.getPath().getFileName().toString(), resource.getPath());
      launchResources.add(entry);
    }

    return launchResources;
  }

  @Override
  public void onLoad(final @NonNull IEnvironment env, final @NonNull Set<String> otherServices) throws IncompatibleEnvironmentException {
    // No-op
  }

  @Override
  @SuppressWarnings("rawtypes")
  public final @NonNull List<ITransformer> transformers() {
    return ImmutableList.of();
  }
}