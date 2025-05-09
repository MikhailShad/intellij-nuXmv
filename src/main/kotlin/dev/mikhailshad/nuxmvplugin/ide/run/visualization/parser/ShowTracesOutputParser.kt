package dev.mikhailshad.nuxmvplugin.ide.run.visualization.parser

import com.intellij.openapi.diagnostic.Logger
import dev.mikhailshad.nuxmvplugin.ide.run.visualization.model.CounterexampleState
import dev.mikhailshad.nuxmvplugin.ide.run.visualization.model.CounterexampleTrace

/**
 * Парсер для вывода команды show_traces в nuXmv.
 * Поддерживает различные форматы вывода, включая HTML-комментарии.
 */
class ShowTracesOutputParser : CommandOutputParser {
    companion object {
        private val logger = Logger.getInstance(ShowTracesOutputParser::class.java)

        // Регулярные выражения для разбора вывода команды show_traces
        private val TRACE_START_REGEX = Regex("(\\*\\*\\* |<!-- #+\\s*)Trace number:\\s*(\\d+)(\\s*#+ -->)?")
        private val TRACE_DESCRIPTION_REGEX = Regex("Trace\\s*[Dd]escription:\\s*(.+)")
        private val TRACE_TYPE_REGEX = Regex("Trace\\s*[Tt]ype:\\s*(.+)")
        private val STATE_REGEX = Regex("\\s*->\\s*State:\\s*(\\d+(\\.\\d+))?\\s*<-")
        private val VAR_REGEX = Regex("\\s*(.+?)\\s*=\\s*(.+)")
        private val LOOP_REGEX = Regex("\\s*--\\s*Loop\\s+starts\\s+here")
    }

    override fun parseOutput(output: String): List<CounterexampleTrace> {
        val traces = mutableListOf<CounterexampleTrace>()

        // Если вывод пустой, сразу возвращаем пустой список
        if (output.isBlank()) {
            return traces
        }

        logger.debug("Parsing show_traces output, length=${output.length}")

        val lines = output.lines()
        var i = 0

        while (i < lines.size) {
            try {
                val line = lines[i].trim()

                // Ищем начало нового трейса
                val traceStartMatch = TRACE_START_REGEX.find(line)
                if (traceStartMatch != null) {
                    val traceNumber = traceStartMatch.groupValues[2].toIntOrNull() ?: 0

                    logger.debug("Found trace start, number=$traceNumber at line $i: $line")

                    // Парсим метаданные трейса
                    val (traceMetadata, metadataEndIndex) = parseTraceMetadata(lines, i + 1)

                    // Парсим состояния трейса
                    val (states, statesEndIndex) = parseTraceStates(lines, metadataEndIndex)

                    if (states.isNotEmpty()) {
                        val trace = CounterexampleTrace(
                            specification = traceMetadata["description"] ?: "Unknown",
                            specType = traceMetadata["type"] ?: "Unknown",
                            states = states,
                            traceNumber = traceNumber
                        )

                        traces.add(trace)
                        logger.debug("Added trace with ${states.size} states")

                        // Перемещаем индекс на конец обработанных состояний
                        i = statesEndIndex
                    } else {
                        i++
                    }
                } else {
                    i++
                }
            } catch (e: Exception) {
                logger.warn("Error parsing line $i: ${e.message}")
                i++
            }
        }

        logger.info("Parsed ${traces.size} traces from show_traces output")
        return traces
    }

    /**
     * Парсит метаданные трейса (описание и тип)
     */
    private fun parseTraceMetadata(lines: List<String>, startIndex: Int): Pair<Map<String, String>, Int> {
        val metadata = mutableMapOf<String, String>()
        var i = startIndex

        // Установим ограничение на количество строк для поиска метаданных
        val maxSearchLines = minOf(i + 10, lines.size)

        while (i < maxSearchLines) {
            val line = lines[i].trim()

            // Ищем описание трейса
            val descMatch = TRACE_DESCRIPTION_REGEX.find(line)
            if (descMatch != null) {
                metadata["description"] = descMatch.groupValues[1].trim()
            }

            // Ищем тип трейса
            val typeMatch = TRACE_TYPE_REGEX.find(line)
            if (typeMatch != null) {
                metadata["type"] = typeMatch.groupValues[1].trim()
            }

            // Если нашли начало состояния, выходим из цикла
            if (STATE_REGEX.find(line) != null) {
                break
            }

            // Если нашли начало нового трейса, тоже выходим
            if (TRACE_START_REGEX.find(line) != null) {
                break
            }

            i++
        }

        // Если тип не найден явно, пытаемся определить его из описания
        if (!metadata.containsKey("type") && metadata.containsKey("description")) {
            val description = metadata["description"] ?: ""
            metadata["type"] = when {
                description.contains("CTL", ignoreCase = true) -> "CTL"
                description.contains("LTL", ignoreCase = true) -> "LTL"
                description.contains("Invariant", ignoreCase = true) -> "INVAR"
                else -> "Counterexample"
            }
        }

        return Pair(metadata, i)
    }

    /**
     * Парсит состояния трейса, включая переменные и маркеры циклов
     */
    private fun parseTraceStates(lines: List<String>, startIndex: Int): Pair<List<CounterexampleState>, Int> {
        val states = mutableListOf<CounterexampleState>()
        var i = startIndex

        var currentStateId = -1.0
        var currentVars = mutableMapOf<String, String>()
        var inLoopSection = false  // Флаг для отметки состояний в цикле

        while (i < lines.size) {
            val line = lines[i].trim()

            // Проверяем на метку начала цикла
            if (LOOP_REGEX.find(line) != null) {
                inLoopSection = true
                i++
                continue
            }

            // Проверяем на начало нового состояния
            val stateMatch = STATE_REGEX.find(line)
            if (stateMatch != null) {
                // Если у нас уже есть данные о состоянии, сохраняем их перед обновлением
                if (currentStateId >= 0 && currentVars.isNotEmpty()) {
                    states.add(CounterexampleState(currentStateId, inLoopSection, currentVars.toMap()))
                    currentVars = mutableMapOf()
                }
                currentStateId = stateMatch.groupValues[1].toDoubleOrNull() ?: 0.0

                logger.debug("Found state $currentStateId at line $i${if (inLoopSection) " (in loop)" else ""}")
            } else {
                val varMatch = VAR_REGEX.find(line)
                if (varMatch != null && varMatch.groupValues.size >= 3) {
                    val varName = varMatch.groupValues[1].trim()
                    val varValue = varMatch.groupValues[2].trim()

                    if (varName.isNotEmpty()) {
                        currentVars[varName] = varValue
                        logger.debug("Added variable $varName = $varValue to state $currentStateId")
                    }
                }
            }

            // Проверяем на условия выхода из цикла:
            // 1. Начало нового трейса
            // 2. Командная строка nuXmv (на 2+ строке после начала парсинга состояний)
            if (i > startIndex && (
                        TRACE_START_REGEX.find(line) != null ||
                                line.contains("<!-- #") ||
                                (line.contains("nuXmv >") && i > startIndex + 1)
                        )
            ) {
                logger.debug("Found exit condition at line $i: $line")
                break
            }

            i++
        }

        // Не забываем добавить последнее обработанное состояние
        if (currentStateId >= 0 && currentVars.isNotEmpty()) {
            states.add(CounterexampleState(currentStateId, inLoopSection, currentVars.toMap()))
        }

        return Pair(states, i)
    }
}
