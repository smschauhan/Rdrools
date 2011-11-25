\name{class}
\alias{class}
\docType{data}
\title{
Sample data set on which rules from the \code{\link{rules}} dataset can be applied
}
\description{
The dataset describes a class of students, their grades and contact details.
}
\usage{data(class)}
\format{
  A data frame with 15 observations on the following 5 variables.
  \describe{
    \item{\code{id}}{a numeric vector}
    \item{\code{name}}{a factor with levels \code{James} \code{Johnny} \code{Joseph}}
    \item{\code{class}}{a factor with levels \code{English} \code{History} \code{Math} \code{Science} \code{Spanish}}
    \item{\code{grade}}{a numeric vector}
    \item{\code{email}}{a factor with levels \code{james@school.edu} \code{johhny@school.edu} \code{johnny@school.edu} \code{joseph@school.edu}}
  }
}
\examples{
data(class)
str(class)
}
\keyword{datasets}