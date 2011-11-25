.onLoad <- function(libname, pkgname) {
	.jpackage(pkgname, lib.loc = libname)
}

rulesConnection<-function()
{
	drools<-.jnew('org/math/r/drools/DroolsService')
	return(drools)
}

runRules<-function(rules.connection,input.df,rules,input.columns, output.columns)
{
	conn<-textConnection('input.csv.string','w')
	write.csv(input.df,file=conn)
	close(conn)
	input.csv.string<-paste(input.csv.string, collapse="\n")
	rules <- paste(rules, collapse="\n")
	input.columns<-paste(input.columns,collapse=",")
	output.columns<-paste(output.columns,collapse=",")
	output.csv.string <- .jcall(rules.connection, "S", "execute",input.csv.string, rules, input.columns, output.columns)
	conn <- textConnection(output.csv.string, "r")
	output.df<-read.csv(file=conn, header=T)
	close(conn)
	return(output.df)
}
