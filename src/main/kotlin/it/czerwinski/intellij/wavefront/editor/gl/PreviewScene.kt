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

package it.czerwinski.intellij.wavefront.editor.gl

import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.colors.ColorKey
import com.intellij.util.ui.UIUtil
import com.jogamp.opengl.GLAnimatorControl
import com.jogamp.opengl.GLProfile
import graphics.glimpse.ClearableBufferType
import graphics.glimpse.FaceCullingMode
import graphics.glimpse.GlimpseAdapter
import graphics.glimpse.cameras.Camera
import graphics.glimpse.cameras.TargetCamera
import graphics.glimpse.lenses.Lens
import graphics.glimpse.lenses.PerspectiveLens
import graphics.glimpse.meshes.Mesh
import graphics.glimpse.textures.Texture
import graphics.glimpse.textures.TextureMagFilter
import graphics.glimpse.textures.TextureMinFilter
import graphics.glimpse.textures.TextureWrap
import graphics.glimpse.types.Angle
import graphics.glimpse.types.Mat4
import graphics.glimpse.types.Vec2
import graphics.glimpse.types.Vec3
import graphics.glimpse.types.scale
import graphics.glimpse.types.translation
import it.czerwinski.intellij.common.ui.ErrorLog
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.gl.meshes.AxisMeshFactory
import it.czerwinski.intellij.wavefront.editor.gl.meshes.EnvironmentMeshFactory
import it.czerwinski.intellij.wavefront.editor.gl.meshes.GridMeshFactory
import it.czerwinski.intellij.wavefront.editor.gl.meshes.TextMeshFactory
import it.czerwinski.intellij.wavefront.editor.gl.shaders.EnvironmentShader
import it.czerwinski.intellij.wavefront.editor.gl.shaders.ProgramExecutorsManager
import it.czerwinski.intellij.wavefront.editor.gl.shaders.TextShader
import it.czerwinski.intellij.wavefront.editor.gl.shaders.WireframeShader
import it.czerwinski.intellij.wavefront.editor.gl.textures.TextureResources
import it.czerwinski.intellij.wavefront.editor.model.PBREnvironment
import it.czerwinski.intellij.wavefront.editor.model.PreviewSceneConfig
import it.czerwinski.intellij.wavefront.editor.model.UpVector
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterialElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlReflectionType

/**
 * Base 3D preview scene.
 *
 * This class implements rendering axes and grid.
 */
