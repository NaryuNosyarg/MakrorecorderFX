package de.saxsys.MakrorecorderFX.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Singleton;


@Singleton
public class TestCaseManagement {

	
	private List<TestCase> testCaseList = new ArrayList<TestCase>();

	public List<TestCase> getTestCaseList() {
		return Collections.unmodifiableList(testCaseList);
	}

public TestCaseManagement(){
	
	testCaseList.add(new TestCase("01", "TestFall 1", "Beschreibung"));
}

}
