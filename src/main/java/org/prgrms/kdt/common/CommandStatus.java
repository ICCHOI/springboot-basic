package org.prgrms.kdt.common;

import org.prgrms.kdt.exception.InvalidArgumentException;

import java.util.Arrays;

public enum CommandStatus {
    EXIT("exit"),
    CREATE("create"),
    ALL("all");

    private final String status;

    CommandStatus(final String status) {
        this.status = status;
    }

    public static CommandStatus findByCommandType(String inputType) {
        return Arrays.stream(CommandStatus.values())
                .filter(c -> c.status.equals(inputType))
                .findAny()
                .orElseThrow(() -> new InvalidArgumentException(InvalidArgumentException.ErrorMessage.NOT_CORRECT_INPUT_MESSAGE));
    }

}
