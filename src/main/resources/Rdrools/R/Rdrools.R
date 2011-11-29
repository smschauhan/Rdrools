.onLoad <- function(libname, pkgname) {
	.jpackage(pkgname, lib.loc = libname)
}

rulesSession<-function(rules,input.columns, output.columns)
{
	rules <- paste(rules, collapse="\n")
	input.columns<-paste(input.columns,collapse=",")
	output.columns<-paste(output.columns,collapse=",")
	droolsSession<-.jnew('org/math/r/drools/DroolsService',rules,input.columns, output.columns)
	return(droolsSession)
}

runRules<-function(rules.session,input.df)
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
