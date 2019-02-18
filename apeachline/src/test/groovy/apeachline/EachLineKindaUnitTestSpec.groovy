package apeachline


import anno.TestRailSuite
import anno.TestRailTest
import spock.lang.*

/**
 * @author  Alex Poddubnyi
 * Below test cases are valid for File object as well
 */
@TestRailSuite(testSuiteName = "a.Jfrog")
class EachLineKindaUnitTestSpec extends Specification {

	@TestRailTest(testCaseId =  "108706", testCaseName =  "Test case №1")
	def "Verify if eachLine method iterates through each line"(){
		
		given:
			def multiLineString = "\nline2\nline3"
//			def multiLineString = new File("src/test/resources/multilineFile.txt")
			def actualResultsList = []
			
		when: "Iterate through the given String"
			multiLineString.eachLine{actualResultsList << it}
							
		then: "Verify each element of the actualResultsList"
			actualResultsList == ["", "line2", "line3"]
	}

	@TestRailTest(testCaseId = "108707", testCaseName =  "Test case №2")
	def "new test for add"(){

		given:
		def multiLineString = "\nline2\nline3"
//			def multiLineString = new File("src/test/resources/multilineFile.txt")
		def actualResultsList = []

		when: "Iterate through the given String"
		multiLineString.eachLine{actualResultsList << it}

		then: "Verify each element of the actualResultsList"
		actualResultsList == ["", "line2", "line3"]
	}
	@TestRailTest(testCaseId = "108708", testCaseName =  "Test case №3")
	def "Check if it returns the last value returned by closure"(){
		
		given:
			def multiLineString = "\nThis is simple multiline string,\nthat I chose for simple test\nand this is a last line"
			
		when: "Iterate through the given String"
			def result = multiLineString.eachLine{it}
							
		then: "Verify the last value returned by the closure"
			result == "and this is a last line"
	}
	@TestRailTest(testCaseId = "108709", testCaseName =  "Test case №4")
	def "Test firstLine parameter default"(){
		
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
	@TestRailTest(testCaseId =  "108710", testCaseName =  "Test case #5")
	def "Test firstLine parameter custom"(){
		
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
