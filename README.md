# CoCoSpoon: source-code level code coverage for java

> CoCoSpoon computes code coverage with source code instrumentation.

Code coverage is a metric representing the percentage of code executed. This metric is used when running test suites to measure the code covered by these suites. Some development methods like TDD will guarantee a good coverage by the fact that the tests are written before the code. 

CoCoSpoon's initial goal is to calculate the code coverage on a program running in a production environment.

## Usage

    CocoSpoon
       -i, --input-path     input project folder
       -o, --output-path    instrumented project destination
      [-c, --classpath      classpath] 

The JavaFX GUI is availaible in `./CoCoSpoon-ui`.


