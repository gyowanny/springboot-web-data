package uk.co.gyotools.healthmetrics.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/private")
@Api(value="status", description="Application health check operations")
public class StatusController {

    @RequestMapping(path="/status", method = GET)
    @ApiOperation(value = "View the status of the application", response = String.class)
    public String status() {
        return "OK";
    }
}
