package io.github.imagine.devit.TestIt.statements;

import static io.github.imagine.devit.TestIt.utils.TextUtils.*;

public record StmtMsg(String value) {

    public static StmtMsg given(String value) {
       return new StmtMsg(blue("GIVEN ") + value);
    }

    public static StmtMsg when(String value) {
        return new StmtMsg( blue("WHEN ") + value);
    }

    public static StmtMsg then(String value) {
        return new StmtMsg(blue("THEN ") + value);
    }

    public static StmtMsg and(String value) {
        return new StmtMsg("↳ "+ blue("AND ") + value);
    }
}
