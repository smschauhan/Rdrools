\name{rulesSession}
\alias{rulesSession}

\title{
Creates a session of the rules engine
}
\description{
The rulesSession creates a session that interfaces between R and the Drools engine. The session is utilized by the runRules function for executing a data frame against a set of rules.
}
\usage{
rulesSession(rules, input.columns, output.columns)
}
\arguments{
 \item{rules}{
     a character vector consisting of lines read from a rules file of format drl(drools rules file)\cr
     This character vector is eventually collapsed into a character vector of length 1, so the way you read the file could potentially be just about anything
}
  \item{input.columns}{
     a character vector of a set of input column, for example\cr
     \code{input.columns<-c("name", "class", "grade", "email")} 
}
  \item{output.columns}{
	a character vector of a set of expected output columns, for example\cr
	\code{output.columns<-c("address", "subject", "body")}
}
}
\details{
An active drools rules session. This promotes re-usability of a session, i.e. you can utilize the same session repetitively for different data sets of the same format.
}
\value{
\item{rules.session.object }{Returns a session to the rules engine}
}
\author{
SMS Chauhan <smschauhan@gmail.com>
}
\note{
Please have a look at the examples provided in the "examples" section of the \code{\link{Rdrools}} man page. A sample data set and a set of rules have been supplied help you understand the package usage.
}

\seealso{
\code{\link{runRules}}, \code{\link{Rdrools}}
}
\examples{
library(Rdrools)
data(rules)
input.columns<-c("name", "class", "grade", "email")
output.columns<-c("address", "subject", "body")
rules.session<-rulesSession(rules, input.columns, output.columns)

## The function is currently defined as
function(rules,input.columns, output.columns)
{
	rules <- paste(rules, collapse="\n")
	input.columns<-paste(input.columns,collapse=",")
	output.columns<-paste(output.columns,collapse=",")
	droolsSession<-.jnew('org/math/r/drools/DroolsService',rules,input.columns, output.columns)
	return(droolsSession)
}
}
\keyword{ rulesSession }
\keyword{ runRules }
\keyword{ Rdrools }