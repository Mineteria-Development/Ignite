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
package com.mineteria.example.mixin.plugins;

import com.mineteria.example.ExampleConfig;
import com.mineteria.ignite.api.Blackboard;
import com.mineteria.ignite.api.config.Configuration;
import com.mineteria.ignite.api.config.ConfigurationKey;
import com.mineteria.ignite.api.config.Configurations;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public final class CorePlugin implements IMixinConfigPlugin {
  private static final ConfigurationKey EXAMPLE_KEY = ConfigurationKey.key("example", Blackboard.getProperty(Blackboard.CONFIG_DIRECTORY_PATH)
    .resolve("example")
    .resolve("example.conf")
  );

  @Override
  public void onLoad(final @NonNull String mixinPackage) {
  }

  @Override
  public final @Nullable String getRefMapperConfig() {
    return null;
  }

  @Override
  public final boolean shouldApplyMixin(final @NonNull String targetClassName, final @NonNull String mixinClassName) {
    final Configuration<ExampleConfig, CommentedConfigurationNode> config = Configurations.load(Configurations.HOCON_LOADER, CorePlugin.EXAMPLE_KEY, new ExampleConfig());

    return config.getInstance().test;
  }

  @Override
  public void acceptTargets(final @NonNull Set<String> myTargets, final @NonNull Set<String> otherTargets) {
  }

  @Override
  public final @Nullable List<String> getMixins() {
    return null;
  }

  @Override
  public void preApply(final @NonNull String targetClassName, final @NonNull ClassNode targetClass, final @NonNull String mixinClassName, final @NonNull IMixinInfo mixinInfo) {
  }

  @Override
  public void postApply(final @NonNull String targetClassName, final @NonNull ClassNode targetClass, final @NonNull String mixinClassName, final @NonNull IMixinInfo mixinInfo) {
  }
}
