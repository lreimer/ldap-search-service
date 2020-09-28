package cloud.nativ.quarkus;

import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TestableHealthCheckResponseBuilder extends HealthCheckResponseBuilder {

    private String name;
    private HealthCheckResponse.State state;
    private Map<String, Object> data = new HashMap<>();

    @Override
    public HealthCheckResponseBuilder name(String s) {
        this.name = s;
        return this;
    }

    @Override
    public HealthCheckResponseBuilder withData(String s, String s1) {
        data.put(s, s1);
        return this;
    }

    @Override
    public HealthCheckResponseBuilder withData(String s, long l) {
        data.put(s, l);
        return this;
    }

    @Override
    public HealthCheckResponseBuilder withData(String s, boolean b) {
        data.put(s, b);
        return this;
    }

    @Override
    public HealthCheckResponseBuilder up() {
        this.state = HealthCheckResponse.State.UP;
        return this;
    }

    @Override
    public HealthCheckResponseBuilder down() {
        this.state = HealthCheckResponse.State.DOWN;
        return this;
    }

    @Override
    public HealthCheckResponseBuilder state(boolean b) {
        if (b) {
            return up();
        } else {
            return down();
        }
    }

    @Override
    public HealthCheckResponse build() {
        return new HealthCheckResponse(name, state, Optional.of(data));
    }
}
