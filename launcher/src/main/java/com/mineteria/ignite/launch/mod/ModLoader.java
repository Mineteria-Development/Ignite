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
package com.mineteria.ignite.launch.mod;

import com.google.inject.Injector;
import com.mineteria.ignite.api.mod.ModContainer;
import com.mineteria.ignite.launch.IgniteLaunch;
import com.mineteria.ignite.launch.IgnitePlatform;
import com.mineteria.ignite.launch.inject.ModModule;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class ModLoader {
  public final @NonNull List<ModContainer> loadContainers(final @NonNull IgnitePlatform platform,
                                                          final @NonNull Collection<ModContainer> pending,
                                                          final @NonNull Map<String, ModContainer> identifierTarget,
                                                          final @NonNull Map<Object, ModContainer> instanceTarget) {
    final List<ModContainer> ordered;
    try {
      ordered = ModDependencyResolver.resolveDependencies(platform, pending);
    } catch (final IllegalStateException exception) {
      platform.getLogger().error("Unable to generate mod dependency graph!", exception);
      return Collections.emptyList();
    }

    for (final ModContainer container : ordered) {
      final String identifier = container.getId();

      try {
        // Instantiate the container.
        final Object mod = this.instantiateContainer(container);
        identifierTarget.put(identifier, container);

        if (mod != null) {
          instanceTarget.put(mod, container);

          // Register the instance events.
          try {
            platform.getEventManager().register(mod, mod);
          } catch (final Exception exception) {
            throw new IllegalStateException("Unable to register mod listeners!", exception);
          }
        } else {
          platform.getLogger().warn("Loaded '" + identifier + "' without a target class.");
        }
      } catch (final Exception exception) {
        platform.getLogger().error("Failed to load mod '" + identifier + "'!", exception);
      }
    }

    return ordered;
  }

  private Object instantiateContainer(final ModContainer container) throws IllegalStateException {
    try {
      // Add the resource to the class loader.
      final URL resourceJar = container.getResource().getPath().toUri().toURL();
      final ModClassLoader classLoader = new ModClassLoader(new URL[] { resourceJar });
      classLoader.addLoaders();

      final String target = container.getConfig().getTarget();
      if (target != null) {
        // Load the class.
        final Class<?> clazz = classLoader.loadClass(target);

        final Injector parentInjector = IgniteLaunch.getInstance().getInjector();
        if (parentInjector != null) {
          final Injector childInjector = parentInjector.createChildInjector(new ModModule(container, clazz));
          return childInjector.getInstance(clazz);
        }

        return clazz.newInstance();
      }

      return null;
    } catch (final Throwable throwable) {
      throw new IllegalStateException("An error occurred attempting to create an instance of mod '" + container.toString() + "'!", throwable);
    }
  }
}
