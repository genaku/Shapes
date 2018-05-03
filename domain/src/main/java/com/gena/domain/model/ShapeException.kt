package com.gena.domain.model

class ShapeException(error: ShapeExceptionError) : Exception(MESSAGES[error.ordinal]) {
    companion object {
        private val MESSAGES = arrayOf(
                "Command is already done",
                "Command isn't done yet",
                "Undo is not available",
                "Redo is not available",
                "Index is out of model list"
        )
    }
}

enum class ShapeExceptionError {
    COMMAND_EXECUTED,
    COMMAND_NOT_EXECUTED,
    UNDO_NOT_AVAILABLE,
    REDO_NOT_AVAILABLE,
    INDEX_OUT_OF_MODEL_LIST
}