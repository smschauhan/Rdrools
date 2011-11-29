\name{runRules}
\alias{runRules}
\title{
apply a set of rule transformations to a data frame
}
\description{
This function is the core of the Rdrools package. Rules are applied on an input data frame and the results are returned as the output of the function. The columns on which the rules need to be applied have to be provided explicitly. Additionally, the new columns that would be created based on the rules have to be provided explicitly as well.\cr 
The rules engine picks up a row from the dataframe, applies the transformation to it based on rules provided and saves the result in an output dataframe. 
}

\usage{
runRules(rules.session, input.df)
}
\arguments{
  \item{rules.session}{
  a session of the rules engine created using the the \code{\link{rulesSession}}
}
  \item{input.df}{
     a dataframe consisting of a set of rows you wish to transform, and columns you wish to use in the transformation
}
 
}
\details{
If you are not familiar with the drools file format, please have a look at the references provided in the \code{\link{Rdrools}} man page. More details on how conflicting rules are resolved using either salience or the Reete algorithm are also present in the references.
}
\value{
  \item{output.df }{a dataframe which is the result of transformations applied to the input dataframe(\code{input.df}), the columns being the list provided through the \code{output.columns} parameter in \code{\link{rulesSession}}.}
}

\author{
SMS Chauhan <smschauhan@gmail.com>
}
\section{Warning}{
\bold{Transformation policy}\cr
Transformations are applied row by row, iteratively. That is to say, all inputs required for a rule transformation should be present in columns as a part of that row itself. Each row should be considered independent of another; all input values required for a transformation should be available in that row itself. The expectation from rules engines are often misplaced.\cr
\bold{Column Mismatch} \cr
Please make sure that the list of output columns provided through the \code{output.columns} parameter is exhaustive. Any additional column which is created through the rules transformation but is not present in the list would inhibit proper functioning. In most cases, an error should be thrown.
}

\seealso{
 \code{\link{Rdrools}}, \code{\link{rulesSession}} 
}
\examples{
library(Rdrools)
data(class)
data(rules)
input.columns<-c("name", "class", "grade", "email")
output.columns<-c("address", "subject", "body")
rules.session<-rulesSession(rules, input.columns, output.columns)
output.df<-runRules(rules.session, class)

## The function is currently defined as
function(rules.session,input.df)
{
	conn<-textConnection('input.csv.string','w')
	write.csv(input.df,file=conn)
	close(conn)
	input.csv.string<-paste(input.csv.string, collapse="\n")
	output.csv.string <- .jcall(rules.session, "S", "execute",input.csv.string)
	conn <- textConnection(output.csv.string, "r")
	output.df<-read.csv(file=conn, header=T)
	close(conn)
	return(output.df)
}

}
\keyword{ runRules }
\keyword{ rulesSession }
\keyword{ Rdrools }