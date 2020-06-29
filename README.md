# Jenkins Job DSL Gradle Example

# ToDo's

TODO: Add section about secret setup and mgmt - how will it work for people building new jobs - do they just have access in non-prod? - how do we get creds setup in prod jenkins?
TODO: Need a linting class added to gradle plugin to enforce standards
TODO: need to publish all quality elements as a gradle plugin - https://guides.gradle.org/publishing-plugins-to-gradle-plugin-portal/

An example [Job DSL](https://github.com/jenkinsci/job-dsl-plugin) project that uses Gradle for building and testing. Check out [this presentation](https://www.youtube.com/watch?v=SSK_JaBacE0) for a walkthrough of this example (starts around 14:00).

## File structure

    .
    ├── src
    │   ├── jobs                # DSL script files
    │   ├── main
    │   │   ├── groovy          # support classes
    │   │   └── resources
    │   │       └── idea.gdsl   # IDE support for IDEA
    │   ├── scripts             # scripts to use with "readFileFromWorkspace"
    │   └── test
    │       └── groovy          # specs
    └── build.gradle            # build file

# Script Examples

* [Example 1](src/jobs/example1Jobs.groovy) - shows basic folder/job creation
* [Example 2](src/jobs/example2Jobs.groovy) - shows how to create a set of jobs for each github branch, each in its own folder
* [Example 3](src/jobs/example3Jobs.groovy) - shows how to use the configure block
* [Example 4](src/jobs/example4Jobs.groovy) - shows a way to reuse job definitions for jobs that differ only with a few properties
* [Example 5](src/jobs/example5Jobs.groovy) - shows how to pull out common components into static methods
* [Example 6](src/jobs/example6Jobs.groovy) - shows how to include script resources from the workspace
* [Example 7](src/jobs/example7Jobs.groovy) - shows how to create jobs using builders
* [Example 8](src/jobs/example8Jobs.groovy) - shows how to use DSL extensions provided by other plugins

***NOTE: These examples should be removed before submission of PR***

## Quick Start

1. Fork this repo
2. Author any desired jobs in the src/jobs directory
  - Jobs can be declarative or scripted pipeline syntax
  - insure you are using a node designation (the 'correct' value can be obtained from the Tool DevOps team)
3. Add any desired tests in the src/test/groovy/com/dsl directory - create a separate .groovy file(s) to
3. Conduct any validation runs on dev jenkins instance
4. Fill out the sample src/jobs/jobs.manifest with the necessary metadata - it should be self-explanatory, and where it isn't there are comments
5. Run Tests `gradle clean test`
6. once all tests are
## Testing

`gradle clean test` runs the specs.

[JobScriptsSpec](src/test/groovy/com/dslexample/JobScriptsSpec.groovy)
will loop through all DSL files and make sure they don't throw any exceptions when processed. All XML output files are written to `build/debug-xml`.
This can be useful if you want to inspect the generated XML before check-in. Plugins providing auto-generated DSL must be added to the build dependencies.

If you prefer to stub the auto-generated DSL, you can use [JobScriptsSpecAlternative](src/test/groovy/com/dslexample/JobScriptsSpecAlternative.groovy),
though you may miss some syntax errors.

## Seed Job

If you have appropriate access to the Jenkins instance, You can create the needed seed job via the Rest API Runner (see below) using the pattern `jobs/seed.groovy`.

Or manually create a job with the same structure:

* Invoke Gradle script
   * Use Gradle Wrapper: `true`
   * Tasks: `clean test`
* Process Job DSLs
   * DSL Scripts: `src/jobs/**/*Jobs.groovy`
   * Additional classpath: `src/main/groovy`
* Publish JUnit test result report
   * Test report XMLs: `build/test-results/**/*.xml`

Note that starting with Job DSL 1.60 the "Additional classpath" setting is not available when
[Job DSL script security](https://github.com/jenkinsci/job-dsl-plugin/wiki/Script-Security) is enabled.

***NOTE: The creation of a seed job in the proposed model would be done by the CI / CD admin / DevOps team this repo is a template for providing pipeline jobs to the set of jobs installed in the running Jenkins***

## Other Resources

* [Job DSL Playground](http://job-dsl.herokuapp.com/) - App for debugging Job DSL scripts.
* [Job DSL API Viewer](https://jenkinsci.github.io/job-dsl-plugin/) - Full Job DSL syntax reference.
* [Job DSL REST Example](https://github.com/sheehan/job-dsl-rest-example) - Example that shows how to make Job DSL updates via the Jenkins REST API. Originally part of this repo, later split off.

#### Gradle Plugins
Plugins that automatically apply the functionality laid out in this example repo.
* [Gradle Job DSL Support Plugin](https://github.com/AOEpeople/gradle-jenkins-job-dsl-plugin) - Plugin for easy management of Jenkins Job DSL scripts with Gradle.
* [Gradle Jenkins Job DSL Plugin](https://github.com/heremaps/gradle-jenkins-jobdsl-plugin) - Plugin to manage Jenkins Job DSL projects in a Gradle project.
