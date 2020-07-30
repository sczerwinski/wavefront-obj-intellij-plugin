package it.czerwinski.intellij.wavefront.services

import com.intellij.openapi.project.Project
import it.czerwinski.intellij.wavefront.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
