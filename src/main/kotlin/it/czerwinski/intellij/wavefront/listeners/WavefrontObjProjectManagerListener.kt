package it.czerwinski.intellij.wavefront.listeners

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import it.czerwinski.intellij.wavefront.services.WavefrontObjProjectService

internal class WavefrontObjProjectManagerListener : ProjectManagerListener {

    override fun projectOpened(project: Project) {
        project.getService(WavefrontObjProjectService::class.java)
    }
}
