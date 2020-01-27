FROM ubuntu:18.04

RUN apt update -y && apt upgrade -y \
    && apt install apt-utils -y \
    && apt install wget -y \
    && apt install git -y \
    && apt install nano -y \
    && apt install inetutils-ping \
    && apt install jq -y

# Install JAVA
RUN wget https://repos.azul.com/azure-only/zulu/packages/zulu-8/8u232/zulu-8-azure-jdk_8.42.0.23-8.0.232-linux_amd64.deb -O zulu.deb \
    && apt install ./zulu.deb -y \
    && rm zulu.deb \
    && rm -fR /usr/lib/jvm/zulu-8-amd64/sample \
    && rm -fR /usr/lib/jvm/zulu-8-amd64/man \
    && rm -fR /usr/lib/jvm/zulu-8-amd64/demo 

# Install Maven
RUN cd /opt \
    && wget https://www-us.apache.org/dist/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz -O maven.tar.gz \
    && tar xzf maven.tar.gz \
    && rm maven.tar.gz \
    && mkdir /m2 \
    && cd /usr/local/bin \
    && ln -s $(ls /opt/*maven*/bin/mvn)

# Install Azure CLI
RUN apt-get install ca-certificates curl apt-transport-https lsb-release gnupg -y  \
    && curl -sL https://packages.microsoft.com/keys/microsoft.asc | \
        gpg --dearmor | \
        tee /etc/apt/trusted.gpg.d/microsoft.asc.gpg > /dev/null \
    && AZ_REPO=$(lsb_release -cs) \
    && echo "deb [arch=amd64] https://packages.microsoft.com/repos/azure-cli/ $AZ_REPO main" | \
        tee /etc/apt/sources.list.d/azure-cli.list \
    && apt-get update -y \
    && apt-get install azure-cli -y \
    && apt autoremove -y

# Install MySQL CLI
RUN apt-get install mysql-client -y

ENV JAVA_HOME /usr/lib/jvm/zulu-8-azure-amd64
ENV M2_HOME /m2

ENTRYPOINT [ "bash", "-c", "tail -f /dev/null" ]
