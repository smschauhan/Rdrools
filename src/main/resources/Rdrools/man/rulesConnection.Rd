\name{rulesConnection}
\alias{rulesConnection}

\title{
Creates a connection to the rules engine
}
\description{
The rulesConnection creates a connection that interfaces between R and the Drools engine. The connection is utilized by the runRules function for executing a data frame against a set of rules.
}
\usage{
rulesConnection()
}

\details{
A rules connection maintains an active drools session. This promotes reusability of a session, i.e. you can utilize the same connection repetitively to either process different data sets with the same set of rules or process the same dataset with a different set of rules. This provides you the flexibility to use Rdrools with agility.
}
\value{
\item{rules.connection.object }{Returns a connection to the rules engine}
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
rule.connection<-rulesConnection()

## The function is currently defined as
function () 
{
    drools <- .jnew("org/math/r/drools/DroolsService")
    return(drools)
  }
}
\keyword{ rulesConnection }
\keyword{ runRules }
\keyword{ Rdrools }