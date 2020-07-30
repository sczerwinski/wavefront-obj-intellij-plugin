package com.github.sczerwinski.wavefrontobjintellijplugin.services

import com.intellij.openapi.project.Project
import com.github.sczerwinski.wavefrontobjintellijplugin.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
