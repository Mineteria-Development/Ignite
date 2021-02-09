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

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Objects;
import java.util.jar.Manifest;

public final class ModResource {
  private final String locator;
  private final Path path;
  private final Manifest manifest;

  private FileSystem fileSystem;

  public ModResource(final @NonNull String locator,
                     final @NonNull Path path,
                     final @NonNull Manifest manifest) {
    this.locator = locator;
    this.path = path;
    this.manifest = manifest;
  }

  public @NonNull String getLocator() {
    return this.locator;
  }

  public @NonNull Path getPath() {
    return this.path;
  }

  public @NonNull Manifest getManifest() {
    return this.manifest;
  }

  public @NonNull FileSystem getFileSystem() {
    if (this.fileSystem == null) {
      try {
        this.fileSystem = FileSystems.newFileSystem(this.getPath(), this.getClass().getClassLoader());
      } catch (final IOException exception) {
        throw new RuntimeException(exception);
      }
    }

    return this.fileSystem;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.locator, this.path, this.manifest);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (!(other instanceof ModResource)) return false;
    final ModResource that = (ModResource) other;
    return Objects.equals(this.locator, that.locator)
      && Objects.equals(this.path, that.path)
      && Objects.equals(this.manifest, that.manifest);
  }

  @Override
  public String toString() {
    return "ModResource{name=" + this.locator + ", path=" + this.path + ", fileSystem=" + this.fileSystem + "}";
  }
}
