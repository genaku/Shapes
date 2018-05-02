package com.gena.domain.model

class ShapeException(code: Int) : Exception(
        if (code in 0 until MESSAGES.size)
            MESSAGES[code]
        else
            MESSAGES[0]
) {
    companion object {

        const val COMMAND_EXECUTED = 1
        const val COMMAND_NOT_EXECUTED = 2
        const val UNDO_NOT_AVAILABLE = 3
        const val REDO_NOT_AVAILABLE = 4

        const val INDEX_OUT_OF_MODEL_LIST = 5

        private val MESSAGES = arrayOf(
                "undefined",
                "Command is already done",
                "Command isn't done yet",
                "Undo is not available",
                "Redo is not available",
                "Index is out of model list"
        )

    }

}
