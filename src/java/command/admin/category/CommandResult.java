package command.admin.category;

import util.enums.CommandResultType;

public class CommandResult {
    private final String view;
    private final CommandResultType type;
    

    public CommandResult(String view, CommandResultType type) {
        this.view = view;
        this.type = type;
    }

    public String getView() {
        return view;
    }

    public CommandResultType getType() {
        return type;
    }
}
