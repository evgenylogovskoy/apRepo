package apeachline

import anno.TestRail
import spock.lang.*

/**
 * @author  Alex Poddubnyi
 * Below test cases are valid for File object as well
 */

class EachLineKindaUnitTestSpec extends Specification {

	@TestRail(testCaseId =  "108343")
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
	@TestRail(testCaseId =  "108344")
	def "Check if it returns the last value returned by closure"(){
		
		given:
			def multiLineString = "\nThis is simple multiline string,\nthat I chose for simple test\nand this is a last line"
			
		when: "Iterate through the given String"
			def result = multiLineString.eachLine{it}
							
		then: "Verify the last value returned by the closure"
			result == "and this is a last line"
	}
	@TestRail(testCaseId =  "108345")
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
	@TestRail(testCaseId =  "108345")
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
