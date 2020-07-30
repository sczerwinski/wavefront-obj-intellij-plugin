package it.czerwinski.intellij.wavefront.services

import com.intellij.openapi.project.Project
import it.czerwinski.intellij.wavefront.WavefrontObjBundle

class WavefrontObjProjectService(project: Project) {

    init {
        println(WavefrontObjBundle.message("projectService", project.name))
    }
}
