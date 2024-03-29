\name{Rdrools-package}
\alias{Rdrools-package}
\alias{Rdrools}
\docType{package}
\title{
a rules engine for R based on the popular Java Drools engine
}
\description{
Rdrools is a robust rules engine based on the popular rules engine Drools written in Java. Rules make it easy to express solutions to different problems. They are often expressed as declarative elements of logic and are held in a file repository. In Rdrools, rules are written in the native Drools language. If you are not familiar, please go through the Drools documentation provided in the list of references below.
}
\details{
\tabular{ll}{
Package: \tab Rdrools\cr
Type: \tab Package\cr
Version: \tab 0.0.3\cr
Date: \tab 2012-10-23\cr
License: \tab LGPL-3\cr
LazyLoad: \tab yes\cr
LazyData: \tab yes\cr
}
Rdrools is fairly straightforward to use. It expects the user to provide a data frame as an input, a rules file, and a comma separated list of input and output columns. Rdrools then picks a row from the data frame, applies the rules provided to it and maps the output to the list of expected output columns. Incorrect inputs would throw a meaningful error.
}
\author{
SMS Chauhan <smschauhan@gmail.com>,  Zubin Dowlaty <zubin@dowlaty.com>

Maintainer: SMS Chauhan <smschauhan@gmail.com>
}
\references{
Wikipedia:\url{http://en.wikipedia.org/wiki/Drools}\cr
Drools Documentation:\url{http://docs.jboss.org/drools/release/5.3.0.Final/drools-expert-docs/html_single/index.html}
}
\note{
For applying a conditional rule, you can use the eval statement in your rules syntax. Please see a detailed example below.
\preformatted{
import  java.util.HashMap;
global  java.util.HashMap  output;
rule"xx"
dialect"mvel"
        salience3
        when
                input:HashMap()
                eval(input.get("currentAction")==input.get("previousAction"))
        then
                output.put("send","false")
end
rule"xy"
dialect"mvel"
        salience2
        when
                input:HashMap()
                eval(input.get("currentAction")!=input.get("previousAction"))
        then
                output.put("send","true")
end
}
}

\keyword{ rulesSession }
\keyword{ runRules }
\keyword{ Rdrools }
\seealso{
 \code{\link{rulesSession}}, \code{\link{runRules}} 
}
\examples{
	library(Rdrools)
	data(class)
	data(rules)
	input.columns<-c("name", "class", "grade", "email")
	output.columns<-c("address", "subject", "body")
	rules.session<-rulesSession(rules, input.columns, output.columns)
	output.df<-runRules(rules.session, class)
}
