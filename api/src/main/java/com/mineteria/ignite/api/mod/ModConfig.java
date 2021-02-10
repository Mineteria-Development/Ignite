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
package com.mineteria.ignite.api.mod;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class ModConfig {
  private String id;
  private String version;
  private String target;
  private List<String> mixins;

  public ModConfig() {}

  public ModConfig(final @NonNull String id,
                   final @NonNull String version,
                   final @Nullable String target,
                   final @Nullable List<String> mixins) {
    this.id = id;
    this.version = version;
    this.target = target;
    this.mixins = mixins;
  }

  public @NonNull String getId() {
    return this.id;
  }

  public @NonNull String getVersion() {
    return this.version;
  }

  public @Nullable String getTarget() {
    return this.target;
  }

  public @Nullable List<String> getMixins() {
    return this.mixins;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.version, this.mixins);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (!(other instanceof ModConfig)) return false;
    final ModConfig that = (ModConfig) other;
    return Objects.equals(this.id, that.id)
      && Objects.equals(this.version, that.version)
      && Objects.equals(this.target, that.target)
      && Objects.equals(this.mixins, that.mixins);
  }

  @Override
  public @NonNull String toString() {
    return "ModConfig{id=" + this.id +
      ", version=" + this.version +
      ", target=" + this.target +
      ", mixins=" + (this.mixins != null ? Arrays.toString(this.mixins.toArray(new String[0])) : "[]") +
      "}";
  }
}
