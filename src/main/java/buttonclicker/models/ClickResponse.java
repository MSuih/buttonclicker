package buttonclicker.models;

public class ClickResponse {
    private final long clicks;
    private final String price;

    public ClickResponse(long clicks, String price) {
        this.clicks = clicks;
        this.price = price;
    }

    public ClickResponse(long clicks) {
        this(clicks, null);
    }

    public long getClicks() {
        return clicks;
    }

    public String getPrice() {
        return price;
    }
}
