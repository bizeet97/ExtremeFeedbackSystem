pipeline{
    agent any
    tools {
        
        maven 'mymaven' 
    }
    stages{
      stage ('build and test'){
            steps{
                
                    sh "mvn clean install -DskipTests"
                
            }
        }
      
        
       stage('Code Quality')
        {
             environment {
                scannerHome=tool 'sonar scanner'
            }
             steps{
                withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId:'Hemant_Sonar_Cred', usernameVariable: 'USER', passwordVariable: 'PASS']])
                 {
                    //  sh "mvn $USER:$PASS -Dsonar.host.url=http://18.224.155.110:9000"
                    echo "code"
                 }
             }
         }
        stage ('Uploading artifact to nexus'){
            steps{
 withCredentials([usernamePassword(credentialsId: 'sudipa_nexus', passwordVariable: 'pass', usernameVariable: 'usr')]) {
sh label: '', script: "curl -u $usr:$pass --upload-file target/xfs-0.0.1-SNAPSHOT.war http://18.224.155.110:8081/nexus/content/repositories/devopstraining/samyy/xfs-0.0.1-SNAPSHOT.war"
}
            
        }
        }
         stage ('Deploy'){
            steps{
              withCredentials([usernamePassword(credentialsId: 'XFS_Deployment', passwordVariable: 'pass', usernameVariable: 'userId')]) {
                    sh "cd target;ls"
                    sh label: '', script:'curl -u $userId:$pass  http://ec2-52-66-245-186.ap-south-1.compute.amazonaws.com:8080/manager/text/undeploy?path=/XFS-deploy'
                    sh label: '', script: 'curl -u  $userId:$pass --upload-file target/xfs-0.0.1-SNAPSHOT.war http://ec2-52-66-245-186.ap-south-1.compute.amazonaws.com:8080/manager/text/deploy?config=file:/var/lib/tomcat8/xfs-0.0.1-SNAPSHOT.war\\&path=/XFS-deploy'
            }
        }

    }
}

 post{
   success {
          script{
              d='{"teamName":"KSR","jobtitle":"'+JOB_NAME+'","bNumber":"'+BUILD_NUMBER+'","bUrl":"'+BUILD_URL+'",buildStatus":"SUCCESS"}'
              sh "curl -H 'Content-Type: application/json' -X POST -d '${d}'  http://ec2-52-66-245-186.ap-south-1.compute.amazonaws.com:8080/ExtremeFeedbackSystem/api/addbuildinfo"
          }
   }
         failure {
            script{
              d='{"teamName":"KSR","jobtitle":"'+JOB_NAME+'","bNumber":"'+BUILD_NUMBER+'","bUrl":"'+BUILD_URL+'",buildStatus":"FAILURE"}'
        
            sh "curl -H 'Content-Type: application/json' -X POST -d '${d}'  http://ec2-52-66-245-186.ap-south-1.compute.amazonaws.com:8080/ExtremeFeedbackSystem/api/addbuildinfo"
         }

}
}
}