abstract class PreviewScene(
    profile: GLProfile,
    parent: Disposable,
    animatorControl: GLAnimatorControl,
    errorLog: ErrorLog
) : BaseScene(profile, parent, animatorControl, errorLog) {

    /**
     * Environment for physically based rendering.
     */
    var environment: PBREnvironment = PBREnvironment.DEFAULT
        set(value) {
            field = value
            requestRender()
        }

    /**
     * Should return maximum distance from the origin point that should be rendered.
     */
    protected abstract val modelSize: Float?

    /**
     * Scene camera configuration.
     */
    protected var camera: Camera<Float> = TargetCamera(eye = Vec3.unitX(), target = Vec3.nullVector())

    /**
     * Scene lens configuration.
     */
    protected var lens: Lens<Float> = PerspectiveLens(Angle.rightAngle(), aspect = 1f, near = 1f, far = 2f)

    /**
     * Vector pointing in the "up" direction of the scene.
     */
    protected abstract val upVector: UpVector

    /**
     * Determines whether axes should be shown.
     */
    protected abstract val showEnvironment: Boolean

    /**
     * Determines whether axes should be shown.
     */
    var showAxes: Boolean = false
        set(value) {
            field = value
            requestRender()
        }

    /**
     * Determines whether grid should be shown.
     */
    var showGrid: Boolean = false
        set(value) {
            field = value
            requestRender()
        }

    /**
     * Advanced scene configuration.
     */
    var config: PreviewSceneConfig = PreviewSceneConfig()
        set(value) {
            field = value
            requestRender()
        }

    override val mipmapping: Boolean get() = config.mipmapping

    private val fontScaling: Float get() = if (UIUtil.isRetina()) 2f else 1f

    protected val programExecutorsManager = ProgramExecutorsManager(errorLog)

    private lateinit var environmentTextures: List<Texture>
    protected val environmentTexture: Texture get() = environmentTextures[environment.ordinal]
    protected open val materialEnvironmentTexture: Texture? get() = null

    private lateinit var irradianceTextures: List<Texture>
    protected val irradianceTexture: Texture get() = irradianceTextures[environment.ordinal]
    protected open val materialIrradianceTexture: Texture? get() = null

    private lateinit var reflectionTextures: List<List<Texture>>
    protected val reflectionTextureLevels: List<Texture>
        get() = listOf(environmentTexture) + reflectionTextures[environment.ordinal]
    protected open val materialReflectionTextures: List<Texture> get() = emptyList()

    protected lateinit var brdfTexture: Texture

    private lateinit var fontTexture: Texture
    private lateinit var boldFontTexture: Texture

    protected lateinit var fallbackTexture: Texture
        private set
    protected lateinit var fallbackNormalmap: Texture
        private set

    private val fallbackTextureProvider: TextureProvider by lazy { FallbackTextureProvider { fallbackTexture } }
    private val fallbackNormalmapProvider: TextureProvider by lazy { FallbackTextureProvider { fallbackNormalmap } }

    private lateinit var environmentMesh: Mesh
    private lateinit var axisMesh: Mesh
    private lateinit var axisConeMesh: Mesh
    private lateinit var axisXLabelMesh: Mesh
    private lateinit var axisYLabelMesh: Mesh
    private lateinit var axisZLabelMesh: Mesh

    private lateinit var gridMesh: Mesh
    private lateinit var fineGridMesh: Mesh

    init {
        TextureResources.prepare(profile)
    }

    override fun initialize(gl: GlimpseAdapter) {
        createShaders(gl)
        createFontTexture(gl)
        createFallbackTextures(gl)
        createAxesMeshes(gl)
        createGridMeshes(gl)
    }

    private fun createShaders(gl: GlimpseAdapter) {
        programExecutorsManager.initialize(gl, config.shaderQuality)
    }

    private fun createFontTexture(gl: GlimpseAdapter) {
        try {
            val textureBuilder = Texture.Builder.getInstance(gl)

            environmentTextures = textureBuilder
                .apply {
                    for (textureImageSource in TextureResources.environmentTextureImageSources) {
                        addTexture(textureImageSource)
                    }
                }
                .setTextureFilter(TextureMinFilter.LINEAR, TextureMagFilter.LINEAR)
                .setTextureWrap(TextureWrap.REPEAT, TextureWrap.CLAMP_TO_EDGE)
                .build()

            irradianceTextures = Texture.Builder.getInstance(gl)
                .apply {
                    for (textureImageSource in TextureResources.irradianceTextureImageSources) {
                        addTexture(textureImageSource)
                    }
                }
                .setTextureFilter(TextureMinFilter.LINEAR, TextureMagFilter.LINEAR)
                .setTextureWrap(TextureWrap.REPEAT, TextureWrap.CLAMP_TO_EDGE)
                .build()

            reflectionTextures = TextureResources.reflectionTextureImageSources.map { singleImageSources ->
                Texture.Builder.getInstance(gl)
                    .apply {
                        for (textureImageSource in singleImageSources) {
                            addTexture(textureImageSource)
                        }
                    }
                    .setTextureFilter(TextureMinFilter.LINEAR, TextureMagFilter.LINEAR)
                    .setTextureWrap(TextureWrap.REPEAT, TextureWrap.CLAMP_TO_EDGE)
                    .build()
            }

            brdfTexture = textureBuilder
                .addTexture(TextureResources.brdfTextureImageSource)
                .build()
                .first()

            val fontTextures = textureBuilder
                .addTexture(TextureResources.fontTextureImageSource)
                .addTexture(TextureResources.boldFontTextureImageSource)
                .apply {
                    if (config.mipmapping) {
                        generateMipmaps()
                    }
                }
                .build()
            fontTexture = fontTextures.first()
            boldFontTexture = fontTextures.last()
        } catch (expected: Throwable) {
            errorLog.addError(
                WavefrontObjBundle.message("editor.common.preview.createFontTexture.error"),
                expected
            )
        }
    }

    private fun createFallbackTextures(gl: GlimpseAdapter) {
        try {
            val textures = Texture.Builder.getInstance(gl)
                .addTexture(TextureResources.fallbackTextureImageSource)
                .addTexture(TextureResources.fallbackNormalmapImageSource)
                .build()
            fallbackTexture = textures.first()
            fallbackNormalmap = textures.last()
        } catch (expected: Throwable) {
            errorLog.addError(
                WavefrontObjBundle.message("editor.common.preview.createFallbackTextures.error"),
                expected
            )
        }
    }

    private fun createAxesMeshes(gl: GlimpseAdapter) {
        try {
            environmentMesh = EnvironmentMeshFactory.create(gl)
            axisMesh = AxisMeshFactory.createAxis(gl)
            axisConeMesh = AxisMeshFactory.createAxisCone(gl)
            axisXLabelMesh = TextMeshFactory.createText(gl, AXIS_X_LABEL)
            axisYLabelMesh = TextMeshFactory.createText(gl, AXIS_Y_LABEL)
            axisZLabelMesh = TextMeshFactory.createText(gl, AXIS_Z_LABEL)
        } catch (expected: Throwable) {
            errorLog.addError(
                WavefrontObjBundle.message("editor.common.preview.createAxesMeshes.error"),
                expected
            )
        }
    }

    private fun createGridMeshes(gl: GlimpseAdapter) {
        try {
            gridMesh = GridMeshFactory.createGrid(gl)
            fineGridMesh = GridMeshFactory.createFineGrid(gl)
        } catch (expected: Throwable) {
            errorLog.addError(
                WavefrontObjBundle.message("editor.common.preview.createGridMeshes.error"),
                expected
            )
        }
    }

    final override fun doRender(gl: GlimpseAdapter) {
        if (showEnvironment) renderEnvironment(gl)

        if (showAxes) renderAxes(gl)

        gl.glCullFace(config.faceCulling)
        renderModel(gl)

        if (showGrid) renderGrid(gl)
        if (showAxes && config.showAxesLabels) renderAxesLabels(gl)
    }

    private fun renderEnvironment(gl: GlimpseAdapter) {
        prepareEnvironment(gl)
        gl.glCullFace(FaceCullingMode.DISABLED)
        val scale = camera.eye.magnitude() * ENVIRONMENT_CUBE_RATIO
        val modelMatrix = translation(camera.eye) * scale(scale)
        programExecutorsManager.renderEnvironment(
            gl,
            EnvironmentShader(
                mvpMatrix = lens.projectionMatrix * camera.viewMatrix * modelMatrix,
                environmentTexture = materialEnvironmentTexture ?: environmentTexture
            ),
            environmentMesh
        )
        gl.glClear(ClearableBufferType.DEPTH_BUFFER)
    }

    /**
     * Prepares environment before rendering.
     */
    protected abstract fun prepareEnvironment(gl: GlimpseAdapter)

    private fun renderAxes(gl: GlimpseAdapter) {
        gl.glCullFace(FaceCullingMode.DISABLED)
        gl.glLineWidth(config.axisLineWidth)

        renderAxis(gl, AxisMeshFactory.xAxisModelMatrix, PreviewColors.COLOR_AXIS_X)
        renderAxis(gl, AxisMeshFactory.yAxisModelMatrix, PreviewColors.COLOR_AXIS_Y)
        renderAxis(gl, AxisMeshFactory.zAxisModelMatrix, PreviewColors.COLOR_AXIS_Z)
    }

    private fun renderAxis(gl: GlimpseAdapter, modelMatrix: Mat4<Float>, colorKey: ColorKey) {
        val scale = (modelSize ?: 1f) * AXIS_LENGTH_FACTOR
        val mvpMatrix = lens.projectionMatrix * camera.viewMatrix * scale(scale) * upVector.modelMatrix * modelMatrix
        programExecutorsManager.renderWireframe(
            gl,
            WireframeShader(mvpMatrix = mvpMatrix, color = colorKey.toVec4()),
            axisMesh,
            axisConeMesh
        )
    }

    /**
     * Renders the actual model being the subject of this preview.
     */
    protected abstract fun renderModel(gl: GlimpseAdapter)

    private fun renderGrid(gl: GlimpseAdapter) {
        gl.glLineWidth(config.gridLineWidth)
        val scale = GridMeshFactory.calculateGridScale(modelSize = modelSize ?: 1f)
        programExecutorsManager.renderWireframe(
            gl,
            WireframeShader(
                mvpMatrix = lens.projectionMatrix * camera.viewMatrix * scale(scale),
                color = PreviewColors.COLOR_GRID.toVec4(alpha = GRID_ALPHA)
            ),
            gridMesh
        )
        if (config.showFineGrid) renderFineGrid(gl, scale)
    }

    private fun renderFineGrid(gl: GlimpseAdapter, scale: Float) {
        programExecutorsManager.renderWireframe(
            gl,
            WireframeShader(
                mvpMatrix = lens.projectionMatrix * camera.viewMatrix * scale(scale),
                color = PreviewColors.COLOR_GRID.toVec4(alpha = FINE_GRID_ALPHA)
            ),
            fineGridMesh
        )
    }

    private fun renderAxesLabels(gl: GlimpseAdapter) {
        renderAxisLabel(gl, AxisMeshFactory.xAxisModelMatrix, PreviewColors.COLOR_AXIS_X, axisXLabelMesh)
        renderAxisLabel(gl, AxisMeshFactory.yAxisModelMatrix, PreviewColors.COLOR_AXIS_Y, axisYLabelMesh)
        renderAxisLabel(gl, AxisMeshFactory.zAxisModelMatrix, PreviewColors.COLOR_AXIS_Z, axisZLabelMesh)
    }

    private fun renderAxisLabel(gl: GlimpseAdapter, modelMatrix: Mat4<Float>, colorKey: ColorKey, labelMesh: Mesh) {
        val textSize = fontScaling * config.axisLabelFontSize
        val scale = (modelSize ?: 1f) * AXIS_LENGTH_FACTOR
        val mvpMatrix = lens.projectionMatrix * camera.viewMatrix * scale(scale) * upVector.modelMatrix * modelMatrix
        val labelPosition = mvpMatrix * (Vec3.unitZ<Float>() * AXIS_LABEL_DISTANCE_FACTOR).toVec4(w = 1f)
        programExecutorsManager.renderText(
            gl,
            TextShader(
                position = labelPosition.toVec3() / labelPosition.w,
                scale = Vec2(x = textSize / width, y = textSize / height),
                color = colorKey.toVec4(),
                texture = boldFontTexture
            ),
            labelMesh
        )
    }

    override fun dispose(gl: GlimpseAdapter) {
        axisMesh.dispose(gl)
        axisConeMesh.dispose(gl)
        gridMesh.dispose(gl)
        fineGridMesh.dispose(gl)
        val textures = environmentTextures + irradianceTextures + reflectionTextures.flatten() + brdfTexture +
            fontTexture + boldFontTexture +
            fallbackTexture + fallbackNormalmap
        for (texture in textures) {
            texture.dispose(gl)
        }
        programExecutorsManager.dispose(gl)
    }

    @Suppress("LongParameterList")
    protected inner class MaterialTexturesProvider private constructor(
        private val ambientTextureProvider: TextureProvider,
        private val diffuseTextureProvider: TextureProvider,
        private val specularTextureProvider: TextureProvider,
        private val emissionTextureProvider: TextureProvider,
        private val specularExponentTextureProvider: TextureProvider,
        private val roughnessTextureProvider: TextureProvider,
        private val metalnessTextureProvider: TextureProvider,
        private val normalmapTextureProvider: TextureProvider,
        private val displacementTextureProvider: TextureProvider,
        private val environmentTextureProvider: TextureProvider,
        private val irradianceTextureProvider: TextureProvider,
        private val reflectionTextureProviders: List<TextureProvider>
    ) {

        constructor(material: MtlMaterialElement) : this(
            ambientTextureProvider = FileTextureProvider(
                project = material.project,
                paths = listOfNotNull(material.ambientColorMap, material.diffuseColorMap),
                relativeTo = material.containingFile?.virtualFile,
                fallbackTexture = { fallbackTexture }
            ),
            diffuseTextureProvider = FileTextureProvider(
                project = material.project,
                paths = listOfNotNull(material.diffuseColorMap),
                relativeTo = material.containingFile?.virtualFile,
                fallbackTexture = { fallbackTexture }
            ),
            specularTextureProvider = FileTextureProvider(
                project = material.project,
                paths = listOfNotNull(material.specularColorMap, material.metalnessMap),
                relativeTo = material.containingFile?.virtualFile,
                fallbackTexture = { fallbackTexture }
            ),
            emissionTextureProvider = FileTextureProvider(
                project = material.project,
                paths = listOfNotNull(material.emissionColorMap),
                relativeTo = material.containingFile?.virtualFile,
                fallbackTexture = { fallbackTexture }
            ),
            specularExponentTextureProvider = FileTextureProvider(
                project = material.project,
                paths = listOfNotNull(material.specularExponentMap, material.roughnessMap),
                relativeTo = material.containingFile?.virtualFile,
                fallbackTexture = { fallbackTexture }
            ),
            roughnessTextureProvider = FileTextureProvider(
                project = material.project,
                paths = listOfNotNull(material.roughnessMap, material.specularExponentMap),
                relativeTo = material.containingFile?.virtualFile,
                fallbackTexture = { fallbackTexture }
            ),
            metalnessTextureProvider = FileTextureProvider(
                project = material.project,
                paths = listOfNotNull(material.metalnessMap, material.specularColorMap),
                relativeTo = material.containingFile?.virtualFile,
                fallbackTexture = { fallbackTexture }
            ),
            normalmapTextureProvider = FileTextureProvider(
                project = material.project,
                paths = listOfNotNull(material.normalMap, material.bumpMap),
                relativeTo = material.containingFile?.virtualFile,
                fallbackTexture = { fallbackNormalmap }
            ),
            displacementTextureProvider = FileTextureProvider(
                project = material.project,
                paths = listOfNotNull(material.displacementMap),
                relativeTo = material.containingFile?.virtualFile,
                fallbackTexture = { fallbackTexture }
            ),
            environmentTextureProvider = FileTextureProvider(
                project = material.project,
                paths = listOfNotNull(
                    material.reflectionMap?.takeIf { material.reflectionMapType == MtlReflectionType.SPHERE }
                ),
                relativeTo = material.containingFile?.virtualFile,
                fallbackTexture = { environmentTexture }
            ),
            irradianceTextureProvider = listOfNotNull(
                material.reflectionMap?.takeIf { material.reflectionMapType == MtlReflectionType.SPHERE }
            ).firstOrNull()?.let { filename ->
                GeneratedTextureProvider(
                    key = "$filename\$irradiance",
                    bufferedImageProvider = DiffuseIrradianceBufferedImageProvider(
                        project = material.project,
                        environmentTextureFilename = filename,
                        relativeTo = material.containingFile?.virtualFile
                    ),
                    fallbackTexture = { irradianceTexture }
                )
            } ?: FallbackTextureProvider { irradianceTexture },
            reflectionTextureProviders = listOfNotNull(
                material.reflectionMap?.takeIf { material.reflectionMapType == MtlReflectionType.SPHERE }
            ).firstOrNull()?.let { filename ->
                (1 until TextureResources.REFLECTION_LEVELS_COUNT).map { index ->
                    GeneratedTextureProvider(
                        key = "$filename\$reflection$index",
                        bufferedImageProvider = PreFilteredEnvironmentBufferedImageProvider(
                            project = material.project,
                            environmentTextureFilename = filename,
                            relativeTo = material.containingFile?.virtualFile,
                            roughness = index.toFloat() / (TextureResources.REFLECTION_LEVELS_COUNT - 1).toFloat()
                        ),
                        fallbackTexture = { reflectionTextureLevels[index] }
                    )
                }
            } ?: (1 until TextureResources.REFLECTION_LEVELS_COUNT).map { index ->
                FallbackTextureProvider { reflectionTextureLevels[index] }
            }
        )

        constructor() : this(
            ambientTextureProvider = fallbackTextureProvider,
            diffuseTextureProvider = fallbackTextureProvider,
            specularTextureProvider = fallbackTextureProvider,
            emissionTextureProvider = fallbackTextureProvider,
            specularExponentTextureProvider = fallbackTextureProvider,
            roughnessTextureProvider = fallbackTextureProvider,
            metalnessTextureProvider = fallbackTextureProvider,
            normalmapTextureProvider = fallbackNormalmapProvider,
            displacementTextureProvider = fallbackTextureProvider,
            environmentTextureProvider = FallbackTextureProvider { environmentTexture },
            irradianceTextureProvider = FallbackTextureProvider { irradianceTexture },
            reflectionTextureProviders = (1 until TextureResources.REFLECTION_LEVELS_COUNT).map { index ->
                FallbackTextureProvider { reflectionTextureLevels[index] }
            }
        )

        val hasEmission get() = !emissionTextureProvider.isFallback
        val hasEnvironment get() = !environmentTextureProvider.isFallback

        fun ambientTexture(gl: GlimpseAdapter): Texture = ambientTextureProvider.get(gl)
        fun diffuseTexture(gl: GlimpseAdapter): Texture = diffuseTextureProvider.get(gl)
        fun specularTexture(gl: GlimpseAdapter): Texture = specularTextureProvider.get(gl)
        fun emissionTexture(gl: GlimpseAdapter): Texture = emissionTextureProvider.get(gl)
        fun specularExponentTexture(gl: GlimpseAdapter): Texture = specularExponentTextureProvider.get(gl)
        fun roughnessTexture(gl: GlimpseAdapter): Texture = roughnessTextureProvider.get(gl)
        fun metalnessTexture(gl: GlimpseAdapter): Texture = metalnessTextureProvider.get(gl)
        fun normalmapTexture(gl: GlimpseAdapter): Texture = normalmapTextureProvider.get(gl)
        fun displacementTexture(gl: GlimpseAdapter): Texture = displacementTextureProvider.get(gl)
        fun environmentTexture(gl: GlimpseAdapter): Texture = environmentTextureProvider.get(gl)
        fun irradianceTexture(gl: GlimpseAdapter): Texture = irradianceTextureProvider.get(gl)
        fun reflectionTextures(gl: GlimpseAdapter): List<Texture> = reflectionTextureProviders.map { it.get(gl) }

        fun prepare() {
            ambientTextureProvider.prepare()
            diffuseTextureProvider.prepare()
            specularTextureProvider.prepare()
            emissionTextureProvider.prepare()
            specularExponentTextureProvider.prepare()
            roughnessTextureProvider.prepare()
            metalnessTextureProvider.prepare()
            normalmapTextureProvider.prepare()
            displacementTextureProvider.prepare()
            environmentTextureProvider.prepare()
            irradianceTextureProvider.prepare()
            for (textureProvider in reflectionTextureProviders) {
                textureProvider.prepare()
            }
        }
    }

    companion object {
        private const val ENVIRONMENT_CUBE_RATIO = 5f

        private const val AXIS_LENGTH_FACTOR = 2f
        private const val AXIS_LABEL_DISTANCE_FACTOR = 1.1f
        private const val AXIS_X_LABEL = "x"
        private const val AXIS_Y_LABEL = "y"
        private const val AXIS_Z_LABEL = "z"

        private const val GRID_ALPHA = 0.3f
        private const val FINE_GRID_ALPHA = 0.1f
    }
}
