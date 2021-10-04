package com.bdd.runner;
import cucumber.api.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
		features = {"src/test/resources/feature"},
		glue = {"com.bdd.api.stepdefinition"},
		plugin={"json:target/build/cucumber.json"},
		tags = {"@Block_prueba"}
		//monochrome=true
)

public class testrunner {

}