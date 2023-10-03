//job.groovy
/* job('awesome-job') {
    description("favorite job of ${SUPERHERO}")
    steps {
        groovyScriptFile('/jobs/scan/nmap.groovy') {
            groovyInstallation('groovy-2.4.2')
            scriptParam('target/reports')
        }
    }
} */
/* pipelineJob('test') {
    definition {
        cpsScm {
            scm {
                git('https://github.com/raphaelsander/jenkins-test.git')
            }
            scriptPath('nmap.groovy')
        }
    }
} */
pipelineJob('nmap') {
    parameters {
        stringParam("TARGET", defaultValue = "target.com", description = "IP, host, range, etc.")
        stringParam("PORTS", defaultValue = "-", description = "Porta, range, etc.")
    }
    definition {
        cps {
            script('''
                pipeline {
                    agent {
                        label 'kali'
                    }
                    stages {
                        stage('Run Nmap') {
                            steps {
                                script {
                                    sh 'nmap -Pn -p ${PORTS} ${TARGET}'
                                }
                            }
                        }
                    }
                }
            '''.stripIndent())
            sandbox()
        }
    }
}
pipelineJob('domain') {
    definition {
        cps {
            script('''
                pipeline {
                    agent {
                        label 'kali'
                    }
                    stages {
                        stage('subfinder') {
                            steps {
                                script {
                                    sh 'echo ${TARGET} | subfinder -s'
                                }
                            }
                        }
                    }
                }
            '''.stripIndent())
            sandbox()
        }
    }
}
pipelineJob('dirb') {
    parameters {
        stringParam("TARGET", defaultValue = "http://target.com", description = "URL.")
        stringParam("WORDLIST", defaultValue = "/usr/share/dirb/wordlists/big.txt", description = "Wordlist path.")
        stringParam("EXT", defaultValue = "ini,txt,sh,bat,env,config", description = "Extensões que serão consultadas.")
    }
    definition {
        cps {
            script('''
                pipeline {
                    agent {
                        label 'kali'
                    }
                    stages {
                        stage('dirb') {
                            steps {
                                script {
                                    sh 'dirb "${TARGET}" "${WORDLIST}" -w -S -X "${EXT}"'
                                }
                            }
                        }
                    }
                }
            '''.stripIndent())
            sandbox()
        }
    }
}