package it.czerwinski.intellij.common.editor

import com.intellij.ide.ui.UISettings
import com.intellij.ide.ui.UISettingsListener
import com.intellij.ui.JBColor
import com.intellij.ui.border.CustomLineBorder
import java.awt.BorderLayout
import java.awt.Graphics
import javax.swing.JPanel

class EditorFooterComponent : JPanel(BorderLayout()), UISettingsListener {

    init {
        border = CustomLineBorder(JBColor.border(), 1, 0, 0, 0)
    }

    override fun paint(g: Graphics) {
        UISettings.setupAntialiasing(g)
        super.paint(g)
    }

    override fun uiSettingsChanged(uiSettings: UISettings) = Unit
}
