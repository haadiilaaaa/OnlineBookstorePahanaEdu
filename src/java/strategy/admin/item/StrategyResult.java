package strategy.admin.item;

public class StrategyResult {
    private final String view;
    private final boolean redirect;

    public StrategyResult(String view, boolean redirect) {
        this.view = view;
        this.redirect = redirect;
    }

    public String getView() {
        return view;
    }

    public boolean isRedirect() {
        return redirect;
    }
}
