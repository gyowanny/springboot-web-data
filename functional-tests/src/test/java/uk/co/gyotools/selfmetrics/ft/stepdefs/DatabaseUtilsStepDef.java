package uk.co.gyotools.selfmetrics.ft.stepdefs;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;

public class DatabaseUtilsStepDef extends AbstractStepDef {
    @Given("^database is empty$")
    public void databaseIsEmpty() throws Throwable {
        cleanupDatabase();
    }
}
