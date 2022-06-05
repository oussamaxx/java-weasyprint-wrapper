![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white)
[![SonarCloud](https://img.shields.io/badge/Sonar%20cloud-F3702A?style=for-the-badge&logo=sonarcloud&logoColor=white)](https://sonarcloud.io/summary/new_code?id=oussamaxx_java-weasyprint-wrapper)

[![License](https://img.shields.io/github/license/oussamaxx/java-weasyprint-wrapper)]()
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=oussamaxx_java-weasyprint-wrapper&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=oussamaxx_java-weasyprint-wrapper)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=oussamaxx_java-weasyprint-wrapper&metric=coverage)](https://sonarcloud.io/summary/new_code?id=oussamaxx_java-weasyprint-wrapper)
# <img src="logo.svg" width="80" height="80" alt="what3words">&nbsp;WeasyPrint Java Wrapper

A Java wrapper to use the [WeasyPrint](https://docs.what3words.com/api/v3/) command line tool.

WeasyPrint is a smart solution helping web developers to create PDF documents. It turns simple HTML pages into gorgeous statistical reports, invoices, tickets…

From a technical point of view, WeasyPrint is a visual rendering engine for HTML and CSS that can export to PDF. It aims to support web standards for printing. WeasyPrint is free software made available under a BSD license.

## Requirements
WeasyPrint has to be installed and working to use this wrapper ([WeasyPrint installation documentation](https://weasyprint.readthedocs.io/en/latest/install.html)).

## Installation

The artifact is available through <a href="https://search.maven.org/search?q=g:com.what3words">Maven Central</a>.

### Maven

```xml
<dependency>
  <groupId>io.github.oussamaxx</groupId>
  <artifactId>java-weasyprint-wrapper</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```

### Gradle

```
implementation 'io.github.oussamaxx:java-weasyprint-wrapper:1.0-SNAPSHOT'
```
For SNAPSHOT versions add this repository in repositories .gradle file:
```
maven {
    url "https://s01.oss.sonatype.org/content/repositories/snapshots/"
}
```

## General Usage And Examples

```Java
// init wp
WeasyPrint wp = new WeasyPrint();
// set html source and save the result to a file "uwu_pdf.pdf"
wp.htmlFromString("<html><b>uwu</b></html>").writePDF("uwu_pdf.pdf");

```

#### choose your html sources format
```Java
// set html from url
wp.htmlFromURL("http://google.com")
// set html from String
wp.htmlFromString("<html><b>uwu</b></html>")
// set html from File
wp.htmlFromFile("file.html")

```
#### export result either to PDF (or PNG)

```Java
// write to PDF
wp.writePDF("uwu_pdf.pdf");

// write directly using the the weasyprint process it self
wp.writePDFAsDirect("uwu_pdf.pdf")
```

⚠️ Be careful PNG is no longer supported by WeasyPrint version 53.0+

from WeasyPrint Version 53.0 change logs :

    API changes:
    --format and --resolution options have been deprecated, PDF is the only output format supported.

But don't worry we still have the option to use PNG if you're using an old version of WeasyPrint:

```Java
// all you have to do is to init wp with legacy flag
WeasyPrint wp = new WeasyPrint(true);
// or
wp.setLegacy(true);
// generate your PNG :)
wp.htmlFromString("<html><b>uwu</b></html>").writePNG("uwu_png.png");
```

#### Adding parameters
[Check the WeasyPrint documentation for parameters you can use](https://doc.courtbouillon.org/weasyprint/latest/api_reference.html#command-line-api)
```Java
// init wp
WeasyPrint wp = new WeasyPrint();
// adding parameters can be done in diffrent ways
wp.addParams(new Param("--optimize-images"), new Param("--attachment", "test.txt"))
          .addParam("-a", "oui.pdf");

File saved_file  = wp.htmlFromURL("http://google.com").writePDF("test_google.pdf");

```

#### More examples

you can check more examples in the test files

### Special thanks
thanks to all the people behind the WeasyPrint project
also big thanks to  [jhonnymertz](https://github.com/jhonnymertz) this project was hugely inspired  by his  [java-wkhtmltopdf-wrapper](https://github.com/jhonnymertz/java-wkhtmltopdf-wrapper)

#### Other notes

while creating the WeasyPrint class I have tried to keep it as close as it can
to the original Python library :
- Like using "almost" similar method names
- Using method chaining
- ...

### License
This project is available under MIT Licence.

