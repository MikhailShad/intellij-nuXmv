package dev.mikhailshad.nuxmvplugin.ide.run.visualization

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.JBColor
import com.intellij.ui.jcef.JBCefBrowser
import com.intellij.ui.jcef.JBCefJSQuery
import dev.mikhailshad.nuxmvplugin.ide.run.visualization.model.CounterexampleTrace
import dev.mikhailshad.nuxmvplugin.ide.run.visualization.model.ModelGraph
import io.github.oshai.kotlinlogging.KotlinLogging
import org.cef.browser.CefBrowser
import org.cef.browser.CefFrame
import org.cef.handler.CefLoadHandler
import org.cef.network.CefRequest
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * Component for visualizing NuXmv models and counterexample traces.
 * Uses JCEF browser with Cytoscape.js for graph visualization.
 */
class NuXmvModelVisualizer(private val project: Project) : Disposable {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    // Создаем браузер с прозрачным фоном
    private val browser: JBCefBrowser = JBCefBrowser()
    private val panel = JPanel(BorderLayout())
    private val jsQuery: JBCefJSQuery = JBCefJSQuery.create(browser)
    private val stateAnalyzer = ModelStateAnalyzer(project)

    // Флаг готовности страницы
    private var pageReady = false

    // Очередь отложенных обновлений
    private val pendingUpdates = java.util.concurrent.ConcurrentLinkedQueue<String>()

    private lateinit var modelGraph: ModelGraph
    private lateinit var modelGraphWithTraces: ModelGraph

    init {
        browser.component.preferredSize = java.awt.Dimension(600, 400)
        browser.component.minimumSize = java.awt.Dimension(300, 200)
        browser.component.isOpaque = false
        browser.component.background = JBColor.PanelBackground

        panel.preferredSize = java.awt.Dimension(600, 400)
        panel.minimumSize = java.awt.Dimension(300, 200)
        panel.border = null
        panel.isOpaque = false
        panel.background = JBColor.PanelBackground
        panel.add(browser.component, BorderLayout.CENTER)

        browser.component.addComponentListener(object : java.awt.event.ComponentAdapter() {
            override fun componentResized(e: java.awt.event.ComponentEvent) {
                logger.debug { "Browser component resized" }
                sendCommandToJs("resize")
            }
        })

        setupBrowser()
    }

    /**
     * Отправляет команду в JavaScript для управления визуализацией
     */
    private fun sendCommandToJs(command: String, data: Map<String, Any> = emptyMap()) {
        try {
            val dataJson = if (data.isNotEmpty()) {
                ", ${data.entries.joinToString(", ") { "\"${it.key}\": ${it.value}" }}"
            } else {
                ""
            }

            val script = """
                if (typeof window.handleCommand === 'function') {
                    window.handleCommand("$command"$dataJson);
                } else {
                    console.error("handleCommand function is not available");
                }
            """.trimIndent()

