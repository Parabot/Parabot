mvn install:install-file -DgroupId=${project.groupId} -DartifactId=${project.artifactId} -Dversion=${project.version} -Dpackaging=jar -Dfile=../target/${project.build.finalName}.jar -DlocalRepositoryPath=../../Maven-Repository

:: mvn install:install-file -DgroupId=org.parabot -DartifactId=client -Dversion=2.2.3 -Dpackaging=jar -Dfile=../target/Parabot-V2.2.3.jar -DlocalRepositoryPath=../../Maven-Repository
:: mvn install:install-file -DgroupId=org.parabot -DartifactId=client -Dversion=2.2.32 -Dpackaging=jar -Dfile=../target/Parabot-V2.2.32.jar -DlocalRepositoryPath=../../Maven-Repository
:: mvn install:install-file -DgroupId=org.parabot -DartifactId=client -Dversion=2.2.33 -Dpackaging=jar -Dfile=../target/Parabot-V2.2.33.jar -DlocalRepositoryPath=../../Maven-Repository
:: mvn install:install-file -DgroupId=org.parabot -DartifactId=client -Dversion=2.2.34 -Dpackaging=jar -Dfile=../target/Parabot-V2.2.34.jar -DlocalRepositoryPath=../../Maven-Repository
:: mvn install:install-file -DgroupId=org.parabot -DartifactId=client -Dversion=2.2.35 -Dpackaging=jar -Dfile=../target/Parabot-V2.2.35.jar -DlocalRepositoryPath=../../Maven-Repository