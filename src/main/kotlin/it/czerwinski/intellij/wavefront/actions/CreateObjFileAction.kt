/*
 * Copyright 2020-2023 Slawomir Czerwinski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.czerwinski.intellij.wavefront.actions

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.lang.ObjFileType

class CreateObjFileAction :
    CreateFileFromTemplateAction(
        WavefrontObjBundle.message("action.it.czerwinski.intellij.wavefront.actions.CreateObjFileAction.text"),
        WavefrontObjBundle.message("action.it.czerwinski.intellij.wavefront.actions.CreateObjFileAction.description"),
        ObjFileType.icon
    ),
    DumbAware {

    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder
            .setTitle(
                WavefrontObjBundle.message(
                    "action.it.czerwinski.intellij.wavefront.actions.CreateObjFileAction.dialog.title"
                )
            )
            .addKind(
                WavefrontObjBundle.message(
                    "action.it.czerwinski.intellij.wavefront.actions.CreateObjFileAction.dialog.kind.obj.empty"
                ),
                ObjFileType.icon,
                "Empty object"
            )
            .addKind(
                WavefrontObjBundle.message(
                    "action.it.czerwinski.intellij.wavefront.actions.CreateObjFileAction.dialog.kind.obj.square"
                ),
                ObjFileType.icon,
                "Square"
            )
            .addKind(
                WavefrontObjBundle.message(
                    "action.it.czerwinski.intellij.wavefront.actions.CreateObjFileAction.dialog.kind.obj.cube"
                ),
                ObjFileType.icon,
                "Cube"
            )
            .addKind(
                WavefrontObjBundle.message(
                    "action.it.czerwinski.intellij.wavefront.actions.CreateObjFileAction.dialog.kind.obj.cylinder"
                ),
                ObjFileType.icon,
                "Cylinder"
            )
            .addKind(
                WavefrontObjBundle.message(
                    "action.it.czerwinski.intellij.wavefront.actions.CreateObjFileAction.dialog.kind.obj.sphere"
                ),
                ObjFileType.icon,
                "Sphere"
            )
            .addKind(
                WavefrontObjBundle.message(
                    "action.it.czerwinski.intellij.wavefront.actions.CreateObjFileAction.dialog.kind.obj.nurbs.circle"
                ),
                ObjFileType.icon,
                "NURBS circle"
            )
            .addKind(
                WavefrontObjBundle.message(
                    "action.it.czerwinski.intellij.wavefront.actions.CreateObjFileAction.dialog.kind.obj.nurbs.sphere"
                ),
                ObjFileType.icon,
                "NURBS sphere"
            )
    }

    override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?): String =
        WavefrontObjBundle.message("action.it.czerwinski.intellij.wavefront.actions.CreateObjFileAction.text")
}
