package apeachline


import anno.TestRailSuite
import anno.TestRailTest
import spock.lang.*

/**
 * @author  Alex Poddubnyi
 * Below test cases are valid for File object as well
 */
@TestRailSuite(testSuiteName = "TestRailTestCases.newSection.hierarchy")
class EachLineKindaUnitTestSpec extends Specification {

	@TestRailTest(testCaseName =  "Verify if eachLine method iterates through each line")
	def "Test case №1"(){
				given:
			def multiLineString = "\nline2\nline3"
//			def multiLineString = new File("src/test/resources/multilineFile.txt")
			def actualResultsList = []
			
		when: "Iterate through the given String"
			multiLineString.eachLine{actualResultsList << it}
							
		then: "Verify each element of the actualResultsList"
			actualResultsList == ["", "line2", "line3"]
	}

	@TestRailTest(testCaseName =  "Verify each element of the actualResultsList")
	def "Test case №2"(){

		given:
		def multiLineString = "\nline2\nline3"
//			def multiLineString = new File("src/test/resources/multilineFile.txt")
		def actualResultsList = []

		when: "Iterate through the given String"
		multiLineString.eachLine{actualResultsList << it}

		then: "Verify each element of the actualResultsList"
		actualResultsList == ["", "line2", "line3"]
	}
	@TestRailTest(testCaseName =  "Check if it returns the last value returned by closure")
	def "Test case №3"(){
		
		given:
			def multiLineString = "\nThis is simple multiline string,\nthat I chose for simple test\nand this is a last line"
			
		when: "Iterate through the given String"
			def result = multiLineString.eachLine{it}
							
		then: "Verify the last value returned by the closure"
			result == "and this is a last line"
	}
	@TestRailTest(testCaseName =  "Test firstLine parameter default")
	def "Test case №4"(){
		
		given:
			def actualString
			def defaultFirstLine = 0 // JavaDoc says '(default is 1, set to 0 to start counting from 0)' but it actually set to 0 by default
									//And there was an issue https://issues.apache.org/jira/browse/GROOVY-2905 
			def multiLineString = """This is simple multiline string,
										that I chose as a test data
										and add couple more lines
										to check firstLine parameter"""
			
		when: "Iterate through the given String"
			def result = multiLineString.eachLine{line, number ->
				if (number == defaultFirstLine){
					actualString = line
				}
			}
							
		then: "Verify first line "
			actualString == "This is simple multiline string,"
	}
	@TestRailTest(testCaseName =  "Test firstLine parameter custom")
	def "Test case №5"(){
		
		given:
			def actualString
			def firstLineNumber = 2
			def multiLineString = """This is simple multiline string,
										that I chose as a test data
										and add couple more lines
										to check firstLine parameter"""
			
		when: "Iterate through the given String"
			def result = multiLineString.eachLine(firstLineNumber){line, number ->
				if (number == firstLineNumber){
					actualString = line
				}
			}
							
		then: "Verify first line "
			actualString == "This is simple multiline string,"
	}


}
