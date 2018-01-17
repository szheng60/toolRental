node {
	stage("setup") {
		checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '0a82e6e2-78c0-407b-a190-52d6f49dd6e8', url: 'https://github.com/szheng60/toolRental.git']]])
	}
	stage("build") {
		bat "mvn clean install"
	}
}