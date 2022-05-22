package it.czerwinski.intellij.wavefront.lang

import com.intellij.openapi.editor.ElementColorProvider
import com.intellij.psi.PsiElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlColorElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlElementFactory
import java.awt.Color

class MtlElementColorProvider : ElementColorProvider {

    override fun getColorFrom(element: PsiElement): Color? {
        val parentElement = element.parent
        return if (parentElement is MtlColorElement && parentElement.firstChild === element) {
            parentElement.color
        } else {
            null
        }
    }

    override fun setColorTo(element: PsiElement, color: Color) {
        val parentElement = element.parent
        if (parentElement is MtlColorElement && parentElement.firstChild === element && parentElement.color != color) {
            setColorTo(parentElement, color)
        }
    }

    private fun setColorTo(element: MtlColorElement, color: Color) {
        val newColorNode = MtlElementFactory.createColorElement(element.project, color).node
        newColorNode.replaceChild(newColorNode.firstChildNode, element.node.firstChildNode)

        element.node.replaceAllChildrenToChildrenOf(newColorNode)
    }
}
