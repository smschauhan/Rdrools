\name{rules}
\alias{rules}
\docType{data}
\title{
a set of rules, for the \code{\link{class}} dataset
}
\description{
a character vector of lines read using \code{readLines()} from a drools rule file(text files with drools rules)
}
\usage{data(rules)}
\format{
  The format is:
 chr [1:12] "import java.util.HashMap;" "global java.util.HashMap output;" "" ...
}
\details{
You could create a similar rules character vector by using \cr
\code{rules<-readLines("rules.drl")}
}
\examples{
data(rules)
str(rules)
}
\keyword{datasets}
