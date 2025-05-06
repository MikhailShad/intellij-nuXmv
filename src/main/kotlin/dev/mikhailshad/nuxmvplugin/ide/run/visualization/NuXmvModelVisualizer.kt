package dev.mikhailshad.nuxmvplugin.ide.run.visualization

import com.intellij.openapi.Disposable
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.jcef.JBCefBrowser
import com.intellij.ui.jcef.JBCefJSQuery
import org.cef.browser.CefBrowser
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * Visualizer for NuXmv model state transitions using JCEF (Java Chromium Embedded Framework)
 * and Cytoscape.js for visualization
 */
class NuXmvModelVisualizer(private val project: Project) : Disposable {
    private val logger = Logger.getInstance(NuXmvModelVisualizer::class.java)
    private val browser: JBCefBrowser = JBCefBrowser()
    private val panel = JPanel(BorderLayout())
    private val jsQuery: JBCefJSQuery = JBCefJSQuery.create(browser)
    private val stateAnalyzer = ModelStateAnalyzer(project)
    private val graphGenerator = ModelGraphGenerator()

    init {
        Disposer.register(this, browser)
        Disposer.register(this, jsQuery)

        panel.add(browser.component, BorderLayout.CENTER)

        setupBrowser()
    }

    private fun setupBrowser() {
        try {
            setupJsCallbacks()

            val htmlContent = getHtmlContent()
            logger.info("Loading visualization HTML, content size: ${htmlContent.length}")
            browser.loadHTML(htmlContent)
        } catch (e: Exception) {
            logger.error("Error setting up browser", e)
        }
    }

    fun getComponent(): JComponent = panel

    private fun getHtmlContent(): String {
        val stream = javaClass.classLoader.getResourceAsStream("visualization/model_graph.html")

        return if (stream != null) {
            stream.bufferedReader().use { it.readText() }
        } else {
            logger.warn("Model graph HTML not found in resources")
            "<html><body>Error: could not load visualization</body></html>"
        }
    }

    private fun setupJsCallbacks() {
        jsQuery.addHandler { msg ->
            logger.info("Message from JS: $msg")
            null
        }

        try {
            val cefBrowser: CefBrowser = browser.cefBrowser
            cefBrowser.executeJavaScript(
                """
                window.javaCallback = function(message) {
                    ${jsQuery.inject("message")}
                };
                """.trimIndent(),
                cefBrowser.url, 0
            )
        } catch (e: Exception) {
            logger.error("Error setting up JavaScript callbacks", e)
        }
    }

    fun visualizeModelFile(file: VirtualFile) {
        try {
            logger.info("Visualizing model file: ${file.path}")

            // Analyze the model using PSI to find state variables and transitions
            val stateVariables = stateAnalyzer.analyzeModelFile(file)
            logger.info("Found ${stateVariables.size} state variables")

            // Generate JSON graph representation
            val graphJson = graphGenerator.generateStateGraph(stateVariables)
            logger.info("Generated graph JSON (${graphJson.length} chars)")

            // Update visualization
            updateVisualization(graphJson)

        } catch (e: Exception) {
            logger.error("Error visualizing model file: ${file.path}", e)

            // Show empty graph
            updateVisualization("[]")
        }
    }

    fun clearVisualization() {
        updateVisualization("[]")
    }

    private fun updateVisualization(modelData: String) {
        try {
            logger.info("Updating visualization with model data (${modelData.length} chars)")

            // Properly escape the JSON for JavaScript
            val escapedData = modelData
                .replace("\\", "\\\\")
                .replace("'", "\\'")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")

            // Call the updateVisualization function in the JavaScript
            val script = """
                console.log("JavaScript update called from Java");
                try {
                    if (typeof updateVisualization === 'function') {
                        console.log("Calling updateVisualization function");
                        updateVisualization("$escapedData");
                    } else {
                        console.error("updateVisualization function not found");
                    }
                } catch(e) {
                    console.error("Error updating visualization:", e);
                }
            """.trimIndent()

            // Use a valid URL for JavaScript execution
            browser.cefBrowser.executeJavaScript(script, browser.cefBrowser.url, 0)

            // Force a layout refresh
            refreshVisualization()
        } catch (e: Exception) {
            logger.error("Error executing JavaScript in browser", e)
        }
    }

    /**
     * Attempt to fix visualization if it appears broken or unstable
     */
    fun refreshVisualization() {
        try {
            // Call the refreshVisualization function in the JavaScript
            val script = "if (typeof refreshVisualization === 'function') refreshVisualization();"
            browser.cefBrowser.executeJavaScript(script, browser.cefBrowser.url, 0)
        } catch (e: Exception) {
            logger.error("Error refreshing visualization", e)
        }
    }

    override fun dispose() {
        // Resources are disposed automatically through registration
    }
}
