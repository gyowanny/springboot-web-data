package uk.co.gyotools.selfmetrics.ft.state;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.stereotype.Component;
import uk.co.gyotools.selfmetrics.ft.model.SelfMetric;

@Component
public class SelfMetricState {

    private SelfMetric selfMetric;

    public SelfMetric getSelfMetric() {
        return selfMetric;
    }

    public void setSelfMetric(SelfMetric selfMetric) {
        this.selfMetric = selfMetric;
    }
}
