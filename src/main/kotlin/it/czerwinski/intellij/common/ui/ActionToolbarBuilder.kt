/*
 * Copyright 2020-2021 Slawomir Czerwinski
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

package it.czerwinski.intellij.common.ui

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.ActionToolbar
import org.jetbrains.annotations.NonNls
import java.lang.ref.WeakReference
import javax.swing.JComponent

/**
 * Action toolbar builder.
 */
class ActionToolbarBuilder {

    @NonNls private var myActionGroupId: String = ""
    @NonNls private var myPlace: String = ActionPlaces.UNKNOWN
    private var myHorizontal: Boolean = true
    private var myTargetComponent: WeakReference<JComponent> = WeakReference(null)
    private var myReservePlaceAutoPopupIcon: Boolean = false

    /**
     * Sets ID of the action group to be used to create the toolbar.
     */
    fun setActionGroupId(@NonNls actionGroupId: String): ActionToolbarBuilder {
        myActionGroupId = actionGroupId
        return this
    }

    /**
     * Sets that will be set for `AnActionEvent` passed when an action from the group
     * is either performed or updated.
     *
     * Default value is [UNKNOWN][ActionPlaces.UNKNOWN].
     */
    fun setPlace(@NonNls place: String): ActionToolbarBuilder {
        myPlace = place
        return this
    }

    /**
     * Sets orientation of the toolbar: `true` – horizontal, `false` – vertical.
     *
     * Default value is `true`.
     */
    fun setHorizontal(horizontal: Boolean): ActionToolbarBuilder {
        myHorizontal = horizontal
        return this
    }

    /**
     * Sets component used for data-context retrieval.
     */
    fun setTargetComponent(component: JComponent): ActionToolbarBuilder {
        myTargetComponent = WeakReference(component)
        return this
    }

    /**
     * Sets the flag for reserving place for auto popup icon.
     *
     * Default value is `false`.
     */
    fun setReservePlaceAutoPopupIcon(reserve: Boolean): ActionToolbarBuilder {
        myReservePlaceAutoPopupIcon = reserve
        return this
    }

    /**
     * Builds an [ActionToolbar].
     */
    fun build(): ActionToolbar {
        val actionManager = ActionManager.getInstance()

        require(actionManager.isGroup(myActionGroupId)) { "Actions group not found: $myActionGroupId" }

        val group = actionManager.getAction(myActionGroupId) as ActionGroup

        val toolbar = actionManager.createActionToolbar(myPlace, group, myHorizontal)
        myTargetComponent.get()?.also { component -> toolbar.setTargetComponent(component) }
        toolbar.setReservePlaceAutoPopupIcon(false)
        return toolbar
    }
}
