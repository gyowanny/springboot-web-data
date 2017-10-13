package uk.co.gyotools.selfmetrics.async.handler;

import ratpack.handling.Context;
import ratpack.handling.Handler;
import uk.co.gyotools.selfmetrics.async.dao.SelfMetricDao;

import javax.inject.Inject;

public class CreateSelfMetricHandler implements Handler {
    private final SelfMetricDao selfMetricDao;

    @Inject
    public CreateSelfMetricHandler(SelfMetricDao selfMetricDao) {
        this.selfMetricDao = selfMetricDao;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        ctx.getResponse().status(201).send();
    }
}
