package mockhttp;

class InvalidationCheckingHttpSession extends MockHttpSession {
    private boolean invalidated = false;

    @Override
    public void invalidate() {
        super.invalidate(); // Or just set the flag
        invalidated = true;
    }

    public boolean isInvalidated() {
        return invalidated;
    }
}