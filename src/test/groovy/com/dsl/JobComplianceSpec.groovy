package com.dslexample

import com.dslexample.support.TestUtil
import hudson.model.Item
import hudson.model.View
import javaposse.jobdsl.dsl.DslScriptLoader
import javaposse.jobdsl.dsl.GeneratedItems
import javaposse.jobdsl.dsl.GeneratedJob
import javaposse.jobdsl.dsl.GeneratedView
import javaposse.jobdsl.dsl.JobManagement
import javaposse.jobdsl.plugin.JenkinsJobManagement
import jenkins.model.Jenkins
import org.junit.ClassRule
import org.jvnet.hudson.test.JenkinsRule
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Tests that all dsl scripts in the jobs directory do not contain any restricted strings / commands
 *
 * This runs against the jenkins test harness. Plugins providing auto-generated DSL must be added to the build dependencies.
 */
class JobComplianceSpec extends Specification {

    @Shared
    @ClassRule
    private JenkinsRule jenkinsRule = new JenkinsRule()

    @Shared
    private File outputDir = new File('./build/debug-xml')

    def setupSpec() {
        outputDir.deleteDir()
    }

    @Unroll
    void 'check no restricted patterns in script #file.name'(File file) {
        when:
            def jobText = file.text

            def prohibitedPatternFilePath = "prohibited.patterns"
            def prohibitedPatternFile = new File(prohibitedPatternFilePath)
            def prohibitedPatterns = []
            if (prohibitedPatternFile.exists()) {
                prohibitedPatternFile.eachLine { line ->
                    prohibitedPatterns << line
                }
            } else {
                def defaultProhibitedPatterns = [".*rm.*-r"]
                defaultProhibitedPatterns.each { p ->
                    prohibitedPatterns << p
                }
            }

        then:
          def matchProhibitedPatterns = []
          def result = ""
          prohibitedPatterns.each { p ->
              jobText.eachLine { l ->
                  if (l =~ /${p}/) {
                      result = "ERROR - Prohibited Pattern ${p} found in ${file}!"
                      matchProhibitedPatterns << result
                  }
              }
          }
          assert matchProhibitedPatterns.size() == 0
        where:
            file << TestUtil.getJobFiles()
    }
}
