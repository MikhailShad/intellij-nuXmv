package dev.mikhailshad.nuxmvplugin.ide.run.visualization.parser

import dev.mikhailshad.nuxmvplugin.ide.run.visualization.model.CounterexampleTrace

/**
 * Базовый интерфейс для парсеров вывода команд nuXmv.
 */
interface CommandOutputParser {
    /**
     * Парсит вывод команды и возвращает список трейсов.
     *
     * @param output Текстовый вывод команды nuXmv
     * @return Список трейсов контрпримеров, извлеченных из вывода
     */
    fun parseOutput(output: String): List<CounterexampleTrace>
}
