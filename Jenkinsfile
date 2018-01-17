node {
	stage("setup") {
		checkout https://github.com/szheng60/toolRental.git
	}
	stage("build") {
		bat "mvn -B validate deploy"
	}
}