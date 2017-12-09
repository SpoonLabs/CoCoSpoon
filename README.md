# CoCoSpoon: source-code level code coverage for java

CoCoSpoon enables you to compute statement coverage on Java source code.
Code coverage is a metric representing the percentage of code executed. This metric is used when running test suites to measure the code covered by these suites.

CoCoSpoon uses [Spoon](https://github.com/INRIA/spoon/) to instrument the source code.
The idea is that the transformed program can be observed to notify a user of its coverage at any time of its execution.
This is a way to compute coverage in production directly.

[![alt tag](https://github.com/maxcleme/OPL-Rendu2/blob/master/report/images/youtubeVideo.png)](https://youtu.be/QUZK3erdiEM)

# Usage

    CocoSpoon
       -i, --input-path     input project folder
       -o, --output-path    instrumented project destination
      [-c, --classpath      classpath] 

A GUI is availaible on <https://github.com/SpoonLabs/CoCoSpoon-ui>

# Screenshots

![alt tag](https://github.com/maxcleme/OPL-Rendu2/blob/master/report/images/overall_view.png)
![alt tag](https://github.com/maxcleme/OPL-Rendu2/blob/master/report/images/package_view.png)
![alt tag](https://github.com/maxcleme/OPL-Rendu2/blob/master/report/images/couverture_classe.png)
