/*
 * Copyright (c) 2019-2023, JetBrains s.r.o. and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation. JetBrains designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact JetBrains, Na Hrebenech II 1718/10, Prague, 14000, Czech Republic
 * if you need additional information or have any questions.
 */
import java.util.*

pluginManagement {
  val kotlinVersion: String by settings
  val intellijPluginVersion: String by settings

  plugins {
    kotlin("jvm") version kotlinVersion apply false
    id("org.jetbrains.intellij") version "1.16.1" apply false
  }
}

rootProject.name = "projector-server"

val projectorClientGroup: String by settings

val localProperties = Properties().apply {
  try {
    load(File(rootDir, "local.properties").inputStream())
  }
  catch (t: Throwable) {
    println("Can't read local.properties: $t, assuming empty")
  }
}

if (localProperties["useLocalProjectorClient"] == "true") {
  includeBuild("../projector-client") {
    dependencySubstitution {
      substitute(module("$projectorClientGroup:projector-common")).using(project(":projector-common"))
      substitute(module("$projectorClientGroup:projector-server-core")).using(project(":projector-server-core"))
      substitute(module("$projectorClientGroup:projector-util-loading")).using(project(":projector-util-loading"))
      substitute(module("$projectorClientGroup:projector-util-logging")).using(project(":projector-util-logging"))
    }
  }
}

include("projector-agent")
include("projector-awt")
include("projector-awt-common")
include("projector-awt-jdk17")
include("projector-plugin")
include("projector-server")
include("projector-server-common")
include("projector-server-jdk17")
