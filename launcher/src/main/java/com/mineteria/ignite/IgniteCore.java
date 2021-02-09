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
package com.mineteria.ignite;

import com.google.gson.Gson;
import com.mineteria.ignite.api.mod.ModEngine;
import com.mineteria.ignite.mod.IgniteModEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.asm.launch.MixinBootstrap;

public final class IgniteCore {
  public static final IgniteCore INSTANCE = new IgniteCore();

  private final Logger logger = LogManager.getLogger("IgniteCore");
  private final Gson gson = new Gson();

  private final ModEngine modEngine;

  /* package */ IgniteCore() {
    this.modEngine = new IgniteModEngine(this);

    MixinBootstrap.init();
  }

  public @NonNull Logger getLogger() {
    return this.logger;
  }

  public @NonNull Gson getGson() {
    return this.gson;
  }

  public @NonNull ModEngine getEngine() {
    return this.modEngine;
  }
}