            browser.cefBrowser.executeJavaScript(script, browser.cefBrowser.url, 0)
        } catch (e: Exception) {
            logger.error(e) { "Error sending command to JavaScript: $command" }
        }
    }

    private fun setupBrowser() {
        try {
            setupJsCallbacks()
            val htmlContent = getHtmlContent()
            browser.loadHTML(htmlContent)

            // Добавим обработчик загрузки страницы
            browser.jbCefClient.addLoadHandler(object : CefLoadHandler {
                override fun onLoadingStateChange(
                    browser: CefBrowser,
                    isLoading: Boolean,
                    canGoBack: Boolean,
                    canGoForward: Boolean
                ) {
                    // do nothing
                }

                override fun onLoadStart(
                    p0: CefBrowser?,
                    p1: CefFrame?,
                    p2: CefRequest.TransitionType?
                ) {
                    // do nothing
                }

                override fun onLoadEnd(p0: CefBrowser?, p1: CefFrame?, p2: Int) {
                    pageReady = true
                    processPendingUpdates()
                }

                override fun onLoadError(
                    p0: CefBrowser?,
                    p1: CefFrame?,
                    p2: CefLoadHandler.ErrorCode?,
                    p3: String?,
                    p4: String?
                ) {
                    // do nothing
                }
            }, browser.cefBrowser)
        } catch (e: Exception) {
            logger.error(e) { "Error setting up browser" }
            e.printStackTrace()
        }
    }

    fun getComponent(): JComponent = panel

    private fun getHtmlContent(): String {
        val stream = javaClass.classLoader.getResourceAsStream("visualization/model_graph.html")
        return stream?.bufferedReader()?.use { it.readText() }
            ?: "<html><body>Error: could not load visualization</body></html>"
    }

    /**
     * Обрабатывает все отложенные обновления визуализации
     */
    private fun processPendingUpdates() {
        if (!pageReady) {
            return
        }

        while (pendingUpdates.isNotEmpty()) {
            val modelData = pendingUpdates.poll()
            if (modelData != null) {
                doUpdateVisualization(modelData)
            }
        }
    }

    private fun setupJsCallbacks() {
        jsQuery.addHandler { msg ->
            logger.info { "Message from JS: $msg" }
            try {
                return@addHandler JBCefJSQuery.Response("Received message: $msg")
            } catch (e: Exception) {
                logger.error(e) { "Error processing message from JS: $msg" }
                return@addHandler JBCefJSQuery.Response("Error: ${e.message}")
            }
        }

        try {
            val cefBrowser: CefBrowser = browser.cefBrowser
            cefBrowser.executeJavaScript(
                """
                window.javaCallback = function(message) {
                    ${jsQuery.inject("message")}
                };

                // Helper function to call Java with structured data
                window.callJava = function(action, data) {
                    const message = {
                        action: action,
                        ...data
                    };
                    ${jsQuery.inject("JSON.stringify(message)")}
                };
                """.trimIndent(),
                cefBrowser.url, 0
            )
        } catch (e: Exception) {
            logger.error(e) { "Error setting up JavaScript callbacks" }
        }
    }


    /**
     * Visualize a model file
     */
    fun visualizeModelFile(file: VirtualFile) {
        logger.info { "Request to visualize model file: ${file.path}" }
        if (!this::modelGraph.isInitialized) {
            modelGraph = stateAnalyzer.analyzeModelFile(file)
        }
        updateVisualization(modelGraph.toJson().toString())
    }

    /**
     * Visualize multiple counterexample traces
     */
    fun visualizeTraces(traces: List<CounterexampleTrace>) {
        try {
            if (traces.isEmpty()) {
                logger.warn { "No traces to visualize" }
                return
            }

            if (!::modelGraph.isInitialized) {
                logger.warn { "No model available for visualization" }
                return
            }

            if (!this::modelGraphWithTraces.isInitialized) {
                modelGraphWithTraces = modelGraph.copy(traces = traces)
            }

            updateVisualization(modelGraphWithTraces.toJson().toString())
        } catch (e: Exception) {
            logger.error(e) { "Error visualizing multiple traces" }
        }
    }


    /**
     * Visualize counterexample traces
     */
    fun visualizeCounterexampleTraces(traces: List<CounterexampleTrace>) {
        if (traces.isEmpty()) {
            logger.info { "No counterexample traces to visualize" }
            return
        }

        visualizeTraces(traces)
    }

    /**
     * Update the visualization with new data
     */
    private fun updateVisualization(modelData: String) {
        if (!pageReady) {
            pendingUpdates.add(modelData)
            return
        }

        doUpdateVisualization(modelData)
    }

    /**
     * Непосредственное обновление визуализации
     */
    private fun doUpdateVisualization(modelData: String) {
        try {
            logger.info { "Updating visualization with model data (${modelData.length} chars)" }
            val base64Data = java.util.Base64.getEncoder().encodeToString(modelData.toByteArray())
            val script = """
                console.time("visualize");
                console.log("Received Base64 data from Java, length: ${base64Data.length}");
                try {
                    const jsonString = atob("$base64Data");
                    console.log("Decoded Base64 data, length:", jsonString.length);
                    window.updateVisualization(jsonString);
                    console.log("Visualization update complete");
                } catch(e) {
                    console.error("Error updating visualization:", e);
                    console.error("Error details:", e.toString());
                    if (e.stack) console.error("Stack trace:", e.stack);
                }
                console.timeEnd("visualize");
            """.trimIndent()

            browser.cefBrowser.executeJavaScript(script, browser.cefBrowser.url, 0)
        } catch (e: Exception) {
            logger.error(e) { "Error executing JavaScript in browser" }
            e.printStackTrace()
        }
    }

    /**
     * Refresh visualization - публичный метод для обновления визуализации извне
     */
    fun refreshVisualization() {
        if (pageReady) {
            sendCommandToJs("refresh")
        }
    }

    /**
     * Fit graph to view - публичный метод для подгонки графа под размер окна
     */
    fun fitGraphToView() {
        if (pageReady) {
            sendCommandToJs("fit")
        }
    }

    /**
     * Dispose of resources
     */
    override fun dispose() {
        browser.dispose()
        jsQuery.dispose()
    }
}
