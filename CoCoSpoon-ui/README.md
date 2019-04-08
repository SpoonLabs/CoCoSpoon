<p align="center">
<img width="10%" src="https://github.com/maxcleme/OPL-Rendu2/blob/master/report/images/logoCoCoSpoon.png" />
</p>

# CoCoSpoon-ui

This project is the graphical part of [CoCoSpoon](https://github.com/SpoonLabs/CoCoSpoon.git) 

## Install

Prerequisite: use a JVM with JavaFX installed.

    cat $JAVA_HOME/jre/lib/javafx.properties

To Install CoCoSpoon-Ui :

```
git clone http://github.com/SpoonLabs/CoCoSpoon.git
cd CoCoSpoon/CoCoSpoon-ui
sh install.sh
```


## Run

### Example

You can run as example commons-lang by running the following script:
```sh
./example_commons-lang.sh
```

### Your own project 

You can run CoCoSpoon-Ui on your project with:
```sh
mvn exec:java -Dexec.mainClass="Main" -Dexec.args="-i <pathToMyProject> -o <pathToMyOutput> -v OVERALL"
```
and by replacing `<pathToMyProject>` by the path to the root directory of your project, and 
`<pathToMyOutput>` by the desired location of the output. The output can be the same as your input, but CoCoSpoon will erase old version by the instrumented one.

To the real time coverage, just run your test suite in your favorite IDE. 

### Options

    CocoSpoon
       -i, --input-path     input project folder
       -o, --output-path    instrumented project destination
      [-c, --classpath      classpath]
      [-v, --view-type      TEXT | OVERALL | INTERACTIVE]

### Screencast

[![alt tag](https://github.com/maxcleme/OPL-Rendu2/blob/master/report/images/youtubeVideo.png)](https://youtu.be/QUZK3erdiEM)


![alt tag](https://github.com/maxcleme/OPL-Rendu2/blob/master/report/images/overall_view.png)
![alt tag](https://github.com/maxcleme/OPL-Rendu2/blob/master/report/images/package_view.png)
![alt tag](https://github.com/maxcleme/OPL-Rendu2/blob/master/report/images/couverture_classe.png)
